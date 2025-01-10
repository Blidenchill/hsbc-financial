package com.hsbc.financial.domain.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring Bean的工厂工具类
 *
 * @author zhaoyongping
 * @date 2023/11/8 17:09
 * @className ApplicationContextUtil
 **/
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    /**
     * applicationContext
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置应用程序上下文
     *
     * @param applicationContext 应用程序上下文对象
     * @throws BeansException 如果发生Beans异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据目标类获取对应的Bean实例
     *
     * @param targetClz 目标类的Class对象
     * @param <T>       目标类的泛型参数
     * @return 返回目标类对应的Bean实例
     * @throws RuntimeException 如果在Spring容器中找不到目标类对应的组件，则抛出运行时异常
     */
    public static <T> T getBean(Class<T> targetClz) {
        T beanInstance = null;
        //优先按type查
        try {
            beanInstance = (T) applicationContext.getBean(targetClz);
        } catch (Exception e) {
            throw new RuntimeException("applicationContext.getBean " + targetClz + " error");
        }
        //按name查
        if (beanInstance == null) {
            String simpleName = targetClz.getSimpleName();
            //首字母小写
            simpleName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            beanInstance = (T) applicationContext.getBean(simpleName);
        }
        if (beanInstance == null) {
            throw new RuntimeException("Component " + targetClz + " can not be found in Spring Container");
        }
        return beanInstance;
    }

    /**
     * 获取指定类的bean对象
     *
     * @param claz 类的全限定名
     * @return 返回指定类的bean对象
     */
    public static Object getBean(String claz) {
        return ApplicationContextUtil.applicationContext.getBean(claz);
    }

    /**
     * 根据给定的bean名称和所需的类型，获取对应的bean对象。
     *
     * @param name         bean的名称
     * @param requiredType 所需的bean类型
     * @param <T>          泛型参数，表示bean的类型
     * @return 返回对应的bean对象
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return ApplicationContextUtil.applicationContext.getBean(name, requiredType);
    }


    /**
     * 获取指定类型的Bean对象
     *
     * @param requiredType 要获取的Bean的类型
     * @param params       传递给Bean构造函数的数组
     **/
    public static <T> T getBean(Class<T> requiredType, Object... params) {
        return ApplicationContextUtil.applicationContext.getBean(requiredType, params);
    }

    /**
     * 获取应用程序的上下文
     *
     * @return 应用程序的上下文对象
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
