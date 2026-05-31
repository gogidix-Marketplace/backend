package com.gogidix.dashboard.shared.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Nested
    @DisplayName("Builder and Constructor tests")
    class BuilderTests {

        @Test
        void builder_defaults_successIsTrue() {
            ApiResponse<String> response = ApiResponse.<String>builder().build();
            assertTrue(response.isSuccess());
        }

        @Test
        void builder_defaults_timestampIsSet() {
            ApiResponse<String> response = ApiResponse.<String>builder().build();
            assertNotNull(response.getTimestamp());
        }

        @Test
        void noArgsConstructor_createsInstance() {
            ApiResponse<String> response = new ApiResponse<>();
            assertNotNull(response);
        }

        @Test
        void allArgsConstructor_setsAllFields() {
            LocalDateTime now = LocalDateTime.now();
            List<ApiResponse.ApiError> errors = List.of(
                    ApiResponse.ApiError.builder().field("f").message("m").build()
            );
            ApiResponse<String> response = new ApiResponse<>(
                    true, "msg", "data", "ERR", errors, now, "corr", "/path"
            );
            assertTrue(response.isSuccess());
            assertEquals("msg", response.getMessage());
            assertEquals("data", response.getData());
            assertEquals("ERR", response.getErrorCode());
            assertEquals(errors, response.getErrors());
            assertEquals(now, response.getTimestamp());
            assertEquals("corr", response.getCorrelationId());
            assertEquals("/path", response.getPath());
        }

        @Test
        void setters_workCorrectly() {
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("error");
            response.setData("payload");
            response.setErrorCode("ERR001");
            response.setCorrelationId("corr-123");
            response.setPath("/api/test");

            assertFalse(response.isSuccess());
            assertEquals("error", response.getMessage());
            assertEquals("payload", response.getData());
            assertEquals("ERR001", response.getErrorCode());
            assertEquals("corr-123", response.getCorrelationId());
            assertEquals("/api/test", response.getPath());
        }
    }

    @Nested
    @DisplayName("success factory method tests")
    class SuccessFactoryTests {

        @Test
        void success_withData_returnsSuccessResponse() {
            ApiResponse<Integer> response = ApiResponse.success(42);
            assertTrue(response.isSuccess());
            assertEquals(42, response.getData());
        }

        @Test
        void success_withMessageAndData_returnsSuccessResponse() {
            ApiResponse<String> response = ApiResponse.success("Operation completed", "testData");
            assertTrue(response.isSuccess());
            assertEquals("Operation completed", response.getMessage());
            assertEquals("testData", response.getData());
        }

        @Test
        void success_withMessageOnly_returnsSuccessResponse() {
            ApiResponse<String> response = ApiResponse.success("Done");
            assertTrue(response.isSuccess());
            assertEquals("Done", response.getMessage());
            assertNull(response.getData());
        }
    }

    @Nested
    @DisplayName("error factory method tests")
    class ErrorFactoryTests {

        @Test
        void error_withCodeAndMessage_returnsErrorResponse() {
            ApiResponse<String> response = ApiResponse.error("ERR001", "Something went wrong");
            assertFalse(response.isSuccess());
            assertEquals("ERR001", response.getErrorCode());
            assertEquals("Something went wrong", response.getMessage());
        }

        @Test
        void error_withCodeMessageAndErrors_returnsErrorResponse() {
            List<ApiResponse.ApiError> errors = Arrays.asList(
                    ApiResponse.ApiError.builder().field("name").message("required").build()
            );
            ApiResponse<String> response = ApiResponse.error("ERR001", "Validation failed", errors);
            assertFalse(response.isSuccess());
            assertEquals("ERR001", response.getErrorCode());
            assertEquals("Validation failed", response.getMessage());
            assertEquals(1, response.getErrors().size());
        }
    }

    @Nested
    @DisplayName("ApiError tests")
    class ApiErrorTests {

        @Test
        void apiError_builder_worksCorrectly() {
            ApiResponse.ApiError error = ApiResponse.ApiError.builder()
                    .field("name")
                    .code("REQUIRED")
                    .message("Field is required")
                    .rejectedValue(null)
                    .build();
            assertEquals("name", error.getField());
            assertEquals("REQUIRED", error.getCode());
            assertEquals("Field is required", error.getMessage());
            assertNull(error.getRejectedValue());
        }

        @Test
        void apiError_noArgsConstructor() {
            ApiResponse.ApiError error = new ApiResponse.ApiError();
            assertNotNull(error);
        }

        @Test
        void apiError_allArgsConstructor() {
            ApiResponse.ApiError error = new ApiResponse.ApiError("f", "c", "m", "v");
            assertEquals("f", error.getField());
            assertEquals("c", error.getCode());
            assertEquals("m", error.getMessage());
            assertEquals("v", error.getRejectedValue());
        }

        @Test
        void apiError_setters_workCorrectly() {
            ApiResponse.ApiError error = new ApiResponse.ApiError();
            error.setField("field");
            error.setCode("CODE");
            error.setMessage("message");
            error.setRejectedValue("value");
            assertEquals("field", error.getField());
            assertEquals("CODE", error.getCode());
            assertEquals("message", error.getMessage());
            assertEquals("value", error.getRejectedValue());
        }
    }

    @Nested
    @DisplayName("Builder default branch tests")
    class BuilderDefaultTests {

        @Test
        void builder_withExplicitTimestamp_usesProvidedValue() {
            LocalDateTime ts = LocalDateTime.of(2025, 1, 1, 12, 0);
            ApiResponse<String> r = ApiResponse.<String>builder()
                    .timestamp(ts)
                    .build();
            assertEquals(ts, r.getTimestamp());
        }

        @Test
        void builder_withExplicitSuccessFalse_usesProvidedValue() {
            ApiResponse<String> r = ApiResponse.<String>builder()
                    .success(false)
                    .build();
            assertFalse(r.isSuccess());
        }

        @Test
        void builder_withExplicitSuccessTrue_usesProvidedValue() {
            ApiResponse<String> r = ApiResponse.<String>builder()
                    .success(true)
                    .build();
            assertTrue(r.isSuccess());
        }
    }

    @Nested
    @DisplayName("ApiResponse equals, hashCode, toString tests")
    class ApiResponseEqualsHashCodeTests {

        @Test
        void equals_sameInstance_returnsTrue() {
            ApiResponse<String> r = new ApiResponse<>();
            assertEquals(r, r);
        }

        @Test
        void equals_null_returnsFalse() {
            assertNotEquals(null, new ApiResponse<>());
        }

        @Test
        void equals_differentType_returnsFalse() {
            assertNotEquals(new Object(), new ApiResponse<>());
        }

        @Test
        void equals_equalObjects_returnsTrue() {
            LocalDateTime ts = LocalDateTime.now();
            ApiResponse<String> r1 = new ApiResponse<>(true, "m", "d", "e", null, ts, "c", "/p");
            ApiResponse<String> r2 = new ApiResponse<>(true, "m", "d", "e", null, ts, "c", "/p");
            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        void equals_differentSuccess_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(false, null, null, null, null, null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_nullMessage_vs_nonNullMessage_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, "msg", null, null, null, null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_differentMessage_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, "a", null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, "b", null, null, null, null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_nullData_vs_nonNullData_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, "data", null, null, null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_differentData_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, "a", null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, "b", null, null, null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_nullErrorCode_vs_nonNullErrorCode_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, "E", null, null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_differentErrorCode_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, "E1", null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, "E2", null, null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_nullErrors_vs_nonNullErrors_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, null, List.of(), null, null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_nullTimestamp_vs_nonNullTimestamp_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, null, null, LocalDateTime.now(), null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_differentTimestamp_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, LocalDateTime.now(), null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, null, null, LocalDateTime.now().plusDays(1), null, null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_nullCorrelationId_vs_nonNullCorrelationId_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, null, null, null, "c", null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_differentCorrelationId_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, "c1", null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, null, null, null, "c2", null);
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_nullPath_vs_nonNullPath_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, null, null, null, null, "/p");
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_differentPath_returnsFalse() {
            ApiResponse<String> r1 = new ApiResponse<>(true, null, null, null, null, null, null, "/a");
            ApiResponse<String> r2 = new ApiResponse<>(true, null, null, null, null, null, null, "/b");
            assertNotEquals(r1, r2);
        }

        @Test
        void equals_allNullFields_returnsTrue() {
            ApiResponse<String> r1 = new ApiResponse<>(false, null, null, null, null, null, null, null);
            ApiResponse<String> r2 = new ApiResponse<>(false, null, null, null, null, null, null, null);
            assertEquals(r1, r2);
        }

        @Test
        void hashCode_allNullFields_doesNotThrow() {
            assertDoesNotThrow(() -> new ApiResponse<>().hashCode());
        }

        @Test
        void hashCode_nonNullFields_doesNotThrow() {
            ApiResponse<String> r = new ApiResponse<>(true, "m", "d", "e", List.of(), LocalDateTime.now(), "c", "/p");
            assertDoesNotThrow(() -> r.hashCode());
        }

        @Test
        void toString_containsClassName() {
            String str = new ApiResponse<String>().toString();
            assertTrue(str.startsWith("ApiResponse"));
        }

        @Test
        void toString_containsFieldValues() {
            ApiResponse<String> r = new ApiResponse<>(true, "hello", "data", "ERR", null, null, "corr", "/path");
            String str = r.toString();
            assertTrue(str.contains("hello"));
            assertTrue(str.contains("data"));
            assertTrue(str.contains("ERR"));
            assertTrue(str.contains("corr"));
            assertTrue(str.contains("/path"));
        }
    }

    @Nested
    @DisplayName("ApiError equals, hashCode, toString tests")
    class ApiErrorEqualsHashCodeTests {

        @Test
        void apiError_equals_sameInstance_returnsTrue() {
            ApiResponse.ApiError e = new ApiResponse.ApiError();
            assertEquals(e, e);
        }

        @Test
        void apiError_equals_null_returnsFalse() {
            assertNotEquals(null, new ApiResponse.ApiError());
        }

        @Test
        void apiError_equals_differentType_returnsFalse() {
            assertNotEquals(new Object(), new ApiResponse.ApiError());
        }

        @Test
        void apiError_equals_equalObjects_returnsTrue() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError("f", "c", "m", "v");
            ApiResponse.ApiError e2 = new ApiResponse.ApiError("f", "c", "m", "v");
            assertEquals(e1, e2);
            assertEquals(e1.hashCode(), e2.hashCode());
        }

        @Test
        void apiError_equals_differentField_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError("a", null, null, null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError("b", null, null, null);
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_nullField_vs_nonNullField_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, null, null, null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError("f", null, null, null);
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_differentCode_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, "a", null, null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError(null, "b", null, null);
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_nullCode_vs_nonNullCode_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, null, null, null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError(null, "c", null, null);
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_differentMessage_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, null, "a", null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError(null, null, "b", null);
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_nullMessage_vs_nonNullMessage_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, null, null, null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError(null, null, "m", null);
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_differentRejectedValue_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, null, null, "a");
            ApiResponse.ApiError e2 = new ApiResponse.ApiError(null, null, null, "b");
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_nullRejectedValue_vs_nonNullRejectedValue_returnsFalse() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, null, null, null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError(null, null, null, "v");
            assertNotEquals(e1, e2);
        }

        @Test
        void apiError_equals_allNullFields_returnsTrue() {
            ApiResponse.ApiError e1 = new ApiResponse.ApiError(null, null, null, null);
            ApiResponse.ApiError e2 = new ApiResponse.ApiError(null, null, null, null);
            assertEquals(e1, e2);
        }

        @Test
        void apiError_hashCode_allNullFields_doesNotThrow() {
            assertDoesNotThrow(() -> new ApiResponse.ApiError().hashCode());
        }

        @Test
        void apiError_hashCode_nonNullFields_doesNotThrow() {
            ApiResponse.ApiError e = new ApiResponse.ApiError("f", "c", "m", "v");
            assertDoesNotThrow(() -> e.hashCode());
        }

        @Test
        void apiError_toString_containsClassName() {
            String str = new ApiResponse.ApiError().toString();
            assertTrue(str.startsWith("ApiResponse.ApiError"));
        }

        @Test
        void apiError_toString_containsFieldValues() {
            ApiResponse.ApiError e = new ApiResponse.ApiError("fname", "code1", "msg1", "val1");
            String str = e.toString();
            assertTrue(str.contains("fname"));
            assertTrue(str.contains("code1"));
            assertTrue(str.contains("msg1"));
            assertTrue(str.contains("val1"));
        }
    }
}
