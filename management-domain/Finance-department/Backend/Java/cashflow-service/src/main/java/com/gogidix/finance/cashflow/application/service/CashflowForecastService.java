package com.gogidix.finance.cashflow.application.service;

import com.gogidix.finance.cashflow.domain.event.CashflowForecastGeneratedEvent;
import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.port.in.CashflowForecastCommand;
import com.gogidix.finance.cashflow.domain.port.out.EventPublisher;
import com.gogidix.finance.cashflow.domain.repository.CashflowForecastRepository;
import com.gogidix.finance.cashflow.domain.repository.CashflowItemRepository;
import com.gogidix.finance.cashflow.shared.exception.NotFoundException;
import com.gogidix.finance.cashflow.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cashflow Forecast Command Service
 * Handles all write operations for cashflow forecasts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CashflowForecastService {

    private final CashflowForecastRepository cashflowForecastRepository;
    private final CashflowItemRepository cashflowItemRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public CashflowForecast create(CashflowForecastCommand.CreateForecastCommand command) {
        log.info("Creating cashflow forecast for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        validateForecastDates(command.getStartDate(), command.getEndDate());

        CashflowForecast forecast = CashflowForecast.create(
                command.getTenantId(),
                command.getName(),
                command.getStartDate(),
                command.getEndDate(),
                command.getPeriod(),
                command.getScenario(),
                command.getGeneratedBy(),
                command.getOpeningBalance()
        );

        // Set additional fields
        forecast.setDescription(command.getDescription());
        forecast.setConfidenceLevel(command.getConfidenceLevel() != null
                ? command.getConfidenceLevel() : CashflowForecast.ConfidenceLevel.MEDIUM);
        forecast.setTags(command.getTags() != null ? command.getTags() : new ArrayList<>());
        forecast.setNotes(command.getNotes());
        forecast.setIsBaseline(command.getIsBaseline() != null ? command.getIsBaseline() : false);
        forecast.setParentForecastId(command.getParentForecastId());

        CashflowForecast savedForecast = cashflowForecastRepository.save(forecast);
        publishEvents(savedForecast);

        log.info("Created cashflow forecast: {} for tenant: {}",
                savedForecast.getForecastId(), command.getTenantId());
        return savedForecast;
    }

    @Transactional
    public CashflowForecast update(CashflowForecastCommand.UpdateForecastCommand command) {
        log.info("Updating cashflow forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        if (forecast.getStatus() == CashflowForecast.ForecastStatus.APPROVED ||
                forecast.getStatus() == CashflowForecast.ForecastStatus.ARCHIVED) {
            throw new ValidationException("Cannot update approved or archived forecasts");
        }

        if (command.getName() != null) {
            forecast.setName(command.getName());
        }
        if (command.getDescription() != null) {
            forecast.setDescription(command.getDescription());
        }
        if (command.getOpeningBalance() != null) {
            forecast.setOpeningBalance(command.getOpeningBalance());
        }
        if (command.getConfidenceLevel() != null) {
            forecast.setConfidenceLevel(command.getConfidenceLevel());
        }
        if (command.getTags() != null) {
            forecast.setTags(command.getTags());
        }
        if (command.getNotes() != null) {
            forecast.setNotes(command.getNotes());
        }

        forecast.setLastUpdated(java.time.Instant.now());

        CashflowForecast savedForecast = cashflowForecastRepository.save(forecast);

        log.info("Updated cashflow forecast: {}", command.getForecastId());
        return savedForecast;
    }

    @Transactional
    public void generate(CashflowForecastCommand.GenerateForecastCommand command) {
        log.info("Generating cashflow forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        LocalDate startDate = command.getStartDate() != null ? command.getStartDate() : forecast.getStartDate();
        LocalDate endDate = command.getEndDate() != null ? command.getEndDate() : forecast.getEndDate();

        // Get relevant cashflow items
        List<CashflowItem> items = getCashflowItemsForForecast(
                command.getTenantId(), startDate, endDate,
                command.getItemCategories(), command.getCostCenters(), command.getProjects());

        forecast.generate(items);
        cashflowForecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Generated cashflow forecast: {}", command.getForecastId());
    }

    @Transactional
    public void approve(CashflowForecastCommand.ApproveForecastCommand command) {
        log.info("Approving cashflow forecast: {} by: {} for tenant: {}",
                command.getForecastId(), command.getApprovedBy(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        forecast.approve(command.getApprovedBy());
        cashflowForecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Approved cashflow forecast: {}", command.getForecastId());
    }

    @Transactional
    public void reject(CashflowForecastCommand.RejectForecastCommand command) {
        log.info("Rejecting cashflow forecast: {} by: {} for tenant: {}",
                command.getForecastId(), command.getRejectedBy(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        forecast.reject(command.getRejectedBy(), command.getReason());
        cashflowForecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Rejected cashflow forecast: {}", command.getForecastId());
    }

    @Transactional
    public void archive(CashflowForecastCommand.ArchiveForecastCommand command) {
        log.info("Archiving cashflow forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        forecast.archive();
        cashflowForecastRepository.save(forecast);

        log.info("Archived cashflow forecast: {}", command.getForecastId());
    }

    @Transactional
    public CashflowForecast createNewVersion(CashflowForecastCommand.CreateNewVersionCommand command) {
        log.info("Creating new version of forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        CashflowForecast newForecast = forecast.createNewVersion(command.getGeneratedBy());
        CashflowForecast savedForecast = cashflowForecastRepository.save(newForecast);
        publishEvents(savedForecast);

        log.info("Created new version of cashflow forecast: {}", savedForecast.getForecastId());
        return savedForecast;
    }

    @Transactional
    public void calculateVariance(CashflowForecastCommand.CalculateVarianceCommand command) {
        log.info("Calculating variance for forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        forecast.calculateVariance(
                command.getCategory(),
                command.getForecastedAmount(),
                command.getActualAmount()
        );

        cashflowForecastRepository.save(forecast);

        log.info("Calculated variance for forecast: {}", command.getForecastId());
    }

    @Transactional
    public void delete(CashflowForecastCommand.DeleteForecastCommand command) {
        log.info("Deleting cashflow forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        if (forecast.getStatus() == CashflowForecast.ForecastStatus.APPROVED) {
            throw new ValidationException("Cannot delete approved forecasts");
        }

        cashflowForecastRepository.deleteByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId());

        log.info("Deleted cashflow forecast: {}", command.getForecastId());
    }

    @Transactional
    public void setConfidence(CashflowForecastCommand.SetConfidenceCommand command) {
        log.info("Setting confidence for forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        CashflowForecast forecast = cashflowForecastRepository.findByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CashflowForecast", command.getForecastId()));

        forecast.setConfidence(command.getConfidenceLevel(), command.getVariancePercentage());
        cashflowForecastRepository.save(forecast);

        log.info("Set confidence for forecast: {}", command.getForecastId());
    }

    private void validateForecastDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ValidationException("End date must be after start date");
        }

        long durationDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        if (durationDays > 3650) { // 10 years
            throw new ValidationException("Forecast period cannot exceed 10 years");
        }
    }

    private List<CashflowItem> getCashflowItemsForForecast(String tenantId, LocalDate startDate,
                                                             LocalDate endDate, List<String> categories,
                                                             List<String> costCenters, List<String> projects) {
        List<CashflowItem> items = cashflowItemRepository.findByTenantIdAndTransactionDateBetween(
                tenantId, startDate, endDate);

        // Filter by categories if provided
        if (categories != null && !categories.isEmpty()) {
            List<CashflowItem.CashflowCategory> categoryEnums = categories.stream()
                    .map(CashflowItem.CashflowCategory::valueOf)
                    .toList();
            items = items.stream()
                    .filter(item -> categoryEnums.contains(item.getCategory()))
                    .toList();
        }

        // Filter by cost centers if provided
        if (costCenters != null && !costCenters.isEmpty()) {
            items = items.stream()
                    .filter(item -> costCenters.contains(item.getCostCenter()))
                    .toList();
        }

        // Filter by projects if provided
        if (projects != null && !projects.isEmpty()) {
            items = items.stream()
                    .filter(item -> projects.contains(item.getProjectId()))
                    .toList();
        }

        return items;
    }

    private void publishEvents(CashflowForecast forecast) {
        if (!forecast.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(forecast.getDomainEvents().stream().map(e -> (Object) e).toList());
            forecast.clearDomainEvents();
        }
    }
}
