package com.gogidix.aiservices.aisalesforecastingservice.application.command;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class CreateSalesForecastCommandTest {

    private ForecastCriteria validCriteria() {
        return ForecastCriteria.builder()
            .type(ForecastCriteria.CriteriaType.DEMOGRAPHIC)
            .operator(ForecastCriteria.CriteriaOperator.EQUALS)
            .field("age")
            .value("25")
            .logicalOperator(ForecastCriteria.LogicalOperator.AND)
            .build();
    }

    @Test
    void shouldCreateValidCommand() {
        var cmd = new CreateSalesForecastCommand("t1", "u1", "name", "desc", "BEHAVIORAL", validCriteria());
        assertThat(cmd.name()).isEqualTo("name");
        assertThat(cmd.tenantId()).isEqualTo("t1");
    }

    @Test
    void shouldRejectNullName() {
        assertThatThrownBy(() -> new CreateSalesForecastCommand("t1", "u1", null, "d", "s", validCriteria()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectBlankName() {
        assertThatThrownBy(() -> new CreateSalesForecastCommand("t1", "u1", "  ", "d", "s", validCriteria()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectLongName() {
        assertThatThrownBy(() -> new CreateSalesForecastCommand("t1", "u1", "x".repeat(101), "d", "s", validCriteria()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullCriteria() {
        assertThatThrownBy(() -> new CreateSalesForecastCommand("t1", "u1", "name", "d", "s", null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullSegmentType() {
        assertThatThrownBy(() -> new CreateSalesForecastCommand("t1", "u1", "name", "d", null, validCriteria()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectBlankSegmentType() {
        assertThatThrownBy(() -> new CreateSalesForecastCommand("t1", "u1", "name", "d", "  ", validCriteria()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
