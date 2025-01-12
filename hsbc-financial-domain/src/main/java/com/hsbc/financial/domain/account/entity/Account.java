package com.hsbc.financial.domain.account.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
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
public class Account implements Serializable {

    /**
     * 主键ID，自增生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账户ID，必须唯一且不可为空
     */
    @Column(unique = true, nullable = false)
    private String accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账户当前余额
     */
    private BigDecimal balance;

    /**
     * 创建时间戳，记录账户创建的时间点。
     */
    private Timestamp createdAt;

    /**
     * 最后更新时间戳，记录账户信息最后一次被修改的时间点。
     */
    private Timestamp updatedAt;

    /**
     * 标记账户是否已被删除。
     */
    private Boolean isDeleted;

}
