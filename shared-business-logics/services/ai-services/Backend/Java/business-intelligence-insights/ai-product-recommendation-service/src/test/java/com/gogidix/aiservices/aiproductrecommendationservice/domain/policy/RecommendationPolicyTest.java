package com.gogidix.aiservices.aiproductrecommendationservice.domain.policy;

import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.BusinessException;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.ConflictException;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Recommendation Policy Tests")
class RecommendationPolicyTest {

    @Nested
    @DisplayName("MinimumProductCountPolicy Tests")
    class MinimumProductCountPolicyTests {
        @Test
        void shouldUseDefaultMinimum() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy();
            assertThat(policy.getMinimumProductCount()).isEqualTo(10);
        }
        @Test
        void shouldAcceptCustomMinimum() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy(50);
            assertThat(policy.getMinimumProductCount()).isEqualTo(50);
        }
        @Test
        void shouldRejectNegative() {
            assertThatThrownBy(() -> new MinimumProductCountPolicy(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
        @Test
        void shouldValidateForActivation() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy(10);
            policy.validateForActivation(15, "Test");
        }
        @Test
        void shouldThrowForTooFew() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy(10);
            assertThatThrownBy(() -> policy.validateForActivation(5, "Test"))
                    .isInstanceOf(BusinessException.class);
        }
        @Test
        void shouldValidateForAnalysis() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy(10);
            policy.validateForAnalysis(10, "Test");
        }
        @Test
        void shouldThrowForAnalysisTooFew() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy(10);
            assertThatThrownBy(() -> policy.validateForAnalysis(0, "Test"))
                    .isInstanceOf(ValidationException.class);
        }
        @Test
        void shouldReportCanActivate() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy(10);
            assertThat(policy.canActivate(10)).isTrue();
            assertThat(policy.canActivate(9)).isFalse();
        }
        @Test
        void shouldCalculateAdditionalNeeded() {
            MinimumProductCountPolicy policy = new MinimumProductCountPolicy(10);
            assertThat(policy.getAdditionalProductsNeeded(5)).isEqualTo(5);
            assertThat(policy.getAdditionalProductsNeeded(10)).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("MaxRecommendationsPerTenantPolicy Tests")
    class MaxRecommendationsPerTenantPolicyTests {
        @Test
        void shouldUseDefaultMax() {
            MaxRecommendationsPerTenantPolicy policy = new MaxRecommendationsPerTenantPolicy();
            assertThat(policy.getMaxRecommendations()).isEqualTo(100);
        }
        @Test
        void shouldRejectZero() {
            assertThatThrownBy(() -> new MaxRecommendationsPerTenantPolicy(0))
                    .isInstanceOf(IllegalArgumentException.class);
        }
        @Test
        void shouldValidateUnderLimit() {
            MaxRecommendationsPerTenantPolicy policy = new MaxRecommendationsPerTenantPolicy(10);
            policy.validate(5, "tenant-1");
        }
        @Test
        void shouldThrowAtLimit() {
            MaxRecommendationsPerTenantPolicy policy = new MaxRecommendationsPerTenantPolicy(10);
            assertThatThrownBy(() -> policy.validate(10, "tenant-1"))
                    .isInstanceOf(BusinessException.class);
        }
        @Test
        void shouldReportCanCreate() {
            MaxRecommendationsPerTenantPolicy policy = new MaxRecommendationsPerTenantPolicy(10);
            assertThat(policy.canCreateRecommendation(9)).isTrue();
            assertThat(policy.canCreateRecommendation(10)).isFalse();
        }
        @Test
        void shouldCalculateRemaining() {
            MaxRecommendationsPerTenantPolicy policy = new MaxRecommendationsPerTenantPolicy(10);
            assertThat(policy.getRemainingRecommendations(5)).isEqualTo(5);
            assertThat(policy.getRemainingRecommendations(15)).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("RecommendationNameUniquePolicy Tests")
    class RecommendationNameUniquePolicyTests {
        private final RecommendationNameUniquePolicy policy = new RecommendationNameUniquePolicy();

        @Test
        void shouldValidateUniqueName() {
            List<String> existing = Arrays.asList("Premium", "Standard");
            policy.validate(existing, "Enterprise", "tenant-1");
        }
        @Test
        void shouldThrowForDuplicateCaseInsensitive() {
            List<String> existing = Arrays.asList("Premium", "Standard");
            assertThatThrownBy(() -> policy.validate(existing, "premium", "tenant-1"))
                    .isInstanceOf(ConflictException.class);
        }
        @Test
        void shouldAcceptNullExisting() {
            policy.validate(null, "AnyName", "tenant-1");
        }
        @Test
        void shouldReportIsUnique() {
            List<String> existing = Arrays.asList("Premium", "Standard");
            assertThat(policy.isUnique(existing, "Enterprise")).isTrue();
            assertThat(policy.isUnique(existing, "Premium")).isFalse();
            assertThat(policy.isUnique(null, "Premium")).isFalse();
        }
        @Test
        void shouldGenerateUniqueName() {
            List<String> existing = Arrays.asList("Premium", "Premium_1");
            String unique = policy.generateUniqueName("Premium", existing);
            assertThat(unique).isEqualTo("Premium_2");
        }
    }
}
