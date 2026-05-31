package com.gogidix.management.executive.financial.application.command;

import com.gogidix.management.executive.financial.application.command.UpdateFinancialDataCommand;
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
class UpdateFinancialDataCommandTest {

        @Test
    void testBuilder() {
        UpdateFinancialDataCommand dto = UpdateFinancialDataCommand.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateFinancialDataCommand.FinancialDataStatus.DRAFT)
            .build();
        assertNotNull(dto);
        assertEquals("test-financialDataId", dto.getFinancialDataId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-layout", dto.getLayout());
        assertEquals(UpdateFinancialDataCommand.FinancialDataStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        UpdateFinancialDataCommand dto = new UpdateFinancialDataCommand();
        dto.setFinancialDataId("val-financialDataId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setLayout("val-layout");
        dto.setStatus(UpdateFinancialDataCommand.FinancialDataStatus.DRAFT);
        assertEquals("val-financialDataId", dto.getFinancialDataId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-layout", dto.getLayout());
        assertEquals(UpdateFinancialDataCommand.FinancialDataStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateFinancialDataCommand dto1 = UpdateFinancialDataCommand.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateFinancialDataCommand.FinancialDataStatus.DRAFT)
            .build();
        UpdateFinancialDataCommand dto2 = UpdateFinancialDataCommand.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateFinancialDataCommand.FinancialDataStatus.DRAFT)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateFinancialDataCommand dto = UpdateFinancialDataCommand.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateFinancialDataCommand.FinancialDataStatus.DRAFT)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}