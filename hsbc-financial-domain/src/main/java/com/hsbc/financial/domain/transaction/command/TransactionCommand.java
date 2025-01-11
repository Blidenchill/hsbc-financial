package com.hsbc.financial.domain.transaction.command;

import lombok.Data;

import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * TransactionCommand
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionCommand
 **/
@Data
public class TransactionCommand {
    private String transactionId;
    private String sourceAccountId;
    private String destAccountId;
    private BigDecimal amount;
    private Timestamp timestamp;
}

