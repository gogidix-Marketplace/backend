// React Query Hooks
// Data fetching hooks using TanStack Query

import { useQuery, useMutation, useQueryClient, type UseQueryOptions } from '@tanstack/react-query';
import {
  authRepository,
  globalDashboardRepository,
  countryRepository,
  pipelineRepository,
  performanceRepository,
  forecastingRepository,
  reportsRepository,
  partnersRepository,
} from '../api';
import type {
  GlobalDashboardSummary,
  CountrySummary,
  CountryComparison,
  GlobalPipelineSummary,
  GlobalPerformanceData,
  GlobalTopPerformer,
  GlobalSalesForecast,
  Report,
  GeneratedReport,
  ReportConfig,
  GlobalPartnersSummary,
  PartnerApplication,
  SalesTeamPartner,
  PartnerCommission,
  PartnerTerritoryAssignment,
  LoginRequest,
  AuthResponse,
} from '@domain/types';

// Query Keys
export const queryKeys = {
  // Auth
  auth: ['auth'] as const,
  me: ['me'] as const,

  // Dashboard
  globalDashboard: (period: string) => ['dashboard', 'global', period] as const,
  countrySummaries: () => ['countries', 'summaries'] as const,

  // Countries
  countries: (filters?: string[]) => ['countries', filters] as const,
  country: (code: string) => ['country', code] as const,
  countryComparison: (countries: string[]) => ['countries', 'compare', countries.sort()] as const,
  countryRankings: (metric: string) => ['countries', 'rankings', metric] as const,

  // Pipeline
  globalPipeline: (filters?: any) => ['pipeline', 'global', filters] as const,
  majorDeals: (minValue?: number) => ['pipeline', 'major-deals', minValue] as const,

  // Performance
  globalPerformance: (period: string) => ['performance', 'global', period] as const,
  topPerformers: (limit?: number) => ['performance', 'top-performers', limit] as const,

  // Forecasting
  forecast: (scenario?: string) => ['forecast', scenario] as const,

  // Reports
  reports: () => ['reports'] as const,
  reportHistory: (id: string) => ['reports', id, 'history'] as const,

  // Partners
  partnersSummary: (period?: string) => ['partners', 'summary', period] as const,
  partners: (filters?: any) => ['partners', filters] as const,
  partnerApplications: (status?: string) => ['partners', 'applications', status] as const,
  partnerApplication: (id: string) => ['partners', 'applications', id] as const,
  partnersCommission: (period?: string) => ['partners', 'commission', period] as const,
  partnersTerritory: (country?: string) => ['partners', 'territory', country] as const,
} as const;

// Auth Hooks
export const useLogin = () => {
  return useMutation({
    mutationFn: (credentials: LoginRequest) => authRepository.login(credentials),
  });
};

export const useLogout = () => {
  return useMutation({
    mutationFn: () => authRepository.logout(),
  });
};

export const useCurrentUser = (options?: Omit<UseQueryOptions<HQSalesUser>, 'queryKey' | 'queryFn'>) => {
  return useQuery({
    queryKey: queryKeys.me,
    queryFn: () => authRepository.getCurrentUser(),
    ...options,
  });
};

// Dashboard Hooks
export const useGlobalDashboard = (
  period?: string,
  options?: Omit<UseQueryOptions<GlobalDashboardSummary>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.globalDashboard(period || 'month'),
    queryFn: () => globalDashboardRepository.getGlobalSummary(period),
    ...options,
  });
};

export const useCountrySummaries = (
  period?: string,
  options?: Omit<UseQueryOptions<CountrySummary[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.countrySummaries(),
    queryFn: () => globalDashboardRepository.getCountrySummaries(period),
    ...options,
  });
};

export const useTopPerformers = (
  period?: string,
  limit = 10,
  options?: Omit<UseQueryOptions<GlobalTopPerformer[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: ['performance', 'top-performers', period, limit],
    queryFn: () => globalDashboardRepository.getTopPerformers(period, limit),
    ...options,
  });
};

export const useMajorDeals = (
  minValue = 50000,
  options?: Omit<UseQueryOptions<GlobalDashboardSummary['majorDeals']>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.majorDeals(minValue),
    queryFn: () => globalDashboardRepository.getMajorDeals(minValue),
    ...options,
  });
};

