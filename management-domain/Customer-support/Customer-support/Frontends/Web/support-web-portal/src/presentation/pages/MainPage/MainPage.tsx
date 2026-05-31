import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { BarChart3, Clock, Star, CheckCircle, AlertTriangle } from 'lucide-react';
import { createMetricsApi } from '../../../infrastructure/api/metricsApi';
import { useMetricsStore } from '../../../application/store/metricsStore';
import { MetricCard } from '../../components/MetricCard';
import { TicketTrendChart } from '../../components/TicketTrendChart';
import { CountryMetricsTable } from '../../components/CountryMetricsTable';
import { TeamPerformanceCard } from '../../components/TeamPerformanceCard';
import { LoadingSpinner } from '../../components/LoadingSpinner';
import { ErrorAlert } from '../../components/ErrorAlert';
import { formatPercentage, formatDuration } from '../../../shared/utils';

const metricsApi = createMetricsApi();

export function MainPage() {
  const { filters, setCountryMetrics, setTicketTrends, setTeamPerformance, setSlaBreaches } = useMetricsStore();

  // Fetch all data
  const { data: countryMetrics, isLoading: isLoadingMetrics, error: metricsError } = useQuery({
    queryKey: ['countryMetrics', filters],
    queryFn: () => metricsApi.getAllCountriesMetrics(filters),
  });

  const { data: ticketTrends, isLoading: isLoadingTrends } = useQuery({
    queryKey: ['ticketTrends', filters],
    queryFn: () => metricsApi.getTicketTrends(filters),
  });

  const { data: teams, isLoading: isLoadingTeams } = useQuery({
    queryKey: ['teamPerformance', filters],
    queryFn: () => metricsApi.getAllTeamsPerformance(filters),
  });

  const { data: slaBreaches } = useQuery({
    queryKey: ['slaBreaches', filters],
    queryFn: () => metricsApi.getSlaBreaches(filters),
  });

  useEffect(() => {
    if (countryMetrics) setCountryMetrics(countryMetrics);
  }, [countryMetrics, setCountryMetrics]);

  useEffect(() => {
    if (ticketTrends) setTicketTrends(ticketTrends);
  }, [ticketTrends, setTicketTrends]);

  useEffect(() => {
    if (teams) setTeamPerformance(teams);
  }, [teams, setTeamPerformance]);

  useEffect(() => {
    if (slaBreaches) setSlaBreaches(slaBreaches);
  }, [slaBreaches, setSlaBreaches]);

  const isLoading = isLoadingMetrics || isLoadingTrends || isLoadingTeams;

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (metricsError) {
    return (
      <ErrorAlert
        message="Failed to load dashboard data. Please try again later."
      />
    );
  }

  const aggregatedMetrics = countryMetrics?.reduce((acc, m) => ({
    totalTickets: acc.totalTickets + m.totalTickets.value,
    openTickets: acc.openTickets + m.openTickets.value,
    resolvedTickets: acc.resolvedTickets + m.resolvedTickets.value,
    avgResolutionTime: acc.avgResolutionTime + m.avgResolutionTime.value,
    customerSatisfaction: acc.customerSatisfaction + m.customerSatisfaction.value,
    slaCompliance: acc.slaCompliance + m.slaCompliance.value,
  }), {
    totalTickets: 0,
    openTickets: 0,
    resolvedTickets: 0,
    avgResolutionTime: 0,
    customerSatisfaction: 0,
    slaCompliance: 0,
  });

  const count = countryMetrics?.length || 1;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Country Support Dashboard</h1>
          <p className="text-gray-500 mt-1">Monitor support performance across all countries</p>
        </div>
        {slaBreaches && slaBreaches.length > 0 && (
          <div className="flex items-center gap-2 px-4 py-2 bg-danger-50 rounded-lg">
            <AlertTriangle className="w-5 h-5 text-danger-600" />
            <span className="text-sm font-medium text-danger-900">
              {slaBreaches.length} SLA breaches require attention
            </span>
          </div>
        )}
      </div>

      {/* Key Metrics */}
      {aggregatedMetrics && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <MetricCard
            title="Total Tickets"
            value={aggregatedMetrics.totalTickets}
            icon={BarChart3}
          />
          <MetricCard
            title="Open Tickets"
            value={aggregatedMetrics.openTickets}
            icon={Clock}
          />
          <MetricCard
            title="Avg Resolution Time"
            value={formatDuration(aggregatedMetrics.avgResolutionTime / count)}
            icon={Clock}
          />
          <MetricCard
            title="Customer Satisfaction"
            value={(aggregatedMetrics.customerSatisfaction / count).toFixed(1)}
            unit="/5.0"
            icon={Star}
          />
        </div>
      )}

      {/* SLA Compliance Summary */}
      {aggregatedMetrics && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <MetricCard
            title="SLA Compliance"
            value={formatPercentage(aggregatedMetrics.slaCompliance / count)}
            icon={CheckCircle}
          />
          <MetricCard
            title="Resolved Tickets"
            value={aggregatedMetrics.resolvedTickets}
            icon={CheckCircle}
          />
          <MetricCard
            title="Active Breaches"
            value={slaBreaches?.length || 0}
            icon={AlertTriangle}
          />
        </div>
      )}

      {/* Ticket Trends Chart */}
      {ticketTrends && <TicketTrendChart data={ticketTrends} />}

      {/* Country Metrics Table */}
      {countryMetrics && <CountryMetricsTable data={countryMetrics} />}

      {/* Team Performance */}
      <div>
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Team Performance</h2>
        {teams && teams.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {teams.map((team) => (
              <TeamPerformanceCard key={team.teamId} team={team} />
            ))}
          </div>
        ) : (
          <div className="bg-white rounded-xl p-8 text-center text-gray-500">
            No team performance data available
          </div>
        )}
      </div>
    </div>
  );
}
