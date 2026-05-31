package com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SocialMediaPost domain model.
 */
@DisplayName("SocialMediaPost Domain Model Tests")
class SocialMediaPostTest {

    @Test
    @DisplayName("Should create social media post with builder")
    void shouldCreateSocialMediaPostWithBuilder() {
        TenantId tenantId = new TenantId("tenant123");

        SocialMediaPost post = SocialMediaPost.builder()
                .tenantId(tenantId)
                .domainContext("ecommerce")
                .postId("post123")
                .postType(SocialMediaPost.PostType.IMAGE)
                .status(SocialMediaPost.PostStatus.DRAFT)
                .content("Test post content")
                .build();

        assertEquals(tenantId, post.getTenantId());
        assertEquals("ecommerce", post.getDomainContext());
        assertEquals("post123", post.getPostId());
        assertEquals(SocialMediaPost.PostType.IMAGE, post.getPostType());
        assertEquals(SocialMediaPost.PostStatus.DRAFT, post.getStatus());
        assertEquals("Test post content", post.getContent());
    }

    @Test
    @DisplayName("Should create social media post with no-args constructor")
    void shouldCreateSocialMediaPostWithNoArgsConstructor() {
        SocialMediaPost post = new SocialMediaPost();

        assertNotNull(post);
        assertNull(post.getTenantId());
        assertNull(post.getPostId());
        assertNull(post.getPostType());
        assertNull(post.getStatus());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        TenantId tenantId = new TenantId("tenant123");
        LocalDateTime now = LocalDateTime.now();

        SocialMediaPost post = new SocialMediaPost();
        post.setTenantId(tenantId);
        post.setId("id123");
        post.setDomainContext("marketing");
        post.setProductId(789L);
        post.setCampaignId(456L);
        post.setPostId("post123");
        post.setPlatformPostId("platform-post-123");
        post.setPostType(SocialMediaPost.PostType.VIDEO);
        post.setStatus(SocialMediaPost.PostStatus.PUBLISHED);
        post.setContent("Post content here");
        post.setMediaUrls("[\"url1\", \"url2\"]");
        post.setLinkUrl("https://example.com");
        post.setHashtags("#test #post");
        post.setMentions("@user1 @user2");
        post.setScheduledAt(now);
        post.setPublishedAt(now);
        post.setExpiresAt(now.plusDays(7));
        post.setLikeCount(100L);
        post.setShareCount(50L);
        post.setCommentCount(25L);
        post.setViewCount(1000L);
        post.setClickCount(75L);
        post.setBoostStatus(SocialMediaPost.BoostStatus.ACTIVE);
        post.setBoostBudget(100.0);
        post.setBoostStartAt(now);
        post.setBoostEndAt(now.plusDays(1));
        post.setConversionTracking(true);
        post.setTrackingPixels("pixel1,pixel2");
        post.setCustomAttributes(Map.of("key", "value"));
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        assertEquals(tenantId, post.getTenantId());
        assertEquals("id123", post.getId());
        assertEquals("marketing", post.getDomainContext());
        assertEquals(789L, post.getProductId());
        assertEquals(456L, post.getCampaignId());
        assertEquals("post123", post.getPostId());
        assertEquals("platform-post-123", post.getPlatformPostId());
        assertEquals(SocialMediaPost.PostType.VIDEO, post.getPostType());
        assertEquals(SocialMediaPost.PostStatus.PUBLISHED, post.getStatus());
        assertEquals("Post content here", post.getContent());
        assertEquals(100L, post.getLikeCount());
        assertEquals(50L, post.getShareCount());
        assertEquals(25L, post.getCommentCount());
        assertEquals(1000L, post.getViewCount());
        assertEquals(75L, post.getClickCount());
        assertEquals(SocialMediaPost.BoostStatus.ACTIVE, post.getBoostStatus());
        assertEquals(100.0, post.getBoostBudget());
        assertTrue(post.getConversionTracking());
    }

