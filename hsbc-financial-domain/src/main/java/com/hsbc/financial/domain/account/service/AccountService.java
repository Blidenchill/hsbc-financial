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
    void updateAccountBalances(TransactionCommand command);
    Account getAccountById(String accountId);
}
