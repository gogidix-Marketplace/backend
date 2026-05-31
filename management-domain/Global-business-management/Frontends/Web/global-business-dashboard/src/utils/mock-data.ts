import {
  KPIMetric,
  Region,
  Country,
  RegionalMetrics,
  FinancialReport,
  CustomerMetrics,
  OperationalMetrics,
  ComplianceMetrics,
  ChartData,
  TimeSeriesData,
  PerformanceRating,
} from '../types';

// Generate random trend data
function generateTrendData(points: number, min: number, max: number): number[] {
  return Array.from({ length: points }, () => Math.random() * (max - min) + min);
}

// Generate time series data
function generateTimeSeriesData(months: number, baseValue: number, variance: number = 0.2): TimeSeriesData[] {
  const data: TimeSeriesData[] = [];
  const now = new Date();

  for (let i = months - 1; i >= 0; i--) {
    const date = new Date(now.getFullYear(), now.getMonth() - i, 1);
    const randomFactor = 1 + (Math.random() - 0.5) * variance;
    data.push({
      period: date.toLocaleDateString('en-US', { month: 'short', year: '2-digit' }),
      value: baseValue * randomFactor,
      target: baseValue * 1.1,
    });
  }

  return data;
}

// KPI Metrics
export const mockKPIMetrics: KPIMetric[] = [
  {
    id: 'kpi-1',
    name: 'totalRevenue',
    value: 28475000,
    previousValue: 26500000,
    change: 7.45,
    changeType: 'increase',
    unit: 'USD',
    trend: generateTrendData(12, 25000000, 29000000),
    target: 30000000,
    category: 'revenue',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-2',
    name: 'netProfit',
    value: 8425000,
    previousValue: 7950000,
    change: 5.97,
    changeType: 'increase',
    unit: 'USD',
    trend: generateTrendData(12, 7500000, 8500000),
    target: 9000000,
    category: 'profit',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-3',
    name: 'profitMargin',
    value: 29.6,
    previousValue: 30.0,
    change: -1.33,
    changeType: 'decrease',
    unit: '%',
    trend: generateTrendData(12, 28, 31),
    target: 32,
    category: 'profit',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-4',
    name: 'revenueGrowth',
    value: 12.4,
    previousValue: 9.8,
    change: 26.53,
    changeType: 'increase',
    unit: '%',
    trend: generateTrendData(12, 8, 14),
    target: 15,
    category: 'growth',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-5',
    name: 'customerCount',
    value: 48756,
    previousValue: 45234,
    change: 7.79,
    changeType: 'increase',
    unit: '',
    trend: generateTrendData(12, 42000, 49000),
    target: 50000,
    category: 'customer',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-6',
    name: 'customerRetention',
    value: 94.2,
    previousValue: 92.8,
    change: 1.51,
    changeType: 'increase',
    unit: '%',
    trend: generateTrendData(12, 91, 95),
    target: 95,
    category: 'customer',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-7',
    name: 'customerSatisfaction',
    value: 4.6,
    previousValue: 4.4,
    change: 4.55,
    changeType: 'increase',
    unit: '/5',
    trend: generateTrendData(12, 4.2, 4.7),
    target: 4.8,
    category: 'satisfaction',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-8',
    name: 'operationalEfficiency',
    value: 87.3,
    previousValue: 84.1,
    change: 3.80,
    changeType: 'increase',
    unit: '%',
    trend: generateTrendData(12, 82, 88),
    target: 90,
    category: 'operational',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-9',
    name: 'complianceScore',
    value: 96.8,
    previousValue: 95.2,
    change: 1.68,
    changeType: 'increase',
    unit: '%',
    trend: generateTrendData(12, 94, 97),
    target: 98,
    category: 'compliance',
    lastUpdated: new Date().toISOString(),
  },
  {
    id: 'kpi-10',
    name: 'ebitda',
    value: 11250000,
    previousValue: 10500000,
    change: 7.14,
    changeType: 'increase',
    unit: 'USD',
    trend: generateTrendData(12, 10000000, 11500000),
    target: 12000000,
    category: 'financial',
    lastUpdated: new Date().toISOString(),
  },
];

