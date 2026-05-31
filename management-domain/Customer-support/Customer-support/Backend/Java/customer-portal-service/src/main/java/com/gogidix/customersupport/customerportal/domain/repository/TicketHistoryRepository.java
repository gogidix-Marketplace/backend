package com.gogidix.customersupport.customerportal.domain.repository;

import com.gogidix.customersupport.customerportal.domain.model.TicketHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TicketHistoryRepository extends MongoRepository<TicketHistory, String> {

    List<TicketHistory> findByTenantId(String tenantId);

    List<TicketHistory> findByCustomerId(String customerId);

    List<TicketHistory> findByCustomerIdOrderByCreatedAtDesc(String customerId);

    List<TicketHistory> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<TicketHistory> findByTenantIdAndCustomerIdOrderByCreatedAtDesc(String tenantId, String customerId);

    List<TicketHistory> findByTicketId(String ticketId);

    List<TicketHistory> findByCustomerIdAndStatus(String customerId, String status);

    List<TicketHistory> findByCustomerIdAndCreatedAtBetween(String customerId, Instant startDate, Instant endDate);

    void deleteByTicketId(String ticketId);

    void deleteByCustomerId(String customerId);

    long countByCustomerId(String customerId);

    long countByCustomerIdAndStatus(String customerId, String status);
}
