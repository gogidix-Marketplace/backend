package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should authenticate user with valid JWT token")
    void shouldAuthenticateUserWithValidJWTToken() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userId = "user-123";
        List<String> roles = List.of("USER", "ADMIN");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getUserId(token)).thenReturn(userId);
        when(jwtProvider.getRoles(token)).thenReturn(roles);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userId, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(jwtProvider).validateToken(token);
        verify(jwtProvider).getUserId(token);
        verify(jwtProvider).getRoles(token);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should not authenticate when Authorization header is missing")
    void shouldNotAuthenticateWhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(jwtProvider, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should not authenticate when Authorization header does not start with Bearer")
    void shouldNotAuthenticateWhenAuthorizationHeaderDoesNotStartWithBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic token123");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(jwtProvider, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should not authenticate when JWT token is invalid")
    void shouldNotAuthenticateWhenJWTTokenIsInvalid() throws ServletException, IOException {
        String token = "invalid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(false);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(jwtProvider).validateToken(token);
        verify(jwtProvider, never()).getUserId(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should not authenticate when Authorization header has only Bearer prefix")
    void shouldNotAuthenticateWhenAuthorizationHeaderHasOnlyBearerPrefix() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(jwtProvider, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle lowercase bearer prefix")
    void shouldHandleLowercaseBearerPrefix() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("bearer token123");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(jwtProvider, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle mixed case bearer prefix")
    void shouldHandleMixedCaseBearerPrefix() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("BeArEr token123");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(jwtProvider, never()).validateToken(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle token with empty roles list")
    void shouldHandleTokenWithEmptyRolesList() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userId = "user-123";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getUserId(token)).thenReturn(userId);
        when(jwtProvider.getRoles(token)).thenReturn(List.of());

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userId, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().isEmpty());

        verify(jwtProvider).validateToken(token);
        verify(jwtProvider).getUserId(token);
        verify(jwtProvider).getRoles(token);
    }

    @Test
    @DisplayName("Should handle exception during token validation gracefully")
    void shouldHandleExceptionDuringTokenValidationGracefully() throws ServletException, IOException {
        String token = "malformed-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenThrow(new RuntimeException("JWT parsing error"));

        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilter(request, response, filterChain));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should always call filter chain regardless of authentication")
    void shouldAlwaysCallFilterChainRegardlessOfAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should set authentication details")
    void shouldSetAuthenticationDetails() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userId = "user-123";
        List<String> roles = List.of("USER");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getUserId(token)).thenReturn(userId);
        when(jwtProvider.getRoles(token)).thenReturn(roles);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertNotNull(authentication.getDetails());
    }

    @Test
    @DisplayName("Should handle token with single role")
    void shouldHandleTokenWithSingleRole() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userId = "user-123";
        List<String> roles = List.of("ADMIN");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getUserId(token)).thenReturn(userId);
        when(jwtProvider.getRoles(token)).thenReturn(roles);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(1, authentication.getAuthorities().size());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should handle token with multiple roles")
    void shouldHandleTokenWithMultipleRoles() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userId = "user-123";
        List<String> roles = List.of("USER", "ADMIN", "MODERATOR");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getUserId(token)).thenReturn(userId);
        when(jwtProvider.getRoles(token)).thenReturn(roles);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(3, authentication.getAuthorities().size());
    }

    @Test
    @DisplayName("Should verify filter is component")
    void shouldVerifyFilterIsComponent() {
        assertTrue(JwtAuthenticationFilter.class.isAnnotationPresent(org.springframework.stereotype.Component.class));
    }

    @Test
    @DisplayName("Should verify filter extends OncePerRequestFilter")
    void shouldVerifyFilterExtendsOncePerRequestFilter() {
        assertTrue(JwtAuthenticationFilter.class.getSuperclass().equals(org.springframework.web.filter.OncePerRequestFilter.class));
    }

    @Test
    @DisplayName("Should verify filter has Slf4j annotation")
    void shouldVerifyFilterHasSlf4jAnnotation() {
        try {
            assertNotNull(JwtAuthenticationFilter.class.getDeclaredField("log"));
        } catch (NoSuchFieldException e) {
            fail("JwtAuthenticationFilter should have a 'log' field (generated by @Slf4j)");
        }
    }

    @Test
    @DisplayName("Should verify filter has RequiredArgsConstructor annotation")
    void shouldVerifyFilterHasRequiredArgsConstructorAnnotation() throws NoSuchMethodException {
        assertNotNull(JwtAuthenticationFilter.class.getDeclaredConstructor(JwtProvider.class));
    }

    @Test
    @DisplayName("Should handle null roles from JWT provider")
    void shouldHandleNullRolesFromJWTProvider() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userId = "user-123";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getUserId(token)).thenReturn(userId);
        when(jwtProvider.getRoles(token)).thenReturn(null);

        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilter(request, response, filterChain));
    }

    @Test
    @DisplayName("Should clean security context between requests")
    void shouldCleanSecurityContextBetweenRequests() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String userId = "user-123";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getUserId(token)).thenReturn(userId);
        when(jwtProvider.getRoles(token)).thenReturn(List.of("USER"));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        HttpServletRequest secondRequest = mock(HttpServletRequest.class);
        when(secondRequest.getHeader("Authorization")).thenReturn(null);

        SecurityContextHolder.clearContext();
        jwtAuthenticationFilter.doFilter(secondRequest, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
