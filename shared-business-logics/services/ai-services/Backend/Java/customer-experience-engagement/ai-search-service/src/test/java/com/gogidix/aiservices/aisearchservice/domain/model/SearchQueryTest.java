package com.gogidix.aiservices.aisearchservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SearchQuery Domain Model Tests")
class SearchQueryTest {

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            List<String> filters = List.of("category:electronics", "brand:Apple");
            List<String> categories = List.of("laptops", "tablets");
            Map<String, String> facetFilters = Map.of("price", "0-1000", "brand", "Samsung");

            SearchQuery query = SearchQuery.builder()
                    .query("laptop")
                    .filters(filters)
                    .sort("price_asc")
                    .page(1)
                    .size(20)
                    .categories(categories)
                    .facetFilters(facetFilters)
                    .build();

            assertThat(query).isNotNull();
            assertThat(query.getQuery()).isEqualTo("laptop");
            assertThat(query.getFilters()).isEqualTo(filters);
            assertThat(query.getSort()).isEqualTo("price_asc");
            assertThat(query.getPage()).isEqualTo(1);
            assertThat(query.getSize()).isEqualTo(20);
            assertThat(query.getCategories()).isEqualTo(categories);
            assertThat(query.getFacetFilters()).isEqualTo(facetFilters);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            SearchQuery query = SearchQuery.builder()
                    .query("phone")
                    .build();

