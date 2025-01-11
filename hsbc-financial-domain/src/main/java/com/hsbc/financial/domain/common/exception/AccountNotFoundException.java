package com.hsbc.financial.domain.common.exception;

/**
 * AccountNotFoundException
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountNotFoundException
 **/
public class AccountNotFoundException extends  BaseException{
    public AccountNotFoundException(String errMessage) {
        super(errMessage);
    }
}
