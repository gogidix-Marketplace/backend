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
class DealController_AddTeamMemberRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        DealController.AddTeamMemberRequestDto dto = new DealController.AddTeamMemberRequestDto();
        dto.setUserId("val-userId");
        assertEquals("val-userId", dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        DealController.AddTeamMemberRequestDto dto1 = new DealController.AddTeamMemberRequestDto();
        DealController.AddTeamMemberRequestDto dto2 = new DealController.AddTeamMemberRequestDto();
        dto1.setUserId("test");
        dto2.setUserId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setUserId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealController.AddTeamMemberRequestDto dto = new DealController.AddTeamMemberRequestDto();
        dto.setUserId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealController.AddTeamMemberRequestDto dto = new DealController.AddTeamMemberRequestDto();
        dto.setUserId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}