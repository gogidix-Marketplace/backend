package com.gogidix.aiservices.aireporting.domain;

import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;

class ReportDomainTest {
    @Test
    void create() {
        var r = new Report("id", ReportType.SUMMARY, ExportFormat.PDF,
            LocalDateTime.now(), Report.ReportStatus.PROCESSING, null,
            LocalDateTime.now().plusDays(1));
        assertThat(r.getReportId()).isEqualTo("id");
        assertThat(r.isExpired()).isFalse();
    }

    @Test
    void setters() {
        var r = new Report("id", ReportType.DETAILED, ExportFormat.CSV,
            LocalDateTime.now(), Report.ReportStatus.PROCESSING, null, null);
        r.setStatus(Report.ReportStatus.COMPLETED);
        r.setDownloadUrl("http://url");
        r.setErrorMessage("err");
        assertThat(r.getStatus()).isEqualTo(Report.ReportStatus.COMPLETED);
    }

    @Test
    void reportStatusEnum() {
        assertThat(Report.ReportStatus.values()).isNotEmpty();
    }
}
