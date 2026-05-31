// OverviewPage - Global Marketing Dashboard
// Main dashboard with high-level metrics and insights

import React, { useMemo, useState } from 'react';
import { Card, CardMetric } from '../../../shared/components/common/Card';
import { StatusBadge } from '../../../shared/components/common/StatusBadge';
import { CampaignsTable } from '../../../shared/components/tables/CampaignsTable';
import { LineChart } from '../../../shared/components/charts/LineChart';
import { BarChart } from '../../../shared/components/charts/BarChart';
import { PieChart } from '../../../shared/components/charts/PieChart';
import { CampaignPerformanceChart } from '../../../shared/components/charts/CampaignPerformanceChart';
import { BudgetChart } from '../../../shared/components/charts/BudgetChart';
import { LeadSourceChart } from '../../../shared/components/charts/LeadSourceChart';
import { useCampaignStore } from '../../../infrastructure/stores/campaignStore';
import { useBudgetStore } from '../../../infrastructure/stores/budgetStore';
import { useLeadStore } from '../../../infrastructure/stores/leadStore';
import { useAnalyticsStore } from '../../../infrastructure/stores/analyticsStore';
import { formatCurrency, formatCompactNumber, formatPercentage } from '../../../shared/utils/formatters';
import { getCampaignStatusColor } from '../../../shared/constants/channels';
import mockCampaigns from '../../../shared/mock-data/campaigns.mock';
import { mockTimeSeriesData, mockChannelMetrics, mockCountryPerformance } from '../../../shared/mock-data/analytics.mock';
import './OverviewPage.css';

