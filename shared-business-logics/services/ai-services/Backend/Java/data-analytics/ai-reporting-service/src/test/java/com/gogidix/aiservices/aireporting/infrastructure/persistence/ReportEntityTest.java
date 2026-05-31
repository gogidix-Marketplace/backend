package com.gogidix.aiservices.aireporting.infrastructure.persistence;

import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aireporting.domain.model.*;
import com.gogidix.aiservices.aireporting.domain.aggregate.ReportGeneration;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ReportEntityTest {
    @Test
    void settersAndGetters() {
        var e = new ReportEntity();
        e.setGenerationId("id");
        e.setType(ReportType.SUMMARY);
        e.setIncludeMetrics(List.of("rev"));
        e.setFormat(ExportFormat.PDF);
        e.setDateRangeStart(Instant.now());
        e.setDateRangeEnd(Instant.now());
        e.setStatus(ReportGeneration.GenerationStatus.COMPLETED);
        e.setProgress(100);
        e.setDownloadUrl("http://url");
        e.setExpiresAt(Instant.now());
        e.setCreatedAt(Instant.now());
        e.setCompletedAt(Instant.now());
        assertThat(e.getGenerationId()).isEqualTo("id");
        assertThat(e.getProgress()).isEqualTo(100);
    }
}
