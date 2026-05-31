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
class TaxFilingController_AdjustmentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingController.AdjustmentRequestDto dto = new TaxFilingController.AdjustmentRequestDto();
        dto.setAdjustmentType("val-adjustmentType");
        dto.setAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        dto.setReference("val-reference");
        assertEquals("val-adjustmentType", dto.getAdjustmentType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-reference", dto.getReference());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingController.AdjustmentRequestDto dto1 = new TaxFilingController.AdjustmentRequestDto();
        TaxFilingController.AdjustmentRequestDto dto2 = new TaxFilingController.AdjustmentRequestDto();
        dto1.setAdjustmentType("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setReason("test");
        dto1.setReference("test");
        dto2.setAdjustmentType("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setReason("test");
        dto2.setReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAdjustmentType(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingController.AdjustmentRequestDto dto = new TaxFilingController.AdjustmentRequestDto();
        dto.setAdjustmentType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setReason("test");
        dto.setReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingController.AdjustmentRequestDto dto = new TaxFilingController.AdjustmentRequestDto();
        dto.setAdjustmentType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setReason("test");
        dto.setReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}