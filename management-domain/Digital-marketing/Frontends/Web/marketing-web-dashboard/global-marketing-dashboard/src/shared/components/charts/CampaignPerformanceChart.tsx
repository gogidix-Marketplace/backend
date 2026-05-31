// CampaignPerformanceChart Component
// Displays campaign performance metrics over time

import React, { useState } from 'react';
import { ChartCard } from './ChartCard';
import { LineChart, DataPoint } from './LineChart';
import { Select, SelectOption } from '../common/Select';

export interface CampaignPerformanceData {
  campaignId: string;
  campaignName: string;
  metrics: {
    date: Date;
    impressions: number;
    clicks: number;
    conversions: number;
    cost: number;
    revenue: number;
  }[];
}

export interface CampaignPerformanceChartProps {
  data: CampaignPerformanceData[];
  className?: string;
}

type MetricType = 'impressions' | 'clicks' | 'conversions' | 'cost' | 'revenue' | 'roi';

const metricOptions: SelectOption[] = [
  { value: 'impressions', label: 'Impressions' },
  { value: 'clicks', label: 'Clicks' },
  { value: 'conversions', label: 'Conversions' },
  { value: 'cost', label: 'Cost' },
  { value: 'revenue', label: 'Revenue' },
  { value: 'roi', label: 'ROI' },
];

const metricColors: Record<MetricType, string> = {
  impressions: '#3B82F6',
  clicks: '#8B5CF6',
  conversions: '#10B981',
  cost: '#F59E0B',
  revenue: '#06B6D4',
  roi: '#EC4899',
};

export const CampaignPerformanceChart: React.FC<CampaignPerformanceChartProps> = ({
  data,
  className = '',
}) => {
  const [selectedCampaign, setSelectedCampaign] = useState<string>(data[0]?.campaignId || '');
  const [selectedMetric, setSelectedMetric] = useState<MetricType>('revenue');

  const campaignOptions: SelectOption[] = data.map((c) => ({
    value: c.campaignId,
    label: c.campaignName,
  }));

  const currentCampaign = data.find((c) => c.campaignId === selectedCampaign);

  const chartData: DataPoint[] = currentCampaign?.metrics.map((m) => ({
    label: new Date(m.date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
    value: selectedMetric === 'roi'
      ? m.revenue > 0 ? ((m.revenue - m.cost) / m.cost) * 100 : 0
      : m[selectedMetric as keyof typeof m] as number,
    date: m.date,
  })) || [];

  const formatYValue = (value: number) => {
    switch (selectedMetric) {
      case 'roi':
        return `${value.toFixed(1)}%`;
      case 'cost':
      case 'revenue':
        return `$${(value / 1000).toFixed(0)}K`;
      default:
        return value.toLocaleString();
    }
  };

  return (
    <ChartCard
      title="Campaign Performance"
      subtitle={currentCampaign?.campaignName}
      className={className}
      actions={
        <div style={{ display: 'flex', gap: '0.5rem' }}>
          <Select
            options={campaignOptions}
            value={selectedCampaign}
            onChange={setSelectedCampaign}
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
      <LineChart
        data={chartData}
        color={metricColors[selectedMetric]}
        height={280}
        showArea
        curve="smooth"
      />
      <div className="campaign-chart-stats">
        {currentCampaign && (
          <>
            <div className="stat-item">
              <span className="stat-label">Total {metricOptions.find(o => o.value === selectedMetric)?.label}</span>
              <span className="stat-value">
                {formatYValue(chartData.reduce((sum, d) => sum + d.value, 0))}
              </span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Avg. Daily</span>
              <span className="stat-value">
                {formatYValue(chartData.length > 0 ? chartData.reduce((sum, d) => sum + d.value, 0) / chartData.length : 0)}
              </span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Trend</span>
              <span className="stat-value trend-up">
                {chartData.length >= 2
                  ? ((chartData[chartData.length - 1].value - chartData[0].value) / chartData[0].value * 100).toFixed(1)
                  : 0}%
              </span>
            </div>
          </>
        )}
      </div>
    </ChartCard>
  );
};

export default CampaignPerformanceChart;
