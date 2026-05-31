/**
 * AI Sales Forecasting Service
 *
 * Service: ai-sales-forecasting-service (Port 7005)
 * Base Path: /api/v1/ai/forecasting
 *
 * Features:
 * - Revenue forecasting with confidence intervals
 * - Multi-period forecasting (quarterly, annual)
 * - Scenario modeling (best, expected, worst case)
 * - Driver-based forecasting
 * - Cross-domain revenue aggregation
 */

import { aiApiClient } from '../../../client/axios-client'
import type { TimePeriod, DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

export type ForecastHorizon = '1M' | '3M' | '6M' | '1Y' | '2Y' | '5Y'

export type ForecastScenario = 'BEST' | 'EXPECTED' | 'WORST'

export type ForecastModel = 'ARIMA' | 'PROPHET' | 'LSTM' | 'ENSEMBLE'

export interface ForecastRequest {
  horizon: ForecastHorizon
  scenario?: ForecastScenario
  model?: ForecastModel
  domains?: string[] // Domains to include in forecast
  region?: string // Filter by region
  confidenceLevel?: number // 0-100, default 95
}

export interface SalesForecastResponse {
  forecastId: string
  generatedAt: string
  horizon: ForecastHorizon
  model: ForecastModel
  confidenceLevel: number

  // Summary
  summary: ForecastSummary

  // Time series data
  timeSeries: ForecastDataPoint[]

  // Breakdown by domain
  domainBreakdown: DomainForecast[]

  // Breakdown by region
  regionBreakdown: RegionForecast[]

  // Drivers
  drivers: ForecastDriver[]

  // Accuracy metrics
  accuracy: ForecastAccuracy
}

export interface ForecastSummary {
  currentRevenue: number
  forecastedRevenue: number
  growthRate: number
  confidenceInterval: {
    lower: number
    upper: number
  }
  bestCase: number
  worstCase: number
}

export interface ForecastDataPoint {
  period: string
  forecast: number
  lowerBound: number
  upperBound: number
  actual?: number // For historical comparison
}

export interface DomainForecast {
  domain: string
  domainName: string
  currentRevenue: number
  forecastedRevenue: number
  growthRate: number
  contribution: number // Percentage of total
  trend: 'UP' | 'DOWN' | 'STABLE'
}

export interface RegionForecast {
  region: string
  regionName: string
  currentRevenue: number
  forecastedRevenue: number
  growthRate: number
  contribution: number
}

export interface ForecastDriver {
  name: string
  impact: 'POSITIVE' | 'NEGATIVE' | 'NEUTRAL'
  importance: number // 0-100
  description: string
  currentValue: number
  projectedValue: number
}

export interface ForecastAccuracy {
  mape: number // Mean Absolute Percentage Error
  rmse: number // Root Mean Square Error
  mae: number // Mean Absolute Error
  lastActual: number
  lastForecast: number
  lastAccuracy: number // Percentage
}

export interface HistoricalForecastRequest {
  startDate: string
  endDate: string
  domain?: string
  region?: string
}

export interface ScenarioAnalysisResponse {
  baseCase: ForecastSummary
  bestCase: ForecastSummary
  worstCase: ForecastSummary
  keyFactors: {
    factor: string
    bestCaseImpact: number
    worstCaseImpact: number
  }[]
}

// ============================================================================
// Service Class
// ============================================================================

class SalesForecastingService {
  private readonly basePath = '/api/v1/ai/forecasting'

  /**
   * Generate sales forecast
   * Main method for getting revenue predictions
   */
  async generateForecast(request: ForecastRequest): Promise<SalesForecastResponse> {
    const response = await aiApiClient.post<SalesForecastResponse>(
      `${this.basePath}/generate`,
      request
    )
    return response.data
  }

  /**
   * Get quick forecast (default parameters)
   * Convenience method for dashboard widgets
   */
  async getQuickForecast(horizon: ForecastHorizon = '6M'): Promise<SalesForecastResponse> {
    return this.generateForecast({
      horizon,
      scenario: 'EXPECTED',
      model: 'ENSEMBLE',
      confidenceLevel: 95,
    })
  }

  /**
   * Get forecast by domain
   */
  async getDomainForecast(
    domain: string,
    horizon: ForecastHorizon = '6M'
  ): Promise<SalesForecastResponse> {
    return this.generateForecast({
      horizon,
      domains: [domain],
      confidenceLevel: 95,
    })
  }

  /**
   * Get forecast by region
   */
  async getRegionalForecast(
    region: string,
    horizon: ForecastHorizon = '6M'
  ): Promise<SalesForecastResponse> {
    return this.generateForecast({
      horizon,
      region,
      confidenceLevel: 95,
    })
  }

  /**
   * Get scenario analysis
   * Compare best, expected, and worst case scenarios
   */
  async getScenarioAnalysis(
    horizon: ForecastHorizon = '1Y'
  ): Promise<ScenarioAnalysisResponse> {
    const response = await aiApiClient.post<ScenarioAnalysisResponse>(
      `${this.basePath}/scenarios`,
      { horizon }
    )
    return response.data
  }

  /**
   * Get historical forecast accuracy
   */
  async getHistoricalForecastAccuracy(
    request: HistoricalForecastRequest
  ): Promise<ForecastAccuracy[]> {
    const response = await aiApiClient.get<ForecastAccuracy[]>(
      `${this.basePath}/accuracy`,
      { params: request }
    )
    return response.data
  }

  /**
   * Get forecast drivers
   * Understand what factors are driving the forecast
   */
  async getForecastDrivers(horizon: ForecastHorizon = '6M'): Promise<ForecastDriver[]> {
    const response = await aiApiClient.get<ForecastDriver[]>(
      `${this.basePath}/drivers`,
      { params: { horizon } }
    )
    return response.data
  }

  /**
   * Compare forecasts across domains
   */
  async compareDomainForecasts(
    domains: string[],
    horizon: ForecastHorizon = '6M'
  ): Promise<DomainForecast[]> {
    const response = await aiApiClient.post<DomainForecast[]>(
      `${this.basePath}/compare`,
      { domains, horizon }
    )
    return response.data
  }

  /**
   * Get forecast trend
   * Compare current forecast to previous periods
   */
  async getForecastTrend(): Promise<{
    currentPeriod: ForecastSummary
    previousPeriod: ForecastSummary
    change: {
      revenue: number
      percentage: number
    }
  }> {
    const response = await aiApiClient.get(`${this.basePath}/trend`)
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Format currency for forecast display
   */
  formatCurrency(amount: number, currency = 'USD'): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency,
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount)
  }

  /**
   * Get confidence color based on level
   */
  getConfidenceColor(confidence: number): string {
    if (confidence >= 90) return '#4CAF50' // Green
    if (confidence >= 70) return '#2196F3' // Blue
    if (confidence >= 50) return '#FF9800' // Orange
    return '#F44336' // Red
  }

  /**
   * Get growth rate color
   */
  getGrowthRateColor(rate: number): string {
    if (rate > 0) return '#4CAF50' // Green
    if (rate === 0) return '#9E9E9E' // Gray
    return '#F44336' // Red
  }

  /**
   * Calculate forecast range width
   */
  getForecastRangeWidth(forecast: ForecastSummary): number {
    return ((forecast.confidenceInterval.upper - forecast.confidenceInterval.lower) /
            forecast.forecastedRevenue) * 100
  }
}

// Export singleton instance
export const salesForecastingService = new SalesForecastingService()

