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
class CustomerController_AddTagRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CustomerController.AddTagRequestDto dto = new CustomerController.AddTagRequestDto();
        dto.setTag("val-tag");
        assertEquals("val-tag", dto.getTag());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerController.AddTagRequestDto dto1 = new CustomerController.AddTagRequestDto();
        CustomerController.AddTagRequestDto dto2 = new CustomerController.AddTagRequestDto();
        dto1.setTag("test");
        dto2.setTag("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTag(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerController.AddTagRequestDto dto = new CustomerController.AddTagRequestDto();
        dto.setTag("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerController.AddTagRequestDto dto = new CustomerController.AddTagRequestDto();
        dto.setTag("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}