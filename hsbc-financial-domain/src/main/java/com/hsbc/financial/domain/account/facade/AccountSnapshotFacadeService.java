package com.hsbc.financial.domain.account.facade;

import com.hsbc.financial.domain.account.entity.AccountSnapshot;
import com.hsbc.financial.domain.common.exception.InfrastructureException;

import java.util.Optional;

/**
 * AccountSnapshotFacadeService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountSnapshotFacadeService
 **/
public interface AccountSnapshotFacadeService {

    /**
     * 根据账户ID查找最新版本的账户快照。
     * @param accountId 账户ID。
     * @return 最新版本的账户快照，若不存在则返回空。
     * @throws InfrastructureException 当基础设施出现异常时抛出。
     */
    Optional<AccountSnapshot> findTopByAccountIdOrderByVersionDesc(String accountId) throws InfrastructureException;

    /**
     * 保存账户快照。
     * @param accountSnapshot 要保存的账户快照对象。
     */
    void save(AccountSnapshot accountSnapshot);
}
