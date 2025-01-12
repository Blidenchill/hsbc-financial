package com.hsbc.financial.infrastructure.cache;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;

import com.hsbc.financial.domain.common.exception.InfrastructureException;
import org.redisson.api.RBucket;

import java.util.concurrent.TimeUnit;
import org.mockito.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * This class is used for unit testing the RedissonCacheService class.(Generated by JoyCoder)
 *
 * @author: zhaoyongping8
 * @date: 2025-01-12
 */
@ExtendWith(MockitoExtension.class)
public class RedissonCacheServiceTest {


    @Mock
    private RedissonClient redissonClient;

    @InjectMocks
    private RedissonCacheService redissonCacheService;

    @Mock
    private RBucket rBucket;

    @Test
    public void testGetSuccess() {
        String key = "testKey";
        String expectedValue = "testValue";

        // Setup mock behavior
        when(redissonClient.getBucket(key)).thenReturn(rBucket);
        when(rBucket.get()).thenReturn(expectedValue);

        // Execute the method
        String actualValue = redissonCacheService.get(key);

        // Verify
        assertEquals(expectedValue, actualValue);
        verify(redissonClient, times(1)).getBucket(key);
        verify(rBucket, times(1)).get();
    }

    @Test
    public void testGetException() {
        String key = "testKey";
        when(redissonClient.getBucket(key)).thenReturn(rBucket);
        when(rBucket.get()).thenThrow(new RuntimeException("Simulated exception"));

        // Execute and verify exception
        InfrastructureException exception = assertThrows(InfrastructureException.class, () -> {
            redissonCacheService.get(key);
        });

        assertEquals("get error", exception.getMessage());
        verify(redissonClient, times(1)).getBucket(key);
        verify(rBucket, times(1)).get();
    }

    @Test
    public void testAdd() {
        String key = "testKey";
        String value = "testValue";
        long expirationTime = 60;
        TimeUnit timeUnit = TimeUnit.SECONDS;

        // Setup mock behavior
        when(redissonClient.getBucket(key)).thenReturn(rBucket);

        // Execute the method
        redissonCacheService.add(key, value, expirationTime, timeUnit);

        // Verify
        verify(redissonClient, times(1)).getBucket(key);
        verify(rBucket, times(1)).set(value, expirationTime, timeUnit);
    }

    @Test
    void testAddException() {
        String key = "testKey";
        String value = "testValue";
        long expirationTime = 60;
        TimeUnit timeUnit = TimeUnit.SECONDS;

        // Setup mock behavior to throw an exception
        when(redissonClient.getBucket(key)).thenReturn(rBucket);
        doThrow(new RuntimeException("Simulated exception")).when(rBucket).set(value, expirationTime, timeUnit);

        // Execute and verify exception
        InfrastructureException exception = assertThrows(InfrastructureException.class, () -> {
            redissonCacheService.add(key, value, expirationTime, timeUnit);
        });

        assertEquals("add error", exception.getMessage());
        verify(redissonClient, times(1)).getBucket(key);
        verify(rBucket, times(1)).set(value, expirationTime, timeUnit);
    }


    @Test
    void testGenerateKeySuccess() {
        String range = "range1";
        String key = "key1";

        // Execute the method
        String result = redissonCacheService.generateKey(range, key);

        // Verify
        assertThat(result).isEqualTo("intl-wl-contract:range1:key1");
    }
    @Test
    void testGenerateKeyWithNullRange() {
        String range = null;
        String key = "key1";

        // Execute and verify exception
        InfrastructureException exception = assertThrows(InfrastructureException.class, () -> {
            redissonCacheService.generateKey(range, key);
        });

        assertThat(exception.getMessage()).isEqualTo("range or key is null");
    }
    @Test
    void testGenerateKeyWithNullKey() {
        String range = "range1";
        String key = null;

        // Execute and verify exception
        InfrastructureException exception = assertThrows(InfrastructureException.class, () -> {
            redissonCacheService.generateKey(range, key);
        });

        assertThat(exception.getMessage()).isEqualTo("range or key is null");
    }
    @Test
    void testGenerateKeyWithBlankRange() {
        String range = "   ";
        String key = "key1";

        // Execute and verify exception
        InfrastructureException exception = assertThrows(InfrastructureException.class, () -> {
            redissonCacheService.generateKey(range, key);
        });

        assertThat(exception.getMessage()).isEqualTo("range or key is null");
    }
    @Test
    void testGenerateKeyWithBlankKey() {
        String range = "range1";
        String key = "   ";

        // Execute and verify exception
        InfrastructureException exception = assertThrows(InfrastructureException.class, () -> {
            redissonCacheService.generateKey(range, key);
        });

        assertThat(exception.getMessage()).isEqualTo("range or key is null");
    }

}