package com.gogidix.shared.warehousing.receipt.application.dto;

import com.gogidix.shared.warehousing.receipt.domain.entity.Receipt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Receipt data transfer object")
public class ReceiptDTO {

    private String id;
    private String tenantId;
    private String receiptNumber;
    private String purchaseOrderNumber;
    private String supplierId;
    private String supplierName;
    private String warehouseId;
    private Receipt.ReceiptStatus status;
    private LocalDateTime receiptDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private List<Receipt.ReceiptLine> lines;
    private Integer totalQuantity;
    private List<Receipt.ReceiptDocument> documents;
    private String notes;
    private String receivedBy;
    private String dockDoor;
    private String carrier;
    private String vehicleNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
