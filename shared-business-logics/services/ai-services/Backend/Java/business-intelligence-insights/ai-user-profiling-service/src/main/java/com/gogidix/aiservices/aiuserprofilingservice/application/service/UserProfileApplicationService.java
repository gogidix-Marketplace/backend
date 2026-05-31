package com.gogidix.aiservices.aiuserprofilingservice.application.service;

import com.gogidix.aiservices.aiuserprofilingservice.application.command.AddUsersToProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.AnalyzeProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.CreateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.DeleteProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.RemoveUsersFromProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.UpdateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.UserProfileResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.ProfileAnalysisResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.mapper.UserProfileMapper;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.UserProfile;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import com.gogidix.aiservices.aiuserprofilingservice.domain.port.out.UserProfileRepositoryPort;
import com.gogidix.aiservices.aiuserprofilingservice.domain.policy.ProfileBusinessPolicy;
import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.ValidationException;
import com.gogidix.aiservices.aiuserprofilingservice.shared.context.RequestContext;
import com.gogidix.aiservices.aiuserprofilingservice.shared.context.RequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Application service for customer segment operations.
 */
@Service
@Transactional(readOnly = true)
public class UserProfileApplicationService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileApplicationService.class);
    private static final String CACHE_NAME = "segments";

    private final UserProfileRepositoryPort repository;
    private final UserProfileMapper mapper;
    private final ProfileBusinessPolicy businessPolicy;
    private final ProfileAnalysisService analysisService;

    public UserProfileApplicationService(
            UserProfileRepositoryPort repository,
            UserProfileMapper mapper,
            ProfileBusinessPolicy businessPolicy,
            ProfileAnalysisService analysisService) {
        this.repository = repository;
        this.mapper = mapper;
        this.businessPolicy = businessPolicy;
        this.analysisService = analysisService;
    }

    /**
     * Create a new segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public UserProfileResponseDto createProfile(CreateProfileCommand command) {
        log.info("Creating segment '{}' for tenant: {}", command.name(), command.tenantId());

        businessPolicy.validateProfileCreation(command.tenantId());

        // Convert Map<String, Object> criteria to ProfileCriteria
        ProfileCriteria criteria = mapToProfileCriteria(command.criteria());

        UserProfile segment = new UserProfile(
                command.tenantId(),
                command.name(),
                criteria
        );

        UserProfile saved = repository.save(segment);
        log.info("Profile created with ID: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Update an existing segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public UserProfileResponseDto updateProfile(UpdateProfileCommand command) {
        log.info("Updating segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        UserProfile existing = findProfileByIdAndTenant(command.segmentId(), command.tenantId());

        if (command.name() != null) {
            existing.updateDetails(command.name(), command.description());
        }

        UserProfile saved = repository.save(existing);
        log.info("Profile updated: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Delete a segment.
     */
    @Transactional
    public void deleteProfile(String segmentId, String tenantId, String userId) {
        log.info("Deleting segment: {} for tenant: {}", segmentId, tenantId);

        UserProfile segment = findProfileByIdAndTenant(segmentId, tenantId);
        businessPolicy.validateProfileDeletion(segment);

        repository.deleteById(segmentId);
        log.info("Profile deleted: {}", segmentId);
    }

    /**
     * Get a segment by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#segmentId + ':' + #tenantId")
    public UserProfileResponseDto getProfileById(String segmentId, String tenantId) {
        log.debug("Fetching segment: {} for tenant: {}", segmentId, tenantId);

        UserProfile segment = findProfileByIdAndTenant(segmentId, tenantId);
        return mapper.toResponseDto(segment);
    }

    /**
     * List segments for a tenant with pagination.
     */
    public PagedResponseDto<UserProfileResponseDto> getProfilesByTenant(
            String tenantId,
            ProfileType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Fetching segments for tenant: {}", tenantId);

        // For now, return empty page since repository doesn't have this method
        List<UserProfileResponseDto> content = new ArrayList<>();
        return new PagedResponseDto<>(
                content,
                page,
                size,
                0,
                0,
                true,
                true
        );
    }

    /**
     * Add customers to a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public UserProfileResponseDto addUsersToProfile(AddUsersToProfileCommand command) {
        log.info("Adding {} customers to segment: {}", command.customerIds().size(), command.segmentId());

        UserProfile segment = findProfileByIdAndTenant(command.segmentId(), command.tenantId());
        businessPolicy.validateUserAddition(segment, command.customerIds().size());

        segment.addUsers(command.customerIds());
        UserProfile saved = repository.save(segment);

        log.info("Added {} customers to segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Remove customers from a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public UserProfileResponseDto removeUsersFromProfile(RemoveUsersFromProfileCommand command) {
        log.info("Removing {} customers from segment: {}", command.customerIds().size(), command.segmentId());

        UserProfile segment = findProfileByIdAndTenant(command.segmentId(), command.tenantId());

        segment.removeUsers(command.customerIds());
        UserProfile saved = repository.save(segment);

        log.info("Removed {} customers from segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Analyze a segment.
     */
    public ProfileAnalysisResponseDto analyzeProfile(AnalyzeProfileCommand command) {
        log.info("Analyzing segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        UserProfile segment = findProfileByIdAndTenant(command.segmentId(), command.tenantId());

        if (!segment.isActive()) {
            throw new ValidationException("Cannot analyze inactive segment");
        }

        return analysisService.analyzeProfile(segment, command.analysisOptions());
    }

    /**
     * Helper method to find a segment by ID and tenant.
     */
    private UserProfile findProfileByIdAndTenant(String segmentId, String tenantId) {
        return repository.findById(segmentId)
                .filter(seg -> seg.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException(
                        "Profile not found with ID: " + segmentId + " for tenant: " + tenantId));
    }

    /**
     * Convert Map<String, Object> to ProfileCriteria.
     */
    private ProfileCriteria mapToProfileCriteria(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }

        String field = (String) criteria.getOrDefault("field", "lifetimeValue");
        String operator = (String) criteria.getOrDefault("operator", ">");
        Object value = criteria.getOrDefault("value", 0);

        return ProfileCriteria.builder()
                .type(ProfileCriteria.CriteriaType.CUSTOM)
                .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                .field(field)
                .value(value)
                .build();
    }
}
