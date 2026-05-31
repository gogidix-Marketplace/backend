import { Injectable } from '@nestjs/common';
import { Forecast, ForecastDataPoint } from '../../../domain/entities/forecast.entity';
import { ForecastAccuracy } from '../../../domain/entities/forecast-accuracy.entity';
import {
  ForecastResponseDto,
  GenerateForecastResponseDto,
  AdjustForecastResponseDto,
  ListForecastsResponseDto,
  CalculateAccuracyResponseDto,
  ForecastDataPointResponseDto,
  ForecastBreakdownResponseDto,
  ForecastAccuracyResponseDto,
} from '../../../application/dto/response.dto';
import { GenerateForecastResult } from '../../../domain/ports/in/generate-forecast.use-case';
import { AdjustForecastResult } from '../../../domain/ports/in/adjust-forecast.use-case';
import { ListForecastsResult } from '../../../domain/ports/in/list-forecasts.use-case';
import { CalculateAccuracyResult } from '../../../domain/ports/in/calculate-accuracy.use-case';

@Injectable()
export class ForecastMapper {
  toDataPointResponse(dataPoint: ForecastDataPoint): ForecastDataPointResponseDto {
    return {
      period: dataPoint.period,
      amount: dataPoint.amount,
      confidence: dataPoint.confidence,
      weightedAmount: dataPoint.weightedAmount,
      bestCase: dataPoint.bestCase,
      worstCase: dataPoint.worstCase,
      dealsCount: dataPoint.dealsCount,
    };
  }

  toBreakdownResponse(breakdown: {
    byStage: Map<string, number>;
    byProduct: Map<string, number>;
    byRep: Map<string, number>;
    byTerritory: Map<string, number>;
  }): ForecastBreakdownResponseDto {
    return {
      byStage: Object.fromEntries(breakdown.byStage),
      byProduct: Object.fromEntries(breakdown.byProduct),
      byRep: Object.fromEntries(breakdown.byRep),
      byTerritory: Object.fromEntries(breakdown.byTerritory),
    };
  }

  toResponse(forecast: Forecast): ForecastResponseDto {
    return {
      id: forecast.id,
      tenantId: forecast.tenantId,
      name: forecast.name,
      description: forecast.description,
      model: forecast.model,
      period: forecast.period,
      status: forecast.status,
      granularity: forecast.granularity,
      granularityId: forecast.granularityId,
      dataPoints: forecast.dataPoints.map(dp => this.toDataPointResponse(dp)),
      breakdown: this.toBreakdownResponse(forecast.breakdown),
      totalForecast: forecast.totalForecast,
      totalWeightedForecast: forecast.totalWeightedForecast,
      currency: forecast.currency,
      startDate: forecast.startDate,
      endDate: forecast.endDate,
      generatedAt: forecast.generatedAt,
      generatedBy: forecast.generatedBy,
      lastUpdated: forecast.lastUpdated,
      lastUpdatedBy: forecast.lastUpdatedBy,
      version: forecast.version,
      previousForecastId: forecast.previousForecastId,
      confidenceLevel: forecast.confidenceLevel,
      metadata: Object.fromEntries(forecast.metadata),
    };
  }

  toGenerateResponse(result: GenerateForecastResult): GenerateForecastResponseDto {
    return {
      forecast: this.toResponse(result.forecast),
      accuracy: result.accuracy,
    };
  }

  toAdjustResponse(result: AdjustForecastResult): AdjustForecastResponseDto {
    return {
      forecast: this.toResponse(result.forecast),
      previousTotal: result.previousTotal,
      newTotal: result.newTotal,
      previousWeighted: result.previousWeighted,
      newWeighted: result.newWeighted,
    };
  }

  toListResponse(result: ListForecastsResult): ListForecastsResponseDto {
    return {
      forecasts: result.forecasts.map(f => this.toResponse(f)),
      total: result.total,
      page: result.page,
      limit: result.limit,
      totalPages: result.totalPages,
    };
  }

  toAccuracyResponseDto(accuracy: ForecastAccuracy): ForecastAccuracyResponseDto {
    return {
      id: accuracy.id,
      forecastId: accuracy.forecastId,
      modelUsed: accuracy.modelUsed,
      metrics: Array.from(accuracy.metrics.values()),
      overallAccuracy: accuracy.overallAccuracy,
      sampleSize: accuracy.sampleSize,
      comparisonStartDate: accuracy.comparisonStartDate,
      comparisonEndDate: accuracy.comparisonEndDate,
      calculatedAt: accuracy.calculatedAt,
      trend: accuracy.trend,
      previousAccuracy: accuracy.previousAccuracy,
      notes: accuracy.notes,
    };
  }

  toAccuracyResponse(result: CalculateAccuracyResult): CalculateAccuracyResponseDto {
    return {
      accuracy: this.toAccuracyResponseDto(result.accuracy),
      alertThreshold: result.alertThreshold,
    };
  }
}
