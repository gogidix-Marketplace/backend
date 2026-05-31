/**
 * AI Market Basket Analysis Service
 *
 * Service: ai-market-basket-analysis-service (Port 7003)
 * Base Path: /api/v1/ai/market-basket
 *
 * Features:
 * - Product association discovery
 * - Association rule mining (Apriori, FP-Growth)
 * - Cross-sell and up-sell recommendations
 * - Basket-based product suggestions
 * - Market segmentation by purchase patterns
 * - Real-time recommendation engine
 */

import { aiApiClient } from '../../../client/axios-client'
import type { DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

export type AssociationAlgorithm = 'APRIORI' | 'FP_GROWTH' | 'ECLAT' | 'HYBRID'

export type RecommendationStrategy = 'CONFIDENCE' | 'LIFT' | 'AFFINITY' | 'PROFITABILITY'

/**
 * Product association representation
 */
export interface ProductAssociation {
  productId: string
  productName: string
  category: string
  associatedProducts: AssociatedProduct[]
  metrics: AssociationMetrics
  lastUpdated: string
}

/**
 * Associated product details
 */
export interface AssociatedProduct {
  productId: string
  productName: string
  category: string
  confidence: number // 0-100
  lift: number
  support: number // 0-100
  affinityScore: number // 0-100
  avgBasketPosition: number
  coOccurrenceCount: number
}

/**
 * Association metrics
 */
export interface AssociationMetrics {
  totalTransactions: number
  totalBaskets: number
  uniqueProducts: number
  avgBasketSize: number
  avgBasketValue: number
  strongestAssociation: {
    lift: number
    confidence: number
    products: [string, string]
  }
  algorithmUsed: AssociationAlgorithm
}

/**
 * Association rule
 */
export interface AssociationRule {
  ruleId: string
  antecedent: string[] // Products that lead to...
  consequent: string[] // ...these products
  support: number // 0-100
  confidence: number // 0-100
  lift: number
  conviction: number
  leverage: number
  ruleQuality: 'HIGH' | 'MEDIUM' | 'LOW'
  profitImpact?: number
  recommendationScore: number // 0-100
  createdAt: string
  validUntil?: string
}

/**
 * Market basket analysis result
 */
export interface MarketBasketAnalysis {
  analysisId: string
  generatedAt: string
  parameters: AnalysisRequest
  status: 'COMPLETED' | 'IN_PROGRESS' | 'FAILED'
  results?: AnalysisResults
  error?: string
}

/**
 * Analysis results
 */
export interface AnalysisResults {
  ruleSet: RuleSet
  topAssociations: ProductAssociation[]
  categoryAffinity: CategoryAffinity[]
  insights: AnalysisInsight[]
  recommendations: ActionableRecommendation[]
}

/**
 * Rule set
 */
export interface RuleSet {
  totalRules: number
  highQualityRules: number
  mediumQualityRules: number
  lowQualityRules: number
  rules: AssociationRule[]
  algorithm: AssociationAlgorithm
  minSupport: number
  minConfidence: number
  minLift: number
}

/**
 * Category affinity
 */
export interface CategoryAffinity {
  primaryCategory: string
  associatedCategories: {
    category: string
    affinityScore: number // 0-100
    lift: number
    support: number
  }[]
  basketCount: number
}

/**
 * Analysis insight
 */
export interface AnalysisInsight {
  type: 'OPPORTUNITY' | 'RISK' | 'PATTERN' | 'ANOMALY'
  title: string
  description: string
  impact: 'HIGH' | 'MEDIUM' | 'LOW'
  metrics?: Record<string, number>
  actionable: boolean
}

/**
 * Actionable recommendation
 */
export interface ActionableRecommendation {
  recommendationId: string
  type: 'CROSS_SELL' | 'UP_SELL' | 'BUNDLE' | 'PROMOTION' | 'PLACEMENT'
  priority: 'HIGH' | 'MEDIUM' | 'LOW'
  products: string[]
  description: string
  expectedImpact: {
    revenueIncrease?: number
    conversionRate?: number
    basketSizeIncrease?: number
  }
  confidence: number // 0-100
  effort: 'LOW' | 'MEDIUM' | 'HIGH'
}

/**
 * Basket recommendation
 */
export interface BasketRecommendation {
  basketId: string
  currentProducts: BasketProduct[]
  recommendations: ProductRecommendation[]
  crossSellOpportunities: CrossSellOpportunity[]
  bundleSuggestions: BundleSuggestion[]
  upsellCandidates: UpsellCandidate[]
  totalPotentialValue: number
  confidence: number
  generatedAt: string
}

/**
 * Product in basket
 */
export interface BasketProduct {
  productId: string
  productName: string
  category: string
  price: number
  quantity: number
}

/**
 * Product recommendation
 */
export interface ProductRecommendation {
  productId: string
  productName: string
  category: string
  price: number
  reason: string
  confidence: number // 0-100
  lift: number
  expectedProbability: number // 0-100
}

/**
 * Cross-sell opportunity
 */
export interface CrossSellOpportunity {
  triggerProduct: string
  recommendedProduct: string
  confidence: number
  lift: number
  expectedRevenue: number
  reason: string
}

/**
 * Bundle suggestion
 */
export interface BundleSuggestion {
  bundleId: string
  products: string[]
  discountSuggestion?: number
  expectedLift: number
  bundleFrequency: number
  averageBundleValue: number
}

/**
 * Upsell candidate
 */
export interface UpsellCandidate {
  currentProductId: string
  suggestedProductId: string
  suggestedProductName: string
  priceDifference: number
  valueProposition: string
  confidence: number
}

/**
 * Analysis request
 */
export interface AnalysisRequest {
  dateRange?: DateRange
  minSupport?: number // 0-100, default 5
  minConfidence?: number // 0-100, default 50
  minLift?: number // default 1.2
  algorithm?: AssociationAlgorithm
  maxRules?: number // default 1000
  categories?: string[] // Filter by categories
  regions?: string[] // Filter by regions
  segments?: string[] // Filter by customer segments
  includeProfitability?: boolean
}

/**
 * Analysis response
 */
export interface AnalysisResponse {
  analysisId: string
  status: 'ACCEPTED' | 'RUNNING' | 'COMPLETED' | 'FAILED'
  estimatedCompletion?: string
  resultsUrl?: string
}

// ============================================================================
// Service Class
// ============================================================================

class MarketBasketAnalysisService {
  private readonly basePath = '/api/v1/ai/market-basket'

  /**
   * Get all product associations
   * Returns comprehensive product association data
   */
  async getAssociations(params?: {
    category?: string
    minLift?: number
    minConfidence?: number
    limit?: number
  }): Promise<ProductAssociation[]> {
    const response = await aiApiClient.get<ProductAssociation[]>(
      `${this.basePath}/associations`,
      { params }
    )
    return response.data
  }

  /**
   * Get associations for a specific product
   * Returns products commonly bought together with the given product
   */
  async getProductAssociations(
    productId: string,
    params?: {
      limit?: number
      minLift?: number
      minConfidence?: number
    }
  ): Promise<ProductAssociation> {
    const response = await aiApiClient.get<ProductAssociation>(
      `${this.basePath}/associations/${productId}`,
      { params }
    )
    return response.data
  }

  /**
   * Run market basket analysis
   * Triggers a new analysis job with specified parameters
   */
  async runAnalysis(request: AnalysisRequest): Promise<AnalysisResponse> {
    const response = await aiApiClient.post<AnalysisResponse>(
      `${this.basePath}/analyze`,
      request
    )
    return response.data
  }

  /**
   * Get analysis results by ID
   */
  async getAnalysisResults(analysisId: string): Promise<MarketBasketAnalysis> {
    const response = await aiApiClient.get<MarketBasketAnalysis>(
      `${this.basePath}/analyze/${analysisId}`
    )
    return response.data
  }

  /**
   * Get basket recommendations
   * Returns product recommendations for a specific basket
   */
  async getBasketRecommendations(
    basketId: string,
    strategy: RecommendationStrategy = 'CONFIDENCE'
  ): Promise<BasketRecommendation> {
    const response = await aiApiClient.get<BasketRecommendation>(
      `${this.basePath}/recommendations/${basketId}`,
      { params: { strategy } }
    )
    return response.data
  }

  /**
   * Get real-time basket recommendations
   * Pass current products to get instant recommendations
   */
  async getRealTimeRecommendations(
    products: { productId: string; quantity?: number }[],
    options?: {
      strategy?: RecommendationStrategy
      maxRecommendations?: number
      includeBundles?: boolean
    }
  ): Promise<BasketRecommendation> {
    const response = await aiApiClient.post<BasketRecommendation>(
      `${this.basePath}/recommendations/real-time`,
      { products, ...options }
    )
    return response.data
  }

  /**
   * Get association rules
   * Returns discovered association rules with filtering options
   */
  async getAssociationRules(params?: {
    minLift?: number
    minConfidence?: number
    minSupport?: number
    category?: string
    limit?: number
    quality?: 'HIGH' | 'MEDIUM' | 'LOW'
  }): Promise<AssociationRule[]> {
    const response = await aiApiClient.get<AssociationRule[]>(
      `${this.basePath}/rules`,
      { params }
    )
    return response.data
  }

  /**
   * Get rule set summary
   * Returns aggregated information about all rules
   */
  async getRuleSetSummary(): Promise<RuleSet> {
    const response = await aiApiClient.get<RuleSet>(
      `${this.basePath}/rules/summary`
    )
    return response.data
  }

  /**
   * Get category affinities
   * Returns which categories are commonly bought together
   */
  async getCategoryAffinities(category?: string): Promise<CategoryAffinity[]> {
    const response = await aiApiClient.get<CategoryAffinity[]>(
      `${this.basePath}/affinities/categories`,
      { params: { category } }
    )
    return response.data
  }

  /**
   * Get actionable recommendations
   * Returns cross-sell, up-sell, and bundle suggestions
   */
  async getActionableRecommendations(params?: {
    type?: 'CROSS_SELL' | 'UP_SELL' | 'BUNDLE' | 'PROMOTION' | 'PLACEMENT'
    priority?: 'HIGH' | 'MEDIUM' | 'LOW'
    limit?: number
  }): Promise<ActionableRecommendation[]> {
    const response = await aiApiClient.get<ActionableRecommendation[]>(
      `${this.basePath}/recommendations/actionable`,
      { params }
    )
    return response.data
  }

  /**
   * Get analysis insights
   * Returns key insights and patterns discovered
   */
  async getAnalysisInsights(analysisId?: string): Promise<AnalysisInsight[]> {
    const response = await aiApiClient.get<AnalysisInsight[]>(
      `${this.basePath}/insights`,
      { params: { analysisId } }
    )
    return response.data
  }

  /**
   * Get bundle suggestions
   * Returns product bundle recommendations
   */
  async getBundleSuggestions(params?: {
    category?: string
    minFrequency?: number
    maxBundles?: number
  }): Promise<BundleSuggestion[]> {
    const response = await aiApiClient.get<BundleSuggestion[]>(
      `${this.basePath}/bundles/suggestions`,
      { params }
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Calculate recommendation strength score
   * Combines confidence, lift, and support into single score
   */
  calculateRecommendationStrength(
    confidence: number,
    lift: number,
    support: number
  ): number {
    // Weighted average: confidence 40%, lift 40%, support 20%
    return (confidence * 0.4) + (Math.min(lift * 20, 100) * 0.4) + (support * 0.2)
  }

  /**
   * Get rule quality color
   */
  getRuleQualityColor(quality: 'HIGH' | 'MEDIUM' | 'LOW'): string {
    switch (quality) {
      case 'HIGH':
        return '#4CAF50' // Green
      case 'MEDIUM':
        return '#2196F3' // Blue
      case 'LOW':
        return '#FF9800' // Orange
    }
  }

  /**
   * Get lift interpretation
   */
  interpretLift(lift: number): string {
    if (lift > 3) return 'Very Strong Association'
    if (lift > 2) return 'Strong Association'
    if (lift > 1) return 'Moderate Association'
    if (lift === 1) return 'Independent'
    return 'Negative Association'
  }

  /**
   * Get confidence level label
   */
  getConfidenceLevel(confidence: number): string {
    if (confidence >= 80) return 'Very High'
    if (confidence >= 60) return 'High'
    if (confidence >= 40) return 'Moderate'
    if (confidence >= 20) return 'Low'
    return 'Very Low'
  }

  /**
   * Format rule as readable text
   */
  formatRule(rule: AssociationRule): string {
    const antecedent = rule.antecedent.slice(0, 3).join(', ')
    const consequent = rule.consequent.slice(0, 3).join(', ')
    return `Customers who buy ${antecedent} also buy ${consequent} (${rule.confidence.toFixed(1)}% confidence, ${rule.lift.toFixed(2)}x lift)`
  }

  /**
   * Check if rule is statistically significant
   */
  isRuleSignificant(rule: AssociationRule): boolean {
    return rule.lift >= 1.2 && rule.confidence >= 50 && rule.support >= 5
  }

  /**
   * Calculate expected revenue from recommendation
   */
  calculateExpectedRevenue(
    recommendation: ProductRecommendation,
    avgBasketValue: number,
    conversionRate: number
  ): number {
    return recommendation.price * conversionRate * (recommendation.confidence / 100)
  }

  /**
   * Sort recommendations by priority
   */
  sortRecommendations<T extends { confidence: number; lift?: number }>(
    recommendations: T[]
  ): T[] {
    return recommendations.sort((a, b) => {
      const scoreA = a.confidence * (a.lift || 1)
      const scoreB = b.confidence * (b.lift || 1)
      return scoreB - scoreA
    })
  }

  /**
   * Get priority color for recommendations
   */
  getPriorityColor(priority: 'HIGH' | 'MEDIUM' | 'LOW'): string {
    switch (priority) {
      case 'HIGH':
        return '#F44336' // Red
      case 'MEDIUM':
        return '#FF9800' // Orange
      case 'LOW':
        return '#4CAF50' // Green
    }
  }
}

// Export singleton instance
export const marketBasketAnalysisService = new MarketBasketAnalysisService()

