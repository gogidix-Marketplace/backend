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
class DealController_RejectApprovalRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        DealController.RejectApprovalRequestDto dto = new DealController.RejectApprovalRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        DealController.RejectApprovalRequestDto dto1 = new DealController.RejectApprovalRequestDto();
        DealController.RejectApprovalRequestDto dto2 = new DealController.RejectApprovalRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealController.RejectApprovalRequestDto dto = new DealController.RejectApprovalRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealController.RejectApprovalRequestDto dto = new DealController.RejectApprovalRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}