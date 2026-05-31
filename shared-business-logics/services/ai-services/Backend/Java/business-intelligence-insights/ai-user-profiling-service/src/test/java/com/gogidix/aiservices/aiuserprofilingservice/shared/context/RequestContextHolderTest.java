package com.gogidix.aiservices.aiuserprofilingservice.shared.context;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class RequestContextHolderTest {

    @AfterEach
    void cleanup() { RequestContextHolder.clear(); }

    @Test
    void shouldSetAndGetContext() {
        var ctx = RequestContext.create("t1", "u1");
        RequestContextHolder.setContext(ctx);
        assertThat(RequestContextHolder.getContext()).isEqualTo(ctx);
    }

    @Test
    void shouldThrowWhenNoContext() {
        assertThatThrownBy(RequestContextHolder::getContext).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldReturnOptionalEmpty() {
        assertThat(RequestContextHolder.getOptionalContext()).isEmpty();
    }

    @Test
    void shouldGetTenantId() {
        RequestContextHolder.setContext(RequestContext.create("t1", "u1"));
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("t1");
    }

    @Test
    void shouldGetUserId() {
        RequestContextHolder.setContext(RequestContext.create("t1", "u1"));
        assertThat(RequestContextHolder.getUserId()).isEqualTo("u1");
    }

    @Test
    void shouldGetCorrelationId() {
        RequestContextHolder.setContext(RequestContext.createWithCorrelationId("t1", "u1", "corr1"));
        assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("corr1");
    }
}
