package com.gogidix.sales.dealmanagement.interfaces.rest;

import com.gogidix.sales.dealmanagement.interfaces.rest.DealController;
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
class DealController_MarkAsWonRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        DealController.MarkAsWonRequestDto dto = new DealController.MarkAsWonRequestDto();
        dto.setFinalAmount(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals(BigDecimal.ONE, dto.getFinalAmount());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        DealController.MarkAsWonRequestDto dto1 = new DealController.MarkAsWonRequestDto();
        DealController.MarkAsWonRequestDto dto2 = new DealController.MarkAsWonRequestDto();
        dto1.setFinalAmount(BigDecimal.TEN);
        dto1.setNotes("test");
        dto2.setFinalAmount(BigDecimal.TEN);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFinalAmount(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealController.MarkAsWonRequestDto dto = new DealController.MarkAsWonRequestDto();
        dto.setFinalAmount(BigDecimal.TEN);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealController.MarkAsWonRequestDto dto = new DealController.MarkAsWonRequestDto();
        dto.setFinalAmount(BigDecimal.TEN);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}