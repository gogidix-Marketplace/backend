package com.gogidix.finance.compliance.domain.model;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
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
class ComplianceCheck_CheckViolationDetailTest {

        @Test
    void testBuilder() {
        ComplianceCheck.CheckViolationDetail dto = ComplianceCheck.CheckViolationDetail.builder()
                        .field("test-field")
            .expected("test-expected")
            .actual("test-actual")
            .message("test-message")
            .build();
        assertNotNull(dto);
        assertEquals("test-field", dto.getField());
        assertEquals("test-expected", dto.getExpected());
        assertEquals("test-actual", dto.getActual());
        assertEquals("test-message", dto.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceCheck.CheckViolationDetail dto = new ComplianceCheck.CheckViolationDetail();
        dto.setField("val-field");
        dto.setExpected("val-expected");
        dto.setActual("val-actual");
        dto.setMessage("val-message");
        assertEquals("val-field", dto.getField());
        assertEquals("val-expected", dto.getExpected());
        assertEquals("val-actual", dto.getActual());
        assertEquals("val-message", dto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheck.CheckViolationDetail dto1 = ComplianceCheck.CheckViolationDetail.builder()
                        .field("test-field")
            .expected("test-expected")
            .actual("test-actual")
            .message("test-message")
            .build();
        ComplianceCheck.CheckViolationDetail dto2 = ComplianceCheck.CheckViolationDetail.builder()
                        .field("test-field")
            .expected("test-expected")
            .actual("test-actual")
            .message("test-message")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceCheck.CheckViolationDetail dto = ComplianceCheck.CheckViolationDetail.builder()
                        .field("test-field")
            .expected("test-expected")
            .actual("test-actual")
            .message("test-message")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}