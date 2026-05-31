package com.gogidix.aiservices.aigatewayservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RouteFilter.
 */
@DisplayName("RouteFilter Tests")
class RouteFilterTest {

    @Test
    @DisplayName("Should create filter with valid parameters")
    void shouldCreateFilterWithValidParameters() {
        RouteFilter filter = RouteFilter.builder()
                .name("RateLimitFilter")
                .type("RATE_LIMIT")
                .parameters(Map.of("limit", 100))
                .build();

        assertEquals("RateLimitFilter", filter.getName());
        assertEquals("RATE_LIMIT", filter.getType());
        assertEquals(Map.of("limit", 100), filter.getParameters());
    }

    @Test
    @DisplayName("Should create filter with null parameters")
    void shouldCreateFilterWithNullParameters() {
        RouteFilter filter = RouteFilter.builder()
                .name("AuthFilter")
                .type("AUTHENTICATION")
                .build();

        assertEquals("AuthFilter", filter.getName());
        assertEquals("AUTHENTICATION", filter.getType());
        assertNull(filter.getParameters());
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        RouteFilter filter1 = RouteFilter.builder()
                .name("TestFilter")
                .type("TEST")
                .build();

        RouteFilter filter2 = RouteFilter.builder()
                .name("TestFilter")
                .type("TEST")
                .build();

        RouteFilter filter3 = RouteFilter.builder()
                .name("OtherFilter")
                .type("TEST")
                .build();

        assertEquals(filter1, filter2);
        assertNotEquals(filter1, filter3);
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        RouteFilter filter1 = RouteFilter.builder()
                .name("TestFilter")
                .type("TEST")
                .build();

        RouteFilter filter2 = RouteFilter.builder()
                .name("TestFilter")
                .type("TEST")
                .build();

        assertEquals(filter1.hashCode(), filter2.hashCode());
    }

    @Test
    @DisplayName("Should convert to string correctly")
    void shouldConvertToStringCorrectly() {
        RouteFilter filter = RouteFilter.builder()
                .name("TestFilter")
                .type("TEST")
                .parameters(Map.of("key", "value"))
                .build();

        String result = filter.toString();

        assertTrue(result.contains("TestFilter"));
        assertTrue(result.contains("TEST"));
    }
}
