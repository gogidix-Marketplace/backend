package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxCalculationCommand;
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
class TaxCalculationCommand_AddExemptionCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationCommand.AddExemptionCommand dto = new TaxCalculationCommand.AddExemptionCommand();
        dto.setTenantId("val-tenantId");
        dto.setCalculationId("val-calculationId");
        dto.setExemptionCode("val-exemptionCode");
        dto.setExemptionType("val-exemptionType");
        dto.setAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        dto.setCertificateNumber("val-certificateNumber");
        dto.setCertificateExpiry(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-calculationId", dto.getCalculationId());
        assertEquals("val-exemptionCode", dto.getExemptionCode());
        assertEquals("val-exemptionType", dto.getExemptionType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-certificateNumber", dto.getCertificateNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getCertificateExpiry());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationCommand.AddExemptionCommand dto1 = new TaxCalculationCommand.AddExemptionCommand();
        TaxCalculationCommand.AddExemptionCommand dto2 = new TaxCalculationCommand.AddExemptionCommand();
        dto1.setTenantId("test");
        dto1.setCalculationId("test");
        dto1.setExemptionCode("test");
        dto1.setExemptionType("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setReason("test");
        dto1.setCertificateNumber("test");
        dto1.setCertificateExpiry(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setCalculationId("test");
        dto2.setExemptionCode("test");
        dto2.setExemptionType("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setReason("test");
        dto2.setCertificateNumber("test");
        dto2.setCertificateExpiry(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationCommand.AddExemptionCommand dto = new TaxCalculationCommand.AddExemptionCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
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
        TaxCalculationCommand.AddExemptionCommand dto = new TaxCalculationCommand.AddExemptionCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
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