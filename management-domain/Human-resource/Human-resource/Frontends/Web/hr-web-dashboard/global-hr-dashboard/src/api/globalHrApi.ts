import axios, { AxiosInstance } from 'axios';

// Types
export interface GlobalDashboardSummary {
  worldwideHeadcount: number;
  totalCountries: number;
  totalRegions: number;
  globalHealthScore: number;
  globalRecruitment: GlobalRecruitmentStats;
  globalPayroll: GlobalPayrollSummary;
  globalPerformance: GlobalPerformanceSummary;
  workforceTrends: WorkforceTrends;
}

export interface GlobalRecruitmentStats {
  totalActiveJobs: number;
  totalApplicants: number;
  globalPipeline: PipelineStage[];
  averageTimeToFill: number;
  costPerHire: number;
  hiresByCountry: CountryHire[];
}

export interface PipelineStage {
  stage: 'applied' | 'screened' | 'interview' | 'offer' | 'hired';
  count: number;
  percentage: number;
}

export interface CountryHire {
  countryCode: string;
  countryName: string;
  hires: number;
  applicants: number;
}

export interface GlobalPayrollSummary {
  totalPayroll: number;
  totalPayrollUSD: number;
  currencyBreakdown: CurrencyPayroll[];
  payrollByCountry: CountryPayroll[];
  payrollTrend: MonthlyPayroll[];
}

export interface CurrencyPayroll {
  currency: string;
  amount: number;
  amountUSD: number;
  percentage: number;
}

export interface CountryPayroll {
  countryCode: string;
  countryName: string;
  amount: number;
  amountUSD: number;
  currency: string;
  status: 'processed' | 'processing' | 'pending';
  variance: number;
}

export interface MonthlyPayroll {
  month: string;
  amount: number;
  headcount: number;
}

export interface GlobalPerformanceSummary {
  reviewCompletionRate: number;
  averageRating: number;
  participationRate: number;
  performanceByCountry: CountryPerformance[];
  trainingCompletion: TrainingStats;
}

export interface CountryPerformance {
  countryCode: string;
  countryName: string;
  completionRate: number;
  averageRating: number;
  participants: number;
}

export interface TrainingStats {
  totalCourses: number;
  globalCompletion: number;
  byCountry: CountryTraining[];
}

export interface CountryTraining {
  countryCode: string;
  countryName: string;
  completion: number;
  courses: number;
}

export interface WorkforceTrends {
  growthRate: number;
  turnoverRate: number;
  retentionRate: number;
  diversityIndex: number;
  monthlyHeadcount: MonthlyHeadcount[];
}

export interface MonthlyHeadcount {
  month: string;
  headcount: number;
  hires: number;
  departures: number;
}

export interface CountryComparison {
  countryCode: string;
  countryName: string;
  region: string;
  headcount: number;
  growth: number;
  healthScore: number;
  payroll: number;
  payrollPerEmployee: number;
  performanceRating: number;
  turnoverRate: number;
  trainingCompletion: number;
  complianceStatus: 'compliant' | 'warning' | 'critical';
  trends: {
    headcount: number[];
    healthScore: number[];
  };
}

export interface CountryComparisonFilters {
  regions?: string[];
  minHeadcount?: number;
  sortBy?: 'name' | 'headcount' | 'healthScore' | 'payroll';
}

export interface RegionalSummary {
  regionCode: string;
  regionName: string;
  countries: string[];
  headcount: number;
  healthScore: number;
  payroll: number;
  topPerformers: string[];
  concerns: string[];
}

export interface GlobalInsight {
  id: string;
  type: 'opportunity' | 'risk' | 'achievement' | 'alert';
  severity: 'high' | 'medium' | 'low';
  category: 'workforce' | 'recruitment' | 'payroll' | 'performance' | 'compliance';
  title: string;
  description: string;
  affectedCountries?: string[];
  recommendation?: string;
  dueDate?: string;
}

export interface GlobalReport {
  id: string;
  name: string;
  description: string;
  category: 'workforce' | 'recruitment' | 'payroll' | 'performance' | 'compliance' | 'executive';
  frequency: 'on-demand' | 'weekly' | 'monthly' | 'quarterly';
  lastRun?: string;
  nextRun?: string;
  format: 'pdf' | 'excel' | 'dashboard';
  createdBy: string;
}

export interface GlobalUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'CHRO' | 'HR_VP' | 'HR_DIRECTOR' | 'HR_ANALYST';
  permissions: string[];
  accessibleRegions: string[];
  accessibleCountries: string[];
}

export interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

export interface LoginResponse {
  token: string;
  user: GlobalUser;
  sessionTimeout: number;
}

