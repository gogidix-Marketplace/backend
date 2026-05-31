package com.gogidix.management.executive.technology.application.command;

import com.gogidix.management.executive.technology.application.command.UpdateTechnologyCommand;
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
class UpdateTechnologyCommandTest {

        @Test
    void testBuilder() {
        UpdateTechnologyCommand dto = UpdateTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateTechnologyCommand.TechnologyStatus.DRAFT)
            .build();
        assertNotNull(dto);
        assertEquals("test-technologyId", dto.getTechnologyId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-layout", dto.getLayout());
        assertEquals(UpdateTechnologyCommand.TechnologyStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        UpdateTechnologyCommand dto = new UpdateTechnologyCommand();
        dto.setTechnologyId("val-technologyId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setLayout("val-layout");
        dto.setStatus(UpdateTechnologyCommand.TechnologyStatus.DRAFT);
        assertEquals("val-technologyId", dto.getTechnologyId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-layout", dto.getLayout());
        assertEquals(UpdateTechnologyCommand.TechnologyStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateTechnologyCommand dto1 = UpdateTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateTechnologyCommand.TechnologyStatus.DRAFT)
            .build();
        UpdateTechnologyCommand dto2 = UpdateTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateTechnologyCommand.TechnologyStatus.DRAFT)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateTechnologyCommand dto = UpdateTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateTechnologyCommand.TechnologyStatus.DRAFT)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}