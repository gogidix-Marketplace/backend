package com.gogidix.sales.crm.application.service;

import com.gogidix.sales.crm.domain.event.CustomerCreatedEvent;
import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.domain.port.in.CustomerCommand;
import com.gogidix.sales.crm.domain.port.out.EventPublisher;
import com.gogidix.sales.crm.domain.repository.CustomerRepository;
import com.gogidix.sales.crm.shared.exception.ConflictException;
import com.gogidix.sales.crm.shared.exception.NotFoundException;
import com.gogidix.sales.crm.shared.exception.ValidationException;
import com.gogidix.sales.crm.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Customer Command Service
 * Handles all write operations for customers
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerCommandService {

    private final CustomerRepository customerRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Customer create(CustomerCommand.CreateCustomerCommand command) {
        log.info("Creating customer for tenant: {}, company: {}",
            command.getTenantId(), command.getCompanyName());

        // Validate unique account number if provided
        if (command.getAccountNumber() != null &&
            customerRepository.existsByAccountNumberAndTenantId(command.getAccountNumber(), command.getTenantId())) {
            throw new ConflictException("Customer", "account number " + command.getAccountNumber());
        }

        Customer customer = Customer.create(
            command.getTenantId(),
            command.getCreatedBy(),
            command.getCompanyName(),
            command.getIndustry(),
            command.getSegment(),
            command.getLifecycleStage(),
            command.getLeadSource()
        );

        // Set additional fields
        customer.setAccountNumber(command.getAccountNumber());
        customer.setWebsite(command.getWebsite());
        customer.setDescription(command.getDescription());
        customer.setEmployeeCount(command.getEmployeeCount());
        customer.setAnnualRevenue(command.getAnnualRevenue());
        customer.setOwnerId(command.getOwnerId());
        customer.setOwnerName(command.getOwnerName());
        customer.setTerritory(command.getTerritory());

        // Set billing address
        customer.setBillingAddressStreet(command.getBillingAddressStreet());
        customer.setBillingAddressCity(command.getBillingAddressCity());
        customer.setBillingAddressState(command.getBillingAddressState());
        customer.setBillingAddressPostalCode(command.getBillingAddressPostalCode());
        customer.setBillingAddressCountry(command.getBillingAddressCountry());

        // Set shipping address
        customer.setShippingAddressStreet(command.getShippingAddressStreet());
        customer.setShippingAddressCity(command.getShippingAddressCity());
        customer.setShippingAddressState(command.getShippingAddressState());
        customer.setShippingAddressPostalCode(command.getShippingAddressPostalCode());
        customer.setShippingAddressCountry(command.getShippingAddressCountry());

        customer.setPhoneNumber(command.getPhoneNumber());
        customer.setEmail(command.getEmail());
        customer.setAccountType(command.getAccountType());
        customer.setParentAccountId(command.getParentAccountId());

        // Business fields
        customer.setTaxId(command.getTaxId());
        customer.setPaymentTerms(command.getPaymentTerms());
        customer.setCurrency(command.getCurrency());
        customer.setCreditLimit(command.getCreditLimit());

        if (command.getTags() != null) {
            customer.setTags(command.getTags());
        }
        customer.setNotes(command.getNotes());

        Customer savedCustomer = customerRepository.save(customer);
        publishEvents(savedCustomer);

        log.info("Created customer: {} for tenant: {}", savedCustomer.getCustomerId(), command.getTenantId());
        return savedCustomer;
    }

    @Transactional
    public Customer update(CustomerCommand.UpdateCustomerCommand command) {
        log.info("Updating customer: {} for tenant: {}", command.getCustomerId(), command.getTenantId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.updateCustomer(
            RequestContextHolder.getUserId(),
            command.getCompanyName(),
            command.getIndustry(),
            command.getSegment(),
            command.getDescription(),
            command.getWebsite(),
            command.getEmployeeCount(),
            command.getAnnualRevenue()
        );

        if (command.getPhoneNumber() != null) {
            customer.setPhoneNumber(command.getPhoneNumber());
        }
        if (command.getEmail() != null) {
            customer.setEmail(command.getEmail());
        }
        if (command.getTerritory() != null) {
            customer.setTerritory(command.getTerritory());
        }
        if (command.getSatisfactionScore() != null) {
            customer.setSatisfactionScore(command.getSatisfactionScore());
        }
        if (command.getPaymentTerms() != null) {
            customer.setPaymentTerms(command.getPaymentTerms());
        }
        if (command.getCurrency() != null) {
            customer.setCurrency(command.getCurrency());
        }
        if (command.getCreditLimit() != null) {
            customer.setCreditLimit(command.getCreditLimit());
        }
        if (command.getTags() != null) {
            customer.setTags(command.getTags());
        }
        if (command.getNotes() != null) {
            customer.setNotes(command.getNotes());
        }

        Customer savedCustomer = customerRepository.save(customer);
        publishEvents(savedCustomer);

        return savedCustomer;
    }

    @Transactional
    public void advanceLifecycleStage(CustomerCommand.AdvanceLifecycleStageCommand command) {
        log.info("Advancing lifecycle stage for customer: {} to stage: {}",
            command.getCustomerId(), command.getNewStage());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.advanceLifecycleStage(command.getNewStage(), command.getUpdatedBy());
        customerRepository.save(customer);
        publishEvents(customer);

        log.info("Advanced customer: {} to stage: {}", command.getCustomerId(), command.getNewStage());
    }

    @Transactional
    public void assignOwner(CustomerCommand.AssignOwnerCommand command) {
        log.info("Assigning owner: {} to customer: {}", command.getOwnerId(), command.getCustomerId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.assignOwner(command.getOwnerId(), command.getOwnerName(), command.getTerritory());
        customerRepository.save(customer);

        log.info("Assigned owner: {} to customer: {}", command.getOwnerId(), command.getCustomerId());
    }

    @Transactional
    public void setFollowUpDate(CustomerCommand.SetFollowUpDateCommand command) {
        log.info("Setting follow-up date for customer: {}", command.getCustomerId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.setFollowUpDate(command.getFollowUpDate());
        customerRepository.save(customer);

        log.info("Set follow-up date for customer: {}", command.getCustomerId());
    }

    @Transactional
    public void addTag(CustomerCommand.AddTagCommand command) {
        log.info("Adding tag: {} to customer: {}", command.getTag(), command.getCustomerId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.addTag(command.getTag());
        customerRepository.save(customer);

        log.info("Added tag: {} to customer: {}", command.getTag(), command.getCustomerId());
    }

    @Transactional
    public void removeTag(CustomerCommand.RemoveTagCommand command) {
        log.info("Removing tag: {} from customer: {}", command.getTag(), command.getCustomerId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.removeTag(command.getTag());
        customerRepository.save(customer);

        log.info("Removed tag: {} from customer: {}", command.getTag(), command.getCustomerId());
    }

    @Transactional
    public void markAsChurned(CustomerCommand.MarkAsChurnedCommand command) {
        log.info("Marking customer: {} as churned", command.getCustomerId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.markAsChurned(command.getReason(), command.getUpdatedBy());
        customerRepository.save(customer);
        publishEvents(customer);

        log.info("Marked customer: {} as churned", command.getCustomerId());
    }

    @Transactional
    public void reactivate(CustomerCommand.ReactivateCustomerCommand command) {
        log.info("Reactivating customer: {}", command.getCustomerId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.reactivate(command.getUpdatedBy());
        customerRepository.save(customer);
        publishEvents(customer);

        log.info("Reactivated customer: {}", command.getCustomerId());
    }

    @Transactional
    public void setParentAccount(CustomerCommand.SetParentAccountCommand command) {
        log.info("Setting parent account: {} for customer: {}", command.getParentAccountId(), command.getCustomerId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.setParentAccount(command.getParentAccountId());
        customerRepository.save(customer);

        log.info("Set parent account: {} for customer: {}", command.getParentAccountId(), command.getCustomerId());
    }

    @Transactional
    public void delete(CustomerCommand.DeleteCustomerCommand command) {
        log.info("Deleting customer: {} for tenant: {}", command.getCustomerId(), command.getTenantId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        if (customer.getLifecycleStage() == Customer.CustomerLifecycleStage.CUSTOMER &&
            customer.getIsActive()) {
            throw new ValidationException("Cannot delete active customers. Mark as churned first.");
        }

        customerRepository.deleteByCustomerIdAndTenantId(command.getCustomerId(), command.getTenantId());

        log.info("Deleted customer: {}", command.getCustomerId());
    }

    private void publishEvents(Customer customer) {
        if (!customer.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(customer.getDomainEvents());
            customer.clearDomainEvents();
        }
    }
}
