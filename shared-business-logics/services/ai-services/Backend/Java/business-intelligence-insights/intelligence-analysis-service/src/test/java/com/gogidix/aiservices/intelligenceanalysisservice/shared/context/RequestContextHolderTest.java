package com.gogidix.aiservices.intelligenceanalysisservice.shared.context;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class RequestContextHolderTest {
    @AfterEach void cleanup() { RequestContextHolder.clear(); }
    @Test void setAndGet() { RequestContextHolder.setContext(RequestContext.create("t","u")); assertThat(RequestContextHolder.getTenantId()).isEqualTo("t"); }
    @Test void throwsWhenEmpty() { assertThatThrownBy(RequestContextHolder::getContext).isInstanceOf(IllegalStateException.class); }
    @Test void optionalEmpty() { assertThat(RequestContextHolder.getOptionalContext()).isEmpty(); }
    @Test void correlationId() { RequestContextHolder.setContext(RequestContext.createWithCorrelationId("t","u","c")); assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("c"); }
}
