package com.gogidix.ecommerce.airfreight.application.service;

import com.gogidix.ecommerce.airfreight.application.dto.*;
import com.gogidix.ecommerce.airfreight.application.mapper.AirFreightMapper;
import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import com.gogidix.ecommerce.airfreight.domain.repository.AirFreightRepository;
import com.gogidix.ecommerce.airfreight.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.airfreight.shared.requestcontext.RequestContextHolder;
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
@DisplayName("AirFreightService Tests")
class AirFreightServiceTest {

    @Mock private AirFreightRepository repository;
    @Mock private AirFreightMapper mapper;
    private AirFreightService service;

    @BeforeEach
    void setUp() {
        service = new AirFreightService(repository, mapper);
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
        AirFreight entity = new AirFreight("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(
            new AirFreightResponse("id-1", "test", null, null, null, null, null, true, null, null));
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
        AirFreight entity = new AirFreight();
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new AirFreightResponse("id-1", "test", null, null, null, null, null, true, null, null));
        assertThat(service.create(new CreateAirFreightRequest("test", null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should update")
    void shouldUpdate() {
        AirFreight entity = new AirFreight("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new AirFreightResponse("id-1", "updated", null, null, null, null, null, true, null, null));
        assertThat(service.update("id-1", new UpdateAirFreightRequest("updated", null, null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should delete")
    void shouldDelete() {
        AirFreight entity = new AirFreight("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        service.delete("id-1");
        verify(repository).deleteById("id-1");
    }
}
