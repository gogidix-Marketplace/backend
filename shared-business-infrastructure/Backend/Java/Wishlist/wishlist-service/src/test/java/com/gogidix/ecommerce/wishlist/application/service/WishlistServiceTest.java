package com.gogidix.ecommerce.wishlist.application.service;

import com.gogidix.ecommerce.wishlist.application.dto.*;
import com.gogidix.ecommerce.wishlist.application.mapper.WishlistMapper;
import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import com.gogidix.ecommerce.wishlist.domain.repository.WishlistRepository;
import com.gogidix.ecommerce.wishlist.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.wishlist.shared.requestcontext.RequestContextHolder;
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
@DisplayName("WishlistService Tests")
class WishlistServiceTest {

    @Mock private WishlistRepository repository;
    @Mock private WishlistMapper mapper;
    private WishlistService service;

    @BeforeEach
    void setUp() {
        service = new WishlistService(repository, mapper);
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
        Wishlist entity = new Wishlist("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(
            new WishlistResponse("id-1", "test", null, null, null, null, null, true, null, null));
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
        Wishlist entity = new Wishlist();
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new WishlistResponse("id-1", "test", null, null, null, null, null, true, null, null));
        assertThat(service.create(new CreateWishlistRequest("test", null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should update")
    void shouldUpdate() {
        Wishlist entity = new Wishlist("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toResponse(any())).thenReturn(
            new WishlistResponse("id-1", "updated", null, null, null, null, null, true, null, null));
        assertThat(service.update("id-1", new UpdateWishlistRequest("updated", null, null, null, null, null, null))).isNotNull();
    }

    @Test
    @DisplayName("Should delete")
    void shouldDelete() {
        Wishlist entity = new Wishlist("tenant-1");
        entity.setId("id-1");
        when(repository.findById("id-1")).thenReturn(Optional.of(entity));
        service.delete("id-1");
        verify(repository).deleteById("id-1");
    }
}
