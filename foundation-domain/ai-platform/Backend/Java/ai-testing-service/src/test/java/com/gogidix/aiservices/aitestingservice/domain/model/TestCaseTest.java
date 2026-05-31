package com.gogidix.aiservices.aitestingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TestCase Domain Entity Tests")
class TestCaseTest {

    private static final String CASE_ID = "case-123";
    private static final String NAME = "Login Test";
    private static final String TEST_TYPE = "UNIT";
    private static final Map<String, Object> PARAMETERS = Map.of("username", "testuser");

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create test case with all fields")
        void shouldCreateWithAllFields() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getCaseId()).isEqualTo(CASE_ID);
            assertThat(testCase.getName()).isEqualTo(NAME);
            assertThat(testCase.getTestType()).isEqualTo(TEST_TYPE);
            assertThat(testCase.getParameters()).isEqualTo(PARAMETERS);
        }

        @Test
        @DisplayName("Should initialize with PENDING status")
        void shouldInitializeWithPendingStatus() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.PENDING);
        }

        @Test
        @DisplayName("Should accept null parameters")
        void shouldAcceptNullParameters() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, null);

            assertThat(testCase.getParameters()).isNull();
        }

        @Test
        @DisplayName("Should accept empty parameters")
        void shouldAcceptEmptyParameters() {
            Map<String, Object> emptyParams = new HashMap<>();
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, emptyParams);

            assertThat(testCase.getParameters()).isEmpty();
        }

        @Test
        @DisplayName("Should throw when caseId is null")
        void shouldThrowWhenCaseIdIsNull() {
            assertThatThrownBy(() -> new TestCase(null, NAME, TEST_TYPE, PARAMETERS))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThatThrownBy(() -> new TestCase(CASE_ID, null, TEST_TYPE, PARAMETERS))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw when testType is null")
        void shouldThrowWhenTestTypeIsNull() {
            assertThatThrownBy(() -> new TestCase(CASE_ID, NAME, null, PARAMETERS))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Test Execution Tests")
    class TestExecutionTests {

        @Test
        @DisplayName("Should pass test case")
        void shouldPassTestCase() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.pass("Test passed successfully");

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.PASSED);
            assertThat(testCase.getResult()).isEqualTo("Test passed successfully");
        }

        @Test
        @DisplayName("Should fail test case")
        void shouldFailTestCase() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.fail("Test failed with assertion error");

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.FAILED);
            assertThat(testCase.getResult()).isEqualTo("Test failed with assertion error");
        }

        @Test
        @DisplayName("Should set execution time")
        void shouldSetExecutionTime() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.setExecutionTimeMs(1500);

            assertThat(testCase.getExecutionTimeMs()).isEqualTo(1500);
        }

        @Test
        @DisplayName("Should track execution time")
        void shouldTrackExecutionTime() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.setExecutionTimeMs(2500);
            assertThat(testCase.getExecutionTimeMs()).isEqualTo(2500);

            testCase.setExecutionTimeMs(3000);
            assertThat(testCase.getExecutionTimeMs()).isEqualTo(3000);
        }

        @Test
        @DisplayName("Should allow zero execution time")
        void shouldAllowZeroExecutionTime() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.setExecutionTimeMs(0);

            assertThat(testCase.getExecutionTimeMs()).isZero();
        }
    }

    @Nested
    @DisplayName("Test Lifecycle Tests")
    class TestLifecycleTests {

        @Test
        @DisplayName("Should follow PENDING -> PASSED lifecycle")
        void shouldFollowPendingToPassedLifecycle() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.PENDING);

            testCase.pass("Success");

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.PASSED);
        }

        @Test
        @DisplayName("Should follow PENDING -> FAILED lifecycle")
        void shouldFollowPendingToFailedLifecycle() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.PENDING);

            testCase.fail("Error");

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.FAILED);
        }

        @Test
        @DisplayName("Should update result when passing")
        void shouldUpdateResultWhenPassing() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.pass("Custom success message");

            assertThat(testCase.getResult()).isEqualTo("Custom success message");
        }

        @Test
        @DisplayName("Should update result when failing")
        void shouldUpdateResultWhenFailing() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.fail("Custom failure message");

            assertThat(testCase.getResult()).isEqualTo("Custom failure message");
        }
    }

    @Nested
    @DisplayName("Parameters Tests")
    class ParametersTests {

        @Test
        @DisplayName("Should access parameters")
        void shouldAccessParameters() {
            Map<String, Object> params = Map.of(
                    "param1", "value1",
                    "param2", 100,
                    "param3", true
            );

            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, params);

            assertThat(testCase.getParameters()).hasSize(3);
            assertThat(testCase.getParameters().get("param1")).isEqualTo("value1");
            assertThat(testCase.getParameters().get("param2")).isEqualTo(100);
            assertThat(testCase.getParameters().get("param3")).isEqualTo(true);
        }

        @Test
        @DisplayName("Should handle null parameters")
        void shouldHandleNullParameters() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, null);

            assertThat(testCase.getParameters()).isNull();
        }

        @Test
        @DisplayName("Should handle string parameters")
        void shouldHandleStringParameters() {
            Map<String, Object> params = Map.of("input", "test data");

            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, params);

            assertThat(testCase.getParameters().get("input")).isEqualTo("test data");
        }

        @Test
        @DisplayName("Should handle numeric parameters")
        void shouldHandleNumericParameters() {
            Map<String, Object> params = Map.of("count", 5, "rate", 0.5);

            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, params);

            assertThat(testCase.getParameters().get("count")).isEqualTo(5);
            assertThat(testCase.getParameters().get("rate")).isEqualTo(0.5);
        }

        @Test
        @DisplayName("Should handle boolean parameters")
        void shouldHandleBooleanParameters() {
            Map<String, Object> params = Map.of("enabled", true, "flag", false);

            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, params);

            assertThat(testCase.getParameters().get("enabled")).isEqualTo(true);
            assertThat(testCase.getParameters().get("flag")).isEqualTo(false);
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get case ID")
        void shouldGetCaseId() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getCaseId()).isEqualTo(CASE_ID);
        }

        @Test
        @DisplayName("Should get name")
        void shouldGetName() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getName()).isEqualTo(NAME);
        }

        @Test
        @DisplayName("Should get test type")
        void shouldGetTestType() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getTestType()).isEqualTo(TEST_TYPE);
        }

        @Test
        @DisplayName("Should get status")
        void shouldGetStatus() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getStatus()).isEqualTo(TestStatus.PENDING);
        }

        @Test
        @DisplayName("Should get result")
        void shouldGetResult() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getResult()).isNull();
        }

        @Test
        @DisplayName("Should get execution time")
        void shouldGetExecutionTime() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getExecutionTimeMs()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("TestType Tests")
    class TestTypeTests {

        @Test
        @DisplayName("Should accept UNIT test type")
        void shouldAcceptUnitTestType() {
            TestCase testCase = new TestCase(CASE_ID, NAME, "UNIT", PARAMETERS);

            assertThat(testCase.getTestType()).isEqualTo("UNIT");
        }

        @Test
        @DisplayName("Should accept INTEGRATION test type")
        void shouldAcceptIntegrationTestType() {
            TestCase testCase = new TestCase(CASE_ID, NAME, "INTEGRATION", PARAMETERS);

            assertThat(testCase.getTestType()).isEqualTo("INTEGRATION");
        }

        @Test
        @DisplayName("Should accept E2E test type")
        void shouldAcceptE2ETestType() {
            TestCase testCase = new TestCase(CASE_ID, NAME, "E2E", PARAMETERS);

            assertThat(testCase.getTestType()).isEqualTo("E2E");
        }

        @Test
        @DisplayName("Should accept API test type")
        void shouldAcceptApiTestType() {
            TestCase testCase = new TestCase(CASE_ID, NAME, "API", PARAMETERS);

            assertThat(testCase.getTestType()).isEqualTo("API");
        }

        @Test
        @DisplayName("Should accept PERFORMANCE test type")
        void shouldAcceptPerformanceTestType() {
            TestCase testCase = new TestCase(CASE_ID, NAME, "PERFORMANCE", PARAMETERS);

            assertThat(testCase.getTestType()).isEqualTo("PERFORMANCE");
        }
    }

    @Nested
    @DisplayName("Result Tests")
    class ResultTests {

        @Test
        @DisplayName("Should store success result")
        void shouldStoreSuccessResult() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);
            String resultMessage = "Test completed successfully";

            testCase.pass(resultMessage);

            assertThat(testCase.getResult()).isEqualTo(resultMessage);
        }

        @Test
        @DisplayName("Should store failure result")
        void shouldStoreFailureResult() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);
            String resultMessage = "Assertion failed: expected true but was false";

            testCase.fail(resultMessage);

            assertThat(testCase.getResult()).isEqualTo(resultMessage);
        }

        @Test
        @DisplayName("Should update result on status change")
            void shouldUpdateResultOnStatusChange() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.pass("First result");
            assertThat(testCase.getResult()).isEqualTo("First result");

            testCase.fail("Second result");
            assertThat(testCase.getResult()).isEqualTo("Second result");
        }

        @Test
        @DisplayName("Should store null result before execution")
        void shouldStoreNullResultBeforeExecution() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getResult()).isNull();
        }
    }

    @Nested
    @DisplayName("Execution Time Tests")
    class ExecutionTimeTests {

        @Test
        @DisplayName("Should store execution time in milliseconds")
        void shouldStoreExecutionTimeInMilliseconds() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.setExecutionTimeMs(500);

            assertThat(testCase.getExecutionTimeMs()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should track execution time updates")
        void shouldTrackExecutionTimeUpdates() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.setExecutionTimeMs(1000);
            assertThat(testCase.getExecutionTimeMs()).isEqualTo(1000);

            testCase.setExecutionTimeMs(2000);
            assertThat(testCase.getExecutionTimeMs()).isEqualTo(2000);
        }

        @Test
        @DisplayName("Should allow setting very high execution time")
        void shouldAllowVeryHighExecutionTime() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.setExecutionTimeMs(3600000); // 1 hour

            assertThat(testCase.getExecutionTimeMs()).isEqualTo(3600000);
        }

        @Test
        @DisplayName("Should allow microsecond precision execution time")
        void shouldAllowMicrosecondPrecision() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.setExecutionTimeMs(1);

            assertThat(testCase.getExecutionTimeMs()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long case ID")
        void shouldHandleVeryLongCaseId() {
            String longId = "case-" + "a".repeat(200);

            TestCase testCase = new TestCase(longId, NAME, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getCaseId()).isEqualTo(longId);
        }

        @Test
        @DisplayName("Should handle very long name")
        void shouldHandleVeryLongName() {
            String longName = "This is a very long test case name that exceeds normal length ".repeat(3);

            TestCase testCase = new TestCase(CASE_ID, longName, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should handle unicode in name")
        void shouldHandleUnicodeInName() {
            String unicodeName = "テストケース";

            TestCase testCase = new TestCase(CASE_ID, unicodeName, TEST_TYPE, PARAMETERS);

            assertThat(testCase.getName()).isEqualTo(unicodeName);
        }

        @Test
        @DisplayName("Should handle special characters in test type")
        void shouldHandleSpecialCharactersInTestType() {
            TestCase testCase = new TestCase(CASE_ID, NAME, "API-E2E", PARAMETERS);

            assertThat(testCase.getTestType()).isEqualTo("API-E2E");
        }

        @Test
        @DisplayName("Should handle empty result message")
        void shouldHandleEmptyResultMessage() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.pass("");

            assertThat(testCase.getResult()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null result after execution")
        void shouldHandleNullResultAfterExecution() {
            TestCase testCase = new TestCase(CASE_ID, NAME, TEST_TYPE, PARAMETERS);

            testCase.pass(null);

            assertThat(testCase.getResult()).isNull();
        }
    }
}
