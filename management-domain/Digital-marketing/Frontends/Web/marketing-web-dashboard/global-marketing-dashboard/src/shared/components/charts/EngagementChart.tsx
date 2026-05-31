// EngagementChart Component
// Displays social media engagement metrics across platforms

import React, { useState, useMemo } from 'react';
import { ChartCard } from './ChartCard';
import { BarChart, DataPoint } from './BarChart';
import { Select, SelectOption } from '../common/Select';
import { formatCompactNumber, formatPercentage } from '../../utils/formatters';

export interface EngagementData {
  platform: string;
  followers: number;
  engagement: number;
  likes: number;
  comments: number;
  shares: number;
  clicks: number;
  impressions: number;
  engagementRate: number;
}

export interface EngagementChartProps {
  data: EngagementData[];
  className?: string;
}

type MetricType = 'engagement' | 'likes' | 'comments' | 'shares' | 'clicks' | 'impressions' | 'engagementRate';

const metricOptions: SelectOption[] = [
  { value: 'engagement', label: 'Total Engagement' },
  { value: 'likes', label: 'Likes' },
  { value: 'comments', label: 'Comments' },
  { value: 'shares', label: 'Shares' },
  { value: 'clicks', label: 'Clicks' },
  { value: 'impressions', label: 'Impressions' },
  { value: 'engagementRate', label: 'Engagement Rate' },
];

const platformColors: Record<string, string> = {
  facebook: '#1877F2',
  instagram: '#E4405F',
  twitter: '#1DA1F2',
  linkedin: '#0A66C2',
  tiktok: '#000000',
  youtube: '#FF0000',
  pinterest: '#E60023',
};

export const EngagementChart: React.FC<EngagementChartProps> = ({
  data,
  className = '',
}) => {
  const [selectedMetric, setSelectedMetric] = useState<MetricType>('engagement');
  const [sortBy, setSortBy] = useState<'name' | 'value'>('value');

  const sortedData = useMemo(() => {
    const sorted = [...data].sort((a, b) => {
      if (sortBy === 'value') {
        return (b[selectedMetric] as number) - (a[selectedMetric] as number);
      }
      return a.platform.localeCompare(b.platform);
    });
    return sorted;
  }, [data, selectedMetric, sortBy]);

  const chartData: DataPoint[] = sortedData.map((item) => ({
    label: item.platform.charAt(0).toUpperCase() + item.platform.slice(1),
    value: item[selectedMetric] as number,
    color: platformColors[item.platform.toLowerCase()] || '#8B5CF6',
  }));

  const totalValue = chartData.reduce((sum, d) => sum + d.value, 0);
  const avgValue = chartData.length > 0 ? totalValue / chartData.length : 0;

  const formatValue = (value: number, metric: MetricType): string => {
    if (metric === 'engagementRate') {
      return formatPercentage(value);
    }
    return formatCompactNumber(value);
  };

  return (
    <ChartCard
      title="Social Media Engagement"
      subtitle="Performance across all platforms"
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
      <BarChart
        data={chartData}
        horizontal
        height={Math.max(200, chartData.length * 40)}
        showGrid
        showValues
      />

      <div className="engagement-chart-stats">
        <div className="stat-item">
          <span className="stat-label">Total {metricOptions.find(o => o.value === selectedMetric)?.label}</span>
          <span className="stat-value">{formatValue(totalValue, selectedMetric)}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Average</span>
          <span className="stat-value">{formatValue(avgValue, selectedMetric)}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Top Platform</span>
          <span className="stat-value">{chartData[0]?.label || 'N/A'}</span>
        </div>
      </div>

      <div className="platform-breakdown">
        {sortedData.map((item) => (
          <div key={item.platform} className="platform-row">
            <div className="platform-info">
              <div
                className="platform-indicator"
                style={{
                  backgroundColor: platformColors[item.platform.toLowerCase()] || '#8B5CF6',
                }}
              />
              <span className="platform-name">
                {item.platform.charAt(0).toUpperCase() + item.platform.slice(1)}
              </span>
            </div>
            <div className="platform-metrics">
              <span className="platform-metric">
                {formatCompactNumber(item.followers)} followers
              </span>
              <span className="platform-metric">
                {formatPercentage(item.engagementRate)} rate
              </span>
            </div>
          </div>
        ))}
      </div>
    </ChartCard>
  );
};

export default EngagementChart;
