package com.gogidix.customersupport.slamanagement.application.mapper;

import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyRequestDto;
import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyResponseDto;
import com.gogidix.customersupport.slamanagement.domain.model.SLAPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SLAPolicyMapper {

    public SLAPolicy toEntity(SLAPolicyRequestDto dto, String tenantId) {
        SLAPolicy policy = SLAPolicy.create(tenantId, dto.getPolicyName(), dto.getPolicyCode());

        policy.setDescription(dto.getDescription());
        policy.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        policy.setPriority(dto.getPriority());
        policy.setResponseTimeTargetMinutes(dto.getResponseTimeTargetMinutes());
        policy.setResolutionTimeTargetMinutes(dto.getResolutionTimeTargetMinutes());
        policy.setBusinessHoursOnly(dto.getBusinessHoursOnly());
        policy.setTimezone(dto.getTimezone());
        policy.setGracePeriodMinutes(dto.getGracePeriodMinutes());
        policy.setApplicableCategories(dto.getApplicableCategories());
        policy.setApplicablePriorities(dto.getApplicablePriorities());
        policy.setApplicableChannels(dto.getApplicableChannels());
        policy.setTags(dto.getTags());

        if (dto.getBusinessHoursConfig() != null) {
            policy.setBusinessHoursConfig(SLAPolicy.BusinessHoursConfig.builder()
                    .startHour(dto.getBusinessHoursConfig().getStartHour())
                    .endHour(dto.getBusinessHoursConfig().getEndHour())
                    .workingDays(dto.getBusinessHoursConfig().getWorkingDays())
                    .timezone(dto.getBusinessHoursConfig().getTimezone())
                    .build());
        }

        if (dto.getEscalationRules() != null && !dto.getEscalationRules().isEmpty()) {
            List<SLAPolicy.EscalationRule> rules = dto.getEscalationRules().stream()
                    .map(r -> SLAPolicy.EscalationRule.builder()
                            .level(r.getLevel())
                            .name(r.getName())
                            .triggerAfterMinutes(r.getTriggerAfterMinutes())
                            .escalateTo(r.getEscalateTo())
                            .notifyUsers(r.getNotifyUsers())
                            .notifyTeams(r.getNotifyTeams())
                            .build())
                    .collect(Collectors.toList());
            policy.setEscalationRules(rules);
        }

        if (dto.getPenaltyConfig() != null) {
            policy.setPenaltyConfig(SLAPolicy.PenaltyConfig.builder()
                    .enabled(dto.getPenaltyConfig().getEnabled())
                    .penaltyPerBreach(dto.getPenaltyConfig().getPenaltyPerBreach())
                    .maxPenaltyPerTicket(dto.getPenaltyConfig().getMaxPenaltyPerTicket())
                    .build());
        }

        if (dto.getNotificationConfig() != null) {
            policy.setNotificationConfig(SLAPolicy.NotificationConfig.builder()
                    .notifyOnBreach(dto.getNotificationConfig().getNotifyOnBreach())
                    .notifyBeforeBreach(dto.getNotificationConfig().getNotifyBeforeBreach())
                    .notifyBeforeMinutes(dto.getNotificationConfig().getNotifyBeforeMinutes())
                    .notificationRecipients(dto.getNotificationConfig().getNotificationRecipients())
                    .build());
        }

        return policy;
    }

    public SLAPolicyResponseDto toResponseDto(SLAPolicy entity) {
        SLAPolicyResponseDto.BusinessHoursConfigDto hoursConfigDto = null;
        if (entity.getBusinessHoursConfig() != null) {
            hoursConfigDto = SLAPolicyResponseDto.BusinessHoursConfigDto.builder()
                    .startHour(entity.getBusinessHoursConfig().getStartHour())
                    .endHour(entity.getBusinessHoursConfig().getEndHour())
                    .workingDays(entity.getBusinessHoursConfig().getWorkingDays())
                    .timezone(entity.getBusinessHoursConfig().getTimezone())
                    .build();
        }

        List<SLAPolicyResponseDto.EscalationRuleDto> ruleDtos = null;
        if (entity.getEscalationRules() != null) {
            ruleDtos = entity.getEscalationRules().stream()
                    .map(r -> SLAPolicyResponseDto.EscalationRuleDto.builder()
                            .level(r.getLevel())
                            .name(r.getName())
                            .triggerAfterMinutes(r.getTriggerAfterMinutes())
                            .escalateTo(r.getEscalateTo())
                            .notifyUsers(r.getNotifyUsers())
                            .notifyTeams(r.getNotifyTeams())
                            .build())
                    .collect(Collectors.toList());
        }

        SLAPolicyResponseDto.PenaltyConfigDto penaltyDto = null;
        if (entity.getPenaltyConfig() != null) {
            penaltyDto = SLAPolicyResponseDto.PenaltyConfigDto.builder()
                    .enabled(entity.getPenaltyConfig().getEnabled())
                    .penaltyPerBreach(entity.getPenaltyConfig().getPenaltyPerBreach())
                    .maxPenaltyPerTicket(entity.getPenaltyConfig().getMaxPenaltyPerTicket())
                    .build();
        }

        SLAPolicyResponseDto.NotificationConfigDto notificationDto = null;
        if (entity.getNotificationConfig() != null) {
            notificationDto = SLAPolicyResponseDto.NotificationConfigDto.builder()
                    .notifyOnBreach(entity.getNotificationConfig().getNotifyOnBreach())
                    .notifyBeforeBreach(entity.getNotificationConfig().getNotifyBeforeBreach())
                    .notifyBeforeMinutes(entity.getNotificationConfig().getNotifyBeforeMinutes())
                    .notificationRecipients(entity.getNotificationConfig().getNotificationRecipients())
                    .build();
        }

        return SLAPolicyResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .policyName(entity.getPolicyName())
                .policyCode(entity.getPolicyCode())
                .description(entity.getDescription())
                .isActive(entity.getIsActive())
                .priority(entity.getPriority())
                .responseTimeTargetMinutes(entity.getResponseTimeTargetMinutes())
                .resolutionTimeTargetMinutes(entity.getResolutionTimeTargetMinutes())
                .businessHoursOnly(entity.getBusinessHoursOnly())
                .businessHoursConfig(hoursConfigDto)
                .applicableCategories(entity.getApplicableCategories())
                .applicablePriorities(entity.getApplicablePriorities())
                .applicableChannels(entity.getApplicableChannels())
                .escalationRules(ruleDtos)
                .timezone(entity.getTimezone())
                .gracePeriodMinutes(entity.getGracePeriodMinutes())
                .penaltyConfig(penaltyDto)
                .notificationConfig(notificationDto)
                .tags(entity.getTags())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
