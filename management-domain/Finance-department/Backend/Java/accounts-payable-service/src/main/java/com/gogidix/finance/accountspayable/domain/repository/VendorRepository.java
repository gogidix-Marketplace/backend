package com.gogidix.finance.accountspayable.domain.repository;

import com.gogidix.finance.accountspayable.domain.model.Vendor;

import java.util.List;
import java.util.Optional;

/**
 * Vendor Repository Interface (Port)
 * Defines the contract for vendor persistence operations
 */
public interface VendorRepository {

    Vendor save(Vendor vendor);

    List<Vendor> saveAll(List<Vendor> vendors);

    Optional<Vendor> findById(String id);

    Optional<Vendor> findByVendorIdAndTenantId(String vendorId, String tenantId);

    Optional<Vendor> findByVendorCodeAndTenantId(String vendorCode, String tenantId);

    List<Vendor> findByTenantId(String tenantId);

    List<Vendor> findByTenantIdAndStatus(String tenantId, Vendor.VendorStatus status);

    List<Vendor> findByTenantIdAndVendorType(String tenantId, Vendor.VendorType vendorType);

    List<Vendor> findByTenantIdAndIsPreferredVendorTrue(String tenantId);

    List<Vendor> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Vendor> searchByName(String tenantId, String searchTerm);

    List<Vendor> findByTenantIdAndEmailContaining(String tenantId, String email);

    List<Vendor> findByTenantIdAndTaxId(String tenantId, String taxId);

    boolean existsByVendorCodeAndTenantId(String vendorCode, String tenantId);

    boolean existsByVendorIdAndTenantId(String vendorId, String tenantId);

    void deleteById(String id);

    void deleteByVendorIdAndTenantId(String vendorId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Vendor.VendorStatus status);
}
