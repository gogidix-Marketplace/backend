/**
 * React Hook: useAIInsights
 *
 * Provides AI-powered insights from various AI services
 * Supports: sales forecasting, customer segmentation, churn prediction,
 * market basket analysis, recommendations, user profiling, intelligence analysis,
 * predictive analytics, time series forecasting, analytics dashboard, and centralized data
 */

import { useState, useEffect, useCallback } from 'react'

// Import AI services
import { salesForecastingService } from '../services/ai/business-intelligence/sales-forecasting.service'
import { userProfilingService } from '../services/ai/business-intelligence/user-profiling.service'
import { churnPredictionService } from '../services/ai/business-intelligence/churn-prediction.service'
import { customerSegmentationService } from '../services/ai/business-intelligence/customer-segmentation.service'
import { intelligenceAnalysisService } from '../services/ai/business-intelligence/intelligence-analysis.service'
import { marketBasketAnalysisService } from '../services/ai/business-intelligence/market-basket-analysis.service'
import { productRecommendationService } from '../services/ai/business-intelligence/product-recommendation.service'
import { predictiveAnalyticsService } from '../services/ai/data-analytics/predictive-analytics.service'
import { timeSeriesForecastingService } from '../services/ai/data-analytics/time-series-forecasting.service'
import { analyticsDashboardService } from '../services/ai/data-analytics/analytics-dashboard.service'
import { centralizedDataService } from '../services/aggregation/centralized-data.service'

// Import types
import type {
  AIInsight,
  AIPrediction,
  AIAnomaly,
} from '../client/types'
import type {
  ForecastHorizon,
  ForecastScenario,
  SalesForecastResponse,
  ScenarioAnalysisResponse,
} from '../services/ai/business-intelligence/sales-forecasting.service'
import type {
  UserProfile,
  ProfileInsight,
  ProfileRecommendation,
  BehaviorAnalytics,
} from '../services/ai/business-intelligence/user-profiling.service'
import type {
  ChurnPrediction,
  ChurnPredictionResponse,
  ChurnFactors,
  ChurnTrendsResponse,
} from '../services/ai/business-intelligence/churn-prediction.service'
import type {
  CustomerSegment,
  CustomerSegmentDetails,
  SegmentationAnalysis,
} from '../services/ai/business-intelligence/customer-segmentation.service'
import type {
  IntelligenceInsight,
  InsightTrend,
  IntelligenceAlert,
  IntelligenceRecommendation,
  AnalysisResponse,
} from '../services/ai/business-intelligence/intelligence-analysis.service'
import type {
  ProductAssociation,
  BasketRecommendation,
  TrendingProductsResponse,
  RecommendationResponse,
} from '../services/ai/business-intelligence/market-basket-analysis.service'
import type {
  RecommendationRequest,
} from '../services/ai/business-intelligence/product-recommendation.service'
import type {
  PredictionResult,
  PredictionModel,
  FeatureImportance,
  ModelAccuracy,
} from '../services/ai/data-analytics/predictive-analytics.service'
import type {
  TimeSeriesForecastResponse,
  ForecastModel,
  SeasonalityAnalysis,
} from '../services/ai/data-analytics/time-series-forecasting.service'
import type {
  AIDashboard,
  DashboardWidget,
  AIInsight as DashboardAIInsight,
  AIAlert,
  ModelPerformance,
} from '../services/ai/data-analytics/analytics-dashboard.service'
import type {
  CentralizedData,
  DomainData,
  DataSourcesHealth,
} from '../services/aggregation/centralized-data.service'

// ============================================================================
// Types
// ============================================================================

