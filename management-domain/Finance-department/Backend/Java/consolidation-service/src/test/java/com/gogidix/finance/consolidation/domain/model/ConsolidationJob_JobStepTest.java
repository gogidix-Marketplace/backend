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
class ConsolidationJob_JobStepTest {

        @Test
    void testBuilder() {
        ConsolidationJob.JobStep dto = ConsolidationJob.JobStep.builder()
                        .stepId("test-stepId")
            .stepName("test-stepName")
            .status(null)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .message("test-message")
            .metadata(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-stepId", dto.getStepId());
        assertEquals("test-stepName", dto.getStepName());
        assertEquals("test-message", dto.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationJob.JobStep dto = new ConsolidationJob.JobStep();
        dto.setStepId("val-stepId");
        dto.setStepName("val-stepName");
        dto.setMessage("val-message");
        assertEquals("val-stepId", dto.getStepId());
        assertEquals("val-stepName", dto.getStepName());
        assertEquals("val-message", dto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationJob.JobStep dto1 = ConsolidationJob.JobStep.builder()
                        .stepId("test-stepId")
            .stepName("test-stepName")
            .status(null)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .message("test-message")
            .metadata(Collections.emptyMap())
            .build();
        ConsolidationJob.JobStep dto2 = ConsolidationJob.JobStep.builder()
                        .stepId("test-stepId")
            .stepName("test-stepName")
            .status(null)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .message("test-message")
            .metadata(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationJob.JobStep dto = ConsolidationJob.JobStep.builder()
                        .stepId("test-stepId")
            .stepName("test-stepName")
            .status(null)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .message("test-message")
            .metadata(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}