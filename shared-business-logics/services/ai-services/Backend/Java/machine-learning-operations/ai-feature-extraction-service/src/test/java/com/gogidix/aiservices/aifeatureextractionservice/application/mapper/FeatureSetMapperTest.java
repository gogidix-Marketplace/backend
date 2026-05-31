package com.gogidix.aiservices.aifeatureextractionservice.application.mapper;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class FeatureSetMapperTest {

    private final FeatureSetMapper mapper = new FeatureSetMapper();

    @Test
    void toResponseDto() {
        var fs = FeatureSet.builder().tenantId("t").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.TFIDF))
            .features(List.of(FeatureValue.numeric("f1", 1.0)))
            .errorMessage("err").build();
        var dto = mapper.toResponseDto(fs);
        assertThat(dto.featureSetId()).isEqualTo(fs.getId());
        assertThat(dto.dataSource()).isEqualTo("src");
        assertThat(dto.status()).isEqualTo(FeatureExtractionStatus.PENDING);
        assertThat(dto.featureCount()).isEqualTo(0);
    }

    @Test
    void toResponseDtoNullFeatureCount() {
        var fs = FeatureSet.builder().tenantId("t").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.PCA)).build();
        var dto = mapper.toResponseDto(fs);
        assertThat(dto.featureCount()).isEqualTo(0);
    }

    @Test
    void toSchemaDto() {
        var fs = FeatureSet.builder().tenantId("t").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.BERT)).build();
        var dto = mapper.toSchemaDto(fs);
        assertThat(dto.featureSetId()).isEqualTo(fs.getId());
        assertThat(dto.schemaType()).isEqualTo("numerical");
    }
}