interface UseAIInsightsResult {
  insights: AIInsight[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseAIPredictionResult {
  prediction: AIPrediction | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseSalesForecastResult {
  forecast: SalesForecastResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseScenarioAnalysisResult {
  scenarios: ScenarioAnalysisResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

// Available AI service types
export type AIServiceType =
  | 'sales-forecasting'
  | 'churn-prediction'
  | 'customer-segmentation'
  | 'market-basket-analysis'
  | 'product-recommendation'
  | 'user-profiling'
  | 'sentiment-analysis'
  | 'fraud-detection'
  | 'demand-forecasting'
  | 'price-optimization'
  | 'inventory-optimization'
  | 'intelligence-analysis'
  | 'predictive-analytics'
  | 'time-series-forecasting'

// ============================================================================
// Hook: AI Insights by Category
// ============================================================================

export function useAIInsights(
  category: AIServiceType,
  params?: Record<string, any>
): UseAIInsightsResult {
  const [insights, setInsights] = useState<AIInsight[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchInsights = useCallback(async () => {
    setLoading(true)
    setError(null)

    try {
      // Route to appropriate AI service based on category
      let data: AIInsight[] = []

      switch (category) {
        case 'sales-forecasting':
          const forecast = await salesForecastingService.getQuickForecast()
          data = convertForecastToInsights(forecast)
          break

        case 'churn-prediction':
          data = await fetchChurnInsights(params)
          break

        case 'customer-segmentation':
          data = await fetchSegmentationInsights(params)
          break

        case 'intelligence-analysis':
          data = await fetchIntelligenceInsights(params)
          break

        case 'sentiment-analysis':
          data = await fetchSentimentInsights(params)
          break

        case 'fraud-detection':
          data = await fetchFraudInsights(params)
          break

        default:
          data = []
      }

      setInsights(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : `Failed to fetch ${category} insights`)
    } finally {
      setLoading(false)
    }
  }, [category, params])

  useEffect(() => {
    fetchInsights()
  }, [fetchInsights])

  return { insights, loading, error, refetch: fetchInsights }
}

// ============================================================================
// Hook: Sales Forecast (AI)
// ============================================================================

export function useSalesForecast(
  horizon: ForecastHorizon = '6M',
  scenario?: ForecastScenario
): UseSalesForecastResult {
  const [forecast, setForecast] = useState<SalesForecastResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchForecast = useCallback(async () => {
    setLoading(true)
    setError(null)

    try {
      const data = await salesForecastingService.generateForecast({
        horizon,
        scenario: scenario || 'EXPECTED',
        model: 'ENSEMBLE',
        confidenceLevel: 95,
      })
      setForecast(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to generate forecast')
    } finally {
      setLoading(false)
    }
  }, [horizon, scenario])

  useEffect(() => {
    fetchForecast()
  }, [fetchForecast])

  return { forecast, loading, error, refetch: fetchForecast }
}

// ============================================================================
// Hook: Scenario Analysis (AI)
// ============================================================================

export function useScenarioAnalysis(
  horizon: ForecastHorizon = '1Y'
): UseScenarioAnalysisResult {
  const [scenarios, setScenarios] = useState<ScenarioAnalysisResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchScenarios = useCallback(async () => {
    setLoading(true)
    setError(null)

    try {
      const data = await salesForecastingService.getScenarioAnalysis(horizon)
      setScenarios(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to analyze scenarios')
    } finally {
      setLoading(false)
    }
  }, [horizon])

  useEffect(() => {
    fetchScenarios()
  }, [fetchScenarios])

  return { scenarios, loading, error, refetch: fetchScenarios }
}

// ============================================================================
// Hook: User Profiling
// ============================================================================

export function useUserProfiling(userId?: string) {
  const [profile, setProfile] = useState<UserProfile | null>(null)
  const [insights, setInsights] = useState<ProfileInsight[]>([])
  const [recommendations, setRecommendations] = useState<ProfileRecommendation[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!userId) return

    const fetchProfileData = async () => {
      setLoading(true)
      setError(null)

      try {
        const [profileData, insightsData, recommendationsData] = await Promise.all([
          userProfilingService.getProfile(userId),
          userProfilingService.getUserInsights(userId, 20),
          userProfilingService.getRecommendations(userId, { limit: 10 }),
        ])

        setProfile(profileData)
        setInsights(insightsData)
        setRecommendations(recommendationsData)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch user profile')
      } finally {
        setLoading(false)
      }
    }

    fetchProfileData()
  }, [userId])

  const getBehaviorAnalytics = useCallback(async (userId: string, dateRange: any) => {
    try {
      return await userProfilingService.getBehaviorAnalytics(userId, dateRange)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch behavior analytics')
      return null
    }
  }, [])

  return { profile, insights, recommendations, loading, error, getBehaviorAnalytics }
}

// ============================================================================
// Hook: Churn Prediction
// ============================================================================

export function useChurnPrediction(entityType: 'CUSTOMER' | 'EMPLOYEE' = 'CUSTOMER', entityIds?: string[]) {
  const [predictions, setPredictions] = useState<ChurnPrediction[]>([])
  const [factors, setFactors] = useState<ChurnFactors | null>(null)
  const [trends, setTrends] = useState<ChurnTrendsResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!entityIds || entityIds.length === 0) return

    const fetchPredictions = async () => {
      setLoading(true)
      setError(null)

      try {
        const response = await churnPredictionService.predictChurn({
          entityType,
          entityIds,
          includeFactors: true,
          timeHorizon: 90,
        })
        setPredictions(response.predictions)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to predict churn')
      } finally {
        setLoading(false)
      }
    }

    fetchPredictions()
  }, [entityType, entityIds])

  const fetchFactors = useCallback(async (dateRange?: any) => {
    try {
      const factorsData = await churnPredictionService.getChurnFactors(entityType, dateRange)
      setFactors(factorsData)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch churn factors')
    }
  }, [entityType])

  const fetchTrends = useCallback(async (dateRange: any, period: any = 'monthly') => {
    try {
      const trendsData = await churnPredictionService.getChurnTrends(entityType, dateRange, period)
      setTrends(trendsData)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch churn trends')
    }
  }, [entityType])

  return { predictions, factors, trends, loading, error, fetchFactors, fetchTrends }
}

// ============================================================================
// Hook: Customer Segmentation
// ============================================================================

export function useCustomerSegmentation(options?: { status?: string; type?: string }) {
  const [segments, setSegments] = useState<CustomerSegment[]>([])
  const [segmentDetails, setSegmentDetails] = useState<CustomerSegmentDetails | null>(null)
  const [analysis, setAnalysis] = useState<SegmentationAnalysis | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchSegments = async () => {
      setLoading(true)
      setError(null)

      try {
        const segmentsData = await customerSegmentationService.getSegments(options)
        setSegments(segmentsData)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch segments')
      } finally {
        setLoading(false)
      }
    }

    fetchSegments()
  }, [options])

  const fetchSegmentDetails = useCallback(async (segmentId: string) => {
    try {
      const details = await customerSegmentationService.getSegmentById(segmentId)
      setSegmentDetails(details)
      return details
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch segment details')
      return null
    }
  }, [])

  const runAnalysis = useCallback(async (request: any) => {
    try {
      const response = await customerSegmentationService.runAnalysis(request)
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to run analysis')
      return null
    }
  }, [])

  return { segments, segmentDetails, analysis, loading, error, fetchSegmentDetails, runAnalysis }
}

// ============================================================================
// Hook: Intelligence Analysis
// ============================================================================

export function useIntelligenceAnalysis(lookbackDays = 30) {
  const [insights, setInsights] = useState<IntelligenceInsight[]>([])
  const [trends, setTrends] = useState<InsightTrend[]>([])
  const [alerts, setAlerts] = useState<IntelligenceAlert[]>([])
  const [recommendations, setRecommendations] = useState<IntelligenceRecommendation[]>([])
  const [analysis, setAnalysis] = useState<AnalysisResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchIntelligence = async () => {
      setLoading(true)
      setError(null)

      try {
        const analysisData = await intelligenceAnalysisService.getQuickAnalysis(lookbackDays)
        setAnalysis(analysisData)
        setInsights(analysisData.insights)
        setTrends(analysisData.trends)
        setRecommendations(analysisData.recommendations)

        // Fetch active alerts
        const alertsData = await intelligenceAnalysisService.getActiveAlerts()
        setAlerts(alertsData)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch intelligence analysis')
      } finally {
        setLoading(false)
      }
    }

    fetchIntelligence()
  }, [lookbackDays])

  const acknowledgeAlert = useCallback(async (alertId: string, comment?: string) => {
    try {
      const updated = await intelligenceAnalysisService.acknowledgeAlert(alertId, comment)
      setAlerts(prev => prev.map(a => a.id === alertId ? updated : a))
      return updated
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to acknowledge alert')
      return null
    }
  }, [])

  return {
    insights,
    trends,
    alerts,
    recommendations,
    analysis,
    loading,
    error,
    acknowledgeAlert,
  }
}

