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
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Jackson Error", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> targetType) {
        try {
            return OBJECT_MAPPER.readValue(json, targetType);
        } catch (JsonProcessingException e) {
            log.error("Jackson Error", e);
            return null;
        }
    }
    public static <T> T getObject(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Jackson Error", e);
            return null;
        }
    }

    /**
     * json to list
     **/
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
