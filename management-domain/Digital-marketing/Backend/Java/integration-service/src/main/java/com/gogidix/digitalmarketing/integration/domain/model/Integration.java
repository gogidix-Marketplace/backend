package com.gogidix.digitalmarketing.integration.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "integrations")
public class Integration {

    @Id
    private String id;
    private String tenantId;
    private String name;
     private String type;
     private String provider;
     private String status;
     private String webhookUrl;
     private String apiKey;
     private String configuration;

    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;

    public Integration(String tenantId, String name , String type , String provider , String status , String webhookUrl , String apiKey , String configuration) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.name = name;
         this.type = type;
         this.provider = provider;
         this.status = status;
         this.webhookUrl = webhookUrl;
         this.apiKey = apiKey;
         this.configuration = configuration;

        this.status = "ACTIVE";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}