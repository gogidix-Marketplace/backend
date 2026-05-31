package com.gogidix.shared.model.adapter.in.web;

import com.gogidix.shared.model.adapter.in.web.ModelTransformationController.*;
import com.gogidix.shared.model.application.port.in.ModelTransformationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Controller DTO Tests")
class ControllerDtoTest {

    @Nested
    @DisplayName("ValidationResponse Tests")
    class ValidationResponseTests {
        @Test void fromValidResult() {
            var result = ModelTransformationUseCase.ValidationResult.valid();
            var response = ValidationResponse.from(result);
            assertTrue(response.isValid());
            assertTrue(response.getErrors().isEmpty());
            assertTrue(response.getWarnings().isEmpty());
        }

        @Test void fromInvalidResult() {
            var result = ModelTransformationUseCase.ValidationResult.invalid(List.of("err"));
            var response = ValidationResponse.from(result);
            assertFalse(response.isValid());
            assertEquals(List.of("err"), response.getErrors());
        }
    }

    @Nested
    @DisplayName("BatchValidationResponse Tests")
    class BatchValidationResponseTests {
        @Test void fromResult() {
            var r1 = ModelTransformationUseCase.ValidationResult.valid();
            var r2 = ModelTransformationUseCase.ValidationResult.invalid(List.of("err"), List.of("warn"));
            var batch = new ModelTransformationUseCase.BatchValidationResult(List.of(r1, r2));
            var response = BatchValidationResponse.from(batch);
            assertEquals(2, response.getTotalCount());
            assertEquals(1, response.getValidCount());
            assertEquals(1, response.getInvalidCount());
            assertEquals(2, response.getResults().size());
            assertFalse(response.getAllErrors().isEmpty());
            assertFalse(response.getAllWarnings().isEmpty());
        }
    }

    @Nested
    @DisplayName("SerializationResponse Tests")
    class SerializationResponseTests {
        @Test void fromSuccess() {
            var result = ModelTransformationUseCase.SerializationResult.success("data", "JSON");
            var response = SerializationResponse.from(result);
            assertTrue(response.isSuccess());
            assertEquals("data", response.getSerializedData());
            assertEquals("JSON", response.getFormat());
            assertNull(response.getErrorMessage());
        }

