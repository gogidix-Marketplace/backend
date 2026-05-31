package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.persistence;

import com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate.Dashboard;
import com.gogidix.aiservices.aianalyticsdashboard.domain.model.Widget;
import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Dashboard Repository Infrastructure Tests")
class DashboardRepositoryImplTest {

    @Mock
    private DashboardDataSource dataSource;

    @InjectMocks
    private DashboardRepositoryImpl repository;

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String DASHBOARD_ID = "660e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Save Operations")
    class SaveTests {

        @Test
        @DisplayName("Should save new dashboard")
        void shouldSaveNewDashboard() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);

            when(dataSource.save(any(DashboardEntity.class))).thenReturn(new DashboardEntity());

            Dashboard saved = repository.save(dashboard);

            assertThat(saved).isNotNull();
            verify(dataSource).save(any(DashboardEntity.class));
        }

        @Test
        @DisplayName("Should update existing dashboard")
        void shouldUpdateDashboard() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            dashboard.setDescription("Updated");

            when(dataSource.save(any(DashboardEntity.class))).thenReturn(new DashboardEntity());

            repository.save(dashboard);

            verify(dataSource).save(any(DashboardEntity.class));
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindTests {

        @Test
        @DisplayName("Should find dashboard by ID")
        void shouldFindById() {
            DashboardEntity entity = createDashboardEntity();
            when(dataSource.findById(DASHBOARD_ID)).thenReturn(Optional.of(entity));

            Optional<Dashboard> result = repository.findById(DASHBOARD_ID);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Test Dashboard");
            verify(dataSource).findById(DASHBOARD_ID);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(dataSource.findById(DASHBOARD_ID)).thenReturn(Optional.empty());

            Optional<Dashboard> result = repository.findById(DASHBOARD_ID);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should find dashboards by user ID")
        void shouldFindByUserId() {
            List<DashboardEntity> entities = List.of(
                    createDashboardEntity(),
                    createDashboardEntity()
            );
            when(dataSource.findByUserId(USER_ID)).thenReturn(entities);

            List<Dashboard> results = repository.findByUserId(USER_ID);

            assertThat(results).hasSize(2);
            verify(dataSource).findByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should find public dashboards")
        void shouldFindPublicDashboards() {
            List<DashboardEntity> entities = List.of(createDashboardEntity());
            when(dataSource.findPublicDashboards()).thenReturn(entities);

            List<Dashboard> results = repository.findPublicDashboards();

            assertThat(results).hasSize(1);
            verify(dataSource).findPublicDashboards();
        }

        @Test
        @DisplayName("Should find dashboards updated after timestamp")
        void shouldFindByUpdatedAfter() {
            Instant timestamp = Instant.now();
            List<DashboardEntity> entities = List.of(createDashboardEntity());
            when(dataSource.findUpdatedAfter(timestamp)).thenReturn(entities);

            List<Dashboard> results = repository.findUpdatedAfter(timestamp);

            assertThat(results).hasSize(1);
            verify(dataSource).findUpdatedAfter(timestamp);
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteTests {

        @Test
        @DisplayName("Should delete dashboard by ID")
        void shouldDeleteById() {
            doNothing().when(dataSource).delete(DASHBOARD_ID);

            repository.delete(DASHBOARD_ID);

            verify(dataSource).delete(DASHBOARD_ID);
        }
    }

    @Nested
    @DisplayName("Entity Mapping Tests")
    class MappingTests {

        @Test
        @DisplayName("Should map entity to domain correctly")
        void shouldMapEntityToDomain() {
            DashboardEntity entity = createDashboardEntity();
            entity.setWidgets(List.of(
                    createWidgetEntity("widget-1", "Chart 1", WidgetType.CHART),
                    createWidgetEntity("widget-2", "Metric 1", WidgetType.METRIC)
            ));

            when(dataSource.findById(DASHBOARD_ID)).thenReturn(Optional.of(entity));

            Optional<Dashboard> result = repository.findById(DASHBOARD_ID);

            assertThat(result).isPresent();
            assertThat(result.get().getWidgetCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should map domain to entity correctly")
        void shouldMapDomainToEntity() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            Widget widget = Widget.builder()
                    .widgetId("widget-1")
                    .type(WidgetType.CHART)
                    .title("Chart")
                    .dataSource("data")
                    .build();
            dashboard.addWidget(widget);

            when(dataSource.save(any(DashboardEntity.class))).thenReturn(new DashboardEntity());

            repository.save(dashboard);

            verify(dataSource).save(argThat(entity ->
                    entity.getWidgets() != null && entity.getWidgets().size() == 1
            ));
        }
    }

    private DashboardEntity createDashboardEntity() {
        DashboardEntity entity = new DashboardEntity();
        entity.setDashboardId(DASHBOARD_ID);
        entity.setUserId(USER_ID);
        entity.setName("Test Dashboard");
        entity.setDescription("Test description");
        entity.setRefreshInterval(300);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setPublic(false);
        entity.setTheme("default");
        entity.setWidgets(List.of());
        return entity;
    }

    private WidgetEntity createWidgetEntity(String id, String title, WidgetType type) {
        WidgetEntity entity = new WidgetEntity();
        entity.setWidgetId(id);
        entity.setType(type);
        entity.setTitle(title);
        entity.setDataSource("data-source");
        entity.setPosition(0);
        return entity;
    }
}
