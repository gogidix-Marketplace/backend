package com.gogidix.shared.warehousing.label.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "label_templates")
@CompoundIndex(def = "{'tenantId': 1, 'templateCode': 1}", name = "idx_tenant_template")
@Schema(description = "Label template entity")
public class LabelTemplate {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String templateCode;

    private String templateName;

    private String carrier;

    private String description;

    private Integer width;

    private Integer height;

    private ShippingLabel.LabelFormat format;

    private String templateData;

    private Map<String, Object> config;
}
