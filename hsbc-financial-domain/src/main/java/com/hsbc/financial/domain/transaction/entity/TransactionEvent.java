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
    /**
     * 主键ID，自增生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 事件的唯一标识符，不能为空
     */
    @Column(unique = true, nullable = false)
    private String eventId;

    /**
     * 交易ID，关联到具体的交易记录。
     */
    private String transactionId;

    /**
     * 来源账户的ID，用于记录交易的来源账户。
     */
    private String sourceAccountId;

    /**
     * 目标账户的ID，用于记录交易的目标账户。
     */
    private String destAccountId;

    /**
     * 交易金额，表示交易的具体数值。
     */
    private BigDecimal amount;

    /**
     * 事件类型，表示交易事件的具体类型。
     */
    private String eventType;

    /**
     * 事件的详细数据，可能包含大量文本或二进制数据。
     */
    @Lob
    private String eventData;

    /**
     * 创建时间戳，记录该交易事件的创建时间。
     */
    private Timestamp createdAt;

    /**
     * 标记该交易事件是否已被删除。
     */
    private Boolean isDeleted;
}
