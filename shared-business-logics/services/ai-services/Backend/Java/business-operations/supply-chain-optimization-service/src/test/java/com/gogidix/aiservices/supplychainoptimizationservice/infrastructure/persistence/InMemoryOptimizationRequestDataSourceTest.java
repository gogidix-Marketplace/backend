package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.persistence;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationStatus;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InMemoryOptimizationRequestDataSource Infrastructure Tests")
class InMemoryOptimizationRequestDataSourceTest {

    private InMemoryOptimizationRequestDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new InMemoryOptimizationRequestDataSource();
    }

    @Nested
    @DisplayName("Save Operations Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save entity successfully")
        void shouldSaveEntity() {
            OptimizationRequestEntity entity = new OptimizationRequestEntity();
            entity.setRequestId(UUID.randomUUID());
            entity.setTenantId("tenant-123");
            entity.setType(OptimizationType.INVENTORY_LEVELS);
            entity.setStatus(OptimizationStatus.PENDING);
            entity.setPriority(5);

            OptimizationRequestEntity saved = dataSource.save(entity);

            assertThat(saved).isNotNull();
            assertThat(saved.getTenantId()).isEqualTo("tenant-123");
        }

        @Test
        @DisplayName("Should update existing entity")
        void shouldUpdateEntity() {
            OptimizationRequestEntity entity = new OptimizationRequestEntity();
            entity.setRequestId(UUID.randomUUID());
            entity.setTenantId("tenant-123");
            entity.setType(OptimizationType.INVENTORY_LEVELS);
            entity.setStatus(OptimizationStatus.PENDING);
            dataSource.save(entity);

            entity.setStatus(OptimizationStatus.IN_PROGRESS);
            OptimizationRequestEntity updated = dataSource.save(entity);

            assertThat(updated.getStatus()).isEqualTo(OptimizationStatus.IN_PROGRESS);
        }
    }

    @Nested
    @DisplayName("Find Operations Tests")
    class FindTests {

        @Test
        @DisplayName("Should find by ID")
        void shouldFindById() {
            OptimizationRequestEntity entity = new OptimizationRequestEntity();
            entity.setRequestId(UUID.randomUUID());
            entity.setTenantId("tenant-123");
            entity.setType(OptimizationType.INVENTORY_LEVELS);
            entity.setStatus(OptimizationStatus.PENDING);
            dataSource.save(entity);

            Optional<OptimizationRequestEntity> found = dataSource.findById(entity.getId());

            assertThat(found).isPresent();
            assertThat(found.get().getTenantId()).isEqualTo("tenant-123");
        }

        @Test
        @DisplayName("Should return empty for non-existent ID")
        void shouldReturnEmptyForNonExistent() {
            Optional<OptimizationRequestEntity> found = dataSource.findById("non-existent");

            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should find by tenant ID")
        void shouldFindByTenantId() {
            OptimizationRequestEntity entity = new OptimizationRequestEntity();
            entity.setRequestId(UUID.randomUUID());
            entity.setTenantId("tenant-123");
            entity.setType(OptimizationType.INVENTORY_LEVELS);
            entity.setStatus(OptimizationStatus.PENDING);
            dataSource.save(entity);

            List<OptimizationRequestEntity> results = dataSource.findByTenantId("tenant-123");

            assertThat(results).hasSize(1);
        }

        @Test
        @DisplayName("Should find by status")
        void shouldFindByStatus() {
            OptimizationRequestEntity entity1 = new OptimizationRequestEntity();
            entity1.setRequestId(UUID.randomUUID());
            entity1.setTenantId("tenant-123");
            entity1.setType(OptimizationType.INVENTORY_LEVELS);
            entity1.setStatus(OptimizationStatus.PENDING);
            dataSource.save(entity1);

            OptimizationRequestEntity entity2 = new OptimizationRequestEntity();
            entity2.setRequestId(UUID.randomUUID());
            entity2.setTenantId("tenant-123");
            entity2.setType(OptimizationType.DEMAND_FORECASTING);
            entity2.setStatus(OptimizationStatus.COMPLETED);
            dataSource.save(entity2);

            List<OptimizationRequestEntity> pending = dataSource.findByStatus(OptimizationStatus.PENDING);
            List<OptimizationRequestEntity> completed = dataSource.findByStatus(OptimizationStatus.COMPLETED);

            assertThat(pending).hasSize(1);
            assertThat(completed).hasSize(1);
        }

        @Test
        @DisplayName("Should find pending requests")
        void shouldFindPendingRequests() {
            for (int i = 0; i < 5; i++) {
                OptimizationRequestEntity entity = new OptimizationRequestEntity();
                entity.setRequestId(UUID.randomUUID());
                entity.setTenantId("tenant-123");
                entity.setType(OptimizationType.INVENTORY_LEVELS);
                entity.setStatus(OptimizationStatus.PENDING);
                entity.setPriority(i);
                dataSource.save(entity);
            }

            List<OptimizationRequestEntity> pending = dataSource.findPendingRequests(3);

            assertThat(pending).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Delete Operations Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete entity")
        void shouldDeleteEntity() {
            OptimizationRequestEntity entity = new OptimizationRequestEntity();
            entity.setRequestId(UUID.randomUUID());
            entity.setTenantId("tenant-123");
            entity.setType(OptimizationType.INVENTORY_LEVELS);
            dataSource.save(entity);

            dataSource.delete(entity.getId());

            assertThat(dataSource.findById(entity.getId())).isEmpty();
        }
    }
}
