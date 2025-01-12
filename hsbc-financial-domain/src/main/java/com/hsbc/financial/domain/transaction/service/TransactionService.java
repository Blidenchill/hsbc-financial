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
    /**
     * 处理交易命令
     * @param command 交易命令对象
     */
    void processTransaction(TransactionCommand command);
}
