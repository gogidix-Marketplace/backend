/**
 * API Integration Layer - Complete Export
 *
 * All services, hooks, and types exported for easy import
 *
 * Usage:
 * ```ts
 * import { analyticsService, useAnalytics } from '@shared/services/api'
 * import { strategyService, useStrategy } from '@shared/services/api'
 * import { churnPredictionService, useChurnPrediction } from '@shared/services/api'
 * ```
 */

// ============================================================================
// Client Configuration
// ============================================================================

export * from './client/api-config'
export * from './client/axios-client'
export * from './client/types'

// ============================================================================
// Executive Services
// ============================================================================

export { analyticsService } from './services/executive/analytics.service'
export type { AnalyticsResponse } from './services/executive/analytics.service'

export { strategyService } from './services/executive/strategy.service'
export type {
  StrategicKPI,
  OKR,
  KeyResult,
  Initiative,
  Milestone,
  InitiativeRisk,
  StrategyDashboardResponse,
  CreateStrategicKPIRequest,
  UpdateStrategicKPIRequest,
  CreateOKRRequest,
  UpdateOKRRequest,
  CreateInitiativeRequest,
  UpdateInitiativeRequest,
} from './services/executive/strategy.service'

export { approvalService } from './services/executive/approval.service'
export type {
  Approval,
  ApprovalStatus,
  ApprovalType,
  ApprovalPriority,
  ApproveRequest,
  RejectRequest,
  DelegateRequest,
  ApprovalQueueResponse,
  ApprovalHistoryResponse,
  WorkflowStatus,
  PendingApprovalsSummary,
} from './services/executive/approval.service'

export { reportService } from './services/executive/report.service'
export type {
  Report,
  ReportType,
  ReportFormat,
  ReportStatus,
  ReportFrequency,
  ReportTemplate,
  ScheduleReportRequest,
  ScheduledReport,
  GenerateReportRequest,
  ReportHistory,
  ReportHistoryItem,
  ReportStatistics,
} from './services/executive/report.service'

// ============================================================================
// Data Aggregation Services
// ============================================================================

export { dataAggregationService } from './services/aggregation/data-aggregation.service'
export type {
  CrossDomainDataPoint,
  SourceDataPoint,
  DomainSummary,
  DomainMetric,
  DomainTrend,
  RealTimeMetrics,
  RealTimeDomainMetrics,
  RealTimeAlert,
  CreateAggregationRequest,
  AggregationRequest,
  AggregationStatus,
  CrossDomainResponse,
  SummaryResponse,
  RealTimeResponse,
} from './services/aggregation/data-aggregation.service'

export { metricsAggregationService } from './services/aggregation/metrics-aggregation.service'
export type {
  AggregatedKPI,
  KPISource,
  AggregationMethod,
  KPITrend,
  KPITrendAnalysis,
  TrendDataPoint,
  MetricComparison,
  ComparisonType,
  ComparisonResult,
  ComparisonInsight,
  KPIBatchRequest,
  KPIBatchResponse,
  ComparisonRequest,
  DomainKPIsResponse,
  TrendingRequest,
  TrendingResponse,
} from './services/aggregation/metrics-aggregation.service'

export { centralizedDataService } from './services/aggregation/centralized-data.service'
export type {
  CentralizedData,
  DomainCentralizedData,
  CentralizedSummary,
  DomainData,
  DomainSummary,
  DomainSource,
  KPISources,
  KPIDetailedSource,
  KPIData,
  KPISource,
  DataSourcesHealth,
  SourceStatus,
  SourceError,
  RefreshRequest,
  RefreshResponse,
} from './services/aggregation/centralized-data.service'

// ============================================================================
// AI Services - Business Intelligence
// ============================================================================

export { salesForecastingService } from './services/ai/business-intelligence/sales-forecasting.service'
export type {
  ForecastRequest,
  SalesForecastResponse,
  ForecastSummary,
  ForecastDataPoint,
  DomainForecast,
  RegionForecast,
  ForecastDriver,
  ForecastAccuracy,
  ScenarioAnalysisResponse,
} from './services/ai/business-intelligence/sales-forecasting.service'

