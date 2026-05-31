// API Repository Implementations
// These implement the repository interfaces from the domain layer

import axiosClient from './axios-client';
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
  Territory,
  PaginationInfo as PaginationType,
} from '@domain/types';
import { mockGlobalDashboardSummary, mockCountrySummaries, mockTopPerformers, mockMajorDeals, mockCountryComparison, mockPipelineSummary, mockPartnersSummary, mockPartnerApplications, mockPartners, mockForecast, mockReports } from '@shared/mock-data';

// Flag to use mock data (set to true for development without backend)
const USE_MOCK_DATA = import.meta.env.VITE_USE_MOCK_DATA === 'true' || true;

// Auth Repository Implementation
class AuthRepository {
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    if (USE_MOCK_DATA) {
      // Mock login response
      return {
        user: {
          id: 'usr-001',
          email: credentials.email,
          firstName: 'John',
          lastName: 'Doe',
          avatar: 'https://i.pravatar.cc/150?img=1',
          role: 'VP_SALES',
          regionScope: 'GLOBAL',
          permissions: [],
          timezone: 'UTC',
          createdAt: new Date('2024-01-01'),
          lastLoginAt: new Date(),
        },
        accessToken: 'mock-access-token',
        refreshToken: 'mock-refresh-token',
        expiresIn: 3600,
      };
    }

    const { data } = await axiosClient.post<AuthResponse>('/auth/login', credentials);
    return data;
  }

  async logout(): Promise<void> {
    if (USE_MOCK_DATA) return;
    await axiosClient.post('/auth/logout');
  }

  async refreshToken(refreshToken: string): Promise<{ accessToken: string; expiresIn: number }> {
    if (USE_MOCK_DATA) {
      return { accessToken: 'new-mock-token', expiresIn: 3600 };
    }

    const { data } = await axiosClient.post<{ accessToken: string; expiresIn: number }>('/auth/refresh', { refreshToken });
    return data;
  }

  async getCurrentUser(): Promise<HQSalesUser> {
    if (USE_MOCK_DATA) {
      return {
        id: 'usr-001',
        email: 'john.doe@company.com',
        firstName: 'John',
        lastName: 'Doe',
        avatar: 'https://i.pravatar.cc/150?img=1',
        role: 'VP_SALES',
        regionScope: 'GLOBAL',
        permissions: [],
        timezone: 'UTC',
        createdAt: new Date('2024-01-01'),
        lastLoginAt: new Date(),
      };
    }

    const { data } = await axiosClient.get<HQSalesUser>('/auth/me');
    return data;
  }

  async verifyToken(token: string): Promise<boolean> {
    if (USE_MOCK_DATA) return true;
    const { data } = await axiosClient.post<{ valid: boolean }>('/auth/verify', { token });
    return data.valid;
  }
}

// Global Dashboard Repository Implementation
class GlobalDashboardRepository {
  async getGlobalSummary(period?: string, countries?: string[]): Promise<GlobalDashboardSummary> {
    if (USE_MOCK_DATA) return mockGlobalDashboardSummary;

    const params = new URLSearchParams();
    if (period) params.append('period', period);
    countries?.forEach(c => params.append('countries', c));

    const { data } = await axiosClient.get<GlobalDashboardSummary>(`/dashboard/global?${params}`);
    return data;
  }

  async getCountrySummaries(period?: string, region?: string): Promise<CountrySummary[]> {
    if (USE_MOCK_DATA) return mockCountrySummaries;

    const params = new URLSearchParams();
    if (period) params.append('period', period);
    if (region) params.append('region', region);

    const { data } = await axiosClient.get<CountrySummary[]>(`/dashboard/countries?${params}`);
    return data;
  }

  async getTopPerformers(period?: string, limit?: number): Promise<GlobalTopPerformer[]> {
    if (USE_MOCK_DATA) return mockTopPerformers;

    const params = new URLSearchParams();
    if (period) params.append('period', period);
    if (limit) params.append('limit', String(limit));

    const { data } = await axiosClient.get<GlobalTopPerformer[]>(`/performance/top-performers?${params}`);
    return data;
  }

