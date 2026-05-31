package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Publication Domain Model Tests")
class PublicationTest {

    @Test
    @DisplayName("Should create publication successfully")
    void shouldCreatePublication() {
        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "AI Research Paper",
                "This paper presents novel AI techniques",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Nature Journal",
                "author@example.com"
        );

        assertNotNull(publication);
        assertEquals("pub-001", publication.getPublicationId());
        assertEquals("proj-001", publication.getProjectId());
        assertEquals("AI Research Paper", publication.getTitle());
        assertEquals("This paper presents novel AI techniques", publication.getAbstractText());
        assertEquals(Publication.PublicationType.JOURNAL_ARTICLE, publication.getType());
        assertEquals("Nature Journal", publication.getJournal());
        assertEquals("author@example.com", publication.getSubmittedBy());
        assertEquals(Publication.PublicationStatus.DRAFT, publication.getStatus());
        assertEquals(0, publication.getPageCount());
    }

    @Test
    @DisplayName("Should generate citation key correctly")
    void shouldGenerateCitationKey() {
        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "Machine Learning Advances",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Nature",
                "smith@example.com"
        );

        String citationKey = publication.getCitationKey();

        assertNotNull(citationKey);
        assertTrue(citationKey.contains("mac"));
        assertTrue(citationKey.contains("sm"));
        assertTrue(citationKey.contains(String.valueOf(LocalDateTime.now().getYear())));
    }

    @Test
    @DisplayName("Should throw exception when publicationId is null")
    void shouldThrowWhenPublicationIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Publication(
                        null,
                        "proj-001",
                        "Title",
                        "Abstract",
                        Publication.PublicationType.JOURNAL_ARTICLE,
                        "Journal",
                        "author@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when projectId is null")
    void shouldThrowWhenProjectIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Publication(
                        "pub-001",
                        null,
                        "Title",
                        "Abstract",
                        Publication.PublicationType.JOURNAL_ARTICLE,
                        "Journal",
                        "author@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when title is null")
    void shouldThrowWhenTitleIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Publication(
                        "pub-001",
                        "proj-001",
                        null,
                        "Abstract",
                        Publication.PublicationType.JOURNAL_ARTICLE,
                        "Journal",
                        "author@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when type is null")
    void shouldThrowWhenTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Publication(
                        "pub-001",
                        "proj-001",
                        "Title",
                        "Abstract",
                        null,
                        "Journal",
                        "author@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should allow null abstractText")
    void shouldAllowNullAbstractText() {
        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "Title",
                null,
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Journal",
                "author@example.com"
        );

        assertNull(publication.getAbstractText());
    }

    @Test
    @DisplayName("Should allow null journal")
    void shouldAllowNullJournal() {
        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "Title",
                "Abstract",
                Publication.PublicationType.PREPRINT,
                null,
                "author@example.com"
        );

        assertNull(publication.getJournal());
    }

    @Test
    @DisplayName("Should allow null submittedBy")
    void shouldAllowNullSubmittedBy() {
        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "Title",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Journal",
                null
        );

        assertNull(publication.getSubmittedBy());
    }

    @Test
    @DisplayName("Should verify submittedAt is set to current time")
    void shouldVerifySubmittedAt() {
        LocalDateTime before = LocalDateTime.now();
        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "Title",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Journal",
                "author@example.com"
        );
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(publication.getSubmittedAt());
        assertTrue(publication.getSubmittedAt().isBefore(after) || publication.getSubmittedAt().isEqual(after));
        assertTrue(publication.getSubmittedAt().isAfter(before) || publication.getSubmittedAt().isEqual(before));
    }

    @Test
    @DisplayName("Should verify equals based on publicationId")
    void shouldVerifyEquals() {
        Publication publication1 = new Publication(
                "pub-001",
                "proj-001",
                "Title 1",
                "Abstract 1",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Journal 1",
                "author1@example.com"
        );

        Publication publication2 = new Publication(
                "pub-001",
                "proj-002",
                "Title 2",
                "Abstract 2",
                Publication.PublicationType.CONFERENCE_PAPER,
                "Journal 2",
                "author2@example.com"
        );

        assertEquals(publication1, publication2);
        assertEquals(publication1.hashCode(), publication2.hashCode());
    }

    @Test
    @DisplayName("Should not equal different publications")
    void shouldNotEqualDifferentPublications() {
        Publication publication1 = new Publication(
                "pub-001",
                "proj-001",
                "Title",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Journal",
                "author@example.com"
        );

        Publication publication2 = new Publication(
                "pub-002",
                "proj-001",
                "Title",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Journal",
                "author@example.com"
        );

        assertNotEquals(publication1, publication2);
    }

    @Test
    @DisplayName("Should verify toString contains key fields")
    void shouldVerifyToString() {
        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "AI Research Paper",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Nature",
                "author@example.com"
        );

        String result = publication.toString();

        assertTrue(result.contains("pub-001"));
        assertTrue(result.contains("proj-001"));
        assertTrue(result.contains("AI Research Paper"));
        assertTrue(result.contains("JOURNAL_ARTICLE"));
        assertTrue(result.contains("Nature"));
    }

    @Test
    @DisplayName("Should create publication with all types")
    void shouldCreateWithAllTypes() {
        Publication[] publications = {
                new Publication("p1", "pr1", "T", "A", Publication.PublicationType.JOURNAL_ARTICLE, "J", "a"),
                new Publication("p2", "pr2", "T", "A", Publication.PublicationType.CONFERENCE_PAPER, "J", "a"),
                new Publication("p3", "pr3", "T", "A", Publication.PublicationType.BOOK_CHAPTER, "J", "a"),
                new Publication("p4", "pr4", "T", "A", Publication.PublicationType.TECHNICAL_REPORT, "J", "a"),
                new Publication("p5", "pr5", "T", "A", Publication.PublicationType.PREPRINT, "J", "a"),
                new Publication("p6", "pr6", "T", "A", Publication.PublicationType.THESIS, "J", "a"),
                new Publication("p7", "pr7", "T", "A", Publication.PublicationType.WHITE_PAPER, "J", "a"),
                new Publication("p8", "pr8", "T", "A", Publication.PublicationType.WORKSHOP_PAPER, "J", "a")
        };

        assertEquals(Publication.PublicationType.JOURNAL_ARTICLE, publications[0].getType());
        assertEquals(Publication.PublicationType.WORKSHOP_PAPER, publications[7].getType());
    }
}
