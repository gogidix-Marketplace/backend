package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.application.dto.response.CustomerResponseDto;
import com.gogidix.sales.crm.application.service.CustomerCommandService;
import com.gogidix.sales.crm.application.service.CustomerQueryService;
import com.gogidix.sales.crm.domain.model.Customer;
import com.gogidix.sales.crm.domain.port.in.CustomerCommand;
import com.gogidix.sales.crm.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            RequestContextHolder.getUserId(),
            request.getCompanyName(),
            request.getIndustry(),
            request.getSegment(),
            request.getLifecycleStage(),
            request.getLeadSource(),
            request.getWebsite(),
            request.getDescription(),
            request.getEmployeeCount(),
            request.getAnnualRevenue(),
            request.getOwnerId(),
            request.getOwnerName(),
            request.getTerritory(),
            request.getBillingAddressStreet(),
            request.getBillingAddressCity(),
            request.getBillingAddressState(),
            request.getBillingAddressPostalCode(),
            request.getBillingAddressCountry(),
            request.getShippingAddressStreet(),
            request.getShippingAddressCity(),
            request.getShippingAddressState(),
            request.getShippingAddressPostalCode(),
            request.getShippingAddressCountry(),
            request.getPhoneNumber(),
            request.getEmail(),
            request.getAccountNumber(),
            request.getAccountType(),
            request.getParentAccountId(),
            request.getTaxId(),
            request.getPaymentTerms(),
            request.getCurrency(),
            request.getCreditLimit(),
            request.getTags(),
            request.getNotes()
        );

        Customer customer = customerCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(customer));
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<CustomerResponseDto> getCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        Customer customer = customerQueryService.getByCustomerIdAndTenantId(
            customerId, RequestContextHolder.getTenantId());
        return ResponseEntity.ok(toDto(customer));
    }

    @GetMapping
    @Operation(summary = "Get all customers for tenant")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<Customer> customers = customerQueryService.getAllForTenant(RequestContextHolder.getTenantId());
        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated customers")
    public ResponseEntity<Page<CustomerResponseDto>> getPaginatedCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        // Note: In real implementation, you would need a Pageable parameter
        // This is simplified for the example
        List<Customer> customers = customerQueryService.getAllForTenant(RequestContextHolder.getTenantId());
        // Convert to page - simplified
        return ResponseEntity.ok(null);
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers by name")
    public ResponseEntity<List<CustomerResponseDto>> searchCustomers(
            @Parameter(description = "Search term") @RequestParam String searchTerm) {
        List<Customer> customers = customerQueryService.searchByName(
            RequestContextHolder.getTenantId(), searchTerm);
        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @GetMapping("/lifecycle-stage/{stage}")
    @Operation(summary = "Get customers by lifecycle stage")
    public ResponseEntity<List<CustomerResponseDto>> getCustomersByLifecycleStage(
            @Parameter(description = "Lifecycle stage") @PathVariable Customer.CustomerLifecycleStage stage) {
        List<Customer> customers = customerQueryService.getByLifecycleStage(
            RequestContextHolder.getTenantId(), stage);
        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @GetMapping("/segment/{segment}")
    @Operation(summary = "Get customers by segment")
    public ResponseEntity<List<CustomerResponseDto>> getCustomersBySegment(
            @Parameter(description = "Customer segment") @PathVariable Customer.CustomerSegment segment) {
        List<Customer> customers = customerQueryService.getBySegment(
            RequestContextHolder.getTenantId(), segment);
        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get customers by owner")
    public ResponseEntity<List<CustomerResponseDto>> getCustomersByOwner(
            @Parameter(description = "Owner ID") @PathVariable String ownerId) {
        List<Customer> customers = customerQueryService.getByOwner(
            RequestContextHolder.getTenantId(), ownerId);
        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @GetMapping("/follow-up-needed")
    @Operation(summary = "Get customers needing follow-up")
    public ResponseEntity<List<CustomerResponseDto>> getCustomersNeedingFollowUp(
            @Parameter(description = "Before date") @RequestParam LocalDate beforeDate) {
        List<Customer> customers = customerQueryService.getCustomersNeedingFollowUp(
            RequestContextHolder.getTenantId(), beforeDate);
        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get customer summary")
    public ResponseEntity<CustomerQueryService.CustomerSummary> getSummary() {
        CustomerQueryService.CustomerSummary summary =
            customerQueryService.getSummary(RequestContextHolder.getTenantId());
        return ResponseEntity.ok(summary);
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Valid @RequestBody UpdateCustomerRequestDto request) {
        CustomerCommand.UpdateCustomerCommand command = new CustomerCommand.UpdateCustomerCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.getCompanyName(),
            request.getIndustry(),
            request.getSegment(),
            request.getDescription(),
            request.getWebsite(),
            request.getEmployeeCount(),
            request.getAnnualRevenue(),
            request.getPhoneNumber(),
            request.getEmail(),
            request.getTerritory(),
            request.getSatisfactionScore(),
            request.getPaymentTerms(),
            request.getCurrency(),
            request.getCreditLimit(),
            request.getTags(),
            request.getNotes()
        );

        Customer customer = customerCommandService.update(command);
        return ResponseEntity.ok(toDto(customer));
    }

    @PostMapping("/{customerId}/lifecycle-stage")
    @Operation(summary = "Advance customer lifecycle stage")
    public ResponseEntity<Void> advanceLifecycleStage(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody AdvanceLifecycleStageRequestDto request) {
        CustomerCommand.AdvanceLifecycleStageCommand command = new CustomerCommand.AdvanceLifecycleStageCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.getNewStage(),
            RequestContextHolder.getUserId()
        );

        customerCommandService.advanceLifecycleStage(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/assign-owner")
    @Operation(summary = "Assign owner to customer")
    public ResponseEntity<Void> assignOwner(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody AssignOwnerRequestDto request) {
        CustomerCommand.AssignOwnerCommand command = new CustomerCommand.AssignOwnerCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.getOwnerId(),
            request.getOwnerName(),
            request.getTerritory()
        );

        customerCommandService.assignOwner(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/follow-up")
    @Operation(summary = "Set follow-up date for customer")
    public ResponseEntity<Void> setFollowUpDate(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody SetFollowUpDateRequestDto request) {
        CustomerCommand.SetFollowUpDateCommand command = new CustomerCommand.SetFollowUpDateCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.getFollowUpDate()
        );

        customerCommandService.setFollowUpDate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/tags")
    @Operation(summary = "Add tag to customer")
    public ResponseEntity<Void> addTag(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody AddTagRequestDto request) {
        CustomerCommand.AddTagCommand command = new CustomerCommand.AddTagCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.getTag()
        );

        customerCommandService.addTag(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}/tags/{tag}")
    @Operation(summary = "Remove tag from customer")
    public ResponseEntity<Void> removeTag(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Tag") @PathVariable String tag) {
        CustomerCommand.RemoveTagCommand command = new CustomerCommand.RemoveTagCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            tag
        );

        customerCommandService.removeTag(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/churn")
    @Operation(summary = "Mark customer as churned")
    public ResponseEntity<Void> markAsChurned(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody MarkAsChurnedRequestDto request) {
        CustomerCommand.MarkAsChurnedCommand command = new CustomerCommand.MarkAsChurnedCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.getReason(),
            RequestContextHolder.getUserId()
        );

        customerCommandService.markAsChurned(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/reactivate")
    @Operation(summary = "Reactivate churned customer")
    public ResponseEntity<Void> reactivateCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        CustomerCommand.ReactivateCustomerCommand command = new CustomerCommand.ReactivateCustomerCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            RequestContextHolder.getUserId()
        );

        customerCommandService.reactivate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/parent-account")
    @Operation(summary = "Set parent account for customer")
    public ResponseEntity<Void> setParentAccount(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestBody SetParentAccountRequestDto request) {
        CustomerCommand.SetParentAccountCommand command = new CustomerCommand.SetParentAccountCommand(
            RequestContextHolder.getTenantId(),
            customerId,
            request.getParentAccountId()
        );

        customerCommandService.setParentAccount(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete customer")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        CustomerCommand.DeleteCustomerCommand command = new CustomerCommand.DeleteCustomerCommand(
            RequestContextHolder.getTenantId(),
            customerId
        );

        customerCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private CustomerResponseDto toDto(Customer customer) {
        return CustomerResponseDto.builder()
            .id(customer.getId())
            .customerId(customer.getCustomerId())
            .tenantId(customer.getTenantId())
            .accountNumber(customer.getAccountNumber())
            .companyName(customer.getCompanyName())
            .industry(customer.getIndustry())
            .segment(mapSegment(customer.getSegment()))
            .lifecycleStage(mapLifecycleStage(customer.getLifecycleStage()))
            .website(customer.getWebsite())
            .description(customer.getDescription())
            .employeeCount(customer.getEmployeeCount())
            .annualRevenue(customer.getAnnualRevenue())
            .leadSource(customer.getLeadSource())
            .leadDate(customer.getLeadDate())
            .convertedDate(customer.getConvertedDate())
            .ownerId(customer.getOwnerId())
            .ownerName(customer.getOwnerName())
            .territory(customer.getTerritory())
            .billingAddress(mapAddress(
                customer.getBillingAddressStreet(), customer.getBillingAddressCity(),
                customer.getBillingAddressState(), customer.getBillingAddressPostalCode(),
                customer.getBillingAddressCountry()
            ))
            .shippingAddress(mapAddress(
                customer.getShippingAddressStreet(), customer.getShippingAddressCity(),
                customer.getShippingAddressState(), customer.getShippingAddressPostalCode(),
                customer.getShippingAddressCountry()
            ))
            .phoneNumber(customer.getPhoneNumber())
            .email(customer.getEmail())
            .isActive(customer.getIsActive())
            .lastContactDate(customer.getLastContactDate())
            .nextFollowUpDate(customer.getNextFollowUpDate())
            .totalInteractions(customer.getTotalInteractions())
            .totalDealValue(customer.getTotalDealValue())
            .openDealsCount(customer.getOpenDealsCount())
            .parentAccountId(customer.getParentAccountId())
            .childAccountIds(customer.getChildAccountIds())
            .accountType(mapAccountType(customer.getAccountType()))
            .taxId(customer.getTaxId())
            .paymentTerms(customer.getPaymentTerms())
            .currency(customer.getCurrency())
            .creditLimit(customer.getCreditLimit())
            .tags(customer.getTags())
            .notes(customer.getNotes())
            .satisfactionScore(customer.getSatisfactionScore())
            .churnDate(customer.getChurnDate())
            .churnReason(customer.getChurnReason())
            .createdAt(customer.getCreatedAt())
            .updatedAt(customer.getUpdatedAt())
            .build();
    }

    private CustomerResponseDto.CustomerSegmentDto mapSegment(Customer.CustomerSegment segment) {
        return segment != null ? CustomerResponseDto.CustomerSegmentDto.valueOf(segment.name()) : null;
    }

    private CustomerResponseDto.CustomerLifecycleStageDto mapLifecycleStage(Customer.CustomerLifecycleStage stage) {
        return stage != null ? CustomerResponseDto.CustomerLifecycleStageDto.valueOf(stage.name()) : null;
    }

    private CustomerResponseDto.AccountTypeDto mapAccountType(Customer.AccountType type) {
        return type != null ? CustomerResponseDto.AccountTypeDto.valueOf(type.name()) : null;
    }

    private CustomerResponseDto.AddressDto mapAddress(String street, String city, String state,
                                                       String postalCode, String country) {
        if (street == null && city == null && state == null && postalCode == null && country == null) {
            return null;
        }
        return CustomerResponseDto.AddressDto.builder()
            .street(street)
            .city(city)
            .state(state)
            .postalCode(postalCode)
            .country(country)
            .build();
    }

    // Request DTOs
    @Data
    public static class CreateCustomerRequestDto {
        public String companyName;
        public String industry;
        public Customer.CustomerSegment segment;
        public Customer.CustomerLifecycleStage lifecycleStage;
        public String leadSource;
        public String website;
        public String description;
        public Integer employeeCount;
        public Double annualRevenue;
        public String ownerId;
        public String ownerName;
        public String territory;
        public String billingAddressStreet;
        public String billingAddressCity;
        public String billingAddressState;
        public String billingAddressPostalCode;
        public String billingAddressCountry;
        public String shippingAddressStreet;
        public String shippingAddressCity;
        public String shippingAddressState;
        public String shippingAddressPostalCode;
        public String shippingAddressCountry;
        public String phoneNumber;
        public String email;
        public String accountNumber;
        public Customer.AccountType accountType;
        public String parentAccountId;
        public String taxId;
        public String paymentTerms;
        public String currency;
        public Double creditLimit;
        public List<String> tags;
        public String notes;
    }

    @Data

    public static class UpdateCustomerRequestDto {
        public String companyName;
        public String industry;
        public Customer.CustomerSegment segment;
        public String description;
        public String website;
        public Integer employeeCount;
        public Double annualRevenue;
        public String phoneNumber;
        public String email;
        public String territory;
        public Integer satisfactionScore;
        public String paymentTerms;
        public String currency;
        public Double creditLimit;
        public List<String> tags;
        public String notes;
    }

    @Data

    public static class AdvanceLifecycleStageRequestDto {
        public Customer.CustomerLifecycleStage newStage;
    }

    @Data

    public static class AssignOwnerRequestDto {
        public String ownerId;
        public String ownerName;
        public String territory;
    }

    @Data

    public static class SetFollowUpDateRequestDto {
        public LocalDate followUpDate;
    }

    @Data

    public static class AddTagRequestDto {
        public String tag;
    }

    @Data

    public static class MarkAsChurnedRequestDto {
        public String reason;
    }

    @Data

    public static class SetParentAccountRequestDto {
        public String parentAccountId;
    }
}
