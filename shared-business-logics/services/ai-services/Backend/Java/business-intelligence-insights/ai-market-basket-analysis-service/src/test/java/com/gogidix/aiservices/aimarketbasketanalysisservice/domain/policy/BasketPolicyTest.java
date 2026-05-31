package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.policy;

import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.BusinessException;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.ConflictException;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Basket Policy Tests")
class BasketPolicyTest {

    @Nested
    @DisplayName("MinimumCustomerCountPolicy Tests")
    class MinimumCustomerCountPolicyTests {

        @Test
        @DisplayName("Should use default minimum of 10")
        void shouldUseDefaultMinimum() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy();
            assertThat(policy.getMinimumCustomerCount()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should accept custom minimum")
        void shouldAcceptCustomMinimum() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(50);
            assertThat(policy.getMinimumCustomerCount()).isEqualTo(50);
        }

        @Test
        @DisplayName("Should reject negative minimum")
        void shouldRejectNegativeMinimum() {
            assertThatThrownBy(() -> new MinimumCustomerCountPolicy(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should validate for activation successfully")
        void shouldValidateForActivation() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            policy.validateForActivation(15, "Test");
        }

        @Test
        @DisplayName("Should throw for activation with too few customers")
        void shouldThrowForActivationTooFew() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertThatThrownBy(() -> policy.validateForActivation(5, "Test"))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("Should validate for analysis successfully")
        void shouldValidateForAnalysis() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            policy.validateForAnalysis(10, "Test");
        }

        @Test
        @DisplayName("Should throw for analysis with too few customers")
        void shouldThrowForAnalysisTooFew() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertThatThrownBy(() -> policy.validateForAnalysis(0, "Test"))
                    .isInstanceOf(ValidationException.class);
        }

        @Test
        @DisplayName("Should report canActivate correctly")
        void shouldReportCanActivate() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertThat(policy.canActivate(10)).isTrue();
            assertThat(policy.canActivate(9)).isFalse();
            assertThat(policy.canActivate(15)).isTrue();
        }

        @Test
        @DisplayName("Should calculate additional customers needed")
        void shouldCalculateAdditionalNeeded() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertThat(policy.getAdditionalCustomersNeeded(5)).isEqualTo(5);
            assertThat(policy.getAdditionalCustomersNeeded(10)).isEqualTo(0);
            assertThat(policy.getAdditionalCustomersNeeded(15)).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("MaxBasketsPerTenantPolicy Tests")
    class MaxBasketsPerTenantPolicyTests {

        @Test
        @DisplayName("Should use default max of 100")
        void shouldUseDefaultMax() {
            MaxBasketsPerTenantPolicy policy = new MaxBasketsPerTenantPolicy();
            assertThat(policy.getMaxBaskets()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should accept custom max")
        void shouldAcceptCustomMax() {
            MaxBasketsPerTenantPolicy policy = new MaxBasketsPerTenantPolicy(50);
            assertThat(policy.getMaxBaskets()).isEqualTo(50);
        }

        @Test
        @DisplayName("Should reject zero max")
        void shouldRejectZeroMax() {
            assertThatThrownBy(() -> new MaxBasketsPerTenantPolicy(0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should reject negative max")
        void shouldRejectNegativeMax() {
            assertThatThrownBy(() -> new MaxBasketsPerTenantPolicy(-5))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should validate when under limit")
        void shouldValidateUnderLimit() {
            MaxBasketsPerTenantPolicy policy = new MaxBasketsPerTenantPolicy(10);
            policy.validate(5, "tenant-1");
        }

        @Test
        @DisplayName("Should throw when at limit")
        void shouldThrowAtLimit() {
            MaxBasketsPerTenantPolicy policy = new MaxBasketsPerTenantPolicy(10);
            assertThatThrownBy(() -> policy.validate(10, "tenant-1"))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("Should report canCreateBasket correctly")
        void shouldReportCanCreate() {
            MaxBasketsPerTenantPolicy policy = new MaxBasketsPerTenantPolicy(10);
            assertThat(policy.canCreateBasket(9)).isTrue();
            assertThat(policy.canCreateBasket(10)).isFalse();
        }

        @Test
        @DisplayName("Should calculate remaining baskets")
        void shouldCalculateRemaining() {
            MaxBasketsPerTenantPolicy policy = new MaxBasketsPerTenantPolicy(10);
            assertThat(policy.getRemainingBaskets(5)).isEqualTo(5);
            assertThat(policy.getRemainingBaskets(10)).isEqualTo(0);
            assertThat(policy.getRemainingBaskets(15)).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("BasketNameUniquePolicy Tests")
    class BasketNameUniquePolicyTests {

        private final BasketNameUniquePolicy policy = new BasketNameUniquePolicy();

        @Test
        @DisplayName("Should validate unique name")
        void shouldValidateUniqueName() {
            List<String> existing = Arrays.asList("Premium", "Standard");
            policy.validate(existing, "Enterprise", "tenant-1");
        }

        @Test
        @DisplayName("Should throw for duplicate name case insensitive")
        void shouldThrowForDuplicate() {
            List<String> existing = Arrays.asList("Premium", "Standard");
            assertThatThrownBy(() -> policy.validate(existing, "premium", "tenant-1"))
                    .isInstanceOf(ConflictException.class);
        }

        @Test
        @DisplayName("Should accept null existing names")
        void shouldAcceptNullExisting() {
            policy.validate(null, "AnyName", "tenant-1");
        }

        @Test
        @DisplayName("Should report isUnique correctly")
        void shouldReportIsUnique() {
            List<String> existing = Arrays.asList("Premium", "Standard");
            assertThat(policy.isUnique(existing, "Enterprise")).isTrue();
            assertThat(policy.isUnique(existing, "Premium")).isFalse();
            assertThat(policy.isUnique(null, "Premium")).isFalse();
            assertThat(policy.isUnique(existing, null)).isFalse();
        }

        @Test
        @DisplayName("Should generate unique name")
        void shouldGenerateUniqueName() {
            List<String> existing = Arrays.asList("Premium", "Premium_1");
            String unique = policy.generateUniqueName("Premium", existing);
            assertThat(unique).isEqualTo("Premium_2");
        }

        @Test
        @DisplayName("Should return base name if unique")
        void shouldReturnBaseIfUnique() {
            List<String> existing = Arrays.asList("Standard");
            String unique = policy.generateUniqueName("Premium", existing);
            assertThat(unique).isEqualTo("Premium");
        }
    }
}
