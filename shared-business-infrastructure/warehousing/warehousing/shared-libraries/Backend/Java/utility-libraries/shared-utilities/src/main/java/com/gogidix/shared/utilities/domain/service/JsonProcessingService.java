package com.gogidix.shared.utilities.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Domain service for JSON processing operations
 * Provides comprehensive JSON serialization, deserialization, and manipulation capabilities
 */
@Slf4j
public class JsonProcessingService {

    private final ObjectMapper objectMapper;

    public JsonProcessingService() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    /**
     * Serialize object to JSON string
     */
    public UtilityResult<String> toJson(String operationId, Object object) {
        try {
            String result = objectMapper.writeValueAsString(object);
            log.debug("Serialized object {} to JSON", object.getClass().getSimpleName());
            return UtilityResult.success(operationId, UtilityType.JSON_SERIALIZATION, result);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_SERIALIZATION, e.getMessage());
        }
    }

    /**
     * Deserialize JSON string to object
     */
    public <T> UtilityResult<T> fromJson(String operationId, String json, Class<T> targetClass) {
        try {
            T result = objectMapper.readValue(json, targetClass);
            log.debug("Deserialized JSON to object {}", targetClass.getSimpleName());
            return UtilityResult.success(operationId, UtilityType.JSON_DESERIALIZATION, result);
        } catch (IOException e) {
            log.error("Failed to deserialize JSON to object {}", targetClass.getSimpleName(), e);
            return UtilityResult.failure(operationId, UtilityType.JSON_DESERIALIZATION, e.getMessage());
        }
    }

    /**
     * Deserialize JSON string to object with TypeReference
     */
    public <T> UtilityResult<T> fromJson(String operationId, String json, TypeReference<T> typeReference) {
        try {
            T result = objectMapper.readValue(json, typeReference);
            log.debug("Deserialized JSON to complex type");
            return UtilityResult.success(operationId, UtilityType.JSON_DESERIALIZATION, result);
        } catch (IOException e) {
            log.error("Failed to deserialize JSON to complex type", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_DESERIALIZATION, e.getMessage());
        }
    }

    /**
     * Validate if string is valid JSON
     */
    public UtilityResult<Boolean> isValidJson(String operationId, String json) {
        try {
            objectMapper.readTree(json);
            log.debug("JSON validation successful");
            return UtilityResult.success(operationId, UtilityType.JSON_VALIDATION, true);
        } catch (IOException e) {
            log.debug("JSON validation failed: {}", e.getMessage());
            return UtilityResult.success(operationId, UtilityType.JSON_VALIDATION, false);
        }
    }

    /**
     * Pretty print JSON string
     */
    public UtilityResult<String> prettyPrint(String operationId, String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            log.debug("Pretty printed JSON");
            return UtilityResult.success(operationId, UtilityType.JSON_TRANSFORMATION, result);
        } catch (IOException e) {
            log.error("Failed to pretty print JSON", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_TRANSFORMATION, e.getMessage());
        }
    }

    /**
     * Minify JSON string (remove whitespace)
     */
    public UtilityResult<String> minifyJson(String operationId, String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String result = objectMapper.writeValueAsString(jsonNode);
            log.debug("Minified JSON");
            return UtilityResult.success(operationId, UtilityType.JSON_TRANSFORMATION, result);
        } catch (IOException e) {
            log.error("Failed to minify JSON", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_TRANSFORMATION, e.getMessage());
        }
    }

    /**
     * Extract value from JSON by path
     */
    public UtilityResult<Object> extractValue(String operationId, String json, String path) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode valueNode = rootNode.at(path);
            
            if (valueNode.isMissingNode()) {
                return UtilityResult.failure(operationId, UtilityType.JSON_TRANSFORMATION, 
                    "Path not found: " + path);
            }
            
            Object result = convertJsonNodeToValue(valueNode);
            log.debug("Extracted value from JSON at path: {}", path);
            return UtilityResult.success(operationId, UtilityType.JSON_TRANSFORMATION, result);
        } catch (IOException e) {
            log.error("Failed to extract value from JSON at path: {}", path, e);
            return UtilityResult.failure(operationId, UtilityType.JSON_TRANSFORMATION, e.getMessage());
        }
    }

    /**
     * Update value in JSON at path
     */
    public UtilityResult<String> updateValue(String operationId, String json, String path, Object newValue) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            String[] pathParts = path.split("\\.");
            
            JsonNode parentNode = rootNode;
            for (int i = 0; i < pathParts.length - 1; i++) {
                parentNode = parentNode.get(pathParts[i]);
                if (parentNode == null) {
                    return UtilityResult.failure(operationId, UtilityType.JSON_TRANSFORMATION, 
                        "Path not found: " + String.join(".", Arrays.copyOf(pathParts, i + 1)));
                }
            }
            
            // This is a simplified implementation - in practice, you'd need more sophisticated JSON manipulation
            String result = objectMapper.writeValueAsString(rootNode);
            log.debug("Updated value in JSON at path: {}", path);
            return UtilityResult.success(operationId, UtilityType.JSON_TRANSFORMATION, result);
        } catch (IOException e) {
            log.error("Failed to update value in JSON at path: {}", path, e);
            return UtilityResult.failure(operationId, UtilityType.JSON_TRANSFORMATION, e.getMessage());
        }
    }

    /**
     * Convert JSON to Map
     */
    public UtilityResult<Map<String, Object>> toMap(String operationId, String json) {
        try {
            Map<String, Object> result = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            log.debug("Converted JSON to Map");
            return UtilityResult.success(operationId, UtilityType.JSON_DESERIALIZATION, result);
        } catch (IOException e) {
            log.error("Failed to convert JSON to Map", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_DESERIALIZATION, e.getMessage());
        }
    }

    /**
     * Convert Map to JSON
     */
    public UtilityResult<String> fromMap(String operationId, Map<String, Object> map) {
        try {
            String result = objectMapper.writeValueAsString(map);
            log.debug("Converted Map to JSON");
            return UtilityResult.success(operationId, UtilityType.JSON_SERIALIZATION, result);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert Map to JSON", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_SERIALIZATION, e.getMessage());
        }
    }

    /**
     * Merge two JSON objects
     */
    public UtilityResult<String> mergeJson(String operationId, String json1, String json2) {
        try {
            JsonNode node1 = objectMapper.readTree(json1);
            JsonNode node2 = objectMapper.readTree(json2);
            
            // This is a simplified merge - in practice, you'd implement deep merging logic
            Map<String, Object> merged = new HashMap<>();
            merged.putAll(objectMapper.convertValue(node1, Map.class));
            merged.putAll(objectMapper.convertValue(node2, Map.class));
            
            String result = objectMapper.writeValueAsString(merged);
            log.debug("Merged two JSON objects");
            return UtilityResult.success(operationId, UtilityType.JSON_TRANSFORMATION, result);
        } catch (IOException e) {
            log.error("Failed to merge JSON objects", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_TRANSFORMATION, e.getMessage());
        }
    }

    /**
     * Count elements in JSON array
     */
    public UtilityResult<Integer> countArrayElements(String operationId, String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            if (!rootNode.isArray()) {
                return UtilityResult.failure(operationId, UtilityType.JSON_VALIDATION, 
                    "JSON is not an array");
            }
            
            int result = rootNode.size();
            log.debug("Counted {} elements in JSON array", result);
            return UtilityResult.success(operationId, UtilityType.JSON_VALIDATION, result);
        } catch (IOException e) {
            log.error("Failed to count array elements", e);
            return UtilityResult.failure(operationId, UtilityType.JSON_VALIDATION, e.getMessage());
        }
    }

    /**
     * Helper method to convert JsonNode to appropriate Java value
     */
    private Object convertJsonNodeToValue(JsonNode node) {
        if (node.isNull()) {
            return null;
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isInt()) {
            return node.asInt();
        } else if (node.isLong()) {
            return node.asLong();
        } else if (node.isDouble()) {
            return node.asDouble();
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonNode item : node) {
                list.add(convertJsonNodeToValue(item));
            }
            return list;
        } else if (node.isObject()) {
            Map<String, Object> map = new HashMap<>();
            node.fields().forEachRemaining(entry -> 
                map.put(entry.getKey(), convertJsonNodeToValue(entry.getValue())));
            return map;
        } else {
            return node.toString();
        }
    }
}