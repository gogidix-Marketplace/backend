package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidatedBalance;
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
class ConsolidatedBalance_BalanceComponentTest {

        @Test
    void testBuilder() {
        ConsolidatedBalance.BalanceComponent dto = ConsolidatedBalance.BalanceComponent.builder()
                        .componentId("test-componentId")
            .sourceEntityId("test-sourceEntityId")
            .sourceEntityName("test-sourceEntityName")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .type("test-type")
            .build();
        assertNotNull(dto);
        assertEquals("test-componentId", dto.getComponentId());
        assertEquals("test-sourceEntityId", dto.getSourceEntityId());
        assertEquals("test-sourceEntityName", dto.getSourceEntityName());
        assertEquals("test-accountCode", dto.getAccountCode());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getExchangeRate());
        assertEquals(BigDecimal.TEN, dto.getConvertedAmount());
        assertEquals("test-type", dto.getType());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidatedBalance.BalanceComponent dto = new ConsolidatedBalance.BalanceComponent();
        dto.setComponentId("val-componentId");
        dto.setSourceEntityId("val-sourceEntityId");
        dto.setSourceEntityName("val-sourceEntityName");
        dto.setAccountCode("val-accountCode");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setExchangeRate(BigDecimal.ONE);
        dto.setConvertedAmount(BigDecimal.ONE);
        dto.setType("val-type");
        assertEquals("val-componentId", dto.getComponentId());
        assertEquals("val-sourceEntityId", dto.getSourceEntityId());
        assertEquals("val-sourceEntityName", dto.getSourceEntityName());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getExchangeRate());
        assertEquals(BigDecimal.ONE, dto.getConvertedAmount());
        assertEquals("val-type", dto.getType());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidatedBalance.BalanceComponent dto1 = ConsolidatedBalance.BalanceComponent.builder()
                        .componentId("test-componentId")
            .sourceEntityId("test-sourceEntityId")
            .sourceEntityName("test-sourceEntityName")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .type("test-type")
            .build();
        ConsolidatedBalance.BalanceComponent dto2 = ConsolidatedBalance.BalanceComponent.builder()
                        .componentId("test-componentId")
            .sourceEntityId("test-sourceEntityId")
            .sourceEntityName("test-sourceEntityName")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .type("test-type")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidatedBalance.BalanceComponent dto = ConsolidatedBalance.BalanceComponent.builder()
                        .componentId("test-componentId")
            .sourceEntityId("test-sourceEntityId")
            .sourceEntityName("test-sourceEntityName")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .exchangeRate(BigDecimal.TEN)
            .convertedAmount(BigDecimal.TEN)
            .type("test-type")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}