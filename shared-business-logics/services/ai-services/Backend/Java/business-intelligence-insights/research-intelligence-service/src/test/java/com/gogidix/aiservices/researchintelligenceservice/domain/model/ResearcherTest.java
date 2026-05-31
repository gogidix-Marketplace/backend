package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Researcher Domain Model Tests")
class ResearcherTest {

    @Test
    @DisplayName("Should create researcher successfully")
    void shouldCreateResearcher() {
        Researcher researcher = new Researcher(
                "res-001",
                "Dr. Jane Smith",
                "jane.smith@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR
        );

        assertNotNull(researcher);
        assertEquals("res-001", researcher.getResearcherId());
        assertEquals("Dr. Jane Smith", researcher.getName());
        assertEquals("jane.smith@example.com", researcher.getEmail());
        assertEquals(Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR, researcher.getRole());
        assertEquals("", researcher.getAffiliation());
    }

    @Test
    @DisplayName("Should create researcher with affiliation")
    void shouldCreateResearcherWithAffiliation() {
        Researcher researcher = new Researcher(
                "res-001",
                "Dr. Jane Smith",
                "jane.smith@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR,
                "MIT"
        );

        assertEquals("MIT", researcher.getAffiliation());
    }

    @Test
    @DisplayName("Should throw exception when researcherId is null")
    void shouldThrowWhenResearcherIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Researcher(null, "Name", "email@example.com", Researcher.ResearchRole.RESEARCH_ASSOCIATE)
        );
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowWhenNameIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Researcher("res-001", null, "email@example.com", Researcher.ResearchRole.RESEARCH_ASSOCIATE)
        );
    }

    @Test
    @DisplayName("Should throw exception when role is null")
    void shouldThrowWhenRoleIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Researcher("res-001", "Name", "email@example.com", null)
        );
    }

    @Test
    @DisplayName("Should allow null email")
    void shouldAllowNullEmail() {
        Researcher researcher = new Researcher(
                "res-001",
                "Dr. Jane Smith",
                null,
                Researcher.ResearchRole.RESEARCH_ASSOCIATE
        );

        assertNull(researcher.getEmail());
    }

    @Test
    @DisplayName("Should allow null affiliation")
    void shouldAllowNullAffiliation() {
        Researcher researcher = new Researcher(
                "res-001",
                "Dr. Jane Smith",
                "email@example.com",
                Researcher.ResearchRole.RESEARCH_ASSOCIATE,
                null
        );

        assertNull(researcher.getAffiliation());
    }

    @Test
    @DisplayName("Should verify equals based on researcherId")
    void shouldVerifyEquals() {
        Researcher researcher1 = new Researcher(
                "res-001",
                "John Doe",
                "john@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR
        );

        Researcher researcher2 = new Researcher(
                "res-001",
                "Jane Doe",
                "jane@example.com",
                Researcher.ResearchRole.RESEARCH_ASSOCIATE
        );

        assertEquals(researcher1, researcher2);
        assertEquals(researcher1.hashCode(), researcher2.hashCode());
    }

    @Test
    @DisplayName("Should not equal different researchers")
    void shouldNotEqualDifferentResearchers() {
        Researcher researcher1 = new Researcher(
                "res-001",
                "John Doe",
                "john@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR
        );

        Researcher researcher2 = new Researcher(
                "res-002",
                "John Doe",
                "john@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR
        );

        assertNotEquals(researcher1, researcher2);
    }

    @Test
    @DisplayName("Should verify toString contains key fields")
    void shouldVerifyToString() {
        Researcher researcher = new Researcher(
                "res-001",
                "Dr. Jane Smith",
                "jane@example.com",
                Researcher.ResearchRole.SENIOR_RESEARCHER
        );

        String result = researcher.toString();

        assertTrue(result.contains("res-001"));
        assertTrue(result.contains("Dr. Jane Smith"));
        assertTrue(result.contains("jane@example.com"));
        assertTrue(result.contains("SENIOR_RESEARCHER"));
    }

    @Test
    @DisplayName("Should create researcher with all roles")
    void shouldCreateWithAllRoles() {
        Researcher[] researchers = {
                new Researcher("r1", "Name1", "e1", Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR),
                new Researcher("r2", "Name2", "e2", Researcher.ResearchRole.CO_INVESTIGATOR),
                new Researcher("r3", "Name3", "e3", Researcher.ResearchRole.SENIOR_RESEARCHER),
                new Researcher("r4", "Name4", "e4", Researcher.ResearchRole.RESEARCH_ASSOCIATE),
                new Researcher("r5", "Name5", "e5", Researcher.ResearchRole.RESEARCH_ASSISTANT),
                new Researcher("r6", "Name6", "e6", Researcher.ResearchRole.DATA_ANALYST),
                new Researcher("r7", "Name7", "e7", Researcher.ResearchRole.INTERN),
                new Researcher("r8", "Name8", "e8", Researcher.ResearchRole.CONSULTANT)
        };

        assertEquals(Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR, researchers[0].getRole());
        assertEquals(Researcher.ResearchRole.CONSULTANT, researchers[7].getRole());
    }
}
