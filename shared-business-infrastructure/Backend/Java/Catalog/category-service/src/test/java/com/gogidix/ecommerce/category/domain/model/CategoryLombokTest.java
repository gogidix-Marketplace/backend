package com.gogidix.ecommerce.category.domain.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryLombokTest {
    private Category createFull() {
        Category c = new Category("t1");
        c.setId("id1"); c.setCategoryCode("CAT1"); c.setName("Test");
        c.setDescription("desc"); c.setParentId("p1"); c.setLevel(1); c.setPath("P1/CAT1");
        c.setAncestorIds(List.of("p1")); c.setDisplayOrder(1); c.setIconUrl("icon.png");
        c.setBannerUrl("banner.png"); c.setImageUrl("image.png");
        c.setImageUrls(List.of("img1")); c.setIsLeaf(true); c.setIsActive(true);
        c.setIsVisible(true); c.setChildren(new ArrayList<>());
        return c;
    }

    @Test void equals_same() { assertThat(createFull()).isEqualTo(createFull()); }
    @Test void equals_different() {
        Category c1 = createFull(); Category c2 = createFull(); c2.setName("Other");
        assertThat(c1).isNotEqualTo(c2);
    }
    @Test void equals_null() { assertThat(createFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() { Category c = createFull(); assertThat(c.hashCode()).isEqualTo(c.hashCode()); }
    @Test void toString_notNull() { assertThat(createFull().toString()).contains("Category"); }
    @Test void canEqual() { assertThat(createFull().canEqual(new Category())).isTrue(); }
    @Test void canEqual_false() { assertThat(createFull().canEqual("str")).isFalse(); }
    @Test void equals_self() { Category c = createFull(); assertThat(c).isEqualTo(c); }
}