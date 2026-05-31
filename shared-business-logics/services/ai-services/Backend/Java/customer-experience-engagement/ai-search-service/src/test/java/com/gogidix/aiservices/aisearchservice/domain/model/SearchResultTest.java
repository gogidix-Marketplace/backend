package com.gogidix.aiservices.aisearchservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SearchResult Domain Model Tests")
class SearchResultTest {

    @Nested
    @DisplayName("SearchResult Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create search result")
        void shouldCreateSearchResult() {
            SearchResult result = SearchResult.builder()
                    .itemId("item-1")
                    .title("Test Product")
                    .description("A test product description")
                    .relevanceScore(0.95)
                    .category("electronics")
                    .url("https://example.com/item-1")
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getItemId()).isEqualTo("item-1");
            assertThat(result.getRelevanceScore()).isEqualTo(0.95);
        }
    }

    @Nested
    @DisplayName("SearchQuery Tests")
    class QueryTests {

        @Test
        @DisplayName("Should create search query")
        void shouldCreateSearchQuery() {
            SearchQuery query = SearchQuery.builder()
                    .query("laptop")
                    .page(0)
                    .size(10)
                    .categories(List.of("electronics", "computers"))
                    .build();

            assertThat(query).isNotNull();
            assertThat(query.getQuery()).isEqualTo("laptop");
            assertThat(query.getPage()).isEqualTo(0);
            assertThat(query.getSize()).isEqualTo(10);
        }
    }
}
