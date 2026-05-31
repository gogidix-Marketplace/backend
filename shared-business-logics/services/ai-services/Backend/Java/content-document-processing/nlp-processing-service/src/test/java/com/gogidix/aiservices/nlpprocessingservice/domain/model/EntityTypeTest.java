package com.gogidix.aiservices.nlpprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("EntityType Domain Model Tests")
class EntityTypeTest {

    @Nested
    @DisplayName("Entity Type Tests")
    class EntityTypeValidationTests {

        @ParameterizedTest
        @EnumSource(EntityType.class)
        @DisplayName("Should have valid entity types")
        void shouldHaveValidEntityTypes(EntityType entityType) {
            assertThat(entityType).isNotNull();
            assertThat(entityType.name()).isIn(
                    "PERSON", "ORGANIZATION", "LOCATION", "DATE",
                    "TIME", "MONEY", "PERCENT", "NUMBER",
                    "EMAIL", "PHONE", "URL", "UNKNOWN"
            );
        }

        @Test
        @DisplayName("Should have PERSON entity")
        void shouldHavePersonEntity() {
            assertThat(EntityType.PERSON).isNotNull();
            assertThat(EntityType.PERSON.getDisplayName()).isEqualTo("Person");
            assertThat(EntityType.PERSON.getShortCode()).isEqualTo("PER");
        }

        @Test
        @DisplayName("Should have ORGANIZATION entity")
        void shouldHaveOrganizationEntity() {
            assertThat(EntityType.ORGANIZATION).isNotNull();
            assertThat(EntityType.ORGANIZATION.getDisplayName()).isEqualTo("Organization");
            assertThat(EntityType.ORGANIZATION.getShortCode()).isEqualTo("ORG");
        }

        @Test
        @DisplayName("Should have LOCATION entity")
        void shouldHaveLocationEntity() {
            assertThat(EntityType.LOCATION).isNotNull();
            assertThat(EntityType.LOCATION.getDisplayName()).isEqualTo("Location");
            assertThat(EntityType.LOCATION.getShortCode()).isEqualTo("LOC");
        }

        @Test
        @DisplayName("Should have DATE entity")
        void shouldHaveDateEntity() {
            assertThat(EntityType.DATE).isNotNull();
            assertThat(EntityType.DATE.getDisplayName()).isEqualTo("Date");
            assertThat(EntityType.DATE.getShortCode()).isEqualTo("DATE");
        }

        @Test
        @DisplayName("Should have MONEY entity")
        void shouldHaveMoneyEntity() {
            assertThat(EntityType.MONEY).isNotNull();
            assertThat(EntityType.MONEY.getDisplayName()).isEqualTo("Money");
            assertThat(EntityType.MONEY.getShortCode()).isEqualTo("MONEY");
        }
    }

    @Nested
    @DisplayName("Entity Parsing Tests")
    class EntityParsingTests {

        @Test
        @DisplayName("Should parse from short code")
        void shouldParseFromShortCode() {
            assertThat(EntityType.fromShortCode("PER")).isEqualTo(EntityType.PERSON);
            assertThat(EntityType.fromShortCode("ORG")).isEqualTo(EntityType.ORGANIZATION);
            assertThat(EntityType.fromShortCode("LOC")).isEqualTo(EntityType.LOCATION);
            assertThat(EntityType.fromShortCode("DATE")).isEqualTo(EntityType.DATE);
            assertThat(EntityType.fromShortCode("TIME")).isEqualTo(EntityType.TIME);
            assertThat(EntityType.fromShortCode("MONEY")).isEqualTo(EntityType.MONEY);
            assertThat(EntityType.fromShortCode("PERCENT")).isEqualTo(EntityType.PERCENT);
            assertThat(EntityType.fromShortCode("NUMBER")).isEqualTo(EntityType.NUMBER);
        }

