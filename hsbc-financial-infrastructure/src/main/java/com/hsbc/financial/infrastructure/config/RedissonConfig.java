package com.hsbc.financial.infrastructure.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedissonConfig
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className RedissonConfig
 **/
@Configuration
public class RedissonConfig {
    /**
     * Redis服务器的主机地址。
     */
    @Value("${spring.redis.host}")
    private String host;
    /**
     * Redis服务器端口号。
     */
    @Value("${spring.redis.port}")
    private String port;
    /**
     * Redisson连接密码。
     */
    @Value("${spring.redis.password}")
    private String password;

    /**
     * 创建Redisson客户端实例。
     *
     * @return Redisson客户端实例。
     */
    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        String addressTemplate = "redis://%s:%s";
        String address = String.format(addressTemplate, host, port);
        config.useSingleServer().setAddress(address);
        config.useSingleServer().setPassword(password);
        return Redisson.create(config);
    }

}