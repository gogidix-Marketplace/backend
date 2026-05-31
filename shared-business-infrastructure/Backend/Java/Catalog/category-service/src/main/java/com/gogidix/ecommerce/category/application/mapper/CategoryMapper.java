package com.gogidix.ecommerce.category.application.mapper;

import com.gogidix.ecommerce.category.application.dto.*;
import com.gogidix.ecommerce.category.domain.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) return null;

        List<CategoryResponse> childrenResponses = null;
        if (category.getChildren() != null) {
            childrenResponses = category.getChildren().stream()
                    .map(this::toCategoryResponse)
                    .collect(Collectors.toList());
        }

        return new CategoryResponse(
                category.getId(),
                category.getCategoryCode(),
                category.getName(),
                category.getDescription(),
                category.getParentId(),
                category.getLevel(),
                category.getPath(),
                category.getAncestorIds(),
                category.getDisplayOrder(),
                category.getIconUrl(),
                category.getBannerUrl(),
                category.getImageUrl(),
                category.getImageUrls(),
                toCategorySeoDto(category.getSeo()),
                toCategoryAttributeDtoList(category.getAttributes()),
                category.getIsLeaf(),
                category.getIsActive(),
                category.getIsVisible(),
                childrenResponses,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public Category toCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setCategoryCode(request.categoryCode());
        category.setName(request.name());
        category.setDescription(request.description());
        category.setParentId(request.parentId());
        category.setDisplayOrder(request.displayOrder());
        category.setIconUrl(request.iconUrl());
        category.setBannerUrl(request.bannerUrl());
        category.setImageUrl(request.imageUrl());
        category.setImageUrls(request.imageUrls());
        category.setIsActive(request.isActive() != null ? request.isActive() : true);
        category.setIsVisible(request.isVisible() != null ? request.isVisible() : true);

        if (request.seo() != null) {
            category.setSeo(toCategorySeo(request.seo()));
        }

        if (request.attributes() != null && !request.attributes().isEmpty()) {
            category.setAttributes(request.attributes().stream()
                    .map(this::toCategoryAttribute)
                    .collect(Collectors.toList()));
        }

        return category;
    }

    public void updateCategoryFromRequest(Category category, UpdateCategoryRequest request) {
        category.setName(request.name());
        category.setDescription(request.description());
        category.setDisplayOrder(request.displayOrder());
        category.setIconUrl(request.iconUrl());
        category.setBannerUrl(request.bannerUrl());
        category.setImageUrl(request.imageUrl());
        category.setImageUrls(request.imageUrls());
        category.setIsActive(request.isActive());
        category.setIsVisible(request.isVisible());

        if (request.seo() != null) {
            category.setSeo(toCategorySeo(request.seo()));
        }

        if (request.attributes() != null && !request.attributes().isEmpty()) {
            category.setAttributes(request.attributes().stream()
                    .map(this::toCategoryAttribute)
                    .collect(Collectors.toList()));
        }
    }

    private CategorySeoDto toCategorySeoDto(Category.CategorySeo seo) {
        if (seo == null) return null;
        return new CategorySeoDto(
                seo.getMetaTitle(),
                seo.getMetaDescription(),
                seo.getMetaKeywords(),
                seo.getSlug()
        );
    }

    private Category.CategorySeo toCategorySeo(CategorySeoDto dto) {
        if (dto == null) return null;
        Category.CategorySeo seo = new Category.CategorySeo();
        seo.setMetaTitle(dto.metaTitle());
        seo.setMetaDescription(dto.metaDescription());
        seo.setMetaKeywords(dto.metaKeywords());
        seo.setSlug(dto.slug());
        return seo;
    }

    private CategoryAttributeDto toCategoryAttributeDto(Category.CategoryAttribute attribute) {
        if (attribute == null) return null;
        return new CategoryAttributeDto(
                attribute.getName(),
                attribute.getCode(),
                attribute.getType(),
                attribute.getIsRequired(),
                attribute.getIsFilterable(),
                attribute.getOptions()
        );
    }

    private List<CategoryAttributeDto> toCategoryAttributeDtoList(List<Category.CategoryAttribute> attributes) {
        if (attributes == null) return List.of();
        return attributes.stream()
                .map(this::toCategoryAttributeDto)
                .collect(Collectors.toList());
    }

    private Category.CategoryAttribute toCategoryAttribute(CategoryAttributeDto dto) {
        if (dto == null) return null;
        Category.CategoryAttribute attribute = new Category.CategoryAttribute();
        attribute.setName(dto.name());
        attribute.setCode(dto.code());
        attribute.setType(dto.type());
        attribute.setIsRequired(dto.isRequired());
        attribute.setIsFilterable(dto.isFilterable());
        attribute.setOptions(dto.options());
        return attribute;
    }
}
