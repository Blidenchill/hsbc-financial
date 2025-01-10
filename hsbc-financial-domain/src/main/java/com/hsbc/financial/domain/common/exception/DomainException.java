package com.hsbc.financial.domain.common.exception;

import com.hsbc.financial.domain.common.enums.ErrorSpec;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DomainException
 *
 * @author zhaoyongping
 * @date 2023/11/8 17:27
 * @className DomainException
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class DomainException extends RuntimeException {

    /**
     * 返回外部接口调用返回状态码
     */
    public static final String SUB_CODE = "subCode";
    /**
     * 返回外部接口调用返回描述
     */
    public static final String SUB_MESSAGE = "subMessage";

    /**
     * 返回具体参数校验失败字段
     */
    public static final String VALIDATE_FAIL_FIELD = "validateFailField";
    /**
     * 异常优先级
     */
    protected int priority;
    /**
     * 异常码统一定义返回
     */
    protected ErrorSpec dooErrorSpec;

    protected String[] vars;

    /**
     * 异常信息扩展字段
     */
    private Map<String, String> ext = new HashMap<>();

    /**
     * (业务前台)个性化消息.
     * <p>
     * <p>例如，业务前台要求它抛出的错误消息，中台不要再加工，要原封不动地输出</p>
     */
    protected String custom;

    /**
     * 构造函数
     */
    public DomainException() {

    }

    /**
     * 构造函数
     * @param errorReason 异常原因
     */
    public DomainException(ErrorSpec errorReason) {
        super();
        this.dooErrorSpec = errorReason;
    }

    /**
     * 构造函数
     * @param errorReason 原因
     * @param exception root cause
     */
    public DomainException(ErrorSpec errorReason, Exception exception) {
        super(exception);
        this.dooErrorSpec = errorReason;
    }

    /**
     * 异常原因
     * @param message 信息
     */
    public DomainException(ErrorSpec errorReason, String message) {
        super(message);
        this.dooErrorSpec = errorReason;
    }

    /**
     * 异常原因
     * @param message 信息
     * @param exception 异常
     */
    public DomainException(String message, Exception exception) {
        super(message, exception);
    }

    /**
     * 异常原因
     * @param cause root cause
     */
    public DomainException(Throwable cause) {
        super(cause);
    }


    /**
     * 设置错误消息占位符的变量值.
     * <p>
     * <p>错误消息，通过i18n机制为错误码设定，其中包含占位符的，在抛出异常时需要传递占位符变量的值</p>
     *
     * @param vars 占位符变量的值
     */
    public DomainException withVars(String... vars) {
        this.vars = vars;
        return this;
    }

    /**
     * 设置(业务前台)个性化消息.
     *
     * @param custom 个性化消息
     */
    public DomainException withCustom(String custom) {
        this.custom = custom;
        return this;
    }

    /**
     * 设置sub code
     * @param subCode
     * @return
     */
    public DomainException withSubCode(String subCode) {
        this.ext.put(SUB_CODE, subCode);
        return this;
    }

    public DomainException withSubCode(Integer subCode) {
        if (subCode != null) {
            this.ext.put(SUB_CODE, String.valueOf(subCode));
        }
        return this;
    }

    public DomainException withSubMessage(String subMessage) {
        this.ext.put(SUB_MESSAGE, subMessage);
        return this;
    }

    public DomainException putExt(String extKey, String extValue) {
        if (StringUtils.isBlank(extKey) || StringUtils.isBlank(extValue)) {
            return this;
        }
        this.ext.put(extKey, extValue);
        return this;
    }

    public String getExt(String extKey) {
        return this.ext.get(extKey);
    }

    public String subCode() {
        return this.ext.get(SUB_CODE);
    }

    public String subMessage() {
        return this.ext.get(SUB_MESSAGE);
    }

    /**
     * 是否有个性化信息.
     */
    public boolean hasCustom() {
        return custom != null;
    }

    /**
     * 获取错误码.
     */
    public String code() {
        return dooErrorSpec.getCode();
    }

    @Override
    public String getMessage() {
        if (hasCustom()) {
            return custom;
        }

        if (vars == null || vars.length == 0) {
            return code();
        }

        return code() + Arrays.toString(vars);
    }
}
