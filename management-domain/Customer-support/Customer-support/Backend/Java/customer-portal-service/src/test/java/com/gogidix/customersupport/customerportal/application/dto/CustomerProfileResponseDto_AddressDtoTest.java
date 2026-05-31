package com.gogidix.customersupport.customerportal.application.dto;

import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileResponseDto;
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
class CustomerProfileResponseDto_AddressDtoTest {

        @Test
    void testBuilder() {
        CustomerProfileResponseDto.AddressDto dto = CustomerProfileResponseDto.AddressDto.builder()
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
        CustomerProfileResponseDto.AddressDto dto = new CustomerProfileResponseDto.AddressDto();
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
        CustomerProfileResponseDto.AddressDto dto1 = CustomerProfileResponseDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .build();
        CustomerProfileResponseDto.AddressDto dto2 = CustomerProfileResponseDto.AddressDto.builder()
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
        CustomerProfileResponseDto.AddressDto dto = CustomerProfileResponseDto.AddressDto.builder()
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