package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.domain.model.RevenueRecognitionSchedule;
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
class RevenueRecognitionSchedule_ScheduleEntryTest {

        @Test
    void testBuilder() {
        RevenueRecognitionSchedule.ScheduleEntry dto = RevenueRecognitionSchedule.ScheduleEntry.builder()
                        .entryId("test-entryId")
            .periodNumber(42)
            .recognitionDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .cumulativeAmount(BigDecimal.TEN)
            .status(RevenueRecognitionSchedule.ScheduleEntry.EntryStatus.PENDING)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-entryId", dto.getEntryId());
        assertEquals(42, dto.getPeriodNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getRecognitionDate());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getCumulativeAmount());
        assertEquals(RevenueRecognitionSchedule.ScheduleEntry.EntryStatus.PENDING, dto.getStatus());
        assertEquals(LocalDate.of(2025,1,15), dto.getRecognizedDate());
        assertEquals("test-recognizedBy", dto.getRecognizedBy());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        RevenueRecognitionSchedule.ScheduleEntry dto = new RevenueRecognitionSchedule.ScheduleEntry();
        dto.setEntryId("val-entryId");
        dto.setPeriodNumber(99);
        dto.setRecognitionDate(LocalDate.of(2025,6,1));
        dto.setAmount(BigDecimal.ONE);
        dto.setCumulativeAmount(BigDecimal.ONE);
        dto.setStatus(RevenueRecognitionSchedule.ScheduleEntry.EntryStatus.PENDING);
        dto.setRecognizedDate(LocalDate.of(2025,6,1));
        dto.setRecognizedBy("val-recognizedBy");
        dto.setNotes("val-notes");
        assertEquals("val-entryId", dto.getEntryId());
        assertEquals(99, dto.getPeriodNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getRecognitionDate());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getCumulativeAmount());
        assertEquals(RevenueRecognitionSchedule.ScheduleEntry.EntryStatus.PENDING, dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getRecognizedDate());
        assertEquals("val-recognizedBy", dto.getRecognizedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueRecognitionSchedule.ScheduleEntry dto1 = RevenueRecognitionSchedule.ScheduleEntry.builder()
                        .entryId("test-entryId")
            .periodNumber(42)
            .recognitionDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .cumulativeAmount(BigDecimal.TEN)
            .status(RevenueRecognitionSchedule.ScheduleEntry.EntryStatus.PENDING)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .notes("test-notes")
            .build();
        RevenueRecognitionSchedule.ScheduleEntry dto2 = RevenueRecognitionSchedule.ScheduleEntry.builder()
                        .entryId("test-entryId")
            .periodNumber(42)
            .recognitionDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .cumulativeAmount(BigDecimal.TEN)
            .status(RevenueRecognitionSchedule.ScheduleEntry.EntryStatus.PENDING)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueRecognitionSchedule.ScheduleEntry dto = RevenueRecognitionSchedule.ScheduleEntry.builder()
                        .entryId("test-entryId")
            .periodNumber(42)
            .recognitionDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .cumulativeAmount(BigDecimal.TEN)
            .status(RevenueRecognitionSchedule.ScheduleEntry.EntryStatus.PENDING)
            .recognizedDate(LocalDate.of(2025,1,15))
            .recognizedBy("test-recognizedBy")
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}