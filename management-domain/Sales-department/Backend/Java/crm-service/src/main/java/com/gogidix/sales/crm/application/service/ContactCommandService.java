package com.gogidix.sales.crm.application.service;

import com.gogidix.sales.crm.domain.event.ContactCreatedEvent;
import com.gogidix.sales.crm.domain.model.Contact;
import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.domain.port.in.ContactCommand;
import com.gogidix.sales.crm.domain.port.out.EventPublisher;
import com.gogidix.sales.crm.domain.repository.ContactRepository;
import com.gogidix.sales.crm.domain.repository.CustomerRepository;
import com.gogidix.sales.crm.shared.exception.NotFoundException;
import com.gogidix.sales.crm.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contact Command Service
 * Handles all write operations for contacts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContactCommandService {

    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Contact create(ContactCommand.CreateContactCommand command) {
        log.info("Creating contact for customer: {} in tenant: {}",
            command.getCustomerId(), command.getTenantId());

        // Verify customer exists
        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        Contact contact = Contact.create(
            command.getTenantId(),
            command.getCustomerId(),
            command.getFirstName(),
            command.getLastName(),
            command.getEmail(),
            command.getTitle(),
            command.getContactType()
        );

        // Set additional fields
        contact.setPhone(command.getPhone());
        contact.setMobilePhone(command.getMobilePhone());
        contact.setAlternatePhone(command.getAlternatePhone());
        contact.setIsPrimary(command.getIsPrimary() != null ? command.getIsPrimary() : false);
        contact.setIsDecisionMaker(command.getIsDecisionMaker() != null ? command.getIsDecisionMaker() : false);
        contact.setLinkedInUrl(command.getLinkedInUrl());
        contact.setTimezone(command.getTimezone());
        contact.setPreferredContactMethod(command.getPreferredContactMethod());
        contact.setAssistantName(command.getAssistantName());
        contact.setAssistantPhone(command.getAssistantPhone());
        contact.setAssistantEmail(command.getAssistantEmail());
        contact.setReportsToContactId(command.getReportsToContactId());
        contact.setBirthDate(command.getBirthDate());
        contact.setNotes(command.getNotes());

        if (command.getTags() != null) {
            contact.setTags(command.getTags());
        }

        // Set address
        contact.setAddressStreet(command.getAddressStreet());
        contact.setAddressCity(command.getAddressCity());
        contact.setAddressState(command.getAddressState());
        contact.setAddressPostalCode(command.getAddressPostalCode());
        contact.setAddressCountry(command.getAddressCountry());

        Contact savedContact = contactRepository.save(contact);

        // Add contact to customer
        customer.addContact(savedContact);
        customerRepository.save(customer);

        // Publish event
        if (eventPublisher.isReady()) {
            eventPublisher.publish(ContactCreatedEvent.create(
                savedContact.getContactId(),
                savedContact.getTenantId(),
                savedContact.getCustomerId(),
                savedContact.getFullName(),
                savedContact.getEmail(),
                savedContact.getTitle(),
                savedContact.getContactType().name(),
                command.getTenantId()
            ));
        }

        log.info("Created contact: {} for customer: {}", savedContact.getContactId(), command.getCustomerId());
        return savedContact;
    }

    @Transactional
    public Contact update(ContactCommand.UpdateContactCommand command) {
        log.info("Updating contact: {} for tenant: {}", command.getContactId(), command.getTenantId());

        Contact contact = contactRepository.findByContactIdAndTenantId(
            command.getContactId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Contact", command.getContactId()));

        contact.updateContact(
            command.getFirstName(),
            command.getLastName(),
            command.getEmail(),
            command.getPhone(),
            command.getTitle(),
            command.getDepartment()
        );

        if (command.getMobilePhone() != null) {
            contact.setMobilePhone(command.getMobilePhone());
        }
        if (command.getAlternatePhone() != null) {
            contact.setAlternatePhone(command.getAlternatePhone());
        }
        if (command.getLinkedInUrl() != null) {
            contact.setLinkedInUrl(command.getLinkedInUrl());
        }
        if (command.getTimezone() != null) {
            contact.setTimezone(command.getTimezone());
        }
        if (command.getPreferredContactMethod() != null) {
            contact.setPreferredContactMethod(command.getPreferredContactMethod());
        }
        if (command.getNotes() != null) {
            contact.setNotes(command.getNotes());
        }
        if (command.getTags() != null) {
            contact.setTags(command.getTags());
        }

        Contact savedContact = contactRepository.save(contact);

        log.info("Updated contact: {}", command.getContactId());
        return savedContact;
    }

    @Transactional
    public void markAsPrimary(String tenantId, String contactId) {
        log.info("Marking contact: {} as primary", contactId);

        Contact contact = contactRepository.findByContactIdAndTenantId(contactId, tenantId)
            .orElseThrow(() -> new NotFoundException("Contact", contactId));

        // Remove primary status from other contacts for the same customer
        List<Contact> customerContacts = contactRepository.findByCustomerIdAndTenantIdAndIsPrimary(
            contact.getCustomerId(), tenantId, true);
        for (Contact c : customerContacts) {
            c.removePrimaryStatus();
            contactRepository.save(c);
        }

        contact.markAsPrimary();
        contactRepository.save(contact);

        log.info("Marked contact: {} as primary", contactId);
    }

    @Transactional
    public void markAsDecisionMaker(String tenantId, String contactId) {
        log.info("Marking contact: {} as decision maker", contactId);

        Contact contact = contactRepository.findByContactIdAndTenantId(contactId, tenantId)
            .orElseThrow(() -> new NotFoundException("Contact", contactId));

        contact.markAsDecisionMaker();
        contactRepository.save(contact);

        log.info("Marked contact: {} as decision maker", contactId);
    }

    @Transactional
    public void deactivate(String tenantId, String contactId) {
        log.info("Deactivating contact: {}", contactId);

        Contact contact = contactRepository.findByContactIdAndTenantId(contactId, tenantId)
            .orElseThrow(() -> new NotFoundException("Contact", contactId));

        contact.deactivate();
        contactRepository.save(contact);

        log.info("Deactivated contact: {}", contactId);
    }

    @Transactional
    public void activate(String tenantId, String contactId) {
        log.info("Activating contact: {}", contactId);

        Contact contact = contactRepository.findByContactIdAndTenantId(contactId, tenantId)
            .orElseThrow(() -> new NotFoundException("Contact", contactId));

        contact.activate();
        contactRepository.save(contact);

        log.info("Activated contact: {}", contactId);
    }

    @Transactional
    public void delete(ContactCommand.DeleteContactCommand command) {
        log.info("Deleting contact: {} for tenant: {}", command.getContactId(), command.getTenantId());

        Contact contact = contactRepository.findByContactIdAndTenantId(
            command.getContactId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Contact", command.getContactId()));

        if (contact.getIsPrimary() != null && contact.getIsPrimary()) {
            throw new ValidationException("Cannot delete primary contact. Set another contact as primary first.");
        }

        contactRepository.deleteByContactIdAndTenantId(command.getContactId(), command.getTenantId());

        log.info("Deleted contact: {}", command.getContactId());
    }
}
