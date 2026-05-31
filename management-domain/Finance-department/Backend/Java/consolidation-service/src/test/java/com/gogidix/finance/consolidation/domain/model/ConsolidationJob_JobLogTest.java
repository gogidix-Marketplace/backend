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
class ConsolidationJob_JobLogTest {

        @Test
    void testBuilder() {
        ConsolidationJob.JobLog dto = ConsolidationJob.JobLog.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .level(null)
            .message("test-message")
            .step("test-step")
            .context(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-message", dto.getMessage());
        assertEquals("test-step", dto.getStep());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationJob.JobLog dto = new ConsolidationJob.JobLog();
        dto.setMessage("val-message");
        dto.setStep("val-step");
        assertEquals("val-message", dto.getMessage());
        assertEquals("val-step", dto.getStep());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationJob.JobLog dto1 = ConsolidationJob.JobLog.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .level(null)
            .message("test-message")
            .step("test-step")
            .context(Collections.emptyMap())
            .build();
        ConsolidationJob.JobLog dto2 = ConsolidationJob.JobLog.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .level(null)
            .message("test-message")
            .step("test-step")
            .context(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationJob.JobLog dto = ConsolidationJob.JobLog.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .level(null)
            .message("test-message")
            .step("test-step")
            .context(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}