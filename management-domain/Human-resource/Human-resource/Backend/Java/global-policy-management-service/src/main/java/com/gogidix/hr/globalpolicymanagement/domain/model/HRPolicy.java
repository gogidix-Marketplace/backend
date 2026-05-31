package com.gogidix.hr.globalpolicymanagement.domain.model;

import com.gogidix.hr.globalpolicymanagement.domain.enums.PolicyStatus;
import com.gogidix.hr.globalpolicymanagement.domain.enums.PolicyType;
import com.gogidix.hr.globalpolicymanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * HRPolicy Domain Entity
 * Represents HR policies with versioning and approval workflow
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "hr_policies")
public class HRPolicy extends BaseEntity {

    @Indexed(unique = true)
    private String policyCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String categoryId;

    private String categoryName;

    @Indexed
    private PolicyType policyType;

    private String policyName;
    private String title;
    private String description;
    private String summary;

    @Indexed
    private PolicyStatus status;

    private String content;
    private String contentFormat;
    private String documentUrl;
    private Integer version;

    @Indexed
    private String currentVersionId;

    private List<String> versionIds = new ArrayList<>();

    @Indexed
    private String effectiveDate;

    @Indexed
    private String expiryDate;

    private String reviewDate;
    private String approvalDate;
    private String publishedDate;

    @Indexed
    private String createdBy;

    @Indexed
    private String approvedBy;

    @Indexed
    private String publishedBy;

    @Indexed
    private List<String> applicableCountries;

    @Indexed
    private List<String> applicableDepartments;

    @Indexed
    private List<String> applicableRoles;

    @Indexed
    private Boolean isGlobal;

    private Boolean requiresAcknowledgment;
    private Integer acknowledgmentDays;

    @Indexed
    private Boolean isMandatory;

    private String scope;
    private String complianceLevel;

    private List<String> relatedPolicyIds = new ArrayList<>();

    private List<String> supersededPolicyIds = new ArrayList<>();

    private Map<String, Object> attributes = new HashMap<>();

    private List<String> tags = new ArrayList<>();

    @Indexed
    private Boolean isArchived;

    private String archivedDate;
    private String archivedBy;
    private String archiveReason;

    private List<String> attachmentIds = new ArrayList<>();

    private List<String> acknowledgmentIds = new ArrayList<>();

    private Map<String, Object> metadata = new HashMap<>();

    private String changeNotes;
    private Integer viewCount;

    /**
     * Creates a new HR policy
     */
    public static HRPolicy create(String tenantId, String categoryId, String categoryName,
                                   PolicyType policyType, String policyName, String title,
                                   String description, String createdBy) {
        String policyCode = generatePolicyCode(tenantId, policyType);

        HRPolicy policy = new HRPolicy();
        policy.tenantId = tenantId;
        policy.categoryId = categoryId;
        policy.categoryName = categoryName;
        policy.policyType = policyType;
        policy.policyName = policyName;
        policy.title = title;
        policy.description = description;
        policy.policyCode = policyCode;
        policy.status = PolicyStatus.DRAFT;
        policy.version = 1;
        policy.createdBy = createdBy;
        policy.isGlobal = false;
        policy.isMandatory = false;
        policy.requiresAcknowledgment = false;
        policy.isArchived = false;
        policy.viewCount = 0;
        policy.applicableCountries = new ArrayList<>();
        policy.applicableDepartments = new ArrayList<>();
        policy.applicableRoles = new ArrayList<>();
        policy.versionIds = new ArrayList<>();
        policy.relatedPolicyIds = new ArrayList<>();
        policy.supersededPolicyIds = new ArrayList<>();
        policy.attributes = new HashMap<>();
        policy.tags = new ArrayList<>();
        policy.attachmentIds = new ArrayList<>();
        policy.acknowledgmentIds = new ArrayList<>();
        policy.metadata = new HashMap<>();

        return policy;
    }

    /**
     * Submits policy for review
     */
    public void submitForReview() {
        if (this.status != PolicyStatus.DRAFT) {
            throw new IllegalStateException("Policy can only be submitted for review from DRAFT status");
        }
        this.status = PolicyStatus.UNDER_REVIEW;
    }

    /**
     * Approves policy
     */
    public void approve(String approvedBy, String approvalDate) {
        if (this.status != PolicyStatus.UNDER_REVIEW && this.status != PolicyStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Policy can only be approved from review status");
        }
        this.status = PolicyStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvalDate = approvalDate;
    }

    /**
     * Rejects policy
     */
    public void reject(String reason) {
        if (this.status != PolicyStatus.UNDER_REVIEW && this.status != PolicyStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Policy can only be rejected from review status");
        }
        this.status = PolicyStatus.REJECTED;
        this.changeNotes = reason;
    }

    /**
     * Publishes policy
     */
    public void publish(String publishedBy, String publishedDate, String effectiveDate) {
        if (this.status != PolicyStatus.APPROVED) {
            throw new IllegalStateException("Policy can only be published after approval");
        }
        this.status = PolicyStatus.PUBLISHED;
        this.publishedBy = publishedBy;
        this.publishedDate = publishedDate;
        this.effectiveDate = effectiveDate;
    }

