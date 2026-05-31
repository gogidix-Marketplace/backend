// ChannelROIChart Component
// Displays return on investment by marketing channel

import React, { useState, useMemo } from 'react';
import { ChartCard } from './ChartCard';
import { BarChart, DataPoint } from './BarChart';
import { PieChart } from './PieChart';
import { Select, SelectOption } from '../common/Select';
import { formatCurrency, formatPercentage, formatCompactNumber } from '../../utils/formatters';

export interface ChannelROIData {
  channel: string;
  spend: number;
  revenue: number;
  roi: number;
  roas: number;
  conversions: number;
  leads: number;
  clicks: number;
  impressions: number;
}

export interface ChannelROIChartProps {
  data: ChannelROIData[];
  className?: string;
  view?: 'bar' | 'pie';
}

type MetricType = 'spend' | 'revenue' | 'roi' | 'roas' | 'conversions' | 'leads';

const metricOptions: SelectOption[] = [
  { value: 'spend', label: 'Spend' },
  { value: 'revenue', label: 'Revenue' },
  { value: 'roi', label: 'ROI' },
  { value: 'roas', label: 'ROAS' },
  { value: 'conversions', label: 'Conversions' },
  { value: 'leads', label: 'Leads' },
];

const channelColors: Record<string, string> = {
  email: '#10B981',
  social: '#8B5CF6',
  search: '#3B82F6',
  display: '#F59E0B',
  video: '#EF4444',
  direct_mail: '#6B7280',
  events: '#EC4899',
  webinars: '#06B6D4',
  content: '#84CC16',
};

export const ChannelROIChart: React.FC<ChannelROIChartProps> = ({
  data,
  className = '',
  view = 'bar',
}) => {
  const [selectedMetric, setSelectedMetric] = useState<MetricType>('revenue');
  const [sortBy, setSortBy] = useState<'name' | 'value'>('value');

  const sortedData = useMemo(() => {
    const sorted = [...data].sort((a, b) => {
      if (sortBy === 'value') {
        return (b[selectedMetric] as number) - (a[selectedMetric] as number);
      }
      return a.channel.localeCompare(b.channel);
    });
    return sorted;
  }, [data, selectedMetric, sortBy]);

  const chartData: DataPoint[] = sortedData.map((item) => ({
    label: item.channel.split('_').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' '),
    value: item[selectedMetric] as number,
    color: channelColors[item.channel.toLowerCase()] || '#8B5CF6',
  }));

  const pieData = sortedData.map((item) => ({
    label: item.channel.split('_').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' '),
    value: item[selectedMetric] as number,
    percentage: (item[selectedMetric] as number / sortedData.reduce((sum, d) => sum + d[selectedMetric] as number, 0)) * 100,
  }));

  const totalValue = sortedData.reduce((sum, d) => sum + (d[selectedMetric] as number), 0);
  const totalSpend = sortedData.reduce((sum, d) => sum + d.spend, 0);
  const totalRevenue = sortedData.reduce((sum, d) => sum + d.revenue, 0);
  const overallROI = totalSpend > 0 ? ((totalRevenue - totalSpend) / totalSpend) * 100 : 0;
  const overallROAS = totalSpend > 0 ? totalRevenue / totalSpend : 0;

  const formatValue = (value: number, metric: MetricType): string => {
    switch (metric) {
      case 'spend':
      case 'revenue':
        return formatCurrency(value);
      case 'roi':
        return formatPercentage(value);
      case 'roas':
        return `${value.toFixed(2)}x`;
      case 'conversions':
      case 'leads':
        return formatCompactNumber(value);
      default:
        return value.toLocaleString();
    }
  };

  return (
    <ChartCard
      title="Channel ROI Analysis"
      subtitle="Return on investment by marketing channel"
      className={className}
      actions={
        <div style={{ display: 'flex', gap: '0.5rem' }}>
          <Select
            options={[
              { value: 'value', label: 'Sort by Value' },
              { value: 'name', label: 'Sort by Name' },
            ]}
            value={sortBy}
            onChange={(v) => setSortBy(v as 'name' | 'value')}
            size="sm"
          />
          <Select
            options={metricOptions}
            value={selectedMetric}
            onChange={(v) => setSelectedMetric(v as MetricType)}
            size="sm"
          />
        </div>
      }
    >
      <div className="channel-roi-toggle">
        <button
          className={`toggle-btn ${view === 'bar' ? 'active' : ''}`}
          onClick={() => {}}
          type="button"
        >
          Bar Chart
        </button>
        <button
          className={`toggle-btn ${view === 'pie' ? 'active' : ''}`}
          onClick={() => {}}
          type="button"
        >
          Pie Chart
        </button>
      </div>

      {view === 'bar' ? (
        <BarChart
          data={chartData}
          horizontal={selectedMetric === 'roi' || selectedMetric === 'roas'}
          height={selectedMetric === 'roi' || selectedMetric === 'roas' ? Math.max(200, chartData.length * 40) : 200}
          showGrid
          showValues
        />
      ) : (
        <PieChart
          data={pieData}
          size="medium"
        />
      )}

      <div className="roi-summary-cards">
        <div className="roi-summary-card">
          <span className="roi-summary-label">Total Spend</span>
          <span className="roi-summary-value">{formatCurrency(totalSpend)}</span>
        </div>
        <div className="roi-summary-card">
          <span className="roi-summary-label">Total Revenue</span>
          <span className="roi-summary-value">{formatCurrency(totalRevenue)}</span>
        </div>
        <div className="roi-summary-card">
          <span className="roi-summary-label">Overall ROI</span>
          <span className={`roi-summary-value ${overallROI >= 0 ? 'positive' : 'negative'}`}>
            {formatPercentage(overallROI)}
          </span>
        </div>
        <div className="roi-summary-card">
          <span className="roi-summary-label">Overall ROAS</span>
          <span className={`roi-summary-value ${overallROAS >= 1 ? 'positive' : 'negative'}`}>
            {overallROAS.toFixed(2)}x
          </span>
        </div>
      </div>

      <div className="channel-roi-table">
        <table>
          <thead>
            <tr>
              <th>Channel</th>
              <th>Spend</th>
              <th>Revenue</th>
              <th>ROI</th>
              <th>ROAS</th>
              <th>Conv.</th>
            </tr>
          </thead>
          <tbody>
            {sortedData.map((item) => (
              <tr key={item.channel}>
                <td>
                  <div className="channel-name-cell">
                    <div
                      className="channel-indicator"
                      style={{
                        backgroundColor: channelColors[item.channel.toLowerCase()] || '#8B5CF6',
                      }}
                    />
                    <span>{item.channel.split('_').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')}</span>
                  </div>
                </td>
                <td>{formatCurrency(item.spend)}</td>
                <td>{formatCurrency(item.revenue)}</td>
                <td className={item.roi >= 0 ? 'positive' : 'negative'}>
                  {formatPercentage(item.roi)}
                </td>
                <td className={item.roas >= 1 ? 'positive' : 'negative'}>
                  {item.roas.toFixed(2)}x
                </td>
                <td>{formatCompactNumber(item.conversions)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </ChartCard>
  );
};

export default ChannelROIChart;
