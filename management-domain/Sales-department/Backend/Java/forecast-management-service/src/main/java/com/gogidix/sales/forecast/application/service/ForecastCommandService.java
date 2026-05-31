package com.gogidix.sales.forecast.application.service;

import com.gogidix.sales.forecast.domain.event.*;
import com.gogidix.sales.forecast.domain.model.Forecast;
import com.gogidix.sales.forecast.domain.model.ForecastLineItem;
import com.gogidix.sales.forecast.domain.port.in.ForecastCommand;
import com.gogidix.sales.forecast.domain.port.out.EventPublisher;
import com.gogidix.sales.forecast.domain.repository.ForecastRepository;
import com.gogidix.sales.forecast.shared.exception.ConflictException;
import com.gogidix.sales.forecast.shared.exception.NotFoundException;
import com.gogidix.sales.forecast.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Forecast Command Service
 * Handles all write operations for forecasts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ForecastCommandService {

    private final ForecastRepository forecastRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Forecast create(ForecastCommand.CreateForecastCommand command) {
        log.info("Creating forecast for tenant: {}, name: {}",
            command.getTenantId(), command.getName());

        validateForecastPeriod(command.getStartDate(), command.getEndDate());

        Forecast forecast = Forecast.create(
            command.getTenantId(),
            command.getName(),
            command.getDescription(),
            command.getPeriod(),
            command.getStartDate(),
            command.getEndDate(),
            command.getCreatedBy(),
            command.getCurrency()
        );

        // Set additional fields
        forecast.setRegion(command.getRegion());
        forecast.setTerritory(command.getTerritory());
        forecast.setBusinessUnit(command.getBusinessUnit());

        Forecast savedForecast = forecastRepository.save(forecast);
        publishEvents(savedForecast);

        log.info("Created forecast: {} for tenant: {}", savedForecast.getForecastId(), command.getTenantId());
        return savedForecast;
    }

    @Transactional
    public Forecast update(ForecastCommand.UpdateForecastCommand command) {
        log.info("Updating forecast: {} for tenant: {}", command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (forecast.getStatus() != Forecast.ForecastStatus.DRAFT) {
            throw new ValidationException("Can only update draft forecasts");
        }

        if (command.getName() != null) {
            forecast.setName(command.getName());
        }
        if (command.getDescription() != null) {
            forecast.setDescription(command.getDescription());
        }
        if (command.getStartDate() != null) {
            forecast.setStartDate(command.getStartDate());
        }
        if (command.getEndDate() != null) {
            forecast.setEndDate(command.getEndDate());
        }
        if (command.getRegion() != null) {
            forecast.setRegion(command.getRegion());
        }
        if (command.getTerritory() != null) {
            forecast.setTerritory(command.getTerritory());
        }
        if (command.getBusinessUnit() != null) {
            forecast.setBusinessUnit(command.getBusinessUnit());
        }

        Forecast savedForecast = forecastRepository.save(forecast);
        publishEvents(savedForecast);

        return savedForecast;
    }

    @Transactional
    public void submit(ForecastCommand.SubmitForecastCommand command) {
        log.info("Submitting forecast: {} for tenant: {}", command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.submit();
        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Submitted forecast: {}", command.getForecastId());
    }

    @Transactional
    public void approve(ForecastCommand.ApproveForecastCommand command) {
        log.info("Approving forecast: {} by: {} for tenant: {}",
            command.getForecastId(), command.getApprover(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.approve(command.getApprover(), command.getApprovalLevel());
        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Approved forecast: {}", command.getForecastId());
    }

    @Transactional
    public void reject(ForecastCommand.RejectForecastCommand command) {
        log.info("Rejecting forecast: {} by: {} for tenant: {}",
            command.getForecastId(), command.getRejecter(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.reject(command.getRejecter(), command.getReason());
        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Rejected forecast: {}", command.getForecastId());
    }

    @Transactional
    public void publish(ForecastCommand.PublishForecastCommand command) {
        log.info("Publishing forecast: {} for tenant: {}", command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.publish();
        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Published forecast: {}", command.getForecastId());
    }

    @Transactional
    public void archive(ForecastCommand.ArchiveForecastCommand command) {
        log.info("Archiving forecast: {} for tenant: {}", command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.archive();
        forecastRepository.save(forecast);

        log.info("Archived forecast: {}", command.getForecastId());
    }

    @Transactional
    public void delete(ForecastCommand.DeleteForecastCommand command) {
        log.info("Deleting forecast: {} for tenant: {}", command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (forecast.getStatus() != Forecast.ForecastStatus.DRAFT) {
            throw new ValidationException("Can only delete draft forecasts");
        }

        forecastRepository.deleteByForecastIdAndTenantId(command.getForecastId(), command.getTenantId());

        log.info("Deleted forecast: {}", command.getForecastId());
    }

    @Transactional
    public ForecastLineItem addLineItem(ForecastCommand.AddLineItemCommand command) {
        log.info("Adding line item to forecast: {} for tenant: {}",
            command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (forecast.getLocked()) {
            throw new ValidationException("Cannot modify locked forecast");
        }

        ForecastLineItem lineItem = ForecastLineItem.create(
            command.getForecastId(),
            command.getTenantId(),
            command.getName(),
            command.getCategory(),
            command.getType(),
            command.getBestCase(),
            command.getLikely(),
            command.getWorstCase(),
            command.getCurrency()
        );

        // Set additional fields
        lineItem.setDescription(command.getDescription());
        lineItem.setProductId(command.getProductId());
        lineItem.setProductName(command.getProductName());
        lineItem.setTerritoryId(command.getTerritoryId());
        lineItem.setTerritoryName(command.getTerritoryName());
        lineItem.setCustomerSegmentId(command.getCustomerSegmentId());
        lineItem.setCustomerSegmentName(command.getCustomerSegmentName());
        lineItem.setSalesChannel(command.getSalesChannel());
        lineItem.setNotes(command.getNotes());
        lineItem.setOwner(command.getOwner());

        forecast.addLineItem(lineItem);
        forecastRepository.save(forecast);

        // Publish line item event
        ForecastLineItemEvent event = ForecastLineItemEvent.create(
            lineItem.getLineItemId(),
            command.getForecastId(),
            command.getTenantId(),
            command.getName(),
            command.getCategory().name(),
            command.getLikely(),
            command.getCurrency(),
            "ADDED"
        );
        eventPublisher.publishLineItemEvent(event);

        log.info("Added line item: {} to forecast: {}", lineItem.getLineItemId(), command.getForecastId());
        return lineItem;
    }

    @Transactional
    public void updateLineItem(ForecastCommand.UpdateLineItemCommand command) {
        log.info("Updating line item: {} in forecast: {} for tenant: {}",
            command.getLineItemId(), command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (forecast.getLocked()) {
            throw new ValidationException("Cannot modify locked forecast");
        }

        forecast.updateLineItem(
            command.getLineItemId(),
            command.getCategory(),
            command.getBestCase(),
            command.getLikely(),
            command.getWorstCase()
        );

        if (command.getName() != null) {
            ForecastLineItem item = forecast.getLineItems().stream()
                .filter(li -> li.getLineItemId().equals(command.getLineItemId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Line Item", command.getLineItemId()));
            item.setName(command.getName());
        }
        if (command.getNotes() != null) {
            ForecastLineItem item = forecast.getLineItems().stream()
                .filter(li -> li.getLineItemId().equals(command.getLineItemId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Line Item", command.getLineItemId()));
            item.setNotes(command.getNotes());
        }

        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Updated line item: {} in forecast: {}", command.getLineItemId(), command.getForecastId());
    }

    @Transactional
    public void removeLineItem(ForecastCommand.RemoveLineItemCommand command) {
        log.info("Removing line item: {} from forecast: {} for tenant: {}",
            command.getLineItemId(), command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (forecast.getLocked()) {
            throw new ValidationException("Cannot modify locked forecast");
        }

        forecast.removeLineItem(command.getLineItemId());
        forecastRepository.save(forecast);

        log.info("Removed line item: {} from forecast: {}", command.getLineItemId(), command.getForecastId());
    }

    @Transactional
    public Forecast createVersion(ForecastCommand.CreateVersionCommand command) {
        log.info("Creating new version of forecast: {} for tenant: {}",
            command.getForecastId(), command.getTenantId());

        Forecast originalForecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        Forecast newVersion = originalForecast.createNewVersion(command.getUpdatedBy());
        Forecast savedVersion = forecastRepository.save(newVersion);
        publishEvents(savedVersion);

        log.info("Created new version: {} of forecast: {}", savedVersion.getForecastId(), command.getForecastId());
        return savedVersion;
    }

    @Transactional
    public void lock(ForecastCommand.LockForecastCommand command) {
        log.info("Locking forecast: {} for tenant: {}", command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.lock();
        forecastRepository.save(forecast);

        log.info("Locked forecast: {}", command.getForecastId());
    }

    @Transactional
    public void unlock(ForecastCommand.UnlockForecastCommand command) {
        log.info("Unlocking forecast: {} for tenant: {}", command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
            command.getForecastId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.unlock();
        forecastRepository.save(forecast);

        log.info("Unlocked forecast: {}", command.getForecastId());
    }

    private void validateForecastPeriod(java.time.YearMonth startDate, java.time.YearMonth endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ValidationException("endDate", "End date must be after start date");
        }
    }

    private void publishEvents(Forecast forecast) {
        if (!forecast.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(forecast.getDomainEvents());
            forecast.clearDomainEvents();
        }
    }
}
