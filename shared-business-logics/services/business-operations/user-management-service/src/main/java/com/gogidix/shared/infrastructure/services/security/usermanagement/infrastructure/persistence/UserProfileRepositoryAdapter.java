package com.gogidix.shared.infrastructure.services.security.usermanagement.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.port.out.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> findByIdAndTenantId(String userId, String tenantId) {
        return userProfileRepository.findById(userId)
                .filter(p -> p.getTenantId() != null && tenantId.equals(p.getTenantId().getValue()));
    }

    @Override
    public List<UserProfile> findByTenantId(String tenantId) {
        return userProfileRepository.findByTenantId_Value(tenantId);
    }

    @Override
    public void delete(UserProfile userProfile) {
        userProfileRepository.delete(userProfile);
    }

    @Override
    public boolean existsByIdAndTenantId(String userId, String tenantId) {
        return userProfileRepository.existsByIdAndTenantId_Value(userId, tenantId);
    }
}
