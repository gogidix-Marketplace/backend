package com.gogidix.sales.leadmanagement.application.dto.response;

import com.gogidix.sales.leadmanagement.domain.model.Lead;
import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Lead Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponseDto {

    private String id;
    private String leadId;
    private String tenantId;

    // Contact Information
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String mobilePhone;

    // Professional Information
    private String company;
    private String title;
    private String industry;
    private String companySize;
    private String website;
    private String linkedInUrl;

    // Lead Classification
    private String source;
    private String sourceDetails;
    private String campaign;
    private String stage;
    private String status;
    private String quality;
    private Integer score;
    private Integer probability;

    // Assignment
    private String ownerId;
    private String ownerName;
    private List<String> teamMemberIds;

    // Territory and Segmentation
    private String territory;
    private String region;
    private String segment;

    // Lead Qualification
    private Integer budget;
    private Integer authority;
    private Integer need;
    private Integer timeline;
    private Integer bantScore;

    // Engagement Metrics
    private Integer emailOpens;
    private Integer emailClicks;
    private Integer webVisits;
    private Integer formSubmissions;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastActivityDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate firstContactDate;

    // Conversion Information
    private String convertedDealId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate convertedDate;

    private String conversionReason;

    // Loss Information
    private String lossReason;
    private String lossReasonDetails;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lostDate;

    // Duplicate Detection
    private Boolean isDuplicate;
    private String duplicateOfLeadId;
    private Double duplicateMatchScore;

    // Additional Information
    private String notes;
    private List<String> tags;

    // Expected Revenue
    private BigDecimal estimatedValue;
    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedCloseDate;

    // Timestamps
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    private String updatedBy;

    // Embedded Activities (summary)
    private List<ActivitySummary> recentActivities;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivitySummary {
        private String activityId;
        private String activityType;
        private String subject;
        private String status;
        private String priority;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant dueDate;
    }

    /**
     * Converts Lead entity to DTO
     */
    public static LeadResponseDto fromEntity(Lead lead) {
        return LeadResponseDto.builder()
                .id(lead.getId())
                .leadId(lead.getLeadId())
                .tenantId(lead.getTenantId())
                .firstName(lead.getFirstName())
                .lastName(lead.getLastName())
                .fullName(lead.getFullName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .mobilePhone(lead.getMobilePhone())
                .company(lead.getCompany())
                .title(lead.getTitle())
                .industry(lead.getIndustry())
                .companySize(lead.getCompanySize())
                .website(lead.getWebsite())
                .linkedInUrl(lead.getLinkedInUrl())
                .source(lead.getSource() != null ? lead.getSource().name() : null)
                .sourceDetails(lead.getSourceDetails())
                .campaign(lead.getCampaign())
                .stage(lead.getStage() != null ? lead.getStage().name() : null)
                .status(lead.getStatus() != null ? lead.getStatus().name() : null)
                .quality(lead.getQuality() != null ? lead.getQuality().name() : null)
                .score(lead.getScore())
                .probability(lead.getProbability())
                .ownerId(lead.getOwnerId())
                .ownerName(lead.getOwnerName())
                .teamMemberIds(lead.getTeamMemberIds())
                .territory(lead.getTerritory())
                .region(lead.getRegion())
                .segment(lead.getSegment())
                .budget(lead.getBudget())
                .authority(lead.getAuthority())
                .need(lead.getNeed())
                .timeline(lead.getTimeline())
                .bantScore(lead.getBantScore())
                .emailOpens(lead.getEmailOpens())
                .emailClicks(lead.getEmailClicks())
                .webVisits(lead.getWebVisits())
                .formSubmissions(lead.getFormSubmissions())
                .lastActivityDate(lead.getLastActivityDate())
                .firstContactDate(lead.getFirstContactDate())
                .convertedDealId(lead.getConvertedDealId())
                .convertedDate(lead.getConvertedDate())
                .conversionReason(lead.getConversionReason())
                .lossReason(lead.getLossReason())
                .lossReasonDetails(lead.getLossReasonDetails())
                .lostDate(lead.getLostDate())
                .isDuplicate(lead.getIsDuplicate())
                .duplicateOfLeadId(lead.getDuplicateOfLeadId())
                .duplicateMatchScore(lead.getDuplicateMatchScore())
                .notes(lead.getNotes())
                .tags(lead.getTagList())
                .estimatedValue(lead.getEstimatedValue())
                .currency(lead.getCurrency())
                .expectedCloseDate(lead.getExpectedCloseDate())
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .updatedBy(lead.getUpdatedBy())
                .recentActivities(lead.getActivities() != null
                        ? lead.getActivities().stream()
                                .limit(5)
                                .map(activity -> ActivitySummary.builder()
                                        .activityId(activity.getActivityId())
                                        .activityType(activity.getActivityType() != null ? activity.getActivityType().name() : null)
                                        .subject(activity.getSubject())
                                        .status(activity.getStatus() != null ? activity.getStatus().name() : null)
                                        .priority(activity.getPriority() != null ? activity.getPriority().name() : null)
                                        .createdAt(activity.getCreatedAt())
                                        .dueDate(activity.getDueDate())
                                        .build())
                                .toList()
                        : null)
                .build();
    }
}
