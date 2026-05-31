package com.gogidix.corporate.website.domain.repository;

import com.gogidix.corporate.website.domain.model.BlogPost;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BlogPostRepository {
    BlogPost save(BlogPost blogPost);
    Optional<BlogPost> findById(String id);
    Optional<BlogPost> findBySlug(String slug);
    List<BlogPost> findByStatus(ContentStatus status);
    List<BlogPost> findPublishedPosts();
    List<BlogPost> findPublishedPostsByRegion(Region region);
    List<BlogPost> findFeaturedPosts();
    List<BlogPost> findByAuthorId(String authorId);
    List<BlogPost> findByCategoriesContaining(String category);
    List<BlogPost> findByTagsContaining(String tag);
    List<BlogPost> searchByKeyword(String keyword, Language language);
    List<BlogPost> findRecentPosts(int limit);
    List<BlogPost> findRelatedPosts(String postId, int limit);
    void deleteById(String id);
    boolean existsBySlug(String slug);
    List<BlogPost> findPostsScheduledForPublish(LocalDateTime now);
    void incrementViewCount(String postId);
    void incrementLikeCount(String postId);
    long countByStatus(ContentStatus status);
    long countByAuthorId(String authorId);
}
