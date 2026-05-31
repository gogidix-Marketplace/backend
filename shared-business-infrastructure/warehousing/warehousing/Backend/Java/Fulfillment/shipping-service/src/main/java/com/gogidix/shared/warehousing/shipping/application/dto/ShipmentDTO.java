package com.gogidix.shared.warehousing.shipping.application.dto;

import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Shipment Data Transfer Object
 *
 * Used for API responses and internal data transfer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Shipment DTO")
public class ShipmentDTO {

    @Schema(description = "Unique shipment identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Order reference number")
    private String orderNumber;

    @Schema(description = "Customer reference number")
    private String customerReference;

    @Schema(description = "Carrier-specific tracking number")
    private String trackingNumber;

    @Schema(description = "Carrier code")
    private Shipment.Carrier carrier;

    @Schema(description = "Service level")
    private Shipment.ServiceLevel serviceLevel;

    @Schema(description = "Shipment status")
    private Shipment.ShipmentStatus status;

    @Schema(description = "Shipper information")
    private Shipment.Party shipper;

    @Schema(description = "Recipient information")
    private Shipment.Party recipient;

    @Schema(description = "Package details")
    private List<Shipment.PackageDetail> packages;

    @Schema(description = "Shipment weight in kg")
    private BigDecimal weight;

    @Schema(description = "Shipment dimensions")
    private Shipment.Dimensions dimensions;

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

    @Schema(description = "Origin address")
    private Shipment.Address originAddress;

    @Schema(description = "Destination address")
    private Shipment.Address destinationAddress;

    @Schema(description = "Special handling instructions")
    private List<String> specialInstructions;

    @Schema(description = "Carrier-specific data")
    private Map<String, Object> carrierData;

    @Schema(description = "Customs declaration")
    private Shipment.CustomsDeclaration customsDeclaration;

    @Schema(description = "Additional attributes")
    private Map<String, Object> attributes;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
