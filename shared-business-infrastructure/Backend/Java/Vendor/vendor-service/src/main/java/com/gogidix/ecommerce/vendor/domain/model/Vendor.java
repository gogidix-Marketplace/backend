package com.gogidix.ecommerce.vendor.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "vendors")
public class Vendor {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    public enum VendorStatus { ACTIVE, INACTIVE, SUSPENDED, PENDING_APPROVAL, BLOCKED }
    private String vendorId;
    private String name;
    private VendorStatus status;
    private java.time.Instant createdAt;
    private java.time.Instant updatedAt;
}