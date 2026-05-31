package com.gogidix.ecommerce.category.shared.requestcontext;
import org.junit.jupiter.api.AfterEach; import org.junit.jupiter.api.Test; import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat; import static org.assertj.core.api.Assertions.assertThatThrownBy;
class RequestContextTest {
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void builder_minimal() { RequestContext ctx = RequestContext.builder().tenantId("t1").build(); assertThat(ctx.tenantId()).isEqualTo("t1"); }
    @Test void builder_noTenant() { assertThatThrownBy(() -> RequestContext.builder().build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void holder_setGet() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); assertThat(RequestContextHolder.get()).isNotNull(); }
    @Test void holder_clear() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); RequestContextHolder.clear(); assertThatThrownBy(() -> RequestContextHolder.get()).isInstanceOf(IllegalStateException.class); }
}