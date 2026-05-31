package com.gogidix.finance.tax.application.service;

import com.gogidix.finance.tax.domain.event.TaxCalculationCompletedEvent;
import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.port.in.TaxCalculationCommand;
import com.gogidix.finance.tax.domain.port.out.EventPublisher;
import com.gogidix.finance.tax.domain.repository.TaxCalculationRepository;
import com.gogidix.finance.tax.domain.repository.TaxRateRepository;
import com.gogidix.finance.tax.shared.exception.NotFoundException;
import com.gogidix.finance.tax.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tax Calculation Service
 * Handles all tax calculation operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaxCalculationService {

    private final TaxCalculationRepository taxCalculationRepository;
    private final TaxRateRepository taxRateRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public TaxCalculation createCalculation(TaxCalculationCommand.CreateCalculationCommand command) {
        log.info("Creating tax calculation for tenant: {}, transaction: {}",
            command.getTenantId(), command.getTransactionId());

        TaxCalculation calculation = TaxCalculation.create(
            command.getTenantId(),
            command.getTransactionId(),
            command.getTransactionType(),
            command.getTransactionDate(),
            command.getJurisdiction(),
            command.getCurrency(),
            command.getBaseAmount(),
            command.getCalculatedBy()
        );

        if (command.getContext() != null) {
            command.getContext().forEach(calculation::addContext);
        }

        TaxCalculation savedCalculation = taxCalculationRepository.save(calculation);
        publishEvents(savedCalculation);

        log.info("Created tax calculation: {} for tenant: {}", savedCalculation.getCalculationId(), command.getTenantId());
        return savedCalculation;
    }

    @Transactional
    public TaxCalculation addTaxRate(TaxCalculationCommand.AddTaxRateCommand command) {
        log.info("Adding tax rate to calculation: {} for tenant: {}",
            command.getCalculationId(), command.getTenantId());

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        Boolean isRecoverable = command.getIsRecoverable() != null ? command.getIsRecoverable() : false;

        if (Boolean.TRUE.equals(command.getIsCompound())) {
            calculation.calculateCompoundTax(
                command.getTaxCode(),
                command.getTaxType(),
                command.getRate(),
                isRecoverable,
                command.getDescription()
            );
        } else {
            calculation.calculateWithRate(
                command.getTaxCode(),
                command.getTaxType(),
                command.getRate(),
                isRecoverable,
                command.getDescription()
            );
        }

        TaxCalculation savedCalculation = taxCalculationRepository.save(calculation);
        publishEvents(savedCalculation);

        log.info("Added tax rate to calculation: {}", command.getCalculationId());
        return savedCalculation;
    }

    @Transactional
    public TaxCalculation addExemption(TaxCalculationCommand.AddExemptionCommand command) {
        log.info("Adding exemption to calculation: {} for tenant: {}",
            command.getCalculationId(), command.getTenantId());

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        calculation.addExemption(
            command.getExemptionCode(),
            command.getExemptionType(),
            command.getAmount(),
            command.getReason(),
            command.getCertificateNumber(),
            command.getCertificateExpiry()
        );

        TaxCalculation savedCalculation = taxCalculationRepository.save(calculation);

        log.info("Added exemption to calculation: {}", command.getCalculationId());
        return savedCalculation;
    }

    @Transactional
    public TaxCalculation addDeduction(TaxCalculationCommand.AddDeductionCommand command) {
        log.info("Adding deduction to calculation: {} for tenant: {}",
            command.getCalculationId(), command.getTenantId());

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        calculation.addDeduction(
            command.getDeductionType(),
            command.getAmount(),
            command.getDescription(),
            command.getReference()
        );

        TaxCalculation savedCalculation = taxCalculationRepository.save(calculation);

        log.info("Added deduction to calculation: {}", command.getCalculationId());
        return savedCalculation;
    }

    @Transactional
    public TaxCalculation finalizeCalculation(TaxCalculationCommand.FinalizeCalculationCommand command) {
        log.info("Finalizing calculation: {} for tenant: {}",
            command.getCalculationId(), command.getTenantId());

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        calculation.finalize();
        TaxCalculation savedCalculation = taxCalculationRepository.save(calculation);
        publishEvents(savedCalculation);

        log.info("Finalized calculation: {}", command.getCalculationId());
        return savedCalculation;
    }

    @Transactional
    public TaxCalculation verifyCalculation(TaxCalculationCommand.VerifyCalculationCommand command) {
        log.info("Verifying calculation: {} for tenant: {}",
            command.getCalculationId(), command.getTenantId());

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        calculation.verify(command.getVerifiedBy());
        TaxCalculation savedCalculation = taxCalculationRepository.save(calculation);
        publishEvents(savedCalculation);

        log.info("Verified calculation: {}", command.getCalculationId());
        return savedCalculation;
    }

    @Transactional
    public void markAsApplied(TaxCalculationCommand.MarkAsAppliedCommand command) {
        log.info("Marking calculation as applied: {} for tenant: {}",
            command.getCalculationId(), command.getTenantId());

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        calculation.markAsApplied();
        taxCalculationRepository.save(calculation);
        publishEvents(calculation);

        log.info("Marked calculation as applied: {}", command.getCalculationId());
    }

    @Transactional
    public void reverseCalculation(TaxCalculationCommand.ReverseCalculationCommand command) {
        log.info("Reversing calculation: {} for tenant: {}",
            command.getCalculationId(), command.getTenantId());

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        calculation.reverse(command.getReason());
        taxCalculationRepository.save(calculation);
        publishEvents(calculation);

        log.info("Reversed calculation: {}", command.getCalculationId());
    }

    @Transactional
    public TaxCalculation calculateTax(TaxCalculationCommand.CalculateTaxCommand command) {
        log.info("Calculating tax for transaction: {} for tenant: {}",
            command.getTransactionId(), command.getTenantId());

        // Find applicable tax rate
        LocalDate taxDate = command.getTransactionDate() != null ? command.getTransactionDate() : LocalDate.now();
        Optional<TaxRate> taxRateOpt = taxRateRepository.findEffectiveRateForDate(
            command.getTenantId(),
            command.getJurisdiction(),
            command.getTaxType(),
            null,
            taxDate
        );

        if (taxRateOpt.isEmpty()) {
            throw new ValidationException("No applicable tax rate found for the given criteria");
        }

        TaxRate taxRate = taxRateOpt.get();

        // Create calculation
        TaxCalculation calculation = TaxCalculation.create(
            command.getTenantId(),
            command.getTransactionId(),
            command.getTransactionType(),
            command.getTransactionDate(),
            command.getJurisdiction(),
            command.getCurrency(),
            command.getAmount(),
            command.getCalculatedBy()
        );

        // Add context
        if (command.getCategory() != null) {
            calculation.addContext("category", command.getCategory());
        }
        if (command.getEntityCode() != null) {
            calculation.addContext("entityCode", command.getEntityCode());
        }
        if (command.getAdditionalContext() != null) {
            command.getAdditionalContext().forEach(calculation::addContext);
        }

        // Calculate tax
        calculation.calculateWithRate(
            taxRate.getTaxCode(),
            taxRate.getTaxType(),
            taxRate.getRatePercentage(),
            taxRate.getIsRecoverable(),
            taxRate.getDescription()
        );

        // Finalize
        calculation.finalize();

        TaxCalculation savedCalculation = taxCalculationRepository.save(calculation);
        publishEvents(savedCalculation);

        log.info("Calculated tax for transaction: {}, total tax: {}",
            command.getTransactionId(), savedCalculation.getTotalTax());

        return savedCalculation;
    }

    @Transactional
    public List<TaxCalculation> batchCalculate(TaxCalculationCommand.BatchCalculateCommand command) {
        log.info("Batch calculating tax for {} transactions for tenant: {}",
            command.getTransactions().size(), command.getTenantId());

        List<TaxCalculation> results = new ArrayList<>();

        for (TaxCalculationCommand.BatchCalculateCommand.TransactionForCalculation tx : command.getTransactions()) {
            try {
                TaxCalculationCommand.CalculateTaxCommand calcCommand = new TaxCalculationCommand.CalculateTaxCommand(
                    command.getTenantId(),
                    tx.getTransactionId(),
                    tx.getTransactionType(),
                    tx.getTransactionDate(),
                    tx.getJurisdiction(),
                    tx.getTaxType(),
                    tx.getCurrency(),
                    tx.getAmount(),
                    command.getCalculatedBy(),
                    tx.getCategory(),
                    null,
                    tx.getContext()
                );

                TaxCalculation result = calculateTax(calcCommand);
                results.add(result);
            } catch (Exception e) {
                log.error("Failed to calculate tax for transaction: {}", tx.getTransactionId(), e);
                // Continue with next transaction
            }
        }

        log.info("Batch calculated tax for {} transactions", results.size());
        return results;
    }

    private void publishEvents(TaxCalculation calculation) {
        if (!calculation.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(new ArrayList<>(calculation.getDomainEvents()));
            calculation.clearDomainEvents();
        }
    }
}
