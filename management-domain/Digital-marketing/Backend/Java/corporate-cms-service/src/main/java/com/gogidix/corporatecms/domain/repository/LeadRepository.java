package com.gogidix.corporatecms.domain.repository;

import com.gogidix.corporatecms.domain.enums.LeadStatus;
import com.gogidix.corporatecms.domain.model.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Lead entity.
 */
@Repository
public interface LeadRepository extends MongoRepository<Lead, String> {

    Optional<Lead> findByEmail(String email);

    List<Lead> findByStatus(LeadStatus status);

    Page<Lead> findByStatus(LeadStatus status, Pageable pageable);

    List<Lead> findByAssignedTo(String assignedTo);

    Page<Lead> findByAssignedTo(String assignedTo, Pageable pageable);

    Page<Lead> findBySourceAndDeletedFalse(String source, Pageable pageable);

    @Query("{'$or': [" +
            "{'email': {$regex: ?0, $options: 'i'}}, " +
            "{'firstName': {$regex: ?0, $options: 'i'}}, " +
            "{'lastName': {$regex: ?0, $options: 'i'}}, " +
            "{'company': {$regex: ?0, $options: 'i'}}" +
            "], 'deleted': false}")
    Page<Lead> searchByKeyword(String keyword, Pageable pageable);

    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Lead> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("{'score': {$gte: ?0}}")
    List<Lead> findByScoreGreaterThanEqual(Integer minScore);

    @Query("{'convertedAt': {$gte: ?0, $lte: ?1}}")
    List<Lead> findConvertedBetween(LocalDateTime start, LocalDateTime end);

    Long countByStatus(LeadStatus status);

    Long countByAssignedTo(String assignedTo);

    @Query("{'status': 'NEW'}")
    Long countNewLeads();
}
