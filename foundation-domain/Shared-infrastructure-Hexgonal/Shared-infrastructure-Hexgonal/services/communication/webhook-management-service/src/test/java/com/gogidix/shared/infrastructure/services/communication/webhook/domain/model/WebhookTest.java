package com.gogidix.shared.infrastructure.services.communication.webhook.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Webhook domain model.
 */
@DisplayName("Webhook Domain Model Tests")
class WebhookTest {

    @Test
    @DisplayName("Should create webhook with constructor")
    void shouldCreateWebhookWithConstructor() {
        TenantId tenantId = new TenantId("tenant123");
        Webhook webhook = new Webhook(tenantId, "webhook1", "https://example.com/webhook", "user.created");

        assertEquals(tenantId, webhook.getTenantId());
        assertEquals("webhook1", webhook.getName());
        assertEquals("https://example.com/webhook", webhook.getUrl());
        assertEquals("user.created", webhook.getEventType());
        assertEquals("ACTIVE", webhook.getStatus());
        assertEquals("POST", webhook.getHttpMethod());
        assertEquals(3, webhook.getRetryAttempts());
        assertEquals(1000L, webhook.getRetryDelay());
        assertEquals(30, webhook.getTimeout());
        assertTrue(webhook.getSslVerificationEnabled());
    }

