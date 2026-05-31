package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.port.in.TaxFilingCommand;
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
class TaxFilingCommand_CreateFilingCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingCommand.CreateFilingCommand dto = new TaxFilingCommand.CreateFilingCommand();
        dto.setTenantId("val-tenantId");
        dto.setCurrency("val-currency");
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setSubmittedBy("val-submittedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals("val-submittedBy", dto.getSubmittedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingCommand.CreateFilingCommand dto1 = new TaxFilingCommand.CreateFilingCommand();
        TaxFilingCommand.CreateFilingCommand dto2 = new TaxFilingCommand.CreateFilingCommand();
        dto1.setTenantId("test");
        dto1.setFilingPeriod(null);
        dto1.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto1.setJurisdiction(null);
        dto1.setTaxType(null);
        dto1.setCurrency("test");
        dto1.setDueDate(LocalDate.of(2025,1,1));
        dto1.setSubmittedBy("test");
        dto2.setTenantId("test");
        dto2.setFilingPeriod(null);
        dto2.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto2.setJurisdiction(null);
        dto2.setTaxType(null);
        dto2.setCurrency("test");
        dto2.setDueDate(LocalDate.of(2025,1,1));
        dto2.setSubmittedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingCommand.CreateFilingCommand dto = new TaxFilingCommand.CreateFilingCommand();
        dto.setTenantId("test");
        dto.setFilingPeriod(null);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto.setJurisdiction(null);
        dto.setTaxType(null);
        dto.setCurrency("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setSubmittedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingCommand.CreateFilingCommand dto = new TaxFilingCommand.CreateFilingCommand();
        dto.setTenantId("test");
        dto.setFilingPeriod(null);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto.setJurisdiction(null);
        dto.setTaxType(null);
        dto.setCurrency("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setSubmittedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}