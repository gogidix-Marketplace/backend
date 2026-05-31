/**
 * AI Product Recommendation Service
 *
 * Service: ai-product-recommendation-service (Port 7004)
 * Base Path: /api/v1/ai/recommendations
 *
 * Features:
 * - Product recommendations using multiple algorithms
 * - Collaborative filtering based on user behavior
 * - Content-based recommendations using product attributes
 * - Trending products discovery
 * - Personalized recommendations for users
 * - Recommendation feedback loop for continuous improvement
 */

import { aiApiClient } from '../../../client/axios-client'
import type { DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Recommendation algorithms supported by the service
 */
export type RecommendationAlgorithm =
  | 'COLLABORATIVE_FILTERING'
  | 'CONTENT_BASED'
  | 'HYBRID'
  | 'MATRIX_FACTORIZATION'
  | 'DEEP_LEARNING'
  | 'ASSOCIATION_RULES'

/**
 * Reasons why a product was recommended
 */
export type RecommendationReason =
  | 'SIMILAR_PRODUCTS'
  | 'FREQUENTLY_BOUGHT_TOGETHER'
  | 'TRENDING'
  | 'USER_PREFERENCES'
  | 'POPULAR_IN_CATEGORY'
  | 'NEW_ARRIVAL'
  | 'BEST_SELLER'
  | 'PERSONALIZED'

/**
 * Request to get product recommendations
 */
export interface RecommendationRequest {
  userId?: string
  productId?: string
  category?: string
  algorithm?: RecommendationAlgorithm
  limit?: number
  minScore?: number
  includeAttributes?: boolean
  context?: RecommendationContext
}

/**
 * Context for the recommendation request
 */
export interface RecommendationContext {
  sessionId?: string
  currentPage?: string
  cartItems?: string[]
  recentViews?: string[]
  priceRange?: {
    min: number
    max: number
  }
  brands?: string[]
}

/**
 * Response containing product recommendations
 */
export interface RecommendationResponse {
  recommendationId: string
  generatedAt: string
  algorithm: RecommendationAlgorithm
  totalResults: number
  recommendations: ProductRecommendation[]
  metadata: RecommendationMetadata
}

/**
 * Individual product recommendation
 */
export interface ProductRecommendation {
  productId: string
  productName: string
  score: number // Confidence score 0-100
  relevanceScore: number // Relevance to user 0-100
  reasons: RecommendationReason[]
  attributes?: ProductAttributes
  pricing: ProductPricing
  performance: ProductPerformance
}

/**
 * Product attributes used for content-based filtering
 */
export interface ProductAttributes {
  category: string
  subcategory?: string
  brand: string
  tags: string[]
  features: string[]
  specifications?: Record<string, string>
}

/**
 * Product pricing information
 */
export interface ProductPricing {
  currentPrice: number
  originalPrice?: number
  currency: string
  discount?: number
  discountPercentage?: number
}

/**
 * Product performance metrics
 */
export interface ProductPerformance {
  viewCount: number
  purchaseCount: number
  conversionRate: number
  averageRating: number
  reviewCount: number
  trendScore: number
}

/**
 * Metadata about the recommendation
 */
export interface RecommendationMetadata {
  processingTime: number
  modelVersion: string
  dataSource: string
  freshness: string
}

/**
 * Collaborative filtering recommendations response
 */
export interface CollaborativeRecommendationsResponse {
  userId: string
  recommendations: ProductRecommendation[]
  similarUsers: SimilarUser[]
  generatedAt: string
}

/**
 * Similar user for collaborative filtering
 */
export interface SimilarUser {
  userId: string
  similarityScore: number
  commonProducts: string[]
}

/**
 * Content-based recommendations response
 */
export interface ContentBasedRecommendationsResponse {
  productId: string
  recommendations: ProductRecommendation[]
  similarityFactors: SimilarityFactor[]
  generatedAt: string
}

/**
 * Factors contributing to similarity in content-based filtering
 */
export interface SimilarityFactor {
  factor: string
  weight: number
  value: string
}

/**
 * Trending products response
 */
export interface TrendingProductsResponse {
  period: string
  products: TrendingProduct[]
  generatedAt: string
  categories: {
    category: string
    productCount: number
  }[]
}

/**
 * Individual trending product
 */
export interface TrendingProduct {
  productId: string
  productName: string
  rank: number
  trendScore: number
  velocity: number // Rate of increase in popularity
  category: string
  pricing: ProductPricing
  performance: ProductPerformance
  timeInTrending: number // Days in trending list
}

/**
 * Personalized recommendations response
 */
export interface PersonalizedRecommendationsResponse {
  userId: string
  recommendations: PersonalizedRecommendation[]
  userSegments: string[]
  generatedAt: string
}

/**
 * Individual personalized recommendation
 */
export interface PersonalizedRecommendation extends ProductRecommendation {
  personalizedScore: number
  predictedAffinity: number
  boostFactors: BoostFactor[]
}

/**
 * Factors that boosted this recommendation
 */
export interface BoostFactor {
  factor: string
  impact: number
  description: string
}

/**
 * Feedback request for recommendation
 */
export interface FeedbackRequest {
  recommendationId: string
  productId: string
  userId?: string
  feedbackType: FeedbackType
  rating?: number
  clicked?: boolean
  purchased?: boolean
  reason?: string
}

/**
 * Type of feedback provided
 */
export type FeedbackType = 'POSITIVE' | 'NEGATIVE' | 'NEUTRAL' | 'CLICKED' | 'PURCHASED' | 'DISMISSED'

/**
 * Feedback submission response
 */
export interface FeedbackResponse {
  feedbackId: string
  submittedAt: string
  acknowledged: boolean
}

// ============================================================================
// Service Class
// ============================================================================

class ProductRecommendationService {
  private readonly basePath = '/api/v1/ai/recommendations'

  /**
   * Get product recommendations
   * Main method for getting personalized product suggestions
   */
  async getRecommendations(request: RecommendationRequest): Promise<RecommendationResponse> {
    const response = await aiApiClient.post<RecommendationResponse>(
      `${this.basePath}/recommend`,
      request
    )
    return response.data
  }

  /**
   * Get quick recommendations (default parameters)
   * Convenience method for dashboard widgets
   */
  async getQuickRecommendations(
    userId: string,
    limit = 10
  ): Promise<RecommendationResponse> {
    return this.getRecommendations({
      userId,
      limit,
      algorithm: 'HYBRID',
      includeAttributes: true,
    })
  }

  /**
   * Get recommendations for a specific product
   * Useful for "Similar Products" and "Frequently Bought Together" sections
   */
  async getProductRecommendations(
    productId: string,
    limit = 10
  ): Promise<RecommendationResponse> {
    return this.getRecommendations({
      productId,
      limit,
      algorithm: 'CONTENT_BASED',
      includeAttributes: true,
    })
  }

  /**
   * Get collaborative filtering recommendations
   * Based on similar users' preferences and behaviors
   */
  async getCollaborativeRecommendations(
    userId: string,
    limit = 10
  ): Promise<CollaborativeRecommendationsResponse> {
    const response = await aiApiClient.get<CollaborativeRecommendationsResponse>(
      `${this.basePath}/collaborative/${userId}`,
      { params: { limit } }
    )
    return response.data
  }

  /**
   * Get content-based recommendations
   * Based on product attributes and similarity
   */
  async getContentBasedRecommendations(
    productId: string,
    limit = 10
  ): Promise<ContentBasedRecommendationsResponse> {
    const response = await aiApiClient.get<ContentBasedRecommendationsResponse>(
      `${this.basePath}/content-based/${productId}`,
      { params: { limit } }
    )
    return response.data
  }

  /**
   * Get trending products
   * Products currently gaining popularity across the platform
   */
  async getTrendingProducts(
    category?: string,
    limit = 20
  ): Promise<TrendingProductsResponse> {
    const response = await aiApiClient.get<TrendingProductsResponse>(
      `${this.basePath}/trending`,
      { params: { category, limit } }
    )
    return response.data
  }

  /**
   * Get trending products by category
   */
  async getTrendingByCategory(category: string, limit = 20): Promise<TrendingProductsResponse> {
    return this.getTrendingProducts(category, limit)
  }

  /**
   * Get personalized recommendations for a user
   * Combines multiple signals for the most relevant suggestions
   */
  async getPersonalizedRecommendations(
    userId: string,
    limit = 10,
    context?: RecommendationContext
  ): Promise<PersonalizedRecommendationsResponse> {
    const response = await aiApiClient.get<PersonalizedRecommendationsResponse>(
      `${this.basePath}/personalized/${userId}`,
      { params: { limit, ...context } }
    )
    return response.data
  }

  /**
   * Submit recommendation feedback
   * Helps improve future recommendation quality
   */
  async submitFeedback(request: FeedbackRequest): Promise<FeedbackResponse> {
    const response = await aiApiClient.post<FeedbackResponse>(
      `${this.basePath}/feedback`,
      request
    )
    return response.data
  }

  /**
   * Record a click on a recommendation
   * Convenience method for tracking recommendation clicks
   */
  async recordClick(
    recommendationId: string,
    productId: string,
    userId?: string
  ): Promise<FeedbackResponse> {
    return this.submitFeedback({
      recommendationId,
      productId,
      userId,
      feedbackType: 'CLICKED',
      clicked: true,
    })
  }

  /**
   * Record a purchase following a recommendation
   * Convenience method for tracking recommendation conversions
   */
  async recordPurchase(
    recommendationId: string,
    productId: string,
    userId?: string,
    rating?: number
  ): Promise<FeedbackResponse> {
    return this.submitFeedback({
      recommendationId,
      productId,
      userId,
      feedbackType: 'PURCHASED',
      purchased: true,
      rating,
    })
  }

  /**
   * Record a negative feedback (dismissed/rejected)
   * Convenience method for tracking rejected recommendations
   */
  async recordDismissal(
    recommendationId: string,
    productId: string,
    userId?: string,
    reason?: string
  ): Promise<FeedbackResponse> {
    return this.submitFeedback({
      recommendationId,
      productId,
      userId,
      feedbackType: 'DISMISSED',
      reason,
    })
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Format score as percentage
   */
  formatScore(score: number): string {
    return `${Math.round(score)}%`
  }

  /**
   * Get confidence color based on score
   */
  getScoreColor(score: number): string {
    if (score >= 80) return '#4CAF50' // Green
    if (score >= 60) return '#2196F3' // Blue
    if (score >= 40) return '#FF9800' // Orange
    return '#F44336' // Red
  }

  /**
   * Get trend indicator color
   */
  getTrendColor(velocity: number): string {
    if (velocity > 0) return '#4CAF50' // Green - rising
    if (velocity === 0) return '#9E9E9E' // Gray - stable
    return '#F44336' // Red - falling
  }

  /**
   * Format recommendation reason for display
   */
  formatReason(reason: RecommendationReason): string {
    const reasonLabels: Record<RecommendationReason, string> = {
      SIMILAR_PRODUCTS: 'Because you viewed similar items',
      FREQUENTLY_BOUGHT_TOGETHER: 'Frequently bought together',
      TRENDING: 'Trending now',
      USER_PREFERENCES: 'Based on your preferences',
      POPULAR_IN_CATEGORY: 'Popular in this category',
      NEW_ARRIVAL: 'New arrival',
      BEST_SELLER: 'Best seller',
      PERSONALIZED: 'Recommended for you',
    }
    return reasonLabels[reason] || reason
  }

  /**
   * Calculate discount percentage
   */
  calculateDiscountPercentage(currentPrice: number, originalPrice?: number): number | null {
    if (!originalPrice || originalPrice <= currentPrice) return null
    return Math.round(((originalPrice - currentPrice) / originalPrice) * 100)
  }

  /**
   * Get star rating display
   */
  getStarRating(rating: number): string {
    const fullStars = Math.floor(rating)
    const hasHalfStar = rating % 1 >= 0.5
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0)

    return '\u2605'.repeat(fullStars) +
           (hasHalfStar ? '\u00BD' : '') +
           '\u2606'.repeat(emptyStars)
  }

  /**
   * Filter recommendations by minimum score
   */
  filterByScore(
    recommendations: ProductRecommendation[],
    minScore: number
  ): ProductRecommendation[] {
    return recommendations.filter(rec => rec.score >= minScore)
  }

  /**
   * Sort recommendations by score
   */
  sortByScore(
    recommendations: ProductRecommendation[],
    order: 'asc' | 'desc' = 'desc'
  ): ProductRecommendation[] {
    return [...recommendations].sort((a, b) =>
      order === 'desc' ? b.score - a.score : a.score - b.score
    )
  }

  /**
   * Group recommendations by reason
   */
  groupByReason(
    recommendations: ProductRecommendation[]
  ): Map<RecommendationReason, ProductRecommendation[]> {
    const groups = new Map<RecommendationReason, ProductRecommendation[]>()
    for (const rec of recommendations) {
      for (const reason of rec.reasons) {
        if (!groups.has(reason)) {
          groups.set(reason, [])
        }
        groups.get(reason)!.push(rec)
      }
    }
    return groups
  }
}

// Export singleton instance
export const productRecommendationService = new ProductRecommendationService()

