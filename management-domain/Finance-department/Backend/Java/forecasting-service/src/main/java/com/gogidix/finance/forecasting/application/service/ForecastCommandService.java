package com.gogidix.finance.forecasting.application.service;

import com.gogidix.finance.forecasting.domain.model.Forecast;
import com.gogidix.finance.forecasting.domain.model.ForecastMetric;
import com.gogidix.finance.forecasting.domain.port.in.ForecastCommand;
import com.gogidix.finance.forecasting.domain.port.out.EventPublisher;
import com.gogidix.finance.forecasting.domain.repository.ForecastRepository;
import com.gogidix.finance.forecasting.shared.exception.ConflictException;
import com.gogidix.finance.forecasting.shared.exception.NotFoundException;
import com.gogidix.finance.forecasting.shared.exception.ValidationException;
import com.gogidix.finance.forecasting.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

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

    /**
     * Creates a new forecast
     *
     * @param command the create forecast command
     * @return the created forecast
     */
    @Transactional
    public Forecast create(ForecastCommand.CreateForecastCommand command) {
        log.info("Creating forecast for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        validateForecastDates(command.getStartDate(), command.getEndDate());
        validateForecastName(command.getName(), command.getTenantId());

        Forecast forecast = Forecast.create(
                command.getTenantId(),
                command.getName(),
                command.getForecastType(),
                command.getForecastHorizon(),
                command.getStartDate(),
                command.getEndDate(),
                command.getCurrency(),
                command.getCreatedBy()
        );

        // Set optional fields
        forecast.setDescription(command.getDescription());
        forecast.setDepartment(command.getDepartment());
        forecast.setCategory(command.getCategory());
        forecast.setScenario(command.getScenario());
        forecast.setDataSource(command.getDataSource());
        forecast.setNotes(command.getNotes());

        // Set confidence level if provided
        if (command.getConfidenceLevel() != null) {
            forecast.updateConfidenceLevel(command.getConfidenceLevel());
        }

        // Add metrics if provided
        if (command.getMetrics() != null && !command.getMetrics().isEmpty()) {
            command.getMetrics().forEach(forecast::addMetric);
            forecast.recalculateTotal();
        }

        // Set initial amount if provided
        if (command.getInitialAmount() != null) {
            forecast.setTotalForecastAmount(command.getInitialAmount());
        }

        Forecast savedForecast = forecastRepository.save(forecast);
        publishEvents(savedForecast);

        log.info("Created forecast: {} for tenant: {}",
                savedForecast.getForecastId(), command.getTenantId());
        return savedForecast;
    }

    /**
     * Updates an existing forecast
     *
     * @param command the update forecast command
     * @return the updated forecast
     */
    @Transactional
    public Forecast update(ForecastCommand.UpdateForecastCommand command) {
        log.info("Updating forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (!forecast.isEditable()) {
            throw new ValidationException("Cannot update forecast in current status: "
                    + forecast.getStatus());
        }

        // Update fields if provided
        if (command.getName() != null) {
            forecast.setName(command.getName());
        }
        if (command.getDescription() != null) {
            forecast.setDescription(command.getDescription());
        }
        if (command.getStartDate() != null && command.getEndDate() != null) {
            validateForecastDates(command.getStartDate(), command.getEndDate());
            forecast.setStartDate(command.getStartDate());
            forecast.setEndDate(command.getEndDate());
        }
        if (command.getDepartment() != null) {
            forecast.setDepartment(command.getDepartment());
        }
        if (command.getCategory() != null) {
            forecast.setCategory(command.getCategory());
        }
        if (command.getScenario() != null) {
            forecast.setScenario(command.getScenario());
        }
        if (command.getConfidenceLevel() != null) {
            forecast.updateConfidenceLevel(command.getConfidenceLevel());
        }
        if (command.getDataSource() != null) {
            forecast.setDataSource(command.getDataSource());
        }
        if (command.getNotes() != null) {
            forecast.setNotes(command.getNotes());
        }
        if (command.getMetrics() != null) {
            forecast.setMetrics(command.getMetrics());
            forecast.recalculateTotal();
        }

        forecast.updateTimestamp();

        Forecast savedForecast = forecastRepository.save(forecast);
        publishEvents(savedForecast);

        log.info("Updated forecast: {}", command.getForecastId());
        return savedForecast;
    }

    /**
     * Submits a forecast for approval
     *
     * @param command the submit forecast command
     */
    @Transactional
    public void submit(ForecastCommand.SubmitForecastCommand command) {
        log.info("Submitting forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.submitForApproval();
        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Submitted forecast: {}", command.getForecastId());
    }

    /**
     * Approves a forecast
     *
     * @param command the approve forecast command
     */
    @Transactional
    public void approve(ForecastCommand.ApproveForecastCommand command) {
        log.info("Approving forecast: {} by: {} for tenant: {}",
                command.getForecastId(), command.getApprover(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.approve(command.getApprover());
        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Approved forecast: {}", command.getForecastId());
    }

    /**
     * Rejects a forecast
     *
     * @param command the reject forecast command
     */
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

    /**
     * Deletes a forecast
     *
     * @param command the delete forecast command
     */
    @Transactional
    public void delete(ForecastCommand.DeleteForecastCommand command) {
        log.info("Deleting forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (!forecast.isEditable()) {
            throw new ValidationException("Cannot delete forecast in current status: "
                    + forecast.getStatus());
        }

        forecastRepository.deleteByForecastIdAndTenantId(
                command.getForecastId(), command.getTenantId());

        log.info("Deleted forecast: {}", command.getForecastId());
    }

    /**
     * Regenerates a forecast with new data
     *
     * @param command the regenerate forecast command
     * @return the regenerated forecast
     */
    @Transactional
    public Forecast regenerate(ForecastCommand.RegenerateForecastCommand command) {
        log.info("Regenerating forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        BigDecimal newTotalAmount = command.getNewTotalAmount();
        if (newTotalAmount == null && command.getNewMetrics() != null) {
            // Calculate total from metrics
            newTotalAmount = command.getNewMetrics().stream()
                    .map(ForecastMetric::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        forecast.regenerate(command.getNewMetrics(), newTotalAmount);

        if (command.getNewConfidenceLevel() != null) {
            forecast.updateConfidenceLevel(command.getNewConfidenceLevel());
        }

        if (command.getScenario() != null) {
            forecast.setScenario(command.getScenario());
        }

        if (command.getDataSource() != null) {
            forecast.setDataSource(command.getDataSource());
        }

        Forecast savedForecast = forecastRepository.save(forecast);
        publishEvents(savedForecast);

        log.info("Regenerated forecast: {}", command.getForecastId());
        return savedForecast;
    }

    /**
     * Archives a forecast
     *
     * @param command the archive forecast command
     */
    @Transactional
    public void archive(ForecastCommand.ArchiveForecastCommand command) {
        log.info("Archiving forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.archive();
        forecastRepository.save(forecast);

        log.info("Archived forecast: {}", command.getForecastId());
    }

    /**
     * Updates the actual amount for a forecast
     *
     * @param command the update actual amount command
     */
    @Transactional
    public void updateActualAmount(ForecastCommand.UpdateActualAmountCommand command) {
        log.info("Updating actual amount for forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        validateAmount(command.getActualAmount());

        forecast.updateActualAmount(command.getActualAmount());
        forecastRepository.save(forecast);

        log.info("Updated actual amount for forecast: {}", command.getForecastId());
    }

    /**
     * Adds a metric to a forecast
     *
     * @param command the add metric command
     */
    @Transactional
    public void addMetric(ForecastCommand.AddMetricCommand command) {
        log.info("Adding metric to forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        if (!forecast.isEditable()) {
            throw new ValidationException("Cannot add metric to forecast in current status: "
                    + forecast.getStatus());
        }

        ForecastMetric metric = command.getMetric();
        if (metric.getForecastId() == null) {
            metric.setForecastId(command.getForecastId());
        }

        forecast.addMetric(metric);
        forecastRepository.save(forecast);

        log.info("Added metric to forecast: {}", command.getForecastId());
    }

    /**
     * Updates the confidence level of a forecast
     *
     * @param command the update confidence level command
     */
    @Transactional
    public void updateConfidenceLevel(ForecastCommand.UpdateConfidenceLevelCommand command) {
        log.info("Updating confidence level for forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.updateConfidenceLevel(command.getConfidenceLevel());
        forecastRepository.save(forecast);

        log.info("Updated confidence level for forecast: {}", command.getForecastId());
    }

    /**
     * Sets the scenario for a forecast
     *
     * @param command the set scenario command
     */
    @Transactional
    public void setScenario(ForecastCommand.SetScenarioCommand command) {
        log.info("Setting scenario for forecast: {} for tenant: {}",
                command.getForecastId(), command.getTenantId());

        Forecast forecast = forecastRepository.findByForecastIdAndTenantId(
                        command.getForecastId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Forecast", command.getForecastId()));

        forecast.setScenario(command.getScenario());
        forecastRepository.save(forecast);
        publishEvents(forecast);

        log.info("Set scenario for forecast: {}", command.getForecastId());
    }

    /**
     * Submits a forecast for approval using tenant context
     *
     * @param forecastId the forecast ID
     */
    @Transactional
    public void submitForApproval(String forecastId) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        ForecastCommand.SubmitForecastCommand command =
                new ForecastCommand.SubmitForecastCommand(tenantId, forecastId, userId);
        submit(command);
    }

    /**
     * Approves a forecast using tenant context
     *
     * @param forecastId the forecast ID
     * @param comments optional approval comments
     */
    @Transactional
    public void approveForecast(String forecastId, String comments) {
        String tenantId = RequestContextHolder.getTenantId();
        String approver = RequestContextHolder.getUserId().orElse("system");

        ForecastCommand.ApproveForecastCommand command =
                new ForecastCommand.ApproveForecastCommand(tenantId, forecastId, approver, comments, null);
        approve(command);
    }

    /**
     * Rejects a forecast using tenant context
     *
     * @param forecastId the forecast ID
     * @param reason the rejection reason
     */
    @Transactional
    public void rejectForecast(String forecastId, String reason) {
        String tenantId = RequestContextHolder.getTenantId();
        String rejecter = RequestContextHolder.getUserId().orElse("system");

        ForecastCommand.RejectForecastCommand command =
                new ForecastCommand.RejectForecastCommand(tenantId, forecastId, rejecter, reason);
        reject(command);
    }

    /**
     * Deletes a forecast using tenant context
     *
     * @param forecastId the forecast ID
     */
    @Transactional
    public void deleteForecast(String forecastId) {
        String tenantId = RequestContextHolder.getTenantId();

        ForecastCommand.DeleteForecastCommand command =
                new ForecastCommand.DeleteForecastCommand(tenantId, forecastId, null);
        delete(command);
    }

    private void validateForecastDates(Instant startDate, Instant endDate) {
        if (startDate == null) {
            throw new ValidationException("startDate", "Start date is required");
        }
        if (endDate == null) {
            throw new ValidationException("endDate", "End date is required");
        }
        if (endDate.isBefore(startDate)) {
            throw new ValidationException("endDate", "End date must be after start date");
        }
    }

    private void validateForecastName(String name, String tenantId) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", "Forecast name is required");
        }
        // Check for duplicate name in draft status
        List<Forecast> existingDrafts = forecastRepository.findByTenantIdAndStatus(
                tenantId, Forecast.ForecastStatus.DRAFT);
        boolean duplicateExists = existingDrafts.stream()
                .anyMatch(f -> f.getName().equals(name));
        if (duplicateExists) {
            throw new ConflictException("Forecast", "name", name);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new ValidationException("amount", "Amount is required");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("amount", "Amount must be non-negative");
        }
    }

    private void publishEvents(Forecast forecast) {
        if (!forecast.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(forecast.getDomainEvents());
            forecast.clearDomainEvents();
        }
    }
}
