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
class CheckController_UpdateCheckRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CheckController.UpdateCheckRequestDto dto = new CheckController.UpdateCheckRequestDto();
        dto.setCorrectiveAction("val-correctiveAction");
        dto.setActualCompletionDate(LocalDate.of(2025,6,1));
        dto.setComment("val-comment");
        assertEquals("val-correctiveAction", dto.getCorrectiveAction());
        assertEquals(LocalDate.of(2025,6,1), dto.getActualCompletionDate());
        assertEquals("val-comment", dto.getComment());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckController.UpdateCheckRequestDto dto1 = new CheckController.UpdateCheckRequestDto();
        CheckController.UpdateCheckRequestDto dto2 = new CheckController.UpdateCheckRequestDto();
        dto1.setCorrectiveAction("test");
        dto1.setActualCompletionDate(LocalDate.of(2025,1,1));
        dto1.setComment("test");
        dto2.setCorrectiveAction("test");
        dto2.setActualCompletionDate(LocalDate.of(2025,1,1));
        dto2.setComment("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCorrectiveAction(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckController.UpdateCheckRequestDto dto = new CheckController.UpdateCheckRequestDto();
        dto.setCorrectiveAction("test");
        dto.setActualCompletionDate(LocalDate.of(2025,1,1));
        dto.setComment("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckController.UpdateCheckRequestDto dto = new CheckController.UpdateCheckRequestDto();
        dto.setCorrectiveAction("test");
        dto.setActualCompletionDate(LocalDate.of(2025,1,1));
        dto.setComment("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}