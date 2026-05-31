package com.gogidix.ecommerce.vendor.dashboard.application.mapper;
import com.gogidix.ecommerce.vendor.dashboard.application.dto.CreateVendorDashboardRequest;
import com.gogidix.ecommerce.vendor.dashboard.application.dto.VendorDashboardResponse;
import com.gogidix.ecommerce.vendor.dashboard.domain.model.VendorDashboard;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach; import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
import java.time.Instant; import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
class VendorDashboardMapperTest {
    private VendorDashboardMapper mapper = new VendorDashboardMapper();
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").metadata(Map.of()).build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void toResponse() {
        VendorDashboard e = new VendorDashboard(); e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(Instant.now()); e.setUpdatedAt(Instant.now());
        VendorDashboardResponse r = mapper.toResponse(e);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
    }
    @Test void toEntity() {
        CreateVendorDashboardRequest req = new CreateVendorDashboardRequest("n1");
        VendorDashboard e = mapper.toEntity(req);
        assertThat(e.getTenantId()).isEqualTo("t1"); assertThat(e.getName()).isEqualTo("n1");
    }
}