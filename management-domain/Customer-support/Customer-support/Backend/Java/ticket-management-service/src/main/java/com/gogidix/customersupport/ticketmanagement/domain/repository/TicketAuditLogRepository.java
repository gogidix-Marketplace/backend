package com.gogidix.customersupport.ticketmanagement.domain.repository;

import com.gogidix.customersupport.ticketmanagement.domain.model.TicketAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TicketAuditLogRepository extends MongoRepository<TicketAuditLog, String> {

    List<TicketAuditLog> findByTenantId(String tenantId);

    List<TicketAuditLog> findByTicketId(String ticketId);

    List<TicketAuditLog> findByTicketIdOrderByPerformedAtDesc(String ticketId);

    List<TicketAuditLog> findByTenantIdAndTicketId(String tenantId, String ticketId);

    List<TicketAuditLog> findByTicketIdAndActionPerformedBetween(String ticketId, Instant startDate, Instant endDate);

    List<TicketAuditLog> findByTenantIdAndActionPerformedBy(String tenantId, String performedBy);

    void deleteByTicketId(String ticketId);
}
