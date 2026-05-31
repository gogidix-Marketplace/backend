package com.gogidix.ecommerce.category.application.mapper;

import com.gogidix.ecommerce.category.application.dto.*;
import com.gogidix.ecommerce.category.domain.model.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapper();

    private Category createCategory() {
        Category c = new Category("t1");
        c.setId("id1");
        c.setCategoryCode("CAT1");
        c.setName("Electronics");
        c.setDescription("Electronic items");
        c.setParentId("parent1");
        c.setLevel(1);
        c.setPath("/root/electronics");
        c.setAncestorIds(List.of("root"));
        c.setDisplayOrder(1);
        c.setIconUrl("icon.png");
        c.setBannerUrl("banner.png");
        c.setImageUrl("image.png");
        c.setImageUrls(List.of("img1.png", "img2.png"));
        c.setIsLeaf(false);
        c.setIsActive(true);
        c.setIsVisible(true);

        Category.CategorySeo seo = new Category.CategorySeo();
        seo.setMetaTitle("Electronics");
        seo.setMetaDescription("All electronics");
        seo.setMetaKeywords("electronics,gadgets");
        seo.setSlug("electronics");
        c.setSeo(seo);

        Category.CategoryAttribute attr = new Category.CategoryAttribute();
        attr.setName("Color");
        attr.setCode("color");
        attr.setType("string");
        attr.setIsRequired(true);
        attr.setIsFilterable(true);
        attr.setOptions(List.of("red", "blue"));
        c.setAttributes(List.of(attr));

        return c;
    }

    @Test
    void toResponse_full() {
        CategoryResponse resp = mapper.toCategoryResponse(createCategory());
        assertThat(resp.categoryCode()).isEqualTo("CAT1");
        assertThat(resp.name()).isEqualTo("Electronics");
        assertThat(resp.seo()).isNotNull();
        assertThat(resp.seo().metaTitle()).isEqualTo("Electronics");
        assertThat(resp.attributes()).hasSize(1);
        assertThat(resp.attributes().get(0).name()).isEqualTo("Color");
    }

    @Test
    void toResponse_null() {
        assertThat(mapper.toCategoryResponse(null)).isNull();
    }

    @Test
    void toResponse_withChildren() {
        Category parent = createCategory();
        Category child = new Category("t1");
        child.setId("child1");
        child.setCategoryCode("CHILD1");
        child.setName("Phones");
        parent.setChildren(List.of(child));
        CategoryResponse resp = mapper.toCategoryResponse(parent);
        assertThat(resp.children()).hasSize(1);
        assertThat(resp.children().get(0).name()).isEqualTo("Phones");
    }

    @Test
    void toResponse_nullChildren() {
        Category c = createCategory();
        c.setChildren(null);
        CategoryResponse resp = mapper.toCategoryResponse(c);
        assertThat(resp.children()).isNull();
    }

    @Test
    void toResponse_nullSeo() {
        Category c = createCategory();
        c.setSeo(null);
        CategoryResponse resp = mapper.toCategoryResponse(c);
        assertThat(resp.seo()).isNull();
    }

    @Test
    void toResponse_nullAttributes() {
        Category c = createCategory();
        c.setAttributes(null);
        CategoryResponse resp = mapper.toCategoryResponse(c);
        assertThat(resp.attributes()).isEmpty();
    }

    @Test
    void toResponseList() {
        List<CategoryResponse> list = mapper.toCategoryResponseList(List.of(createCategory()));
        assertThat(list).hasSize(1);
        assertThat(list.get(0).categoryCode()).isEqualTo("CAT1");
    }

    @Test
    void toCategory_full() {
        CategorySeoDto seoDto = new CategorySeoDto("Title", "Desc", "kw", "slug");
        CategoryAttributeDto attrDto = new CategoryAttributeDto("Color", "color", "string", true, true, List.of("red"));
        CreateCategoryRequest req = new CreateCategoryRequest(
                "CAT1", "Electronics", "desc", "parent1", 1,
                "icon.png", "banner.png", "image.png", List.of("img1.png"),
                true, true, seoDto, List.of(attrDto)
        );
        Category c = mapper.toCategory(req);
        assertThat(c.getCategoryCode()).isEqualTo("CAT1");
        assertThat(c.getName()).isEqualTo("Electronics");
        assertThat(c.getSeo()).isNotNull();
        assertThat(c.getSeo().getSlug()).isEqualTo("slug");
        assertThat(c.getAttributes()).hasSize(1);
    }

    @Test
    void toCategory_nullDefaults() {
        CreateCategoryRequest req = new CreateCategoryRequest(
                "CAT1", "Name", null, null, null,
                null, null, null, null,
                null, null, null, null
        );
        Category c = mapper.toCategory(req);
        assertThat(c.getIsActive()).isTrue();
        assertThat(c.getIsVisible()).isTrue();
    }

    @Test
    void toCategory_falseValues() {
        CreateCategoryRequest req = new CreateCategoryRequest(
                "CAT1", "Name", null, null, null,
                null, null, null, null,
                false, false, null, null
        );
        Category c = mapper.toCategory(req);
        assertThat(c.getIsActive()).isFalse();
        assertThat(c.getIsVisible()).isFalse();
    }

    @Test
    void toCategory_emptyAttributes() {
        CreateCategoryRequest req = new CreateCategoryRequest(
                "CAT1", "Name", null, null, null,
                null, null, null, null,
                true, true, null, List.of()
        );
        Category c = mapper.toCategory(req);
        assertThat(c.getAttributes()).isNull();
    }

    @Test
    void updateCategory() {
        Category c = createCategory();
        CategorySeoDto seoDto = new CategorySeoDto("NewTitle", "NewDesc", "kw2", "new-slug");
        CategoryAttributeDto attrDto = new CategoryAttributeDto("Size", "size", "int", false, false, List.of("S", "M"));
        UpdateCategoryRequest req = new UpdateCategoryRequest(
                "Updated", "new desc", 2, "new-icon.png", "new-banner.png",
                "new-image.png", List.of("new.png"), false, false,
                seoDto, List.of(attrDto)
        );
        mapper.updateCategoryFromRequest(c, req);
        assertThat(c.getName()).isEqualTo("Updated");
        assertThat(c.getIsActive()).isFalse();
        assertThat(c.getSeo().getMetaTitle()).isEqualTo("NewTitle");
        assertThat(c.getAttributes()).hasSize(1);
    }

    @Test
    void updateCategory_nullSeoAndAttributes() {
        Category c = createCategory();
        UpdateCategoryRequest req = new UpdateCategoryRequest(
                "Updated", null, null, null, null, null, null, null, null,
                null, null
        );
        mapper.updateCategoryFromRequest(c, req);
        assertThat(c.getName()).isEqualTo("Updated");
        assertThat(c.getSeo()).isNotNull();
    }
}
