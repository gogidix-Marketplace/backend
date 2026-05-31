package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.interfaces.rest.QuotaController;
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
class QuotaController_AdjustQuotaRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        QuotaController.AdjustQuotaRequestDto dto = new QuotaController.AdjustQuotaRequestDto();
        dto.setNewAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        assertEquals(BigDecimal.ONE, dto.getNewAmount());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaController.AdjustQuotaRequestDto dto1 = new QuotaController.AdjustQuotaRequestDto();
        QuotaController.AdjustQuotaRequestDto dto2 = new QuotaController.AdjustQuotaRequestDto();
        dto1.setNewAmount(BigDecimal.TEN);
        dto1.setReason("test");
        dto2.setNewAmount(BigDecimal.TEN);
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewAmount(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        QuotaController.AdjustQuotaRequestDto dto = new QuotaController.AdjustQuotaRequestDto();
        dto.setNewAmount(BigDecimal.TEN);
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        QuotaController.AdjustQuotaRequestDto dto = new QuotaController.AdjustQuotaRequestDto();
        dto.setNewAmount(BigDecimal.TEN);
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}