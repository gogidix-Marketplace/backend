package com.gogidix.ecommerce.vendor.application.mapper;
import com.gogidix.ecommerce.vendor.application.dto.CreateVendorRequest;
import com.gogidix.ecommerce.vendor.application.dto.VendorResponse;
import com.gogidix.ecommerce.vendor.domain.model.Vendor;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach; import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
import java.time.Instant; import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
class VendorMapperTest {
    private VendorMapper mapper = new VendorMapper();
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").metadata(Map.of()).build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void toResponse() {
        Vendor e = new Vendor(); e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(Instant.now()); e.setUpdatedAt(Instant.now());
        VendorResponse r = mapper.toResponse(e);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
    }
    @Test void toEntity() {
        CreateVendorRequest req = new CreateVendorRequest("n1");
        Vendor e = mapper.toEntity(req);
        assertThat(e.getTenantId()).isEqualTo("t1"); assertThat(e.getName()).isEqualTo("n1");
    }
}