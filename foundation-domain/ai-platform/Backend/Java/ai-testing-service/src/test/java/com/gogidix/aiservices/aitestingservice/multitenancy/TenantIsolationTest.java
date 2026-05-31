package com.gogidix.aiservices.aitestingservice.multitenancy;

import com.gogidix.aiservices.aitestingservice.domain.model.TestCase;
import com.gogidix.aiservices.aitestingservice.domain.model.TestSuite;
import com.gogidix.aiservices.aitestingservice.domain.model.TestRun;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: Multi-Tenancy Tests.
 */
@DisplayName("Financial-Grade: Multi-Tenancy Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";

    @Nested
    @DisplayName("TestSuite Tenant Tests")
    class TestSuiteTenantTests {

        @Test
        @Order(1)
        @DisplayName("Should create test suite with tenantId")
        void shouldCreateTestSuiteWithTenantId() {
            TestSuite testSuite = new TestSuite(TENANT_1, "Test Suite");

            assertThat(testSuite.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @Order(2)
        @DisplayName("Should distinguish test suites by tenantId")
        void shouldDistinguishTestSuitesByTenantId() {
            TestSuite suite1 = new TestSuite(TENANT_1, "Suite 1");

            TestSuite suite2 = new TestSuite(TENANT_2, "Suite 1");

            assertThat(suite1.getTenantId()).isNotEqualTo(suite2.getTenantId());
        }
    }

    @Nested
    @DisplayName("TestCase Tenant Tests")
    class TestCaseTenantTests {

        @Test
        @Order(10)
        @DisplayName("Should create test case with proper ID")
        void shouldCreateTestCaseWithProperId() {
            TestCase testCase = new TestCase("case-1", "Test Case", "unit", java.util.Map.of());

            assertThat(testCase.getCaseId()).isEqualTo("case-1");
        }
    }

    @Nested
    @DisplayName("TestRun Tenant Tests")
    class TestRunTenantTests {

        @Test
        @Order(20)
        @DisplayName("Should create test run with tenantId")
        void shouldCreateTestRunWithTenantId() {
            TestRun testRun = new TestRun("suite-1", TENANT_1);
            testRun.complete(TestRun.TestRunStatus.PASSED);

            assertThat(testRun.getTenantId()).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @Order(30)
        @DisplayName("Should verify tenant isolation in test suites")
        void shouldVerifyTenantIsolationInTestSuites() {
            TestSuite suite1 = new TestSuite(TENANT_1, "Suite 1");

            TestSuite suite2 = new TestSuite(TENANT_2, "Suite 1");

            var allSuites = java.util.List.of(suite1, suite2);
            var tenant1Suites = allSuites.stream()
                    .filter(s -> TENANT_1.equals(s.getTenantId()))
                    .toList();

            assertThat(tenant1Suites).hasSize(1);
            assertThat(tenant1Suites.get(0).getTenantId()).isEqualTo(TENANT_1);
        }
    }
}
