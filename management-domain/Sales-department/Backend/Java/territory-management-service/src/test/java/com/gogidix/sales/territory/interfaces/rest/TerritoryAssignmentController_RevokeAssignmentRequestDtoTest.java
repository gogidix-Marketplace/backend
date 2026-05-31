package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.interfaces.rest.TerritoryAssignmentController;
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
class TerritoryAssignmentController_RevokeAssignmentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentController.RevokeAssignmentRequestDto dto = new TerritoryAssignmentController.RevokeAssignmentRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentController.RevokeAssignmentRequestDto dto1 = new TerritoryAssignmentController.RevokeAssignmentRequestDto();
        TerritoryAssignmentController.RevokeAssignmentRequestDto dto2 = new TerritoryAssignmentController.RevokeAssignmentRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentController.RevokeAssignmentRequestDto dto = new TerritoryAssignmentController.RevokeAssignmentRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryAssignmentController.RevokeAssignmentRequestDto dto = new TerritoryAssignmentController.RevokeAssignmentRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}