// ============================================================================
// Hook: Market Basket Analysis
// ============================================================================

export function useMarketBasketAnalysis(productId?: string) {
  const [associations, setAssociations] = useState<any>(null)
  const [basketRecommendations, setBasketRecommendations] = useState<BasketRecommendation | null>(null)
  const [trending, setTrending] = useState<TrendingProductsResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchBasketData = async () => {
      setLoading(true)
      setError(null)

      try {
        if (productId) {
          const associationsData = await marketBasketAnalysisService.getProductAssociations(productId)
          setAssociations(associationsData)
        }

        const trendingData = await marketBasketAnalysisService.getTrendingProducts()
        setTrending(trendingData)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch market basket data')
      } finally {
        setLoading(false)
      }
    }

    fetchBasketData()
  }, [productId])

  const getRealTimeRecommendations = useCallback(async (products: any[]) => {
    try {
      const recs = await marketBasketAnalysisService.getRealTimeRecommendations(products)
      setBasketRecommendations(recs)
      return recs
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to get recommendations')
      return null
    }
  }, [])

  return {
    associations,
    basketRecommendations,
    trending,
    loading,
    error,
    getRealTimeRecommendations,
  }
}

// ============================================================================
// Hook: Product Recommendation
// ============================================================================

export function useProductRecommendation(userId?: string, category?: string, limit = 10) {
  const [recommendations, setRecommendations] = useState<RecommendationResponse | null>(null)
  const [personalized, setPersonalized] = useState<any>(null)
  const [collaborative, setCollaborative] = useState<any>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchRecommendations = async () => {
      setLoading(true)
      setError(null)

      try {
        const request: RecommendationRequest = { limit }

        if (userId) {
          request.userId = userId
          setPersonalized(await productRecommendationService.getPersonalizedRecommendations(userId, limit))
          setCollaborative(await productRecommendationService.getCollaborativeRecommendations(userId, limit))
        }

        if (category) {
          request.category = category
        }

        const recs = await productRecommendationService.getRecommendations(request)
        setRecommendations(recs)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch recommendations')
      } finally {
        setLoading(false)
      }
    }

    fetchRecommendations()
  }, [userId, category, limit])

  const recordClick = useCallback(async (recommendationId: string, productId: string) => {
    try {
      await productRecommendationService.recordClick(recommendationId, productId, userId)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to record click')
    }
  }, [userId])

  const recordPurchase = useCallback(async (recommendationId: string, productId: string, rating?: number) => {
    try {
      await productRecommendationService.recordPurchase(recommendationId, productId, userId, rating)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to record purchase')
    }
  }, [userId])

  return {
    recommendations,
    personalized,
    collaborative,
    loading,
    error,
    recordClick,
    recordPurchase,
  }
}

