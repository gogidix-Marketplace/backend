/**
 * AI Time Series Forecasting Service
 *
 * Service: time-series-forecasting-service (Port 7037)
 * Base Path: /api/v1/ai/forecasting/time-series
 *
 * Features:
 * - Advanced time series forecasting with multiple models
 * - ARIMA, Prophet, LSTM, and Ensemble model support
 * - Confidence interval calculations
 * - Seasonality detection and analysis
 * - Model comparison and accuracy metrics
 * - Automatic model selection based on data characteristics
 */

import { aiApiClient } from '../../../client/axios-client'

// ============================================================================
// Types
// ============================================================================

/**
 * Available forecasting models
 */
export type ForecastingModel =
  | 'ARIMA'
  | 'SARIMA'
  | 'PROPHET'
  | 'LSTM'
  | 'XGBOOST'
  | 'ENSEMBLE'
  | 'AUTO'

/**
 * Frequency of time series data
 */
export type DataFrequency =
  | 'HOURLY'
  | 'DAILY'
  | 'WEEKLY'
  | 'MONTHLY'
  | 'QUARTERLY'
  | 'YEARLY'

/**
 * Seasonality types
 */
export type SeasonalityType = 'NONE' | 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY' | 'YEARLY'

/**
 * Forecast status
 */
export type ForecastStatus = 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED'

/**
 * Time series data point
 */
export interface TimeSeriesDataPoint {
  timestamp: string
  value: number
  metadata?: Record<string, any>
}

/**
 * Request for generating a time series forecast
 */
export interface TimeSeriesForecastRequest {
  /** Historical time series data */
  data: TimeSeriesDataPoint[]
  /** Forecasting model to use (AUTO for automatic selection) */
  model?: ForecastingModel
  /** Number of periods to forecast */
  forecastPeriods: number
  /** Frequency of the data */
  frequency?: DataFrequency
  /** Confidence level for intervals (0-100, default 95) */
  confidenceLevel?: number
  /** Include seasonality in forecast */
  includeSeasonality?: boolean
  /** External regressors/factors */
  regressors?: ExternalRegressor[]
  /** Custom model parameters */
  modelParameters?: ModelParameters
}

/**
 * External regressor for multivariate forecasting
 */
export interface ExternalRegressor {
  name: string
  data: Array<{ timestamp: string; value: number }>
}

/**
 * Custom model parameters
 */
export interface ModelParameters {
  /** ARIMA: (p,d,q) parameters */
  arima?: {
    p?: number
    d?: number
    q?: number
    seasonalP?: number
    seasonalD?: number
    seasonalQ?: number
  }
  /** Prophet parameters */
  prophet?: {
    changepointPriorScale?: number
    seasonalityPriorScale?: number
    holidaysPriorScale?: number
    weeklySeasonality?: boolean
    yearlySeasonality?: boolean
    dailySeasonality?: boolean
  }
  /** LSTM parameters */
  lstm?: {
    epochs?: number
    batchSize?: number
    hiddenUnits?: number
    lookbackWindow?: number
  }
  /** XGBoost parameters */
  xgboost?: {
    nEstimators?: number
    maxDepth?: number
    learningRate?: number
    subsample?: number
  }
}

/**
 * Response for time series forecast
 */
export interface TimeSeriesForecastResponse {
  /** Unique forecast identifier */
  forecastId: string
  /** Timestamp when forecast was generated */
  generatedAt: string
  /** Model used for forecasting */
  model: ForecastingModel
  /** Forecast status */
  status: ForecastStatus
  /** Input data summary */
  inputDataSummary: DataSummary
  /** Forecast results */
  forecast: ForecastResult
  /** Model accuracy metrics */
  accuracy?: ModelAccuracy
  /** Seasonality analysis */
  seasonality?: SeasonalityAnalysis
  /** Diagnostics information */
  diagnostics?: ForecastDiagnostics
}

/**
 * Summary of input data
 */
