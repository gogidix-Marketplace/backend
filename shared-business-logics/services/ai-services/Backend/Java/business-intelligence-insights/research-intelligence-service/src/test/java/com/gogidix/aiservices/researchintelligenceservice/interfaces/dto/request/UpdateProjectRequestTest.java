package com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.request;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject; import static org.assertj.core.api.Assertions.*;
class UpdateProjectRequestTest {
    @Test void create() {
        var r = new UpdateProjectRequest("t","d",ResearchProject.ResearchDomain.MACHINE_LEARNING,ResearchProject.Priority.HIGH);
        assertThat(r.title()).isEqualTo("t");
        assertThat(r.description()).isEqualTo("d");
        assertThat(r.domain()).isEqualTo(ResearchProject.ResearchDomain.MACHINE_LEARNING);
        assertThat(r.priority()).isEqualTo(ResearchProject.Priority.HIGH);
    }
    @Test void nulls() { assertThat(new UpdateProjectRequest(null,null,null,null).title()).isNull(); }
}
