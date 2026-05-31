package com.gogidix.ecommerce.vendor.interfaces.rest;

import com.gogidix.ecommerce.vendor.domain.service.VendorService;
import com.gogidix.ecommerce.vendor.domain.model.Vendor;
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
class VendorControllerTest {
    @Mock private VendorService vendorService;
    @InjectMocks private VendorController controller;

    private Vendor buildEntity() { Vendor e = new Vendor(); e.setId("id1"); return e; }

    @Test void listAll() {
        when(vendorService.findAll()).thenReturn(List.of(buildEntity()));
        assertThat(controller.listAll().getBody()).hasSize(1);
    }
    @Test void getById_found() {
        when(vendorService.findById("id1")).thenReturn(buildEntity());
        assertThat(controller.getById("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getById_notFound() {
        when(vendorService.findById("x")).thenReturn(null);
        assertThat(controller.getById("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void testCreate() {
        when(vendorService.create(any())).thenAnswer(inv -> inv.getArgument(0));
        assertThat(controller.create(new Vendor()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_found() {
        when(vendorService.update(eq("id1"),any())).thenReturn(buildEntity());
        assertThat(controller.update("id1",new Vendor()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_notFound() {
        when(vendorService.update(eq("x"),any())).thenReturn(null);
        assertThat(controller.update("x",new Vendor()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void delete() {
        assertThat(controller.delete("id1").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(vendorService).delete("id1");
    }
}