package com.gogidix.ecommerce.search.shared.context;

import com.gogidix.ecommerce.search.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    public String getCurrentTenantId() { return RequestContextHolder.getTenantId(); }
    public String getCurrentCorrelationId() { return RequestContextHolder.getCorrelationId(); }
    public boolean isInitialized() { return RequestContextHolder.isInitialized(); }
}
