package com.gogidix.aiservices.aifeatureextractionservice.domain.model;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class FeatureSetTest {

    private FeatureSet createFeatureSet() {
        return new FeatureSet("tenant1", "source1", List.of(ExtractionMethod.TFIDF));
    }

    @Test
    void constructorSetsDefaults() {
        var fs = createFeatureSet();
        assertThat(fs.getTenantId()).isEqualTo("tenant1");
        assertThat(fs.getDataSource()).isEqualTo("source1");
        assertThat(fs.getStatus()).isEqualTo(FeatureExtractionStatus.PENDING);
        assertThat(fs.getFeatureCount()).isEqualTo(0);
        assertThat(fs.isNormalized()).isTrue();
        assertThat(fs.isModifiable()).isTrue();
        assertThat(fs.isCompleted()).isFalse();
        assertThat(fs.getId()).isNotNull();
        assertThat(fs.getCreatedAt()).isNotNull();
    }

    @Test
    void constructorNullTenantId() {
        assertThatThrownBy(() -> new FeatureSet(null, "src", List.of(ExtractionMethod.PCA)))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void constructorNullDataSource() {
        assertThatThrownBy(() -> new FeatureSet("t", null, List.of(ExtractionMethod.PCA)))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void constructorNullMethods() {
        assertThatThrownBy(() -> new FeatureSet("t", "src", null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void markAsProcessing() {
        var fs = createFeatureSet();
        fs.markAsProcessing();
        assertThat(fs.getStatus()).isEqualTo(FeatureExtractionStatus.PROCESSING);
        assertThat(fs.isModifiable()).isFalse();
    }

    @Test
    void markAsProcessingFromNonPendingFails() {
        var fs = createFeatureSet();
        fs.markAsProcessing();
        assertThatThrownBy(fs::markAsProcessing).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void completeWithFeatures() {
        var fs = createFeatureSet();
        fs.markAsProcessing();
        var features = List.of(FeatureValue.numeric("f1", 1.0), FeatureValue.text("f2", "hello"));
        fs.completeWith(features);
        assertThat(fs.getStatus()).isEqualTo(FeatureExtractionStatus.COMPLETED);
        assertThat(fs.isCompleted()).isTrue();
        assertThat(fs.getFeatureCount()).isEqualTo(2);
        assertThat(fs.getCompletedAt()).isNotNull();
    }

    @Test
    void completeWithFromNonProcessingFails() {
        var fs = createFeatureSet();
        assertThatThrownBy(() -> fs.completeWith(List.of()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void completeWithNullFeaturesFails() {
        var fs = createFeatureSet();
        fs.markAsProcessing();
        assertThatThrownBy(() -> fs.completeWith(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void failWith() {
        var fs = createFeatureSet();
        fs.markAsProcessing();
        fs.failWith("error occurred");
        assertThat(fs.getStatus()).isEqualTo(FeatureExtractionStatus.FAILED);
        assertThat(fs.getErrorMessage()).isEqualTo("error occurred");
        assertThat(fs.getCompletedAt()).isNotNull();
    }

    @Test
    void failWithFromNonProcessingFails() {
        var fs = createFeatureSet();
        assertThatThrownBy(() -> fs.failWith("err")).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void cancel() {
        var fs = createFeatureSet();
        fs.cancel();
        assertThat(fs.getStatus()).isEqualTo(FeatureExtractionStatus.CANCELLED);
    }

    @Test
    void cancelCompletedFails() {
        var fs = createFeatureSet();
        fs.markAsProcessing();
        fs.completeWith(List.of(FeatureValue.numeric("f1", 1.0)));
        assertThatThrownBy(fs::cancel).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void cancelFailedFails() {
        var fs = createFeatureSet();
        fs.markAsProcessing();
        fs.failWith("err");
        assertThatThrownBy(fs::cancel).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void addFeature() {
        var fs = createFeatureSet();
        fs.addFeature(FeatureValue.numeric("f1", 42));
        assertThat(fs.getFeatureCount()).isEqualTo(1);
    }

    @Test
    void addFeatureNullFails() {
        var fs = createFeatureSet();
        assertThatThrownBy(() -> fs.addFeature(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void setNormalized() {
        var fs = createFeatureSet();
        fs.setNormalized(false);
        assertThat(fs.isNormalized()).isFalse();
    }

    @Test
    void validateSuccess() {
        var fs = createFeatureSet();
        fs.validate();
    }

    @Test
    void validateMissingTenantIdFails() throws Exception {
        var fs = FeatureSet.builder().tenantId("t").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.TFIDF)).build();
        var field = FeatureSet.class.getDeclaredField("tenantId");
        field.setAccessible(true);
        field.set(fs, "  ");
        assertThatThrownBy(fs::validate)
            .isInstanceOf(com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException.class);
    }

    @Test
    void validateMissingDataSourceFails() throws Exception {
        var fs = FeatureSet.builder().tenantId("t").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.TFIDF)).build();
        var field = FeatureSet.class.getDeclaredField("dataSource");
        field.setAccessible(true);
        field.set(fs, "  ");
        assertThatThrownBy(fs::validate)
            .isInstanceOf(com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException.class);
    }

    @Test
    void validateEmptyMethodsFails() throws Exception {
        var fs = FeatureSet.builder().tenantId("t").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.TFIDF)).build();
        var field = FeatureSet.class.getDeclaredField("extractionMethods");
        field.setAccessible(true);
        field.set(fs, Collections.emptyList());
        assertThatThrownBy(fs::validate)
            .isInstanceOf(com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException.class);
    }

    @Test
    void builderMissingTenantIdFails() {
        assertThatThrownBy(() -> FeatureSet.builder().dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.TFIDF)).build())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void builderMissingDataSourceFails() {
        assertThatThrownBy(() -> FeatureSet.builder().tenantId("t")
            .extractionMethods(List.of(ExtractionMethod.TFIDF)).build())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void builderSetsDefaults() {
        var fs = FeatureSet.builder().tenantId("t").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.BERT))
            .errorMessage("err").featureCount(5).build();
        assertThat(fs.getStatus()).isEqualTo(FeatureExtractionStatus.PENDING);
        assertThat(fs.isNormalized()).isTrue();
        assertThat(fs.getCreatedAt()).isNotNull();
    }

    @Test
    void equalsAndHashCode() {
        var fs1 = createFeatureSet();
        var fs2 = FeatureSet.builder().tenantId("other").dataSource("src")
            .extractionMethods(List.of(ExtractionMethod.TFIDF)).build();
        assertThat(fs1).isNotEqualTo(fs2);
        assertThat(fs1).isEqualTo(fs1);
        assertThat(fs1).isNotEqualTo(null);
        assertThat(fs1).isNotEqualTo("string");
    }

    @Test
    void toStringTest() {
        var fs = createFeatureSet();
        assertThat(fs.toString()).contains("FeatureSet").contains("tenant1");
    }

    @Test
    void enumValues() {
        assertThat(FeatureExtractionStatus.values()).hasSize(5);
        assertThat(ExtractionMethod.values()).hasSize(7);
        assertThat(FeatureType.values()).hasSize(8);
    }
}
