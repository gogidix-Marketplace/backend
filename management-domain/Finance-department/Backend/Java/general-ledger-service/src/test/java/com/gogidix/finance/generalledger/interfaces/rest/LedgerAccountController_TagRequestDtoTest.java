package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.interfaces.rest.LedgerAccountController;
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
class LedgerAccountController_TagRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountController.TagRequestDto dto = new LedgerAccountController.TagRequestDto();
        dto.setKey("val-key");
        dto.setValue("val-value");
        assertEquals("val-key", dto.getKey());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountController.TagRequestDto dto1 = new LedgerAccountController.TagRequestDto();
        LedgerAccountController.TagRequestDto dto2 = new LedgerAccountController.TagRequestDto();
        dto1.setKey("test");
        dto1.setValue("test");
        dto2.setKey("test");
        dto2.setValue("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setKey(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountController.TagRequestDto dto = new LedgerAccountController.TagRequestDto();
        dto.setKey("test");
        dto.setValue("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountController.TagRequestDto dto = new LedgerAccountController.TagRequestDto();
        dto.setKey("test");
        dto.setValue("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}