package com.gogidix.aiservices.aidatavalidation.infrastructure.persistence;

import com.gogidix.aiservices.aidatavalidation.domain.aggregate.ValidationExecution;
import com.gogidix.aiservices.aidatavalidation.domain.model.Severity;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationResult;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationRule;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Validation Repository Infrastructure Tests")
class ValidationRepositoryImplTest {

    @Mock
    private ValidationDataSource dataSource;

    @InjectMocks
    private ValidationRepositoryImpl repository;

    private static final String DATA_SOURCE = "s3://bucket/data.csv";
    private static final String SCHEMA = "{\"fields\":[]}";
    private static final String EXECUTION_ID = "exec-123";

    @Nested
    @DisplayName("Save Operations")
    class SaveTests {

        @Test
        @DisplayName("Should save new validation execution")
        void shouldSaveNewExecution() {
            ValidationExecution execution = ValidationExecution.create(DATA_SOURCE, SCHEMA);

            when(dataSource.save(any(ValidationEntity.class))).thenReturn(new ValidationEntity());

            ValidationExecution saved = repository.save(execution);

            assertThat(saved).isNotNull();
            verify(dataSource).save(any(ValidationEntity.class));
        }

        @Test
        @DisplayName("Should update existing execution")
        void shouldUpdateExecution() {
            ValidationExecution execution = ValidationExecution.create(DATA_SOURCE, SCHEMA);
            execution.start();

            when(dataSource.save(any(ValidationEntity.class))).thenReturn(new ValidationEntity());

            repository.save(execution);

            verify(dataSource).save(any(ValidationEntity.class));
        }

        @Test
        @DisplayName("Should save validation rule")
        void shouldSaveRule() {
            ValidationRule rule = ValidationRule.create("rule-1", "Rule", ValidationType.CUSTOM);

            when(dataSource.saveRule(any(ValidationRuleEntity.class))).thenReturn(new ValidationRuleEntity());

            ValidationRule saved = repository.saveRule(rule);

            assertThat(saved).isNotNull();
            verify(dataSource).saveRule(any(ValidationRuleEntity.class));
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindTests {

        @Test
        @DisplayName("Should find execution by ID")
        void shouldFindById() {
            ValidationEntity entity = createValidationEntity();
            when(dataSource.findById(EXECUTION_ID)).thenReturn(Optional.of(entity));

            Optional<ValidationExecution> result = repository.findById(EXECUTION_ID);

            assertThat(result).isPresent();
            assertThat(result.get().getDataSource()).isEqualTo(DATA_SOURCE);
            verify(dataSource).findById(EXECUTION_ID);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(dataSource.findById(EXECUTION_ID)).thenReturn(Optional.empty());

            Optional<ValidationExecution> result = repository.findById(EXECUTION_ID);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should find pending validations")
        void shouldFindPending() {
            List<ValidationEntity> entities = List.of(
                    createValidationEntity(),
                    createValidationEntity()
            );
            when(dataSource.findPending()).thenReturn(entities);

            List<ValidationExecution> results = repository.findPending();

            assertThat(results).hasSize(2);
            verify(dataSource).findPending();
        }

        @Test
        @DisplayName("Should find rules by type")
        void shouldFindRulesByType() {
            List<ValidationRuleEntity> entities = List.of(
                    createValidationRuleEntity("rule-1", "Schema Rule", ValidationType.SCHEMA)
            );
            when(dataSource.findRulesByType(ValidationType.SCHEMA)).thenReturn(entities);

            List<ValidationRule> results = repository.findRulesByType(ValidationType.SCHEMA);

            assertThat(results).hasSize(1);
            verify(dataSource).findRulesByType(ValidationType.SCHEMA);
        }

        @Test
        @DisplayName("Should find rule by ID")
        void shouldFindRuleById() {
            ValidationRuleEntity entity = createValidationRuleEntity("rule-1", "Rule", ValidationType.CUSTOM);
            when(dataSource.findRuleById("rule-1")).thenReturn(Optional.of(entity));

            Optional<ValidationRule> result = repository.findRuleById("rule-1");

            assertThat(result).isPresent();
            assertThat(result.get().getRuleId()).isEqualTo("rule-1");
        }
    }

    @Nested
    @DisplayName("Statistics Operations")
    class StatisticsTests {

        @Test
        @DisplayName("Should get validation statistics")
        void shouldGetStatistics() {
            Map<String, Object> stats = Map.of(
                    "totalValidations", 100,
                    "completed", 85,
                    "failed", 10,
                    "pending", 5
            );
            when(dataSource.getStatistics()).thenReturn(stats);

            Map<String, Object> result = repository.getStatistics();

            assertThat(result).isEqualTo(stats);
            verify(dataSource).getStatistics();
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteTests {

        @Test
        @DisplayName("Should delete execution by ID")
        void shouldDeleteById() {
            doNothing().when(dataSource).delete(EXECUTION_ID);

            repository.delete(EXECUTION_ID);

            verify(dataSource).delete(EXECUTION_ID);
        }

        @Test
        @DisplayName("Should delete rule by ID")
        void shouldDeleteRuleById() {
            doNothing().when(dataSource).deleteRule("rule-1");

            repository.deleteRule("rule-1");

            verify(dataSource).deleteRule("rule-1");
        }
    }

    @Nested
    @DisplayName("Entity Mapping Tests")
    class MappingTests {

        @Test
        @DisplayName("Should map entity to domain correctly")
        void shouldMapEntityToDomain() {
            ValidationEntity entity = createValidationEntity();
            entity.setStatus(ValidationExecution.Status.RUNNING);

            when(dataSource.findById(EXECUTION_ID)).thenReturn(Optional.of(entity));

            Optional<ValidationExecution> result = repository.findById(EXECUTION_ID);

            assertThat(result).isPresent();
            assertThat(result.get().getStatus()).isEqualTo(ValidationExecution.Status.RUNNING);
        }

        @Test
        @DisplayName("Should map domain to entity correctly")
        void shouldMapDomainToEntity() {
            ValidationExecution execution = ValidationExecution.create(DATA_SOURCE, SCHEMA);
            execution.start();

            when(dataSource.save(any(ValidationEntity.class))).thenReturn(new ValidationEntity());

            repository.save(execution);

            verify(dataSource).save(argThat(e ->
                    e.getStatus() == ValidationExecution.Status.RUNNING
            ));
        }
    }

    private ValidationEntity createValidationEntity() {
        ValidationEntity entity = new ValidationEntity();
        entity.setExecutionId(EXECUTION_ID);
        entity.setDataSource(DATA_SOURCE);
        entity.setSchema(SCHEMA);
        entity.setStatus(ValidationExecution.Status.PENDING);
        entity.setCreatedAt(Instant.now());
        entity.setTimeout(3600);
        entity.setProgress(0);
        return entity;
    }

    private ValidationRuleEntity createValidationRuleEntity(String id, String name, ValidationType type) {
        ValidationRuleEntity entity = new ValidationRuleEntity();
        entity.setRuleId(id);
        entity.setName(name);
        entity.setType(type);
        entity.setSeverity(Severity.MEDIUM);
        entity.setPriority(5);
        entity.setEnabled(true);
        return entity;
    }
}
