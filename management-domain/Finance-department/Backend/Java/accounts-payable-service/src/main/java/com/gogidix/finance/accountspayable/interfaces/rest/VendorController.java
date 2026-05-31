package com.gogidix.finance.accountspayable.interfaces.rest;

import com.gogidix.finance.accountspayable.application.dto.response.VendorResponseDto;
import com.gogidix.finance.accountspayable.application.service.VendorCommandService;
import com.gogidix.finance.accountspayable.application.service.VendorQueryService;
import com.gogidix.finance.accountspayable.domain.model.Vendor;
import com.gogidix.finance.accountspayable.domain.port.in.VendorCommand;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Vendor REST Controller
 * Handles HTTP requests for vendor operations
 */
@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendors", description = "Vendor management endpoints")
public class VendorController {

    private final VendorCommandService vendorCommandService;
    private final VendorQueryService vendorQueryService;

    @PostMapping
    @Operation(summary = "Create a new vendor")
    public ResponseEntity<VendorResponseDto> createVendor(
            @Valid @RequestBody CreateVendorRequestDto request) {
        VendorCommand.CreateVendorCommand command = VendorCommand.CreateVendorCommand.builder()
            .tenantId(RequestContextHolder.getTenantId())
            .vendorCode(request.vendorCode)
            .vendorName(request.vendorName)
            .vendorType(request.vendorType)
            .taxId(request.taxId)
            .currency(request.currency)
            .paymentTerms(request.paymentTerms)
            .paymentDays(request.paymentDays)
            .contactPerson(request.contactPerson)
            .email(request.email)
            .phone(request.phone)
            .website(request.website)
            .billingAddress(request.billingAddress)
            .shippingAddress(request.shippingAddress)
            .bankAccountNumber(request.bankAccountNumber)
            .bankRoutingNumber(request.bankRoutingNumber)
            .bankName(request.bankName)
            .bankAccountType(request.bankAccountType)
            .creditLimit(request.creditLimit)
            .notes(request.notes)
            .tags(request.tags)
            .parentVendorId(request.parentVendorId)
            .createdBy(RequestContextHolder.getUserId())
            .discountPercentage(request.discountPercentage)
            .validFrom(request.validFrom)
            .validUntil(request.validUntil)
            .build();

        Vendor vendor = vendorCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(vendor));
    }

    @GetMapping("/{vendorId}")
    @Operation(summary = "Get vendor by ID")
    public ResponseEntity<VendorResponseDto> getVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId) {
        Vendor vendor = vendorQueryService.getById(vendorId);
        return ResponseEntity.ok(toDto(vendor));
    }

    @GetMapping("/code/{vendorCode}")
    @Operation(summary = "Get vendor by code")
    public ResponseEntity<VendorResponseDto> getVendorByCode(
            @Parameter(description = "Vendor Code") @PathVariable String vendorCode) {
        Vendor vendor = vendorQueryService.getByCode(vendorCode);
        return ResponseEntity.ok(toDto(vendor));
    }

    @GetMapping
    @Operation(summary = "Get all vendors for tenant")
    public ResponseEntity<Page<VendorResponseDto>> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Vendor> vendors = vendorQueryService.getAllVendors(page, size);
        return ResponseEntity.ok(vendors.map(this::toDto));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active vendors")
    public ResponseEntity<Page<VendorResponseDto>> getActiveVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Vendor> vendors = vendorQueryService.getActiveVendors(page, size);
        return ResponseEntity.ok(vendors.map(this::toDto));
    }

    @GetMapping("/preferred")
    @Operation(summary = "Get preferred vendors")
    public ResponseEntity<Page<VendorResponseDto>> getPreferredVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Vendor> vendors = vendorQueryService.getPreferredVendors(page, size);
        return ResponseEntity.ok(vendors.map(this::toDto));
    }

    @GetMapping("/type/{vendorType}")
    @Operation(summary = "Get vendors by type")
    public ResponseEntity<Page<VendorResponseDto>> getVendorsByType(
            @Parameter(description = "Vendor Type") @PathVariable Vendor.VendorType vendorType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Vendor> vendors = vendorQueryService.getByType(vendorType, page, size);
        return ResponseEntity.ok(vendors.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get vendors by status")
    public ResponseEntity<Page<VendorResponseDto>> getVendorsByStatus(
            @Parameter(description = "Vendor Status") @PathVariable Vendor.VendorStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Vendor> vendors = vendorQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(vendors.map(this::toDto));
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Get vendors by tag")
    public ResponseEntity<Page<VendorResponseDto>> getVendorsByTag(
            @Parameter(description = "Tag") @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Vendor> vendors = vendorQueryService.getByTag(tag, page, size);
        return ResponseEntity.ok(vendors.map(this::toDto));
    }

    @GetMapping("/search")
    @Operation(summary = "Search vendors")
    public ResponseEntity<Page<VendorResponseDto>> searchVendors(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @RequestParam(required = false) Vendor.VendorType vendorType,
            @RequestParam(required = false) Vendor.VendorStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Vendor> vendors = vendorQueryService.search(searchTerm, vendorType, status, page, size);
        return ResponseEntity.ok(vendors.map(this::toDto));
    }

    @PutMapping("/{vendorId}")
    @Operation(summary = "Update vendor")
    public ResponseEntity<VendorResponseDto> updateVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @Valid @RequestBody UpdateVendorRequestDto request) {
        VendorCommand.UpdateVendorCommand command = new VendorCommand.UpdateVendorCommand(
            RequestContextHolder.getTenantId(),
            vendorId,
            request.vendorName,
            request.contactPerson,
            request.email,
            request.phone,
            request.billingAddress,
            request.shippingAddress,
            request.notes,
            request.tags,
            request.website
        );

        Vendor vendor = vendorCommandService.update(command);
        return ResponseEntity.ok(toDto(vendor));
    }

    @PostMapping("/{vendorId}/activate")
    @Operation(summary = "Activate vendor")
    public ResponseEntity<Void> activateVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId) {
        VendorCommand.ActivateVendorCommand command = new VendorCommand.ActivateVendorCommand(
            RequestContextHolder.getTenantId(), vendorId);
        vendorCommandService.activate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{vendorId}/deactivate")
    @Operation(summary = "Deactivate vendor")
    public ResponseEntity<Void> deactivateVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestBody DeactivateRequestDto request) {
        VendorCommand.DeactivateVendorCommand command = new VendorCommand.DeactivateVendorCommand(
            RequestContextHolder.getTenantId(), vendorId, request.reason);
        vendorCommandService.deactivate(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{vendorId}/suspend")
    @Operation(summary = "Suspend vendor")
    public ResponseEntity<Void> suspendVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestBody SuspendRequestDto request) {
        VendorCommand.SuspendVendorCommand command = new VendorCommand.SuspendVendorCommand(
            RequestContextHolder.getTenantId(), vendorId, request.reason);
        vendorCommandService.suspend(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{vendorId}/blacklist")
    @Operation(summary = "Blacklist vendor")
    public ResponseEntity<Void> blacklistVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestBody BlacklistRequestDto request) {
        VendorCommand.BlacklistVendorCommand command = new VendorCommand.BlacklistVendorCommand(
            RequestContextHolder.getTenantId(), vendorId, request.reason);
        vendorCommandService.blacklist(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vendorId}/preferred")
    @Operation(summary = "Set vendor as preferred")
    public ResponseEntity<Void> setPreferredVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestBody PreferredRequestDto request) {
        VendorCommand.SetPreferredVendorCommand command = new VendorCommand.SetPreferredVendorCommand(
            RequestContextHolder.getTenantId(), vendorId, request.preferred);
        vendorCommandService.setPreferred(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vendorId}/payment-terms")
    @Operation(summary = "Update vendor payment terms")
    public ResponseEntity<Void> updatePaymentTerms(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestBody PaymentTermsRequestDto request) {
        VendorCommand.UpdatePaymentTermsCommand command = new VendorCommand.UpdatePaymentTermsCommand(
            RequestContextHolder.getTenantId(), vendorId, request.paymentTerms, request.paymentDays);
        vendorCommandService.updatePaymentTerms(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vendorId}/bank-info")
    @Operation(summary = "Update vendor bank information")
    public ResponseEntity<Void> updateBankInfo(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestBody BankInfoRequestDto request) {
        VendorCommand.UpdateBankInfoCommand command = new VendorCommand.UpdateBankInfoCommand(
            RequestContextHolder.getTenantId(), vendorId,
            request.bankAccountNumber, request.bankRoutingNumber,
            request.bankName, request.bankAccountType);
        vendorCommandService.updateBankInfo(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{vendorId}/tags")
    @Operation(summary = "Add tag to vendor")
    public ResponseEntity<Void> addTag(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestBody TagRequestDto request) {
        VendorCommand.AddTagCommand command = new VendorCommand.AddTagCommand(
            RequestContextHolder.getTenantId(), vendorId, request.tag);
        vendorCommandService.addTag(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{vendorId}/tags/{tag}")
    @Operation(summary = "Remove tag from vendor")
    public ResponseEntity<Void> removeTag(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @Parameter(description = "Tag") @PathVariable String tag) {
        VendorCommand.RemoveTagCommand command = new VendorCommand.RemoveTagCommand(
            RequestContextHolder.getTenantId(), vendorId, tag);
        vendorCommandService.removeTag(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{vendorId}")
    @Operation(summary = "Delete vendor")
    public ResponseEntity<Void> deleteVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId) {
        VendorCommand.DeleteVendorCommand command = new VendorCommand.DeleteVendorCommand(
            RequestContextHolder.getTenantId(), vendorId);
        vendorCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private VendorResponseDto toDto(Vendor vendor) {
        return VendorResponseDto.builder()
            .id(vendor.getId())
            .vendorId(vendor.getVendorId())
            .tenantId(vendor.getTenantId())
            .vendorCode(vendor.getVendorCode())
            .vendorName(vendor.getVendorName())
            .vendorType(mapVendorType(vendor.getVendorType()))
            .taxId(vendor.getTaxId())
            .currency(vendor.getCurrency())
            .paymentTerms(vendor.getPaymentTerms())
            .paymentDays(vendor.getPaymentDays())
            .contactPerson(vendor.getContactPerson())
            .email(vendor.getEmail())
            .phone(vendor.getPhone())
            .website(vendor.getWebsite())
            .billingAddress(mapAddress(vendor.getBillingAddress()))
            .shippingAddress(mapAddress(vendor.getShippingAddress()))
            .status(mapVendorStatus(vendor.getStatus()))
            .createdBy(vendor.getCreatedBy())
            .activatedAt(vendor.getActivatedAt())
            .deactivatedAt(vendor.getDeactivatedAt())
            .deactivationReason(vendor.getDeactivationReason())
            .bankName(vendor.getBankName())
            .bankAccountType(vendor.getBankAccountType())
            .creditLimit(vendor.getCreditLimit())
            .notes(vendor.getNotes())
            .tags(vendor.getTags())
            .parentVendorId(vendor.getParentVendorId())
            .isPreferredVendor(vendor.getIsPreferredVendor())
            .discountPercentage(vendor.getDiscountPercentage())
            .validFrom(vendor.getValidFrom())
            .validUntil(vendor.getValidUntil())
            .createdAt(vendor.getCreatedAt())
            .updatedAt(vendor.getUpdatedAt())
            .build();
    }

    private VendorResponseDto.VendorTypeDto mapVendorType(Vendor.VendorType type) {
        return type != null ? VendorResponseDto.VendorTypeDto.valueOf(type.name()) : null;
    }

    private VendorResponseDto.VendorStatusDto mapVendorStatus(Vendor.VendorStatus status) {
        return status != null ? VendorResponseDto.VendorStatusDto.valueOf(status.name()) : null;
    }

    private VendorResponseDto.AddressDto mapAddress(Vendor.Address address) {
        if (address == null) return null;
        return VendorResponseDto.AddressDto.builder()
            .street(address.getStreet())
            .city(address.getCity())
            .state(address.getState())
            .postalCode(address.getPostalCode())
            .country(address.getCountry())
            .addressLine1(address.getAddressLine1())
            .addressLine2(address.getAddressLine2())
            .build();
    }

    // Request DTOs
    public static class CreateVendorRequestDto {
        public String vendorCode;
        public String vendorName;
        public Vendor.VendorType vendorType;
        public String taxId;
        public String currency;
        public String paymentTerms;
        public Integer paymentDays;
        public String contactPerson;
        public String email;
        public String phone;
        public String website;
        public Vendor.Address billingAddress;
        public Vendor.Address shippingAddress;
        public String bankAccountNumber;
        public String bankRoutingNumber;
        public String bankName;
        public String bankAccountType;
        public LocalDate creditLimit;
        public String notes;
        public List<String> tags;
        public String parentVendorId;
        public Double discountPercentage;
        public LocalDate validFrom;
        public LocalDate validUntil;
    }

    public static class UpdateVendorRequestDto {
        public String vendorName;
        public String contactPerson;
        public String email;
        public String phone;
        public Vendor.Address billingAddress;
        public Vendor.Address shippingAddress;
        public String notes;
        public List<String> tags;
        public String website;
    }

    public static class DeactivateRequestDto {
        public String reason;
    }

    public static class SuspendRequestDto {
        public String reason;
    }

    public static class BlacklistRequestDto {
        public String reason;
    }

    public static class PreferredRequestDto {
        public Boolean preferred;
    }

    public static class PaymentTermsRequestDto {
        public String paymentTerms;
        public Integer paymentDays;
    }

    public static class BankInfoRequestDto {
        public String bankAccountNumber;
        public String bankRoutingNumber;
        public String bankName;
        public String bankAccountType;
    }

    public static class TagRequestDto {
        public String tag;
    }
}
