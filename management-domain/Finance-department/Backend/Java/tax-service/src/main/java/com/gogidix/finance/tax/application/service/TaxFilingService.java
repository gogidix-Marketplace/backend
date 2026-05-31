package com.gogidix.finance.tax.application.service;

import com.gogidix.finance.tax.domain.event.TaxFilingSubmittedEvent;
import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.port.in.TaxFilingCommand;
import com.gogidix.finance.tax.domain.port.out.EventPublisher;
import com.gogidix.finance.tax.domain.repository.TaxCalculationRepository;
import com.gogidix.finance.tax.domain.repository.TaxFilingRepository;
import com.gogidix.finance.tax.shared.exception.NotFoundException;
import com.gogidix.finance.tax.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Tax Filing Service
 * Handles all tax filing operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaxFilingService {

    private final TaxFilingRepository taxFilingRepository;
    private final TaxCalculationRepository taxCalculationRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public TaxFiling createFiling(TaxFilingCommand.CreateFilingCommand command) {
        log.info("Creating tax filing for tenant: {}, period: {}",
            command.getTenantId(), command.getFilingPeriod());

        // Check if filing already exists for this period
        List<TaxFiling> existing = taxFilingRepository.findByTenantIdAndFilingPeriod(
            command.getTenantId(), command.getFilingPeriod(),
            command.getJurisdiction(), command.getTaxType());

        for (TaxFiling f : existing) {
            if (f.getJurisdiction() == command.getJurisdiction() &&
                f.getTaxType() == command.getTaxType() &&
                f.getStatus() != TaxFiling.FilingStatus.CANCELLED &&
                f.getStatus() != TaxFiling.FilingStatus.ARCHIVED) {
                throw new ValidationException("Filing already exists for this period, jurisdiction, and tax type");
            }
        }

        TaxFiling filing = TaxFiling.create(
            command.getTenantId(),
            command.getFilingPeriod(),
            command.getFilingType(),
            command.getJurisdiction(),
            command.getTaxType(),
            command.getCurrency(),
            command.getDueDate(),
            command.getSubmittedBy()
        );

        TaxFiling savedFiling = taxFilingRepository.save(filing);

        log.info("Created tax filing: {} for tenant: {}", savedFiling.getFilingId(), command.getTenantId());
        return savedFiling;
    }

    @Transactional
    public TaxFiling updateFilingFigures(TaxFilingCommand.UpdateFilingFiguresCommand command) {
        log.info("Updating figures for filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.updateFigures(
            command.getGrossSales() != null ? command.getGrossSales() : filing.getGrossSales(),
            command.getTaxableSales() != null ? command.getTaxableSales() : filing.getTaxableSales(),
            command.getExemptSales() != null ? command.getExemptSales() : filing.getExemptSales(),
            command.getTotalTaxCollected() != null ? command.getTotalTaxCollected() : filing.getTotalTaxCollected(),
            command.getTotalTaxPaid() != null ? command.getTotalTaxPaid() : filing.getTotalTaxPaid()
        );

        TaxFiling savedFiling = taxFilingRepository.save(filing);

        log.info("Updated figures for filing: {}", command.getFilingId());
        return savedFiling;
    }

    @Transactional
    public void submitForReview(TaxFilingCommand.SubmitForReviewCommand command) {
        log.info("Submitting filing for review: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.submitForReview();
        taxFilingRepository.save(filing);

        log.info("Submitted filing for review: {}", command.getFilingId());
    }

    @Transactional
    public void submitFiling(TaxFilingCommand.SubmitFilingCommand command) {
        log.info("Submitting filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.submit();
        taxFilingRepository.save(filing);
        publishEvent(filing, "FILING_SUBMITTED");

        log.info("Submitted filing: {}", command.getFilingId());
    }

    @Transactional
    public void acknowledgeFiling(TaxFilingCommand.AcknowledgeFilingCommand command) {
        log.info("Acknowledging filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.acknowledge(command.getAcknowledgementNumber());
        taxFilingRepository.save(filing);
        publishEvent(filing, "FILING_ACKNOWLEDGED");

        log.info("Acknowledged filing: {}", command.getFilingId());
    }

    @Transactional
    public void acceptFiling(TaxFilingCommand.AcceptFilingCommand command) {
        log.info("Accepting filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.accept(command.getApprovedBy());
        taxFilingRepository.save(filing);
        publishEvent(filing, "FILING_ACCEPTED");

        log.info("Accepted filing: {}", command.getFilingId());
    }

    @Transactional
    public void rejectFiling(TaxFilingCommand.RejectFilingCommand command) {
        log.info("Rejecting filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.reject(command.getReason());
        taxFilingRepository.save(filing);
        publishEvent(filing, "FILING_REJECTED", command.getReason());

        log.info("Rejected filing: {}", command.getFilingId());
    }

    @Transactional
    public void addAdjustment(TaxFilingCommand.AddAdjustmentCommand command) {
        log.info("Adding adjustment to filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.addAdjustment(
            command.getAdjustmentType(),
            command.getAmount(),
            command.getReason(),
            command.getReference()
        );

        taxFilingRepository.save(filing);

        log.info("Added adjustment to filing: {}", command.getFilingId());
    }

    @Transactional
    public void linkCalculation(TaxFilingCommand.LinkCalculationCommand command) {
        log.info("Linking calculation to filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        TaxCalculation calculation = taxCalculationRepository.findByCalculationIdAndTenantId(
            command.getCalculationId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxCalculation", command.getCalculationId()));

        filing.linkCalculation(command.getCalculationId());

        // Update filing figures based on calculation
        BigDecimal newTaxCollected = filing.getTotalTaxCollected().add(calculation.getTotalTax());
        filing.updateFigures(
            filing.getGrossSales(),
            filing.getTaxableSales(),
            filing.getExemptSales(),
            newTaxCollected,
            filing.getTotalTaxPaid()
        );

        taxFilingRepository.save(filing);

        log.info("Linked calculation {} to filing: {}", command.getCalculationId(), command.getFilingId());
    }

    @Transactional
    public void markAsPaid(TaxFilingCommand.MarkAsPaidCommand command) {
        log.info("Marking filing as paid: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.markAsPaid(command.getPaymentReference());
        taxFilingRepository.save(filing);
        publishEvent(filing, "FILING_PAID");

        log.info("Marked filing as paid: {}", command.getFilingId());
    }

    @Transactional
    public void archiveFiling(TaxFilingCommand.ArchiveFilingCommand command) {
        log.info("Archiving filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.archive();
        taxFilingRepository.save(filing);

        log.info("Archived filing: {}", command.getFilingId());
    }

    @Transactional
    public void cancelFiling(TaxFilingCommand.CancelFilingCommand command) {
        log.info("Cancelling filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.cancel(command.getReason());
        taxFilingRepository.save(filing);
        publishEvent(filing, "FILING_CANCELLED", command.getReason());

        log.info("Cancelled filing: {}", command.getFilingId());
    }

    @Transactional
    public void addAttachment(TaxFilingCommand.AddAttachmentCommand command) {
        log.info("Adding attachment to filing: {} for tenant: {}",
            command.getFilingId(), command.getTenantId());

        TaxFiling filing = taxFilingRepository.findByFilingIdAndTenantId(
            command.getFilingId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxFiling", command.getFilingId()));

        filing.addAttachment(
            command.getFileName(),
            command.getFileType(),
            command.getFileSize(),
            command.getStorageLocation(),
            command.getUrl(),
            command.getUploadedBy()
        );

        taxFilingRepository.save(filing);

        log.info("Added attachment to filing: {}", command.getFilingId());
    }

    @Transactional
    public TaxFiling autoGenerateFiling(TaxFilingCommand.AutoGenerateFilingCommand command) {
        log.info("Auto-generating filing for tenant: {}, period: {}",
            command.getTenantId(), command.getFilingPeriod());

        // Get all calculations for the period
        List<TaxCalculation> calculations = taxCalculationRepository.findByTenantIdAndPeriod(
            command.getTenantId(),
            command.getFilingPeriod(),
            command.getJurisdiction(),
            command.getTaxType()
        );

        if (calculations.isEmpty()) {
            throw new ValidationException("No calculations found for the specified period");
        }

        // Check if filing already exists
        List<TaxFiling> existing = taxFilingRepository.findByTenantIdAndFilingPeriod(
            command.getTenantId(), command.getFilingPeriod(),
            command.getJurisdiction(), command.getTaxType());

        for (TaxFiling f : existing) {
            if (f.getJurisdiction() == command.getJurisdiction() &&
                f.getTaxType() == command.getTaxType() &&
                f.getStatus() != TaxFiling.FilingStatus.CANCELLED) {
                throw new ValidationException("Filing already exists for this period");
            }
        }

        // Calculate due date (typically 20th of following month)
        YearMonth followingMonth = command.getFilingPeriod().plusMonths(1);
        LocalDate dueDate = followingMonth.atDay(20);

        // Create filing
        TaxFiling filing = TaxFiling.create(
            command.getTenantId(),
            command.getFilingPeriod(),
            command.getFilingType(),
            command.getJurisdiction(),
            command.getTaxType(),
            "USD",
            dueDate,
            command.getGeneratedBy()
        );

        // Aggregate calculation data
        BigDecimal grossSales = calculations.stream()
            .map(TaxCalculation::getBaseAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTaxCollected = calculations.stream()
            .map(TaxCalculation::getTotalTax)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        filing.updateFigures(
            grossSales,
            grossSales,
            BigDecimal.ZERO,
            totalTaxCollected,
            BigDecimal.ZERO
        );

        // Link all calculations
        for (TaxCalculation calc : calculations) {
            filing.linkCalculation(calc.getCalculationId());
        }

        TaxFiling savedFiling = taxFilingRepository.save(filing);

        log.info("Auto-generated filing: {} with {} calculations",
            savedFiling.getFilingId(), calculations.size());

        return savedFiling;
    }

    private void publishEvent(TaxFiling filing, String eventType) {
        publishEvent(filing, eventType, null);
    }

    private void publishEvent(TaxFiling filing, String eventType, String reason) {
        if (eventPublisher.isReady()) {
            TaxFilingSubmittedEvent event = TaxFilingSubmittedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .filingId(filing.getFilingId())
                .tenantId(filing.getTenantId())
                .filingPeriod(filing.getFilingPeriod())
                .jurisdiction(filing.getJurisdiction())
                .taxType(filing.getTaxType())
                .filingType(filing.getFilingType())
                .eventType(eventType)
                .timestamp(java.time.Instant.now())
                .taxDue(filing.getTaxDue())
                .taxRefund(filing.getTaxRefund())
                .netAmount(filing.getNetAmount())
                .acknowledgementNumber(filing.getAcknowledgementNumber())
                .submittedBy(filing.getSubmittedBy())
                .approvedBy(filing.getApprovedBy())
                .reason(reason)
                .paymentReference(filing.getPaymentReference())
                .build();

            eventPublisher.publish(event);
        }
    }
}
