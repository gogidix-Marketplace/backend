package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model;

import com.gogidix.shared.servicediscovery.config.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserProfile Comprehensive Tests")
class UserProfileComprehensiveTest {

    @Nested
    @DisplayName("Equality")
    class EqualityTests {

        @Test
        void shouldBeEqualWithSameFields() {
            UserProfile p1 = UserProfile.builder().id("1").userId("u1").build();
            UserProfile p2 = UserProfile.builder().id("1").userId("u1").build();
            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        void shouldNotBeEqualWithDifferentId() {
            UserProfile p1 = UserProfile.builder().id("1").build();
            UserProfile p2 = UserProfile.builder().id("2").build();
            assertNotEquals(p1, p2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            UserProfile p = UserProfile.builder().id("1").build();
            assertNotEquals(null, p);
        }

        @Test
        void shouldNotBeEqualToDifferentType() {
            UserProfile p = UserProfile.builder().id("1").build();
            assertNotEquals("string", p);
        }

        @Test
        void shouldBeEqualToSelf() {
            UserProfile p = UserProfile.builder().id("1").build();
            assertEquals(p, p);
        }

        @Test
        void shouldHaveConsistentHashCode() {
            UserProfile p = UserProfile.builder().id("1").userId("u1").build();
            assertEquals(p.hashCode(), p.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        @Test
        void shouldIncludeFieldValues() {
            UserProfile p = UserProfile.builder()
                .id("p1")
                .userId("u1")
                .firstName("John")
                .build();
            String str = p.toString();
            assertNotNull(str);
            assertTrue(str.contains("p1"));
            assertTrue(str.contains("u1"));
            assertTrue(str.contains("John"));
        }
    }

    @Nested
    @DisplayName("Address Equality and ToString")
    class AddressTests {

        @Test
        void shouldBeEqualWithSameFields() {
            UserProfile.Address a1 = UserProfile.Address.builder()
                .street("1 St").city("NYC").state("NY").postalCode("10001").country("US").build();
            UserProfile.Address a2 = UserProfile.Address.builder()
                .street("1 St").city("NYC").state("NY").postalCode("10001").country("US").build();
            assertEquals(a1, a2);
            assertEquals(a1.hashCode(), a2.hashCode());
        }

        @Test
        void shouldNotBeEqualWithDifferentFields() {
            UserProfile.Address a1 = UserProfile.Address.builder().city("NYC").build();
            UserProfile.Address a2 = UserProfile.Address.builder().city("LA").build();
            assertNotEquals(a1, a2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            UserProfile.Address a = UserProfile.Address.builder().build();
            assertNotEquals(null, a);
        }

        @Test
        void shouldBeEqualToSelf() {
            UserProfile.Address a = UserProfile.Address.builder().build();
            assertEquals(a, a);
        }

        @Test
        void shouldHaveToString() {
            UserProfile.Address a = UserProfile.Address.builder().city("NYC").build();
            assertNotNull(a.toString());
        }
    }

    @Nested
    @DisplayName("UserPreferences Equality and ToString")
    class PreferencesTests {

        @Test
        void shouldBeEqualWithSameFields() {
            UserProfile.UserPreferences p1 = UserProfile.UserPreferences.builder()
                .language("en").timezone("UTC").theme("dark").currency("USD").dateFormat("MM/dd").build();
            UserProfile.UserPreferences p2 = UserProfile.UserPreferences.builder()
                .language("en").timezone("UTC").theme("dark").currency("USD").dateFormat("MM/dd").build();
            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        void shouldNotBeEqualWithDifferentFields() {
            UserProfile.UserPreferences p1 = UserProfile.UserPreferences.builder().language("en").build();
            UserProfile.UserPreferences p2 = UserProfile.UserPreferences.builder().language("es").build();
            assertNotEquals(p1, p2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            UserProfile.UserPreferences p = UserProfile.UserPreferences.builder().build();
            assertNotEquals(null, p);
        }

        @Test
        void shouldBeEqualToSelf() {
            UserProfile.UserPreferences p = UserProfile.UserPreferences.builder().build();
            assertEquals(p, p);
        }

        @Test
        void shouldHaveToString() {
            UserProfile.UserPreferences p = UserProfile.UserPreferences.builder().language("en").build();
            assertNotNull(p.toString());
        }

        @Test
        void shouldSetBooleanFields() {
            UserProfile.UserPreferences p = new UserProfile.UserPreferences();
            p.setEmailNotifications(true);
            p.setSmsNotifications(true);
            p.setPushNotifications(true);
            assertTrue(p.isEmailNotifications());
            assertTrue(p.isSmsNotifications());
            assertTrue(p.isPushNotifications());
        }
    }

    @Nested
    @DisplayName("Full UserProfile with all fields")
    class FullProfileTests {

        @Test
        void shouldBuildWithAllFieldsAndVerifyEquality() {
            LocalDateTime now = LocalDateTime.now();
            UserProfile p1 = UserProfile.builder()
                .id("1").userId("u1").tenantId(TenantId.of("t1"))
                .firstName("John").lastName("Doe").displayName("John Doe")
                .email("j@t.com").phoneNumber("+1").avatarUrl("a.png").bio("bio")
                .address(UserProfile.Address.builder().street("s").city("c").state("st")
                    .postalCode("pc").country("co").build())
                .preferences(UserProfile.UserPreferences.builder()
                    .language("en").timezone("UTC").theme("dark").currency("USD")
                    .dateFormat("MM/dd").emailNotifications(true).smsNotifications(false)
                    .pushNotifications(true).build())
                .metadata(Map.of("k1", "v1", "k2", 42))
                .createdAt(now).updatedAt(now)
                .build();

            UserProfile p2 = UserProfile.builder()
                .id("1").userId("u1").tenantId(TenantId.of("t1"))
                .firstName("John").lastName("Doe").displayName("John Doe")
                .email("j@t.com").phoneNumber("+1").avatarUrl("a.png").bio("bio")
                .address(UserProfile.Address.builder().street("s").city("c").state("st")
                    .postalCode("pc").country("co").build())
                .preferences(UserProfile.UserPreferences.builder()
                    .language("en").timezone("UTC").theme("dark").currency("USD")
                    .dateFormat("MM/dd").emailNotifications(true).smsNotifications(false)
                    .pushNotifications(true).build())
                .metadata(Map.of("k1", "v1", "k2", 42))
                .createdAt(now).updatedAt(now)
                .build();

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
            assertNotNull(p1.toString());
        }
    }
}
