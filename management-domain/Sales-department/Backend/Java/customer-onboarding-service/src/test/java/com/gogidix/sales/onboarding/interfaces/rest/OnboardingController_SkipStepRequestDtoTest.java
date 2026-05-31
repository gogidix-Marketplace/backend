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
class OnboardingController_SkipStepRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        OnboardingController.SkipStepRequestDto dto = new OnboardingController.SkipStepRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingController.SkipStepRequestDto dto1 = new OnboardingController.SkipStepRequestDto();
        OnboardingController.SkipStepRequestDto dto2 = new OnboardingController.SkipStepRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingController.SkipStepRequestDto dto = new OnboardingController.SkipStepRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingController.SkipStepRequestDto dto = new OnboardingController.SkipStepRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}