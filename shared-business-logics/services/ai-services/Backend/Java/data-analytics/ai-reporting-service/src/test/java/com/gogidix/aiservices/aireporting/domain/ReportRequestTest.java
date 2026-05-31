package com.gogidix.aiservices.aireporting.domain;

import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;

class ReportRequestTest {
    @Test
    void builder() {
        var req = ReportRequest.builder()
            .reportId("id")
            .type(ReportType.SUMMARY)
            .format(ExportFormat.PDF)
            .startDate(LocalDateTime.now().minusDays(1))
            .endDate(LocalDateTime.now())
            .build();
        assertThat(req.getReportId()).isEqualTo("id");
    }

    @Test
    void isValidDateRange() {
        var req = ReportRequest.builder()
            .reportId("id")
            .type(ReportType.SUMMARY)
            .format(ExportFormat.PDF)
            .startDate(LocalDateTime.now().minusDays(1))
            .endDate(LocalDateTime.now())
            .build();
        assertThat(req.isValidDateRange()).isTrue();
    }

    @Test
    void invalidDateRange() {
        var req = ReportRequest.builder()
            .reportId("id")
            .type(ReportType.DETAILED)
            .format(ExportFormat.CSV)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().minusDays(1))
            .build();
        assertThat(req.isValidDateRange()).isFalse();
    }
}
