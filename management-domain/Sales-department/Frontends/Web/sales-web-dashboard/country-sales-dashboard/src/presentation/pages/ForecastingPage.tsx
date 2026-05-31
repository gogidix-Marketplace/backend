// ForecastingPage Component
// Country revenue forecasts and projections

import React, { useState } from 'react';
import { RevenueChart, PerformanceChart } from '../components/charts';
import { Card, Button, Tabs, MetricCard } from '../components/common';
import { formatCurrency, formatPercentage } from '@shared';

interface ForecastData {
  period: string;
  forecast: number;
  actual?: number;
  bestCase: number;
  worstCase: number;
  weightedPipeline: number;
}

interface TeamForecast {
  teamId: string;
  teamName: string;
  forecast: number;
  quota: number;
  attainment: number;
  risk: 'low' | 'medium' | 'high';
}

const mockForecastData: ForecastData[] = [
  { period: 'Jan', forecast: 24500000, actual: 24800000, bestCase: 28000000, worstCase: 22000000, weightedPipeline: 8500000 },
  { period: 'Feb', forecast: 25000000, actual: 28500000, bestCase: 32000000, worstCase: 23000000, weightedPipeline: 8750000 },
  { period: 'Mar', forecast: 26000000, bestCase: 30000000, worstCase: 23000000, weightedPipeline: 12000000 },
  { period: 'Apr', forecast: 26500000, bestCase: 31000000, worstCase: 23500000, weightedPipeline: 9500000 },
  { period: 'May', forecast: 27000000, bestCase: 32000000, worstCase: 24000000, weightedPipeline: 10500000 },
  { period: 'Jun', forecast: 27500000, bestCase: 33000000, worstCase: 24500000, weightedPipeline: 11000000 },
];

const mockTeamForecasts: TeamForecast[] = [
  { teamId: 'team-001', teamName: 'Lagos Mainland Team', forecast: 5500000, quota: 5000000, attainment: 110, risk: 'low' },
  { teamId: 'team-002', teamName: 'Lagos Island Team', forecast: 6300000, quota: 6000000, attainment: 105, risk: 'low' },
  { teamId: 'team-003', teamName: 'Abuja Metro Team', forecast: 3800000, quota: 4000000, attainment: 95, risk: 'medium' },
  { teamId: 'team-004', teamName: 'Port Harcourt Team', forecast: 3200000, quota: 3500000, attainment: 91, risk: 'medium' },
  { teamId: 'team-005', teamName: 'Kano Team', forecast: 2100000, quota: 2500000, attainment: 84, risk: 'high' },
];

