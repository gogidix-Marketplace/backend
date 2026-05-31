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
class OnboardingController_AssignRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        OnboardingController.AssignRequestDto dto = new OnboardingController.AssignRequestDto();
        dto.setAssignee("val-assignee");
        assertEquals("val-assignee", dto.getAssignee());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingController.AssignRequestDto dto1 = new OnboardingController.AssignRequestDto();
        OnboardingController.AssignRequestDto dto2 = new OnboardingController.AssignRequestDto();
        dto1.setAssignee("test");
        dto2.setAssignee("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAssignee(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingController.AssignRequestDto dto = new OnboardingController.AssignRequestDto();
        dto.setAssignee("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingController.AssignRequestDto dto = new OnboardingController.AssignRequestDto();
        dto.setAssignee("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}