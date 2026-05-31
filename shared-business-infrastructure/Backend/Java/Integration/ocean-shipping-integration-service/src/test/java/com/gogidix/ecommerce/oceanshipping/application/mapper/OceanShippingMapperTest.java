package com.gogidix.ecommerce.oceanshipping.application.mapper;

import com.gogidix.ecommerce.oceanshipping.application.dto.*;
import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OceanShippingMapper Tests")
class OceanShippingMapperTest {

    private OceanShippingMapper mapper;

    @BeforeEach
    void setUp() { mapper = new OceanShippingMapper(); }

    @Test
    @DisplayName("Should map entity to response")
    void shouldMapToResponse() {
        OceanShipping entity = new OceanShipping("tenant-1");
        entity.setId("id-1");
        entity.setName("test-name");
        entity.setDescription("test-desc");
        entity.setType("test-type");
        entity.setIsActive(true);
        OceanShippingResponse response = mapper.toResponse(entity);
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
        CreateOceanShippingRequest request = new CreateOceanShippingRequest("test", "desc", "type", null, null, null);
        OceanShipping entity = mapper.toEntity(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("test");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should update entity from request")
    void shouldUpdateFromRequest() {
        OceanShipping entity = new OceanShipping("tenant-1");
        entity.setName("old");
        UpdateOceanShippingRequest request = new UpdateOceanShippingRequest("new", "desc", null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("new");
        assertThat(entity.getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("Should skip null fields on update")
    void shouldSkipNullFields() {
        OceanShipping entity = new OceanShipping("tenant-1");
        entity.setName("original");
        entity.setDescription("original-desc");
        UpdateOceanShippingRequest request = new UpdateOceanShippingRequest(null, null, null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("original");
        assertThat(entity.getDescription()).isEqualTo("original-desc");
    }
}
