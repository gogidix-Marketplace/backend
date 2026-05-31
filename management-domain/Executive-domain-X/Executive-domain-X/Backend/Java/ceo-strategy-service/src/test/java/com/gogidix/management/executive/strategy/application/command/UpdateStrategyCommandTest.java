package com.gogidix.management.executive.strategy.application.command;

import com.gogidix.management.executive.strategy.application.command.UpdateStrategyCommand;
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
class UpdateStrategyCommandTest {

        @Test
    void testBuilder() {
        UpdateStrategyCommand dto = UpdateStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateStrategyCommand.StrategyStatus.DRAFT)
            .build();
        assertNotNull(dto);
        assertEquals("test-strategyId", dto.getStrategyId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-layout", dto.getLayout());
        assertEquals(UpdateStrategyCommand.StrategyStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        UpdateStrategyCommand dto = new UpdateStrategyCommand();
        dto.setStrategyId("val-strategyId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setLayout("val-layout");
        dto.setStatus(UpdateStrategyCommand.StrategyStatus.DRAFT);
        assertEquals("val-strategyId", dto.getStrategyId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-layout", dto.getLayout());
        assertEquals(UpdateStrategyCommand.StrategyStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateStrategyCommand dto1 = UpdateStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateStrategyCommand.StrategyStatus.DRAFT)
            .build();
        UpdateStrategyCommand dto2 = UpdateStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateStrategyCommand.StrategyStatus.DRAFT)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateStrategyCommand dto = UpdateStrategyCommand.builder()
                        .strategyId("test-strategyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateStrategyCommand.StrategyStatus.DRAFT)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}