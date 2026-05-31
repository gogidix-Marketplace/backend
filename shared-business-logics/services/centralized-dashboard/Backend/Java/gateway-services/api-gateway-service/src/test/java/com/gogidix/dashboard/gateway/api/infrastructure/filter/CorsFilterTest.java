package com.gogidix.dashboard.gateway.api.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CorsFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void doFilter_setsCorsHeaders() throws Exception {
        CorsFilter filter = new CorsFilter();
        when(request.getHeader("Origin")).thenReturn("http://localhost:3000");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        verify(response).setHeader("Access-Control-Allow-Credentials", "true");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilter_optionsRequest_returnsOk() throws Exception {
        CorsFilter filter = new CorsFilter();
        when(request.getHeader("Origin")).thenReturn("http://localhost:3000");
        when(request.getMethod()).thenReturn("OPTIONS");

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilter_nullOrigin_usesWildcard() throws Exception {
        CorsFilter filter = new CorsFilter();
        when(request.getHeader("Origin")).thenReturn(null);
        when(request.getMethod()).thenReturn("GET");

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader("Access-Control-Allow-Origin", "*");
    }
}
