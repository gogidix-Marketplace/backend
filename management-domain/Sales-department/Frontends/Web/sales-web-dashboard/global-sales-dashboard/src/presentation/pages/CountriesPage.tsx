// Countries Page
// Country comparison and detail views for all countries

import { useEffect, useState, useMemo } from 'react';
import { useParams, useNavigate, NavLink, Outlet } from 'react-router-dom';
import { useDashboardStore } from '@infrastructure';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Table } from '../components/tables/Table';
import {
  CountryComparisonChart,
  MonthlyTrendChart,
  PerformanceBarChart,
} from '../components/charts';
import { FilterForm, CountryFilter } from '../components/forms';
import { formatCurrency, formatPercentage } from '@shared';
import { mockCountrySummaries, mockCountries, mockTopPerformers, mockMajorDeals } from '@shared/mock-data';
import './CountriesPage.css';

export function CountriesPage() {
  const navigate = useNavigate();

  return (
    <div className="countries-page">
      <Outlet />
    </div>
  );
}

// Comparison Overview
export function CountriesOverview() {
  const [selectedCountries, setSelectedCountries] = useState<string[]>([]);
  const [selectedMetric, setSelectedMetric] = useState<keyof CountrySummary['metrics']>('revenue');
  const [period, setPeriod] = useState('month');

  const filteredCountries = useMemo(() => {
    if (selectedCountries.length === 0) return mockCountrySummaries;
    return mockCountrySummaries.filter((cs) =>
      selectedCountries.includes(cs.country.code)
    );
  }, [selectedCountries]);

  const globalMetrics = useMemo(() => {
    const totalRevenue = filteredCountries.reduce((sum, cs) => sum + cs.metrics.revenue, 0);
    const totalQuota = filteredCountries.reduce((sum, cs) => sum + cs.metrics.quota, 0);
    const totalDeals = filteredCountries.reduce((sum, cs) => sum + cs.metrics.dealsClosed, 0);
    const avgWinRate =
      filteredCountries.reduce((sum, cs) => sum + cs.metrics.winRate, 0) / filteredCountries.length;

    return {
      revenue: totalRevenue,
      quota: totalQuota,
      attainment: totalQuota > 0 ? (totalRevenue / totalQuota) * 100 : 0,
      deals: totalDeals,
      winRate: avgWinRate,
    };
  }, [filteredCountries]);

  return (
    <div className="countries-overview">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Countries Overview</h1>
          <p className="page-subtitle">Compare performance across all countries</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary" size="sm">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
              <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z" />
              <path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z" />
            </svg>
            Export
          </Button>
        </div>
      </div>

      {/* Filters */}
      <Card>
        <CardBody>
          <div className="countries-filters">
            <CountryFilter
              countries={mockCountries}
              selected={selectedCountries}
              onChange={setSelectedCountries}
            />
            <div className="metric-selector">
              <label>Compare by:</label>
              <select
                value={selectedMetric}
                onChange={(e) => setSelectedMetric(e.target.value as any)}
                className="metric-select"
              >
                <option value="revenue">Revenue</option>
                <option value="quota">Quota</option>
                <option value="attainment">Attainment</option>
                <option value="dealsClosed">Deals Closed</option>
                <option value="pipelineValue">Pipeline Value</option>
                <option value="winRate">Win Rate</option>
              </select>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Global Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Total Revenue"
            value={formatCurrency(globalMetrics.revenue)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Global Attainment"
            value={`${formatPercentage(globalMetrics.attainment)}`}
            attainment={globalMetrics.attainment}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
          <MetricCard
            title="Total Deals"
            value={globalMetrics.deals.toLocaleString()}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l3.76 3.76a1 1 0 0 0 1.4-0l1.6-1.6a1 1 0 0 0 0-1.4z" />
              </svg>
            }
          />
          <MetricCard
            title="Avg Win Rate"
            value={`${formatPercentage(globalMetrics.winRate)}`}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Country Comparison Chart */}
      <section className="comparison-section">
        <Card>
          <CardHeader>
            <h3>Country Comparison - {selectedMetric}</h3>
          </CardHeader>
          <CardBody>
            <CountryComparisonChart
              countries={filteredCountries}
              metric={selectedMetric}
              height={280}
            />
          </CardBody>
        </Card>
      </section>

      {/* Countries Table */}
      <section className="table-section">
        <Card>
          <CardHeader>
            <h3>All Countries</h3>
            <Button variant="link" size="sm">View Full Report</Button>
          </CardHeader>
          <CardBody>
            <Table
              columns={[
                { key: 'country', label: 'Country', render: (cs: any) => (
                <div className="country-cell">
                  <span className="country-flag">{cs.country.flag}</span>
                  <span>{cs.country.name}</span>
                </div>
              )},
              { key: 'director', label: 'Director', render: (cs: any) => cs.country.directorName },
              {
                key: 'revenue',
                label: 'Revenue',
                render: (cs: any) => formatCurrency(cs.metrics.revenue, cs.country.currency),
              },
              {
                key: 'attainment',
                label: 'Quota Attainment',
                render: (cs: any) => `${cs.metrics.attainment}%`,
              },
              {
                key: 'deals',
                label: 'Deals',
                render: (cs: any) => cs.metrics.dealsClosed,
              },
              {
                key: 'pipeline',
                label: 'Pipeline',
                render: (cs: any) => formatCurrency(cs.metrics.pipelineValue),
              },
              {
                key: 'status',
                label: 'Status',
                render: (cs: any) => (
                  <span className={`status-badge status-${cs.status}`}>
                    {cs.status === 'on_track' && 'On Track'}
                    {cs.status === 'at_risk' && 'At Risk'}
                    {cs.status === 'off_track' && 'Off Track'}
                  </span>
                ),
              },
              {
                key: 'actions',
                label: 'Actions',
                render: (cs: any) => (
                  <Button variant="link" size="sm" onClick={() => navigate(`/countries/${cs.country.code}`)}>
                    Details
                  </Button>
                ),
              },
              ]}
              data={filteredCountries}
            />
          </CardBody>
        </Card>
      </section>
    </div>
  );
}

