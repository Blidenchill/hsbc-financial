package com.hsbc.financial.infrastructure.repository;

import com.hsbc.financial.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * AccountRepository
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountRepository
 **/
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountId = :accountId")
    Account findByAccountIdForUpdate(@Param("accountId") String accountId);

    Account findByAccountId(String accountId);
}

