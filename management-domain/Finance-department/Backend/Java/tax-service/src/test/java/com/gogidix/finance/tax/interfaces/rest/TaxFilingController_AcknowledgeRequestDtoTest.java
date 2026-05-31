package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxFilingController;
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
class TaxFilingController_AcknowledgeRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingController.AcknowledgeRequestDto dto = new TaxFilingController.AcknowledgeRequestDto();
        dto.setAcknowledgementNumber("val-acknowledgementNumber");
        assertEquals("val-acknowledgementNumber", dto.getAcknowledgementNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingController.AcknowledgeRequestDto dto1 = new TaxFilingController.AcknowledgeRequestDto();
        TaxFilingController.AcknowledgeRequestDto dto2 = new TaxFilingController.AcknowledgeRequestDto();
        dto1.setAcknowledgementNumber("test");
        dto2.setAcknowledgementNumber("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAcknowledgementNumber(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingController.AcknowledgeRequestDto dto = new TaxFilingController.AcknowledgeRequestDto();
        dto.setAcknowledgementNumber("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingController.AcknowledgeRequestDto dto = new TaxFilingController.AcknowledgeRequestDto();
        dto.setAcknowledgementNumber("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}