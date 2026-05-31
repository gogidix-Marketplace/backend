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
class LeadController_ConvertLeadRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LeadController.ConvertLeadRequestDto dto = new LeadController.ConvertLeadRequestDto();
        dto.setDealId("val-dealId");
        dto.setReason("val-reason");
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadController.ConvertLeadRequestDto dto1 = new LeadController.ConvertLeadRequestDto();
        LeadController.ConvertLeadRequestDto dto2 = new LeadController.ConvertLeadRequestDto();
        dto1.setDealId("test");
        dto1.setReason("test");
        dto2.setDealId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setDealId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadController.ConvertLeadRequestDto dto = new LeadController.ConvertLeadRequestDto();
        dto.setDealId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadController.ConvertLeadRequestDto dto = new LeadController.ConvertLeadRequestDto();
        dto.setDealId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}