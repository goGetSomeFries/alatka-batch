package com.alatka.batch.infra.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class XmlUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new XmlFactory());

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Map<String, Object> getMap(String content) {
        return getMap(content, null, Object.class);
    }

    public static <T> Map<String, T> getMap(String content, String rootName, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readerForMapOf(clazz).withRootName(rootName).readValue(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> getList(String content, String rootName) {
        try {
            return OBJECT_MAPPER.readerForListOf(Map.class).withRootName(rootName).readValue(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode getJsonNode(String content) {
        try {
            return OBJECT_MAPPER.readTree(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
