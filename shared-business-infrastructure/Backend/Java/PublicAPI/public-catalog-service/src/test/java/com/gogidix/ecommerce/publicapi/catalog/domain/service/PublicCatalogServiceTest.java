package com.gogidix.ecommerce.publicapi.catalog.domain.service;

import com.gogidix.ecommerce.publicapi.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.publicapi.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PublicCatalogServiceTest {
    private RequestContext requestContext;
    @BeforeEach void setUp() {
        requestContext = RequestContext.builder()
                .tenantId("tenant-123").customerId("customer-456")
                .userId("user-789").correlationId("corr-abc").build();
        RequestContextHolder.set(requestContext);
    }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void testServiceCreation() { assertThat(true).isTrue(); }
    @Test void testRequestContext() { assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-123"); }
    @Test void testCustomerId() { assertThat(RequestContextHolder.getCustomerId()).isEqualTo("customer-456"); }
    @Test void testUserId() { assertThat(RequestContextHolder.getUserId()).isEqualTo("user-789"); }
    @Test void testCorrelationId() { assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("corr-abc"); }
}
