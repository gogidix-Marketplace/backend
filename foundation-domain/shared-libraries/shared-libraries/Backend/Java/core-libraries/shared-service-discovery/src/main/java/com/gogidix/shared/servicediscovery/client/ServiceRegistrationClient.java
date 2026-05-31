package com.gogidix.shared.servicediscovery.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.servicediscovery.interfaces.dto.RegisterServiceRequest;
import com.gogidix.shared.servicediscovery.interfaces.dto.ServiceRegistrationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Auto-Registration Client for Domain Services
 *
 * <p>This client automatically registers services with the Foundation service registry
 * on startup and maintains heartbeat updates. All domain services use this client
 * by simply adding the dependency and configuring properties.</p>
 *
 * <p>Usage: Add shared-service-discovery dependency and configure:</p>
 * <pre>
 * gogidix:
 *   service-discovery:
 *     enabled: true
 *     service-name: my-service
 *     domain: BUSINESS
 *     capabilities: courier,tracking
 * </pre>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "gogidix.service-discovery.enabled", havingValue = "true", matchIfMissing = true)
public class ServiceRegistrationClient {

    @Value("${gogidix.service-discovery.registry-url:http://localhost:8761}")
    private String registryUrl;

    @Value("${gogidix.service-discovery.service-name:${spring.application.name}}")
    private String serviceName;

    @Value("${gogidix.service-discovery.domain:FOUNDATION}")
    private String domain;

    @Value("${gogidix.service-discovery.tenant-id:default}")
    private String tenantId;

    @Value("${server.port:8080}")
    private int port;

    @Value("${gogidix.service-discovery.protocol:http}")
    private String protocol;

    @Value("${gogidix.service-discovery.health-check-path:/actuator/health}")
    private String healthCheckPath;

    @Value("${gogidix.service-discovery.api-version:1.0.0}")
    private String apiVersion;

    @Value("${gogidix.service-discovery.capabilities:}")
    private List<String> capabilities;

    @Value("${gogidix.service-discovery.tags:}")
    private List<String> tags;

    @Value("${gogidix.service-discovery.auth-methods:JWT}")
    private List<String> authMethods;

    @Value("${gogidix.service-discovery.auto-register:true}")
    private boolean autoRegister;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AtomicBoolean registered = new AtomicBoolean(false);
    private String instanceId;
    private String serviceUrl;

    public ServiceRegistrationClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void initialize() {
        if (!autoRegister) {
            log.info("Service discovery auto-registration disabled");
            return;
        }

        try {
            // Determine host address
            String host = InetAddress.getLocalHost().getHostAddress();
            this.serviceUrl = protocol + "://" + host + ":" + port;

            log.info("Initializing service registration: {} @ {}", serviceName, serviceUrl);

            // Register service
            register();

        } catch (Exception e) {
            log.error("Failed to initialize service registration", e);
        }
    }

    /**
     * Register service with the registry
     */
    public void register() {
        try {
            RegisterServiceRequest request = buildRegistrationRequest();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RegisterServiceRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ServiceRegistrationResponse> response = restTemplate.exchange(
                    registryUrl + "/api/v1/services",
                    HttpMethod.POST,
                    entity,
                    ServiceRegistrationResponse.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                ServiceRegistrationResponse registration = response.getBody();
                if (registration != null) {
                    this.instanceId = registration.getInstanceId();
                    this.registered.set(true);
                    log.info("Service registered successfully: {} (instance: {})",
                            serviceName, instanceId);
                }
            }

        } catch (Exception e) {
            log.error("Failed to register service: {}", serviceName, e);
            // Retry will happen via scheduled method
        }
    }

    /**
     * Send heartbeat to keep service registration alive
     */
    @Scheduled(fixedRate = 30000, initialDelay = 30000)
    public void heartbeat() {
        if (!registered.get() || instanceId == null) {
            // Not registered, try to register
            register();
            return;
        }

        try {
            RegisterServiceRequest request = new RegisterServiceRequest();
            request.setHost(getHostAddress());
            request.setPort(port);
            request.setHealthCheckUrl(protocol + "://" + getHostAddress() + ":" + port + healthCheckPath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RegisterServiceRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<?> response = restTemplate.exchange(
                    registryUrl + "/api/v1/services/" + instanceId,
                    HttpMethod.PUT,
                    entity,
                    Object.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Heartbeat failed, will attempt re-registration");
                registered.set(false);
            }

        } catch (Exception e) {
            log.warn("Heartbeat failed: {}", e.getMessage());
            registered.set(false);
        }
    }

    /**
     * Deregister service on shutdown
     */
    @PreDestroy
    public void deregister() {
        if (!registered.get() || instanceId == null) {
            return;
        }

        try {
            restTemplate.delete(registryUrl + "/api/v1/services/" + instanceId);
            log.info("Service deregistered: {}", instanceId);
        } catch (Exception e) {
            log.error("Failed to deregister service: {}", instanceId, e);
        }
    }

    /**
     * Get service instance ID
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Check if service is registered
     */
    public boolean isRegistered() {
        return registered.get();
    }

    /**
     * Get service URL
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    // ==============================================
    // PRIVATE METHODS
    // ==============================================

    private RegisterServiceRequest buildRegistrationRequest() {
        RegisterServiceRequest request = new RegisterServiceRequest();

        request.setServiceName(serviceName);
        request.setServiceDomain(parseServiceDomain(domain));
        request.setTenantId(tenantId);
        request.setHost(getHostAddress());
        request.setPort(port);
        request.setProtocol(protocol);
        request.setHealthCheckUrl(protocol + "://" + getHostAddress() + ":" + port + healthCheckPath);
        request.setApiVersion(apiVersion);
        request.setRequiresAuth(true);
        request.setCapabilities(capabilities != null ? new HashSet<>(capabilities) : new HashSet<>());
        request.setTags(tags != null ? new HashSet<>(tags) : new HashSet<>());
        request.setAuthMethods(authMethods != null ? new HashSet<>(authMethods) : new HashSet<>(Set.of("JWT")));

        return request;
    }

    private String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }

    private com.gogidix.shared.servicediscovery.domain.model.ServiceMetadata.ServiceDomain parseServiceDomain(String domain) {
        try {
            return com.gogidix.shared.servicediscovery.domain.model.ServiceMetadata.ServiceDomain.valueOf(domain);
        } catch (IllegalArgumentException e) {
            return com.gogidix.shared.servicediscovery.domain.model.ServiceMetadata.ServiceDomain.FOUNDATION;
        }
    }
}
