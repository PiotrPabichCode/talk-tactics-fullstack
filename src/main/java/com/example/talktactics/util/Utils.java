package com.example.talktactics.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static boolean isEmptyString(String value) {
        return value == null || value.trim().isEmpty();
    }
    public static Map<String, String> getJsonPropertyFieldMap(Class<?> clazz) {
        Map<String, String> fieldMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            JsonProperty annotation = field.getAnnotation(JsonProperty.class);
            if (annotation != null) {
                fieldMap.put(annotation.value(), field.getName());
            } else {
                fieldMap.put(field.getName(), field.getName());
            }
        }
        return fieldMap;
    }

}