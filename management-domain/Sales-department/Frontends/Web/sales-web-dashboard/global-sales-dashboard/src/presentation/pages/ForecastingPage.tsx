// Forecasting Page
// Global sales forecasting with scenarios and accuracy tracking

import { useState, useMemo } from 'react';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Table } from '../components/tables/Table';
import {
  ForecastScenarioChart,
  ForecastTrendChart,
  CountryForecastChart,
  ForecastAccuracyChart,
} from '../components/charts';
import { formatCurrency } from '@shared';
import { mockCountries, mockForecast } from '@shared/mock-data';
import './ForecastingPage.css';

type ScenarioType = 'worst' | 'likely' | 'best';
type PeriodType = 'month' | 'quarter' | 'year';

export function ForecastingPage() {
  const [selectedScenario, setSelectedScenario] = useState<ScenarioType>('likely');
  const [selectedPeriod, setSelectedPeriod] = useState<PeriodType>('quarter');
  const [selectedCountry, setSelectedCountry] = useState<string>('all');

  const currentForecast = useMemo(() => mockForecast, []);

  const getForecastValue = (scenario: ScenarioType) => {
    switch (scenario) {
      case 'worst':
        return currentForecast.global.worstCase;
      case 'best':
        return currentForecast.global.bestCase;
      default:
        return currentForecast.global.forecast;
    }
  };

  const getCountryForecastValue = (countryCode: string, scenario: ScenarioType) => {
    const countryData = currentForecast.byCountry.find(c => c.country.code === countryCode);
    if (!countryData) return 0;

    switch (scenario) {
      case 'worst':
        return countryData.worstCase;
      case 'best':
        return countryData.bestCase;
      default:
        return countryData.forecast;
    }
  };

  const filteredCountryForecasts = useMemo(() => {
    if (selectedCountry === 'all') return currentForecast.byCountry;
    return currentForecast.byCountry.filter(c => c.country.code === selectedCountry);
  }, [selectedCountry, currentForecast]);

  const totalForecast = useMemo(() => {
    if (selectedCountry === 'all') {
      return getForecastValue(selectedScenario);
    }
    return getCountryForecastValue(selectedCountry, selectedScenario);
  }, [selectedCountry, selectedScenario]);

  const accuracyMetrics = useMemo(() => currentForecast.accuracyMetrics, [currentForecast]);

  const monthlyTrends = useMemo(() => currentForecast.trends, [currentForecast]);

  return (
    <div className="forecasting-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Global Forecasting</h1>
          <p className="page-subtitle">Sales forecasts and predictions for {currentForecast.period.label}</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <polyline points="7 10 12 15 17 10" />
              <line x1="12" y1="15" x2="12" y2="3" />
            </svg>
            Export
          </Button>
          <Button variant="primary" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
            </svg>
            Edit Forecast
          </Button>
        </div>
      </div>

      {/* Scenario Selector */}
      <section className="scenario-section">
        <Card>
          <CardHeader>
            <h3>Forecast Scenario</h3>
          </CardHeader>
          <CardBody>
            <div className="scenario-selector">
              <button
                className={`scenario-btn ${selectedScenario === 'worst' ? 'active' : ''}`}
                onClick={() => setSelectedScenario('worst')}
              >
                <span className="scenario-icon">\u26A0</span>
                <div className="scenario-info">
                  <span className="scenario-label">Worst Case</span>
                  <span className="scenario-value">{formatCurrency(currentForecast.global.worstCase)}</span>
                </div>
              </button>
              <button
                className={`scenario-btn ${selectedScenario === 'likely' ? 'active' : ''}`}
                onClick={() => setSelectedScenario('likely')}
              >
                <span className="scenario-icon">{'\u{1F4CA}'}</span>
                <div className="scenario-info">
                  <span className="scenario-label">Likely</span>
                  <span className="scenario-value">{formatCurrency(currentForecast.global.forecast)}</span>
                </div>
              </button>
              <button
                className={`scenario-btn ${selectedScenario === 'best' ? 'active' : ''}`}
                onClick={() => setSelectedScenario('best')}
              >
                <span className="scenario-icon">{'\u{1F4C8}'}</span>
                <div className="scenario-info">
                  <span className="scenario-label">Best Case</span>
                  <span className="scenario-value">{formatCurrency(currentForecast.global.bestCase)}</span>
                </div>
              </button>
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Key Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title={`${selectedScenario.charAt(0).toUpperCase() + selectedScenario.slice(1)} Forecast`}
            value={formatCurrency(totalForecast)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 12h-4l-3 9L9 3l-3 9H2" />
              </svg>
            }
          />
          <MetricCard
            title="Confidence Level"
            value={`${currentForecast.global.confidence}%`}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
              </svg>
            }
          />
          <MetricCard
            title="Weighted Pipeline"
            value={formatCurrency(currentForecast.byStage.reduce((sum, s) => sum + s.weightedValue, 0))}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="12" cy="12" r="10" />
                <path d="M12 6v6l4 2" />
              </svg>
            }
          />
          <MetricCard
            title="Forecast Accuracy"
            value={`${accuracyMetrics[0]?.accuracy.toFixed(1) || 94}%`}
            change={2}
            changeType="increase"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Scenario Comparison Chart */}
      <section className="scenario-comparison-section">
        <Card>
          <CardHeader>
            <h3>Scenario Comparison</h3>
          </CardHeader>
          <CardBody>
            <ForecastScenarioChart forecast={currentForecast} height={200} />
          </CardBody>
        </Card>
      </section>

      {/* Trend Chart */}
      <section className="trend-section">
        <Card>
          <CardHeader>
            <h3>Forecast Trend</h3>
          </CardHeader>
          <CardBody>
            <ForecastTrendChart trends={monthlyTrends} height={180} />
          </CardBody>
        </Card>
      </section>

      {/* Country Forecasts */}
      <section className="countries-section">
        <Card>
          <CardHeader>
            <div className="countries-header">
              <h3>Country Forecasts</h3>
              <select
                value={selectedCountry}
                onChange={(e) => setSelectedCountry(e.target.value)}
                className="country-filter"
              >
                <option value="all">All Countries</option>
                {mockCountries.map(country => (
                  <option key={country.code} value={country.code}>
                    {country.flag} {country.name}
                  </option>
                ))}
              </select>
            </div>
          </CardHeader>
          <CardBody>
            <div className="country-forecasts">
              {filteredCountryForecasts.map((countryForecast) => (
                <div key={countryForecast.country.code} className="country-forecast-card">
                  <div className="country-header">
                    <span className="country-flag">{countryForecast.country.flag}</span>
                    <div className="country-info">
                      <h4 className="country-name">{countryForecast.country.name}</h4>
                      <span className={`risk-badge risk-${countryForecast.riskLevel}`}>
                        {countryForecast.riskLevel} risk
                      </span>
                    </div>
                    <div className="country-contribution">
                      <span className="contribution-value">{countryForecast.contribution}%</span>
                      <span className="contribution-label">contribution</span>
                    </div>
                  </div>

                  <div className="country-scenarios">
                    <div className="scenario-row">
                      <span className="scenario-label">Worst</span>
                      <span className="scenario-value">{formatCurrency(countryForecast.worstCase)}</span>
                    </div>
                    <div className="scenario-row primary">
                      <span className="scenario-label">Likely</span>
                      <span className="scenario-value">{formatCurrency(countryForecast.forecast)}</span>
                    </div>
                    <div className="scenario-row">
                      <span className="scenario-label">Best</span>
                      <span className="scenario-value">{formatCurrency(countryForecast.bestCase)}</span>
                    </div>
                  </div>

                  <div className="country-pipeline">
                    <div className="pipeline-info">
                      <span className="pipeline-label">Pipeline</span>
                      <span className="pipeline-value">{formatCurrency(countryForecast.pipeline)}</span>
                    </div>
                    <div className="pipeline-coverage">
                      <span className="coverage-label">
                        {((countryForecast.pipeline / countryForecast.forecast) * 1).toFixed(1)}x coverage
                      </span>
                    </div>
                  </div>

                  <div className="country-chart">
                    <CountryForecastChart
                      forecast={countryForecast}
                      height={80}
                    />
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Accuracy Tracking */}
      <section className="accuracy-section">
        <div className="accuracy-grid">
          <Card>
            <CardHeader>
              <h3>Forecast Accuracy</h3>
            </CardHeader>
            <CardBody>
              <ForecastAccuracyChart metrics={accuracyMetrics} height={160} />
            </CardBody>
          </Card>

          <Card>
            <CardHeader>
              <h3>Accuracy History</h3>
            </CardHeader>
            <CardBody>
              <div className="accuracy-list">
                {accuracyMetrics.map((metric) => (
                  <div key={metric.period} className="accuracy-item">
                    <div className="accuracy-period">{metric.period}</div>
                    <div className="accuracy-metrics">
                      <div className="accuracy-metric">
                        <span className="metric-label">Accuracy</span>
                        <span className={`metric-value ${metric.accuracy >= 90 ? 'excellent' : metric.accuracy >= 80 ? 'good' : 'fair'}`}>
                          {metric.accuracy.toFixed(1)}%
                        </span>
                      </div>
                      <div className="accuracy-metric">
                        <span className="metric-label">Variance</span>
                        <span className={`metric-value ${metric.variance >= 0 ? 'positive' : 'negative'}`}>
                          {metric.variance >= 0 ? '+' : ''}{metric.variancePercentage.toFixed(1)}%
                        </span>
                      </div>
                    </div>
                    <div className="accuracy-revenue">
                      <span className="revenue-label">Forecast: {formatCurrency(metric.forecastedRevenue)}</span>
                      <span className="revenue-actual">Actual: {formatCurrency(metric.actualRevenue)}</span>
                    </div>
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>
        </div>
      </section>

      {/* Pipeline by Stage */}
      <section className="pipeline-section">
        <Card>
          <CardHeader>
            <h3>Forecast by Pipeline Stage</h3>
          </CardHeader>
          <CardBody>
            <div className="pipeline-grid">
              {currentForecast.byStage.map((stage) => (
                <div key={stage.stage} className="pipeline-stage-card">
                  <div className="stage-header">
                    <h4 className="stage-name">{stage.stage}</h4>
                    <span className="stage-count">{stage.count} deals</span>
                  </div>
                  <div className="stage-values">
                    <div className="stage-value">
                      <span className="value-label">Total Value</span>
                      <span className="value-amount">{formatCurrency(stage.value)}</span>
                    </div>
                    <div className="stage-value">
                      <span className="value-label">Weighted</span>
                      <span className="value-amount">{formatCurrency(stage.weightedValue)}</span>
                    </div>
                  </div>
                  <div className="stage-probability">
                    <span className="probability-label">Close Probability</span>
                    <div className="probability-bar">
                      <div
                        className="probability-fill"
                        style={{ width: `${stage.probability}%` }}
                      ></div>
                    </div>
                    <span className="probability-value">{stage.probability}%</span>
                  </div>
                  <div className="stage-contribution">
                    <span className="contribution-label">Contribution to Forecast</span>
                    <span className="contribution-value">{stage.contribution}%</span>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>
    </div>
  );
}
