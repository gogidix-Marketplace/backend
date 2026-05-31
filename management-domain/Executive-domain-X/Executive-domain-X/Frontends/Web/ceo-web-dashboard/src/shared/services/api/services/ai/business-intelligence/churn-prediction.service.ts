/**
 * AI Churn Prediction Service
 *
 * Service: ai-churn-prediction-service (Port 7001)
 * Base Path: /api/v1/ai/churn
 *
 * Features:
 * - Customer churn prediction with probability scores
 * - Employee churn risk analysis
 * - Churn factor identification and analysis
 * - Risk-based segmentation
 * - Historical churn trend analysis
 * - Batch prediction capabilities
 */

import { aiApiClient } from '../../../client/axios-client'
import type { DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

export type ChurnEntityType = 'CUSTOMER' | 'EMPLOYEE'

export type ChurnRiskLevel = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

export type ChurnPredictionModel = 'LOGISTIC_REGRESSION' | 'RANDOM_FOREST' | 'XGBOOST' | 'NEURAL_NETWORK' | 'ENSEMBLE'

export type ChurnFactor =
  | 'ENGAGEMENT'
  | 'SATISFACTION'
  | 'USAGE_FREQUENCY'
  | 'PURCHASE_VALUE'
  | 'SUPPORT_TICKETS'
  | 'CONTRACT_RENEWAL'
  | 'PAYMENT_DELAY'
  | 'COMPETITOR_ACTIVITY'
  | 'PRICING'
  | 'PRODUCT_QUALITY'
  | 'SALARY'
  | 'WORK_LIFE_BALANCE'
  | 'CAREER_GROWTH'
  | 'MANAGEMENT'
  | 'WORK_ENVIRONMENT'

export interface ChurnPredictionRequest {
  entityType: ChurnEntityType
  entityIds: string[]
  model?: ChurnPredictionModel
  includeFactors?: boolean
  timeHorizon?: number // Days into future, default 90
  domain?: string // Filter by domain for customer churn
  department?: string // Filter by department for employee churn
}

export interface ChurnPredictionResponse {
  predictionId: string
  generatedAt: string
  entityType: ChurnEntityType
  model: ChurnPredictionModel
  timeHorizon: number
  totalPredicted: number
  highRiskCount: number

  // Individual predictions
  predictions: ChurnPrediction[]

  // Summary statistics
  summary: ChurnSummary

  // Risk distribution
  riskDistribution: RiskDistribution
}

export interface ChurnPrediction {
  entityId: string
  entityName: string
  churnProbability: number // 0-100
  riskLevel: ChurnRiskLevel
  confidence: number // 0-100
  predictedChurnDate?: string
  factors?: ChurnFactorDetail[]
  recommendations?: string[]
}

export interface ChurnFactorDetail {
  factor: ChurnFactor
  impact: number // 0-100, higher means more impact on churn
  value: number // Current value
  status: 'POSITIVE' | 'NEGATIVE' | 'NEUTRAL'
  description: string
}

export interface ChurnSummary {
  averageChurnProbability: number
  medianChurnProbability: number
  highRiskPercentage: number
  mediumRiskPercentage: number
  lowRiskPercentage: number
  predictedChurnRate: number
  estimatedRevenueAtRisk?: number // For customers
}

export interface RiskDistribution {
  low: number // Count
  medium: number
  high: number
  critical: number
}

export interface ChurnFactors {
  entityType: ChurnEntityType
  generatedAt: string
  timeRange: DateRange

  // Overall factors
  globalFactors: GlobalChurnFactor[]

  // Factor importance ranking
  factorImportance: FactorImportance[]

  // Factor trends
  factorTrends: FactorTrend[]
}

export interface GlobalChurnFactor {
  factor: ChurnFactor
  correlation: number // -1 to 1
  impact: 'HIGH' | 'MEDIUM' | 'LOW'
  trend: 'INCREASING' | 'DECREASING' | 'STABLE'
  affectedEntities: number
}

export interface FactorImportance {
  factor: ChurnFactor
  importance: number // 0-100
  description: string
  actionable: boolean
}

export interface FactorTrend {
  factor: ChurnFactor
  period: string
  impact: number
  change: number // Change from previous period
}

export interface ChurnSegment {
  segmentId: string
  name: string
  description: string
  riskLevel: ChurnRiskLevel
  entityCount: number
  avgChurnProbability: number
  definingCharacteristics: string[]
  keyFactors: ChurnFactor[]
  recommendedActions: string[]
}

export interface ChurnSegmentsResponse {
  generatedAt: string
  entityType: ChurnEntityType
  totalSegments: number
  segments: ChurnSegment[]
}

export interface ChurnTrend {
  period: string
  churnRate: number // Percentage
  churnCount: number
  totalEntities: number
  highRiskCount: number
  avgChurnProbability: number
  revenueImpact?: number
}

export interface ChurnTrendsResponse {
  entityType: ChurnEntityType
  startDate: string
  endDate: string
  period: 'daily' | 'weekly' | 'monthly' | 'quarterly'
  trends: ChurnTrend[]

  // Trend analysis
  trendDirection: 'INCREASING' | 'DECREASING' | 'STABLE'
  avgChurnRate: number
  peakChurnRate: number
  peakPeriod: string
}

export interface BatchPredictionRequest {
  entityType: ChurnEntityType
  filters?: BatchPredictionFilters
  model?: ChurnPredictionModel
  includeFactors?: boolean
  batchSize?: number
  timeHorizon?: number
}

export interface BatchPredictionFilters {
  domain?: string
  department?: string
  registrationDateRange?: DateRange
  lastActivityDateRange?: DateRange
  valueRange?: {
    min?: number
    max?: number
  }
  riskLevels?: ChurnRiskLevel[]
  customFilters?: Record<string, any>
}

export interface BatchPredictionResponse {
  batchId: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'
  entityType: ChurnEntityType
  totalEntities: number
  processedEntities: number
  progress: number // 0-100
  startedAt: string
  completedAt?: string

  // Results (available when completed)
  predictions?: ChurnPrediction[]
  summary?: ChurnSummary

  // Download link for large batches
  downloadUrl?: string
  expiresAt?: string
}

// ============================================================================
// Service Class
// ============================================================================

class ChurnPredictionService {
  private readonly basePath = '/api/v1/ai/churn'

  /**
   * Predict churn for specific customers or employees
   * Main method for individual churn predictions
   */
  async predictChurn(request: ChurnPredictionRequest): Promise<ChurnPredictionResponse> {
    const response = await aiApiClient.post<ChurnPredictionResponse>(
      `${this.basePath}/predict`,
      request
    )
    return response.data
  }

  /**
   * Get quick churn prediction for a single entity
   * Convenience method for single entity lookup
   */
  async getQuickPrediction(
    entityType: ChurnEntityType,
    entityId: string
  ): Promise<ChurnPrediction> {
    const response = await this.predictChurn({
      entityType,
      entityIds: [entityId],
      includeFactors: true,
      timeHorizon: 90,
    })
    return response.predictions[0]
  }

  /**
   * Get customer churn predictions
   */
  async predictCustomerChurn(
    customerIds: string[],
    domain?: string
  ): Promise<ChurnPredictionResponse> {
    return this.predictChurn({
      entityType: 'CUSTOMER',
      entityIds: customerIds,
      domain,
      includeFactors: true,
    })
  }

  /**
   * Get employee churn predictions
   */
  async predictEmployeeChurn(
    employeeIds: string[],
    department?: string
  ): Promise<ChurnPredictionResponse> {
    return this.predictChurn({
      entityType: 'EMPLOYEE',
      entityIds: employeeIds,
      department,
      includeFactors: true,
    })
  }

  /**
   * Get churn factors/analysis
   * Understand what factors are driving churn
   */
  async getChurnFactors(
    entityType: ChurnEntityType,
    dateRange?: DateRange
  ): Promise<ChurnFactors> {
    const response = await aiApiClient.get<ChurnFactors>(
      `${this.basePath}/factors`,
      {
        params: {
          entityType,
          startDate: dateRange?.startDate,
          endDate: dateRange?.endDate,
        },
      }
    )
    return response.data
  }

  /**
   * Get churn risk segments
   * Group entities by risk characteristics
   */
  async getChurnSegments(entityType: ChurnEntityType): Promise<ChurnSegmentsResponse> {
    const response = await aiApiClient.get<ChurnSegmentsResponse>(
      `${this.basePath}/segments`,
      {
        params: { entityType },
      }
    )
    return response.data
  }

  /**
   * Get churn trends over time
   * Analyze historical churn patterns
   */
  async getChurnTrends(
    entityType: ChurnEntityType,
    dateRange: DateRange,
    period: 'daily' | 'weekly' | 'monthly' | 'quarterly' = 'monthly'
  ): Promise<ChurnTrendsResponse> {
    const response = await aiApiClient.get<ChurnTrendsResponse>(
      `${this.basePath}/trends`,
      {
        params: {
          entityType,
          startDate: dateRange.startDate,
          endDate: dateRange.endDate,
          period,
        },
      }
    )
    return response.data
  }

  /**
   * Batch churn prediction
   * Process predictions for multiple entities with filters
   */
  async batchPredictChurn(request: BatchPredictionRequest): Promise<BatchPredictionResponse> {
    const response = await aiApiClient.post<BatchPredictionResponse>(
      `${this.basePath}/batch-predict`,
      request
    )
    return response.data
  }

  /**
   * Get batch prediction status
   * Check progress of an async batch prediction
   */
  async getBatchPredictionStatus(batchId: string): Promise<BatchPredictionResponse> {
    const response = await aiApiClient.get<BatchPredictionResponse>(
      `${this.basePath}/batch-predict/${batchId}`
    )
    return response.data
  }

  /**
   * Get high-risk entities
   * Quick lookup for entities at high churn risk
   */
  async getHighRiskEntities(
    entityType: ChurnEntityType,
    limit = 50,
    domain?: string,
    department?: string
  ): Promise<ChurnPrediction[]> {
    const response = await aiApiClient.get<{ predictions: ChurnPrediction[] }>(
      `${this.basePath}/high-risk`,
      {
        params: {
          entityType,
          limit,
          domain,
          department,
        },
      }
    )
    return response.data.predictions
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get risk level from probability
   */
  getRiskLevelFromProbability(probability: number): ChurnRiskLevel {
    if (probability >= 75) return 'CRITICAL'
    if (probability >= 50) return 'HIGH'
    if (probability >= 25) return 'MEDIUM'
    return 'LOW'
  }

  /**
   * Get risk color for UI display
   */
  getRiskColor(riskLevel: ChurnRiskLevel): string {
    switch (riskLevel) {
      case 'CRITICAL':
        return '#D32F2F' // Red
      case 'HIGH':
        return '#F57C00' // Orange
      case 'MEDIUM':
        return '#FBC02D' // Yellow
      case 'LOW':
        return '#388E3C' // Green
      default:
        return '#9E9E9E' // Gray
    }
  }

  /**
   * Get risk color from probability
   */
  getProbabilityColor(probability: number): string {
    return this.getRiskColor(this.getRiskLevelFromProbability(probability))
  }

  /**
   * Format probability for display
   */
  formatProbability(probability: number): string {
    return `${probability.toFixed(1)}%`
  }

  /**
   * Format revenue impact
   */
  formatRevenueImpact(amount: number, currency = 'USD'): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency,
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount)
  }

  /**
   * Calculate retention score (inverse of churn probability)
   */
  calculateRetentionScore(churnProbability: number): number {
    return Math.max(0, 100 - churnProbability)
  }

  /**
   * Get recommended actions based on risk level
   */
  getRecommendedActions(riskLevel: ChurnRiskLevel): string[] {
    switch (riskLevel) {
      case 'CRITICAL':
        return [
          'Immediate outreach required',
          'Assign account manager',
          'Offer retention incentive',
          'Schedule executive review',
        ]
      case 'HIGH':
        return [
          'Schedule check-in call',
          'Review recent interactions',
          'Identify pain points',
          'Provide personalized offer',
        ]
      case 'MEDIUM':
        return [
          'Monitor engagement',
          'Send targeted communication',
          'Gather feedback',
          'Review usage patterns',
        ]
      case 'LOW':
        return [
          'Maintain regular contact',
          'Provide value-add content',
          'Monitor for changes',
          'Encourage advocacy',
        ]
      default:
        return []
    }
  }

  /**
   * Sort predictions by churn probability
   */
  sortByRisk(predictions: ChurnPrediction[], order: 'desc' | 'asc' = 'desc'): ChurnPrediction[] {
    return [...predictions].sort((a, b) =>
      order === 'desc'
        ? b.churnProbability - a.churnProbability
        : a.churnProbability - b.churnProbability
    )
  }

  /**
   * Filter predictions by risk level
   */
  filterByRiskLevel(predictions: ChurnPrediction[], riskLevels: ChurnRiskLevel[]): ChurnPrediction[] {
    return predictions.filter((p) => riskLevels.includes(p.riskLevel))
  }
}

// Export singleton instance
export const churnPredictionService = new ChurnPredictionService()

