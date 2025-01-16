package com.hsbc.financial.domain.account.service.impl;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.entity.AccountSnapshot;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.domain.account.facade.AccountSnapshotFacadeService;
import com.hsbc.financial.domain.account.service.AccountService;
import com.hsbc.financial.domain.common.aop.annotation.MethodLog;
import com.hsbc.financial.domain.common.cache.CacheService;
import com.hsbc.financial.domain.common.exception.AccountNotFoundException;
import com.hsbc.financial.domain.common.exception.BusinessException;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import com.hsbc.financial.domain.common.exception.InsufficientBalanceException;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * AccountServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountServiceImpl
 **/
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    /**
     * 账户服务的Facade接口，提供账户相关的业务逻辑操作。
     */
    private final AccountFacadeService accountFacadeService;

    /**
     * 账户快照服务的Facade接口，提供账户快照相关的业务逻辑操作。
     */
    private final AccountSnapshotFacadeService accountSnapshotFacadeService;

    /**
     * 负责管理和操作缓存数据的服务。
     */
    private final CacheService cacheService;

    /**
     * 缓存键的前缀，用于区分不同类型的缓存数据。
     */
    private final static String RANGE = "account";

    /**
     * 缓存数据的过期时间，单位为天。
     */
    private final static long EXPIRATION_DAY = 1L;


    /**
     * 构造函数，初始化AccountServiceImpl的依赖服务。
     *
     * @param accountFacadeService         账户操作接口服务
     * @param accountSnapshotFacadeService 账户快照操作接口服务
     * @param cacheService                 缓存服务
     */
    @Autowired
    public AccountServiceImpl(AccountFacadeService accountFacadeService,
                              AccountSnapshotFacadeService accountSnapshotFacadeService,
                              CacheService cacheService) {
        this.accountFacadeService = accountFacadeService;
        this.accountSnapshotFacadeService = accountSnapshotFacadeService;
        this.cacheService = cacheService;
    }

    /**
     * 更新账户余额并创建或更新快照。
     *
     * @param command TransactionCommand 对象，包含源账户ID、目标账户ID和转账金额。
     * @throws AccountNotFoundException     如果源账户ID或目标账户ID不存在。
     * @throws InsufficientBalanceException 如果源账户余额不足。
     * @throws BusinessException              如果在更新账户时出现数据库异常或其他未知异常。
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(
            value = {BusinessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Override
    @MethodLog
    public void updateAccountBalances(TransactionCommand command) {
        try {
            // 1. 获取源账户和目标账户的ID，并排序, 为了方式发生死锁
            String firstAccountId = command.getSourceAccountId().compareTo(command.getDestAccountId()) < 0 ?
                    command.getSourceAccountId() : command.getDestAccountId();
            String secondAccountId = command.getSourceAccountId().compareTo(command.getDestAccountId()) > 0 ?
                    command.getSourceAccountId() : command.getDestAccountId();
            // 2. 按照ID顺序获取并锁定账户
            Account firstAccount = accountFacadeService.findByAccountIdForUpdate(firstAccountId)
                    .orElseThrow(() -> new AccountNotFoundException("账户ID不存在"));
            Account secondAccount = accountFacadeService.findByAccountIdForUpdate(secondAccountId)
                    .orElseThrow(() -> new AccountNotFoundException("账户ID不存在"));
            // 3. 根据实际情况判断 source 和 destination
            Account sourceAccount, destAccount;
            if (firstAccount.getAccountId().equals(command.getSourceAccountId())) {
                sourceAccount = firstAccount;
                destAccount = secondAccount;
            } else {
                sourceAccount = secondAccount;
                destAccount = firstAccount;
            }
            // 4. 检查余额是否充足
            if (sourceAccount.getBalance().compareTo(command.getAmount()) < 0) {
                throw new InsufficientBalanceException("余额不足");
            }
            // 5. 更新余额
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(command.getAmount()));
            destAccount.setBalance(destAccount.getBalance().add(command.getAmount()));
            // 6. 更新更新时间
            sourceAccount.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            destAccount.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            // 7. 保存账户
            accountFacadeService.save(sourceAccount);
            accountFacadeService.save(destAccount);
            // 8. 更新缓存
            cacheService.add(cacheService.generateKey(RANGE, sourceAccount.getAccountId()), sourceAccount, EXPIRATION_DAY, TimeUnit.DAYS);
            cacheService.add(cacheService.generateKey(RANGE, destAccount.getAccountId()), destAccount, EXPIRATION_DAY, TimeUnit.DAYS);
            // 9.创建或更新快照
            createOrUpdateSnapshot(sourceAccount);
            createOrUpdateSnapshot(destAccount);
        } catch (InfrastructureException e) {
            log.error("基础设施层数据库操作异常", e);
            throw new BusinessException("基础设施层数据库操作异常"); // 抛出异常以触发重试
        } catch (InsufficientBalanceException e) {
            log.error("当前原账户余额不足", e);
            throw e;
        } catch (AccountNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("当前业务出现其他异常", e);
            throw new BusinessException("当前业务出现其他异常");
        }
    }

    /**
     * 根据账户ID获取账户信息。
     *
     * @param accountId 账户ID
     * @return 对应的账户对象
     */
    @Override
    @MethodLog
    public Account getAccountById(String accountId) {
        // 先从缓存中获取
        Account account = cacheService.get(cacheService.generateKey(RANGE, accountId));
        if (account == null) {
            // 缓存中没有，从数据库获取并缓存
            account = accountFacadeService.findByAccountId(accountId)
                    .orElseThrow(() -> new AccountNotFoundException("账户不存在"));
            cacheService.add(cacheService.generateKey(RANGE, accountId), account, EXPIRATION_DAY, TimeUnit.DAYS);
        }
        return account;
    }

    /**
     * 创建或更新账户快照。
     *
     * @param account 账户对象，包含账户信息。
     */
    @Override
    @MethodLog
    public void createOrUpdateSnapshot(Account account) {
        try {
            // 获取当前最新版本号
            long version = 1;
            Optional<AccountSnapshot> latestSnapshotOpt = accountSnapshotFacadeService
                    .findTopByAccountIdOrderByVersionDesc(account.getAccountId());
            if (latestSnapshotOpt.isPresent()) {
                version = latestSnapshotOpt.get().getVersion() + 1;
            }

            // 创建新的快照
            AccountSnapshot snapshot = new AccountSnapshot();
            snapshot.setSnapshotId(UUID.randomUUID().toString());
            snapshot.setAccountId(account.getAccountId());
            snapshot.setBalance(account.getBalance());
            snapshot.setVersion(version);
            snapshot.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            // 保存快照
            accountSnapshotFacadeService.save(snapshot);
        } catch (InfrastructureException ex) {
            log.error(ex.getMessage(), ex);
            throw new BusinessException("创建快照时,数据库操作异常");
        }

    }
}

