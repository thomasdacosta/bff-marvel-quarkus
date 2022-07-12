package br.com.marvel.application.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    @SuppressWarnings("rawtypes")
    public static Object createObject(String value, Class klazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(value, klazz);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String createJson(Object value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return null;
        }
    }

}
