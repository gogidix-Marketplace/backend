package com.gogidix.aiservices.aireporting.domain;

import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aireporting.domain.model.*;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ReportGeneratorTest {
    private ReportGenerator gen;

    @BeforeEach
    void setup() { gen = new ReportGenerator(); }

    @Test
    void generateReport() {
        var req = ReportRequest.builder()
            .reportId("id")
            .type(ReportType.SUMMARY)
            .format(ExportFormat.PDF)
            .startDate(LocalDateTime.now().minusDays(1))
            .endDate(LocalDateTime.now())
            .includeMetrics(List.of("revenue"))
            .build();
        var report = gen.generateReport(req);
        assertThat(report).isNotNull();
    }
}
