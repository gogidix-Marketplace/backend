package com.gogidix.ecommerce.vendor.onboarding.application.dto;
import org.junit.jupiter.api.Test; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class OnboardingDtoTest {
    @Test void response_allFields() {
        Instant now = Instant.now();
        OnboardingResponse r = new OnboardingResponse("id1", "t1", "ACTIVE", now, now);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.tenantId()).isEqualTo("t1");
        assertThat(r.status()).isEqualTo("ACTIVE"); assertThat(r.createdAt()).isEqualTo(now);
    }
    @Test void response_nulls() { OnboardingResponse r = new OnboardingResponse(null, null, null, null, null); assertThat(r.id()).isNull(); }
    @Test void createRequest() { CreateOnboardingRequest req = new CreateOnboardingRequest("n1"); assertThat(req.name()).isEqualTo("n1"); }
    @Test void createRequest_null() { CreateOnboardingRequest req = new CreateOnboardingRequest(null); assertThat(req.name()).isNull(); }
    @Test void response_equality() {
        Instant now = Instant.now();
        OnboardingResponse a = new OnboardingResponse("id", "t", "S", now, now);
        OnboardingResponse b = new OnboardingResponse("id", "t", "S", now, now);
        assertThat(a).isEqualTo(b);
    }
}