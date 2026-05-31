import axios, { AxiosInstance } from 'axios';

// Types
export interface EmployeeProfile {
  id: string;
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  department: string;
  position: string;
  manager: string;
  location: string;
  startDate: string;
  status: 'active' | 'on-leave' | 'terminated';
  avatar?: string;
}

export interface LeaveBalance {
  annual: number;
  used: number;
  pending: number;
  remaining: number;
  carryOver: number;
}

export interface LeaveRequest {
  id: string;
  type: 'annual' | 'sick' | 'personal' | 'parental' | 'unpaid';
  startDate: string;
  endDate: string;
  days: number;
  reason: string;
  status: 'pending' | 'approved' | 'rejected' | 'cancelled';
  submittedDate: string;
  approver?: string;
}

export interface Payslip {
  id: string;
  period: string;
  payDate: string;
  grossPay: number;
  netPay: number;
  deductions: number;
  taxes: number;
  currency: string;
}

export interface TimesheetEntry {
  id: string;
  date: string;
  project: string;
  hours: number;
  description: string;
  status: 'submitted' | 'approved' | 'rejected';
}

export interface Benefit {
  id: string;
  name: string;
  type: string;
  description: string;
  enrollmentDate: string;
  status: 'active' | 'pending' | 'inactive';
}

export interface PerformanceReview {
  id: string;
  period: string;
  reviewer: string;
  status: 'pending' | 'self-assessment' | 'manager-review' | 'completed';
  dueDate: string;
  overallRating?: number;
}

export interface TrainingCourse {
  id: string;
  title: string;
  description: string;
  duration: number;
  status: 'not-started' | 'in-progress' | 'completed';
  progress: number;
  dueDate?: string;
  completedDate?: string;
}

// API Configuration
const API_BASE_URL = process.env.NEXT_PUBLIC_EMPLOYEE_API_URL || 'http://localhost:8080';

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
export const employeeApi = {
  // Profile
  getProfile: async (): Promise<EmployeeProfile> => {
    const response = await apiClient.get<EmployeeProfile>('/api/v1/employee/profile');
    return response.data;
  },

  updateProfile: async (data: Partial<EmployeeProfile>): Promise<EmployeeProfile> => {
    const response = await apiClient.put<EmployeeProfile>('/api/v1/employee/profile', data);
    return response.data;
  },

  // Leave Management
  getLeaveBalance: async (): Promise<LeaveBalance> => {
    const response = await apiClient.get<LeaveBalance>('/api/v1/employee/leave/balance');
    return response.data;
  },

  getLeaveRequests: async (): Promise<LeaveRequest[]> => {
    const response = await apiClient.get<LeaveRequest[]>('/api/v1/employee/leave/requests');
    return response.data;
  },

  submitLeaveRequest: async (data: Omit<LeaveRequest, 'id' | 'status' | 'submittedDate'>): Promise<LeaveRequest> => {
    const response = await apiClient.post<LeaveRequest>('/api/v1/employee/leave/requests', data);
    return response.data;
  },

  cancelLeaveRequest: async (id: string): Promise<void> => {
    await apiClient.delete(`/api/v1/employee/leave/requests/${id}`);
  },

  // Payslips
  getPayslips: async (): Promise<Payslip[]> => {
    const response = await apiClient.get<Payslip[]>('/api/v1/employee/payslips');
    return response.data;
  },

  getPayslip: async (id: string): Promise<Payslip> => {
    const response = await apiClient.get<Payslip>(`/api/v1/employee/payslips/${id}`);
    return response.data;
  },

  downloadPayslip: async (id: string): Promise<Blob> => {
    const response = await apiClient.get(`/api/v1/employee/payslips/${id}/download`, {
      responseType: 'blob',
    });
    return response.data;
  },

  // Timesheets
  getTimesheets: async (startDate: string, endDate: string): Promise<TimesheetEntry[]> => {
    const response = await apiClient.get<TimesheetEntry[]>('/api/v1/employee/timesheets', {
      params: { startDate, endDate },
    });
    return response.data;
  },

  submitTimesheet: async (entries: Omit<TimesheetEntry, 'id' | 'status'>[]): Promise<TimesheetEntry[]> => {
    const response = await apiClient.post<TimesheetEntry[]>('/api/v1/employee/timesheets', entries);
    return response.data;
  },

  // Benefits
  getBenefits: async (): Promise<Benefit[]> => {
    const response = await apiClient.get<Benefit[]>('/api/v1/employee/benefits');
    return response.data;
  },

  enrollBenefit: async (benefitId: string): Promise<Benefit> => {
    const response = await apiClient.post<Benefit>(`/api/v1/employee/benefits/${benefitId}/enroll`);
    return response.data;
  },

  // Performance
  getPerformanceReviews: async (): Promise<PerformanceReview[]> => {
    const response = await apiClient.get<PerformanceReview[]>('/api/v1/employee/performance/reviews');
    return response.data;
  },

  submitSelfAssessment: async (reviewId: string, data: any): Promise<PerformanceReview> => {
    const response = await apiClient.post<PerformanceReview>(
      `/api/v1/employee/performance/reviews/${reviewId}/self-assessment`,
      data
    );
    return response.data;
  },

  // Training
  getTrainingCourses: async (): Promise<TrainingCourse[]> => {
    const response = await apiClient.get<TrainingCourse[]>('/api/v1/employee/training/courses');
    return response.data;
  },

  getTrainingCourse: async (id: string): Promise<TrainingCourse> => {
    const response = await apiClient.get<TrainingCourse>(`/api/v1/employee/training/courses/${id}`);
    return response.data;
  },

  updateTrainingProgress: async (id: string, progress: number): Promise<TrainingCourse> => {
    const response = await apiClient.put<TrainingCourse>(
      `/api/v1/employee/training/courses/${id}/progress`,
      { progress }
    );
    return response.data;
  },

  // Documents
  getDocuments: async (): Promise<any[]> => {
    const response = await apiClient.get('/api/v1/employee/documents');
    return response.data;
  },

  uploadDocument: async (file: File, type: string): Promise<any> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    const response = await apiClient.post('/api/v1/employee/documents/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  downloadDocument: async (id: string): Promise<Blob> => {
    const response = await apiClient.get(`/api/v1/employee/documents/${id}/download`, {
      responseType: 'blob',
    });
    return response.data;
  },
};

export default employeeApi;
