// Global Overview Page
// Main dashboard showing worldwide sales metrics and country performance

import { useEffect } from 'react';
import { useDashboardStore } from '@infrastructure';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { formatCurrency, formatDate } from '@shared';
import './GlobalOverviewPage.css';

export function GlobalOverviewPage() {
  const { summary, isLoading, loadGlobalSummary, error } = useDashboardStore();

  useEffect(() => {
    loadGlobalSummary();
  }, []);

  if (error) {
    return (
      <div className="error-state">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
          <circle cx="12" cy="12" r="10" />
          <line x1="12" y1="8" x2="12" y2="12" />
          <line x1="12" y1="16" x2="12.01" y2="16" />
        </svg>
        <h3>Failed to load dashboard data</h3>
        <p>{error}</p>
        <Button onClick={() => loadGlobalSummary()}>Retry</Button>
      </div>
    );
  }

  if (!summary) {
    return (
      <div className="loading-state">
        <div className="loading-spinner"></div>
        <p>Loading dashboard...</p>
      </div>
    );
  }

  const { globalMetrics, countriesSummary, topPerformers, majorDeals, alerts, forecastSummary } = summary;

  return (
    <div className="global-overview-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Global Overview</h1>
          <p className="page-subtitle">
            Worldwide sales performance for {formatDate(summary.period.start)} - {formatDate(summary.period.end)}
          </p>
        </div>
        <div className="page-actions">
          <Button variant="ghost" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
              <line x1="3" y1="9" x2="21" y2="9" />
              <line x1="9" y1="21" x2="9" y2="9" />
            </svg>
            Dashboard
          </Button>
          <Button variant="secondary" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <polyline points="7 10 12 15 17 10" />
              <line x1="12" y1="15" x2="12" y2="3" />
            </svg>
            Export
          </Button>
        </div>
      </div>

      {/* Alerts */}
      {alerts.length > 0 && (
        <div className="alerts-section">
          {alerts.map((alert) => (
            <div key={alert.id} className={`alert alert-${alert.type}`}>
              <span className="alert-icon">
                {alert.type === 'warning' && '\u26A0'}
                {alert.type === 'success' && '\u2705'}
                {alert.type === 'error' && '\u274C'}
                {alert.type === 'info' && '\u2139'}
              </span>
              <div className="alert-content">
                <strong>{alert.title}</strong>
                <p>{alert.message}</p>
              </div>
              {alert.actionUrl && (
                <a href={alert.actionUrl} className="alert-action">
                  View Details
                </a>
              )}
            </div>
          ))}
        </div>
      )}

      {/* Key Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Global Revenue"
            value={globalMetrics.revenue.label}
            change={globalMetrics.revenue.change}
            changeType={globalMetrics.revenue.changeType}
            target={globalMetrics.revenue.target}
            attainment={globalMetrics.revenue.attainment}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Pipeline Value"
            value={globalMetrics.pipelineValue.label}
            change={globalMetrics.pipelineValue.change}
            changeType={globalMetrics.pipelineValue.changeType}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2" />
              </svg>
            }
          />
          <MetricCard
            title="Deals Closed"
            value={globalMetrics.dealsClosed.label}
            change={globalMetrics.dealsClosed.change}
            changeType={globalMetrics.dealsClosed.changeType}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
          <MetricCard
            title="Active Deals"
            value={globalMetrics.activeDeals.label}
            change={globalMetrics.activeDeals.change}
            changeType={globalMetrics.activeDeals.changeType}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l3.76 3.76a1 1 0 0 0 1.4-0l1.6-1.6a1 1 0 0 0 0-1.4z" />
              </svg>
            }
          />
          <MetricCard
            title="Win Rate"
            value={globalMetrics.winRate.label}
            change={globalMetrics.winRate.change}
            changeType={globalMetrics.winRate.changeType}
            unit=""
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
          <MetricCard
            title="Avg Deal Size"
            value={globalMetrics.avgDealSize.label}
            change={globalMetrics.avgDealSize.change}
            changeType={globalMetrics.avgDealSize.changeType}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="20" x2="12" y2="10" />
                <line x1="18" y1="20" x2="18" y2="4" />
                <line x1="6" y1="20" x2="6" y2="16" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Country Performance Grid */}
      <section className="countries-section">
        <div className="section-header">
          <h2>Countries Performance</h2>
          <Button variant="link" href="/countries">View All Countries</Button>
        </div>
        <div className="countries-grid">
          {countriesSummary.slice(0, 6).map((country) => (
            <Card
              key={country.country.code}
              className="country-card"
              hoverable
              onClick={() => window.location.href = `/countries/${country.country.code}`}
            >
              <div className="country-card-header">
                <span className="country-flag">{country.country.flag}</span>
                <div className="country-info">
                  <h3 className="country-name">{country.country.name}</h3>
                  <p className="country-director">{country.country.directorName}</p>
                </div>
                <span className={`country-status country-status-${country.status}`}>
                  {country.status === 'on_track' && '\u{2705}'}
                  {country.status === 'at_risk' && '\u26A0'}
                  {country.status === 'off_track' && '\u274C'}
                </span>
              </div>
              <div className="country-metrics">
                <div className="country-metric">
                  <span className="metric-label">Revenue</span>
                  <span className="metric-value">{formatCurrency(country.metrics.revenue, country.country.currency)}</span>
                </div>
                <div className="country-metric">
                  <span className="metric-label">Quota</span>
                  <span className="metric-value">{country.metrics.attainment}%</span>
                </div>
                <div className="country-metric">
                  <span className="metric-label">Deals</span>
                  <span className="metric-value">{country.metrics.dealsClosed}</span>
                </div>
              </div>
              <div className="country-progress">
                <div className="progress-bar">
                  <div
                    className={`progress-fill progress-fill-${country.status}`}
                    style={{ width: `${Math.min(country.metrics.attainment, 100)}%` }}
                  ></div>
                </div>
              </div>
            </Card>
          ))}
        </div>
      </section>

      {/* Bottom Row: Top Performers and Major Deals */}
      <section className="bottom-section">
        {/* Top Performers */}
        <Card className="top-performers-card">
          <CardHeader>
            <h3>Top Performers</h3>
            <Button variant="link" size="sm" href="/teams">View All</Button>
          </CardHeader>
          <CardBody>
            <div className="performers-list">
              {topPerformers.slice(0, 5).map((performer, index) => (
                <div key={performer.userId} className="performer-item">
                  <div className="performer-rank">
                    <span className={`rank-badge rank-${index + 1}`}>
                      {index === 0 && '\u{1F947}'}
                      {index === 1 && '\u{1F948}'}
                      {index === 2 && '\u{1F949}'}
                      {index > 2 && index + 1}
                    </span>
                  </div>
                  {performer.avatar && (
                    <img src={performer.avatar} alt={performer.name} className="performer-avatar" />
                  )}
                  <div className="performer-info">
                    <h4 className="performer-name">{performer.name}</h4>
                    <p className="performer-meta">
                      {performer.country.flag} {performer.department}
                    </p>
                  </div>
                  <div className="performer-stats">
                    <div className="performer-stat">
                      <span className="stat-label">Revenue</span>
                      <span className="stat-value">{formatCurrency(performer.revenue)}</span>
                    </div>
                    <div className="performer-stat">
                      <span className="stat-label">Attainment</span>
                      <span className="stat-value stat-highlight">{performer.attainment}%</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>

        {/* Major Deals */}
        <Card className="major-deals-card">
          <CardHeader>
            <h3>Major Deals</h3>
            <Button variant="link" size="sm" href="/pipeline">View Pipeline</Button>
          </CardHeader>
          <CardBody>
            <div className="deals-list">
              {majorDeals.slice(0, 5).map((deal) => (
                <div key={deal.id} className="deal-item">
                  <div className="deal-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                      <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l3.76 3.76a1 1 0 0 0 1.4-0l1.6-1.6a1 1 0 0 0 0-1.4z" />
                    </svg>
                  </div>
                  <div className="deal-info">
                    <h4 className="deal-name">{deal.name}</h4>
                    <p className="deal-company">{deal.company}</p>
                    <div className="deal-meta">
                      <span className="deal-country">{deal.country.flag}</span>
                      <span className="deal-stage">{deal.stage}</span>
                      <span className="deal-probability">{deal.probability}%</span>
                    </div>
                  </div>
                  <div className="deal-value">
                    <span className="value-amount">{formatCurrency(deal.value)}</span>
                    <span className="value-owner">{deal.owner.name}</span>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>

        {/* Forecast Summary */}
        <Card className="forecast-card">
          <CardHeader>
            <h3>Forecast Summary</h3>
            <Button variant="link" size="sm" href="/forecasting">Details</Button>
          </CardHeader>
          <CardBody>
            <div className="forecast-content">
              <div className="forecast-main">
                <span className="forecast-period">{forecastSummary.period}</span>
                <span className="forecast-amount">{formatCurrency(forecastSummary.forecast)}</span>
                <span className="forecast-label">Forecast</span>
              </div>
              <div className="forecast-scenarios">
                <div className="forecast-scenario forecast-best">
                  <span className="scenario-label">Best Case</span>
                  <span className="scenario-value">{formatCurrency(forecastSummary.bestCase)}</span>
                </div>
                <div className="forecast-scenario forecast-worst">
                  <span className="scenario-label">Worst Case</span>
                  <span className="scenario-value">{formatCurrency(forecastSummary.worstCase)}</span>
                </div>
              </div>
              <div className="forecast-accuracy">
                <span className="accuracy-label">Accuracy</span>
                <span className="accuracy-value">{forecastSummary.accuracy}%</span>
              </div>
            </div>
          </CardBody>
        </Card>
      </section>
    </div>
  );
}
