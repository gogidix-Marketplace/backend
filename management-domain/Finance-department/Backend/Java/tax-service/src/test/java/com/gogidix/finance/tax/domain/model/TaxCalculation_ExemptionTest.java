package com.gogidix.finance.tax.domain.model;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
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
class TaxCalculation_ExemptionTest {

        @Test
    void testBuilder() {
        TaxCalculation.Exemption dto = TaxCalculation.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionType("test-exemptionType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .certificateNumber("test-certificateNumber")
            .certificateExpiry(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-exemptionCode", dto.getExemptionCode());
        assertEquals("test-exemptionType", dto.getExemptionType());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-certificateNumber", dto.getCertificateNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getCertificateExpiry());
    }

    @Test
    void testSettersAndGetters() {
        TaxCalculation.Exemption dto = new TaxCalculation.Exemption();
        dto.setExemptionCode("val-exemptionCode");
        dto.setExemptionType("val-exemptionType");
        dto.setAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        dto.setCertificateNumber("val-certificateNumber");
        dto.setCertificateExpiry(LocalDate.of(2025,6,1));
        assertEquals("val-exemptionCode", dto.getExemptionCode());
        assertEquals("val-exemptionType", dto.getExemptionType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-certificateNumber", dto.getCertificateNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getCertificateExpiry());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculation.Exemption dto1 = TaxCalculation.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionType("test-exemptionType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .certificateNumber("test-certificateNumber")
            .certificateExpiry(LocalDate.of(2025,1,15))
            .build();
        TaxCalculation.Exemption dto2 = TaxCalculation.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionType("test-exemptionType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .certificateNumber("test-certificateNumber")
            .certificateExpiry(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxCalculation.Exemption dto = TaxCalculation.Exemption.builder()
                        .exemptionCode("test-exemptionCode")
            .exemptionType("test-exemptionType")
            .amount(BigDecimal.TEN)
            .reason("test-reason")
            .certificateNumber("test-certificateNumber")
            .certificateExpiry(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}