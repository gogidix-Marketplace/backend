package com.gogidix.hr.payroll.application.dto.response;

import com.gogidix.hr.payroll.application.dto.response.PayrollEntryResponse;
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
class PayrollEntryResponse_EarningDetailResponseTest {

        @Test
    void testBuilder() {
        PayrollEntryResponse.EarningDetailResponse dto = PayrollEntryResponse.EarningDetailResponse.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .referenceId("test-referenceId")
            .build();
        assertNotNull(dto);
        assertEquals("test-earningType", dto.getEarningType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(42, dto.getHours());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals("test-referenceId", dto.getReferenceId());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryResponse.EarningDetailResponse dto = new PayrollEntryResponse.EarningDetailResponse();
        dto.setEarningType("val-earningType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setHours(99);
        dto.setRate(BigDecimal.ONE);
        dto.setReferenceId("val-referenceId");
        assertEquals("val-earningType", dto.getEarningType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(99, dto.getHours());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals("val-referenceId", dto.getReferenceId());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryResponse.EarningDetailResponse dto1 = PayrollEntryResponse.EarningDetailResponse.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .referenceId("test-referenceId")
            .build();
        PayrollEntryResponse.EarningDetailResponse dto2 = PayrollEntryResponse.EarningDetailResponse.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .referenceId("test-referenceId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryResponse.EarningDetailResponse dto = PayrollEntryResponse.EarningDetailResponse.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .referenceId("test-referenceId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}