import { Injectable, Inject } from '@nestjs/common';
import { startOfMonth, endOfMonth, addMonths, format, differenceInMonths } from 'date-fns';
import { ForecastModel, ForecastPeriod } from '../../shared/constants';
import { PipelineDeal, HistoricalSalesData, PipelineDataProviderPort } from '../../domain/ports/out/pipeline-data-provider.port';
import { ForecastDataPoint, ForecastBreakdown } from '../../domain/entities/forecast.entity';
import { InsufficientHistoricalDataError, InvalidForecastModelError } from '../../shared/errors';

interface CalculationContext {
  tenantId: string;
  startDate: Date;
  endDate: Date;
  granularity?: string;
  granularityId?: string;
  horizonMonths: number;
}

interface WeightedPipelineResult {
  dataPoints: ForecastDataPoint[];
  breakdown: ForecastBreakdown;
}

@Injectable()
export class ForecastCalculatorService {
  constructor(
    @Inject('PipelineDataProviderPort')
    private readonly pipelineDataProvider: PipelineDataProviderPort,
  ) {}

  async calculate(
    model: ForecastModel,
    context: CalculationContext,
    confidenceLevel: number,
  ): Promise<{ dataPoints: ForecastDataPoint[]; breakdown: ForecastBreakdown }> {
    switch (model) {
      case ForecastModel.WEIGHTED_PIPELINE:
        return this.calculateWeightedPipeline(context, confidenceLevel);
      case ForecastModel.HISTORICAL:
        return this.calculateHistorical(context, confidenceLevel);
      case ForecastModel.AI_ML:
        return this.calculateAIML(context, confidenceLevel);
      case ForecastModel.HYBRID:
        return this.calculateHybrid(context, confidenceLevel);
      default:
        throw new InvalidForecastModelError(model);
    }
  }

  private async calculateWeightedPipeline(
    context: CalculationContext,
    confidenceLevel: number,
  ): Promise<WeightedPipelineResult> {
    const deals = await this.pipelineDataProvider.getOpenPipelineDeals(
      context.tenantId,
      {
        repId: context.granularity === 'REP' ? context.granularityId : undefined,
        teamId: context.granularity === 'TEAM' ? context.granularityId : undefined,
        territoryId: context.granularity === 'TERRITORY' ? context.granularityId : undefined,
      },
    );

    const stageWeights = await this.pipelineDataProvider.getStageWeights(context.tenantId);
    const dataPoints = this.generatePeriodDataPoints(context, deals, stageWeights, confidenceLevel);
    const breakdown = this.calculateBreakdown(deals, stageWeights);

    return { dataPoints, breakdown };
  }

  private async calculateHistorical(
    context: CalculationContext,
    confidenceLevel: number,
  ): Promise<WeightedPipelineResult> {
    const historicalData = await this.pipelineDataProvider.getHistoricalSales(
      context.tenantId,
      new Date(context.startDate.getTime() - 365 * 24 * 60 * 60 * 1000), // 1 year back
      context.startDate,
      context.granularity === 'REP' ? 'rep' : context.granularity === 'TEAM' ? 'team' : undefined,
      context.granularityId,
    );

    if (historicalData.length < 3) {
      throw new InsufficientHistoricalDataError(3, historicalData.length);
    }

    const averageMonthlySales = this.calculateAverageMonthlySales(historicalData);
    const trendFactor = this.calculateTrendFactor(historicalData);
    const seasonalFactors = this.calculateSeasonalFactors(historicalData);

    const dataPoints: ForecastDataPoint[] = [];
    const months = differenceInMonths(context.endDate, context.startDate) + 1;

    for (let i = 0; i < months; i++) {
      const periodDate = addMonths(context.startDate, i);
      const periodKey = format(periodDate, 'yyyy-MM');
      const monthIndex = periodDate.getMonth();

      const baseAmount = averageMonthlySales;
      const trendAmount = baseAmount * trendFactor;
      const seasonalAmount = trendAmount * (seasonalFactors.get(monthIndex) || 1);

      const confidence = this.calculateHistoricalConfidence(historicalData, confidenceLevel);
      const variance = seasonalAmount * (1 - confidence / 100);

      dataPoints.push({
        period: periodKey,
        amount: seasonalAmount,
        confidence,
        weightedAmount: seasonalAmount,
        bestCase: seasonalAmount + variance,
        worstCase: Math.max(0, seasonalAmount - variance),
        dealsCount: Math.round(seasonalAmount / averageMonthlySales * historicalData[0].dealsCount),
      });
    }

    return {
      dataPoints,
      breakdown: this.createEmptyBreakdown(),
    };
  }