        @Test void fromFailure() {
            var result = ModelTransformationUseCase.SerializationResult.failure("err");
            var response = SerializationResponse.from(result);
            assertFalse(response.isSuccess());
            assertEquals("err", response.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("DeserializationResponse Tests")
    class DeserializationResponseTests {
        @Test void fromFailure() {
            var result = ModelTransformationUseCase.DeserializationResult.failure("err");
            var response = DeserializationResponse.from(result);
            assertFalse(response.isSuccess());
            assertEquals("err", response.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("ComparisonResponse Tests")
    class ComparisonResponseTests {
        @Test void fromIdentical() {
            var result = new ModelTransformationUseCase.ComparisonResult(true, Map.of(), 1.0);
            var response = ComparisonResponse.from(result);
            assertTrue(response.isIdentical());
            assertEquals(1.0, response.getSimilarityScore());
            assertTrue(response.getDifferences().isEmpty());
        }

        @Test void fromDifferent() {
            var diff = new ModelTransformationUseCase.ComparisonResult.FieldDifference("f", "a", "b");
            var result = new ModelTransformationUseCase.ComparisonResult(false,
                Map.of("f", diff), 0.5);
            var response = ComparisonResponse.from(result);
            assertFalse(response.isIdentical());
            assertEquals(1, response.getDifferences().size());
        }
    }

    @Nested
    @DisplayName("FieldDifferenceResponse Tests")
    class FieldDifferenceResponseTests {
        @Test void fromDifference() {
            var diff = new ModelTransformationUseCase.ComparisonResult.FieldDifference("name", "old", "new");
            var response = ComparisonResponse.FieldDifferenceResponse.from(diff);
            assertEquals("name", response.getFieldName());
            assertEquals("old", response.getOldValue());
            assertEquals("new", response.getNewValue());
        }
    }

    @Nested
    @DisplayName("CopyResponse Tests")
    class CopyResponseTests {
        @Test void fromFailure() {
            var result = ModelTransformationUseCase.CopyResult.failure("err");
            var response = CopyResponse.from(result);
            assertFalse(response.isSuccess());
            assertEquals("err", response.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("MergeResponse Tests")
    class MergeResponseTests {
        @Test void fromFailure() {
            var result = ModelTransformationUseCase.MergeResult.failure("err");
            var response = MergeResponse.from(result);
            assertFalse(response.isSuccess());
            assertEquals("err", response.getErrorMessage());
            assertTrue(response.getChangedFields().isEmpty());
        }
    }

    @Nested
    @DisplayName("TransformationMappingResponse Tests")
    class TransformationMappingResponseTests {
        @Test void fromMapping() {
            var mapping = new ModelTransformationUseCase.TransformationMapping(
                String.class, Integer.class, "test", Map.of("s", "i"), true);
            var response = TransformationMappingResponse.from(mapping);
            assertEquals("String", response.getSourceClass());
            assertEquals("Integer", response.getTargetClass());
            assertEquals("test", response.getMappingName());
            assertTrue(response.isBidirectional());
            assertEquals(Map.of("s", "i"), response.getFieldMappings());
        }
    }

    @Nested
    @DisplayName("RegistrationResponse Tests")
    class RegistrationResponseTests {
        @Test void fromSuccess() {
            var result = ModelTransformationUseCase.RegistrationResult.success("id-123");
            var response = RegistrationResponse.from(result);
            assertTrue(response.isSuccess());
            assertEquals("id-123", response.getMappingId());
            assertNull(response.getErrorMessage());
        }

        @Test void fromFailure() {
            var result = ModelTransformationUseCase.RegistrationResult.failure("err");
            var response = RegistrationResponse.from(result);
            assertFalse(response.isSuccess());
            assertEquals("err", response.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("Request DTO Tests")
    class RequestDtoTests {
        @Test void validateEntityRequest() {
            var req = new ValidateEntityRequest();
            assertNull(req.getEntity());
            req.setEntity(null);
            assertNull(req.getEntity());
        }

        @Test void validateEntitiesRequest() {
            var req = new ValidateEntitiesRequest();
            assertNull(req.getEntities());
            req.setEntities(List.of());
            assertEquals(List.of(), req.getEntities());
        }

        @Test void serializeEntityRequest() {
            var req = new SerializeEntityRequest();
            assertNull(req.getEntity());
            req.setEntity(null);
            assertNull(req.getEntity());
        }

        @Test void deserializeEntityRequest() {
            var req = new DeserializeEntityRequest();
            assertNull(req.getJson());
            assertNull(req.getEntityClass());
            req.setJson("test");
            req.setEntityClass(null);
            assertEquals("test", req.getJson());
        }

        @Test void compareEntitiesRequest() {
            var req = new CompareEntitiesRequest();
            assertNull(req.getEntity1());
            assertNull(req.getEntity2());
        }

        @Test void copyEntityRequest() {
            var req = new CopyEntityRequest();
            assertNull(req.getEntity());
        }

        @Test void mergeEntitiesRequest() {
            var req = new MergeEntitiesRequest();
            assertNull(req.getTarget());
            assertNull(req.getSource());
            assertNull(req.getUpdatedBy());
            req.setUpdatedBy("admin");
            assertEquals("admin", req.getUpdatedBy());
        }

        @Test void registerMappingRequest() {
            var req = new RegisterMappingRequest();
            assertNull(req.getSourceClass());
            assertNull(req.getTargetClass());
            assertNull(req.getMappingName());
            assertNull(req.getFieldMappings());
            assertFalse(req.isBidirectional());
            req.setBidirectional(true);
            assertTrue(req.isBidirectional());
            req.setFieldMappings(Map.of("a", "b"));
            assertEquals(Map.of("a", "b"), req.getFieldMappings());
            req.setMappingName("test");
            assertEquals("test", req.getMappingName());
        }
    }
}
