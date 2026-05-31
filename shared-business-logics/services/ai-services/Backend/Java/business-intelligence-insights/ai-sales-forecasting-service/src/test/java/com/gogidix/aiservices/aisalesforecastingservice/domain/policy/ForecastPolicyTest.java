package com.gogidix.aiservices.aisalesforecastingservice.domain.policy;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ForecastPolicyTest {

    @Nested
    class MaxForecastsPerTenantPolicyTests {
        @Test
        void shouldCreateWithDefault() {
            var p = new MaxForecastsPerTenantPolicy();
            assertThat(p.getMaxForecasts()).isEqualTo(100);
        }

        @Test
        void shouldCreateWithCustom() {
            var p = new MaxForecastsPerTenantPolicy(50);
            assertThat(p.getMaxForecasts()).isEqualTo(50);
        }

        @Test
        void shouldRejectZeroOrNegative() {
            assertThatThrownBy(() -> new MaxForecastsPerTenantPolicy(0))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new MaxForecastsPerTenantPolicy(-1))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldValidateUnderLimit() {
            var p = new MaxForecastsPerTenantPolicy(10);
            assertThatCode(() -> p.validate(5, "t1")).doesNotThrowAnyException();
        }

        @Test
        void shouldThrowAtLimit() {
            var p = new MaxForecastsPerTenantPolicy(10);
            assertThatThrownBy(() -> p.validate(10, "t1"))
                .isInstanceOf(Exception.class);
        }

        @Test
        void canCreateForecastTest() {
            var p = new MaxForecastsPerTenantPolicy(10);
            assertThat(p.canCreateForecast(9)).isTrue();
            assertThat(p.canCreateForecast(10)).isFalse();
        }

        @Test
        void getRemainingForecastsTest() {
            var p = new MaxForecastsPerTenantPolicy(10);
            assertThat(p.getRemainingForecasts(7)).isEqualTo(3);
            assertThat(p.getRemainingForecasts(15)).isEqualTo(0);
        }
    }

    @Nested
    class ForecastNameUniquePolicyTests {
        private final ForecastNameUniquePolicy policy = new ForecastNameUniquePolicy();

        @Test
        void shouldAllowUniqueName() {
            assertThatCode(() -> policy.validate(java.util.List.of("a", "b"), "c", "t1"))
                .doesNotThrowAnyException();
        }

        @Test
        void shouldRejectDuplicateName() {
            assertThatThrownBy(() -> policy.validate(java.util.List.of("alpha", "beta"), "ALPHA", "t1"))
                .isInstanceOf(Exception.class);
        }

        @Test
        void shouldAllowNullList() {
            assertThatCode(() -> policy.validate(null, "name", "t1"))
                .doesNotThrowAnyException();
        }

        @Test
        void isUniqueTest() {
            assertThat(policy.isUnique(java.util.List.of("a", "b"), "c")).isTrue();
            assertThat(policy.isUnique(java.util.List.of("a", "b"), "A")).isFalse();
            assertThat(policy.isUnique(null, "a")).isFalse();
            assertThat(policy.isUnique(java.util.List.of("a"), null)).isFalse();
        }

        @Test
        void generateUniqueNameTest() {
            assertThat(policy.generateUniqueName("base", java.util.List.of("other"))).isEqualTo("base");
            String name = policy.generateUniqueName("base", java.util.List.of("base", "base_1"));
            assertThat(name).isEqualTo("base_2");
        }
    }

    @Nested
    class MinimumForecastModelCountPolicyTests {
        @Test
        void shouldCreateWithDefault() {
            var p = new MinimumForecastModelCountPolicy();
            assertThat(p.getMinimumForecastModelCount()).isEqualTo(10);
        }

        @Test
        void shouldCreateWithCustom() {
            var p = new MinimumForecastModelCountPolicy(20);
            assertThat(p.getMinimumForecastModelCount()).isEqualTo(20);
        }

        @Test
        void shouldRejectNegative() {
            assertThatThrownBy(() -> new MinimumForecastModelCountPolicy(-1))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldValidateActivationEnough() {
            var p = new MinimumForecastModelCountPolicy(10);
            assertThatCode(() -> p.validateForActivation(10, "f1")).doesNotThrowAnyException();
        }

        @Test
        void shouldRejectActivationTooFew() {
            var p = new MinimumForecastModelCountPolicy(10);
            assertThatThrownBy(() -> p.validateForActivation(5, "f1"))
                .isInstanceOf(Exception.class);
        }

        @Test
        void shouldValidateAnalysisEnough() {
            var p = new MinimumForecastModelCountPolicy(10);
            assertThatCode(() -> p.validateForAnalysis(5, "f1")).doesNotThrowAnyException();
        }

        @Test
        void shouldRejectAnalysisTooFew() {
            var p = new MinimumForecastModelCountPolicy(10);
            assertThatThrownBy(() -> p.validateForAnalysis(0, "f1"))
                .isInstanceOf(Exception.class);
        }

        @Test
        void canActivateTest() {
            var p = new MinimumForecastModelCountPolicy(10);
            assertThat(p.canActivate(10)).isTrue();
            assertThat(p.canActivate(9)).isFalse();
        }

        @Test
        void getAdditionalForecastModelsNeededTest() {
            var p = new MinimumForecastModelCountPolicy(10);
            assertThat(p.getAdditionalForecastModelsNeeded(7)).isEqualTo(3);
            assertThat(p.getAdditionalForecastModelsNeeded(15)).isEqualTo(0);
        }
    }
}
