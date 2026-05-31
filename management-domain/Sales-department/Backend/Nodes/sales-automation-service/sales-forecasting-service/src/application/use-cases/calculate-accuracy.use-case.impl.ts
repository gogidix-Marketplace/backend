import { Inject, Injectable } from '@nestjs/common';
import { ForecastAccuracy } from '../../domain/entities/forecast-accuracy.entity';
import { ForecastAccuracyMetric } from '../../shared/constants';
import { RequestContextData } from '../../shared/request-context';
import { ForecastNotFoundError } from '../../shared/errors';
import {
  CalculateAccuracyUseCase,
  CalculateAccuracyInput,
  CalculateAccuracyResult,
} from '../../domain/ports/in/calculate-accuracy.use-case';
import { ForecastRepositoryPort } from '../../domain/ports/out/forecast.repository.port';
import { ForecastAccuracyRepositoryPort } from '../../domain/ports/out/forecast-accuracy.repository.port';
import { EventPublisherPort } from '../../domain/ports/out/event-publisher.port';
import { ForecastAccuracyCalculatedEvent } from '../../domain/events/forecast-accuracy-calculated.event';

@Injectable()
export class CalculateAccuracyUseCaseImpl implements CalculateAccuracyUseCase {
  private readonly ACCURACY_THRESHOLD = 80;

  constructor(
    @Inject('ForecastRepositoryPort')
    private readonly forecastRepository: ForecastRepositoryPort,
    @Inject('ForecastAccuracyRepositoryPort')
    private readonly accuracyRepository: ForecastAccuracyRepositoryPort,
    @Inject('EventPublisherPort')
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  async execute(
    context: RequestContextData,
    input: CalculateAccuracyInput,
  ): Promise<CalculateAccuracyResult> {
    // Find forecast
    const forecast = await this.forecastRepository.findById(
      input.forecastId,
      context.tenantId,
    );

    if (!forecast) {
      throw new ForecastNotFoundError(input.forecastId);
    }

    // Get previous accuracy for trend calculation
    const previousAccuracy = await this.accuracyRepository.findLatestByForecastId(
      input.forecastId,
      context.tenantId,
    );

    // Calculate metrics
    const metrics = this.calculateAllMetrics(
      forecast.totalWeightedForecast,
      input.actualAmount,
    );

    const overallAccuracy = this.calculateOverallAccuracy(metrics);

    // Create accuracy entity
    const accuracy = new ForecastAccuracy({
      tenantId: context.tenantId,
      forecastId: input.forecastId,
      periodId: input.periodId,
      modelUsed: forecast.model,
      metrics: [
        { metric: ForecastAccuracyMetric.MAPE, value: metrics.mape },
        { metric: ForecastAccuracyMetric.MAE, value: metrics.mae },
        { metric: ForecastAccuracyMetric.RMSE, value: metrics.rmse },
        { metric: ForecastAccuracyMetric.BIAS, value: metrics.bias },
      ],
      overallAccuracy,
      sampleSize: forecast.dataPoints.length,
      comparisonStartDate: input.comparisonStartDate,
      comparisonEndDate: input.comparisonEndDate,
      calculatedBy: context.userId,
    });

    // Calculate trend if we have previous data
    if (previousAccuracy) {
      accuracy.calculateTrend(previousAccuracy.overallAccuracy);
    }

    // Save accuracy
    const savedAccuracy = await this.accuracyRepository.save(accuracy);

    // Check threshold
    const alertThreshold = {
      threshold: this.ACCURACY_THRESHOLD,
      currentAccuracy: overallAccuracy,
      requiresAttention: overallAccuracy < this.ACCURACY_THRESHOLD,
    };

    // Publish event
    const event = new ForecastAccuracyCalculatedEvent({
      tenantId: context.tenantId,
      correlationId: context.correlationId,
      accuracyId: savedAccuracy.id,
      forecastId: forecast.id,
      forecastName: forecast.name,
      modelUsed: forecast.model,
      overallAccuracy,
      accuracyThreshold: this.ACCURACY_THRESHOLD,
      metrics: [
        {
          metric: ForecastAccuracyMetric.MAPE,
          value: metrics.mape,
          threshold: 20,
          withinThreshold: metrics.mape < 20,
        },
        {
          metric: ForecastAccuracyMetric.MAE,
          value: metrics.mae,
          threshold: input.actualAmount * 0.1,
          withinThreshold: metrics.mae < input.actualAmount * 0.1,
        },
        {
          metric: ForecastAccuracyMetric.RMSE,
          value: metrics.rmse,
          threshold: input.actualAmount * 0.15,
          withinThreshold: metrics.rmse < input.actualAmount * 0.15,
        },
        {
          metric: ForecastAccuracyMetric.BIAS,
          value: metrics.bias,
          threshold: 0.1,
          withinThreshold: Math.abs(metrics.bias) < 0.1,
        },
      ],
      sampleSize: forecast.dataPoints.length,
      trend: accuracy.trend,
      previousAccuracy: previousAccuracy?.overallAccuracy,
      calculatedBy: context.userId,
    });

    await this.eventPublisher.publishForecastAccuracyCalculated(event);

    return {
      accuracy: savedAccuracy,
      alertThreshold,
    };
  }

  private calculateAllMetrics(forecasted: number, actual: number) {
    const error = forecasted - actual;
    const absoluteError = Math.abs(error);
    const squaredError = error * error;

    const mape = actual > 0 ? (absoluteError / actual) * 100 : 0;
    const mae = absoluteError;
    const rmse = Math.sqrt(squaredError);
    const bias = actual > 0 ? error / actual : 0;

    return {
      mape: Math.round(mape * 100) / 100,
      mae: Math.round(mae * 100) / 100,
      rmse: Math.round(rmse * 100) / 100,
      bias: Math.round(bias * 1000) / 1000,
    };
  }

  private calculateOverallAccuracy(metrics: {
    mape: number;
    mae: number;
    rmse: number;
    bias: number;
  }): number {
    // Weighted average of metrics (converted to accuracy scores)
    const mapeScore = Math.max(0, 100 - metrics.mape);
    const biasScore = Math.max(0, 100 - Math.abs(metrics.bias) * 100);

    // Give more weight to MAPE (60%) and Bias (40%)
    const overallAccuracy = mapeScore * 0.6 + biasScore * 0.4;

    return Math.round(overallAccuracy * 100) / 100;
  }
}





