package com.gogidix.ecommerce.vendor.dropship.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "dropship_orders")
public class Dropship {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    public enum DropshipStatus { PENDING, VENDOR_NOTIFIED, SHIPPED, DELIVERED, CANCELLED, RETURNED }
    private String vendorId;
    private String name;
    private DropshipStatus status;
    private java.time.Instant createdAt;
    private java.time.Instant updatedAt;
}