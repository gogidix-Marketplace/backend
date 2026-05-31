package com.gogidix.ecommerce.vendor.analytics.interfaces.rest;

import com.gogidix.ecommerce.vendor.analytics.domain.service.VendorAnalyticsService;
import com.gogidix.ecommerce.vendor.analytics.domain.model.VendorAnalytics;
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
class VendorAnalyticsControllerTest {
    @Mock private VendorAnalyticsService vendor_analyticsService;
    @InjectMocks private VendorAnalyticsController controller;

    private VendorAnalytics buildEntity() { VendorAnalytics e = new VendorAnalytics(); e.setId("id1"); return e; }

    @Test void listAll() {
        when(vendor_analyticsService.findAll()).thenReturn(List.of(buildEntity()));
        assertThat(controller.listAll().getBody()).hasSize(1);
    }
    @Test void getById_found() {
        when(vendor_analyticsService.findById("id1")).thenReturn(buildEntity());
        assertThat(controller.getById("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getById_notFound() {
        when(vendor_analyticsService.findById("x")).thenReturn(null);
        assertThat(controller.getById("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void testCreate() {
        when(vendor_analyticsService.create(any())).thenAnswer(inv -> inv.getArgument(0));
        assertThat(controller.create(new VendorAnalytics()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_found() {
        when(vendor_analyticsService.update(eq("id1"),any())).thenReturn(buildEntity());
        assertThat(controller.update("id1",new VendorAnalytics()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_notFound() {
        when(vendor_analyticsService.update(eq("x"),any())).thenReturn(null);
        assertThat(controller.update("x",new VendorAnalytics()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void delete() {
        assertThat(controller.delete("id1").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(vendor_analyticsService).delete("id1");
    }
}