        @Test
        @DisplayName("Should return UNKNOWN for invalid short code")
        void shouldReturnUnknownForInvalidShortCode() {
            assertThat(EntityType.fromShortCode("INVALID")).isEqualTo(EntityType.UNKNOWN);
            assertThat(EntityType.fromShortCode("XYZ")).isEqualTo(EntityType.UNKNOWN);
            assertThat(EntityType.fromShortCode(null)).isEqualTo(EntityType.UNKNOWN);
        }

        @Test
        @DisplayName("Should be case sensitive for short codes")
        void shouldBeCaseSensitiveForShortCodes() {
            assertThat(EntityType.fromShortCode("per")).isNotEqualTo(EntityType.PERSON);
            assertThat(EntityType.fromShortCode("PER")).isEqualTo(EntityType.PERSON);
        }
    }

    @Nested
    @DisplayName("Contact Entity Tests")
    class ContactEntityTests {

        @Test
        @DisplayName("Should have EMAIL entity")
        void shouldHaveEmailEntity() {
            assertThat(EntityType.EMAIL).isNotNull();
            assertThat(EntityType.EMAIL.getDisplayName()).isEqualTo("Email");
            assertThat(EntityType.EMAIL.getShortCode()).isEqualTo("EMAIL");
        }

        @Test
        @DisplayName("Should have PHONE entity")
        void shouldHavePhoneEntity() {
            assertThat(EntityType.PHONE).isNotNull();
            assertThat(EntityType.PHONE.getDisplayName()).isEqualTo("Phone");
            assertThat(EntityType.PHONE.getShortCode()).isEqualTo("PHONE");
        }

        @Test
        @DisplayName("Should have URL entity")
        void shouldHaveUrlEntity() {
            assertThat(EntityType.URL).isNotNull();
            assertThat(EntityType.URL.getDisplayName()).isEqualTo("URL");
            assertThat(EntityType.URL.getShortCode()).isEqualTo("URL");
        }

        @Test
        @DisplayName("Should parse contact entity codes")
        void shouldParseContactEntityCodes() {
            assertThat(EntityType.fromShortCode("EMAIL")).isEqualTo(EntityType.EMAIL);
            assertThat(EntityType.fromShortCode("PHONE")).isEqualTo(EntityType.PHONE);
            assertThat(EntityType.fromShortCode("URL")).isEqualTo(EntityType.URL);
        }
    }

    @Nested
    @DisplayName("Entity Properties Tests")
    class EntityPropertiesTests {

        @Test
        @DisplayName("Should have display names")
        void shouldHaveDisplayNames() {
            assertThat(EntityType.NUMBER.getDisplayName()).isEqualTo("Number");
            assertThat(EntityType.PERCENT.getDisplayName()).isEqualTo("Percentage");
            assertThat(EntityType.TIME.getDisplayName()).isEqualTo("Time");
        }

        @Test
        @DisplayName("Should have consistent short codes")
        void shouldHaveConsistentShortCodes() {
            for (EntityType type : EntityType.values()) {
                assertThat(type.getShortCode()).isNotNull();
                assertThat(type.getShortCode()).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Entity Comparison Tests")
    class EntityComparisonTests {

        @Test
        @DisplayName("Should be equal when same type")
        void shouldBeEqualWhenSameType() {
            assertThat(EntityType.PERSON).isEqualTo(EntityType.PERSON);
            assertThat(EntityType.LOCATION).isEqualTo(EntityType.LOCATION);
        }

        @Test
        @DisplayName("Should not be equal when different type")
        void shouldNotBeEqualWhenDifferentType() {
            assertThat(EntityType.PERSON).isNotEqualTo(EntityType.ORGANIZATION);
            assertThat(EntityType.DATE).isNotEqualTo(EntityType.MONEY);
        }

        @Test
        @DisplayName("Should have ordinal values")
        void shouldHaveOrdinalValues() {
            EntityType[] values = EntityType.values();

            assertThat(values).hasSize(10);
            assertThat(values[0].ordinal()).isLessThan(values[1].ordinal());
        }
    }
}
