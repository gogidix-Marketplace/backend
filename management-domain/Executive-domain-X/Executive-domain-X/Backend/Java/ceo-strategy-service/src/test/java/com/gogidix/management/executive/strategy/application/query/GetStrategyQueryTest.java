package com.gogidix.management.executive.strategy.application.query;

import com.gogidix.management.executive.strategy.application.query.GetStrategyQuery;
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
class GetStrategyQueryTest {

        @Test
    void testBuilder() {
        GetStrategyQuery dto = GetStrategyQuery.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-strategyId", dto.getStrategyId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        GetStrategyQuery dto = new GetStrategyQuery();
        dto.setStrategyId("val-strategyId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-strategyId", dto.getStrategyId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        GetStrategyQuery dto1 = GetStrategyQuery.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        GetStrategyQuery dto2 = GetStrategyQuery.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GetStrategyQuery dto = GetStrategyQuery.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}