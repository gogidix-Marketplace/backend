// Country Overview Page - Country Marketing Dashboard
// Country-specific marketing metrics and local campaign status

import React from 'react'
import { MetricCard } from '../../../shared/components/common/MetricCard'
import { Button } from '../../../shared/components/common/Button'
import { Card } from '../../../shared/components/common/Card'
import { BarChart, DataPoint } from '../../../shared/components/charts/BarChart'
import { LineChart } from '../../../shared/components/charts/LineChart'
import { PieChart } from '../../../shared/components/charts/PieChart'
import { FunnelChart } from '../../../shared/components/charts/FunnelChart'
import { useLocalCampaignStore } from '../../../infrastructure/stores/localCampaignStore'
import { useLocalLeadStore } from '../../../infrastructure/stores/localLeadStore'
import { useLocalAnalyticsStore } from '../../../infrastructure/stores/localAnalyticsStore'
import { formatCurrency, formatCompactNumber, formatPercentage } from '../../../shared/utils/formatters'
import './CountryOverviewPage.css'

const CountryOverviewPage: React.FC = () => {
  const campaigns = useLocalCampaignStore((state) => state.campaigns)
  const leads = useLocalLeadStore((state) => state.leads)
  const { metrics } = useLocalAnalyticsStore((state) => state.metrics)
  const { loadMetrics } = useLocalAnalyticsStore()

  // Calculate summary metrics
  const summaryMetrics = React.useMemo(() => {
    const activeCampaigns = campaigns.filter((c) => c.status === 'active')
    return {
      totalCampaigns: campaigns.length,
      activeCampaigns: activeCampaigns.length,
      totalBudget: campaigns.reduce((sum, c) => sum + c.budget.total, 0),
      totalSpent: campaigns.reduce((sum, c) => sum + c.budget.spent, 0),
      totalRevenue: campaigns.reduce((sum, c) => sum + c.metrics.revenue, 0),
      totalLeads: campaigns.reduce((sum, c) => sum + c.metrics.leads, 0),
      avgROAS: activeCampaigns.length > 0
        ? activeCampaigns.reduce((sum, c) => sum + c.metrics.roas, 0) / activeCampaigns.length
        : 0,
    }
  }, [campaigns])

  const leadMetrics = React.useMemo(() => {
    return {
      total: leads.length,
      new: leads.filter(l => l.status === 'new').length,
      contacted: leads.filter(l => l.status === 'contacted').length,
      qualified: leads.filter(l => l.status === 'qualified').length,
      converted: leads.filter(l => l.status === 'closed').length,
      totalValue: leads.reduce((sum, l) => sum + l.value.estimated, 0),
    }
  }, [leads])

  const revenueData: DataPoint[] = [
    { label: 'Jan', value: 45000 },
    { label: 'Feb', value: 52000 },
    { label: 'Mar', value: 68000 },
    { label: 'Apr', value: 71000 },
    { label: 'May', value: 75000 },
    { label: 'Jun', value: 82000 },
  ]

  const channelData: DataPoint[] = [
    { label: 'Social', value: 28000 },
    { label: 'Email', value: 35000 },
    { label: 'Search', value: 18000 },
    { label: 'Display', value: 12000 },
    { label: 'Referral', value: 9000 },
  ]

  const leadSourceData = [
    { source: 'Social Media', leads: 350, percentage: 28 },
    { source: 'Website', leads: 280, percentage: 22 },
    { source: 'Email', leads: 220, percentage: 18 },
    { 'source': 'Referral', leads: 180, percentage: 14 },
    { source: 'Events', leads: 120, percentage: 10 },
    { source: 'Other', leads: 100, percentage: 8 },
  ]

  const funnelData = [
    { name: 'Impressions', value: 500000 },
    { name: 'Visitors', value: 125000 },
    { name: 'Leads', value: 1250 },
    { name: 'Qualified', value: 350 },
    { name: 'Closed', value: 175 },
  ]

  return (
    <div className="country-overview-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Country Overview</h1>
              <p className="page__subtitle">United States - Local marketing performance and team KPIs</p>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Country Selector */}
        <div className="country-selector">
          <Button variant="secondary" icon="🌍">Switch Country</Button>
          <span className="current-country">🇺🇸 United States</span>
        </div>

        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard label="Active Campaigns" value={summaryMetrics.activeCampaigns} />
          <MetricCard
            label="Budget Spent"
            value={formatCurrency(summaryMetrics.totalSpent)}
            total={summaryMetrics.totalBudget}
            change={75}
          />
          <MetricCard
            label="Revenue Generated"
            value={formatCurrency(summaryMetrics.totalRevenue)}
            change={28}
            changeType="positive"
          />
          <MetricCard label="Total Leads" value={summaryMetrics.totalLeads} />
          <MetricCard label="Avg ROAS" value={`${summaryMetrics.avgROAS.toFixed(2)}x`} />
        </div>

        {/* Charts Row */}
        <div className="grid grid--2">
          <Card title="Revenue Trend" subtitle="Last 6 months">
            <LineChart
              data={revenueData.map((d) => ({ label: d.label, value: d.value }))}
              color="#10B981"
              height={200}
              showArea
              curve="smooth"
            />
          </Card>

          <Card title="Channel Performance" subtitle="Current period">
            <BarChart
              data={channelData}
              height={200}
              showGrid
              showValues
            />
          </Card>
        </div>

        <div className="grid grid--2">
          <Card title="Lead Funnel" subtitle="Conversion pipeline">
            <FunnelChart data={funnelData} height={220} />
          </Card>

          <Card title="Lead Sources" subtitle="Top performing sources">
            <BarChart
              data={leadSourceData.map((d) => ({ label: d.source, value: d.leads }))}
              horizontal
              height={220}
              showGrid
            />
          </Card>
        </div>

        {/* Top Campaigns */}
        <Card title="Top Performing Campaigns" subtitle="Sorted by revenue">
          <div className="campaign-list">
            {campaigns
              .sort((a, b) => b.metrics.revenue - a.metrics.revenue)
              .slice(0, 5)
              .map((campaign) => (
                <div key={campaign.id} className="campaign-item">
                  <div className="campaign-item-header">
                    <span className="campaign-name">{campaign.name}</span>
                    <span className="campaign-revenue">
                      {formatCurrency(campaign.metrics.revenue)}
                    </span>
                  </div>
                  <div className="campaign-item-stats">
                    <span>{formatCompactNumber(campaign.metrics.impressions)} impressions</span>
                    <span>{formatCompactNumber(campaign.metrics.leads)} leads</span>
                    <span>{campaign.metrics.roas.toFixed(2)}x ROAS</span>
                  </div>
                  <div className="campaign-progress">
                    <div
                      className="progress-bar"
                      style={{
                        width: `${(campaign.budget.spent / campaign.budget.total) * 100}%`,
                        backgroundColor:
                          campaign.budget.spent / campaign.budget.total > 0.9
                            ? '#EF4444'
                            : campaign.budget.spent / campaign.budget.total > 0.75
                            ? '#F59E0B'
                            : '#10B981',
                      }}
                    />
                    <span>
                      {formatPercentage((campaign.budget.spent / campaign.budget.total) * 100)} spent
                    </span>
                  </div>
                </div>
              ))}
          </div>
        </Card>
      </div>
    </div>
  )
}

export default CountryOverviewPage
