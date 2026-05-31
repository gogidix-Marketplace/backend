package com.gogidix.aiservices.leadgenerationaiservice.domain.event;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.leadgenerationaiservice.domain.model.ActivityType; import java.time.Instant; import java.util.Map; import java.util.UUID; import static org.assertj.core.api.Assertions.*;
class LeadActivityTest {
    @Test void create() { var a = LeadActivity.builder().leadId(UUID.randomUUID()).type(ActivityType.EMAIL_SENT).description("d").timestamp(Instant.now()).build(); assertThat(a.getActivityId()).isNotNull(); assertThat(a.getType()).isEqualTo(ActivityType.EMAIL_SENT); assertThat(a.isOutreachActivity()).isTrue(); }
    @Test void engagement() { var a = LeadActivity.builder().type(ActivityType.EMAIL_OPENED).description("d").timestamp(Instant.now()).build(); assertThat(a.isEngagementActivity()).isTrue(); }
    @Test void nullType() { assertThatThrownBy(() -> LeadActivity.builder().type(null).description("d").timestamp(Instant.now()).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void nullDesc() { assertThatThrownBy(() -> LeadActivity.builder().type(ActivityType.EMAIL_SENT).description(null).timestamp(Instant.now()).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void emptyDesc() { assertThatThrownBy(() -> LeadActivity.builder().type(ActivityType.EMAIL_SENT).description("").timestamp(Instant.now()).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void nullTimestamp() { assertThatThrownBy(() -> LeadActivity.builder().type(ActivityType.EMAIL_SENT).description("d").timestamp(null).build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void metadata() { var a = LeadActivity.builder().type(ActivityType.CALL_MADE).description("d").timestamp(Instant.now()).metadata(Map.of("k","v")).build(); assertThat(a.getMetadata()).containsEntry("k","v"); }
    @Test void equality() { var a = LeadActivity.builder().type(ActivityType.EMAIL_SENT).description("d").timestamp(Instant.now()).build(); assertThat(a.equals(a)).isTrue(); assertThat(a.equals(null)).isFalse(); }
    @Test void toString_() { assertThat(LeadActivity.builder().type(ActivityType.EMAIL_SENT).description("d").timestamp(Instant.now()).build().toString()).contains("LeadActivity"); }
}
