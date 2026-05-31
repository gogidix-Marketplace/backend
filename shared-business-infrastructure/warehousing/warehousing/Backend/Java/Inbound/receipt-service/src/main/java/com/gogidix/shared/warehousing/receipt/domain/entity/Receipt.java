package com.gogidix.shared.warehousing.receipt.domain.entity;

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
@Document(collection = "receipts")
@CompoundIndex(def = "{'tenantId': 1, 'receiptNumber': 1}", name = "idx_tenant_receipt")
@Schema(description = "Goods receipt entity")
public class Receipt {

    @Id
    @Schema(description = "Receipt ID")
    private String id;

    @Indexed
    @Schema(description = "Tenant ID")
    private String tenantId;

    @Indexed
    @Schema(description = "Receipt number")
    private String receiptNumber;

    @Schema(description = "Purchase order number")
    private String purchaseOrderNumber;

    @Schema(description = "Supplier ID")
    private String supplierId;

    @Schema(description = "Supplier name")
    private String supplierName;

    @Schema(description = "Warehouse ID")
    private String warehouseId;

    @Schema(description = "Receipt status")
    private ReceiptStatus status;

    @Schema(description = "Receipt date")
    private LocalDateTime receiptDate;

    @Schema(description = "Expected delivery date")
    private LocalDateTime expectedDeliveryDate;

    @Schema(description = "Actual delivery date")
    private LocalDateTime actualDeliveryDate;

    @Schema(description = "Receipt lines")
    private List<ReceiptLine> lines;

    @Schema(description = "Total quantity received")
    private Integer totalQuantity;

    @Schema(description = "Documents attached")
    private List<ReceiptDocument> documents;

    @Schema(description = "Notes")
    private String notes;

    @Schema(description = "Received by")
    private String receivedBy;

    @Schema(description = "Dock door")
    private String dockDoor;

    @Schema(description = "Carrier")
    private String carrier;

    @Schema(description = "Vehicle number")
    private String vehicleNumber;

    @CreatedDate
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    public enum ReceiptStatus {
        PENDING, IN_RECEIVING, PARTIALLY_RECEIVED, FULLY_RECEIVED, CLOSED, CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Receipt line")
    public static class ReceiptLine {
        @Schema(description = "Line number")
        private Integer lineNumber;

        @Schema(description = "Product SKU")
        private String sku;

        @Schema(description = "Product name")
        private String productName;

        @Schema(description = "Expected quantity")
        private Integer expectedQuantity;

        @Schema(description = "Received quantity")
        private Integer receivedQuantity;

        @Schema(description = "Quantity UOM")
        private String unitOfMeasure;

        @Schema(description = "Batch/lot number")
        private String batchNumber;

        @Schema(description = "Expiration date")
        private LocalDateTime expirationDate;

        @Schema(description = "Unit price")
        private BigDecimal unitPrice;

        @Schema(description = "Line status")
        private LineStatus status;

        @Schema(description = "Location")
        private String locationId;

        @Schema(description = "Notes")
        private String notes;

        public enum LineStatus {
            PENDING, PARTIALLY_RECEIVED, RECEIVED, DAMAGED, MISSING
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Receipt document")
    public static class ReceiptDocument {
        @Schema(description = "Document type")
        private DocumentType type;

        @Schema(description = "Document URL")
        private String url;

        @Schema(description = "Document name")
        private String name;

        @Schema(description = "Upload date")
        private LocalDateTime uploadDate;

        public enum DocumentType {
            DELIVERY_NOTE, PACKING_LIST, CERTIFICATE_OF_ANALYSIS, BILL_OF_LADING, OTHER
        }
    }
}
