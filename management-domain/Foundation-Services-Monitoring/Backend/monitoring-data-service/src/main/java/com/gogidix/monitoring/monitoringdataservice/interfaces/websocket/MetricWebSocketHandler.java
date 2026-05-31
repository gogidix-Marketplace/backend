package com.gogidix.monitoring.monitoringdataservice.interfaces.websocket;

import com.gogidix.monitoring.monitoringdataservice.application.dto.MetricDataPointResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.mapper.MetricDataPointMapper;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * WebSocket handler for broadcasting real-time metric updates.
 */
@Component
public class MetricWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MetricWebSocketHandler.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final MetricDataPointMapper mapper;

    public MetricWebSocketHandler(SimpMessagingTemplate messagingTemplate, MetricDataPointMapper mapper) {
        this.messagingTemplate = messagingTemplate;
        this.mapper = mapper;
    }

    /**
     * Broadcast a metric to all subscribers of a service.
     *
     * @param tenantId   the tenant ID
     * @param serviceName the service name
     * @param metric     the metric data point
     */
    public void broadcastMetric(String tenantId, String serviceName, MetricDataPoint metric) {
        MetricDataPointResponseDto dto = mapper.toResponseDto(metric);

        // Broadcast to service-specific topic
        String destination = String.format("/topic/metrics/%s/%s", tenantId, serviceName);
        messagingTemplate.convertAndSend(destination, dto);

        log.trace("Broadcast metric to: {}", destination);
    }

    /**
     * Broadcast an alert to all subscribers.
     *
     * @param tenantId   the tenant ID
     * @param alert      the alert message
     */
    public void broadcastAlert(String tenantId, String alert) {
        String destination = String.format("/topic/alerts/%s", tenantId);
        messagingTemplate.convertAndSend(destination, alert);

        log.info("Broadcast alert to: {}", destination);
    }

    /**
     * Send a metric to a specific user.
     *
     * @param userId   the user ID
     * @param metric   the metric data point
     */
    public void sendMetricToUser(String userId, MetricDataPoint metric) {
        MetricDataPointResponseDto dto = mapper.toResponseDto(metric);

        String destination = String.format("/queue/metrics/%s", userId);
        messagingTemplate.convertAndSendToUser(userId, "/metrics", dto);

        log.trace("Sent metric to user: {}", userId);
    }
}
