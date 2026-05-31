package com.gogidix.finance.bankreconciliation.application.service;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import com.gogidix.finance.bankreconciliation.domain.port.in.BankAccountCommand;
import com.gogidix.finance.bankreconciliation.domain.repository.BankAccountRepository;
import com.gogidix.finance.bankreconciliation.shared.exception.ConflictException;
import com.gogidix.finance.bankreconciliation.shared.exception.NotFoundException;
import com.gogidix.finance.bankreconciliation.shared.exception.ValidationException;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Bank Account Command Service
 * Handles all write operations for bank accounts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountCommandService {

    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public BankAccount create(BankAccountCommand.CreateBankAccountCommand command) {
        log.info("Creating bank account for tenant: {}, accountNumber: {}",
                command.getTenantId(), command.getAccountNumber());

        // Validate account number uniqueness
        if (bankAccountRepository.existsByAccountNumberAndTenantId(
                command.getAccountNumber(), command.getTenantId())) {
            throw new ConflictException("BankAccount", command.getAccountNumber());
        }

        BankAccount account = BankAccount.create(
                command.getTenantId(),
                command.getAccountNumber(),
                command.getAccountName(),
                command.getAccountType(),
                command.getBankName(),
                command.getCurrency()
        );

        // Set optional fields
        account.setBankCode(command.getBankCode());
        account.setIban(command.getIban());
        account.setSwiftCode(command.getSwiftCode());
        account.setRoutingNumber(command.getRoutingNumber());
        account.setDescription(command.getDescription());
        account.setTags(command.getTags() != null ? command.getTags() : List.of());
        account.setStatementFrequency(command.getStatementFrequency());
        account.setReconciliationTolerance(command.getReconciliationTolerance() != null
                ? command.getReconciliationTolerance() : new BigDecimal("0.01"));
        account.setAutoReconcile(command.getAutoReconcile() != null ? command.getAutoReconcile() : false);

        // Set initial balance if provided
        if (command.getOpeningBalance() != null && command.getBalanceDate() != null) {
            account.setBalance(command.getOpeningBalance());
            account.setBalanceDate(command.getBalanceDate());
            account.setOpeningBalance(command.getOpeningBalance());
        }

        // Set as primary if requested
        if (Boolean.TRUE.equals(command.getIsPrimary())) {
            // Unset existing primary
            bankAccountRepository.findByTenantIdAndIsPrimary(command.getTenantId(), true)
                    .forEach(existing -> {
                        existing.setIsPrimary(false);
                        bankAccountRepository.save(existing);
                    });
            account.setIsPrimary(true);
        }

        BankAccount savedAccount = bankAccountRepository.save(account);
        log.info("Created bank account: {} for tenant: {}", savedAccount.getId(), command.getTenantId());
        return savedAccount;
    }

    @Transactional
    public BankAccount update(BankAccountCommand.UpdateBankAccountCommand command) {
        log.info("Updating bank account: {} for tenant: {}", command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        if (command.getAccountName() != null) {
            account.setAccountName(command.getAccountName());
        }
        if (command.getDescription() != null) {
            account.setDescription(command.getDescription());
        }
        if (command.getBalance() != null && command.getBalanceDate() != null) {
            account.updateBalance(command.getBalance(), command.getBalanceDate());
        }
        if (command.getIban() != null) {
            account.setIban(command.getIban());
        }
        if (command.getSwiftCode() != null) {
            account.setSwiftCode(command.getSwiftCode());
        }
        if (command.getRoutingNumber() != null) {
            account.setRoutingNumber(command.getRoutingNumber());
        }
        if (command.getTags() != null) {
            account.setTags(command.getTags());
        }
        if (command.getReconciliationTolerance() != null) {
            account.setReconciliationTolerance(command.getReconciliationTolerance());
        }
        if (command.getStatementFrequency() != null) {
            account.setStatementFrequency(command.getStatementFrequency());
        }
        if (command.getAutoReconcile() != null) {
            account.setAutoReconcile(command.getAutoReconcile());
        }

        BankAccount savedAccount = bankAccountRepository.save(account);
        log.info("Updated bank account: {}", command.getAccountId());
        return savedAccount;
    }

    @Transactional
    public void updateBalance(BankAccountCommand.UpdateBalanceCommand command) {
        log.info("Updating balance for account: {} for tenant: {}",
                command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        account.updateBalance(command.getNewBalance(), command.getBalanceDate());
        bankAccountRepository.save(account);

        log.info("Updated balance for account: {}", command.getAccountId());
    }

    @Transactional
    public void setAsPrimary(BankAccountCommand.SetAsPrimaryCommand command) {
        log.info("Setting account as primary: {} for tenant: {}",
                command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        // Unset existing primary accounts
        bankAccountRepository.findByTenantIdAndIsPrimary(command.getTenantId(), true)
                .forEach(existing -> {
                    if (!existing.getId().equals(command.getAccountId())) {
                        existing.setIsPrimary(false);
                        bankAccountRepository.save(existing);
                    }
                });

        account.setAsPrimary();
        bankAccountRepository.save(account);

        log.info("Set account as primary: {}", command.getAccountId());
    }

    @Transactional
    public void activate(BankAccountCommand.ActivateAccountCommand command) {
        log.info("Activating account: {} for tenant: {}", command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        account.activate();
        bankAccountRepository.save(account);

        log.info("Activated account: {}", command.getAccountId());
    }

    @Transactional
    public void deactivate(BankAccountCommand.DeactivateAccountCommand command) {
        log.info("Deactivating account: {} for tenant: {}", command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        account.deactivate();
        bankAccountRepository.save(account);

        log.info("Deactivated account: {}", command.getAccountId());
    }

    @Transactional
    public void close(BankAccountCommand.CloseAccountCommand command) {
        log.info("Closing account: {} for tenant: {}", command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        account.close();
        bankAccountRepository.save(account);

        log.info("Closed account: {}", command.getAccountId());
    }

    @Transactional
    public void markAsReconciled(BankAccountCommand.MarkAsReconciledCommand command) {
        log.info("Marking account as reconciled: {} for tenant: {}",
                command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        account.markAsReconciled(command.getStatementDate());
        bankAccountRepository.save(account);

        log.info("Marked account as reconciled: {}", command.getAccountId());
    }

    @Transactional
    public void delete(BankAccountCommand.DeleteBankAccountCommand command) {
        log.info("Deleting bank account: {} for tenant: {}", command.getAccountId(), command.getTenantId());

        BankAccount account = bankAccountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new NotFoundException("BankAccount", command.getAccountId()));

        if (!account.getTenantId().equals(command.getTenantId())) {
            throw new ValidationException("Tenant ID mismatch");
        }

        if (account.getStatus() != BankAccount.AccountStatus.INACTIVE) {
            throw new ValidationException("Can only delete inactive accounts");
        }

        bankAccountRepository.deleteById(command.getAccountId());

        log.info("Deleted bank account: {}", command.getAccountId());
    }
}