export interface DataSummary {
  dataPoints: number
  startDate: string
  endDate: string
  frequency: DataFrequency
  mean: number
  stdDev: number
  min: number
  max: number
  hasMissingValues: boolean
  hasOutliers: boolean
}

/**
 * Forecast result with intervals
 */
export interface ForecastResult {
  /** Forecasted data points with confidence intervals */
  forecasts: ForecastDataPoint[]
  /** Overall forecast statistics */
  summary: ForecastSummary
}

/**
 * Single forecast data point
 */
export interface ForecastDataPoint {
  /** Timestamp for the forecast */
  timestamp: string
  /** Forecasted value */
  forecast: number
  /** Lower confidence bound */
  lowerBound: number
  /** Upper confidence bound */
  upperBound: number
  /** Prediction interval percentage */
  interval: ForecastInterval
}

/**
 * Confidence interval
 */
export interface ForecastInterval {
  /** Confidence level (e.g., 95 for 95%) */
  level: number
  /** Lower bound value */
  lower: number
  /** Upper bound value */
  upper: number
}

/**
 * Forecast summary statistics
 */
export interface ForecastSummary {
  /** Total forecasted sum */
  total: number
  /** Average forecasted value */
  average: number
  /** Minimum forecasted value */
  min: number
  /** Maximum forecasted value */
  max: number
  /** Trend direction */
  trend: 'UPWARD' | 'DOWNWARD' | 'STABLE' | 'VOLATILE'
  /** Growth rate percentage */
  growthRate?: number
}

/**
 * Model accuracy metrics
 */
export interface ModelAccuracy {
  /** Mean Absolute Percentage Error */
  mape: number
  /** Root Mean Square Error */
  rmse: number
  /** Mean Absolute Error */
  mae: number
  /** R-squared score */
  r2: number
  /** Symmetric Mean Absolute Percentage Error */
  smape: number
  /** Mean Absolute Scaled Error */
  mase: number
}

/**
 * Seasonality analysis results
 */
export interface SeasonalityAnalysis {
  /** Whether seasonality is detected */
  hasSeasonality: boolean
  /** Detected seasonality type */
  seasonalityType: SeasonalityType
  /** Seasonal strength (0-1) */
  seasonalStrength: number
  /** Seasonal periods */
  periods: SeasonalityPattern[]
  /** Decomposition results */
  decomposition?: TimeSeriesDecomposition
}

/**
 * Seasonality pattern
 */
export interface SeasonalityPattern {
  /** Period type (e.g., WEEKLY, MONTHLY) */
  period: SeasonalityType
  /** Strength of the pattern (0-1) */
  strength: number
  /** Peak periods */
  peaks: string[]
  /** Low periods */
  troughs: string[]
}

/**
 * Time series decomposition components
 */
export interface TimeSeriesDecomposition {
  trend: TimeSeriesDataPoint[]
  seasonal: TimeSeriesDataPoint[]
  residual: TimeSeriesDataPoint[]
  observed: TimeSeriesDataPoint[]
}

/**
 * Forecast diagnostics
 */
export interface ForecastDiagnostics {
  /** Residuals analysis */
  residuals: ResidualAnalysis
  /** Model fit quality */
  fitQuality: 'EXCELLENT' | 'GOOD' | 'FAIR' | 'POOR'
  /** Warnings */
  warnings: DiagnosticWarning[]
}

/**
 * Residuals analysis
 */
export interface ResidualAnalysis {
  /** Mean of residuals */
  mean: number
  /** Standard deviation of residuals */
  stdDev: number
  /** Ljung-Box test p-value */
  ljungBoxPValue?: number
  /** Are residuals normally distributed? */
  isNormal: boolean
  /** Is there autocorrelation? */
  hasAutocorrelation: boolean
}

/**
 * Diagnostic warning
 */
export interface DiagnosticWarning {
  severity: 'INFO' | 'WARNING' | 'ERROR'
  code: string
  message: string
  suggestion?: string
}

