package com.hsbc.financial.domain.common.exception;

/**
 * InsufficientBalanceException
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className InsufficientBalanceException
 **/
public class InsufficientBalanceException extends BaseException {

    /**
     * 构造一个新的 InsufficientBalanceException 实例，带有指定的错误消息。
     * @param message 错误消息。
     */
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
