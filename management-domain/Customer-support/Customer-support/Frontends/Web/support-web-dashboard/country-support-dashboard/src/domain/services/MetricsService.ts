/**
 * Domain Service: MetricsService
 * Business logic for metrics calculations and aggregations
 */

import { CountryMetrics } from '../entities/Metrics';

export class MetricsService {
  calculateTrend(currentValue: number, previousValue: number): 'up' | 'down' | 'stable' {
    const threshold = 0.05; // 5% threshold for stability
    const change = (currentValue - previousValue) / previousValue;

    if (Math.abs(change) < threshold) return 'stable';
    return change > 0 ? 'up' : 'down';
  }

  calculatePercentageChange(currentValue: number, previousValue: number): number {
    if (previousValue === 0) return currentValue > 0 ? 100 : 0;
    return ((currentValue - previousValue) / previousValue) * 100;
  }

  calculateSlaCompliance(resolvedWithinSla: number, totalResolved: number): number {
    if (totalResolved === 0) return 0;
    return (resolvedWithinSla / totalResolved) * 100;
  }

  calculateCustomerSatisfaction(ratings: number[]): number {
    if (ratings.length === 0) return 0;
    const sum = ratings.reduce((acc, rating) => acc + rating, 0);
    return sum / ratings.length;
  }

  calculateAgentUtilization(activeTime: number, availableTime: number): number {
    if (availableTime === 0) return 0;
    return (activeTime / availableTime) * 100;
  }

  aggregateCountryMetrics(metrics: CountryMetrics[]): CountryMetrics {
    const totals = metrics.reduce((acc, metric) => ({
      totalTickets: acc.totalTickets + metric.totalTickets.value,
      openTickets: acc.openTickets + metric.openTickets.value,
      resolvedTickets: acc.resolvedTickets + metric.resolvedTickets.value,
      sumResolutionTime: acc.sumResolutionTime + metric.avgResolutionTime.value,
      sumSatisfaction: acc.sumSatisfaction + metric.customerSatisfaction.value,
      sumSlaCompliance: acc.sumSlaCompliance + metric.slaCompliance.value,
      sumFcr: acc.sumFcr + metric.firstContactResolution.value,
      sumUtilization: acc.sumUtilization + metric.agentUtilization.value,
    }), {
      totalTickets: 0,
      openTickets: 0,
      resolvedTickets: 0,
      sumResolutionTime: 0,
      sumSatisfaction: 0,
      sumSlaCompliance: 0,
      sumFcr: 0,
      sumUtilization: 0,
    });

    const count = metrics.length || 1;

    return {
      country: 'All Countries',
      countryCode: 'ALL',
      totalTickets: { value: totals.totalTickets, change: 0, trend: 'stable' },
      openTickets: { value: totals.openTickets, change: 0, trend: 'stable' },
      resolvedTickets: { value: totals.resolvedTickets, change: 0, trend: 'stable' },
      avgResolutionTime: { value: totals.sumResolutionTime / count, change: 0, trend: 'stable' },
      customerSatisfaction: { value: totals.sumSatisfaction / count, change: 0, trend: 'stable' },
      slaCompliance: { value: totals.sumSlaCompliance / count, change: 0, trend: 'stable' },
      firstContactResolution: { value: totals.sumFcr / count, change: 0, trend: 'stable' },
      agentUtilization: { value: totals.sumUtilization / count, change: 0, trend: 'stable' },
    };
  }

  generateInsights(metrics: CountryMetrics): string[] {
    const insights: string[] = [];

    if (metrics.slaCompliance.value < 90) {
      insights.push(`SLA compliance is below target at ${metrics.slaCompliance.value.toFixed(1)}%`);
    }

    if (metrics.customerSatisfaction.value < 4.0) {
      insights.push(`Customer satisfaction needs improvement (${metrics.customerSatisfaction.value.toFixed(1)}/5.0)`);
    }

    if (metrics.avgResolutionTime.value > 24) {
      insights.push(`Average resolution time is high at ${metrics.avgResolutionTime.value.toFixed(1)} hours`);
    }

    if (metrics.firstContactResolution.value > 60) {
      insights.push(`Great first contact resolution rate at ${metrics.firstContactResolution.value.toFixed(1)}%`);
    }

    return insights;
  }
}
