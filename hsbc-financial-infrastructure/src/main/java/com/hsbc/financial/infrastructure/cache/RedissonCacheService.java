package com.hsbc.financial.infrastructure.cache;

import com.hsbc.financial.domain.common.cache.CacheService;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * RedissonCacheService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className RedissonCacheService
 **/
@Service
@Slf4j
public class RedissonCacheService implements CacheService {
    /**
     * Redisson客户端实例，用于操作Redisson缓存。
     */
    @Autowired
    RedissonClient redissonClient;

    /**
     * 根据给定的范围和键生成国际化合同的唯一标识符。
     *
     * @param range 范围字符串，用于区分不同的合同类型。
     * @param key   键字符串，用于标识具体的合同。
     * @return 生成的唯一标识符，格式为 'intl-wl-contract:range:key'。
     */
    @Override
    public String generateKey(String range, String key) {
        if (StringUtils.isBlank(range) || StringUtils.isBlank(key)) {
            log.error("generateKey error, range or key is null");
            throw new InfrastructureException("range or key is null");
        }
        return "intl-wl-contract:" + range + ":" + key;
    }

    /**
     * 将指定的值存储到Redis中，并设置过期时间。
     *
     * @param key            存储的键名。
     * @param value          存储的值。
     * @param expirationTime 过期时间，单位由 timeUnit 参数指定。
     * @param timeUnit       过期时间的单位。
     */
    @Override
    public <V> void add(String key, V value, long expirationTime, TimeUnit timeUnit) {
        try {
            RBucket<V> bucket = redissonClient.getBucket(key);
            bucket.set(value, expirationTime, timeUnit);
        } catch (Exception ex) {
            log.error("add error, key:{}", key, ex);
            throw new InfrastructureException("add error");
        }


    }

    /**
     * 根据给定的键从Redis中获取对应的值。
     *
     * @param key Redis中的键。
     * @return 对应的值。
     */
    @Override
    public <V> V get(String key) {
        try {
            RBucket<V> bucket = redissonClient.getBucket(key);
            return bucket.get();
        } catch (Exception ex) {
            log.error("get error, key:{}", key, ex);
            throw new InfrastructureException("get error");
        }

    }


}
