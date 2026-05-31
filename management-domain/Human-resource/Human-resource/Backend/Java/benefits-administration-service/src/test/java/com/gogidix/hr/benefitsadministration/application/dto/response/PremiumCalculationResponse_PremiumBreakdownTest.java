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
class PremiumCalculationResponse_PremiumBreakdownTest {

        @Test
    void testBuilder() {
        PremiumCalculationResponse.PremiumBreakdown dto = PremiumCalculationResponse.PremiumBreakdown.builder()
                        .component("test-component")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .isEmployeePaid(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-component", dto.getComponent());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertTrue(dto.getIsEmployeePaid());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
    }

    @Test
    void testSettersAndGetters() {
        PremiumCalculationResponse.PremiumBreakdown dto = new PremiumCalculationResponse.PremiumBreakdown();
        dto.setComponent("val-component");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setIsEmployeePaid(true);
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        assertEquals("val-component", dto.getComponent());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertTrue(dto.getIsEmployeePaid());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
    }

    @Test
    void testEqualsAndHashCode() {
        PremiumCalculationResponse.PremiumBreakdown dto1 = PremiumCalculationResponse.PremiumBreakdown.builder()
                        .component("test-component")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .isEmployeePaid(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .build();
        PremiumCalculationResponse.PremiumBreakdown dto2 = PremiumCalculationResponse.PremiumBreakdown.builder()
                        .component("test-component")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .isEmployeePaid(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PremiumCalculationResponse.PremiumBreakdown dto = PremiumCalculationResponse.PremiumBreakdown.builder()
                        .component("test-component")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .isEmployeePaid(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}