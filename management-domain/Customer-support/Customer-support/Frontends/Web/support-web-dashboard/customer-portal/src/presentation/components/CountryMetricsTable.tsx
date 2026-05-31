import { useNavigate } from 'react-router-dom';
import { CountryMetrics } from '../../domain/entities';
import { cn, formatNumber, formatPercentage, getTrendIcon, getTrendColor } from '../../shared/utils';

interface CountryMetricsTableProps {
  data: CountryMetrics[];
}

export function CountryMetricsTable({ data }: CountryMetricsTableProps) {
  const navigate = useNavigate();

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div className="px-6 py-4 border-b border-gray-100">
        <h3 className="text-lg font-semibold text-gray-900">Country Performance</h3>
      </div>
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Country
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Total Tickets
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Open
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Resolved
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Avg Resolution
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                CSAT
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                SLA Compliance
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {data.map((country) => (
              <tr
                key={country.countryCode}
                className="hover:bg-gray-50 cursor-pointer transition-colors"
                onClick={() => navigate(`/country/${country.countryCode}`)}
              >
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex items-center">
                    <div className="text-sm font-medium text-gray-900">{country.country}</div>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right">
                  <div className="text-sm text-gray-900">{formatNumber(country.totalTickets.value)}</div>
                  <div className={cn('text-xs', getTrendColor(country.totalTickets.trend))}>
                    {getTrendIcon(country.totalTickets.trend)} {formatPercentage(country.totalTickets.change)}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right">
                  <div className="text-sm text-gray-900">{formatNumber(country.openTickets.value)}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right">
                  <div className="text-sm text-gray-900">{formatNumber(country.resolvedTickets.value)}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right">
                  <div className="text-sm text-gray-900">{country.avgResolutionTime.value.toFixed(1)}h</div>
                  <div className={cn('text-xs', getTrendColor(country.avgResolutionTime.trend, true))}>
                    {getTrendIcon(country.avgResolutionTime.trend)} {formatPercentage(country.avgResolutionTime.change)}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right">
                  <div className="text-sm font-medium text-gray-900">{country.customerSatisfaction.value.toFixed(1)}</div>
                  <div className={cn('text-xs', getTrendColor(country.customerSatisfaction.trend))}>
                    {getTrendIcon(country.customerSatisfaction.trend)} {formatPercentage(country.customerSatisfaction.change)}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right">
                  <div className="text-sm text-gray-900">{formatPercentage(country.slaCompliance.value)}</div>
                  <div className={cn('text-xs', getTrendColor(country.slaCompliance.trend))}>
                    {getTrendIcon(country.slaCompliance.trend)} {formatPercentage(country.slaCompliance.change)}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
