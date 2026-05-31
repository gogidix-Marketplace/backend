package com.gogidix.centralizeddashboard.reporting.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExportRequestTest {

    @Test
    void builderCreatesInstance() {
        ExportRequest req = ExportRequest.builder()
                .type("csv").reportType("sales").dataSource("db")
                .startDate(LocalDateTime.now()).endDate(LocalDateTime.now())
                .format("csv").fileName("report.csv")
                .emailTo("test@example.com").scheduled(true)
                .scheduleCron("0 0 * * *").build();

        assertEquals("csv", req.getType());
        assertEquals("sales", req.getReportType());
        assertEquals("db", req.getDataSource());
        assertEquals("csv", req.getFormat());
    }

    @Test
    void noArgsConstructor() {
        ExportRequest req = new ExportRequest();
        assertNull(req.getType());
    }

    @Test
    void allArgsConstructor() {
        ExportRequest req = new ExportRequest("csv", "sales", "db", null, null,
                null, null, "csv", "file.csv", "a@b.com", true, "cron");
        assertEquals("csv", req.getType());
        assertEquals("sales", req.getReportType());
    }

    @Test
    void getReportType_fallsBackToType() {
        ExportRequest req = ExportRequest.builder().type("csv").build();
        assertEquals("csv", req.getReportType());
    }

    @Test
    void getReportType_returnsReportTypeWhenSet() {
        ExportRequest req = ExportRequest.builder().type("csv").reportType("sales").build();
        assertEquals("sales", req.getReportType());
    }

    @Test
    void getFormat_fallsBackToType() {
        ExportRequest req = ExportRequest.builder().type("pdf").build();
        assertEquals("pdf", req.getFormat());
    }

    @Test
    void getFormat_returnsFormatWhenSet() {
        ExportRequest req = ExportRequest.builder().type("csv").format("excel").build();
        assertEquals("excel", req.getFormat());
    }

    @Test
    void settersWork() {
        ExportRequest req = new ExportRequest();
        req.setType("csv");
        req.setReportType("sales");
        req.setDataSource("db");
        req.setFormat("csv");
        req.setEmailTo("test@test.com");
        req.setScheduled(true);

        assertEquals("csv", req.getType());
        assertTrue(req.isScheduled());
    }
}
