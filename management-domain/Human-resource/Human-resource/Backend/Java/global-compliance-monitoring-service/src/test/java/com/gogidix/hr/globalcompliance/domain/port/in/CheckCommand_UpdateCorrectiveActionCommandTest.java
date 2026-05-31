package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.CheckCommand;
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
class CheckCommand_UpdateCorrectiveActionCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.UpdateCorrectiveActionCommand dto = new CheckCommand.UpdateCorrectiveActionCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setCorrectiveAction("val-correctiveAction");
        dto.setActualCompletionDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-correctiveAction", dto.getCorrectiveAction());
        assertEquals(LocalDate.of(2025,6,1), dto.getActualCompletionDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.UpdateCorrectiveActionCommand dto1 = new CheckCommand.UpdateCorrectiveActionCommand();
        CheckCommand.UpdateCorrectiveActionCommand dto2 = new CheckCommand.UpdateCorrectiveActionCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setCorrectiveAction("test");
        dto1.setActualCompletionDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setCorrectiveAction("test");
        dto2.setActualCompletionDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.UpdateCorrectiveActionCommand dto = new CheckCommand.UpdateCorrectiveActionCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setCorrectiveAction("test");
        dto.setActualCompletionDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.UpdateCorrectiveActionCommand dto = new CheckCommand.UpdateCorrectiveActionCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setCorrectiveAction("test");
        dto.setActualCompletionDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}