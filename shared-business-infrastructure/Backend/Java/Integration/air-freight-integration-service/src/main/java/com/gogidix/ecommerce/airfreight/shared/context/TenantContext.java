package com.gogidix.ecommerce.airfreight.shared.context;

import com.gogidix.ecommerce.airfreight.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    public String getCurrentTenantId() { return RequestContextHolder.getTenantId(); }
    public String getCurrentCorrelationId() { return RequestContextHolder.getCorrelationId(); }
    public boolean isInitialized() { return RequestContextHolder.isInitialized(); }
}
