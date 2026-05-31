package com.gogidix.ecommerce.vendor.onboarding.interfaces.rest;

import com.gogidix.ecommerce.vendor.onboarding.domain.service.OnboardingService;
import com.gogidix.ecommerce.vendor.onboarding.domain.model.Onboarding;
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
class OnboardingControllerTest {
    @Mock private OnboardingService onboardingService;
    @InjectMocks private OnboardingController controller;

    private Onboarding buildEntity() { Onboarding e = new Onboarding(); e.setId("id1"); return e; }

    @Test void listAll() {
        when(onboardingService.findAll()).thenReturn(List.of(buildEntity()));
        assertThat(controller.listAll().getBody()).hasSize(1);
    }
    @Test void getById_found() {
        when(onboardingService.findById("id1")).thenReturn(buildEntity());
        assertThat(controller.getById("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getById_notFound() {
        when(onboardingService.findById("x")).thenReturn(null);
        assertThat(controller.getById("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void testCreate() {
        when(onboardingService.create(any())).thenAnswer(inv -> inv.getArgument(0));
        assertThat(controller.create(new Onboarding()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_found() {
        when(onboardingService.update(eq("id1"),any())).thenReturn(buildEntity());
        assertThat(controller.update("id1",new Onboarding()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void update_notFound() {
        when(onboardingService.update(eq("x"),any())).thenReturn(null);
        assertThat(controller.update("x",new Onboarding()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void delete() {
        assertThat(controller.delete("id1").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(onboardingService).delete("id1");
    }
}