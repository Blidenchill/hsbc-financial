package com.hsbc.financial.domain.account.service;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;

/**
 * AccountService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountService
 **/
public interface AccountService {
    /**
     * 更新账户余额。
     * @param command 交易命令对象，包含更新账户余额所需的信息。
     */
    void updateAccountBalances(TransactionCommand command);

    /**
     * 根据账户ID获取对应的账户信息。
     * @param accountId 账户的唯一标识符。
     * @return 对应账户ID的账户对象。
     */
    Account getAccountById(String accountId);

    /**
     * 创建或更新账户快照。
     * @param account 账户对象，包含需要创建或更新快照的信息。
     */
    void createOrUpdateSnapshot(Account account);
}
