package com.hsbc.financial.infrastructure.repository.facade;

import com.hsbc.financial.domain.account.entity.AccountSnapshot;
import com.hsbc.financial.domain.account.facade.AccountSnapshotFacadeService;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import com.hsbc.financial.infrastructure.repository.AccountSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * AccountSnapshotFacadeServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountSnapshotFacadeServiceImpl
 **/
@Service
@Slf4j
public class AccountSnapshotFacadeServiceImpl implements AccountSnapshotFacadeService {

    /**
     * 账户快照仓库接口实例，用于执行数据库操作。
     */
    private final AccountSnapshotRepository accountSnapshotRepository;

    /**
     * 构造函数，初始化AccountSnapshotFacadeServiceImpl对象。
     * @param accountSnapshotRepository 账户快照仓库，用于获取和保存账户快照信息。
     */
    @Autowired
    public AccountSnapshotFacadeServiceImpl(AccountSnapshotRepository accountSnapshotRepository) {
        this.accountSnapshotRepository = accountSnapshotRepository;
    }

    /**
     * 根据账号ID查找最新版本的账号快照信息。
     * @param accountId 账号ID。
     * @return 最新版本的账号快照信息，若不存在则返回空。
     * @throws InfrastructureException 当前数据库获取账号快照信息异常。
     */
    @Override
    public Optional<AccountSnapshot> findTopByAccountIdOrderByVersionDesc(String accountId) throws InfrastructureException {
        try{
            return accountSnapshotRepository.findTopByAccountIdOrderByVersionDesc(accountId);
        } catch (Exception ex) {
            log.error("当前数据库获取账号快照信息异常", ex);
            throw new InfrastructureException("当前数据库获取账号快照信息异常");
        }

    }

    /**
     * 保存账号快照信息到数据库。
     * @param accountSnapshot 账号快照信息对象。
     * @throws InfrastructureException 如果保存操作失败。
     */
    @Override
    public void save(AccountSnapshot accountSnapshot) {
        try{
            accountSnapshotRepository.save(accountSnapshot);
        } catch (Exception ex) {
            log.error("当前数据库保存账号快照信息异常", ex);
            throw new InfrastructureException("当前数据库保存账号快照信息异常");
        }
    }

}
