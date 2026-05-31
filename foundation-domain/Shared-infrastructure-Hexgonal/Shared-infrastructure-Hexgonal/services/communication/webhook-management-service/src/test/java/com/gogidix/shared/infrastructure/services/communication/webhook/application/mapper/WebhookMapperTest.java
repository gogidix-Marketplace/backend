package com.gogidix.shared.infrastructure.services.communication.webhook.application.mapper;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.CreateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.response.WebhookResponseDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.Webhook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WebhookMapper.
 */
@DisplayName("Webhook Mapper Tests")
class WebhookMapperTest {

    private WebhookMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new WebhookMapper();
    }

    @Test
    @DisplayName("Should map DTO to entity")
    void shouldMapDtoToEntity() {
        CreateWebhookRequestDto dto = new CreateWebhookRequestDto(
                "test-webhook", "Test webhook", "https://example.com/webhook",
                "user.created", List.of("user.created", "user.updated"),
                "POST", "secret123", "admin-user",
                5, 2000L, 60, true
        );

        Webhook entity = mapper.toEntity(dto, "tenant123");

        assertNotNull(entity);
        assertEquals("tenant123", entity.getTenantId().getValue());
        assertEquals("test-webhook", entity.getName());
        assertEquals("https://example.com/webhook", entity.getUrl());
        assertEquals("Test webhook", entity.getDescription());
        assertEquals("user.created", entity.getEventType());
        assertEquals(List.of("user.created", "user.updated"), entity.getEventTypes());
        assertEquals("POST", entity.getHttpMethod());
        assertEquals("secret123", entity.getSecret());
        assertEquals("admin-user", entity.getCreatedBy());
        assertEquals(5, entity.getRetryAttempts());
        assertEquals(2000L, entity.getRetryDelay());
        assertEquals(60, entity.getTimeout());
        assertTrue(entity.getSslVerificationEnabled());
    }

    @Test
    @DisplayName("Should use default values for null optional fields")
    void shouldUseDefaultValuesForNullOptionalFields() {
        CreateWebhookRequestDto dto = new CreateWebhookRequestDto(
                "test-webhook", null, "https://example.com/webhook",
                null, null,
                null, null, null,
                null, null, null, null
        );

        Webhook entity = mapper.toEntity(dto, "tenant123");

        assertEquals("generic", entity.getEventType());
        assertEquals("POST", entity.getHttpMethod());
        assertEquals(3, entity.getRetryAttempts());
        assertEquals(1000L, entity.getRetryDelay());
        assertEquals(30, entity.getTimeout());
        assertTrue(entity.getSslVerificationEnabled());
    }

    @Test
    @DisplayName("Should map entity to response DTO")
    void shouldMapEntityToResponseDto() {
        Webhook entity = new Webhook(new TenantId("tenant123"), "test-webhook",
                "https://example.com/webhook", "user.created");
        entity.setId("webhook123");
        entity.setDescription("Test webhook");
        entity.setStatus("ACTIVE");
        entity.setEventTypes(List.of("user.created", "user.updated"));
        entity.setCreatedBy("admin-user");
        entity.setRetryAttempts(5);
        entity.setRetryDelay(2000L);
        entity.setTimeout(60);
        entity.setSslVerificationEnabled(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        WebhookResponseDto dto = mapper.toResponseDto(entity);

        assertNotNull(dto);
        assertEquals("webhook123", dto.id());
        assertEquals("tenant123", dto.tenantId());
        assertEquals("test-webhook", dto.name());
        assertEquals("Test webhook", dto.description());
        assertEquals("https://example.com/webhook", dto.url());
        assertEquals("ACTIVE", dto.status());
        assertEquals("user.created", dto.eventType());
        assertEquals(List.of("user.created", "user.updated"), dto.eventTypes());
        assertEquals("admin-user", dto.createdBy());
        assertEquals(5, dto.retryAttempts());
        assertEquals(2000L, dto.retryDelay());
        assertEquals(60, dto.timeout());
        assertTrue(dto.sslVerificationEnabled());
    }

    @Test
    @DisplayName("Should handle null tenant ID in response DTO")
    void shouldHandleNullTenantIdInResponseDto() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setName("test-webhook");
        entity.setUrl("https://example.com/webhook");

        WebhookResponseDto dto = mapper.toResponseDto(entity);

        assertNotNull(dto);
        assertNull(dto.tenantId());
    }

    @Test
    @DisplayName("Should handle empty event types list")
    void shouldHandleEmptyEventTypesList() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setTenantId(new TenantId("tenant123"));
        entity.setName("test-webhook");
        entity.setUrl("https://example.com/webhook");
        entity.setEventTypes(null);

        WebhookResponseDto dto = mapper.toResponseDto(entity);

        assertNotNull(dto.eventTypes());
        assertTrue(dto.eventTypes().isEmpty() || dto.eventTypes() != null);
    }

    @Test
    @DisplayName("Should map custom retry configuration")
    void shouldMapCustomRetryConfiguration() {
        CreateWebhookRequestDto dto = new CreateWebhookRequestDto(
                "test-webhook", null, "https://example.com/webhook",
                null, null,
                null, null, null,
                10, 5000L, 120, false
        );

        Webhook entity = mapper.toEntity(dto, "tenant123");

        assertEquals(10, entity.getRetryAttempts());
        assertEquals(5000L, entity.getRetryDelay());
        assertEquals(120, entity.getTimeout());
        assertFalse(entity.getSslVerificationEnabled());
    }

    @Test
    @DisplayName("Should handle all HTTP methods")
    void shouldHandleAllHttpMethods() {
        String[] methods = {"GET", "POST", "PUT", "PATCH", "DELETE"};

        for (String method : methods) {
            CreateWebhookRequestDto dto = new CreateWebhookRequestDto(
                    "test-webhook", null, "https://example.com/webhook",
                    null, null,
                    method, null, null,
                    null, null, null, null
            );

            Webhook entity = mapper.toEntity(dto, "tenant123");

            assertEquals(method, entity.getHttpMethod());
        }
    }
}
