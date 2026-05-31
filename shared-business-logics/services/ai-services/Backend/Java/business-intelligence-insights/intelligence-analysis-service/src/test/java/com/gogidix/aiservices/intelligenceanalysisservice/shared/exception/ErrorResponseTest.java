package com.gogidix.aiservices.intelligenceanalysisservice.shared.exception;
import org.junit.jupiter.api.*; import java.util.List; import static org.assertj.core.api.Assertions.*;
class ErrorResponseTest {
    @Test void build() { var e = ErrorResponse.builder().status(400).error("E").message("m").path("/").correlationId("c").tenantId("t").details(List.of("d")).timestamp(java.time.Instant.now()).build(); assertThat(e.getStatus()).isEqualTo(400); }
    @Test void defaults() { var e = ErrorResponse.builder().status(500).error("E").message("m").path("/").build(); assertThat(e.getTimestamp()).isNotNull(); assertThat(e.getDetails()).isNull(); }
    @Test void fromEx() { var e = ErrorResponse.fromException(new ValidationException("m"),"/",400).correlationId("c").tenantId("t").build(); assertThat(e.getStatus()).isEqualTo(400); }
    @Test void fromBiz() { var e = ErrorResponse.fromException(new BusinessException("m","CODE"),"/",409).correlationId("c").tenantId("t").build(); assertThat(e.getError()).isEqualTo("CODE"); }
}
