package com.gogidix.ecommerce.notification.shared.context;

import com.gogidix.ecommerce.notification.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    public String getCurrentTenantId() { return RequestContextHolder.getTenantId(); }
    public String getCurrentCorrelationId() { return RequestContextHolder.getCorrelationId(); }
    public boolean isInitialized() { return RequestContextHolder.isInitialized(); }
}
