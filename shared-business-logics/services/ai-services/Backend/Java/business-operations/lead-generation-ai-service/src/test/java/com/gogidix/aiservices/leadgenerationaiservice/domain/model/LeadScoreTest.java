package com.gogidix.aiservices.leadgenerationaiservice.domain.model;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class LeadScoreTest {
    @Test void create() { var s = LeadScore.builder().leadId("l1").score(75).build(); assertThat(s.getScore()).isEqualTo(75.0); assertThat(s.getTier()).isEqualTo(LeadTier.HIGH_QUALITY); assertThat(s.isHighQuality()).isTrue(); }
    @Test void nullLeadId() { assertThatThrownBy(() -> LeadScore.builder().leadId(null).score(50).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void emptyLeadId() { assertThatThrownBy(() -> LeadScore.builder().leadId("").score(50).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void negativeScore() { assertThatThrownBy(() -> LeadScore.builder().leadId("l1").score(-1).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void overHundred() { assertThatThrownBy(() -> LeadScore.builder().leadId("l1").score(101).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void tiers() { assertThat(LeadScore.builder().leadId("l").score(95).build().isHotLead()).isTrue(); assertThat(LeadScore.builder().leadId("l").score(60).build().isMediumQuality()).isTrue(); assertThat(LeadScore.builder().leadId("l").score(30).build().isLowQuality()).isTrue(); }
    @Test void actionable() { assertThat(LeadScore.builder().leadId("l").score(55).build().isActionable()).isTrue(); assertThat(LeadScore.builder().leadId("l").score(40).build().isActionable()).isFalse(); }
    @Test void withBoost() { var s = LeadScore.builder().leadId("l").score(50).build().withBoost(10); assertThat(s.getScore()).isGreaterThan(50); }
    @Test void withReduction() { var s = LeadScore.builder().leadId("l").score(50).build().withReduction(50); assertThat(s.getScore()).isEqualTo(25.0); }
    @Test void withAbsoluteScore() { var s = LeadScore.builder().leadId("l").score(50).build().withAbsoluteScore(80); assertThat(s.getScore()).isEqualTo(80.0); }
    @Test void compareTo() { var s1 = LeadScore.builder().leadId("l").score(60).build(); var s2 = LeadScore.builder().leadId("l").score(80).build(); assertThat(s1.compareTo(s2)).isGreaterThan(0); }
    @Test void classifiedTier() { assertThat(LeadScore.builder().leadId("l").score(95).build().getClassifiedTier()).isEqualTo(LeadTier.HOT_LEAD); }
}
