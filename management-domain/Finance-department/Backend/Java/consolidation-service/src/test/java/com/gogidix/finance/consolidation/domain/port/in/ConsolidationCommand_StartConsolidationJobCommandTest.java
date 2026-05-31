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
class ConsolidationCommand_StartConsolidationJobCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.StartConsolidationJobCommand dto = new ConsolidationCommand.StartConsolidationJobCommand();
        dto.setTenantId("val-tenantId");
        dto.setJobName("val-jobName");
        dto.setDescription("val-description");
        dto.setInitiatedBy("val-initiatedBy");
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setRuleId("val-ruleId");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setCorrelationId("val-correlationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jobName", dto.getJobName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-initiatedBy", dto.getInitiatedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-correlationId", dto.getCorrelationId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.StartConsolidationJobCommand dto1 = new ConsolidationCommand.StartConsolidationJobCommand();
        ConsolidationCommand.StartConsolidationJobCommand dto2 = new ConsolidationCommand.StartConsolidationJobCommand();
        dto1.setTenantId("test");
        dto1.setJobName("test");
        dto1.setDescription("test");
        dto1.setJobType(null);
        dto1.setInitiatedBy("test");
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setRuleId("test");
        dto1.setBaseCurrency("test");
        dto1.setIncludedSubsidiaries(Collections.emptyList());
        dto1.setIncludedDepartments(Collections.emptyList());
        dto1.setIncludedCostCenters(Collections.emptyList());
        dto1.setParameters(Collections.emptyMap());
        dto1.setCorrelationId("test");
        dto2.setTenantId("test");
        dto2.setJobName("test");
        dto2.setDescription("test");
        dto2.setJobType(null);
        dto2.setInitiatedBy("test");
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setRuleId("test");
        dto2.setBaseCurrency("test");
        dto2.setIncludedSubsidiaries(Collections.emptyList());
        dto2.setIncludedDepartments(Collections.emptyList());
        dto2.setIncludedCostCenters(Collections.emptyList());
        dto2.setParameters(Collections.emptyMap());
        dto2.setCorrelationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.StartConsolidationJobCommand dto = new ConsolidationCommand.StartConsolidationJobCommand();
        dto.setTenantId("test");
        dto.setJobName("test");
        dto.setDescription("test");
        dto.setJobType(null);
        dto.setInitiatedBy("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setRuleId("test");
        dto.setBaseCurrency("test");
        dto.setIncludedSubsidiaries(Collections.emptyList());
        dto.setIncludedDepartments(Collections.emptyList());
        dto.setIncludedCostCenters(Collections.emptyList());
        dto.setParameters(Collections.emptyMap());
        dto.setCorrelationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.StartConsolidationJobCommand dto = new ConsolidationCommand.StartConsolidationJobCommand();
        dto.setTenantId("test");
        dto.setJobName("test");
        dto.setDescription("test");
        dto.setJobType(null);
        dto.setInitiatedBy("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setRuleId("test");
        dto.setBaseCurrency("test");
        dto.setIncludedSubsidiaries(Collections.emptyList());
        dto.setIncludedDepartments(Collections.emptyList());
        dto.setIncludedCostCenters(Collections.emptyList());
        dto.setParameters(Collections.emptyMap());
        dto.setCorrelationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}