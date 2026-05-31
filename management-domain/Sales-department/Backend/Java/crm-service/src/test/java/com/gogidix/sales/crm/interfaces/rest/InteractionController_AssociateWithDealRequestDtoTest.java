package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.interfaces.rest.InteractionController;
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
class InteractionController_AssociateWithDealRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        InteractionController.AssociateWithDealRequestDto dto = new InteractionController.AssociateWithDealRequestDto();
        dto.setDealId("val-dealId");
        assertEquals("val-dealId", dto.getDealId());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionController.AssociateWithDealRequestDto dto1 = new InteractionController.AssociateWithDealRequestDto();
        InteractionController.AssociateWithDealRequestDto dto2 = new InteractionController.AssociateWithDealRequestDto();
        dto1.setDealId("test");
        dto1.setDealValue(null);
        dto2.setDealId("test");
        dto2.setDealValue(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setDealId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionController.AssociateWithDealRequestDto dto = new InteractionController.AssociateWithDealRequestDto();
        dto.setDealId("test");
        dto.setDealValue(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionController.AssociateWithDealRequestDto dto = new InteractionController.AssociateWithDealRequestDto();
        dto.setDealId("test");
        dto.setDealValue(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}