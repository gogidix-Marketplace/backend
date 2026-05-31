package com.gogidix.analytics.bi.infrastructure.messaging.kafka;

import com.gogidix.analytics.bi.domain.model.ReportExecution;
import com.gogidix.analytics.bi.domain.port.out.ReportNotificationGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka implementation of ReportNotificationGateway.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReportNotificationGatewayImpl implements ReportNotificationGateway {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendReportReadyNotification(ReportExecution execution) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("eventType", "REPORT_READY");
            notification.put("executionId", execution.getId());
            notification.put("reportId", execution.getReportDefinition().getId());
            notification.put("reportName", execution.getReportDefinition().getReportName());
            notification.put("fileUrl", execution.getFilePath());
            notification.put("tenantId", execution.getReportDefinition().getTenantId());
            notification.put("timestamp", LocalDateTime.now().toString());

            String message = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send("bi.report-notifications", execution.getId(), message);

            log.info("Report ready notification sent: executionId={}", execution.getId());
        } catch (Exception e) {
            log.error("Failed to send report ready notification", e);
        }
    }

    @Override
    public void sendReportFailedNotification(ReportExecution execution, String errorMessage) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("eventType", "REPORT_FAILED");
            notification.put("executionId", execution.getId());
            notification.put("reportId", execution.getReportDefinition().getId());
            notification.put("reportName", execution.getReportDefinition().getReportName());
            notification.put("errorMessage", errorMessage);
            notification.put("tenantId", execution.getReportDefinition().getTenantId());
            notification.put("timestamp", LocalDateTime.now().toString());

            String message = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send("bi.report-notifications", execution.getId(), message);

            log.error("Report failed notification sent: executionId={}", execution.getId());
        } catch (Exception e) {
            log.error("Failed to send report failed notification", e);
        }
    }
}
