package com.hsbc.financial.domain.common.enums;

/**
 * ErrorSpec
 *
 * @author zhaoyongping
 * @date 2023/11/8 17:25
 * @className ErrorSpec
 **/
public interface ErrorSpec {
    /**
     * 异常错误状态码
     * @return
     */
    String getCode();

    /**
     * 异常错误描述
     * @return
     */
    String getDesc();
}
