package com.gogidix.customersupport.phonesupport.domain.model;

import com.gogidix.customersupport.phonesupport.domain.model.PhoneCall;
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
class PhoneCall_CallerInfoTest {

        @Test
    void testBuilder() {
        PhoneCall.CallerInfo dto = PhoneCall.CallerInfo.builder()
                        .phoneNumber("test-phoneNumber")
            .name("test-name")
            .email("test-email")
            .customerId("test-customerId")
            .accountNumber("test-accountNumber")
            .location("test-location")
            .callerId("test-callerId")
            .build();
        assertNotNull(dto);
        assertEquals("test-phoneNumber", dto.getPhoneNumber());
        assertEquals("test-name", dto.getName());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-location", dto.getLocation());
        assertEquals("test-callerId", dto.getCallerId());
    }

    @Test
    void testSettersAndGetters() {
        PhoneCall.CallerInfo dto = new PhoneCall.CallerInfo();
        dto.setPhoneNumber("val-phoneNumber");
        dto.setName("val-name");
        dto.setEmail("val-email");
        dto.setCustomerId("val-customerId");
        dto.setAccountNumber("val-accountNumber");
        dto.setLocation("val-location");
        dto.setCallerId("val-callerId");
        assertEquals("val-phoneNumber", dto.getPhoneNumber());
        assertEquals("val-name", dto.getName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-location", dto.getLocation());
        assertEquals("val-callerId", dto.getCallerId());
    }

    @Test
    void testEqualsAndHashCode() {
        PhoneCall.CallerInfo dto1 = PhoneCall.CallerInfo.builder()
                        .phoneNumber("test-phoneNumber")
            .name("test-name")
            .email("test-email")
            .customerId("test-customerId")
            .accountNumber("test-accountNumber")
            .location("test-location")
            .callerId("test-callerId")
            .build();
        PhoneCall.CallerInfo dto2 = PhoneCall.CallerInfo.builder()
                        .phoneNumber("test-phoneNumber")
            .name("test-name")
            .email("test-email")
            .customerId("test-customerId")
            .accountNumber("test-accountNumber")
            .location("test-location")
            .callerId("test-callerId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PhoneCall.CallerInfo dto = PhoneCall.CallerInfo.builder()
                        .phoneNumber("test-phoneNumber")
            .name("test-name")
            .email("test-email")
            .customerId("test-customerId")
            .accountNumber("test-accountNumber")
            .location("test-location")
            .callerId("test-callerId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}