package com.hsbc.financial.domain.common.aop.aspect;

import com.hsbc.financial.domain.common.utils.JacksonUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * MethodLogAspect
 *
 * @author zhaoyongping
 * @date 2023/11/8 15:26
 * @className MethodLogAspect
 **/
@Log4j2
@Aspect
@Component
public class MethodLogAspect {
    /**
     * 在方法调用前记录日志信息。
     * @param joinPoint 切入点对象，用于获取被调用的方法信息和参数。
     */
    @Before("@annotation(com.hsbc.financial.domain.common.aop.annotation.MethodLog)")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String packageName = joinPoint.getSignature().getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        StringBuilder builder = new StringBuilder("Entering method:");
        builder.append(packageName);
        builder.append(".");
        builder.append(methodName);
        builder.append(";");
        builder.append("入参：");
        for (Object item : args) {
            builder.append(String.valueOf(item));
            builder.append(",");
        }
        log.info(builder.toString());

    }

    @AfterReturning(pointcut = "@annotation(com.hsbc.financial.domain.common.aop.annotation.MethodLog)", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String packageName = joinPoint.getSignature().getDeclaringTypeName();
        StringBuilder builder = new StringBuilder("Exiting method:");
        builder.append(packageName);
        builder.append(".");
        builder.append(methodName);
        builder.append(";");
        builder.append("结果：");
        builder.append(JacksonUtil.toJson(result));
        log.info(builder.toString());
    }
}