const OverviewPage: React.FC = () => {
  const campaigns = useCampaignStore((state) => state.campaigns);
  const budgets = useBudgetStore((state) => state.budgets);
  const { metrics: leadMetrics } = useLeadStore();
  const { alerts, unreadAlertCount } = useAnalyticsStore();

  // Calculate summary metrics
  const summaryMetrics = useMemo(() => {
    const activeCampaigns = campaigns.filter((c) => c.status === 'active');
    const totalBudget = campaigns.reduce((sum, c) => sum + c.budget.total, 0);
    const totalSpent = campaigns.reduce((sum, c) => sum + c.budget.spent, 0);
    const totalRevenue = campaigns.reduce((sum, c) => sum + c.metrics.revenue, 0);
    const totalLeads = campaigns.reduce((sum, c) => sum + c.metrics.leads, 0);
    const avgROAS = activeCampaigns.length > 0
      ? activeCampaigns.reduce((sum, c) => sum + c.metrics.roas, 0) / activeCampaigns.length
      : 0;

    return {
      totalCampaigns: campaigns.length,
      activeCampaigns: activeCampaigns.length,
      totalBudget,
      totalSpent,
      totalRevenue,
      totalLeads,
      avgROAS,
      budgetUtilization: totalBudget > 0 ? (totalSpent / totalBudget) * 100 : 0,
      overallROI: totalSpent > 0 ? ((totalRevenue - totalSpent) / totalSpent) * 100 : 0,
    };
  }, [campaigns]);

  // Prepare chart data
  const performanceTrendData = useMemo(() => {
    return mockTimeSeriesData.revenue.map((point, i) => ({
      label: point.date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
      value: point.value,
    }));
  }, []);

  const channelData = useMemo(() => {
    return mockChannelMetrics.map((m) => ({
      label: m.channel,
      value: m.revenue,
    }));
  }, []);

  const countryData = useMemo(() => {
    return mockCountryPerformance.slice(0, 8).map((c) => ({
      label: c.countryName,
      value: c.metrics.revenue,
    }));
  }, []);

  const topCampaigns = useMemo(() => {
    return [...campaigns]
      .sort((a, b) => b.metrics.roas - a.metrics.roas)
      .slice(0, 5);
  }, [campaigns]);

  const recentAlerts = useMemo(() => {
    return alerts.slice(0, 3);
  }, [alerts]);

  return (
    <div className="overview-page">
      {/* Header */}
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Global Marketing Overview</h1>
              <p className="page__subtitle">Track your marketing performance across all campaigns and regions</p>
            </div>
            <div className="page__actions">
              {unreadAlertCount > 0 && (
                <div className="alert-badge">
                  <span className="alert-badge__icon">!</span>
                  {unreadAlertCount} new alerts
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <CardMetric
            label="Active Campaigns"
            value={summaryMetrics.activeCampaigns}
            change={summaryMetrics.activeCampaigns > 0 ? 12 : 0}
            changeType="positive"
            icon={<span>📢</span>}
          />
          <CardMetric
            label="Total Budget"
            value={formatCurrency(summaryMetrics.totalBudget)}
            change={summaryMetrics.budgetUtilization > 75 ? -5 : 8}
            changeType={summaryMetrics.budgetUtilization > 75 ? 'negative' : 'positive'}
            icon={<span>💰</span>}
          />
          <CardMetric
            label="Total Spent"
            value={formatCurrency(summaryMetrics.totalSpent)}
            change={15}
            changeType="positive"
            icon={<span>📊</span>}
          />
          <CardMetric
            label="Revenue Generated"
            value={formatCurrency(summaryMetrics.totalRevenue)}
            change={22}
            changeType="positive"
            icon={<span>💵</span>}
          />
          <CardMetric
            label="Total Leads"
            value={formatCompactNumber(summaryMetrics.totalLeads)}
            change={18}
            changeType="positive"
            icon={<span>👥</span>}
          />
          <CardMetric
            label="Average ROAS"
            value={`${summaryMetrics.avgROAS.toFixed(2)}x`}
            change={5}
            changeType="positive"
            icon={<span>📈</span>}
          />
        </div>

        {/* Charts Row */}
        <div className="grid grid--3">
          <Card
            title="Revenue Trend"
            subtitle="Last 8 weeks"
            className="chart-card-full"
          >
            <LineChart
              data={performanceTrendData}
              color="#10B981"
              height={200}
              showArea
              curve="smooth"
            />
          </Card>

          <Card
            title="Revenue by Channel"
            subtitle="Current period"
          >
            <BarChart
              data={channelData}
              height={200}
              showGrid
              showValues
            />
          </Card>

          <Card
            title="Revenue by Country"
            subtitle="Top 8 markets"
          >
            <BarChart
              data={countryData}
              horizontal
              height={200}
              showGrid
            />
          </Card>
        </div>

        {/* Campaign Performance & Alerts */}
        <div className="grid grid--2">
          <Card title="Top Performing Campaigns" subtitle="By ROAS">
            <div className="campaign-list">
              {topCampaigns.map((campaign) => (
                <div key={campaign.id} className="campaign-list-item">
                  <div className="campaign-item-main">
                    <div className="campaign-item-info">
                      <span className="campaign-item-name">{campaign.name}</span>
                      <StatusBadge status={campaign.status} size="sm" />
                    </div>
                    <div className="campaign-item-metrics">
                      <div className="campaign-metric">
                        <span className="campaign-metric-label">ROAS</span>
                        <span className="campaign-metric-value">
                          {campaign.metrics.roas.toFixed(2)}x
                        </span>
                      </div>
                      <div className="campaign-metric">
                        <span className="campaign-metric-label">Revenue</span>
                        <span className="campaign-metric-value">
                          {formatCurrency(campaign.metrics.revenue)}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div className="campaign-item-progress">
                    <div className="campaign-progress-bar">
                      <div
                        className="campaign-progress-fill"
                        style={{
                          width: `${Math.min((campaign.budget.spent / campaign.budget.total) * 100, 100)}%`,
                          backgroundColor: getCampaignStatusColor(campaign.status),
                        }}
                      />
                    </div>
                    <span className="campaign-progress-text">
                      {formatPercentage((campaign.budget.spent / campaign.budget.total) * 100)} budget used
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </Card>

          <Card title="Recent Alerts" subtitle="Requires attention">
            <div className="alerts-list">
              {recentAlerts.length === 0 ? (
                <div className="alerts-empty">No alerts</div>
              ) : (
                recentAlerts.map((alert) => (
                  <div key={alert.id} className={`alert-item alert-item--${alert.type}`}>
                    <div className="alert-icon">
                      {alert.type === 'error' && '⚠️'}
                      {alert.type === 'warning' && '⚡'}
                      {alert.type === 'success' && '✅'}
                      {alert.type === 'info' && 'ℹ️'}
                    </div>
                    <div className="alert-content">
                      <span className="alert-title">{alert.title}</span>
                      <span className="alert-message">{alert.message}</span>
                    </div>
                    <span className="alert-time">
                      {new Date(alert.createdAt).toLocaleDateString()}
                    </span>
                  </div>
                ))
              )}
            </div>
          </Card>
        </div>

        {/* Budget & Leads Overview */}
        <div className="grid grid--2">
          <BudgetChart
            allocations={budgets[0]?.allocations || []}
            currency={budgets[0]?.currency || 'USD'}
            view="pie"
          />
          <LeadSourceChart
            data={Object.entries(leadMetrics.bySource).map(([source, leads]) => ({
              source: source.charAt(0).toUpperCase() + source.slice(1).replace('_', ' '),
              leads,
              percentage: (leads / leadMetrics.total) * 100,
              conversionRate: 2.5 + Math.random() * 5,
            }))}
            view="horizontal"
          />
        </div>
      </div>
    </div>
  );
};

export default OverviewPage;
