package com.gogidix.ecommerce.pricing.shared.context;

import com.gogidix.ecommerce.pricing.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.pricing.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {

    public String getCurrentTenantId() {
        return RequestContextHolder.getTenantId();
    }

    public String getCurrentUserId() {
        return RequestContextHolder.getUserId();
    }

    public String getCurrentCorrelationId() {
        return RequestContextHolder.getCorrelationId();
    }

    public boolean isInitialized() {
        return RequestContextHolder.isInitialized();
    }
}
