package com.gogidix.shared.warehousing.shipping.application.command;

import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Command to update an existing shipment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command to update an existing shipment")
public class UpdateShipmentCommand {

    @Schema(description = "Updated customer reference")
    private String customerReference;

    @Schema(description = "Updated service level")
    private Shipment.ServiceLevel serviceLevel;

    @Schema(description = "Updated recipient")
    private Shipment.Party recipient;

    @Schema(description = "Updated packages")
    private List<Shipment.PackageDetail> packages;

    @Schema(description = "Updated weight")
    private Integer weight;

    @Schema(description = "Updated dimensions")
    private Shipment.Dimensions dimensions;

    @Schema(description = "Updated destination address")
    private Shipment.Address destinationAddress;

    @Schema(description = "Updated special instructions")
    private List<String> specialInstructions;

    @Schema(description = "Updated customs declaration")
    private Shipment.CustomsDeclaration customsDeclaration;

    @Schema(description = "Updated attributes")
    private Map<String, Object> attributes;

    @Schema(description = "Estimated delivery date")
    private LocalDateTime estimatedDelivery;

    @Schema(description = "Actual delivery date")
    private LocalDateTime actualDelivery;
}
