package com.gogidix.customersupport.customerportal.domain.model;

import com.gogidix.customersupport.customerportal.domain.model.CustomerProfile;
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
class CustomerProfile_AddressTest {

        @Test
    void testBuilder() {
        CustomerProfile.Address dto = CustomerProfile.Address.builder()
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
        CustomerProfile.Address dto = new CustomerProfile.Address();
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
        CustomerProfile.Address dto1 = CustomerProfile.Address.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .build();
        CustomerProfile.Address dto2 = CustomerProfile.Address.builder()
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
        CustomerProfile.Address dto = CustomerProfile.Address.builder()
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