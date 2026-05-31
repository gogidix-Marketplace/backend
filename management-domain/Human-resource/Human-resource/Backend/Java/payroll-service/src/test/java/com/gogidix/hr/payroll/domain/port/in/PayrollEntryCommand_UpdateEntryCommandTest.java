package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
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
class PayrollEntryCommand_UpdateEntryCommandTest {

        @Test
    void testBuilder() {
        PayrollEntryCommand.UpdateEntryCommand dto = PayrollEntryCommand.UpdateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-entryId", dto.getEntryId());
        assertEquals(BigDecimal.TEN, dto.getBasicSalary());
        assertEquals(BigDecimal.TEN, dto.getOvertimeHours());
        assertEquals(BigDecimal.TEN, dto.getOvertimeRate());
        assertEquals(BigDecimal.TEN, dto.getBonus());
        assertEquals(BigDecimal.TEN, dto.getCommission());
        assertEquals(BigDecimal.TEN, dto.getAllowances());
        assertEquals(BigDecimal.TEN, dto.getHealthInsurance());
        assertEquals(BigDecimal.TEN, dto.getDentalInsurance());
        assertEquals(BigDecimal.TEN, dto.getRetirement401k());
        assertEquals("test-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("test-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryCommand.UpdateEntryCommand dto = new PayrollEntryCommand.UpdateEntryCommand();
        dto.setTenantId("val-tenantId");
        dto.setEntryId("val-entryId");
        dto.setBasicSalary(BigDecimal.ONE);
        dto.setOvertimeHours(BigDecimal.ONE);
        dto.setOvertimeRate(BigDecimal.ONE);
        dto.setBonus(BigDecimal.ONE);
        dto.setCommission(BigDecimal.ONE);
        dto.setAllowances(BigDecimal.ONE);
        dto.setHealthInsurance(BigDecimal.ONE);
        dto.setDentalInsurance(BigDecimal.ONE);
        dto.setRetirement401k(BigDecimal.ONE);
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setNotes("val-notes");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-entryId", dto.getEntryId());
        assertEquals(BigDecimal.ONE, dto.getBasicSalary());
        assertEquals(BigDecimal.ONE, dto.getOvertimeHours());
        assertEquals(BigDecimal.ONE, dto.getOvertimeRate());
        assertEquals(BigDecimal.ONE, dto.getBonus());
        assertEquals(BigDecimal.ONE, dto.getCommission());
        assertEquals(BigDecimal.ONE, dto.getAllowances());
        assertEquals(BigDecimal.ONE, dto.getHealthInsurance());
        assertEquals(BigDecimal.ONE, dto.getDentalInsurance());
        assertEquals(BigDecimal.ONE, dto.getRetirement401k());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryCommand.UpdateEntryCommand dto1 = PayrollEntryCommand.UpdateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        PayrollEntryCommand.UpdateEntryCommand dto2 = PayrollEntryCommand.UpdateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryCommand.UpdateEntryCommand dto = PayrollEntryCommand.UpdateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}