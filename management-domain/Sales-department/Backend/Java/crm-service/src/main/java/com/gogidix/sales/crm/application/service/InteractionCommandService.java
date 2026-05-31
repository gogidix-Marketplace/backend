package com.gogidix.sales.crm.application.service;

import com.gogidix.sales.crm.domain.event.InteractionLoggedEvent;
import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.domain.model.Interaction;
import com.gogidix.sales.crm.domain.port.in.InteractionCommand;
import com.gogidix.sales.crm.domain.port.out.EventPublisher;
import com.gogidix.sales.crm.domain.repository.CustomerRepository;
import com.gogidix.sales.crm.domain.repository.InteractionRepository;
import com.gogidix.sales.crm.shared.exception.NotFoundException;
import com.gogidix.sales.crm.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Interaction Command Service
 * Handles all write operations for interactions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InteractionCommandService {

    private final InteractionRepository interactionRepository;
    private final CustomerRepository customerRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Interaction create(InteractionCommand.CreateInteractionCommand command) {
        log.info("Creating interaction for customer: {} in tenant: {}",
            command.getCustomerId(), command.getTenantId());

        // Verify customer exists and get name if not provided
        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        String customerName = command.getCustomerName() != null ? command.getCustomerName() : customer.getCompanyName();

        Interaction interaction = Interaction.create(
            command.getTenantId(),
            command.getCustomerId(),
            customerName,
            command.getContactId(),
            command.getContactName(),
            command.getType(),
            command.getDirection(),
            command.getInteractionDate(),
            command.getSubject()
        );

        // Set additional fields
        interaction.setDescription(command.getDescription());
        interaction.setLocation(command.getLocation());
        interaction.setAssignedTo(command.getAssignedTo());
        interaction.setAssignedToName(command.getAssignedToName());
        interaction.setCampaignId(command.getCampaignId());
        interaction.setDealId(command.getDealId());
        interaction.setDealValue(command.getDealValue());
        interaction.setProbability(command.getProbability());
        interaction.setIsHighPriority(command.getIsHighPriority() != null ? command.getIsHighPriority() : false);
        interaction.setNotes(command.getNotes());

        if (command.getParticipantContactIds() != null) {
            interaction.setParticipantContactIds(command.getParticipantContactIds());
        }

        Interaction savedInteraction = interactionRepository.save(interaction);
        publishEvents(savedInteraction);

        // Update customer last contact date
        customer.updateLastContactDate(command.getInteractionDate().toLocalDate());
        customerRepository.save(customer);

        log.info("Created interaction: {} for customer: {}", savedInteraction.getInteractionId(), command.getCustomerId());
        return savedInteraction;
    }

    @Transactional
    public void complete(InteractionCommand.CompleteInteractionCommand command) {
        log.info("Completing interaction: {} for tenant: {}", command.getInteractionId(), command.getTenantId());

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            command.getInteractionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Interaction", command.getInteractionId()));

        interaction.complete(command.getOutcome(), command.getNotes(), command.getDurationMinutes());

        if (command.getFollowUpDate() != null) {
            interaction.setFollowUp(command.getFollowUpDate(), command.getFollowUpNotes());
        }

        if (command.getNextStep() != null) {
            interaction.setNextStep(command.getNextStep(), command.getNextStepDate());
        }

        interactionRepository.save(interaction);
        publishEvents(interaction);

        // Update customer
        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            interaction.getCustomerId(), command.getTenantId())
            .orElse(null);
        if (customer != null) {
            customer.updateLastContactDate(interaction.getInteractionDate().toLocalDate());
            customerRepository.save(customer);
        }

        log.info("Completed interaction: {}", command.getInteractionId());
    }

    @Transactional
    public void cancel(InteractionCommand.CancelInteractionCommand command) {
        log.info("Cancelling interaction: {} for tenant: {}", command.getInteractionId(), command.getTenantId());

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            command.getInteractionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Interaction", command.getInteractionId()));

        interaction.cancel(command.getReason());
        interactionRepository.save(interaction);
        publishEvents(interaction);

        log.info("Cancelled interaction: {}", command.getInteractionId());
    }

    @Transactional
    public void reschedule(InteractionCommand.RescheduleInteractionCommand command) {
        log.info("Rescheduling interaction: {} to new date: {}",
            command.getInteractionId(), command.getNewDate());

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            command.getInteractionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Interaction", command.getInteractionId()));

        interaction.reschedule(command.getNewDate(), command.getReason());
        interactionRepository.save(interaction);
        publishEvents(interaction);

        log.info("Rescheduled interaction: {}", command.getInteractionId());
    }

    @Transactional
    public void addParticipant(InteractionCommand.AddParticipantCommand command) {
        log.info("Adding participant: {} to interaction: {}", command.getContactId(), command.getInteractionId());

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            command.getInteractionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Interaction", command.getInteractionId()));

        interaction.addParticipant(command.getContactId());
        interactionRepository.save(interaction);

        log.info("Added participant: {} to interaction: {}", command.getContactId(), command.getInteractionId());
    }

    @Transactional
    public void addAttachment(InteractionCommand.AddAttachmentCommand command) {
        log.info("Adding attachment to interaction: {}", command.getInteractionId());

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            command.getInteractionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Interaction", command.getInteractionId()));

        interaction.addAttachment(command.getAttachmentUrl());
        interactionRepository.save(interaction);

        log.info("Added attachment to interaction: {}", command.getInteractionId());
    }

    @Transactional
    public void associateWithDeal(InteractionCommand.AssociateWithDealCommand command) {
        log.info("Associating interaction: {} with deal: {}", command.getInteractionId(), command.getDealId());

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            command.getInteractionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Interaction", command.getInteractionId()));

        interaction.associateWithDeal(command.getDealId(), command.getDealValue());
        interactionRepository.save(interaction);

        log.info("Associated interaction: {} with deal: {}", command.getInteractionId(), command.getDealId());
    }

    @Transactional
    public void markAsHighPriority(String tenantId, String interactionId) {
        log.info("Marking interaction: {} as high priority", interactionId);

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            interactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("Interaction", interactionId));

        interaction.markAsHighPriority();
        interactionRepository.save(interaction);

        log.info("Marked interaction: {} as high priority", interactionId);
    }

    @Transactional
    public void delete(InteractionCommand.DeleteInteractionCommand command) {
        log.info("Deleting interaction: {} for tenant: {}", command.getInteractionId(), command.getTenantId());

        Interaction interaction = interactionRepository.findByInteractionIdAndTenantId(
            command.getInteractionId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Interaction", command.getInteractionId()));

        if (interaction.getStatus() == Interaction.InteractionStatus.COMPLETED) {
            throw new ValidationException("Cannot delete completed interactions");
        }

        interactionRepository.deleteByInteractionIdAndTenantId(command.getInteractionId(), command.getTenantId());

        log.info("Deleted interaction: {}", command.getInteractionId());
    }

    private void publishEvents(Interaction interaction) {
        if (!interaction.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(interaction.getDomainEvents());
            interaction.clearDomainEvents();
        }
    }
}
