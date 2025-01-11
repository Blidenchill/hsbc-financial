package com.hsbc.financial.domain.account.service.impl;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.domain.account.facade.AccountSnapshotFacadeService;
import com.hsbc.financial.domain.account.service.AccountService;
import com.hsbc.financial.domain.common.exception.InsufficientBalanceException;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.sql.Timestamp;

/**
 * AccountServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountServiceImpl
 **/
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountFacadeService accountFacadeService;
    private final AccountSnapshotFacadeService accountSnapshotFacadeService;

    @Autowired
    public AccountServiceImpl(AccountFacadeService accountFacadeService,
                              AccountSnapshotFacadeService accountSnapshotFacadeService) {
        this.accountFacadeService = accountFacadeService;
        this.accountSnapshotFacadeService = accountSnapshotFacadeService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(
            value = { CannotAcquireLockException.class, DeadlockLoserDataAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Override
    public void updateAccountBalances(TransactionCommand command) {
        try {
            // 获取源账户和目标账户，加锁防止并发更新
            Account sourceAccount = accountFacadeService.findByIdForUpdate(command.getSourceAccountId());
            Account destAccount = accountFacadeService.findByIdForUpdate(command.getDestAccountId());

            // 检查余额是否充足
            if (sourceAccount.getBalance().compareTo(command.getAmount()) < 0) {
                throw new InsufficientBalanceException("余额不足");
            }

            // 更新余额
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(command.getAmount()));
            destAccount.setBalance(destAccount.getBalance().add(command.getAmount()));

            // 更新更新时间
            sourceAccount.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            destAccount.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            // 保存账户
            accountFacadeService.save(sourceAccount);
            accountFacadeService.save(destAccount);
            // 更新缓存
            // 创建或更新快照
        } catch (CannotAcquireLockException | DeadlockLoserDataAccessException e) {
            // 可选：记录日志
            throw e; // 抛出异常以触发重试
        } catch (InsufficientBalanceException e) {
            // 业务异常，不重试，直接抛出
            throw e;
        } catch (Exception e) {
            // 其他异常，可视情况处理
            throw e;
        }
    }

    @Override
    public Account getAccountById(String accountId) {
        return null;
    }

    // 省略其他方法...
}

