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
class TaxConfiguration_ExemptionTest {

        @Test
    void testBuilder() {
        TaxConfiguration.Exemption dto = TaxConfiguration.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionName("test-exemptionName")
            .maxAmount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .requiresDocumentation(true)
            .applicationProcess("test-applicationProcess")
            .build();
        assertNotNull(dto);
        assertEquals("test-exemptionCode", dto.getExemptionCode());
        assertEquals("test-exemptionName", dto.getExemptionName());
        assertEquals(BigDecimal.TEN, dto.getMaxAmount());
        assertEquals("test-eligibilityCriteria", dto.getEligibilityCriteria());
        assertTrue(dto.getRequiresDocumentation());
        assertEquals("test-applicationProcess", dto.getApplicationProcess());
    }

    @Test
    void testSettersAndGetters() {
        TaxConfiguration.Exemption dto = new TaxConfiguration.Exemption();
        dto.setExemptionCode("val-exemptionCode");
        dto.setExemptionName("val-exemptionName");
        dto.setMaxAmount(BigDecimal.ONE);
        dto.setEligibilityCriteria("val-eligibilityCriteria");
        dto.setRequiresDocumentation(true);
        dto.setApplicationProcess("val-applicationProcess");
        assertEquals("val-exemptionCode", dto.getExemptionCode());
        assertEquals("val-exemptionName", dto.getExemptionName());
        assertEquals(BigDecimal.ONE, dto.getMaxAmount());
        assertEquals("val-eligibilityCriteria", dto.getEligibilityCriteria());
        assertTrue(dto.getRequiresDocumentation());
        assertEquals("val-applicationProcess", dto.getApplicationProcess());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxConfiguration.Exemption dto1 = TaxConfiguration.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionName("test-exemptionName")
            .maxAmount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .requiresDocumentation(true)
            .applicationProcess("test-applicationProcess")
            .build();
        TaxConfiguration.Exemption dto2 = TaxConfiguration.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionName("test-exemptionName")
            .maxAmount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .requiresDocumentation(true)
            .applicationProcess("test-applicationProcess")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxConfiguration.Exemption dto = TaxConfiguration.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionName("test-exemptionName")
            .maxAmount(BigDecimal.TEN)
            .eligibilityCriteria("test-eligibilityCriteria")
            .requiresDocumentation(true)
            .applicationProcess("test-applicationProcess")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}