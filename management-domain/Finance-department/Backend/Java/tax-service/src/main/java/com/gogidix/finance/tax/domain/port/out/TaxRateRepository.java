package com.gogidix.finance.tax.domain.port.out;


public interface TaxRateRepository {
    void deleteById(String id);
    void deleteAllByTenantId(String tenantId);
}
