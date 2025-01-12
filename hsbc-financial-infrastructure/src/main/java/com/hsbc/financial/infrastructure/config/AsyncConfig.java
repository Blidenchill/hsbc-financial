package com.hsbc.financial.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * AsyncConfig
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AsyncConfig
 **/
@Configuration
@EnableAsync
public class AsyncConfig {
    /**
     * 创建一个名为'eventTaskExecutor'的线程池执行器。
     *
     * @return 配置好的线程池执行器对象。
     */
    @Bean(name = "eventTaskExecutor")
    public Executor eventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("eventExecutor-");
        executor.initialize();
        return executor;
    }
}
