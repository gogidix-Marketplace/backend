package com.gogidix.ecommerce.inventorysync.infrastructure.config;

import com.gogidix.ecommerce.inventorysync.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.inventorysync.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WebConfigTest {

    private final WebConfig.RequestContextFilter filter = new WebConfig.RequestContextFilter();

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    @Test
    void filter_withTenantHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Tenant-ID", "t1");
        request.addHeader("X-Correlation-ID", "corr1");
        request.addHeader("X-Customer-ID", "cust1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        doAnswer(inv -> {
            RequestContext ctx = RequestContextHolder.get();
            assertThat(ctx).isNotNull();
            assertThat(ctx.tenantId()).isEqualTo("t1");
            assertThat(ctx.correlationId()).isEqualTo("corr1");
            assertThat(ctx.customerId()).isEqualTo("cust1");
            return null;
        }).when(chain).doFilter(any(), any());

        filter.doFilterInternal(request, response, chain);
        verify(chain).doFilter(any(), any());
    }

    @Test
    void filter_missingTenant_returns400() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(400);
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void filter_blankTenant_returns400() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Tenant-ID", "  ");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void filter_missingCorrelation_generatesUuid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Tenant-ID", "t1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        doAnswer(inv -> {
            RequestContext ctx = RequestContextHolder.get();
            assertThat(ctx.correlationId()).isNotBlank();
            return null;
        }).when(chain).doFilter(any(), any());

        filter.doFilterInternal(request, response, chain);
    }

    @Test
    void filter_clearsContextInFinally() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Tenant-ID", "t1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, chain);

        assertThat(RequestContextHolder.isSet()).isFalse();
    }

    @Test
    void requestContextFilter_bean() {
        WebConfig config = new WebConfig();
        assertThat(config.requestContextFilter()).isNotNull();
    }
}