// ============================================================================
// Hook: Predictive Analytics
// ============================================================================

export function usePredictiveAnalytics(predictionType?: string) {
  const [models, setModels] = useState<PredictionModel[]>([])
  const [prediction, setPrediction] = useState<PredictionResult | null>(null)
  const [featureImportance, setFeatureImportance] = useState<FeatureImportance | null>(null)
  const [accuracy, setAccuracy] = useState<ModelAccuracy | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchPredictiveData = async () => {
      setLoading(true)
      setError(null)

      try {
        const modelsData = await predictiveAnalyticsService.getAvailableModels(predictionType as any)
        setModels(modelsData)

        if (modelsData.length > 0) {
          const modelId = modelsData[0].id
          const importanceData = await predictiveAnalyticsService.getFeatureImportance(modelId)
          setFeatureImportance(importanceData)

          const accuracyData = await predictiveAnalyticsService.getModelAccuracy(modelId)
          setAccuracy(accuracyData)
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch predictive analytics')
      } finally {
        setLoading(false)
      }
    }

    fetchPredictiveData()
  }, [predictionType])

  const runPrediction = useCallback(async (request: any) => {
    try {
      const response = await predictiveAnalyticsService.runPrediction(request)
      // Poll for result
      let result = await predictiveAnalyticsService.getPredictionResult(response.predictionId)
      while (result.status === 'PENDING' || result.status === 'PROCESSING') {
        await new Promise(resolve => setTimeout(resolve, 1000))
        result = await predictiveAnalyticsService.getPredictionResult(response.predictionId)
      }
      setPrediction(result)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to run prediction')
      return null
    }
  }, [])

  return {
    models,
    prediction,
    featureImportance,
    accuracy,
    loading,
    error,
    runPrediction,
  }
}

