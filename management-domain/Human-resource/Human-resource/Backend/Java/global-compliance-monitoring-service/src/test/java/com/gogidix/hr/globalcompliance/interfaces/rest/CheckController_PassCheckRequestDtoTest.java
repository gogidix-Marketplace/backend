package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.interfaces.rest.CheckController;
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
class CheckController_PassCheckRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CheckController.PassCheckRequestDto dto = new CheckController.PassCheckRequestDto();
        dto.setFindings("val-findings");
        assertEquals("val-findings", dto.getFindings());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckController.PassCheckRequestDto dto1 = new CheckController.PassCheckRequestDto();
        CheckController.PassCheckRequestDto dto2 = new CheckController.PassCheckRequestDto();
        dto1.setFindings("test");
        dto1.setSupportingDocuments(Collections.emptyList());
        dto2.setFindings("test");
        dto2.setSupportingDocuments(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFindings(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckController.PassCheckRequestDto dto = new CheckController.PassCheckRequestDto();
        dto.setFindings("test");
        dto.setSupportingDocuments(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckController.PassCheckRequestDto dto = new CheckController.PassCheckRequestDto();
        dto.setFindings("test");
        dto.setSupportingDocuments(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}