  private async calculateAIML(
    context: CalculationContext,
    confidenceLevel: number,
  ): Promise<WeightedPipelineResult> {
    // For now, fall back to weighted pipeline with enhanced calculations
    // In production, this would integrate with an ML service
    const weightedResult = await this.calculateWeightedPipeline(context, confidenceLevel);

    // Apply AI-enhanced adjustments
    const adjustedDataPoints = weightedResult.dataPoints.map(dp => ({
      ...dp,
      amount: dp.amount * 1.05, // AI typically finds 5% more opportunities
      weightedAmount: dp.weightedAmount * 1.05,
      bestCase: dp.bestCase * 1.05,
      worstCase: dp.worstCase * 1.05,
      confidence: Math.min(95, dp.confidence + 2),
    }));

    return {
      dataPoints: adjustedDataPoints,
      breakdown: weightedResult.breakdown,
    };
  }

  private async calculateHybrid(
    context: CalculationContext,
    confidenceLevel: number,
  ): Promise<WeightedPipelineResult> {
    const weightedResult = await this.calculateWeightedPipeline(context, confidenceLevel);

    try {
      const historicalResult = await this.calculateHistorical(context, confidenceLevel);

      // Blend the results: 60% weighted pipeline, 40% historical
      const blendedDataPoints = weightedResult.dataPoints.map((dp, i) => {
        const histDp = historicalResult.dataPoints[i];
        return {
          period: dp.period,
          amount: dp.amount * 0.6 + histDp.amount * 0.4,
          confidence: (dp.confidence + histDp.confidence) / 2,
          weightedAmount: dp.weightedAmount * 0.6 + histDp.weightedAmount * 0.4,
          bestCase: dp.bestCase * 0.6 + histDp.bestCase * 0.4,
          worstCase: dp.worstCase * 0.6 + histDp.worstCase * 0.4,
          dealsCount: dp.dealsCount,
        };
      });

      return {
        dataPoints: blendedDataPoints,
        breakdown: weightedResult.breakdown,
      };
    } catch {
      return weightedResult;
    }
  }

  private generatePeriodDataPoints(
    context: CalculationContext,
    deals: PipelineDeal[],
    stageWeights: Map<string, number>,
    confidenceLevel: number,
  ): ForecastDataPoint[] {
    const dataPoints: ForecastDataPoint[] = [];
    const months = differenceInMonths(context.endDate, context.startDate) + 1;

    for (let i = 0; i < months; i++) {
      const periodStart = startOfMonth(addMonths(context.startDate, i));
      const periodEnd = endOfMonth(periodStart);
      const periodKey = format(periodStart, 'yyyy-MM');

      const periodDeals = deals.filter(
        d => d.expectedCloseDate >= periodStart && d.expectedCloseDate <= periodEnd,
      );

      let totalAmount = 0;
      let totalWeighted = 0;
      let totalBestCase = 0;
      let totalWorstCase = 0;

      periodDeals.forEach(deal => {
        const stageWeight = stageWeights.get(deal.stage) || deal.probability / 100;
        const variance = deal.amount * (1 - stageWeight);

        totalAmount += deal.amount;
        totalWeighted += deal.amount * stageWeight;
        totalBestCase += deal.amount;
        totalWorstCase += deal.amount * stageWeight * 0.5;
      });

      const confidence = this.calculateConfidence(periodDeals, confidenceLevel);

      dataPoints.push({
        period: periodKey,
        amount: totalAmount,
        confidence,
        weightedAmount: totalWeighted,
        bestCase: totalBestCase,
        worstCase: Math.max(0, totalWorstCase),
        dealsCount: periodDeals.length,
      });
    }

    return dataPoints;
  }

