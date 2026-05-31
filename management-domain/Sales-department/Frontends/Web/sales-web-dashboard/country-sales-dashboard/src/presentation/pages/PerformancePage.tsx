// PerformancePage Component
// Team and individual performance tracking

import React, { useState, useEffect } from 'react';
import { useDashboardStore } from '@infrastructure/stores';
import { mockPerformanceData, mockSalesTeams } from '@shared/mock-data';
import { PerformanceChart, RevenueChart } from '../components/charts';
import { SearchBar, Button, Tabs, Card, MetricCard, StatusBadge } from '../components/common';
import { formatCurrency, formatPercentage } from '@shared';

export const PerformancePage: React.FC = () => {
  const { summary } = useDashboardStore();
  const [period, setPeriod] = useState<'month' | 'quarter' | 'year'>('month');
  const [activeTab, setActiveTab] = useState('overview');

  const displayData = mockPerformanceData;

  const summaryCards = [
    {
      title: 'Revenue',
      value: formatCurrency(displayData.summary.revenue.actual, 'NGN'),
      target: formatCurrency(displayData.summary.revenue.target, 'NGN'),
      attainment: `${displayData.summary.revenue.attainment}%`,
      change: `${displayData.summary.revenue.change > 0 ? '+' : ''}${displayData.summary.revenue.change}%`,
      positive: displayData.summary.revenue.change >= 0,
    },
    {
      title: 'Quota Attainment',
      value: `${displayData.summary.quota.attainment}%`,
      target: '100%',
      attainment: `${displayData.summary.quota.attainment}%`,
      change: `${displayData.summary.quota.change > 0 ? '+' : ''}${displayData.summary.quota.change}%`,
      positive: displayData.summary.quota.change >= 0,
    },
    {
      title: 'Deals Closed',
      value: displayData.summary.deals.closed.toString(),
      target: '-',
      attainment: '-',
      change: `${displayData.summary.deals.change > 0 ? '+' : ''}${displayData.summary.deals.change}%`,
      positive: displayData.summary.deals.change >= 0,
    },
    {
      title: 'Win Rate',
      value: formatPercentage(displayData.summary.winRate.value),
      target: '35%',
      attainment: '-',
      change: `${displayData.summary.winRate.change > 0 ? '+' : ''}${displayData.summary.winRate.change}%`,
      positive: displayData.summary.winRate.change >= 0,
    },
  ];

  const teamComparisonData = displayData.teamComparison.map(team => ({
    label: team.teamName.slice(0, 15),
    value: team.metrics.revenue,
    target: team.metrics.quota,
    color: team.metrics.quotaAttainment >= 100 ? '#10B981' : team.metrics.quotaAttainment >= 80 ? '#F59E0B' : '#EF4444',
  }));

  const trendChartData = displayData.trendAnalysis.map(t => ({
    period: t.period,
    revenue: t.revenue,
    target: t.target,
  }));

  const tabs = [
    {
      id: 'overview',
      label: 'Overview',
      content: (
        <div className="performance-overview">
          <div className="performance-summary-cards">
            {summaryCards.map((card, i) => (
              <Card key={i} className={`summary-card ${card.positive ? 'positive' : 'negative'}`}>
                <span className="card-title">{card.title}</span>
                <span className="card-value">{card.value}</span>
                <div className="card-details">
                  <span className="card-target">Target: {card.target}</span>
                  <span className={`card-change ${card.positive ? 'positive' : 'negative'}`}>
                    {card.change}
                  </span>
                </div>
              </Card>
            ))}
          </div>

          <div className="performance-charts">
            <Card>
              <h4>Revenue Trend</h4>
              <RevenueChart data={trendChartData} height={250} showTarget />
            </Card>

            <Card>
              <h4>Team Comparison</h4>
              <PerformanceChart data={teamComparisonData} type="bar" height={250} />
            </Card>
          </div>
        </div>
      ),
    },
    {
      id: 'teams',
      label: 'Team Performance',
      content: (
        <div className="team-performance">
          <div className="team-performance-list">
            {displayData.teamComparison.map((team, index) => (
              <Card key={team.teamId} className="team-performance-card">
                <div className="team-performance-header">
                  <div className="team-rank">#{index + 1}</div>
                  <div className="team-info">
                    <h4>{team.teamName}</h4>
                    <span className="team-lead">{team.members[0]?.name || 'No lead'}</span>
                  </div>
                  <div className="team-trend">
                    <span className={`trend-icon trend-${team.trend}`}>
                      {team.trend === 'up' && '\u2197'}
                      {team.trend === 'down' && '\u2198'}
                      {team.trend === 'neutral' && '\u2192'}
                    </span>
                  </div>
                </div>

                <div className="team-performance-metrics">
                  <div className="metric">
                    <span className="metric-label">Revenue</span>
                    <span className="metric-value">{formatCurrency(team.metrics.revenue, 'NGN')}</span>
                  </div>
                  <div className="metric">
                    <span className="metric-label">Quota</span>
                    <span className="metric-value">{formatCurrency(team.metrics.quota, 'NGN')}</span>
                  </div>
                  <div className="metric">
                    <span className="metric-label">Attainment</span>
                    <span className={`metric-value ${team.metrics.quotaAttainment >= 100 ? 'success' : ''}`}>
                      {formatPercentage(team.metrics.quotaAttainment)}
                    </span>
                  </div>
                  <div className="metric">
                    <span className="metric-label">Deals</span>
                    <span className="metric-value">{team.metrics.dealsClosed}</span>
                  </div>
                </div>

                <div className="team-members-preview">
                  <span className="members-count">{team.members.length} members</span>
                  <div className="members-avatars">
                    {team.members.slice(0, 5).map(member => (
                      <div key={member.userId} className="member-avatar" title={member.name}>
                        {member.avatar ? (
                          <img src={member.avatar} alt="" />
                        ) : (
                          <span>{member.name.split(' ').map(n => n[0]).join('')}</span>
                        )}
                      </div>
                    ))}
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>
      ),
    },
    {
      id: 'individuals',
      label: 'Individual Rankings',
      content: (
        <div className="individual-rankings">
          <Card>
            <div className="rankings-table">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Rank</th>
                    <th>Sales Person</th>
                    <th>Team</th>
                    <th>Revenue</th>
                    <th>Attainment</th>
                    <th>Deals</th>
                    <th>Activities</th>
                  </tr>
                </thead>
                <tbody>
                  {displayData.individualRankings.map((individual) => (
                    <tr key={individual.userId}>
                      <td>
                        <span className={`rank-badge rank-${individual.rank <= 3 ? individual.rank : 'default'}`}>
                          #{individual.rank}
                        </span>
                      </td>
                      <td>
                        <div className="individual-info">
                          {individual.avatar ? (
                            <img src={individual.avatar} alt="" className="individual-avatar" />
                          ) : (
                            <div className="individual-avatar">
                              {individual.name.split(' ').map(n => n[0]).join('')}
                            </div>
                          )}
                          <span>{individual.name}</span>
                        </div>
                      </td>
                      <td>{individual.team}</td>
                      <td>{formatCurrency(individual.revenue, 'NGN')}</td>
                      <td>
                        <span className={individual.attainment >= 100 ? 'attainment-success' : ''}>
                          {formatPercentage(individual.attainment)}
                        </span>
                      </td>
                      <td>{individual.dealsClosed}</td>
                      <td>
                        <div className="activities-mini">
                          <span title="Calls">\u260E {individual.activities.calls}</span>
                          <span title="Emails">\u2709 {individual.activities.emails}</span>
                          <span title="Meetings">\uD83D\uDCC5 {individual.activities.meetings}</span>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </Card>
        </div>
      ),
    },
    {
      id: 'products',
      label: 'Product Performance',
      content: (
        <div className="product-performance">
          <Card>
            <h4>Product Breakdown</h4>
            <div className="product-list">
              {displayData.productBreakdown.map(product => (
                <div key={product.productCode} className="product-item">
                  <div className="product-info">
                    <span className="product-name">{product.productName}</span>
                    <span className="product-category">{product.category}</span>
                  </div>
                  <div className="product-metrics">
                    <span className="product-revenue">{formatCurrency(product.revenue, 'NGN')}</span>
                    <span className="product-share">{formatPercentage(product.percentage)}</span>
                    <span className={`product-growth ${product.growth >= 0 ? 'positive' : 'negative'}`}>
                      {product.growth > 0 ? '+' : ''}{product.growth}%
                    </span>
                  </div>
                  <div className="product-attainment">
                    <span className="attainment-label">Target Attainment</span>
                    <div className="attainment-bar">
                      <div
                        className={`attainment-fill ${product.targetAttainment >= 100 ? 'success' : ''}`}
                        style={{ width: `${Math.min(product.targetAttainment, 100)}%` }}
                      />
                    </div>
                    <span className="attainment-value">{formatPercentage(product.targetAttainment)}</span>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>
      ),
    },
  ];

  return (
    <div className="page performance-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Performance</h1>
          <p className="page-subtitle">Track sales performance and metrics</p>
        </div>
        <div className="page-actions">
          <select
            value={period}
            onChange={(e) => setPeriod(e.target.value as any)}
            className="period-select"
          >
            <option value="month">This Month</option>
            <option value="quarter">This Quarter</option>
            <option value="year">This Year</option>
          </select>
          <Button variant="secondary">Export</Button>
        </div>
      </div>

      {/* Summary Metrics */}
      <div className="performance-metrics-summary">
        <MetricCard
          title="Revenue"
          value={displayData.summary.revenue.actual}
          change={displayData.summary.revenue.change}
          changeType={displayData.summary.revenue.change >= 0 ? 'increase' : 'decrease'}
          target={displayData.summary.revenue.target}
          currency="NGN"
        />
        <MetricCard
          title="Quota Attainment"
          value={displayData.summary.quota.attainment}
          change={displayData.summary.quota.change}
          changeType={displayData.summary.quota.change >= 0 ? 'increase' : 'decrease'}
          suffix="%"
        />
        <MetricCard
          title="Deals Closed"
          value={displayData.summary.deals.closed}
          change={displayData.summary.deals.change}
          changeType={displayData.summary.deals.change >= 0 ? 'increase' : 'decrease'}
        />
        <MetricCard
          title="Win Rate"
          value={displayData.summary.winRate.value}
          change={displayData.summary.winRate.change}
          changeType={displayData.summary.winRate.change >= 0 ? 'increase' : 'decrease'}
          suffix="%"
        />
      </div>

      {/* Tabs */}
      <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} />
    </div>
  );
};

export default PerformancePage;
