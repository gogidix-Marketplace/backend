package com.gogidix.dashboard.gateway.websocket.interfaces.rest;

import com.gogidix.dashboard.gateway.websocket.application.service.ConnectionRegistry;
import com.gogidix.dashboard.gateway.websocket.infrastructure.redis.RedisPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketControllerTest {

    @Mock
    private ConnectionRegistry connectionRegistry;

    @Mock
    private RedisPublisher redisPublisher;

    @InjectMocks
    private WebSocketController webSocketController;

    @Test
    void getStatistics_returnsOk() {
        when(connectionRegistry.getStatistics()).thenReturn(Map.of("totalConnections", 5));

        ResponseEntity<Map<String, Object>> response = webSocketController.getStatistics();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void broadcast_withAllTenants_publishesWithNullTenant() {
        Map<String, Object> message = Map.of("text", "hello");

        ResponseEntity<Map<String, String>> response = webSocketController.broadcast(message, "all");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("broadcasted", response.getBody().get("status"));
        verify(redisPublisher).publishDashboardUpdate(isNull(), eq(message));
    }

    @Test
    void broadcast_withSpecificTenant_publishesWithTenant() {
        Map<String, Object> message = Map.of("text", "hello");

        ResponseEntity<Map<String, String>> response = webSocketController.broadcast(message, "tenant-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("tenant-1", response.getBody().get("tenantId"));
        verify(redisPublisher).publishDashboardUpdate(eq("tenant-1"), eq(message));
    }

    @Test
    void publishToTopic_dashboard_callsDashboardPublisher() {
        Map<String, Object> data = Map.of("metric", "cpu");

        ResponseEntity<Map<String, String>> response = webSocketController.publishToTopic("dashboard", data, "t1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("published", response.getBody().get("status"));
        verify(redisPublisher).publishDashboardUpdate("t1", data);
    }

    @Test
    void publishToTopic_dashboardUpdates_callsDashboardPublisher() {
        Map<String, Object> data = Map.of("metric", "cpu");
        webSocketController.publishToTopic("dashboard-updates", data, "t1");
        verify(redisPublisher).publishDashboardUpdate("t1", data);
    }

    @Test
    void publishToTopic_saga_callsSagaPublisher() {
        Map<String, Object> data = Map.of("event", "completed");
        webSocketController.publishToTopic("saga", data, "t1");
        verify(redisPublisher).publishSagaEvent("t1", data);
    }

    @Test
    void publishToTopic_sagaEvents_callsSagaPublisher() {
        Map<String, Object> data = Map.of("event", "completed");
        webSocketController.publishToTopic("saga-events", data, "t1");
        verify(redisPublisher).publishSagaEvent("t1", data);
    }

    @Test
    void publishToTopic_chart_callsChartPublisher() {
        Map<String, Object> data = Map.of("chart", "sales");
        webSocketController.publishToTopic("chart", data, "t1");
        verify(redisPublisher).publishChartUpdate("t1", data);
    }

    @Test
    void publishToTopic_chartUpdates_callsChartPublisher() {
        Map<String, Object> data = Map.of("chart", "sales");
        webSocketController.publishToTopic("chart-updates", data, "t1");
        verify(redisPublisher).publishChartUpdate("t1", data);
    }

    @Test
    void publishToTopic_monitoring_callsMonitoringPublisher() {
        Map<String, Object> data = Map.of("alert", "high-cpu");
        webSocketController.publishToTopic("monitoring", data, "t1");
        verify(redisPublisher).publishMonitoringAlert("t1", data);
    }

    @Test
    void publishToTopic_monitoringAlerts_callsMonitoringPublisher() {
        Map<String, Object> data = Map.of("alert", "high-cpu");
        webSocketController.publishToTopic("monitoring-alerts", data, "t1");
        verify(redisPublisher).publishMonitoringAlert("t1", data);
    }

    @Test
    void publishToTopic_unknownTopic_publishesGeneric() {
        Map<String, Object> data = Map.of("custom", "data");

        ResponseEntity<Map<String, String>> response = webSocketController.publishToTopic("custom-topic", data, "t1");

        assertEquals("published", response.getBody().get("status"));
        verify(redisPublisher).publish(eq("topic:custom-topic"), any());
    }
}
