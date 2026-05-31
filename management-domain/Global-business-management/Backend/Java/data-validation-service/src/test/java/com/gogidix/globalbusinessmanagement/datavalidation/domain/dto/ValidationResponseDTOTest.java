package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationResponseDTO;
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
class ValidationResponseDTOTest {

        @Test
    void testBuilder() {
        ValidationResponseDTO dto = ValidationResponseDTO.builder()
                        .validationId("test-validationId")
            .success(true)
            .passed(true)
            .totalRules(42)
            .passedRules(42)
            .failedRules(42)
            .results(Collections.emptyList())
            .message("test-message")
            .executionTimeMs(42L)
            .build();
        assertNotNull(dto);
        assertEquals("test-validationId", dto.getValidationId());
        assertTrue(dto.isSuccess());
        assertTrue(dto.isPassed());
        assertEquals(42, dto.getTotalRules());
        assertEquals(42, dto.getPassedRules());
        assertEquals(42, dto.getFailedRules());
        assertEquals("test-message", dto.getMessage());
        assertEquals(42L, dto.getExecutionTimeMs());
    }

    @Test
    void testSettersAndGetters() {
        ValidationResponseDTO dto = new ValidationResponseDTO();
        dto.setValidationId("val-validationId");
        dto.setSuccess(true);
        dto.setPassed(true);
        dto.setTotalRules(99);
        dto.setPassedRules(99);
        dto.setFailedRules(99);
        dto.setMessage("val-message");
        assertEquals("val-validationId", dto.getValidationId());
        assertTrue(dto.isSuccess());
        assertTrue(dto.isPassed());
        assertEquals(99, dto.getTotalRules());
        assertEquals(99, dto.getPassedRules());
        assertEquals(99, dto.getFailedRules());
        assertEquals("val-message", dto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ValidationResponseDTO dto1 = ValidationResponseDTO.builder()
                        .validationId("test-validationId")
            .success(true)
            .passed(true)
            .totalRules(42)
            .passedRules(42)
            .failedRules(42)
            .results(Collections.emptyList())
            .message("test-message")
            .executionTimeMs(42L)
            .build();
        ValidationResponseDTO dto2 = ValidationResponseDTO.builder()
                        .validationId("test-validationId")
            .success(true)
            .passed(true)
            .totalRules(42)
            .passedRules(42)
            .failedRules(42)
            .results(Collections.emptyList())
            .message("test-message")
            .executionTimeMs(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ValidationResponseDTO dto = ValidationResponseDTO.builder()
                        .validationId("test-validationId")
            .success(true)
            .passed(true)
            .totalRules(42)
            .passedRules(42)
            .failedRules(42)
            .results(Collections.emptyList())
            .message("test-message")
            .executionTimeMs(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}