package com.gogidix.ecommerce.influencer.affiliate.application.dto;
import org.junit.jupiter.api.Test; import java.math.BigDecimal; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class AffiliateDtoTest {
    @Test void dto_allFields() {
        Instant now = Instant.now();
        AffiliateDto dto = new AffiliateDto("id1", "t1", "u1", "AFF-001", BigDecimal.valueOf(0.1), BigDecimal.valueOf(500), true, now, now);
        assertThat(dto.id()).isEqualTo("id1"); assertThat(dto.userId()).isEqualTo("u1");
        assertThat(dto.affiliateCode()).isEqualTo("AFF-001");
        assertThat(dto.commissionRate()).isEqualByComparingTo("0.1");
        assertThat(dto.totalEarnings()).isEqualByComparingTo("500");
        assertThat(dto.active()).isTrue();
    }
    @Test void dto_nulls() { AffiliateDto dto = new AffiliateDto(null, null, null, null, null, null, false, null, null); assertThat(dto.id()).isNull(); }
    @Test void response_fromDto() {
        AffiliateDto dto = new AffiliateDto("id1", "t1", "u1", "AFF-001", BigDecimal.TEN, BigDecimal.valueOf(100), true, null, null);
        AffiliateResponse r = AffiliateResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.userId()).isEqualTo("u1");
        assertThat(r.affiliateCode()).isEqualTo("AFF-001");
        assertThat(r.totalEarnings()).isEqualByComparingTo("100");
    }
    @Test void createRequest() { CreateAffiliateRequest req = new CreateAffiliateRequest("u1", "AFF-002", BigDecimal.valueOf(0.15)); assertThat(req.userId()).isEqualTo("u1"); }
    @Test void createRequest_nulls() { CreateAffiliateRequest req = new CreateAffiliateRequest(null, null, null); assertThat(req.userId()).isNull(); }
    @Test void equality() {
        AffiliateDto a = new AffiliateDto("id", "t", "u", "c", BigDecimal.ONE, BigDecimal.ZERO, true, null, null);
        AffiliateDto b = new AffiliateDto("id", "t", "u", "c", BigDecimal.ONE, BigDecimal.ZERO, true, null, null);
        assertThat(a).isEqualTo(b);
    }
}