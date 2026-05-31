/**
 * Domain Repository Interface: MetricsRepository
 * Defines the contract for metrics data access
 */

import { CountryMetrics, TeamPerformance, TicketTrend, TicketDistribution, SlaBreach, DashboardFilters } from '../entities/Metrics';

export interface IMetricsRepository {
  getCountryMetrics(country: string, filters: DashboardFilters): Promise<CountryMetrics>;
  getAllCountriesMetrics(filters: DashboardFilters): Promise<CountryMetrics[]>;
  getTeamPerformance(teamId: string, filters: DashboardFilters): Promise<TeamPerformance>;
  getAllTeamsPerformance(filters: DashboardFilters): Promise<TeamPerformance[]>;
  getTicketTrends(filters: DashboardFilters): Promise<TicketTrend[]>;
  getTicketDistribution(filters: DashboardFilters): Promise<TicketDistribution[]>;
  getSlaBreaches(filters: DashboardFilters): Promise<SlaBreach[]>;
}

export const MetricsRepositoryType = Symbol('IMetricsRepository');
