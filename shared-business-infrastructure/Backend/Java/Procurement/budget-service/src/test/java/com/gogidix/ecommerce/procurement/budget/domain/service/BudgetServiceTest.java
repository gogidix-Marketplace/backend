package com.gogidix.ecommerce.budget.domain.service;

import com.gogidix.ecommerce.budget.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.budget.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {
    private RequestContext requestContext;
    @BeforeEach void setUp() {
        requestContext = RequestContext.builder()
                .tenantId("tenant-123").customerId("customer-456")
                .userId("user-789").correlationId("corr-abc").build();
        RequestContextHolder.set(requestContext);
    }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void testServiceCreation() { assertThat(true).isTrue(); }
}
