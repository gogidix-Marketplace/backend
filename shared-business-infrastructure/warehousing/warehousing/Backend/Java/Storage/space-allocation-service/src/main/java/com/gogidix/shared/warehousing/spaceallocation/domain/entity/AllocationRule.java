package com.gogidix.shared.warehousing.spaceallocation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Allocation Rule Entity
 *
 * Rules for space allocation logic
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "allocation_rules")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'priority': -1}", name = "idx_rule_tenant_priority")
public class AllocationRule {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String ruleName;

    private String description;

    private RuleType ruleType;

    private Integer priority;

    private Boolean active;

    private String appliesToItemType;

    private String appliesToZoneType;

    private AllocationStrategy strategy;

    private Integer maxItemsPerLocation;

    private Integer maxWeightPerLocation;

    private Integer maxVolumePerLocation;

    private Boolean allowMixing;

    private String[] compatibleItemTypes;

    private Map<String, Object> parameters;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum RuleType {
        CAPACITY,
        WEIGHT,
        VOLUME,
        COMPATIBILITY,
        PROXIMITY,
        ROTATION,
        TEMPERATURE,
        SECURITY,
        FRAGILITY
    }

    public enum AllocationStrategy {
        FIRST_AVAILABLE,
        BEST_FIT,
        WORST_FIT,
        RANDOM,
        ZONE_BASED,
        ITEM_TYPE_BASED,
        VELOCITY_BASED,
        ABC_ANALYSIS,
        FAMILY_GROUPING
    }
}
