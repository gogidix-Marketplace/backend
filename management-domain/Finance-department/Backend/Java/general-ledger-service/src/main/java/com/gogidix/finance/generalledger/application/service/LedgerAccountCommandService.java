package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.domain.repository.LedgerAccountRepository;
import com.gogidix.finance.generalledger.shared.exception.ConflictException;
import com.gogidix.finance.generalledger.shared.exception.NotFoundException;
import com.gogidix.finance.generalledger.shared.exception.ValidationException;
import com.gogidix.finance.ledger.domain.event.LedgerAccountEvent;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.port.in.LedgerAccountCommand;
import com.gogidix.finance.ledger.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Ledger Account Command Service
 * Handles all write operations for ledger accounts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerAccountCommandService {

    private final LedgerAccountRepository ledgerAccountRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    @CacheEvict(value = "ledgerAccounts", allEntries = true)
    public LedgerAccount create(LedgerAccountCommand.CreateAccountCommand command) {
        log.info("Creating ledger account: {} for tenant: {}",
            command.getAccountNumber(), command.getTenantId());

        // Validate account number doesn't exist
        if (ledgerAccountRepository.existsByAccountNumberAndTenantId(
                command.getAccountNumber(), command.getTenantId())) {
            throw new ConflictException("LedgerAccount", command.getAccountNumber());
        }

        LedgerAccount account = LedgerAccount.create(
            command.getTenantId(),
            command.getAccountNumber(),
            command.getAccountName(),
            command.getAccountType(),
            command.getAccountSubType(),
            command.getCurrency(),
            command.getCreatedBy()
        );

        account.setDescription(command.getDescription());
        account.setCostCenter(command.getCostCenter());
        account.setDepartment(command.getDepartment());
        account.setLocation(command.getLocation());

        if (command.getParentAccountId() != null) {
            account.setParentAccountId(command.getParentAccountId());
        }
        if (command.getAccountLevel() != null) {
            account.setAccountLevel(command.getAccountLevel());
        }

        account.setIsCashAccount(command.getIsCashAccount() != null ? command.getIsCashAccount() : false);
        account.setIsReconcilable(command.getIsReconcilable() != null ? command.getIsReconcilable() : false);
        account.setIsTaxAccount(command.getIsTaxAccount() != null ? command.getIsTaxAccount() : false);
        account.setTaxCode(command.getTaxCode());
        account.setAllowsManualEntry(command.getAllowsManualEntry() != null ? command.getAllowsManualEntry() : true);
        account.setCreditLimit(command.getCreditLimit());
        account.setNotes(command.getNotes());

        if (command.getTags() != null && !command.getTags().isEmpty()) {
            account.setTags(command.getTags());
        }

        // Set opening balance if provided
        if (command.getOpeningBalance() != null && command.getOpeningBalanceDate() != null) {
            account.setOpeningBalance(command.getOpeningBalance(), command.getOpeningBalanceDate());
        }

        LedgerAccount savedAccount = ledgerAccountRepository.save(account);
        publishAccountEvents(savedAccount, "AccountCreated");

        log.info("Created ledger account: {} for tenant: {}",
            savedAccount.getAccountId(), command.getTenantId());
        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount update(LedgerAccountCommand.UpdateAccountCommand command) {
        log.info("Updating ledger account: {} for tenant: {}",
            command.getAccountId(), command.getTenantId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        if (account.getStatus() == LedgerAccount.AccountStatus.ARCHIVED) {
            throw new ValidationException("Cannot update archived accounts");
        }

        if (command.getAccountName() != null) {
            account.setAccountName(command.getAccountName());
        }
        if (command.getDescription() != null) {
            account.setDescription(command.getDescription());
        }
        if (command.getCostCenter() != null) {
            account.setCostCenter(command.getCostCenter());
        }
        if (command.getDepartment() != null) {
            account.setDepartment(command.getDepartment());
        }
        if (command.getLocation() != null) {
            account.setLocation(command.getLocation());
        }

        if (command.getIsCashAccount() != null) {
            account.setIsCashAccount(command.getIsCashAccount());
        }
        if (command.getIsReconcilable() != null) {
            account.setIsReconcilable(command.getIsReconcilable());
        }
        if (command.getTaxCode() != null) {
            account.setTaxCode(command.getTaxCode());
        }
        if (command.getAllowsManualEntry() != null) {
            account.setAllowsManualEntry(command.getAllowsManualEntry());
        }
        if (command.getCreditLimit() != null) {
            account.setCreditLimit(command.getCreditLimit());
        }
        if (command.getNotes() != null) {
            account.setNotes(command.getNotes());
        }
        if (command.getTags() != null) {
            account.setTags(command.getTags());
        }

        LedgerAccount savedAccount = ledgerAccountRepository.save(account);
        publishAccountEvents(savedAccount, "AccountUpdated");

        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount activate(LedgerAccountCommand.ActivateAccountCommand command) {
        log.info("Activating ledger account: {}", command.getAccountId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        account.activate();
        LedgerAccount savedAccount = ledgerAccountRepository.save(account);
        publishAccountEvents(savedAccount, "AccountActivated");

        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount freeze(LedgerAccountCommand.FreezeAccountCommand command) {
        log.info("Freezing ledger account: {}", command.getAccountId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        account.freeze(command.getReason());
        LedgerAccount savedAccount = ledgerAccountRepository.save(account);
        publishAccountEvents(savedAccount, "AccountFrozen", command.getReason());

        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount unfreeze(LedgerAccountCommand.UnfreezeAccountCommand command) {
        log.info("Unfreezing ledger account: {}", command.getAccountId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        account.unfreeze();
        LedgerAccount savedAccount = ledgerAccountRepository.save(account);
        publishAccountEvents(savedAccount, "AccountUnfrozen");

        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount archive(LedgerAccountCommand.ArchiveAccountCommand command) {
        log.info("Archiving ledger account: {}", command.getAccountId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        account.archive(command.getArchivedBy(), command.getReason());
        LedgerAccount savedAccount = ledgerAccountRepository.save(account);
        publishAccountEvents(savedAccount, "AccountArchived", command.getReason());

        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount setOpeningBalance(LedgerAccountCommand.SetOpeningBalanceCommand command) {
        log.info("Setting opening balance for account: {}", command.getAccountId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        account.setOpeningBalance(command.getOpeningBalance(), command.getAsOfDate());
        LedgerAccount savedAccount = ledgerAccountRepository.save(account);
        publishAccountEvents(savedAccount, "OpeningBalanceSet");

        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount addTag(LedgerAccountCommand.AddTagCommand command) {
        log.info("Adding tag to account: {}", command.getAccountId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        account.addTag(command.getKey(), command.getValue());
        LedgerAccount savedAccount = ledgerAccountRepository.save(account);

        return savedAccount;
    }

    @Transactional
    @CachePut(value = "ledgerAccounts", key = "#command.accountId")
    public LedgerAccount removeTag(LedgerAccountCommand.RemoveTagCommand command) {
        log.info("Removing tag from account: {}", command.getAccountId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        account.removeTag(command.getKey());
        LedgerAccount savedAccount = ledgerAccountRepository.save(account);

        return savedAccount;
    }

    @Transactional
    @CacheEvict(value = "ledgerAccounts", allEntries = true)
    public void delete(LedgerAccountCommand.DeleteAccountCommand command) {
        log.info("Deleting ledger account: {} for tenant: {}",
            command.getAccountId(), command.getTenantId());

        LedgerAccount account = ledgerAccountRepository.findByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("LedgerAccount", command.getAccountId()));

        if (account.getStatus() == LedgerAccount.AccountStatus.ACTIVE) {
            throw new ValidationException("Cannot delete active accounts. Archive them first.");
        }

        if (account.hasNonZeroBalance()) {
            throw new ValidationException("Cannot delete account with non-zero balance");
        }

        ledgerAccountRepository.deleteByAccountIdAndTenantId(
            command.getAccountId(), command.getTenantId());

        log.info("Deleted ledger account: {}", command.getAccountId());
    }

    private void publishAccountEvents(LedgerAccount account, String eventType) {
        publishAccountEvents(account, eventType, null);
    }

    private void publishAccountEvents(LedgerAccount account, String eventType, String reason) {
        if (!eventPublisher.isReady()) {
            return;
        }

        LedgerAccountEvent event = LedgerAccountEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .accountId(account.getAccountId())
            .accountNumber(account.getAccountNumber())
            .tenantId(account.getTenantId())
            .eventType(eventType)
            .accountType(account.getAccountType())
            .accountSubType(account.getAccountSubType())
            .accountName(account.getAccountName())
            .balance(account.getCurrentBalance())
            .currency(account.getCurrency())
            .timestamp(java.time.LocalDateTime.now())
            .reason(reason)
            .build();

        eventPublisher.publishLedgerAccountEvent(event);
    }
}
