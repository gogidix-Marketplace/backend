// Performance Page
// Global performance tracking with KPIs, leaderboards, and analytics

import { useState, useMemo } from 'react';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Table } from '../components/tables/Table';
import { PerformanceTrendChart, AttainmentGauge } from '../components/charts';
import { formatCurrency, formatPercentage } from '@shared';
import { mockCountries, mockTopPerformers, mockCountrySummaries } from '@shared/mock-data';
import './PerformancePage.css';

interface KPI {
  id: string;
  name: string;
  description: string;
  category: 'revenue' | 'pipeline' | 'activity' | 'efficiency';
  globalTarget: number;
  globalActual: number;
  globalAttainment: number;
  trend: 'up' | 'down' | 'neutral';
  changePercentage: number;
}

const mockKPIs: KPI[] = [
  {
    id: 'kpi-001',
    name: 'Revenue Quota Attainment',
    description: 'Percentage of revenue quota achieved',
    category: 'revenue',
    globalTarget: 100,
    globalActual: 102,
    globalAttainment: 102,
    trend: 'up',
    changePercentage: 8,
  },
  {
    id: 'kpi-002',
    name: 'Pipeline Coverage',
    description: 'Pipeline value divided by revenue target',
    category: 'pipeline',
    globalTarget: 3,
    globalActual: 3.2,
    globalAttainment: 107,
    trend: 'up',
    changePercentage: 5,
  },
  {
    id: 'kpi-003',
    name: 'Deal Win Rate',
    description: 'Percentage of deals won',
    category: 'efficiency',
    globalTarget: 35,
    globalActual: 34,
    globalAttainment: 97,
    trend: 'neutral',
    changePercentage: -2,
  },
  {
    id: 'kpi-004',
    name: 'Sales Cycle Length',
    description: 'Average days to close a deal',
    category: 'efficiency',
    globalTarget: 90,
    globalActual: 96,
    globalAttainment: 94,
    trend: 'down',
    changePercentage: -7,
  },
  {
    id: 'kpi-005',
    name: 'Activity Score',
    description: 'Calls, emails, and meetings per rep',
    category: 'activity',
    globalTarget: 50,
    globalActual: 47,
    globalAttainment: 94,
    trend: 'up',
    changePercentage: 3,
  },
];

interface PerformanceRanking {
  rank: number;
  country: typeof mockCountries[0];
  revenue: number;
  attainment: number;
  dealsClosed: number;
  winRate: number;
  trend: 'up' | 'down' | 'neutral';
  rankChange: number;
}

const performanceRankings: PerformanceRanking[] = [
  {
    rank: 1,
    country: mockCountries[3],
    revenue: 2100000,
    attainment: 105,
    dealsClosed: 312,
    winRate: 38,
    trend: 'up',
    rankChange: 0,
  },
  {
    rank: 2,
    country: mockCountries[1],
    revenue: 950000,
    attainment: 106,
    dealsClosed: 189,
    winRate: 35,
    trend: 'up',
    rankChange: 1,
  },
  {
    rank: 3,
    country: mockCountries[0],
    revenue: 1200000,
    attainment: 92,
    dealsClosed: 245,
    winRate: 32,
    trend: 'up',
    rankChange: -1,
  },
  {
    rank: 4,
    country: mockCountries[2],
    revenue: 780000,
    attainment: 92,
    dealsClosed: 156,
    winRate: 28,
    trend: 'up',
    rankChange: 0,
  },
  {
    rank: 5,
    country: mockCountries[4],
    revenue: 650000,
    attainment: 93,
    dealsClosed: 98,
    winRate: 30,
    trend: 'neutral',
    rankChange: 0,
  },
  {
    rank: 6,
    country: mockCountries[5],
    revenue: 420000,
    attainment: 93,
    dealsClosed: 78,
    winRate: 29,
    trend: 'up',
    rankChange: 0,
  },
];

interface MonthlyPerformance {
  month: string;
  revenue: number;
  target: number;
  attainment: number;
  deals: number;
  winRate: number;
}

const monthlyPerformance: MonthlyPerformance[] = [
  { month: 'Sep', revenue: 7200000, target: 7500000, attainment: 96, deals: 1080, winRate: 32 },
  { month: 'Oct', revenue: 7800000, target: 7800000, attainment: 100, deals: 1145, winRate: 33 },
  { month: 'Nov', revenue: 8200000, target: 8000000, attainment: 103, deals: 1190, winRate: 34 },
  { month: 'Dec', revenue: 9100000, target: 8500000, attainment: 107, deals: 1280, winRate: 36 },
  { month: 'Jan', revenue: 8500000, target: 8500000, attainment: 100, deals: 1245, winRate: 34 },
  { month: 'Feb (Forecast)', revenue: 8800000, target: 8500000, attainment: 104, deals: 0, winRate: 0 },
];

