package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.BatchValidationRequestDTO;
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
class BatchValidationRequestDTOTest {

        @Test
    void testBuilder() {
        BatchValidationRequestDTO dto = BatchValidationRequestDTO.builder()
                        .batchId("test-batchId")
            .tenantId("test-tenantId")
            .priority(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(42, dto.getPriority());
    }

    @Test
    void testSettersAndGetters() {
        BatchValidationRequestDTO dto = new BatchValidationRequestDTO();
        dto.setBatchId("val-batchId");
        dto.setTenantId("val-tenantId");
        dto.setPriority(99);
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getPriority());
    }

    @Test
    void testEqualsAndHashCode() {
        BatchValidationRequestDTO dto1 = BatchValidationRequestDTO.builder()
                        .batchId("test-batchId")
            .tenantId("test-tenantId")
            .priority(42)
            .build();
        BatchValidationRequestDTO dto2 = BatchValidationRequestDTO.builder()
                        .batchId("test-batchId")
            .tenantId("test-tenantId")
            .priority(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BatchValidationRequestDTO dto = BatchValidationRequestDTO.builder()
                        .batchId("test-batchId")
            .tenantId("test-tenantId")
            .priority(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}