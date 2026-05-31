package com.gogidix.hr.employeeselfservice.domain.repository;

import com.gogidix.hr.employeeselfservice.domain.model.BankDetails;

import java.util.List;
import java.util.Optional;

/**
 * Bank Details Repository Interface (Port)
 * Defines the contract for bank details persistence operations
 */
public interface BankDetailsRepository {

    BankDetails save(BankDetails bankDetails);

    List<BankDetails> saveAll(List<BankDetails> bankDetailsList);

    Optional<BankDetails> findById(String id);

    Optional<BankDetails> findByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<BankDetails> findAllByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<BankDetails> findByTenantId(String tenantId);

    List<BankDetails> findByTenantIdAndPrimaryAccountTrue(String tenantId);

    List<BankDetails> findByEmployeeIdAndTenantIdAndPrimaryAccountTrue(String employeeId, String tenantId);

    List<BankDetails> findByTenantIdAndVerifiedTrue(String tenantId);

    List<BankDetails> findByTenantIdAndAccountType(String tenantId, BankDetails.AccountType accountType);

    List<BankDetails> findByTenantIdAndBankName(String tenantId, String bankName);

    List<BankDetails> findByTenantIdAndCurrency(String tenantId, String currency);

    List<BankDetails> findByTenantIdAndActiveTrue(String tenantId);

    List<BankDetails> findByEmployeeIdAndTenantIdAndActiveTrue(String employeeId, String tenantId);

    List<BankDetails> findByTenantIdAndVerifiedFalse(String tenantId);

    boolean existsByEmployeeIdAndTenantIdAndAccountNumber(String employeeId, String tenantId, String accountNumber);

    boolean existsByEmployeeIdAndTenantIdAndPrimaryAccountTrue(String employeeId, String tenantId);

    void deleteById(String id);

    void deleteByEmployeeIdAndTenantId(String employeeId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByEmployeeIdAndTenantId(String employeeId, String tenantId);

    long countByTenantIdAndVerifiedTrue(String tenantId);

    void removePrimaryFlagFromEmployeeAccounts(String employeeId, String tenantId);
}
