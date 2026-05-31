package com.gogidix.customersupport.ticketmanagement.domain.repository;

import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByTenantId(String tenantId);

    Optional<Ticket> findByTenantIdAndId(String tenantId, String id);

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    List<Ticket> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Ticket> findByTenantIdAndAssignedAgentId(String tenantId, String agentId);

    List<Ticket> findByTenantIdAndStatus(String tenantId, Ticket.TicketStatus status);

    List<Ticket> findByTenantIdAndPriority(String tenantId, Ticket.TicketPriority priority);

    List<Ticket> findByTenantIdAndCategory(String tenantId, String category);

    List<Ticket> findByTenantIdAndChannel(String tenantId, Ticket.TicketChannel channel);

    @Query("{ 'tenantId': ?0, 'status': ?1, 'assignedAgentId': ?2 }")
    List<Ticket> findByTenantIdAndStatusAndAssignedAgentId(String tenantId, Ticket.TicketStatus status, String agentId);

    Page<Ticket> findByTenantId(String tenantId, Pageable pageable);

    Page<Ticket> findByTenantIdAndStatus(String tenantId, Ticket.TicketStatus status, Pageable pageable);

    @Query("{ 'tenantId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<Ticket> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate);

    @Query("{ 'tenantId': ?0, 'slaBreached': true }")
    List<Ticket> findSlaBreachedTickets(String tenantId);

    @Query("{ 'tenantId': ?0, 'dueDate': { $lt: ?1 }, 'status': { $in: [?2, ?3, ?4] } }")
    List<Ticket> findOverdueTickets(String tenantId, Instant now, Ticket.TicketStatus status1, Ticket.TicketStatus status2, Ticket.TicketStatus status3);

    @Query("{ 'tenantId': ?0, '$or': [ {'title': { $regex: ?1, $options: 'i' } }, {'description': { $regex: ?1, $options: 'i' } }, {'ticketNumber': { $regex: ?1, $options: 'i' } } ] }")
    Page<Ticket> searchTickets(String tenantId, String keyword, Pageable pageable);

    List<Ticket> findByTenantIdAndTagsIn(String tenantId, List<String> tags);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsByTicketNumber(String ticketNumber);

    long countByTenantIdAndStatus(String tenantId, Ticket.TicketStatus status);

    long countByTenantIdAndAssignedAgentId(String tenantId, String agentId);
}
