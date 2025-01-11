package com.hsbc.financial.domain.transaction.service;

import com.hsbc.financial.domain.transaction.command.TransactionCommand;

/**
 * TransactionService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionService
 **/
public interface TransactionService {
    void processTransaction(TransactionCommand command);
}