// Country Hooks
export const useCountryComparison = (
  countries: string[],
  period?: string,
  options?: Omit<UseQueryOptions<CountryComparison>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.countryComparison(countries),
    queryFn: () => countryRepository.getCountryComparison(countries, period),
    enabled: countries.length >= 2,
    ...options,
  });
};

export const useCountryDetail = (
  code: string,
  period?: string,
  options?: Omit<UseQueryOptions<any>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.country(code),
    queryFn: () => countryRepository.getCountryDetail(code, period),
    enabled: !!code,
    ...options,
  });
};

export const useCountryRankings = (
  metric: string,
  options?: Omit<UseQueryOptions<any>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.countryRankings(metric),
    queryFn: () => countryRepository.getCountryRankings(metric),
    ...options,
  });
};

export const useAllCountries = (options?: Omit<UseQueryOptions<any>, 'queryKey' | 'queryFn'>) => {
  return useQuery({
    queryKey: queryKeys.countries(),
    queryFn: () => countryRepository.getAllCountries(),
    ...options,
  });
};

// Pipeline Hooks
export const useGlobalPipeline = (
  filters?: any,
  options?: Omit<UseQueryOptions<GlobalPipelineSummary>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.globalPipeline(filters),
    queryFn: () => pipelineRepository.getGlobalPipeline(filters),
    ...options,
  });
};

// Performance Hooks
export const useGlobalPerformance = (
  period: string,
  options?: Omit<UseQueryOptions<GlobalPerformanceData>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.globalPerformance(period),
    queryFn: () => performanceRepository.getGlobalPerformance(period),
    ...options,
  });
};

// Forecasting Hooks
export const useForecast = (
  scenario?: string,
  options?: Omit<UseQueryOptions<GlobalSalesForecast>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.forecast(scenario),
    queryFn: () => forecastingRepository.getForecast(scenario as any),
    ...options,
  });
};

export const useGenerateForecast = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: { period: any; options?: any }) =>
      forecastingRepository.generateForecast(data.period, data.options),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.forecast() });
    },
  });
};

// Reports Hooks
export const useReports = (options?: Omit<UseQueryOptions<Report[]>, 'queryKey' | 'queryFn'>) => {
  return useQuery({
    queryKey: queryKeys.reports(),
    queryFn: () => reportsRepository.getReports(),
    ...options,
  });
};

export const useGenerateReport = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, config }: { id: string; config: ReportConfig }) =>
      reportsRepository.generateReport(id, config),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.reports() });
    },
  });
};

// Partners Hooks
export const usePartnersSummary = (
  period?: string,
  options?: Omit<UseQueryOptions<GlobalPartnersSummary>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.partnersSummary(period),
    queryFn: () => partnersRepository.getPartnersSummary(period),
    ...options,
  });
};

export const usePartners = (
  filters?: any,
  options?: Omit<UseQueryOptions<{ items: SalesTeamPartner[]; pagination: any }>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.partners(filters),
    queryFn: () => partnersRepository.getPartners(filters),
    ...options,
  });
};

export const usePartnerApplications = (
  filters?: any,
  options?: Omit<UseQueryOptions<{ items: PartnerApplication[]; pagination: any }>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.partnerApplications(filters?.status?.join(',')),
    queryFn: () => partnersRepository.getPartnerApplications(filters),
    ...options,
  });
};

export const useReviewApplication = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, action, data }: { id: string; action: 'APPROVE' | 'REJECT'; data?: any }) =>
      partnersRepository.reviewApplication(id, action, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.partnerApplications() });
      queryClient.invalidateQueries({ queryKey: queryKeys.partnersSummary() });
    },
  });
};

export const usePartnersCommission = (
  period?: string,
  options?: Omit<UseQueryOptions<any>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.partnersCommission(period),
    queryFn: () => partnersRepository.getPartnersCommission(period),
    ...options,
  });
};

export const usePartnersTerritory = (
  country?: string,
  options?: Omit<UseQueryOptions<PartnerTerritoryAssignment[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.partnersTerritory(country),
    queryFn: () => partnersRepository.getPartnersTerritory(country),
    ...options,
  });
};

export const useAssignTerritory = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ partnerId, data }: { partnerId: string; data: any }) =>
      partnersRepository.assignTerritory(partnerId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.partnersTerritory() });
    },
  });
};
