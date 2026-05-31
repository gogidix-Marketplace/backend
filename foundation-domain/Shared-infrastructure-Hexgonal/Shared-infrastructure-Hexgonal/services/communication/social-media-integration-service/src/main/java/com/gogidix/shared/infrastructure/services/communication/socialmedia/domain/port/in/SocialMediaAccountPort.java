package com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.port.in;

import com.gogidix.shared.infrastructure.services.communication.socialmedia.application.dto.response.SocialMediaPostResponse;
import com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model.SocialMediaAccount;

import java.util.List;
import java.util.Optional;

/**
 * Input port for SocialMediaAccount use cases
 */
public interface SocialMediaAccountPort {

    SocialMediaAccount createAccount(SocialMediaAccount account);

    Optional<SocialMediaAccount> getAccount(String id);

    List<SocialMediaAccount> getAccountsByTenantId(String tenantId);

    List<SocialMediaAccount> getAccountsByUserId(Long userId);

    SocialMediaAccount updateAccount(String id, SocialMediaAccount account);

    void deleteAccount(String id);

    void syncAccount(String id);

    SocialMediaPostResponse postToSocialMedia(String accountId, String content, List<String> mediaUrls);

    boolean isTokenValid(String accountId);
}
