package com.gogidix.aiservices.researchintelligenceservice.domain.model;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ResearchMetricsTest {
    @Test void create() {
        var m = new ResearchMetrics("m1","p1",ResearchMetrics.MetricsPeriod.MONTHLY);
        assertThat(m.getMetricId()).isEqualTo("m1");
        assertThat(m.getProjectId()).isEqualTo("p1");
        assertThat(m.getPeriod()).isEqualTo(ResearchMetrics.MetricsPeriod.MONTHLY);
        assertThat(m.getTotalPublications()).isEqualTo(0);
        assertThat(m.getHIndex()).isEqualTo(0.0);
        assertThat(m.getCalculatedAt()).isNotNull();
    }
    @Test void nullMetricId() { assertThatThrownBy(() -> new ResearchMetrics(null,"p1",ResearchMetrics.MetricsPeriod.DAILY)).isInstanceOf(NullPointerException.class); }
    @Test void nullProjectId() { assertThatThrownBy(() -> new ResearchMetrics("m1",null,ResearchMetrics.MetricsPeriod.DAILY)).isInstanceOf(NullPointerException.class); }
    @Test void nullPeriod() { assertThatThrownBy(() -> new ResearchMetrics("m1","p1",null)).isInstanceOf(NullPointerException.class); }
    @Test void equality() {
        var m1 = new ResearchMetrics("m1","p1",ResearchMetrics.MetricsPeriod.DAILY);
        var m2 = new ResearchMetrics("m1","p2",ResearchMetrics.MetricsPeriod.WEEKLY);
        var m3 = new ResearchMetrics("m2","p1",ResearchMetrics.MetricsPeriod.DAILY);
        assertThat(m1).isEqualTo(m1); assertThat(m1).isEqualTo(m2); assertThat(m1).isNotEqualTo(m3);
        assertThat(m1).isNotEqualTo(null); assertThat(m1).isNotEqualTo("str");
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }
    @Test void toString_() { assertThat(new ResearchMetrics("m1","p1",ResearchMetrics.MetricsPeriod.DAILY).toString()).contains("m1"); }
    @Test void metricsPeriodValues() { assertThat(ResearchMetrics.MetricsPeriod.values()).hasSize(6); assertThat(ResearchMetrics.MetricsPeriod.valueOf("ALL_TIME")).isEqualTo(ResearchMetrics.MetricsPeriod.ALL_TIME); }
}
