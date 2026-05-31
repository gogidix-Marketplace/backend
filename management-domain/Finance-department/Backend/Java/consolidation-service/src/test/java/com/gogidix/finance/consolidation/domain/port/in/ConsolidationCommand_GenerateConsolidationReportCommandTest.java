package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.port.in.ConsolidationCommand;
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
class ConsolidationCommand_GenerateConsolidationReportCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.GenerateConsolidationReportCommand dto = new ConsolidationCommand.GenerateConsolidationReportCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportName("val-reportName");
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setBaseCurrency("val-baseCurrency");
        dto.setGeneratedBy("val-generatedBy");
        dto.setJobId("val-jobId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportName", dto.getReportName());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
        assertEquals("val-jobId", dto.getJobId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.GenerateConsolidationReportCommand dto1 = new ConsolidationCommand.GenerateConsolidationReportCommand();
        ConsolidationCommand.GenerateConsolidationReportCommand dto2 = new ConsolidationCommand.GenerateConsolidationReportCommand();
        dto1.setTenantId("test");
        dto1.setReportName("test");
        dto1.setReportType(null);
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setBaseCurrency("test");
        dto1.setGeneratedBy("test");
        dto1.setJobId("test");
        dto1.setIncludedSubsidiaries(Collections.emptyList());
        dto1.setParameters(Collections.emptyMap());
        dto2.setTenantId("test");
        dto2.setReportName("test");
        dto2.setReportType(null);
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setBaseCurrency("test");
        dto2.setGeneratedBy("test");
        dto2.setJobId("test");
        dto2.setIncludedSubsidiaries(Collections.emptyList());
        dto2.setParameters(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.GenerateConsolidationReportCommand dto = new ConsolidationCommand.GenerateConsolidationReportCommand();
        dto.setTenantId("test");
        dto.setReportName("test");
        dto.setReportType(null);
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setGeneratedBy("test");
        dto.setJobId("test");
        dto.setIncludedSubsidiaries(Collections.emptyList());
        dto.setParameters(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.GenerateConsolidationReportCommand dto = new ConsolidationCommand.GenerateConsolidationReportCommand();
        dto.setTenantId("test");
        dto.setReportName("test");
        dto.setReportType(null);
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setGeneratedBy("test");
        dto.setJobId("test");
        dto.setIncludedSubsidiaries(Collections.emptyList());
        dto.setParameters(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}