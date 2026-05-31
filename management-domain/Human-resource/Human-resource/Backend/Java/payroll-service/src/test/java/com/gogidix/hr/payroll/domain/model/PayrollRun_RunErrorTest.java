package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.model.PayrollRun;
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
class PayrollRun_RunErrorTest {

        @Test
    void testBuilder() {
        PayrollRun.RunError dto = PayrollRun.RunError.builder()
                        .employeeId("test-employeeId")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .timestamp(42L)
            .stackTrace("test-stackTrace")
            .build();
        assertNotNull(dto);
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-errorCode", dto.getErrorCode());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertEquals(42L, dto.getTimestamp());
        assertEquals("test-stackTrace", dto.getStackTrace());
    }

    @Test
    void testSettersAndGetters() {
        PayrollRun.RunError dto = new PayrollRun.RunError();
        dto.setEmployeeId("val-employeeId");
        dto.setErrorCode("val-errorCode");
        dto.setErrorMessage("val-errorMessage");
        dto.setStackTrace("val-stackTrace");
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-errorCode", dto.getErrorCode());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertEquals("val-stackTrace", dto.getStackTrace());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollRun.RunError dto1 = PayrollRun.RunError.builder()
                        .employeeId("test-employeeId")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .timestamp(42L)
            .stackTrace("test-stackTrace")
            .build();
        PayrollRun.RunError dto2 = PayrollRun.RunError.builder()
                        .employeeId("test-employeeId")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .timestamp(42L)
            .stackTrace("test-stackTrace")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollRun.RunError dto = PayrollRun.RunError.builder()
                        .employeeId("test-employeeId")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .timestamp(42L)
            .stackTrace("test-stackTrace")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}