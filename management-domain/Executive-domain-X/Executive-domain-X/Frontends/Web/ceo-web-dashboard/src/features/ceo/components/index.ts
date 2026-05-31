/**
 * CEO Dashboard Components
 *
 * Production-grade components for the CEO executive dashboard
 */

export { StrategicKPIOverview } from './strategic-kpi-overview'
export type { StrategicKPI } from './strategic-kpi-overview'

export { CrossDomainPerformanceChart } from './cross-domain-chart'
export type { DomainData, CrossDomainChartProps } from './cross-domain-chart'

export { CrisisManagementCenter } from './crisis-center'
export type { CrisisAlert, AlertSeverity, AlertStatus, CrisisManagementCenterProps } from './crisis-center'

export { AIInsightsWidget } from './ai-insights-widget'
export type { AIInsightsWidgetProps, AIInsight, AIInsightType } from './ai-insights-widget'

export { PendingApprovalsWidget } from './pending-approvals-widget'
export type { PendingApproval, ApprovalType, ApprovalPriority, ApprovalStatus, PendingApprovalsWidgetProps } from './pending-approvals-widget'

export { GlobalOperationalHeatmap } from './global-heatmap'
export type { RegionData, RegionHealth, GlobalHeatmapProps } from './global-heatmap'

// NEW: Strategic Charts
export { KPITrendsChart } from './kpi-trends-chart'
export type { KPITrendData } from './kpi-trends-chart'

export { BusinessDomainPerformanceChart } from './business-domain-performance-chart'
export type { BusinessDomainData } from './business-domain-performance-chart'

export { RegionalRevenueChart } from './regional-revenue-chart'
export type { RegionalData } from './regional-revenue-chart'
