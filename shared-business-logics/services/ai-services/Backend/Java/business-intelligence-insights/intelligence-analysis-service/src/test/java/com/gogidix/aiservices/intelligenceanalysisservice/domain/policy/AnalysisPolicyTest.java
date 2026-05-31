package com.gogidix.aiservices.intelligenceanalysisservice.domain.policy;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class AnalysisPolicyTest {
    @Nested class MaxAnalysissPerTenantPolicyTests {
        @Test void defaults() { var p = new MaxAnalysissPerTenantPolicy(); assertThat(p.getMaxAnalysiss()).isEqualTo(100); }
        @Test void rejectsZero() { assertThatThrownBy(() -> new MaxAnalysissPerTenantPolicy(0)).isInstanceOf(IllegalArgumentException.class); }
        @Test void validateUnderLimit() { var p = new MaxAnalysissPerTenantPolicy(10); assertThatCode(() -> p.validate(5,"t")).doesNotThrowAnyException(); }
        @Test void throwsAtLimit() { var p = new MaxAnalysissPerTenantPolicy(10); assertThatThrownBy(() -> p.validate(10,"t")).isInstanceOf(Exception.class); }
        @Test void canCreate() { var p = new MaxAnalysissPerTenantPolicy(10); assertThat(p.canCreateAnalysis(9)).isTrue(); assertThat(p.canCreateAnalysis(10)).isFalse(); }
        @Test void remaining() { assertThat(new MaxAnalysissPerTenantPolicy(10).getRemainingAnalysiss(7)).isEqualTo(3); }
    }
    @Nested class AnalysisNameUniquePolicyTests {
        private final AnalysisNameUniquePolicy p = new AnalysisNameUniquePolicy();
        @Test void unique() { assertThatCode(() -> p.validate(java.util.List.of("a"),"b","t")).doesNotThrowAnyException(); }
        @Test void duplicate() { assertThatThrownBy(() -> p.validate(java.util.List.of("A"),"a","t")).isInstanceOf(Exception.class); }
        @Test void nullList() { assertThatCode(() -> p.validate(null,"n","t")).doesNotThrowAnyException(); }
        @Test void isUnique() { assertThat(p.isUnique(java.util.List.of("a"),"b")).isTrue(); assertThat(p.isUnique(null,"a")).isFalse(); }
    }
    @Nested class MinimumIntelligenceReportCountPolicyTests {
        @Test void activation() { var p = new MinimumIntelligenceReportCountPolicy(10); assertThatCode(() -> p.validateForActivation(10,"f")).doesNotThrowAnyException(); assertThatThrownBy(() -> p.validateForActivation(5,"f")).isInstanceOf(Exception.class); }
        @Test void canActivate() { assertThat(new MinimumIntelligenceReportCountPolicy(10).canActivate(10)).isTrue(); }
        @Test void needed() { assertThat(new MinimumIntelligenceReportCountPolicy(10).getAdditionalIntelligenceReportsNeeded(7)).isEqualTo(3); }
    }
}
