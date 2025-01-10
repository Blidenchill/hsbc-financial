package com.hsbc.financial.domain.common.aop.annotation;

import java.lang.annotation.*;

/**
 * MethodLog
 *
 * @author zhaoyongping
 * @date 2023/11/8 15:25
 * @className MethodLog
 **/
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {
}
