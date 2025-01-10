package com.hsbc.financial.domain.common.enums;

/**
 * ErrorCodeEnum
 *
 * @author zhaoyongping
 * @date 2023/11/8 17:25
 * @className ErrorCodeEnum
 **/
public enum ErrorCodeEnum implements ErrorSpec{
    /**
     * 系统未知异常
     */
    INTERNAL_ERROR("10000", "系统内部错误"),

    /**
     * 参数错误错误
     **/
    PARAM_ERROR("20000", "参数错误"),

    /**
     * 未识别异常
     */
    UNKNOWN_EXCEPTION("39999", "未识别异常"),

    /**
     * RPC错误
     **/
    RPC_ERROR("30000", "RPC错误"),
    ;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码描述，长度不能超过128
     */
    private String desc;

    /**
     * 构造函数
     */
    ErrorCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 错误码
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 错误码描述
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
