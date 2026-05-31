package com.gogidix.aiservices.aidatavalidation.domain.aggregate;

import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationRule;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ValidationExecutionTest {
    @Test
    void create() {
        var e = ValidationExecution.create("ds", "schema");
        assertThat(e.getExecutionId()).isNotNull();
        assertThat(e.getDataSource()).isEqualTo("ds");
        assertThat(e.getStatus()).isEqualTo(ValidationExecution.Status.PENDING);
    }

    @Test
    void addAndRemoveRule() {
        var e = ValidationExecution.create("ds", "schema");
        var r = ValidationRule.create("rule1", "Rule One", ValidationType.SCHEMA);
        e.addRule(r);
        assertThat(e.getRules()).hasSize(1);
        e.removeRule("rule1");
        assertThat(e.getRules()).isEmpty();
    }

    @Test
    void startComplete() {
        var e = ValidationExecution.create("ds", "schema");
        e.start();
        assertThat(e.getStatus()).isEqualTo(ValidationExecution.Status.RUNNING);
    }

    @Test
    void startFail() {
        var e = ValidationExecution.create("ds", "schema");
        e.start();
        e.fail("error");
        assertThat(e.getStatus()).isEqualTo(ValidationExecution.Status.FAILED);
        assertThat(e.getErrorMessage()).isEqualTo("error");
    }

    @Test
    void setTimeoutAndProgress() {
        var e = ValidationExecution.create("ds", "schema");
        e.setTimeout(120);
        e.setProgress(50);
        assertThat(e.getTimeout()).isEqualTo(120);
        assertThat(e.getProgress()).isEqualTo(50);
    }

    @Test
    void restore() {
        var e = ValidationExecution.restore("id", "ds", "schema",
            ValidationExecution.Status.COMPLETED, Instant.now(), null, Instant.now(),
            List.of(), 60, 100, null, null);
        assertThat(e.getExecutionId()).isEqualTo("id");
    }

    @Test
    void statusEnum() {
        assertThat(ValidationExecution.Status.values()).hasSize(4);
    }
}
