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
class ConsolidationJob_JobResultTest {

        @Test
    void testBuilder() {
        ConsolidationJob.JobResult dto = ConsolidationJob.JobResult.builder()
                        .resultId("test-resultId")
            .reportId("test-reportId")
            .success(true)
            .message("test-message")
            .summary(Collections.emptyMap())
            .warnings(Collections.emptyList())
            .generatedReports(Collections.emptyList())
            .consolidatedData(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-resultId", dto.getResultId());
        assertEquals("test-reportId", dto.getReportId());
        assertTrue(dto.getSuccess());
        assertEquals("test-message", dto.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationJob.JobResult dto = new ConsolidationJob.JobResult();
        dto.setResultId("val-resultId");
        dto.setReportId("val-reportId");
        dto.setSuccess(true);
        dto.setMessage("val-message");
        assertEquals("val-resultId", dto.getResultId());
        assertEquals("val-reportId", dto.getReportId());
        assertTrue(dto.getSuccess());
        assertEquals("val-message", dto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationJob.JobResult dto1 = ConsolidationJob.JobResult.builder()
                        .resultId("test-resultId")
            .reportId("test-reportId")
            .success(true)
            .message("test-message")
            .summary(Collections.emptyMap())
            .warnings(Collections.emptyList())
            .generatedReports(Collections.emptyList())
            .consolidatedData(Collections.emptyMap())
            .build();
        ConsolidationJob.JobResult dto2 = ConsolidationJob.JobResult.builder()
                        .resultId("test-resultId")
            .reportId("test-reportId")
            .success(true)
            .message("test-message")
            .summary(Collections.emptyMap())
            .warnings(Collections.emptyList())
            .generatedReports(Collections.emptyList())
            .consolidatedData(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationJob.JobResult dto = ConsolidationJob.JobResult.builder()
                        .resultId("test-resultId")
            .reportId("test-reportId")
            .success(true)
            .message("test-message")
            .summary(Collections.emptyMap())
            .warnings(Collections.emptyList())
            .generatedReports(Collections.emptyList())
            .consolidatedData(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}