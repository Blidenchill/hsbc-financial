package com.hsbc.financial.infrastructure.cache;

import com.hsbc.financial.domain.common.cache.CacheService;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * RedissonCacheService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className RedissonCacheService
 **/
@Service
public class RedissonCacheService implements CacheService {
    /**
     * Redisson客户端实例，用于操作Redisson缓存。
     */
    @Autowired
    RedissonClient redissonClient;

    /**
     * 根据给定的范围和键生成国际化合同的唯一标识符。
     * @param range 范围字符串，用于区分不同的合同类型。
     * @param key 键字符串，用于标识具体的合同。
     * @return 生成的唯一标识符，格式为 'intl-wl-contract:range:key'。
     */
    @Override
    public String generateKey(String range, String key) {
        return "intl-wl-contract:" + range + ":" + key;
    }

    /**
     * 将指定的值存储到Redis中，并设置过期时间。
     * @param key 存储的键名。
     * @param value 存储的值。
     * @param expirationTime 过期时间，单位由 timeUnit 参数指定。
     * @param timeUnit 过期时间的单位。
     */
    @Override
    public <V> void add(String key, V value, long expirationTime, TimeUnit timeUnit) {
        RBucket<V> bucket = redissonClient.getBucket(key);
        bucket.set(value, expirationTime, timeUnit);

    }

    /**
     * 根据给定的键从Redis中获取对应的值。
     * @param key Redis中的键。
     * @return 对应的值。
     */
    @Override
    public <V> V get(String key) {
        RBucket<V> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 从 Redis 中删除指定 key 的值。
     * @param key 要删除的键名。
     */
    @Override
    public <V> void remove(String key) {
        redissonClient.getBucket(key).delete();

    }

    /**
     * 向Redis列表中添加元素。
     * @param key 列表的键。
     * @param values 要添加的元素。
     */
    @Override
    public <V> void addToList(String key, V... values) {
        RList<V> list = redissonClient.getList(key);
        list.addAll(Arrays.asList(values));

    }

    /**
     * 向 Redis 中的指定列表添加元素。
     * @param key 列表的键名。
     * @param array 要添加的元素列表。
     */
    @Override
    public <V> void addToList(String key, List<V> array) {
        RList<V> list = redissonClient.getList(key);
        list.addAll(array);
    }

    /**
     * 从 Redis 中获取指定 key 的列表数据。
     * @param key 列表的 key。
     * @return 指定 key 的列表数据。
     */
    @Override
    public <V> List<V> getList(String key) {
        RList<V> list = redissonClient.getList(key);
        return list.readAll();
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        RKeys keys = redissonClient.getKeys();
        keys.expire(key, timeout, unit);
    }



}
