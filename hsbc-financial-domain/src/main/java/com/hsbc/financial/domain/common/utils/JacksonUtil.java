package com.hsbc.financial.domain.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * JacksonUtil
 *
 * @author zhaoyongping
 * @date 2023/11/8 15:31
 * @className JacksonUtil
 **/
@Slf4j
public class JacksonUtil {
    /**
     * JacksonUtil类的默认ObjectMapper实例，用于处理JSON序列化和反序列化。
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将对象转换为 JSON 字符串。
     * @param object 要转换的对象。
     * @return 转换后的 JSON 字符串，若转换失败则返回 null。
     */
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Jackson Error", e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象。
     * @param json JSON 字符串
     * @param targetType 目标类型的 Class 对象
     * @return 转换后的对象，若发生 Jackson 错误则返回 null
     */
    public static <T> T fromJson(String json, Class<T> targetType) {
        try {
            return OBJECT_MAPPER.readValue(json, targetType);
        } catch (JsonProcessingException e) {
            log.error("Jackson Error", e);
            return null;
        }
    }
    /**
     * 将JSON字符串转换为指定类型的对象。
     * @param json JSON字符串
     * @param typeReference 指定类型的TypeReference对象
     * @return 转换后的对象，若发生异常则返回null
     */
    public static <T> T getObject(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Jackson Error", e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为指定类型的List对象。
     * @param json JSON字符串
     * @param targetType 目标类型的Class对象
     * @return 转换后的List对象，若转换失败则返回null
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> targetType) {
        try {
            // TypeReference<T>对象，因为是抽象类所以要追加{}实现
            CollectionType collectionType =
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, targetType);
            // 读取字符串，开始转换
            return OBJECT_MAPPER.readValue(json, collectionType);
        } catch (JsonProcessingException e) {
            log.error("Jackson Error", e);
            return null;
        }
    }
}
