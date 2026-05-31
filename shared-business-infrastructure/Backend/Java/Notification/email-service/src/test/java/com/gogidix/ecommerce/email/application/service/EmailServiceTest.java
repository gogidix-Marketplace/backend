package com.gogidix.ecommerce.email.application.service;

import com.gogidix.ecommerce.email.application.dto.*;
import com.gogidix.ecommerce.email.application.mapper.EmailMapper;
import com.gogidix.ecommerce.email.domain.model.Email;
import com.gogidix.ecommerce.email.domain.repository.EmailRepository;
import com.gogidix.ecommerce.email.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.email.shared.requestcontext.RequestContextHolder;
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
@DisplayName("EmailService Tests")
class EmailServiceTest {

    @Mock private EmailRepository repository;
    @Mock private EmailMapper mapper;
    private EmailService service;

    @BeforeEach
    void setUp() {
        service = new EmailService(repository, mapper);
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
        Email entity = new Email("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(
            new EmailResponse("id-1", "test", null, null, null, null, null, true, null, null));
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
        Email entity = new Email();
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new EmailResponse("id-1", "test", null, null, null, null, null, true, null, null));
        assertThat(service.create(new CreateEmailRequest("test", null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should update")
    void shouldUpdate() {
        Email entity = new Email("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new EmailResponse("id-1", "updated", null, null, null, null, null, true, null, null));
        assertThat(service.update("id-1", new UpdateEmailRequest("updated", null, null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should delete")
    void shouldDelete() {
        Email entity = new Email("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        service.delete("id-1");
        verify(repository).deleteById("id-1");
    }
}
