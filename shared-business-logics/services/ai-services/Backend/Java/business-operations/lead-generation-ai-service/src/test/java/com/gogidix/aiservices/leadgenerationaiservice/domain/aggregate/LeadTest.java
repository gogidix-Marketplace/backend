package com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*; import com.gogidix.aiservices.leadgenerationaiservice.domain.event.LeadActivity; import static org.assertj.core.api.Assertions.*;
import java.time.Instant; import java.util.UUID;
class LeadTest {
    private ContactInfo contact;
    @BeforeEach void setup() { contact = ContactInfo.builder().email("t@t.com").firstName("J").lastName("D").build(); }
    @Test void create() { Lead l = Lead.create(contact); assertThat(l.getLeadId()).isNotNull(); assertThat(l.getStatus()).isEqualTo(LeadStatus.NEW); }
    @Test void reconstruct() { UUID id = UUID.randomUUID(); Lead l = Lead.reconstruct(id, contact, LeadStatus.CONTACTED, Instant.now()); assertThat(l.getLeadId()).isEqualTo(id); assertThat(l.getStatus()).isEqualTo(LeadStatus.CONTACTED); }
    @Test void nullContact() { assertThatThrownBy(() -> Lead.create(null)).isInstanceOf(IllegalArgumentException.class); }
    @Test void markContacted() { Lead l = Lead.create(contact); l.markAsContacted(); assertThat(l.getStatus()).isEqualTo(LeadStatus.CONTACTED); }
    @Test void markQualified() { Lead l = Lead.create(contact); l.markAsContacted(); l.markAsQualified(); assertThat(l.getStatus()).isEqualTo(LeadStatus.QUALIFIED); }
    @Test void markQualifiedFromNew() { Lead l = Lead.create(contact); l.markAsQualified(); assertThat(l.getStatus()).isEqualTo(LeadStatus.QUALIFIED); }
    @Test void markConverted() { Lead l = Lead.create(contact); l.markAsContacted(); l.markAsQualified(); l.markAsConverted(); assertThat(l.getStatus()).isEqualTo(LeadStatus.CONVERTED); }
    @Test void convertedNotQualified() { assertThatThrownBy(() -> Lead.create(contact).markAsConverted()).isInstanceOf(Exception.class); }
    @Test void markLost() { Lead l = Lead.create(contact); l.markAsLost(LeadLossReason.NOT_INTERESTED); assertThat(l.getStatus()).isEqualTo(LeadStatus.LOST); }
    @Test void lostConverted() { Lead l = Lead.create(contact); l.markAsContacted(); l.markAsQualified(); l.markAsConverted(); assertThatThrownBy(() -> l.markAsLost(LeadLossReason.OTHER)).isInstanceOf(Exception.class); }
    @Test void setSource() { Lead l = Lead.create(contact); l.setSource(LeadSource.builder().name("W").channel(LeadChannel.WEBSITE).build()); assertThat(l.getSource()).isNotNull(); }
    @Test void setScore() { Lead l = Lead.create(contact); l.setScore(LeadScore.builder().leadId(l.getLeadId().toString()).score(80).build()); assertThat(l.getScore().getScore()).isEqualTo(80.0); }
    @Test void assignTo() { Lead l = Lead.create(contact); l.assignTo("o1","Owner"); assertThat(l.getOwnerId()).isEqualTo("o1"); }
    @Test void assignToNull() { assertThatThrownBy(() -> Lead.create(contact).assignTo(null,"x")).isInstanceOf(IllegalArgumentException.class); }
    @Test void addActivity() { Lead l = Lead.create(contact); var a = LeadActivity.builder().type(ActivityType.EMAIL_SENT).description("d").timestamp(Instant.now()).build(); l.addActivity(a); assertThat(l.getActivities()).hasSize(1); }
    @Test void addNullActivity() { assertThatThrownBy(() -> Lead.create(contact).addActivity(null)).isInstanceOf(IllegalArgumentException.class); }
    @Test void addQualification() { Lead l = Lead.create(contact); l.addQualificationCriteria(QualificationCriteria.BUDGET_CONFIRMED); assertThat(l.getQualificationCriteria()).contains(QualificationCriteria.BUDGET_CONFIRMED); }
    @Test void addNullCriteria() { assertThatThrownBy(() -> Lead.create(contact).addQualificationCriteria(null)).isInstanceOf(IllegalArgumentException.class); }
    @Test void customField() { Lead l = Lead.create(contact); l.setCustomField("k","v"); assertThat(l.getCustomFields()).containsEntry("k","v"); }
    @Test void conversionValue() { Lead l = Lead.create(contact); l.setConversionValue(100.0,"USD"); }
    @Test void calculateScore() { Lead l = Lead.create(contact); l.calculateScore(); assertThat(l.getScore()).isNotNull(); }
    @Test void equality() { Lead l1 = Lead.create(contact); Lead l2 = Lead.create(contact); assertThat(l1).isNotEqualTo(l2); assertThat(l1).isEqualTo(l1); assertThat(l1).isNotEqualTo(null); }
    @Test void engagementCount() { Lead l = Lead.create(contact); l.addActivity(LeadActivity.builder().type(ActivityType.EMAIL_OPENED).description("d").timestamp(Instant.now()).build()); assertThat(l.getEngagementCount()).isEqualTo(1); }
    @Test void outreachCount() { Lead l = Lead.create(contact); l.addActivity(LeadActivity.builder().type(ActivityType.EMAIL_SENT).description("d").timestamp(Instant.now()).build()); assertThat(l.getOutreachCount()).isEqualTo(1); }
    @Test void fullyQualified() { Lead l = Lead.create(contact); l.addQualificationCriteria(QualificationCriteria.BUDGET_CONFIRMED); l.addQualificationCriteria(QualificationCriteria.AUTHORITY_CONFIRMED); l.addQualificationCriteria(QualificationCriteria.NEED_VALIDATED); assertThat(l.isFullyQualified()).isTrue(); }
    @Test void stale() { Lead l = Lead.create(contact); assertThat(l.isStale()).isFalse(); }
    @Test void recent() { Lead l = Lead.create(contact); assertThat(l.isRecent()).isTrue(); }
    @Test void toString_() { assertThat(Lead.create(contact).toString()).contains("Lead{"); }
}
