package com.gogidix.finance.revenuetracking.application.dto.response;

import com.gogidix.finance.revenuetracking.application.dto.response.RevenueRecognitionResponseDto;
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
class RevenueRecognitionResponseDto_RecognitionPeriodDtoTest {

        @Test
    void testBuilder() {
        RevenueRecognitionResponseDto.RecognitionPeriodDto dto = RevenueRecognitionResponseDto.RecognitionPeriodDto.builder()
                        .periodNumber(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .recognizedAmount(BigDecimal.TEN)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .status(RevenueRecognitionResponseDto.RecognitionPeriodDto.PeriodStatusDto.PENDING)
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getPeriodNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getRecognizedAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getRecognizedDate());
        assertEquals("test-recognizedBy", dto.getRecognizedBy());
        assertEquals(RevenueRecognitionResponseDto.RecognitionPeriodDto.PeriodStatusDto.PENDING, dto.getStatus());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        RevenueRecognitionResponseDto.RecognitionPeriodDto dto = new RevenueRecognitionResponseDto.RecognitionPeriodDto();
        dto.setPeriodNumber(99);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setAmount(BigDecimal.ONE);
        dto.setRecognizedAmount(BigDecimal.ONE);
        dto.setRecognizedDate(LocalDate.of(2025,6,1));
        dto.setRecognizedBy("val-recognizedBy");
        dto.setStatus(RevenueRecognitionResponseDto.RecognitionPeriodDto.PeriodStatusDto.PENDING);
        dto.setNotes("val-notes");
        assertEquals(99, dto.getPeriodNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getRecognizedAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getRecognizedDate());
        assertEquals("val-recognizedBy", dto.getRecognizedBy());
        assertEquals(RevenueRecognitionResponseDto.RecognitionPeriodDto.PeriodStatusDto.PENDING, dto.getStatus());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueRecognitionResponseDto.RecognitionPeriodDto dto1 = RevenueRecognitionResponseDto.RecognitionPeriodDto.builder()
                        .periodNumber(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .recognizedAmount(BigDecimal.TEN)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .status(RevenueRecognitionResponseDto.RecognitionPeriodDto.PeriodStatusDto.PENDING)
            .notes("test-notes")
            .build();
        RevenueRecognitionResponseDto.RecognitionPeriodDto dto2 = RevenueRecognitionResponseDto.RecognitionPeriodDto.builder()
                        .periodNumber(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .recognizedAmount(BigDecimal.TEN)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .status(RevenueRecognitionResponseDto.RecognitionPeriodDto.PeriodStatusDto.PENDING)
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueRecognitionResponseDto.RecognitionPeriodDto dto = RevenueRecognitionResponseDto.RecognitionPeriodDto.builder()
                        .periodNumber(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .recognizedAmount(BigDecimal.TEN)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .status(RevenueRecognitionResponseDto.RecognitionPeriodDto.PeriodStatusDto.PENDING)
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}