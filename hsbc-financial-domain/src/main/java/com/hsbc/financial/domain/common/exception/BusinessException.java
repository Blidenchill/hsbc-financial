package com.hsbc.financial.domain.common.exception;

/**
 * BusinessException
 *
 * @author zhaoyongping
 * @date 2025/1/12
 * @className BusinessException
 **/
public class BusinessException extends BaseException {
    /**
     * 构造一个新的业务异常对象，带有指定的错误消息。
     * @param message 错误消息。
     */
    public BusinessException(String message) {
        super(message);
    }
}
