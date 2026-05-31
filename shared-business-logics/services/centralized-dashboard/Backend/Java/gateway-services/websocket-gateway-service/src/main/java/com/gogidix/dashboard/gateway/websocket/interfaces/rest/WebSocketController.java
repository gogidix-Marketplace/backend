package com.gogidix.dashboard.gateway.websocket.interfaces.rest;

import com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage;
import com.gogidix.dashboard.gateway.websocket.application.service.ConnectionRegistry;
import com.gogidix.dashboard.gateway.websocket.infrastructure.redis.RedisPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST controller for WebSocket Gateway operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/websocket")
@RequiredArgsConstructor
@Tag(name = "WebSocket Gateway", description = "WebSocket connection management APIs")
public class WebSocketController {

    private final ConnectionRegistry connectionRegistry;
    private final RedisPublisher redisPublisher;

    /**
     * Get connection statistics
     */
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get WebSocket statistics",
        description = "Returns statistics about active WebSocket connections"
    )
    public ResponseEntity<Map<String, Object>> getStatistics() {
        log.info("GET /api/v1/websocket/statistics");

        Map<String, Object> stats = new java.util.HashMap<>(connectionRegistry.getStatistics());
        stats.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(stats);
    }

    /**
     * Broadcast message to all connected clients
     */
    @PostMapping(value = "/broadcast", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Broadcast message",
        description = "Broadcasts a message to all connected WebSocket clients"
    )
    public ResponseEntity<Map<String, String>> broadcast(
        @Parameter(description = "Message to broadcast") @RequestBody Map<String, Object> message,
        @Parameter(description = "Target tenant") @RequestParam(defaultValue = "all") String tenantId
    ) {
        log.info("POST /api/v1/websocket/broadcast - tenant: {}", tenantId);

        redisPublisher.publishDashboardUpdate("all".equals(tenantId) ? null : tenantId, message);

        return ResponseEntity.ok(Map.of(
            "status", "broadcasted",
            "tenantId", tenantId,
            "timestamp", LocalDateTime.now().toString()
        ));
    }

    /**
     * Send message to specific topic
     */
    @PostMapping(value = "/publish/{topic}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Publish to topic",
        description = "Publishes a message to a specific topic"
    )
    public ResponseEntity<Map<String, String>> publishToTopic(
        @Parameter(description = "Topic name") @PathVariable String topic,
        @Parameter(description = "Message data") @RequestBody Map<String, Object> data,
        @Parameter(description = "Tenant ID") @RequestParam(defaultValue = "default") String tenantId
    ) {
        log.info("POST /api/v1/websocket/publish/{} - tenant: {}", topic, tenantId);

        switch (topic.toLowerCase()) {
            case "dashboard", "dashboard-updates" ->
                redisPublisher.publishDashboardUpdate(tenantId, data);
            case "saga", "saga-events" ->
                redisPublisher.publishSagaEvent(tenantId, data);
            case "chart", "chart-updates" ->
                redisPublisher.publishChartUpdate(tenantId, data);
            case "monitoring", "monitoring-alerts" ->
                redisPublisher.publishMonitoringAlert(tenantId, data);
            default ->
                redisPublisher.publish("topic:" + topic, WebSocketMessage.builder()
                        .type("message")
                        .topic(topic)
                        .tenantId(tenantId)
                        .data(data)
                        .timestamp(java.time.LocalDateTime.now())
                        .build());
        }

        return ResponseEntity.ok(Map.of(
            "status", "published",
            "topic", topic,
            "tenantId", tenantId,
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}
