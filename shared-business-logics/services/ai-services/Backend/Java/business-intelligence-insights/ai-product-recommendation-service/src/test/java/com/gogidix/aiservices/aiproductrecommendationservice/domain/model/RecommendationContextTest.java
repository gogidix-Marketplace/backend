package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecommendationContext Tests")
class RecommendationContextTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        void shouldCreateWithDefaults() {
            RecommendationContext ctx = new RecommendationContext();
            assertThat(ctx.getMaxResults()).isEqualTo(10);
            assertThat(ctx.getExcludedCategories()).isEmpty();
            assertThat(ctx.isIncludeOutOfStock()).isTrue();
        }

        @Test
        void shouldCreateWithArgs() {
            RecommendationContext ctx = new RecommendationContext("c1", "t1", RecommendationType.COLLABORATIVE_FILTERING);
            assertThat(ctx.getCustomerId()).isEqualTo("c1");
            assertThat(ctx.getTenantId()).isEqualTo("t1");
            assertThat(ctx.getType()).isEqualTo(RecommendationType.COLLABORATIVE_FILTERING);
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {
        @Test
        void shouldBuildWithAllFields() {
            RecommendationContext ctx = RecommendationContext.builder()
                    .customerId("c1")
                    .tenantId("t1")
                    .type(RecommendationType.CONTENT_BASED)
                    .maxResults(20)
                    .excludedCategories(Arrays.asList("books"))
                    .includeOutOfStock(false)
                    .build();
            assertThat(ctx.getCustomerId()).isEqualTo("c1");
            assertThat(ctx.getMaxResults()).isEqualTo(20);
            assertThat(ctx.getExcludedCategories()).containsExactly("books");
            assertThat(ctx.isIncludeOutOfStock()).isFalse();
        }

        @Test
        void shouldDefaultMaxResults() {
            RecommendationContext ctx = RecommendationContext.builder()
                    .customerId("c").tenantId("t").build();
            assertThat(ctx.getMaxResults()).isEqualTo(10);
        }

        @Test
        void shouldDefaultExcludedCategories() {
            RecommendationContext ctx = RecommendationContext.builder().build();
            assertThat(ctx.getExcludedCategories()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {
        @Test
        void shouldSetFields() {
            RecommendationContext ctx = new RecommendationContext();
            ctx.setCustomerId("c1");
            ctx.setTenantId("t1");
            ctx.setType(RecommendationType.HYBRID);
            ctx.setMaxResults(50);
            ctx.setIncludeOutOfStock(false);
            assertThat(ctx.getCustomerId()).isEqualTo("c1");
            assertThat(ctx.getTenantId()).isEqualTo("t1");
            assertThat(ctx.getType()).isEqualTo(RecommendationType.HYBRID);
            assertThat(ctx.getMaxResults()).isEqualTo(50);
            assertThat(ctx.isIncludeOutOfStock()).isFalse();
        }
    }

    @Test
    void shouldHaveToString() {
        RecommendationContext ctx = RecommendationContext.builder()
                .customerId("c1").tenantId("t1").build();
        assertThat(ctx.toString()).contains("RecommendationContext");
    }
}
