package com.gogidix.finance.accountspayable.application.service;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
import com.gogidix.finance.accountspayable.domain.port.in.VendorCommand;
import com.gogidix.finance.accountspayable.domain.port.out.EventPublisher;
import com.gogidix.finance.accountspayable.domain.repository.VendorRepository;
import com.gogidix.finance.accountspayable.shared.exception.ConflictException;
import com.gogidix.finance.accountspayable.shared.exception.NotFoundException;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Vendor Command Service
 * Handles all write operations for vendors
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VendorCommandService {

    private final VendorRepository vendorRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Vendor create(VendorCommand.CreateVendorCommand command) {
        log.info("Creating vendor for tenant: {}, code: {}",
            command.getTenantId(), command.getVendorCode());

        // Check if vendor code already exists
        if (vendorRepository.existsByVendorCodeAndTenantId(
                command.getVendorCode(), command.getTenantId())) {
            throw new ConflictException("Vendor", command.getVendorCode());
        }

        // Check if tax ID already exists for another vendor
        if (command.getTaxId() != null) {
            List<Vendor> existingWithTaxId = vendorRepository.findByTenantIdAndTaxId(
                command.getTenantId(), command.getTaxId());
            if (!existingWithTaxId.isEmpty()) {
                throw new ConflictException("Vendor with tax ID " + command.getTaxId() + " already exists");
            }
        }

        Vendor vendor = Vendor.create(
            command.getTenantId(),
            command.getVendorCode(),
            command.getVendorName(),
            command.getVendorType(),
            command.getTaxId(),
            command.getCurrency(),
            command.getPaymentDays(),
            command.getContactPerson(),
            command.getEmail(),
            command.getBillingAddress(),
            command.getCreatedBy()
        );

        // Set additional fields
        vendor.setShippingAddress(command.getShippingAddress());
        vendor.setPhone(command.getPhone());
        vendor.setWebsite(command.getWebsite());
        vendor.setPaymentTerms(command.getPaymentTerms());
        vendor.setBankAccountNumber(command.getBankAccountNumber());
        vendor.setBankRoutingNumber(command.getBankRoutingNumber());
        vendor.setBankName(command.getBankName());
        vendor.setBankAccountType(command.getBankAccountType());
        vendor.setCreditLimit(command.getCreditLimit());
        vendor.setNotes(command.getNotes());
        vendor.setTags(command.getTags());
        vendor.setParentVendorId(command.getParentVendorId());
        vendor.setDiscountPercentage(command.getDiscountPercentage());
        vendor.setValidFrom(command.getValidFrom());
        vendor.setValidUntil(command.getValidUntil());

        Vendor savedVendor = vendorRepository.save(vendor);
        publishVendorEvents(savedVendor);

        log.info("Created vendor: {} for tenant: {}", savedVendor.getVendorId(), command.getTenantId());
        return savedVendor;
    }

    @Transactional
    public Vendor update(VendorCommand.UpdateVendorCommand command) {
        log.info("Updating vendor: {} for tenant: {}", command.getVendorId(), command.getTenantId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.update(
            command.getVendorName(),
            command.getContactPerson(),
            command.getEmail(),
            command.getPhone(),
            command.getBillingAddress()
        );

        if (command.getShippingAddress() != null) {
            vendor.setShippingAddress(command.getShippingAddress());
        }
        if (command.getNotes() != null) {
            vendor.setNotes(command.getNotes());
        }
        if (command.getTags() != null) {
            vendor.setTags(command.getTags());
        }
        if (command.getWebsite() != null) {
            vendor.setWebsite(command.getWebsite());
        }

        Vendor savedVendor = vendorRepository.save(vendor);
        publishVendorEvents(savedVendor);

        return savedVendor;
    }

    @Transactional
    public void activate(VendorCommand.ActivateVendorCommand command) {
        log.info("Activating vendor: {} for tenant: {}", command.getVendorId(), command.getTenantId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.activate();
        vendorRepository.save(vendor);
        publishVendorEvents(vendor);

        log.info("Activated vendor: {}", command.getVendorId());
    }

    @Transactional
    public void deactivate(VendorCommand.DeactivateVendorCommand command) {
        log.info("Deactivating vendor: {} for tenant: {}", command.getVendorId(), command.getTenantId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.deactivate(command.getReason());
        vendorRepository.save(vendor);
        publishVendorEvents(vendor);

        log.info("Deactivated vendor: {}", command.getVendorId());
    }

    @Transactional
    public void suspend(VendorCommand.SuspendVendorCommand command) {
        log.info("Suspending vendor: {} for tenant: {}", command.getVendorId(), command.getTenantId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.suspend(command.getReason());
        vendorRepository.save(vendor);
        publishVendorEvents(vendor);

        log.info("Suspended vendor: {}", command.getVendorId());
    }

    @Transactional
    public void blacklist(VendorCommand.BlacklistVendorCommand command) {
        log.info("Blacklisting vendor: {} for tenant: {}", command.getVendorId(), command.getTenantId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.blacklist(command.getReason());
        vendorRepository.save(vendor);
        publishVendorEvents(vendor);

        log.info("Blacklisted vendor: {}", command.getVendorId());
    }

    @Transactional
    public void setPreferred(VendorCommand.SetPreferredVendorCommand command) {
        log.info("Setting preferred: {} for vendor: {}", command.getPreferred(), command.getVendorId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.setPreferred(command.getPreferred());
        vendorRepository.save(vendor);

        log.info("Set preferred status for vendor: {}", command.getVendorId());
    }

    @Transactional
    public void updatePaymentTerms(VendorCommand.UpdatePaymentTermsCommand command) {
        log.info("Updating payment terms for vendor: {}", command.getVendorId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.updatePaymentTerms(command.getPaymentTerms(), command.getPaymentDays());
        vendorRepository.save(vendor);

        log.info("Updated payment terms for vendor: {}", command.getVendorId());
    }

    @Transactional
    public void updateBankInfo(VendorCommand.UpdateBankInfoCommand command) {
        log.info("Updating bank info for vendor: {}", command.getVendorId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.updateBankInfo(
            command.getBankAccountNumber(),
            command.getBankRoutingNumber(),
            command.getBankName(),
            command.getBankAccountType()
        );
        vendorRepository.save(vendor);

        log.info("Updated bank info for vendor: {}", command.getVendorId());
    }

    @Transactional
    public void addTag(VendorCommand.AddTagCommand command) {
        log.info("Adding tag: {} to vendor: {}", command.getTag(), command.getVendorId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.addTag(command.getTag());
        vendorRepository.save(vendor);

        log.info("Added tag to vendor: {}", command.getVendorId());
    }

    @Transactional
    public void removeTag(VendorCommand.RemoveTagCommand command) {
        log.info("Removing tag: {} from vendor: {}", command.getTag(), command.getVendorId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        vendor.removeTag(command.getTag());
        vendorRepository.save(vendor);

        log.info("Removed tag from vendor: {}", command.getVendorId());
    }

    @Transactional
    public void delete(VendorCommand.DeleteVendorCommand command) {
        log.info("Deleting vendor: {} for tenant: {}", command.getVendorId(), command.getTenantId());

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(
            command.getVendorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Vendor", command.getVendorId()));

        if (vendor.isActive()) {
            throw new IllegalStateException("Cannot delete active vendor. Deactivate first.");
        }

        vendorRepository.deleteByVendorIdAndTenantId(command.getVendorId(), command.getTenantId());

        log.info("Deleted vendor: {}", command.getVendorId());
    }

    private void publishVendorEvents(Vendor vendor) {
        if (!vendor.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : vendor.getDomainEvents()) {
                eventPublisher.publishVendorEvent(event);
            }
            vendor.clearDomainEvents();
        }
    }
}
