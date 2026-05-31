package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("LeadStatus Enum Tests")
class LeadStatusEnumTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(LeadStatus.class)
        @DisplayName("Should have all LeadStatus values")
        void shouldHaveAllValues(LeadStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 7 LeadStatus values")
        void shouldHave7Values() {
            assertThat(LeadStatus.values()).hasSize(7);
        }

        @Test
        @DisplayName("Should contain NEW")
        void shouldContainNew() {
            assertThat(LeadStatus.valueOf("NEW")).isEqualTo(LeadStatus.NEW);
        }

        @Test
        @DisplayName("Should contain CONTACTED")
        void shouldContainContacted() {
            assertThat(LeadStatus.valueOf("CONTACTED")).isEqualTo(LeadStatus.CONTACTED);
        }

        @Test
        @DisplayName("Should contain QUALIFIED")
        void shouldContainQualified() {
            assertThat(LeadStatus.valueOf("QUALIFIED")).isEqualTo(LeadStatus.QUALIFIED);
        }

        @Test
        @DisplayName("Should contain PROPOSAL_SENT")
        void shouldContainProposalSent() {
            assertThat(LeadStatus.valueOf("PROPOSAL_SENT")).isEqualTo(LeadStatus.PROPOSAL_SENT);
        }

        @Test
        @DisplayName("Should contain NEGOTIATION")
        void shouldContainNegotiation() {
            assertThat(LeadStatus.valueOf("NEGOTIATION")).isEqualTo(LeadStatus.NEGOTIATION);
        }

        @Test
        @DisplayName("Should contain CONVERTED")
        void shouldContainConverted() {
            assertThat(LeadStatus.valueOf("CONVERTED")).isEqualTo(LeadStatus.CONVERTED);
        }

        @Test
        @DisplayName("Should contain LOST")
        void shouldContainLost() {
            assertThat(LeadStatus.valueOf("LOST")).isEqualTo(LeadStatus.LOST);
        }
    }

    @Nested
    @DisplayName("fromString() Tests")
    class FromStringTests {

        @Test
        @DisplayName("Should parse NEW from string")
        void shouldParseNew() {
            assertThat(LeadStatus.fromString("new")).isEqualTo(LeadStatus.NEW);
        }

        @Test
        @DisplayName("Should parse CONTACTED from string")
        void shouldParseContacted() {
            assertThat(LeadStatus.fromString("contacted")).isEqualTo(LeadStatus.CONTACTED);
        }

        @Test
        @DisplayName("Should parse case insensitive")
        void shouldParseCaseInsensitive() {
            assertThat(LeadStatus.fromString("NEW")).isEqualTo(LeadStatus.NEW);
            assertThat(LeadStatus.fromString("New")).isEqualTo(LeadStatus.NEW);
            assertThat(LeadStatus.fromString("nEw")).isEqualTo(LeadStatus.NEW);
        }

        @ParameterizedTest
        @ValueSource(strings = {"new", "contacted", "qualified", "proposal_sent", "negotiation", "converted", "lost"})
        @DisplayName("Should parse all valid values")
        void shouldParseAllValidValues(String value) {
            assertThat(LeadStatus.fromString(value)).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception for invalid value")
        void shouldThrowForInvalidValue() {
            assertThatThrownBy(() -> LeadStatus.fromString("invalid"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unknown lead status");
        }
    }

    @Nested
    @DisplayName("isClosed() Tests")
    class IsClosedTests {

        @Test
        @DisplayName("Should return true for CONVERTED")
        void shouldReturnTrueForConverted() {
            assertThat(LeadStatus.CONVERTED.isClosed()).isTrue();
        }

        @Test
        @DisplayName("Should return true for LOST")
        void shouldReturnTrueForLost() {
            assertThat(LeadStatus.LOST.isClosed()).isTrue();
        }

        @Test
        @DisplayName("Should return false for NEW")
        void shouldReturnFalseForNew() {
            assertThat(LeadStatus.NEW.isClosed()).isFalse();
        }

        @Test
        @DisplayName("Should return false for CONTACTED")
        void shouldReturnFalseForContacted() {
            assertThat(LeadStatus.CONTACTED.isClosed()).isFalse();
        }

        @Test
        @DisplayName("Should return false for QUALIFIED")
        void shouldReturnFalseForQualified() {
            assertThat(LeadStatus.QUALIFIED.isClosed()).isFalse();
        }

        @Test
        @DisplayName("Should return false for PROPOSAL_SENT")
        void shouldReturnFalseForProposalSent() {
            assertThat(LeadStatus.PROPOSAL_SENT.isClosed()).isFalse();
        }

        @Test
        @DisplayName("Should return false for NEGOTIATION")
        void shouldReturnFalseForNegotiation() {
            assertThat(LeadStatus.NEGOTIATION.isClosed()).isFalse();
        }
    }

    @Nested
    @DisplayName("isActive() Tests")
    class IsActiveTests {

        @Test
        @DisplayName("Should return false for CONVERTED")
        void shouldReturnFalseForConverted() {
            assertThat(LeadStatus.CONVERTED.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should return false for LOST")
        void shouldReturnFalseForLost() {
            assertThat(LeadStatus.LOST.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should return true for NEW")
        void shouldReturnTrueForNew() {
            assertThat(LeadStatus.NEW.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should return true for CONTACTED")
        void shouldReturnTrueForContacted() {
            assertThat(LeadStatus.CONTACTED.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should return true for QUALIFIED")
        void shouldReturnTrueForQualified() {
            assertThat(LeadStatus.QUALIFIED.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should return true for PROPOSAL_SENT")
        void shouldReturnTrueForProposalSent() {
            assertThat(LeadStatus.PROPOSAL_SENT.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should return true for NEGOTIATION")
        void shouldReturnTrueForNegotiation() {
            assertThat(LeadStatus.NEGOTIATION.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("getValue() Tests")
    class GetValueTests {

        @Test
        @DisplayName("Should return 'new' for NEW")
        void shouldReturnNewForNew() {
            assertThat(LeadStatus.NEW.getValue()).isEqualTo("new");
        }

        @Test
        @DisplayName("Should return 'contacted' for CONTACTED")
        void shouldReturnContacted() {
            assertThat(LeadStatus.CONTACTED.getValue()).isEqualTo("contacted");
        }

        @Test
        @DisplayName("Should return 'qualified' for QUALIFIED")
        void shouldReturnQualified() {
            assertThat(LeadStatus.QUALIFIED.getValue()).isEqualTo("qualified");
        }

        @Test
        @DisplayName("Should return 'proposal_sent' for PROPOSAL_SENT")
        void shouldReturnProposalSent() {
            assertThat(LeadStatus.PROPOSAL_SENT.getValue()).isEqualTo("proposal_sent");
        }

        @Test
        @DisplayName("Should return 'negotiation' for NEGOTIATION")
        void shouldReturnNegotiation() {
            assertThat(LeadStatus.NEGOTIATION.getValue()).isEqualTo("negotiation");
        }

        @Test
        @DisplayName("Should return 'converted' for CONVERTED")
        void shouldReturnConverted() {
            assertThat(LeadStatus.CONVERTED.getValue()).isEqualTo("converted");
        }

        @Test
        @DisplayName("Should return 'lost' for LOST")
        void shouldReturnLost() {
            assertThat(LeadStatus.LOST.getValue()).isEqualTo("lost");
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        @ParameterizedTest
        @EnumSource(LeadStatus.class)
        @DisplayName("Should return value string for all statuses")
        void shouldReturnValueString(LeadStatus status) {
            assertThat(status.toString()).isEqualTo(status.getValue());
        }
    }
}
