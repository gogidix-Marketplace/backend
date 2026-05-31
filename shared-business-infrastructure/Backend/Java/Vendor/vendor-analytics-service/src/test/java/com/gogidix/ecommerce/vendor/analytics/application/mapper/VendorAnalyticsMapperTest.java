package com.gogidix.ecommerce.vendor.analytics.application.mapper;
import com.gogidix.ecommerce.vendor.analytics.application.dto.CreateVendorAnalyticsRequest;
import com.gogidix.ecommerce.vendor.analytics.application.dto.VendorAnalyticsResponse;
import com.gogidix.ecommerce.vendor.analytics.domain.model.VendorAnalytics;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach; import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
import java.time.Instant; import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
class VendorAnalyticsMapperTest {
    private VendorAnalyticsMapper mapper = new VendorAnalyticsMapper();
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").metadata(Map.of()).build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void toResponse() {
        VendorAnalytics e = new VendorAnalytics(); e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(Instant.now()); e.setUpdatedAt(Instant.now());
        VendorAnalyticsResponse r = mapper.toResponse(e);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
    }
    @Test void toEntity() {
        CreateVendorAnalyticsRequest req = new CreateVendorAnalyticsRequest("n1");
        VendorAnalytics e = mapper.toEntity(req);
        assertThat(e.getTenantId()).isEqualTo("t1"); assertThat(e.getName()).isEqualTo("n1");
    }
}