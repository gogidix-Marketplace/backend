package com.gogidix.aiservices.leadgenerationaiservice.domain.port.out;

import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LeadRepository {
    Lead save(Lead lead);
    Optional<Lead> findById(String id);
    Optional<Lead> findByEmail(String email);
    List<Lead> findAll();
    List<Lead> findByStatus(LeadStatus status);
    List<Lead> findByOwnerId(String ownerId);
    List<Lead> findByCreatedAtAfter(Instant timestamp);
    List<Lead> findTopScores(int limit);
    void delete(String id);
    List<Lead> saveAll(List<Lead> leads);
}
