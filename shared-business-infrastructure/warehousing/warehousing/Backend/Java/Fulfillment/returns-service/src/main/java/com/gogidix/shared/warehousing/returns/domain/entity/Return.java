package com.gogidix.shared.warehousing.returns.domain.entity;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "returns")
@CompoundIndex(def = "{'tenantId': 1, 'orderNumber': 1}", name = "idx_tenant_order")
@Schema(description = "Return entity representing a customer return")
public class Return {

    @Id
    @Schema(description = "Unique return identifier")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Indexed
    @Schema(description = "Return authorization number (RMA)")
    private String rmaNumber;

    @Schema(description = "Original order number")
    private String orderNumber;

    @Schema(description = "Customer ID")
    private String customerId;

    @Schema(description = "Return reason")
    private ReturnReason reason;

    @Schema(description = "Return status")
    private ReturnStatus status;

    @Schema(description = "Return items")
    private List<ReturnItem> items;

    @Schema(description = "Refund type")
    private RefundType refundType;

    @Schema(description = "Refund amount")
    private BigDecimal refundAmount;

    @Schema(description = "Currency code")
    private String currency;

    @Schema(description = "Restocking fee")
    private BigDecimal restockingFee;

    @Schema(description = "Return shipping address")
    private Address returnAddress;

    @Schema(description = "Return tracking number")
    private String trackingNumber;

    @Schema(description = "Carrier for return shipment")
    private String carrier;

    @Schema(description = "Notes from customer")
    private String customerNotes;

    @Schema(description = "Internal notes")
    private String internalNotes;

    @Schema(description = "Quality check result")
    private QualityCheckResult qualityCheck;

    @Schema(description = "Date return was requested")
    private LocalDateTime requestedDate;

    @Schema(description = "Date return was received")
    private LocalDateTime receivedDate;

    @Schema(description = "Date refund was processed")
    private LocalDateTime refundedDate;

    @CreatedDate
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    public enum ReturnStatus {
        REQUESTED, APPROVED, REJECTED, IN_TRANSIT, RECEIVED,
        INSPECTING, APPROVED_FOR_REFUND, REFUNDED, CLOSED
    }

    public enum ReturnReason {
        DAMAGED, DEFECTIVE, WRONG_ITEM, NO_LONGER_NEEDED,
        BETTER_PRICE_AVAILABLE, QUALITY_NOT_EXPECTED, MISORDERED, OTHER
    }

    public enum RefundType {
        ORIGINAL_PAYMENT, STORE_CREDIT, EXCHANGE, BANK_TRANSFER
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Return item details")
    public static class ReturnItem {
        @Schema(description = "Line item ID from original order")
        private String lineItemId;

        @Schema(description = "Product SKU")
        private String sku;

        @Schema(description = "Product name")
        private String productName;

        @Schema(description = "Quantity being returned")
        private Integer quantity;

        @Schema(description = "Reason for return")
        private ReturnReason reason;

        @Schema(description = "Item condition")
        private ItemCondition condition;

        @Schema(description = "Item price")
        private BigDecimal price;

        @Schema(description = "Refund amount for this item")
        private BigDecimal refundAmount;

        public enum ItemCondition {
            NEW, OPENED, USED, DAMAGED, DEFECTIVE
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Address information")
    public static class Address {
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String postalCode;
        private String countryCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Quality check result")
    public static class QualityCheckResult {
        @Schema(description = "Whether quality check passed")
        private Boolean passed;

        @Schema(description = "Inspector ID")
        private String inspectorId;

        @Schema(description = "Inspection date")
        private LocalDateTime inspectionDate;

        @Schema(description = "Inspection notes")
        private String notes;

        @Schema(description = "List of defects found")
        private List<String> defects;
    }
}
