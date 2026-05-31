package com.gogidix.aiservices.aifrauddetectionservice.multitenancy;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.response.AnalysisResponse;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.*;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.FraudRepository;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.MlModelPort;
import com.gogidix.aiservices.aifrauddetectionservice.domain.policy.FraudDetectionPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Multi-Tenancy Tenant Isolation Tests")
class TenantIsolationTest {

    @Mock
    private FraudRepository fraudRepository;

    @Mock
    private MlModelPort mlModelPort;

    @Mock
    private FraudDetectionPolicy policy;

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";
    private static final String USER_1_TENANT_1 = "user-t1-001";
    private static final String USER_1_TENANT_2 = "user-t2-001";
    private static final String TRANSACTION_ID = "txn-123";
    private static final Instant TIMESTAMP = Instant.now();

    @Nested
    @DisplayName("Transaction Domain Model Tenant Tests")
    class TransactionTenantTests {

        @Test
        @DisplayName("Should create transaction with tenantId")
        void shouldCreateTransactionWithTenantId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThat(transaction.getTenantId()).isEqualTo(TENANT_1);
            assertThat(transaction.getUserId()).isEqualTo(USER_1_TENANT_1);
        }

        @Test
        @DisplayName("Should create transaction with null tenantId")
        void shouldCreateTransactionWithNullTenantId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(null)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThat(transaction.getTenantId()).isNull();
        }

        @Test
        @DisplayName("Should create transaction with empty tenantId")
        void shouldCreateTransactionWithEmptyTenantId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId("")
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThat(transaction.getTenantId()).isEmpty();
        }

        @Test
        @DisplayName("Should distinguish transactions by tenantId")
        void shouldDistinguishTransactionsByTenantId() {
            Transaction transaction1 = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            Transaction transaction2 = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_2)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThat(transaction1.getTenantId()).isNotEqualTo(transaction2.getTenantId());
        }

        @Test
        @DisplayName("Should allow same userId across different tenants")
        void shouldAllowSameUserIdAcrossDifferentTenants() {
            String sameUserId = "user-001";

            Transaction transaction1 = Transaction.builder()
                    .transactionId("txn-1")
                    .userId(sameUserId)
                    .tenantId(TENANT_1)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            Transaction transaction2 = Transaction.builder()
                    .transactionId("txn-2")
                    .userId(sameUserId)
                    .tenantId(TENANT_2)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThat(transaction1.getUserId()).isEqualTo(transaction2.getUserId());
            assertThat(transaction1.getTenantId()).isNotEqualTo(transaction2.getTenantId());
        }
    }

    @Nested
    @DisplayName("FraudAnalysisResult Tenant Tests")
    class FraudAnalysisResultTenantTests {

        @Test
        @DisplayName("Should create analysis result with tenantId")
        void shouldCreateAnalysisResultWithTenantId() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should create analysis result with null tenantId")
        void shouldCreateAnalysisResultWithNullTenantId() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(null)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.getTenantId()).isNull();
        }

        @Test
        @DisplayName("Should preserve tenantId through business methods")
        void shouldPreserveTenantIdThroughBusinessMethods() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("High fraud"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.getTenantId()).isEqualTo(TENANT_1);
            assertThat(result.isFraudulent()).isTrue();
            assertThat(result.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should distinguish analysis results by tenantId")
        void shouldDistinguishAnalysisResultsByTenantId() {
            FraudAnalysisResult result1 = FraudAnalysisResult.builder()
                    .analysisId("analysis-1")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            FraudAnalysisResult result2 = FraudAnalysisResult.builder()
                    .analysisId("analysis-2")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_2)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result1.getTenantId()).isNotEqualTo(result2.getTenantId());
        }
    }

    @Nested
    @DisplayName("FraudPattern Tenant Tests")
    class FraudPatternTenantTests {

        @Test
        @DisplayName("Should create fraud pattern with tenantId")
        void shouldCreateFraudPatternWithTenantId() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("pattern-123")
                    .patternName("Test Pattern")
                    .description("Test description")
                    .tenantId(TENANT_1)
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            assertThat(pattern.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should create fraud pattern with null tenantId")
        void shouldCreateFraudPatternWithNullTenantId() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("pattern-123")
                    .patternName("Test Pattern")
                    .description("Test description")
                    .tenantId(null)
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            assertThat(pattern.getTenantId()).isNull();
        }

        @Test
        @DisplayName("Should distinguish patterns by tenantId")
        void shouldDistinguishPatternsByTenantId() {
            FraudPattern pattern1 = FraudPattern.builder()
                    .patternId("pattern-1")
                    .patternName("Pattern 1")
                    .description("Description 1")
                    .tenantId(TENANT_1)
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            FraudPattern pattern2 = FraudPattern.builder()
                    .patternId("pattern-2")
                    .patternName("Pattern 2")
                    .description("Description 2")
                    .tenantId(TENANT_2)
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            assertThat(pattern1.getTenantId()).isNotEqualTo(pattern2.getTenantId());
        }
    }

    @Nested
    @DisplayName("AnalysisResponse DTO Tenant Tests")
    class AnalysisResponseTenantTests {

        @Test
        @DisplayName("Should map tenantId from domain to response")
        void shouldMapTenantIdFromDomainToResponse() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should map null tenantId from domain to response")
        void shouldMapNullTenantIdFromDomainToResponse() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(null)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getTenantId()).isNull();
        }

        @Test
        @DisplayName("Should build response with tenantId")
        void shouldBuildResponseWithTenantId() {
            AnalysisResponse response = AnalysisResponse.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .action(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(response.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should set and get tenantId via setter")
        void shouldSetAndGetTenantIdViaSetter() {
            AnalysisResponse response = new AnalysisResponse();
            response.setTenantId(TENANT_1);

            assertThat(response.getTenantId()).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @DisplayName("Should verify tenant isolation in analysis results")
        void shouldVerifyTenantIsolationInAnalysisResults() {
            FraudAnalysisResult result1 = FraudAnalysisResult.builder()
                    .analysisId("analysis-1")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            FraudAnalysisResult result2 = FraudAnalysisResult.builder()
                    .analysisId("analysis-2")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_2)
                    .tenantId(TENANT_2)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            // Verify that results are distinct by tenant
            assertThat(result1.getTenantId()).isNotEqualTo(result2.getTenantId());
            assertThat(result1.getUserId()).isNotEqualTo(result2.getUserId());

            // Simulate filtering by tenant
            List<FraudAnalysisResult> allResults = List.of(result1, result2);
            List<FraudAnalysisResult> tenant1Results = allResults.stream()
                    .filter(r -> TENANT_1.equals(r.getTenantId()))
                    .toList();

            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant1Results.get(0).getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should verify tenant isolation in patterns")
        void shouldVerifyTenantIsolationInPatterns() {
            FraudPattern pattern1 = FraudPattern.builder()
                    .patternId("pattern-1")
                    .patternName("Pattern 1")
                    .description("Description 1")
                    .tenantId(TENANT_1)
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            FraudPattern pattern2 = FraudPattern.builder()
                    .patternId("pattern-2")
                    .patternName("Pattern 2")
                    .description("Description 2")
                    .tenantId(TENANT_2)
                    .confidenceScore(0.9)
                    .lastSeen(Instant.now())
                    .occurrenceCount(10)
                    .build();

            // Verify that patterns are distinct by tenant
            assertThat(pattern1.getTenantId()).isNotEqualTo(pattern2.getTenantId());

            // Simulate filtering by tenant
            List<FraudPattern> allPatterns = List.of(pattern1, pattern2);
            List<FraudPattern> tenant1Patterns = allPatterns.stream()
                    .filter(p -> TENANT_1.equals(p.getTenantId()))
                    .toList();

            assertThat(tenant1Patterns).hasSize(1);
            assertThat(tenant1Patterns.get(0).getTenantId()).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Repository Tenant Filtering Tests")
    class RepositoryTenantFilteringTests {

        @Test
        @DisplayName("Should demonstrate tenant filtering concept")
        void shouldDemonstrateTenantFilteringConcept() {
            // Create analysis results for different tenants
            FraudAnalysisResult result1 = FraudAnalysisResult.builder()
                    .analysisId("analysis-1")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            FraudAnalysisResult result2 = FraudAnalysisResult.builder()
                    .analysisId("analysis-2")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_2)
                    .tenantId(TENANT_2)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            // Simulate repository filtering by tenant
            List<FraudAnalysisResult> allResults = List.of(result1, result2);

            // Filter for tenant 1
            List<FraudAnalysisResult> tenant1Results = allResults.stream()
                    .filter(r -> TENANT_1.equals(r.getTenantId()))
                    .toList();

            // Filter for tenant 2
            List<FraudAnalysisResult> tenant2Results = allResults.stream()
                    .filter(r -> TENANT_2.equals(r.getTenantId()))
                    .toList();

            // Verify isolation
            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant2Results).hasSize(1);
            assertThat(tenant1Results.get(0).getTenantId()).isEqualTo(TENANT_1);
            assertThat(tenant2Results.get(0).getTenantId()).isEqualTo(TENANT_2);
        }

        @Test
        @DisplayName("Should handle null tenantId in filtering")
        void shouldHandleNullTenantIdInFiltering() {
            FraudAnalysisResult resultWithTenant = FraudAnalysisResult.builder()
                    .analysisId("analysis-1")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            FraudAnalysisResult resultWithoutTenant = FraudAnalysisResult.builder()
                    .analysisId("analysis-2")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_2)
                    .tenantId(null)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            List<FraudAnalysisResult> allResults = List.of(resultWithTenant, resultWithoutTenant);

            // Filter for non-null tenant
            List<FraudAnalysisResult> withTenant = allResults.stream()
                    .filter(r -> r.getTenantId() != null)
                    .toList();

            // Filter for null tenant
            List<FraudAnalysisResult> withoutTenant = allResults.stream()
                    .filter(r -> r.getTenantId() == null)
                    .toList();

            assertThat(withTenant).hasSize(1);
            assertThat(withoutTenant).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Domain Model Tenant Field Presence Tests")
    class DomainModelTenantFieldPresenceTests {

        @Test
        @DisplayName("Should verify Transaction has tenantId field")
        void shouldVerifyTransactionHasTenantIdField() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThat(transaction.getTenantId()).isNotNull();
            assertThat(transaction.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should verify FraudAnalysisResult has tenantId field")
        void shouldVerifyFraudAnalysisResultHasTenantIdField() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.getTenantId()).isNotNull();
            assertThat(result.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should verify FraudPattern has tenantId field")
        void shouldVerifyFraudPatternHasTenantIdField() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("pattern-123")
                    .patternName("Test Pattern")
                    .description("Test description")
                    .tenantId(TENANT_1)
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            assertThat(pattern.getTenantId()).isNotNull();
            assertThat(pattern.getTenantId()).isEqualTo(TENANT_1);
        }

        @Test
        @DisplayName("Should verify AnalysisResponse has tenantId field")
        void shouldVerifyAnalysisResponseHasTenantIdField() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_1_TENANT_1)
                    .tenantId(TENANT_1)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getTenantId()).isNotNull();
            assertThat(response.getTenantId()).isEqualTo(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Tenant Context Thread Safety Tests")
    class TenantContextThreadSafetyTests {

        @Test
        @DisplayName("Should support multiple tenants concurrently")
        void shouldSupportMultipleTenantsConcurrently() throws InterruptedException {
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            final boolean[] errors = {false};
            final String[] results = new String[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                final String tenantId = "tenant-" + (i + 1);

                threads[i] = new Thread(() -> {
                    try {
                        Transaction transaction = Transaction.builder()
                                .transactionId("txn-" + index)
                                .userId("user-" + index)
                                .tenantId(tenantId)
                                .amount(new BigDecimal("100"))
                                .merchant("Test Merchant")
                                .timestamp(Instant.now())
                                .currency("USD")
                                .build();

                        results[index] = new StringBuilder()
                                .append("Thread: ").append(index)
                                .append(", Tenant: ").append(transaction.getTenantId())
                                .toString();
                    } catch (Exception e) {
                        errors[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errors[0]).isFalse();

            // Verify each thread had its own tenant
            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isNotNull();
                assertThat(results[i].toString()).contains("tenant-" + (i + 1));
            }
        }
    }
}
