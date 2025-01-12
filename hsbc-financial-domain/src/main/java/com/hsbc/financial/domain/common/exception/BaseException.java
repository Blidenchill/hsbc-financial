package com.hsbc.financial.domain.common.exception;

/**
 * BaseException
 *
 * @author zhaoyongping
 * @date 2023/11/8 17:23
 * @className BaseException
 **/
public abstract class BaseException extends RuntimeException {

    /**
     * 序列化版本号，用于确保序列化和反序列化的兼容性。
     */
    private static final long serialVersionUID = 1L;

    /**
     * 错误码，用于标识具体的错误类型。
     */
    private String errCode;

    /**
     * 构造一个BaseException对象，传入错误消息。
     * @param errMessage 错误消息。
     */
    public BaseException(String errMessage) {
        super(errMessage);
    }

    /**
     * 构造一个带有错误码和错误消息的 BaseException 对象。
     * @param errCode 错误码。
     * @param errMessage 错误消息。
     */
    public BaseException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    /**
     * 构造一个带有错误消息和异常的 BaseException 对象。
     * @param errMessage 错误消息。
     * @param e 异常对象。
     */
    public BaseException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    /**
     * 构造一个带有错误码和错误消息的 BaseException 对象。
     * @param errCode 错误码
     * @param errMessage 错误消息
     * @param e 可能的原因异常
     */
    public BaseException(String errCode, String errMessage, Throwable e) {
        super(errMessage, e);
        this.errCode = errCode;
    }

    /**
     * 获取错误代码
     * @return 错误代码
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * 设置错误代码
     * @param errCode 错误代码
     */
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

}