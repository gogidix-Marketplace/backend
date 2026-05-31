package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.port.in.OnboardingCommand;
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
class OnboardingCommand_UploadDocumentCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.UploadDocumentCommand dto = new OnboardingCommand.UploadDocumentCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setChecklistId("val-checklistId");
        dto.setItemId("val-itemId");
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        dto.setUploadedBy("val-uploadedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-checklistId", dto.getChecklistId());
        assertEquals("val-itemId", dto.getItemId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals("val-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.UploadDocumentCommand dto1 = new OnboardingCommand.UploadDocumentCommand();
        OnboardingCommand.UploadDocumentCommand dto2 = new OnboardingCommand.UploadDocumentCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setChecklistId("test");
        dto1.setItemId("test");
        dto1.setFileName("test");
        dto1.setFileUrl("test");
        dto1.setFileSizeBytes(42L);
        dto1.setUploadedBy("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setChecklistId("test");
        dto2.setItemId("test");
        dto2.setFileName("test");
        dto2.setFileUrl("test");
        dto2.setFileSizeBytes(42L);
        dto2.setUploadedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.UploadDocumentCommand dto = new OnboardingCommand.UploadDocumentCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setFileName("test");
        dto.setFileUrl("test");
        dto.setFileSizeBytes(42L);
        dto.setUploadedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.UploadDocumentCommand dto = new OnboardingCommand.UploadDocumentCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setFileName("test");
        dto.setFileUrl("test");
        dto.setFileSizeBytes(42L);
        dto.setUploadedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}