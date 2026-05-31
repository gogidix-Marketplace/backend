package com.gogidix.aiservices.leadgenerationaiservice.application.dto;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.leadgenerationaiservice.application.dto.request.*; import com.gogidix.aiservices.leadgenerationaiservice.application.dto.response.*; import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*; import static org.assertj.core.api.Assertions.*;
class DtoTest {
    @Test void createLeadRequest() { var r = CreateLeadRequest.builder().email("t@t.com").firstName("J").lastName("D").company("C").channel(LeadChannel.WEBSITE).source("Web").build(); assertThat(r.getEmail()).isEqualTo("t@t.com"); }
    @Test void updateStatusRequest() { var r = UpdateLeadStatusRequest.builder().leadId("l1").status(LeadStatus.CONTACTED).build(); assertThat(r.getLeadId()).isEqualTo("l1"); }
    @Test void assignRequest() { var r = AssignLeadRequest.builder().leadId("l1").ownerId("o1").ownerName("O").build(); assertThat(r.getOwnerId()).isEqualTo("o1"); }
    @Test void convertRequest() { var r = ConvertLeadRequest.builder().leadId("l1").value(100.0).currency("USD").build(); assertThat(r.getValue()).isEqualTo(100.0); }
    @Test void addActivityRequest() { var r = AddActivityRequest.builder().leadId("l1").type(ActivityType.EMAIL_SENT).description("d").createdBy("u").build(); assertThat(r.getType()).isEqualTo(ActivityType.EMAIL_SENT); }
    @Test void addQualificationRequest() { var r = AddQualificationRequest.builder().leadId("l1").criteria(QualificationCriteria.BUDGET_CONFIRMED).build(); assertThat(r.getCriteria()).isEqualTo(QualificationCriteria.BUDGET_CONFIRMED); }
    @Test void leadResponse() { var r = LeadResponse.builder().leadId("l1").email("t@t.com").status(LeadStatus.NEW).score(75.0).tier(LeadTier.HIGH_QUALITY).build(); assertThat(r.getLeadId()).isEqualTo("l1"); }
    @Test void scoreResponse() { var r = ScoreResponse.builder().leadId("l1").score(80.0).tier(LeadTier.HIGH_QUALITY).build(); assertThat(r.score()).isEqualTo(80.0); }
    @Test void activityResponse() { var r = ActivityResponse.builder().activityId("a1").type(ActivityType.EMAIL_SENT).description("d").build(); assertThat(r.getActivityId()).isEqualTo("a1"); }
    @Test void conversionResponse() { var r = ConversionResponse.builder().leadId("l1").status(LeadStatus.CONVERTED).value(100.0).currency("USD").build(); assertThat(r.status()).isEqualTo(LeadStatus.CONVERTED); }
}
