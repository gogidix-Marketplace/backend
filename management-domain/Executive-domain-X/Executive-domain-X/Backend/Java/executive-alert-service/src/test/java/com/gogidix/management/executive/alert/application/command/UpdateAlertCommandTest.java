package com.gogidix.management.executive.alert.application.command;

import com.gogidix.management.executive.alert.application.command.UpdateAlertCommand;
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
class UpdateAlertCommandTest {

        @Test
    void testBuilder() {
        UpdateAlertCommand dto = UpdateAlertCommand.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateAlertCommand.AlertStatus.DRAFT)
            .build();
        assertNotNull(dto);
        assertEquals("test-alertId", dto.getAlertId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-layout", dto.getLayout());
        assertEquals(UpdateAlertCommand.AlertStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        UpdateAlertCommand dto = new UpdateAlertCommand();
        dto.setAlertId("val-alertId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setLayout("val-layout");
        dto.setStatus(UpdateAlertCommand.AlertStatus.DRAFT);
        assertEquals("val-alertId", dto.getAlertId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-layout", dto.getLayout());
        assertEquals(UpdateAlertCommand.AlertStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateAlertCommand dto1 = UpdateAlertCommand.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateAlertCommand.AlertStatus.DRAFT)
            .build();
        UpdateAlertCommand dto2 = UpdateAlertCommand.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateAlertCommand.AlertStatus.DRAFT)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateAlertCommand dto = UpdateAlertCommand.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateAlertCommand.AlertStatus.DRAFT)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}