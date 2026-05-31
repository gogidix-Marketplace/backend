package com.gogidix.shared.warehousing.returns.application.dto;

import com.gogidix.shared.warehousing.returns.domain.entity.Return;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Return data transfer object")
public class ReturnDTO {

    @Schema(description = "Return ID")
    private String id;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "RMA number")
    private String rmaNumber;

    @Schema(description = "Order number")
    private String orderNumber;

    @Schema(description = "Customer ID")
    private String customerId;

    @Schema(description = "Return reason")
    private Return.ReturnReason reason;

    @Schema(description = "Return status")
    private Return.ReturnStatus status;

    @Schema(description = "Return items")
    private List<Return.ReturnItem> items;

    @Schema(description = "Refund type")
    private Return.RefundType refundType;

    @Schema(description = "Refund amount")
    private BigDecimal refundAmount;

    @Schema(description = "Currency")
    private String currency;

    @Schema(description = "Restocking fee")
    private BigDecimal restockingFee;

    @Schema(description = "Tracking number")
    private String trackingNumber;

    @Schema(description = "Carrier")
    private String carrier;

    @Schema(description = "Customer notes")
    private String customerNotes;

    @Schema(description = "Internal notes")
    private String internalNotes;

    @Schema(description = "Quality check result")
    private Return.QualityCheckResult qualityCheck;

    @Schema(description = "Requested date")
    private LocalDateTime requestedDate;

    @Schema(description = "Received date")
    private LocalDateTime receivedDate;

    @Schema(description = "Refunded date")
    private LocalDateTime refundedDate;

    @Schema(description = "Created at")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;
}
