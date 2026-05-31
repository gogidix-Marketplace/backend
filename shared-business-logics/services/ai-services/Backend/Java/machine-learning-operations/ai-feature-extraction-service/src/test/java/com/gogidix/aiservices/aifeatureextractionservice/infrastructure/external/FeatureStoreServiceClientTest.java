package com.gogidix.aiservices.aifeatureextractionservice.infrastructure.external;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FeatureStoreServiceClient.
 */
@DisplayName("FeatureStoreServiceClient Tests")
class FeatureStoreServiceClientTest {

    private final FeatureStoreServiceClient client = new FeatureStoreServiceClient();

    @Nested
    @DisplayName("Store Features Tests")
    class StoreFeaturesTests {

        @Test
        @DisplayName("Should store features successfully")
        void shouldStoreFeaturesSuccessfully() {
            List<FeatureValue> features = List.of(
                    FeatureValue.numeric("feature1", 1.0),
                    FeatureValue.numeric("feature2", 2.0)
            );

            boolean result = client.storeFeatures("fs-123", "tenant-123", features);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should handle empty feature list")
        void shouldHandleEmptyFeatureList() {
            boolean result = client.storeFeatures("fs-123", "tenant-123", List.of());

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("Retrieve Features Tests")
    class RetrieveFeaturesTests {

        @Test
        @DisplayName("Should retrieve features successfully")
        void shouldRetrieveFeaturesSuccessfully() {
            List<String> entityIds = List.of("entity-1", "entity-2");

            List<FeatureValue> result = client.retrieveFeatures("feature-set-name", entityIds);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should handle empty entity id list")
        void shouldHandleEmptyEntityIdList() {
            List<FeatureValue> result = client.retrieveFeatures("feature-set-name", List.of());

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
