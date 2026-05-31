package com.gogidix.shared.warehousing.shipping.application.command;

import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Command to create a new shipment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command to create a new shipment")
public class CreateShipmentCommand {

    @NotBlank(message = "Order number is required")
    @Schema(description = "Order reference number")
    private String orderNumber;

    @Schema(description = "Customer reference number")
    private String customerReference;

    @NotNull(message = "Carrier is required")
    @Schema(description = "Carrier code")
    private Shipment.Carrier carrier;

    @NotNull(message = "Service level is required")
    @Schema(description = "Service level")
    private Shipment.ServiceLevel serviceLevel;

    @NotNull(message = "Shipper information is required")
    @Schema(description = "Shipper information")
    private Shipment.Party shipper;

    @NotNull(message = "Recipient information is required")
    @Schema(description = "Recipient information")
    private Shipment.Party recipient;

    @NotEmpty(message = "At least one package is required")
    @Schema(description = "Package details")
    private List<Shipment.PackageDetail> packages;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    @Schema(description = "Total shipment weight in kg")
    private BigDecimal weight;

    @Schema(description = "Shipment dimensions")
    private Shipment.Dimensions dimensions;

    @NotNull(message = "Origin address is required")
    @Schema(description = "Origin address")
    private Shipment.Address originAddress;

    @NotNull(message = "Destination address is required")
    @Schema(description = "Destination address")
    private Shipment.Address destinationAddress;

    @Schema(description = "Special handling instructions")
    private List<String> specialInstructions;

    @Schema(description = "Customs declaration for international shipments")
    private Shipment.CustomsDeclaration customsDeclaration;

    @Schema(description = "Additional attributes")
    private Map<String, Object> attributes;

    @Schema(description = "Currency code for shipping cost")
    private String currency;
}
