package com.gogidix.aiservices.aireporting.application.service;

import com.gogidix.aiservices.aireporting.application.dto.request.GenerateReportRequest;
import com.gogidix.aiservices.aireporting.application.dto.response.ReportResponse;
import com.gogidix.aiservices.aireporting.domain.aggregate.ReportGeneration;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.domain.port.out.ReportRepository;
import com.gogidix.aiservices.aireporting.shared.exception.ReportNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Report Service Application Tests")
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Should generate report successfully")
    void shouldGenerateReport() {
        GenerateReportRequest request = GenerateReportRequest.builder()
                .type(ReportType.SUMMARY)
                .dateRange(Map.of("start", Instant.now(), "end", Instant.now()))
                .includeMetrics(List.of("metric1"))
                .format(ExportFormat.PDF)
                .build();

        ReportGeneration generation = ReportGeneration.create(ReportType.SUMMARY, List.of("metric1"), ExportFormat.PDF);
        when(reportRepository.save(any())).thenReturn(generation);
        when(reportRepository.findById(any())).thenReturn(Optional.of(generation));

        ReportResponse response = reportService.generateReport(request);

        assertThat(response).isNotNull();
        verify(reportRepository).save(any());
    }

    @Test
    @DisplayName("Should throw when report not found")
    void shouldThrowWhenNotFound() {
        when(reportRepository.findById("report-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.getReport("report-1"))
                .isInstanceOf(ReportNotFoundException.class);
    }
}
