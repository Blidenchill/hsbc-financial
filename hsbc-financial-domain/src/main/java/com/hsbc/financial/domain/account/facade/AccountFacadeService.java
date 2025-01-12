package com.hsbc.financial.domain.account.facade;

import com.hsbc.financial.domain.account.entity.Account;

import java.util.Optional;

/**
 * AccountFacadeService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountFacadeService
 **/
public interface AccountFacadeService {
    /**
     * 根据账户ID查找并锁定账户，用于更新操作。
     * @param accountId 账户ID
     * @return 找到的账户实例，若不存在则返回空
     */
    Optional<Account> findByAccountIdForUpdate(String accountId);

    /**
     * 保存账户信息
     * @param account 要保存的账户对象
     */
    void save(Account account);

    /**
     * 根据账户ID查找账户信息。
     * @param accountId 账户ID。
     * @return 对应的账户信息，若不存在则返回空。
     */
    Optional<Account> findByAccountId(String accountId);
}
