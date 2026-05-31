package com.gogidix.finance.tax.application.service;

import com.gogidix.finance.tax.domain.event.TaxRateUpdatedEvent;
import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.port.in.TaxRateCommand;
import com.gogidix.finance.tax.domain.port.out.EventPublisher;
import com.gogidix.finance.tax.domain.repository.TaxRateRepository;
import com.gogidix.finance.tax.shared.exception.ConflictException;
import com.gogidix.finance.tax.shared.exception.NotFoundException;
import com.gogidix.finance.tax.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Tax Rate Command Service
 * Handles all write operations for tax rates
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaxRateCommandService {

    private final TaxRateRepository taxRateRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public TaxRate createTaxRate(TaxRateCommand.CreateTaxRateCommand command) {
        log.info("Creating tax rate for tenant: {}, tax code: {}",
            command.getTenantId(), command.getTaxCode());

        // Check if tax rate already exists
        if (taxRateRepository.existsByTaxCodeAndJurisdictionAndTenantId(
                command.getTaxCode(), command.getJurisdiction(), command.getTenantId())) {
            throw new ConflictException("TaxRate", command.getTaxCode() + "-" + command.getJurisdiction());
        }

        TaxRate taxRate = TaxRate.create(
            command.getTenantId(),
            command.getJurisdiction(),
            command.getTaxType(),
            command.getTaxCode(),
            command.getRatePercentage(),
            command.getEffectiveDate(),
            command.getCreatedBy()
        );

        // Set optional fields
        taxRate.setDescription(command.getDescription());
        taxRate.setExpiryDate(command.getExpiryDate());
        taxRate.setIsCompound(command.getIsCompound() != null ? command.getIsCompound() : false);
        taxRate.setIsRecoverable(command.getIsRecoverable() != null ? command.getIsRecoverable() : false);
        taxRate.setRecoveryRate(command.getRecoveryRate());
        taxRate.setMinThreshold(command.getMinThreshold());
        taxRate.setMaxThreshold(command.getMaxThreshold());
        taxRate.setNotes(command.getNotes());

        TaxRate savedTaxRate = taxRateRepository.save(taxRate);
        publishEvent(savedTaxRate, "TAX_RATE_CREATED");

        log.info("Created tax rate: {} for tenant: {}", savedTaxRate.getTaxRateId(), command.getTenantId());
        return savedTaxRate;
    }

    @Transactional
    public TaxRate updateTaxRate(TaxRateCommand.UpdateTaxRateCommand command) {
        log.info("Updating tax rate: {} for tenant: {}", command.getTaxRateId(), command.getTenantId());

        TaxRate taxRate = taxRateRepository.findByTaxRateIdAndTenantId(
            command.getTaxRateId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxRate", command.getTaxRateId()));

        BigDecimal oldRate = taxRate.getRatePercentage();

        if (taxRate.getStatus() == TaxRate.TaxRateStatus.ACTIVE) {
            throw new ValidationException("Cannot update active tax rates. Create a new version instead.");
        }

        if (command.getNewRate() != null) {
            taxRate.updateRate(command.getNewRate(), "system");
        }
        if (command.getDescription() != null) {
            taxRate.setDescription(command.getDescription());
        }
        if (command.getNewExpiryDate() != null) {
            taxRate.setExpiryDate(command.getNewExpiryDate());
        }
        if (command.getIsCompound() != null) {
            taxRate.setIsCompound(command.getIsCompound());
        }
        if (command.getIsRecoverable() != null) {
            taxRate.setIsRecoverable(command.getIsRecoverable());
        }
        if (command.getRecoveryRate() != null) {
            taxRate.setRecoveryRate(command.getRecoveryRate());
        }
        if (command.getNotes() != null) {
            taxRate.setNotes(command.getNotes());
        }

        TaxRate savedTaxRate = taxRateRepository.save(taxRate);

        if (command.getNewRate() != null && !command.getNewRate().equals(oldRate)) {
            publishEvent(savedTaxRate, "TAX_RATE_UPDATED", oldRate, command.getNewRate(), null);
        } else {
            publishEvent(savedTaxRate, "TAX_RATE_UPDATED");
        }

        log.info("Updated tax rate: {}", command.getTaxRateId());
        return savedTaxRate;
    }

    @Transactional
    public void activateTaxRate(TaxRateCommand.ActivateTaxRateCommand command) {
        log.info("Activating tax rate: {} for tenant: {}", command.getTaxRateId(), command.getTenantId());

        TaxRate taxRate = taxRateRepository.findByTaxRateIdAndTenantId(
            command.getTaxRateId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxRate", command.getTaxRateId()));

        taxRate.activate(command.getApprovedBy());
        taxRateRepository.save(taxRate);
        publishEvent(taxRate, "TAX_RATE_ACTIVATED");

        log.info("Activated tax rate: {}", command.getTaxRateId());
    }

    @Transactional
    public void expireTaxRate(TaxRateCommand.ExpireTaxRateCommand command) {
        log.info("Expiring tax rate: {} for tenant: {}", command.getTaxRateId(), command.getTenantId());

        TaxRate taxRate = taxRateRepository.findByTaxRateIdAndTenantId(
            command.getTaxRateId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxRate", command.getTaxRateId()));

        taxRate.expire();
        taxRateRepository.save(taxRate);
        publishEvent(taxRate, "TAX_RATE_EXPIRED");

        log.info("Expired tax rate: {}", command.getTaxRateId());
    }

    @Transactional
    public void archiveTaxRate(TaxRateCommand.ArchiveTaxRateCommand command) {
        log.info("Archiving tax rate: {} for tenant: {}", command.getTaxRateId(), command.getTenantId());

        TaxRate taxRate = taxRateRepository.findByTaxRateIdAndTenantId(
            command.getTaxRateId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxRate", command.getTaxRateId()));

        taxRate.archive();
        taxRateRepository.save(taxRate);
        publishEvent(taxRate, "TAX_RATE_ARCHIVED");

        log.info("Archived tax rate: {}", command.getTaxRateId());
    }

    @Transactional
    public TaxRate createNewVersion(TaxRateCommand.CreateNewVersionCommand command) {
        log.info("Creating new version of tax rate: {} for tenant: {}",
            command.getTaxRateId(), command.getTenantId());

        TaxRate existingTaxRate = taxRateRepository.findByTaxRateIdAndTenantId(
            command.getTaxRateId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxRate", command.getTaxRateId()));

        TaxRate newVersion = existingTaxRate.createNewVersion(
            command.getNewRate(),
            command.getNewEffectiveDate(),
            command.getCreatedBy()
        );

        TaxRate savedTaxRate = taxRateRepository.save(newVersion);
        publishEvent(savedTaxRate, "TAX_RATE_VERSION_CREATED", existingTaxRate.getRatePercentage(), command.getNewRate(), command.getCreatedBy());

        log.info("Created new version: {} of tax rate: {}", savedTaxRate.getTaxRateId(), command.getTaxRateId());
        return savedTaxRate;
    }

    @Transactional
    public void deleteTaxRate(TaxRateCommand.DeleteTaxRateCommand command) {
        log.info("Deleting tax rate: {} for tenant: {}", command.getTaxRateId(), command.getTenantId());

        TaxRate taxRate = taxRateRepository.findByTaxRateIdAndTenantId(
            command.getTaxRateId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("TaxRate", command.getTaxRateId()));

        if (taxRate.getStatus() == TaxRate.TaxRateStatus.ACTIVE) {
            throw new ValidationException("Cannot delete active tax rates");
        }

        taxRateRepository.deleteByTaxRateIdAndTenantId(command.getTaxRateId(), command.getTenantId());

        log.info("Deleted tax rate: {}", command.getTaxRateId());
    }

    @Transactional
    public void batchUpdateRates(TaxRateCommand.BatchUpdateRatesCommand command) {
        log.info("Batch updating {} tax rates for tenant: {}",
            command.getRates().size(), command.getTenantId());

        List<TaxRate> updatedRates = new ArrayList<>();

        for (TaxRateCommand.BatchUpdateRatesCommand.RateUpdate rateUpdate : command.getRates()) {
            TaxRate taxRate = taxRateRepository.findByTaxRateIdAndTenantId(
                rateUpdate.getTaxRateId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("TaxRate", rateUpdate.getTaxRateId()));

            if (taxRate.getStatus() != TaxRate.TaxRateStatus.ACTIVE) {
                taxRate.updateRate(rateUpdate.getNewRate(), command.getUpdatedBy());
                if (rateUpdate.getNewEffectiveDate() != null) {
                    taxRate.setEffectiveDate(rateUpdate.getNewEffectiveDate());
                }
                updatedRates.add(taxRate);
            }
        }

        taxRateRepository.saveAll(updatedRates);

        for (TaxRate taxRate : updatedRates) {
            publishEvent(taxRate, "TAX_RATE_UPDATED", command.getUpdatedBy());
        }

        log.info("Batch updated {} tax rates", updatedRates.size());
    }

    private void publishEvent(TaxRate taxRate, String eventType) {
        publishEvent(taxRate, eventType, null, null, null);
    }

    private void publishEvent(TaxRate taxRate, String eventType, String changedBy) {
        publishEvent(taxRate, eventType, null, null, changedBy);
    }

    private void publishEvent(TaxRate taxRate, String eventType, BigDecimal oldRate, BigDecimal newRate, String changedBy) {
        if (eventPublisher.isReady()) {
            TaxRateUpdatedEvent event = TaxRateUpdatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .taxRateId(taxRate.getTaxRateId())
                .tenantId(taxRate.getTenantId())
                .jurisdiction(taxRate.getJurisdiction())
                .taxType(taxRate.getTaxType())
                .taxCode(taxRate.getTaxCode())
                .eventType(eventType)
                .timestamp(java.time.Instant.now())
                .oldRate(oldRate)
                .newRate(newRate != null ? newRate : taxRate.getRatePercentage())
                .effectiveDate(taxRate.getEffectiveDate())
                .status(taxRate.getStatus() != null ? taxRate.getStatus().name() : null)
                .version(taxRate.getVersion())
                .changedBy(changedBy)
                .build();

            eventPublisher.publish(event);
        }
    }
}
