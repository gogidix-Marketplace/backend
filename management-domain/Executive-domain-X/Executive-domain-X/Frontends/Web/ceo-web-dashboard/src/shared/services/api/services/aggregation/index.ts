/**
 * Aggregation Services
 * Exports all aggregation-related API clients
 */

export { metricsAggregationService } from './metrics-aggregation.service'
export type {
  MetricsAggregationService,
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
} from './metrics-aggregation.service'
