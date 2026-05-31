package com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.response;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher; import static org.assertj.core.api.Assertions.*;
class ResearcherResponseTest {
    @Test void from() {
        var r = new Researcher("r1","John","john@test.com",Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR,"MIT");
        var resp = ResearcherResponse.from(r);
        assertThat(resp.researcherId()).isEqualTo("r1");
        assertThat(resp.name()).isEqualTo("John");
        assertThat(resp.email()).isEqualTo("john@test.com");
        assertThat(resp.role()).isEqualTo(Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR);
        assertThat(resp.affiliation()).isEqualTo("MIT");
    }
}
