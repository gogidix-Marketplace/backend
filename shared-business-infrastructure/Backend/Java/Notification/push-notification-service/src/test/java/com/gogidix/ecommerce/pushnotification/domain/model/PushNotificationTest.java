package com.gogidix.ecommerce.pushnotification.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PushNotification Domain Model Tests")
class PushNotificationTest {

    @Test
    @DisplayName("Should create with tenant ID")
    void shouldCreateWithTenantId() {
        PushNotification entity = new PushNotification("tenant-1");
        assertThat(entity.getTenantId()).isEqualTo("tenant-1");
        assertThat(entity.getIsActive()).isTrue();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        PushNotification entity = new PushNotification();
        entity.setName("test-name");
        assertThat(entity.getName()).isEqualTo("test-name");
    }

    @Test
    @DisplayName("Should update timestamp")
    void shouldUpdateTimestamp() {
        PushNotification entity = new PushNotification("tenant-1");
        var before = entity.getUpdatedAt();
        entity.updateTimestamp();
        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("Should set isActive")
    void shouldSetIsActive() {
        PushNotification entity = new PushNotification();
        entity.setIsActive(false);
        assertThat(entity.getIsActive()).isFalse();
    }
}
