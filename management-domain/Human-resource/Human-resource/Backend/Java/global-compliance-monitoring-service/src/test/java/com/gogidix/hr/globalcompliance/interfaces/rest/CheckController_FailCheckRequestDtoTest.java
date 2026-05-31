package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.interfaces.rest.CheckController;
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
class CheckController_FailCheckRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CheckController.FailCheckRequestDto dto = new CheckController.FailCheckRequestDto();
        dto.setFindings("val-findings");
        dto.setCorrectiveAction("val-correctiveAction");
        dto.setTargetCompletionDate(LocalDate.of(2025,6,1));
        assertEquals("val-findings", dto.getFindings());
        assertEquals("val-correctiveAction", dto.getCorrectiveAction());
        assertEquals(LocalDate.of(2025,6,1), dto.getTargetCompletionDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckController.FailCheckRequestDto dto1 = new CheckController.FailCheckRequestDto();
        CheckController.FailCheckRequestDto dto2 = new CheckController.FailCheckRequestDto();
        dto1.setFindings("test");
        dto1.setCorrectiveAction("test");
        dto1.setTargetCompletionDate(LocalDate.of(2025,1,1));
        dto1.setSupportingDocuments(Collections.emptyList());
        dto2.setFindings("test");
        dto2.setCorrectiveAction("test");
        dto2.setTargetCompletionDate(LocalDate.of(2025,1,1));
        dto2.setSupportingDocuments(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFindings(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckController.FailCheckRequestDto dto = new CheckController.FailCheckRequestDto();
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
        CheckController.FailCheckRequestDto dto = new CheckController.FailCheckRequestDto();
        dto.setFindings("test");
        dto.setCorrectiveAction("test");
        dto.setTargetCompletionDate(LocalDate.of(2025,1,1));
        dto.setSupportingDocuments(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}