export { churnPredictionService } from './services/ai/business-intelligence/churn-prediction.service'
export type {
  ChurnPredictionRequest,
  ChurnPredictionResponse,
  ChurnFactors,
  ChurnSegment,
  ChurnTrend,
  ChurnRiskLevel,
} from './services/ai/business-intelligence/churn-prediction.service'

export { customerSegmentationService } from './services/ai/business-intelligence/customer-segmentation.service'
export type {
  CustomerSegment,
  CustomerSegmentDetails,
  SegmentCharacteristic,
  CharacteristicAnalysis,
  SegmentationAnalysis,
  SegmentRequest,
  SegmentResponse,
  CustomerInSegment,
  SegmentComparison,
} from './services/ai/business-intelligence/customer-segmentation.service'

export { userProfilingService } from './services/ai/business-intelligence/user-profiling.service'
export type {
  UserProfile,
  ProfileSegment,
  ProfileInsight,
  UserBehavior,
  BehaviorType,
  UserInterest,
  UserPreference,
  ProfileRecommendation,
} from './services/ai/business-intelligence/user-profiling.service'

export { intelligenceAnalysisService } from './services/ai/business-intelligence/intelligence-analysis.service'
export type {
  IntelligenceInsight,
  InsightType,
  InsightPriority,
  InsightSource,
  InsightTrend,
  IntelligenceAlert,
  AnalysisRequest,
  AnalysisResponse,
  IntelligenceRecommendation,
} from './services/ai/business-intelligence/intelligence-analysis.service'

export { marketBasketAnalysisService } from './services/ai/business-intelligence/market-basket-analysis.service'
export type {
  ProductAssociation,
  AssociationRule,
  MarketBasketAnalysis,
  AssociationMetrics,
  BasketRecommendation,
  CategoryAffinity,
  AnalysisInsight,
  ActionableRecommendation,
} from './services/ai/business-intelligence/market-basket-analysis.service'

export { productRecommendationService } from './services/ai/business-intelligence/product-recommendation.service'
export type {
  RecommendationRequest,
  RecommendationResponse,
  ProductRecommendation,
  RecommendationAlgorithm,
  RecommendationReason,
  TrendingProduct,
  PersonalizedRecommendation,
} from './services/ai/business-intelligence/product-recommendation.service'

// ============================================================================
// AI Services - Data Analytics
// ============================================================================

export { predictiveAnalyticsService } from './services/ai/data-analytics/predictive-analytics.service'
export type {
  PredictionRequest,
  PredictionResponse,
  PredictionResult,
  PredictionModel,
  FeatureImportance,
  BatchPredictionRequest,
  BatchPredictionResponse,
  ModelAccuracy,
} from './services/ai/data-analytics/predictive-analytics.service'

export { timeSeriesForecastingService } from './services/ai/data-analytics/time-series-forecasting.service'
export type {
  TimeSeriesForecastRequest,
  TimeSeriesForecastResponse,
  ForecastModel,
  SeasonalityAnalysis,
  ModelComparison,
  ForecastInterval,
  SeasonalityPattern,
} from './services/ai/data-analytics/time-series-forecasting.service'

// Rename conflicting type
export type {
  ForecastDataPoint as TimeSeriesDataPoint
} from './services/ai/data-analytics/time-series-forecasting.service'

export { analyticsDashboardService } from './services/ai/data-analytics/analytics-dashboard.service'
export type {
  AIDashboard,
  DashboardWidget,
  WidgetType,
  AIInsight,
  AIAlert,
  ModelPerformance,
  DashboardConfiguration,
} from './services/ai/data-analytics/analytics-dashboard.service'

// ============================================================================
// React Hooks - Executive
// ============================================================================

export {
  useAnalytics,
  useAnalyticsByCategory,
  useTrendingKPIs,
  useTimeSeriesData,
  useRequiringAttention,
  useAnalyticsByDateRange,
  useAggregatedAnalytics,
  useAnalyticsSearch,
  useAnalyticsExport,
  useAnalyticsHealth,
} from './hooks/useAnalytics'

export {
  useStrategicKPIs,
  useStrategicKPI,
  useOKRs,
  useOKR,
  useInitiatives,
  useInitiative,
  useStrategyDashboard,
  useStrategyInsights,
  usePerformanceTrends,
} from './hooks/useStrategy'

