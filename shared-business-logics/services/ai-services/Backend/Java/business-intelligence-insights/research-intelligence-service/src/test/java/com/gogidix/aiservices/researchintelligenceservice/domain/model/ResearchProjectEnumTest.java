package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ResearchProject Enum Tests")
class ResearchProjectEnumTest {

    @Nested
    @DisplayName("ProjectStatus Enum Tests")
    class ProjectStatusTests {

        @ParameterizedTest
        @EnumSource(ResearchProject.ProjectStatus.class)
        @DisplayName("Should have all ProjectStatus values")
        void shouldHaveAllValues(ResearchProject.ProjectStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 6 ProjectStatus values")
        void shouldHave6Values() {
            assertThat(ResearchProject.ProjectStatus.values()).hasSize(6);
        }

        @Test
        @DisplayName("Should contain INITIATED")
        void shouldContainInitiated() {
            assertThat(ResearchProject.ProjectStatus.valueOf("INITIATED"))
                    .isEqualTo(ResearchProject.ProjectStatus.INITIATED);
        }

        @Test
        @DisplayName("Should contain IN_PROGRESS")
        void shouldContainInProgress() {
            assertThat(ResearchProject.ProjectStatus.valueOf("IN_PROGRESS"))
                    .isEqualTo(ResearchProject.ProjectStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("Should contain ON_HOLD")
        void shouldContainOnHold() {
            assertThat(ResearchProject.ProjectStatus.valueOf("ON_HOLD"))
                    .isEqualTo(ResearchProject.ProjectStatus.ON_HOLD);
        }

        @Test
        @DisplayName("Should contain COMPLETED")
        void shouldContainCompleted() {
            assertThat(ResearchProject.ProjectStatus.valueOf("COMPLETED"))
                    .isEqualTo(ResearchProject.ProjectStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should contain CANCELLED")
        void shouldContainCancelled() {
            assertThat(ResearchProject.ProjectStatus.valueOf("CANCELLED"))
                    .isEqualTo(ResearchProject.ProjectStatus.CANCELLED);
        }

        @Test
        @DisplayName("Should contain ARCHIVED")
        void shouldContainArchived() {
            assertThat(ResearchProject.ProjectStatus.valueOf("ARCHIVED"))
                    .isEqualTo(ResearchProject.ProjectStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("ResearchDomain Enum Tests")
    class ResearchDomainTests {

        @ParameterizedTest
        @EnumSource(ResearchProject.ResearchDomain.class)
        @DisplayName("Should have all ResearchDomain values")
        void shouldHaveAllValues(ResearchProject.ResearchDomain domain) {
            assertThat(domain).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 11 ResearchDomain values")
        void shouldHave11Values() {
            assertThat(ResearchProject.ResearchDomain.values()).hasSize(11);
        }

        @Test
        @DisplayName("Should contain DATA_SCIENCE")
        void shouldContainDataScience() {
            assertThat(ResearchProject.ResearchDomain.valueOf("DATA_SCIENCE"))
                    .isEqualTo(ResearchProject.ResearchDomain.DATA_SCIENCE);
        }

        @Test
        @DisplayName("Should contain MACHINE_LEARNING")
        void shouldContainMachineLearning() {
            assertThat(ResearchProject.ResearchDomain.valueOf("MACHINE_LEARNING"))
                    .isEqualTo(ResearchProject.ResearchDomain.MACHINE_LEARNING);
        }

        @Test
        @DisplayName("Should contain NATURAL_LANGUAGE_PROCESSING")
        void shouldContainNLP() {
            assertThat(ResearchProject.ResearchDomain.valueOf("NATURAL_LANGUAGE_PROCESSING"))
                    .isEqualTo(ResearchProject.ResearchDomain.NATURAL_LANGUAGE_PROCESSING);
        }

        @Test
        @DisplayName("Should contain COMPUTER_VISION")
        void shouldContainComputerVision() {
            assertThat(ResearchProject.ResearchDomain.valueOf("COMPUTER_VISION"))
                    .isEqualTo(ResearchProject.ResearchDomain.COMPUTER_VISION);
        }

        @Test
        @DisplayName("Should contain ROBOTICS")
        void shouldContainRobotics() {
            assertThat(ResearchProject.ResearchDomain.valueOf("ROBOTICS"))
                    .isEqualTo(ResearchProject.ResearchDomain.ROBOTICS);
        }

        @Test
        @DisplayName("Should contain BIOTECHNOLOGY")
        void shouldContainBiotechnology() {
            assertThat(ResearchProject.ResearchDomain.valueOf("BIOTECHNOLOGY"))
                    .isEqualTo(ResearchProject.ResearchDomain.BIOTECHNOLOGY);
        }

        @Test
        @DisplayName("Should contain QUANTUM_COMPUTING")
        void shouldContainQuantumComputing() {
            assertThat(ResearchProject.ResearchDomain.valueOf("QUANTUM_COMPUTING"))
                    .isEqualTo(ResearchProject.ResearchDomain.QUANTUM_COMPUTING);
        }

        @Test
        @DisplayName("Should contain BLOCKCHAIN")
        void shouldContainBlockchain() {
            assertThat(ResearchProject.ResearchDomain.valueOf("BLOCKCHAIN"))
                    .isEqualTo(ResearchProject.ResearchDomain.BLOCKCHAIN);
        }

        @Test
        @DisplayName("Should contain IOT")
        void shouldContainIoT() {
            assertThat(ResearchProject.ResearchDomain.valueOf("IOT"))
                    .isEqualTo(ResearchProject.ResearchDomain.IOT);
        }

        @Test
        @DisplayName("Should contain CYBERSECURITY")
        void shouldContainCybersecurity() {
            assertThat(ResearchProject.ResearchDomain.valueOf("CYBERSECURITY"))
                    .isEqualTo(ResearchProject.ResearchDomain.CYBERSECURITY);
        }

        @Test
        @DisplayName("Should contain GENERAL_RESEARCH")
        void shouldContainGeneralResearch() {
            assertThat(ResearchProject.ResearchDomain.valueOf("GENERAL_RESEARCH"))
                    .isEqualTo(ResearchProject.ResearchDomain.GENERAL_RESEARCH);
        }
    }

    @Nested
    @DisplayName("Priority Enum Tests")
    class PriorityTests {

        @ParameterizedTest
        @EnumSource(ResearchProject.Priority.class)
        @DisplayName("Should have all Priority values")
        void shouldHaveAllValues(ResearchProject.Priority priority) {
            assertThat(priority).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 4 Priority values")
        void shouldHave4Values() {
            assertThat(ResearchProject.Priority.values()).hasSize(4);
        }

        @Test
        @DisplayName("Should contain LOW")
        void shouldContainLow() {
            assertThat(ResearchProject.Priority.valueOf("LOW"))
                    .isEqualTo(ResearchProject.Priority.LOW);
        }

        @Test
        @DisplayName("Should contain MEDIUM")
        void shouldContainMedium() {
            assertThat(ResearchProject.Priority.valueOf("MEDIUM"))
                    .isEqualTo(ResearchProject.Priority.MEDIUM);
        }

        @Test
        @DisplayName("Should contain HIGH")
        void shouldContainHigh() {
            assertThat(ResearchProject.Priority.valueOf("HIGH"))
                    .isEqualTo(ResearchProject.Priority.HIGH);
        }

        @Test
        @DisplayName("Should contain URGENT")
        void shouldContainUrgent() {
            assertThat(ResearchProject.Priority.valueOf("URGENT"))
                    .isEqualTo(ResearchProject.Priority.URGENT);
        }
    }
}
