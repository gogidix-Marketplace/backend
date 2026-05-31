package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.PremiumCalculationResponse;
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
class PremiumCalculationResponse_CalculationMetadataTest {

        @Test
    void testBuilder() {
        PremiumCalculationResponse.CalculationMetadata dto = PremiumCalculationResponse.CalculationMetadata.builder()
                        .calculationMethod("test-calculationMethod")
            .calculationDate(LocalDate.of(2025,1,15))
            .isEstimate(true)
            .factors(Collections.emptyList())
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-calculationMethod", dto.getCalculationMethod());
        assertEquals(LocalDate.of(2025,1,15), dto.getCalculationDate());
        assertTrue(dto.getIsEstimate());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        PremiumCalculationResponse.CalculationMetadata dto = new PremiumCalculationResponse.CalculationMetadata();
        dto.setCalculationMethod("val-calculationMethod");
        dto.setCalculationDate(LocalDate.of(2025,6,1));
        dto.setIsEstimate(true);
        dto.setNotes("val-notes");
        assertEquals("val-calculationMethod", dto.getCalculationMethod());
        assertEquals(LocalDate.of(2025,6,1), dto.getCalculationDate());
        assertTrue(dto.getIsEstimate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        PremiumCalculationResponse.CalculationMetadata dto1 = PremiumCalculationResponse.CalculationMetadata.builder()
                        .calculationMethod("test-calculationMethod")
            .calculationDate(LocalDate.of(2025,1,15))
            .isEstimate(true)
            .factors(Collections.emptyList())
            .notes("test-notes")
            .build();
        PremiumCalculationResponse.CalculationMetadata dto2 = PremiumCalculationResponse.CalculationMetadata.builder()
                        .calculationMethod("test-calculationMethod")
            .calculationDate(LocalDate.of(2025,1,15))
            .isEstimate(true)
            .factors(Collections.emptyList())
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PremiumCalculationResponse.CalculationMetadata dto = PremiumCalculationResponse.CalculationMetadata.builder()
                        .calculationMethod("test-calculationMethod")
            .calculationDate(LocalDate.of(2025,1,15))
            .isEstimate(true)
            .factors(Collections.emptyList())
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}