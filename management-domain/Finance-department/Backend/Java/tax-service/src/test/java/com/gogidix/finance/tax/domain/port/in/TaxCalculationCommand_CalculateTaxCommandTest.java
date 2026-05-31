package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
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
class TaxCalculationCommand_CalculateTaxCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationCommand.CalculateTaxCommand dto = new TaxCalculationCommand.CalculateTaxCommand();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setCurrency("val-currency");
        dto.setAmount(BigDecimal.ONE);
        dto.setCalculatedBy("val-calculatedBy");
        dto.setCategory("val-category");
        dto.setEntityCode("val-entityCode");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-calculatedBy", dto.getCalculatedBy());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-entityCode", dto.getEntityCode());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationCommand.CalculateTaxCommand dto1 = new TaxCalculationCommand.CalculateTaxCommand();
        TaxCalculationCommand.CalculateTaxCommand dto2 = new TaxCalculationCommand.CalculateTaxCommand();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto1.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto1.setTransactionDate(LocalDate.of(2025,1,1));
        dto1.setJurisdiction(null);
        dto1.setTaxType(null);
        dto1.setCurrency("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCalculatedBy("test");
        dto1.setCategory("test");
        dto1.setEntityCode("test");
        dto1.setAdditionalContext(Collections.emptyMap());
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        dto2.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto2.setTransactionDate(LocalDate.of(2025,1,1));
        dto2.setJurisdiction(null);
        dto2.setTaxType(null);
        dto2.setCurrency("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCalculatedBy("test");
        dto2.setCategory("test");
        dto2.setEntityCode("test");
        dto2.setAdditionalContext(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationCommand.CalculateTaxCommand dto = new TaxCalculationCommand.CalculateTaxCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction(null);
        dto.setTaxType(null);
        dto.setCurrency("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setCalculatedBy("test");
        dto.setCategory("test");
        dto.setEntityCode("test");
        dto.setAdditionalContext(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationCommand.CalculateTaxCommand dto = new TaxCalculationCommand.CalculateTaxCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction(null);
        dto.setTaxType(null);
        dto.setCurrency("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setCalculatedBy("test");
        dto.setCategory("test");
        dto.setEntityCode("test");
        dto.setAdditionalContext(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}