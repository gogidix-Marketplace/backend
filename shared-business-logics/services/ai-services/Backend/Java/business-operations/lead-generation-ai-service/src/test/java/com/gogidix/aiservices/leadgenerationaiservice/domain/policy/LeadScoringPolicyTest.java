package com.gogidix.aiservices.leadgenerationaiservice.domain.policy;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead; import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*; import static org.assertj.core.api.Assertions.*;
class LeadScoringPolicyTest {
    private final LeadScoringPolicy policy = new LeadScoringPolicy();
    private Lead baseLead;
    @BeforeEach void setup() { baseLead = Lead.create(ContactInfo.builder().email("t@t.com").company("C").companySize(CompanySize.ENTERPRISE).jobTitle("CEO").industry("Tech").build()); }
    @Test void calculateBaseScore() { double s = policy.calculateBaseScore(baseLead); assertThat(s).isBetween(0.0, 100.0); }
    @Test void scoreSource() { baseLead.setSource(LeadSource.builder().name("R").channel(LeadChannel.REFERRAL).build()); assertThat(policy.scoreSource(baseLead.getSource())).isGreaterThan(0); }
    @Test void scoreSourceNull() { assertThat(policy.scoreSource(null)).isEqualTo(0.0); }
    @Test void scoreEngagement() { assertThat(policy.scoreEngagement(baseLead)).isGreaterThanOrEqualTo(0.0); }
    @Test void scoreQualification() { baseLead.addQualificationCriteria(QualificationCriteria.BUDGET_CONFIRMED); assertThat(policy.scoreQualification(baseLead)).isGreaterThan(0); }
    @Test void scoreDemographics() { double s = policy.scoreDemographics(baseLead); assertThat(s).isGreaterThan(0); }
    @Test void applyTimeDecay() { double s = policy.applyTimeDecay(80, baseLead); assertThat(s).isEqualTo(80.0); }
    @Test void calculateScore() { var score = policy.calculateScore(baseLead); assertThat(score).isNotNull(); assertThat(score.getScore()).isBetween(0.0, 100.0); }
    @Test void isHighPriority() { baseLead.addQualificationCriteria(QualificationCriteria.BUDGET_CONFIRMED); baseLead.addQualificationCriteria(QualificationCriteria.AUTHORITY_CONFIRMED); baseLead.addQualificationCriteria(QualificationCriteria.NEED_VALIDATED); baseLead.setSource(LeadSource.builder().name("R").channel(LeadChannel.REFERRAL).build()); boolean hp = policy.isHighPriority(baseLead); assertThat(hp).isTrue(); }
    @Test void shouldEscalate() { assertThat(policy.shouldEscalate(baseLead)).isFalse(); }
}
