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
class ConsolidationCommand_UpdateConsolidationRuleCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.UpdateConsolidationRuleCommand dto = new ConsolidationCommand.UpdateConsolidationRuleCommand();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setRuleName("val-ruleName");
        dto.setDescription("val-description");
        dto.setCurrencyCode("val-currencyCode");
        dto.setExchangeRate(BigDecimal.ONE);
        dto.setExchangeRateSource("val-exchangeRateSource");
        dto.setActive(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-ruleName", dto.getRuleName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-currencyCode", dto.getCurrencyCode());
        assertEquals(BigDecimal.ONE, dto.getExchangeRate());
        assertEquals("val-exchangeRateSource", dto.getExchangeRateSource());
        assertTrue(dto.getActive());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.UpdateConsolidationRuleCommand dto1 = new ConsolidationCommand.UpdateConsolidationRuleCommand();
        ConsolidationCommand.UpdateConsolidationRuleCommand dto2 = new ConsolidationCommand.UpdateConsolidationRuleCommand();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto1.setRuleName("test");
        dto1.setDescription("test");
        dto1.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto1.setCurrencyCode("test");
        dto1.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto1.setExchangeRate(BigDecimal.TEN);
        dto1.setExchangeRateSource("test");
        dto1.setActive(true);
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        dto2.setRuleName("test");
        dto2.setDescription("test");
        dto2.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto2.setCurrencyCode("test");
        dto2.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto2.setExchangeRate(BigDecimal.TEN);
        dto2.setExchangeRateSource("test");
        dto2.setActive(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.UpdateConsolidationRuleCommand dto = new ConsolidationCommand.UpdateConsolidationRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setRuleName("test");
        dto.setDescription("test");
        dto.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto.setCurrencyCode("test");
        dto.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto.setExchangeRate(BigDecimal.TEN);
        dto.setExchangeRateSource("test");
        dto.setActive(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.UpdateConsolidationRuleCommand dto = new ConsolidationCommand.UpdateConsolidationRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setRuleName("test");
        dto.setDescription("test");
        dto.setConsolidationMethod(ConsolidationRule.ConsolidationMethod.FULL_CONSOLIDATION);
        dto.setCurrencyCode("test");
        dto.setConversionMethod(ConsolidationRule.CurrencyConversionMethod.CLOSING_RATE);
        dto.setExchangeRate(BigDecimal.TEN);
        dto.setExchangeRateSource("test");
        dto.setActive(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}