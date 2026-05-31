package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxCalculationController;
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
class TaxCalculationController_AddExemptionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationController.AddExemptionRequestDto dto = new TaxCalculationController.AddExemptionRequestDto();
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
        TaxCalculationController.AddExemptionRequestDto dto1 = new TaxCalculationController.AddExemptionRequestDto();
        TaxCalculationController.AddExemptionRequestDto dto2 = new TaxCalculationController.AddExemptionRequestDto();
        dto1.setExemptionCode("test");
        dto1.setExemptionType("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setReason("test");
        dto1.setCertificateNumber("test");
        dto1.setCertificateExpiry(LocalDate.of(2025,1,1));
        dto2.setExemptionCode("test");
        dto2.setExemptionType("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setReason("test");
        dto2.setCertificateNumber("test");
        dto2.setCertificateExpiry(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setExemptionCode(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationController.AddExemptionRequestDto dto = new TaxCalculationController.AddExemptionRequestDto();
        dto.setExemptionCode("test");
        dto.setExemptionType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setReason("test");
        dto.setCertificateNumber("test");
        dto.setCertificateExpiry(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationController.AddExemptionRequestDto dto = new TaxCalculationController.AddExemptionRequestDto();
        dto.setExemptionCode("test");
        dto.setExemptionType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setReason("test");
        dto.setCertificateNumber("test");
        dto.setCertificateExpiry(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}