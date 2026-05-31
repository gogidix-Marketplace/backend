package com.gogidix.centralizeddashboard.reporting.service;

import com.gogidix.centralizeddashboard.reporting.dto.ExportRequest;
import com.gogidix.centralizeddashboard.reporting.dto.ExportResponse;
import com.gogidix.centralizeddashboard.reporting.exception.ExportException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    @Mock
    private DataFetchService dataFetchService;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ExportService exportService;

    @Test
    void exportReport_csvFormat_returnsResponse() {
        ExportRequest request = ExportRequest.builder()
                .reportType("sales").format("csv").dataSource("db").build();

        when(dataFetchService.fetchReportData(any(ExportRequest.class)))
                .thenReturn(com.gogidix.centralizeddashboard.reporting.dto.ReportData.builder()
                        .reportId("r1").reportName("Sales")
                        .columns(List.of("id", "name"))
                        .data(List.of(java.util.Map.of("id", 1, "name", "test")))
                        .build());
        when(fileStorageService.storeFile(anyString(), anyString(), any()))
                .thenReturn("https://storage.example.com/reports/test.csv");

        ExportResponse response = exportService.exportReport(request);

        assertNotNull(response);
        assertNotNull(response.getExportId());
        assertEquals("https://storage.example.com/reports/test.csv", response.getDownloadUrl());
        assertTrue(response.getFilename().endsWith(".csv"));
        assertEquals("text/csv", response.getContentType());
    }

    @Test
    void exportReport_excelFormat_throwsException() {
        ExportRequest request = ExportRequest.builder()
                .reportType("sales").format("excel").dataSource("db").build();

        when(dataFetchService.fetchReportData(any(ExportRequest.class)))
                .thenReturn(com.gogidix.centralizeddashboard.reporting.dto.ReportData.builder()
                        .reportId("r1").columns(List.of("id")).data(List.of()).build());

        assertThrows(ExportException.class, () -> exportService.exportReport(request));
    }

    @Test
    void exportReport_pdfFormat_throwsException() {
        ExportRequest request = ExportRequest.builder()
                .reportType("sales").format("pdf").dataSource("db").build();

        when(dataFetchService.fetchReportData(any(ExportRequest.class)))
                .thenReturn(com.gogidix.centralizeddashboard.reporting.dto.ReportData.builder()
                        .reportId("r1").columns(List.of("id")).data(List.of()).build());

        assertThrows(ExportException.class, () -> exportService.exportReport(request));
    }

    @Test
    void exportReport_unsupportedFormat_throwsException() {
        ExportRequest request = ExportRequest.builder()
                .reportType("sales").format("xml").dataSource("db").build();

        when(dataFetchService.fetchReportData(any(ExportRequest.class)))
                .thenReturn(com.gogidix.centralizeddashboard.reporting.dto.ReportData.builder()
                        .reportId("r1").columns(List.of("id")).data(List.of()).build());

        assertThrows(ExportException.class, () -> exportService.exportReport(request));
    }

    @Test
    void scheduleExport_returnsScheduleId() {
        ExportRequest request = ExportRequest.builder().reportType("sales").build();
        String scheduleId = exportService.scheduleExport(request, "test@example.com");
        assertNotNull(scheduleId);
    }

    @Test
    void getRecentExports_returnsEmptyList() {
        List<ExportResponse> exports = exportService.getRecentExports("user1", 10);
        assertNotNull(exports);
        assertTrue(exports.isEmpty());
    }
}
