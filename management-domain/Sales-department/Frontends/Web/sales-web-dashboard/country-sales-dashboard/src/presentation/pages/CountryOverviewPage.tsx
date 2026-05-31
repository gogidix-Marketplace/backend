// CountryOverviewPage Component
// Main dashboard showing country-specific metrics

import React, { useEffect } from 'react';
import { useDashboardStore } from '@infrastructure/stores';
import { mockDashboardSummary, mockSalesTeams, mockPartners, mockPipelineData, mockDeals } from '@shared/mock-data';
import { MetricCard, Card } from '../components/common';
import { RevenueChart, PipelineChart, PartnerDistributionChart } from '../components/charts';
import { formatCurrency } from '@shared';

export const CountryOverviewPage: React.FC = () => {
  const {
    summary,
    alerts,
    isLoading,
    loadDashboardSummary,
    loadAlerts,
  } = useDashboardStore();

  useEffect(() => {
    // Load data (using mock data for now)
    loadDashboardSummary();
    loadAlerts();
  }, [loadDashboardSummary, loadAlerts]);

  // For development, use mock data if store is empty
  const displaySummary = summary || mockDashboardSummary;
  const displayAlerts = alerts.length > 0 ? alerts : mockDashboardSummary.alerts;

  const revenueChartData = [
    { period: 'Sep', revenue: 22000000, target: 23000000 },
    { period: 'Oct', revenue: 24500000, target: 24000000 },
    { period: 'Nov', revenue: 26000000, target: 25000000 },
    { period: 'Dec', revenue: 26000000, target: 25000000 },
    { period: 'Jan', revenue: 24800000, target: 24500000 },
    { period: 'Feb', revenue: 28500000, target: 25000000 },
  ];

  const partnerDistributionData = [
    { type: 'COURIER' as const, count: 35 },
    { type: 'HAULAGE' as const, count: 28 },
    { type: 'WAREHOUSE' as const, count: 22 },
    { type: 'ECOMMERCE' as const, count: 31 },
    { type: 'AIR_OCEAN' as const, count: 15 },
    { type: 'WHOLESALE' as const, count: 18 },
    { type: 'INFLUENCER' as const, count: 7 },
  ];

  return (
    <div className="page country-overview-page">
      <div className="page-header">
        <div className="page-title">
          <h1>{displaySummary.country.flag} {displaySummary.country.name} Dashboard</h1>
          <p className="page-subtitle">{displaySummary.period.label}</p>
        </div>
        <div className="page-actions">
          <button className="btn btn-secondary">Export Report</button>
          <button className="btn btn-primary">Add Deal</button>
        </div>
      </div>

      {/* Alerts */}
      {displayAlerts.filter(a => !a.isRead).length > 0 && (
        <div className="alerts-banner">
          {displayAlerts.filter(a => !a.isRead).slice(0, 3).map(alert => (
            <div key={alert.id} className={`alert alert-${alert.type}`}>
              <span className="alert-icon">
                {alert.type === 'warning' && '\u26A0'}
                {alert.type === 'success' && '\u2705'}
                {alert.type === 'info' && '\u2139'}
                {alert.type === 'error' && '\u274C'}
              </span>
              <div className="alert-content">
                <span className="alert-title">{alert.title}</span>
                <span className="alert-message">{alert.message}</span>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Key Metrics */}
      <div className="metrics-grid">
        <MetricCard
          title="Revenue"
          value={displaySummary.metrics.revenue.value}
          change={displaySummary.metrics.revenue.change}
          changeType={displaySummary.metrics.revenue.changeType}
          target={displaySummary.metrics.revenue.target}
          currency={displaySummary.metrics.revenue.currency}
          prefix=""
          isLoading={isLoading}
        />
        <MetricCard
          title="Pipeline Value"
          value={displaySummary.metrics.pipelineValue.value}
          change={displaySummary.metrics.pipelineValue.change}
          changeType={displaySummary.metrics.pipelineValue.changeType}
          currency={displaySummary.metrics.pipelineValue.currency}
          isLoading={isLoading}
        />
        <MetricCard
          title="Deals Closed"
          value={displaySummary.metrics.dealsClosed.value}
          change={displaySummary.metrics.dealsClosed.change}
          changeType={displaySummary.metrics.dealsClosed.changeType}
          isLoading={isLoading}
        />
        <MetricCard
          title="Win Rate"
          value={displaySummary.metrics.winRate.value}
          change={displaySummary.metrics.winRate.change}
          changeType={displaySummary.metrics.winRate.changeType}
          suffix="%"
          isLoading={isLoading}
        />
      </div>

      <div className="dashboard-grid">
        {/* Revenue Trend Chart */}
        <Card className="chart-card">
          <div className="card-header">
            <h3>Revenue Trend</h3>
            <select className="period-select">
              <option>6 Months</option>
              <option>3 Months</option>
              <option>This Year</option>
            </select>
          </div>
          <RevenueChart data={revenueChartData} height={250} showTarget />
        </Card>

        {/* Pipeline Chart */}
        <Card className="chart-card">
          <div className="card-header">
            <h3>Pipeline by Stage</h3>
            <button className="btn-link">View Pipeline</button>
          </div>
          <PipelineChart data={mockPipelineData.byStage} height={250} />
        </Card>

        {/* Team Rankings */}
        <Card className="ranking-card">
          <div className="card-header">
            <h3>Team Rankings</h3>
            <button className="btn-link">View All</button>
          </div>
          <div className="team-rankings">
            {displaySummary.teamRankings.map((team, index) => (
              <div key={team.teamId} className="ranking-item">
                <div className="ranking-position">
                  <span className={`position-badge position-${index + 1}`}>
                    {team.rank}
                  </span>
                </div>
                <div className="ranking-info">
                  <span className="ranking-name">{team.teamName}</span>
                  <span className="ranking-lead">{team.teamLead}</span>
                </div>
                <div className="ranking-stats">
                  <div className="ranking-revenue">
                    {formatCurrency(team.revenue, 'NGN')}
                  </div>
                  <div className="ranking-attainment">
                    {team.attainment}% of quota
                  </div>
                </div>
                <div className="ranking-trend">
                  <span className={`trend-icon trend-${team.trend}`}>
                    {team.trend === 'up' && '\u2197'}
                    {team.trend === 'down' && '\u2198'}
                    {team.trend === 'neutral' && '\u2192'}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </Card>

        {/* Partner Distribution */}
        <Card className="chart-card">
          <div className="card-header">
            <h3>Partner Distribution</h3>
            <button className="btn-link">View Partners</button>
          </div>
          <PartnerDistributionChart data={partnerDistributionData} size="md" />
        </Card>

        {/* Top Performers */}
        <Card className="performers-card">
          <div className="card-header">
            <h3>Top Performers</h3>
            <button className="btn-link">View All</button>
          </div>
          <div className="top-performers">
            {displaySummary.topPerformers.slice(0, 5).map((performer, index) => (
              <div key={performer.userId} className="performer-item">
                <div className="performer-rank">#{performer.rank}</div>
                <div className="performer-avatar">
                  {performer.avatar ? (
                    <img src={performer.avatar} alt="" />
                  ) : (
                    <span>{performer.name.split(' ').map(n => n[0]).join('')}</span>
                  )}
                </div>
                <div className="performer-info">
                  <span className="performer-name">{performer.name}</span>
                  <span className="performer-team">{performer.team}</span>
                </div>
                <div className="performer-metrics">
                  <div className="performer-revenue">
                    {formatCurrency(performer.revenue, 'NGN')}
                  </div>
                  <div className="performer-attainment">
                    {performer.attainment}%
                  </div>
                </div>
              </div>
            ))}
          </div>
        </Card>

        {/* Recent Deals */}
        <Card className="deals-card">
          <div className="card-header">
            <h3>Recent Deals</h3>
            <button className="btn-link">View All</button>
          </div>
          <div className="recent-deals">
            {displaySummary.majorDeals.slice(0, 4).map(deal => (
              <div key={deal.id} className="deal-item">
                <div className="deal-info">
                  <span className="deal-name">{deal.name}</span>
                  <span className="deal-account">{deal.accountName}</span>
                </div>
                <div className="deal-value">
                  {formatCurrency(deal.value, deal.currency)}
                </div>
                <div className={`deal-stage deal-stage-${deal.stage.toLowerCase()}`}>
                  {deal.stage}
                </div>
              </div>
            ))}
          </div>
        </Card>
      </div>

      {/* Forecast Summary */}
      <Card className="forecast-card">
        <div className="card-header">
          <h3>Forecast Summary - {displaySummary.forecastSummary.period}</h3>
          {displaySummary.forecastSummary.accuracy && (
            <span className="forecast-accuracy">
              Accuracy: {displaySummary.forecastSummary.accuracy}%
            </span>
          )}
        </div>
        <div className="forecast-grid">
          <div className="forecast-item">
            <span className="forecast-label">Forecast</span>
            <span className="forecast-value forecast-primary">
              {formatCurrency(displaySummary.forecastSummary.forecast, displaySummary.forecastSummary.currency)}
            </span>
          </div>
          <div className="forecast-item">
            <span className="forecast-label">Best Case</span>
            <span className="forecast-value forecast-success">
              {formatCurrency(displaySummary.forecastSummary.bestCase, displaySummary.forecastSummary.currency)}
            </span>
          </div>
          <div className="forecast-item">
            <span className="forecast-label">Worst Case</span>
            <span className="forecast-value forecast-warning">
              {formatCurrency(displaySummary.forecastSummary.worstCase, displaySummary.forecastSummary.currency)}
            </span>
          </div>
          <div className="forecast-item">
            <span className="forecast-label">Weighted Pipeline</span>
            <span className="forecast-value">
              {formatCurrency(displaySummary.forecastSummary.weightedPipeline, 'USD')}
            </span>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default CountryOverviewPage;
