// Repository Interfaces for HQ Sales Dashboard
// Following hexagonal architecture - domain defines repository contracts

import type {
  HQSalesUser,
  LoginRequest,
  AuthResponse,
  GlobalDashboardSummary,
  CountrySummary,
  CountryComparison,
  CountryInfo,
  GlobalPipelineSummary,
  MajorDeal,
  GlobalPerformanceData,
  GlobalTopPerformer,
  ProductPerformance,
  GlobalSalesForecast,
  ForecastPeriod,
  ForecastScenario,
  Report,
  GeneratedReport,
  ReportConfig,
  SalesTeamPartner,
  PartnerApplication,
  ApplicationStatus,
  PartnerType,
  PartnerStatus,
  PartnerTier,
  PartnerCommission,
  CommissionPeriod,
  PartnerTerritoryAssignment,
  GlobalPartnersSummary,
  PaginationInfo,
  GlobalCustomer,
  CustomerAnalytics,
  TerritoryPerformance,
  TerritoryQuota,
} from '../types';

// Auth Repository Interface
export interface IAuthRepository {
  login(credentials: LoginRequest): Promise<AuthResponse>;
  logout(): Promise<void>;
  refreshToken(refreshToken: string): Promise<{ accessToken: string; expiresIn: number }>;
  getCurrentUser(): Promise<HQSalesUser>;
  verifyToken(token: string): Promise<boolean>;
}

// Global Dashboard Repository Interface
export interface IGlobalDashboardRepository {
  getGlobalSummary(period?: string, countries?: string[]): Promise<GlobalDashboardSummary>;
  getCountrySummaries(period?: string, region?: string): Promise<CountrySummary[]>;
  getTopPerformers(period?: string, limit?: number): Promise<GlobalTopPerformer[]>;
  getMajorDeals(minValue?: number, countries?: string[], limit?: number): Promise<MajorDeal[]>;
  getAlerts(): Promise<GlobalDashboardSummary['alerts']>;
}

// Country Repository Interface
export interface ICountryRepository {
  getCountryComparison(countries: string[], period?: string, metrics?: string[]): Promise<CountryComparison>;
  getCountryDetail(code: string, period?: string): Promise<{
    country: CountryInfo;
    summary: CountrySummary;
    topPerformers: GlobalTopPerformer[];
    majorDeals: MajorDeal[];
  }>;
  getCountryRankings(metric: string, period?: string): Promise<Array<{
    rank: number;
    country: CountryInfo;
    value: number;
    display: string;
  }>>;
  getAllCountries(): Promise<CountryInfo[]>;
}

// Pipeline Repository Interface
export interface IPipelineRepository {
  getGlobalPipeline(filters?: {
    countries?: string[];
    minDealValue?: number;
    majorDealsOnly?: boolean;
  }): Promise<GlobalPipelineSummary>;
  getMajorDeals(filters?: {
    minValue?: number;
    countries?: string[];
    stages?: string[];
    limit?: number;
  }): Promise<MajorDeal[]>;
  getPipelineByStage(country?: string): Promise<GlobalPipelineSummary['byStage']>;
}

// Performance Repository Interface
export interface IPerformanceRepository {
  getGlobalPerformance(period: string, groupBy?: string): Promise<GlobalPerformanceData>;
  getTopPerformers(period?: string, limit?: number, country?: string, department?: string): Promise<GlobalTopPerformer[]>;
  getProductPerformance(period?: string, country?: string): Promise<ProductPerformance[]>;
}

// Forecasting Repository Interface
export interface IForecastingRepository {
  getForecast(scenario?: ForecastScenario): Promise<GlobalSalesForecast>;
  generateForecast(period: ForecastPeriod, options?: {
    scenario?: ForecastScenario;
    includeBestCase?: boolean;
    includeWorstCase?: boolean;
    useHistoricalTrends?: boolean;
    countries?: string[];
  }): Promise<GlobalSalesForecast>;
  getForecastAccuracy(periods?: number): Promise<Array<{
    period: string;
    accuracy: number;
    variance: number;
    actualRevenue: number;
    forecastedRevenue: number;
  }>>;
}

