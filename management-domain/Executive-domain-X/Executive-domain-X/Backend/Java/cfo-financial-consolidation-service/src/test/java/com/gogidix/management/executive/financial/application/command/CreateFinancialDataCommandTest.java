package com.gogidix.management.executive.financial.application.command;

import com.gogidix.management.executive.financial.application.command.CreateFinancialDataCommand;
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
class CreateFinancialDataCommandTest {

        @Test
    void testBuilder() {
        CreateFinancialDataCommand dto = CreateFinancialDataCommand.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .layout("test-layout")
            .widgets(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals("test-layout", dto.getLayout());
    }

    @Test
    void testSettersAndGetters() {
        CreateFinancialDataCommand dto = new CreateFinancialDataCommand();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setOwnerId("val-ownerId");
        dto.setLayout("val-layout");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-layout", dto.getLayout());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateFinancialDataCommand dto1 = CreateFinancialDataCommand.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .layout("test-layout")
            .widgets(Collections.emptyList())
            .build();
        CreateFinancialDataCommand dto2 = CreateFinancialDataCommand.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .layout("test-layout")
            .widgets(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreateFinancialDataCommand dto = CreateFinancialDataCommand.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .layout("test-layout")
            .widgets(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}