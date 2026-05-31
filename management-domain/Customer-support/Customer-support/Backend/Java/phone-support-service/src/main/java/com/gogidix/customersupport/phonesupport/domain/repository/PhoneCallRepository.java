package com.gogidix.customersupport.phonesupport.domain.repository;

import com.gogidix.customersupport.phonesupport.domain.model.PhoneCall;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneCallRepository extends MongoRepository<PhoneCall, String> {

    List<PhoneCall> findByTenantId(String tenantId);

    Optional<PhoneCall> findByTenantIdAndId(String tenantId, String id);

    Optional<PhoneCall> findByCallId(String callId);

    List<PhoneCall> findByTenantIdAndAgentId(String tenantId, String agentId);

    List<PhoneCall> findByTenantIdAndCallStatus(String tenantId, PhoneCall.CallStatus callStatus);

    List<PhoneCall> findByTenantIdAndStartedAtBetween(String tenantId, Instant startDate, Instant endDate);

    List<PhoneCall> findByTenantIdAndCallerInfoPhoneNumber(String tenantId, String phoneNumber);

    List<PhoneCall> findByTenantIdAndRelatedTicketId(String tenantId, String ticketId);

    List<PhoneCall> findByTenantIdOrderByStartedAtDesc(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);

    long countByTenantIdAndAgentIdAndCallStatus(String tenantId, String agentId, PhoneCall.CallStatus callStatus);

    long countByTenantIdAndCallStatus(String tenantId, PhoneCall.CallStatus callStatus);
}