// ============================================================================
// Hook: Time Series Forecasting
// ============================================================================

export function useTimeSeriesForecasting() {
  const [forecast, setForecast] = useState<TimeSeriesForecastResponse | null>(null)
  const [models, setModels] = useState<ForecastModel[]>([])
  const [seasonality, setSeasonality] = useState<SeasonalityAnalysis | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchAvailableModels = async () => {
      try {
        const modelsData = await timeSeriesForecastingService.getAvailableModels()
        setModels(modelsData)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch models')
      }
    }

    fetchAvailableModels()
  }, [])

  const generateForecast = useCallback(async (request: any) => {
    setLoading(true)
    setError(null)

    try {
      const forecastData = await timeSeriesForecastingService.generateForecast(request)
      setForecast(forecastData)
      return forecastData
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to generate forecast')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  const getSeasonality = useCallback(async (kpi: string, options?: any) => {
    try {
      const seasonalityData = await timeSeriesForecastingService.getSeasonality(kpi, options)
      setSeasonality(seasonalityData)
      return seasonalityData
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to get seasonality')
      return null
    }
  }, [])

  return {
    forecast,
    models,
    seasonality,
    loading,
    error,
    generateForecast,
    getSeasonality,
  }
}

// ============================================================================
// Hook: Analytics Dashboard
// ============================================================================

