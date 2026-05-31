package com.gogidix.aiservices.aifeaturestoreservice.application.service;

import com.gogidix.aiservices.aifeaturestoreservice.application.dto.*;
import com.gogidix.aiservices.aifeaturestoreservice.application.mapper.FeatureDefinitionMapper;
import com.gogidix.aiservices.aifeaturestoreservice.domain.model.*;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.out.FeatureDefinitionRepositoryPort;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.out.FeatureValueRepositoryPort;
import com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FeatureStoreApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureStoreApplicationService Tests")
class FeatureStoreApplicationServiceTest {

    @Mock
    private FeatureDefinitionRepositoryPort definitionRepository;

    @Mock
    private FeatureValueRepositoryPort valueRepository;

    @Mock
    private FeatureDefinitionMapper mapper;

    private FeatureStoreApplicationService service;

    private FeatureDefinition definition;
    private FeatureDefinitionResponseDto definitionDto;

    @BeforeEach
    void setUp() {
        service = new FeatureStoreApplicationService(definitionRepository, valueRepository, mapper);
        definition = new FeatureDefinition("tenant-123", "user_lifetime_value", FeatureType.NUMERIC);
        definitionDto = new FeatureDefinitionResponseDto(
                "user_lifetime_value",
                FeatureType.NUMERIC,
                "Customer lifetime value",
                "1.0",
                List.of(),
                definition.getCreatedAt(),
                definition.getUpdatedAt()
        );
    }

    @Nested
    @DisplayName("Store Features Tests")
    class StoreFeaturesTests {

        @Test
        @DisplayName("Should store new feature definition and values")
        void shouldStoreNewFeatureDefinitionAndValues() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "user_lifetime_value",
                    FeatureType.NUMERIC,
                    List.of(
                            new StoreFeaturesRequestDto.EntityFeatureValue("user-001", 1000.0),
                            new StoreFeaturesRequestDto.EntityFeatureValue("user-002", 2000.0)
                    ),
                    "Customer lifetime value",
                    null
            );

            when(definitionRepository.findByFeatureNameAndTenantId(anyString(), anyString()))
                    .thenReturn(Optional.empty());
            when(definitionRepository.save(any(FeatureDefinition.class))).thenReturn(definition);
            when(valueRepository.saveAll(anyList())).thenReturn(List.of());
            when(definitionRepository.save(any(FeatureDefinition.class))).thenReturn(definition);

            StoreFeaturesResponseDto result = service.storeFeatures(request);

            assertNotNull(result);
            assertEquals(2, result.storedCount());
            verify(definitionRepository).save(any(FeatureDefinition.class));
            verify(valueRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("Should update existing feature definition")
        void shouldUpdateExistingFeatureDefinition() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "user_lifetime_value",
                    FeatureType.NUMERIC,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("user-001", 1000.0)),
                    null,
                    null
            );

            when(definitionRepository.findByFeatureNameAndTenantId(anyString(), anyString()))
                    .thenReturn(Optional.of(definition));
            when(valueRepository.saveAll(anyList())).thenReturn(List.of());
            when(definitionRepository.save(any(FeatureDefinition.class))).thenReturn(definition);

            StoreFeaturesResponseDto result = service.storeFeatures(request);

            assertNotNull(result);
            verify(definitionRepository, times(2)).save(any(FeatureDefinition.class));
        }

        @Test
        @DisplayName("Should throw exception when feature type mismatches")
        void shouldThrowWhenFeatureTypeMismatches() {
            FeatureDefinition existingDefinition = new FeatureDefinition("tenant-123", "user_lifetime_value", FeatureType.CATEGORICAL);

            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "user_lifetime_value",
                    FeatureType.NUMERIC,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("user-001", 1000.0)),
                    null,
                    null
            );

            when(definitionRepository.findByFeatureNameAndTenantId(anyString(), anyString()))
                    .thenReturn(Optional.of(existingDefinition));

            assertThrows(IllegalArgumentException.class, () -> service.storeFeatures(request));
        }
    }

    @Nested
    @DisplayName("Get Features Tests")
    class GetFeaturesTests {

        @Test
        @DisplayName("Should get features for specific entity")
        void shouldGetFeaturesForSpecificEntity() {
            FeatureValue fv = new FeatureValue("tenant-123", "user_lifetime_value", "user-001", 1000.0);

            when(valueRepository.findByFeatureNameAndEntityIdAndTenantId("user_lifetime_value", "user-001", "tenant-123"))
                    .thenReturn(Optional.of(fv));

            List<EntityFeatureDto> result = service.getFeatures("user_lifetime_value", "user-001", "tenant-123");

            assertEquals(1, result.size());
            assertEquals("user-001", result.get(0).entityId());
        }

        @Test
        @DisplayName("Should get all features for feature name")
        void shouldGetAllFeaturesForFeatureName() {
            List<FeatureValue> values = List.of(
                    new FeatureValue("tenant-123", "user_lifetime_value", "user-001", 1000.0),
                    new FeatureValue("tenant-123", "user_lifetime_value", "user-002", 2000.0)
            );

            when(valueRepository.findByFeatureNameAndTenantId("user_lifetime_value", "tenant-123"))
                    .thenReturn(values);

            List<EntityFeatureDto> result = service.getFeatures("user_lifetime_value", null, "tenant-123");

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should return empty list when entity not found")
        void shouldReturnEmptyListWhenEntityNotFound() {
            when(valueRepository.findByFeatureNameAndEntityIdAndTenantId("user_lifetime_value", "unknown", "tenant-123"))
                    .thenReturn(Optional.empty());

            List<EntityFeatureDto> result = service.getFeatures("user_lifetime_value", "unknown", "tenant-123");

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Get Feature Definition Tests")
    class GetFeatureDefinitionTests {

        @Test
        @DisplayName("Should get feature definition")
        void shouldGetFeatureDefinition() {
            when(definitionRepository.findByFeatureNameAndTenantId("user_lifetime_value", "tenant-123"))
                    .thenReturn(Optional.of(definition));
            when(mapper.toResponseDto(definition)).thenReturn(definitionDto);

            FeatureDefinitionResponseDto result = service.getFeatureDefinition("user_lifetime_value", "tenant-123");

            assertNotNull(result);
            assertEquals("user_lifetime_value", result.featureName());
        }

        @Test
        @DisplayName("Should throw exception when feature definition not found")
        void shouldThrowWhenFeatureDefinitionNotFound() {
            when(definitionRepository.findByFeatureNameAndTenantId("unknown", "tenant-123"))
                    .thenReturn(Optional.empty());

            assertThrows(ValidationException.class,
                    () -> service.getFeatureDefinition("unknown", "tenant-123"));
        }
    }

    @Nested
    @DisplayName("Delete Feature Definition Tests")
    class DeleteFeatureDefinitionTests {

        @Test
        @DisplayName("Should delete feature definition and values")
        void shouldDeleteFeatureDefinitionAndValues() {
            service.deleteFeatureDefinition("user_lifetime_value", "tenant-123");

            verify(definitionRepository).deleteByFeatureNameAndTenantId("user_lifetime_value", "tenant-123");
            verify(valueRepository).deleteByFeatureNameAndTenantId("user_lifetime_value", "tenant-123");
        }
    }
}
