package com.gogidix.sales.crm.domain.repository;

import com.gogidix.sales.crm.domain.model.Interaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interaction Repository Interface (Port)
 * Defines the contract for interaction persistence operations
 */
public interface InteractionRepository {

    Interaction save(Interaction interaction);

    List<Interaction> saveAll(List<Interaction> interactions);

    Optional<Interaction> findById(String id);

    Optional<Interaction> findByInteractionIdAndTenantId(String interactionId, String tenantId);

    List<Interaction> findByTenantId(String tenantId);

    Page<Interaction> findByTenantId(String tenantId, Pageable pageable);

    List<Interaction> findByCustomerIdAndTenantId(String customerId, String tenantId);

    List<Interaction> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Interaction> findByContactIdAndTenantId(String contactId, String tenantId);

    List<Interaction> findByTenantIdAndType(String tenantId, Interaction.InteractionType type);

    List<Interaction> findByTenantIdAndDirection(String tenantId, Interaction.InteractionDirection direction);

    List<Interaction> findByTenantIdAndStatus(String tenantId, Interaction.InteractionStatus status);

    List<Interaction> findByTenantIdAndAssignedTo(String tenantId, String assignedTo);

    List<Interaction> findByTenantIdAndInteractionDateBetween(String tenantId, LocalDateTime startDate,
                                                                LocalDateTime endDate);

    List<Interaction> findByTenantIdAndInteractionDateAfter(String tenantId, LocalDateTime date);

    List<Interaction> findByTenantIdAndInteractionDateBefore(String tenantId, LocalDateTime date);

    List<Interaction> findByTenantIdAndStatusAndInteractionDateBefore(String tenantId,
                                                                       Interaction.InteractionStatus status,
                                                                       LocalDateTime date);

    List<Interaction> findByTenantIdAndHasFollowUpTrueAndFollowUpDateBefore(String tenantId, LocalDate date);

    List<Interaction> findByTenantIdAndDealId(String tenantId, String dealId);

    List<Interaction> findByTenantIdAndCampaignId(String tenantId, String campaignId);

    List<Interaction> findByTenantIdAndIsHighPriorityTrue(String tenantId);

    List<Interaction> findByTenantIdAndSubjectContainingIgnoreCase(String tenantId, String searchTerm);

    Page<Interaction> findByTenantIdAndSubjectContainingIgnoreCase(String tenantId, String searchTerm, Pageable pageable);

    List<Interaction> findByTenantIdAndDescriptionContainingIgnoreCase(String tenantId, String searchTerm);

    boolean existsByInteractionIdAndTenantId(String interactionId, String tenantId);

    void deleteById(String id);

    void deleteByInteractionIdAndTenantId(String interactionId, String tenantId);

    void deleteAllByCustomerIdAndTenantId(String customerId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByCustomerIdAndTenantId(String customerId, String tenantId);

    long countByTenantIdAndStatus(String tenantId, Interaction.InteractionStatus status);

    long countByTenantIdAndType(String tenantId, Interaction.InteractionType type);

    long countByTenantIdAndAssignedTo(String tenantId, String assignedTo);

    long countByTenantIdAndInteractionDateBetween(String tenantId, LocalDateTime startDate, LocalDateTime endDate);

    List<Interaction> findByTenantIdOrderByInteractionDateDesc(String tenantId, Pageable pageable);
}
