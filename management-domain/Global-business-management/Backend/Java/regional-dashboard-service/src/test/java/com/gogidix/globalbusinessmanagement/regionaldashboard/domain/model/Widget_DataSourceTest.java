package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.Widget;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class Widget_DataSourceTest {

        @Test
    void testBuilder() {
        Widget.DataSource dto = Widget.DataSource.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .method("test-method")
            .headers(Collections.emptyMap())
            .body(Collections.emptyMap())
            .cacheKey("test-cacheKey")
            .cacheTtl(42)
            .timeout(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-sourceType", dto.getSourceType());
        assertEquals("test-endpoint", dto.getEndpoint());
        assertEquals("test-query", dto.getQuery());
        assertEquals("test-collection", dto.getCollection());
        assertEquals("test-method", dto.getMethod());
        assertEquals("test-cacheKey", dto.getCacheKey());
        assertEquals(42, dto.getCacheTtl());
        assertEquals(42, dto.getTimeout());
    }

    @Test
    void testSettersAndGetters() {
        Widget.DataSource dto = new Widget.DataSource();
        dto.setSourceType("val-sourceType");
        dto.setEndpoint("val-endpoint");
        dto.setQuery("val-query");
        dto.setCollection("val-collection");
        dto.setMethod("val-method");
        dto.setCacheKey("val-cacheKey");
        dto.setCacheTtl(99);
        dto.setTimeout(99);
        assertEquals("val-sourceType", dto.getSourceType());
        assertEquals("val-endpoint", dto.getEndpoint());
        assertEquals("val-query", dto.getQuery());
        assertEquals("val-collection", dto.getCollection());
        assertEquals("val-method", dto.getMethod());
        assertEquals("val-cacheKey", dto.getCacheKey());
        assertEquals(99, dto.getCacheTtl());
        assertEquals(99, dto.getTimeout());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.DataSource dto1 = Widget.DataSource.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .method("test-method")
            .headers(Collections.emptyMap())
            .body(Collections.emptyMap())
            .cacheKey("test-cacheKey")
            .cacheTtl(42)
            .timeout(42)
            .build();
        Widget.DataSource dto2 = Widget.DataSource.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .method("test-method")
            .headers(Collections.emptyMap())
            .body(Collections.emptyMap())
            .cacheKey("test-cacheKey")
            .cacheTtl(42)
            .timeout(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Widget.DataSource dto = Widget.DataSource.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .method("test-method")
            .headers(Collections.emptyMap())
            .body(Collections.emptyMap())
            .cacheKey("test-cacheKey")
            .cacheTtl(42)
            .timeout(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}