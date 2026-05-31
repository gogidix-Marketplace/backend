package com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.port.out;

import com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model.SocialMediaAccount;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;

import java.util.List;
import java.util.Optional;

/**
 * Output port for SocialMediaAccount repository
 */
public interface SocialMediaAccountRepositoryPort {

    SocialMediaAccount save(SocialMediaAccount entity);

    Optional<SocialMediaAccount> findById(String id);

    Optional<SocialMediaAccount> findByIdAndTenantId(String id, TenantId tenantId);

    List<SocialMediaAccount> findByTenantId(TenantId tenantId);

    List<SocialMediaAccount> findByUserId(Long userId);

    List<SocialMediaAccount> findByPlatform(SocialMediaAccount.SocialPlatform platform);

    Optional<SocialMediaAccount> findByPlatformUserIdAndTenantId(String platformUserId, TenantId tenantId);

    long countByTenantId(TenantId tenantId);

    void deleteById(String id);

    void deleteByTenantId(TenantId tenantId);

    boolean existsByIdAndTenantId(String id, TenantId tenantId);

    List<SocialMediaAccount> findActiveAccounts();
}
