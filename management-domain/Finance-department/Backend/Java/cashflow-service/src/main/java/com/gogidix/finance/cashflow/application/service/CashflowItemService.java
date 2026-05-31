package com.gogidix.finance.cashflow.application.service;

import com.gogidix.finance.cashflow.domain.event.CashflowItemRecordedEvent;
import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.port.in.CashflowItemCommand;
import com.gogidix.finance.cashflow.domain.port.out.EventPublisher;
import com.gogidix.finance.cashflow.domain.repository.CashflowItemRepository;
import com.gogidix.finance.cashflow.shared.exception.ConflictException;
import com.gogidix.finance.cashflow.shared.exception.NotFoundException;
import com.gogidix.finance.cashflow.shared.exception.ValidationException;
import com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cashflow Item Command Service
 * Handles all write operations for cashflow items
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CashflowItemService {

    private final CashflowItemRepository cashflowItemRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public CashflowItem create(CashflowItemCommand.CreateCashflowItemCommand command) {
        log.info("Creating cashflow item for tenant: {}, type: {}",
                command.getTenantId(), command.getType());

        validateCashflowItemAmount(command.getAmount());

        CashflowItem item = CashflowItem.create(
                command.getTenantId(),
                command.getRecordedBy(),
                command.getType(),
                command.getCategory(),
                command.getAmount(),
                command.getCurrency(),
                command.getTransactionDate(),
                command.getDescription()
        );

        // Set additional fields
        item.setReference(command.getReference() != null ? command.getReference() : generateReference());
        item.setExpectedDate(command.getExpectedDate() != null ? command.getExpectedDate() : command.getTransactionDate());
        item.setCounterparty(command.getCounterparty());
        item.setAccount(command.getAccount());
        item.setCostCenter(command.getCostCenter());
        item.setProjectId(command.getProjectId());
        item.setPaymentMethod(command.getPaymentMethod());
        item.setTags(command.getTags() != null ? command.getTags() : new ArrayList<>());
        item.setNotes(command.getNotes());
        item.setLinkedExpenseId(command.getLinkedExpenseId());
        item.setLinkedRevenueId(command.getLinkedRevenueId());

        // Handle recurring setup
        if (command.getRecurring() != null && command.getRecurring()) {
            item.setupRecurring(command.getRecurringFrequency());
        }

        // Calculate tax if provided
        if (command.getTaxAmount() != null) {
            item.calculateNetAmount(command.getTaxAmount());
        }

        CashflowItem savedItem = cashflowItemRepository.save(item);
        publishEvents(savedItem);

        log.info("Created cashflow item: {} for tenant: {}", savedItem.getCashflowItemId(), command.getTenantId());
        return savedItem;
    }

    @Transactional
    public List<CashflowItem> bulkCreate(CashflowItemCommand.BulkCreateCommand command) {
        log.info("Bulk creating {} cashflow items for tenant: {}",
                command.getItems().size(), command.getTenantId());

        List<CashflowItem> items = new ArrayList<>();
        for (CashflowItemCommand.CreateCashflowItemCommand itemCommand : command.getItems()) {
            itemCommand.setTenantId(command.getTenantId());
            itemCommand.setRecordedBy(command.getRecordedBy());
            items.add(create(itemCommand));
        }

        return items;
    }

    @Transactional
    public CashflowItem update(CashflowItemCommand.UpdateCashflowItemCommand command) {
        log.info("Updating cashflow item: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        if (item.getStatus() != CashflowItem.ItemStatus.PENDING) {
            throw new ValidationException("Can only update pending cashflow items");
        }

        if (command.getDescription() != null) {
            item.setDescription(command.getDescription());
        }
        if (command.getAmount() != null) {
            validateCashflowItemAmount(command.getAmount());
            item.setAmount(command.getAmount());
        }
        if (command.getExpectedDate() != null) {
            item.setExpectedDate(command.getExpectedDate());
        }
        if (command.getTransactionDate() != null) {
            item.setTransactionDate(command.getTransactionDate());
        }
        if (command.getCounterparty() != null) {
            item.setCounterparty(command.getCounterparty());
        }
        if (command.getAccount() != null) {
            item.setAccount(command.getAccount());
        }
        if (command.getCostCenter() != null) {
            item.setCostCenter(command.getCostCenter());
        }
        if (command.getTags() != null) {
            item.setTags(command.getTags());
        }
        if (command.getNotes() != null) {
            item.setNotes(command.getNotes());
        }
        if (command.getTaxAmount() != null) {
            item.calculateNetAmount(command.getTaxAmount());
        }

        CashflowItem savedItem = cashflowItemRepository.save(item);
        publishEvents(savedItem);

        return savedItem;
    }

    @Transactional
    public void markAsExpected(CashflowItemCommand.MarkAsExpectedCommand command) {
        log.info("Marking cashflow item as expected: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        item.markAsExpected();
        cashflowItemRepository.save(item);
        publishEvents(item);

        log.info("Marked cashflow item as expected: {}", command.getCashflowItemId());
    }

    @Transactional
    public void commit(CashflowItemCommand.CommitCashflowItemCommand command) {
        log.info("Committing cashflow item: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        item.commit();
        cashflowItemRepository.save(item);
        publishEvents(item);

        log.info("Committed cashflow item: {}", command.getCashflowItemId());
    }

    @Transactional
    public void settle(CashflowItemCommand.SettleCashflowItemCommand command) {
        log.info("Settling cashflow item: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        item.settle(command.getBankReference());
        cashflowItemRepository.save(item);
        publishEvents(item);

        log.info("Settled cashflow item: {}", command.getCashflowItemId());
    }

    @Transactional
    public void cancel(CashflowItemCommand.CancelCashflowItemCommand command) {
        log.info("Cancelling cashflow item: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        item.cancel(command.getReason());
        cashflowItemRepository.save(item);
        publishEvents(item);

        log.info("Cancelled cashflow item: {}", command.getCashflowItemId());
    }

    @Transactional
    public void markAsFailed(CashflowItemCommand.MarkAsFailedCommand command) {
        log.info("Marking cashflow item as failed: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        item.markAsFailed(command.getReason());
        cashflowItemRepository.save(item);
        publishEvents(item);

        log.info("Marked cashflow item as failed: {}", command.getCashflowItemId());
    }

    @Transactional
    public void setupRecurring(CashflowItemCommand.SetupRecurringCommand command) {
        log.info("Setting up recurring for cashflow item: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        item.setupRecurring(command.getFrequency());
        cashflowItemRepository.save(item);

        log.info("Set up recurring for cashflow item: {}", command.getCashflowItemId());
    }

    @Transactional
    public void delete(CashflowItemCommand.DeleteCashflowItemCommand command) {
        log.info("Deleting cashflow item: {} for tenant: {}",
                command.getCashflowItemId(), command.getTenantId());

        CashflowItem item = cashflowItemRepository.findByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowItem", command.getCashflowItemId()));

        if (item.getStatus() != CashflowItem.ItemStatus.PENDING) {
            throw new ValidationException("Can only delete pending cashflow items");
        }

        cashflowItemRepository.deleteByCashflowItemIdAndTenantId(
                command.getCashflowItemId(), command.getTenantId());

        log.info("Deleted cashflow item: {}", command.getCashflowItemId());
    }

    private void validateCashflowItemAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount", "Amount must be positive");
        }
        if (amount.compareTo(new BigDecimal("1000000000")) > 0) {
            throw new ValidationException("amount", "Amount exceeds maximum limit");
        }
    }

    private String generateReference() {
        return "CFI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void publishEvents(CashflowItem item) {
        if (!item.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(item.getDomainEvents().stream().map(e -> (Object) e).toList());
            item.clearDomainEvents();
        }
    }
}
