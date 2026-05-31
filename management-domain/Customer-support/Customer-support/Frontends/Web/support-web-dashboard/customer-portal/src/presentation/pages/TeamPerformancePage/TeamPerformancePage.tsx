import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { ArrowLeft, Mail, Phone } from 'lucide-react';
import { createMetricsApi } from '../../../infrastructure/api/metricsApi';
import { MetricCard } from '../../components/MetricCard';
import { LoadingSpinner } from '../../components/LoadingSpinner';
import { ErrorAlert } from '../../components/ErrorAlert';
import { formatDuration, formatPercentage } from '../../../shared/utils';

const metricsApi = createMetricsApi();

export function TeamPerformancePage() {
  const { teamId } = useParams<{ teamId: string }>();
  const navigate = useNavigate();

  const { data: team, isLoading, error } = useQuery({
    queryKey: ['teamPerformance', teamId],
    queryFn: () => metricsApi.getTeamPerformance(teamId!, { dateRange: { from: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000), to: new Date() } }),
    enabled: !!teamId,
  });

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (error || !team) {
    return (
      <ErrorAlert message="Failed to load team details. Please try again later." />
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
          <h1 className="text-2xl font-bold text-gray-900">{team.teamName}</h1>
          <p className="text-gray-500 mt-1">Team Lead: {team.teamLead}</p>
        </div>
        <div className="flex items-center gap-4">
          <div className="text-center">
            <p className="text-2xl font-bold text-gray-900">{team.memberCount}</p>
            <p className="text-xs text-gray-500">Members</p>
          </div>
        </div>
      </div>

      {/* Team Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <MetricCard
          title="Total Tickets Handled"
          value={team.totalTicketsHandled}
        />
        <MetricCard
          title="Avg Resolution Time"
          value={formatDuration(team.avgResolutionTime)}
        />
        <MetricCard
          title="Customer Satisfaction"
          value={team.customerSatisfaction.toFixed(1)}
          unit="/5.0"
        />
        <MetricCard
          title="SLA Compliance"
          value={formatPercentage(team.slaComplianceRate)}
        />
      </div>

      {/* Top Performers */}
      {team.topPerformers && team.topPerformers.length > 0 && (
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-100">
            <h3 className="text-lg font-semibold text-gray-900">Top Performers</h3>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Agent
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Tickets Resolved
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Avg Resolution Time
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Customer Rating
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    SLA Compliance
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Active Tickets
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {team.topPerformers.map((agent) => (
                  <tr key={agent.agentId} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 bg-primary-100 rounded-full flex items-center justify-center">
                          <span className="text-sm font-medium text-primary-700">
                            {agent.agentName.split(' ').map(n => n[0]).join('')}
                          </span>
                        </div>
                        <div>
                          <p className="text-sm font-medium text-gray-900">{agent.agentName}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm text-gray-900">
                      {agent.ticketsResolved}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm text-gray-900">
                      {formatDuration(agent.avgResolutionTime)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right">
                      <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                        {agent.customerRating.toFixed(1)}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right">
                      <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium ${
                        agent.slaCompliance >= 95 ? 'bg-green-100 text-green-800' :
                        agent.slaCompliance >= 90 ? 'bg-yellow-100 text-yellow-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {formatPercentage(agent.slaCompliance)}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm text-gray-900">
                      {agent.activeTickets}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Contact Actions */}
      <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Team Contact</h3>
        <div className="flex gap-4">
          <button className="flex items-center gap-2 px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors">
            <Mail className="w-4 h-4" />
            <span>Email Team</span>
          </button>
          <button className="flex items-center gap-2 px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors">
            <Phone className="w-4 h-4" />
            <span>Call Team Lead</span>
          </button>
        </div>
      </div>
    </div>
  );
}
