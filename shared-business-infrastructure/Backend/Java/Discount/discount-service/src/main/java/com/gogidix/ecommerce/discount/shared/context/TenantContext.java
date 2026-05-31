package com.gogidix.ecommerce.discount.shared.context;

import com.gogidix.ecommerce.discount.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    public String getCurrentTenantId() { return RequestContextHolder.getTenantId(); }
    public String getCurrentCorrelationId() { return RequestContextHolder.getCorrelationId(); }
    public boolean isInitialized() { return RequestContextHolder.isInitialized(); }
}
