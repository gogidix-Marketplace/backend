package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * MongoDB document for service registrations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "service_registrations")
@CompoundIndex(name = "tenant_service_idx", def = "{'tenantId': 1, 'serviceName': 1}", unique = true)
@CompoundIndex(name = "tenant_type_status_idx", def = "{'tenantId': 1, 'serviceType': 1, 'status': 1}")
public class ServiceRegistrationDocument {

    @Id
    private String id;

    @Indexed
    private String serviceId;

    @Indexed
    private String tenantId;

    @Indexed
    private String serviceName;

    @Indexed
    private String serviceType;

    @Indexed
    private String category;

    private String version;

    private String description;

    private String baseUrl;

    private String healthEndpoint;

    private String metricsEndpoint;

    private Integer collectionInterval;

    @Indexed
    private Boolean enabled;

    private Map<String, String> tags;

    private Map<String, Object> metadata;

    @Indexed
    private Instant registeredAt;

    @Indexed
    private Instant lastHeartbeat;

    @Indexed
    private String status;
}