    /**
     * Archives policy
     */
    public void archive(String archivedBy, String archivedDate, String reason) {
        if (this.status == PolicyStatus.ARCHIVED) {
            throw new IllegalStateException("Policy is already archived");
        }
        this.status = PolicyStatus.ARCHIVED;
        this.isArchived = true;
        this.archivedBy = archivedBy;
        this.archivedDate = archivedDate;
        this.archiveReason = reason;
    }

    /**
     * Creates new version
     */
    public void createNewVersion(String newVersionId, String changeNotes) {
        if (this.status != PolicyStatus.PUBLISHED) {
            throw new IllegalStateException("Can only create new version of published policies");
        }
        this.versionIds.add(this.currentVersionId);
        this.currentVersionId = newVersionId;
        this.version = this.version + 1;
        this.status = PolicyStatus.DRAFT;
        this.changeNotes = changeNotes;
    }

    /**
     * Sets effective date
     */
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Sets expiry date
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Sets review date
     */
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * Adds applicable country
     */
    public void addApplicableCountry(String countryCode) {
        if (this.applicableCountries == null) {
            this.applicableCountries = new ArrayList<>();
        }
        if (!this.applicableCountries.contains(countryCode)) {
            this.applicableCountries.add(countryCode);
        }
    }

    /**
     * Adds applicable department
     */
    public void addApplicableDepartment(String departmentId) {
        if (this.applicableDepartments == null) {
            this.applicableDepartments = new ArrayList<>();
        }
        if (!this.applicableDepartments.contains(departmentId)) {
            this.applicableDepartments.add(departmentId);
        }
    }

    /**
     * Adds applicable role
     */
    public void addApplicableRole(String roleId) {
        if (this.applicableRoles == null) {
            this.applicableRoles = new ArrayList<>();
        }
        if (!this.applicableRoles.contains(roleId)) {
            this.applicableRoles.add(roleId);
        }
    }

    /**
     * Sets as global policy
     */
    public void setAsGlobal() {
        this.isGlobal = true;
        this.applicableCountries.clear();
    }

    /**
     * Adds related policy
     */
    public void addRelatedPolicy(String policyId) {
        if (this.relatedPolicyIds == null) {
            this.relatedPolicyIds = new ArrayList<>();
        }
        if (!this.relatedPolicyIds.contains(policyId)) {
            this.relatedPolicyIds.add(policyId);
        }
    }

    /**
     * Adds superseded policy
     */
    public void addSupersededPolicy(String policyId) {
        if (this.supersededPolicyIds == null) {
            this.supersededPolicyIds = new ArrayList<>();
        }
        if (!this.supersededPolicyIds.contains(policyId)) {
            this.supersededPolicyIds.add(policyId);
        }
    }

    /**
     * Adds acknowledgment
     */
    public void addAcknowledgment(String acknowledgmentId) {
        if (this.acknowledgmentIds == null) {
            this.acknowledgmentIds = new ArrayList<>();
        }
        if (!this.acknowledgmentIds.contains(acknowledgmentId)) {
            this.acknowledgmentIds.add(acknowledgmentId);
        }
    }

    /**
     * Adds attachment
     */
    public void addAttachment(String attachmentId) {
        if (this.attachmentIds == null) {
            this.attachmentIds = new ArrayList<>();
        }
        if (!this.attachmentIds.contains(attachmentId)) {
            this.attachmentIds.add(attachmentId);
        }
    }

    /**
     * Adds tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Sets attribute
     */
    public void setAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
    }

    /**
     * Increments view count
     */
    public void incrementViewCount() {
        this.viewCount = (this.viewCount != null ? this.viewCount : 0) + 1;
    }

    /**
     * Checks if policy is active
     */
    public boolean isActive() {
        if (this.status != PolicyStatus.PUBLISHED) {
            return false;
        }
        if (this.effectiveDate != null && LocalDate.now().isBefore(LocalDate.parse(this.effectiveDate))) {
            return false;
        }
        if (this.expiryDate != null && LocalDate.now().isAfter(LocalDate.parse(this.expiryDate))) {
            return false;
        }
        return true;
    }

    /**
     * Checks if policy is expired
     */
    public boolean isExpired() {
        return this.expiryDate != null && LocalDate.now().isAfter(LocalDate.parse(this.expiryDate));
    }

    /**
     * Checks if policy requires review
     */
    public boolean requiresReview() {
        return this.reviewDate != null && LocalDate.now().isAfter(LocalDate.parse(this.reviewDate));
    }

    /**
     * Gets acknowledgment status
     */
    public boolean requiresAcknowledgment() {
        return this.requiresAcknowledgment != null && this.requiresAcknowledgment;
    }

    /**
     * Generates policy code
     */
    private static String generatePolicyCode(String tenantId, PolicyType policyType) {
        String prefix = policyType.name().substring(0, 3).toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "POL-" + prefix + "-" + uniqueId;
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
