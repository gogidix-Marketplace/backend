package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Command Tests")
class CommandTest {

    @Test
    void shouldBuildCreateCommand() {
        CreateTenantCommand cmd = CreateTenantCommand.builder()
            .name("Test").domain("test.com").logoUrl("logo.png")
            .primaryContactEmail("e@t.com").primaryContactName("Admin")
            .maxUsers(100L).maxStorageGB(50L)
            .settings(Map.of("k","v")).features(Map.of("f",true))
            .build();
        assertEquals("Test", cmd.getName());
        assertEquals("test.com", cmd.getDomain());
        assertEquals(100L, cmd.getMaxUsers());
    }

    @Test
    void shouldSetCreateCommandFields() {
        CreateTenantCommand cmd = new CreateTenantCommand();
        cmd.setName("Test");
        cmd.setDomain("test.com");
        assertEquals("Test", cmd.getName());
    }

    @Test
    void shouldCreateCommandEquality() {
        CreateTenantCommand c1 = CreateTenantCommand.builder().name("Test").domain("test.com").build();
        CreateTenantCommand c2 = CreateTenantCommand.builder().name("Test").domain("test.com").build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void shouldNotBeEqualToNull() {
        assertNotEquals(null, CreateTenantCommand.builder().build());
    }

    @Test
    void shouldHaveToString() {
        assertNotNull(CreateTenantCommand.builder().name("Test").build().toString());
    }

    @Test
    void shouldBuildUpdateCommand() {
        UpdateTenantCommand cmd = UpdateTenantCommand.builder()
            .id("t1").name("Updated")
            .primaryContactEmail("new@t.com").primaryContactName("New Admin")
            .maxUsers(200L).maxStorageGB(100L)
            .settings(Map.of("k","v")).features(Map.of("f",true))
            .build();
        assertEquals("t1", cmd.getId());
        assertEquals("Updated", cmd.getName());
    }

    @Test
    void shouldUpdateCommandEquality() {
        UpdateTenantCommand c1 = UpdateTenantCommand.builder().id("t1").build();
        UpdateTenantCommand c2 = UpdateTenantCommand.builder().id("t1").build();
        assertEquals(c1, c2);
    }

    @Test
    void shouldNotUpdateCommandEqualToNull() {
        assertNotEquals(null, UpdateTenantCommand.builder().build());
    }
}
