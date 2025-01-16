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

    /**
     * 更新交易的处理状态
     * @param transactionId 交易ID
     */
    void changeTransactionProcessed(String transactionId);

    /**
     * 将指定交易的状态改为失败，并记录失败原因。
     * @param transactionId 交易ID
     * @param failReason 失败原因
     */
    void changeTransactionFailed(String transactionId, String failReason);

    /**
     * 检查指定交易是否已经处理完成。
     * @param transactionId 交易ID
     * @return true 如果交易已经处理完成，false 否则
     */
    Boolean checkTransactionProcessed(String transactionId);
}
