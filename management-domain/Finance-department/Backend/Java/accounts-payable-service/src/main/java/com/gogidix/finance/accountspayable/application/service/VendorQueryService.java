package com.gogidix.finance.accountspayable.application.service;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
import com.gogidix.finance.accountspayable.domain.repository.VendorRepository;
import com.gogidix.finance.accountspayable.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Vendor Query Service
 * Handles all read operations for vendors
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VendorQueryService {

    private final VendorRepository vendorRepository;

    public Vendor getById(String vendorId) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching vendor: {} for tenant: {}", vendorId, tenantId);

        return vendorRepository.findByVendorIdAndTenantId(vendorId, tenantId)
            .orElseThrow(() -> new NotFoundException("Vendor", vendorId));
    }

    public Vendor getByCode(String vendorCode) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching vendor by code: {} for tenant: {}", vendorCode, tenantId);

        return vendorRepository.findByVendorCodeAndTenantId(vendorCode, tenantId)
            .orElseThrow(() -> new NotFoundException("Vendor", vendorCode));
    }

    public Page<Vendor> getByType(Vendor.VendorType vendorType, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching vendors by type: {} for tenant: {}", vendorType, tenantId);

        List<Vendor> vendors = vendorRepository.findByTenantIdAndVendorType(tenantId, vendorType);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(vendors, pageRequest, vendors.size());
    }

    public Page<Vendor> getByStatus(Vendor.VendorStatus status, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching vendors by status: {} for tenant: {}", status, tenantId);

        List<Vendor> vendors = vendorRepository.findByTenantIdAndStatus(tenantId, status);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(vendors, pageRequest, vendors.size());
    }

    public Page<Vendor> getPreferredVendors(int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching preferred vendors for tenant: {}", tenantId);

        List<Vendor> vendors = vendorRepository.findByTenantIdAndIsPreferredVendorTrue(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(vendors, pageRequest, vendors.size());
    }

    public Page<Vendor> search(String searchTerm, Vendor.VendorType vendorType,
                               Vendor.VendorStatus status, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Searching vendors for tenant: {} with term: {}", tenantId, searchTerm);

        List<Vendor> vendors = vendorRepository.searchByName(tenantId, searchTerm);

        // Filter by type if specified
        if (vendorType != null) {
            vendors = vendors.stream()
                .filter(v -> v.getVendorType() == vendorType)
                .toList();
        }

        // Filter by status if specified
        if (status != null) {
            vendors = vendors.stream()
                .filter(v -> v.getStatus() == status)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(vendors, pageRequest, vendors.size());
    }

    public Page<Vendor> getByTag(String tag, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching vendors by tag: {} for tenant: {}", tag, tenantId);

        List<Vendor> vendors = vendorRepository.findByTenantIdAndTagsContaining(tenantId, tag);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(vendors, pageRequest, vendors.size());
    }

    public Page<Vendor> getActiveVendors(int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching active vendors for tenant: {}", tenantId);

        List<Vendor> vendors = vendorRepository.findByTenantIdAndStatus(tenantId, Vendor.VendorStatus.ACTIVE);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(vendors, pageRequest, vendors.size());
    }

    public Page<Vendor> getAllVendors(int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching all vendors for tenant: {}", tenantId);

        List<Vendor> vendors = vendorRepository.findByTenantId(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(vendors, pageRequest, vendors.size());
    }

    public List<Vendor> getAllForTenant() {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching all vendors for tenant: {}", tenantId);

        return vendorRepository.findByTenantId(tenantId);
    }

    public long countByTenant() {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return vendorRepository.countByTenantId(tenantId);
    }

    public long countByStatus(Vendor.VendorStatus status) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return vendorRepository.countByTenantIdAndStatus(tenantId, status);
    }
}
