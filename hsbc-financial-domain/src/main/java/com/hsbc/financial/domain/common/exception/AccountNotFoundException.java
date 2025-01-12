package com.hsbc.financial.domain.common.exception;

/**
 * AccountNotFoundException
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountNotFoundException
 **/
public class AccountNotFoundException extends  BaseException{
    /**
     * 构造一个带有错误消息的 AccountNotFoundException 异常对象。
     * @param errMessage 错误消息字符串。
     */
    public AccountNotFoundException(String errMessage) {
        super(errMessage);
    }
}
