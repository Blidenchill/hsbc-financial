package com.hsbc.financial.domain.common.exception;

/**
 * SystemException
 *
 * @author zhaoyongping
 * @date 2025/1/12
 * @className SystemException
 **/
public class SystemException extends BaseException {

    /**
     * 构造一个带有指定消息和原因的SystemException对象。
     * @param message 异常的详细消息。
     * @param e 异常的原因。
     */
    public SystemException(String message, Throwable e) {
        super(message, e);
    }
}
