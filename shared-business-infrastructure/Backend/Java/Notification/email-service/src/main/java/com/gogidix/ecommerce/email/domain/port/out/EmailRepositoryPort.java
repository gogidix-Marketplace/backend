package com.gogidix.ecommerce.email.domain.port.out;

import com.gogidix.ecommerce.email.domain.model.Email;
import java.util.List;
import java.util.Optional;

public interface EmailRepositoryPort {
    Email save(Email entity);
    Optional<Email> findById(String id);
    List<Email> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
