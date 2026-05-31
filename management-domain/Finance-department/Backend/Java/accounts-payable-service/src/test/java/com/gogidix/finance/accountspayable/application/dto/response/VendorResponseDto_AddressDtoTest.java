package com.gogidix.finance.accountspayable.application.dto.response;

import com.gogidix.finance.accountspayable.application.dto.response.VendorResponseDto;
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
class VendorResponseDto_AddressDtoTest {

        @Test
    void testBuilder() {
        VendorResponseDto.AddressDto dto = VendorResponseDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .addressLine1("test-addressLine1")
            .addressLine2("test-addressLine2")
            .build();
        assertNotNull(dto);
        assertEquals("test-street", dto.getStreet());
        assertEquals("test-city", dto.getCity());
        assertEquals("test-state", dto.getState());
        assertEquals("test-postalCode", dto.getPostalCode());
        assertEquals("test-country", dto.getCountry());
        assertEquals("test-addressLine1", dto.getAddressLine1());
        assertEquals("test-addressLine2", dto.getAddressLine2());
    }

    @Test
    void testSettersAndGetters() {
        VendorResponseDto.AddressDto dto = new VendorResponseDto.AddressDto();
        dto.setStreet("val-street");
        dto.setCity("val-city");
        dto.setState("val-state");
        dto.setPostalCode("val-postalCode");
        dto.setCountry("val-country");
        dto.setAddressLine1("val-addressLine1");
        dto.setAddressLine2("val-addressLine2");
        assertEquals("val-street", dto.getStreet());
        assertEquals("val-city", dto.getCity());
        assertEquals("val-state", dto.getState());
        assertEquals("val-postalCode", dto.getPostalCode());
        assertEquals("val-country", dto.getCountry());
        assertEquals("val-addressLine1", dto.getAddressLine1());
        assertEquals("val-addressLine2", dto.getAddressLine2());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorResponseDto.AddressDto dto1 = VendorResponseDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .addressLine1("test-addressLine1")
            .addressLine2("test-addressLine2")
            .build();
        VendorResponseDto.AddressDto dto2 = VendorResponseDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .addressLine1("test-addressLine1")
            .addressLine2("test-addressLine2")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        VendorResponseDto.AddressDto dto = VendorResponseDto.AddressDto.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .addressLine1("test-addressLine1")
            .addressLine2("test-addressLine2")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}