            assertThat(query).isNotNull();
            assertThat(query.getQuery()).isEqualTo("phone");
            assertThat(query.getFilters()).isNull();
            assertThat(query.getSort()).isNull();
            assertThat(query.getPage()).isEqualTo(0);
            assertThat(query.getSize()).isEqualTo(0);
            assertThat(query.getCategories()).isNull();
            assertThat(query.getFacetFilters()).isNull();
        }

        @Test
        @DisplayName("Should build with empty collections")
        void shouldBuildWithEmptyCollections() {
            SearchQuery query = SearchQuery.builder()
                    .query("tablet")
                    .filters(List.of())
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            assertThat(query).isNotNull();
            assertThat(query.getFilters()).isEmpty();
            assertThat(query.getCategories()).isEmpty();
            assertThat(query.getFacetFilters()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodsTests {

        @Test
        @DisplayName("All getters should return correct values")
        void allGettersShouldReturnCorrectValues() {
            List<String> filters = List.of("filter1");
            List<String> categories = List.of("cat1", "cat2");
            Map<String, String> facetFilters = Map.of("key", "value");

            SearchQuery query = SearchQuery.builder()
                    .query("test query")
                    .filters(filters)
                    .sort("relevance")
                    .page(2)
                    .size(15)
                    .categories(categories)
                    .facetFilters(facetFilters)
                    .build();

            assertThat(query.getQuery()).isEqualTo("test query");
            assertThat(query.getFilters()).isEqualTo(filters);
            assertThat(query.getSort()).isEqualTo("relevance");
            assertThat(query.getPage()).isEqualTo(2);
            assertThat(query.getSize()).isEqualTo(15);
            assertThat(query.getCategories()).isEqualTo(categories);
            assertThat(query.getFacetFilters()).isEqualTo(facetFilters);
        }

        @Test
        @DisplayName("Getters should return null for unset fields")
        void gettersShouldReturnNullForUnsetFields() {
            SearchQuery query = SearchQuery.builder()
                    .query("minimal")
                    .build();

            assertThat(query.getQuery()).isEqualTo("minimal");
            assertThat(query.getFilters()).isNull();
            assertThat(query.getSort()).isNull();
            assertThat(query.getCategories()).isNull();
            assertThat(query.getFacetFilters()).isNull();
        }
    }

    @Nested
    @DisplayName("Pagination Tests")
    class PaginationTests {

        @Test
        @DisplayName("Should handle zero-based page numbers")
        void shouldHandleZeroBasedPageNumbers() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .page(0)
                    .size(10)
                    .build();

            assertThat(query.getPage()).isEqualTo(0);
            assertThat(query.getSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should handle large page numbers")
        void shouldHandleLargePageNumbers() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .page(999)
                    .size(50)
                    .build();

            assertThat(query.getPage()).isEqualTo(999);
            assertThat(query.getSize()).isEqualTo(50);
        }

        @Test
 @DisplayName("Should handle zero size")
        void shouldHandleZeroSize() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .size(0)
                    .build();

            assertThat(query.getSize()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Filter Tests")
    class FilterTests {

        @Test
        @DisplayName("Should build with multiple filters")
        void shouldBuildWithMultipleFilters() {
            List<String> filters = List.of("category:electronics", "price:0-100", "brand:Samsung");

            SearchQuery query = SearchQuery.builder()
                    .query("laptop")
                    .filters(filters)
                    .build();

            assertThat(query.getFilters()).hasSize(3);
            assertThat(query.getFilters()).contains("category:electronics", "price:0-100", "brand:Samsung");
        }

        @Test
        @DisplayName("Should handle empty filter list")
        void shouldHandleEmptyFilterList() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .filters(List.of())
                    .build();

            assertThat(query.getFilters()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Facet Filter Tests")
    class FacetFilterTests {

        @Test
        @DisplayName("Should build with facet filters")
        void shouldBuildWithFacetFilters() {
            Map<String, String> facets = Map.of(
                    "price", "0-1000",
                    "brand", "Apple,Samsung",
                    "rating", "4+"
            );

            SearchQuery query = SearchQuery.builder()
                    .query("phone")
                    .facetFilters(facets)
                    .build();

            assertThat(query.getFacetFilters()).hasSize(3);
            assertThat(query.getFacetFilters()).containsEntry("price", "0-1000");
            assertThat(query.getFacetFilters()).containsEntry("brand", "Apple,Samsung");
        }

        @Test
        @DisplayName("Should handle empty facet filters")
        void shouldHandleEmptyFacetFilters() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .facetFilters(Map.of())
                    .build();

            assertThat(query.getFacetFilters()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Sort Tests")
    class SortTests {

        @Test
        @DisplayName("Should build with sort parameter")
        void shouldBuildWithSortParameter() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .sort("price_desc")
                    .build();

            assertThat(query.getSort()).isEqualTo("price_desc");
        }

        @Test
        @DisplayName("Should build with relevance sort")
        void shouldBuildWithRelevanceSort() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .sort("relevance")
                    .build();

            assertThat(query.getSort()).isEqualTo("relevance");
        }
    }

    @Nested
    @DisplayName("Category Tests")
    class CategoryTests {

        @Test
        @DisplayName("Should build with multiple categories")
        void shouldBuildWithMultipleCategories() {
            List<String> categories = List.of("electronics", "computers", "laptops");

            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .categories(categories)
                    .build();

            assertThat(query.getCategories()).hasSize(3);
            assertThat(query.getCategories()).contains("electronics", "computers", "laptops");
        }

        @Test
        @DisplayName("Should handle empty category list")
        void shouldHandleEmptyCategoryList() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .categories(List.of())
                    .build();

            assertThat(query.getCategories()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty query string")
        void shouldHandleEmptyQueryString() {
            SearchQuery query = SearchQuery.builder()
                    .query("")
                    .build();

            assertThat(query.getQuery()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle long query string")
        void shouldHandleLongQueryString() {
            String longQuery = "a".repeat(1000);

            SearchQuery query = SearchQuery.builder()
                    .query(longQuery)
                    .build();

            assertThat(query.getQuery()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle negative page numbers")
        void shouldHandleNegativePageNumbers() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .page(-1)
                    .build();

            assertThat(query.getPage()).isEqualTo(-1);
        }
    }

    @Nested
    @DisplayName("Builder Null Safety Tests")
    class BuilderNullSafetyTests {

        @Test
        @DisplayName("Should allow null optional fields")
        void shouldAllowNullOptionalFields() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .filters(null)
                    .sort(null)
                    .categories(null)
                    .facetFilters(null)
                    .build();

            assertThat(query).isNotNull();
            assertThat(query.getQuery()).isEqualTo("test");
        }
    }
}
