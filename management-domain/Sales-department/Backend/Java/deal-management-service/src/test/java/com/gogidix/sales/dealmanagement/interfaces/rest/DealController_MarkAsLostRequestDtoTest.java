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
class DealController_MarkAsLostRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        DealController.MarkAsLostRequestDto dto = new DealController.MarkAsLostRequestDto();
        dto.setLossReason("val-lossReason");
        dto.setLossDetails("val-lossDetails");
        assertEquals("val-lossReason", dto.getLossReason());
        assertEquals("val-lossDetails", dto.getLossDetails());
    }

    @Test
    void testEqualsAndHashCode() {
        DealController.MarkAsLostRequestDto dto1 = new DealController.MarkAsLostRequestDto();
        DealController.MarkAsLostRequestDto dto2 = new DealController.MarkAsLostRequestDto();
        dto1.setLossReason("test");
        dto1.setLossDetails("test");
        dto2.setLossReason("test");
        dto2.setLossDetails("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setLossReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealController.MarkAsLostRequestDto dto = new DealController.MarkAsLostRequestDto();
        dto.setLossReason("test");
        dto.setLossDetails("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealController.MarkAsLostRequestDto dto = new DealController.MarkAsLostRequestDto();
        dto.setLossReason("test");
        dto.setLossDetails("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}