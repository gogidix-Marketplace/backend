package com.gogidix.ecommerce.sms.infrastructure.persistence.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "sms")
public class SmsDocument {

    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String type;
    private boolean active;
    private int priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}