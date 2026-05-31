/**
 * AI User Profiling Service
 *
 * Service: ai-user-profiling-service (Port 7006)
 * Base Path: /api/v1/ai/profiling
 *
 * Features:
 * - Comprehensive user profile management
 * - Behavioral tracking and analysis
 * - User segmentation and insights
 * - Personalized recommendations
 * - Interest and preference tracking
 * - Cross-domain profile aggregation
 */

import { aiApiClient } from '../../../client/axios-client'
import type { DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Behavior types that can be tracked
 */
export type BehaviorType =
  | 'PAGE_VIEW'
  | 'CLICK'
  | 'SEARCH'
  | 'PURCHASE'
  | 'ADD_TO_CART'
  | 'WISHLIST'
  | 'REVIEW'
  | 'SHARE'
  | 'DOWNLOAD'
  | 'SIGNUP'
  | 'LOGIN'
  | 'LOGOUT'
  | 'FILTER'
  | 'SORT'
  | 'SCROLL'

/**
 * User interest categories
 */
export type UserInterest =
  | 'TECHNOLOGY'
  | 'FASHION'
  | 'SPORTS'
  | 'HEALTH'
  | 'FINANCE'
  | 'ENTERTAINMENT'
  | 'TRAVEL'
  | 'FOOD'
  | 'EDUCATION'
  | 'GAMING'
  | 'BUSINESS'
  | 'LIFESTYLE'

/**
 * User preference types
 */
export type UserPreference =
  | 'LANGUAGE'
  | 'CURRENCY'
  | 'CATEGORY'
  | 'BRAND'
  | 'PRICE_RANGE'
  | 'SIZE'
  | 'COLOR'
  | 'NOTIFICATION'
  | 'SHIPPING'

/**
 * User profile status
 */
export type ProfileStatus = 'ACTIVE' | 'INACTIVE' | 'PENDING' | 'ARCHIVED'

/**
 * Profile segment type
 */
export type SegmentType =
  | 'HIGH_VALUE'
  | 'AT_RISK'
  | 'NEW_CUSTOMER'
  | 'LOYAL'
  | 'DORMANT'
  | 'POTENTIAL_CHURN'
  | 'VIP'
  | 'BROWSE_ONLY'

/**
 * Insight category
 */
export type InsightCategory =
  | 'BEHAVIORAL'
  | 'PREFERENCES'
  | 'ENGAGEMENT'
  | 'LIFECYCLE'
  | 'PURCHASE'
  | 'DEMOGRAPHIC'

/**
 * Recommendation type
 */
export type RecommendationType =
  | 'PRODUCT'
  | 'CONTENT'
  | 'OFFER'
  | 'CATEGORY'
  | 'FEATURE'
  | 'CHANNEL'

// ============================================================================
// Interfaces
// ============================================================================

/**
 * Core user profile data
 */
export interface UserProfile {
  userId: string
  profileId: string
  status: ProfileStatus
  createdAt: string
  updatedAt: string
  lastActiveAt: string

  // Basic info
  email?: string
  phone?: string
  firstName?: string
  lastName?: string
  displayName?: string
  avatarUrl?: string

  // Demographics
  age?: number
  gender?: string
  location?: {
    country: string
    region?: string
    city?: string
    timezone?: string
  }

  // Behavioral metrics
  metrics: {
    totalSessions: number
    totalPageViews: number
    avgSessionDuration: number // seconds
    totalPurchases: number
    totalRevenue: number
    lastPurchaseAt?: string
    purchaseFrequency: number // days
    lifetimeValue: number
  }

  // Interests and preferences
  interests: UserInterest[]
  preferences: UserPreference[]

  // Segment assignments
  segments: string[]

  // Engagement score (0-100)
  engagementScore: number

  // Churn risk (0-100)
  churnRisk: number

  // Domain-specific data
  domainData?: Record<string, unknown>
}

/**
 * Profile segment definition
 */
export interface ProfileSegment {
  segmentId: string
  name: string
  type: SegmentType
  description: string
  criteria: SegmentCriteria
  userCount: number
  avgEngagementScore: number
  avgLifetimeValue: number
  createdAt: string
  updatedAt: string
}

/**
 * Segment criteria for filtering users
 */
export interface SegmentCriteria {
  minEngagementScore?: number
  maxEngagementScore?: number
  minLifetimeValue?: number
  maxLifetimeValue?: number
  minPurchaseCount?: number
  maxPurchaseCount?: number
  daysSinceLastActivity?: number
  daysSinceLastPurchase?: number
  interests?: UserInterest[]
  domains?: string[]
}

/**
 * Profile insight data
 */
export interface ProfileInsight {
  insightId: string
  userId: string
  category: InsightCategory
  type: string
  title: string
  description: string
  severity: 'INFO' | 'WARNING' | 'CRITICAL'
  confidence: number // 0-100
  createdAt: string
  expiresAt?: string
  actionable: boolean
  metadata: Record<string, unknown>
}

/**
 * User behavior tracking record
 */
export interface UserBehavior {
  behaviorId: string
  userId: string
  type: BehaviorType
  timestamp: string
  sessionId: string
  domain: string
  page?: string
  productId?: string
  categoryId?: string
  searchQuery?: string
  metadata: Record<string, unknown>
  device?: {
    type: 'DESKTOP' | 'MOBILE' | 'TABLET'
    os?: string
    browser?: string
  }
  location?: {
    country?: string
    region?: string
    city?: string
  }
}

/**
 * Request payload for creating a profile
 */
export interface CreateProfileRequest {
  userId: string
  email?: string
  phone?: string
  firstName?: string
  lastName?: string
  displayName?: string
  avatarUrl?: string
  age?: number
  gender?: string
  location?: {
    country: string
    region?: string
    city?: string
    timezone?: string
  }
  interests?: UserInterest[]
  preferences?: UserPreference[]
  domainData?: Record<string, unknown>
}

/**
 * Request payload for updating a profile
 */
export interface UpdateProfileRequest {
  email?: string
  phone?: string
  firstName?: string
  lastName?: string
  displayName?: string
  avatarUrl?: string
  age?: number
  gender?: string
  location?: {
    country: string
    region?: string
    city?: string
    timezone?: string
  }
  interests?: UserInterest[]
  preferences?: UserPreference[]
  domainData?: Record<string, unknown>
}

/**
 * Request payload for behavior tracking
 */
export interface BehaviorTrackingRequest {
  userId: string
  type: BehaviorType
  sessionId: string
  domain: string
  page?: string
  productId?: string
  categoryId?: string
  searchQuery?: string
  metadata?: Record<string, unknown>
  device?: {
    type: 'DESKTOP' | 'MOBILE' | 'TABLET'
    os?: string
    browser?: string
  }
  location?: {
    country?: string
    region?: string
    city?: string
  }
}

/**
 * Profile-based recommendation
 */
export interface ProfileRecommendation {
  recommendationId: string
  userId: string
  type: RecommendationType
  itemId: string
  itemName: string
  score: number // 0-100 relevance score
  reason: string
  category?: string
  imageUrl?: string
  price?: number
  currency?: string
  validUntil?: string
  metadata: Record<string, unknown>
  generatedAt: string
}

/**
 * Request for profile recommendations
 */
export interface ProfileRecommendationRequest {
  type?: RecommendationType
  limit?: number
  offset?: number
  categories?: string[]
  minScore?: number
}

/**
 * Batch behavior tracking request
 */
export interface BatchBehaviorTrackingRequest {
  behaviors: BehaviorTrackingRequest[]
}

/**
 * Query parameters for listing profiles
 */
export interface ProfileQueryParams {
  status?: ProfileStatus
  segment?: string
  minEngagementScore?: number
  maxEngagementScore?: number
  minLifetimeValue?: number
  maxLifetimeValue?: number
  interest?: UserInterest
  limit?: number
  offset?: number
  sortBy?: 'createdAt' | 'updatedAt' | 'lastActiveAt' | 'engagementScore' | 'lifetimeValue'
  sortOrder?: 'ASC' | 'DESC'
}

/**
 * User behavior analytics summary
 */
export interface BehaviorAnalytics {
  userId: string
  period: DateRange
  totalBehaviors: number
  behaviorBreakdown: {
    type: BehaviorType
    count: number
    percentage: number
  }[]
  topPages: {
    page: string
    views: number
  }[]
  topProducts: {
    productId: string
    productName: string
    interactions: number
  }[]
  peakActivityHours: {
    hour: number
    count: number
  }[]
}

// ============================================================================
// Service Class
// ============================================================================

class UserProfilingService {
  private readonly basePath = '/api/v1/ai/profiling'

  // ========================================================================
  // Profile Management
  // ========================================================================

  /**
   * Get all user profiles
   * Supports filtering and pagination
   */
  async getProfiles(params?: ProfileQueryParams): Promise<{
    profiles: UserProfile[]
    total: number
    limit: number
    offset: number
  }> {
    const response = await aiApiClient.get(`${this.basePath}/profiles`, { params })
    return response.data
  }

  /**
   * Get a specific user profile
   */
  async getProfile(userId: string): Promise<UserProfile> {
    const response = await aiApiClient.get<UserProfile>(
      `${this.basePath}/profiles/${encodeURIComponent(userId)}`
    )
    return response.data
  }

  /**
   * Create a new user profile
   */
  async createProfile(request: CreateProfileRequest): Promise<UserProfile> {
    const response = await aiApiClient.post<UserProfile>(
      `${this.basePath}/profiles/create`,
      request
    )
    return response.data
  }

  /**
   * Update an existing user profile
   */
  async updateProfile(
    userId: string,
    request: UpdateProfileRequest
  ): Promise<UserProfile> {
    const response = await aiApiClient.put<UserProfile>(
      `${this.basePath}/profiles/${encodeURIComponent(userId)}`,
      request
    )
    return response.data
  }

  /**
   * Delete a user profile
   */
  async deleteProfile(userId: string): Promise<void> {
    await aiApiClient.delete(
      `${this.basePath}/profiles/${encodeURIComponent(userId)}`
    )
  }

  /**
   * Merge two user profiles
   * Useful when users link accounts or consolidate data
   */
  async mergeProfiles(sourceUserId: string, targetUserId: string): Promise<UserProfile> {
    const response = await aiApiClient.post<UserProfile>(
      `${this.basePath}/profiles/merge`,
      { sourceUserId, targetUserId }
    )
    return response.data
  }

  // ========================================================================
  // Segments
  // ========================================================================

  /**
   * Get all profile segments
   */
  async getSegments(): Promise<ProfileSegment[]> {
    const response = await aiApiClient.get<ProfileSegment[]>(
      `${this.basePath}/segments`
    )
    return response.data
  }

  /**
   * Get a specific segment
   */
  async getSegment(segmentId: string): Promise<ProfileSegment> {
    const response = await aiApiClient.get<ProfileSegment>(
      `${this.basePath}/segments/${encodeURIComponent(segmentId)}`
    )
    return response.data
  }

  /**
   * Get users in a specific segment
   */
  async getSegmentUsers(
    segmentId: string,
    limit = 50,
    offset = 0
  ): Promise<{
    users: UserProfile[]
    total: number
  }> {
    const response = await aiApiClient.get(
      `${this.basePath}/segments/${encodeURIComponent(segmentId)}/users`,
      { params: { limit, offset } }
    )
    return response.data
  }

  /**
   * Create a custom segment
   */
  async createSegment(
    name: string,
    type: SegmentType,
    description: string,
    criteria: SegmentCriteria
  ): Promise<ProfileSegment> {
    const response = await aiApiClient.post<ProfileSegment>(
      `${this.basePath}/segments/create`,
      { name, type, description, criteria }
    )
    return response.data
  }

  // ========================================================================
  // Insights
  // ========================================================================

  /**
   * Get insights for a specific user
   */
  async getUserInsights(
    userId: string,
    limit = 20
  ): Promise<ProfileInsight[]> {
    const response = await aiApiClient.get<ProfileInsight[]>(
      `${this.basePath}/insights/${encodeURIComponent(userId)}`,
      { params: { limit } }
    )
    return response.data
  }

  /**
   * Get insights by category
   */
  async getInsightsByCategory(
    category: InsightCategory,
    limit = 50
  ): Promise<ProfileInsight[]> {
    const response = await aiApiClient.get<ProfileInsight[]>(
      `${this.basePath}/insights`,
      { params: { category, limit } }
    )
    return response.data
  }

  /**
   * Get critical insights across all users
   */
  async getCriticalInsights(limit = 20): Promise<ProfileInsight[]> {
    const response = await aiApiClient.get<ProfileInsight[]>(
      `${this.basePath}/insights/critical`,
      { params: { limit } }
    )
    return response.data
  }

  // ========================================================================
  // Behavior Tracking
  // ========================================================================

  /**
   * Track a single user behavior event
   */
  async trackBehavior(request: BehaviorTrackingRequest): Promise<UserBehavior> {
    const response = await aiApiClient.post<UserBehavior>(
      `${this.basePath}/behavior/track`,
      request
    )
    return response.data
  }

  /**
   * Track multiple behavior events in batch
   */
  async trackBehaviorsBatch(
    request: BatchBehaviorTrackingRequest
  ): Promise<UserBehavior[]> {
    const response = await aiApiClient.post<UserBehavior[]>(
      `${this.basePath}/behavior/track/batch`,
      request
    )
    return response.data
  }

  /**
   * Get behavior history for a user
   */
  async getUserBehavior(
    userId: string,
    params?: {
      startDate?: string
      endDate?: string
      type?: BehaviorType
      limit?: number
      offset?: number
    }
  ): Promise<{
    behaviors: UserBehavior[]
    total: number
  }> {
    const response = await aiApiClient.get(
      `${this.basePath}/behavior/${encodeURIComponent(userId)}`,
      { params }
    )
    return response.data
  }

  /**
   * Get behavior analytics for a user
   */
  async getBehaviorAnalytics(
    userId: string,
    dateRange: DateRange
  ): Promise<BehaviorAnalytics> {
    const response = await aiApiClient.post<BehaviorAnalytics>(
      `${this.basePath}/behavior/analytics/${encodeURIComponent(userId)}`,
      dateRange
    )
    return response.data
  }

  // ========================================================================
  // Recommendations
  // ========================================================================

  /**
   * Get profile-based recommendations for a user
   */
  async getRecommendations(
    userId: string,
    request?: ProfileRecommendationRequest
  ): Promise<ProfileRecommendation[]> {
    const response = await aiApiClient.get<ProfileRecommendation[]>(
      `${this.basePath}/recommendations/${encodeURIComponent(userId)}`,
      { params: request }
    )
    return response.data
  }

  /**
   * Get product recommendations for a user
   */
  async getProductRecommendations(
    userId: string,
    limit = 10,
    categories?: string[]
  ): Promise<ProfileRecommendation[]> {
    return this.getRecommendations(userId, {
      type: 'PRODUCT',
      limit,
      categories,
    })
  }

  /**
   * Get content recommendations for a user
   */
  async getContentRecommendations(
    userId: string,
    limit = 10
  ): Promise<ProfileRecommendation[]> {
    return this.getRecommendations(userId, {
      type: 'CONTENT',
      limit,
    })
  }

  /**
   * Get offer recommendations for a user
   */
  async getOfferRecommendations(
    userId: string,
    limit = 5
  ): Promise<ProfileRecommendation[]> {
    return this.getRecommendations(userId, {
      type: 'OFFER',
      limit,
    })
  }

  // ========================================================================
  // Analytics & Aggregation
  // ========================================================================

  /**
   * Get profile statistics
   */
  async getProfileStats(): Promise<{
    totalProfiles: number
    activeProfiles: number
    inactiveProfiles: number
    avgEngagementScore: number
    avgLifetimeValue: number
    segmentDistribution: {
      segment: string
      count: number
      percentage: number
    }[]
  }> {
    const response = await aiApiClient.get(`${this.basePath}/stats`)
    return response.data
  }

  /**
   * Get top users by engagement score
   */
  async getTopEngagedUsers(limit = 10): Promise<UserProfile[]> {
    const response = await aiApiClient.get(`${this.basePath}/users/top-engaged`, {
      params: { limit },
    })
    return response.data
  }

  /**
   * Get at-risk users (high churn risk)
   */
  async getAtRiskUsers(
    minChurnRisk = 70,
    limit = 50
  ): Promise<UserProfile[]> {
    const response = await aiApiClient.get(`${this.basePath}/users/at-risk`, {
      params: { minChurnRisk, limit },
    })
    return response.data
  }

  /**
   * Get high-value users
   */
  async getHighValueUsers(
    minLifetimeValue = 1000,
    limit = 50
  ): Promise<UserProfile[]> {
    const response = await aiApiClient.get(`${this.basePath}/users/high-value`, {
      params: { minLifetimeValue, limit },
    })
    return response.data
  }

  /**
   * Search profiles by query
   */
  async searchProfiles(
    query: string,
    limit = 20
  ): Promise<UserProfile[]> {
    const response = await aiApiClient.get(`${this.basePath}/profiles/search`, {
      params: { q: query, limit },
    })
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get profile status color
   */
  getStatusColor(status: ProfileStatus): string {
    switch (status) {
      case 'ACTIVE':
        return '#4CAF50' // Green
      case 'INACTIVE':
        return '#9E9E9E' // Gray
      case 'PENDING':
        return '#FF9800' // Orange
      case 'ARCHIVED':
        return '#607D8B' // Blue Gray
      default:
        return '#9E9E9E'
    }
  }

  /**
   * Get segment type color
   */
  getSegmentColor(type: SegmentType): string {
    switch (type) {
      case 'VIP':
        return '#FFD700' // Gold
      case 'HIGH_VALUE':
        return '#4CAF50' // Green
      case 'AT_RISK':
      case 'POTENTIAL_CHURN':
        return '#F44336' // Red
      case 'LOYAL':
        return '#2196F3' // Blue
      case 'NEW_CUSTOMER':
        return '#9C27B0' // Purple
      case 'DORMANT':
        return '#9E9E9E' // Gray
      case 'BROWSE_ONLY':
        return '#FF9800' // Orange
      default:
        return '#9E9E9E'
    }
  }

  /**
   * Get engagement level label
   */
  getEngagementLevel(score: number): string {
    if (score >= 80) return 'Very High'
    if (score >= 60) return 'High'
    if (score >= 40) return 'Medium'
    if (score >= 20) return 'Low'
    return 'Very Low'
  }

  /**
   * Get engagement level color
   */
  getEngagementColor(score: number): string {
    if (score >= 80) return '#4CAF50' // Green
    if (score >= 60) return '#8BC34A' // Light Green
    if (score >= 40) return '#FF9800' // Orange
    if (score >= 20) return '#FF5722' // Deep Orange
    return '#F44336' // Red
  }

  /**
   * Get churn risk level label
   */
  getChurnRiskLabel(risk: number): string {
    if (risk >= 70) return 'High'
    if (risk >= 40) return 'Medium'
    if (risk >= 20) return 'Low'
    return 'Very Low'
  }

  /**
   * Get insight severity color
   */
  getInsightColor(severity: 'INFO' | 'WARNING' | 'CRITICAL'): string {
    switch (severity) {
      case 'CRITICAL':
        return '#F44336' // Red
      case 'WARNING':
        return '#FF9800' // Orange
      case 'INFO':
      default:
        return '#2196F3' // Blue
    }
  }

  /**
   * Format user display name
   */
  formatDisplayName(profile: UserProfile): string {
    if (profile.displayName) return profile.displayName
    if (profile.firstName && profile.lastName) {
      return `${profile.firstName} ${profile.lastName}`
    }
    if (profile.firstName) return profile.firstName
    if (profile.email) {
      return profile.email.split('@')[0]
    }
    return profile.userId
  }

  /**
   * Calculate days since last activity
   */
  daysSinceLastActivity(profile: UserProfile): number {
    const lastActive = new Date(profile.lastActiveAt)
    const now = new Date()
    const diffTime = Math.abs(now.getTime() - lastActive.getTime())
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  }
}

// Export singleton instance
export const userProfilingService = new UserProfilingService()

