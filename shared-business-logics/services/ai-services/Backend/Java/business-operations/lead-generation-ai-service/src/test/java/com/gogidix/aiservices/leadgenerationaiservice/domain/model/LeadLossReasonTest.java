package com.gogidix.aiservices.leadgenerationaiservice.domain.model;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class LeadLossReasonTest {
    @Test void values() { assertThat(LeadLossReason.values()).isNotEmpty(); for (var r : LeadLossReason.values()) { assertThat(r.getValue()).isNotNull(); assertThat(r.getDescription()).isNotNull(); } }
    @Test void fromString() { assertThat(LeadLossReason.fromString("not_interested")).isEqualTo(LeadLossReason.NOT_INTERESTED); }
    @Test void fromStringInvalid() { assertThatThrownBy(() -> LeadLossReason.fromString("invalid")).isInstanceOf(IllegalArgumentException.class); }
}
