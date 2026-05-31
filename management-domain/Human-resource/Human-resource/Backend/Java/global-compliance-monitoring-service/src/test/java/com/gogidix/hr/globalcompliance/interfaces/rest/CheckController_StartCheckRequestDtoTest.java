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
class CheckController_StartCheckRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CheckController.StartCheckRequestDto dto = new CheckController.StartCheckRequestDto();
        dto.setCheckedByName("val-checkedByName");
        assertEquals("val-checkedByName", dto.getCheckedByName());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckController.StartCheckRequestDto dto1 = new CheckController.StartCheckRequestDto();
        CheckController.StartCheckRequestDto dto2 = new CheckController.StartCheckRequestDto();
        dto1.setCheckedByName("test");
        dto2.setCheckedByName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCheckedByName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckController.StartCheckRequestDto dto = new CheckController.StartCheckRequestDto();
        dto.setCheckedByName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckController.StartCheckRequestDto dto = new CheckController.StartCheckRequestDto();
        dto.setCheckedByName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}