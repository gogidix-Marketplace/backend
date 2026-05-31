package com.gogidix.sales.leadmanagement.interfaces.rest;

import com.gogidix.sales.leadmanagement.interfaces.rest.LeadController;
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
class LeadController_UpdateScoreRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LeadController.UpdateScoreRequestDto dto = new LeadController.UpdateScoreRequestDto();
        dto.setScore(99);
        assertEquals(99, dto.getScore());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadController.UpdateScoreRequestDto dto1 = new LeadController.UpdateScoreRequestDto();
        LeadController.UpdateScoreRequestDto dto2 = new LeadController.UpdateScoreRequestDto();
        dto1.setScore(42);
        dto2.setScore(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setScore(999);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadController.UpdateScoreRequestDto dto = new LeadController.UpdateScoreRequestDto();
        dto.setScore(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadController.UpdateScoreRequestDto dto = new LeadController.UpdateScoreRequestDto();
        dto.setScore(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}