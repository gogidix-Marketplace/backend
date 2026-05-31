package com.gogidix.shared.infrastructure.services.security.auth.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth Domain Event Tests")
class AuthEventTest {

    @Nested
    @DisplayName("UserLoggedInEvent")
    class LoggedInEventTests {

        @Test
        void shouldCreateWithAllFields() {
            UserLoggedInEvent event = new UserLoggedInEvent("u1", "john", "10.0.0.1");

            assertEquals("u1", event.getUserId());
            assertEquals("john", event.getUsername());
            assertEquals("10.0.0.1", event.getIpAddress());
            assertNotNull(event.getOccurredAt());
        }

        @Test
        void shouldBeEqualByUserId() {
            UserLoggedInEvent e1 = new UserLoggedInEvent("u1", "john", "10.0.0.1");
            UserLoggedInEvent e2 = new UserLoggedInEvent("u1", "john", "10.0.0.2");
            assertEquals(e1, e2);
            assertEquals(e1.hashCode(), e2.hashCode());
        }

        @Test
        void shouldNotBeEqualForDifferentUsers() {
            UserLoggedInEvent e1 = new UserLoggedInEvent("u1", "john", "10.0.0.1");
            UserLoggedInEvent e2 = new UserLoggedInEvent("u2", "jane", "10.0.0.1");
            assertNotEquals(e1, e2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            UserLoggedInEvent event = new UserLoggedInEvent("u1", "john", "10.0.0.1");
            assertNotEquals(null, event);
        }

        @Test
        void shouldNotBeEqualToDifferentType() {
            UserLoggedInEvent event = new UserLoggedInEvent("u1", "john", "10.0.0.1");
            assertNotEquals("string", event);
        }

        @Test
        void shouldBeEqualtoSelf() {
            UserLoggedInEvent event = new UserLoggedInEvent("u1", "john", "10.0.0.1");
            assertEquals(event, event);
        }
    }

    @Nested
    @DisplayName("UserLoggedOutEvent")
    class LoggedOutEventTests {

        @Test
        void shouldCreateWithAllFields() {
            UserLoggedOutEvent event = new UserLoggedOutEvent("u1", "10.0.0.1");

            assertEquals("u1", event.getUserId());
            assertEquals("10.0.0.1", event.getIpAddress());
            assertNotNull(event.getOccurredAt());
        }

        @Test
        void shouldBeEqualByUserId() {
            UserLoggedOutEvent e1 = new UserLoggedOutEvent("u1", "10.0.0.1");
            UserLoggedOutEvent e2 = new UserLoggedOutEvent("u1", "10.0.0.2");
            assertEquals(e1, e2);
            assertEquals(e1.hashCode(), e2.hashCode());
        }

        @Test
        void shouldNotBeEqualForDifferentUsers() {
            UserLoggedOutEvent e1 = new UserLoggedOutEvent("u1", "10.0.0.1");
            UserLoggedOutEvent e2 = new UserLoggedOutEvent("u2", "10.0.0.1");
            assertNotEquals(e1, e2);
        }
    }

    @Nested
    @DisplayName("UserRegisteredEvent")
    class RegisteredEventTests {

        @Test
        void shouldCreateWithAllFields() {
            UserRegisteredEvent event = new UserRegisteredEvent("u1", "john", "john@test.com", "tenant-1");

            assertEquals("u1", event.getUserId());
            assertEquals("john", event.getUsername());
            assertEquals("john@test.com", event.getEmail());
            assertEquals("tenant-1", event.getTenantId());
            assertNotNull(event.getOccurredAt());
        }

        @Test
        void shouldBeEqualByUserId() {
            UserRegisteredEvent e1 = new UserRegisteredEvent("u1", "john", "a@t.com", "t1");
            UserRegisteredEvent e2 = new UserRegisteredEvent("u1", "jane", "b@t.com", "t2");
            assertEquals(e1, e2);
            assertEquals(e1.hashCode(), e2.hashCode());
        }

        @Test
        void shouldNotBeEqualForDifferentUsers() {
            UserRegisteredEvent e1 = new UserRegisteredEvent("u1", "john", "a@t.com", "t1");
            UserRegisteredEvent e2 = new UserRegisteredEvent("u2", "john", "a@t.com", "t1");
            assertNotEquals(e1, e2);
        }
    }
}
