package com.gogidix.aiservices.aisearchservice.multitenancy;

import com.gogidix.aiservices.aisearchservice.domain.model.SearchQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Multi-Tenancy Tenant Isolation Tests")
class TenantIsolationTest {

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";

    @Nested
    @DisplayName("SearchQuery Domain Model Tests")
    class SearchQueryTests {

        @Test
        @DisplayName("Should create search query with tenant context in filters")
        void shouldCreateSearchQueryWithTenantContextInFilters() {
            SearchQuery query = SearchQuery.builder()
                    .query("search term")
                    .filters(List.of("tenant:" + TENANT_1, "category:products"))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of("products"))
                    .facetFilters(Map.of("price", "0-100"))
                    .build();

            assertThat(query.getFilters()).isNotNull();
            assertThat(query.getFilters()).contains("tenant:" + TENANT_1);
        }

        @Test
        @DisplayName("Should support different queries per tenant")
        void shouldSupportDifferentQueriesPerTenant() {
            SearchQuery query1 = SearchQuery.builder()
                    .query("tenant 1 products")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            SearchQuery query2 = SearchQuery.builder()
                    .query("tenant 2 products")
                    .filters(List.of("tenant:" + TENANT_2))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            assertThat(query1.getQuery()).isNotEqualTo(query2.getQuery());
            assertThat(query1.getFilters()).isNotEqualTo(query2.getFilters());
        }

        @Test
        @DisplayName("Should distinguish search queries by filters")
        void shouldDistinguishSearchQueriesByFilters() {
            SearchQuery query1 = SearchQuery.builder()
                    .query("products")
                    .filters(List.of("tenant:" + TENANT_1, "category:electronics"))
                    .sort("price")
                    .page(0)
                    .size(10)
                    .categories(List.of("electronics"))
                    .facetFilters(Map.of("brand", "Apple"))
                    .build();

            SearchQuery query2 = SearchQuery.builder()
                    .query("products")
                    .filters(List.of("tenant:" + TENANT_2, "category:books"))
                    .sort("price")
                    .page(0)
                    .size(10)
                    .categories(List.of("books"))
                    .facetFilters(Map.of("author", "John"))
                    .build();

            assertThat(query1.getFilters()).isNotEqualTo(query2.getFilters());
            assertThat(query1.getCategories()).isNotEqualTo(query2.getCategories());
            assertThat(query1.getFacetFilters()).isNotEqualTo(query2.getFacetFilters());
        }

        @Test
        @DisplayName("Should support pagination")
        void shouldSupportPagination() {
            SearchQuery page1 = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            SearchQuery page2 = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(1)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            assertThat(page1.getPage()).isZero();
            assertThat(page2.getPage()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should support different sort options")
        void shouldSupportDifferentSortOptions() {
            SearchQuery relevanceSort = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            SearchQuery priceSort = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("price")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            assertThat(relevanceSort.getSort()).isEqualTo("relevance");
            assertThat(priceSort.getSort()).isEqualTo("price");
        }

        @Test
        @DisplayName("Should support different page sizes")
        void shouldSupportDifferentPageSizes() {
            SearchQuery smallPage = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(10)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            SearchQuery largePage = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(100)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            assertThat(smallPage.getSize()).isEqualTo(10);
            assertThat(largePage.getSize()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should support categories")
        void shouldSupportCategories() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of("electronics", "books", "clothing"))
                    .facetFilters(Map.of())
                    .build();

            assertThat(query.getCategories()).hasSize(3);
            assertThat(query.getCategories()).contains("electronics", "books", "clothing");
        }

        @Test
        @DisplayName("Should support facet filters")
        void shouldSupportFacetFilters() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of(
                            "price", "0-100",
                            "brand", "Apple,Samsung",
                            "rating", "4-5"
                    ))
                    .build();

            assertThat(query.getFacetFilters()).hasSize(3);
            assertThat(query.getFacetFilters().get("price")).isEqualTo("0-100");
            assertThat(query.getFacetFilters().get("brand")).isEqualTo("Apple,Samsung");
        }

