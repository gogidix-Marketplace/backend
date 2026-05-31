package com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto;

import com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto.IngestionJobRequestDto;
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
class IngestionJobRequestDtoTest {

        @Test
    void testBuilder() {
        IngestionJobRequestDto dto = IngestionJobRequestDto.builder()
                        .tenantId("test-tenantId")
            .topic("test-topic")
            .source("test-source")
            .status("test-status")
            .recordCount("test-recordCount")
            .errorCount("test-errorCount")
            .lastProcessedOffset("test-lastProcessedOffset")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-topic", dto.getTopic());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-recordCount", dto.getRecordCount());
        assertEquals("test-errorCount", dto.getErrorCount());
        assertEquals("test-lastProcessedOffset", dto.getLastProcessedOffset());
    }

    @Test
    void testSettersAndGetters() {
        IngestionJobRequestDto dto = new IngestionJobRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setTopic("val-topic");
        dto.setSource("val-source");
        dto.setStatus("val-status");
        dto.setRecordCount("val-recordCount");
        dto.setErrorCount("val-errorCount");
        dto.setLastProcessedOffset("val-lastProcessedOffset");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-topic", dto.getTopic());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-recordCount", dto.getRecordCount());
        assertEquals("val-errorCount", dto.getErrorCount());
        assertEquals("val-lastProcessedOffset", dto.getLastProcessedOffset());
    }

    @Test
    void testEqualsAndHashCode() {
        IngestionJobRequestDto dto1 = IngestionJobRequestDto.builder()
                        .tenantId("test-tenantId")
            .topic("test-topic")
            .source("test-source")
            .status("test-status")
            .recordCount("test-recordCount")
            .errorCount("test-errorCount")
            .lastProcessedOffset("test-lastProcessedOffset")
            .build();
        IngestionJobRequestDto dto2 = IngestionJobRequestDto.builder()
                        .tenantId("test-tenantId")
            .topic("test-topic")
            .source("test-source")
            .status("test-status")
            .recordCount("test-recordCount")
            .errorCount("test-errorCount")
            .lastProcessedOffset("test-lastProcessedOffset")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        IngestionJobRequestDto dto = IngestionJobRequestDto.builder()
                        .tenantId("test-tenantId")
            .topic("test-topic")
            .source("test-source")
            .status("test-status")
            .recordCount("test-recordCount")
            .errorCount("test-errorCount")
            .lastProcessedOffset("test-lastProcessedOffset")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}