/**
 * Available forecast model info
 */
export interface ForecastModel {
  /** Model identifier */
  id: ForecastingModel
  /** Model display name */
  name: string
  /** Model description */
  description: string
  /** Supported data frequencies */
  supportedFrequencies: DataFrequency[]
  /** Handles seasonality */
  supportsSeasonality: boolean
  /** Supports external regressors */
  supportsRegressors: boolean
  /** Typical training time */
  typicalTrainingTime: string
  /** Best use cases */
  bestFor: string[]
}

/**
 * Model comparison result
 */
export interface ModelComparison {
  /** Comparison ID */
  comparisonId: string
  /** Timestamp of comparison */
  timestamp: string
  /** Models compared */
  models: ForecastingModel[]
  /** Comparison metrics */
  metrics: ComparisonMetrics[]
  /** Recommended model */
  recommendedModel: ForecastingModel
  /** Recommendation reason */
  recommendationReason: string
}

/**
 * Comparison metrics for each model
 */
export interface ComparisonMetrics {
  /** Model identifier */
  model: ForecastingModel
  /** Accuracy score */
  accuracy: ModelAccuracy
  /** Training time in seconds */
  trainingTime: number
  /** Inference time in seconds */
  inferenceTime: number
  /** Overall score (0-100) */
  overallScore: number
  /** Rank in comparison */
  rank: number
}

/**
 * Model accuracy response
 */
export interface ModelAccuracyResponse {
  /** Model identifier */
  model: ForecastingModel
  /** Accuracy metrics */
  metrics: ModelAccuracy
  /** Historical accuracy over time */
  history: AccuracyHistoryEntry[]
  /** Average accuracy across all forecasts */
  averageAccuracy: number
  /** Number of forecasts evaluated */
  sampleSize: number
}

/**
 * Historical accuracy entry
 */
export interface AccuracyHistoryEntry {
  /** Forecast ID */
  forecastId: string
  /** Date of forecast */
  date: string
  /** MAPE score */
  mape: number
  /** RMSE score */
  rmse: number
  /** Data points evaluated */
  dataPoints: number
}

// ============================================================================
// Service Class
// ============================================================================

/**
 * AI Time Series Forecasting Service
 *
 * Provides methods for generating, retrieving, and analyzing time series forecasts
 * using various machine learning models.
 */
class TimeSeriesForecastingService {
  private readonly basePath = '/api/v1/ai/forecasting/time-series'

  /**
   * Generate a new time series forecast
   *
   * @param request - Forecast request with data and parameters
   * @returns Promise with forecast response containing ID and results
   *
   * @example
   * ```typescript
   * const forecast = await timeSeriesForecastingService.generateForecast({
   *   data: [{ timestamp: '2024-01-01', value: 100 }],
   *   forecastPeriods: 12,
   *   model: 'AUTO',
   *   confidenceLevel: 95
   * })
   * ```
   */
  async generateForecast(request: TimeSeriesForecastRequest): Promise<TimeSeriesForecastResponse> {
    try {
      const response = await aiApiClient.post<TimeSeriesForecastResponse>(
        `${this.basePath}/forecast`,
        request
      )
      return response.data
    } catch (error) {
      throw this.handleError(error, 'Failed to generate forecast')
    }
  }

  /**
   * Get forecast result by ID
   *
   * @param forecastId - Unique forecast identifier
   * @returns Promise with forecast details
   *
   * @example
   * ```typescript
   * const forecast = await timeSeriesForecastingService.getForecast('forecast-123')
   * ```
   */
  async getForecast(forecastId: string): Promise<TimeSeriesForecastResponse> {
    try {
      const response = await aiApiClient.get<TimeSeriesForecastResponse>(
        `${this.basePath}/forecast/${forecastId}`
      )
      return response.data
    } catch (error) {
      throw this.handleError(error, `Failed to retrieve forecast ${forecastId}`)
    }
  }

