package com.gogidix.shared.warehousing.publicapi.booking.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Warehousing Booking Request entity
 * Represents a public booking request for warehousing services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "warehousing_booking_requests")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1, 'bookingNumber': 1}", name = "idx_tenant_booking", unique = true)
@CompoundIndex(def = "{'tenantId': 1, 'customerEmail': 1}", name = "idx_tenant_customer")
@CompoundIndex(def = "{'tenantId': 1, 'status': 1}", name = "idx_tenant_status")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1}", name = "idx_tenant_warehouse")
public class WarehousingBookingRequest {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String bookingNumber;

    @Indexed
    private String customerEmail;

    private String customerName;

    private String customerPhone;

    private String customerCompany;

    private BookingType bookingType;

    private BookingStatus status;

    private LocalDateTime requestedStartDate;

    private LocalDateTime requestedEndDate;

    private LocalDateTime confirmedStartDate;

    private LocalDateTime confirmedEndDate;

    private StorageRequirements storageRequirements;

    private ServiceRequirements serviceRequirements;

    private Double quotedPrice;

    private String currency;

    private String warehouseId;

    private String warehouseName;

    private String assignedSpaceId;

    private String quoteId;

    private String rejectionReason;

    private List<String> documents;

    private String specialInstructions;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorageRequirements {
        private Double requiredArea;
        private String areaUnit;
        private Double volume;
        private String volumeUnit;
        private Double weight;
        private String weightUnit;
        private Integer palletCount;
        private String temperatureRequirement;
        private Boolean climateControlled;
        private Boolean hazardous;
        private String hazardClass;
        private List<String> specialHandling;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceRequirements {
        private Boolean receivingRequired;
        private Boolean putAwayRequired;
        private Boolean pickingRequired;
        private Boolean packingRequired;
        private Boolean shippingRequired;
        private Boolean inventoryManagement;
        private Boolean valueAddedServices;
        private List<String> additionalServices;
    }

    public enum BookingType {
        STORAGE_ONLY,
        FULL_SERVICE,
        FULFILLMENT,
        CROSS_DOCKING,
        TRANSLOAD,
        SHORT_TERM,
        LONG_TERM
    }

    public enum BookingStatus {
        PENDING,
        QUOTED,
        CONFIRMED,
        ACTIVE,
        COMPLETED,
        CANCELLED,
        REJECTED,
        EXPIRED
    }

    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = BookingStatus.ACTIVE;
        this.confirmedStartDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = BookingStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(String reason) {
        this.status = BookingStatus.REJECTED;
        this.rejectionReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        this.status = BookingStatus.CANCELLED;
        this.rejectionReason = reason;
        this.updatedAt = LocalDateTime.now();
    }
}
