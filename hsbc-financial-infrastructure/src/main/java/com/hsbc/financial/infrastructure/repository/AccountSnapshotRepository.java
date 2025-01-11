package com.hsbc.financial.infrastructure.repository;

import com.hsbc.financial.domain.account.entity.AccountSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AccountSnapshotRepository
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountSnapshotRepository
 **/
@Repository
public interface AccountSnapshotRepository extends JpaRepository<AccountSnapshot, Long> {

    // 基于accountId查询账户快照
    AccountSnapshot findByAccountId(String accountId);
}

