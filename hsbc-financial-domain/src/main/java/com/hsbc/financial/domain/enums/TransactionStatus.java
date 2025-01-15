package com.hsbc.financial.domain.enums;

import lombok.Getter;

/**
 * TransactionStatus
 *
 * @author zhaoyongping
 * @date 2025/1/13
 * @className TransactionStatus
 **/
@Getter
public enum TransactionStatus {
    /**
     * 提交完成的交易状态。
     */
    SUBMITTED("提交完成"),
    /**
     * 表示事务已经被成功处理的状态。
     */
    PROCESSED("处理完成"),
    /**
     * 处理失败的交易状态。
     */
    FAILED("处理失败");

    /**
     * 事务状态的描述信息。
     */
    private final String description;

    /**
     * 创建一个新的 TransactionStatus 对象，用于描述交易状态。
     * @param description 描述交易状态的字符串。
     */
    TransactionStatus(String description) {
        this.description = description;
    }

}
