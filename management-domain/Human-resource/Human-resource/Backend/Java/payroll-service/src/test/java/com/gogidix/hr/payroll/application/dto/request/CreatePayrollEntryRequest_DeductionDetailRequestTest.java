package com.gogidix.hr.payroll.application.dto.request;

import com.gogidix.hr.payroll.application.dto.request.CreatePayrollEntryRequest;
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
class CreatePayrollEntryRequest_DeductionDetailRequestTest {

        @Test
    void testBuilder() {
        CreatePayrollEntryRequest.DeductionDetailRequest dto = CreatePayrollEntryRequest.DeductionDetailRequest.builder()
                        .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .pretax(true)
            .referenceId("test-referenceId")
            .build();
        assertNotNull(dto);
        assertEquals("test-deductionType", dto.getDeductionType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertTrue(dto.getPretax());
        assertEquals("test-referenceId", dto.getReferenceId());
    }

    @Test
    void testSettersAndGetters() {
        CreatePayrollEntryRequest.DeductionDetailRequest dto = new CreatePayrollEntryRequest.DeductionDetailRequest();
        dto.setDeductionType("val-deductionType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setPretax(true);
        dto.setReferenceId("val-referenceId");
        assertEquals("val-deductionType", dto.getDeductionType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertTrue(dto.getPretax());
        assertEquals("val-referenceId", dto.getReferenceId());
    }

    @Test
    void testEqualsAndHashCode() {
        CreatePayrollEntryRequest.DeductionDetailRequest dto1 = CreatePayrollEntryRequest.DeductionDetailRequest.builder()
                        .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .pretax(true)
            .referenceId("test-referenceId")
            .build();
        CreatePayrollEntryRequest.DeductionDetailRequest dto2 = CreatePayrollEntryRequest.DeductionDetailRequest.builder()
                        .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .pretax(true)
            .referenceId("test-referenceId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreatePayrollEntryRequest.DeductionDetailRequest dto = CreatePayrollEntryRequest.DeductionDetailRequest.builder()
                        .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .pretax(true)
            .referenceId("test-referenceId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}