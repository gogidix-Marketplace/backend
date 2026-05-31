import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import { createMetricsApi } from '../../../infrastructure/api/metricsApi';
import { useMetricsStore } from '../../../application/store/metricsStore';
import { MetricCard } from '../../components/MetricCard';
import { TicketTrendChart } from '../../components/TicketTrendChart';
import { LoadingSpinner } from '../../components/LoadingSpinner';
import { ErrorAlert } from '../../components/ErrorAlert';
import { formatPercentage, formatDuration, getTrendIcon, getTrendColor, cn } from '../../../shared/utils';

const metricsApi = createMetricsApi();

export function CountryDetailsPage() {
  const { countryCode } = useParams<{ countryCode: string }>();
  const navigate = useNavigate();
  const { filters } = useMetricsStore();

  const { data: metrics, isLoading, error } = useQuery({
    queryKey: ['countryMetrics', countryCode, filters],
    queryFn: () => metricsApi.getCountryMetrics(countryCode!, filters),
    enabled: !!countryCode,
  });

  const { data: trends } = useQuery({
    queryKey: ['ticketTrends', countryCode, filters],
    queryFn: () => metricsApi.getTicketTrends({ ...filters, country: countryCode }),
    enabled: !!countryCode,
  });

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (error || !metrics) {
    return (
      <ErrorAlert message="Failed to load country details. Please try again later." />
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <button
        onClick={() => navigate(-1)}
        className="flex items-center gap-2 text-gray-600 hover:text-gray-900 transition-colors"
      >
        <ArrowLeft className="w-4 h-4" />
        <span className="text-sm font-medium">Back to Dashboard</span>
      </button>

      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">{metrics.country}</h1>
          <p className="text-gray-500 mt-1">Country: {metrics.countryCode}</p>
        </div>
        <div className="flex items-center gap-2 px-4 py-2 bg-primary-50 rounded-lg">
          <span className="text-sm font-medium text-primary-900">
            Overall Score: {(
              ((metrics.customerSatisfaction.value / 5) * 0.3 +
              (metrics.slaCompliance.value / 100) * 0.3 +
              (metrics.firstContactResolution.value / 100) * 0.2 +
              ((100 - metrics.avgResolutionTime.value) / 100) * 0.2) * 100
            ).toFixed(1)}
          </span>
        </div>
      </div>

      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <MetricCard
          title="Total Tickets"
          value={metrics.totalTickets.value}
          change={metrics.totalTickets.change}
          trend={metrics.totalTickets.trend}
        />
        <MetricCard
          title="Open Tickets"
          value={metrics.openTickets.value}
          change={metrics.openTickets.change}
          trend={metrics.openTickets.trend}
        />
        <MetricCard
          title="Resolved Tickets"
          value={metrics.resolvedTickets.value}
          change={metrics.resolvedTickets.change}
          trend={metrics.resolvedTickets.trend}
        />
        <MetricCard
          title="Avg Resolution Time"
          value={formatDuration(metrics.avgResolutionTime.value)}
          change={metrics.avgResolutionTime.change}
          trend={metrics.avgResolutionTime.trend}
          isInverted
        />
      </div>

      {/* Quality Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <MetricCard
          title="Customer Satisfaction"
          value={metrics.customerSatisfaction.value.toFixed(1)}
          unit="/5.0"
          change={metrics.customerSatisfaction.change}
          trend={metrics.customerSatisfaction.trend}
        />
        <MetricCard
          title="SLA Compliance"
          value={formatPercentage(metrics.slaCompliance.value)}
          change={metrics.slaCompliance.change}
          trend={metrics.slaCompliance.trend}
        />
        <MetricCard
          title="First Contact Resolution"
          value={formatPercentage(metrics.firstContactResolution.value)}
          change={metrics.firstContactResolution.change}
          trend={metrics.firstContactResolution.trend}
        />
        <MetricCard
          title="Agent Utilization"
          value={formatPercentage(metrics.agentUtilization.value)}
          change={metrics.agentUtilization.change}
          trend={metrics.agentUtilization.trend}
        />
      </div>

      {/* Trends Chart */}
      {trends && <TicketTrendChart data={trends} />}

      {/* Performance Summary */}
      <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Performance Summary</h3>
        <div className="space-y-4">
          <div className="flex items-center justify-between py-3 border-b border-gray-100">
            <span className="text-gray-600">Resolution Time Trend</span>
            <span className={cn('text-sm font-medium flex items-center gap-1', getTrendColor(metrics.avgResolutionTime.trend, true))}>
              {getTrendIcon(metrics.avgResolutionTime.trend)} {formatPercentage(metrics.avgResolutionTime.change)} vs last period
            </span>
          </div>
          <div className="flex items-center justify-between py-3 border-b border-gray-100">
            <span className="text-gray-600">Customer Satisfaction Trend</span>
            <span className={cn('text-sm font-medium flex items-center gap-1', getTrendColor(metrics.customerSatisfaction.trend))}>
              {getTrendIcon(metrics.customerSatisfaction.trend)} {formatPercentage(metrics.customerSatisfaction.change)} vs last period
            </span>
          </div>
          <div className="flex items-center justify-between py-3">
            <span className="text-gray-600">SLA Compliance Status</span>
            <span className={cn('text-sm font-medium', metrics.slaCompliance.value >= 90 ? 'text-success-600' : 'text-warning-600')}>
              {metrics.slaCompliance.value >= 90 ? 'On Track' : 'Needs Improvement'}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
