package com.gogidix.ecommerce.category.domain.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test void create() {
        Category c = Category.create("t1", "ELEC", "Electronics");
        assertThat(c.getCategoryCode()).isEqualTo("ELEC");
        assertThat(c.getName()).isEqualTo("Electronics");
        assertThat(c.getIsActive()).isTrue();
        assertThat(c.getIsVisible()).isTrue();
        assertThat(c.getIsLeaf()).isTrue();
    }

    @Test void allFields() {
        Category c = new Category("t1");
        c.setCategoryCode("CLOTH"); c.setName("Clothing"); c.setDescription("Apparel");
        c.setParentId("p1"); c.setLevel(2); c.setPath("/root/cloth");
        c.setAncestorIds(List.of("root", "cloth")); c.setDisplayOrder(5);
        c.setIconUrl("http://icon"); c.setBannerUrl("http://banner");
        c.setImageUrl("http://img"); c.setImageUrls(List.of("http://img1"));
        c.setIsLeaf(false); c.setIsActive(true); c.setIsVisible(true);
        assertThat(c.getCategoryCode()).isEqualTo("CLOTH");
        assertThat(c.getDescription()).isEqualTo("Apparel");
        assertThat(c.getParentId()).isEqualTo("p1");
        assertThat(c.getLevel()).isEqualTo(2);
        assertThat(c.getAncestorIds()).containsExactly("root", "cloth");
        assertThat(c.getDisplayOrder()).isEqualTo(5);
        assertThat(c.getIsLeaf()).isFalse();
    }

    @Test void categorySeo() {
        Category.CategorySeo seo = new Category.CategorySeo();
        seo.setMetaTitle("Title"); seo.setMetaDescription("Desc");
        seo.setMetaKeywords("k1,k2"); seo.setSlug("electronics");
        assertThat(seo.getMetaTitle()).isEqualTo("Title");
        assertThat(seo.getSlug()).isEqualTo("electronics");
    }

    @Test void categoryAttribute() {
        Category.CategoryAttribute attr = new Category.CategoryAttribute();
        attr.setName("Color"); attr.setCode("color"); attr.setType("STRING");
        attr.setIsRequired(true); attr.setIsFilterable(true);
        attr.setOptions(List.of("red", "blue"));
        assertThat(attr.getName()).isEqualTo("Color");
        assertThat(attr.getIsRequired()).isTrue();
        assertThat(attr.getOptions()).containsExactly("red", "blue");
    }

    @Test void defaults() {
        Category c = new Category();
        assertThat(c.getIsActive()).isTrue();
        assertThat(c.getIsVisible()).isTrue();
        assertThat(c.getIsLeaf()).isTrue();
    }

    @Test void children() {
        Category parent = Category.create("t1", "ROOT", "Root");
        Category child = Category.create("t1", "CHILD", "Child");
        parent.setChildren(List.of(child));
        parent.setIsLeaf(false);
        assertThat(parent.getChildren()).hasSize(1);
        assertThat(parent.getChildren().get(0).getCategoryCode()).isEqualTo("CHILD");
    }
}