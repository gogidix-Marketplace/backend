package com.gogidix.aiservices.aiuserprofilingservice.domain.port.in;

import com.gogidix.aiservices.aiuserprofilingservice.application.command.AddUsersToProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.AnalyzeProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.CreateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.DeleteProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.RemoveUsersFromProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.UpdateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.UserProfileResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.ProfileAnalysisResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;

/**
 * Input port for customer segment operations.
 * This interface defines the contract for the application service layer.
 */
public interface UserProfileServicePort {

    /**
     * Create a new customer segment.
     */
    UserProfileResponseDto createProfile(CreateProfileCommand command);

    /**
     * Update an existing customer segment.
     */
    UserProfileResponseDto updateProfile(UpdateProfileCommand command);

    /**
     * Delete a customer segment.
     */
    void deleteProfile(String segmentId, String tenantId, String userId);

    /**
     * Find a segment by ID.
     */
    UserProfileResponseDto getProfileById(String segmentId, String tenantId);

    /**
     * Find segments by tenant with filtering.
     */
    PagedResponseDto<UserProfileResponseDto> getProfilesByTenant(
            String tenantId,
            ProfileType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Add customers to a segment.
     */
    UserProfileResponseDto addUsersToProfile(AddUsersToProfileCommand command);

    /**
     * Remove customers from a segment.
     */
    UserProfileResponseDto removeUsersFromProfile(RemoveUsersFromProfileCommand command);

    /**
     * Analyze a segment.
     */
    ProfileAnalysisResponseDto analyzeProfile(AnalyzeProfileCommand command);
}
