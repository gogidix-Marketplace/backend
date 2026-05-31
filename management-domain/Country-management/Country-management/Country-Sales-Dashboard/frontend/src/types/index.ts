export interface Customer {
  id: string;
  customerCode: string;
  companyName: string;
  industry?: string;
  status: string;
  tier?: string;
  annualRevenue?: number;
  accountManagerName?: string;
  lastContactDate?: string;
  createdAt: string;
}

export interface Deal {
  id: string;
  dealCode: string;
  title: string;
  customerName: string;
  stage: string;
  priority: string;
  value: number;
  weightedValue: number;
  probability: number;
  expectedCloseDate: string;
  ownerName?: string;
  overdue?: boolean;
  createdAt: string;
}

export interface SalesMetrics {
  revenue: {
    totalRevenue: number;
    averageDealSize: number;
    revenueGrowth?: number;
  };
  pipeline: {
    totalPipeline: number;
    weightedPipeline: number;
    totalOpenDeals: number;
    conversionRate: number;
    newDeals: number;
    closedWon: number;
    closedLost: number;
    dealsByStage: Record<string, number>;
  };
  customer: {
    totalCustomers: number;
    activeCustomers: number;
    customersByTier: Record<string, number>;
  };
  team: {
    totalMembers: number;
    averageWinRate: number;
    topPerformer?: string;
  };
  target: {
    monthlyTarget: number;
    monthlyAchieved: number;
    monthlyProgress: number;
    daysRemaining: number;
  };
  asOfDate: string;
}

export interface PipelineSummary {
  totalOpenDeals: number;
  totalPipelineValue: number;
  weightedPipelineValue: number;
  averageDealSize: number;
}

export interface DashboardSummary {
  totalCustomers: number;
  openDeals: number;
  revenueThisMonth: number;
  revenueYTD: number;
  overdueDeals: number;
  asOfDate: string;
}

export interface SalesTarget {
  id: string;
  targetCode: string;
  name: string;
  period: string;
  startDate: string;
  endDate: string;
  revenueTarget: number;
  revenueAchieved: number;
  status: string;
}
