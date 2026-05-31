package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.persistence;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OptimizationRequestRepositoryImpl Infrastructure Tests")
class OptimizationRequestRepositoryImplTest {

    private InMemoryOptimizationRequestDataSource dataSource;
    private OptimizationRequestRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        dataSource = new InMemoryOptimizationRequestDataSource();
        repository = new OptimizationRequestRepositoryImpl(dataSource);
    }

    @Nested
    @DisplayName("Save Operation Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save new optimization request")
        void shouldSaveNewRequest() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest saved = repository.save(request);

            assertThat(saved.getRequestId()).isEqualTo(request.getRequestId());
            assertThat(repository.findById(saved.getRequestId().toString())).isPresent();
        }

        @Test
        @DisplayName("Should update existing request")
        void shouldUpdateExistingRequest() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest saved = repository.save(request);
            saved.startProcessing();
            OptimizationRequest updated = repository.save(saved);

            assertThat(updated.getStatus()).isEqualTo(OptimizationStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("Should preserve events on update")
        void shouldPreserveEvents() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest saved = repository.save(request);
            saved.startProcessing();
            OptimizationRequest updated = repository.save(saved);

            assertThat(updated.getEvents()).hasSize(2); // Created + Processing Started
        }
    }

    @Nested
    @DisplayName("Find By ID Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should find request by ID")
        void shouldFindById() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest saved = repository.save(request);

            Optional<OptimizationRequest> found = repository.findById(saved.getRequestId().toString());

            assertThat(found).isPresent();
            assertThat(found.get().getRequestId()).isEqualTo(saved.getRequestId());
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            Optional<OptimizationRequest> found = repository.findById(UUID.randomUUID().toString());

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By Request ID Tests")
    class FindByRequestIdTests {

        @Test
        @DisplayName("Should find request by UUID request ID")
        void shouldFindByRequestId() {
            UUID requestId = UUID.randomUUID();
            OptimizationRequest request = OptimizationRequest.builder()
                    .requestId(requestId)
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            repository.save(request);

            Optional<OptimizationRequest> found = repository.findByRequestId(requestId);

            assertThat(found).isPresent();
            assertThat(found.get().getRequestId()).isEqualTo(requestId);
        }

        @Test
        @DisplayName("Should return empty for non-existent request ID")
        void shouldReturnEmptyForNonExistentRequestId() {
            UUID requestId = UUID.randomUUID();

            Optional<OptimizationRequest> found = repository.findByRequestId(requestId);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By Tenant ID Tests")
    class FindByTenantIdTests {

        @Test
        @DisplayName("Should find requests by tenant ID")
        void shouldFindByTenantId() {
            String tenantId = "tenant-123";

            OptimizationRequest request1 = OptimizationRequest.builder()
                    .tenantId(tenantId)
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest request2 = OptimizationRequest.builder()
                    .tenantId(tenantId)
                    .type(OptimizationType.ROUTE_OPTIMIZATION)
                    .build();

            repository.save(request1);
            repository.save(request2);

            List<OptimizationRequest> found = repository.findByTenantId(tenantId);

            assertThat(found).hasSize(2);
        }

        @Test
        @DisplayName("Should return empty list for non-existent tenant")
        void shouldReturnEmptyForNonExistentTenant() {
            List<OptimizationRequest> found = repository.findByTenantId("non-existent-tenant");

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By Status Tests")
    class FindByStatusTests {

        @Test
        @DisplayName("Should find requests by status")
        void shouldFindByStatus() {
            OptimizationRequest request1 = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest request2 = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.ROUTE_OPTIMIZATION)
                    .build();

            repository.save(request1);
            repository.save(request2);

            request1.startProcessing();

            List<OptimizationRequest> pending = repository.findByStatus(OptimizationStatus.PENDING);
            List<OptimizationRequest> inProgress = repository.findByStatus(OptimizationStatus.IN_PROGRESS);

            assertThat(pending).hasSize(1);
            assertThat(inProgress).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Find By Type Tests")
    class FindByTypeTests {

        @Test
        @DisplayName("Should find requests by type")
        void shouldFindByType() {
            OptimizationRequest request1 = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest request2 = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.ROUTE_OPTIMIZATION)
                    .build();

            repository.save(request1);
            repository.save(request2);

            List<OptimizationRequest> inventoryRequests = repository.findByType(OptimizationType.INVENTORY_LEVELS);
            List<OptimizationRequest> routeRequests = repository.findByType(OptimizationType.ROUTE_OPTIMIZATION);

            assertThat(inventoryRequests).hasSize(1);
            assertThat(routeRequests).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Find Pending Requests Tests")
    class FindPendingRequestsTests {

        @Test
        @DisplayName("Should find pending requests")
        void shouldFindPendingRequests() {
            for (int i = 0; i < 5; i++) {
                OptimizationRequest request = OptimizationRequest.builder()
                        .tenantId("tenant-123")
                        .type(OptimizationType.INVENTORY_LEVELS)
                        .priority(i)
                        .build();
                repository.save(request);
            }

            List<OptimizationRequest> pending = repository.findPendingRequests(3);

            assertThat(pending).hasSize(3);
        }

        @Test
        @DisplayName("Should respect limit parameter")
        void shouldRespectLimitParameter() {
            for (int i = 0; i < 10; i++) {
                OptimizationRequest request = OptimizationRequest.builder()
                        .tenantId("tenant-123")
                        .type(OptimizationType.INVENTORY_LEVELS)
                        .build();
                repository.save(request);
            }

            List<OptimizationRequest> pending = repository.findPendingRequests(5);

            assertThat(pending).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Find By Created After Tests")
    class FindByCreatedAtAfterTests {

        @Test
        @DisplayName("Should find requests created after timestamp")
        void shouldFindByCreatedAtAfter() {
            Instant now = Instant.now();

            OptimizationRequest request1 = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            repository.save(request1);

            Instant beforeRequest = Instant.now().minusSeconds(10);
            Instant afterRequest = Instant.now().plusSeconds(10);

            List<OptimizationRequest> foundBefore = repository.findByCreatedAtAfter(beforeRequest);
            List<OptimizationRequest> foundAfter = repository.findByCreatedAtAfter(afterRequest);

            assertThat(foundBefore).hasSize(1);
            assertThat(foundAfter).isEmpty();
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete request")
        void shouldDeleteRequest() {
            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .build();

            OptimizationRequest saved = repository.save(request);

            repository.delete(saved.getRequestId().toString());

            Optional<OptimizationRequest> found = repository.findById(saved.getRequestId().toString());
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("Save All Tests")
    class SaveAllTests {

        @Test
        @DisplayName("Should save multiple requests")
        void shouldSaveMultipleRequests() {
            List<OptimizationRequest> requests = Arrays.asList(
                    OptimizationRequest.builder()
                            .tenantId("tenant-123")
                            .type(OptimizationType.INVENTORY_LEVELS)
                            .build(),
                    OptimizationRequest.builder()
                            .tenantId("tenant-123")
                            .type(OptimizationType.ROUTE_OPTIMIZATION)
                            .build(),
                    OptimizationRequest.builder()
                            .tenantId("tenant-123")
                            .type(OptimizationType.DEMAND_FORECASTING)
                            .build()
            );

            List<OptimizationRequest> saved = repository.saveAll(requests);

            assertThat(saved).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Entity Mapping Tests")
    class EntityMappingTests {

        @Test
        @DisplayName("Should map request to entity correctly")
        void shouldMapToEntity() {
            Map<String, Object> params = Map.of(
                    "warehouseId", "WH-001",
                    "horizon", 30
            );

            OptimizationRequest request = OptimizationRequest.builder()
                    .tenantId("tenant-123")
                    .type(OptimizationType.INVENTORY_LEVELS)
                    .parameters(params)
                    .priority(7)
                    .build();

            request.startProcessing();

            OptimizationRequest saved = repository.save(request);

            assertThat(saved.getTenantId()).isEqualTo("tenant-123");
            assertThat(saved.getType()).isEqualTo(OptimizationType.INVENTORY_LEVELS);
            assertThat(saved.getStatus()).isEqualTo(OptimizationStatus.IN_PROGRESS);
        }
    }
}
