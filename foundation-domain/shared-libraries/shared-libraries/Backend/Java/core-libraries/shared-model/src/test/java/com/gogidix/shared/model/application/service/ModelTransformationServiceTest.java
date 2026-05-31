package com.gogidix.shared.model.application.service;

import com.gogidix.shared.model.application.port.in.ModelTransformationUseCase;
import com.gogidix.shared.model.application.port.in.ModelTransformationUseCase.*;
import com.gogidix.shared.model.domain.model.DomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ModelTransformationService Tests")
class ModelTransformationServiceTest {

    private ModelTransformationService service;

    static class TestDomainEntity extends DomainEntity {
        private String name;
        private String description;

        public TestDomainEntity() {}

        public TestDomainEntity(String name, String description) {
            this.name = name;
            this.description = description;
            initializeForCreation("test");
        }

        @Override public boolean isValid() { return getId() != null && name != null; }
        @Override public String getEntityType() { return "TEST_ENTITY"; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    static class TestDto {
        private String name;
        private String description;

        public TestDto() {}
        public TestDto(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    @BeforeEach
    void setUp() {
        service = new ModelTransformationService();
    }

    @Nested
    @DisplayName("transformToDto Tests")
    class TransformToDtoTests {
        @Test void success() {
            TestDomainEntity entity = new TestDomainEntity("test", "desc");
            TransformationResult<TestDto> result = service.transformToDto(entity, TestDto.class);
            assertTrue(result.isSuccess());
            assertNotNull(result.getResult());
            assertEquals("test", result.getResult().getName());
            assertNotNull(result.getMetadata());
            assertEquals("TestDomainEntity", result.getMetadata().get("sourceType"));
            assertEquals("TestDto", result.getMetadata().get("targetType"));
        }

        @Test void nullEntity() {
            var result = service.transformToDto(null, TestDto.class);
            assertFalse(result.isSuccess());
            assertEquals("Source entity cannot be null", result.getErrorMessage());
        }

        @Test void nullDtoClass() {
            TestDomainEntity entity = new TestDomainEntity("test", "desc");
            var result = service.transformToDto(entity, null);
            assertFalse(result.isSuccess());
            assertEquals("Target DTO class cannot be null", result.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("transformToDomain Tests")
    class TransformToDomainTests {
        @Test void nullDto() {
            var result = service.transformToDomain(null, TestDomainEntity.class);
            assertFalse(result.isSuccess());
            assertEquals("Source DTO cannot be null", result.getErrorMessage());
        }

        @Test void nullEntityClass() {
            TestDto dto = new TestDto("test", "desc");
            var result = service.transformToDomain(dto, null);
            assertFalse(result.isSuccess());
            assertEquals("Target entity class cannot be null", result.getErrorMessage());
        }

        @Test void unsupportedOperation() {
            TestDto dto = new TestDto("test", "desc");
            var result = service.transformToDomain(dto, TestDomainEntity.class);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessage().contains("Transformation failed"));
            assertTrue(result.getError().isPresent());
        }
    }

    @Nested
    @DisplayName("transformListToDto Tests")
    class TransformListToDtoTests {
        @Test void transformMultiple() {
            TestDomainEntity e1 = new TestDomainEntity("a", "desc1");
            TestDomainEntity e2 = new TestDomainEntity("b", "desc2");
            var result = service.transformListToDto(List.of(e1, e2), TestDto.class);
            assertEquals(2, result.getTotalCount());
            assertEquals(2, result.getSuccessCount());
            assertEquals(0, result.getFailureCount());
            assertTrue(result.isAllSuccessful());
            assertEquals(2, result.getSuccessfulResults().size());
            assertTrue(result.getExecutionTimeMs() >= 0);
            assertEquals(1.0, result.getSuccessRate());
        }

        @Test void emptyList() {
            var result = service.transformListToDto(List.of(), TestDto.class);
            assertEquals(0, result.getTotalCount());
            assertTrue(result.isAllSuccessful());
        }
    }

    @Nested
    @DisplayName("transformListToDomain Tests")
    class TransformListToDomainTests {
        @Test void allFail() {
            TestDto dto = new TestDto("test", "desc");
            var result = service.transformListToDomain(List.of(dto), TestDomainEntity.class);
            assertEquals(1, result.getTotalCount());
            assertEquals(1, result.getFailureCount());
            assertFalse(result.isAllSuccessful());
            assertEquals(1, result.getErrorMessages().size());
        }
    }

    @Nested
    @DisplayName("validateEntity Tests")
    class ValidateEntityTests {
        @Test void nullEntity() {
            var result = service.validateEntity(null);
            assertFalse(result.isValid());
            assertEquals(List.of("Entity cannot be null"), result.getErrors());
        }

        @Test void validEntity() {
            TestDomainEntity entity = new TestDomainEntity("test", "desc");
            var result = service.validateEntity(entity);
            assertTrue(result.isValid());
        }

        @Test void invalidEntity() {
            TestDomainEntity entity = new TestDomainEntity();
            var result = service.validateEntity(entity);
            assertFalse(result.isValid());
            assertFalse(result.getErrors().isEmpty());
        }
    }

    @Nested
    @DisplayName("validateEntities Tests")
    class ValidateEntitiesTests {
        @Test void batchValidation() {
            TestDomainEntity e1 = new TestDomainEntity("test", "desc");
            TestDomainEntity e2 = new TestDomainEntity();
            var result = service.validateEntities(List.of(e1, e2));
            assertEquals(2, result.getTotalCount());
            assertEquals(1, result.getValidCount());
            assertEquals(1, result.getInvalidCount());
            assertFalse(result.isAllValid());
            assertFalse(result.getAllErrors().isEmpty());
        }

        @Test void allValid() {
            TestDomainEntity e1 = new TestDomainEntity("a", "d1");
            TestDomainEntity e2 = new TestDomainEntity("b", "d2");
            var result = service.validateEntities(List.of(e1, e2));
            assertTrue(result.isAllValid());
            assertTrue(result.getAllErrors().isEmpty());
            assertTrue(result.getAllWarnings().isEmpty());
        }

        @Test void emptyList() {
            var result = service.validateEntities(List.of());
            assertEquals(0, result.getTotalCount());
            assertTrue(result.isAllValid());
        }
    }

    @Nested
    @DisplayName("mergeEntities Tests")
    class MergeEntitiesTests {
        @Test void nullTarget() {
            TestDomainEntity source = new TestDomainEntity("test", "desc");
            var result = service.mergeEntities(null, source, "admin");
            assertFalse(result.isSuccess());
            assertEquals("Target and source entities cannot be null", result.getErrorMessage());
        }

        @Test void nullSource() {
            TestDomainEntity target = new TestDomainEntity("test", "desc");
            var result = service.mergeEntities(target, null, "admin");
            assertFalse(result.isSuccess());
            assertEquals("Target and source entities cannot be null", result.getErrorMessage());
        }

        @Test void differentTypes() {
            TestDomainEntity target = new TestDomainEntity("a", "d1");
            DomainEntity source = new DomainEntity() {
                @Override public boolean isValid() { return true; }
                @Override public String getEntityType() { return "OTHER"; }
            };
            var result = service.mergeEntities(target, source, "admin");
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessage().contains("same type"));
        }

        @Test void successfulMerge() {
            TestDomainEntity target = new TestDomainEntity("test", "desc");
            TestDomainEntity source = new TestDomainEntity("test2", "desc2");
            var result = service.mergeEntities(target, source, "admin");
            assertTrue(result.isSuccess());
            assertNotNull(result.getMergedEntity());
        }
    }

    @Nested
    @DisplayName("copyEntity Tests")
    class CopyEntityTests {
        @Test void nullEntity() {
            var result = service.copyEntity(null);
            assertFalse(result.isSuccess());
            assertEquals("Entity cannot be null", result.getErrorMessage());
        }

        @Test void copyFailsUnsupported() {
            TestDomainEntity entity = new TestDomainEntity("test", "desc");
            var result = service.copyEntity(entity);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessage().contains("Copy failed"));
        }
    }

    @Nested
    @DisplayName("comparEntities Tests")
    class CompareEntitiesTests {
        @Test void bothNull() {
            var result = service.comparEntities(null, null);
            assertTrue(result.isIdentical());
            assertFalse(result.hasDifferences());
            assertEquals(1.0, result.getSimilarityScore());
        }

        @Test void firstNull() {
            TestDomainEntity e = new TestDomainEntity("test", "desc");
            var result = service.comparEntities(null, e);
            assertFalse(result.isIdentical());
            assertTrue(result.hasDifferences());
            assertEquals(0.0, result.getSimilarityScore());
        }

        @Test void secondNull() {
            TestDomainEntity e = new TestDomainEntity("test", "desc");
            var result = service.comparEntities(e, null);
            assertFalse(result.isIdentical());
            assertTrue(result.hasDifferences());
        }

        @Test void differentTypes() {
            TestDomainEntity e1 = new TestDomainEntity("test", "desc");
            DomainEntity e2 = new DomainEntity() {
                @Override public boolean isValid() { return true; }
                @Override public String getEntityType() { return "OTHER"; }
            };
            var result = service.comparEntities(e1, e2);
            assertFalse(result.isIdentical());
            assertTrue(result.hasDifferences());
        }

        @Test void sameEntity() {
            TestDomainEntity e = new TestDomainEntity("test", "desc");
            var result = service.comparEntities(e, e);
            assertTrue(result.isIdentical());
            assertFalse(result.hasDifferences());
            assertEquals(1.0, result.getSimilarityScore());
        }

        @Test void differentEntities() {
            TestDomainEntity e1 = new TestDomainEntity("a", "d1");
            TestDomainEntity e2 = new TestDomainEntity("b", "d2");
            var result = service.comparEntities(e1, e2);
            assertTrue(result.hasDifferences());
            assertTrue(result.getSimilarityScore() >= 0.0);
            assertTrue(result.getSimilarityScore() <= 1.0);
        }
    }

    @Nested
    @DisplayName("serializeToJson Tests")
    class SerializeToJsonTests {
        @Test void nullEntity() {
            var result = service.serializeToJson(null);
            assertFalse(result.isSuccess());
            assertEquals("Entity cannot be null", result.getErrorMessage());
        }

        @Test void success() {
            TestDomainEntity entity = new TestDomainEntity("test", "desc");
            var result = service.serializeToJson(entity);
            assertTrue(result.isSuccess());
            assertNotNull(result.getSerializedData());
            assertEquals("JSON", result.getFormat());
            assertTrue(result.getSerializedData().contains("TEST_ENTITY"));
        }
    }

    @Nested
    @DisplayName("deserializeFromJson Tests")
    class DeserializeFromJsonTests {
        @Test void nullJson() {
            var result = service.deserializeFromJson(null, TestDomainEntity.class);
            assertFalse(result.isSuccess());
            assertEquals("JSON string cannot be null or empty", result.getErrorMessage());
        }

        @Test void emptyJson() {
            var result = service.deserializeFromJson("  ", TestDomainEntity.class);
            assertFalse(result.isSuccess());
            assertEquals("JSON string cannot be null or empty", result.getErrorMessage());
        }

        @Test void nullClass() {
            var result = service.deserializeFromJson("{}", null);
            assertFalse(result.isSuccess());
            assertEquals("Entity class cannot be null", result.getErrorMessage());
        }

        @Test void unsupported() {
            var result = service.deserializeFromJson("{\"id\":\"test\"}", TestDomainEntity.class);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessage().contains("Deserialization failed"));
        }
    }

    @Nested
    @DisplayName("Mapping Registration Tests")
    class MappingTests {
        @Test void getAvailableMappingsEmpty() {
            var mappings = service.getAvailableMappings();
            assertNotNull(mappings);
            assertTrue(mappings.isEmpty());
        }

        @Test void registerNullMapping() {
            var result = service.registerMapping(null);
            assertFalse(result.isSuccess());
            assertEquals("Mapping cannot be null", result.getErrorMessage());
        }

        @Test void registerMappingSuccess() {
            TransformationMapping mapping = new TransformationMapping(
                TestDomainEntity.class, TestDto.class, "test-mapping",
                Map.of("name", "name"), false);
            var result = service.registerMapping(mapping);
            assertTrue(result.isSuccess());
            assertNotNull(result.getMappingId());
        }

        @Test void registerDuplicateMapping() {
            TransformationMapping mapping = new TransformationMapping(
                TestDomainEntity.class, TestDto.class, "test-mapping",
                Map.of("name", "name"), false);
            service.registerMapping(mapping);
            var result = service.registerMapping(mapping);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessage().contains("already exists"));
        }

        @Test void getAvailableMappingsAfterRegistration() {
            TransformationMapping mapping = new TransformationMapping(
                TestDomainEntity.class, TestDto.class, "test-mapping",
                Map.of("name", "name"), true);
            service.registerMapping(mapping);
            var mappings = service.getAvailableMappings();
            assertEquals(1, mappings.size());
            assertEquals(TestDomainEntity.class, mappings.get(0).getSourceClass());
            assertEquals(TestDto.class, mappings.get(0).getTargetClass());
            assertEquals("test-mapping", mappings.get(0).getMappingName());
            assertTrue(mappings.get(0).isBidirectional());
        }

        @Test void mappingIdGenerationBidirectional() {
            TransformationMapping mapping = new TransformationMapping(
                String.class, Integer.class, "test", Map.of(), true);
            var result = service.registerMapping(mapping);
            assertTrue(result.getMappingId().contains("bidirectional"));
        }

        @Test void mappingIdGenerationUnidirectional() {
            TransformationMapping mapping = new TransformationMapping(
                String.class, Integer.class, "test", Map.of(), false);
            var result = service.registerMapping(mapping);
            assertFalse(result.getMappingId().contains("bidirectional"));
        }
    }

    @Nested
    @DisplayName("TransformationResult Tests")
    class TransformationResultTests {
        @Test void successWithMetadata() {
            var result = TransformationResult.success("data", Map.of("key", "value"));
            assertTrue(result.isSuccess());
            assertEquals("data", result.getResult());
            assertNull(result.getErrorMessage());
            assertTrue(result.getError().isEmpty());
            assertEquals(Map.of("key", "value"), result.getMetadata());
        }

        @Test void successNoMetadata() {
            var result = TransformationResult.success("data");
            assertTrue(result.isSuccess());
            assertEquals(Map.of(), result.getMetadata());
        }

        @Test void failureNoException() {
            var result = TransformationResult.<String>failure("error msg");
            assertFalse(result.isSuccess());
            assertNull(result.getResult());
            assertEquals("error msg", result.getErrorMessage());
            assertTrue(result.getError().isEmpty());
        }

        @Test void failureWithException() {
            Exception ex = new RuntimeException("test");
            var result = TransformationResult.<String>failure("error", ex);
            assertFalse(result.isSuccess());
            assertTrue(result.getError().isPresent());
            assertEquals(ex, result.getError().get());
        }
    }

    @Nested
    @DisplayName("BatchTransformationResult Tests")
    class BatchTransformationResultTests {
        @Test void mixedResults() {
            var r1 = TransformationResult.success("a");
            var r2 = TransformationResult.<String>failure("err");
            var batch = new BatchTransformationResult<>(List.of(r1, r2), 100L);
            assertEquals(2, batch.getTotalCount());
            assertEquals(1, batch.getSuccessCount());
            assertEquals(1, batch.getFailureCount());
            assertEquals(0.5, batch.getSuccessRate());
            assertFalse(batch.isAllSuccessful());
            assertEquals(List.of("a"), batch.getSuccessfulResults());
            assertEquals(List.of("err"), batch.getErrorMessages());
        }
    }

    @Nested
    @DisplayName("ValidationResult Tests")
    class ValidationResultTests {
        @Test void valid() {
            var r = ValidationResult.valid();
            assertTrue(r.isValid());
            assertTrue(r.getErrors().isEmpty());
            assertTrue(r.getWarnings().isEmpty());
            assertFalse(r.hasWarnings());
            assertTrue(r.getValidationDetails().isEmpty());
        }

        @Test void validWithWarnings() {
            var r = ValidationResult.validWithWarnings(List.of("warn1"));
            assertTrue(r.isValid());
            assertTrue(r.hasWarnings());
            assertEquals(List.of("warn1"), r.getWarnings());
        }

        @Test void invalidErrorsOnly() {
            var r = ValidationResult.invalid(List.of("err1"));
            assertFalse(r.isValid());
            assertEquals(List.of("err1"), r.getErrors());
            assertFalse(r.hasWarnings());
        }

        @Test void invalidWithWarnings() {
            var r = ValidationResult.invalid(List.of("err1"), List.of("warn1"));
            assertFalse(r.isValid());
            assertTrue(r.hasWarnings());
        }
    }

    @Nested
    @DisplayName("BatchValidationResult Tests")
    class BatchValidationResultTests {
        @Test void mixedValidation() {
            var r1 = ValidationResult.valid();
            var r2 = ValidationResult.invalid(List.of("err"));
            var batch = new BatchValidationResult(List.of(r1, r2));
            assertEquals(2, batch.getTotalCount());
            assertEquals(1, batch.getValidCount());
            assertEquals(1, batch.getInvalidCount());
            assertFalse(batch.isAllValid());
            assertEquals(List.of("err"), batch.getAllErrors());
        }

        @Test void allValid() {
            var batch = new BatchValidationResult(List.of(ValidationResult.valid(), ValidationResult.valid()));
            assertTrue(batch.isAllValid());
            assertTrue(batch.getAllErrors().isEmpty());
        }
    }

    @Nested
    @DisplayName("MergeResult Tests")
    class MergeResultTests {
        @Test void successResult() {
            TestDomainEntity e = new TestDomainEntity("test", "desc");
            var result = MergeResult.success(e, List.of("name"));
            assertTrue(result.isSuccess());
            assertEquals(e, result.getMergedEntity());
            assertEquals(List.of("name"), result.getChangedFields());
            assertTrue(result.hasChanges());
            assertNull(result.getErrorMessage());
        }

        @Test void failureResult() {
            var result = MergeResult.<TestDomainEntity>failure("error");
            assertFalse(result.isSuccess());
            assertFalse(result.hasChanges());
            assertEquals("error", result.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("CopyResult Tests")
    class CopyResultTests {
        @Test void successResult() {
            TestDomainEntity e = new TestDomainEntity("test", "desc");
            var result = CopyResult.success(e);
            assertTrue(result.isSuccess());
            assertEquals(e, result.getCopiedEntity());
            assertNull(result.getErrorMessage());
        }

        @Test void failureResult() {
            var result = CopyResult.<TestDomainEntity>failure("error");
            assertFalse(result.isSuccess());
            assertNull(result.getCopiedEntity());
            assertEquals("error", result.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("ComparisonResult Tests")
    class ComparisonResultTests {
        @Test void identicalResult() {
            var result = new ComparisonResult(true, Map.of(), 1.0);
            assertTrue(result.isIdentical());
            assertFalse(result.hasDifferences());
            assertEquals(1.0, result.getSimilarityScore());
        }

        @Test void differentResult() {
            var diff = new ComparisonResult.FieldDifference("field", "old", "new");
            var result = new ComparisonResult(false, Map.of("field", diff), 0.5);
            assertFalse(result.isIdentical());
            assertTrue(result.hasDifferences());
            assertEquals(0.5, result.getSimilarityScore());
        }

        @Test void fieldDifference() {
            var diff = new ComparisonResult.FieldDifference("name", "old", "new");
            assertEquals("name", diff.getFieldName());
            assertEquals("old", diff.getOldValue());
            assertEquals("new", diff.getNewValue());
        }
    }

    @Nested
    @DisplayName("SerializationResult Tests")
    class SerializationResultTests {
        @Test void success() {
            var result = SerializationResult.success("{\"key\":\"val\"}", "JSON");
            assertTrue(result.isSuccess());
            assertEquals("{\"key\":\"val\"}", result.getSerializedData());
            assertEquals("JSON", result.getFormat());
            assertNull(result.getErrorMessage());
        }

        @Test void failure() {
            var result = SerializationResult.failure("error");
            assertFalse(result.isSuccess());
            assertNull(result.getSerializedData());
            assertEquals("error", result.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("DeserializationResult Tests")
    class DeserializationResultTests {
        @Test void success() {
            TestDomainEntity e = new TestDomainEntity("test", "desc");
            var result = DeserializationResult.success(e);
            assertTrue(result.isSuccess());
            assertEquals(e, result.getEntity());
            assertNull(result.getErrorMessage());
        }

        @Test void failure() {
            var result = DeserializationResult.<TestDomainEntity>failure("error");
            assertFalse(result.isSuccess());
            assertNull(result.getEntity());
            assertEquals("error", result.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("TransformationMapping Tests")
    class TransformationMappingTests {
        @Test void allFields() {
            var mapping = new TransformationMapping(String.class, Integer.class, "map1",
                Map.of("s", "i"), true);
            assertEquals(String.class, mapping.getSourceClass());
            assertEquals(Integer.class, mapping.getTargetClass());
            assertEquals("map1", mapping.getMappingName());
            assertEquals(Map.of("s", "i"), mapping.getFieldMappings());
            assertTrue(mapping.isBidirectional());
        }

        @Test void notBidirectional() {
            var mapping = new TransformationMapping(String.class, Integer.class, "map1",
                Map.of(), false);
            assertFalse(mapping.isBidirectional());
        }
    }

    @Nested
    @DisplayName("RegistrationResult Tests")
    class RegistrationResultTests {
        @Test void success() {
            var result = RegistrationResult.success("map-123");
            assertTrue(result.isSuccess());
            assertEquals("map-123", result.getMappingId());
            assertNull(result.getErrorMessage());
        }

        @Test void failure() {
            var result = RegistrationResult.failure("already exists");
            assertFalse(result.isSuccess());
            assertNull(result.getMappingId());
            assertEquals("already exists", result.getErrorMessage());
        }
    }
}
