package com.gogidix.finance.forecasting.interfaces.rest;

import com.gogidix.finance.forecasting.interfaces.rest.ForecastController;
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
class ForecastController_RejectionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ForecastController.RejectionRequestDto dto = new ForecastController.RejectionRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastController.RejectionRequestDto dto1 = new ForecastController.RejectionRequestDto();
        ForecastController.RejectionRequestDto dto2 = new ForecastController.RejectionRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastController.RejectionRequestDto dto = new ForecastController.RejectionRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastController.RejectionRequestDto dto = new ForecastController.RejectionRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}