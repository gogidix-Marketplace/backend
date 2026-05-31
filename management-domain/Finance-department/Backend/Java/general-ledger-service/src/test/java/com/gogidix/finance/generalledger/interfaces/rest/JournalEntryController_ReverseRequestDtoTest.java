package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.interfaces.rest.JournalEntryController;
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
class JournalEntryController_ReverseRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        JournalEntryController.ReverseRequestDto dto = new JournalEntryController.ReverseRequestDto();
        dto.setReversalReason("val-reversalReason");
        dto.setReversalDate(LocalDate.of(2025,6,1));
        assertEquals("val-reversalReason", dto.getReversalReason());
        assertEquals(LocalDate.of(2025,6,1), dto.getReversalDate());
    }

    @Test
    void testEqualsAndHashCode() {
        JournalEntryController.ReverseRequestDto dto1 = new JournalEntryController.ReverseRequestDto();
        JournalEntryController.ReverseRequestDto dto2 = new JournalEntryController.ReverseRequestDto();
        dto1.setReversalReason("test");
        dto1.setReversalDate(LocalDate.of(2025,1,1));
        dto2.setReversalReason("test");
        dto2.setReversalDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReversalReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        JournalEntryController.ReverseRequestDto dto = new JournalEntryController.ReverseRequestDto();
        dto.setReversalReason("test");
        dto.setReversalDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        JournalEntryController.ReverseRequestDto dto = new JournalEntryController.ReverseRequestDto();
        dto.setReversalReason("test");
        dto.setReversalDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}