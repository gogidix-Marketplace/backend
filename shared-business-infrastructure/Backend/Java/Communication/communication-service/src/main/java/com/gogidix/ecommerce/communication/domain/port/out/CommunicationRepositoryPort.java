package com.gogidix.ecommerce.communication.domain.port.out;

import com.gogidix.ecommerce.communication.domain.model.Communication;
import java.util.List;
import java.util.Optional;

public interface CommunicationRepositoryPort {
    Communication save(Communication entity);
    Optional<Communication> findById(String id);
    List<Communication> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
