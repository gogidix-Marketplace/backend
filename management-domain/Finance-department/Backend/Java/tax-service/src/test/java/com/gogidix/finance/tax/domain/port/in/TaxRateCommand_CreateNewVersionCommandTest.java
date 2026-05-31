package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxRateCommand;
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
class TaxRateCommand_CreateNewVersionCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxRateCommand.CreateNewVersionCommand dto = new TaxRateCommand.CreateNewVersionCommand();
        dto.setTenantId("val-tenantId");
        dto.setTaxRateId("val-taxRateId");
        dto.setNewRate(BigDecimal.ONE);
        dto.setNewEffectiveDate(LocalDate.of(2025,6,1));
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-taxRateId", dto.getTaxRateId());
        assertEquals(BigDecimal.ONE, dto.getNewRate());
        assertEquals(LocalDate.of(2025,6,1), dto.getNewEffectiveDate());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateCommand.CreateNewVersionCommand dto1 = new TaxRateCommand.CreateNewVersionCommand();
        TaxRateCommand.CreateNewVersionCommand dto2 = new TaxRateCommand.CreateNewVersionCommand();
        dto1.setTenantId("test");
        dto1.setTaxRateId("test");
        dto1.setNewRate(BigDecimal.TEN);
        dto1.setNewEffectiveDate(LocalDate.of(2025,1,1));
        dto1.setCreatedBy("test");
        dto2.setTenantId("test");
        dto2.setTaxRateId("test");
        dto2.setNewRate(BigDecimal.TEN);
        dto2.setNewEffectiveDate(LocalDate.of(2025,1,1));
        dto2.setCreatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateCommand.CreateNewVersionCommand dto = new TaxRateCommand.CreateNewVersionCommand();
        dto.setTenantId("test");
        dto.setTaxRateId("test");
        dto.setNewRate(BigDecimal.TEN);
        dto.setNewEffectiveDate(LocalDate.of(2025,1,1));
        dto.setCreatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateCommand.CreateNewVersionCommand dto = new TaxRateCommand.CreateNewVersionCommand();
        dto.setTenantId("test");
        dto.setTaxRateId("test");
        dto.setNewRate(BigDecimal.TEN);
        dto.setNewEffectiveDate(LocalDate.of(2025,1,1));
        dto.setCreatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}