  /**
   * Get list of available forecasting models
   *
   * @returns Promise with array of available models
   *
   * @example
   * ```typescript
   * const models = await timeSeriesForecastingService.getAvailableModels()
   * ```
   */
  async getAvailableModels(): Promise<ForecastModel[]> {
    try {
      const response = await aiApiClient.get<ForecastModel[]>(`${this.basePath}/models`)
      return response.data
    } catch (error) {
      throw this.handleError(error, 'Failed to retrieve available models')
    }
  }

  /**
   * Get accuracy metrics for a specific model
   *
   * @param model - Model identifier
   * @returns Promise with model accuracy information
   *
   * @example
   * ```typescript
   * const accuracy = await timeSeriesForecastingService.getModelAccuracy('LSTM')
   * ```
   */
  async getModelAccuracy(model: ForecastingModel): Promise<ModelAccuracyResponse> {
    try {
      const response = await aiApiClient.get<ModelAccuracyResponse>(
        `${this.basePath}/accuracy/${model}`
      )
      return response.data
    } catch (error) {
      throw this.handleError(error, `Failed to retrieve accuracy for model ${model}`)
    }
  }

  /**
   * Compare multiple forecasting models
   *
   * @param request - Comparison request with data and models to compare
   * @returns Promise with comparison results
   *
   * @example
   * ```typescript
   * const comparison = await timeSeriesForecastingService.compareModels({
   *   data: timeSeriesData,
   *   models: ['ARIMA', 'PROPHET', 'LSTM'],
   *   forecastPeriods: 12
   * })
   * ```
   */
  async compareModels(request: ModelComparisonRequest): Promise<ModelComparison> {
    try {
      const response = await aiApiClient.post<ModelComparison>(
        `${this.basePath}/compare`,
        request
      )
      return response.data
    } catch (error) {
      throw this.handleError(error, 'Failed to compare models')
    }
  }

  /**
   * Get seasonality analysis for a KPI
   *
   * @param kpi - KPI identifier to analyze
   * @param options - Optional analysis parameters
   * @returns Promise with seasonality analysis results
   *
   * @example
   * ```typescript
   * const seasonality = await timeSeriesForecastingService.getSeasonality('revenue')
   * ```
   */
  async getSeasonality(
    kpi: string,
    options?: SeasonalityAnalysisOptions
  ): Promise<SeasonalityAnalysis> {
    try {
      const params = options ? { ...options } : {}
      const response = await aiApiClient.get<SeasonalityAnalysis>(
        `${this.basePath}/seasonality/${kpi}`,
        { params }
      )
      return response.data
    } catch (error) {
      throw this.handleError(error, `Failed to retrieve seasonality for ${kpi}`)
    }
  }

  /**
   * Delete a forecast
   *
   * @param forecastId - Forecast identifier to delete
   * @returns Promise resolving when deleted
   */
  async deleteForecast(forecastId: string): Promise<void> {
    try {
      await aiApiClient.delete(`${this.basePath}/forecast/${forecastId}`)
    } catch (error) {
      throw this.handleError(error, `Failed to delete forecast ${forecastId}`)
    }
  }

  // ========================================================================
  // Convenience Methods
  // ========================================================================

  /**
   * Generate a quick forecast with default parameters
   *
   * @param data - Time series data
   * @param periods - Number of periods to forecast (default: 12)
   * @returns Promise with forecast response
   */
  async quickForecast(
    data: TimeSeriesDataPoint[],
    periods = 12
  ): Promise<TimeSeriesForecastResponse> {
    return this.generateForecast({
      data,
      forecastPeriods: periods,
      model: 'AUTO',
      confidenceLevel: 95,
      includeSeasonality: true,
    })
  }

  /**
   * Check if a forecast is complete
   *
   * @param forecastId - Forecast identifier
   * @returns Promise with status
   */
  async getForecastStatus(forecastId: string): Promise<ForecastStatus> {
    const forecast = await this.getForecast(forecastId)
    return forecast.status
  }

