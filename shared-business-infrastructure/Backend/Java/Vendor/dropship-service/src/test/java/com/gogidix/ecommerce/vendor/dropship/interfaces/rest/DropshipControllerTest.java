package com.gogidix.ecommerce.vendor.dropship.interfaces.rest;

import com.gogidix.ecommerce.vendor.dropship.domain.service.DropshipService;
import com.gogidix.ecommerce.vendor.dropship.domain.model.Dropship;
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
class DropshipControllerTest {
    @Mock private DropshipService dropshipService;
    @InjectMocks private DropshipController controller;

    private Dropship buildEntity() { Dropship e = new Dropship(); e.setId("id1"); return e; }

    @Test void listAll() {
        when(dropshipService.findAll()).thenReturn(List.of(buildEntity()));
        assertThat(controller.listAll().getBody()).hasSize(1);
    }
    @Test void getById_found() {
        when(dropshipService.findById("id1")).thenReturn(buildEntity());
        assertThat(controller.getById("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getById_notFound() {
        when(dropshipService.findById("x")).thenReturn(null);
        assertThat(controller.getById("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void testCreate() {
        when(dropshipService.create(any())).thenAnswer(inv -> inv.getArgument(0));
        assertThat(controller.create(new Dropship()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_found() {
        when(dropshipService.update(eq("id1"),any())).thenReturn(buildEntity());
        assertThat(controller.update("id1",new Dropship()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_notFound() {
        when(dropshipService.update(eq("x"),any())).thenReturn(null);
        assertThat(controller.update("x",new Dropship()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void delete() {
        assertThat(controller.delete("id1").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(dropshipService).delete("id1");
    }
}