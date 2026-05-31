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
class CustomerController_SetParentAccountRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CustomerController.SetParentAccountRequestDto dto = new CustomerController.SetParentAccountRequestDto();
        dto.setParentAccountId("val-parentAccountId");
        assertEquals("val-parentAccountId", dto.getParentAccountId());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerController.SetParentAccountRequestDto dto1 = new CustomerController.SetParentAccountRequestDto();
        CustomerController.SetParentAccountRequestDto dto2 = new CustomerController.SetParentAccountRequestDto();
        dto1.setParentAccountId("test");
        dto2.setParentAccountId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setParentAccountId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerController.SetParentAccountRequestDto dto = new CustomerController.SetParentAccountRequestDto();
        dto.setParentAccountId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerController.SetParentAccountRequestDto dto = new CustomerController.SetParentAccountRequestDto();
        dto.setParentAccountId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}