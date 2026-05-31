package com.gogidix.universal.tracking.application.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Mapper for JSON conversion operations.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonMapper {

    private final ObjectMapper objectMapper;

    /**
     * Convert Map to JSON string
     */
    public String toJsonString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("Error converting map to JSON string", e);
            return null;
        }
    }

    /**
     * Convert JSON string to Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> fromJsonString(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON string to map", e);
            return null;
        }
    }
}
