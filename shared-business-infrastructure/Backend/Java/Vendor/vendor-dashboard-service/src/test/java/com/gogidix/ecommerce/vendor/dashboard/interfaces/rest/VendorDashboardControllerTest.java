package com.gogidix.ecommerce.vendor.dashboard.interfaces.rest;

import com.gogidix.ecommerce.vendor.dashboard.domain.service.VendorDashboardService;
import com.gogidix.ecommerce.vendor.dashboard.domain.model.VendorDashboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorDashboardControllerTest {
    @Mock private VendorDashboardService vendor_dashboardService;
    @InjectMocks private VendorDashboardController controller;

    private VendorDashboard buildEntity() { VendorDashboard e = new VendorDashboard(); e.setId("id1"); return e; }

    @Test void listAll() {
        when(vendor_dashboardService.findAll()).thenReturn(List.of(buildEntity()));
        assertThat(controller.listAll().getBody()).hasSize(1);
    }
    @Test void getById_found() {
        when(vendor_dashboardService.findById("id1")).thenReturn(buildEntity());
        assertThat(controller.getById("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getById_notFound() {
        when(vendor_dashboardService.findById("x")).thenReturn(null);
        assertThat(controller.getById("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void testCreate() {
        when(vendor_dashboardService.create(any())).thenAnswer(inv -> inv.getArgument(0));
        assertThat(controller.create(new VendorDashboard()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_found() {
        when(vendor_dashboardService.update(eq("id1"),any())).thenReturn(buildEntity());
        assertThat(controller.update("id1",new VendorDashboard()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_notFound() {
        when(vendor_dashboardService.update(eq("x"),any())).thenReturn(null);
        assertThat(controller.update("x",new VendorDashboard()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void delete() {
        assertThat(controller.delete("id1").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(vendor_dashboardService).delete("id1");
    }
}