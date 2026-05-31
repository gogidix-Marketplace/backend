package com.gogidix.shared.infrastructure.services.communication.webhook.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.CreateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.UpdateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.response.WebhookResponseDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.mapper.WebhookMapper;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.exception.WebhookNotFoundException;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.Webhook;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.port.out.IWebhookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WebhookService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Webhook Service Tests")
class WebhookServiceTest {

    @Mock
    private WebhookMapper mapper;

    @Mock
    private IWebhookRepository repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private WebhookService webhookService;

    private static final String TENANT_ID = "tenant123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
    }

    @Test
    @DisplayName("Should create webhook")
    void shouldCreateWebhook() {
        CreateWebhookRequestDto dto = new CreateWebhookRequestDto(
                "test-webhook", "https://example.com/webhook", "Test webhook",
                "user.created", List.of("user.created", "user.updated"),
                "POST", null, "user123", 3, 1000L, 30, true
        );

        Webhook entity = new Webhook();
        entity.setId("webhook123");

        when(repository.existsByNameAndTenantId("test-webhook", TENANT_ID)).thenReturn(false);
        when(mapper.toEntity(dto, TENANT_ID)).thenReturn(entity);
        when(repository.save(any(Webhook.class))).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new WebhookResponseDto(
                "webhook123", TENANT_ID, "test-webhook", "Test webhook",
                "https://example.com/webhook", "ACTIVE", "user.created",
                List.of("user.created", "user.updated"), "POST", "user123",
                3, 1000L, 30, true, LocalDateTime.now(), LocalDateTime.now()
        ));

        WebhookResponseDto response = webhookService.create(dto);

        assertNotNull(response);
        assertEquals("webhook123", response.id());
        verify(repository).existsByNameAndTenantId("test-webhook", TENANT_ID);
        verify(mapper).toEntity(dto, TENANT_ID);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should throw exception when webhook name already exists")
    void shouldThrowExceptionWhenWebhookNameAlreadyExists() {
        CreateWebhookRequestDto dto = new CreateWebhookRequestDto(
                "existing-webhook", "https://example.com/webhook", null,
                null, null, null, null, null, null, null, null, null
        );

        when(repository.existsByNameAndTenantId("existing-webhook", TENANT_ID)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> webhookService.create(dto));
    }

    @Test
    @DisplayName("Should find webhook by ID")
    void shouldFindWebhookById() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setName("test-webhook");

        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new WebhookResponseDto(
                "webhook123", TENANT_ID, "test-webhook", null,
                "https://example.com/webhook", "ACTIVE", "user.created",
                List.of(), "POST", "user123",
                3, 1000L, 30, true, LocalDateTime.now(), LocalDateTime.now()
        ));

        WebhookResponseDto response = webhookService.findById("webhook123");

        assertNotNull(response);
        assertEquals("webhook123", response.id());
        verify(repository).findByIdAndTenantId("webhook123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when webhook not found by ID")
    void shouldThrowExceptionWhenWebhookNotFoundById() {
        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.empty());

        assertThrows(WebhookNotFoundException.class, () -> webhookService.findById("webhook123"));
    }

    @Test
    @DisplayName("Should find all webhooks")
    void shouldFindAllWebhooks() {
        Webhook entity1 = new Webhook();
        entity1.setId("webhook1");
        Webhook entity2 = new Webhook();
        entity2.setId("webhook2");

        when(repository.findAllByTenantId(TENANT_ID)).thenReturn(List.of(entity1, entity2));
        when(mapper.toResponseDto(any())).thenReturn(new WebhookResponseDto(
                "webhook1", TENANT_ID, "webhook1", null,
                "https://example.com/webhook", "ACTIVE", "user.created",
                List.of(), "POST", "user123",
                3, 1000L, 30, true, LocalDateTime.now(), LocalDateTime.now()
        ));

        List<WebhookResponseDto> response = webhookService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("Should find webhooks by status")
    void shouldFindWebhooksByStatus() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setStatus("ACTIVE");

        when(repository.findByTenantIdAndStatus(TENANT_ID, "ACTIVE")).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new WebhookResponseDto(
                "webhook123", TENANT_ID, "webhook", null,
                "https://example.com/webhook", "ACTIVE", "user.created",
                List.of(), "POST", "user123",
                3, 1000L, 30, true, LocalDateTime.now(), LocalDateTime.now()
        ));

        List<WebhookResponseDto> response = webhookService.findByStatus("ACTIVE");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find webhooks by event type")
    void shouldFindWebhooksByEventType() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setEventType("user.created");

        when(repository.findByTenantIdAndEventType(TENANT_ID, "user.created")).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new WebhookResponseDto(
                "webhook123", TENANT_ID, "webhook", null,
                "https://example.com/webhook", "ACTIVE", "user.created",
                List.of(), "POST", "user123",
                3, 1000L, 30, true, LocalDateTime.now(), LocalDateTime.now()
        ));

        List<WebhookResponseDto> response = webhookService.findByEventType("user.created");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find webhooks by creator")
    void shouldFindWebhooksByCreator() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setCreatedBy("user123");

        when(repository.findByTenantIdAndCreatedBy(TENANT_ID, "user123")).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new WebhookResponseDto(
                "webhook123", TENANT_ID, "webhook", null,
                "https://example.com/webhook", "ACTIVE", "user.created",
                List.of(), "POST", "user123",
                3, 1000L, 30, true, LocalDateTime.now(), LocalDateTime.now()
        ));

        List<WebhookResponseDto> response = webhookService.findByCreatedBy("user123");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should update webhook")
    void shouldUpdateWebhook() {
        UpdateWebhookRequestDto dto = new UpdateWebhookRequestDto(
                "test-webhook", "Updated description", "https://example.com/new-webhook", "PUT", null
        );

        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setName("test-webhook");
        entity.setDescription("Old description");
        entity.setUrl("https://example.com/old-webhook");
        entity.setHttpMethod("POST");
        entity.setSecret("old-secret");

        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(Webhook.class))).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new WebhookResponseDto(
                "webhook123", TENANT_ID, "test-webhook", "Updated description",
                "https://example.com/new-webhook", "ACTIVE", "user.created",
                List.of(), "PUT", "user123",
                3, 1000L, 30, true, LocalDateTime.now(), LocalDateTime.now()
        ));

        WebhookResponseDto response = webhookService.update("webhook123", dto);

        assertNotNull(response);
        assertEquals("Updated description", entity.getDescription());
        assertEquals("https://example.com/new-webhook", entity.getUrl());
        assertEquals("PUT", entity.getHttpMethod());
        assertEquals("old-secret", entity.getSecret()); // Secret should not change
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should delete webhook by ID")
    void shouldDeleteWebhookById() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");

        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.of(entity));
        doNothing().when(repository).deleteByTenantIdAndId(TENANT_ID, "webhook123");

        webhookService.delete("webhook123");

        verify(repository).deleteByTenantIdAndId(TENANT_ID, "webhook123");
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent webhook")
    void shouldThrowExceptionWhenDeletingNonExistentWebhook() {
        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.empty());

        assertThrows(WebhookNotFoundException.class, () -> webhookService.delete("webhook123"));
    }

    @Test
    @DisplayName("Should activate webhook")
    void shouldActivateWebhook() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setStatus("INACTIVE");

        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(Webhook.class))).thenReturn(entity);

        webhookService.activate("webhook123");

        assertEquals("ACTIVE", entity.getStatus());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should deactivate webhook")
    void shouldDeactivateWebhook() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setStatus("ACTIVE");

        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(Webhook.class))).thenReturn(entity);

        webhookService.deactivate("webhook123");

        assertEquals("INACTIVE", entity.getStatus());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should trigger webhook successfully")
    void shouldTriggerWebhookSuccessfully() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setStatus("ACTIVE");
        entity.setUrl("https://example.com/webhook");
        entity.setEventType("user.created");
        entity.setRetryAttempts(1);

        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(Webhook.class))).thenReturn(entity);

        webhookService.trigger("webhook123");

        verify(repository).save(entity);
        assertEquals(2, entity.getRetryAttempts()); // Incremented
    }

    @Test
    @DisplayName("Should throw exception when triggering non-active webhook")
    void shouldThrowExceptionWhenTriggeringNonActiveWebhook() {
        Webhook entity = new Webhook();
        entity.setId("webhook123");
        entity.setStatus("INACTIVE");

        when(repository.findByIdAndTenantId("webhook123", TENANT_ID)).thenReturn(Optional.of(entity));

        assertThrows(IllegalStateException.class, () -> webhookService.trigger("webhook123"));
    }
}