export function useAnalyticsDashboard(refreshInterval = 30000) {
  const [dashboard, setDashboard] = useState<AIDashboard | null>(null)
  const [widgets, setWidgets] = useState<DashboardWidget[]>([])
  const [insights, setInsights] = useState<DashboardAIInsight[]>([])
  const [alerts, setAlerts] = useState<AIAlert[]>([])
  const [performance, setPerformance] = useState<ModelPerformance[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchDashboard = useCallback(async () => {
    setLoading(true)
    setError(null)

    try {
      const dashboardData = await analyticsDashboardService.getDashboard()
      setDashboard(dashboardData)
      setWidgets(dashboardData.widgets)
      setInsights(dashboardData.insights)
      setAlerts(dashboardData.alerts)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch dashboard')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchDashboard()

    if (refreshInterval > 0) {
      const interval = setInterval(fetchDashboard, refreshInterval)
      return () => clearInterval(interval)
    }
  }, [refreshInterval, fetchDashboard])

  const acknowledgeAlert = useCallback(async (alertId: string) => {
    try {
      const updated = await analyticsDashboardService.acknowledgeAlert(alertId)
      setAlerts(prev => prev.map(a => a.alertId === alertId ? updated : a))
      return updated
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to acknowledge alert')
      return null
    }
  }, [])

  const refresh = useCallback(async () => {
    const refreshed = await analyticsDashboardService.refreshDashboard()
    setDashboard(refreshed)
    setWidgets(refreshed.widgets)
    setInsights(refreshed.insights)
    setAlerts(refreshed.alerts)
    return refreshed
  }, [])

  return {
    dashboard,
    widgets,
    insights,
    alerts,
    performance,
    loading,
    error,
    acknowledgeAlert,
    refresh,
  }
}

// ============================================================================
// Hook: Centralized Data
// ============================================================================

export function useCentralizedData(domain?: string, autoRefresh = true) {
  const [centralizedData, setCentralizedData] = useState<CentralizedData | null>(null)
  const [domainData, setDomainData] = useState<DomainData | null>(null)
  const [health, setHealth] = useState<DataSourcesHealth | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchCentralizedData = useCallback(async () => {
    setLoading(true)
    setError(null)

    try {
      if (domain) {
        const domainDataValue = await centralizedDataService.getDomainData(domain as any)
        setDomainData(domainDataValue)
      } else {
        const data = await centralizedDataService.getAllCentralizedData()
        setCentralizedData(data)
      }

      const healthData = await centralizedDataService.getDataSourcesHealth()
      setHealth(healthData)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch centralized data')
    } finally {
      setLoading(false)
    }
  }, [domain])

  useEffect(() => {
    fetchCentralizedData()

    if (autoRefresh) {
      const interval = setInterval(fetchCentralizedData, 60000)
      return () => clearInterval(interval)
    }
  }, [fetchCentralizedData, autoRefresh])

  const refreshData = useCallback(async (sources?: string[]) => {
    try {
      const response = await centralizedDataService.refreshData({ sources })
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to refresh data')
      return null
    }
  }, [])

  return {
    centralizedData,
    domainData,
    health,
    loading,
    error,
    refreshData,
    refetch: fetchCentralizedData,
  }
}

// ============================================================================
// Hook: AI Anomalies Detection
// ============================================================================

export function useAIAnomalies(domain?: string, timeRange = '7d') {
  const [anomalies, setAnomalies] = useState<AIAnomaly[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchAnomalies = async () => {
      setLoading(true)
      setError(null)

      try {
        // TODO: Call anomaly detection service
        // const data = await anomalyDetectionService.getAnomalies({ domain, timeRange })
        // setAnomalies(data)
        setAnomalies([])
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch anomalies')
      } finally {
        setLoading(false)
      }
    }

    fetchAnomalies()
  }, [domain, timeRange])

  return { anomalies, loading, error }
}

// ============================================================================
// Hook: Customer Churn Prediction (Legacy - use useChurnPrediction)
// ============================================================================

export function useLegacyChurnPrediction(customerIds?: string[]) {
  const [predictions, setPredictions] = useState<{
    customerId: string
    churnProbability: number
    riskLevel: 'LOW' | 'MEDIUM' | 'HIGH'
    factors: string[]
  }[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!customerIds || customerIds.length === 0) return

    const fetchPredictions = async () => {
      setLoading(true)
      setError(null)

      try {
        const response = await churnPredictionService.predictCustomerChurn(customerIds)
        setPredictions(response.predictions.map(p => ({
          customerId: p.entityId,
          churnProbability: p.churnProbability,
          riskLevel: p.riskLevel,
          factors: p.factors?.map(f => f.factor) || [],
        })))
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to predict churn')
      } finally {
        setLoading(false)
      }
    }

    fetchPredictions()
  }, [customerIds])

  return { predictions, loading, error }
}

// ============================================================================
// Hook: Customer Segmentation (Legacy)
// ============================================================================

export function useLegacyCustomerSegmentation(segmentBy = 'behavior') {
  const [segments, setSegments] = useState<{
    segmentId: string
    segmentName: string
    size: number
    characteristics: Record<string, any>
    avgValue: number
  }[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchSegments = async () => {
      setLoading(true)
      setError(null)

      try {
        const segmentsData = await customerSegmentationService.getSegments()
        setSegments(segmentsData.map(s => ({
          segmentId: s.segmentId,
          segmentName: s.name,
          size: s.customerCount,
          characteristics: { type: s.type, algorithm: s.algorithm },
          avgValue: s.avgLifetimeValue,
        })))
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch segments')
      } finally {
        setLoading(false)
      }
    }

    fetchSegments()
  }, [segmentBy])

  return { segments, loading, error }
}

// ============================================================================
// Hook: Product Recommendations (Legacy)
// ============================================================================

