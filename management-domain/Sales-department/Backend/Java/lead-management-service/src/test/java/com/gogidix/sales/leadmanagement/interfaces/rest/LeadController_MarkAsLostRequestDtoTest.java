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
class LeadController_MarkAsLostRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LeadController.MarkAsLostRequestDto dto = new LeadController.MarkAsLostRequestDto();
        dto.setLossReason("val-lossReason");
        dto.setLossDetails("val-lossDetails");
        assertEquals("val-lossReason", dto.getLossReason());
        assertEquals("val-lossDetails", dto.getLossDetails());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadController.MarkAsLostRequestDto dto1 = new LeadController.MarkAsLostRequestDto();
        LeadController.MarkAsLostRequestDto dto2 = new LeadController.MarkAsLostRequestDto();
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
        LeadController.MarkAsLostRequestDto dto = new LeadController.MarkAsLostRequestDto();
        dto.setLossReason("test");
        dto.setLossDetails("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadController.MarkAsLostRequestDto dto = new LeadController.MarkAsLostRequestDto();
        dto.setLossReason("test");
        dto.setLossDetails("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}