// Reports Repository Interface
export interface IReportsRepository {
  getReports(type?: string, category?: string): Promise<Report[]>;
  getReportById(id: string): Promise<Report>;
  createReport(report: Partial<Report>): Promise<Report>;
  updateReport(id: string, report: Partial<Report>): Promise<Report>;
  deleteReport(id: string): Promise<void>;
  generateReport(id: string, config: ReportConfig): Promise<GeneratedReport>;
  getReportHistory(id: string, limit?: number): Promise<GeneratedReport[]>;
  downloadReport(id: string, format: 'PDF' | 'EXCEL' | 'CSV'): Promise<Blob>;
}

// Partners Repository Interface
export interface IPartnersRepository {
  getPartnersSummary(period?: string, country?: string): Promise<GlobalPartnersSummary>;
  getPartners(filters?: {
    page?: number;
    pageSize?: number;
    sortBy?: string;
    sortOrder?: 'asc' | 'desc';
    search?: string;
    country?: string[];
    partnerType?: PartnerType[];
    status?: PartnerStatus[];
    tier?: PartnerTier[];
  }): Promise<{ items: SalesTeamPartner[]; pagination: PaginationInfo }>;
  getPartnerById(id: string): Promise<SalesTeamPartner>;
  getPartnerApplications(filters?: {
    page?: number;
    pageSize?: number;
    status?: ApplicationStatus[];
    country?: string[];
    partnerType?: PartnerType[];
  }): Promise<{ items: PartnerApplication[]; pagination: PaginationInfo }>;
  getApplicationById(id: string): Promise<PartnerApplication>;
  reviewApplication(id: string, action: 'APPROVE' | 'REJECT' | 'REQUEST_INFO', data?: {
    notes?: string;
    rejectionReason?: string;
    assignTerritory?: string;
  }): Promise<PartnerApplication>;
  getPartnersPerformance(period?: string, groupBy?: string): Promise<{
    summary: any;
    topPerformers: GlobalTopPerformer[];
    byCountry: any[];
    byType: any[];
    byTier: any[];
  }>;
  getPartnersCommission(period?: string, partnerId?: string, status?: string): Promise<{
    summary: any;
    breakdown: PartnerCommission[];
  }>;
  processCommission(partnerIds: string[], period: CommissionPeriod): Promise<{
    processed: number;
    totalAmount: number;
    transactions: string[];
  }>;
  getPartnersTerritory(country?: string, partnerId?: string): Promise<PartnerTerritoryAssignment[]>;
  assignTerritory(partnerId: string, data: {
    country: string;
    territories: string[];
    exclusive?: boolean;
  }): Promise<PartnerTerritoryAssignment>;
  getPartnersAnalytics(period?: string, metrics?: string[]): Promise<{
    recruitment: any;
    performance: any;
    commission: any;
    trends: any[];
  }>;
}

// Customers Repository Interface
export interface ICustomersRepository {
  getGlobalCustomers(filters?: {
    page?: number;
    pageSize?: number;
    search?: string;
    country?: string[];
    segment?: string[];
    status?: string[];
  }): Promise<{ items: GlobalCustomer[]; pagination: PaginationInfo }>;
  getCustomerById(id: string): Promise<GlobalCustomer>;
  getCustomerAnalytics(period?: string): Promise<CustomerAnalytics>;
  getMultiNationalAccounts(): Promise<GlobalCustomer[]>;
}

// Territory Repository Interface
export interface ITerritoryRepository {
  getTerritories(filters?: {
    country?: string;
    region?: string;
  }): Promise<Territory[]>;
  getTerritoryPerformance(territoryId?: string): Promise<TerritoryPerformance[]>;
  getTerritoryQuotas(period?: string): Promise<TerritoryQuota[]>;
  assignQuota(territoryId: string, quota: number, assignTo: string[]): Promise<TerritoryQuota>;
  updateTerritory(id: string, data: Partial<Territory>): Promise<Territory>;
}

// Settings Repository Interface
export interface ISettingsRepository {
  getGlobalSettings(): Promise<any>;
  updateGlobalSettings(settings: any): Promise<any>;
  getUsers(filters?: any): Promise<any[]>;
  createUser(user: any): Promise<any>;
  updateUser(id: string, user: any): Promise<any>;
  deleteUser(id: string): Promise<void>;
  getRoles(): Promise<any[]>;
  updateRole(id: string, role: any): Promise<any>;
  getCommissionStructures(): Promise<any[]>;
  updateCommissionStructure(id: string, structure: any): Promise<any>;
}
