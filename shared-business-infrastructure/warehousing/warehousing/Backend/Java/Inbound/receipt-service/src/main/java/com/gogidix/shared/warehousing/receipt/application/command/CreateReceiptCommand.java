package com.gogidix.shared.warehousing.receipt.application.command;

import com.gogidix.shared.warehousing.receipt.domain.entity.Receipt;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class CreateReceiptCommand {

    @NotBlank(message = "Purchase order number is required")
    private String purchaseOrderNumber;

    @NotBlank(message = "Supplier ID is required")
    private String supplierId;

    private String supplierName;

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private LocalDateTime expectedDeliveryDate;

    @NotEmpty(message = "At least one receipt line is required")
    @Valid
    private List<Receipt.ReceiptLine> lines;

    private String notes;

    private String receivedBy;

    private String dockDoor;

    private String carrier;

    private String vehicleNumber;
}
