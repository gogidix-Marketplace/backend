package com.gogidix.ecommerce.inventory.application.mapper;

import com.gogidix.ecommerce.inventory.application.dto.*;
import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InventoryMapper Tests")
class InventoryMapperTest {

    private InventoryMapper mapper;

    @BeforeEach
    void setUp() { mapper = new InventoryMapper(); }

    @Test
    @DisplayName("Should map entity to response")
    void shouldMapToResponse() {
        Inventory entity = new Inventory("tenant-1");
        entity.setId("id-1");
        entity.setName("test-name");
        entity.setDescription("test-desc");
        entity.setType("test-type");
        entity.setIsActive(true);
        InventoryResponse response = mapper.toResponse(entity);
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo("id-1");
        assertThat(response.name()).isEqualTo("test-name");
        assertThat(response.description()).isEqualTo("test-desc");
        assertThat(response.type()).isEqualTo("test-type");
        assertThat(response.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should return null for null entity")
    void shouldReturnNullForNull() {
        assertThat(mapper.toResponse(null)).isNull();
    }

    @Test
    @DisplayName("Should map create request to entity")
    void shouldMapToEntity() {
        CreateInventoryRequest request = new CreateInventoryRequest("test", "desc", "type", null, null, null);
        Inventory entity = mapper.toEntity(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("test");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should update entity from request")
    void shouldUpdateFromRequest() {
        Inventory entity = new Inventory("tenant-1");
        entity.setName("old");
        UpdateInventoryRequest request = new UpdateInventoryRequest("new", "desc", null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("new");
        assertThat(entity.getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("Should skip null fields on update")
    void shouldSkipNullFields() {
        Inventory entity = new Inventory("tenant-1");
        entity.setName("original");
        entity.setDescription("original-desc");
        UpdateInventoryRequest request = new UpdateInventoryRequest(null, null, null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("original");
        assertThat(entity.getDescription()).isEqualTo("original-desc");
    }
}
