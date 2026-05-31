package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto;
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
class KPIBoardDto_DataSourceDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto.DataSourceDto dto = KPIBoardDto.DataSourceDto.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-sourceType", dto.getSourceType());
        assertEquals("test-endpoint", dto.getEndpoint());
        assertEquals("test-query", dto.getQuery());
        assertEquals("test-collection", dto.getCollection());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto.DataSourceDto dto = new KPIBoardDto.DataSourceDto();
        dto.setSourceType("val-sourceType");
        dto.setEndpoint("val-endpoint");
        dto.setQuery("val-query");
        dto.setCollection("val-collection");
        assertEquals("val-sourceType", dto.getSourceType());
        assertEquals("val-endpoint", dto.getEndpoint());
        assertEquals("val-query", dto.getQuery());
        assertEquals("val-collection", dto.getCollection());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto.DataSourceDto dto1 = KPIBoardDto.DataSourceDto.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .build();
        KPIBoardDto.DataSourceDto dto2 = KPIBoardDto.DataSourceDto.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto.DataSourceDto dto = KPIBoardDto.DataSourceDto.builder()
                        .sourceType("test-sourceType")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}