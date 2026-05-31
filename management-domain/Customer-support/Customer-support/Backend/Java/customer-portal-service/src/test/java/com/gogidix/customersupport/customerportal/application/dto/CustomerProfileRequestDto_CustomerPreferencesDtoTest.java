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
class CustomerProfileRequestDto_CustomerPreferencesDtoTest {

        @Test
    void testBuilder() {
        CustomerProfileRequestDto.CustomerPreferencesDto dto = CustomerProfileRequestDto.CustomerPreferencesDto.builder()
                        .emailNotifications(true)
            .smsNotifications(true)
            .phoneNotifications(true)
            .pushNotifications(true)
            .preferredContactMethod("test-preferredContactMethod")
            .notificationPreferences(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertTrue(dto.getEmailNotifications());
        assertTrue(dto.getSmsNotifications());
        assertTrue(dto.getPhoneNotifications());
        assertTrue(dto.getPushNotifications());
        assertEquals("test-preferredContactMethod", dto.getPreferredContactMethod());
    }

    @Test
    void testSettersAndGetters() {
        CustomerProfileRequestDto.CustomerPreferencesDto dto = new CustomerProfileRequestDto.CustomerPreferencesDto();
        dto.setEmailNotifications(true);
        dto.setSmsNotifications(true);
        dto.setPhoneNotifications(true);
        dto.setPushNotifications(true);
        dto.setPreferredContactMethod("val-preferredContactMethod");
        assertTrue(dto.getEmailNotifications());
        assertTrue(dto.getSmsNotifications());
        assertTrue(dto.getPhoneNotifications());
        assertTrue(dto.getPushNotifications());
        assertEquals("val-preferredContactMethod", dto.getPreferredContactMethod());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerProfileRequestDto.CustomerPreferencesDto dto1 = CustomerProfileRequestDto.CustomerPreferencesDto.builder()
                        .emailNotifications(true)
            .smsNotifications(true)
            .phoneNotifications(true)
            .pushNotifications(true)
            .preferredContactMethod("test-preferredContactMethod")
            .notificationPreferences(Collections.emptyMap())
            .build();
        CustomerProfileRequestDto.CustomerPreferencesDto dto2 = CustomerProfileRequestDto.CustomerPreferencesDto.builder()
                        .emailNotifications(true)
            .smsNotifications(true)
            .phoneNotifications(true)
            .pushNotifications(true)
            .preferredContactMethod("test-preferredContactMethod")
            .notificationPreferences(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CustomerProfileRequestDto.CustomerPreferencesDto dto = CustomerProfileRequestDto.CustomerPreferencesDto.builder()
                        .emailNotifications(true)
            .smsNotifications(true)
            .phoneNotifications(true)
            .pushNotifications(true)
            .preferredContactMethod("test-preferredContactMethod")
            .notificationPreferences(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}