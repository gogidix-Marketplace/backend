package com.gogidix.hr.globalworkforceanalytics.domain.model;

import com.gogidix.hr.globalworkforceanalytics.domain.model.TrendAnalysis;
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
class TrendAnalysis_TrendDataPointTest {

        @Test
    void testSettersAndGetters() {
        TrendAnalysis.TrendDataPoint dto = new TrendAnalysis.TrendDataPoint();
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setValue(BigDecimal.ONE);
        dto.setChangeFromPrevious(BigDecimal.ONE);
        dto.setChangePercentage(BigDecimal.ONE);
        dto.setIsAnomaly(true);
        dto.setNotes("val-notes");
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals(BigDecimal.ONE, dto.getChangeFromPrevious());
        assertEquals(BigDecimal.ONE, dto.getChangePercentage());
        assertTrue(dto.getIsAnomaly());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TrendAnalysis.TrendDataPoint dto1 = new TrendAnalysis.TrendDataPoint();
        TrendAnalysis.TrendDataPoint dto2 = new TrendAnalysis.TrendDataPoint();
        dto1.setPeriod(null);
        dto1.setDate(LocalDate.of(2025,1,1));
        dto1.setValue(BigDecimal.TEN);
        dto1.setChangeFromPrevious(BigDecimal.TEN);
        dto1.setChangePercentage(BigDecimal.TEN);
        dto1.setIsAnomaly(true);
        dto1.setNotes("test");
        dto1.setAttributes(Collections.emptyMap());
        dto2.setPeriod(null);
        dto2.setDate(LocalDate.of(2025,1,1));
        dto2.setValue(BigDecimal.TEN);
        dto2.setChangeFromPrevious(BigDecimal.TEN);
        dto2.setChangePercentage(BigDecimal.TEN);
        dto2.setIsAnomaly(true);
        dto2.setNotes("test");
        dto2.setAttributes(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TrendAnalysis.TrendDataPoint dto = new TrendAnalysis.TrendDataPoint();
        dto.setPeriod(null);
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setValue(BigDecimal.TEN);
        dto.setChangeFromPrevious(BigDecimal.TEN);
        dto.setChangePercentage(BigDecimal.TEN);
        dto.setIsAnomaly(true);
        dto.setNotes("test");
        dto.setAttributes(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TrendAnalysis.TrendDataPoint dto = new TrendAnalysis.TrendDataPoint();
        dto.setPeriod(null);
        dto.setDate(LocalDate.of(2025,1,1));
        dto.setValue(BigDecimal.TEN);
        dto.setChangeFromPrevious(BigDecimal.TEN);
        dto.setChangePercentage(BigDecimal.TEN);
        dto.setIsAnomaly(true);
        dto.setNotes("test");
        dto.setAttributes(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}