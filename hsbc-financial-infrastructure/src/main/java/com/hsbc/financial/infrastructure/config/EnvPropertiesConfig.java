package com.hsbc.financial.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * 动态加载不同环境下的配置文件
 *
 * @author zhaoyongping
 * @date 2023/11/8 16:48
 * @className EnvPropertiesConfig
 **/
@Configuration
public class EnvPropertiesConfig {
    /**
     * 加载属性配置
     *
     * @param environment 环境属性
     * @return PropertySourcesPlaceholderConfigurer
     * @author zhaoyongping
     * @date 2023/8/2 10:45
     * @description 加载属性配置
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfig(Environment environment) throws IOException {
        PropertySourcesPlaceholderConfigurer config = new PropertySourcesPlaceholderConfigurer();
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String resourceUrl = "classpath:properties/" + environment.getActiveProfiles()[0] + "/*.properties";
            config.setLocations(new PathMatchingResourcePatternResolver().getResources(resourceUrl));
        } else {
            //兜底策略
            config.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:properties/dev/*.properties"));
        }
        return config;
    }
}