// API Configuration
const API_BASE_URL = process.env.NEXT_PUBLIC_GLOBAL_HR_API_URL || 'http://localhost:8080';

// Create axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');

    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    // Global HR uses aggregation endpoint
    config.headers['X-Aggregation'] = 'global';

    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
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
export const globalHrApi = {
  // Authentication
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await apiClient.post<LoginResponse>('/api/v1/global-hr/auth/login', credentials);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await apiClient.post('/api/v1/global-hr/auth/logout');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  // Global Dashboard
  getGlobalOverview: async (): Promise<GlobalDashboardSummary> => {
    const response = await apiClient.get<GlobalDashboardSummary>('/api/v1/global-hr/dashboard/overview');
    return response.data;
  },

  // Country Comparison
  getCountriesComparison: async (filters?: CountryComparisonFilters): Promise<CountryComparison[]> => {
    const response = await apiClient.get<CountryComparison[]>('/api/v1/global-hr/countries/comparison', {
      params: filters,
    });
    return response.data;
  },

  getCountryDetail: async (countryCode: string): Promise<CountryComparison> => {
    const response = await apiClient.get<CountryComparison>(`/api/v1/global-hr/countries/${countryCode}`);
    return response.data;
  },

  // Regional Summary
  getRegionalSummary: async (): Promise<RegionalSummary[]> => {
    const response = await apiClient.get<RegionalSummary[]>('/api/v1/global-hr/regions/summary');
    return response.data;
  },

  // Global Recruitment
  getGlobalRecruitment: async (): Promise<GlobalRecruitmentStats> => {
    const response = await apiClient.get<GlobalRecruitmentStats>('/api/v1/global-hr/recruitment/summary');
    return response.data;
  },

  getRecruitmentByCountry: async (countryCode?: string): Promise<CountryHire[]> => {
    const response = await apiClient.get<CountryHire[]>('/api/v1/global-hr/recruitment/by-country', {
      params: { countryCode },
    });
    return response.data;
  },

  // Global Payroll
  getGlobalPayroll: async (period?: string): Promise<GlobalPayrollSummary> => {
    const response = await apiClient.get<GlobalPayrollSummary>('/api/v1/global-hr/payroll/summary', {
      params: { period },
    });
    return response.data;
  },

  getPayrollComparison: async (): Promise<CountryPayroll[]> => {
    const response = await apiClient.get<CountryPayroll[]>('/api/v1/global-hr/payroll/comparison');
    return response.data;
  },

  // Global Performance
  getGlobalPerformance: async (period?: string): Promise<GlobalPerformanceSummary> => {
    const response = await apiClient.get<GlobalPerformanceSummary>('/api/v1/global-hr/performance/summary', {
      params: { period },
    });
    return response.data;
  },

  // Global Insights
  getGlobalInsights: async (): Promise<GlobalInsight[]> => {
    const response = await apiClient.get<GlobalInsight[]>('/api/v1/global-hr/insights');
    return response.data;
  },

  // Reports
  getReports: async (): Promise<GlobalReport[]> => {
    const response = await apiClient.get<GlobalReport[]>('/api/v1/global-hr/reports');
    return response.data;
  },

  generateReport: async (reportId: string, params: any): Promise<any> => {
    const response = await apiClient.post(`/api/v1/global-hr/reports/${reportId}/generate`, params);
    return response.data;
  },

  // Settings
  getProfile: async (): Promise<GlobalUser> => {
    const response = await apiClient.get<GlobalUser>('/api/v1/global-hr/settings/profile');
    return response.data;
  },

  updateProfile: async (profile: Partial<GlobalUser>): Promise<GlobalUser> => {
    const response = await apiClient.put<GlobalUser>('/api/v1/global-hr/settings/profile', profile);
    return response.data;
  },

  // Country Management
  getCountries: async (): Promise<any[]> => {
    const response = await apiClient.get('/api/v1/global-hr/settings/countries');
    return response.data;
  },

  // Analytics Data
  getWorkforceTrends: async (months: number = 12): Promise<WorkforceTrends> => {
    const response = await apiClient.get<WorkforceTrends>('/api/v1/global-hr/analytics/workforce-trends', {
      params: { months },
    });
    return response.data;
  },

  getDiversityMetrics: async (): Promise<any> => {
    const response = await apiClient.get('/api/v1/global-hr/analytics/diversity');
    return response.data;
  },

  getRetentionAnalysis: async (): Promise<any> => {
    const response = await apiClient.get('/api/v1/global-hr/analytics/retention');
    return response.data;
  },
};

export default globalHrApi;
