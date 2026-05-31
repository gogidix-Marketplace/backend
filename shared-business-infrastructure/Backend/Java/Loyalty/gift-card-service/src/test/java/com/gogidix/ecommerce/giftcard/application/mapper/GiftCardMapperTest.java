package com.gogidix.ecommerce.giftcard.application.mapper;

import com.gogidix.ecommerce.giftcard.application.dto.*;
import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("GiftCardMapper Tests")
class GiftCardMapperTest {

    private GiftCardMapper mapper;

    @BeforeEach
    void setUp() { mapper = new GiftCardMapper(); }

    @Test
    @DisplayName("Should map entity to response")
    void shouldMapToResponse() {
        GiftCard entity = new GiftCard("tenant-1");
        entity.setId("id-1");
        entity.setName("test-name");
        entity.setDescription("test-desc");
        entity.setType("test-type");
        entity.setIsActive(true);
        GiftCardResponse response = mapper.toResponse(entity);
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
        CreateGiftCardRequest request = new CreateGiftCardRequest("test", "desc", "type", null, null, null);
        GiftCard entity = mapper.toEntity(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("test");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should update entity from request")
    void shouldUpdateFromRequest() {
        GiftCard entity = new GiftCard("tenant-1");
        entity.setName("old");
        UpdateGiftCardRequest request = new UpdateGiftCardRequest("new", "desc", null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("new");
        assertThat(entity.getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("Should skip null fields on update")
    void shouldSkipNullFields() {
        GiftCard entity = new GiftCard("tenant-1");
        entity.setName("original");
        entity.setDescription("original-desc");
        UpdateGiftCardRequest request = new UpdateGiftCardRequest(null, null, null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("original");
        assertThat(entity.getDescription()).isEqualTo("original-desc");
    }
}
