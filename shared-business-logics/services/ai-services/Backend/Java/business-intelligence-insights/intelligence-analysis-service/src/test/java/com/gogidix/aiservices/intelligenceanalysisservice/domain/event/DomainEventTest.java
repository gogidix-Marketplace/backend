package com.gogidix.aiservices.intelligenceanalysisservice.domain.event;
import org.junit.jupiter.api.*; import java.time.Instant; import static org.assertj.core.api.Assertions.*;
class DomainEventTest {
    @Test void created() { var e = new AnalysisCreatedEvent("a","t","n","s",Instant.now()); assertThat(e.getEventId()).isNotNull(); assertThat(e.getAnalysisName()).isEqualTo("n"); }
    @Test void createdNull() { assertThatThrownBy(() -> new AnalysisCreatedEvent(null,"t","n","s",Instant.now())).isInstanceOf(NullPointerException.class); }
    @Test void deleted() { var e = new AnalysisDeletedEvent("a","t","n","d",1L,Instant.now()); assertThat(e.getDeletionType()).isEqualTo("d"); assertThat(e.getIntelligenceReportCount()).isEqualTo(1L); }
    @Test void updated() { var e = new AnalysisUpdatedEvent("a","t","o","n","c",Instant.now()); assertThat(e.getOldName()).isEqualTo("o"); }
    @Test void reportsAdded() { var e = new IntelligenceReportsAddedToAnalysisEvent("a","t","n",5,10L,Instant.now()); assertThat(e.getIntelligenceReportsAdded()).isEqualTo(5); }
    @Test void equality() { var e = new AnalysisCreatedEvent("a","t","n","s",Instant.now()); assertThat(e.equals(e)).isTrue(); assertThat(e.equals(null)).isFalse(); assertThat(e.toString()).contains("AnalysisCreatedEvent"); }
}
