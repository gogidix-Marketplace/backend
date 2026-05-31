package com.gogidix.finance.accountsreceivable.interfaces.rest;

import com.gogidix.finance.accountsreceivable.application.dto.response.CustomerResponseDto;
import com.gogidix.finance.accountsreceivable.application.service.CustomerCommandService;
import com.gogidix.finance.accountsreceivable.application.service.CustomerQueryService;
import com.gogidix.finance.accountsreceivable.domain.model.Customer;
import com.gogidix.finance.accountsreceivable.domain.port.in.CustomerCommand;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Customer REST Controller
 * Handles HTTP requests for customer operations
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerCommandService customerCommandService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CreateCustomerRequestDto request) {
        CustomerCommand.CreateCustomerCommand command = new CustomerCommand.CreateCustomerCommand(
            RequestContextHolder.getTenantId(),
            request.customerCode,
            request.customerName,
            request.customerType,
            request.currency,
            request.email,
            request.phone,
            request.website,
            request.taxId,
            request.taxRegistrationNumber,
            request.billingAddressLine1,
            request.billingAddressLine2,
            request.billingCity,
            request.billingState,
            request.billingPostalCode,
            request.billingCountry,
            request.shippingAddressLine1,
            request.shippingAddressLine2,
            request.shippingCity,
            request.shippingState,
            request.shippingPostalCode,
            request.shippingCountry,
            request.paymentTerms,
            request.creditLimit,
            request.creditDays,
            request.salesRepresentative,
            request.industry,
            request.notes,
            request.defaultPaymentMethod,
            request.bankAccountNumber,
            request.bankName,
            request.bankRoutingNumber,
            request.allowCredit,
            request.sendElectronicInvoices,
            request.invoiceDeliveryEmail,
            request.parentCustomerId,
            request.isParentCustomer,
            request.tags,
            request.paymentGatewayCustomerId,
            request.autoChargePaymentMethod
        );

        Customer customer = customerCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(customer));
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<CustomerResponseDto> getCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        Customer customer = customerQueryService.getById(customerId);
        return ResponseEntity.ok(toDto(customer));
    }

    @GetMapping("/code/{customerCode}")
    @Operation(summary = "Get customer by code")
    public ResponseEntity<CustomerResponseDto> getCustomerByCode(
            @Parameter(description = "Customer Code") @PathVariable String customerCode) {
        Customer customer = customerQueryService.getByCode(customerCode);
        return ResponseEntity.ok(toDto(customer));
    }

    @GetMapping
    @Operation(summary = "Get all customers for tenant")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<Customer> customers = customerQueryService.getAllForTenant();
        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @GetMapping("/type/{customerType}")
    @Operation(summary = "Get customers by type")
    public ResponseEntity<Page<CustomerResponseDto>> getCustomersByType(
            @Parameter(description = "Customer Type") @PathVariable String customerType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Customer> customers = customerQueryService.getByType(customerType, page, size);
        return ResponseEntity.ok(customers.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers by status")
    public ResponseEntity<Page<CustomerResponseDto>> getCustomersByStatus(
            @Parameter(description = "Status") @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Customer> customers = customerQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(customers.map(this::toDto));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue customers")
    public ResponseEntity<Page<CustomerResponseDto>> getOverdueCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Customer> customers = customerQueryService.getOverdueCustomers(page, size);
        return ResponseEntity.ok(customers.map(this::toDto));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers")
    public ResponseEntity<Page<CustomerResponseDto>> searchCustomers(
            @RequestParam String searchTerm,
            @RequestParam(required = false) String customerType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Customer> customers = customerQueryService.search(searchTerm, customerType, status, page, size);
        return ResponseEntity.ok(customers.map(this::toDto));
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Valid @RequestBody UpdateCustomerRequestDto request) {

        CustomerCommand.UpdateCustomerCommand command = new CustomerCommand.UpdateCustomerCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.customerName,
            request.email,
            request.phone,
            request.website,
            request.billingAddressLine1,
            request.billingAddressLine2,
            request.billingCity,
            request.billingState,
            request.billingPostalCode,
            request.billingCountry,
            request.shippingAddressLine1,
            request.shippingAddressLine2,
            request.shippingCity,
            request.shippingState,
            request.shippingPostalCode,
            request.shippingCountry,
            request.paymentTerms,
            request.creditLimit,
            request.creditDays,
            request.salesRepresentative,
            request.industry,
            request.notes,
            request.defaultPaymentMethod,
            request.bankAccountNumber,
            request.bankName,
            request.bankRoutingNumber,
            request.allowCredit,
            request.sendElectronicInvoices,
            request.invoiceDeliveryEmail,
            request.tags,
            request.paymentGatewayCustomerId,
            request.autoChargePaymentMethod
        );

        Customer customer = customerCommandService.update(command);
        return ResponseEntity.ok(toDto(customer));
    }

    @PostMapping("/{customerId}/activate")
    @Operation(summary = "Activate customer")
    public ResponseEntity<Void> activateCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {

        CustomerCommand.ActivateCustomerCommand command = new CustomerCommand.ActivateCustomerCommand(
            RequestContextHolder.getTenantId(), customerId);

        customerCommandService.activate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/suspend")
    @Operation(summary = "Suspend customer")
    public ResponseEntity<Void> suspendCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody SuspendCustomerRequestDto request) {

        CustomerCommand.SuspendCustomerCommand command = new CustomerCommand.SuspendCustomerCommand(
            RequestContextHolder.getTenantId(), customerId, request.reason);

        customerCommandService.suspend(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{customerId}/credit")
    @Operation(summary = "Update customer credit information")
    public ResponseEntity<Void> updateCreditInfo(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody UpdateCreditInfoRequestDto request) {

        CustomerCommand.UpdateCreditInfoCommand command = new CustomerCommand.UpdateCreditInfoCommand(
            RequestContextHolder.getTenantId(), customerId, request.creditLimit, request.creditDays);

        customerCommandService.updateCreditInfo(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete customer")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {

        CustomerCommand.DeleteCustomerCommand command = new CustomerCommand.DeleteCustomerCommand(
            RequestContextHolder.getTenantId(), customerId);

        customerCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private CustomerResponseDto toDto(Customer customer) {
        return CustomerResponseDto.builder()
            .id(customer.getId())
            .customerId(customer.getCustomerId())
            .tenantId(customer.getTenantId())
            .customerCode(customer.getCustomerCode())
            .customerName(customer.getCustomerName())
            .customerType(mapCustomerType(customer.getCustomerType()))
            .email(customer.getEmail())
            .phone(customer.getPhone())
            .website(customer.getWebsite())
            .taxId(customer.getTaxId())
            .taxRegistrationNumber(customer.getTaxRegistrationNumber())
            .billingAddressLine1(customer.getBillingAddressLine1())
            .billingAddressLine2(customer.getBillingAddressLine2())
            .billingCity(customer.getBillingCity())
            .billingState(customer.getBillingState())
            .billingPostalCode(customer.getBillingPostalCode())
            .billingCountry(customer.getBillingCountry())
            .shippingAddressLine1(customer.getShippingAddressLine1())
            .shippingAddressLine2(customer.getShippingAddressLine2())
            .shippingCity(customer.getShippingCity())
            .shippingState(customer.getShippingState())
            .shippingPostalCode(customer.getShippingPostalCode())
            .shippingCountry(customer.getShippingCountry())
            .currency(customer.getCurrency())
            .paymentTerms(customer.getPaymentTerms())
            .creditLimit(customer.getCreditLimit())
            .creditDays(customer.getCreditDays())
            .salesRepresentative(customer.getSalesRepresentative())
            .customerSince(null) // TODO: Convert String to Instant if needed
            .status(mapCustomerStatus(customer.getStatus()))
            .industry(customer.getIndustry())
            .notes(customer.getNotes())
            .defaultPaymentMethod(customer.getDefaultPaymentMethod())
            .allowCredit(customer.getAllowCredit())
            .sendElectronicInvoices(customer.getSendElectronicInvoices())
            .invoiceDeliveryEmail(customer.getInvoiceDeliveryEmail())
            .parentCustomerId(customer.getParentCustomerId())
            .isParentCustomer(customer.getIsParentCustomer())
            .outstandingBalance(customer.getOutstandingBalance())
            .creditUsed(customer.getCreditUsed())
            .availableCredit(customer.getAvailableCredit())
            .overdueInvoicesCount(customer.getOverdueInvoicesCount())
            .lastPaymentDate(customer.getLastPaymentDate())
            .lastInvoiceDate(customer.getLastInvoiceDate())
            .totalPurchases(customer.getTotalPurchases())
            .totalInvoicesIssued(customer.getTotalInvoicesIssued())
            .assignedCollector(customer.getAssignedCollector())
            .collectionStage(mapCollectionStage(customer.getCollectionStage()))
            .tags(customer.getTags())
            .createdAt(customer.getCreatedAt())
            .updatedAt(customer.getUpdatedAt())
            .build();
    }

    private CustomerResponseDto.CustomerTypeDto mapCustomerType(Customer.CustomerType type) {
        return type != null ? CustomerResponseDto.CustomerTypeDto.valueOf(type.name()) : null;
    }

    private CustomerResponseDto.CustomerStatusDto mapCustomerStatus(Customer.CustomerStatus status) {
        return status != null ? CustomerResponseDto.CustomerStatusDto.valueOf(status.name()) : null;
    }

    private CustomerResponseDto.CollectionStageDto mapCollectionStage(Customer.CollectionStage stage) {
        return stage != null ? CustomerResponseDto.CollectionStageDto.valueOf(stage.name()) : null;
    }

    // Request DTOs
    public static class CreateCustomerRequestDto {
        public String customerCode;
        public String customerName;
        public Customer.CustomerType customerType;
        public String currency;
        public String email;
        public String phone;
        public String website;
        public String taxId;
        public String taxRegistrationNumber;
        public String billingAddressLine1;
        public String billingAddressLine2;
        public String billingCity;
        public String billingState;
        public String billingPostalCode;
        public String billingCountry;
        public String shippingAddressLine1;
        public String shippingAddressLine2;
        public String shippingCity;
        public String shippingState;
        public String shippingPostalCode;
        public String shippingCountry;
        public String paymentTerms;
        public Integer creditLimit;
        public Integer creditDays;
        public String salesRepresentative;
        public String industry;
        public String notes;
        public String defaultPaymentMethod;
        public String bankAccountNumber;
        public String bankName;
        public String bankRoutingNumber;
        public Boolean allowCredit;
        public Boolean sendElectronicInvoices;
        public String invoiceDeliveryEmail;
        public String parentCustomerId;
        public Boolean isParentCustomer;
        public List<String> tags;
        public String paymentGatewayCustomerId;
        public Boolean autoChargePaymentMethod;
    }

    public static class UpdateCustomerRequestDto {
        public String customerName;
        public String email;
        public String phone;
        public String website;
        public String billingAddressLine1;
        public String billingAddressLine2;
        public String billingCity;
        public String billingState;
        public String billingPostalCode;
        public String billingCountry;
        public String shippingAddressLine1;
        public String shippingAddressLine2;
        public String shippingCity;
        public String shippingState;
        public String shippingPostalCode;
        public String shippingCountry;
        public String paymentTerms;
        public Integer creditLimit;
        public Integer creditDays;
        public String salesRepresentative;
        public String industry;
        public String notes;
        public String defaultPaymentMethod;
        public String bankAccountNumber;
        public String bankName;
        public String bankRoutingNumber;
        public Boolean allowCredit;
        public Boolean sendElectronicInvoices;
        public String invoiceDeliveryEmail;
        public List<String> tags;
        public String paymentGatewayCustomerId;
        public Boolean autoChargePaymentMethod;
    }

    public static class SuspendCustomerRequestDto {
        public String reason;
    }

    public static class UpdateCreditInfoRequestDto {
        public Integer creditLimit;
        public Integer creditDays;
    }
}
