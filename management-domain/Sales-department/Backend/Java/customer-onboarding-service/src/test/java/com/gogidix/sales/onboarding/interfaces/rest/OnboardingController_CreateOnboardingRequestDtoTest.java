package com.gogidix.sales.onboarding.interfaces.rest;

import com.gogidix.sales.onboarding.interfaces.rest.OnboardingController;
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
class OnboardingController_CreateOnboardingRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        OnboardingController.CreateOnboardingRequestDto dto = new OnboardingController.CreateOnboardingRequestDto();
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setTemplateId("val-templateId");
        dto.setTemplateName("val-templateName");
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-templateName", dto.getTemplateName());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingController.CreateOnboardingRequestDto dto1 = new OnboardingController.CreateOnboardingRequestDto();
        OnboardingController.CreateOnboardingRequestDto dto2 = new OnboardingController.CreateOnboardingRequestDto();
        dto1.setCustomerId("test");
        dto1.setCustomerName("test");
        dto1.setCustomerEmail("test");
        dto1.setCustomerType(null);
        dto1.setTemplateId("test");
        dto1.setTemplateName("test");
        dto1.setPriority(null);
        dto2.setCustomerId("test");
        dto2.setCustomerName("test");
        dto2.setCustomerEmail("test");
        dto2.setCustomerType(null);
        dto2.setTemplateId("test");
        dto2.setTemplateName("test");
        dto2.setPriority(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCustomerId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingController.CreateOnboardingRequestDto dto = new OnboardingController.CreateOnboardingRequestDto();
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setCustomerEmail("test");
        dto.setCustomerType(null);
        dto.setTemplateId("test");
        dto.setTemplateName("test");
        dto.setPriority(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingController.CreateOnboardingRequestDto dto = new OnboardingController.CreateOnboardingRequestDto();
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setCustomerEmail("test");
        dto.setCustomerType(null);
        dto.setTemplateId("test");
        dto.setTemplateName("test");
        dto.setPriority(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}