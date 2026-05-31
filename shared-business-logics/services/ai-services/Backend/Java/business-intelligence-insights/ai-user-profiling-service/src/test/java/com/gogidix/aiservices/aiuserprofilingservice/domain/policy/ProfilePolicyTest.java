package com.gogidix.aiservices.aiuserprofilingservice.domain.policy;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ProfilePolicyTest {

    @Nested
    class MaxProfilesPerTenantPolicyTests {
        @Test
        void shouldCreateWithDefault() {
            var p = new MaxProfilesPerTenantPolicy();
            assertThat(p.getMaxProfiles()).isEqualTo(100);
        }
        @Test
        void shouldRejectZero() {
            assertThatThrownBy(() -> new MaxProfilesPerTenantPolicy(0))
                .isInstanceOf(IllegalArgumentException.class);
        }
        @Test
        void shouldValidateUnderLimit() {
            var p = new MaxProfilesPerTenantPolicy(10);
            assertThatCode(() -> p.validate(5, "t1")).doesNotThrowAnyException();
        }
        @Test
        void shouldThrowAtLimit() {
            var p = new MaxProfilesPerTenantPolicy(10);
            assertThatThrownBy(() -> p.validate(10, "t1")).isInstanceOf(Exception.class);
        }
        @Test
        void canCreateProfileTest() {
            var p = new MaxProfilesPerTenantPolicy(10);
            assertThat(p.canCreateProfile(9)).isTrue();
            assertThat(p.canCreateProfile(10)).isFalse();
        }
        @Test
        void getRemainingProfilesTest() {
            var p = new MaxProfilesPerTenantPolicy(10);
            assertThat(p.getRemainingProfiles(7)).isEqualTo(3);
        }
    }

    @Nested
    class ProfileNameUniquePolicyTests {
        private final ProfileNameUniquePolicy policy = new ProfileNameUniquePolicy();
        @Test
        void shouldAllowUniqueName() {
            assertThatCode(() -> policy.validate(java.util.List.of("a", "b"), "c", "t1")).doesNotThrowAnyException();
        }
        @Test
        void shouldRejectDuplicate() {
            assertThatThrownBy(() -> policy.validate(java.util.List.of("alpha"), "ALPHA", "t1")).isInstanceOf(Exception.class);
        }
        @Test
        void shouldAllowNullList() {
            assertThatCode(() -> policy.validate(null, "name", "t1")).doesNotThrowAnyException();
        }
        @Test
        void isUniqueTest() {
            assertThat(policy.isUnique(java.util.List.of("a"), "b")).isTrue();
            assertThat(policy.isUnique(java.util.List.of("a"), "A")).isFalse();
            assertThat(policy.isUnique(null, "a")).isFalse();
        }
        @Test
        void generateUniqueNameTest() {
            assertThat(policy.generateUniqueName("base", java.util.List.of("other"))).isEqualTo("base");
            assertThat(policy.generateUniqueName("base", java.util.List.of("base"))).isEqualTo("base_1");
        }
    }

    @Nested
    class MinimumUserCountPolicyTests {
        @Test
        void shouldValidateActivationEnough() {
            var p = new MinimumUserCountPolicy(10);
            assertThatCode(() -> p.validateForActivation(10, "f1")).doesNotThrowAnyException();
        }
        @Test
        void shouldRejectActivationTooFew() {
            var p = new MinimumUserCountPolicy(10);
            assertThatThrownBy(() -> p.validateForActivation(5, "f1")).isInstanceOf(Exception.class);
        }
        @Test
        void canActivateTest() {
            var p = new MinimumUserCountPolicy(10);
            assertThat(p.canActivate(10)).isTrue();
            assertThat(p.canActivate(9)).isFalse();
        }
        @Test
        void getAdditionalUsersNeededTest() {
            var p = new MinimumUserCountPolicy(10);
            assertThat(p.getAdditionalUsersNeeded(7)).isEqualTo(3);
            assertThat(p.getAdditionalUsersNeeded(15)).isEqualTo(0);
        }
    }
}
