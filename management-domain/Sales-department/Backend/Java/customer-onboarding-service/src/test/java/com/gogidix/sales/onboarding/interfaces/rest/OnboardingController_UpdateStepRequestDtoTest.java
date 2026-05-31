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
class OnboardingController_UpdateStepRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        OnboardingController.UpdateStepRequestDto dto = new OnboardingController.UpdateStepRequestDto();
        dto.setNotes("val-notes");
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingController.UpdateStepRequestDto dto1 = new OnboardingController.UpdateStepRequestDto();
        OnboardingController.UpdateStepRequestDto dto2 = new OnboardingController.UpdateStepRequestDto();
        dto1.setStatus(null);
        dto1.setNotes("test");
        dto2.setStatus(null);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNotes(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingController.UpdateStepRequestDto dto = new OnboardingController.UpdateStepRequestDto();
        dto.setStatus(null);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingController.UpdateStepRequestDto dto = new OnboardingController.UpdateStepRequestDto();
        dto.setStatus(null);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}