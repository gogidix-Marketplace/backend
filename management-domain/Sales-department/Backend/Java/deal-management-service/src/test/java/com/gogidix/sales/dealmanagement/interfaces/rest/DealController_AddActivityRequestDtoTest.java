package com.gogidix.sales.dealmanagement.interfaces.rest;

import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
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
class DealController_AddActivityRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        DealController.AddActivityRequestDto dto = new DealController.AddActivityRequestDto();
        dto.setSubject("val-subject");
        dto.setDescription("val-description");
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        DealController.AddActivityRequestDto dto1 = new DealController.AddActivityRequestDto();
        DealController.AddActivityRequestDto dto2 = new DealController.AddActivityRequestDto();
        dto1.setActivityType(DealActivity.ActivityType.CALL);
        dto1.setSubject("test");
        dto1.setDescription("test");
        dto1.setDueDate(null);
        dto1.setPriority(DealActivity.Priority.LOW);
        dto2.setActivityType(DealActivity.ActivityType.CALL);
        dto2.setSubject("test");
        dto2.setDescription("test");
        dto2.setDueDate(null);
        dto2.setPriority(DealActivity.Priority.LOW);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setActivityType(DealActivity.ActivityType.EMAIL);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealController.AddActivityRequestDto dto = new DealController.AddActivityRequestDto();
        dto.setActivityType(DealActivity.ActivityType.CALL);
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setDueDate(null);
        dto.setPriority(DealActivity.Priority.LOW);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealController.AddActivityRequestDto dto = new DealController.AddActivityRequestDto();
        dto.setActivityType(DealActivity.ActivityType.CALL);
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setDueDate(null);
        dto.setPriority(DealActivity.Priority.LOW);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}