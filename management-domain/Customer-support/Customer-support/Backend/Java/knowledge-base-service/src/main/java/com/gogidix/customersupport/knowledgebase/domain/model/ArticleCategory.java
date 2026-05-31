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
@Document(collection = "kb_categories")
public class ArticleCategory extends BaseEntity {

    @Field("name")
    @Indexed
    private String name;

    @Field("slug")
    @Indexed(unique = true)
    private String slug;

    @Field("description")
    private String description;

    @Field("parent_category_id")
    private String parentCategoryId;

    @Field("icon")
    private String icon;

    @Field("color")
    private String color;

    @Field("order_index")
    private Integer orderIndex;

    @Field("is_active")
    private Boolean isActive;

    @Field("article_count")
    private Integer articleCount;

    public static ArticleCategory create(String tenantId, String name) {
        ArticleCategory category = new ArticleCategory();
        category.setId(java.util.UUID.randomUUID().toString());
        category.setTenantId(tenantId);
        category.setName(name);
        category.setSlug(generateSlug(name));
        category.setIsActive(true);
        category.setArticleCount(0);
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        return category;
    }

    private static String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim() + "-" + System.currentTimeMillis();
    }
}
