/**
 * Infrastructure Layer: Metrics API
 * HTTP client for metrics-related API calls
 */

import axios, { AxiosInstance, AxiosError } from 'axios';
import { IMetricsRepository } from '../../domain/repositories/MetricsRepository';
import {
  CountryMetrics,
  TeamPerformance,
  TicketTrend,
  TicketDistribution,
  SlaBreach,
  DashboardFilters,
} from '../../domain/entities/Metrics';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';
const API_VERSION = 'v1';

export class MetricsApi implements IMetricsRepository {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: `${API_BASE_URL}/${API_VERSION}`,
      timeout: 30000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor
    this.client.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('auth_token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Response interceptor
    this.client.interceptors.response.use(
      (response) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401) {
          // Handle unauthorized access
          localStorage.removeItem('auth_token');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  async getCountryMetrics(country: string, filters: DashboardFilters): Promise<CountryMetrics> {
    const params = new URLSearchParams({
      from: filters.dateRange.from.toISOString(),
      to: filters.dateRange.to.toISOString(),
    });

    if (filters.category) params.append('category', filters.category);

    const { data } = await this.client.get<CountryMetrics>(`/metrics/countries/${country}`, { params });
    return data;
  }

  async getAllCountriesMetrics(filters: DashboardFilters): Promise<CountryMetrics[]> {
    const params = new URLSearchParams({
      from: filters.dateRange.from.toISOString(),
      to: filters.dateRange.to.toISOString(),
    });

    if (filters.category) params.append('category', filters.category);

    const { data } = await this.client.get<CountryMetrics[]>('/metrics/countries', { params });
    return data;
  }

  async getTeamPerformance(teamId: string, filters: DashboardFilters): Promise<TeamPerformance> {
    const params = new URLSearchParams({
      from: filters.dateRange.from.toISOString(),
      to: filters.dateRange.to.toISOString(),
    });

    const { data } = await this.client.get<TeamPerformance>(`/metrics/teams/${teamId}`, { params });
    return data;
  }

  async getAllTeamsPerformance(filters: DashboardFilters): Promise<TeamPerformance[]> {
    const params = new URLSearchParams({
      from: filters.dateRange.from.toISOString(),
      to: filters.dateRange.to.toISOString(),
    });

    if (filters.team) params.append('team', filters.team);

    const { data } = await this.client.get<TeamPerformance[]>('/metrics/teams', { params });
    return data;
  }

  async getTicketTrends(filters: DashboardFilters): Promise<TicketTrend[]> {
    const params = new URLSearchParams({
      from: filters.dateRange.from.toISOString(),
      to: filters.dateRange.to.toISOString(),
      granularity: 'daily',
    });

    if (filters.country) params.append('country', filters.country);

    const { data } = await this.client.get<TicketTrend[]>('/metrics/tickets/trends', { params });
    return data;
  }

  async getTicketDistribution(filters: DashboardFilters): Promise<TicketDistribution[]> {
    const params = new URLSearchParams({
      from: filters.dateRange.from.toISOString(),
      to: filters.dateRange.to.toISOString(),
    });

    if (filters.country) params.append('country', filters.country);

    const { data } = await this.client.get<TicketDistribution[]>('/metrics/tickets/distribution', { params });
    return data;
  }

  async getSlaBreaches(filters: DashboardFilters): Promise<SlaBreach[]> {
    const params = new URLSearchParams({
      from: filters.dateRange.from.toISOString(),
      to: filters.dateRange.to.toISOString(),
    });

    if (filters.country) params.append('country', filters.country);

    const { data } = await this.client.get<SlaBreach[]>('/metrics/sla/breaches', { params });
    return data;
  }
}

// Mock API for development without backend
export class MockMetricsApi implements IMetricsRepository {
  private mockCountries: CountryMetrics[] = [
    {
      country: 'United States',
      countryCode: 'US',
      totalTickets: { value: 12543, change: 8.2, trend: 'up' },
      openTickets: { value: 1243, change: -3.1, trend: 'down' },
      resolvedTickets: { value: 11243, change: 12.4, trend: 'up' },
      avgResolutionTime: { value: 18.5, change: -5.2, trend: 'down' },
      customerSatisfaction: { value: 4.2, change: 3.1, trend: 'up' },
      slaCompliance: { value: 94.5, change: 2.3, trend: 'up' },
      firstContactResolution: { value: 67.8, change: 4.5, trend: 'up' },
      agentUtilization: { value: 82.3, change: 1.2, trend: 'up' },
    },
    {
      country: 'United Kingdom',
      countryCode: 'UK',
      totalTickets: { value: 8234, change: 5.4, trend: 'up' },
      openTickets: { value: 823, change: -8.7, trend: 'down' },
      resolvedTickets: { value: 7411, change: 7.2, trend: 'up' },
      avgResolutionTime: { value: 22.1, change: -2.1, trend: 'down' },
      customerSatisfaction: { value: 4.0, change: 1.5, trend: 'up' },
      slaCompliance: { value: 91.2, change: 1.8, trend: 'up' },
      firstContactResolution: { value: 62.4, change: 2.1, trend: 'up' },
      agentUtilization: { value: 78.9, change: -1.5, trend: 'down' },
    },
    {
      country: 'Germany',
      countryCode: 'DE',
      totalTickets: { value: 6543, change: 3.2, trend: 'up' },
      openTickets: { value: 543, change: -12.3, trend: 'down' },
      resolvedTickets: { value: 6000, change: 5.6, trend: 'up' },
      avgResolutionTime: { value: 20.3, change: -4.5, trend: 'down' },
      customerSatisfaction: { value: 4.4, change: 2.8, trend: 'up' },
      slaCompliance: { value: 96.8, change: 3.2, trend: 'up' },
      firstContactResolution: { value: 71.2, change: 5.3, trend: 'up' },
      agentUtilization: { value: 85.1, change: 2.4, trend: 'up' },
    },
    {
      country: 'France',
      countryCode: 'FR',
      totalTickets: { value: 5432, change: 6.7, trend: 'up' },
      openTickets: { value: 521, change: -5.4, trend: 'down' },
      resolvedTickets: { value: 4911, change: 8.9, trend: 'up' },
      avgResolutionTime: { value: 24.5, change: -1.2, trend: 'down' },
      customerSatisfaction: { value: 3.9, change: 0.8, trend: 'up' },
      slaCompliance: { value: 89.3, change: -0.5, trend: 'down' },
      firstContactResolution: { value: 58.7, change: 1.2, trend: 'up' },
      agentUtilization: { value: 76.4, change: 0.3, trend: 'stable' },
    },
    {
      country: 'Japan',
      countryCode: 'JP',
      totalTickets: { value: 4321, change: 4.5, trend: 'up' },
      openTickets: { value: 321, change: -15.2, trend: 'down' },
      resolvedTickets: { value: 4000, change: 7.8, trend: 'up' },
      avgResolutionTime: { value: 16.8, change: -6.7, trend: 'down' },
      customerSatisfaction: { value: 4.6, change: 4.2, trend: 'up' },
      slaCompliance: { value: 98.2, change: 2.1, trend: 'up' },
      firstContactResolution: { value: 78.9, change: 6.7, trend: 'up' },
      agentUtilization: { value: 88.7, change: 3.1, trend: 'up' },
    },
  ];

  async getCountryMetrics(country: string, _filters: DashboardFilters): Promise<CountryMetrics> {
    await this.delay(300);
    const metrics = this.mockCountries.find(c => c.countryCode === country || c.country === country);
    if (!metrics) {
      throw new Error(`Country ${country} not found`);
    }
    return metrics;
  }

  async getAllCountriesMetrics(_filters: DashboardFilters): Promise<CountryMetrics[]> {
    await this.delay(500);
    return this.mockCountries;
  }

  async getTeamPerformance(teamId: string, _filters: DashboardFilters): Promise<TeamPerformance> {
    await this.delay(300);
    return {
      teamId,
      teamName: 'North America Support Team',
      teamLead: 'Sarah Johnson',
      memberCount: 12,
      totalTicketsHandled: 5432,
      avgResolutionTime: 16.5,
      customerSatisfaction: 4.3,
      slaComplianceRate: 94.5,
      topPerformers: [
        { agentId: '1', agentName: 'John Smith', ticketsResolved: 234, avgResolutionTime: 12.5, customerRating: 4.8, slaCompliance: 98.5, activeTickets: 8 },
        { agentId: '2', agentName: 'Emily Davis', ticketsResolved: 218, avgResolutionTime: 14.2, customerRating: 4.7, slaCompliance: 96.8, activeTickets: 6 },
        { agentId: '3', agentName: 'Michael Brown', ticketsResolved: 198, avgResolutionTime: 15.8, customerRating: 4.6, slaCompliance: 95.2, activeTickets: 10 },
      ],
    };
  }

  async getAllTeamsPerformance(_filters: DashboardFilters): Promise<TeamPerformance[]> {
    await this.delay(500);
    return [
      {
        teamId: 'team-1',
        teamName: 'North America Support Team',
        teamLead: 'Sarah Johnson',
        memberCount: 12,
        totalTicketsHandled: 5432,
        avgResolutionTime: 16.5,
        customerSatisfaction: 4.3,
        slaComplianceRate: 94.5,
        topPerformers: [],
      },
      {
        teamId: 'team-2',
        teamName: 'Europe Support Team',
        teamLead: 'Hans Mueller',
        memberCount: 10,
        totalTicketsHandled: 4321,
        avgResolutionTime: 18.2,
        customerSatisfaction: 4.1,
        slaComplianceRate: 92.8,
        topPerformers: [],
      },
      {
        teamId: 'team-3',
        teamName: 'Asia Pacific Support Team',
        teamLead: 'Yuki Tanaka',
        memberCount: 8,
        totalTicketsHandled: 3654,
        avgResolutionTime: 14.8,
        customerSatisfaction: 4.5,
        slaComplianceRate: 96.2,
        topPerformers: [],
      },
    ];
  }

  async getTicketTrends(_filters: DashboardFilters): Promise<TicketTrend[]> {
    await this.delay(300);
    const trends: TicketTrend[] = [];
    const today = new Date();
    for (let i = 29; i >= 0; i--) {
      const date = new Date(today);
      date.setDate(date.getDate() - i);
      trends.push({
        date: date.toISOString().split('T')[0],
        new: Math.floor(Math.random() * 200) + 300,
        resolved: Math.floor(Math.random() * 180) + 280,
        open: Math.floor(Math.random() * 50) + 100,
        escalated: Math.floor(Math.random() * 20) + 5,
      });
    }
    return trends;
  }

  async getTicketDistribution(_filters: DashboardFilters): Promise<TicketDistribution[]> {
    await this.delay(200);
    return [
      { category: 'Technical Issues', count: 4532, percentage: 35.2, color: '#3b82f6' },
      { category: 'Billing', count: 2876, percentage: 22.3, color: '#10b981' },
      { category: 'Account Access', count: 2109, percentage: 16.4, color: '#f59e0b' },
      { category: 'Product Information', count: 1654, percentage: 12.8, color: '#8b5cf6' },
      { category: 'Feature Requests', count: 987, percentage: 7.6, color: '#ec4899' },
      { category: 'Other', count: 732, percentage: 5.7, color: '#6b7280' },
    ];
  }

  async getSlaBreaches(_filters: DashboardFilters): Promise<SlaBreach[]> {
    await this.delay(300);
    return [
      {
        ticketId: 'TKT-100234',
        ticketSubject: 'Critical system outage affecting multiple users',
        severity: 'critical',
        overdueBy: 4.5,
        assignedTo: 'John Smith',
        customer: 'Acme Corp',
      },
      {
        ticketId: 'TKT-100235',
        ticketSubject: 'Payment processing delay',
        severity: 'high',
        overdueBy: 2.3,
        assignedTo: 'Emily Davis',
        customer: 'TechStart Inc',
      },
      {
        ticketId: 'TKT-100236',
        ticketSubject: 'API integration errors',
        severity: 'medium',
        overdueBy: 1.8,
        assignedTo: 'Michael Brown',
        customer: 'DataFlow Systems',
      },
    ];
  }

  private delay(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}

// Factory function to get the appropriate API implementation
export const createMetricsApi = (): IMetricsRepository => {
  const useMock = import.meta.env.VITE_USE_MOCK_API === 'true' || !import.meta.env.VITE_API_BASE_URL;
  return useMock ? new MockMetricsApi() : new MetricsApi();
};
