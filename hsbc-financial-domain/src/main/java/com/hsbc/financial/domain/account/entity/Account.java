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
 * AccountEntity
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountEntity
 **/
@Entity
@Table(name = "accounts")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountId;

    private String accountName;

    private BigDecimal balance;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Boolean isDeleted;

}
