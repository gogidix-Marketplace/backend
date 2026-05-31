package com.gogidix.dashboard.gateway.websocket.application.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WebSocketMessageTest {

    @Test
    void builderCreatesInstance() {
        LocalDateTime now = LocalDateTime.now();
        WebSocketMessage msg = WebSocketMessage.builder()
                .type("dashboard-update")
                .topic("dashboard")
                .tenantId("t1")
                .data(Map.of("key", "value"))
                .timestamp(now)
                .correlationId("corr-1")
                .source("redis")
                .build();

        assertEquals("dashboard-update", msg.getType());
        assertEquals("dashboard", msg.getTopic());
        assertEquals("t1", msg.getTenantId());
        assertEquals(Map.of("key", "value"), msg.getData());
        assertEquals(now, msg.getTimestamp());
        assertEquals("corr-1", msg.getCorrelationId());
        assertEquals("redis", msg.getSource());
    }

    @Test
    void noArgsConstructorCreatesInstance() {
        WebSocketMessage msg = new WebSocketMessage();
        assertNotNull(msg);
        assertNull(msg.getType());
    }

    @Test
    void allArgsConstructorCreatesInstance() {
        WebSocketMessage msg = new WebSocketMessage("type", "topic", "t1",
                "data", LocalDateTime.now(), "corr", "src");
        assertEquals("type", msg.getType());
        assertEquals("topic", msg.getTopic());
    }

    @Test
    void settersWork() {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("ping");
        msg.setTopic("test");
        msg.setTenantId("t2");
        msg.setData(Map.of("x", 1));
        msg.setCorrelationId("c2");
        msg.setSource("api");

        assertEquals("ping", msg.getType());
        assertEquals("test", msg.getTopic());
        assertEquals("t2", msg.getTenantId());
        assertEquals(Map.of("x", 1), msg.getData());
        assertEquals("c2", msg.getCorrelationId());
        assertEquals("api", msg.getSource());
    }

    @Test
    void equalsAndHashCode() {
        WebSocketMessage m1 = WebSocketMessage.builder().type("a").correlationId("c1").build();
        WebSocketMessage m2 = WebSocketMessage.builder().type("a").correlationId("c1").build();
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }
}
