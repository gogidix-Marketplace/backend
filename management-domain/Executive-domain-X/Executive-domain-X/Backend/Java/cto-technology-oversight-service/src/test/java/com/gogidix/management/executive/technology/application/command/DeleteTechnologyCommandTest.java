package com.gogidix.management.executive.technology.application.command;

import com.gogidix.management.executive.technology.application.command.DeleteTechnologyCommand;
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
class DeleteTechnologyCommandTest {

        @Test
    void testBuilder() {
        DeleteTechnologyCommand dto = DeleteTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-technologyId", dto.getTechnologyId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        DeleteTechnologyCommand dto = new DeleteTechnologyCommand();
        dto.setTechnologyId("val-technologyId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-technologyId", dto.getTechnologyId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        DeleteTechnologyCommand dto1 = DeleteTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        DeleteTechnologyCommand dto2 = DeleteTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DeleteTechnologyCommand dto = DeleteTechnologyCommand.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}