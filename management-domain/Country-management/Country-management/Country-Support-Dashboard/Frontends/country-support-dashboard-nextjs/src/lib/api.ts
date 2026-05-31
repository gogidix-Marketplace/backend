import axios, { AxiosInstance, AxiosError } from 'axios';
import type { ErrorResponse } from '@/types';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

class ApiClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor to add auth token
    this.client.interceptors.request.use((config) => {
      const token = localStorage.getItem('auth_token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    });

    // Response interceptor for error handling
    this.client.interceptors.response.use(
      (response) => response,
      (error: AxiosError<ErrorResponse>) => {
        if (error.response) {
          console.error('API Error:', error.response.data);
          return Promise.reject(error.response.data);
        }
        return Promise.reject(error);
      }
    );
  }

  // Tickets
  async getTickets(params?: { page?: number; size?: number; status?: string; keyword?: string }) {
    const response = await this.client.get('/tickets', { params });
    return response.data;
  }

  async getTicket(id: number) {
    const response = await this.client.get(`/tickets/${id}`);
    return response.data;
  }

  async getTicketByNumber(ticketNumber: string) {
    const response = await this.client.get(`/tickets/number/${ticketNumber}`);
    return response.data;
  }

  async createTicket(data: any) {
    const response = await this.client.post('/tickets', data);
    return response.data;
  }

  async updateTicket(id: number, data: any) {
    const response = await this.client.put(`/tickets/${id}`, data);
    return response.data;
  }

  async assignTicket(id: number, agentId: number) {
    const response = await this.client.post(`/tickets/${id}/assign`, null, {
      params: { agentId },
    });
    return response.data;
  }

  async addComment(id: number, comment: string, isInternal = false) {
    const response = await this.client.post(`/tickets/${id}/comments`, {
      commentText: comment,
      isInternal,
    });
    return response.data;
  }

  async closeTicket(id: number, resolutionNotes: string) {
    const response = await this.client.post(`/tickets/${id}/close`, {
      resolutionNotes,
    });
    return response.data;
  }

  async escalateTicket(id: number, reason: string, businessImpact?: string, stepsTaken?: string) {
    const response = await this.client.post(`/tickets/${id}/escalate`, {
      reason,
      businessImpact,
      stepsTaken,
    });
    return response.data;
  }

  async getEscalatedTickets() {
    const response = await this.client.get('/tickets/escalated');
    return response.data;
  }

  async getUnassignedTickets() {
    const response = await this.client.get('/tickets/unassigned');
    return response.data;
  }

  async getOverdueResponseTickets() {
    const response = await this.client.get('/tickets/overdue/response');
    return response.data;
  }

  // Agents
  async getAgents(params?: { page?: number; size?: number; countryCode?: string }) {
    const response = await this.client.get('/agents', { params });
    return response.data;
  }

  async getAgent(id: number) {
    const response = await this.client.get(`/agents/${id}`);
    return response.data;
  }

  async getAvailableAgents(countryCode = 'US') {
    const response = await this.client.get('/agents/available', {
      params: { countryCode },
    });
    return response.data;
  }

  async updateAgentStatus(id: number, status: string) {
    const response = await this.client.put(`/agents/${id}/status`, null, {
      params: { status },
    });
    return response.data;
  }

  async getTopPerformers(startDate?: string, endDate?: string, limit = 10) {
    const response = await this.client.get('/agents/top-performers', {
      params: { startDate, endDate, limit },
    });
    return response.data;
  }

  // Dashboard
  async getDashboardMetrics(days = 7) {
    const response = await this.client.get('/dashboard/metrics', {
      params: { days },
    });
    return response.data;
  }

  async getSLACompliance(startDate: string, endDate: string) {
    const response = await this.client.get('/dashboard/sla', {
      params: { startDate, endDate },
    });
    return response.data;
  }

  async getTeamPerformance(startDate: string, endDate: string) {
    const response = await this.client.get('/dashboard/team-performance', {
      params: { startDate, endDate },
    });
    return response.data;
  }

  async getFeedbackSummary(startDate: string, endDate: string) {
    const response = await this.client.get('/dashboard/feedback-summary', {
      params: { startDate, endDate },
    });
    return response.data;
  }

  async getTicketTrends(days = 30) {
    const response = await this.client.get('/dashboard/ticket-trends', {
      params: { days },
    });
    return response.data;
  }

  // Escalations
  async getEscalations(params?: { page?: number; size?: number; countryCode?: string }) {
    const response = await this.client.get('/escalations', { params });
    return response.data;
  }

  async getEscalation(id: number) {
    const response = await this.client.get(`/escalations/${id}`);
    return response.data;
  }

  async getActiveEscalations(countryCode = 'US') {
    const response = await this.client.get('/escalations/active', {
      params: { countryCode },
    });
    return response.data;
  }

  async resolveEscalation(id: number, resolutionSummary: string) {
    const response = await this.client.post(`/escalations/${id}/resolve`, {
      resolutionSummary,
    });
    return response.data;
  }

  async cancelEscalation(id: number, reason: string) {
    const response = await this.client.post(`/escalations/${id}/cancel`, { reason });
    return response.data;
  }

  // Knowledge Base
  async searchKBArticles(keyword: string, countryCode = 'US', page = 0, size = 20) {
    const response = await this.client.get('/knowledge-base/search', {
      params: { keyword, countryCode, page, size },
    });
    return response.data;
  }

  async getKBArticle(id: number) {
    const response = await this.client.get(`/knowledge-base/articles/${id}`);
    return response.data;
  }

  async getKBArticlesByCategory(category: string, countryCode = 'US', page = 0, size = 20) {
    const response = await this.client.get(`/knowledge-base/category/${category}`, {
      params: { countryCode, page, size },
    });
    return response.data;
  }

  async getMostViewedArticles(countryCode = 'US', page = 0, size = 10) {
    const response = await this.client.get('/knowledge-base/most-viewed', {
      params: { countryCode, page, size },
    });
    return response.data;
  }

  async createKBArticle(data: any) {
    const response = await this.client.post('/knowledge-base/articles', data);
    return response.data;
  }

  async updateKBArticle(id: number, data: any) {
    const response = await this.client.put(`/knowledge-base/articles/${id}`, data);
    return response.data;
  }

  async publishKBArticle(id: number) {
    const response = await this.client.post(`/knowledge-base/articles/${id}/publish`);
    return response.data;
  }

  async markArticleHelpful(id: number, helpful = true) {
    const endpoint = helpful ? 'helpful' : 'not-helpful';
    const response = await this.client.post(`/knowledge-base/articles/${id}/${endpoint}`);
    return response.data;
  }
}

export const api = new ApiClient();
export default api;
