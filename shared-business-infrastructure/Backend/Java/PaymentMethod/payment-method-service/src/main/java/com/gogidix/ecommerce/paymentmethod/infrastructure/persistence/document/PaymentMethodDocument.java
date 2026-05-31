package com.gogidix.ecommerce.paymentmethod.infrastructure.persistence.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "paymentmethod")
public class PaymentMethodDocument {

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