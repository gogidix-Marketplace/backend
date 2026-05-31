package com.gogidix.ecommerce.vendor.analytics.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "vendor_analytics")
public class VendorAnalytics {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String vendorId;
    private String name;
    private java.time.Instant createdAt;
    private java.time.Instant updatedAt;
}