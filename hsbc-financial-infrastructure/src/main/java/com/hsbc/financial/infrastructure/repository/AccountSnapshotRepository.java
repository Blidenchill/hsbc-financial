package com.hsbc.financial.infrastructure.repository;

import com.hsbc.financial.domain.account.entity.AccountSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AccountSnapshotRepository
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountSnapshotRepository
 **/
@Repository
public interface AccountSnapshotRepository extends JpaRepository<AccountSnapshot, Long> {

    /**
     * 根据账户ID查询账户快照。
     * @param accountId 账户ID。
     * @return 对应账户的快照信息。
     */
    AccountSnapshot findByAccountId(String accountId);

    /**
     * 根据账户ID查找最新版本的账户快照。
     * @param accountId 账户ID
     * @return 最新版本的账户快照，若不存在则返回空
     */
    Optional<AccountSnapshot> findTopByAccountIdOrderByVersionDesc(String accountId);

}

