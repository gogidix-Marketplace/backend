package com.gogidix.ecommerce.vendor.dashboard.domain.service;

import com.gogidix.ecommerce.vendor.dashboard.domain.repository.VendorDashboardRepository;
import com.gogidix.ecommerce.vendor.dashboard.domain.model.VendorDashboard;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorDashboardServiceTest {
    @Mock private VendorDashboardRepository repository;
    @InjectMocks private VendorDashboardService service;

    @BeforeEach void setUp() {
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").metadata(java.util.Map.of()).build());
    }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private VendorDashboard createEntity() {
        VendorDashboard e = new VendorDashboard();
        e.setId("id1"); e.setTenantId("t1"); e.setVendorId("v1");
        e.setName("test"); e.setCreatedAt(Instant.now()); e.setUpdatedAt(Instant.now());
        return e;
    }

    @Test void findAll() {
        when(repository.findByTenantId("t1")).thenReturn(List.of(createEntity()));
        List<VendorDashboard> result = service.findAll();
        assertThat(result).hasSize(1);
        verify(repository).findByTenantId("t1");
    }

    @Test void findById_found() {
        when(repository.findByTenantIdAndId("t1", "id1")).thenReturn(Optional.of(createEntity()));
        VendorDashboard result = service.findById("id1");
        assertThat(result).isNotNull(); assertThat(result.getId()).isEqualTo("id1");
    }

    @Test void findById_notFound() {
        when(repository.findByTenantIdAndId("t1", "missing")).thenReturn(Optional.empty());
        assertThat(service.findById("missing")).isNull();
    }

    @Test void create() {
        VendorDashboard entity = new VendorDashboard(); entity.setName("new");
        when(repository.save(any(VendorDashboard.class))).thenAnswer(inv -> inv.getArgument(0));
        VendorDashboard result = service.create(entity);
        assertThat(result.getTenantId()).isEqualTo("t1");
        assertThat(result.getCreatedAt()).isNotNull();
        verify(repository).save(any(VendorDashboard.class));
    }

    @Test void update_found() {
        VendorDashboard existing = createEntity();
        when(repository.findByTenantIdAndId("t1", "id1")).thenReturn(Optional.of(existing));
        when(repository.save(any(VendorDashboard.class))).thenAnswer(inv -> inv.getArgument(0));
        VendorDashboard updated = new VendorDashboard(); updated.setName("updated");
        VendorDashboard result = service.update("id1", updated);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test void update_notFound() {
        when(repository.findByTenantIdAndId("t1", "missing")).thenReturn(Optional.empty());
        assertThat(service.update("missing", new VendorDashboard())).isNull();
    }

    @Test void delete_found() {
        when(repository.findByTenantIdAndId("t1", "id1")).thenReturn(Optional.of(createEntity()));
        service.delete("id1");
        verify(repository).delete(any(VendorDashboard.class));
    }

    @Test void delete_notFound() {
        when(repository.findByTenantIdAndId("t1", "missing")).thenReturn(Optional.empty());
        service.delete("missing");
        verify(repository, never()).delete(any());
    }
}