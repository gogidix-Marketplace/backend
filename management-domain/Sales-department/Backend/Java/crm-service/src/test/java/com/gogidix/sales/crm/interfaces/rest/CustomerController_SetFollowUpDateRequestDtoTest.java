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
class CustomerController_SetFollowUpDateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CustomerController.SetFollowUpDateRequestDto dto = new CustomerController.SetFollowUpDateRequestDto();
        dto.setFollowUpDate(LocalDate.of(2025,6,1));
        assertEquals(LocalDate.of(2025,6,1), dto.getFollowUpDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerController.SetFollowUpDateRequestDto dto1 = new CustomerController.SetFollowUpDateRequestDto();
        CustomerController.SetFollowUpDateRequestDto dto2 = new CustomerController.SetFollowUpDateRequestDto();
        dto1.setFollowUpDate(LocalDate.of(2025,1,1));
        dto2.setFollowUpDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFollowUpDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerController.SetFollowUpDateRequestDto dto = new CustomerController.SetFollowUpDateRequestDto();
        dto.setFollowUpDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerController.SetFollowUpDateRequestDto dto = new CustomerController.SetFollowUpDateRequestDto();
        dto.setFollowUpDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}