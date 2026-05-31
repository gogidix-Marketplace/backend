package com.gogidix.ecommerce.email.domain.port.out;

import com.gogidix.ecommerce.email.domain.model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmailRepositoryPort {

    Email save(Email entity);

    Optional<Email> findByIdAndTenantId(String id, String tenantId);

    Page<Email> findByTenantId(String tenantId, Pageable pageable);

    void deleteById(String id);
}