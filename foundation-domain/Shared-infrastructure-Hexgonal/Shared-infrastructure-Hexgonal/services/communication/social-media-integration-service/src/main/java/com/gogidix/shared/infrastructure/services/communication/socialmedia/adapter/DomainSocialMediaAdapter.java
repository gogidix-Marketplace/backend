package com.gogidix.shared.infrastructure.services.communication.socialmedia.adapter;

import com.gogidix.shared.infrastructure.services.communication.socialmedia.application.dto.response.SocialMediaPostResponse;
import com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model.SocialMediaPost;

/**
 * Interface for domain-specific social media adapters
 * Each domain implements this to provide custom business logic and field mapping
 */
public interface DomainSocialMediaAdapter {

    /**
     * Adapt social media post for specific domain with domain-specific analytics
     *
     * @param post The social media post to adapt
     * @param domain The target domain
     * @return Domain-adapted post response with analytics
     */
    SocialMediaPostResponse adaptForDomain(SocialMediaPost post, String domain);

    /**
     * Check if this adapter supports the given domain
     *
     * @param domain The domain to check
     * @return true if supported, false otherwise
     */
    boolean supportsDomain(String domain);

    /**
     * Get the domain name this adapter handles
     *
     * @return The domain name
     */
    String getDomainName();
}