export function useLegacyProductRecommendations(category?: string, limit = 10) {
  const [recommendations, setRecommendations] = useState<{
    productId: string
    productName: string
    score: number
    reason: string
  }[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchRecommendations = async () => {
      setLoading(true)
      setError(null)

      try {
        const recs = await productRecommendationService.getQuickRecommendations('dashboard-user', limit)
        setRecommendations(recs.recommendations.map(r => ({
          productId: r.productId,
          productName: r.productName,
          score: r.score,
          reason: r.reason || 'Personalized for you',
        })))
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch recommendations')
      } finally {
        setLoading(false)
      }
    }

    fetchRecommendations()
  }, [category, limit])

  return { recommendations, loading, error }
}

// ============================================================================
// Hook: Sentiment Analysis
// ============================================================================

export function useSentimentAnalysis(source = 'customer-feedback', period = '7d') {
  const [sentiment, setSentiment] = useState<{
    overall: 'POSITIVE' | 'NEUTRAL' | 'NEGATIVE'
    score: number // -1 to 1
    positiveCount: number
    neutralCount: number
    negativeCount: number
    trends: {
      date: string
      score: number
    }[]
  } | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchSentiment = async () => {
      setLoading(true)
      setError(null)

      try {
        // TODO: Call sentiment analysis service
        // const data = await sentimentAnalysisService.analyze({ source, period })
        // setSentiment(data)
        setSentiment(null)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to analyze sentiment')
      } finally {
        setLoading(false)
      }
    }

    fetchSentiment()
  }, [source, period])

  return { sentiment, loading, error }
}

// ============================================================================
// Hook: AI-Powered Alert Generation
// ============================================================================

export function useAIAlerts() {
  const [alerts, setAlerts] = useState<AIInsight[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchAlerts = async () => {
      setLoading(true)
      setError(null)

      try {
        // Combine insights from multiple AI services to generate alerts
        const insights: AIInsight[] = []

        // Get sales forecast alerts
        try {
          const forecast = await salesForecastingService.getQuickForecast()
          if (forecast.summary.growthRate < -5) {
            insights.push({
              id: `alert-sales-${Date.now()}`,
              type: 'sales-forecast',
              title: 'Revenue Decline Alert',
              description: `Sales forecast shows a ${forecast.summary.growthRate.toFixed(1)}% decline. Consider reviewing strategy.`,
              confidence: 'HIGH',
              confidenceScore: 85,
              category: 'REVENUE',
              generatedAt: new Date().toISOString(),
              modelName: 'sales-forecasting-ensemble',
              recommendations: [
                'Review pricing strategy',
                'Analyze customer churn',
                'Evaluate market conditions',
              ],
              impact: 'HIGH',
              actionable: true,
            })
          }
        } catch {
          // Ignore forecast errors
        }

        setAlerts(insights)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to generate AI alerts')
      } finally {
        setLoading(false)
      }
    }

    fetchAlerts()

    // Refresh every 5 minutes
    const interval = setInterval(fetchAlerts, 5 * 60 * 1000)
    return () => clearInterval(interval)
  }, [])

  return { alerts, loading, error }
}

// ============================================================================
// Helper Functions
// ============================================================================

/**
 * Convert forecast data to AI insights format
 */
