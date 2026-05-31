package com.gogidix.ecommerce.influencer.commission.application.dto;
import org.junit.jupiter.api.Test; import java.math.BigDecimal; import java.time.Instant; import static org.assertj.core.api.Assertions.assertThat;
class CommissionDtoTest {
    @Test void dto_allFields() {
        Instant now = Instant.now();
        CommissionDto dto = new CommissionDto("id1", "t1", "aff1", "ord1", BigDecimal.valueOf(100), BigDecimal.valueOf(0.1), BigDecimal.TEN, "PENDING", now, now);
        assertThat(dto.id()).isEqualTo("id1"); assertThat(dto.affiliateId()).isEqualTo("aff1");
        assertThat(dto.orderId()).isEqualTo("ord1"); assertThat(dto.amount()).isEqualByComparingTo("100");
        assertThat(dto.commissionRate()).isEqualByComparingTo("0.1"); assertThat(dto.commission()).isEqualByComparingTo("10");
        assertThat(dto.status()).isEqualTo("PENDING");
    }
    @Test void dto_nulls() { CommissionDto dto = new CommissionDto(null, null, null, null, null, null, null, null, null, null); assertThat(dto.id()).isNull(); }
    @Test void response_fromDto() {
        CommissionDto dto = new CommissionDto("id1", "t1", "aff1", "ord1", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ONE, "APPROVED", null, null);
        CommissionResponse r = CommissionResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.affiliateId()).isEqualTo("aff1");
        assertThat(r.commission()).isEqualByComparingTo(BigDecimal.ONE); assertThat(r.status()).isEqualTo("APPROVED");
    }
    @Test void createRequest() { CreateCommissionRequest req = new CreateCommissionRequest("aff1", "ord1", BigDecimal.TEN, BigDecimal.ONE); assertThat(req.affiliateId()).isEqualTo("aff1"); }
    @Test void createRequest_nulls() { CreateCommissionRequest req = new CreateCommissionRequest(null, null, null, null); assertThat(req.affiliateId()).isNull(); }
    @Test void equality() {
        CommissionDto a = new CommissionDto("id", "t", "a", "o", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, "S", null, null);
        CommissionDto b = new CommissionDto("id", "t", "a", "o", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, "S", null, null);
        assertThat(a).isEqualTo(b);
    }
}