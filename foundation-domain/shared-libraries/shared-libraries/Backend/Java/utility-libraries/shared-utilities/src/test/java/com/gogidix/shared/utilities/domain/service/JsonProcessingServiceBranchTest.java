package com.gogidix.shared.utilities.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class JsonProcessingServiceBranchTest {

    private final JsonProcessingService service = new JsonProcessingService();

    @Test
    void extractValue_nullNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"key\":\"value\"}", "/nonexistent");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void extractValue_booleanNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"flag\":true}", "/flag");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(true, result.getResult());
    }

    @Test
    void extractValue_intNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"num\":42}", "/num");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(42, result.getResult());
    }

    @Test
    void extractValue_longNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"num\":9999999999}", "/num");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult() instanceof Number);
    }

    @Test
    void extractValue_doubleNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"num\":3.14}", "/num");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(3.14, (Double) result.getResult(), 0.001);
    }

    @Test
    void extractValue_textNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"name\":\"hello\"}", "/name");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("hello", result.getResult());
    }

    @Test
    void extractValue_nullJsonValue() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"val\":null}", "/val");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertNull(result.getResult());
    }

    @Test
    void extractValue_arrayNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"arr\":[1,2,3]}", "/arr");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult() instanceof List);
        assertEquals(3, ((List<?>) result.getResult()).size());
    }

    @Test
    void extractValue_objectNode() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"obj\":{\"a\":1}}", "/obj");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult() instanceof Map);
        assertEquals(1, ((Map<?, ?>) result.getResult()).size());
    }

    @Test
    void fromJsonWithTypeReference() {
        UtilityResult<List<Integer>> result = service.fromJson("op-1", "[1,2,3]", new TypeReference<List<Integer>>() {});
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(3, result.getResult().size());
    }

    @Test
    void fromJsonWithTypeReference_invalidJson() {
        UtilityResult<List<Integer>> result = service.fromJson("op-1", "not json", new TypeReference<List<Integer>>() {});
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void toJson_throwable() {
        UtilityResult<String> result = service.toJson("op-1", new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("boom");
            }
        });
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void updateValue_validSinglePath() {
        UtilityResult<String> result = service.updateValue("op-1", "{\"key\":\"value\"}", "key", "new");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
    }

    @Test
    void updateValue_invalidJson() {
        UtilityResult<String> result = service.updateValue("op-1", "invalid", "key", "new");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void mergeJson_secondInvalid() {
        UtilityResult<String> result = service.mergeJson("op-1", "{\"a\":1}", "invalid");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void fromMap_empty() {
        UtilityResult<String> result = service.fromMap("op-1", Map.of());
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("{}", result.getResult());
    }

    @Test
    void toMap_withNested() {
        UtilityResult<Map<String, Object>> result = service.toMap("op-1", "{\"a\":{\"b\":1}}");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult().get("a") instanceof Map);
    }

    @Test
    void isValidJson_array() {
        UtilityResult<Boolean> result = service.isValidJson("op-1", "[1,2,3]");
        assertTrue(result.getResult());
    }

    @Test
    void isValidJson_object() {
        UtilityResult<Boolean> result = service.isValidJson("op-1", "{\"k\":\"v\"}");
        assertTrue(result.getResult());
    }
}
