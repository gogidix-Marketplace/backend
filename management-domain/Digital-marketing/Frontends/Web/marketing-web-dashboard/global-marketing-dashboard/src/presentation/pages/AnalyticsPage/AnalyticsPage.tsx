// AnalyticsPage - Global Marketing Dashboard
// Marketing analytics and insights page

import React, { useState, useMemo } from 'react';
import { LineChart, DataPoint } from '../../../shared/components/charts/LineChart';
import { BarChart } from '../../../shared/components/charts/BarChart';
import { ChannelROIChart } from '../../../shared/components/charts/ChannelROIChart';
import { EngagementChart } from '../../../shared/components/charts/EngagementChart';
import { FunnelChart } from '../../../shared/components/charts/FunnelChart';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { SelectDropdown } from '../../../shared/components/common/SelectDropdown';
import { formatCurrency, formatCompactNumber, formatPercentage } from '../../../shared/utils/formatters';
import { mockTimeSeriesData, mockChannelMetrics, mockCountryPerformance, mockFunnelData } from '../../../shared/mock-data/analytics.mock';
import './AnalyticsPage.css';

const periodOptions = [
  { value: '7d', label: 'Last 7 Days' },
  { value: '30d', label: 'Last 30 Days' },
  { value: '90d', label: 'Last 90 Days' },
  { value: '1y', label: 'Last Year' },
];

const comparisonOptions = [
  { value: 'previous', label: 'Previous Period' },
  { value: 'lastYear', label: 'Last Year' },
];

const AnalyticsPage: React.FC = () => {
  const [selectedPeriod, setSelectedPeriod] = useState('30d');
  const [comparisonPeriod, setComparisonPeriod] = useState('previous');
  const [activeTab, setActiveTab] = useState('overview');

  // Prepare chart data
  const revenueData = useMemo(() => {
    return mockTimeSeriesData.revenue.map(point => ({
      label: point.date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
      value: point.value,
    }));
  }, []);

  const channelData = useMemo(() => {
    return mockChannelMetrics.map(m => ({
      label: m.channel,
      value: m.revenue,
      color: m.channel === 'Email' ? '#10B981' : m.channel === 'Social' ? '#8B5CF6' : '#3B82F6',
    }));
  }, []);

  const countryData = useMemo(() => {
    return mockCountryPerformance.slice(0, 10).map(c => ({
      label: c.countryName,
      value: c.metrics.revenue,
    }));
  }, []);

  const engagementData = useMemo(() => {
    return [
      {
        platform: 'facebook',
        followers: 150000,
        engagement: 45000,
        likes: 38000,
        comments: 4200,
        shares: 2800,
        clicks: 12500,
        impressions: 2500000,
        engagementRate: 1.8,
      },
      {
        platform: 'instagram',
        followers: 95000,
        engagement: 58000,
        likes: 48000,
        comments: 5600,
        shares: 4400,
        clicks: 18200,
        impressions: 3200000,
        engagementRate: 1.81,
      },
      {
        platform: 'linkedin',
        followers: 75000,
        engagement: 22000,
        likes: 14500,
        comments: 3800,
        shares: 3700,
        clicks: 28500,
        impressions: 850000,
        engagementRate: 2.59,
      },
      {
        platform: 'twitter',
        followers: 45000,
        engagement: 8500,
        likes: 6200,
        comments: 890,
        shares: 1410,
        clicks: 8900,
        impressions: 420000,
        engagementRate: 2.02,
      },
    ];
  }, []);

  const roiData = useMemo(() => {
    return mockChannelMetrics.map(m => ({
      channel: m.channel.toLowerCase().replace(' ', '_'),
      spend: m.spend,
      revenue: m.revenue,
      roi: ((m.revenue - m.spend) / m.spend) * 100,
      roas: m.revenue / m.spend,
      conversions: m.conversions,
      leads: m.leads,
      clicks: m.clicks,
      impressions: m.impressions,
    }));
  }, []);

  const tabs = [
    { id: 'overview', label: 'Overview' },
    { id: 'channels', label: 'Channels' },
    { id: 'engagement', label: 'Engagement' },
    { id: 'attribution', label: 'Attribution' },
  ];

  return (
    <div className="analytics-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Marketing Analytics</h1>
              <p className="page__subtitle">Comprehensive analytics and performance insights</p>
            </div>
            <div className="page__actions">
              <SelectDropdown
                options={periodOptions}
                value={selectedPeriod}
                onChange={setSelectedPeriod}
                placeholder="Select Period"
              />
              <SelectDropdown
                options={comparisonOptions}
                value={comparisonPeriod}
                onChange={setComparisonPeriod}
                placeholder="Compare To"
              />
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard
            label="Total Impressions"
            value={formatCompactNumber(152000000)}
            change={18}
            changeType="positive"
            icon="👁️"
          />
          <MetricCard
            label="Total Clicks"
            value={formatCompactNumber(4520000)}
            change={22}
            changeType="positive"
            icon="🖱️"
          />
          <MetricCard
            label="Conversions"
            value={formatCompactNumber(145000)}
            change={15}
            changeType="positive"
            icon="✅"
          />
          <MetricCard
            label="Avg CTR"
            value="2.97%"
            change={8}
            changeType="positive"
            icon="📊"
          />
          <MetricCard
            label="Avg CPC"
            value="$0.42"
            change={-5}
            changeType="positive"
            icon="💰"
          />
          <MetricCard
            label="Conversion Rate"
            value="3.21%"
            change={12}
            changeType="positive"
            icon="📈"
          />
        </div>

        {/* Tabs */}
        <Tabs
          tabs={tabs}
          activeTab={activeTab}
          onChange={setActiveTab}
          variant="underlined"
        />

        <TabPanel id="overview" activeTab={activeTab}>
          <div className="analytics-grid">
            <div className="chart-card">
              <h3>Revenue Trend</h3>
              <LineChart
                data={revenueData}
                color="#10B981"
                height={250}
                showArea
                curve="smooth"
              />
            </div>

            <div className="chart-card">
              <h3>Revenue by Country</h3>
              <BarChart
                data={countryData}
                horizontal
                height={250}
                showGrid
                showValues
              />
            </div>

            <div className="chart-card">
              <h3>Conversion Funnel</h3>
              <FunnelChart
                data={mockFunnelData}
                height={250}
              />
            </div>
          </div>
        </TabPanel>

        <TabPanel id="channels" activeTab={activeTab}>
          <ChannelROIChart data={roiData} />
        </TabPanel>

        <TabPanel id="engagement" activeTab={activeTab}>
          <EngagementChart data={engagementData} />
        </TabPanel>

        <TabPanel id="attribution" activeTab={activeTab}>
          <div className="attribution-panel">
            <div className="chart-card">
              <h3>First-Touch Attribution</h3>
              <BarChart
                data={channelData}
                height={250}
                showGrid
                showValues
              />
            </div>
            <div className="chart-card">
              <h3>Last-Touch Attribution</h3>
              <BarChart
                data={[...channelData].sort((a, b) => b.value - a.value)}
                height={250}
                showGrid
                showValues
              />
            </div>
          </div>
        </TabPanel>
      </div>
    </div>
  );
};

export default AnalyticsPage;
