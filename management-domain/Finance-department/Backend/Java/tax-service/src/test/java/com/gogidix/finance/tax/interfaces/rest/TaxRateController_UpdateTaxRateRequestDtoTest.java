package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxRateController;
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
class TaxRateController_UpdateTaxRateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxRateController.UpdateTaxRateRequestDto dto = new TaxRateController.UpdateTaxRateRequestDto();
        dto.setNewRate(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setNewExpiryDate(LocalDate.of(2025,6,1));
        dto.setIsCompound(true);
        dto.setIsRecoverable(true);
        dto.setRecoveryRate(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals(BigDecimal.ONE, dto.getNewRate());
        assertEquals("val-description", dto.getDescription());
        assertEquals(LocalDate.of(2025,6,1), dto.getNewExpiryDate());
        assertTrue(dto.getIsCompound());
        assertTrue(dto.getIsRecoverable());
        assertEquals(BigDecimal.ONE, dto.getRecoveryRate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateController.UpdateTaxRateRequestDto dto1 = new TaxRateController.UpdateTaxRateRequestDto();
        TaxRateController.UpdateTaxRateRequestDto dto2 = new TaxRateController.UpdateTaxRateRequestDto();
        dto1.setNewRate(BigDecimal.TEN);
        dto1.setDescription("test");
        dto1.setNewExpiryDate(LocalDate.of(2025,1,1));
        dto1.setIsCompound(true);
        dto1.setIsRecoverable(true);
        dto1.setRecoveryRate(BigDecimal.TEN);
        dto1.setNotes("test");
        dto2.setNewRate(BigDecimal.TEN);
        dto2.setDescription("test");
        dto2.setNewExpiryDate(LocalDate.of(2025,1,1));
        dto2.setIsCompound(true);
        dto2.setIsRecoverable(true);
        dto2.setRecoveryRate(BigDecimal.TEN);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewRate(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateController.UpdateTaxRateRequestDto dto = new TaxRateController.UpdateTaxRateRequestDto();
        dto.setNewRate(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setNewExpiryDate(LocalDate.of(2025,1,1));
        dto.setIsCompound(true);
        dto.setIsRecoverable(true);
        dto.setRecoveryRate(BigDecimal.TEN);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateController.UpdateTaxRateRequestDto dto = new TaxRateController.UpdateTaxRateRequestDto();
        dto.setNewRate(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setNewExpiryDate(LocalDate.of(2025,1,1));
        dto.setIsCompound(true);
        dto.setIsRecoverable(true);
        dto.setRecoveryRate(BigDecimal.TEN);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}