export function PerformancePage() {
  const [selectedPeriod, setSelectedPeriod] = useState<string>('month');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');

  const filteredKPIs = useMemo(() => {
    if (selectedCategory === 'all') return mockKPIs;
    return mockKPIs.filter(kpi => kpi.category === selectedCategory);
  }, [selectedCategory]);

  const categoryColors = {
    revenue: '#2563EB',
    pipeline: '#7C3AED',
    activity: '#10B981',
    efficiency: '#F59E0B',
  };

  const globalAttainment = useMemo(() => {
    const totalRevenue = mockCountrySummaries.reduce((sum, c) => sum + c.metrics.revenue, 0);
    const totalQuota = mockCountrySummaries.reduce((sum, c) => sum + c.metrics.quota, 0);
    return totalQuota > 0 ? (totalRevenue / totalQuota) * 100 : 0;
  }, []);

  const topMover = performanceRankings.find(r => r.rankChange > 0);
  const topFaller = performanceRankings.find(r => r.rankChange < 0);

  return (
    <div className="performance-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Global Performance</h1>
          <p className="page-subtitle">Track KPIs, rankings, and performance trends</p>
        </div>
        <div className="page-actions">
          <select
            value={selectedPeriod}
            onChange={(e) => setSelectedPeriod(e.target.value)}
            className="period-selector"
          >
            <option value="month">This Month</option>
            <option value="quarter">This Quarter</option>
            <option value="year">This Year</option>
          </select>
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

      {/* Global Attainment Overview */}
      <section className="attainment-overview">
        <div className="attainment-gauge-container">
          <Card>
            <CardHeader>
              <h3>Global Quota Attainment</h3>
            </CardHeader>
            <CardBody className="gauge-body">
              <AttainmentGauge value={globalAttainment} size={200} showLabel />
            </CardBody>
          </Card>
        </div>

        <div className="attainment-cards">
          <MetricCard
            title="Global Revenue"
            value={formatCurrency(8500000)}
            change={15}
            changeType="increase"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Total Deals Won"
            value="1,245"
            change={8}
            changeType="increase"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
          <MetricCard
            title="Avg Win Rate"
            value="34%"
            change={-2}
            changeType="decrease"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
        </div>
      </section>

      {/* KPIs */}
      <section className="kpi-section">
        <Card>
          <CardHeader>
            <div className="kpi-header">
              <h3>Key Performance Indicators</h3>
              <div className="kpi-filters">
                <button
                  className={`kpi-filter ${selectedCategory === 'all' ? 'active' : ''}`}
                  onClick={() => setSelectedCategory('all')}
                >
                  All
                </button>
                <button
                  className={`kpi-filter ${selectedCategory === 'revenue' ? 'active' : ''}`}
                  onClick={() => setSelectedCategory('revenue')}
                >
                  Revenue
                </button>
                <button
                  className={`kpi-filter ${selectedCategory === 'pipeline' ? 'active' : ''}`}
                  onClick={() => setSelectedCategory('pipeline')}
                >
                  Pipeline
                </button>
                <button
                  className={`kpi-filter ${selectedCategory === 'efficiency' ? 'active' : ''}`}
                  onClick={() => setSelectedCategory('efficiency')}
                >
                  Efficiency
                </button>
                <button
                  className={`kpi-filter ${selectedCategory === 'activity' ? 'active' : ''}`}
                  onClick={() => setSelectedCategory('activity')}
                >
                  Activity
                </button>
              </div>
            </div>
          </CardHeader>
          <CardBody>
            <div className="kpi-grid">
              {filteredKPIs.map((kpi) => (
                <div key={kpi.id} className="kpi-card">
                  <div className="kpi-header-row">
                    <div className="kpi-icon" style={{ backgroundColor: categoryColors[kpi.category] + '20' }}>
                      <div className="kpi-icon-dot" style={{ backgroundColor: categoryColors[kpi.category] }}></div>
                    </div>
                    <span className={`kpi-trend kpi-trend-${kpi.trend}`}>
                      {kpi.trend === 'up' && '\u2197'}
                      {kpi.trend === 'down' && '\u2198'}
                      {kpi.trend === 'neutral' && '\u2192'}
                      {kpi.changePercentage > 0 ? '+' : ''}{kpi.changePercentage}%
                    </span>
                  </div>
                  <h4 className="kpi-name">{kpi.name}</h4>
                  <p className="kpi-description">{kpi.description}</p>
                  <div className="kpi-values">
                    <div className="kpi-actual">
                      <span className="kpi-value">{kpi.globalActual}{kpi.category === 'efficiency' && kpi.name.includes('Days') ? ' days' : '%'}</span>
                      <span className="kpi-label">Actual</span>
                    </div>
                    <div className="kpi-target">
                      <span className="kpi-value">{kpi.globalTarget}{kpi.category === 'efficiency' && kpi.name.includes('Days') ? ' days' : '%'}</span>
                      <span className="kpi-label">Target</span>
                    </div>
                    <div className="kpi-attainment">
                      <span className={`kpi-value ${kpi.globalAttainment >= 100 ? 'success' : ''}`}>
                        {kpi.globalAttainment}%
                      </span>
                      <span className="kpi-label">Attainment</span>
                    </div>
                  </div>
                  <div className="kpi-progress">
                    <div
                      className={`kpi-progress-bar ${kpi.globalAttainment >= 100 ? 'success' : ''}`}
                      style={{ width: `${Math.min(kpi.globalAttainment, 100)}%` }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Country Rankings */}
      <section className="rankings-section">
        <Card>
          <CardHeader>
            <h3>Country Rankings</h3>
            <Button variant="link" size="sm">View Full Rankings</Button>
          </CardHeader>
          <CardBody>
            <div className="rankings-table">
              <table>
                <thead>
                  <tr>
                    <th>Rank</th>
                    <th>Country</th>
                    <th>Revenue</th>
                    <th>Attainment</th>
                    <th>Deals Won</th>
                    <th>Win Rate</th>
                    <th>Trend</th>
                  </tr>
                </thead>
                <tbody>
                  {performanceRankings.map((ranking) => (
                    <tr key={ranking.country.code}>
                      <td>
                        <span className={`rank-badge rank-${ranking.rank}`}>
                          #{ranking.rank}
                        </span>
                      </td>
                      <td>
                        <div className="country-cell">
                          <span className="country-flag">{ranking.country.flag}</span>
                          <span>{ranking.country.name}</span>
                        </div>
                      </td>
                      <td>{formatCurrency(ranking.revenue)}</td>
                      <td>
                        <span className={`attainment-badge ${ranking.attainment >= 100 ? 'success' : ''}`}>
                          {ranking.attainment}%
                        </span>
                      </td>
                      <td>{ranking.dealsClosed}</td>
                      <td>{ranking.winRate}%</td>
                      <td>
                        <span className={`trend-badge trend-${ranking.trend}`}>
                          {ranking.trend === 'up' && '\u2191'}
                          {ranking.trend === 'down' && '\u2193'}
                          {ranking.trend === 'neutral' && '\u2192'}
                          {ranking.rankChange !== 0 && (
                            <span className="rank-change">
                              ({ranking.rankChange > 0 ? '+' : ''}{ranking.rankChange})
                            </span>
                          )}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Performance Trend */}
      <section className="trend-section">
        <Card>
          <CardHeader>
            <h3>Performance Trend</h3>
          </CardHeader>
          <CardBody>
            <PerformanceTrendChart data={monthlyPerformance} height={200} />
          </CardBody>
        </Card>
      </section>

      {/* Top Performers */}
      <section className="performers-section">
        <div className="performers-grid">
          <Card>
            <CardHeader>
              <h3>Top Performers</h3>
            </CardHeader>
            <CardBody>
              <div className="performers-list">
                {mockTopPerformers.slice(0, 5).map((performer, index) => (
                  <div key={performer.userId} className="performer-item">
                    <div className="performer-rank">
                      <span className={`rank-badge rank-${index + 1}`}>
                        {index + 1}
                      </span>
                    </div>
                    {performer.avatar && (
                      <img src={performer.avatar} alt={performer.name} className="performer-avatar" />
                    )}
                    <div className="performer-info">
                      <span className="performer-name">{performer.name}</span>
                      <span className="performer-meta">
                        {performer.country.flag} {performer.department}
                      </span>
                    </div>
                    <div className="performer-attainment">
                      <span className="attainment-value">{performer.attainment}%</span>
                      <span className="attainment-label">Attainment</span>
                    </div>
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>

          <Card>
            <CardHeader>
              <h3>Movement Highlights</h3>
            </CardHeader>
            <CardBody>
              <div className="movement-list">
                {topMover && (
                  <div className="movement-item movement-up">
                    <div className="movement-icon">\u2191</div>
                    <div className="movement-info">
                      <span className="movement-title">Top Mover</span>
                      <span className="movement-country">
                        {topMover.country.flag} {topMover.country.name}
                      </span>
                      <span className="movement-detail">Moved up {topMover.rankChange} position</span>
                    </div>
                  </div>
                )}
                {topFaller && (
                  <div className="movement-item movement-down">
                    <div className="movement-icon">\u2193</div>
                    <div className="movement-info">
                      <span className="movement-title">Decline</span>
                      <span className="movement-country">
                        {topFaller.country.flag} {topFaller.country.name}
                      </span>
                      <span className="movement-detail">Moved down {Math.abs(topFaller.rankChange)} position</span>
                    </div>
                  </div>
                )}
                <div className="movement-item movement-neutral">
                  <div className="movement-icon">\u2192</div>
                  <div className="movement-info">
                    <span className="movement-title">Stable</span>
                    <span className="movement-detail">
                      {performanceRankings.filter(r => r.rankChange === 0).length} countries maintained position
                    </span>
                  </div>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>
      </section>
    </div>
  );
}
