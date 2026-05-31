package com.gogidix.customersupport.knowledgebase.application.service;

import com.gogidix.customersupport.knowledgebase.domain.model.Article;
import com.gogidix.customersupport.knowledgebase.domain.model.ArticleCategory;
import com.gogidix.customersupport.knowledgebase.domain.model.ArticleTag;
import com.gogidix.customersupport.knowledgebase.domain.repository.ArticleCategoryRepository;
import com.gogidix.customersupport.knowledgebase.domain.repository.ArticleRepository;
import com.gogidix.customersupport.knowledgebase.domain.repository.ArticleTagRepository;
import com.gogidix.customersupport.knowledgebase.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeBaseService {

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository articleCategoryRepository;
    private final ArticleTagRepository articleTagRepository;

    public List<Article> getAllArticles() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findByTenantId(tenantId);
    }

    public List<Article> getPublishedArticles() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findByTenantIdAndStatus(tenantId, Article.ArticleStatus.PUBLISHED);
    }

    public Article getArticleById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + id));
    }

    public Article getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with slug: " + slug));
        article.incrementView();
        return articleRepository.save(article);
    }

    public List<Article> getArticlesByCategory(String categoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findPublishedByCategory(tenantId, categoryId);
    }

    public List<Article> getArticlesByTag(String tag) {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findByTenantIdAndTagsContaining(tenantId, tag);
    }

    public List<Article> searchArticles(String keyword) {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.searchArticles(tenantId, keyword);
    }

    public List<Article> getFeaturedArticles() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findByTenantIdAndIsFeaturedTrue(tenantId);
    }

    public List<Article> getMostViewedArticles() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findMostViewed(tenantId);
    }

    public List<Article> getMostHelpfulArticles() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleRepository.findMostHelpful(tenantId);
    }

    @Transactional
    public Article createArticle(String title, String content, String categoryId, String authorId, String authorName) {
        String tenantId = RequestContextHolder.getTenantId();
        Article article = Article.create(tenantId, title, content, categoryId);
        article.setAuthorId(authorId);
        article.setAuthorName(authorName);
        Article saved = articleRepository.save(article);
        log.info("Created article: {}", saved.getTitle());
        return saved;
    }

    @Transactional
    public Article updateArticle(String id, String title, String content, String summary, String categoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        Article article = articleRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + id));

        article.setTitle(title);
        article.setContent(content);
        article.setSummary(summary);
        article.setCategoryId(categoryId);
        article.updateTimestamp();

        return articleRepository.save(article);
    }

    @Transactional
    public Article publishArticle(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Article article = articleRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + id));
        article.publish();
        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        articleRepository.deleteByTenantIdAndId(tenantId, id);
        log.info("Deleted article with id: {}", id);
    }

    @Transactional
    public Article markArticleHelpful(String id, boolean helpful) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + id));
        article.markHelpful(helpful);
        return articleRepository.save(article);
    }

    public List<ArticleCategory> getAllCategories() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleCategoryRepository.findByTenantId(tenantId);
    }

    public List<ArticleCategory> getActiveCategories() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleCategoryRepository.findByTenantIdAndIsActiveTrue(tenantId);
    }

    @Transactional
    public ArticleCategory createCategory(String name, String description) {
        String tenantId = RequestContextHolder.getTenantId();
        ArticleCategory category = ArticleCategory.create(tenantId, name);
        category.setDescription(description);
        return articleCategoryRepository.save(category);
    }

    public List<ArticleTag> getAllTags() {
        String tenantId = RequestContextHolder.getTenantId();
        return articleTagRepository.findByTenantIdOrderByUsageCountDesc(tenantId);
    }

    @Transactional
    public ArticleTag createTag(String name, String description) {
        String tenantId = RequestContextHolder.getTenantId();
        ArticleTag tag = ArticleTag.create(tenantId, name);
        tag.setDescription(description);
        return articleTagRepository.save(tag);
    }
}
