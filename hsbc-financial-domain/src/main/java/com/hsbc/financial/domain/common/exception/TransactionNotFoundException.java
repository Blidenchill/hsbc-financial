package com.hsbc.financial.domain.common.exception;

/**
 * TransactionNotFoundException
 *
 * @author zhaoyongping
 * @date 2025/1/16
 * @className TransactionNotFoundException
 **/
public class TransactionNotFoundException extends BaseException {
    /**
     * 构造一个TransactionNotFoundException异常对象。
     * @param message 异常信息。
     */
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