export {
  useApprovalQueue,
  usePendingApprovals,
  useApproval,
  useApprovalHistory,
  useWorkflowStatus,
  useApprovalActions,
  useBulkActions,
  useApprovalTypes,
  useUrgentApprovals,
  useMyApprovals,
  useApprovalSearch,
  useApprovalsByStatus,
  useApprovalsByType,
  useApprovalsByPriority,
  useApprovalHealth,
  useCreateApproval,
  useUpdateApproval,
} from './hooks/useApprovals'

export {
  useReportLibrary,
  useReport,
  useGenerateReport,
  useScheduledReports,
  useReportTemplates,
  useReportHistory,
  useDownloadReport,
  useReportStatistics,
  useScheduleReport,
  useUpdateReport,
  useDeleteReport,
  useArchiveReport,
  useToggleScheduledReport,
  useReportSearch,
  useReportsByType,
  useReportHealth,
  useCreateReportTemplate,
  useUpdateReportTemplate,
  useDeleteReportTemplate,
  useRegenerateReport,
} from './hooks/useReports'

// ============================================================================
// React Hooks - AI & Aggregation
// ============================================================================

export {
  useAIInsights,
  useSalesForecast,
  useScenarioAnalysis,
  useAIAnomalies,
  useChurnPrediction,
  useCustomerSegmentation,
  useProductRecommendation,
  useSentimentAnalysis,
  useAIAlerts,
  // New AI hooks
  useUserProfiling,
  useIntelligenceAnalysis,
  useMarketBasketAnalysis,
  usePredictiveAnalytics,
  useTimeSeriesForecasting,
  useAnalyticsDashboard,
} from './hooks/useAI'

export {
  useCrossDomainData,
  useCrossDomainSummary,
  useRealTimeMetrics,
  useCentralizedData,
  useDomainData,
  useKPISources,
  useDataSourceHealth,
  useAggregatedKPI,
  useKPIBatch,
  useDomainKPIs,
  useTrendingMetrics,
  useKPITrend,
  useMetricComparison,
  useAggregationRequest,
  useAggregationRequests,
  useRefreshStatus,
  useAvailableMetrics,
  useAggregationCapabilities,
  useDataAggregationHealth,
  useDataQualityInfo,
  useHealthColor,
  useTrendDisplay,
} from './hooks/useDataAggregation'

// Re-export API_CONFIG for use in utility functions
import { API_CONFIG } from './client/api-config'

// ============================================================================
// Utility Functions
// ============================================================================

/**
 * Get API base URL for a specific role
 */
export function getRoleApiUrl(role: 'CEO' | 'CFO' | 'COO' | 'CTO'): string {
  const urls: Record<string, string> = {
    CEO: API_CONFIG.executive.analytics,
    CFO: API_CONFIG.executive.financial,
    COO: API_CONFIG.executive.operations,
    CTO: API_CONFIG.executive.technology,
  }
  return urls[role] || urls.CEO
}

/**
 * Check if API is available
 */
export async function checkApiHealth(role: 'CEO' | 'CFO' | 'COO' | 'CTO'): Promise<boolean> {
  try {
    const url = getRoleApiUrl(role)
    const response = await fetch(`${url}/api/v1/analytics/health`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    return response.ok
  } catch {
    return false
  }
}

/**
 * Format API error for display
 */
export function formatApiError(error: unknown): string {
  if (error && typeof error === 'object' && 'response' in error) {
    const err = error as { response?: { data?: { message?: string } } }
    return err.response?.data?.message || 'An unexpected error occurred'
  }
  if (error instanceof Error) {
    return error.message
  }
  return 'An unexpected error occurred'
}

/**
 * Parse API error details
 */
export function parseApiError(error: unknown): {
  message: string
  code?: string
  details?: Record<string, unknown>
} {
  if (error && typeof error === 'object' && 'response' in error) {
    const err = error as { response?: { data?: any } }
    return {
      message: err.response?.data?.message || 'An unexpected error occurred',
      code: err.response?.data?.code,
      details: err.response?.data?.details,
    }
  }
  if (error instanceof Error) {
    return { message: error.message }
  }
  return { message: 'An unexpected error occurred' }
}

// Re-export API_CONFIG for convenience
export { API_CONFIG } from './client/api-config'
