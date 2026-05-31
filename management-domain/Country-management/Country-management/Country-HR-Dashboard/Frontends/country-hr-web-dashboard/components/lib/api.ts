import axios from 'axios'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1/country-hr'

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    const countryCode = localStorage.getItem('countryCode') || 'NGA'
    config.headers['X-Country-Code'] = countryCode
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized - redirect to login
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Dashboard API
export const dashboardApi = {
  getOverview: () => api.get('/dashboard/overview'),
  getHealthScore: () => api.get('/dashboard/health-score'),
}

// Employee API
export const employeeApi = {
  search: (params: any) => api.get('/employees', { params }),
  getById: (id: string) => api.get(`/employees/${id}`),
  getByEmployeeId: (employeeId: string) => api.get(`/employees/employee-id/${employeeId}`),
  create: (data: any) => api.post('/employees', data),
  update: (employeeId: string, data: any) => api.put(`/employees/${employeeId}`, data),
  terminate: (employeeId: string, data: any) => api.post(`/employees/${employeeId}/terminate`, data),
  getDepartmentStats: (countryCode: string) => api.get('/employees/stats/department', { params: { countryCode } }),
  getStatusStats: (countryCode: string) => api.get('/employees/stats/status', { params: { countryCode } }),
  getProbationEnding: (countryCode: string, days: number) =>
    api.get('/employees/probation-ending', { params: { countryCode, days } }),
}

// Leave API
export const leaveApi = {
  getRequests: (params: any) => api.get('/leave/requests', { params }),
  getPending: (countryCode: string) => api.get('/leave/requests/pending', { params: { countryCode } }),
  create: (data: any) => api.post('/leave/requests', data),
  approve: (id: string, approvedBy: string) => api.post(`/leave/requests/${id}/approve`, { approvedBy }),
  reject: (id: string, rejectionReason: string) => api.post(`/leave/requests/${id}/reject`, { rejectionReason }),
  getBalance: (employeeId: string) => api.get(`/leave/balance/${employeeId}`),
}

// Recruitment API
export const recruitmentApi = {
  getJobPostings: (params: any) => api.get('/recruitment/jobs', { params }),
  createJobPosting: (data: any) => api.post('/recruitment/jobs', data),
  updateJobPosting: (id: string, data: any) => api.put(`/recruitment/jobs/${id}`, data),
  getApplicants: (jobId: string, params: any) => api.get(`/recruitment/jobs/${jobId}/applicants`, { params }),
  updateApplicantStatus: (id: string, status: string) => api.patch(`/recruitment/applicants/${id}`, { status }),
}

// Performance API
export const performanceApi = {
  getReviews: (params: any) => api.get('/performance/reviews', { params }),
  getReview: (id: string) => api.get(`/performance/reviews/${id}`),
  createReview: (data: any) => api.post('/performance/reviews', data),
  submitReview: (id: string, data: any) => api.post(`/performance/reviews/${id}/submit`, data),
  getEmployeeReviews: (employeeId: string) => api.get(`/performance/employee/${employeeId}`),
}

// Training API
export const trainingApi = {
  getPrograms: (params: any) => api.get('/training/programs', { params }),
  getProgram: (id: string) => api.get(`/training/programs/${id}`),
  enrollEmployee: (programId: string, employeeId: string) =>
    api.post(`/training/programs/${programId}/enroll`, { employeeId }),
  updateProgress: (enrollmentId: string, data: any) =>
    api.patch(`/training/enrollments/${enrollmentId}`, data),
}

// Compliance API
export const complianceApi = {
  getRecords: (params: any) => api.get('/compliance/records', { params }),
  getPendingPolicies: (countryCode: string) =>
    api.get('/compliance/policies/pending', { params: { countryCode } }),
  acknowledgePolicy: (id: string) => api.post(`/compliance/policies/${id}/acknowledge`),
}

// Auth API
export const authApi = {
  login: (email: string, password: string) =>
    api.post('/auth/login', { email, password }),
  logout: () => api.post('/auth/logout'),
  me: () => api.get('/auth/me'),
}

export type DashboardOverview = {
  healthScore: {
    score: number
    trend: number
    level: string
  }
  employeeStats: {
    totalEmployees: number
    activeEmployees: number
    onLeave: number
    newHiresThisMonth: number
    onboarding: number
  }
  recruitmentStats: {
    activeJobs: number
    totalApplicants: number
    newApplicantsThisMonth: number
    offersSent: number
    hiredThisMonth: number
    timeToHire: number
  }
  payrollSummary: {
    currentPeriod: string
    status: string
    employeesToPay: number
    totalGrossPay: number
    totalNetPay: number
    nextPayDate: string
  }
  trainingStats: {
    activePrograms: number
    totalEnrollments: number
    completionRate: number
    averageRating: number
  }
  leaveStats: {
    pendingRequests: number
    onLeaveToday: number
    scheduledLeaves: number
  }
  complianceStats: {
    score: number
    criticalIssues: number
    warnings: number
    pendingAcknowledgments: number
  }
  alerts: Array<{
    type: string
    message: string
    priority: string
    actionUrl: string
  }>
  departmentBreakdown: Record<string, number>
}
