package com.gogidix.shared.infrastructure.services.security.usermanagement.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    List<UserProfile> findByTenantId_Value(String tenantId);

    boolean existsByIdAndTenantId_Value(String id, String tenantId);
}
