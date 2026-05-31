package com.gogidix.aiservices.leadgenerationaiservice.domain.model;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class QualificationCriteriaTest {
    @Test void values() { assertThat(QualificationCriteria.values()).isNotEmpty(); for (var c : QualificationCriteria.values()) { assertThat(c.getValue()).isNotNull(); assertThat(c.getDescription()).isNotNull(); } }
    @Test void fromString() { assertThat(QualificationCriteria.fromString("budget_confirmed")).isEqualTo(QualificationCriteria.BUDGET_CONFIRMED); }
}
