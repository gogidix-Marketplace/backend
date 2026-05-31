package com.gogidix.finance.tax.domain.port.out;


public interface TaxFilingRepository {
    void deleteById(String id);
    void deleteAllByTenantId(String tenantId);
}
