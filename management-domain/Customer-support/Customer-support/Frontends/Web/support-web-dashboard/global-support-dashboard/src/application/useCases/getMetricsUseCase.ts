/**
 * Application Layer: Get Metrics Use Case
 * Orchestrates the retrieval of metrics data
 */

import { IMetricsRepository } from '../../domain/repositories/MetricsRepository';
import { MetricsService } from '../../domain/services/MetricsService';
import { CountryMetrics, TeamPerformance, TicketTrend, DashboardFilters } from '../../domain/entities/Metrics';

export class GetMetricsUseCase {
  constructor(
    private metricsRepository: IMetricsRepository,
    private metricsService: MetricsService
  ) {}

  async execute(filters: DashboardFilters): Promise<{
    countryMetrics: CountryMetrics[];
    aggregatedMetrics: CountryMetrics;
    insights: string[];
  }> {
    const countryMetrics = await this.metricsRepository.getAllCountriesMetrics(filters);
    const aggregatedMetrics = this.metricsService.aggregateCountryMetrics(countryMetrics);
    const insights = countryMetrics.flatMap(m => this.metricsService.generateInsights(m));

    return {
      countryMetrics,
      aggregatedMetrics,
      insights,
    };
  }

  async getCountryMetrics(country: string, filters: DashboardFilters): Promise<{
    metrics: CountryMetrics;
    insights: string[];
  }> {
    const metrics = await this.metricsRepository.getCountryMetrics(country, filters);
    const insights = this.metricsService.generateInsights(metrics);

    return { metrics, insights };
  }

  async getTeamPerformance(teamId: string, filters: DashboardFilters): Promise<TeamPerformance> {
    return this.metricsRepository.getTeamPerformance(teamId, filters);
  }

  async getAllTeamsPerformance(filters: DashboardFilters): Promise<TeamPerformance[]> {
    return this.metricsRepository.getAllTeamsPerformance(filters);
  }

  async getTicketTrends(filters: DashboardFilters): Promise<TicketTrend[]> {
    return this.metricsRepository.getTicketTrends(filters);
  }
}
