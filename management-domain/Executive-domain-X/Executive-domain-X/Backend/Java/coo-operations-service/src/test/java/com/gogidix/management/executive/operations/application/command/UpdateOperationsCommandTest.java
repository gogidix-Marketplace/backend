package com.gogidix.management.executive.operations.application.command;

import com.gogidix.management.executive.operations.application.command.UpdateOperationsCommand;
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
class UpdateOperationsCommandTest {

        @Test
    void testBuilder() {
        UpdateOperationsCommand dto = UpdateOperationsCommand.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateOperationsCommand.OperationsStatus.DRAFT)
            .build();
        assertNotNull(dto);
        assertEquals("test-operationsId", dto.getOperationsId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-layout", dto.getLayout());
        assertEquals(UpdateOperationsCommand.OperationsStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        UpdateOperationsCommand dto = new UpdateOperationsCommand();
        dto.setOperationsId("val-operationsId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setLayout("val-layout");
        dto.setStatus(UpdateOperationsCommand.OperationsStatus.DRAFT);
        assertEquals("val-operationsId", dto.getOperationsId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-layout", dto.getLayout());
        assertEquals(UpdateOperationsCommand.OperationsStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateOperationsCommand dto1 = UpdateOperationsCommand.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateOperationsCommand.OperationsStatus.DRAFT)
            .build();
        UpdateOperationsCommand dto2 = UpdateOperationsCommand.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateOperationsCommand.OperationsStatus.DRAFT)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateOperationsCommand dto = UpdateOperationsCommand.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateOperationsCommand.OperationsStatus.DRAFT)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}