import { useQuery, useMutation, useQueryClient, UseQueryOptions } from '@tanstack/react-query';
import { useDashboardStore } from '../stores/dashboard-store';
import api, { dashboardApi, financialApi, customerApi, operationsApi, complianceApi, reportsApi } from '../services/api';
import mockApiService from '../services/mock-api';
import {
  KPIMetric,
  Region,
  Country,
  RegionalMetrics,
  FinancialReport,
  CustomerMetrics,
  OperationalMetrics,
  ComplianceMetrics,
  FilterOptions,
  ExportOptions,
} from '../types';

// Use mock API if environment variable is set
const USE_MOCK_API = import.meta.env.VITE_USE_MOCK_API === 'true' || true; // Default to mock for now

// Dashboard Queries
export function useKPIMetrics(filters?: FilterOptions, options?: Partial<UseQueryOptions<KPIMetric[], Error>>) {
  return useQuery({
    queryKey: ['kpi-metrics', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getKPIMetrics(filters);
        return response.data;
      }
      const response = await dashboardApi.getKPIMetrics(filters);
      return response.data;
    },
    staleTime: 5 * 60 * 1000, // 5 minutes
    ...options,
  });
}

export function useKPIMetric(id: string, options?: Partial<UseQueryOptions<KPIMetric, Error>>) {
  return useQuery({
    queryKey: ['kpi-metric', id],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getKPIMetric(id);
        return response.data;
      }
      const response = await dashboardApi.getKPIMetric(id);
      return response.data;
    },
    enabled: !!id,
    ...options,
  });
}

export function useRegions(filters?: FilterOptions, options?: Partial<UseQueryOptions<Region[], Error>>) {
  return useQuery({
    queryKey: ['regions', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getRegions(filters);
        return response.data;
      }
      const response = await dashboardApi.getRegions(filters);
      return response.data;
    },
    staleTime: 15 * 60 * 1000, // 15 minutes
    ...options,
  });
}

export function useRegion(id: string, options?: Partial<UseQueryOptions<Region, Error>>) {
  return useQuery({
    queryKey: ['region', id],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getRegion(id);
        return response.data;
      }
      const response = await dashboardApi.getRegion(id);
      return response.data;
    },
    enabled: !!id,
    ...options,
  });
}

export function useCountries(filters?: FilterOptions, options?: Partial<UseQueryOptions<Country[], Error>>) {
  return useQuery({
    queryKey: ['countries', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getCountries(filters);
        return response.data;
      }
      const response = await dashboardApi.getCountries(filters);
      return response.data;
    },
    staleTime: 15 * 60 * 1000,
    ...options,
  });
}

export function useCountry(id: string, options?: Partial<UseQueryOptions<Country, Error>>) {
  return useQuery({
    queryKey: ['country', id],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getCountry(id);
        return response.data;
      }
      const response = await dashboardApi.getCountry(id);
      return response.data;
    },
    enabled: !!id,
    ...options,
  });
}

