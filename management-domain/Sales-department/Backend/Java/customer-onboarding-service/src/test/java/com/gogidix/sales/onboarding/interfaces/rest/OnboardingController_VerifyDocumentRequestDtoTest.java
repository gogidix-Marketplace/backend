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
class OnboardingController_VerifyDocumentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        OnboardingController.VerifyDocumentRequestDto dto = new OnboardingController.VerifyDocumentRequestDto();
        dto.setChecklistId("val-checklistId");
        dto.setItemId("val-itemId");
        dto.setApproved(true);
        dto.setNotes("val-notes");
        assertEquals("val-checklistId", dto.getChecklistId());
        assertEquals("val-itemId", dto.getItemId());
        assertTrue(dto.getApproved());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingController.VerifyDocumentRequestDto dto1 = new OnboardingController.VerifyDocumentRequestDto();
        OnboardingController.VerifyDocumentRequestDto dto2 = new OnboardingController.VerifyDocumentRequestDto();
        dto1.setChecklistId("test");
        dto1.setItemId("test");
        dto1.setApproved(true);
        dto1.setNotes("test");
        dto2.setChecklistId("test");
        dto2.setItemId("test");
        dto2.setApproved(true);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setChecklistId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingController.VerifyDocumentRequestDto dto = new OnboardingController.VerifyDocumentRequestDto();
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setApproved(true);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingController.VerifyDocumentRequestDto dto = new OnboardingController.VerifyDocumentRequestDto();
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setApproved(true);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}