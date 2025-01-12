package com.hsbc.financial.domain.common.exception;

/**
 * InfrastructureException
 *
 * @author zhaoyongping
 * @date 2025/1/12
 * @className InfrastructureException
 **/
public class InfrastructureException extends BaseException {
    /**
     * 构造一个带有指定消息的InfrastructureException对象。
     * @param message 异常的详细信息。
     */
    public InfrastructureException(String message) {
        super(message);
    }
    public InfrastructureException(String message, Throwable ex) {
        super(message, ex);
    }
}
