package com.gogidix.sales.countrydashboard.interfaces.rest;

import com.gogidix.sales.countrydashboard.interfaces.rest.CountryDashboardController;
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
class CountryDashboardController_RefreshDashboardRequestTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardController.RefreshDashboardRequest dto = new CountryDashboardController.RefreshDashboardRequest();
        dto.setForceRefresh(true);
        dto.setCalculateYoY(true);
        dto.setCalculateMoM(true);
        dto.setCalculateQoQ(true);
        assertTrue(dto.getForceRefresh());
        assertTrue(dto.getCalculateYoY());
        assertTrue(dto.getCalculateMoM());
        assertTrue(dto.getCalculateQoQ());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardController.RefreshDashboardRequest dto1 = new CountryDashboardController.RefreshDashboardRequest();
        CountryDashboardController.RefreshDashboardRequest dto2 = new CountryDashboardController.RefreshDashboardRequest();
        dto1.setForceRefresh(true);
        dto1.setCalculateYoY(true);
        dto1.setCalculateMoM(true);
        dto1.setCalculateQoQ(true);
        dto2.setForceRefresh(true);
        dto2.setCalculateYoY(true);
        dto2.setCalculateMoM(true);
        dto2.setCalculateQoQ(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setForceRefresh(false);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardController.RefreshDashboardRequest dto = new CountryDashboardController.RefreshDashboardRequest();
        dto.setForceRefresh(true);
        dto.setCalculateYoY(true);
        dto.setCalculateMoM(true);
        dto.setCalculateQoQ(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardController.RefreshDashboardRequest dto = new CountryDashboardController.RefreshDashboardRequest();
        dto.setForceRefresh(true);
        dto.setCalculateYoY(true);
        dto.setCalculateMoM(true);
        dto.setCalculateQoQ(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}