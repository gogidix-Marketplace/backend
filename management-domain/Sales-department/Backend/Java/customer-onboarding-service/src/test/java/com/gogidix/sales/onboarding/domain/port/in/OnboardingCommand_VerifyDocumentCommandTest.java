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
class OnboardingCommand_VerifyDocumentCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.VerifyDocumentCommand dto = new OnboardingCommand.VerifyDocumentCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setChecklistId("val-checklistId");
        dto.setItemId("val-itemId");
        dto.setApproved(true);
        dto.setVerifiedBy("val-verifiedBy");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-checklistId", dto.getChecklistId());
        assertEquals("val-itemId", dto.getItemId());
        assertTrue(dto.getApproved());
        assertEquals("val-verifiedBy", dto.getVerifiedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.VerifyDocumentCommand dto1 = new OnboardingCommand.VerifyDocumentCommand();
        OnboardingCommand.VerifyDocumentCommand dto2 = new OnboardingCommand.VerifyDocumentCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setChecklistId("test");
        dto1.setItemId("test");
        dto1.setApproved(true);
        dto1.setVerifiedBy("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setChecklistId("test");
        dto2.setItemId("test");
        dto2.setApproved(true);
        dto2.setVerifiedBy("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.VerifyDocumentCommand dto = new OnboardingCommand.VerifyDocumentCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setApproved(true);
        dto.setVerifiedBy("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.VerifyDocumentCommand dto = new OnboardingCommand.VerifyDocumentCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setChecklistId("test");
        dto.setItemId("test");
        dto.setApproved(true);
        dto.setVerifiedBy("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}