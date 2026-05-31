package com.gogidix.shared.utilities.domain.service;

import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Map;

class JsonProcessingServiceTest {

    private final JsonProcessingService service = new JsonProcessingService();

    @Test
    void toJsonSuccess() {
        UtilityResult<String> result = service.toJson("op-1", Map.of("key", "value"));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult().contains("key"));
        assertTrue(result.getResult().contains("value"));
    }

    @Test
    void toJsonWithList() {
        UtilityResult<String> result = service.toJson("op-1", List.of(1, 2, 3));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult().contains("["));
    }

    @Test
    void fromJsonSuccess() {
        UtilityResult<Map> result = service.fromJson("op-1", "{\"key\":\"value\"}", Map.class);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("value", result.getResult().get("key"));
    }

    @Test
    void fromJsonInvalid() {
        UtilityResult<Map> result = service.fromJson("op-1", "not json", Map.class);
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void isValidJsonTrue() {
        UtilityResult<Boolean> result = service.isValidJson("op-1", "{\"valid\":true}");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult());
    }

    @Test
    void isValidJsonFalse() {
        UtilityResult<Boolean> result = service.isValidJson("op-1", "not json at all");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertFalse(result.getResult());
    }

    @Test
    void prettyPrint() {
        UtilityResult<String> result = service.prettyPrint("op-1", "{\"key\":\"value\"}");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult().contains("\n"));
    }

    @Test
    void prettyPrintInvalid() {
        UtilityResult<String> result = service.prettyPrint("op-1", "invalid");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void fromJsonWithTypeReference() {
        UtilityResult<List> result = service.fromJson("op-1", "[1,2,3]", List.class);
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(3, result.getResult().size());
    }

    @Test
    void minifyJson() {
        UtilityResult<String> result = service.minifyJson("op-1", "{\n  \"key\"  :  \"value\"\n}");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertFalse(result.getResult().contains("\n"));
    }

    @Test
    void minifyJsonInvalid() {
        UtilityResult<String> result = service.minifyJson("op-1", "invalid");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void extractValue() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"key\":\"value\"}", "/key");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("value", result.getResult());
    }

    @Test
    void extractValueMissingPath() {
        UtilityResult<Object> result = service.extractValue("op-1", "{\"key\":\"value\"}", "/missing");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void extractValueInvalidJson() {
        UtilityResult<Object> result = service.extractValue("op-1", "invalid", "/key");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void updateValue() {
        UtilityResult<String> result = service.updateValue("op-1", "{\"key\":\"value\"}", "key", "newvalue");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
    }

    @Test
    void updateValueInvalidPath() {
        UtilityResult<String> result = service.updateValue("op-1", "{\"key\":\"value\"}", "missing.deep.path", "newvalue");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void toMap() {
        UtilityResult<Map<String, Object>> result = service.toMap("op-1", "{\"key\":\"value\",\"num\":42}");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("value", result.getResult().get("key"));
        assertEquals(42, result.getResult().get("num"));
    }

    @Test
    void toMapInvalid() {
        UtilityResult<Map<String, Object>> result = service.toMap("op-1", "invalid");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void fromMap() {
        UtilityResult<String> result = service.fromMap("op-1", Map.of("key", "value"));
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult().contains("key"));
    }

    @Test
    void mergeJson() {
        UtilityResult<String> result = service.mergeJson("op-1", "{\"a\":1}", "{\"b\":2}");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertTrue(result.getResult().contains("a"));
        assertTrue(result.getResult().contains("b"));
    }

    @Test
    void mergeJsonInvalid() {
        UtilityResult<String> result = service.mergeJson("op-1", "invalid", "{\"b\":2}");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void countArrayElements() {
        UtilityResult<Integer> result = service.countArrayElements("op-1", "[1,2,3,4,5]");
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals(5, result.getResult());
    }

    @Test
    void countArrayElementsNotArray() {
        UtilityResult<Integer> result = service.countArrayElements("op-1", "{\"key\":\"value\"}");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }

    @Test
    void countArrayElementsInvalid() {
        UtilityResult<Integer> result = service.countArrayElements("op-1", "invalid");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
    }
}
