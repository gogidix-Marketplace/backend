package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.interfaces.rest.CustomerController;
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
class CustomerController_AssignOwnerRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CustomerController.AssignOwnerRequestDto dto = new CustomerController.AssignOwnerRequestDto();
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setTerritory("val-territory");
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-territory", dto.getTerritory());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerController.AssignOwnerRequestDto dto1 = new CustomerController.AssignOwnerRequestDto();
        CustomerController.AssignOwnerRequestDto dto2 = new CustomerController.AssignOwnerRequestDto();
        dto1.setOwnerId("test");
        dto1.setOwnerName("test");
        dto1.setTerritory("test");
        dto2.setOwnerId("test");
        dto2.setOwnerName("test");
        dto2.setTerritory("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setOwnerId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerController.AssignOwnerRequestDto dto = new CustomerController.AssignOwnerRequestDto();
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setTerritory("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerController.AssignOwnerRequestDto dto = new CustomerController.AssignOwnerRequestDto();
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setTerritory("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}