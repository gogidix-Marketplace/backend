package com.gogidix.management.executive.strategy.application.command;

import com.gogidix.management.executive.strategy.application.command.DeleteStrategyCommand;
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
class DeleteStrategyCommandTest {

        @Test
    void testBuilder() {
        DeleteStrategyCommand dto = DeleteStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-strategyId", dto.getStrategyId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        DeleteStrategyCommand dto = new DeleteStrategyCommand();
        dto.setStrategyId("val-strategyId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-strategyId", dto.getStrategyId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        DeleteStrategyCommand dto1 = DeleteStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        DeleteStrategyCommand dto2 = DeleteStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DeleteStrategyCommand dto = DeleteStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}