  async getMajorDeals(minValue?: number, countries?: string[], limit?: number): Promise<MajorDeal[]> {
    if (USE_MOCK_DATA) return mockMajorDeals;

    const params = new URLSearchParams();
    if (minValue) params.append('minValue', String(minValue));
    countries?.forEach(c => params.append('countries', c));
    if (limit) params.append('limit', String(limit));

    const { data } = await axiosClient.get<MajorDeal[]>(`/pipeline/major-deals?${params}`);
    return data;
  }

  async getAlerts(): Promise<GlobalDashboardSummary['alerts']> {
    if (USE_MOCK_DATA) return mockGlobalDashboardSummary.alerts;

    const { data } = await axiosClient.get<GlobalDashboardSummary['alerts']>('/alerts');
    return data;
  }
}

// Country Repository Implementation
class CountryRepository {
  async getCountryComparison(countries: string[], period?: string, metrics?: string[]): Promise<CountryComparison> {
    if (USE_MOCK_DATA) return mockCountryComparison;

    const params = new URLSearchParams();
    countries.forEach(c => params.append('countries', c));
    if (period) params.append('period', period);
    metrics?.forEach(m => params.append('metrics', m));

    const { data } = await axiosClient.get<CountryComparison>(`/countries/compare?${params}`);
    return data;
  }

  async getCountryDetail(code: string, period?: string): Promise<{
    country: CountryInfo;
    summary: CountrySummary;
    topPerformers: GlobalTopPerformer[];
    majorDeals: MajorDeal[];
  }> {
    if (USE_MOCK_DATA) {
      const country = mockCountrySummaries.find(c => c.country.code === code);
      return {
        country: country?.country || mockCountrySummaries[0].country,
        summary: country || mockCountrySummaries[0],
        topPerformers: mockTopPerformers.filter(p => p.country.code === code),
        majorDeals: mockMajorDeals.filter(d => d.country.code === code),
      };
    }

    const params = new URLSearchParams();
    if (period) params.append('period', period);

    const { data } = await axiosClient.get(`/countries/${code}?${params}`);
    return data;
  }

  async getCountryRankings(metric: string, period?: string): Promise<Array<{
    rank: number;
    country: CountryInfo;
    value: number;
    display: string;
  }>> {
    if (USE_MOCK_DATA) {
      return mockCountrySummaries
        .sort((a, b) => b.metrics.revenue - a.metrics.revenue)
        .map((cs, idx) => ({
          rank: idx + 1,
          country: cs.country,
          value: cs.metrics.revenue,
          display: `$${(cs.metrics.revenue / 1000000).toFixed(1)}M`,
        }));
    }

    const params = new URLSearchParams();
    if (period) params.append('period', period);

    const { data } = await axiosClient.get(`/countries/rankings?metric=${metric}&${params}`);
    return data;
  }

  async getAllCountries(): Promise<CountryInfo[]> {
    if (USE_MOCK_DATA) {
      return mockCountrySummaries.map(cs => cs.country);
    }

    const { data } = await axiosClient.get<CountryInfo[]>('/countries');
    return data;
  }
}

// Pipeline Repository Implementation
class PipelineRepository {
  async getGlobalPipeline(filters?: {
    countries?: string[];
    minDealValue?: number;
    majorDealsOnly?: boolean;
  }): Promise<GlobalPipelineSummary> {
    if (USE_MOCK_DATA) return mockPipelineSummary;

    const params = new URLSearchParams();
    filters?.countries?.forEach(c => params.append('countries', c));
    if (filters?.minDealValue) params.append('minDealValue', String(filters.minDealValue));
    if (filters?.majorDealsOnly) params.append('majorDealsOnly', 'true');

    const { data } = await axiosClient.get<GlobalPipelineSummary>(`/pipeline/global?${params}`);
    return data;
  }

