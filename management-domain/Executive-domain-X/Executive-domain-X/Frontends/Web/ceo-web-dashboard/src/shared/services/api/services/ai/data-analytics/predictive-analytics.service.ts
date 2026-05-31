/**
 * AI Predictive Analytics Service API Client
 *
 * Service: ai-predictive-analytics-service (Port 7036)
 * Base Path: /api/v1/ai/predictive
 *
 * Features:
 * - Run predictions using trained ML models
 * - Get available prediction models
 * - Retrieve prediction results by ID
 * - Batch prediction for multiple data points
 * - Feature importance analysis
 * - Model accuracy metrics
 */

import { aiApiClient } from '../../../client/axios-client'
import type { DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Prediction type enumeration
 */
export type PredictionType =
  | 'REVENUE'
  | 'SALES'
  | 'CHURN'
  | 'DEMAND'
  | 'PRICE'
  | 'INVENTORY'
  | 'CUSTOMER_LIFETIME_VALUE'
  | 'CONVERSION'
  | 'NPS'
  | 'EMPLOYEE_ATTRITION'

/**
 * Prediction status enumeration
 */
export type PredictionStatus = 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED'

/**
 * Available prediction models
 */
export interface PredictionModel {
  id: string
  name: string
  type: PredictionType
  algorithm: 'LINEAR_REGRESSION' | 'RANDOM_FOREST' | 'XGBOOST' | 'NEURAL_NETWORK' | 'LSTM' | 'PROPHET'
  version: string
  status: 'ACTIVE' | 'DEPRECATED' | 'TRAINING'
  accuracy: number
  lastTrainedAt: string
  description: string
  features: string[]
  targetVariable: string
  supportedPredictionTypes: PredictionType[]
}

/**
 * Prediction request
 */
export interface PredictionRequest {
  modelId: string
  predictionType: PredictionType
  inputData: Record<string, number | string | boolean>
  horizon?: number // Prediction horizon in days
  confidenceLevel?: number // 0-100, default 95
  includeFeatureImportance?: boolean
  metadata?: Record<string, any>
}

/**
 * Prediction response
 */
export interface PredictionResponse {
  predictionId: string
  modelId: string
  modelName: string
  predictionType: PredictionType
  status: PredictionStatus
  createdAt: string
  completedAt?: string
  estimatedCompletionTime?: string
}

/**
 * Detailed prediction result
 */
export interface PredictionResult {
  predictionId: string
  modelId: string
  modelName: string
  predictionType: PredictionType
  status: PredictionStatus
  createdAt: string
  completedAt?: string

  // Prediction values
  predictedValue: number
  confidenceInterval: {
    lower: number
    upper: number
  }
  confidence: number // 0-100

  // Time series data (if applicable)
  timeSeriesData?: {
    period: string
    predicted: number
    lowerBound: number
    upperBound: number
  }[]

  // Feature contributions
  featureContributions?: {
    feature: string
    value: number
    impact: number
    direction: 'POSITIVE' | 'NEGATIVE'
  }[]

  // Metadata
  inputData: Record<string, number | string | boolean>
  metadata?: Record<string, any>

  // Error details if failed
  error?: {
    code: string
    message: string
    details?: any
  }
}

/**
 * Feature importance data
 */
export interface FeatureImportance {
  modelId: string
  modelName: string
  predictionType: PredictionType
  features: {
    name: string
    importance: number // 0-1
    rank: number
    description?: string
    category?: string
  }[]
  generatedAt: string
  methodology: 'GAIN' | 'PERMUTATION' | 'SHAP' | 'COEFFICIENT'
}

/**
 * Batch prediction request
 */
export interface BatchPredictionRequest {
  modelId: string
  predictionType: PredictionType
  inputData: Record<string, number | string | boolean>[]
  horizon?: number
  confidenceLevel?: number
  includeFeatureImportance?: boolean
  metadata?: Record<string, any>
}

/**
 * Batch prediction response
 */
export interface BatchPredictionResponse {
  batchId: string
  modelId: string
  predictionType: PredictionType
  status: PredictionStatus
  totalCount: number
  completedCount: number
  failedCount: number
  createdAt: string
  estimatedCompletionTime?: string
  completedAt?: string
  results?: PredictionResult[]
  errors?: {
    index: number
    error: string
  }[]
}

/**
 * Model accuracy metrics
 */
export interface ModelAccuracy {
  modelId: string
  modelName: string
  predictionType: PredictionType
  version: string
  lastUpdated: string

  // Accuracy metrics
  mae: number // Mean Absolute Error
  mse: number // Mean Square Error
  rmse: number // Root Mean Square Error
  mape: number // Mean Absolute Percentage Error
  r2Score: number // R-squared
  accuracy: number // Overall accuracy percentage

  // Validation metrics
  trainingAccuracy: number
  validationAccuracy: number
  testAccuracy: number

  // Performance over time
  accuracyHistory: {
    date: string
    accuracy: number
  }[]

  // Comparison
  benchmark?: {
    model: string
    accuracy: number
  }
}

/**
 * Prediction request options
 */
export interface PredictionOptions {
  includeTimeSeries?: boolean
  includeFeatureImportance?: boolean
  returnFullDetails?: boolean
}

// ============================================================================
// Service Class
// ============================================================================

class PredictiveAnalyticsService {
  private readonly basePath = '/api/v1/ai/predictive'

  /**
   * Run a single prediction
   * Submits a prediction request and returns the prediction ID
   *
   * @param request - Prediction request with model ID and input data
   * @returns Prediction response with prediction ID
   */
  async runPrediction(request: PredictionRequest): Promise<PredictionResponse> {
    try {
      const response = await aiApiClient.post<PredictionResponse>(
        `${this.basePath}/predict`,
        request
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Failed to run prediction')
    }
  }

  /**
   * Get available prediction models
   * Returns all models that can be used for predictions
   *
   * @param predictionType - Optional filter by prediction type
   * @returns Array of available prediction models
   */
  async getAvailableModels(predictionType?: PredictionType): Promise<PredictionModel[]> {
    try {
      const params = predictionType ? { type: predictionType } : {}
      const response = await aiApiClient.get<PredictionModel[]>(
        `${this.basePath}/models`,
        { params }
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Failed to fetch available models')
    }
  }

  /**
   * Get model by ID
   * Returns detailed information about a specific model
   *
   * @param modelId - The model ID
   * @returns Model details
   */
  async getModelById(modelId: string): Promise<PredictionModel> {
    try {
      const response = await aiApiClient.get<PredictionModel>(
        `${this.basePath}/models/${modelId}`
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, `Failed to fetch model ${modelId}`)
    }
  }

  /**
   * Get prediction result by ID
   * Returns the full prediction result including values and confidence intervals
   *
   * @param predictionId - The prediction ID
   * @returns Complete prediction result
   */
  async getPredictionResult(predictionId: string): Promise<PredictionResult> {
    try {
      const response = await aiApiClient.get<PredictionResult>(
        `${this.basePath}/predictions/${predictionId}`
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, `Failed to fetch prediction result ${predictionId}`)
    }
  }

  /**
   * Get feature importance for a model
   * Returns the importance ranking of features used by the model
   *
   * @param modelId - The model ID
   * @returns Feature importance data
   */
  async getFeatureImportance(modelId: string): Promise<FeatureImportance> {
    try {
      const response = await aiApiClient.get<FeatureImportance>(
        `${this.basePath}/features`,
        { params: { modelId } }
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, `Failed to fetch feature importance for model ${modelId}`)
    }
  }

  /**
   * Get all feature importance data
   * Returns feature importance for all available models
   *
   * @param predictionType - Optional filter by prediction type
   * @returns Array of feature importance data
   */
  async getAllFeatureImportance(predictionType?: PredictionType): Promise<FeatureImportance[]> {
    try {
      const params = predictionType ? { type: predictionType } : {}
      const response = await aiApiClient.get<FeatureImportance[]>(
        `${this.basePath}/features`,
        { params }
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Failed to fetch feature importance data')
    }
  }

  /**
   * Run batch prediction
   * Submit multiple prediction requests in a single batch
   *
   * @param request - Batch prediction request with multiple input data points
   * @returns Batch prediction response with batch ID
   */
  async runBatchPrediction(request: BatchPredictionRequest): Promise<BatchPredictionResponse> {
    try {
      const response = await aiApiClient.post<BatchPredictionResponse>(
        `${this.basePath}/batch-predict`,
        request
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Failed to run batch prediction')
    }
  }

  /**
   * Get batch prediction results
   * Returns the status and results of a batch prediction
   *
   * @param batchId - The batch prediction ID
   * @returns Batch prediction results
   */
  async getBatchPredictionResults(batchId: string): Promise<BatchPredictionResponse> {
    try {
      const response = await aiApiClient.get<BatchPredictionResponse>(
        `${this.basePath}/batch-predict/${batchId}`
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, `Failed to fetch batch prediction results ${batchId}`)
    }
  }

  /**
   * Get model accuracy metrics
   * Returns performance metrics for a specific model
   *
   * @param model - The model ID or name
   * @returns Model accuracy metrics
   */
  async getModelAccuracy(model: string): Promise<ModelAccuracy> {
    try {
      const response = await aiApiClient.get<ModelAccuracy>(
        `${this.basePath}/accuracy/${model}`
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, `Failed to fetch accuracy for model ${model}`)
    }
  }

  /**
   * Get accuracy metrics for all models
   * Returns accuracy data for all active models
   *
   * @param predictionType - Optional filter by prediction type
   * @returns Array of model accuracy metrics
   */
  async getAllModelAccuracy(predictionType?: PredictionType): Promise<ModelAccuracy[]> {
    try {
      const params = predictionType ? { type: predictionType } : {}
      const response = await aiApiClient.get<ModelAccuracy[]>(
        `${this.basePath}/accuracy`,
        { params }
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Failed to fetch model accuracy data')
    }
  }

  /**
   * Get prediction history
   * Returns historical predictions for a given type and date range
   *
   * @param predictionType - The prediction type
   * @param dateRange - Date range for historical data
   * @returns Array of historical prediction results
   */
  async getPredictionHistory(
    predictionType: PredictionType,
    dateRange: DateRange
  ): Promise<PredictionResult[]> {
    try {
      const response = await aiApiClient.get<PredictionResult[]>(
        `${this.basePath}/history`,
        { params: { type: predictionType, ...dateRange } }
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Failed to fetch prediction history')
    }
  }

  /**
   * Compare model performance
   * Compares accuracy metrics across multiple models
   *
   * @param modelIds - Array of model IDs to compare
   * @returns Array of model accuracy metrics for comparison
   */
  async compareModels(modelIds: string[]): Promise<ModelAccuracy[]> {
    try {
      const response = await aiApiClient.post<ModelAccuracy[]>(
        `${this.basePath}/models/compare`,
        { modelIds }
      )
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Failed to compare models')
    }
  }

  /**
   * Health check for the predictive analytics service
   *
   * @returns Service health status
   */
  async healthCheck(): Promise<{ status: string; timestamp: string }> {
    try {
      const response = await aiApiClient.get(`${this.basePath}/health`)
      return response.data
    } catch (error: any) {
      throw this.handleError(error, 'Health check failed')
    }
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Format prediction value for display
   *
   * @param value - The prediction value
   * @param predictionType - The type of prediction
   * @returns Formatted string
   */
  formatPredictionValue(value: number, predictionType: PredictionType): string {
    switch (predictionType) {
      case 'REVENUE':
      case 'SALES':
      case 'CUSTOMER_LIFETIME_VALUE':
        return this.formatCurrency(value)
      case 'CHURN':
      case 'CONVERSION':
      case 'NPS':
        return `${value.toFixed(1)}%`
      case 'PRICE':
        return this.formatCurrency(value)
      default:
        return value.toLocaleString()
    }
  }

  /**
   * Format currency value
   *
   * @param amount - The amount to format
   * @param currency - Currency code (default: USD)
   * @returns Formatted currency string
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
   * Get confidence color based on confidence level
   *
   * @param confidence - Confidence level (0-100)
   * @returns Color code
   */
  getConfidenceColor(confidence: number): string {
    if (confidence >= 90) return '#4CAF50' // Green
    if (confidence >= 70) return '#2196F3' // Blue
    if (confidence >= 50) return '#FF9800' // Orange
    return '#F44336' // Red
  }

  /**
   * Get prediction status color
   *
   * @param status - Prediction status
   * @returns Color code
   */
  getStatusColor(status: PredictionStatus): string {
    const colors: Record<PredictionStatus, string> = {
      COMPLETED: '#4CAF50',
      PENDING: '#FF9800',
      PROCESSING: '#2196F3',
      FAILED: '#F44336',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get accuracy color
   *
   * @param accuracy - Accuracy percentage (0-100)
   * @returns Color code
   */
  getAccuracyColor(accuracy: number): string {
    if (accuracy >= 90) return '#4CAF50' // Green
    if (accuracy >= 75) return '#2196F3' // Blue
    if (accuracy >= 60) return '#FF9800' // Orange
    return '#F44336' // Red
  }

  /**
   * Calculate prediction range percentage
   *
   * @param result - Prediction result
   * @returns Range as percentage of predicted value
   */
  calculateRangePercentage(result: PredictionResult): number {
    const range = result.confidenceInterval.upper - result.confidenceInterval.lower
    return (range / result.predictedValue) * 100
  }

  /**
   * Check if prediction is complete
   *
   * @param status - Prediction status
   * @returns True if prediction is complete
   */
  isPredictionComplete(status: PredictionStatus): boolean {
    return status === 'COMPLETED' || status === 'FAILED'
  }

  /**
   * Handle API errors
   *
   * @param error - The error object
   * @param defaultMessage - Default error message
   * @returns Error object with message
   */
  private handleError(error: any, defaultMessage: string): Error {
    if (error.response?.data?.message) {
      return new Error(error.response.data.message)
    }
    if (error.message) {
      return new Error(error.message)
    }
    return new Error(defaultMessage)
  }
}

// Export singleton instance
export const predictiveAnalyticsService = new PredictiveAnalyticsService()
