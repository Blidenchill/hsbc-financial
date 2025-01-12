package com.hsbc.financial.infrastructure.repository;

import com.hsbc.financial.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * AccountRepository
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountRepository
 **/
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * 根据账户ID获取账户信息，并使用悲观写锁进行更新操作。
     * @param accountId 账户ID
     * @return 对应的账户信息，若不存在则返回空
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountId = :accountId")
    Optional<Account> findByAccountIdForUpdate(@Param("accountId") String accountId);

    /**
     * 根据账户ID查找账户信息。
     * @param accountId 账户ID。
     * @return 对应的账户对象，若不存在则返回空。
     */
    Optional<Account> findByAccountId(String accountId);
}