  async getMajorDeals(filters?: {
    minValue?: number;
    countries?: string[];
    stages?: string[];
    limit?: number;
  }): Promise<MajorDeal[]> {
    if (USE_MOCK_DATA) return mockMajorDeals;

    const params = new URLSearchParams();
    if (filters?.minValue) params.append('minValue', String(filters.minValue));
    filters?.countries?.forEach(c => params.append('countries', c));
    filters?.stages?.forEach(s => params.append('stages', s));
    if (filters?.limit) params.append('limit', String(filters.limit));

    const { data } = await axiosClient.get<MajorDeal[]>(`/pipeline/major-deals?${params}`);
    return data;
  }

  async getPipelineByStage(country?: string): Promise<GlobalPipelineSummary['byStage']> {
    if (USE_MOCK_DATA) return mockPipelineSummary.byStage;

    const params = new URLSearchParams();
    if (country) params.append('country', country);

    const { data } = await axiosClient.get<GlobalPipelineSummary['byStage']>(`/pipeline/by-stage?${params}`);
    return data;
  }
}

// Performance Repository Implementation
class PerformanceRepository {
  async getGlobalPerformance(period: string, groupBy?: string): Promise<GlobalPerformanceData> {
    if (USE_MOCK_DATA) {
      return {
        period: {
          start: new Date('2025-02-01'),
          end: new Date('2025-02-28'),
          type: 'monthly',
        },
        summary: {
          globalRevenue: { actual: 8500000, target: 8500000, attainment: 100, change: 15 },
          globalQuota: { assigned: 10000000, achieved: 8500000, attainment: 85, change: 12 },
          totalDeals: { closed: 1245, change: 18 },
          avgWinRate: { value: 34, change: 2 },
        },
        countryRankings: [],
        topPerformers: mockTopPerformers,
        productPerformance: [],
        trendAnalysis: [],
      };
    }

    const params = new URLSearchParams();
    params.append('period', period);
    if (groupBy) params.append('groupBy', groupBy);

    const { data } = await axiosClient.get<GlobalPerformanceData>(`/performance/global?${params}`);
    return data;
  }

  async getTopPerformers(period?: string, limit?: number, country?: string, department?: string): Promise<GlobalTopPerformer[]> {
    if (USE_MOCK_DATA) return mockTopPerformers;

    const params = new URLSearchParams();
    if (period) params.append('period', period);
    if (limit) params.append('limit', String(limit));
    if (country) params.append('country', country);
    if (department) params.append('department', department);

    const { data } = await axiosClient.get<GlobalTopPerformer[]>(`/performance/top-performers?${params}`);
    return data;
  }

  async getProductPerformance(period?: string, country?: string): Promise<ProductPerformance[]> {
    if (USE_MOCK_DATA) return [];

    const params = new URLSearchParams();
    if (period) params.append('period', period);
    if (country) params.append('country', country);

    const { data } = await axiosClient.get<ProductPerformance[]>(`/performance/products?${params}`);
    return data;
  }
}

// Forecasting Repository Implementation
class ForecastingRepository {
  async getForecast(scenario?: ForecastScenario): Promise<GlobalSalesForecast> {
    if (USE_MOCK_DATA) return mockForecast;

    const params = new URLSearchParams();
    if (scenario) params.append('scenario', scenario);

    const { data } = await axiosClient.get<GlobalSalesForecast>(`/forecasting/global?${params}`);
    return data;
  }

  async generateForecast(period: ForecastPeriod, options?: {
    scenario?: ForecastScenario;
    includeBestCase?: boolean;
    includeWorstCase?: boolean;
    useHistoricalTrends?: boolean;
    countries?: string[];
  }): Promise<GlobalSalesForecast> {
    if (USE_MOCK_DATA) return mockForecast;

    const { data } = await axiosClient.post<GlobalSalesForecast>('/forecasting/generate', {
      period,
      ...options,
    });
    return data;
  }

  async getForecastAccuracy(periods?: number): Promise<Array<{
    period: string;
    accuracy: number;
    variance: number;
    actualRevenue: number;
    forecastedRevenue: number;
  }>> {
    if (USE_MOCK_DATA) return mockForecast.accuracyMetrics;

    const params = new URLSearchParams();
    if (periods) params.append('periods', String(periods));

    const { data } = await axiosClient.get(`/forecasting/accuracy?${params}`);
    return data;
  }
}

