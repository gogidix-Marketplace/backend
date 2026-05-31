package com.gogidix.finance.conversion.application.service;

import com.gogidix.finance.conversion.domain.event.ConversionCompletedEvent;
import com.gogidix.finance.conversion.domain.model.ConversionRate;
import com.gogidix.finance.conversion.domain.model.CurrencyConversion;
import com.gogidix.finance.conversion.domain.port.in.CurrencyConversionCommand;
import com.gogidix.finance.conversion.domain.port.out.EventPublisher;
import com.gogidix.finance.conversion.domain.port.out.ExchangeRateProvider;
import com.gogidix.finance.conversion.domain.repository.ConversionRateRepository;
import com.gogidix.finance.conversion.domain.repository.CurrencyConversionRepository;
import com.gogidix.finance.conversion.shared.exception.NotFoundException;
import com.gogidix.finance.conversion.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Currency Conversion Service
 * Main service for handling currency conversion operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConversionService {

    private final CurrencyConversionRepository conversionRepository;
    private final ConversionRateRepository rateRepository;
    private final EventPublisher eventPublisher;
    private final ExchangeRateProvider exchangeRateProvider;

    private static final BigDecimal DEFAULT_FEE_PERCENTAGE = new BigDecimal("0.01"); // 1%
    private static final BigDecimal MIN_FEE_AMOUNT = new BigDecimal("0.50");

    /**
     * Converts an amount from one currency to another
     */
    @Transactional
    public CurrencyConversion convertAmount(CurrencyConversionCommand.ConvertAmountCommand command) {
        log.info("Converting amount: {} {} to {} for tenant: {}",
                command.getAmount(), command.getFromCurrency(), command.getToCurrency(), command.getTenantId());

        String correlationId = command.getCorrelationId() != null
                ? command.getCorrelationId()
                : UUID.randomUUID().toString();

        CurrencyConversion conversion = CurrencyConversion.create(
                command.getTenantId(),
                command.getRequestedBy(),
                command.getAmount(),
                command.getFromCurrency(),
                command.getToCurrency(),
                correlationId
        );

        conversion.validateForProcessing();

        try {
            // Get the exchange rate
            BigDecimal rate = getExchangeRate(
                    command.getFromCurrency(),
                    command.getToCurrency(),
                    command.getProvider()
            );

            String provider = command.getProvider() != null
                    ? command.getProvider()
                    : exchangeRateProvider.getProviderName();

            // Complete the conversion
            conversion.complete(rate, provider);

            // Apply fee if configured
            Boolean applyFee = command.getApplyFee();
            if (applyFee != null && applyFee) {
                BigDecimal feePercentage = command.getFeePercentage() != null
                        ? command.getFeePercentage()
                        : DEFAULT_FEE_PERCENTAGE;
                BigDecimal fee = calculateFee(conversion.getConvertedAmount(), feePercentage);
                conversion.setFee(fee);
            }

            // Save and publish events
            CurrencyConversion savedConversion = conversionRepository.save(conversion);
            publishEvents(savedConversion);

            log.info("Conversion completed: {} = {} {} (rate: {})",
                    savedConversion.getAmount(),
                    savedConversion.getConvertedAmount(),
                    savedConversion.getToCurrency(),
                    savedConversion.getRate());

            return savedConversion;

        } catch (Exception e) {
            conversion.fail(e.getMessage());
            conversionRepository.save(conversion);
            throw new ValidationException("Conversion failed: " + e.getMessage());
        }
    }

    /**
     * Performs batch currency conversions
     */
    @Transactional
    public BatchConversionResult batchConvert(CurrencyConversionCommand.BatchConvertCommand command) {
        log.info("Batch converting {} items for tenant: {}",
                command.getConversions().size(), command.getTenantId());

        String correlationId = command.getCorrelationId() != null
                ? command.getCorrelationId()
                : UUID.randomUUID().toString();

        List<CurrencyConversion> conversions = new ArrayList<>();
        List<CurrencyConversion> failedConversions = new ArrayList<>();

        for (CurrencyConversionCommand.BatchConvertCommand.ConversionItem item : command.getConversions()) {
            try {
                CurrencyConversionCommand.ConvertAmountCommand convertCommand =
                        new CurrencyConversionCommand.ConvertAmountCommand(
                                command.getTenantId(),
                                command.getRequestedBy(),
                                item.getAmount(),
                                item.getFromCurrency(),
                                item.getToCurrency(),
                                correlationId,
                                command.getProvider(),
                                command.getApplyFee(),
                                command.getFeePercentage()
                        );

                CurrencyConversion conversion = convertAmount(convertCommand);
                conversions.add(conversion);

            } catch (Exception e) {
                log.error("Failed to convert item: {} {} to {} - {}",
                        item.getAmount(), item.getFromCurrency(), item.getToCurrency(), e.getMessage());
                failedConversions.add(createFailedConversion(item, e.getMessage(), command));
            }
        }

        // Publish batch completion event
        if (!conversions.isEmpty() && eventPublisher.isReady()) {
            ConversionCompletedEvent batchEvent = ConversionCompletedEvent.createBatchEvent(
                    command.getTenantId(),
                    command.getRequestedBy(),
                    conversions.size(),
                    "BATCH_CONVERSION_COMPLETED"
            );
            eventPublisher.publish(batchEvent);
        }

        return new BatchConversionResult(conversions, failedConversions);
    }

    /**
     * Reverses a conversion
     */
    @Transactional
    public void reverseConversion(CurrencyConversionCommand.ReverseConversionCommand command) {
        log.info("Reversing conversion: {} for tenant: {}",
                command.getConversionId(), command.getTenantId());

        CurrencyConversion conversion = conversionRepository
                .findByConversionIdAndTenantId(command.getConversionId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversion", command.getConversionId()));

        if (!conversion.isReversible()) {
            throw new ValidationException("Conversion cannot be reversed. " +
                    "Reversal window is 5 minutes from conversion time.");
        }

        conversion.reverse(command.getReason() != null ? command.getReason() : "User requested reversal");
        conversionRepository.save(conversion);

        log.info("Conversion reversed: {}", command.getConversionId());
    }

    /**
     * Refreshes a cached exchange rate
     */
    @Transactional
    @CacheEvict(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public ConversionRate refreshRate(CurrencyConversionCommand.RefreshRateCommand command) {
        log.info("Refreshing rate: {} to {} for tenant: {}",
                command.getFromCurrency(), command.getToCurrency(), command.getTenantId());

        String rateKey = command.getFromCurrency() + "-" + command.getToCurrency();
        ConversionRate existingRate = rateRepository.findByRateKey(rateKey).orElse(null);

        BigDecimal newRate = getExchangeRate(
                command.getFromCurrency(),
                command.getToCurrency(),
                command.getProvider()
        );

        String provider = command.getProvider() != null
                ? command.getProvider()
                : exchangeRateProvider.getProviderName();

        ConversionRate rate;
        if (existingRate != null) {
            existingRate.refresh(newRate, provider);
            rate = rateRepository.save(existingRate);
        } else {
            rate = ConversionRate.create(
                    command.getFromCurrency(),
                    command.getToCurrency(),
                    newRate,
                    provider
            );
            rate = rateRepository.save(rate);
        }

        log.info("Rate refreshed: {} = {}", rateKey, newRate);
        return rate;
    }

    /**
     * Invalidates a cached rate
     */
    @Transactional
    @CacheEvict(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public void invalidateRateCache(CurrencyConversionCommand.InvalidateRateCacheCommand command) {
        log.info("Invalidating rate cache: {} to {} for tenant: {}",
                command.getFromCurrency(), command.getToCurrency(), command.getTenantId());

        String rateKey = command.getFromCurrency() + "-" + command.getToCurrency();
        rateRepository.deleteByRateKey(rateKey);

        log.info("Rate cache invalidated: {}", rateKey);
    }

    /**
     * Gets the current exchange rate (with caching)
     */
    @Cacheable(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency, String preferredProvider) {
        // Try to get from cache first
        String rateKey = fromCurrency + "-" + toCurrency;
        ConversionRate cachedRate = rateRepository.findByRateKey(rateKey).orElse(null);

        if (cachedRate != null && cachedRate.isValid()) {
            cachedRate.incrementHitCount();
            rateRepository.save(cachedRate);
            log.debug("Cache hit for rate: {}", rateKey);
            return cachedRate.getEffectiveRate();
        }

        // Fetch from external provider
        if (!exchangeRateProvider.isAvailable()) {
            throw new ValidationException("Exchange rate provider is not available");
        }

        BigDecimal rate = exchangeRateProvider.getExchangeRate(fromCurrency, toCurrency);

        // Cache the rate
        String provider = preferredProvider != null
                ? preferredProvider
                : exchangeRateProvider.getProviderName();

        ConversionRate newRate = ConversionRate.create(fromCurrency, toCurrency, rate, provider);
        rateRepository.save(newRate);

        log.info("Fetched and cached rate: {} = {} from {}", rateKey, rate, provider);
        return rate;
    }

    /**
     * Calculates the conversion fee
     */
    private BigDecimal calculateFee(BigDecimal convertedAmount, BigDecimal feePercentage) {
        BigDecimal calculatedFee = convertedAmount.multiply(feePercentage)
                .setScale(2, java.math.RoundingMode.HALF_UP);

        // Apply minimum fee if applicable
        if (calculatedFee.compareTo(MIN_FEE_AMOUNT) < 0) {
            return MIN_FEE_AMOUNT;
        }

        return calculatedFee;
    }

    /**
     * Creates a failed conversion record
     */
    private CurrencyConversion createFailedConversion(
            CurrencyConversionCommand.BatchConvertCommand.ConversionItem item,
            String reason,
            CurrencyConversionCommand.BatchConvertCommand command) {

        CurrencyConversion conversion = CurrencyConversion.create(
                command.getTenantId(),
                command.getRequestedBy(),
                item.getAmount(),
                item.getFromCurrency(),
                item.getToCurrency(),
                command.getCorrelationId()
        );

        conversion.fail(reason);
        return conversion;
    }

    /**
     * Publishes domain events
     */
    private void publishEvents(CurrencyConversion conversion) {
        if (!conversion.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll((List<Object>) (List<?>) conversion.getDomainEvents());
            conversion.clearDomainEvents();
        }
    }

    /**
     * Batch conversion result holder
     */
    public record BatchConversionResult(
            List<CurrencyConversion> successfulConversions,
            List<CurrencyConversion> failedConversions
    ) {
        public int getTotalCount() {
            return successfulConversions.size() + failedConversions.size();
        }

        public boolean hasFailures() {
            return !failedConversions.isEmpty();
        }
    }
}
