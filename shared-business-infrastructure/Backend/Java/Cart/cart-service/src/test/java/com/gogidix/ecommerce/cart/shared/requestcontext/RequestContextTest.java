package com.gogidix.ecommerce.cart.shared.requestcontext;
import org.junit.jupiter.api.AfterEach; import org.junit.jupiter.api.Test; import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat; import static org.assertj.core.api.Assertions.assertThatThrownBy;
class RequestContextTest {
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void builder_minimal() { RequestContext ctx = RequestContext.builder().tenantId("t1").build(); assertThat(ctx.tenantId()).isEqualTo("t1"); }
    @Test void builder_full() { RequestContext ctx = RequestContext.builder().tenantId("t1").customerId("c1").userId("u1").correlationId("corr1").metadata(Map.of("k","v")).build(); assertThat(ctx.customerId()).isEqualTo("c1"); }
    @Test void builder_noTenant() { assertThatThrownBy(() -> RequestContext.builder().build()).isInstanceOf(IllegalArgumentException.class); }
    @Test void holder_setGet() { RequestContext ctx = RequestContext.builder().tenantId("t1").build(); RequestContextHolder.set(ctx); assertThat(RequestContextHolder.get()).isEqualTo(ctx); }
    @Test void holder_noSet() { assertThatThrownBy(() -> RequestContextHolder.get()).isInstanceOf(IllegalStateException.class); }
    @Test void holder_clear() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); RequestContextHolder.clear(); assertThat(RequestContextHolder.isInitialized()).isFalse(); }
}