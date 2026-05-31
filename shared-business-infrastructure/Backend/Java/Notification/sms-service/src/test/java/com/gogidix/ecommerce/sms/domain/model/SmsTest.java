package com.gogidix.ecommerce.sms.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Sms Domain Model Tests")
class SmsTest {

    @Test
    @DisplayName("Should create with tenant ID")
    void shouldCreateWithTenantId() {
        Sms entity = new Sms("tenant-1");
        assertThat(entity.getTenantId()).isEqualTo("tenant-1");
        assertThat(entity.getIsActive()).isTrue();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        Sms entity = new Sms();
        entity.setName("test-name");
        assertThat(entity.getName()).isEqualTo("test-name");
    }

    @Test
    @DisplayName("Should update timestamp")
    void shouldUpdateTimestamp() {
        Sms entity = new Sms("tenant-1");
        var before = entity.getUpdatedAt();
        entity.updateTimestamp();
        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("Should set isActive")
    void shouldSetIsActive() {
        Sms entity = new Sms();
        entity.setIsActive(false);
        assertThat(entity.getIsActive()).isFalse();
    }
}
