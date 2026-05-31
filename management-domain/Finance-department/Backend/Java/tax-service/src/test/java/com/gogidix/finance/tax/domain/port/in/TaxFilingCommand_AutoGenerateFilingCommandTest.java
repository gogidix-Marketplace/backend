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
class TaxFilingCommand_AutoGenerateFilingCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingCommand.AutoGenerateFilingCommand dto = new TaxFilingCommand.AutoGenerateFilingCommand();
        dto.setTenantId("val-tenantId");
        dto.setGeneratedBy("val-generatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingCommand.AutoGenerateFilingCommand dto1 = new TaxFilingCommand.AutoGenerateFilingCommand();
        TaxFilingCommand.AutoGenerateFilingCommand dto2 = new TaxFilingCommand.AutoGenerateFilingCommand();
        dto1.setTenantId("test");
        dto1.setFilingPeriod(null);
        dto1.setJurisdiction(null);
        dto1.setTaxType(null);
        dto1.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto1.setGeneratedBy("test");
        dto2.setTenantId("test");
        dto2.setFilingPeriod(null);
        dto2.setJurisdiction(null);
        dto2.setTaxType(null);
        dto2.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto2.setGeneratedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingCommand.AutoGenerateFilingCommand dto = new TaxFilingCommand.AutoGenerateFilingCommand();
        dto.setTenantId("test");
        dto.setFilingPeriod(null);
        dto.setJurisdiction(null);
        dto.setTaxType(null);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto.setGeneratedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingCommand.AutoGenerateFilingCommand dto = new TaxFilingCommand.AutoGenerateFilingCommand();
        dto.setTenantId("test");
        dto.setFilingPeriod(null);
        dto.setJurisdiction(null);
        dto.setTaxType(null);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto.setGeneratedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}