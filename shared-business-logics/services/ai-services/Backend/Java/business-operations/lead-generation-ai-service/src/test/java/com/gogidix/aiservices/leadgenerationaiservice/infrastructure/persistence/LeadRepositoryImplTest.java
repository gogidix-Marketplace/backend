package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.persistence;

import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.event.LeadActivity;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LeadRepositoryImpl Infrastructure Tests")
class LeadRepositoryImplTest {

    private InMemoryLeadDataSource dataSource;
    private LeadRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        dataSource = new InMemoryLeadDataSource();
        repository = new LeadRepositoryImpl(dataSource);
    }

    @Nested
    @DisplayName("Save Operation Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save new lead")
        void shouldSaveNewLead() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .build();

            Lead lead = Lead.create(contact);
            lead.setSource(LeadSource.builder()
                    .name("Website")
                    .channel(LeadChannel.WEBSITE)
                    .build());

            Lead saved = repository.save(lead);

            assertThat(saved.getLeadId()).isEqualTo(lead.getLeadId());
            assertThat(repository.findById(lead.getLeadId().toString())).isPresent();
        }

        @Test
        @DisplayName("Should update existing lead")
        void shouldUpdateExistingLead() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .firstName("Jane")
                    .lastName("Smith")
                    .build();

            Lead lead = Lead.create(contact);
            repository.save(lead);

            lead.markAsContacted();
            Lead updated = repository.save(lead);

            assertThat(updated.getStatus()).isEqualTo(LeadStatus.CONTACTED);
        }

        @Test
        @DisplayName("Should preserve activities on update")
        void shouldPreserveActivities() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .build();

            Lead lead = Lead.create(contact);
            LeadActivity activity = LeadActivity.builder()
                    .leadId(lead.getLeadId())
                    .type(ActivityType.EMAIL_SENT)
                    .description("Test")
                    .timestamp(Instant.now())
                    .build();
            lead.addActivity(activity);

            repository.save(lead);
            Lead updated = repository.save(lead);

            assertThat(updated.getActivities()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Find By ID Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should find lead by ID")
        void shouldFindById() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .build();

            Lead lead = Lead.create(contact);
            Lead saved = repository.save(lead);

            Optional<Lead> found = repository.findById(saved.getLeadId().toString());

            assertThat(found).isPresent();
            assertThat(found.get().getLeadId()).isEqualTo(saved.getLeadId());
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            Optional<Lead> found = repository.findById(UUID.randomUUID().toString());

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By Email Tests")
    class FindByEmailTests {

        @Test
        @DisplayName("Should find lead by email")
        void shouldFindByEmail() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .build();

            Lead lead = Lead.create(contact);
            repository.save(lead);

            Optional<Lead> found = repository.findByEmail("test@example.com");

            assertThat(found).isPresent();
            assertThat(found.get().getContactInfo().getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("Should return empty for non-existent email")
        void shouldReturnEmptyForNonExistentEmail() {
            Optional<Lead> found = repository.findByEmail("nonexistent@example.com");

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By Status Tests")
    class FindByStatusTests {

        @Test
        @DisplayName("Should find leads by status")
        void shouldFindByStatus() {
            ContactInfo contact1 = ContactInfo.builder()
                    .email("lead1@example.com")
                    .build();
            ContactInfo contact2 = ContactInfo.builder()
                    .email("lead2@example.com")
                    .build();

            Lead lead1 = Lead.create(contact1);
            Lead lead2 = Lead.create(contact2);
            lead2.markAsContacted();

            repository.save(lead1);
            repository.save(lead2);

            List<Lead> newLeads = repository.findByStatus(LeadStatus.NEW);
            List<Lead> contactedLeads = repository.findByStatus(LeadStatus.CONTACTED);

            assertThat(newLeads).hasSize(1);
            assertThat(contactedLeads).hasSize(1);
        }

        @Test
        @DisplayName("Should return empty list for status with no leads")
        void shouldReturnEmptyForNonExistentStatus() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .build();

            Lead lead = Lead.create(contact);
            repository.save(lead);

            List<Lead> convertedLeads = repository.findByStatus(LeadStatus.CONVERTED);

            assertThat(convertedLeads).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find By Owner Tests")
    class FindByOwnerTests {

        @Test
        @DisplayName("Should find leads by owner")
        void shouldFindByOwner() {
            ContactInfo contact1 = ContactInfo.builder()
                    .email("lead1@example.com")
                    .build();
            ContactInfo contact2 = ContactInfo.builder()
                    .email("lead2@example.com")
                    .build();

            Lead lead1 = Lead.create(contact1);
            Lead lead2 = Lead.create(contact2);
            lead1.assignTo("sales-rep-123", "John Doe");
            lead2.assignTo("sales-rep-456", "Jane Smith");

            repository.save(lead1);
            repository.save(lead2);

            List<Lead> rep1Leads = repository.findByOwnerId("sales-rep-123");

            assertThat(rep1Leads).hasSize(1);
            assertThat(rep1Leads.get(0).getOwnerId()).isEqualTo("sales-rep-123");
        }

        @Test
        @DisplayName("Should return empty list for non-existent owner")
        void shouldReturnEmptyForNonExistentOwner() {
            List<Lead> leads = repository.findByOwnerId("non-existent-owner");

            assertThat(leads).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find Top Scores Tests")
    class FindTopScoresTests {

        @Test
        @DisplayName("Should find top scoring leads")
        void shouldFindTopScores() {
            for (int i = 0; i < 5; i++) {
                ContactInfo contact = ContactInfo.builder()
                        .email("lead" + i + "@example.com")
                        .build();
                Lead lead = Lead.create(contact);
                LeadScore score = LeadScore.builder()
                        .leadId(lead.getLeadId().toString())
                        .score(50.0 + i * 10)
                        .build();
                lead.setScore(score);
                repository.save(lead);
            }

            List<Lead> topLeads = repository.findTopScores(3);

            assertThat(topLeads).hasSize(3);
            assertThat(topLeads.get(0).getScore().getScore()).isEqualTo(90.0);
        }
    }

    @Nested
    @DisplayName("Find All Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should find all leads")
        void shouldFindAll() {
            ContactInfo contact1 = ContactInfo.builder()
                    .email("lead1@example.com")
                    .build();
            ContactInfo contact2 = ContactInfo.builder()
                    .email("lead2@example.com")
                    .build();

            repository.save(Lead.create(contact1));
            repository.save(Lead.create(contact2));

            List<Lead> allLeads = repository.findAll();

            assertThat(allLeads).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete lead")
        void shouldDeleteLead() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .build();

            Lead lead = Lead.create(contact);
            Lead saved = repository.save(lead);

            repository.delete(saved.getLeadId().toString());

            Optional<Lead> found = repository.findById(saved.getLeadId().toString());
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("Save All Tests")
    class SaveAllTests {

        @Test
        @DisplayName("Should save multiple leads")
        void shouldSaveMultipleLeads() {
            List<Lead> leads = List.of(
                    Lead.create(ContactInfo.builder().email("lead1@example.com").build()),
                    Lead.create(ContactInfo.builder().email("lead2@example.com").build()),
                    Lead.create(ContactInfo.builder().email("lead3@example.com").build())
            );

            List<Lead> savedLeads = repository.saveAll(leads);

            assertThat(savedLeads).hasSize(3);
            assertThat(repository.findAll()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Entity Mapping Tests")
    class EntityMappingTests {

        @Test
        @DisplayName("Should map lead to entity correctly")
        void shouldMapToEntity() {
            ContactInfo contact = ContactInfo.builder()
                    .email("test@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .phone("+1234567890")
                    .company("Acme Corp")
                    .jobTitle("CEO")
                    .website("https://example.com")
                    .linkedInUrl("https://linkedin.com/in/johndoe")
                    .companySize(CompanySize.LARGE)
                    .industry("Technology")
                    .build();

            Lead lead = Lead.create(contact);
            lead.setSource(LeadSource.builder()
                    .name("Referral")
                    .channel(LeadChannel.REFERRAL)
                    .build());
            LeadScore score = LeadScore.builder()
                    .leadId(lead.getLeadId().toString())
                    .score(75.0)
                    .build();
            lead.setScore(score);
            lead.assignTo("owner-123", "Owner Name");

            Lead saved = repository.save(lead);

            assertThat(saved.getContactInfo().getEmail()).isEqualTo("test@example.com");
            assertThat(saved.getContactInfo().getFirstName()).isEqualTo("John");
            assertThat(saved.getOwnerId()).isEqualTo("owner-123");
        }
    }
}
