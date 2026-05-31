package com.gogidix.customersupport.slamanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "sla_policies")
public class SLAPolicy extends BaseEntity {

    @Field("policy_name")
    @Indexed
    private String policyName;

    @Field("policy_code")
    @Indexed(unique = true)
    private String policyCode;

    @Field("description")
    private String description;

    @Field("is_active")
    @Indexed
    private Boolean isActive;

    @Field("priority")
    private PolicyPriority priority;

    @Field("response_time_target_minutes")
    private Integer responseTimeTargetMinutes;

    @Field("resolution_time_target_minutes")
    private Integer resolutionTimeTargetMinutes;

    @Field("business_hours_only")
    private Boolean businessHoursOnly;

    @Field("business_hours_config")
    private BusinessHoursConfig businessHoursConfig;

    @Field("applicable_categories")
    private List<String> applicableCategories;

    @Field("applicable_priorities")
    private List<String> applicablePriorities;

    @Field("applicable_channels")
    private List<String> applicableChannels;

    @Field("escalation_rules")
    private List<EscalationRule> escalationRules;

    @Field("timezone")
    private String timezone;

    @Field("grace_period_minutes")
    private Integer gracePeriodMinutes;

    @Field("penalty_config")
    private PenaltyConfig penaltyConfig;

    @Field("notification_config")
    private NotificationConfig notificationConfig;

    @Field("tags")
    private List<String> tags;

    public static SLAPolicy create(String tenantId, String policyName, String policyCode) {
        SLAPolicy policy = new SLAPolicy();
        policy.setId(java.util.UUID.randomUUID().toString());
        policy.setTenantId(tenantId);
        policy.setPolicyName(policyName);
        policy.setPolicyCode(policyCode);
        policy.setIsActive(true);
        policy.setCreatedAt(Instant.now());
        policy.setUpdatedAt(Instant.now());
        return policy;
    }

    public boolean isWithinBusinessHours(Instant timestamp) {
        if (businessHoursOnly == null || !businessHoursOnly) {
            return true;
        }
        if (businessHoursConfig == null) {
            return true;
        }
        java.time.ZonedDateTime zdt = java.time.ZonedDateTime.ofInstant(timestamp, java.time.ZoneId.of(timezone != null ? timezone : "UTC"));
        int hour = zdt.getHour();
        int dayOfWeek = zdt.getDayOfWeek().getValue();

        if (businessHoursConfig.getWorkingDays() != null && !businessHoursConfig.getWorkingDays().contains(dayOfWeek)) {
            return false;
        }

        if (businessHoursConfig.getStartHour() != null && businessHoursConfig.getEndHour() != null) {
            return hour >= businessHoursConfig.getStartHour() && hour < businessHoursConfig.getEndHour();
        }

        return true;
    }

    public enum PolicyPriority {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessHoursConfig {
        private Integer startHour;
        private Integer endHour;
        private List<Integer> workingDays;
        private String timezone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EscalationRule {
        private Integer level;
        private String name;
        private Long triggerAfterMinutes;
        private String escalateTo;
        private String notifyUsers;
        private String notifyTeams;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PenaltyConfig {
        private Boolean enabled;
        private Map<String, Double> penaltyPerBreach;
        private Double maxPenaltyPerTicket;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationConfig {
        private Boolean notifyOnBreach;
        private Boolean notifyBeforeBreach;
        private Integer notifyBeforeMinutes;
        private List<String> notificationRecipients;
    }
}
