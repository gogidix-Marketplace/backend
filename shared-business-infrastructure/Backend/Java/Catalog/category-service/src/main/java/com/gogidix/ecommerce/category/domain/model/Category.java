package com.gogidix.ecommerce.category.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "categories")
public class Category extends BaseEntity {

    @Indexed(unique = true)
    @Field("category_code")
    private String categoryCode;

    private String name;
    private String description;

    private String parentId;
    private Integer level;
    private String path;
    private List<String> ancestorIds;

    private Integer displayOrder;
    private String iconUrl;
    private String bannerUrl;

    private String imageUrl;
    private List<String> imageUrls;

    private CategorySeo seo;
    private List<CategoryAttribute> attributes;

    private Boolean isLeaf;
    @Indexed
    @Field("is_active")
    private Boolean isActive;
    @Field("is_visible")
    private Boolean isVisible;

    private List<Category> children;

    public Category() {
        super();
        this.isActive = true;
        this.isVisible = true;
        this.isLeaf = true;
    }

    public Category(String tenantId) {
        super(tenantId);
        this.isActive = true;
        this.isVisible = true;
        this.isLeaf = true;
    }

    public static Category create(String tenantId, String categoryCode, String name) {
        Category category = new Category(tenantId);
        category.categoryCode = categoryCode;
        category.name = name;
        return category;
    }

    @Data
    public static class CategorySeo {
        private String metaTitle;
        private String metaDescription;
        private String metaKeywords;
        private String slug;
    }

    @Data
    public static class CategoryAttribute {
        private String name;
        private String code;
        private String type;
        @Field("is_required")
        private Boolean isRequired;
        @Field("is_filterable")
        private Boolean isFilterable;
        private List<String> options;
    }
}