// Regions
export const mockRegions: Region[] = [
  {
    id: 'region-1',
    name: 'North America',
    code: 'NA',
    manager: 'Sarah Johnson',
    headquarters: 'New York, USA',
    timezone: 'America/New_York',
    countries: [],
  },
  {
    id: 'region-2',
    name: 'Europe',
    code: 'EU',
    manager: 'Hans Mueller',
    headquarters: 'Berlin, Germany',
    timezone: 'Europe/Berlin',
    countries: [],
  },
  {
    id: 'region-3',
    name: 'Asia Pacific',
    code: 'APAC',
    manager: 'Wei Chen',
    headquarters: 'Singapore',
    timezone: 'Asia/Singapore',
    countries: [],
  },
  {
    id: 'region-4',
    name: 'Latin America',
    code: 'LATAM',
    manager: 'Maria Garcia',
    headquarters: 'São Paulo, Brazil',
    timezone: 'America/Sao_Paulo',
    countries: [],
  },
  {
    id: 'region-5',
    name: 'Middle East & Africa',
    code: 'MEA',
    manager: 'Ahmed Hassan',
    headquarters: 'Dubai, UAE',
    timezone: 'Asia/Dubai',
    countries: [],
  },
];

// Countries
export const mockCountries: Country[] = [
  { id: 'us', name: 'United States', code: 'US', regionId: 'region-1', currency: 'USD', language: 'en', timezone: 'America/New_York', flag: '🇺🇸', population: 331000000, gdp: 25462700 },
  { id: 'ca', name: 'Canada', code: 'CA', regionId: 'region-1', currency: 'CAD', language: 'en', timezone: 'America/Toronto', flag: '🇨🇦', population: 38000000, gdp: 1988000 },
  { id: 'mx', name: 'Mexico', code: 'MX', regionId: 'region-1', currency: 'MXN', language: 'es', timezone: 'America/Mexico_City', flag: '🇲🇽', population: 128000000, gdp: 1293000 },
  { id: 'gb', name: 'United Kingdom', code: 'GB', regionId: 'region-2', currency: 'GBP', language: 'en', timezone: 'Europe/London', flag: '🇬🇧', population: 67000000, gdp: 3131378 },
  { id: 'de', name: 'Germany', code: 'DE', regionId: 'region-2', currency: 'EUR', language: 'de', timezone: 'Europe/Berlin', flag: '🇩🇪', population: 83000000, gdp: 4082470 },
  { id: 'fr', name: 'France', code: 'FR', regionId: 'region-2', currency: 'EUR', language: 'fr', timezone: 'Europe/Paris', flag: '🇫🇷', population: 67000000, gdp: 2781553 },
  { id: 'cn', name: 'China', code: 'CN', regionId: 'region-3', currency: 'CNY', language: 'zh', timezone: 'Asia/Shanghai', flag: '🇨🇳', population: 1400000000, gdp: 17963171 },
  { id: 'jp', name: 'Japan', code: 'JP', regionId: 'region-3', currency: 'JPY', language: 'ja', timezone: 'Asia/Tokyo', flag: '🇯🇵', population: 126000000, gdp: 4231141 },
  { id: 'au', name: 'Australia', code: 'AU', regionId: 'region-3', currency: 'AUD', language: 'en', timezone: 'Australia/Sydney', flag: '🇦🇺', population: 25000000, gdp: 1553260 },
  { id: 'br', name: 'Brazil', code: 'BR', regionId: 'region-4', currency: 'BRL', language: 'pt', timezone: 'America/Sao_Paulo', flag: '🇧🇷', population: 213000000, gdp: 1608981 },
  { id: 'ar', name: 'Argentina', code: 'AR', regionId: 'region-4', currency: 'ARS', language: 'es', timezone: 'America/Argentina/Buenos_Aires', flag: '🇦🇷', population: 45000000, gdp: 487493 },
  { id: 'ae', name: 'United Arab Emirates', code: 'AE', regionId: 'region-5', currency: 'AED', language: 'ar', timezone: 'Asia/Dubai', flag: '🇦🇪', population: 10000000, gdp: 507000 },
  { id: 'za', name: 'South Africa', code: 'ZA', regionId: 'region-5', currency: 'ZAR', language: 'en', timezone: 'Africa/Johannesburg', flag: '🇿🇦', population: 60000000, gdp: 397000 },
];

