package com.gogidix.shared.warehousing.label.application.command;

import com.gogidix.shared.warehousing.label.domain.entity.ShippingLabel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateLabelCommand {

    @NotBlank
    private String shipmentId;

    private String orderId;

    @NotBlank
    private String carrier;

    private String serviceLevel;

    private ShippingLabel.LabelFormat format;

    private String templateId;
}
