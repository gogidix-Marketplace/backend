import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import {
  KPIMetric,
  Region,
  Country,
  RegionalMetrics,
  FinancialReport,
  CustomerMetrics,
  OperationalMetrics,
  ComplianceMetrics,
  ApiResponse,
  FilterOptions,
  ExportOptions,
  User,
  Notification,
} from '../types';

// API Configuration
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8000/api/v1';
const API_TIMEOUT = 30000;

// Create axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: API_TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // Add auth token if available
    const token = localStorage.getItem('auth_token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: AxiosError) => Promise.reject(error)
);

// Response interceptor
apiClient.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Handle unauthorized
      localStorage.removeItem('auth_token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// API Error Handler
export class APIError extends Error {
  constructor(
    public message: string,
    public statusCode: number,
    public details?: any
  ) {
    super(message);
    this.name = 'APIError';
  }
}

// Dashboard API
export const dashboardApi = {
  // KPI Metrics
  async getKPIMetrics(filters?: FilterOptions): Promise<ApiResponse<KPIMetric[]>> {
    try {
      const response = await apiClient.get<ApiResponse<KPIMetric[]>>('/dashboard/kpi', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getKPIMetric(id: string): Promise<ApiResponse<KPIMetric>> {
    try {
      const response = await apiClient.get<ApiResponse<KPIMetric>>(`/dashboard/kpi/${id}`);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  // Regional Data
  async getRegions(filters?: FilterOptions): Promise<ApiResponse<Region[]>> {
    try {
      const response = await apiClient.get<ApiResponse<Region[]>>('/dashboard/regions', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getRegion(id: string): Promise<ApiResponse<Region>> {
    try {
      const response = await apiClient.get<ApiResponse<Region>>(`/dashboard/regions/${id}`);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  // Country Data
  async getCountries(filters?: FilterOptions): Promise<ApiResponse<Country[]>> {
    try {
      const response = await apiClient.get<ApiResponse<Country[]>>('/dashboard/countries', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getCountry(id: string): Promise<ApiResponse<Country>> {
    try {
      const response = await apiClient.get<ApiResponse<Country>>(`/dashboard/countries/${id}`);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  // Regional Metrics
  async getRegionalMetrics(filters?: FilterOptions): Promise<ApiResponse<RegionalMetrics[]>> {
    try {
      const response = await apiClient.get<ApiResponse<RegionalMetrics[]>>('/dashboard/regional-metrics', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  // Charts & Analytics
  async getRevenueTrend(period: string): Promise<ApiResponse<any[]>> {
    try {
      const response = await apiClient.get<ApiResponse<any[]>>('/dashboard/charts/revenue-trend', {
        params: { period },
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getRegionalPerformance(): Promise<ApiResponse<any[]>> {
    try {
      const response = await apiClient.get<ApiResponse<any[]>>('/dashboard/charts/regional-performance');
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  // Export Data
  async exportData(options: ExportOptions): Promise<Blob> {
    try {
      const response = await apiClient.post('/dashboard/export', options, {
        responseType: 'blob',
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  // Refresh Data
  async refreshData(): Promise<ApiResponse<{ success: boolean }>> {
    try {
      const response = await apiClient.post<ApiResponse<{ success: boolean }>>('/dashboard/refresh');
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },
};

// Financial API
export const financialApi = {
  async getReports(filters?: FilterOptions): Promise<ApiResponse<FinancialReport[]>> {
    try {
      const response = await apiClient.get<ApiResponse<FinancialReport[]>>('/financial/reports', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getReport(id: string): Promise<ApiResponse<FinancialReport>> {
    try {
      const response = await apiClient.get<ApiResponse<FinancialReport>>(`/financial/reports/${id}`);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async generateReport(data: Partial<FinancialReport>): Promise<ApiResponse<FinancialReport>> {
    try {
      const response = await apiClient.post<ApiResponse<FinancialReport>>('/financial/reports', data);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getMetrics(filters?: FilterOptions): Promise<ApiResponse<any>> {
    try {
      const response = await apiClient.get<ApiResponse<any>>('/financial/metrics', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },
};

// Customer API
export const customerApi = {
  async getMetrics(filters?: FilterOptions): Promise<ApiResponse<CustomerMetrics>> {
    try {
      const response = await apiClient.get<ApiResponse<CustomerMetrics>>('/customers/metrics', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getSegments(filters?: FilterOptions): Promise<ApiResponse<any[]>> {
    try {
      const response = await apiClient.get<ApiResponse<any[]>>('/customers/segments', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getFeedback(filters?: FilterOptions): Promise<ApiResponse<any[]>> {
    try {
      const response = await apiClient.get<ApiResponse<any[]>>('/customers/feedback', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },
};

// Operations API
export const operationsApi = {
  async getMetrics(filters?: FilterOptions): Promise<ApiResponse<OperationalMetrics>> {
    try {
      const response = await apiClient.get<ApiResponse<OperationalMetrics>>('/operations/metrics', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },
};

// Compliance API
export const complianceApi = {
  async getMetrics(filters?: FilterOptions): Promise<ApiResponse<ComplianceMetrics>> {
    try {
      const response = await apiClient.get<ApiResponse<ComplianceMetrics>>('/compliance/metrics', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getRegulations(filters?: FilterOptions): Promise<ApiResponse<any[]>> {
    try {
      const response = await apiClient.get<ApiResponse<any[]>>('/compliance/regulations', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getAudits(filters?: FilterOptions): Promise<ApiResponse<any[]>> {
    try {
      const response = await apiClient.get<ApiResponse<any[]>>('/compliance/audits', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getIncidents(filters?: FilterOptions): Promise<ApiResponse<any[]>> {
    try {
      const response = await apiClient.get<ApiResponse<any[]>>('/compliance/incidents', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },
};

// User API
export const userApi = {
  async getProfile(): Promise<ApiResponse<User>> {
    try {
      const response = await apiClient.get<ApiResponse<User>>('/users/profile');
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async updateProfile(data: Partial<User>): Promise<ApiResponse<User>> {
    try {
      const response = await apiClient.put<ApiResponse<User>>('/users/profile', data);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getNotifications(): Promise<ApiResponse<Notification[]>> {
    try {
      const response = await apiClient.get<ApiResponse<Notification[]>>('/users/notifications');
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async markNotificationRead(id: string): Promise<ApiResponse<void>> {
    try {
      const response = await apiClient.put<ApiResponse<void>>(`/users/notifications/${id}/read`);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async markAllNotificationsRead(): Promise<ApiResponse<void>> {
    try {
      const response = await apiClient.put<ApiResponse<void>>('/users/notifications/read-all');
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getSettings(): Promise<ApiResponse<any>> {
    try {
      const response = await apiClient.get<ApiResponse<any>>('/users/settings');
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async updateSettings(data: any): Promise<ApiResponse<any>> {
    try {
      const response = await apiClient.put<ApiResponse<any>>('/users/settings', data);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },
};

// Reports API
export const reportsApi = {
  async getReports(filters?: FilterOptions): Promise<ApiResponse<FinancialReport[]>> {
    try {
      const response = await apiClient.get<ApiResponse<FinancialReport[]>>('/reports', {
        params: filters,
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async getReport(id: string): Promise<ApiResponse<FinancialReport>> {
    try {
      const response = await apiClient.get<ApiResponse<FinancialReport>>(`/reports/${id}`);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async generateReport(data: any): Promise<ApiResponse<FinancialReport>> {
    try {
      const response = await apiClient.post<ApiResponse<FinancialReport>>('/reports/generate', data);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async downloadReport(id: string, format: string): Promise<Blob> {
    try {
      const response = await apiClient.get(`/reports/${id}/download`, {
        params: { format },
        responseType: 'blob',
      });
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async scheduleReport(data: any): Promise<ApiResponse<any>> {
    try {
      const response = await apiClient.post<ApiResponse<any>>('/reports/schedule', data);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },

  async deleteReport(id: string): Promise<ApiResponse<void>> {
    try {
      const response = await apiClient.delete<ApiResponse<void>>(`/reports/${id}`);
      return response.data;
    } catch (error) {
      throw handleError(error);
    }
  },
};

// Error Handler
function handleError(error: unknown): APIError {
  if (axios.isAxiosError(error)) {
    const message = error.response?.data?.message || error.message || 'An unexpected error occurred';
    const statusCode = error.response?.status || 500;
    return new APIError(message, statusCode, error.response?.data);
  }
  return new APIError('An unexpected error occurred', 500);
}

// Export all APIs
export const api = {
  dashboard: dashboardApi,
  financial: financialApi,
  customer: customerApi,
  operations: operationsApi,
  compliance: complianceApi,
  user: userApi,
  reports: reportsApi,
};

export default apiClient;
