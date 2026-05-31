package com.gogidix.customersupport.qualitymanagement.application.service;

import com.gogidix.customersupport.qualitymanagement.application.dto.ScorecardTemplateDto;
import com.gogidix.customersupport.qualitymanagement.application.mapper.QualityManagementMapper;
import com.gogidix.customersupport.qualitymanagement.domain.model.ScorecardTemplate;
import com.gogidix.customersupport.qualitymanagement.domain.repository.ScorecardTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Scorecard Template operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScorecardTemplateService {

    private final ScorecardTemplateRepository templateRepository;
    private final QualityManagementMapper mapper;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Create a new scorecard template
     */
    @Transactional
    @CacheEvict(value = "scorecardTemplates", allEntries = true)
    public ScorecardTemplateDto createTemplate(ScorecardTemplateDto.CreateScorecardTemplateRequest request) {
        log.info("Creating scorecard template for tenant: {}, name: {}",
                request.getTenantId(), request.getTemplateName());

        ScorecardTemplate template = new ScorecardTemplate(request.getTenantId());
        template.setTemplateName(request.getTemplateName());
        template.setTemplateCode(request.getTemplateCode());
        template.setDescription(request.getDescription());

        if (request.getTemplateType() != null) {
            template.setTemplateType(ScorecardTemplate.TemplateType.valueOf(request.getTemplateType()));
        }

        if (request.getChannelType() != null) {
            template.setChannelType(com.gogidix.customersupport.qualitymanagement.domain.model.QaReview.ChannelType.valueOf(request.getChannelType()));
        }

        template.setCategory(request.getCategory());
        template.setMaxScore(request.getMaxScore());
        template.setPassingPercentage(request.getPassingPercentage());
        template.setWeight(request.getWeight());
        template.setAllowPartialCredit(request.getAllowPartialCredit());
        template.setCriticalFailureEnabled(request.getCriticalFailureEnabled());
        template.setCriticalFailureThreshold(request.getCriticalFailureThreshold());
        template.setEffectiveFrom(request.getEffectiveFrom());
        template.setEffectiveUntil(request.getEffectiveUntil());
        template.setTags(request.getTags() != null ? request.getTags() : new java.util.ArrayList<>());

        // Set criteria sections
        if (request.getCriteriaSections() != null && !request.getCriteriaSections().isEmpty()) {
            for (ScorecardTemplateDto.CriteriaSectionDto sectionDto : request.getCriteriaSections()) {
                ScorecardTemplate.CriteriaSection section = ScorecardTemplate.CriteriaSection.builder()
                        .sectionId(sectionDto.getSectionId())
                        .sectionName(sectionDto.getSectionName())
                        .description(sectionDto.getDescription())
                        .order(sectionDto.getOrder())
                        .weight(sectionDto.getWeight())
                        .maxScore(sectionDto.getMaxScore())
                        .isRequired(sectionDto.getIsRequired())
                        .instructions(sectionDto.getInstructions())
                        .build();

                if (sectionDto.getCriteria() != null) {
                    List<ScorecardTemplate.Criteria> criteriaList = sectionDto.getCriteria().stream()
                            .map(cDto -> ScorecardTemplate.Criteria.builder()
                                    .criteriaId(cDto.getCriteriaId())
                                    .criteriaName(cDto.getCriteriaName())
                                    .description(cDto.getDescription())
                                    .categoryId(cDto.getCategoryId())
                                    .maxScore(cDto.getMaxScore())
                                    .weight(cDto.getWeight())
                                    .isCritical(cDto.getIsCritical())
                                    .isRequired(cDto.getIsRequired())
                                    .order(cDto.getOrder())
                                    .scoringGuidance(cDto.getScoringGuidance())
                                    .examples(cDto.getExamples())
                                    .redFlags(cDto.getRedFlags())
                                    .criteriaType(cDto.getCriteriaType() != null ?
                                            ScorecardTemplate.Criteria.CriteriaType.valueOf(cDto.getCriteriaType()) : null)
                                    .build())
                            .collect(Collectors.toList());
                    section.setCriteria(criteriaList);
                }

                template.addCriteriaSection(section);
            }
        }

        template.calculateTotalCriteriaCount();
        template.calculatePassingScore();

        ScorecardTemplate saved = templateRepository.save(template);
        log.info("Created scorecard template with ID: {}", saved.getTemplateId());

        return mapper.toDto(saved);
    }

    /**
     * Get template by ID
     */
    @Cacheable(value = "scorecardTemplates", key = "#templateId")
    public ScorecardTemplateDto getTemplateById(String templateId) {
        log.debug("Fetching scorecard template: {}", templateId);
        return templateRepository.findByTemplateId(templateId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Scorecard template not found: " + templateId));
    }

    /**
     * Get all templates for tenant
     */
    @Cacheable(value = "scorecardTemplates", key = "'tenant:' + #tenantId")
    public List<ScorecardTemplateDto> getAllTemplates(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        log.debug("Fetching scorecard templates for tenant: {}", tenantId);

        return templateRepository.findByTenantIdOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get active templates
     */
    public List<ScorecardTemplateDto> getActiveTemplates(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return templateRepository.findByTenantIdAndIsActiveTrueOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get templates by type
     */
    public List<ScorecardTemplateDto> getTemplatesByType(String tenantId, String templateType) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        ScorecardTemplate.TemplateType type = ScorecardTemplate.TemplateType.valueOf(templateType);

        return templateRepository.findByTenantIdAndTemplateTypeOrderByCreatedAtDesc(tenantId, type)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get templates by channel type
     */
    public List<ScorecardTemplateDto> getTemplatesByChannelType(String tenantId, String channelType) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        com.gogidix.customersupport.qualitymanagement.domain.model.QaReview.ChannelType channel =
                com.gogidix.customersupport.qualitymanagement.domain.model.QaReview.ChannelType.valueOf(channelType);

        return templateRepository.findByTenantIdAndChannelTypeOrderByCreatedAtDesc(tenantId, channel)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get default template
     */
    public ScorecardTemplateDto getDefaultTemplate(String tenantId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return templateRepository.findByTenantIdAndIsDefaultTrue(effectiveTenantId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("No default template found for tenant: " + effectiveTenantId));
    }

    /**
     * Update scorecard template
     */
    @Transactional
    @CacheEvict(value = "scorecardTemplates", allEntries = true)
    public ScorecardTemplateDto updateTemplate(String templateId, ScorecardTemplateDto.UpdateScorecardTemplateRequest request) {
        log.info("Updating scorecard template: {}", templateId);

        ScorecardTemplate template = templateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Scorecard template not found: " + templateId));

        if (request.getTemplateName() != null) {
            template.setTemplateName(request.getTemplateName());
        }
        if (request.getDescription() != null) {
            template.setDescription(request.getDescription());
        }
        if (request.getTemplateType() != null) {
            template.setTemplateType(ScorecardTemplate.TemplateType.valueOf(request.getTemplateType()));
        }
        if (request.getChannelType() != null) {
            template.setChannelType(com.gogidix.customersupport.qualitymanagement.domain.model.QaReview.ChannelType.valueOf(request.getChannelType()));
        }
        if (request.getCategory() != null) {
            template.setCategory(request.getCategory());
        }
        if (request.getMaxScore() != null) {
            template.setMaxScore(request.getMaxScore());
        }
        if (request.getPassingPercentage() != null) {
            template.setPassingPercentage(request.getPassingPercentage());
        }
        if (request.getWeight() != null) {
            template.setWeight(request.getWeight());
        }
        if (request.getAllowPartialCredit() != null) {
            template.setAllowPartialCredit(request.getAllowPartialCredit());
        }
        if (request.getCriticalFailureEnabled() != null) {
            template.setCriticalFailureEnabled(request.getCriticalFailureEnabled());
        }
        if (request.getCriticalFailureThreshold() != null) {
            template.setCriticalFailureThreshold(request.getCriticalFailureThreshold());
        }
        if (request.getEffectiveFrom() != null) {
            template.setEffectiveFrom(request.getEffectiveFrom());
        }
        if (request.getEffectiveUntil() != null) {
            template.setEffectiveUntil(request.getEffectiveUntil());
        }
        if (request.getTags() != null) {
            template.setTags(request.getTags());
        }

        if (request.getCriteriaSections() != null) {
            template.setCriteriaSections(request.getCriteriaSections().stream()
                    .map(sectionDto -> {
                        ScorecardTemplate.CriteriaSection section = ScorecardTemplate.CriteriaSection.builder()
                                .sectionId(sectionDto.getSectionId())
                                .sectionName(sectionDto.getSectionName())
                                .description(sectionDto.getDescription())
                                .order(sectionDto.getOrder())
                                .weight(sectionDto.getWeight())
                                .maxScore(sectionDto.getMaxScore())
                                .isRequired(sectionDto.getIsRequired())
                                .instructions(sectionDto.getInstructions())
                                .build();

                        if (sectionDto.getCriteria() != null) {
                            List<ScorecardTemplate.Criteria> criteriaList = sectionDto.getCriteria().stream()
                                    .map(cDto -> ScorecardTemplate.Criteria.builder()
                                            .criteriaId(cDto.getCriteriaId())
                                            .criteriaName(cDto.getCriteriaName())
                                            .description(cDto.getDescription())
                                            .categoryId(cDto.getCategoryId())
                                            .maxScore(cDto.getMaxScore())
                                            .weight(cDto.getWeight())
                                            .isCritical(cDto.getIsCritical())
                                            .isRequired(cDto.getIsRequired())
                                            .order(cDto.getOrder())
                                            .scoringGuidance(cDto.getScoringGuidance())
                                            .examples(cDto.getExamples())
                                            .redFlags(cDto.getRedFlags())
                                            .criteriaType(cDto.getCriteriaType() != null ?
                                                    ScorecardTemplate.Criteria.CriteriaType.valueOf(cDto.getCriteriaType()) : null)
                                            .build())
                                    .collect(Collectors.toList());
                            section.setCriteria(criteriaList);
                        }

                        return section;
                    })
                    .collect(Collectors.toList()));
            template.calculateTotalCriteriaCount();
        }

        template.calculatePassingScore();
        template.updateTimestamp();

        ScorecardTemplate saved = templateRepository.save(template);
        log.info("Updated scorecard template: {}", templateId);

        return mapper.toDto(saved);
    }

    /**
     * Activate template
     */
    @Transactional
    @CacheEvict(value = "scorecardTemplates", allEntries = true)
    public ScorecardTemplateDto activateTemplate(String templateId) {
        log.info("Activating scorecard template: {}", templateId);

        ScorecardTemplate template = templateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Scorecard template not found: " + templateId));

        template.setIsActive(true);
        template.setTemplateStatus(ScorecardTemplate.TemplateStatus.ACTIVE);
        template.updateTimestamp();

        ScorecardTemplate saved = templateRepository.save(template);
        return mapper.toDto(saved);
    }

    /**
     * Deactivate template
     */
    @Transactional
    @CacheEvict(value = "scorecardTemplates", allEntries = true)
    public ScorecardTemplateDto deactivateTemplate(String templateId) {
        log.info("Deactivating scorecard template: {}", templateId);

        ScorecardTemplate template = templateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Scorecard template not found: " + templateId));

        template.setIsActive(false);
        template.setTemplateStatus(ScorecardTemplate.TemplateStatus.INACTIVE);
        template.updateTimestamp();

        ScorecardTemplate saved = templateRepository.save(template);
        return mapper.toDto(saved);
    }

    /**
     * Set as default template
     */
    @Transactional
    @CacheEvict(value = "scorecardTemplates", allEntries = true)
    public ScorecardTemplateDto setAsDefault(String templateId, String tenantId) {
        log.info("Setting scorecard template as default: {}", templateId);

        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        // Remove default from existing default template
        templateRepository.findByTenantIdAndIsDefaultTrue(tenantId).ifPresent(t -> {
            t.setIsDefault(false);
            templateRepository.save(t);
        });

        // Set new default
        ScorecardTemplate template = templateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Scorecard template not found: " + templateId));

        template.setIsDefault(true);
        template.updateTimestamp();

        ScorecardTemplate saved = templateRepository.save(template);
        return mapper.toDto(saved);
    }

    /**
     * Approve template
     */
    @Transactional
    @CacheEvict(value = "scorecardTemplates", allEntries = true)
    public ScorecardTemplateDto approveTemplate(String templateId, String approverId) {
        log.info("Approving scorecard template: {} by: {}", templateId, approverId);

        ScorecardTemplate template = templateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Scorecard template not found: " + templateId));

        template.setTemplateStatus(ScorecardTemplate.TemplateStatus.ACTIVE);
        template.setApprovedBy(approverId);
        template.setApprovedAt(Instant.now());
        template.setIsActive(true);
        template.updateTimestamp();

        ScorecardTemplate saved = templateRepository.save(template);
        return mapper.toDto(saved);
    }

    /**
     * Delete template
     */
    @Transactional
    @CacheEvict(value = "scorecardTemplates", allEntries = true)
    public void deleteTemplate(String templateId) {
        log.info("Deleting scorecard template: {}", templateId);

        ScorecardTemplate template = templateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Scorecard template not found: " + templateId));

        templateRepository.delete(template);
        log.info("Deleted scorecard template: {}", templateId);
    }

    /**
     * Search templates by name
     */
    public List<ScorecardTemplateDto> searchTemplates(String tenantId, String namePattern) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return templateRepository.searchByTemplateName(tenantId, namePattern)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
