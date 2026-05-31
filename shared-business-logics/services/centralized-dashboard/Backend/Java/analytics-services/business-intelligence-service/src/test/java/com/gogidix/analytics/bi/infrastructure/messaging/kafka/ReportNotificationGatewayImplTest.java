package com.gogidix.analytics.bi.infrastructure.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import com.gogidix.analytics.bi.domain.model.ReportExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportNotificationGatewayImplTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ReportNotificationGatewayImpl gateway;

    private ReportExecution testExecution;
    private ReportDefinition testReport;

    @BeforeEach
    void setUp() {
        testReport = ReportDefinition.builder()
            .id("report-1")
            .reportName("Test Report")
            .tenantId("tenant-1")
            .build();

        testExecution = ReportExecution.builder()
            .id("exec-1")
            .reportDefinition(testReport)
            .filePath("/reports/exec-1.pdf")
            .build();
    }

    @Nested
    @DisplayName("sendReportReadyNotification Tests")
    class SendReadyTests {

        @Test
        void sendsReadyNotificationSuccessfully() throws Exception {
            when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"REPORT_READY\"}");

            gateway.sendReportReadyNotification(testExecution);

            verify(kafkaTemplate).send(eq("bi.report-notifications"), eq("exec-1"), anyString());
        }

        @Test
        void handlesSerializationExceptionGracefully() throws Exception {
            when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Serialization error") {});

            gateway.sendReportReadyNotification(testExecution);

            verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("sendReportFailedNotification Tests")
    class SendFailedTests {

        @Test
        void sendsFailedNotificationSuccessfully() throws Exception {
            when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"REPORT_FAILED\"}");

            gateway.sendReportFailedNotification(testExecution, "Out of memory");

            verify(kafkaTemplate).send(eq("bi.report-notifications"), eq("exec-1"), anyString());
        }

        @Test
        void handlesSerializationExceptionGracefully() throws Exception {
            when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Serialization error") {});

            gateway.sendReportFailedNotification(testExecution, "Some error");

            verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
        }
    }
}
