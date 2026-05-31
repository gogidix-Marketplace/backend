package com.gogidix.transaction.status.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Controller DTO Tests")
class ControllerDtoTest {

    @Test
    @DisplayName("Should set and get StatusBroadcastRequest")
    void shouldSetAndGetStatusBroadcastRequest() {
        StatusBroadcastController.StatusBroadcastRequest req = new StatusBroadcastController.StatusBroadcastRequest();
        req.setTransactionId("tx-1");
        req.setStatus("COMPLETED");
        req.setData(Map.of("key", "value"));
        assertEquals("tx-1", req.getTransactionId());
        assertEquals("COMPLETED", req.getStatus());
        assertNotNull(req.getData());
    }

    @Test
    @DisplayName("Should set and get BroadcastAllRequest")
    void shouldSetAndGetBroadcastAllRequest() {
        StatusBroadcastController.BroadcastAllRequest req = new StatusBroadcastController.BroadcastAllRequest();
        req.setMessageType("ALERT");
        req.setData(Map.of("msg", "hello"));
        assertEquals("ALERT", req.getMessageType());
        assertNotNull(req.getData());
    }

    @Test
    @DisplayName("Should set and get BroadcastStats")
    void shouldSetAndGetBroadcastStats() {
        StatusBroadcastController.BroadcastStats stats = new StatusBroadcastController.BroadcastStats();
        stats.setActiveSubscribers(5L);
        stats.setTotalSubscribers(10L);
        assertEquals(5L, stats.getActiveSubscribers());
        assertEquals(10L, stats.getTotalSubscribers());
    }
}