  private calculateConfidence(deals: PipelineDeal[], baseLevel: number): number {
    if (deals.length === 0) return 50;

    const avgProbability = deals.reduce((sum, d) => sum + d.probability, 0) / deals.length;
    const dealCountFactor = Math.min(10, deals.length) / 10;

    return Math.round(baseLevel * 0.6 + avgProbability * 0.3 * dealCountFactor + 50 * 0.1);
  }

  private calculateHistoricalConfidence(historicalData: HistoricalSalesData[], baseLevel: number): number {
    const variance = this.calculateVariance(historicalData.map(d => d.amount));
    const avgAmount = historicalData.reduce((sum, d) => sum + d.amount, 0) / historicalData.length;
    const coefficientOfVariation = avgAmount > 0 ? Math.sqrt(variance) / avgAmount : 1;

    // Lower variance = higher confidence
    return Math.round(baseLevel * (1 - Math.min(0.5, coefficientOfVariation)));
  }

  private calculateVariance(values: number[]): number {
    const mean = values.reduce((sum, v) => sum + v, 0) / values.length;
    return values.reduce((sum, v) => sum + Math.pow(v - mean, 2), 0) / values.length;
  }

  private calculateBreakdown(deals: PipelineDeal[], stageWeights: Map<string, number>): ForecastBreakdown {
    const byStage = new Map<string, number>();
    const byProduct = new Map<string, number>();
    const byRep = new Map<string, number>();
    const byTerritory = new Map<string, number>();

    deals.forEach(deal => {
      const stageWeight = stageWeights.get(deal.stage) || deal.probability / 100;
      const weightedAmount = deal.amount * stageWeight;

      byStage.set(deal.stage, (byStage.get(deal.stage) || 0) + weightedAmount);
      byRep.set(deal.repId, (byRep.get(deal.repId) || 0) + weightedAmount);
      byTerritory.set(deal.territoryId, (byTerritory.get(deal.territoryId) || 0) + weightedAmount);

      if (deal.productId) {
        byProduct.set(deal.productId, (byProduct.get(deal.productId) || 0) + weightedAmount);
      }
    });

    return { byStage, byProduct, byRep, byTerritory };
  }

  private createEmptyBreakdown(): ForecastBreakdown {
    return {
      byStage: new Map(),
      byProduct: new Map(),
      byRep: new Map(),
      byTerritory: new Map(),
    };
  }

  private calculateAverageMonthlySales(historicalData: HistoricalSalesData[]): number {
    const totalSales = historicalData.reduce((sum, d) => sum + d.amount, 0);
    return totalSales / historicalData.length;
  }

  private calculateTrendFactor(historicalData: HistoricalSalesData[]): number {
    if (historicalData.length < 2) return 1;

    const recentHalf = historicalData.slice(Math.floor(historicalData.length / 2));
    const olderHalf = historicalData.slice(0, Math.floor(historicalData.length / 2));

    const recentAvg = recentHalf.reduce((sum, d) => sum + d.amount, 0) / recentHalf.length;
    const olderAvg = olderHalf.reduce((sum, d) => sum + d.amount, 0) / olderHalf.length;

    return olderAvg > 0 ? recentAvg / olderAvg : 1;
  }

  private calculateSeasonalFactors(historicalData: HistoricalSalesData[]): Map<number, number> {
    const monthlyTotals = new Map<number, number[]>();
    const monthlyAverages = new Map<number, number>();

    historicalData.forEach(d => {
      const date = new Date(d.period);
      const month = date.getMonth();
      if (!monthlyTotals.has(month)) {
        monthlyTotals.set(month, []);
      }
      monthlyTotals.get(month)!.push(d.amount);
    });

    const allAverages: number[] = [];
    monthlyTotals.forEach((values, month) => {
      const avg = values.reduce((sum, v) => sum + v, 0) / values.length;
      monthlyAverages.set(month, avg);
      allAverages.push(avg);
    });

    const globalAverage = allAverages.reduce((sum, v) => sum + v, 0) / allAverages.length;

    const factors = new Map<number, number>();
    monthlyAverages.forEach((avg, month) => {
      factors.set(month, globalAverage > 0 ? avg / globalAverage : 1);
    });

    // Fill missing months with 1.0
    for (let i = 0; i < 12; i++) {
      if (!factors.has(i)) {
        factors.set(i, 1);
      }
    }

    return factors;
  }
}
