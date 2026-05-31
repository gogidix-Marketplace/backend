package com.gogidix.aiservices.aifrauddetectionservice.shared.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * Utility class for JSON operations.
 * <p>
 * Provides methods for converting objects to/from JSON, masking sensitive data,
 * and common JSON parsing tasks.
 */
@Slf4j
@UtilityClass
public class JsonUtil {

    /**
     * Shared ObjectMapper instance for JSON operations.
     */
    private final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    /**
     * Pattern to match credit card numbers in JSON.
     */
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile("\\b\\d{13,19}\\b");

    /**
     * Pattern to match email addresses.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\b[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}\\b"
    );

    /**
     * Creates and configures the ObjectMapper.
     *
     * @return the configured ObjectMapper
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * Gets the shared ObjectMapper instance.
     *
     * @return the ObjectMapper
     */
    public ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Converts an object to JSON string.
     *
     * @param obj the object to convert
     * @return the JSON string, or null if conversion fails
     */
    public String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Converts an object to pretty-printed JSON string.
     *
     * @param obj the object to convert
     * @return the pretty-printed JSON string, or null if conversion fails
     */
    public String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to pretty JSON: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Converts a JSON string to an object of the specified type.
     *
     * @param json  the JSON string
     * @param clazz the target class
     * @param <T>   the type of the target class
     * @return the parsed object, or null if parsing fails
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON to {}: {}", clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * Converts a JSON string to an object of the specified type with reference.
     *
     * @param json             the JSON string
     * @param typeReference    the type reference
     * @param <T>              the type of the target
     * @return the parsed object, or null if parsing fails
     */
    public <T> T fromJson(String json, com.fasterxml.jackson.core.type.TypeReference<T> typeReference) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Masks sensitive data in a JSON string.
     *
     * @param json the JSON string to mask
     * @return the JSON string with sensitive data masked
     */
    public String maskSensitive(String json) {
        if (json == null || json.isBlank()) {
            return json;
        }

        String masked = json;

        // Mask common field names
        masked = maskField(masked, "password");
        masked = maskField(masked, "secret");
        masked = maskField(masked, "token");
        masked = maskField(masked, "apiKey");
        masked = maskField(masked, "api_key");
        masked = maskField(masked, "accessToken");
        masked = maskField(masked, "access_token");
        masked = maskField(masked, "refreshToken");
        masked = maskField(masked, "refresh_token");
        masked = maskField(masked, "ssn");
        masked = maskField(masked, "socialSecurity");
        masked = maskField(masked, "creditCard");
        masked = maskField(masked, "credit_card");
        masked = maskField(masked, "cardNumber");
        masked = maskField(masked, "card_number");
        masked = maskField(masked, "cvv");
        masked = maskField(masked, "pin");
        masked = maskField(masked, "accountNumber");
        masked = maskField(masked, "account_number");

        return masked;
    }

    /**
     * Masks a specific field in a JSON string.
     *
     * @param json     the JSON string
     * @param fieldName the name of the field to mask
     * @return the JSON string with the field masked
     */
    private String maskField(String json, String fieldName) {
        // Pattern to match: "fieldName":"value" or "fieldName":"value",
        // This handles quoted string values
        Pattern pattern = Pattern.compile(
                "\"" + fieldName + "\"\\s*:\\s*\"([^\"]*)\"",
                Pattern.CASE_INSENSITIVE
        );
        return pattern.matcher(json).replaceAll("\"" + fieldName + "\":\"*****\"");
    }

    /**
     * Masks an object's sensitive fields before converting to JSON.
     *
     * @param obj      the object to mask and convert
     * @param fieldsToMask the array of field names to mask
     * @return the JSON string with sensitive fields masked
     */
    public String toJsonMasked(Object obj, String[] fieldsToMask) {
        if (obj == null) {
            return null;
        }

        try {
            // Convert to JSON node
            com.fasterxml.jackson.databind.JsonNode node = OBJECT_MAPPER.valueToTree(obj);

            // Mask specified fields
            if (node.isObject()) {
                com.fasterxml.jackson.databind.node.ObjectNode objectNode = (com.fasterxml.jackson.databind.node.ObjectNode) node;
                for (String field : fieldsToMask) {
                    if (objectNode.has(field)) {
                        objectNode.put(field, "*****");
                    }
                }
            }

            return OBJECT_MAPPER.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            log.error("Failed to mask and convert object to JSON: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validates if a string is valid JSON.
     *
     * @param json the string to validate
     * @return true if the string is valid JSON, false otherwise
     */
    public boolean isValidJson(String json) {
        if (json == null || json.isBlank()) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * Extracts a specific field value from a JSON string.
     *
     * @param json     the JSON string
     * @param fieldName the name of the field to extract
     * @return the field value as a string, or null if not found
     */
    public String extractField(String json, String fieldName) {
        if (json == null || json.isBlank() || fieldName == null) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.JsonNode node = OBJECT_MAPPER.readTree(json);
            com.fasterxml.jackson.databind.JsonNode fieldNode = node.get(fieldName);
            if (fieldNode != null) {
                return fieldNode.asText();
            }
            return null;
        } catch (JsonProcessingException e) {
            log.error("Failed to extract field '{}' from JSON: {}", fieldName, e.getMessage());
            return null;
        }
    }

    /**
     * Merges two JSON strings.
     *
     * @param json1 the first JSON string
     * @param json2 the second JSON string (values take precedence)
     * @return the merged JSON string
     */
    public String mergeJson(String json1, String json2) {
        if (json1 == null || json1.isBlank()) {
            return json2;
        }
        if (json2 == null || json2.isBlank()) {
            return json1;
        }

        try {
            com.fasterxml.jackson.databind.JsonNode node1 = OBJECT_MAPPER.readTree(json1);
            com.fasterxml.jackson.databind.JsonNode node2 = OBJECT_MAPPER.readTree(json2);

            if (node1.isObject() && node2.isObject()) {
                com.fasterxml.jackson.databind.node.ObjectNode objectNode1 = (com.fasterxml.jackson.databind.node.ObjectNode) node1;
                com.fasterxml.jackson.databind.node.ObjectNode objectNode2 = (com.fasterxml.jackson.databind.node.ObjectNode) node2;
                objectNode1.setAll(objectNode2);
                return OBJECT_MAPPER.writeValueAsString(objectNode1);
            }

            return json2;
        } catch (JsonProcessingException e) {
            log.error("Failed to merge JSON: {}", e.getMessage());
            return json1;
        }
    }

    /**
     * Formats a JSON string for pretty printing.
     *
     * @param json the JSON string to format
     * @return the formatted JSON string, or the original if formatting fails
     */
    public String formatJson(String json) {
        if (json == null || json.isBlank()) {
            return json;
        }
        try {
            Object jsonObject = OBJECT_MAPPER.readValue(json, Object.class);
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            log.warn("Failed to format JSON: {}", e.getMessage());
            return json;
        }
    }

    /**
     * Minifies a JSON string by removing unnecessary whitespace.
     *
     * @param json the JSON string to minify
     * @return the minified JSON string
     */
    public String minifyJson(String json) {
        if (json == null || json.isBlank()) {
            return json;
        }
        try {
            Object jsonObject = OBJECT_MAPPER.readValue(json, Object.class);
            return OBJECT_MAPPER.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            log.warn("Failed to minify JSON: {}", e.getMessage());
            return json;
        }
    }
}
