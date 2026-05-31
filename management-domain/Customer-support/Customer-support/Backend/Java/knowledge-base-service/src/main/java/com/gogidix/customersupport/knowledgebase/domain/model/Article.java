package com.gogidix.customersupport.knowledgebase.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "kb_articles")
public class Article extends BaseEntity {

    @Field("title")
    @Indexed
    private String title;

    @Field("slug")
    @Indexed(unique = true)
    private String slug;

    @Field("content")
    private String content;

    @Field("summary")
    private String summary;

    @Field("category_id")
    @Indexed
    private String categoryId;

    @Field("category_name")
    private String categoryName;

    @Field("tags")
    @Indexed
    private List<String> tags;

    @Field("status")
    @Indexed
    private ArticleStatus status;

    @Field("author_id")
    private String authorId;

    @Field("author_name")
    private String authorName;

    @Field("views")
    private Integer views;

    @Field("helpful_count")
    private Integer helpfulCount;

    @Field("not_helpful_count")
    private Integer notHelpfulCount;

    @Field("attachments")
    private List<ArticleAttachment> attachments;

    @Field("related_article_ids")
    private List<String> relatedArticleIds;

    @Field("language")
    private String language;

    @Field("locale")
    private String locale;

    @Field("is_featured")
    private Boolean isFeatured;

    @Field("order_index")
    private Integer orderIndex;

    @Field("parent_article_id")
    private String parentArticleId;

    @Field("last_reviewed_at")
    private Instant lastReviewedAt;

    @Field("next_review_at")
    private Instant nextReviewAt;

    @Field("search_keywords")
    private List<String> searchKeywords;

    public static Article create(String tenantId, String title, String content, String categoryId) {
        Article article = new Article();
        article.setId(java.util.UUID.randomUUID().toString());
        article.setTenantId(tenantId);
        article.setTitle(title);
        article.setSlug(generateSlug(title));
        article.setContent(content);
        article.setCategoryId(categoryId);
        article.setStatus(ArticleStatus.DRAFT);
        article.setViews(0);
        article.setHelpfulCount(0);
        article.setNotHelpfulCount(0);
        article.setIsFeatured(false);
        article.setCreatedAt(Instant.now());
        article.setUpdatedAt(Instant.now());
        return article;
    }

    private static String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim() + "-" + System.currentTimeMillis();
    }

    public void incrementView() {
        this.views = (this.views != null ? this.views : 0) + 1;
        this.updateTimestamp();
    }

    public void markHelpful(boolean helpful) {
        if (helpful) {
            this.helpfulCount = (this.helpfulCount != null ? this.helpfulCount : 0) + 1;
        } else {
            this.notHelpfulCount = (this.notHelpfulCount != null ? this.notHelpfulCount : 0) + 1;
        }
        this.updateTimestamp();
    }

    public void publish() {
        this.status = ArticleStatus.PUBLISHED;
        this.updateTimestamp();
    }

    public void archive() {
        this.status = ArticleStatus.ARCHIVED;
        this.updateTimestamp();
    }

    public enum ArticleStatus {
        DRAFT, PUBLISHED, ARCHIVED, UNDER_REVIEW
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleAttachment {
        private String fileName;
        private String fileUrl;
        private String fileSize;
        private String mimeType;
    }
}
