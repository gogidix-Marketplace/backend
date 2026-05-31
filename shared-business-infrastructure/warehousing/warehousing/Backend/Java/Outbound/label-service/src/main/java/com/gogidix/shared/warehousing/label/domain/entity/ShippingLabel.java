package com.gogidix.shared.warehousing.label.domain.entity;

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

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipping_labels")
@CompoundIndex(def = "{'tenantId': 1, 'labelNumber': 1}", name = "idx_tenant_label")
@Schema(description = "Shipping label entity")
public class ShippingLabel {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String labelNumber;

    @Indexed
    private String shipmentId;

    @Indexed
    private String orderId;

    private String carrier;

    private String serviceLevel;

    private LabelFormat format;

    private String labelUrl;

    private String pdfData;

    private String zplData;

    private Integer labelWidth;

    private Integer labelHeight;

    private String templateId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum LabelFormat {
        PDF, ZPL, PNG, SVG
    }
}
