package com.hsbc.financial.infrastructure.repository.facade;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import com.hsbc.financial.infrastructure.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * AccountFacadeServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountFacadeServiceImpl
 **/
@Service
@Slf4j
public class AccountFacadeServiceImpl implements AccountFacadeService {


    /**
     * 账户仓库实例，用于操作账户数据。
     */
    private final AccountRepository accountRepository;

    /**
     * 构造方法，初始化AccountFacadeServiceImpl对象。
     *
     * @param accountRepository 账户仓库接口，用于管理账户信息。
     */
    @Autowired
    public AccountFacadeServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * 根据账户ID查找并锁定账户信息
     *
     * @param accountId 账户ID
     * @return 找到的账户信息，若不存在则返回空
     */
    @Override
    public Optional<Account> findByAccountIdForUpdate(String accountId) {
        try {
            return accountRepository.findByAccountIdForUpdate(accountId);
        } catch (CannotAcquireLockException | DeadlockLoserDataAccessException e) {
            log.error("当前更新账户出现死锁异常", e);
            throw new InfrastructureException("当前更新账户出现死锁异常", e); // 抛出异常以触发重试
        } catch (Exception ex) {
            log.error("当前查询账户信息异常", ex);
            throw new InfrastructureException("当前查询账户信息异常", ex);
        }
    }

    /**
     * 保存账户信息。
     *
     * @param account 要保存的账户对象。
     */
    @Override
    public void save(Account account) {
        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            log.error("当前保存账户信息异常", ex);
            throw new InfrastructureException("当前保存账户信息异常");
        }
    }

    /**
     * 根据账户ID查找账户信息。
     *
     * @param accountId 账户ID。
     * @return 对应的账户对象，若不存在则返回空。
     */
    @Override
    public Optional<Account> findByAccountId(String accountId) {
        try {
            return accountRepository.findByAccountId(accountId);
        } catch (Exception ex) {
            log.error("当前查询账户信息异常", ex);
            throw new InfrastructureException("当前查询账户信息异常");
        }
    }
}
