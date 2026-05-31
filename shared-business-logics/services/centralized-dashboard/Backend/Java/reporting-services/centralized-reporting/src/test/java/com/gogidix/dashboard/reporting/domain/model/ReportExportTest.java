package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportExportTest {

    @Test
    void builder_createsExport() {
        ReportExport export = ReportExport.builder()
                .withExportId("exp-1")
                .withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.PDF)
                .withFilePath("/reports/r1.pdf")
                .withFileName("report_r1.pdf")
                .withFileSize(50000)
                .withExportedBy("admin")
                .withExpiresAt(LocalDateTime.now().plusDays(7))
                .build();

        assertEquals("exp-1", export.getExportId());
        assertEquals("r1", export.getReportId().getValue());
        assertEquals(ExportFormat.PDF, export.getFormat());
        assertEquals("/reports/r1.pdf", export.getFilePath());
        assertEquals(50000, export.getFileSize());
        assertFalse(export.isExpired());
    }

    @Test
    void builder_generatesDefaults() {
        ReportExport export = ReportExport.builder()
                .withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.CSV)
                .withFilePath("/reports/r1.csv")
                .build();

        assertNotNull(export.getExportId());
        assertEquals("system", export.getExportedBy());
        assertTrue(export.getFileName().contains("r1"));
        assertEquals(ReportExport.ExportStatus.COMPLETED, export.getStatus());
    }

    @Test
    void builder_nullChecks() {
        assertThrows(NullPointerException.class, () -> ReportExport.builder()
                .withReportId(ReportId.of("r1")).withFormat(ExportFormat.PDF)
                .withFilePath("/x").withFileName("f").withExportedBy("u").build());
    }

    @Test
    void isExpired_futureExpiry() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.PDF).withFilePath("/x").withFileName("f")
                .withExportedBy("u").withExpiresAt(LocalDateTime.now().plusDays(1))
                .build();
        assertFalse(export.isExpired());
    }

    @Test
    void isExpired_pastExpiry() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.PDF).withFilePath("/x").withFileName("f")
                .withExportedBy("u").withExpiresAt(LocalDateTime.now().minusDays(1))
                .build();
        assertTrue(export.isExpired());
    }

    @Test
    void isDownloadable_completedAndNotExpired() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.PDF).withFilePath("/x").withFileName("f")
                .withExportedBy("u").withExpiresAt(LocalDateTime.now().plusDays(1))
                .withStatus(ReportExport.ExportStatus.COMPLETED)
                .build();
        assertTrue(export.isDownloadable());
    }

    @Test
    void isDownloadable_failed() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.PDF).withFilePath("/x").withFileName("f")
                .withExportedBy("u").withStatus(ReportExport.ExportStatus.FAILED)
                .build();
        assertFalse(export.isDownloadable());
    }

    @Test
    void getFileExtension() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.PDF).withFilePath("/x").withFileName("f")
                .withExportedBy("u").build();
        assertEquals("pdf", export.getFileExtension());
    }

    @Test
    void getFormattedFileSize_bytes() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.CSV).withFilePath("/x").withFileName("f")
                .withExportedBy("u").withFileSize(500).build();
        assertEquals("500 B", export.getFormattedFileSize());
    }

    @Test
    void getFormattedFileSize_kb() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.CSV).withFilePath("/x").withFileName("f")
                .withExportedBy("u").withFileSize(2048).build();
        assertEquals("2 KB", export.getFormattedFileSize());
    }

    @Test
    void getFormattedFileSize_mb() {
        ReportExport export = ReportExport.builder()
                .withExportId("e1").withReportId(ReportId.of("r1"))
                .withFormat(ExportFormat.CSV).withFilePath("/x").withFileName("f")
                .withExportedBy("u").withFileSize(5 * 1024 * 1024).build();
        assertEquals("5 MB", export.getFormattedFileSize());
    }
}
