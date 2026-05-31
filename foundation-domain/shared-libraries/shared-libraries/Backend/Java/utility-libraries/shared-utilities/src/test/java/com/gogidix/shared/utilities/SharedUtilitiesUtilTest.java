package com.gogidix.shared.utilities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

class SharedUtilitiesUtilTest {

    @Test
    void jsonUtils_toJson() {
        String json = JsonUtils.toJson(Map.of("key", "value"));
        assertTrue(json.contains("key"));
        assertTrue(json.contains("value"));
    }

    @Test
    void jsonUtils_toJson_throws() {
        assertThrows(RuntimeException.class, () -> JsonUtils.toJson(new Object() {
            @Override
            public String toString() { throw new RuntimeException("boom"); }
        }));
    }

    @Test
    void jsonUtils_fromJson() {
        Map<?, ?> result = JsonUtils.fromJson("{\"key\":\"value\"}", Map.class);
        assertEquals("value", result.get("key"));
    }

    @Test
    void jsonUtils_fromJson_throws() {
        assertThrows(RuntimeException.class, () -> JsonUtils.fromJson("not json", Map.class));
    }

    @Test
    void jsonUtils_isValidJson_true() {
        assertTrue(JsonUtils.isValidJson("{\"key\":\"value\"}"));
    }

    @Test
    void jsonUtils_isValidJson_false() {
        assertFalse(JsonUtils.isValidJson("not json"));
    }

    @Test
    void jsonUtils_isValidJson_array() {
        assertTrue(JsonUtils.isValidJson("[1,2,3]"));
    }

    @Test
    void dateUtils_formatToIso() {
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
        String iso = DateUtils.formatToIso(dt);
        assertTrue(iso.contains("2024-06-15"));
        assertTrue(iso.contains("10:30"));
    }

    @Test
    void dateUtils_parseFromIso() {
        LocalDateTime dt = DateUtils.parseFromIso("2024-06-15T10:30:00");
        assertEquals(2024, dt.getYear());
        assertEquals(6, dt.getMonthValue());
        assertEquals(15, dt.getDayOfMonth());
    }

    @Test
    void dateUtils_toDate() {
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
        Date date = DateUtils.toDate(dt);
        assertNotNull(date);
    }

    @Test
    void dateUtils_fromDate() {
        Date date = new Date();
        LocalDateTime dt = DateUtils.fromDate(date);
        assertNotNull(dt);
    }

    @Test
    void dateUtils_isExpired_true() {
        assertTrue(DateUtils.isExpired(LocalDateTime.now().minusDays(1)));
    }

    @Test
    void dateUtils_isExpired_false() {
        assertFalse(DateUtils.isExpired(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void sharedUtilitiesApplication_classExists() {
        assertNotNull(SharedUtilitiesApplication.class);
    }

    @Test
    void sharedUtilitiesApplication_hasSpringBootAnnotation() {
        assertNotNull(SharedUtilitiesApplication.class.getAnnotation(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void sharedUtilitiesApplication_hasMainMethod() throws NoSuchMethodException {
        assertNotNull(SharedUtilitiesApplication.class.getMethod("main", String[].class));
    }
}
