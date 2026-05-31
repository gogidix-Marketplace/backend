import { Inject, Injectable } from '@nestjs/common';
import { ForecastModel, ForecastPeriod, ForecastGranularity, DEFAULT_FORECAST_HORIZON_MONTHS } from '../../shared/constants';
import { Forecast } from '../../domain/entities/forecast.entity';
import { RequestContextData } from '../../shared/request-context';
import {
  GenerateForecastUseCase,
  GenerateForecastInput,
  GenerateForecastResult,
} from '../../domain/ports/in/generate-forecast.use-case';
import { ForecastRepositoryPort } from '../../domain/ports/out/forecast.repository.port';
import { EventPublisherPort } from '../../domain/ports/out/event-publisher.port';
import { ForecastGeneratedEvent } from '../../domain/events/forecast-generated.event';
import { ForecastCalculatorService } from '../services/forecast-calculator.service';

@Injectable()
export class GenerateForecastUseCaseImpl implements GenerateForecastUseCase {
  constructor(
    @Inject('ForecastRepositoryPort')
    private readonly forecastRepository: ForecastRepositoryPort,
    @Inject('EventPublisherPort')
    private readonly eventPublisher: EventPublisherPort,
    private readonly calculator: ForecastCalculatorService,
  ) {}

  async execute(
    context: RequestContextData,
    input: GenerateForecastInput,
  ): Promise<GenerateForecastResult> {
    // Validate dates
    if (input.endDate <= input.startDate) {
      throw new Error('End date must be after start date');
    }

    const horizonMonths = input.horizonMonths || DEFAULT_FORECAST_HORIZON_MONTHS;

    // Check if forecast name already exists
    const nameExists = await this.forecastRepository.existsByName(
      input.name,
      context.tenantId,
    );
    if (nameExists) {
      throw new Error(`Forecast with name '${input.name}' already exists`);
    }

    // Calculate forecast data points
    const { dataPoints, breakdown } = await this.calculator.calculate(
      input.model as ForecastModel,
      {
        tenantId: context.tenantId,
        startDate: input.startDate,
        endDate: input.endDate,
        granularity: input.granularity,
        granularityId: input.granularityId,
        horizonMonths,
      },
      input.confidenceLevel || 85,
    );

    // Create forecast entity
    const forecast = new Forecast({
      tenantId: context.tenantId,
      name: input.name,
      description: input.description,
      model: input.model as ForecastModel,
      period: input.period as ForecastPeriod,
      granularity: input.granularity as ForecastGranularity,
      granularityId: input.granularityId,
      dataPoints,
      breakdown,
      currency: input.currency,
      startDate: input.startDate,
      endDate: input.endDate,
      generatedBy: context.userId,
      confidenceLevel: input.confidenceLevel || 85,
    });

    // Save forecast
    const savedForecast = await this.forecastRepository.save(forecast);

    // Publish event
    const event = new ForecastGeneratedEvent({
      tenantId: context.tenantId,
      correlationId: context.correlationId,
      forecastId: savedForecast.id,
      forecastName: savedForecast.name,
      model: savedForecast.model,
      period: savedForecast.period,
      granularity: savedForecast.granularity,
      granularityId: savedForecast.granularityId,
      totalForecast: savedForecast.totalForecast,
      weightedForecast: savedForecast.totalWeightedForecast,
      currency: savedForecast.currency,
      startDate: savedForecast.startDate,
      endDate: savedForecast.endDate,
      generatedBy: savedForecast.generatedBy,
      dataPointsCount: savedForecast.dataPoints.length,
      confidenceLevel: savedForecast.confidenceLevel,
    });

    await this.eventPublisher.publishForecastGenerated(event);

    return {
      forecast: savedForecast,
      accuracy: this.estimateAccuracy(savedForecast),
    };
  }

  private estimateAccuracy(forecast: Forecast): {
    overallAccuracy: number;
    mape: number;
    mae: number;
    rmse: number;
  } {
    // Initial accuracy estimate based on confidence level
    const baseAccuracy = forecast.confidenceLevel;

    return {
      overallAccuracy: baseAccuracy,
      mape: 100 - baseAccuracy,
      mae: forecast.totalWeightedForecast * (1 - baseAccuracy / 100),
      rmse: Math.sqrt(Math.pow(forecast.totalWeightedForecast * (1 - baseAccuracy / 100), 2)),
    };
  }
}





