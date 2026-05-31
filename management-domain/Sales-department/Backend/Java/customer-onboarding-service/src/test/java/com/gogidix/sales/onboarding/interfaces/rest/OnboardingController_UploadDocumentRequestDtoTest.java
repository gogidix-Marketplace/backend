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
class OnboardingController_UploadDocumentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        OnboardingController.UploadDocumentRequestDto dto = new OnboardingController.UploadDocumentRequestDto();
        dto.setChecklistId("val-checklistId");
        dto.setItemId("val-itemId");
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        assertEquals("val-checklistId", dto.getChecklistId());
        assertEquals("val-itemId", dto.getItemId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingController.UploadDocumentRequestDto dto1 = new OnboardingController.UploadDocumentRequestDto();
        OnboardingController.UploadDocumentRequestDto dto2 = new OnboardingController.UploadDocumentRequestDto();
        dto1.setChecklistId("test");
        dto1.setItemId("test");
        dto1.setFileName("test");
        dto1.setFileUrl("test");
        dto1.setFileSizeBytes(42L);
        dto2.setChecklistId("test");
        dto2.setItemId("test");
        dto2.setFileName("test");
        dto2.setFileUrl("test");
        dto2.setFileSizeBytes(42L);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setChecklistId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingController.UploadDocumentRequestDto dto = new OnboardingController.UploadDocumentRequestDto();
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setFileName("test");
        dto.setFileUrl("test");
        dto.setFileSizeBytes(42L);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingController.UploadDocumentRequestDto dto = new OnboardingController.UploadDocumentRequestDto();
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setFileName("test");
        dto.setFileUrl("test");
        dto.setFileSizeBytes(42L);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}