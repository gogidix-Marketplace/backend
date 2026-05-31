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
class CustomerProfile_CustomerPreferencesTest {

        @Test
    void testBuilder() {
        CustomerProfile.CustomerPreferences dto = CustomerProfile.CustomerPreferences.builder()
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
        CustomerProfile.CustomerPreferences dto = new CustomerProfile.CustomerPreferences();
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
        CustomerProfile.CustomerPreferences dto1 = CustomerProfile.CustomerPreferences.builder()
                        .emailNotifications(true)
            .smsNotifications(true)
            .phoneNotifications(true)
            .pushNotifications(true)
            .preferredContactMethod("test-preferredContactMethod")
            .notificationPreferences(Collections.emptyMap())
            .build();
        CustomerProfile.CustomerPreferences dto2 = CustomerProfile.CustomerPreferences.builder()
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
        CustomerProfile.CustomerPreferences dto = CustomerProfile.CustomerPreferences.builder()
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