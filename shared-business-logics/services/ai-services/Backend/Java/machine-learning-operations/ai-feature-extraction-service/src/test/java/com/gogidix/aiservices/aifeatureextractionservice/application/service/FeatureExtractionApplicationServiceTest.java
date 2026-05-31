package com.gogidix.aiservices.aifeatureextractionservice.application.service;

import com.gogidix.aiservices.aifeatureextractionservice.application.dto.ExtractFeaturesRequestDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSchemaResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSetResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.mapper.FeatureSetMapper;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureExtractionStatus;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureSet;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.out.FeatureSetRepositoryPort;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.out.FeatureStoreClientPort;
import com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FeatureExtractionApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureExtractionApplicationService Tests")
class FeatureExtractionApplicationServiceTest {

    @Mock
    private FeatureSetRepositoryPort featureSetRepository;

    @Mock
    private FeatureStoreClientPort featureStoreClient;

    @Mock
    private FeatureSetMapper featureSetMapper;

    @InjectMocks
    private FeatureExtractionApplicationService service;

    private FeatureSet featureSet;
    private FeatureSetResponseDto responseDto;

    @BeforeEach
    void setUp() {
        featureSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));
        responseDto = new FeatureSetResponseDto(
                featureSet.getId(),
                featureSet.getDataSource(),
                FeatureExtractionStatus.COMPLETED,
                featureSet.getExtractionMethods(),
                List.of(FeatureValue.numeric("feature1", 1.0)),
                1,
                true,
                null,
                featureSet.getCreatedAt(),
                Instant.now()
        );
    }

    @Nested
    @DisplayName("Extract Features Tests")
    class ExtractFeaturesTests {

        @Test
        @DisplayName("Should extract features successfully")
        void shouldExtractFeaturesSuccessfully() {
            ExtractFeaturesRequestDto request = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1", "feature2"),
                    List.of(ExtractionMethod.TFIDF),
                    true
            );

            when(featureSetRepository.save(any(FeatureSet.class))).thenReturn(featureSet);
            when(featureSetMapper.toResponseDto(any(FeatureSet.class))).thenReturn(responseDto);
            when(featureStoreClient.storeFeatures(anyString(), anyString(), anyList())).thenReturn(true);

            FeatureSetResponseDto result = service.extractFeatures(request);

            assertNotNull(result);
            verify(featureSetRepository, times(2)).save(any(FeatureSet.class));
            verify(featureStoreClient).storeFeatures(anyString(), anyString(), anyList());
        }

        @Test
        @DisplayName("Should handle extraction failure")
        void shouldHandleExtractionFailure() {
            ExtractFeaturesRequestDto request = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1"),
                    List.of(ExtractionMethod.TFIDF),
                    true
            );

            FeatureSet failedSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));
            failedSet.markAsProcessing();
            failedSet.failWith("Extraction error");

            when(featureSetRepository.save(any(FeatureSet.class)))
                    .thenReturn(featureSet)
                    .thenReturn(failedSet);

            FeatureSetResponseDto failedResponse = new FeatureSetResponseDto(
                    failedSet.getId(),
                    failedSet.getDataSource(),
                    FeatureExtractionStatus.FAILED,
                    failedSet.getExtractionMethods(),
                    List.of(),
                    0,
                    true,
                    "Extraction error",
                    failedSet.getCreatedAt(),
                    failedSet.getCompletedAt()
            );

            when(featureSetMapper.toResponseDto(any(FeatureSet.class)))
                    .thenReturn(responseDto)
                    .thenReturn(failedResponse);

            service.extractFeatures(request);

            verify(featureSetRepository, times(2)).save(any(FeatureSet.class));
        }

        @Test
        @DisplayName("Should extract features without normalization")
        void shouldExtractFeaturesWithoutNormalization() {
            ExtractFeaturesRequestDto request = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1"),
                    List.of(ExtractionMethod.PCA),
                    false
            );

            when(featureSetRepository.save(any(FeatureSet.class))).thenReturn(featureSet);
            when(featureSetMapper.toResponseDto(any(FeatureSet.class))).thenReturn(responseDto);

            FeatureSetResponseDto result = service.extractFeatures(request);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("Get Feature Schema Tests")
    class GetFeatureSchemaTests {

        @Test
        @DisplayName("Should get feature schema successfully")
        void shouldGetFeatureSchemaSuccessfully() {
            featureSet.markAsProcessing();
            featureSet.completeWith(List.of(FeatureValue.numeric("feature1", 1.0)));

            when(featureSetRepository.findByIdAndTenantId("fs-123", "tenant-123"))
                    .thenReturn(Optional.of(featureSet));

            FeatureSchemaResponseDto result = service.getFeatureSchema("fs-123", "tenant-123");

            assertNotNull(result);
            assertEquals("fs-123", result.featureSetId());
        }

        @Test
        @DisplayName("Should throw exception when feature set not found")
        void shouldThrowWhenFeatureSetNotFound() {
            when(featureSetRepository.findByIdAndTenantId("invalid-id", "tenant-123"))
                    .thenReturn(Optional.empty());

            assertThrows(ValidationException.class,
                    () -> service.getFeatureSchema("invalid-id", "tenant-123"));
        }
    }

    @Nested
    @DisplayName("Get Feature Set Tests")
    class GetFeatureSetTests {

        @Test
        @DisplayName("Should get feature set successfully")
        void shouldGetFeatureSetSuccessfully() {
            when(featureSetRepository.findByIdAndTenantId("fs-123", "tenant-123"))
                    .thenReturn(Optional.of(featureSet));
            when(featureSetMapper.toResponseDto(featureSet)).thenReturn(responseDto);

            FeatureSetResponseDto result = service.getFeatureSet("fs-123", "tenant-123");

            assertNotNull(result);
            assertEquals("fs-123", result.featureSetId());
        }

        @Test
        @DisplayName("Should throw exception when feature set not found")
        void shouldThrowWhenGettingNonExistentFeatureSet() {
            when(featureSetRepository.findByIdAndTenantId("invalid-id", "tenant-123"))
                    .thenReturn(Optional.empty());

            assertThrows(ValidationException.class,
                    () -> service.getFeatureSet("invalid-id", "tenant-123"));
        }
    }

    @Nested
    @DisplayName("List Feature Sets Tests")
    class ListFeatureSetsTests {

        @Test
        @DisplayName("Should list all feature sets for tenant")
        void shouldListAllFeatureSetsForTenant() {
            List<FeatureSet> featureSets = List.of(
                    featureSet,
                    new FeatureSet("tenant-123", "data-source-2", List.of(ExtractionMethod.PCA))
            );

            when(featureSetRepository.findByTenantId("tenant-123", 0, 10))
                    .thenReturn(featureSets);

            List<FeatureSetResponseDto> result = service.listFeatureSets("tenant-123", null, null, 0, 10);

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should filter feature sets by data source")
        void shouldFilterFeatureSetsByDataSource() {
            FeatureSet fs1 = new FeatureSet("tenant-123", "source-1", List.of(ExtractionMethod.TFIDF));
            FeatureSet fs2 = new FeatureSet("tenant-123", "source-2", List.of(ExtractionMethod.PCA));

            when(featureSetRepository.findByTenantId("tenant-123", 0, 10))
                    .thenReturn(List.of(fs1, fs2));

            List<FeatureSetResponseDto> result = service.listFeatureSets("tenant-123", "source-1", null, 0, 10);

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should filter feature sets by status")
        void shouldFilterFeatureSetsByStatus() {
            FeatureSet fs1 = new FeatureSet("tenant-123", "source-1", List.of(ExtractionMethod.TFIDF));
            fs1.markAsProcessing();
            fs1.completeWith(List.of(FeatureValue.numeric("f1", 1.0)));

            FeatureSet fs2 = new FeatureSet("tenant-123", "source-2", List.of(ExtractionMethod.PCA));

            when(featureSetRepository.findByTenantId("tenant-123", 0, 10))
                    .thenReturn(List.of(fs1, fs2));

            List<FeatureSetResponseDto> result = service.listFeatureSets("tenant-123", null, "COMPLETED", 0, 10);

            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("Delete Feature Set Tests")
    class DeleteFeatureSetTests {

        @Test
        @DisplayName("Should delete feature set successfully")
        void shouldDeleteFeatureSetSuccessfully() {
            featureSet.markAsProcessing();
            featureSet.completeWith(List.of(FeatureValue.numeric("feature1", 1.0)));

            when(featureSetRepository.findByIdAndTenantId("fs-123", "tenant-123"))
                    .thenReturn(Optional.of(featureSet));

            service.deleteFeatureSet("fs-123", "tenant-123");

            verify(featureSetRepository).deleteById("fs-123");
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent feature set")
        void shouldThrowWhenDeletingNonExistentFeatureSet() {
            when(featureSetRepository.findByIdAndTenantId("invalid-id", "tenant-123"))
                    .thenReturn(Optional.empty());

            assertThrows(ValidationException.class,
                    () -> service.deleteFeatureSet("invalid-id", "tenant-123"));
        }

        @Test
        @DisplayName("Should throw exception when deleting processing feature set")
        void shouldThrowWhenDeletingProcessingFeatureSet() {
            FeatureSet processingSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));
            processingSet.markAsProcessing();

            when(featureSetRepository.findByIdAndTenantId("fs-123", "tenant-123"))
                    .thenReturn(Optional.of(processingSet));

            assertThrows(IllegalStateException.class,
                    () -> service.deleteFeatureSet("fs-123", "tenant-123"));
        }
    }
}
