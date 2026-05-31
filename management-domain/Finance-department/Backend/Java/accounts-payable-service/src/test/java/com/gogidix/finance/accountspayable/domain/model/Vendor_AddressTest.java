package com.gogidix.finance.accountspayable.domain.model;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
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
class Vendor_AddressTest {

        @Test
    void testBuilder() {
        Vendor.Address dto = Vendor.Address.builder()
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
        Vendor.Address dto = new Vendor.Address();
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
        Vendor.Address dto1 = Vendor.Address.builder()
                        .street("test-street")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .country("test-country")
            .addressLine1("test-addressLine1")
            .addressLine2("test-addressLine2")
            .build();
        Vendor.Address dto2 = Vendor.Address.builder()
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
        Vendor.Address dto = Vendor.Address.builder()
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