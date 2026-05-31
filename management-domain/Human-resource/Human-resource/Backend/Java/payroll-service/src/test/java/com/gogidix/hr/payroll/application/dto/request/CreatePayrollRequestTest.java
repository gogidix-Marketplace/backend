package com.gogidix.hr.payroll.application.dto.request;

import com.gogidix.hr.payroll.application.dto.request.CreatePayrollRequest;
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
class CreatePayrollRequestTest {

        @Test
    void testBuilder() {
        CreatePayrollRequest dto = CreatePayrollRequest.builder()
                        .countryCode("test-countryCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .employeeCount(42)
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,1,15), dto.getPayDate());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(42, dto.getEmployeeCount());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        CreatePayrollRequest dto = new CreatePayrollRequest();
        dto.setCountryCode("val-countryCode");
        dto.setPayDate(LocalDate.of(2025,6,1));
        dto.setCurrency("val-currency");
        dto.setEmployeeCount(99);
        dto.setNotes("val-notes");
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,6,1), dto.getPayDate());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(99, dto.getEmployeeCount());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CreatePayrollRequest dto1 = CreatePayrollRequest.builder()
                        .countryCode("test-countryCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .employeeCount(42)
            .notes("test-notes")
            .build();
        CreatePayrollRequest dto2 = CreatePayrollRequest.builder()
                        .countryCode("test-countryCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .employeeCount(42)
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreatePayrollRequest dto = CreatePayrollRequest.builder()
                        .countryCode("test-countryCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .currency("test-currency")
            .employeeCount(42)
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}