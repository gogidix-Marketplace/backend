package com.gogidix.centralizeddashboard.reporting.service;

import com.gogidix.centralizeddashboard.reporting.dto.ExportRequest;
import com.gogidix.centralizeddashboard.reporting.dto.ReportData;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataFetchServiceTest {

    private final DataFetchService service = new DataFetchService();

    @Test
    void fetchData_returnsStubData() {
        ReportData data = service.fetchData("test-source", "2025-01-01", "2025-01-31",
                new String[]{"revenue"}, new String[]{"region"});
        assertNotNull(data);
        assertEquals("test-source", data.getDataSource());
    }

    @Test
    void fetchReportData_returnsSampleData() {
        ExportRequest request = ExportRequest.builder()
                .reportType("sales").dataSource("db").build();

        ReportData data = service.fetchReportData(request);

        assertNotNull(data);
        assertEquals("sales Report", data.getReportName());
        assertEquals("db", data.getDataSource());
        assertNotNull(data.getColumns());
        assertNotNull(data.getData());
        assertEquals(10, data.getData().size());
        assertTrue(data.getColumns().contains("id"));
        assertTrue(data.getColumns().contains("name"));
    }
}
