package com.gogidix.shared.infrastructure.services.security.analytics.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.model.SecurityEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface SecurityEventRepository extends MongoRepository<SecurityEvent, String> {
    List<SecurityEvent> findByTenantId_Value(String tenantId);
    List<SecurityEvent> findByTenantId_ValueAndSeverity(String tenantId, String severity);
    List<SecurityEvent> findByTenantId_ValueAndTimestampAfter(String tenantId, LocalDateTime timestamp);
    long countByTenantId_Value(String tenantId);
    void deleteByTenantId_ValueAndId(String tenantId, String id);
}
