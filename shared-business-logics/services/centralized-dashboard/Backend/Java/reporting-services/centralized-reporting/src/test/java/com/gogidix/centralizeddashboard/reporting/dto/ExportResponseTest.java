package com.gogidix.centralizeddashboard.reporting.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExportResponseTest {

    @Test
    void builderCreatesInstance() {
        ExportResponse response = ExportResponse.builder()
                .exportId("exp-1")
                .downloadUrl("https://storage.example.com/report.csv")
                .filename("report.csv")
                .contentType("text/csv")
                .fileSize(1024)
                .status("COMPLETED")
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        assertEquals("exp-1", response.getExportId());
        assertEquals("https://storage.example.com/report.csv", response.getDownloadUrl());
        assertEquals("report.csv", response.getFilename());
        assertEquals("text/csv", response.getContentType());
        assertEquals(1024, response.getFileSize());
        assertEquals("COMPLETED", response.getStatus());
    }

    @Test
    void builder_exportIdSetsId() {
        ExportResponse response = ExportResponse.builder().exportId("e1").build();
        assertEquals("e1", response.getExportId());
        assertEquals("e1", response.getId());
    }

    @Test
    void builder_downloadUrlSetsFileUrl() {
        ExportResponse response = ExportResponse.builder()
                .downloadUrl("https://example.com/file").build();
        assertEquals("https://example.com/file", response.getDownloadUrl());
        assertEquals("https://example.com/file", response.getFileUrl());
    }

    @Test
    void noArgsConstructor() {
        ExportResponse response = new ExportResponse();
        assertNull(response.getExportId());
        assertNull(response.getDownloadUrl());
    }

    @Test
    void settersWork() {
        ExportResponse response = new ExportResponse();
        response.setExportId("e2");
        response.setDownloadUrl("url");
        response.setFilename("f.csv");
        response.setContentType("text/csv");
        response.setFileSize(500L);

        assertEquals("e2", response.getExportId());
        assertEquals("url", response.getDownloadUrl());
        assertEquals("f.csv", response.getFilename());
        assertEquals("text/csv", response.getContentType());
        assertEquals(500L, response.getFileSize());
    }
}