export const ForecastingPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('overview');
  const [selectedPeriod, setSelectedPeriod] = useState<'quarter' | 'year'>('quarter');

  const currentQuarterForecast = mockForecastData.slice(0, 3);
  const totalForecast = currentQuarterForecast.reduce((sum, f) => sum + f.forecast, 0);
  const totalBestCase = currentQuarterForecast.reduce((sum, f) => sum + f.bestCase, 0);
  const totalWorstCase = currentQuarterForecast.reduce((sum, f) => sum + f.worstCase, 0);
  const totalWeighted = currentQuarterForecast.reduce((sum, f) => sum + f.weightedPipeline, 0);

  const forecastChartData = mockForecastData.map(f => ({
    period: f.period,
    revenue: f.actual || f.forecast,
    target: f.forecast,
  }));

  const scenarioData = [
    { label: 'Worst Case', value: totalWorstCase, target: totalForecast, color: '#EF4444' },
    { label: 'Forecast', value: totalForecast, target: totalForecast, color: '#3B82F6' },
    { label: 'Best Case', value: totalBestCase, target: totalForecast, color: '#10B981' },
  ];

  const tabs = [
    {
      id: 'overview',
      label: 'Overview',
      content: (
        <div className="forecast-overview">
          <div className="forecast-summary">
            <Card className="forecast-card-main">
              <h4>Quarter Forecast</h4>
              <div className="forecast-value-main">{formatCurrency(totalForecast, 'NGN')}</div>
              <div className="forecast-scenarios">
                <span className="scenario-best">Best: {formatCurrency(totalBestCase, 'NGN')}</span>
                <span className="scenario-worst">Worst: {formatCurrency(totalWorstCase, 'NGN')}</span>
              </div>
            </Card>

            <Card>
              <h4>Weighted Pipeline</h4>
              <div className="forecast-value">{formatCurrency(totalWeighted, 'USD')}</div>
              <div className="forecast-note">Contributing to forecast</div>
            </Card>

            <Card>
              <h4>Forecast Accuracy</h4>
              <div className="forecast-value">92%</div>
              <div className="forecast-note">Based on last 6 months</div>
            </Card>

            <Card>
              <h4>Risk Level</h4>
              <div className="forecast-value forecast-risk-low">Low</div>
              <div className="forecast-note">On track to meet quota</div>
            </Card>
          </div>

          <Card>
            <h4>Forecast vs Actual</h4>
            <RevenueChart data={forecastChartData} height={250} showTarget />
          </Card>

          <Card>
            <h4>Scenario Analysis</h4>
            <PerformanceChart data={scenarioData} type="bar" height={250} showTarget={false} />
          </Card>
        </div>
      ),
    },
    {
      id: 'by-team',
      label: 'By Team',
      content: (
        <div className="team-forecasts">
          <div className="team-forecast-list">
            {mockTeamForecasts.map(team => (
              <Card key={team.teamId} className={`team-forecast-card risk-${team.risk}`}>
                <div className="team-forecast-header">
                  <h4>{team.teamName}</h4>
                  <span className={`risk-badge risk-${team.risk}`}>
                    {team.risk.toUpperCase()} RISK
                  </span>
                </div>

                <div className="team-forecast-body">
                  <div className="forecast-row">
                    <span className="label">Forecast</span>
                    <span className="value">{formatCurrency(team.forecast, 'NGN')}</span>
                  </div>
                  <div className="forecast-row">
                    <span className="label">Quota</span>
                    <span className="value">{formatCurrency(team.quota, 'NGN')}</span>
                  </div>
                  <div className="forecast-row">
                    <span className="label">Attainment</span>
                    <span className={`value ${team.attainment >= 100 ? 'success' : team.attainment >= 90 ? 'warning' : 'danger'}`}>
                      {formatPercentage(team.attainment)}
                    </span>
                  </div>
                </div>

                <div className="team-forecast-progress">
                  <div className="progress-bar">
                    <div
                      className={`progress-fill ${team.attainment >= 100 ? 'success' : team.attainment >= 90 ? 'warning' : 'danger'}`}
                      style={{ width: `${Math.min(team.attainment, 100)}%` }}
                    />
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>
      ),
    },
    {
      id: 'trends',
      label: 'Trends',
      content: (
        <div className="forecast-trends">
          <Card>
            <h4>Forecast Trends (6 Months)</h4>
            <RevenueChart data={forecastChartData} height={300} showTarget />
          </Card>

          <Card>
            <h4>Forecast Accuracy Over Time</h4>
            <div className="accuracy-list">
              {['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'].map((month, i) => {
                const accuracy = 85 + Math.random() * 12;
                return (
                  <div key={month} className="accuracy-item">
                    <span className="accuracy-month">{month}</span>
                    <div className="accuracy-bar">
                      <div
                        className="accuracy-fill"
                        style={{ width: `${accuracy}%` }}
                      />
                    </div>
                    <span className="accuracy-value">{accuracy.toFixed(1)}%</span>
                  </div>
                );
              })}
            </div>
          </Card>
        </div>
      ),
    },
  ];

  return (
    <div className="page forecasting-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Forecasting</h1>
          <p className="page-subtitle">Revenue projections and target tracking</p>
        </div>
        <div className="page-actions">
          <select
            value={selectedPeriod}
            onChange={(e) => setSelectedPeriod(e.target.value as any)}
            className="period-select"
          >
            <option value="quarter">This Quarter</option>
            <option value="year">This Year</option>
          </select>
          <Button variant="secondary">Export Forecast</Button>
          <Button variant="primary">Adjust Forecast</Button>
        </div>
      </div>

      {/* Summary Metrics */}
      <div className="forecast-metrics">
        <MetricCard
          title="Quarter Forecast"
          value={totalForecast}
          currency="NGN"
          prefix=""
        />
        <MetricCard
          title="Best Case"
          value={totalBestCase}
          currency="NGN"
          prefix=""
        />
        <MetricCard
          title="Worst Case"
          value={totalWorstCase}
          currency="NGN"
          prefix=""
        />
        <MetricCard
          title="Pipeline Coverage"
          value={Math.round((totalWeighted / totalForecast) * 100)}
          suffix="%"
          prefix=""
        />
      </div>

      {/* Tabs */}
      <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} />
    </div>
  );
};

export default ForecastingPage;
