package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.domain.model.Quota;
import com.gogidix.sales.territory.domain.model.Territory;
import com.gogidix.sales.territory.domain.port.in.QuotaCommand;
import com.gogidix.sales.territory.domain.port.out.EventPublisher;
import com.gogidix.sales.territory.domain.repository.QuotaRepository;
import com.gogidix.sales.territory.domain.repository.TerritoryRepository;
import com.gogidix.sales.territory.shared.exception.NotFoundException;
import com.gogidix.sales.territory.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Quota Command Service
 * Handles all write operations for quotas
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuotaCommandService {

    private final QuotaRepository quotaRepository;
    private final TerritoryRepository territoryRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Quota create(QuotaCommand.CreateQuotaCommand command) {
        log.info("Creating quota for territory: {} for tenant: {}",
            command.getTerritoryId(), command.getTenantId());

        // Verify territory exists
        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        // Validate date range
        if (command.getEndDate().isBefore(command.getStartDate())) {
            throw new ValidationException("endDate", "End date must be after start date");
        }

        Quota quota = Quota.create(
            command.getTenantId(),
            command.getTerritoryId(),
            command.getSalesRepresentativeId(),
            command.getType(),
            command.getAmount(),
            command.getCurrency(),
            command.getPeriod(),
            command.getStartDate(),
            command.getEndDate()
        );

        Quota savedQuota = quotaRepository.save(quota);
        publishEvents(savedQuota);

        log.info("Created quota: {} for tenant: {}", savedQuota.getQuotaId(), command.getTenantId());
        return savedQuota;
    }

    @Transactional
    public void activate(QuotaCommand.ActivateQuotaCommand command) {
        log.info("Activating quota: {} for tenant: {}", command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        quota.activate(command.getApprovedBy());
        quotaRepository.save(quota);
        publishEvents(quota);

        log.info("Activated quota: {}", command.getQuotaId());
    }

    @Transactional
    public void pause(QuotaCommand.PauseQuotaCommand command) {
        log.info("Pausing quota: {} for tenant: {}", command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        quota.pause();
        quotaRepository.save(quota);
        publishEvents(quota);

        log.info("Paused quota: {}", command.getQuotaId());
    }

    @Transactional
    public void resume(QuotaCommand.ResumeQuotaCommand command) {
        log.info("Resuming quota: {} for tenant: {}", command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        quota.resume();
        quotaRepository.save(quota);
        publishEvents(quota);

        log.info("Resumed quota: {}", command.getQuotaId());
    }

    @Transactional
    public void cancel(QuotaCommand.CancelQuotaCommand command) {
        log.info("Cancelling quota: {} for tenant: {}", command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        quota.cancel();
        quotaRepository.save(quota);
        publishEvents(quota);

        log.info("Cancelled quota: {}", command.getQuotaId());
    }

    @Transactional
    public void adjust(QuotaCommand.AdjustQuotaCommand command) {
        log.info("Adjusting quota: {} for tenant: {}", command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        quota.adjustAmount(command.getNewAmount(), command.getAdjustedBy());
        quotaRepository.save(quota);
        publishEvents(quota);

        log.info("Adjusted quota: {} to amount: {}", command.getQuotaId(), command.getNewAmount());
    }

    @Transactional
    public void updateAchievement(QuotaCommand.UpdateAchievementCommand command) {
        log.info("Updating achievement for quota: {} for tenant: {}",
            command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        quota.updateAchievement(command.getAchievement());
        quotaRepository.save(quota);

        log.info("Updated achievement for quota: {} to: {}", command.getQuotaId(), command.getAchievement());
    }

    @Transactional
    public void addBreakdown(QuotaCommand.AddQuotaBreakdownCommand command) {
        log.info("Adding breakdown to quota: {} for tenant: {}",
            command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        quota.addBreakdown(
            command.getCategory(),
            command.getAmount(),
            command.getDescription()
        );

        // Optionally set product IDs if provided
        Quota.QuotaBreakdown item = quota.getBreakdown().get(quota.getBreakdown().size() - 1);
        if (command.getProductId() != null) {
            item.setProductId(command.getProductId());
        }
        if (command.getProductCategoryId() != null) {
            item.setProductCategoryId(command.getProductCategoryId());
        }

        quotaRepository.save(quota);

        log.info("Added breakdown to quota: {}", command.getQuotaId());
    }

    @Transactional
    public void delete(QuotaCommand.DeleteQuotaCommand command) {
        log.info("Deleting quota: {} for tenant: {}", command.getQuotaId(), command.getTenantId());

        Quota quota = quotaRepository.findByQuotaIdAndTenantId(
            command.getQuotaId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Quota", command.getQuotaId()));

        if (quota.getStatus() == Quota.QuotaStatus.ACTIVE) {
            throw new ValidationException("Cannot delete active quota. Cancel it first.");
        }

        quotaRepository.deleteByQuotaIdAndTenantId(command.getQuotaId(), command.getTenantId());

        log.info("Deleted quota: {}", command.getQuotaId());
    }

    private void publishEvents(Quota quota) {
        if (!quota.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(quota.getDomainEvents());
            quota.clearDomainEvents();
        }
    }
}
