package com.gogidix.ecommerce.vendor.onboarding.application.mapper;
import com.gogidix.ecommerce.vendor.onboarding.application.dto.CreateOnboardingRequest;
import com.gogidix.ecommerce.vendor.onboarding.application.dto.OnboardingResponse;
import com.gogidix.ecommerce.vendor.onboarding.domain.model.Onboarding;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach; import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
import java.time.Instant; import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
class OnboardingMapperTest {
    private OnboardingMapper mapper = new OnboardingMapper();
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").metadata(Map.of()).build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void toResponse() {
        Onboarding e = new Onboarding(); e.setId("id1"); e.setTenantId("t1"); e.setCreatedAt(Instant.now()); e.setUpdatedAt(Instant.now());
        OnboardingResponse r = mapper.toResponse(e);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
    }
    @Test void toEntity() {
        CreateOnboardingRequest req = new CreateOnboardingRequest("n1");
        Onboarding e = mapper.toEntity(req);
        assertThat(e.getTenantId()).isEqualTo("t1"); assertThat(e.getName()).isEqualTo("n1");
    }
}