package com.gogidix.ecommerce.communication.domain.repository;

import com.gogidix.ecommerce.communication.domain.model.Communication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationRepository extends MongoRepository<Communication, String> {
    List<Communication> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
