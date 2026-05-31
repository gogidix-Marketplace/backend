package com.gogidix.ecommerce.category.application.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryDtoTest {

    @Test void categoryDto_allFields() {
        Instant now = Instant.now();
        CategoryDto dto = new CategoryDto("id1", "t1", "Electronics", "ELEC",
            "p1", List.of("c1","c2"), 2, true, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.name()).isEqualTo("Electronics");
        assertThat(dto.code()).isEqualTo("ELEC");
        assertThat(dto.level()).isEqualTo(2);
        assertThat(dto.active()).isTrue();
    }

    @Test void categoryDto_nulls() {
        CategoryDto dto = new CategoryDto(null, null, null, null, null, null, 0, false, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }

    @Test void categoryResponse_direct() {
        CategoryResponse r = new CategoryResponse("id1", "ELEC", "Electronics", "Desc",
            null, 1, "/e", null, 5, null, null, null, null, null, null, true, true, true,
            null, null, null);
        assertThat(r.categoryCode()).isEqualTo("ELEC");
        assertThat(r.isActive()).isTrue();
    }

    @Test void categorySeoDto() {
        CategorySeoDto seo = new CategorySeoDto("Title", "Desc", "kw", "slug");
        assertThat(seo.metaTitle()).isEqualTo("Title");
        assertThat(seo.slug()).isEqualTo("slug");
    }

    @Test void categoryAttributeDto() {
        CategoryAttributeDto attr = new CategoryAttributeDto("Color", "color", "STRING", true, false, List.of("red"));
        assertThat(attr.name()).isEqualTo("Color");
        assertThat(attr.isRequired()).isTrue();
    }

    @Test void createCategoryRequest() {
        CreateCategoryRequest req = new CreateCategoryRequest("CODE", "Name", "Desc", null, 1, null, null, null, null, true, true, null, null);
        assertThat(req.categoryCode()).isEqualTo("CODE");
        assertThat(req.name()).isEqualTo("Name");
    }

    @Test void updateCategoryRequest() {
        UpdateCategoryRequest req = new UpdateCategoryRequest("Updated", null, 2, null, null, null, null, true, true, null, null);
        assertThat(req.name()).isEqualTo("Updated");
    }

    @Test void dto_equality() {
        CategoryDto a = new CategoryDto("id", "t", "n", "c", null, null, 0, true, null, null);
        CategoryDto b = new CategoryDto("id", "t", "n", "c", null, null, 0, true, null, null);
        assertThat(a).isEqualTo(b);
    }
}