package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.domain.port.in.PayrollEntryCommand;
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
class PayrollEntryCommand_UpdateTaxCommandTest {

        @Test
    void testBuilder() {
        PayrollEntryCommand.UpdateTaxCommand dto = PayrollEntryCommand.UpdateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-entryId", dto.getEntryId());
        assertEquals(BigDecimal.TEN, dto.getFederalTax());
        assertEquals(BigDecimal.TEN, dto.getStateTax());
        assertEquals(BigDecimal.TEN, dto.getLocalTax());
        assertEquals(BigDecimal.TEN, dto.getSocialSecurityTax());
        assertEquals(BigDecimal.TEN, dto.getMedicareTax());
        assertEquals(BigDecimal.TEN, dto.getOtherTaxes());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryCommand.UpdateTaxCommand dto = new PayrollEntryCommand.UpdateTaxCommand();
        dto.setTenantId("val-tenantId");
        dto.setEntryId("val-entryId");
        dto.setFederalTax(BigDecimal.ONE);
        dto.setStateTax(BigDecimal.ONE);
        dto.setLocalTax(BigDecimal.ONE);
        dto.setSocialSecurityTax(BigDecimal.ONE);
        dto.setMedicareTax(BigDecimal.ONE);
        dto.setOtherTaxes(BigDecimal.ONE);
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-entryId", dto.getEntryId());
        assertEquals(BigDecimal.ONE, dto.getFederalTax());
        assertEquals(BigDecimal.ONE, dto.getStateTax());
        assertEquals(BigDecimal.ONE, dto.getLocalTax());
        assertEquals(BigDecimal.ONE, dto.getSocialSecurityTax());
        assertEquals(BigDecimal.ONE, dto.getMedicareTax());
        assertEquals(BigDecimal.ONE, dto.getOtherTaxes());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryCommand.UpdateTaxCommand dto1 = PayrollEntryCommand.UpdateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .updatedBy("test-updatedBy")
            .build();
        PayrollEntryCommand.UpdateTaxCommand dto2 = PayrollEntryCommand.UpdateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryCommand.UpdateTaxCommand dto = PayrollEntryCommand.UpdateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}