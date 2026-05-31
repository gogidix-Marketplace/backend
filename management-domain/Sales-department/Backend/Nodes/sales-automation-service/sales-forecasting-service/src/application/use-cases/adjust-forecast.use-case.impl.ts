import { Inject, Injectable } from '@nestjs/common';
import { Forecast } from '../../domain/entities/forecast.entity';
import { RequestContextData } from '../../shared/request-context';
import { ForecastNotFoundError } from '../../shared/errors';
import {
  AdjustForecastUseCase,
  AdjustForecastInput,
  AdjustForecastResult,
} from '../../domain/ports/in/adjust-forecast.use-case';
import { ForecastRepositoryPort } from '../../domain/ports/out/forecast.repository.port';
import { EventPublisherPort } from '../../domain/ports/out/event-publisher.port';
import { ForecastAdjustedEvent } from '../../domain/events/forecast-adjusted.event';

@Injectable()
export class AdjustForecastUseCaseImpl implements AdjustForecastUseCase {
  constructor(
    @Inject('ForecastRepositoryPort')
    private readonly forecastRepository: ForecastRepositoryPort,
    @Inject('EventPublisherPort')
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async execute(
    context: RequestContextData,
    input: AdjustForecastInput,
  ): Promise<AdjustForecastResult> {
    // Find forecast
    const forecast = await this.forecastRepository.findById(
      input.forecastId,
      context.tenantId,
    );

    if (!forecast) {
      throw new ForecastNotFoundError(input.forecastId);
    }

    // Store previous values
    const previousTotal = forecast.totalForecast;
    const previousWeighted = forecast.totalWeightedForecast;

    // Apply adjustment
    const adjustmentType = input.adjustmentType || 'MANUAL';
    forecast.adjustForecast(input.adjustmentFactor, input.reason, context.userId);

    // Save updated forecast
    const updatedForecast = await this.forecastRepository.update(forecast);

    // Publish event
    const event = new ForecastAdjustedEvent({
      tenantId: context.tenantId,
      correlationId: context.correlationId,
      forecastId: updatedForecast.id,
      forecastName: updatedForecast.name,
      adjustedBy: context.userId,
      adjustmentFactor: input.adjustmentFactor,
      previousTotal,
      newTotal: updatedForecast.totalForecast,
      previousWeighted,
      newWeighted: updatedForecast.totalWeightedForecast,
      reason: input.reason,
      version: updatedForecast.version,
      adjustmentType,
    });

    await this.eventPublisher.publishForecastAdjusted(event);

    return {
      forecast: updatedForecast,
      previousTotal,
      newTotal: updatedForecast.totalForecast,
      previousWeighted,
      newWeighted: updatedForecast.totalWeightedForecast,
    };
  }
}





