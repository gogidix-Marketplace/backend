package com.gogidix.sales.leadmanagement.interfaces.rest;

import com.gogidix.sales.leadmanagement.domain.port.in.LeadCommand;
import com.gogidix.sales.leadmanagement.interfaces.rest.LeadController;
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
class LeadController_RecordInteractionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LeadController.RecordInteractionRequestDto dto = new LeadController.RecordInteractionRequestDto();
        dto.setEmailOpened(true);
        dto.setEmailClicked(true);
        dto.setFormName("val-formName");
        assertTrue(dto.getEmailOpened());
        assertTrue(dto.getEmailClicked());
        assertEquals("val-formName", dto.getFormName());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadController.RecordInteractionRequestDto dto1 = new LeadController.RecordInteractionRequestDto();
        LeadController.RecordInteractionRequestDto dto2 = new LeadController.RecordInteractionRequestDto();
        dto1.setInteractionType(LeadCommand.InteractionType.EMAIL_OPEN);
        dto1.setEmailOpened(true);
        dto1.setEmailClicked(true);
        dto1.setFormName("test");
        dto2.setInteractionType(LeadCommand.InteractionType.EMAIL_OPEN);
        dto2.setEmailOpened(true);
        dto2.setEmailClicked(true);
        dto2.setFormName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setInteractionType(LeadCommand.InteractionType.EMAIL_CLICK);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadController.RecordInteractionRequestDto dto = new LeadController.RecordInteractionRequestDto();
        dto.setInteractionType(LeadCommand.InteractionType.EMAIL_OPEN);
        dto.setEmailOpened(true);
        dto.setEmailClicked(true);
        dto.setFormName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadController.RecordInteractionRequestDto dto = new LeadController.RecordInteractionRequestDto();
        dto.setInteractionType(LeadCommand.InteractionType.EMAIL_OPEN);
        dto.setEmailOpened(true);
        dto.setEmailClicked(true);
        dto.setFormName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}