  /**
   * Wait for forecast completion
   *
   * @param forecastId - Forecast identifier
   * @param options - Polling options
   * @returns Promise with completed forecast
   */
  async waitForForecast(
    forecastId: string,
    options: { intervalMs?: number; timeoutMs?: number } = {}
  ): Promise<TimeSeriesForecastResponse> {
    const { intervalMs = 1000, timeoutMs = 60000 } = options
    const startTime = Date.now()

    while (Date.now() - startTime < timeoutMs) {
      const forecast = await this.getForecast(forecastId)

      if (forecast.status === 'COMPLETED') {
        return forecast
      }

      if (forecast.status === 'FAILED') {
        throw new Error(`Forecast ${forecastId} failed`)
      }

      await this.delay(intervalMs)
    }

    throw new Error(`Forecast ${forecastId} timed out after ${timeoutMs}ms`)
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Format forecast value for display
   */
  formatForecastValue(value: number, decimals = 2): string {
    return value.toFixed(decimals)
  }

  /**
   * Get trend icon/direction
   */
  getTrendIcon(trend: ForecastSummary['trend']): string {
    switch (trend) {
      case 'UPWARD':
        return '↑'
      case 'DOWNWARD':
        return '↓'
      case 'STABLE':
        return '→'
      case 'VOLATILE':
        return '〰'
      default:
        return '?'
    }
  }

  /**
   * Get trend color
   */
  getTrendColor(trend: ForecastSummary['trend']): string {
    switch (trend) {
      case 'UPWARD':
        return '#4CAF50' // Green
      case 'DOWNWARD':
        return '#F44336' // Red
      case 'STABLE':
        return '#9E9E9E' // Gray
      case 'VOLATILE':
        return '#FF9800' // Orange
      default:
        return '#000000'
    }
  }

  /**
   * Get accuracy grade
   */
  getAccuracyGrade(mape: number): string {
    if (mape < 5) return 'A+'
    if (mape < 10) return 'A'
    if (mape < 15) return 'B'
    if (mape < 20) return 'C'
    if (mape < 25) return 'D'
    return 'F'
  }

  /**
   * Get confidence interval width percentage
   */
  getIntervalWidthPercentage(forecast: ForecastDataPoint): number {
    const width = forecast.upperBound - forecast.lowerBound
    return (width / forecast.forecast) * 100
  }

  /**
   * Check if forecast is within acceptable range
   */
  isWithinRange(actual: number, forecast: ForecastDataPoint, tolerance = 0.1): boolean {
    const lower = forecast.lowerBound * (1 - tolerance)
    const upper = forecast.upperBound * (1 + tolerance)
    return actual >= lower && actual <= upper
  }

  /**
   * Delay helper for polling
   */
  private delay(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms))
  }

  /**
   * Handle API errors
   */
  private handleError(error: any, defaultMessage: string): Error {
    if (error?.response?.data?.message) {
      return new Error(error.response.data.message)
    }
    if (error?.message) {
      return new Error(error.message)
    }
    return new Error(defaultMessage)
  }
}

// ============================================================================
// Additional Types
// ============================================================================

/**
 * Model comparison request
 */
export interface ModelComparisonRequest {
  /** Time series data */
  data: TimeSeriesDataPoint[]
  /** Models to compare */
  models: ForecastingModel[]
  /** Number of periods to forecast */
  forecastPeriods: number
  /** Data frequency */
  frequency?: DataFrequency
  /** Confidence level */
  confidenceLevel?: number
}

/**
 * Seasonality analysis options
 */
export interface SeasonalityAnalysisOptions {
  /** Start date for analysis */
  startDate?: string
  /** End date for analysis */
  endDate?: string
  /** Maximum seasonality periods to detect */
  maxPeriods?: number
  /** Include decomposition in response */
  includeDecomposition?: boolean
}

// Export singleton instance
export const timeSeriesForecastingService = new TimeSeriesForecastingService()
