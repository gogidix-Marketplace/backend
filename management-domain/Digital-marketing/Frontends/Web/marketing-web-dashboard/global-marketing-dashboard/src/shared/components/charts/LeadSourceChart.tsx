// LeadSourceChart Component
// Displays lead generation by source

import React from 'react';
import { ChartCard } from './ChartCard';
import { PieChart, PieDataPoint } from './PieChart';
import { BarChart, BarDataPoint } from './BarChart';
import { formatCompactNumber, formatPercentage } from '../../utils/formatters';

export interface LeadSourceData {
  source: string;
  leads: number;
  percentage: number;
  conversionRate?: number;
}

export interface LeadSourceChartProps {
  data: LeadSourceData[];
  className?: string;
  view?: 'pie' | 'bar' | 'horizontal';
}

export const LeadSourceChart: React.FC<LeadSourceChartProps> = ({
  data,
  className = '',
  view = 'pie',
}) => {
  const totalLeads = data.reduce((sum, d) => sum + d.leads, 0);

  const pieData: PieDataPoint[] = data.map((d) => ({
    label: d.source,
    value: d.leads,
  }));

  const barData: BarDataPoint[] = data.map((d) => ({
    label: d.source,
    value: d.leads,
  }));

  const sourceColors: Record<string, string> = {
    'Website': '#3B82F6',
    'Email': '#8B5CF6',
    'Social Media': '#EC4899',
    'Search Engine': '#F59E0B',
    'Referral': '#10B981',
    'Event': '#06B6D4',
    'Advertisement': '#EF4444',
    'Content': '#84CC16',
    'Other': '#6B7280',
  };

  return (
    <ChartCard
      title="Leads by Source"
      subtitle={`${formatCompactNumber(totalLeads)} total leads`}
      className={className}
    >
      {view === 'pie' ? (
        <div style={{ display: 'flex', justifyContent: 'center' }}>
          <PieChart
            data={pieData}
            donut
            innerRadius={0.6}
            showLegend
            colors={data.map((d) => sourceColors[d.source] || '#6B7280')}
          />
        </div>
      ) : view === 'horizontal' ? (
        <BarChart
          data={barData.map((d, i) => ({ ...d, color: sourceColors[d.label] || '#6B7280' }))}
          horizontal
          showGrid
          showValues
        />
      ) : (
        <BarChart
          data={barData.map((d, i) => ({ ...d, color: sourceColors[d.label] || '#6B7280' }))}
          showGrid
          showValues
        />
      )}

      {/* Lead quality breakdown */}
      <div className="lead-source-breakdown">
        <table className="lead-source-table">
          <thead>
            <tr>
              <th>Source</th>
              <th>Leads</th>
              <th>% of Total</th>
              <th>Conv. Rate</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, i) => (
              <tr key={i}>
                <td>
                  <div className="source-indicator">
                    <span
                      className="source-dot"
                      style={{ backgroundColor: sourceColors[item.source] || '#6B7280' }}
                    />
                    {item.source}
                  </div>
                </td>
                <td>{formatCompactNumber(item.leads)}</td>
                <td>{formatPercentage(item.percentage)}</td>
                <td>
                  {item.conversionRate !== undefined ? formatPercentage(item.conversionRate) : 'N/A'}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </ChartCard>
  );
};

export default LeadSourceChart;