// Regional Metrics
export const mockRegionalMetrics: RegionalMetrics[] = [
  {
    regionId: 'region-1',
    regionName: 'North America',
    revenue: 12500000,
    revenueShare: 43.9,
    profit: 4200000,
    growth: 14.2,
    customers: 18756,
    satisfaction: 4.7,
    marketPenetration: 68,
    performance: 'excellent' as PerformanceRating,
    trend: generateTimeSeriesData(12, 10000000),
  },
  {
    regionId: 'region-2',
    regionName: 'Europe',
    revenue: 8750000,
    revenueShare: 30.7,
    profit: 2625000,
    growth: 8.5,
    customers: 14234,
    satisfaction: 4.5,
    marketPenetration: 52,
    performance: 'good' as PerformanceRating,
    trend: generateTimeSeriesData(12, 8000000),
  },
  {
    regionId: 'region-3',
    regionName: 'Asia Pacific',
    revenue: 5200000,
    revenueShare: 18.3,
    profit: 1248000,
    growth: 18.7,
    customers: 11234,
    satisfaction: 4.4,
    marketPenetration: 35,
    performance: 'good' as PerformanceRating,
    trend: generateTimeSeriesData(12, 4000000),
  },
  {
    regionId: 'region-4',
    regionName: 'Latin America',
    revenue: 1425000,
    revenueShare: 5.0,
    profit: 285000,
    growth: 6.3,
    customers: 3522,
    satisfaction: 4.3,
    marketPenetration: 22,
    performance: 'average' as PerformanceRating,
    trend: generateTimeSeriesData(12, 1200000),
  },
  {
    regionId: 'region-5',
    regionName: 'Middle East & Africa',
    revenue: 600000,
    revenueShare: 2.1,
    profit: 67000,
    growth: 22.4,
    customers: 1010,
    satisfaction: 4.2,
    marketPenetration: 15,
    performance: 'average' as PerformanceRating,
    trend: generateTimeSeriesData(12, 450000),
  },
];

// Customer Metrics
export const mockCustomerMetrics: CustomerMetrics = {
  total: 48756,
  new: 3522,
  churned: 890,
  retained: 45866,
  churnRate: 1.82,
  retentionRate: 94.2,
  acquisitionCost: 285,
  lifetimeValue: 4250,
  segments: [
    { segment: 'Enterprise', count: 5234, revenue: 18500000, growth: 15.2, satisfaction: 4.8 },
    { segment: 'Mid-Market', count: 15234, revenue: 7250000, growth: 12.8, satisfaction: 4.5 },
    { segment: 'Small Business', count: 28288, revenue: 2675000, growth: 8.4, satisfaction: 4.3 },
  ],
};

// Operational Metrics
export const mockOperationalMetrics: OperationalMetrics = {
  efficiency: 87.3,
  productivity: 92.1,
  utilization: 78.5,
  throughput: 95.2,
  quality: 96.8,
  onTimeDelivery: 94.5,
  inventoryTurnover: 8.4,
  orderFulfillmentTime: 2.3,
};

// Compliance Metrics
export const mockComplianceMetrics: ComplianceMetrics = {
  overallScore: 96.8,
  regulations: [
    { id: 'reg-1', name: 'GDPR', category: 'Data Privacy', status: 'compliant', lastReview: '2024-01-15', nextReview: '2024-07-15', severity: 'high' },
    { id: 'reg-2', name: 'SOX', category: 'Financial', status: 'compliant', lastReview: '2024-01-10', nextReview: '2024-04-10', severity: 'high' },
    { id: 'reg-3', name: 'ISO 27001', category: 'Security', status: 'compliant', lastReview: '2024-02-01', nextReview: '2025-02-01', severity: 'medium' },
    { id: 'reg-4', name: 'CCPA', category: 'Data Privacy', status: 'pending', lastReview: '2023-12-01', nextReview: '2024-03-01', severity: 'medium' },
  ],
  audits: [
    { id: 'audit-1', type: 'Financial', status: 'completed', date: '2024-01-20', auditor: 'Deloitte', findings: 3, critical: 0 },
    { id: 'audit-2', type: 'Security', status: 'in-progress', date: '2024-02-15', auditor: 'KPMG', findings: 0, critical: 0 },
  ],
  incidents: [
    { id: 'inc-1', type: 'Data Breach', severity: 'low', status: 'resolved', reportedAt: '2024-01-10T10:30:00Z', resolvedAt: '2024-01-12T15:45:00Z', description: 'Minor data exposure incident' },
  ],
  trainingsCompleted: 234,
  trainingsPending: 18,
};

