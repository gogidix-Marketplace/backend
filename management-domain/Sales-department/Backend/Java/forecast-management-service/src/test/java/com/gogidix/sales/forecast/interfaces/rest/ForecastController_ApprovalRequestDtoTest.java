package com.gogidix.sales.forecast.interfaces.rest;

import com.gogidix.sales.forecast.domain.model.Forecast;
import com.gogidix.sales.forecast.interfaces.rest.ForecastController;
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
class ForecastController_ApprovalRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ForecastController.ApprovalRequestDto dto = new ForecastController.ApprovalRequestDto();


    }

    @Test
    void testEqualsAndHashCode() {
        ForecastController.ApprovalRequestDto dto1 = new ForecastController.ApprovalRequestDto();
        ForecastController.ApprovalRequestDto dto2 = new ForecastController.ApprovalRequestDto();
        dto1.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        dto2.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setApprovalLevel(Forecast.ApprovalLevel.MANAGER);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastController.ApprovalRequestDto dto = new ForecastController.ApprovalRequestDto();
        dto.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastController.ApprovalRequestDto dto = new ForecastController.ApprovalRequestDto();
        dto.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}