package com.hsbc.financial.domain.common.cache;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * CacheService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className CacheService
 **/
public interface CacheService {
    /**
     * 根据指定的范围和键生成唯一缓存key。
     * @param range 指定的范围。
     * @param key 用于生成标识符的键。
     * @return 生成的唯一标识符。
     */
    String generateKey(String range, String key);
    /**
     * 向缓存中添加一个键值对，并设置过期时间。
     * @param key 键，用于标识缓存中的值。
     * @param value 值，需要被缓存的对象。
     * @param expirationTime 过期时间，表示从现在开始到过期的时间长度。
     * @param timeUnit 时间单位，用于指定过期时间的单位。
     */
    <V> void add(String key,  V value, long expirationTime, TimeUnit timeUnit);

    /**
     * 根据指定的键获取相应的值。
     * @param key 用于查找的键。
     */
    <V> V get(String key);

    /**
     * 从集合中删除指定键的元素。
     * @param key 要删除的元素的键。
     */
    <V> void remove(String key);

    /**
     * 向列表中添加指定键值对的元素。
     * @param key 键名，用于标识要添加的元素所属的列表。
     * @param values 要添加的元素，可以有多个。
     */
    <V> void addToList(String key, V... values);
    /**
     * 向指定的key对应的List中添加元素。
     * @param key List的标识符。
     * @param array 要添加的元素集合。
     */
    <V> void addToList(String key, List<V> array);
    /**
     * 根据指定的键获取对应的列表。
     * @param key 用于查找列表的键。
     * @return 与指定键关联的列表。
     */
    <V> List<V> getList(String key);



    /**
     * 设置指定key的过期时间
     * @param key 要设置过期时间的键
     * @param timeout 过期时间的值
     * @param unit 过期时间的单位
     */
    void expire(String key, long timeout, TimeUnit unit);

}
