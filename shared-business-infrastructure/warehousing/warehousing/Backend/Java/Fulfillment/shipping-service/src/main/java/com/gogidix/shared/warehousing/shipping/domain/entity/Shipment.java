package com.gogidix.shared.warehousing.shipping.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Shipment Entity - Multi-tenant with MongoDB
 *
 * Represents a shipment with carrier integration and tracking
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipments")
@CompoundIndex(def = "{'tenantId': 1, 'trackingNumber': 1}", name = "idx_tenant_tracking")
@Schema(description = "Shipment entity representing a shipping order")
public class Shipment {

    @Id
    @Schema(description = "Unique shipment identifier")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Schema(description = "Order reference number")
    private String orderNumber;

    @Schema(description = "Customer reference number")
    private String customerReference;

    @Indexed
    @Schema(description = "Carrier-specific tracking number")
    private String trackingNumber;

    @Schema(description = "Carrier code (FEDEX, UPS, DHL, etc.)")
    private Carrier carrier;

    @Schema(description = "Service level (STANDARD, EXPRESS, OVERNIGHT, etc.)")
    private ServiceLevel serviceLevel;

    @Schema(description = "Shipment status")
    private ShipmentStatus status;

    @Schema(description = "Shipper information")
    private Party shipper;

    @Schema(description = "Recipient information")
    private Party recipient;

    @Schema(description = "Package details")
    private List<PackageDetail> packages;

    @Schema(description = "Shipment weight in kg")
    private BigDecimal weight;

    @Schema(description = "Shipment dimensions (L x W x H in cm)")
    private Dimensions dimensions;

    @Schema(description = "Shipping label URL")
    private String labelUrl;

    @Schema(description = "Shipping cost")
    private BigDecimal shippingCost;

    @Schema(description = "Currency code")
    private String currency;

    @Schema(description = "Estimated delivery date")
    private LocalDateTime estimatedDelivery;

    @Schema(description = "Actual delivery date")
    private LocalDateTime actualDelivery;

    @Schema(description = "Shipping origin address")
    private Address originAddress;

    @Schema(description = "Shipping destination address")
    private Address destinationAddress;

    @Schema(description = "Special handling instructions")
    private List<String> specialInstructions;

    @Schema(description = "Carrier-specific data")
    private Map<String, Object> carrierData;

    @Schema(description = "Customs declaration for international shipments")
    private CustomsDeclaration customsDeclaration;

    @Schema(description = "Additional attributes")
    private Map<String, Object> attributes;

    @CreatedDate
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    public enum Carrier {
        FEDEX, UPS, DHL, USPS, CANADA_POST, ROYAL_MAIL, OTHER
    }

    public enum ServiceLevel {
        STANDARD, EXPRESS, OVERNIGHT, SAME_DAY, ECONOMY, FREIGHT
    }

    public enum ShipmentStatus {
        CREATED, LABEL_GENERATED, PICKED_UP, IN_TRANSIT,
        OUT_FOR_DELIVERY, DELIVERED, EXCEPTION, CANCELLED, RETURNED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Party information (shipper or recipient)")
    public static class Party {
        @Schema(description = "Party name")
        private String name;

        @Schema(description = "Contact person")
        private String contactPerson;

        @Schema(description = "Phone number")
        private String phone;

        @Schema(description = "Email address")
        private String email;

        @Schema(description = "Company name")
        private String company;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Package details")
    public static class PackageDetail {
        @Schema(description = "Package number")
        private Integer packageNumber;

        @Schema(description = "Package weight in kg")
        private BigDecimal weight;

        @Schema(description = "Package dimensions")
        private Dimensions dimensions;

        @Schema(description = "Package tracking number")
        private String trackingNumber;

        @Schema(description = "Package contents description")
        private String description;

        @Schema(description = "Package value")
        private BigDecimal value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Dimensions")
    public static class Dimensions {
        @Schema(description = "Length in cm")
        private BigDecimal length;

        @Schema(description = "Width in cm")
        private BigDecimal width;

        @Schema(description = "Height in cm")
        private BigDecimal height;

        @Schema(description = "Dimensional unit")
        private String unit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Address information")
    public static class Address {
        @Schema(description = "Street address line 1")
        private String line1;

        @Schema(description = "Street address line 2")
        private String line2;

        @Schema(description = "City")
        private String city;

        @Schema(description = "State/Province")
        private String state;

        @Schema(description = "Postal code")
        private String postalCode;

        @Schema(description = "Country code")
        private String countryCode;

        @Schema(description = "Country name")
        private String country;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Customs declaration for international shipments")
    public static class CustomsDeclaration {
        @Schema(description = "Declaration type")
        private String declarationType;

        @Schema(description = "Purpose of shipment")
        private String purpose;

        @Schema(description = "Customs items")
        private List<CustomsItem> items;

        @Schema(description = "Total customs value")
        private BigDecimal totalValue;

        @Schema(description = "Currency for customs value")
        private String currency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Customs item")
    public static class CustomsItem {
        @Schema(description = "Item description")
        private String description;

        @Schema(description = "HS tariff code")
        private String hsCode;

        @Schema(description = "Quantity")
        private Integer quantity;

        @Schema(description = "Unit value")
        private BigDecimal unitValue;

        @Schema(description = "Country of origin")
        private String countryOfOrigin;
    }
}
