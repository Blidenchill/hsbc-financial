package com.hsbc.financial.domain.transaction.command;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * TransactionCommand
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionCommand
 **/
@Data
public class TransactionCommand {
    /**
     * 交易ID，用于唯一标识每一笔交易。
     */
    private String transactionId;

    /**
     * 源账户ID，表示交易的发起方账户。
     */
    private String sourceAccountId;

    /**
     * 目标账户ID，表示交易的接收方账户。
     */
    private String destAccountId;

    /**
     * 交易金额，表示本次交易的金额大小。
     */
    private BigDecimal amount;

    /**
     * 记录交易发生的时间戳。
     */
    private Timestamp timestamp;
}

