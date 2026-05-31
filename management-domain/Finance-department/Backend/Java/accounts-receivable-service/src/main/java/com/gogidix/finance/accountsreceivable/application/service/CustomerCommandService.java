package com.gogidix.finance.accountsreceivable.application.service;

import com.gogidix.finance.accountsreceivable.domain.model.Customer;
import com.gogidix.finance.accountsreceivable.domain.port.in.CustomerCommand;
import com.gogidix.finance.accountsreceivable.domain.port.out.EventPublisher;
import com.gogidix.finance.accountsreceivable.domain.repository.CustomerRepository;
import com.gogidix.finance.accountsreceivable.shared.exception.ConflictException;
import com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException;
import com.gogidix.finance.accountsreceivable.shared.exception.ValidationException;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        log.info("Creating customer for tenant: {}, code: {}",
            command.getTenantId(), command.getCustomerCode());

        // Check if customer code already exists
        if (customerRepository.existsByCustomerCodeAndTenantId(
            command.getCustomerCode(), command.getTenantId())) {
            throw new ConflictException("Customer", command.getCustomerCode());
        }

        Customer customer = Customer.create(
            command.getTenantId(),
            command.getCustomerCode(),
            command.getCustomerName(),
            command.getCustomerType(),
            command.getCurrency(),
            command.getEmail()
        );

        // Set additional fields
        customer.setPhone(command.getPhone());
        customer.setWebsite(command.getWebsite());
        customer.setTaxId(command.getTaxId());
        customer.setTaxRegistrationNumber(command.getTaxRegistrationNumber());
        customer.setBillingAddressLine1(command.getBillingAddressLine1());
        customer.setBillingAddressLine2(command.getBillingAddressLine2());
        customer.setBillingCity(command.getBillingCity());
        customer.setBillingState(command.getBillingState());
        customer.setBillingPostalCode(command.getBillingPostalCode());
        customer.setBillingCountry(command.getBillingCountry());
        customer.setShippingAddressLine1(command.getShippingAddressLine1());
        customer.setShippingAddressLine2(command.getShippingAddressLine2());
        customer.setShippingCity(command.getShippingCity());
        customer.setShippingState(command.getShippingState());
        customer.setShippingPostalCode(command.getShippingPostalCode());
        customer.setShippingCountry(command.getShippingCountry());
        customer.setPaymentTerms(command.getPaymentTerms());
        customer.setCreditLimit(command.getCreditLimit() != null ? command.getCreditLimit() : 0);
        customer.setCreditDays(command.getCreditDays() != null ? command.getCreditDays() : 30);
        customer.setSalesRepresentative(command.getSalesRepresentative());
        customer.setIndustry(command.getIndustry());
        customer.setNotes(command.getNotes());
        customer.setDefaultPaymentMethod(command.getDefaultPaymentMethod());
        customer.setBankAccountNumber(command.getBankAccountNumber());
        customer.setBankName(command.getBankName());
        customer.setBankRoutingNumber(command.getBankRoutingNumber());
        customer.setAllowCredit(command.getAllowCredit() != null ? command.getAllowCredit() : false);
        customer.setSendElectronicInvoices(command.getSendElectronicInvoices() != null ? command.getSendElectronicInvoices() : false);
        customer.setInvoiceDeliveryEmail(command.getInvoiceDeliveryEmail());
        customer.setParentCustomerId(command.getParentCustomerId());
        customer.setIsParentCustomer(command.getIsParentCustomer() != null ? command.getIsParentCustomer() : false);
        customer.setTags(command.getTags() != null ? command.getTags() : List.of());
        customer.setPaymentGatewayCustomerId(command.getPaymentGatewayCustomerId());
        customer.setAutoChargePaymentMethod(command.getAutoChargePaymentMethod());

        // Recalculate available credit
        customer.calculateAvailableCredit();

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

        if (command.getCustomerName() != null) {
            customer.setCustomerName(command.getCustomerName());
        }
        if (command.getEmail() != null) {
            customer.setEmail(command.getEmail());
        }
        if (command.getPhone() != null) {
            customer.setPhone(command.getPhone());
        }
        if (command.getWebsite() != null) {
            customer.setWebsite(command.getWebsite());
        }
        if (command.getBillingAddressLine1() != null) {
            customer.setBillingAddressLine1(command.getBillingAddressLine1());
        }
        if (command.getBillingAddressLine2() != null) {
            customer.setBillingAddressLine2(command.getBillingAddressLine2());
        }
        if (command.getBillingCity() != null) {
            customer.setBillingCity(command.getBillingCity());
        }
        if (command.getBillingState() != null) {
            customer.setBillingState(command.getBillingState());
        }
        if (command.getBillingPostalCode() != null) {
            customer.setBillingPostalCode(command.getBillingPostalCode());
        }
        if (command.getBillingCountry() != null) {
            customer.setBillingCountry(command.getBillingCountry());
        }
        if (command.getShippingAddressLine1() != null) {
            customer.setShippingAddressLine1(command.getShippingAddressLine1());
        }
        if (command.getShippingAddressLine2() != null) {
            customer.setShippingAddressLine2(command.getShippingAddressLine2());
        }
        if (command.getShippingCity() != null) {
            customer.setShippingCity(command.getShippingCity());
        }
        if (command.getShippingState() != null) {
            customer.setShippingState(command.getShippingState());
        }
        if (command.getShippingPostalCode() != null) {
            customer.setShippingPostalCode(command.getShippingPostalCode());
        }
        if (command.getShippingCountry() != null) {
            customer.setShippingCountry(command.getShippingCountry());
        }
        if (command.getPaymentTerms() != null) {
            customer.setPaymentTerms(command.getPaymentTerms());
        }
        if (command.getCreditLimit() != null) {
            customer.setCreditLimit(command.getCreditLimit());
        }
        if (command.getCreditDays() != null) {
            customer.setCreditDays(command.getCreditDays());
        }
        if (command.getSalesRepresentative() != null) {
            customer.setSalesRepresentative(command.getSalesRepresentative());
        }
        if (command.getIndustry() != null) {
            customer.setIndustry(command.getIndustry());
        }
        if (command.getNotes() != null) {
            customer.setNotes(command.getNotes());
        }
        if (command.getDefaultPaymentMethod() != null) {
            customer.setDefaultPaymentMethod(command.getDefaultPaymentMethod());
        }
        if (command.getBankAccountNumber() != null) {
            customer.setBankAccountNumber(command.getBankAccountNumber());
        }
        if (command.getBankName() != null) {
            customer.setBankName(command.getBankName());
        }
        if (command.getBankRoutingNumber() != null) {
            customer.setBankRoutingNumber(command.getBankRoutingNumber());
        }
        if (command.getAllowCredit() != null) {
            customer.setAllowCredit(command.getAllowCredit());
        }
        if (command.getSendElectronicInvoices() != null) {
            customer.setSendElectronicInvoices(command.getSendElectronicInvoices());
        }
        if (command.getInvoiceDeliveryEmail() != null) {
            customer.setInvoiceDeliveryEmail(command.getInvoiceDeliveryEmail());
        }
        if (command.getTags() != null) {
            customer.setTags(command.getTags());
        }
        if (command.getPaymentGatewayCustomerId() != null) {
            customer.setPaymentGatewayCustomerId(command.getPaymentGatewayCustomerId());
        }
        if (command.getAutoChargePaymentMethod() != null) {
            customer.setAutoChargePaymentMethod(command.getAutoChargePaymentMethod());
        }

        customer.calculateAvailableCredit();

        Customer savedCustomer = customerRepository.save(customer);
        publishEvents(savedCustomer);

        log.info("Updated customer: {}", command.getCustomerId());
        return savedCustomer;
    }

    @Transactional
    public void activate(CustomerCommand.ActivateCustomerCommand command) {
        log.info("Activating customer: {} for tenant: {}", command.getCustomerId(), command.getTenantId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.activate();
        customerRepository.save(customer);
        publishEvents(customer);

        log.info("Activated customer: {}", command.getCustomerId());
    }

    @Transactional
    public void suspend(CustomerCommand.SuspendCustomerCommand command) {
        log.info("Suspending customer: {} for tenant: {}", command.getCustomerId(), command.getTenantId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.suspend(command.getReason());
        customerRepository.save(customer);
        publishEvents(customer);

        log.info("Suspended customer: {}", command.getCustomerId());
    }

    @Transactional
    public void updateCreditInfo(CustomerCommand.UpdateCreditInfoCommand command) {
        log.info("Updating credit info for customer: {} for tenant: {}",
            command.getCustomerId(), command.getTenantId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        customer.updateCreditInfo(command.getCreditLimit(), command.getCreditDays());
        customerRepository.save(customer);
        publishEvents(customer);

        log.info("Updated credit info for customer: {}", command.getCustomerId());
    }

    @Transactional
    public void delete(CustomerCommand.DeleteCustomerCommand command) {
        log.info("Deleting customer: {} for tenant: {}", command.getCustomerId(), command.getTenantId());

        Customer customer = customerRepository.findByCustomerIdAndTenantId(
            command.getCustomerId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Customer", command.getCustomerId()));

        if (customer.getOutstandingBalance() != null &&
            customer.getOutstandingBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
            throw new ValidationException("Cannot delete customer with outstanding balance");
        }

        customerRepository.deleteByCustomerIdAndTenantId(command.getCustomerId(), command.getTenantId());

        log.info("Deleted customer: {}", command.getCustomerId());
    }

    private void publishEvents(Customer customer) {
        if (!customer.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(customer.getDomainEvents().stream().map(e -> (Object) e).toList());
            customer.clearDomainEvents();
        }
    }
}