    @Test
    @DisplayName("Should create webhook with no-args constructor")
    void shouldCreateWebhookWithNoArgsConstructor() {
        Webhook webhook = new Webhook();

        assertNotNull(webhook);
        assertNull(webhook.getTenantId());
        assertNull(webhook.getName());
        assertNull(webhook.getUrl());
        assertNull(webhook.getEventType());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        TenantId tenantId = new TenantId("tenant123");
        LocalDateTime now = LocalDateTime.now();
        List<String> eventTypes = List.of("user.created", "user.updated", "user.deleted");
        List<Webhook.WebhookHeader> headers = new ArrayList<>();
        headers.add(new Webhook.WebhookHeader("Authorization", "Bearer token123"));
        headers.add(new Webhook.WebhookHeader("Content-Type", "application/json"));

        Webhook webhook = new Webhook();
        webhook.setTenantId(tenantId);
        webhook.setId("webhook123");
        webhook.setName("test-webhook");
        webhook.setDescription("Test webhook description");
        webhook.setUrl("https://example.com/hook");
        webhook.setStatus("ACTIVE");
        webhook.setEventType("user.created");
        webhook.setEventTypes(eventTypes);
        webhook.setHttpMethod("POST");
        webhook.setHeaders(headers);
        webhook.setSecret("secret123");
        webhook.setCreatedBy("user123");
        webhook.setRetryAttempts(5);
        webhook.setRetryDelay(2000L);
        webhook.setTimeout(60);
        webhook.setSslVerificationEnabled(false);
        webhook.setCreatedAt(now);
        webhook.setUpdatedAt(now);

        assertEquals(tenantId, webhook.getTenantId());
        assertEquals("webhook123", webhook.getId());
        assertEquals("test-webhook", webhook.getName());
        assertEquals("Test webhook description", webhook.getDescription());
        assertEquals("https://example.com/hook", webhook.getUrl());
        assertEquals("ACTIVE", webhook.getStatus());
        assertEquals("user.created", webhook.getEventType());
        assertEquals(eventTypes, webhook.getEventTypes());
        assertEquals("POST", webhook.getHttpMethod());
        assertEquals(headers, webhook.getHeaders());
        assertEquals("secret123", webhook.getSecret());
        assertEquals("user123", webhook.getCreatedBy());
        assertEquals(5, webhook.getRetryAttempts());
        assertEquals(2000L, webhook.getRetryDelay());
        assertEquals(60, webhook.getTimeout());
        assertFalse(webhook.getSslVerificationEnabled());
        assertEquals(now, webhook.getCreatedAt());
        assertEquals(now, webhook.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle different statuses")
    void shouldHandleDifferentStatuses() {
        String[] statuses = {"ACTIVE", "INACTIVE", "DISABLED"};

        for (String status : statuses) {
            Webhook webhook = new Webhook();
            webhook.setStatus(status);

            assertEquals(status, webhook.getStatus());
        }
    }

    @Test
    @DisplayName("Should handle different HTTP methods")
    void shouldHandleDifferentHttpMethods() {
        String[] methods = {"GET", "POST", "PUT", "PATCH", "DELETE"};

        for (String method : methods) {
            Webhook webhook = new Webhook();
            webhook.setHttpMethod(method);

            assertEquals(method, webhook.getHttpMethod());
        }
    }

    @Test
    @DisplayName("Should handle event types list")
    void shouldHandleEventTypesList() {
        Webhook webhook = new Webhook();
        List<String> eventTypes = new ArrayList<>();
        eventTypes.add("order.created");
        eventTypes.add("order.updated");
        eventTypes.add("order.deleted");

        webhook.setEventTypes(eventTypes);

        assertEquals(3, webhook.getEventTypes().size());
        assertTrue(webhook.getEventTypes().contains("order.created"));
    }

    @Test
    @DisplayName("Should handle webhook headers")
    void shouldHandleWebhookHeaders() {
        Webhook webhook = new Webhook();
        List<Webhook.WebhookHeader> headers = new ArrayList<>();

        Webhook.WebhookHeader header1 = new Webhook.WebhookHeader("X-Custom-Header", "custom-value");
        Webhook.WebhookHeader header2 = new Webhook.WebhookHeader("Authorization", "Bearer token");

        headers.add(header1);
        headers.add(header2);

        webhook.setHeaders(headers);

        assertEquals(2, webhook.getHeaders().size());
        assertEquals("X-Custom-Header", webhook.getHeaders().get(0).getKey());
        assertEquals("custom-value", webhook.getHeaders().get(0).getValue());
    }

    @Test
    @DisplayName("Should handle webhook header class")
    void shouldHandleWebhookHeaderClass() {
        Webhook.WebhookHeader header = new Webhook.WebhookHeader("Content-Type", "application/json");

        assertEquals("Content-Type", header.getKey());
        assertEquals("application/json", header.getValue());

        header.setKey("Authorization");
        header.setValue("Bearer token");

        assertEquals("Authorization", header.getKey());
        assertEquals("Bearer token", header.getValue());
    }

    @Test
    @DisplayName("Should handle retry configuration")
    void shouldHandleRetryConfiguration() {
        Webhook webhook = new Webhook();
        webhook.setRetryAttempts(10);
        webhook.setRetryDelay(5000L);

        assertEquals(10, webhook.getRetryAttempts());
        assertEquals(5000L, webhook.getRetryDelay());
    }

    @Test
    @DisplayName("Should handle timeout configuration")
    void shouldHandleTimeoutConfiguration() {
        Webhook webhook = new Webhook();
        webhook.setTimeout(120);

        assertEquals(120, webhook.getTimeout());
    }

    @Test
    @DisplayName("Should handle SSL verification setting")
    void shouldHandleSslVerificationSetting() {
        Webhook webhook = new Webhook();
        webhook.setSslVerificationEnabled(true);

        assertTrue(webhook.getSslVerificationEnabled());

        webhook.setSslVerificationEnabled(false);
        assertFalse(webhook.getSslVerificationEnabled());
    }

    @Test
    @DisplayName("Should handle secret")
    void shouldHandleSecret() {
        Webhook webhook = new Webhook();
        webhook.setSecret("my-webhook-secret");

        assertEquals("my-webhook-secret", webhook.getSecret());
    }

    @Test
    @DisplayName("Should handle created by field")
    void shouldHandleCreatedByField() {
        Webhook webhook = new Webhook();
        webhook.setCreatedBy("admin-user");

        assertEquals("admin-user", webhook.getCreatedBy());
    }

    @Test
    @DisplayName("Should handle description")
    void shouldHandleDescription() {
        Webhook webhook = new Webhook();
        webhook.setDescription("Webhook for user events");

        assertEquals("Webhook for user events", webhook.getDescription());
    }
}
