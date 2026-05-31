package com.gogidix.customersupport.customerportal.application.dto;

import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileRequestDto;
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
class CustomerProfileRequestDto_AddressDtoTest {

        @Test
    void testBuilder() {
        CustomerProfileRequestDto.AddressDto dto = CustomerProfileRequestDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .build();
        assertNotNull(dto);
        assertEquals("test-street", dto.getStreet());
        assertEquals("test-city", dto.getCity());
        assertEquals("test-state", dto.getState());
        assertEquals("test-postalCode", dto.getPostalCode());
        assertEquals("test-country", dto.getCountry());
    }

    @Test
    void testSettersAndGetters() {
        CustomerProfileRequestDto.AddressDto dto = new CustomerProfileRequestDto.AddressDto();
        dto.setStreet("val-street");
        dto.setCity("val-city");
        dto.setState("val-state");
        dto.setPostalCode("val-postalCode");
        dto.setCountry("val-country");
        assertEquals("val-street", dto.getStreet());
        assertEquals("val-city", dto.getCity());
        assertEquals("val-state", dto.getState());
        assertEquals("val-postalCode", dto.getPostalCode());
        assertEquals("val-country", dto.getCountry());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerProfileRequestDto.AddressDto dto1 = CustomerProfileRequestDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .build();
        CustomerProfileRequestDto.AddressDto dto2 = CustomerProfileRequestDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CustomerProfileRequestDto.AddressDto dto = CustomerProfileRequestDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}