// Reports Repository Implementation
class ReportsRepository {
  async getReports(type?: string, category?: string): Promise<Report[]> {
    if (USE_MOCK_DATA) return mockReports;

    const params = new URLSearchParams();
    if (type) params.append('type', type);
    if (category) params.append('category', category);

    const { data } = await axiosClient.get<Report[]>(`/reports?${params}`);
    return data;
  }

  async getReportById(id: string): Promise<Report> {
    const { data } = await axiosClient.get<Report>(`/reports/${id}`);
    return data;
  }

  async createReport(report: Partial<Report>): Promise<Report> {
    const { data } = await axiosClient.post<Report>('/reports', report);
    return data;
  }

  async updateReport(id: string, report: Partial<Report>): Promise<Report> {
    const { data } = await axiosClient.put<Report>(`/reports/${id}`, report);
    return data;
  }

  async deleteReport(id: string): Promise<void> {
    await axiosClient.delete(`/reports/${id}`);
  }

  async generateReport(id: string, config: ReportConfig): Promise<GeneratedReport> {
    const { data } = await axiosClient.post<GeneratedReport>(`/reports/${id}/generate`, config);
    return data;
  }

  async getReportHistory(id: string, limit?: number): Promise<GeneratedReport[]> {
    const params = new URLSearchParams();
    if (limit) params.append('limit', String(limit));

    const { data } = await axiosClient.get<GeneratedReport[]>(`/reports/${id}/history?${params}`);
    return data;
  }

  async downloadReport(id: string, format: 'PDF' | 'EXCEL' | 'CSV'): Promise<Blob> {
    const { data } = await axiosClient.get(`/reports/${id}/download`, {
      params: { format },
      responseType: 'blob',
    });
    return data;
  }
}

// Partners Repository Implementation
class PartnersRepository {
  async getPartnersSummary(period?: string, country?: string): Promise<GlobalPartnersSummary> {
    if (USE_MOCK_DATA) return mockPartnersSummary;

    const params = new URLSearchParams();
    if (period) params.append('period', period);
    if (country) params.append('country', country);

    const { data } = await axiosClient.get<GlobalPartnersSummary>(`/partners/summary?${params}`);
    return data;
  }

  async getPartners(filters?: {
    page?: number;
    pageSize?: number;
    sortBy?: string;
    sortOrder?: 'asc' | 'desc';
    search?: string;
    country?: string[];
    partnerType?: PartnerType[];
    status?: PartnerStatus[];
    tier?: PartnerTier[];
  }): Promise<{ items: SalesTeamPartner[]; pagination: PaginationInfo }> {
    if (USE_MOCK_DATA) {
      return {
        items: mockPartners,
        pagination: {
          page: 1,
          pageSize: 20,
          totalItems: mockPartners.length,
          totalPages: 1,
          hasNext: false,
          hasPrevious: false,
        },
      };
    }

    const params = new URLSearchParams();
    if (filters?.page) params.append('page', String(filters.page));
    if (filters?.pageSize) params.append('pageSize', String(filters.pageSize));
    if (filters?.sortBy) params.append('sortBy', filters.sortBy);
    if (filters?.sortOrder) params.append('sortOrder', filters.sortOrder);
    if (filters?.search) params.append('search', filters.search);
    filters?.country?.forEach(c => params.append('country', c));
    filters?.partnerType?.forEach(t => params.append('partnerType', t));
    filters?.status?.forEach(s => params.append('status', s));
    filters?.tier?.forEach(t => params.append('tier', t));

    const { data } = await axiosClient.get(`/partners?${params}`);
    return data;
  }

  async getPartnerById(id: string): Promise<SalesTeamPartner> {
    const { data } = await axiosClient.get<SalesTeamPartner>(`/partners/${id}`);
    return data;
  }

