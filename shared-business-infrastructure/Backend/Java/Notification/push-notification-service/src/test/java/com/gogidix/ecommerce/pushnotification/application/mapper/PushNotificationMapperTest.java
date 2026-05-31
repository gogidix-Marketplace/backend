package com.gogidix.ecommerce.pushnotification.application.mapper;

import com.gogidix.ecommerce.pushnotification.application.dto.*;
import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PushNotificationMapper Tests")
class PushNotificationMapperTest {

    private PushNotificationMapper mapper;

    @BeforeEach
    void setUp() { mapper = new PushNotificationMapper(); }

    @Test
    @DisplayName("Should map entity to response")
    void shouldMapToResponse() {
        PushNotification entity = new PushNotification("tenant-1");
        entity.setId("id-1");
        entity.setName("test-name");
        entity.setDescription("test-desc");
        entity.setType("test-type");
        entity.setIsActive(true);
        PushNotificationResponse response = mapper.toResponse(entity);
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
        CreatePushNotificationRequest request = new CreatePushNotificationRequest("test", "desc", "type", null, null, null);
        PushNotification entity = mapper.toEntity(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("test");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should update entity from request")
    void shouldUpdateFromRequest() {
        PushNotification entity = new PushNotification("tenant-1");
        entity.setName("old");
        UpdatePushNotificationRequest request = new UpdatePushNotificationRequest("new", "desc", null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("new");
        assertThat(entity.getDescription()).isEqualTo("desc");
    }

    @Test
    @DisplayName("Should skip null fields on update")
    void shouldSkipNullFields() {
        PushNotification entity = new PushNotification("tenant-1");
        entity.setName("original");
        entity.setDescription("original-desc");
        UpdatePushNotificationRequest request = new UpdatePushNotificationRequest(null, null, null, null, null, null, null);
        mapper.updateFromRequest(entity, request);
        assertThat(entity.getName()).isEqualTo("original");
        assertThat(entity.getDescription()).isEqualTo("original-desc");
    }
}
