package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.domain.model.TaxConfiguration;
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
class TaxConfiguration_CreditTest {

        @Test
    void testBuilder() {
        TaxConfiguration.Credit dto = TaxConfiguration.Credit.builder()
                        .creditCode("test-creditCode")
            .creditName("test-creditName")
            .amount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .isRefundable(true)
            .carryForwardRules("test-carryForwardRules")
            .build();
        assertNotNull(dto);
        assertEquals("test-creditCode", dto.getCreditCode());
        assertEquals("test-creditName", dto.getCreditName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-eligibilityCriteria", dto.getEligibilityCriteria());
        assertTrue(dto.getIsRefundable());
        assertEquals("test-carryForwardRules", dto.getCarryForwardRules());
    }

    @Test
    void testSettersAndGetters() {
        TaxConfiguration.Credit dto = new TaxConfiguration.Credit();
        dto.setCreditCode("val-creditCode");
        dto.setCreditName("val-creditName");
        dto.setAmount(BigDecimal.ONE);
        dto.setEligibilityCriteria("val-eligibilityCriteria");
        dto.setIsRefundable(true);
        dto.setCarryForwardRules("val-carryForwardRules");
        assertEquals("val-creditCode", dto.getCreditCode());
        assertEquals("val-creditName", dto.getCreditName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-eligibilityCriteria", dto.getEligibilityCriteria());
        assertTrue(dto.getIsRefundable());
        assertEquals("val-carryForwardRules", dto.getCarryForwardRules());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxConfiguration.Credit dto1 = TaxConfiguration.Credit.builder()
                        .creditCode("test-creditCode")
            .creditName("test-creditName")
            .amount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .isRefundable(true)
            .carryForwardRules("test-carryForwardRules")
            .build();
        TaxConfiguration.Credit dto2 = TaxConfiguration.Credit.builder()
                        .creditCode("test-creditCode")
            .creditName("test-creditName")
            .amount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .isRefundable(true)
            .carryForwardRules("test-carryForwardRules")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxConfiguration.Credit dto = TaxConfiguration.Credit.builder()
                        .creditCode("test-creditCode")
            .creditName("test-creditName")
            .amount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .isRefundable(true)
            .carryForwardRules("test-carryForwardRules")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}