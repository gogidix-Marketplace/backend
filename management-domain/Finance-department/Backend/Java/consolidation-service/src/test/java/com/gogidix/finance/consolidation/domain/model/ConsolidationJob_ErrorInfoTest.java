package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationJob;
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
class ConsolidationJob_ErrorInfoTest {

        @Test
    void testBuilder() {
        ConsolidationJob.ErrorInfo dto = ConsolidationJob.ErrorInfo.builder()
                        .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .errorType("test-errorType")
            .errorTime(Instant.parse("2025-01-15T10:00:00Z"))
            .stackTrace("test-stackTrace")
            .failedStep("test-failedStep")
            .context(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-errorCode", dto.getErrorCode());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertEquals("test-errorType", dto.getErrorType());
        assertEquals("test-stackTrace", dto.getStackTrace());
        assertEquals("test-failedStep", dto.getFailedStep());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationJob.ErrorInfo dto = new ConsolidationJob.ErrorInfo();
        dto.setErrorCode("val-errorCode");
        dto.setErrorMessage("val-errorMessage");
        dto.setErrorType("val-errorType");
        dto.setStackTrace("val-stackTrace");
        dto.setFailedStep("val-failedStep");
        assertEquals("val-errorCode", dto.getErrorCode());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertEquals("val-errorType", dto.getErrorType());
        assertEquals("val-stackTrace", dto.getStackTrace());
        assertEquals("val-failedStep", dto.getFailedStep());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationJob.ErrorInfo dto1 = ConsolidationJob.ErrorInfo.builder()
                        .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .errorType("test-errorType")
            .errorTime(Instant.parse("2025-01-15T10:00:00Z"))
            .stackTrace("test-stackTrace")
            .failedStep("test-failedStep")
            .context(Collections.emptyMap())
            .build();
        ConsolidationJob.ErrorInfo dto2 = ConsolidationJob.ErrorInfo.builder()
                        .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .errorType("test-errorType")
            .errorTime(Instant.parse("2025-01-15T10:00:00Z"))
            .stackTrace("test-stackTrace")
            .failedStep("test-failedStep")
            .context(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationJob.ErrorInfo dto = ConsolidationJob.ErrorInfo.builder()
                        .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .errorType("test-errorType")
            .errorTime(Instant.parse("2025-01-15T10:00:00Z"))
            .stackTrace("test-stackTrace")
            .failedStep("test-failedStep")
            .context(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}