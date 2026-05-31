package com.gogidix.aiservices.leadgenerationaiservice.domain.model;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class CompanySizeTest {
    @Test void values() { assertThat(CompanySize.values()).isNotEmpty(); for (var s : CompanySize.values()) { assertThat(s.getDisplayName()).isNotNull(); assertThat(s.getMinEmployees()).isGreaterThanOrEqualTo(0); assertThat(s.getMaxEmployees()).isGreaterThanOrEqualTo(0); } }
    @Test void employeeRanges() { assertThat(CompanySize.SOLO.getMinEmployees()).isEqualTo(1); assertThat(CompanySize.ENTERPRISE.getMinEmployees()).isEqualTo(1000); assertThat(CompanySize.MICRO.getMaxEmployees()).isEqualTo(10); }
}
