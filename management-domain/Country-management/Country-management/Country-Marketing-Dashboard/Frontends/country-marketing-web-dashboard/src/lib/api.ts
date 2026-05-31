import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
    'X-Tenant-Id': 'default-tenant',
    'X-Country': 'NG',
  },
});

// Dashboard API
export const dashboardApi = {
  getQuickStats: async () => {
    const response = await api.get('/api/v1/dashboard/quick-stats');
    return response.data.data;
  },

  getDashboard: async (periodStart?: string, periodEnd?: string) => {
    const params: any = {};
    if (periodStart) params.periodStart = periodStart;
    if (periodEnd) params.periodEnd = periodEnd;
    const response = await api.get('/api/v1/dashboard', { params });
    return response.data.data;
  },

  getOverview: async (periodStart?: string, periodEnd?: string) => {
    const params: any = {};
    if (periodStart) params.periodStart = periodStart;
    if (periodEnd) params.periodEnd = periodEnd;
    const response = await api.get('/api/v1/dashboard/overview', { params });
    return response.data.data;
  },

  getCampaignMetrics: async () => {
    const response = await api.get('/api/v1/dashboard/campaigns/metrics');
    return response.data.data;
  },

  getLeadMetrics: async () => {
    const response = await api.get('/api/v1/dashboard/leads/metrics');
    return response.data.data;
  },

  getBudgetMetrics: async () => {
    const response = await api.get('/api/v1/dashboard/budget/metrics');
    return response.data.data;
  },

  getROIMetrics: async () => {
    const response = await api.get('/api/v1/dashboard/roi/metrics');
    return response.data.data;
  },

  getTopCampaigns: async () => {
    const response = await api.get('/api/v1/dashboard/top-campaigns');
    return response.data.data;
  },

  getRecentActivities: async () => {
    const response = await api.get('/api/v1/dashboard/activities/recent');
    return response.data.data;
  },

  getAlerts: async () => {
    const response = await api.get('/api/v1/dashboard/alerts');
    return response.data.data;
  },
};

// Campaign API
export const campaignApi = {
  getAll: async (page = 0, size = 20) => {
    const response = await api.get('/api/v1/campaigns', { params: { page, size } });
    return response.data.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/api/v1/campaigns/${id}`);
    return response.data.data;
  },

  getByStatus: async (status: string) => {
    const response = await api.get(`/api/v1/campaigns/status/${status}`);
    return response.data.data;
  },

  getActive: async () => {
    const response = await api.get('/api/v1/campaigns/active');
    return response.data.data;
  },

  create: async (data: any) => {
    const response = await api.post('/api/v1/campaigns', data);
    return response.data.data;
  },

  update: async (id: string, data: any) => {
    const response = await api.put(`/api/v1/campaigns/${id}`, data);
    return response.data.data;
  },

  updateStatus: async (id: string, status: string) => {
    const response = await api.patch(`/api/v1/campaigns/${id}/status`, null, { params: { status } });
    return response.data.data;
  },

  launch: async (id: string) => {
    const response = await api.post(`/api/v1/campaigns/${id}/launch`);
    return response.data.data;
  },

  pause: async (id: string) => {
    const response = await api.post(`/api/v1/campaigns/${id}/pause`);
    return response.data.data;
  },

  delete: async (id: string) => {
    await api.delete(`/api/v1/campaigns/${id}`);
  },

  recordSpend: async (id: string, amount: number) => {
    const response = await api.post(`/api/v1/campaigns/${id}/spend`, null, { params: { amount } });
    return response.data.data;
  },

  search: async (query: string, page = 0, size = 20) => {
    const response = await api.get('/api/v1/campaigns/search', { params: { query, page, size } });
    return response.data.data;
  },

  getStatistics: async () => {
    const response = await api.get('/api/v1/campaigns/statistics/summary');
    return response.data.data;
  },
};

// Lead API
export const leadApi = {
  getAll: async (page = 0, size = 20) => {
    const response = await api.get('/api/v1/leads', { params: { page, size } });
    return response.data.data;
  },

  getById: async (id: string) => {
    const response = await api.get(`/api/v1/leads/${id}`);
    return response.data.data;
  },

  getByNumber: async (leadNumber: string) => {
    const response = await api.get(`/api/v1/leads/number/${leadNumber}`);
    return response.data.data;
  },

  getByStatus: async (status: string) => {
    const response = await api.get(`/api/v1/leads/status/${status}`);
    return response.data.data;
  },

  getHot: async () => {
    const response = await api.get('/api/v1/leads/hot');
    return response.data.data;
  },

  create: async (data: any) => {
    const response = await api.post('/api/v1/leads', data);
    return response.data.data;
  },

  updateStatus: async (id: string, status: string, notes?: string) => {
    const response = await api.patch(`/api/v1/leads/${id}/status`, { status, notes });
    return response.data.data;
  },

  assign: async (id: string, assignedTo: string, assignedToName: string, notes?: string) => {
    const response = await api.post(`/api/v1/leads/${id}/assign`, { assignedTo, assignedToName, notes });
    return response.data.data;
  },

  recordEngagement: async (id: string, engagementType: string) => {
    const response = await api.post(`/api/v1/leads/${id}/engagement`, null, { params: { engagementType } });
    return response.data.data;
  },

  delete: async (id: string) => {
    await api.delete(`/api/v1/leads/${id}`);
  },

  getUnassigned: async () => {
    const response = await api.get('/api/v1/leads/unassigned');
    return response.data.data;
  },

  getByAssignee: async (assignee: string) => {
    const response = await api.get(`/api/v1/leads/assigned/${assignee}`);
    return response.data.data;
  },

  search: async (query: string, page = 0, size = 20) => {
    const response = await api.get('/api/v1/leads/search', { params: { query, page, size } });
    return response.data.data;
  },

  getStale: async (days = 30) => {
    const response = await api.get('/api/v1/leads/stale', { params: { days } });
    return response.data.data;
  },

  getStatistics: async () => {
    const response = await api.get('/api/v1/leads/statistics/summary');
    return response.data.data;
  },
};

export default api;