export function useRegionalMetrics(filters?: FilterOptions, options?: Partial<UseQueryOptions<RegionalMetrics[], Error>>) {
  return useQuery({
    queryKey: ['regional-metrics', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getRegionalMetrics(filters);
        return response.data;
      }
      const response = await dashboardApi.getRegionalMetrics(filters);
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
}

export function useRevenueTrend(period: string = '12m', options?: Partial<UseQueryOptions<any[], Error>>) {
  return useQuery({
    queryKey: ['revenue-trend', period],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getRevenueTrend(period);
        return response.data;
      }
      const response = await dashboardApi.getRevenueTrend(period);
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
}

export function useRegionalPerformance(options?: Partial<UseQueryOptions<any[], Error>>) {
  return useQuery({
    queryKey: ['regional-performance'],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getRegionalPerformance();
        return response.data;
      }
      const response = await dashboardApi.getRegionalPerformance();
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
}

export function useMarketDistribution(options?: Partial<UseQueryOptions<any[], Error>>) {
  return useQuery({
    queryKey: ['market-distribution'],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getMarketDistribution();
        return response.data;
      }
      const response = await dashboardApi.getRegionalPerformance();
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
}

// Financial Queries
export function useFinancialReports(filters?: FilterOptions, options?: Partial<UseQueryOptions<FinancialReport[], Error>>) {
  return useQuery({
    queryKey: ['financial-reports', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getFinancialReports(filters);
        return response.data;
      }
      const response = await financialApi.getReports(filters);
      return response.data;
    },
    staleTime: 10 * 60 * 1000,
    ...options,
  });
}

export function useFinancialReport(id: string, options?: Partial<UseQueryOptions<FinancialReport, Error>>) {
  return useQuery({
    queryKey: ['financial-report', id],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getFinancialReport(id);
        return response.data;
      }
      const response = await financialApi.getReport(id);
      return response.data;
    },
    enabled: !!id,
    ...options,
  });
}

// Customer Queries
export function useCustomerMetrics(filters?: FilterOptions, options?: Partial<UseQueryOptions<CustomerMetrics, Error>>) {
  return useQuery({
    queryKey: ['customer-metrics', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getCustomerMetrics(filters);
        return response.data;
      }
      const response = await customerApi.getMetrics(filters);
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
}

export function useCustomerSegments(options?: Partial<UseQueryOptions<any[], Error>>) {
  return useQuery({
    queryKey: ['customer-segments'],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getCustomerSegments();
        return response.data;
      }
      const response = await customerApi.getSegments();
      return response.data;
    },
    staleTime: 10 * 60 * 1000,
    ...options,
  });
}

// Operations Queries
export function useOperationalMetrics(filters?: FilterOptions, options?: Partial<UseQueryOptions<OperationalMetrics, Error>>) {
  return useQuery({
    queryKey: ['operational-metrics', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getOperationalMetrics(filters);
        return response.data;
      }
      const response = await operationsApi.getMetrics(filters);
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
}

// Compliance Queries
export function useComplianceMetrics(filters?: FilterOptions, options?: Partial<UseQueryOptions<ComplianceMetrics, Error>>) {
  return useQuery({
    queryKey: ['compliance-metrics', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getComplianceMetrics(filters);
        return response.data;
      }
      const response = await complianceApi.getMetrics(filters);
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
}

// Reports Queries
export function useReports(filters?: FilterOptions, options?: Partial<UseQueryOptions<FinancialReport[], Error>>) {
  return useQuery({
    queryKey: ['reports', filters],
    queryFn: async () => {
      if (USE_MOCK_API) {
        const response = await mockApiService.getFinancialReports(filters);
        return response.data;
      }
      const response = await reportsApi.getReports(filters);
      return response.data;
    },
    staleTime: 10 * 60 * 1000,
    ...options,
  });
}

// Mutations
export function useRefreshData() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async () => {
      if (USE_MOCK_API) {
        return await mockApiService.refreshData();
      }
      return await dashboardApi.refreshData();
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['kpi-metrics'] });
      queryClient.invalidateQueries({ queryKey: ['regional-metrics'] });
      queryClient.invalidateQueries({ queryKey: ['customer-metrics'] });
      queryClient.invalidateQueries({ queryKey: ['operational-metrics'] });
      queryClient.invalidateQueries({ queryKey: ['compliance-metrics'] });
    },
  });
}

export function useExportData() {
  return useMutation({
    mutationFn: async (options: ExportOptions) => {
      if (USE_MOCK_API) {
        return await mockApiService.exportData(options);
      }
      return await dashboardApi.exportData(options);
    },
  });
}

export function useGenerateReport() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (data: Partial<FinancialReport>) => {
      if (USE_MOCK_API) {
        return await mockApiService.generateFinancialReport(data);
      }
      return await financialApi.generateReport(data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reports'] });
      queryClient.invalidateQueries({ queryKey: ['financial-reports'] });
    },
  });
}

export function useDeleteReport() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      if (!USE_MOCK_API) {
        return await reportsApi.deleteReport(id);
      }
      return { success: true };
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reports'] });
    },
  });
}

// Prefetch function
export function usePrefetchDashboardData() {
  const queryClient = useQueryClient();

  return () => {
    queryClient.prefetchQuery({ queryKey: ['kpi-metrics'], queryFn: async () => {
      const response = USE_MOCK_API
        ? await mockApiService.getKPIMetrics()
        : await dashboardApi.getKPIMetrics();
      return response.data;
    }});
    queryClient.prefetchQuery({ queryKey: ['regions'], queryFn: async () => {
      const response = USE_MOCK_API
        ? await mockApiService.getRegions()
        : await dashboardApi.getRegions();
      return response.data;
    }});
    queryClient.prefetchQuery({ queryKey: ['regional-metrics'], queryFn: async () => {
      const response = USE_MOCK_API
        ? await mockApiService.getRegionalMetrics()
        : await dashboardApi.getRegionalMetrics();
      return response.data;
    }});
  };
}
