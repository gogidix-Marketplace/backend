package com.gogidix.ecommerce.sms.domain.port.out;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import java.util.List;
import java.util.Optional;

public interface SmsRepositoryPort {
    Sms save(Sms entity);
    Optional<Sms> findById(String id);
    List<Sms> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
