package com.gogidix.aiservices.aisalesforecastingservice.shared.context;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class RequestContextHolderTest {

    @AfterEach
    void cleanup() {
        RequestContextHolder.clear();
    }

    @Test
    void shouldSetAndGetContext() {
        var ctx = RequestContext.create("t1", "u1");
        RequestContextHolder.setContext(ctx);
        assertThat(RequestContextHolder.getContext()).isEqualTo(ctx);
    }

    @Test
    void shouldThrowWhenNoContext() {
        assertThatThrownBy(RequestContextHolder::getContext)
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldReturnOptionalEmpty() {
        assertThat(RequestContextHolder.getOptionalContext()).isEmpty();
    }

    @Test
    void shouldReturnOptionalPresent() {
        var ctx = RequestContext.create("t1", "u1");
        RequestContextHolder.setContext(ctx);
        assertThat(RequestContextHolder.getOptionalContext()).isPresent();
    }

    @Test
    void shouldClearContext() {
        RequestContextHolder.setContext(RequestContext.create("t1", "u1"));
        RequestContextHolder.clear();
        assertThat(RequestContextHolder.getOptionalContext()).isEmpty();
    }

    @Test
    void shouldGetTenantId() {
        var ctx = RequestContext.create("t1", "u1");
        RequestContextHolder.setContext(ctx);
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("t1");
    }

    @Test
    void shouldGetUserId() {
        var ctx = RequestContext.create("t1", "u1");
        RequestContextHolder.setContext(ctx);
        assertThat(RequestContextHolder.getUserId()).isEqualTo("u1");
    }

    @Test
    void shouldGetCorrelationId() {
        var ctx = RequestContext.createWithCorrelationId("t1", "u1", "corr1");
        RequestContextHolder.setContext(ctx);
        assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("corr1");
    }

    @Test
    void shouldNotInstantiate() {
        assertThatThrownBy(() -> {
            var m = RequestContextHolder.class.getDeclaredConstructor();
            m.setAccessible(true);
            m.newInstance();
        }).hasCauseInstanceOf(UnsupportedOperationException.class);
    }
}
