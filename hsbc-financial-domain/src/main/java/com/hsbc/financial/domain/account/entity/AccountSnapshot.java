package com.hsbc.financial.domain.account.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * AccountSnapshot
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountSnapshot
 **/
@Entity
@Table(name = "account_snapshots")
@Data
public class AccountSnapshot {
    /**
     * 主键ID，自增生成。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 唯一标识符，用于区分不同的账户快照。
     */
    @Column(unique = true, nullable = false)
    private String snapshotId;

    /**
     * 账户ID，关联到具体的账户实体。
     */
    private String accountId;

    /**
     * 当前账户的余额。
     */
    private BigDecimal balance;

    /**
     * 版本号，用于乐观锁机制，防止并发更新。
     */
    private Long version;

    /**
     * 创建时间戳，记录账户快照的创建时间。
     */
    private Timestamp createdAt;

    /**
     * 标记该账户快照是否已被删除。
     */
    private Boolean isDeleted;

}

