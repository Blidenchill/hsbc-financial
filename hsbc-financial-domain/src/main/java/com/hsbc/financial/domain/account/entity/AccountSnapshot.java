package com.hsbc.financial.domain.account.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.security.Timestamp;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String snapshotId;

    private String accountId;

    private BigDecimal balance;

    private Long version;

    private Timestamp createdAt;

    private Boolean isDeleted;

}