    @Test
    @DisplayName("Should handle all post types")
    void shouldHandleAllPostTypes() {
        SocialMediaPost.PostType[] types = SocialMediaPost.PostType.values();

        assertEquals(8, types.length);
        assertEquals(SocialMediaPost.PostType.TEXT, SocialMediaPost.PostType.valueOf("TEXT"));
        assertEquals(SocialMediaPost.PostType.IMAGE, SocialMediaPost.PostType.valueOf("IMAGE"));
        assertEquals(SocialMediaPost.PostType.VIDEO, SocialMediaPost.PostType.valueOf("VIDEO"));
        assertEquals(SocialMediaPost.PostType.LINK, SocialMediaPost.PostType.valueOf("LINK"));
        assertEquals(SocialMediaPost.PostType.CAROUSEL, SocialMediaPost.PostType.valueOf("CAROUSEL"));
        assertEquals(SocialMediaPost.PostType.STORY, SocialMediaPost.PostType.valueOf("STORY"));
        assertEquals(SocialMediaPost.PostType.REEL, SocialMediaPost.PostType.valueOf("REEL"));
        assertEquals(SocialMediaPost.PostType.POLL, SocialMediaPost.PostType.valueOf("POLL"));
    }

    @Test
    @DisplayName("Should handle all post statuses")
    void shouldHandleAllPostStatuses() {
        SocialMediaPost.PostStatus[] statuses = SocialMediaPost.PostStatus.values();

        assertEquals(6, statuses.length);
        assertEquals(SocialMediaPost.PostStatus.DRAFT, SocialMediaPost.PostStatus.valueOf("DRAFT"));
        assertEquals(SocialMediaPost.PostStatus.SCHEDULED, SocialMediaPost.PostStatus.valueOf("SCHEDULED"));
        assertEquals(SocialMediaPost.PostStatus.PUBLISHED, SocialMediaPost.PostStatus.valueOf("PUBLISHED"));
        assertEquals(SocialMediaPost.PostStatus.FAILED, SocialMediaPost.PostStatus.valueOf("FAILED"));
        assertEquals(SocialMediaPost.PostStatus.DELETED, SocialMediaPost.PostStatus.valueOf("DELETED"));
        assertEquals(SocialMediaPost.PostStatus.EXPIRED, SocialMediaPost.PostStatus.valueOf("EXPIRED"));
    }

    @Test
    @DisplayName("Should handle all boost statuses")
    void shouldHandleAllBoostStatuses() {
        SocialMediaPost.BoostStatus[] statuses = SocialMediaPost.BoostStatus.values();

        assertEquals(5, statuses.length);
        assertEquals(SocialMediaPost.BoostStatus.NONE, SocialMediaPost.BoostStatus.valueOf("NONE"));
        assertEquals(SocialMediaPost.BoostStatus.PENDING, SocialMediaPost.BoostStatus.valueOf("PENDING"));
        assertEquals(SocialMediaPost.BoostStatus.ACTIVE, SocialMediaPost.BoostStatus.valueOf("ACTIVE"));
        assertEquals(SocialMediaPost.BoostStatus.COMPLETED, SocialMediaPost.BoostStatus.valueOf("COMPLETED"));
        assertEquals(SocialMediaPost.BoostStatus.FAILED, SocialMediaPost.BoostStatus.valueOf("FAILED"));
    }

    @Test
    @DisplayName("Should check if post is published")
    void shouldCheckIfPostIsPublished() {
        SocialMediaPost post = new SocialMediaPost();
        post.setStatus(SocialMediaPost.PostStatus.PUBLISHED);

        assertTrue(post.isPublished());

        post.setStatus(SocialMediaPost.PostStatus.DRAFT);
        assertFalse(post.isPublished());
    }

    @Test
    @DisplayName("Should check if post is scheduled")
    void shouldCheckIfPostIsScheduled() {
        SocialMediaPost post = new SocialMediaPost();
        post.setStatus(SocialMediaPost.PostStatus.SCHEDULED);
        post.setScheduledAt(LocalDateTime.now().plusHours(1));

        assertTrue(post.isScheduled());

        post.setStatus(SocialMediaPost.PostStatus.DRAFT);
        assertFalse(post.isScheduled());
    }

    @Test
    @DisplayName("Should check if post is boosted")
    void shouldCheckIfPostIsBoosted() {
        SocialMediaPost post = new SocialMediaPost();
        post.setBoostStatus(SocialMediaPost.BoostStatus.ACTIVE);

        assertTrue(post.isBoosted());

        post.setBoostStatus(SocialMediaPost.BoostStatus.NONE);
        assertFalse(post.isBoosted());
    }

