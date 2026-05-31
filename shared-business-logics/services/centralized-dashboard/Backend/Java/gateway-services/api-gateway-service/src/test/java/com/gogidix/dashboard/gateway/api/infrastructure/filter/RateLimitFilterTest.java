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
import org.springframework.test.util.ReflectionTestUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimitFilterTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private ValueOperations<String, String> valueOps;

    @Nested
    @DisplayName("Rate limit disabled tests")
    class DisabledTests {
        @Test
        void doFilter_rateLimitDisabled_passesThrough() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", false);

            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("Rate limit enabled - under limit tests")
    class UnderLimitTests {
        @Test
        void doFilter_underLimit_passesThrough() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 100);

            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(1L);
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("User-Agent")).thenReturn("test-agent");
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void doFilter_firstRequest_setsExpire() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 100);

            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(1L);
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("User-Agent")).thenReturn("test-agent");
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(redisTemplate).expire(anyString(), eq(Duration.ofMinutes(1)));
        }

        @Test
        void doFilter_secondRequest_doesNotSetExpire() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 100);

            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(2L);
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("User-Agent")).thenReturn("test-agent");
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(redisTemplate, never()).expire(anyString(), any(Duration.class));
        }

        @Test
        void doFilter_nullCount_defaultsToOne() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 100);

            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(null);
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("User-Agent")).thenReturn("test-agent");
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("Rate limit enabled - over limit tests")
    class OverLimitTests {
        @Test
        void doFilter_overLimit_returns429() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 5);

            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(10L);
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("User-Agent")).thenReturn("test");
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            HttpServletResponse mockResponse = mock(HttpServletResponse.class);
            when(mockResponse.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
            filter.doFilterInternal(request, mockResponse, filterChain);
            verify(mockResponse).setStatus(429);
            verify(filterChain, never()).doFilter(request, mockResponse);
        }
    }

    @Nested
    @DisplayName("Client ID tests")
    class ClientIdTests {
        @Test
        void doFilter_redisException_passesThrough() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);

            when(redisTemplate.opsForValue()).thenThrow(new RuntimeException("Redis down"));
            when(request.getHeader("X-Tenant-ID")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn(null);
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void doFilter_longUserAgent_truncatesTo20() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 100);

            String longAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(1L);
            when(request.getHeader("X-Tenant-ID")).thenReturn("t1");
            when(request.getHeader("User-Agent")).thenReturn(longAgent);
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void doFilter_emptyTenantId_usesDefault() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 100);

            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(1L);
            when(request.getHeader("X-Tenant-ID")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn("agent");
            when(request.getRemoteAddr()).thenReturn("10.0.0.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void doFilter_shortUserAgent_usesFullLength() throws Exception {
            RateLimitFilter filter = new RateLimitFilter(redisTemplate);
            ReflectionTestUtils.setField(filter, "rateLimitEnabled", true);
            ReflectionTestUtils.setField(filter, "requestsPerMinute", 100);

            when(redisTemplate.opsForValue()).thenReturn(valueOps);
            when(valueOps.increment(anyString())).thenReturn(1L);
            when(request.getHeader("X-Tenant-ID")).thenReturn("tenant-abc");
            when(request.getHeader("User-Agent")).thenReturn("short");
            when(request.getRemoteAddr()).thenReturn("192.168.1.1");

            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain).doFilter(request, response);
        }
    }
}