// Country Detail Page
export function CountryDetail() {
  const { countryCode } = useParams<{ countryCode: string }>();
  const navigate = useNavigate();
  const [selectedMetric, setSelectedMetric] = useState<string>('revenue');

  const countryData = useMemo(
    () => mockCountrySummaries.find((cs) => cs.country.code === countryCode),
    [countryCode]
  );

  const topPerformers = useMemo(
    () => mockTopPerformers.filter((p) => p.country.code === countryCode),
    [countryCode]
  );

  const majorDeals = useMemo(
    () => mockMajorDeals.filter((d) => d.country.code === countryCode),
    [countryCode]
  );

  if (!countryData) {
    return (
      <div className="error-state">
        <h3>Country not found</h3>
        <Button onClick={() => navigate('/countries')}>Back to Countries</Button>
      </div>
    );
  }

  const monthlyData = [
    { month: 'Sep', revenue: 950000, target: 1000000 },
    { month: 'Oct', revenue: 1050000, target: 1000000 },
    { month: 'Nov', revenue: 1100000, target: 1050000 },
    { month: 'Dec', revenue: 1200000, target: 1100000 },
    { month: 'Jan', revenue: 1150000, target: 1150000 },
    { month: 'Feb', revenue: 1200000, target: 1200000, forecast: 1250000 },
  ];

  return (
    <div className="country-detail">
      {/* Breadcrumb */}
      <div className="breadcrumb">
        <NavLink to="/countries">Countries</NavLink>
        <span>/</span>
        <span>{countryData.country.name}</span>
      </div>

      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>
            {countryData.country.flag} {countryData.country.name}
          </h1>
          <p className="page-subtitle">Country Director: {countryData.country.directorName}</p>
        </div>
        <div className="page-actions">
          <Button variant="ghost" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8" />
              <polyline points="16 6 12 2 8 6" />
              <line x1="12" y1="2" x2="12" y2="15" />
            </svg>
            Export
          </Button>
        </div>
      </div>

      {/* Key Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Revenue"
            value={formatCurrency(countryData.metrics.revenue, countryData.country.currency)}
            change={countryData.metrics.growth}
            changeType={countryData.metrics.growth >= 0 ? 'increase' : 'decrease'}
            target={countryData.metrics.quota}
            attainment={countryData.metrics.attainment}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Pipeline"
            value={formatCurrency(countryData.metrics.pipelineValue, countryData.country.currency)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2" />
              </svg>
            }
          />
          <MetricCard
            title="Deals Closed"
            value={countryData.metrics.dealsClosed}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
          <MetricCard
            title="Win Rate"
            value={`${countryData.metrics.winRate}%`}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Revenue Trend */}
      <section className="trend-section">
        <Card>
          <CardHeader>
            <h3>Revenue Trend</h3>
          </CardHeader>
          <CardBody>
            <MonthlyTrendChart data={monthlyData} height={180} showTarget showForecast />
          </CardBody>
        </Card>
      </section>

      {/* Bottom Grid */}
      <section className="detail-grid">
        {/* Top Performers */}
        <Card>
          <CardHeader>
            <h3>Top Performers</h3>
          </CardHeader>
          <CardBody>
            {topPerformers.length > 0 ? (
              <div className="performers-list">
                {topPerformers.slice(0, 5).map((performer, index) => (
                  <div key={performer.userId} className="performer-item">
                    <div className="performer-rank">#{index + 1}</div>
                    {performer.avatar && (
                      <img src={performer.avatar} alt={performer.name} className="performer-avatar" />
                    )}
                    <div className="performer-info">
                      <span className="performer-name">{performer.name}</span>
                      <span className="performer-dept">{performer.department}</span>
                    </div>
                    <div className="performer-revenue">
                      {formatCurrency(performer.revenue)}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-muted">No performers available for this country.</p>
            )}
          </CardBody>
        </Card>

        {/* Major Deals */}
        <Card>
          <CardHeader>
            <h3>Major Deals</h3>
          </CardHeader>
          <CardBody>
            {majorDeals.length > 0 ? (
              <div className="deals-list">
                {majorDeals.map((deal) => (
                  <div key={deal.id} className="deal-item">
                    <div className="deal-info">
                      <span className="deal-name">{deal.name}</span>
                      <span className="deal-company">{deal.company}</span>
                    </div>
                    <div className="deal-stage">{deal.stage}</div>
                    <div className="deal-value">{formatCurrency(deal.value)}</div>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-muted">No major deals for this country.</p>
            )}
          </CardBody>
        </Card>
      </section>
    </div>
  );
}