    @Test
    @DisplayName("Should check if post is expired")
    void shouldCheckIfPostIsExpired() {
        SocialMediaPost post = new SocialMediaPost();
        post.setExpiresAt(LocalDateTime.now().minusDays(1));

        assertTrue(post.isExpired());

        post.setExpiresAt(LocalDateTime.now().plusDays(1));
        assertFalse(post.isExpired());

        post.setExpiresAt(null);
        assertFalse(post.isExpired());
    }

    @Test
    @DisplayName("Should check if post should publish")
    void shouldCheckIfPostShouldPublish() {
        SocialMediaPost post = new SocialMediaPost();
        post.setStatus(SocialMediaPost.PostStatus.SCHEDULED);
        post.setScheduledAt(LocalDateTime.now().minusMinutes(5));

        assertTrue(post.shouldPublish());

        post.setScheduledAt(LocalDateTime.now().plusMinutes(5));
        assertFalse(post.shouldPublish());
    }

    @Test
    @DisplayName("Should calculate total engagement")
    void shouldCalculateTotalEngagement() {
        SocialMediaPost post = new SocialMediaPost();
        post.setLikeCount(100L);
        post.setShareCount(50L);
        post.setCommentCount(25L);

        assertEquals(175L, post.getTotalEngagement());
    }

    @Test
    @DisplayName("Should calculate engagement rate")
    void shouldCalculateEngagementRate() {
        SocialMediaPost post = new SocialMediaPost();
        post.setLikeCount(100L);
        post.setShareCount(50L);
        post.setCommentCount(25L);

        double engagementRate = post.getEngagementRate(1000L);
        assertEquals(17.5, engagementRate, 0.01);
    }

    @Test
    @DisplayName("Should handle zero follower count for engagement rate")
    void shouldHandleZeroFollowerCountForEngagementRate() {
        SocialMediaPost post = new SocialMediaPost();
        post.setLikeCount(100L);

        double engagementRate = post.getEngagementRate(0L);
        assertEquals(0.0, engagementRate, 0.01);
    }

    @Test
    @DisplayName("Should handle null follower count for engagement rate")
    void shouldHandleNullFollowerCountForEngagementRate() {
        SocialMediaPost post = new SocialMediaPost();
        post.setLikeCount(100L);

        double engagementRate = post.getEngagementRate(null);
        assertEquals(0.0, engagementRate, 0.01);
    }

    @Test
    @DisplayName("Should get and set media URL list")
    void shouldGetAndSetMediaUrlList() {
        SocialMediaPost post = new SocialMediaPost();

        post.setMediaUrlList(java.util.List.of("url1", "url2", "url3"));

        assertNotNull(post.getMediaUrlList());
    }

    @Test
    @DisplayName("Should get and set hashtag list")
    void shouldGetAndSetHashtagList() {
        SocialMediaPost post = new SocialMediaPost();

        post.setHashtagList(java.util.List.of("#test", "#post", "#social"));

        assertNotNull(post.getHashtagList());
    }

    @Test
    @DisplayName("Should get and set custom attributes")
    void shouldGetAndSetCustomAttributes() {
        SocialMediaPost post = new SocialMediaPost();

        post.setCustomAttributes(Map.of("key1", "value1", "key2", "value2"));

        assertNotNull(post.getCustomAttributes());
    }

    @Test
    @DisplayName("Should handle boost budget")
    void shouldHandleBoostBudget() {
        SocialMediaPost post = new SocialMediaPost();
        post.setBoostBudget(250.50);

        assertEquals(250.50, post.getBoostBudget());
    }

    @Test
    @DisplayName("Should handle engagement metrics")
    void shouldHandleEngagementMetrics() {
        SocialMediaPost post = new SocialMediaPost();
        post.setLikeCount(500L);
        post.setShareCount(100L);
        post.setCommentCount(50L);
        post.setViewCount(5000L);
        post.setClickCount(250L);

        assertEquals(500L, post.getLikeCount());
        assertEquals(100L, post.getShareCount());
        assertEquals(50L, post.getCommentCount());
        assertEquals(5000L, post.getViewCount());
        assertEquals(250L, post.getClickCount());
    }
}
