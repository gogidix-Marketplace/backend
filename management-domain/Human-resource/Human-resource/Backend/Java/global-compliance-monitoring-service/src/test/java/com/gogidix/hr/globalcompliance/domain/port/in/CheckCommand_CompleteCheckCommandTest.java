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
class CheckCommand_CompleteCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.CompleteCheckCommand dto = new CheckCommand.CompleteCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setResult("val-result");
        dto.setFindings("val-findings");
        dto.setCorrectiveAction("val-correctiveAction");
        dto.setTargetCompletionDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-result", dto.getResult());
        assertEquals("val-findings", dto.getFindings());
        assertEquals("val-correctiveAction", dto.getCorrectiveAction());
        assertEquals(LocalDate.of(2025,6,1), dto.getTargetCompletionDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.CompleteCheckCommand dto1 = new CheckCommand.CompleteCheckCommand();
        CheckCommand.CompleteCheckCommand dto2 = new CheckCommand.CompleteCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setResult("test");
        dto1.setFindings("test");
        dto1.setCorrectiveAction("test");
        dto1.setTargetCompletionDate(LocalDate.of(2025,1,1));
        dto1.setSupportingDocuments(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setResult("test");
        dto2.setFindings("test");
        dto2.setCorrectiveAction("test");
        dto2.setTargetCompletionDate(LocalDate.of(2025,1,1));
        dto2.setSupportingDocuments(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.CompleteCheckCommand dto = new CheckCommand.CompleteCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setResult("test");
        dto.setFindings("test");
        dto.setCorrectiveAction("test");
        dto.setTargetCompletionDate(LocalDate.of(2025,1,1));
        dto.setSupportingDocuments(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.CompleteCheckCommand dto = new CheckCommand.CompleteCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setResult("test");
        dto.setFindings("test");
        dto.setCorrectiveAction("test");
        dto.setTargetCompletionDate(LocalDate.of(2025,1,1));
        dto.setSupportingDocuments(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}