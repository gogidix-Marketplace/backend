package com.gogidix.ecommerce.loyalty.application.mapper;

import com.gogidix.ecommerce.loyalty.application.dto.*;
import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LoyaltyMapper Tests")
class LoyaltyMapperTest {

    private LoyaltyMapper mapper;

    @BeforeEach
    void setUp() { mapper = new LoyaltyMapper(); }

    @Test
    @DisplayName("Should map entity to response")
    void shouldMapToResponse() {
        Loyalty entity = new Loyalty("tenant-1");
        entity.setId("id-1");
        entity.setName("test-name");
        entity.setDescription("test-desc");
        entity.setType("test-type");
        entity.setIsActive(true);
        LoyaltyResponse response = mapper.toResponse(entity);
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
        CreateLoyaltyRequest request = new CreateLoyaltyRequest("test", "desc", "type", null, null, null);
        Loyalty entity = mapper.toEntity(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("test");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should update entity from request")
    void shouldUpdateFromRequest() {
        Loyalty entity = new Loyalty("tenant-1");
        entity.setName("old");
        UpdateLoyaltyRequest request = new UpdateLoyaltyRequest("new", "desc", null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("new");
        assertThat(entity.getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("Should skip null fields on update")
    void shouldSkipNullFields() {
        Loyalty entity = new Loyalty("tenant-1");
        entity.setName("original");
        entity.setDescription("original-desc");
        UpdateLoyaltyRequest request = new UpdateLoyaltyRequest(null, null, null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("original");
        assertThat(entity.getDescription()).isEqualTo("original-desc");
    }
}
