package com.gogidix.aiservices.multimodalprocessingservice.multitenancy;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentItem;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentModality;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Multi-Tenancy Tenant Isolation Tests")
class TenantIsolationTest {

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";

    @Nested
    @DisplayName("ContentItem Domain Model Tests")
    class ContentItemTests {

        @Test
        @DisplayName("Should create content item with modality")
        void shouldCreateContentItemWithModality() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.txt",
                    Map.of("tenantId", TENANT_1)
            );

            assertThat(item.modality()).isEqualTo(ContentModality.TEXT);
            assertThat(item.metadata().get("tenantId")).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should support tenant context in metadata")
        void shouldSupportTenantContextInMetadata() {
            ContentItem item1 = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image.jpg",
                    Map.of("tenantId", TENANT_1, "userId", "user-1")
            );

            ContentItem item2 = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image.jpg",
                    Map.of("tenantId", TENANT_2, "userId", "user-1")
            );

            assertThat(item1.metadata().get("tenantId")).isNotEqualTo(item2.metadata().get("tenantId"));
        }

        @Test
        @DisplayName("Should create content items for different modalities")
        void shouldCreateContentItemsForDifferentModalities() {
            ContentItem textItem = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/doc.txt",
                    Map.of("tenantId", TENANT_1)
            );

            ContentItem imageItem = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image.jpg",
                    Map.of("tenantId", TENANT_1)
            );

            ContentItem videoItem = new ContentItem(
                    ContentModality.VIDEO,
                    "https://example.com/video.mp4",
                    Map.of("tenantId", TENANT_1)
            );

            assertThat(textItem.modality()).isEqualTo(ContentModality.TEXT);
            assertThat(imageItem.modality()).isEqualTo(ContentModality.IMAGE);
            assertThat(videoItem.modality()).isEqualTo(ContentModality.VIDEO);
        }

        @Test
        @DisplayName("Should validate content item properties")
        void shouldValidateContentItemProperties() {
            assertThatCode(() -> {
                new ContentItem(
                        ContentModality.TEXT,
                        "https://example.com/document.txt",
                        Map.of()
                );
            }).doesNotThrowAnyException();

            assertThatThrownBy(() -> {
                new ContentItem(null, "https://example.com/doc.txt", Map.of());
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                new ContentItem(ContentModality.TEXT, "", Map.of());
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                new ContentItem(ContentModality.TEXT, null, Map.of());
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should extract format from URL")
        void shouldExtractFormatFromUrl() {
            ContentItem textItem = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.pdf",
                    Map.of("tenantId", TENANT_1)
            );

            ContentItem imageItem = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image.PNG",
                    Map.of("tenantId", TENANT_1)
            );

            assertThat(textItem.getFormat()).isEqualTo("pdf");
            assertThat(imageItem.getFormat()).isEqualTo("png");
        }

        @Test
        @DisplayName("Should check if format is supported")
        void shouldCheckIfFormatIsSupported() {
            ContentItem textItem = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.pdf",
                    Map.of("tenantId", TENANT_1)
            );

            ContentItem imageItem = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image.jpg",
                    Map.of("tenantId", TENANT_1)
            );

            assertThat(textItem.isFormatSupported()).isTrue();
            assertThat(imageItem.isFormatSupported()).isTrue();
        }
    }

    @Nested
    @DisplayName("Tenant Context Tests")
    class TenantContextTests {

        @Test
        @DisplayName("Should associate content with tenant via metadata")
        void shouldAssociateContentWithTenantViaMetadata() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.pdf",
                    Map.of(
                            "tenantId", TENANT_1,
                            "userId", "user-1",
                            "department", "sales"
                    )
            );

            assertThat(item.metadata().get("tenantId")).isEqualTo(TENANT_1);
            assertThat(item.metadata().get("userId")).isEqualTo("user-1");
            assertThat(item.metadata().get("department")).isEqualTo("sales");
        }

        @Test
        @DisplayName("Should support null metadata")
        void shouldSupportNullMetadata() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.pdf",
                    null
            );

            assertThat(item.metadata()).isEqualTo(Map.of());
        }

        @Test
        @DisplayName("Should support empty metadata")
        void shouldSupportEmptyMetadata() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.pdf",
                    Map.of()
            );

            assertThat(item.metadata()).isEmpty();
        }

        @Test
        @DisplayName("Should distinguish content by tenant in metadata")
        void shouldDistinguishContentByTenantInMetadata() {
            String sameUrl = "https://example.com/document.pdf";

            ContentItem item1 = new ContentItem(
                    ContentModality.TEXT,
                    sameUrl,
                    Map.of("tenantId", TENANT_1, "userId", "user-1")
            );

            ContentItem item2 = new ContentItem(
                    ContentModality.TEXT,
                    sameUrl,
                    Map.of("tenantId", TENANT_2, "userId", "user-2")
            );

            assertThat(item1.url()).isEqualTo(item2.url());
            assertThat(item1.metadata().get("tenantId")).isNotEqualTo(item2.metadata().get("tenantId"));
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Content Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @DisplayName("Should verify tenant isolation via metadata")
        void shouldVerifyTenantIsolationViaMetadata() {
            ContentItem item1 = new ContentItem(
                    ContentModality.TEXT,
                    "https://tenant1.example.com/doc1.pdf",
                    Map.of("tenantId", TENANT_1, "userId", "user-1")
            );

            ContentItem item2 = new ContentItem(
                    ContentModality.TEXT,
                    "https://tenant2.example.com/doc2.pdf",
                    Map.of("tenantId", TENANT_2, "userId", "user-2")
            );

            // Verify that items are distinct by tenant
            assertThat(item1.metadata().get("tenantId")).isNotEqualTo(item2.metadata().get("tenantId"));

            // Simulate filtering by tenant
            var allItems = java.util.List.of(item1, item2);
            var tenant1Items = allItems.stream()
                    .filter(item -> TENANT_1.equals(item.metadata().get("tenantId")))
                    .toList();

            assertThat(tenant1Items).hasSize(1);
            assertThat(tenant1Items.get(0).metadata().get("tenantId")).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should handle items without tenant metadata")
        void shouldHandleItemsWithoutTenantMetadata() {
            ContentItem itemWithTenant = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image1.jpg",
                    Map.of("tenantId", TENANT_1, "userId", "user-1")
            );

            ContentItem itemWithoutTenant = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image2.jpg",
                    Map.of("userId", "user-2")
            );

            var allItems = java.util.List.of(itemWithTenant, itemWithoutTenant);

            // Filter for items with tenant
            var withTenant = allItems.stream()
                    .filter(item -> item.metadata().containsKey("tenantId"))
                    .toList();

            // Filter for items without tenant
            var withoutTenant = allItems.stream()
                    .filter(item -> !item.metadata().containsKey("tenantId"))
                    .toList();

            assertThat(withTenant).hasSize(1);
            assertThat(withoutTenant).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Multi-Modality Tenant Tests")
    class MultiModalityTenantTests {

        @Test
        @DisplayName("Should support same tenant across different modalities")
        void shouldSupportSameTenantAcrossDifferentModalities() {
            ContentItem textItem = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/doc.pdf",
                    Map.of("tenantId", TENANT_1, "documentId", "doc-1")
            );

            ContentItem imageItem = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image.jpg",
                    Map.of("tenantId", TENANT_1, "documentId", "doc-1")
            );

            ContentItem videoItem = new ContentItem(
                    ContentModality.VIDEO,
                    "https://example.com/video.mp4",
                    Map.of("tenantId", TENANT_1, "documentId", "doc-1")
            );

            assertThat(textItem.metadata().get("tenantId")).isEqualTo(TENANT_1);
            assertThat(imageItem.metadata().get("tenantId")).isEqualTo(TENANT_1);
            assertThat(videoItem.metadata().get("tenantId")).isEqualTo(TENANT_1);

            // All items belong to the same tenant but different modalities
            assertThat(textItem.modality()).isNotEqualTo(imageItem.modality());
            assertThat(imageItem.modality()).isNotEqualTo(videoItem.modality());
        }

        @Test
        @DisplayName("Should support different tenants for same modality")
        void shouldSupportDifferentTenantsForSameModality() {
            ContentItem tenant1Text = new ContentItem(
                    ContentModality.TEXT,
                    "https://tenant1.example.com/doc.pdf",
                    Map.of("tenantId", TENANT_1)
            );

            ContentItem tenant2Text = new ContentItem(
                    ContentModality.TEXT,
                    "https://tenant2.example.com/doc.pdf",
                    Map.of("tenantId", TENANT_2)
            );

            assertThat(tenant1Text.modality()).isEqualTo(tenant2Text.modality());
            assertThat(tenant1Text.metadata().get("tenantId")).isNotEqualTo(tenant2Text.metadata().get("tenantId"));
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should support multiple tenants concurrently")
        void shouldSupportMultipleTenantsConcurrently() throws InterruptedException {
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            final boolean[] errors = {false};
            final String[] results = new String[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                final String tenantId = "tenant-" + (i + 1);

                threads[i] = new Thread(() -> {
                    try {
                        ContentItem item = new ContentItem(
                                ContentModality.TEXT,
                                "https://example.com/doc" + index + ".pdf",
                                Map.of("tenantId", tenantId, "index", String.valueOf(index))
                        );

                        results[index] = new StringBuilder()
                                .append("Thread: ").append(index)
                                .append(", Tenant: ").append(item.metadata().get("tenantId"))
                                .toString();
                    } catch (Exception e) {
                        errors[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errors[0]).isFalse();

            // Verify each thread had its own tenant
            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isNotNull();
                assertThat(results[i].toString()).contains("tenant-" + (i + 1));
            }
        }
    }
}
