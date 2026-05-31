package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;
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
class ConsolidationCommand_CreateConsolidationRuleCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.CreateConsolidationRuleCommand dto = new ConsolidationCommand.CreateConsolidationRuleCommand();
        dto.setTenantId("val-tenantId");
        dto.setRuleName("val-ruleName");
        dto.setDescription("val-description");
        dto.setCreatedBy("val-createdBy");
        dto.setEffectiveFrom(LocalDate.of(2025,6,1));
        dto.setEffectiveTo(LocalDate.of(2025,6,1));
        dto.setCurrencyCode("val-currencyCode");
        dto.setEnableIntercompanyElimination(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleName", dto.getRuleName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveFrom());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveTo());
        assertEquals("val-currencyCode", dto.getCurrencyCode());
        assertTrue(dto.getEnableIntercompanyElimination());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.CreateConsolidationRuleCommand dto1 = new ConsolidationCommand.CreateConsolidationRuleCommand();
        ConsolidationCommand.CreateConsolidationRuleCommand dto2 = new ConsolidationCommand.CreateConsolidationRuleCommand();
        dto1.setTenantId("test");
        dto1.setRuleName("test");
        dto1.setDescription("test");
        dto1.setRuleType(ConsolidationRule.RuleType.FINANCIAL_STATEMENT);
        dto1.setRuleScope(ConsolidationRule.RuleScope.GLOBAL);
        dto1.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto1.setCreatedBy("test");
        dto1.setApplicableDepartments(Collections.emptyList());
        dto1.setApplicableCostCenters(Collections.emptyList());
        dto1.setApplicableSubsidiaries(Collections.emptyList());
        dto1.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto1.setEffectiveTo(LocalDate.of(2025,1,1));
        dto1.setCurrencyCode("test");
        dto1.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto1.setEnableIntercompanyElimination(true);
        dto1.setEliminationRule(null);
        dto1.setAdjustmentRules(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setRuleName("test");
        dto2.setDescription("test");
        dto2.setRuleType(ConsolidationRule.RuleType.FINANCIAL_STATEMENT);
        dto2.setRuleScope(ConsolidationRule.RuleScope.GLOBAL);
        dto2.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto2.setCreatedBy("test");
        dto2.setApplicableDepartments(Collections.emptyList());
        dto2.setApplicableCostCenters(Collections.emptyList());
        dto2.setApplicableSubsidiaries(Collections.emptyList());
        dto2.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto2.setEffectiveTo(LocalDate.of(2025,1,1));
        dto2.setCurrencyCode("test");
        dto2.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto2.setEnableIntercompanyElimination(true);
        dto2.setEliminationRule(null);
        dto2.setAdjustmentRules(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.CreateConsolidationRuleCommand dto = new ConsolidationCommand.CreateConsolidationRuleCommand();
        dto.setTenantId("test");
        dto.setRuleName("test");
        dto.setDescription("test");
        dto.setRuleType(ConsolidationRule.RuleType.FINANCIAL_STATEMENT);
        dto.setRuleScope(ConsolidationRule.RuleScope.GLOBAL);
        dto.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto.setCreatedBy("test");
        dto.setApplicableDepartments(Collections.emptyList());
        dto.setApplicableCostCenters(Collections.emptyList());
        dto.setApplicableSubsidiaries(Collections.emptyList());
        dto.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto.setEffectiveTo(LocalDate.of(2025,1,1));
        dto.setCurrencyCode("test");
        dto.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto.setEnableIntercompanyElimination(true);
        dto.setEliminationRule(null);
        dto.setAdjustmentRules(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.CreateConsolidationRuleCommand dto = new ConsolidationCommand.CreateConsolidationRuleCommand();
        dto.setTenantId("test");
        dto.setRuleName("test");
        dto.setDescription("test");
        dto.setRuleType(ConsolidationRule.RuleType.FINANCIAL_STATEMENT);
        dto.setRuleScope(ConsolidationRule.RuleScope.GLOBAL);
        dto.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto.setCreatedBy("test");
        dto.setApplicableDepartments(Collections.emptyList());
        dto.setApplicableCostCenters(Collections.emptyList());
        dto.setApplicableSubsidiaries(Collections.emptyList());
        dto.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto.setEffectiveTo(LocalDate.of(2025,1,1));
        dto.setCurrencyCode("test");
        dto.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto.setEnableIntercompanyElimination(true);
        dto.setEliminationRule(null);
        dto.setAdjustmentRules(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}