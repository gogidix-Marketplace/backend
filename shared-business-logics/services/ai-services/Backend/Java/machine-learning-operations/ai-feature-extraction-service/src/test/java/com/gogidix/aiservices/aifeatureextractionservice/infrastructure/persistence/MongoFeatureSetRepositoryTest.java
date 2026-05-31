package com.gogidix.aiservices.aifeatureextractionservice.infrastructure.persistence;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureSet;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MongoFeatureSetRepository.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MongoFeatureSetRepository Tests")
class MongoFeatureSetRepositoryTest {

    @Mock
    private SpringDataFeatureSetRepository springDataRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    private MongoFeatureSetRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MongoFeatureSetRepository(springDataRepository, mongoTemplate);
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save feature set")
        void shouldSaveFeatureSet() {
            FeatureSet featureSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));

            when(springDataRepository.save(any(FeatureSet.class))).thenReturn(featureSet);

            FeatureSet result = repository.save(featureSet);

            assertNotNull(result);
            verify(springDataRepository).save(featureSet);
        }
    }

    @Nested
    @DisplayName("Find By Id Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should find feature set by id")
        void shouldFindFeatureSetById() {
            FeatureSet featureSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));

            when(springDataRepository.findById("fs-123")).thenReturn(Optional.of(featureSet));

            Optional<FeatureSet> result = repository.findById("fs-123");

            assertTrue(result.isPresent());
            verify(springDataRepository).findById("fs-123");
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(springDataRepository.findById("invalid")).thenReturn(Optional.empty());

            Optional<FeatureSet> result = repository.findById("invalid");

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should find feature set by id and tenant id")
        void shouldFindFeatureSetByIdAndTenantId() {
            FeatureSet featureSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));

            when(springDataRepository.findByIdAndTenantId("fs-123", "tenant-123"))
                    .thenReturn(Optional.of(featureSet));

            Optional<FeatureSet> result = repository.findByIdAndTenantId("fs-123", "tenant-123");

            assertTrue(result.isPresent());
            verify(springDataRepository).findByIdAndTenantId("fs-123", "tenant-123");
        }
    }

    @Nested
    @DisplayName("Find By Tenant Id Tests")
    class FindByTenantIdTests {

        @Test
        @DisplayName("Should find feature sets by tenant id")
        void shouldFindFeatureSetsByTenantId() {
            FeatureSet featureSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));
            Page<FeatureSet> page = new PageImpl<>(List.of(featureSet));

            when(springDataRepository.findByTenantId(eq("tenant-123"), any(Pageable.class)))
                    .thenReturn(page);

            List<FeatureSet> result = repository.findByTenantId("tenant-123", 0, 10);

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should pass pagination parameters correctly")
        void shouldPassPaginationParametersCorrectly() {
            FeatureSet featureSet = new FeatureSet("tenant-123", "data-source", List.of(ExtractionMethod.TFIDF));

            when(springDataRepository.findByTenantId(eq("tenant-123"), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(featureSet)));

            repository.findByTenantId("tenant-123", 2, 25);

            ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
            verify(springDataRepository).findByTenantId(eq("tenant-123"), captor.capture());

            PageRequest captured = (PageRequest) captor.getValue();
            assertEquals(2, captured.getPageNumber());
            assertEquals(25, captured.getPageSize());
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete feature set by id")
        void shouldDeleteFeatureSetById() {
            repository.deleteById("fs-123");

            verify(springDataRepository).deleteById("fs-123");
        }
    }

    @Nested
    @DisplayName("Exists Tests")
    class ExistsTests {

        @Test
        @DisplayName("Should return true when feature set exists")
        void shouldReturnTrueWhenExists() {
            when(springDataRepository.existsById("fs-123")).thenReturn(true);

            boolean result = repository.existsById("fs-123");

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when feature set does not exist")
        void shouldReturnFalseWhenDoesNotExist() {
            when(springDataRepository.existsById("invalid")).thenReturn(false);

            boolean result = repository.existsById("invalid");

            assertFalse(result);
        }
    }
}