  async getPartnerApplications(filters?: {
    page?: number;
    pageSize?: number;
    status?: ApplicationStatus[];
    country?: string[];
    partnerType?: PartnerType[];
  }): Promise<{ items: PartnerApplication[]; pagination: PaginationInfo }> {
    if (USE_MOCK_DATA) {
      return {
        items: mockPartnerApplications,
        pagination: {
          page: 1,
          pageSize: 20,
          totalItems: mockPartnerApplications.length,
          totalPages: 1,
          hasNext: false,
          hasPrevious: false,
        },
      };
    }

    const params = new URLSearchParams();
    if (filters?.page) params.append('page', String(filters.page));
    if (filters?.pageSize) params.append('pageSize', String(filters.pageSize));
    filters?.status?.forEach(s => params.append('status', s));
    filters?.country?.forEach(c => params.append('country', c));
    filters?.partnerType?.forEach(t => params.append('partnerType', t));

    const { data } = await axiosClient.get(`/partners/applications?${params}`);
    return data;
  }

  async getApplicationById(id: string): Promise<PartnerApplication> {
    const { data } = await axiosClient.get<PartnerApplication>(`/partners/applications/${id}`);
    return data;
  }

  async reviewApplication(id: string, action: 'APPROVE' | 'REJECT' | 'REQUEST_INFO', data?: {
    notes?: string;
    rejectionReason?: string;
    assignTerritory?: string;
  }): Promise<PartnerApplication> {
    const { data: response } = await axiosClient.put<PartnerApplication>(`/partners/applications/${id}/review`, {
      action,
      ...data,
    });
    return response;
  }

  async getPartnersPerformance(period?: string, groupBy?: string): Promise<any> {
    if (USE_MOCK_DATA) {
      return {
        summary: mockPartnersSummary,
        topPerformers: mockPartnersSummary.topPerformers,
        byCountry: mockPartnersSummary.byCountry,
        byType: mockPartnersSummary.byPartnerType,
        byTier: mockPartnersSummary.byTier,
      };
    }

    const params = new URLSearchParams();
    if (period) params.append('period', period);
    if (groupBy) params.append('groupBy', groupBy);

    const { data } = await axiosClient.get(`/partners/performance?${params}`);
    return data;
  }

  async getPartnersCommission(period?: string, partnerId?: string, status?: string): Promise<any> {
    const params = new URLSearchParams();
    if (period) params.append('period', period);
    if (partnerId) params.append('partnerId', partnerId);
    if (status) params.append('status', status);

    const { data } = await axiosClient.get(`/partners/commission?${params}`);
    return data;
  }

  async processCommission(partnerIds: string[], period: CommissionPeriod): Promise<any> {
    const { data } = await axiosClient.post('/partners/commission/process', {
      partnerIds,
      period,
    });
    return data;
  }

  async getPartnersTerritory(country?: string, partnerId?: string): Promise<PartnerTerritoryAssignment[]> {
    const params = new URLSearchParams();
    if (country) params.append('country', country);
    if (partnerId) params.append('partnerId', partnerId);

    const { data } = await axiosClient.get<PartnerTerritoryAssignment[]>(`/partners/territory?${params}`);
    return data;
  }

  async assignTerritory(partnerId: string, data: {
    country: string;
    territories: string[];
    exclusive?: boolean;
  }): Promise<PartnerTerritoryAssignment> {
    const { data: response } = await axiosClient.post<PartnerTerritoryAssignment>('/partners/territory/assign', {
      partnerId,
      ...data,
    });
    return response;
  }

  async getPartnersAnalytics(period?: string, metrics?: string[]): Promise<any> {
    const params = new URLSearchParams();
    if (period) params.append('period', period);
    metrics?.forEach(m => params.append('metrics', m));

    const { data } = await axiosClient.get(`/partners/analytics?${params}`);
    return data;
  }
}

// Export repository instances
export const authRepository = new AuthRepository();
export const globalDashboardRepository = new GlobalDashboardRepository();
export const countryRepository = new CountryRepository();
export const pipelineRepository = new PipelineRepository();
export const performanceRepository = new PerformanceRepository();
export const forecastingRepository = new ForecastingRepository();
export const reportsRepository = new ReportsRepository();
export const partnersRepository = new PartnersRepository();
