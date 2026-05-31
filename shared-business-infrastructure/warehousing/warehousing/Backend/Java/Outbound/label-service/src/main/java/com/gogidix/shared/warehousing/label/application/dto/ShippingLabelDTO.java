package com.gogidix.shared.warehousing.label.application.dto;

import com.gogidix.shared.warehousing.label.domain.entity.ShippingLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingLabelDTO {

    private String id;
    private String tenantId;
    private String labelNumber;
    private String shipmentId;
    private String orderId;
    private String carrier;
    private String serviceLevel;
    private ShippingLabel.LabelFormat format;
    private String labelUrl;
    private Integer labelWidth;
    private Integer labelHeight;
    private String templateId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
