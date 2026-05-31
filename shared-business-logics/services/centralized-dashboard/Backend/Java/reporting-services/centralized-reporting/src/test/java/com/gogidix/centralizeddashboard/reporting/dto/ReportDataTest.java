package com.gogidix.centralizeddashboard.reporting.dto;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportDataTest {

    @Test
    void builderCreatesInstance() {
        ReportData data = ReportData.builder()
                .reportId("r1").reportName("Test Report")
                .dataSource("db").columns(List.of("col1", "col2"))
                .data(List.of(Map.of("col1", "val1", "col2", "val2")))
                .build();

        assertEquals("r1", data.getReportId());
        assertEquals("Test Report", data.getReportName());
        assertEquals("db", data.getDataSource());
        assertEquals(2, data.getColumns().size());
        assertEquals(1, data.getData().size());
    }

    @Test
    void getData_returnsDataWhenNotNull() {
        ReportData data = ReportData.builder()
                .data(List.of(Map.of("k", "v"))).build();
        assertEquals(1, data.getData().size());
    }

    @Test
    void getData_fallsBackToRows() {
        ReportData data = new ReportData();
        data.setRows(List.of(Map.of("k", "v")));
        assertEquals(1, data.getData().size());
    }

    @Test
    void getColumns_returnsColumnsWhenNotNull() {
        ReportData data = ReportData.builder()
                .columns(List.of("a", "b")).build();
        assertEquals(List.of("a", "b"), data.getColumns());
    }

    @Test
    void getColumns_fallsBackToHeaders() {
        ReportData data = new ReportData();
        data.setHeaders(List.of("h1"));
        assertEquals(List.of("h1"), data.getColumns());
    }

    @Test
    void getColumns_returnsEmptyListWhenBothNull() {
        ReportData data = new ReportData();
        assertTrue(data.getColumns().isEmpty());
    }

    @Test
    void settersWork() {
        ReportData data = new ReportData();
        data.setReportId("r2");
        data.setReportName("Name");
        data.setDataSource("source");
        data.setColumns(List.of("c"));
        data.setData(List.of(Map.of("c", "v")));
        data.setMetadata(Map.of("key", "val"));

        assertEquals("r2", data.getReportId());
        assertEquals("Name", data.getReportName());
    }
}
