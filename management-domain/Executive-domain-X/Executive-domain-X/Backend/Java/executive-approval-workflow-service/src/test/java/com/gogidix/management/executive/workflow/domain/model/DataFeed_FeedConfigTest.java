package com.gogidix.management.executive.workflow.domain.model;

import com.gogidix.management.executive.workflow.domain.model.DataFeed;
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
class DataFeed_FeedConfigTest {

        @Test
    void testBuilder() {
        DataFeed.FeedConfig dto = DataFeed.FeedConfig.builder()
                        .url("test-url")
            .method("test-method")
            .headers(Collections.emptyMap())
            .authType("test-authType")
            .authToken("test-authToken")
            .refreshInterval(42)
            .queryParams(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-url", dto.getUrl());
        assertEquals("test-method", dto.getMethod());
        assertEquals("test-authType", dto.getAuthType());
        assertEquals("test-authToken", dto.getAuthToken());
        assertEquals(42, dto.getRefreshInterval());
    }

    @Test
    void testSettersAndGetters() {
        DataFeed.FeedConfig dto = new DataFeed.FeedConfig();
        dto.setUrl("val-url");
        dto.setMethod("val-method");
        dto.setAuthType("val-authType");
        dto.setAuthToken("val-authToken");
        dto.setRefreshInterval(99);
        assertEquals("val-url", dto.getUrl());
        assertEquals("val-method", dto.getMethod());
        assertEquals("val-authType", dto.getAuthType());
        assertEquals("val-authToken", dto.getAuthToken());
        assertEquals(99, dto.getRefreshInterval());
    }

    @Test
    void testEqualsAndHashCode() {
        DataFeed.FeedConfig dto1 = DataFeed.FeedConfig.builder()
                        .url("test-url")
            .method("test-method")
            .headers(Collections.emptyMap())
            .authType("test-authType")
            .authToken("test-authToken")
            .refreshInterval(42)
            .queryParams(Collections.emptyMap())
            .build();
        DataFeed.FeedConfig dto2 = DataFeed.FeedConfig.builder()
                        .url("test-url")
            .method("test-method")
            .headers(Collections.emptyMap())
            .authType("test-authType")
            .authToken("test-authToken")
            .refreshInterval(42)
            .queryParams(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DataFeed.FeedConfig dto = DataFeed.FeedConfig.builder()
                        .url("test-url")
            .method("test-method")
            .headers(Collections.emptyMap())
            .authType("test-authType")
            .authToken("test-authToken")
            .refreshInterval(42)
            .queryParams(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}