package com.gogidix.shared.warehousing.quality.domain.entity;

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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quality_checks")
@CompoundIndex(def = "{'tenantId': 1, 'shipmentId': 1}", name = "idx_tenant_shipment")
@Schema(description = "Quality check entity")
public class QualityCheck {

    @Id
    @Schema(description = "Quality check ID")
    private String id;

    @Indexed
    @Schema(description = "Tenant ID")
    private String tenantId;

    @Indexed
    @Schema(description = "Reference ID (order, shipment, etc.)")
    private String referenceId;

    @Schema(description = "Reference type")
    private ReferenceType referenceType;

    @Schema(description = "Quality check status")
    private QualityStatus status;

    @Schema(description = "Inspector ID")
    private String inspectorId;

    @Schema(description = "Inspector name")
    private String inspectorName;

    @Schema(description = "Inspection date")
    private LocalDateTime inspectionDate;

    @Schema(description = "Inspection location")
    private String locationId;

    @Schema(description = "Check type")
    private CheckType checkType;

    @Schema(description = "Items checked")
    private List<CheckedItem> items;

    @Schema(description = "Defects found")
    private List<Defect> defects;

    @Schema(description = "Overall pass/fail result")
    private Boolean passed;

    @Schema(description = "Quality score (0-100)")
    private Integer qualityScore;

    @Schema(description = "Inspection notes")
    private String notes;

    @Schema(description = "Photos/attachments URLs")
    private List<String> attachments;

    @Schema(description = "Corrective actions required")
    private List<String> correctiveActions;

    @CreatedDate
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    public enum QualityStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED, PASSED, CANCELLED
    }

    public enum ReferenceType {
        SHIPMENT, RETURN, RECEIPT, PRODUCTION
    }

    public enum CheckType {
        INCOMING, OUTGOING, RETURN_INSPECTION, RANDOM_SAMPLE, FULL_INSPECTION
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Checked item details")
    public static class CheckedItem {
        @Schema(description = "Item SKU")
        private String sku;

        @Schema(description = "Item name")
        private String itemName;

        @Schema(description = "Quantity inspected")
        private Integer quantityInspected;

        @Schema(description = "Quantity passed")
        private Integer quantityPassed;

        @Schema(description = "Quantity failed")
        private Integer quantityFailed;

        @Schema(description = "Item passed")
        private Boolean passed;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Defect details")
    public static class Defect {
        @Schema(description = "Defect ID")
        private String id;

        @Schema(description = "Defect type")
        private DefectType type;

        @Schema(description = "Severity")
        private Severity severity;

        @Schema(description = "Description")
        private String description;

        @Schema(description = "Affected item SKU")
        private String sku;

        @Schema(description = "Quantity affected")
        private Integer quantity;

        @Schema(description = "Photo URL")
        private String photoUrl;

        public enum DefectType {
            DAMAGED, DEFECTIVE, MISSING_PARTS, WRONG_ITEM, STAINED, COSMETIC, OTHER
        }

        public enum Severity {
            CRITICAL, MAJOR, MINOR
        }
    }
}