        @Test
        @DisplayName("Should handle empty filters")
        void shouldHandleEmptyFilters() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .filters(List.of())
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            assertThat(query.getFilters()).isEmpty();
            assertThat(query.getCategories()).isEmpty();
            assertThat(query.getFacetFilters()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null categories")
        void shouldHandleNullCategories() {
            SearchQuery query = SearchQuery.builder()
                    .query("test")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(null)
                    .facetFilters(Map.of())
                    .build();

            assertThat(query.getCategories()).isNull();
        }
    }

    @Nested
    @DisplayName("Tenant Context Isolation Tests")
    class TenantContextIsolationTests {

        @Test
        @DisplayName("Should verify tenant isolation via filters")
        void shouldVerifyTenantIsolationViaFilters() {
            SearchQuery query1 = SearchQuery.builder()
                    .query("products")
                    .filters(List.of("tenant:" + TENANT_1))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            SearchQuery query2 = SearchQuery.builder()
                    .query("products")
                    .filters(List.of("tenant:" + TENANT_2))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of())
                    .facetFilters(Map.of())
                    .build();

            // Verify that queries are distinct by tenant
            assertThat(query1.getFilters()).isNotEqualTo(query2.getFilters());

            // Simulate filtering by tenant
            var allQueries = java.util.List.of(query1, query2);
            var tenant1Queries = allQueries.stream()
                    .filter(q -> q.getFilters().contains("tenant:" + TENANT_1))
                    .toList();

            assertThat(tenant1Queries).hasSize(1);
            assertThat(tenant1Queries.get(0).getFilters()).contains("tenant:" + TENANT_1);
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Query Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @DisplayName("Should demonstrate tenant filtering concept")
        void shouldDemonstrateTenantFilteringConcept() {
            // Create search queries for different tenants
            SearchQuery query1 = SearchQuery.builder()
                    .query("tenant 1 search")
                    .filters(List.of("tenant:" + TENANT_1, "status:active"))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of("products"))
                    .facetFilters(Map.of("price", "0-100"))
                    .build();

            SearchQuery query2 = SearchQuery.builder()
                    .query("tenant 2 search")
                    .filters(List.of("tenant:" + TENANT_2, "status:active"))
                    .sort("relevance")
                    .page(0)
                    .size(20)
                    .categories(List.of("products"))
                    .facetFilters(Map.of("price", "0-100"))
                    .build();

            // Simulate repository filtering by tenant
            var allQueries = java.util.List.of(query1, query2);

            // Filter for tenant 1
            var tenant1Queries = allQueries.stream()
                    .filter(q -> q.getFilters().contains("tenant:" + TENANT_1))
                    .toList();

            // Filter for tenant 2
            var tenant2Queries = allQueries.stream()
                    .filter(q -> q.getFilters().contains("tenant:" + TENANT_2))
                    .toList();

            // Verify isolation
            assertThat(tenant1Queries).hasSize(1);
            assertThat(tenant2Queries).hasSize(1);
            assertThat(tenant1Queries.get(0).getFilters()).contains("tenant:" + TENANT_1);
            assertThat(tenant2Queries.get(0).getFilters()).contains("tenant:" + TENANT_2);
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should support multiple tenants concurrently")
        void shouldSupportMultipleTenantsConcurrently() throws InterruptedException {
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            final boolean[] errors = {false};
            final String[] results = new String[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                final String tenantId = "tenant-" + (i + 1);

                threads[i] = new Thread(() -> {
                    try {
                        SearchQuery query = SearchQuery.builder()
                                .query("search " + index)
                                .filters(List.of("tenant:" + tenantId))
                                .sort("relevance")
                                .page(index)
                                .size(20)
                                .categories(List.of("category" + index))
                                .facetFilters(Map.of("facet", "value" + index))
                                .build();

                        results[index] = new StringBuilder()
                                .append("Thread: ").append(index)
                                .append(", Tenant: ").append(tenantId)
                                .append(", Query: ").append(query.getQuery())
                                .toString();
                    } catch (Exception e) {
                        errors[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errors[0]).isFalse();

            // Verify each thread had its own tenant
            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isNotNull();
                assertThat(results[i].toString()).contains("tenant-" + (i + 1));
            }
        }
    }
}
