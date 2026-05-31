package com.gogidix.aiservices.aifeaturestoreservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aifeaturestoreservice.application.dto.*;
import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureType;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.in.FeatureStoreServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeatureStoreController.class)
@DisplayName("FeatureStoreController REST API Tests")
class FeatureStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeatureStoreServicePort featureStoreService;

    private StoreFeaturesRequestDto validRequest;
    private StoreFeaturesResponseDto storeResponse;
    private EntityFeatureDto entityFeatureDto;
    private FeatureDefinitionResponseDto definitionResponse;

    @BeforeEach
    void setUp() {
        validRequest = new StoreFeaturesRequestDto(
                "user_age",
                FeatureType.NUMERIC,
                List.of(new StoreFeaturesRequestDto.EntityFeatureValue("user-123", 25.0)),
                "User age feature",
                null
        );

        storeResponse = new StoreFeaturesResponseDto(
                "v1.0.0",
                1,
                Instant.now()
        );

        entityFeatureDto = new EntityFeatureDto(
                "user-123",
                25.0
        );

        definitionResponse = new FeatureDefinitionResponseDto(
                "user_age",
                FeatureType.NUMERIC,
                "User age feature",
                "v1.0.0",
                List.of(),
                Instant.now().minusSeconds(3600),
                Instant.now()
        );
    }

    @Nested
    @DisplayName("POST /api/v1/feature-store/features - Store Features")
    class StoreFeaturesTests {

        @Test
        @DisplayName("Should store features successfully")
        void shouldStoreFeatures() throws Exception {
            when(featureStoreService.storeFeatures(any(StoreFeaturesRequestDto.class)))
                    .thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.featureVersion").exists())
                    .andExpect(jsonPath("$.storedCount").value(1))
                    .andExpect(jsonPath("$.timestamp").exists());

            verify(featureStoreService).storeFeatures(any(StoreFeaturesRequestDto.class));
        }

        @Test
        @DisplayName("Should accept NUMERIC feature type")
        void shouldAcceptNumericFeatureType() throws Exception {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "price",
                    FeatureType.NUMERIC,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("product-1", 99.99)),
                    null,
                    null
            );

            when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept CATEGORICAL feature type")
        void shouldAcceptCategoricalFeatureType() throws Exception {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "category",
                    FeatureType.CATEGORICAL,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("product-1", "electronics")),
                    null,
                    null
            );

            when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept TEXT feature type")
        void shouldAcceptTextFeatureType() throws Exception {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "description",
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("product-1", "Product description")),
                    null,
                    null
            );

            when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept BOOLEAN feature type")
        void shouldAcceptBooleanFeatureType() throws Exception {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "is_active",
                    FeatureType.BOOLEAN,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("product-1", true)),
                    null,
                    null
            );

            when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept VECTOR feature type")
        void shouldAcceptVectorFeatureType() throws Exception {
            List<Double> vector = List.of(0.1, 0.2, 0.3);
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "embedding",
                    FeatureType.VECTOR,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("item-1", vector)),
                    null,
                    null
            );

            when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should store multiple entity values")
        void shouldStoreMultipleEntityValues() throws Exception {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "feature",
                    FeatureType.NUMERIC,
                    List.of(
                            new StoreFeaturesRequestDto.EntityFeatureValue("entity-1", 10.0),
                            new StoreFeaturesRequestDto.EntityFeatureValue("entity-2", 20.0),
                            new StoreFeaturesRequestDto.EntityFeatureValue("entity-3", 30.0)
                    ),
                    null,
                    null
            );

            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto("v1.0", 3, Instant.now());
            when(featureStoreService.storeFeatures(any())).thenReturn(response);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.storedCount").value(3));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/feature-store/features/{featureName}/entities/{entityId} - Get Features")
    class GetFeaturesTests {

        @Test
        @DisplayName("Should return features for entity")
        void shouldReturnFeaturesForEntity() throws Exception {
            when(featureStoreService.getFeatures(eq("user_age"), eq("user-123"), eq("tenant-001")))
                    .thenReturn(List.of(entityFeatureDto));

            mockMvc.perform(get("/api/v1/feature-store/features/{featureName}/entities/{entityId}",
                                    "user_age", "user-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].entityId").value("user-123"))
                    .andExpect(jsonPath("$[0].value").value(25.0));

            verify(featureStoreService).getFeatures("user_age", "user-123", "tenant-001");
        }

        @Test
        @DisplayName("Should require X-Tenant-ID header")
        void shouldRequireTenantIdHeader() throws Exception {
            mockMvc.perform(get("/api/v1/feature-store/features/{featureName}/entities/{entityId}",
                                    "user_age", "user-123"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(featureStoreService);
        }

        @Test
        @DisplayName("Should handle special characters in feature name")
        void shouldHandleSpecialCharactersInFeatureName() throws Exception {
            when(featureStoreService.getFeatures(anyString(), anyString(), anyString()))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/feature-store/features/{featureName}/entities/{entityId}",
                                    "feature-with_special.chars", "entity-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/feature-store/features/{featureName}/definition - Get Feature Definition")
    class GetFeatureDefinitionTests {

        @Test
        @DisplayName("Should return feature definition")
        void shouldReturnFeatureDefinition() throws Exception {
            when(featureStoreService.getFeatureDefinition(eq("user_age"), eq("tenant-001")))
                    .thenReturn(definitionResponse);

            mockMvc.perform(get("/api/v1/feature-store/features/{featureName}/definition", "user_age")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.featureName").value("user_age"))
                    .andExpect(jsonPath("$.featureType").value("NUMERIC"))
                    .andExpect(jsonPath("$.description").value("User age feature"))
                    .andExpect(jsonPath("$.version").value("v1.0.0"));

            verify(featureStoreService).getFeatureDefinition("user_age", "tenant-001");
        }

        @Test
        @DisplayName("Should require X-Tenant-ID header")
        void shouldRequireTenantIdHeader() throws Exception {
            mockMvc.perform(get("/api/v1/feature-store/features/{featureName}/definition", "user_age"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(featureStoreService);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/feature-store/features - List Feature Definitions")
    class ListFeatureDefinitionsTests {

        @Test
        @DisplayName("Should return list of feature definitions")
        void shouldReturnListOfFeatureDefinitions() throws Exception {
            List<FeatureDefinitionResponseDto> definitions = List.of(definitionResponse);
            when(featureStoreService.listFeatureDefinitions(eq("tenant-001"), eq(0), eq(20)))
                    .thenReturn(definitions);

            mockMvc.perform(get("/api/v1/feature-store/features")
                            .header("X-Tenant-ID", "tenant-001")
                            .param("page", "0")
                            .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].featureName").exists());

            verify(featureStoreService).listFeatureDefinitions("tenant-001", 0, 20);
        }

        @Test
        @DisplayName("Should use default pagination values")
        void shouldUseDefaultPaginationValues() throws Exception {
            when(featureStoreService.listFeatureDefinitions(eq("tenant-001"), eq(0), eq(20)))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/feature-store/features")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk());

            verify(featureStoreService).listFeatureDefinitions("tenant-001", 0, 20);
        }

        @Test
        @DisplayName("Should return empty list when no features")
        void shouldReturnEmptyList() throws Exception {
            when(featureStoreService.listFeatureDefinitions(eq("tenant-001"), eq(0), eq(20)))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/feature-store/features")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should require X-Tenant-ID header")
        void shouldRequireTenantIdHeader() throws Exception {
            mockMvc.perform(get("/api/v1/feature-store/features"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(featureStoreService);
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/feature-store/features/{featureName} - Delete Feature Definition")
    class DeleteFeatureDefinitionTests {

        @Test
        @DisplayName("Should delete feature definition")
        void shouldDeleteFeatureDefinition() throws Exception {
            doNothing().when(featureStoreService).deleteFeatureDefinition(eq("user_age"), eq("tenant-001"));

            mockMvc.perform(delete("/api/v1/feature-store/features/{featureName}", "user_age")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isNoContent());

            verify(featureStoreService).deleteFeatureDefinition("user_age", "tenant-001");
        }

        @Test
        @DisplayName("Should require X-Tenant-ID header")
        void shouldRequireTenantIdHeader() throws Exception {
            mockMvc.perform(delete("/api/v1/feature-store/features/{featureName}", "user_age"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(featureStoreService);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/feature-store/health - Health Check")
    class HealthCheckTests {

        @Test
        @DisplayName("Should return health status")
        void shouldReturnHealthStatus() throws Exception {
            mockMvc.perform(get("/api/v1/feature-store/health"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Feature Store Service is running"));
        }

        @Test
        @DisplayName("Should not require authentication for health check")
        void shouldNotRequireAuthForHealthCheck() throws Exception {
            mockMvc.perform(get("/api/v1/feature-store/health"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("FeatureType Enum Tests")
    class FeatureTypeTests {

        @Test
        @DisplayName("Should accept all feature types in store request")
        void shouldAcceptAllFeatureTypes() throws Exception {
            FeatureType[] types = {
                    FeatureType.NUMERIC,
                    FeatureType.CATEGORICAL,
                    FeatureType.TEXT,
                    FeatureType.BOOLEAN,
                    FeatureType.VECTOR
            };

            for (FeatureType type : types) {
                StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                        "feature",
                        type,
                        List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "val")),
                        null,
                        null
                );

                when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

                mockMvc.perform(post("/api/v1/feature-store/features")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in feature name")
        void shouldHandleUnicodeInFeatureName() throws Exception {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "特性-名",
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "val")),
                    null,
                    null
            );

            when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle very long feature name")
        void shouldHandleVeryLongFeatureName() throws Exception {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "a".repeat(100),
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "val")),
                    null,
                    null
            );

            when(featureStoreService.storeFeatures(any())).thenReturn(storeResponse);

            mockMvc.perform(post("/api/v1/feature-store/features")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle large page size")
        void shouldHandleLargePageSize() throws Exception {
            when(featureStoreService.listFeatureDefinitions(eq("tenant-001"), eq(0), eq(1000)))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/feature-store/features")
                            .header("X-Tenant-ID", "tenant-001")
                            .param("size", "1000"))
                    .andExpect(status().isOk());
        }
    }
}
