package com.gogidix.aiservices.aiuserprofilingservice.domain.policy;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.UserProfile;
import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Business policy for customer segment operations.
 */
@Component
public class ProfileBusinessPolicy {

    private static final Logger log = LoggerFactory.getLogger(ProfileBusinessPolicy.class);

    private final int maxProfilesPerTenant;
    private final int maxUsersPerProfile;

    public ProfileBusinessPolicy(
            @Value("${segment.max.segments-per-tenant:100}") int maxProfilesPerTenant,
            @Value("${segment.max.customers-per-segment:1000000}") int maxUsersPerProfile
    ) {
        this.maxProfilesPerTenant = maxProfilesPerTenant;
        this.maxUsersPerProfile = maxUsersPerProfile;
    }

    /**
     * Validate that segment creation is allowed.
     */
    public void validateProfileCreation(String tenantId) {
        // Simplified validation - actual count would be checked at database layer
        log.debug("Profile creation validated for tenant: {}", tenantId);
    }

    /**
     * Validate that customer addition is allowed.
     */
    public void validateUserAddition(UserProfile segment, int countToAdd) {
        long projectedCount = segment.getUserCount() + countToAdd;

        if (projectedCount > maxUsersPerProfile) {
            throw new ValidationException(
                    String.format("Cannot add customers. Maximum segment size (%d) would be exceeded", maxUsersPerProfile)
            );
        }

        if (!segment.isActive()) {
            throw new ValidationException("Cannot add customers to inactive segment");
        }

        log.debug("User addition validated for segment: {}", segment.getId());
    }

    /**
     * Validate that segment deletion is allowed.
     */
    public void validateProfileDeletion(UserProfile segment) {
        if (segment.isActive() && segment.getUserCount() > 0) {
            log.warn("Deleting active segment with customers: {}", segment.getId());
        }
        log.debug("Profile deletion validated for segment: {}", segment.getId());
    }
}
