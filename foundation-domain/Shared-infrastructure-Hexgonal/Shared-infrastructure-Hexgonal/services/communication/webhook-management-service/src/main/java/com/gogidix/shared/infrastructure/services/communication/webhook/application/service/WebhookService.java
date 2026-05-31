package com.gogidix.shared.infrastructure.services.communication.webhook.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.CreateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.UpdateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.response.WebhookResponseDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.mapper.WebhookMapper;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.exception.WebhookNotFoundException;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.Webhook;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.port.in.IWebhookUseCase;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.port.out.IWebhookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class WebhookService implements IWebhookUseCase {
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    private final WebhookMapper mapper;
    private final IWebhookRepository repository;
    private final TenantContextHolder tenantContextHolder;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Inject
    public WebhookService(WebhookMapper mapper, IWebhookRepository repository,
                          TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WebhookResponseDto create(CreateWebhookRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        if (repository.existsByNameAndTenantId(dto.name(), tenantId)) {
            throw new IllegalArgumentException("Webhook with name '" + dto.name() + "' already exists");
        }
        Webhook entity = mapper.toEntity(dto, tenantId);
        Webhook saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    public WebhookResponseDto findById(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Webhook entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new WebhookNotFoundException(id));
        return mapper.toResponseDto(entity);
    }

    @Override
    public List<WebhookResponseDto> findAll() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<WebhookResponseDto> findByStatus(String status) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTenantIdAndStatus(tenantId, status).stream()
            .map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<WebhookResponseDto> findByEventType(String eventType) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTenantIdAndEventType(tenantId, eventType).stream()
            .map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<WebhookResponseDto> findByCreatedBy(String createdBy) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTenantIdAndCreatedBy(tenantId, createdBy).stream()
            .map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public WebhookResponseDto update(String id, UpdateWebhookRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Webhook entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new WebhookNotFoundException(id));
        entity.setDescription(dto.description());
        entity.setUrl(dto.url());
        entity.setHttpMethod(dto.httpMethod());
        if (dto.secret() != null) entity.setSecret(dto.secret());
        Webhook updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }

    @Override
    public void delete(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new WebhookNotFoundException(id));
        repository.deleteByTenantIdAndId(tenantId, id);
    }

    @Override
    public void activate(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Webhook entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new WebhookNotFoundException(id));
        entity.setStatus("ACTIVE");
        repository.save(entity);
    }

    @Override
    public void deactivate(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Webhook entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new WebhookNotFoundException(id));
        entity.setStatus("INACTIVE");
        repository.save(entity);
    }

    @Override
    public void trigger(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Webhook entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new WebhookNotFoundException(id));

        if (!"ACTIVE".equals(entity.getStatus())) {
            logger.warn("Cannot trigger webhook {} as it is not active (status: {})", id, entity.getStatus());
            throw new IllegalStateException("Webhook must be active to trigger");
        }

        // Build sample webhook payload
        Map<String, Object> payload = buildSamplePayload();

        // Send webhook delivery
        WebhookDeliveryResult result = deliverWebhook(entity, payload);

        // Update webhook statistics
        entity.setRetryAttempts(entity.getRetryAttempts() + 1);
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);

        logger.info("Webhook {} triggered successfully. Delivery status: {}", id, result.success());
    }

    /**
     * Deliver webhook to configured URL.
     *
     * @param webhook The webhook configuration
     * @param payload The payload to send
     * @return Delivery result
     */
    private WebhookDeliveryResult deliverWebhook(Webhook webhook, Map<String, Object> payload) {
        String eventId = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().toString();

        try {
            // Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Add custom headers from webhook configuration
            if (webhook.getHeaders() != null) {
                for (Webhook.WebhookHeader header : webhook.getHeaders()) {
                    headers.add(header.getKey(), header.getValue());
                }
            }

            // Add standard webhook headers
            headers.add("X-Webhook-ID", eventId);
            headers.add("X-Webhook-Timestamp", timestamp);
            headers.add("X-Webhook-Event-Type", webhook.getEventType());
            headers.add("X-Tenant-ID", webhook.getTenantId() != null ? webhook.getTenantId().getValue() : "");

            // Add HMAC signature if secret is configured
            if (webhook.getSecret() != null && !webhook.getSecret().isEmpty()) {
                String signature = generateHmacSignature(payload, webhook.getSecret());
                headers.add("X-Webhook-Signature", signature);
            }

            // Serialize payload
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Create HTTP entity
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonPayload, headers);

            // Determine HTTP method
            HttpMethod httpMethod = HttpMethod.POST;
            if (webhook.getHttpMethod() != null) {
                try {
                    httpMethod = HttpMethod.valueOf(webhook.getHttpMethod());
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid HTTP method '{}', defaulting to POST", webhook.getHttpMethod());
                }
            }

            // Send webhook request
            logger.info("Delivering webhook {} to {} using {}", webhook.getId(), webhook.getUrl(), httpMethod);
            org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(
                    webhook.getUrl(),
                    httpMethod,
                    new HttpEntity<>(jsonPayload, headers),
                    String.class
            );

            // Check response status
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Webhook {} delivered successfully. HTTP status: {}", webhook.getId(), response.getStatusCode());
                return new WebhookDeliveryResult(true, "Delivered", response.getStatusCode().value());
            } else {
                logger.error("Webhook {} delivery failed. HTTP status: {}", webhook.getId(), response.getStatusCode());
                return new WebhookDeliveryResult(false, "Failed: " + response.getStatusCode(), response.getStatusCode().value());
            }

        } catch (Exception e) {
            logger.error("Error delivering webhook {}: {}", webhook.getId(), e.getMessage(), e);
            return new WebhookDeliveryResult(false, "Error: " + e.getMessage(), 500);
        }
    }

    /**
     * Generate HMAC signature for webhook verification.
     *
     * @param payload The webhook payload
     * @param secret The webhook secret for signing
     * @return Base64 encoded HMAC signature
     */
    private String generateHmacSignature(Map<String, Object> payload, String secret) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] signatureBytes = mac.doFinal(jsonPayload.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            logger.error("Failed to generate HMAC signature: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Build a sample webhook payload.
     * In production, this would be populated from actual events.
     *
     * @return Sample payload map
     */
    private Map<String, Object> buildSamplePayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event_id", UUID.randomUUID().toString());
        payload.put("timestamp", LocalDateTime.now().toString());
        payload.put("event_type", "webhook.trigger");
        payload.put("data", Map.of(
                "message", "Webhook triggered successfully",
                "source", "webhook-management-service"
        ));
        return payload;
    }

    /**
     * Result of webhook delivery attempt.
     */
    private record WebhookDeliveryResult(boolean success, String message, int httpStatus) {
    }
}
