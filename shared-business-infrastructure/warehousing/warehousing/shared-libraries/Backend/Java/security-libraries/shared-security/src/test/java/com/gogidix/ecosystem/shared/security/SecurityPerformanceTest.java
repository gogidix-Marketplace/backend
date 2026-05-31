package com.gogidix.ecosystem.shared.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

/**
 * Performance tests for Security components.
 * Tests the performance characteristics of JWT operations.
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@DisplayName("Security Performance Tests")
class SecurityPerformanceTest {

    private JwtTokenProvider jwtTokenProvider;
    private UserPrincipal userPrincipal;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", 
            "performance-test-secret-key-that-is-at-least-256-bits-long-for-testing");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInSeconds", 3600);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtRefreshExpirationInSeconds", 7200);

        userPrincipal = UserPrincipal.create(
            1L, "perfuser", "perf@example.com", "password",
            Arrays.asList("USER", "ADMIN"), true
        );

        authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal, null, userPrincipal.getAuthorities()
        );
    }

    @Test
    @DisplayName("Performance: JWT token generation")
    void performanceJwtTokenGeneration() {
        int iterations = 1000;
        
        Instant start = Instant.now();
        
        for (int i = 0; i < iterations; i++) {
            String token = jwtTokenProvider.generateToken(authentication);
            assertThat(token).isNotNull();
        }
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        System.out.println("Generated " + iterations + " tokens in " + duration.toMillis() + "ms");
        System.out.println("Average time per token: " + (duration.toMillis() / (double) iterations) + "ms");
        
        // Assert that token generation is reasonably fast (should be under 10ms per token on average)
        assertThat(duration.toMillis() / (double) iterations).isLessThan(10.0);
    }

    @Test
    @DisplayName("Performance: JWT token validation")
    void performanceJwtTokenValidation() {
        // Generate a token first
        String token = jwtTokenProvider.generateToken(authentication);
        int iterations = 1000;
        
        Instant start = Instant.now();
        
        for (int i = 0; i < iterations; i++) {
            boolean isValid = jwtTokenProvider.validateToken(token);
            assertThat(isValid).isTrue();
        }
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        System.out.println("Validated " + iterations + " tokens in " + duration.toMillis() + "ms");
        System.out.println("Average time per validation: " + (duration.toMillis() / (double) iterations) + "ms");
        
        // Assert that token validation is reasonably fast (should be under 5ms per validation on average)
        assertThat(duration.toMillis() / (double) iterations).isLessThan(5.0);
    }

    @Test
    @DisplayName("Performance: JWT token parsing")
    void performanceJwtTokenParsing() {
        // Generate a token first
        String token = jwtTokenProvider.generateToken(authentication);
        int iterations = 1000;
        
        Instant start = Instant.now();
        
        for (int i = 0; i < iterations; i++) {
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            String username = jwtTokenProvider.getUsernameFromToken(token);
            assertThat(userId).isEqualTo("1");
            assertThat(username).isEqualTo("perfuser");
        }
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        System.out.println("Parsed " + iterations + " tokens in " + duration.toMillis() + "ms");
        System.out.println("Average time per parse: " + (duration.toMillis() / (double) iterations) + "ms");
        
        // Assert that token parsing is reasonably fast (should be under 5ms per parse on average)
        assertThat(duration.toMillis() / (double) iterations).isLessThan(5.0);
    }

    @Test
    @DisplayName("Performance: Concurrent token operations")
    void performanceConcurrentTokenOperations() throws InterruptedException {
        int threadCount = 10;
        int operationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        Instant start = Instant.now();
        
        CompletableFuture<Void>[] futures = IntStream.range(0, threadCount)
            .mapToObj(i -> CompletableFuture.runAsync(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    // Generate token
                    String token = jwtTokenProvider.generateToken(authentication);
                    
                    // Validate token
                    boolean isValid = jwtTokenProvider.validateToken(token);
                    assertThat(isValid).isTrue();
                    
                    // Parse token
                    String userId = jwtTokenProvider.getUserIdFromToken(token);
                    assertThat(userId).isEqualTo("1");
                }
            }, executor))
            .toArray(CompletableFuture[]::new);
        
        CompletableFuture.allOf(futures).join();
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        int totalOperations = threadCount * operationsPerThread * 3; // 3 operations per iteration
        System.out.println("Performed " + totalOperations + " concurrent operations in " + duration.toMillis() + "ms");
        System.out.println("Average time per operation: " + (duration.toMillis() / (double) totalOperations) + "ms");
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Assert that concurrent operations complete within reasonable time
        assertThat(duration.toMillis()).isLessThan(10000); // Should complete within 10 seconds
    }

    @Test
    @DisplayName("Performance: Memory usage during token operations")
    void performanceMemoryUsageDuringTokenOperations() {
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection before test
        System.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        int iterations = 10000;
        String[] tokens = new String[iterations];
        
        // Generate many tokens
        for (int i = 0; i < iterations; i++) {
            tokens[i] = jwtTokenProvider.generateToken(authentication);
        }
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;
        
        System.out.println("Memory used for " + iterations + " tokens: " + (memoryUsed / 1024 / 1024) + " MB");
        System.out.println("Average memory per token: " + (memoryUsed / iterations) + " bytes");
        
        // Validate all tokens to ensure they're valid
        for (String token : tokens) {
            assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        }
        
        // Assert that memory usage is reasonable (should be less than 100MB for 10k tokens)
        // Note: Memory usage can vary significantly in CI environments due to JVM memory management
        assertThat(memoryUsed / 1024 / 1024).isLessThan(100);
    }

    @Test
    @DisplayName("Performance: Token refresh operations")
    void performanceTokenRefreshOperations() {
        // Generate initial token
        String originalToken = jwtTokenProvider.generateToken(authentication);
        int iterations = 500;
        
        Instant start = Instant.now();
        
        String currentToken = originalToken;
        for (int i = 0; i < iterations; i++) {
            currentToken = jwtTokenProvider.refreshToken(currentToken);
            assertThat(jwtTokenProvider.validateToken(currentToken)).isTrue();
        }
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        System.out.println("Refreshed token " + iterations + " times in " + duration.toMillis() + "ms");
        System.out.println("Average time per refresh: " + (duration.toMillis() / (double) iterations) + "ms");
        
        // Assert that token refresh is reasonably fast (should be under 20ms per refresh on average)
        assertThat(duration.toMillis() / (double) iterations).isLessThan(20.0);
    }

    @Test
    @DisplayName("Performance: UserPrincipal creation")
    void performanceUserPrincipalCreation() {
        int iterations = 10000;
        
        Instant start = Instant.now();
        
        for (int i = 0; i < iterations; i++) {
            UserPrincipal principal = UserPrincipal.create(
                (long) i, "user" + i, "user" + i + "@example.com", "password",
                Arrays.asList("USER"), true
            );
            assertThat(principal).isNotNull();
            assertThat(principal.getId()).isEqualTo((long) i);
        }
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        System.out.println("Created " + iterations + " UserPrincipal objects in " + duration.toMillis() + "ms");
        System.out.println("Average time per creation: " + (duration.toMillis() / (double) iterations) + "ms");
        
        // Assert that UserPrincipal creation is very fast (should be under 1ms per creation on average)
        assertThat(duration.toMillis() / (double) iterations).isLessThan(1.0);
    }

    @Test
    @DisplayName("Performance: Large payload token handling")
    void performanceLargePayloadTokenHandling() {
        // Create user principal with many roles (large payload)
        StringBuilder largeRoleList = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            largeRoleList.append("ROLE_").append(i);
            if (i < 99) largeRoleList.append(",");
        }
        
        UserPrincipal largePrincipal = UserPrincipal.create(
            1L, "largeuser", "large@example.com", "password",
            Arrays.asList(largeRoleList.toString().split(",")), true
        );
        
        Authentication largeAuth = new UsernamePasswordAuthenticationToken(
            largePrincipal, null, largePrincipal.getAuthorities()
        );
        
        int iterations = 100;
        
        Instant start = Instant.now();
        
        for (int i = 0; i < iterations; i++) {
            String token = jwtTokenProvider.generateToken(largeAuth);
            boolean isValid = jwtTokenProvider.validateToken(token);
            assertThat(isValid).isTrue();
            
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            assertThat(userId).isEqualTo("1");
        }
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        System.out.println("Processed " + iterations + " large payload tokens in " + duration.toMillis() + "ms");
        System.out.println("Average time per large token operation: " + (duration.toMillis() / (double) iterations) + "ms");
        
        // Assert that large payload handling is still reasonably fast (should be under 50ms per operation on average)
        assertThat(duration.toMillis() / (double) iterations).isLessThan(50.0);
    }
}