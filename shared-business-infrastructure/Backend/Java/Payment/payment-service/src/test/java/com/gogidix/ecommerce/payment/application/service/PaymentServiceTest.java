package com.gogidix.ecommerce.payment.application.service;

import com.gogidix.ecommerce.payment.application.dto.*;
import com.gogidix.ecommerce.payment.application.mapper.PaymentMapper;
import com.gogidix.ecommerce.payment.domain.model.Payment;
import com.gogidix.ecommerce.payment.domain.repository.PaymentRepository;
import com.gogidix.ecommerce.payment.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.payment.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentService Tests")
class PaymentServiceTest {

    @Mock private PaymentRepository repository;
    @Mock private PaymentMapper mapper;
    private PaymentService service;

    @BeforeEach
    void setUp() {
        service = new PaymentService(repository, mapper);
        RequestContextHolder.set(new RequestContext("tenant-1", null, null, "corr-1", null));
    }

    @AfterEach
    void tearDown() { RequestContextHolder.clear(); }

    @Test
    @DisplayName("Should get all")
    void shouldGetAll() {
        when(repository.findByTenantIdAndIsActive("tenant-1", true)).thenReturn(List.of());
        assertThat(service.getAll()).isEmpty();
    }

    @Test
    @DisplayName("Should get by ID")
    void shouldGetById() {
        Payment entity = new Payment("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(
            new PaymentResponse("id-1", "test", null, null, null, null, null, true, null, null));
        assertThat(service.getById("id-1")).isNotNull();
    }

    @Test
    @DisplayName("Should throw when not found")
    void shouldThrowNotFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById("x")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should create")
    void shouldCreate() {
        Payment entity = new Payment();
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new PaymentResponse("id-1", "test", null, null, null, null, null, true, null, null));
        assertThat(service.create(new CreatePaymentRequest("test", null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should update")
    void shouldUpdate() {
        Payment entity = new Payment("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new PaymentResponse("id-1", "updated", null, null, null, null, null, true, null, null));
        assertThat(service.update("id-1", new UpdatePaymentRequest("updated", null, null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should delete")
    void shouldDelete() {
        Payment entity = new Payment("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        service.delete("id-1");
        verify(repository).deleteById("id-1");
    }
}