// Chart Data
export const mockRevenueTrendData = generateTimeSeriesData(12, 2500000);

export const mockRegionalPerformanceData: ChartData[] = mockRegionalMetrics.map(m => ({
  name: m.regionName,
  value: m.revenue,
  profit: m.profit,
  growth: m.growth,
}));

export const mockMarketDistributionData: ChartData[] = mockRegionalMetrics.map(m => ({
  name: m.regionName,
  value: m.revenueShare,
}));

export const mockCustomerSegmentData = mockCustomerMetrics.segments.map(s => ({
  name: s.segment,
  value: s.count,
  revenue: s.revenue,
  satisfaction: s.satisfaction,
}));

export const mockExpenseBreakdownData: ChartData[] = [
  { name: 'Sales & Marketing', value: 8500000, percentage: 35 },
  { name: 'Operations', value: 6200000, percentage: 25 },
  { name: 'Research & Development', value: 4900000, percentage: 20 },
  { name: 'Administration', value: 3100000, percentage: 13 },
  { name: 'Other', value: 1725000, percentage: 7 },
];

// Financial Reports
export const mockFinancialReports: FinancialReport[] = [
  {
    id: 'report-1',
    period: 'Q4 2023',
    type: 'quarterly',
    status: 'published',
    revenue: {
      total: 28475000,
      byCategory: mockExpenseBreakdownData.map(d => ({ category: d.name, amount: d.value, percentage: d.percentage, change: Math.random() * 20 - 10 })),
      byRegion: mockRegionalMetrics.map(m => ({ region: m.regionName, amount: m.revenue, percentage: m.revenueShare, change: m.growth, currency: 'USD', convertedAmount: m.revenue })),
    },
    expenses: {
      total: 20050000,
      byCategory: mockExpenseBreakdownData.map(d => ({ category: d.name, amount: d.value * 0.7, percentage: d.percentage, change: Math.random() * 15 - 5 })),
      byRegion: mockRegionalMetrics.map(m => ({ region: m.regionName, amount: m.revenue * 0.7, percentage: m.revenueShare, change: m.growth * 0.8, currency: 'USD', convertedAmount: m.revenue * 0.7 })),
    },
    profit: 8425000,
    profitMargin: 29.6,
    metrics: {
      ebitda: 11250000,
      ebitdaMargin: 39.5,
      operatingCashFlow: 9750000,
      freeCashFlow: 7200000,
      debtToEquity: 0.42,
      currentRatio: 2.1,
      quickRatio: 1.4,
      returnOnAssets: 12.4,
      returnOnEquity: 18.7,
    },
    generatedAt: '2024-01-15T10:00:00Z',
    createdBy: 'System',
  },
];

// Helper function to get random data for charts
export function getRandomChartData(count: number, min: number, max: number): ChartData[] {
  const categories = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
  return Array.from({ length: count }, (_, i) => ({
    name: categories[i] || `Category ${i + 1}`,
    value: Math.random() * (max - min) + min,
  }));
}

// Helper to simulate API delay
export function delay(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms));
}

// Helper to simulate API response
export async function simulateApiResponse<T>(data: T, delayMs: number = 500): Promise<T> {
  await delay(delayMs);
  return data;
}

// Export all mock data
export const mockData = {
  kpiMetrics: mockKPIMetrics,
  regions: mockRegions,
  countries: mockCountries,
  regionalMetrics: mockRegionalMetrics,
  customerMetrics: mockCustomerMetrics,
  operationalMetrics: mockOperationalMetrics,
  complianceMetrics: mockComplianceMetrics,
  financialReports: mockFinancialReports,
  revenueTrend: mockRevenueTrendData,
  regionalPerformance: mockRegionalPerformanceData,
  marketDistribution: mockMarketDistributionData,
  customerSegments: mockCustomerSegmentData,
  expenseBreakdown: mockExpenseBreakdownData,
};
