import { useNavigate } from 'react-router-dom';
import { TeamPerformance } from '../../domain/entities';
import { Users, Clock, Star, CheckCircle } from 'lucide-react';
import { formatNumber, formatDuration, formatPercentage } from '../../shared/utils';

interface TeamPerformanceCardProps {
  team: TeamPerformance;
}

export function TeamPerformanceCard({ team }: TeamPerformanceCardProps) {
  const navigate = useNavigate();

  return (
    <div
      className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow cursor-pointer"
      onClick={() => navigate(`/team/${team.teamId}`)}
    >
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">{team.teamName}</h3>
          <p className="text-sm text-gray-500">Lead: {team.teamLead}</p>
        </div>
        <div className="flex items-center gap-1 px-2 py-1 bg-primary-50 rounded-full">
          <Users className="w-4 h-4 text-primary-600" />
          <span className="text-sm font-medium text-primary-700">{team.memberCount}</span>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="flex items-center gap-2">
          <div className="p-2 bg-blue-50 rounded-lg">
            <CheckCircle className="w-4 h-4 text-blue-600" />
          </div>
          <div>
            <p className="text-xs text-gray-500">Tickets</p>
            <p className="text-sm font-semibold text-gray-900">{formatNumber(team.totalTicketsHandled)}</p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <div className="p-2 bg-green-50 rounded-lg">
            <Clock className="w-4 h-4 text-green-600" />
          </div>
          <div>
            <p className="text-xs text-gray-500">Avg Time</p>
            <p className="text-sm font-semibold text-gray-900">{formatDuration(team.avgResolutionTime)}</p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <div className="p-2 bg-yellow-50 rounded-lg">
            <Star className="w-4 h-4 text-yellow-600" />
          </div>
          <div>
            <p className="text-xs text-gray-500">CSAT</p>
            <p className="text-sm font-semibold text-gray-900">{team.customerSatisfaction.toFixed(1)}</p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <div className="p-2 bg-purple-50 rounded-lg">
            <CheckCircle className="w-4 h-4 text-purple-600" />
          </div>
          <div>
            <p className="text-xs text-gray-500">SLA</p>
            <p className="text-sm font-semibold text-gray-900">{formatPercentage(team.slaComplianceRate)}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