function convertForecastToInsights(forecast: SalesForecastResponse): AIInsight[] {
  const insights: AIInsight[] = []

  // Revenue growth insight
  if (forecast.summary.growthRate !== 0) {
    insights.push({
      id: `forecast-revenue-${Date.now()}`,
      type: 'sales-forecast',
      title: `Revenue ${forecast.summary.growthRate > 0 ? 'Growth' : 'Decline'} Forecast`,
      description: `AI models predict ${forecast.summary.growthRate > 0 ? 'positive' : 'negative'} revenue growth of ${Math.abs(forecast.summary.growthRate).toFixed(1)}% over the forecast period.`,
      confidence: forecast.accuracy.lastAccuracy > 80 ? 'HIGH' : forecast.accuracy.lastAccuracy > 60 ? 'MEDIUM' : 'LOW',
      confidenceScore: forecast.accuracy.lastAccuracy,
      category: 'REVENUE',
      generatedAt: forecast.generatedAt,
      modelName: forecast.model,
      recommendations: forecast.drivers
        .filter(d => d.impact === 'POSITIVE')
        .map(d => `Leverage ${d.name}`),
      impact: Math.abs(forecast.summary.growthRate) > 10 ? 'HIGH' : 'MEDIUM',
      actionable: true,
    })
  }

  // Driver insights
  forecast.drivers.slice(0, 3).forEach(driver => {
    insights.push({
      id: `driver-${driver.name}-${Date.now()}`,
      type: 'forecast-driver',
      title: `${driver.name} Impact Analysis`,
      description: driver.description,
      confidence: driver.importance > 70 ? 'HIGH' : 'MEDIUM',
      confidenceScore: driver.importance,
      category: 'OPERATIONS',
      generatedAt: forecast.generatedAt,
      modelName: forecast.model,
      recommendations: [
        `Current: ${driver.currentValue.toFixed(0)}`,
        `Projected: ${driver.projectedValue.toFixed(0)}`,
        driver.impact === 'POSITIVE' ? 'Maintain focus on this area' : 'Review and optimize',
      ],
      impact: driver.importance > 70 ? 'HIGH' : 'MEDIUM',
      actionable: true,
    })
  })

  return insights
}

// Placeholder functions for other AI services
async function fetchChurnInsights(params?: Record<string, any>): Promise<AIInsight[]> {
  try {
    const factors = await churnPredictionService.getChurnFactors('CUSTOMER', params?.dateRange)
    return factors.globalFactors.map(f => ({
      id: `churn-factor-${f.factor}-${Date.now()}`,
      type: 'churn-prediction',
      title: `${f.factor} Churn Factor`,
      description: `${f.factor} has a ${f.impact} impact on churn with a correlation of ${f.correlation.toFixed(2)}`,
      confidence: f.impact === 'HIGH' ? 'HIGH' : f.impact === 'MEDIUM' ? 'MEDIUM' : 'LOW',
      confidenceScore: Math.abs(f.correlation) * 100,
      category: 'CUSTOMER',
      generatedAt: factors.generatedAt,
      modelName: 'churn-prediction-ensemble',
      recommendations: [],
      impact: f.impact,
      actionable: f.impact === 'HIGH',
    }))
  } catch {
    return []
  }
}

async function fetchSegmentationInsights(params?: Record<string, any>): Promise<AIInsight[]> {
  try {
    const segments = await customerSegmentationService.getSegments()
    return segments.slice(0, 5).map(s => ({
      id: `segment-${s.segmentId}-${Date.now()}`,
      type: 'customer-segmentation',
      title: `Segment: ${s.name}`,
      description: `${s.customerCount} customers with ${s.avgLifetimeValue.toFixed(0)} avg LTV`,
      confidence: 'HIGH',
      confidenceScore: 80,
      category: 'CUSTOMER',
      generatedAt: s.lastAnalyzed,
      modelName: s.algorithm,
      recommendations: s.retentionRate > 80 ? ['Maintain engagement strategy'] : ['Review retention tactics'],
      impact: s.customerCount > 1000 ? 'HIGH' : 'MEDIUM',
      actionable: true,
    }))
  } catch {
    return []
  }
}

async function fetchIntelligenceInsights(params?: Record<string, any>): Promise<AIInsight[]> {
  try {
    const response = await intelligenceAnalysisService.getQuickAnalysis(30)
    return response.insights.map(i => ({
      id: i.id,
      type: i.type.toLowerCase() as any,
      title: i.title,
      description: i.description,
      confidence: i.confidence > 80 ? 'HIGH' : i.confidence > 50 ? 'MEDIUM' : 'LOW',
      confidenceScore: i.confidence,
      category: i.category as any,
      generatedAt: i.detectedAt,
      modelName: 'intelligence-analysis',
      recommendations: i.recommendations || [],
      impact: i.priority as any,
      actionable: true,
    }))
  } catch {
    return []
  }
}

async function fetchSentimentInsights(params?: Record<string, any>): Promise<AIInsight[]> {
  // TODO: Implement
  return []
}

async function fetchFraudInsights(params?: Record<string, any>): Promise<AIInsight[]> {
  // TODO: Implement
  return []
}
