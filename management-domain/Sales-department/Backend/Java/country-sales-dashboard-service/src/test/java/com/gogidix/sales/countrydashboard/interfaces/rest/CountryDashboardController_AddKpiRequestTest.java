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
class CountryDashboardController_AddKpiRequestTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardController.AddKpiRequest dto = new CountryDashboardController.AddKpiRequest();
        dto.setName("val-name");
        dto.setTarget("val-target");
        dto.setWeight(99);
        dto.setIsCritical(true);
        assertEquals("val-name", dto.getName());
        assertEquals("val-target", dto.getTarget());
        assertEquals(99, dto.getWeight());
        assertTrue(dto.getIsCritical());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardController.AddKpiRequest dto1 = new CountryDashboardController.AddKpiRequest();
        CountryDashboardController.AddKpiRequest dto2 = new CountryDashboardController.AddKpiRequest();
        dto1.setName("test");
        dto1.setType(null);
        dto1.setValue(null);
        dto1.setTarget("test");
        dto1.setWeight(42);
        dto1.setIsCritical(true);
        dto2.setName("test");
        dto2.setType(null);
        dto2.setValue(null);
        dto2.setTarget("test");
        dto2.setWeight(42);
        dto2.setIsCritical(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardController.AddKpiRequest dto = new CountryDashboardController.AddKpiRequest();
        dto.setName("test");
        dto.setType(null);
        dto.setValue(null);
        dto.setTarget("test");
        dto.setWeight(42);
        dto.setIsCritical(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardController.AddKpiRequest dto = new CountryDashboardController.AddKpiRequest();
        dto.setName("test");
        dto.setType(null);
        dto.setValue(null);
        dto.setTarget("test");
        dto.setWeight(42);
        dto.setIsCritical(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}