package com.gogidix.ecosystem.shared.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * End-to-End tests for Security components.
 * Tests the complete security flow with real HTTP requests.
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@org.junit.jupiter.api.Disabled("E2E tests disabled for library module - use in actual applications")
@DisplayName("Security E2E Tests")
class SecurityE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    @DisplayName("E2E: Complete authentication flow")
    void e2eCompleteAuthenticationFlow() {
        // Step 1: Access public endpoint (should work)
        ResponseEntity<Map> publicResponse = restTemplate.getForEntity(
                getBaseUrl() + "/api/health/status", Map.class);
        
        assertThat(publicResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(publicResponse.getBody()).containsKey("status");

        // Step 2: Try to access protected endpoint without token (should fail)
        ResponseEntity<Map> unauthorizedResponse = restTemplate.getForEntity(
                getBaseUrl() + "/api/protected/resource", Map.class);
        
        assertThat(unauthorizedResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Step 3: Generate valid token
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "testuser", "test@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // Step 4: Access protected endpoint with valid token (should work)
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> authorizedResponse = restTemplate.exchange(
                getBaseUrl() + "/api/protected/resource",
                HttpMethod.GET,
                entity,
                Map.class
        );

        assertThat(authorizedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorizedResponse.getBody()).containsKey("message");
    }

    @Test
    @DisplayName("E2E: Token refresh flow")
    void e2eTokenRefreshFlow() {
        // Step 1: Generate refresh token
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "testuser", "test@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Step 2: Use refresh token to get new access token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> refreshRequest = Map.of("refreshToken", refreshToken);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(refreshRequest, headers);

        ResponseEntity<Map> refreshResponse = restTemplate.postForEntity(
                getBaseUrl() + "/api/auth/refresh",
                entity,
                Map.class
        );

        assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(refreshResponse.getBody()).containsKey("accessToken");

        // Step 3: Use new access token to access protected resource
        String newAccessToken = (String) refreshResponse.getBody().get("accessToken");
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(newAccessToken);
        HttpEntity<String> authEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<Map> protectedResponse = restTemplate.exchange(
                getBaseUrl() + "/api/protected/resource",
                HttpMethod.GET,
                authEntity,
                Map.class
        );

        assertThat(protectedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("E2E: Role-based access control flow")
    void e2eRoleBasedAccessControlFlow() {
        // Step 1: Create user with USER role
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "user", "user@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        String userToken = jwtTokenProvider.generateToken(userAuth);

        // Step 2: Create user with ADMIN role
        UserPrincipal adminPrincipal = UserPrincipal.create(
                2L, "admin", "admin@example.com", "password",
                Arrays.asList("ADMIN"), true
        );
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
                adminPrincipal, null, adminPrincipal.getAuthorities()
        );
        String adminToken = jwtTokenProvider.generateToken(adminAuth);

        // Step 3: User tries to access admin endpoint (should fail)
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<String> userEntity = new HttpEntity<>(userHeaders);

        ResponseEntity<Map> userResponse = restTemplate.exchange(
                getBaseUrl() + "/api/admin/users",
                HttpMethod.GET,
                userEntity,
                Map.class
        );

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        // Step 4: Admin accesses admin endpoint (should work)
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setBearerAuth(adminToken);
        HttpEntity<String> adminEntity = new HttpEntity<>(adminHeaders);

        ResponseEntity<Map> adminResponse = restTemplate.exchange(
                getBaseUrl() + "/api/admin/users",
                HttpMethod.GET,
                adminEntity,
                Map.class
        );

        assertThat(adminResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(adminResponse.getBody()).containsKey("users");
    }

    @Test
    @DisplayName("E2E: Invalid token scenarios")
    void e2eInvalidTokenScenarios() {
        HttpHeaders headers = new HttpHeaders();

        // Test 1: Malformed token
        headers.setBearerAuth("malformed.token");
        HttpEntity<String> malformedEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> malformedResponse = restTemplate.exchange(
                getBaseUrl() + "/api/protected/resource",
                HttpMethod.GET,
                malformedEntity,
                Map.class
        );

        assertThat(malformedResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Test 2: Empty token
        headers.clear();
        headers.set("Authorization", "Bearer ");
        HttpEntity<String> emptyEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> emptyResponse = restTemplate.exchange(
                getBaseUrl() + "/api/protected/resource",
                HttpMethod.GET,
                emptyEntity,
                Map.class
        );

        assertThat(emptyResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Test 3: Wrong auth scheme
        headers.clear();
        headers.set("Authorization", "Basic dGVzdDp0ZXN0");
        HttpEntity<String> basicEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> basicResponse = restTemplate.exchange(
                getBaseUrl() + "/api/protected/resource",
                HttpMethod.GET,
                basicEntity,
                Map.class
        );

        assertThat(basicResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("E2E: Multiple concurrent requests with same token")
    void e2eMultipleConcurrentRequestsWithSameToken() {
        // Generate valid token
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "testuser", "test@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        String accessToken = jwtTokenProvider.generateToken(authentication);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make multiple concurrent requests
        for (int i = 0; i < 5; i++) {
            ResponseEntity<Map> response = restTemplate.exchange(
                    getBaseUrl() + "/api/protected/resource",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).containsKey("message");
        }
    }

    @Test
    @DisplayName("E2E: Security headers validation")
    void e2eSecurityHeadersValidation() {
        // Test that security-related headers are properly set
        ResponseEntity<Map> response = restTemplate.getForEntity(
                getBaseUrl() + "/api/health/status", Map.class);

        HttpHeaders responseHeaders = response.getHeaders();
        
        // Check for security headers (these would be set by additional security configuration)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // The response should not contain sensitive information in headers
        assertThat(responseHeaders.get("Server")).isNullOrEmpty();
    }

    @Test
    @DisplayName("E2E: CORS handling")
    void e2eCorsHandling() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Origin", "http://localhost:3000");
        headers.set("Access-Control-Request-Method", "GET");
        headers.set("Access-Control-Request-Headers", "Authorization");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/api/protected/resource",
                HttpMethod.OPTIONS,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @org.springframework.boot.autoconfigure.SpringBootApplication
    @org.springframework.context.annotation.ComponentScan(
        basePackages = "com.gogidix.ecosystem.shared.security"
    )
    static class TestApplication {
        
        
        @org.springframework.web.bind.annotation.RestController
        static class TestController {
            
            @org.springframework.web.bind.annotation.GetMapping("/api/health/status")
            public java.util.Map<String, String> healthStatus() {
                return java.util.Map.of("status", "UP");
            }
            
            @org.springframework.web.bind.annotation.GetMapping("/api/protected/resource")
            @org.springframework.security.access.prepost.PreAuthorize("hasRole('USER')")
            public java.util.Map<String, String> protectedResource() {
                return java.util.Map.of("message", "Access granted");
            }
            
            @org.springframework.web.bind.annotation.GetMapping("/api/admin/users")
            @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
            public java.util.Map<String, java.util.List<String>> adminUsers() {
                return java.util.Map.of("users", java.util.Arrays.asList("user1", "user2"));
            }
            
            @org.springframework.web.bind.annotation.PostMapping("/api/auth/refresh")
            public java.util.Map<String, String> refreshToken(@org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> request) {
                return java.util.Map.of("accessToken", "new-access-token");
            }
        }
    }
}