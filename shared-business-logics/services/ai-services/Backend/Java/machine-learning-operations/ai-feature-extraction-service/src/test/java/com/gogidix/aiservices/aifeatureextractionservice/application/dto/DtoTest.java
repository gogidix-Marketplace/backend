package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.*;
import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class DtoTest {

    @Test
    void featureSchemaResponseDto() {
        var dto = new FeatureSchemaResponseDto("id1", List.of(FeatureValue.numeric("f", 1.0)), "numerical");
        assertThat(dto.featureSetId()).isEqualTo("id1");
        assertThat(dto.schemaType()).isEqualTo("numerical");
    }

    @Test
    void featureSetResponseDto() {
        var now = Instant.now();
        var dto = new FeatureSetResponseDto("id1", "src", FeatureExtractionStatus.COMPLETED,
            List.of(ExtractionMethod.TFIDF), List.of(), 5, true, null, now, now);
        assertThat(dto.featureSetId()).isEqualTo("id1");
        assertThat(dto.featureCount()).isEqualTo(5);
        assertThat(dto.normalized()).isTrue();
    }

    @Test
    void extractFeaturesRequestDto() {
        var dto = new ExtractFeaturesRequestDto("source", List.of("f1", "f2"),
            List.of(ExtractionMethod.PCA, ExtractionMethod.BERT), true);
        assertThat(dto.dataSource()).isEqualTo("source");
        assertThat(dto.normalize()).isTrue();
        assertThat(dto.methods()).hasSize(2);
    }
}
