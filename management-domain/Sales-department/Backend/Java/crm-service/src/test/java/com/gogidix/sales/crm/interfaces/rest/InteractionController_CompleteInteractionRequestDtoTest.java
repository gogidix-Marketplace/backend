package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.interfaces.rest.InteractionController;
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
class InteractionController_CompleteInteractionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        InteractionController.CompleteInteractionRequestDto dto = new InteractionController.CompleteInteractionRequestDto();
        dto.setOutcome("val-outcome");
        dto.setNotes("val-notes");
        dto.setDurationMinutes(99);
        dto.setFollowUpDate(LocalDate.of(2025,6,1));
        dto.setFollowUpNotes("val-followUpNotes");
        dto.setNextStep("val-nextStep");
        dto.setNextStepDate(LocalDate.of(2025,6,1));
        assertEquals("val-outcome", dto.getOutcome());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(99, dto.getDurationMinutes());
        assertEquals(LocalDate.of(2025,6,1), dto.getFollowUpDate());
        assertEquals("val-followUpNotes", dto.getFollowUpNotes());
        assertEquals("val-nextStep", dto.getNextStep());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextStepDate());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionController.CompleteInteractionRequestDto dto1 = new InteractionController.CompleteInteractionRequestDto();
        InteractionController.CompleteInteractionRequestDto dto2 = new InteractionController.CompleteInteractionRequestDto();
        dto1.setOutcome("test");
        dto1.setNotes("test");
        dto1.setDurationMinutes(42);
        dto1.setFollowUpDate(LocalDate.of(2025,1,1));
        dto1.setFollowUpNotes("test");
        dto1.setNextStep("test");
        dto1.setNextStepDate(LocalDate.of(2025,1,1));
        dto2.setOutcome("test");
        dto2.setNotes("test");
        dto2.setDurationMinutes(42);
        dto2.setFollowUpDate(LocalDate.of(2025,1,1));
        dto2.setFollowUpNotes("test");
        dto2.setNextStep("test");
        dto2.setNextStepDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setOutcome(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionController.CompleteInteractionRequestDto dto = new InteractionController.CompleteInteractionRequestDto();
        dto.setOutcome("test");
        dto.setNotes("test");
        dto.setDurationMinutes(42);
        dto.setFollowUpDate(LocalDate.of(2025,1,1));
        dto.setFollowUpNotes("test");
        dto.setNextStep("test");
        dto.setNextStepDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionController.CompleteInteractionRequestDto dto = new InteractionController.CompleteInteractionRequestDto();
        dto.setOutcome("test");
        dto.setNotes("test");
        dto.setDurationMinutes(42);
        dto.setFollowUpDate(LocalDate.of(2025,1,1));
        dto.setFollowUpNotes("test");
        dto.setNextStep("test");
        dto.setNextStepDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}