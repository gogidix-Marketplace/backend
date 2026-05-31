package com.gogidix.aiservices.intelligenceanalysisservice.shared.exception;
import org.junit.jupiter.api.*; import java.util.List; import static org.assertj.core.api.Assertions.*;
class ExceptionCoverageTest {
    @Test void validation() { var e = new ValidationException(List.of("e1","e2")); assertThat(e.getValidationErrors()).hasSize(2); }
    @Test void validationMsgCause() { assertThat(new ValidationException("m", new RuntimeException()).getValidationErrors()).isNotNull(); }
    @Test void business() { assertThat(new BusinessException("m", "CODE").getErrorCode()).isEqualTo("CODE"); }
    @Test void businessCause() { assertThat(new BusinessException("m", new RuntimeException()).getMessage()).isEqualTo("m"); }
    @Test void businessCodeCause() { assertThat(new BusinessException("m","C",new RuntimeException()).getErrorCode()).isEqualTo("C"); }
    @Test void notFound() { var e = new NotFoundException("User","1"); assertThat(e.getResourceType()).isEqualTo("User"); assertThat(e.getResourceId()).isEqualTo("1"); }
    @Test void notFoundMsg() { assertThat(new NotFoundException("m").getResourceType()).isEqualTo("RESOURCE"); }
    @Test void notFoundCause() { assertThat(new NotFoundException("m", new RuntimeException()).getResourceId()).isEqualTo("UNKNOWN"); }
    @Test void conflict() { var e = new ConflictException("U","v"); assertThat(e.getResourceType()).isEqualTo("U"); assertThat(e.getConflictingValue()).isEqualTo("v"); }
    @Test void conflictMsg() { assertThat(new ConflictException("m").getConflictingValue()).isEqualTo("UNKNOWN"); }
    @Test void conflictCause() { assertThat(new ConflictException("m", new RuntimeException()).getResourceType()).isEqualTo("RESOURCE"); }
    @Test void conflictCode() { assertThat(new ConflictException("U","v","DUP").getErrorCode()).isEqualTo("DUP"); }
}
