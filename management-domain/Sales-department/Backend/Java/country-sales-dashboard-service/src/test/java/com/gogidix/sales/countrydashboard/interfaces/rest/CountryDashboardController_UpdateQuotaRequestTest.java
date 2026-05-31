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
class CountryDashboardController_UpdateQuotaRequestTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardController.UpdateQuotaRequest dto = new CountryDashboardController.UpdateQuotaRequest();
        dto.setAnnualQuota(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setReason("val-reason");
        assertEquals(BigDecimal.ONE, dto.getAnnualQuota());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardController.UpdateQuotaRequest dto1 = new CountryDashboardController.UpdateQuotaRequest();
        CountryDashboardController.UpdateQuotaRequest dto2 = new CountryDashboardController.UpdateQuotaRequest();
        dto1.setAnnualQuota(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setReason("test");
        dto2.setAnnualQuota(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAnnualQuota(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardController.UpdateQuotaRequest dto = new CountryDashboardController.UpdateQuotaRequest();
        dto.setAnnualQuota(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardController.UpdateQuotaRequest dto = new CountryDashboardController.UpdateQuotaRequest();
        dto.setAnnualQuota(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}