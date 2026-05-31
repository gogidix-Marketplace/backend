package com.gogidix.ecosystem.shared.security;

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
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JwtAuthenticationFilter.
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JWT Authentication Filter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should process valid JWT token and set authentication")
    void shouldProcessValidJwtTokenAndSetAuthentication() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(token)).thenReturn("1");
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn("testuser");
        when(jwtTokenProvider.getAuthoritiesFromToken(token)).thenReturn(Arrays.asList("ROLE_USER"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should skip authentication for invalid token")
    void shouldSkipAuthenticationForInvalidToken() throws ServletException, IOException {
        // Given
        String token = "invalid.jwt.token";
        String bearerToken = "Bearer " + token;
        
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should skip authentication when no authorization header")
    void shouldSkipAuthenticationWhenNoAuthorizationHeader() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenProvider, never()).validateToken(any());
    }

    @Test
    @DisplayName("Should skip authentication when authorization header doesn't start with Bearer")
    void shouldSkipAuthenticationWhenAuthorizationHeaderDoesntStartWithBearer() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Basic dGVzdDp0ZXN0");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenProvider, never()).validateToken(any());
    }

    @Test
    @DisplayName("Should handle empty bearer token")
    void shouldHandleEmptyBearerToken() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenProvider, never()).validateToken(any());
    }

    @Test
    @DisplayName("Should handle exception during token processing")
    void shouldHandleExceptionDuringTokenProcessing() throws ServletException, IOException {
        // Given
        String token = "problematic.jwt.token";
        String bearerToken = "Bearer " + token;
        
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtTokenProvider.validateToken(token)).thenThrow(new RuntimeException("Token processing error"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle null username from token")
    void shouldHandleNullUsernameFromToken() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(token)).thenReturn("1");
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn(null);
        when(jwtTokenProvider.getAuthoritiesFromToken(token)).thenReturn(Arrays.asList("ROLE_USER"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle empty authorities from token")
    void shouldHandleEmptyAuthoritiesFromToken() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(token)).thenReturn("1");
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn("testuser");
        when(jwtTokenProvider.getAuthoritiesFromToken(token)).thenReturn(Arrays.asList());

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).isEmpty();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle multiple authorities from token")
    void shouldHandleMultipleAuthoritiesFromToken() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(token)).thenReturn("1");
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn("testuser");
        when(jwtTokenProvider.getAuthoritiesFromToken(token)).thenReturn(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).hasSize(2);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should handle invalid user ID format")
    void shouldHandleInvalidUserIdFormat() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(token)).thenReturn("invalid-id");
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn("testuser");
        when(jwtTokenProvider.getAuthoritiesFromToken(token)).thenReturn(Arrays.asList("ROLE_USER"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}