package com.gogidix.hr.globalpolicymanagement.domain.model;

import com.gogidix.hr.globalpolicymanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * PolicyCategory Domain Entity
 * Represents categories for organizing HR policies
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "policy_categories")
public class PolicyCategory extends BaseEntity {

    @Indexed(unique = true)
    private String categoryCode;

    @Indexed
    private String tenantId;

    private String categoryName;
    private String description;

    @Indexed
    private String parentId;

    private List<String> childCategoryIds = new ArrayList<>();

    @Indexed
    private Integer level;

    private List<String> applicablePolicyTypeIds = new ArrayList<>();

    @Indexed
    private Integer sortOrder;

    @Indexed
    private Boolean isActive;

    @Indexed
    private String icon;

    private String color;
    private String displayTemplate;

    private List<String> tags = new ArrayList<>();

    /**
     * Creates a new policy category
     */
    public static PolicyCategory create(String tenantId, String categoryName, String description) {
        String categoryCode = generateCategoryCode(categoryName);

        PolicyCategory category = new PolicyCategory();
        category.setTenantId(tenantId);
        category.setCategoryName(categoryName);
        category.setDescription(description);
        category.setCategoryCode(categoryCode);
        category.setLevel(0);
        category.setIsActive(true);
        category.setSortOrder(0);
        category.setChildCategoryIds(new ArrayList<>());
        category.setApplicablePolicyTypeIds(new ArrayList<>());
        category.setTags(new ArrayList<>());

        return category;
    }

    /**
     * Adds child category
     */
    public void addChildCategory(String categoryId) {
        if (this.childCategoryIds == null) {
            this.childCategoryIds = new ArrayList<>();
        }
        if (!this.childCategoryIds.contains(categoryId)) {
            this.childCategoryIds.add(categoryId);
        }
    }

    /**
     * Adds applicable policy type
     */
    public void addApplicablePolicyType(String policyTypeId) {
        if (this.applicablePolicyTypeIds == null) {
            this.applicablePolicyTypeIds = new ArrayList<>();
        }
        if (!this.applicablePolicyTypeIds.contains(policyTypeId)) {
            this.applicablePolicyTypeIds.add(policyTypeId);
        }
    }

    /**
     * Activates category
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivates category
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Generates category code
     */
    private static String generateCategoryCode(String categoryName) {
        String normalized = categoryName.toUpperCase().replaceAll("\\s+", "_");
        return "CAT-" + normalized;
    }

    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
