package com.gogidix.shared.utilities.application.service;

import com.gogidix.shared.utilities.application.port.in.ProcessUtilityPort;
import com.gogidix.shared.utilities.application.port.out.CachePort;
import com.gogidix.shared.utilities.application.port.out.NotificationPort;
import com.gogidix.shared.utilities.domain.model.ProcessingRequest;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.service.DateTimeUtilityService;
import com.gogidix.shared.utilities.domain.service.JsonProcessingService;
import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * Main application service for coordinating utility operations
 * Orchestrates different utility services and manages cross-cutting concerns
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UtilityApplicationService implements ProcessUtilityPort {

    private final DateTimeUtilityService dateTimeUtilityService;
    private final JsonProcessingService jsonProcessingService;
    private final CachePort cachePort;
    private final NotificationPort notificationPort;

    @Override
    public UtilityResult<?> processUtilityRequest(ProcessingRequest request) {
        log.info("Processing utility request: {} of type {}", request.getRequestId(), request.getUtilityType());
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Check if request is expired
            if (request.isExpired()) {
                log.warn("Request {} is expired", request.getRequestId());
                return UtilityResult.failure(request.getRequestId(), request.getUtilityType(), "Request expired");
            }

            // Check cache for cacheable operations
            if (request.getUtilityType().isCacheable()) {
                UtilityResult<?> cachedResult = cachePort.get(generateCacheKey(request));
                if (cachedResult != null) {
                    log.debug("Returning cached result for request {}", request.getRequestId());
                    return cachedResult;
                }
            }

            // Route to appropriate service based on utility type
            UtilityResult<?> result = routeToUtilityService(request);
            
            // Cache successful results for cacheable operations
            if (result.isSuccessful() && request.getUtilityType().isCacheable()) {
                cachePort.put(generateCacheKey(request), result, getCacheTtl(request.getUtilityType()));
            }

            // Update processing time
            long processingTime = System.currentTimeMillis() - startTime;
            result = result.toBuilder()
                    .processingTimeMs(processingTime)
                    .build();

            // Send notification for high-priority or failed operations
            if (request.isHighPriority() || result.isFailed()) {
                notifyRequestCompletion(request, result);
            }

            log.info("Completed utility request {} with status {} in {}ms", 
                    request.getRequestId(), result.getStatus(), processingTime);
            
            return result;
            
        } catch (Exception e) {
            log.error("Error processing utility request {}", request.getRequestId(), e);
            long processingTime = System.currentTimeMillis() - startTime;
            
            return UtilityResult.failure(request.getRequestId(), request.getUtilityType(), e.getMessage())
                    .toBuilder()
                    .processingTimeMs(processingTime)
                    .build();
        }
    }

    @Override
    public CompletableFuture<UtilityResult<?>> processUtilityRequestAsync(ProcessingRequest request) {
        log.info("Processing async utility request: {} of type {}", request.getRequestId(), request.getUtilityType());
        
        return CompletableFuture.<UtilityResult<?>>supplyAsync(() -> processUtilityRequest(request))
                .exceptionally(throwable -> {
                    log.error("Error in async processing of request {}", request.getRequestId(), throwable);
                    return UtilityResult.failure(request.getRequestId(), request.getUtilityType(), throwable.getMessage());
                });
    }

    /**
     * Route request to appropriate utility service
     */
    private UtilityResult<?> routeToUtilityService(ProcessingRequest request) {
        String operationId = request.getRequestId();
        UtilityType utilityType = request.getUtilityType();
        Object data = request.getData();

        return switch (utilityType.getCategory()) {
            case "datetime" -> routeToDateTimeService(operationId, utilityType, data, request);
            case "json" -> routeToJsonService(operationId, utilityType, data, request);
            case "string" -> routeToStringService(operationId, utilityType, data, request);
            case "collection" -> routeToCollectionService(operationId, utilityType, data, request);
            case "file" -> routeToFileService(operationId, utilityType, data, request);
            case "http" -> routeToHttpService(operationId, utilityType, data, request);
            case "validation" -> routeToValidationService(operationId, utilityType, data, request);
            case "mathematical" -> routeToMathService(operationId, utilityType, data, request);
            case "hashing", "encoding", "encryption" -> routeToCryptoService(operationId, utilityType, data, request);
            case "object", "dynamic", "annotation", "class" -> routeToReflectionService(operationId, utilityType, data, request);
            default -> UtilityResult.failure(operationId, utilityType, "Unsupported utility type: " + utilityType);
        };
    }

    /**
     * Route to DateTime service
     */
    private UtilityResult<?> routeToDateTimeService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        return switch (utilityType) {
            case DATETIME_FORMATTING -> {
                if (data instanceof java.time.LocalDateTime) {
                    yield dateTimeUtilityService.formatToIso(operationId, (java.time.LocalDateTime) data);
                } else {
                    yield UtilityResult.failure(operationId, utilityType, "Invalid data type for date formatting");
                }
            }
            case DATETIME_PARSING -> {
                if (data instanceof String) {
                    yield dateTimeUtilityService.parseFromIso(operationId, (String) data);
                } else {
                    yield UtilityResult.failure(operationId, utilityType, "Invalid data type for date parsing");
                }
            }
            case DATETIME_VALIDATION -> {
                if (data instanceof java.time.LocalDateTime) {
                    yield dateTimeUtilityService.isExpired(operationId, (java.time.LocalDateTime) data);
                } else {
                    yield UtilityResult.failure(operationId, utilityType, "Invalid data type for date validation");
                }
            }
            default -> UtilityResult.failure(operationId, utilityType, "Unsupported DateTime operation: " + utilityType);
        };
    }

    /**
     * Route to JSON service
     */
    private UtilityResult<?> routeToJsonService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        return switch (utilityType) {
            case JSON_SERIALIZATION -> jsonProcessingService.toJson(operationId, data);
            case JSON_VALIDATION -> {
                if (data instanceof String) {
                    yield jsonProcessingService.isValidJson(operationId, (String) data);
                } else {
                    yield UtilityResult.failure(operationId, utilityType, "Invalid data type for JSON validation");
                }
            }
            case JSON_TRANSFORMATION -> {
                if (data instanceof String) {
                    yield jsonProcessingService.prettyPrint(operationId, (String) data);
                } else {
                    yield UtilityResult.failure(operationId, utilityType, "Invalid data type for JSON transformation");
                }
            }
            default -> UtilityResult.failure(operationId, utilityType, "Unsupported JSON operation: " + utilityType);
        };
    }

    /**
     * Route to String service (placeholder implementation)
     */
    private UtilityResult<?> routeToStringService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to StringUtilityService
        return UtilityResult.failure(operationId, utilityType, "String operations not yet implemented");
    }

    /**
     * Route to Collection service (placeholder implementation)
     */
    private UtilityResult<?> routeToCollectionService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to CollectionUtilityService
        return UtilityResult.failure(operationId, utilityType, "Collection operations not yet implemented");
    }

    /**
     * Route to File service (placeholder implementation)
     */
    private UtilityResult<?> routeToFileService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to FileUtilityService
        return UtilityResult.failure(operationId, utilityType, "File operations not yet implemented");
    }

    /**
     * Route to HTTP service (placeholder implementation)
     */
    private UtilityResult<?> routeToHttpService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to HttpUtilityService
        return UtilityResult.failure(operationId, utilityType, "HTTP operations not yet implemented");
    }

    /**
     * Route to Validation service (placeholder implementation)
     */
    private UtilityResult<?> routeToValidationService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to ValidationUtilityService
        return UtilityResult.failure(operationId, utilityType, "Validation operations not yet implemented");
    }

    /**
     * Route to Math service (placeholder implementation)
     */
    private UtilityResult<?> routeToMathService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to MathUtilityService
        return UtilityResult.failure(operationId, utilityType, "Mathematical operations not yet implemented");
    }

    /**
     * Route to Crypto service (placeholder implementation)
     */
    private UtilityResult<?> routeToCryptoService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to CryptoUtilityService
        return UtilityResult.failure(operationId, utilityType, "Cryptographic operations not yet implemented");
    }

    /**
     * Route to Reflection service (placeholder implementation)
     */
    private UtilityResult<?> routeToReflectionService(String operationId, UtilityType utilityType, Object data, ProcessingRequest request) {
        // Implementation would route to ReflectionUtilityService
        return UtilityResult.failure(operationId, utilityType, "Reflection operations not yet implemented");
    }

    /**
     * Generate cache key for request
     */
    private String generateCacheKey(ProcessingRequest request) {
        return String.format("utility:%s:%s:%s", 
                request.getUtilityType(), 
                request.getData().hashCode(),
                request.getParameters() != null ? request.getParameters().hashCode() : "null");
    }

    /**
     * Get cache TTL based on utility type
     */
    private long getCacheTtl(UtilityType utilityType) {
        return switch (utilityType) {
            case JSON_SCHEMA_VALIDATION -> 3600; // 1 hour
            case BUSINESS_RULE_VALIDATION -> 1800; // 30 minutes
            case MATHEMATICAL_CALCULATION, STATISTICAL_CALCULATION -> 1800; // 30 minutes
            case OBJECT_INTROSPECTION, CLASS_LOADING -> 7200; // 2 hours
            default -> 600; // 10 minutes default
        };
    }

    /**
     * Send notification for request completion
     */
    private void notifyRequestCompletion(ProcessingRequest request, UtilityResult<?> result) {
        try {
            String message = String.format("Utility request %s completed with status %s", 
                    request.getRequestId(), result.getStatus());
            
            if (result.isFailed()) {
                notificationPort.sendErrorNotification(request.getUserId(), message, result.getErrorMessage());
            } else if (request.isHighPriority()) {
                notificationPort.sendSuccessNotification(request.getUserId(), message);
            }
        } catch (Exception e) {
            log.warn("Failed to send notification for request {}", request.getRequestId(), e);
        }
    }
}