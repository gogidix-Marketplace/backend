package com.gogidix.ecommerce.sms.domain.port.out;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SmsRepositoryPort {

    Sms save(Sms entity);

    Optional<Sms> findByIdAndTenantId(String id, String tenantId);

    Page<Sms> findByTenantId(String tenantId, Pageable pageable);

    void deleteById(String id);
}