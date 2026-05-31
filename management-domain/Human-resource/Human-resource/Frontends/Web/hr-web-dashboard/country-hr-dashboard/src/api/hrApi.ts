import axios, { AxiosInstance } from 'axios';

// Types
export interface DashboardSummary {
  totalEmployees: number;
  totalCountries: number;
  healthScore: number;
  recruitment: RecruitmentStats;
  payroll: PayrollSummary;
  performance: PerformanceSummary;
}

export interface RecruitmentStats {
  totalJobs: number;
  totalApplicants: number;
  activePipeline: number;
  hiredThisMonth: number;
  averageTimeToFill: number;
}

export interface PayrollSummary {
  totalPayroll: number;
  totalCountries: number;
  byCountry: CountryPayroll[];
}

export interface CountryPayroll {
  countryCode: string;
  countryName: string;
  amount: number;
  status: string;
}

export interface PerformanceSummary {
  reviewCompletionRate: number;
  trainingCompletionRate: number;
  averageRating: number;
  activeCountries: number;
}

export interface Country {
  countryCode: string;
  countryName: string;
  employeeCount: number;
  healthScore: number;
  payrollAmount: number;
  status: string;
}

export interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

export interface LoginResponse {
  token: string;
  user: User;
  accessibleCountries: string[];
}

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  permissions: string[];
}

export interface OverviewMetrics {
  totalHeadcount: number;
  countries: string[];
  healthScore: number;
  growthRate: number;
}

export interface CountryMetrics {
  countryCode: string;
  metrics: EmployeeMetrics;
  financialMetrics: FinancialMetrics;
  compliance: ComplianceStatus;
}

export interface EmployeeMetrics {
  totalEmployees: number;
  activeEmployees: number;
  newHires: number;
  turnoverRate: number;
  averageTenure: number;
}

export interface FinancialMetrics {
  totalPayroll: number;
  totalBenefits: number;
  currency: string;
}

export interface ComplianceStatus {
  overallStatus: string;
  byCategory: Record<string, string>;
}

// API Configuration
const API_BASE_URL = process.env.NEXT_PUBLIC_HR_API_URL || 'http://localhost:8080';

// Create axios instance with interceptors
const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add tenant ID
apiClient.interceptors.request.use(
  (config) => {
    const tenantId = localStorage.getItem('tenantId') || 'default';
    const token = localStorage.getItem('token');

    if (tenantId) {
      config.headers['X-Tenant-ID'] = tenantId;
    }

    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      // Clear invalid token and redirect to login
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// API Functions
export const hrApi = {
  // Authentication
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>('/api/v1/hq-hr/auth/login', credentials);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await apiClient.post('/api/v1/hq-hr/auth/logout');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  // Dashboard Overview
  getOverview: async (): Promise<DashboardSummary> => {
    const response = await apiClient.get<DashboardSummary>('/api/v1/hq-hr/dashboard/overview');
    return response.data;
  },

  // Countries
  getCountries: async (): Promise<Country[]> => {
    const response = await apiClient.get<Country[]>('/api/v1/hq-hr/dashboard/countries');
    return response.data;
  },

  getCountryDetail: async (countryCode: string): Promise<any> => {
    const response = await apiClient.get(`/api/v1/hq-hr/dashboard/countries/${countryCode}`);
    return response.data;
  },

  // Recruitment
  getGlobalJobs: async (): Promise<any> => {
    const response = await apiClient.get('/api/v1/hq-hr/recruitment/global-jobs');
    return response.data;
  },

  getGlobalPipeline: async (): Promise<any> => {
    const response = await apiClient.get('/api/v1/hq-hr/recruitment/global-pipeline');
    return response.data;
  },

  // Compensation & Payroll
  getGlobalPayroll: async (): Promise<PayrollSummary> => {
    const response = await apiClient.get<PayrollSummary>('/api/v1/hq-hr/payroll/global-summary');
    return response.data;
  },

  // Performance
  getGlobalPerformance: async (): Promise<PerformanceSummary> => {
    const response = await apiClient.get<PerformanceSummary>('/api/v1/hq-hr/performance/global-status');
    return response.data;
  },

  // Analytics
  getInsights: async (): Promise<any> => {
    const response = await apiClient.get('/api/v1/hq-hr/analytics/insights');
    return response.data;
  },

  // Reports
  getReports: async (): Promise<any> => {
    const response = await apiClient.get('/api/v1/hq-hr/reports/list');
    return response.data;
  },

  generateReport: async (params: any): Promise<any> => {
    const response = await apiClient.post('/api/v1/hq-hr/reports/generate', params);
    return response.data;
  },

  // Settings
  getProfile: async (): Promise<User> => {
    const response = await apiClient.get<User>('/api/v1/hq-hr/settings/profile');
    return response.data;
  },

  updateProfile: async (profile: Partial<User>): Promise<User> => {
    const response = await apiClient.put<User>('/api/v1/hq-hr/settings/profile', profile);
    return response.data;
  },
};

export default hrApi;
