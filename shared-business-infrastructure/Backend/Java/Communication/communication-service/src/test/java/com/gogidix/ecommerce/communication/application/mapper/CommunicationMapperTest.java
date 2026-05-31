package com.gogidix.ecommerce.communication.application.mapper;

import com.gogidix.ecommerce.communication.application.dto.*;
import com.gogidix.ecommerce.communication.domain.model.Communication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CommunicationMapper Tests")
class CommunicationMapperTest {

    private CommunicationMapper mapper;

    @BeforeEach
    void setUp() { mapper = new CommunicationMapper(); }

    @Test
    @DisplayName("Should map entity to response")
    void shouldMapToResponse() {
        Communication entity = new Communication("tenant-1");
        entity.setId("id-1");
        entity.setName("test-name");
        entity.setDescription("test-desc");
        entity.setType("test-type");
        entity.setIsActive(true);
        CommunicationResponse response = mapper.toResponse(entity);
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
        CreateCommunicationRequest request = new CreateCommunicationRequest("test", "desc", "type", null, null, null);
        Communication entity = mapper.toEntity(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("test");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should update entity from request")
    void shouldUpdateFromRequest() {
        Communication entity = new Communication("tenant-1");
        entity.setName("old");
        UpdateCommunicationRequest request = new UpdateCommunicationRequest("new", "desc", null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("new");
        assertThat(entity.getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("Should skip null fields on update")
    void shouldSkipNullFields() {
        Communication entity = new Communication("tenant-1");
        entity.setName("original");
        entity.setDescription("original-desc");
        UpdateCommunicationRequest request = new UpdateCommunicationRequest(null, null, null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("original");
        assertThat(entity.getDescription()).isEqualTo("original-desc");
    }
}
