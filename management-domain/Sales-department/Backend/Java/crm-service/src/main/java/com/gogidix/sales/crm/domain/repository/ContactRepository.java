package com.gogidix.sales.crm.domain.repository;

import com.gogidix.sales.crm.domain.model.Contact;

import java.util.List;
import java.util.Optional;

/**
 * Contact Repository Interface (Port)
 * Defines the contract for contact persistence operations
 */
public interface ContactRepository {

    Contact save(Contact contact);

    List<Contact> saveAll(List<Contact> contacts);

    Optional<Contact> findById(String id);

    Optional<Contact> findByContactIdAndTenantId(String contactId, String tenantId);

    List<Contact> findByTenantId(String tenantId);

    List<Contact> findByCustomerIdAndTenantId(String customerId, String tenantId);

    List<Contact> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Contact> findByTenantIdAndIsActive(String tenantId, boolean isActive);

    List<Contact> findByTenantIdAndIsPrimary(String tenantId, boolean isPrimary);

    List<Contact> findByTenantIdAndIsDecisionMaker(String tenantId, boolean isDecisionMaker);

    List<Contact> findByTenantIdAndContactType(String tenantId, Contact.ContactType contactType);

    List<Contact> findByTenantIdAndEmailContainingIgnoreCase(String tenantId, String email);

    List<Contact> findByTenantIdAndFullNameContainingIgnoreCase(String tenantId, String name);

    List<Contact> findByTenantIdAndTitleContainingIgnoreCase(String tenantId, String title);

    List<Contact> findByTenantIdAndDepartment(String tenantId, String department);

    List<Contact> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Contact> findByCustomerIdAndTenantIdAndIsPrimary(String customerId, String tenantId, boolean isPrimary);

    boolean existsByContactIdAndTenantId(String contactId, String tenantId);

    void deleteById(String id);

    void deleteByContactIdAndTenantId(String contactId, String tenantId);

    void deleteAllByCustomerIdAndTenantId(String customerId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByCustomerIdAndTenantId(String customerId, String tenantId);

    long countByTenantIdAndIsActive(String tenantId, boolean isActive);
}
