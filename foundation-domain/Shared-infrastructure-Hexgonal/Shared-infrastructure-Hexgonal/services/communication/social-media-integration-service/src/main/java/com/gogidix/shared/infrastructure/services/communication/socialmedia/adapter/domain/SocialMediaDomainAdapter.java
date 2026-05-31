package com.gogidix.shared.infrastructure.services.communication.socialmedia.adapter.domain;

import com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model.SocialMediaAccount;
import com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model.SocialMediaPost;

import java.util.List;
import java.util.Map;

/**
 * Interface for Social Media domain-specific adapters
 * Each domain implements this to provide custom business logic and field mapping
 */
public interface SocialMediaDomainAdapter {

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

    /**
     * Adapt social media account for specific domain with domain-specific fields
     *
     * @param account The social media account to adapt
     * @param domain The target domain
     * @return Domain-adapted account data
     */
    Map<String, Object> adaptAccountForDomain(SocialMediaAccount account, String domain);

    /**
     * Adapt social media post for specific domain with domain-specific analytics
     *
     * @param post The social media post to adapt
     * @param domain The target domain
     * @return Domain-adapted post data with analytics
     */
    Map<String, Object> adaptPostForDomain(SocialMediaPost post, String domain);

    /**
     * Get domain-specific analytics for multiple posts
     *
     * @param posts List of posts to analyze
     * @param domain The target domain
     * @return Domain-specific analytics data
     */
    Map<String, Object> getDomainSpecificAnalytics(List<SocialMediaPost> posts, String domain);
}