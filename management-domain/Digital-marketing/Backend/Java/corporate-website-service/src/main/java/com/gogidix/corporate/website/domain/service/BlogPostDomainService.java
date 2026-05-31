package com.gogidix.corporate.website.domain.service;

import com.gogidix.corporate.website.domain.model.BlogPost;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogPostDomainService {

    private final BlogPostRepository blogPostRepository;

    @CacheEvict(value = "blogPosts", allEntries = true)
    public BlogPost createBlogPost(BlogPost blogPost) {
        validateBlogPost(blogPost);
        return blogPostRepository.save(blogPost);
    }

    @CacheEvict(value = "blogPosts", allEntries = true)
    public BlogPost updateBlogPost(BlogPost blogPost) {
        validateBlogPost(blogPost);
        return blogPostRepository.save(blogPost);
    }

    @CacheEvict(value = "blogPosts", allEntries = true)
    public void deleteBlogPost(String id) {
        blogPostRepository.deleteById(id);
    }

    @Cacheable(value = "blogPosts", key = "#id")
    public Optional<BlogPost> getBlogPostById(String id) {
        return blogPostRepository.findById(id);
    }

    @Cacheable(value = "blogPosts", key = "'slug:' + #slug")
    public Optional<BlogPost> getBlogPostBySlug(String slug) {
        return blogPostRepository.findBySlug(slug);
    }

    @Cacheable(value = "blogPosts", key = "'published:' + #region")
    public List<BlogPost> getPublishedPostsByRegion(Region region) {
        return blogPostRepository.findPublishedPostsByRegion(region);
    }

    @Cacheable(value = "blogPosts", key = "'featured:' + #region")
    public List<BlogPost> getFeaturedPosts(Region region) {
        return blogPostRepository.findFeaturedPosts().stream()
                .filter(post -> post.getAvailableRegions().contains(region))
                .sorted((p1, p2) -> {
                    if (p1.getFeaturedOrder() == null && p2.getFeaturedOrder() == null) return 0;
                    if (p1.getFeaturedOrder() == null) return 1;
                    if (p2.getFeaturedOrder() == null) return -1;
                    return p1.getFeaturedOrder().compareTo(p2.getFeaturedOrder());
                })
                .toList();
    }

    @Cacheable(value = "blogPosts", key = "'recent:' + #region + ':' + #limit")
    public List<BlogPost> getRecentPosts(Region region, int limit) {
        return blogPostRepository.findPublishedPostsByRegion(region).stream()
                .limit(limit)
                .toList();
    }

    public List<BlogPost> searchBlogPosts(String keyword, Language language) {
        return blogPostRepository.searchByKeyword(keyword, language);
    }

    public List<BlogPost> getPostsByCategory(String category) {
        return blogPostRepository.findByCategoriesContaining(category);
    }

    public List<BlogPost> getPostsByTag(String tag) {
        return blogPostRepository.findByTagsContaining(tag);
    }

    public List<BlogPost> getPostsByAuthor(String authorId) {
        return blogPostRepository.findByAuthorId(authorId);
    }

    public List<BlogPost> getRelatedPosts(String postId, int limit) {
        return blogPostRepository.findRelatedPosts(postId, limit);
    }

    public List<BlogPost> getScheduledPosts() {
        return blogPostRepository.findPostsScheduledForPublish(LocalDateTime.now());
    }

    @CacheEvict(value = "blogPosts", allEntries = true)
    public BlogPost publishBlogPost(String id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));
        post.setStatus(ContentStatus.PUBLISHED);
        post.setPublishDate(LocalDateTime.now());
        return blogPostRepository.save(post);
    }

    @CacheEvict(value = "blogPosts", allEntries = true)
    public void incrementViewCount(String postId) {
        blogPostRepository.incrementViewCount(postId);
    }

    @CacheEvict(value = "blogPosts", allEntries = true)
    public void incrementLikeCount(String postId) {
        blogPostRepository.incrementLikeCount(postId);
    }

    private void validateBlogPost(BlogPost blogPost) {
        if (blogPost.getSlug() == null || blogPost.getSlug().isBlank()) {
            throw new IllegalArgumentException("Blog post slug cannot be null or blank");
        }
        if (blogPost.getLocalizedContent() == null || blogPost.getLocalizedContent().isEmpty()) {
            throw new IllegalArgumentException("Blog post must have at least one localized content");
        }
    }

    public long getBlogPostCount() {
        return blogPostRepository.countByStatus(ContentStatus.PUBLISHED);
    }
}
