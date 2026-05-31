package com.gogidix.hr.globalpolicymanagement.domain.model;

import com.gogidix.hr.globalpolicymanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * PolicyVersion Domain Entity
 * Represents a version of an HR policy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "policy_versions")
public class PolicyVersion extends BaseEntity {

    @Indexed(unique = true)
    private String versionCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String policyId;

    private String policyCode;
    private String policyName;

    @Indexed
    private Integer versionNumber;

    private String content;
    private String contentFormat;
    private String documentUrl;
    private String changeSummary;
    private String changeDescription;

    @Indexed
    private String status;

    @Indexed
    private String createdBy;

    @Indexed
    private String approvedBy;

    @Indexed
    private String effectiveDate;

    @Indexed
    private Boolean isCurrent;

    private List<String> attachmentIds = new ArrayList<>();

    private Map<String, Object> diffFromPrevious = new HashMap<>();

    private List<String> reviewerIds = new ArrayList<>();

    private Map<String, String> reviewComments = new HashMap<>();

    @Indexed
    private Boolean isMajorVersion;

    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Creates a new policy version
     */
    public static PolicyVersion create(String tenantId, String policyId, String policyCode,
                                       String policyName, Integer versionNumber, String content,
                                       String changeSummary, String createdBy) {
        String versionCode = generateVersionCode(policyCode, versionNumber);

        PolicyVersion version = new PolicyVersion();
        version.setTenantId(tenantId);
        version.setPolicyId(policyId);
        version.setPolicyCode(policyCode);
        version.setPolicyName(policyName);
        version.setVersionCode(versionCode);
        version.setVersionNumber(versionNumber);
        version.setContent(content);
        version.setChangeSummary(changeSummary);
        version.setCreatedBy(createdBy);
        version.setStatus("DRAFT");
        version.setIsCurrent(false);
        version.setIsMajorVersion(false);
        version.setAttachmentIds(new ArrayList<>());
        version.setDiffFromPrevious(new HashMap<>());
        version.setReviewerIds(new ArrayList<>());
        version.setReviewComments(new HashMap<>());
        version.setMetadata(new HashMap<>());

        return version;
    }

    /**
     * Marks as current version
     */
    public void markAsCurrent() {
        this.isCurrent = true;
        this.status = "ACTIVE";
    }

    /**
     * Marks as superseded
     */
    public void markAsSuperseded() {
        this.isCurrent = false;
        this.status = "SUPERSEDED";
    }

    /**
     * Approves version
     */
    public void approve(String approvedBy) {
        this.status = "APPROVED";
        this.approvedBy = approvedBy;
    }

    /**
     * Adds reviewer
     */
    public void addReviewer(String reviewerId) {
        if (this.reviewerIds == null) {
            this.reviewerIds = new ArrayList<>();
        }
        if (!this.reviewerIds.contains(reviewerId)) {
            this.reviewerIds.add(reviewerId);
        }
    }

    /**
     * Adds review comment
     */
    public void addReviewComment(String reviewerId, String comment) {
        if (this.reviewComments == null) {
            this.reviewComments = new HashMap<>();
        }
        this.reviewComments.put(reviewerId, comment);
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
     * Generates version code
     */
    private static String generateVersionCode(String policyCode, Integer versionNumber) {
        return policyCode + "-V" + versionNumber;
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
