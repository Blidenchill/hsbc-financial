package com.hsbc.financial.domain.transaction.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * TransactionEvent
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionEvent
 **/
@Entity
@Table(name = "transaction_events")
@Data
public class TransactionEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String eventId;

    private String transactionId;

    private String sourceAccountId;

    private String destAccountId;

    private BigDecimal amount;

    private String eventType;

    @Lob
    private String eventData;

    private Timestamp createdAt;

    private Boolean isDeleted;
}
