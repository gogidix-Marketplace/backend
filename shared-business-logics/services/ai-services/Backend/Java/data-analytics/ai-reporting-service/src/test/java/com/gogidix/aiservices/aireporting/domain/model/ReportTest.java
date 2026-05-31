package com.gogidix.aiservices.aireporting.domain.model;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class ReportTest {
    @Test
    void create() {
        var r = Report.create(ReportType.SUMMARY, List.of("revenue"));
        assertThat(r.getReportId()).isNotNull();
        assertThat(r.getType()).isEqualTo(ReportType.SUMMARY);
        assertThat(r.getStatus()).isEqualTo(Report.Status.PROCESSING);
    }

    @Test
    void complete() {
        var r = Report.create(ReportType.SUMMARY, List.of("revenue"));
        r.complete("http://download.url");
        assertThat(r.getStatus()).isEqualTo(Report.Status.COMPLETED);
        assertThat(r.getDownloadUrl()).isEqualTo("http://download.url");
    }

    @Test
    void fail() {
        var r = Report.create(ReportType.DETAILED, List.of("revenue"));
        r.fail("error msg");
        assertThat(r.getStatus()).isEqualTo(Report.Status.FAILED);
        assertThat(r.getErrorMessage()).isEqualTo("error msg");
    }

    @Test
    void statusEnum() {
        assertThat(Report.Status.values()).containsExactly(
            Report.Status.PROCESSING, Report.Status.COMPLETED, Report.Status.FAILED);
    }

    @Test
    void reportTypeEnum() {
        assertThat(ReportType.values()).isNotEmpty();
    }

    @Test
    void exportFormatEnum() {
        assertThat(ExportFormat.values()).isNotEmpty();
    }
}
