package com.gogidix.ecommerce.communication.infrastructure.persistence.repository;

import com.gogidix.ecommerce.communication.domain.model.Communication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommunicationMongoRepository extends MongoRepository<Communication, String> {
    List<Communication> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
