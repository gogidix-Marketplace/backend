// Forecast Chart Component
// Visualizes sales forecasts with scenarios and accuracy metrics

import { useMemo } from 'react';
import type { GlobalSalesForecast, CountryForecast, ForecastTrend } from '@domain/types';
import { formatCurrency } from '@shared';
import './ForecastChart.css';

export interface ForecastScenarioChartProps {
  forecast: number;
  bestCase?: number;
  worstCase?: number;
  currency?: string;
  height?: number;
  showLabels?: boolean;
}

export function ForecastScenarioChart({
  forecast,
  bestCase,
  worstCase,
  currency = 'USD',
  height = 150,
  showLabels = true,
}: ForecastScenarioChartProps) {
  const maxValue = useMemo(() => {
    return Math.max(forecast, bestCase || 0, worstCase || 0);
  }, [forecast, bestCase, worstCase]);

  const worstHeight = ((worstCase || forecast) / maxValue) * (height - 40);
  const forecastHeight = (forecast / maxValue) * (height - 40);
  const bestHeight = ((bestCase || forecast) / maxValue) * (height - 40);

  return (
    <div className="forecast-scenario-chart" style={{ height }}>
      <svg width="100%" height="100%" viewBox={`0 0 100 ${height}`} preserveAspectRatio="none">
        {/* Reference line */}
        <line
          x1="0"
          y1={height - 20}
          x2="100"
          y2={height - 20}
          stroke="#E5E7EB"
          strokeWidth="0.5"
        />

        {/* Worst case bar */}
        {worstCase && (
          <>
            <rect
              x="25"
              y={height - 20 - worstHeight}
              width="18"
              height={worstHeight}
              fill="#EF4444"
              opacity="0.7"
              rx="2"
            />
            {showLabels && (
              <text
                x="34"
                y={height - 20 - worstHeight - 3}
                textAnchor="middle"
                fontSize="4"
                fill="#EF4444"
              >
                {formatCurrency(worstCase, currency)}
              </text>
            )}
          </>
        )}

        {/* Forecast bar */}
        <rect
          x="48"
          y={height - 20 - forecastHeight}
          width="18"
          height={forecastHeight}
          fill="#3B82F6"
              opacity="0.9"
          rx="2"
        />
        {showLabels && (
          <text
            x="57"
            y={height - 20 - forecastHeight - 3}
            textAnchor="middle"
            fontSize="5"
            fontWeight="bold"
            fill="#3B82F6"
          >
            {formatCurrency(forecast, currency)}
          </text>
        )}

        {/* Best case bar */}
        {bestCase && (
          <>
            <rect
              x="71"
              y={height - 20 - bestHeight}
              width="18"
              height={bestHeight}
              fill="#10B981"
              opacity="0.7"
              rx="2"
            />
            {showLabels && (
              <text
                x="80"
                y={height - 20 - bestHeight - 3}
                textAnchor="middle"
                fontSize="4"
                fill="#10B981"
              >
                {formatCurrency(bestCase, currency)}
              </text>
            )}
          </>
        )}

        {/* Labels */}
        <text
          x="34"
          y={height - 5}
          textAnchor="middle"
          fontSize="3"
          fill="#6B7280"
        >
          Worst
        </text>
        <text
          x="57"
          y={height - 5}
          textAnchor="middle"
          fontSize="3"
          fontWeight="600"
          fill="#3B82F6"
        >
          Forecast
        </text>
        <text
          x="80"
          y={height - 5}
          textAnchor="middle"
          fontSize="3"
          fill="#6B7280"
        >
          Best
        </text>
      </svg>
    </div>
  );
}

export interface ForecastTrendChartProps {
  trends: ForecastTrend[];
  height?: number;
  showConfidence?: boolean;
}

export function ForecastTrendChart({ trends, height = 180, showConfidence = true }: ForecastTrendChartProps) {
  const chartData = useMemo(() => {
    const allValues = trends.flatMap(t => [
      t.actual,
      t.forecast,
      t.bestCase || t.forecast,
      t.worstCase || t.forecast,
    ]);
    const maxValue = Math.max(...allValues.filter(v => v !== undefined));
    const minValue = Math.min(...allValues.filter(v => v !== undefined));
    const range = maxValue - minValue || 1;

    return { minValue, maxValue, range };
  }, [trends]);

  const getY = (value: number) => {
    const padding = 10;
    const chartHeight = height - padding * 2;
    const normalized = (value - chartData.minValue) / chartData.range;
    return padding + chartHeight - normalized * chartHeight;
  };

  const points = trends.map((t, i) => {
    const x = (i / (trends.length - 1 || 1)) * 100;
    const y = getY(t.forecast);
    return { x, y, trend: t };
  });

  const actualPoints = trends
    .filter(t => t.actual !== undefined)
    .map((t, i) => {
      const x = (i / (trends.length - 1 || 1)) * 100;
      const y = getY(t.actual!);
      return `${x},${y}`;
    });

  const forecastPoints = points.map(p => `${p.x},${p.y}`).join(' ');

  return (
    <svg
      width="100%"
      height={height}
      viewBox={`0 0 100 ${height}`}
      preserveAspectRatio="none"
      className="forecast-trend-chart"
    >
      {/* Grid */}
      {[0, 25, 50, 75, 100].map((p) => {
        const y = 10 + (height - 20) * (1 - p / 100);
        return (
          <line
            key={p}
            x1="0"
            y1={y}
            x2="100"
            y2={y}
            stroke="#E5E7EB"
            strokeWidth="0.2"
          />
        );
      })}

      {/* Confidence area */}
      {showConfidence && trends.some(t => t.bestCase || t.worstCase) && (
        <path
          d={`
            M ${trends.map((t, i) => {
              const x = (i / (trends.length - 1 || 1)) * 100;
              const y = getY(t.bestCase || t.forecast);
              return `${x},${y}`;
            }).join(' L ')}
            L ${trends.map((t, i) => {
              const x = (i / (trends.length - 1 || 1)) * 100;
              const y = getY(t.worstCase || t.forecast);
              return `${x},${y}`;
            }).reverse().join(' L ')}
            Z
          `}
          fill="#3B82F6"
          opacity="0.1"
        />
      )}

      {/* Actual line */}
      {actualPoints.length > 0 && (
        <polyline
          points={actualPoints.join(' ')}
          fill="none"
          stroke="#10B981"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeDasharray="3,1"
        />
      )}

      {/* Forecast line */}
      <polyline
        points={forecastPoints}
        fill="none"
        stroke="#3B82F6"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* Data points */}
      {points.map((p, i) => {
        const hasActual = p.trend.actual !== undefined;
        const actualY = hasActual ? getY(p.trend.actual!) : p.y;

        return (
          <g key={i}>
            {/* Actual point */}
            {hasActual && (
              <circle cx={p.x} cy={actualY} r="1.5" fill="#10B981" stroke="white" strokeWidth="0.5" />
            )}
            {/* Forecast point */}
            <circle cx={p.x} cy={p.y} r="1.5" fill="#3B82F6" stroke="white" strokeWidth="0.5" />
          </g>
        );
      })}

      {/* X-axis labels */}
      {trends.map((t, i) => {
        const x = (i / (trends.length - 1 || 1)) * 100;
        return (
          <text
            key={i}
            x={x}
            y={height - 2}
            textAnchor="middle"
            fontSize="3"
            fill="#6B7280"
          >
            {t.period.slice(0, 3)}
          </text>
        );
      })}
    </svg>
  );
}

export interface CountryForecastChartProps {
  countries: CountryForecast[];
  height?: number;
}

export function CountryForecastChart({ countries, height = 200 }: CountryForecastChartProps) {
  const maxValue = useMemo(
    () => Math.max(...countries.map(c => Math.max(c.forecast, c.bestCase || 0))),
    [countries]
  );

  const barWidth = 100 / countries.length;

  return (
    <svg
      width="100%"
      height={height}
      viewBox={`0 0 100 ${height}`}
      preserveAspectRatio="none"
      className="country-forecast-chart"
    >
      {countries.map((country, i) => {
        const forecastHeight = (country.forecast / maxValue) * (height - 30);
        const bestHeight = ((country.bestCase || country.forecast) / maxValue) * (height - 30);
        const worstHeight = ((country.worstCase || country.forecast) / maxValue) * (height - 30);
        const x = i * barWidth;

        return (
          <g key={country.country.code}>
            {/* Range indicator */}
            {country.bestCase && country.worstCase && (
              <rect
                x={x + 2}
                y={height - 25 - bestHeight}
                width={barWidth - 4}
                height={bestHeight - worstHeight}
                fill="#3B82F6"
                opacity="0.15"
                rx="1"
              />
            )}
            {/* Worst case line */}
            {country.worstCase && (
              <line
                x1={x + 2}
                y1={height - 25 - worstHeight}
                x2={x + barWidth - 2}
                y2={height - 25 - worstHeight}
                stroke="#EF4444"
                strokeWidth="0.5"
                strokeDasharray="1,1"
              />
            )}
            {/* Forecast bar */}
            <rect
              x={x + 4}
              y={height - 25 - forecastHeight}
              width={barWidth - 8}
              height={forecastHeight}
              fill="#3B82F6"
              opacity="0.8"
              rx="1"
            />
            {/* Value label */}
            <text
              x={x + barWidth / 2}
              y={height - 25 - forecastHeight - 2}
              textAnchor="middle"
              fontSize="3"
              fill="#374151"
            >
              {(country.forecast / 1000000).toFixed(1)}M
            </text>
            {/* Country label */}
            <text
              x={x + barWidth / 2}
              y={height - 5}
              textAnchor="middle"
              fontSize="3"
              fill="#6B7280"
            >
              {country.country.code}
            </text>
          </g>
        );
      })}
    </svg>
  );
}

export interface ForecastAccuracyChartProps {
  accuracy: Array<{
    period: string;
    accuracy: number;
    variance: number;
    actualRevenue: number;
    forecastedRevenue: number;
  }>;
  height?: number;
}

export function ForecastAccuracyChart({ accuracy, height = 150 }: ForecastAccuracyChartProps) {
  return (
    <div className="forecast-accuracy-chart">
      <svg
        width="100%"
        height={height}
        viewBox={`0 0 100 ${height}`}
        preserveAspectRatio="none"
      >
        {/* Grid */}
        {[0, 25, 50, 75, 100].map((p) => {
          const y = (height - 20) * (1 - p / 100) + 10;
          return (
            <g key={p}>
              <line x1="0" y1={y} x2="100" y2={y} stroke="#E5E7EB" strokeWidth="0.2" />
              <text x="2" y={y - 1} fontSize="3" fill="#9CA3AF">{p}%</text>
            </g>
          );
        })}

        {/* Accuracy line */}
        <polyline
          points={accuracy.map((a, i) => {
            const x = (i / (accuracy.length - 1 || 1)) * 100;
            const y = (height - 20) * (1 - a.accuracy / 100) + 10;
            return `${x},${y}`;
          }).join(' ')}
          fill="none"
          stroke="#10B981"
          strokeWidth="2"
          strokeLinecap="round"
        />

        {/* Data points */}
        {accuracy.map((a, i) => {
          const x = (i / (accuracy.length - 1 || 1)) * 100;
          const y = (height - 20) * (1 - a.accuracy / 100) + 10;
          return (
            <g key={i}>
              <circle cx={x} cy={y} r="2" fill="#10B981" />
              {accuracy.length <= 6 && (
                <text
                  x={x}
                  y={y - 4}
                  textAnchor="middle"
                  fontSize="3"
                  fill="#10B981"
                  fontWeight="600"
                >
                  {a.accuracy.toFixed(0)}%
                </text>
              )}
            </g>
          );
        })}

        {/* X-axis labels */}
        {accuracy.map((a, i) => {
          const x = (i / (accuracy.length - 1 || 1)) * 100;
          return (
            <text
              key={i}
              x={x}
              y={height - 2}
              textAnchor="middle"
              fontSize="3"
              fill="#6B7280"
            >
              {a.period.slice(0, 4)}
            </text>
          );
        })}
      </svg>
    </div>
  );
}

export interface ForecastSummaryCardProps {
  forecast: GlobalSalesForecast;
}

export function ForecastSummaryCard({ forecast }: ForecastSummaryCardProps) {
  const variance = forecast.global.bestCase - forecast.global.worstCase;
  const variancePercent = forecast.global.forecast > 0
    ? (variance / forecast.global.forecast) * 100
    : 0;

  return (
    <div className="forecast-summary-card">
      <div className="forecast-header">
        <h3>{forecast.name}</h3>
        <span className="forecast-period">{forecast.period.label}</span>
      </div>

      <div className="forecast-main-metric">
        <span className="forecast-value">
          {formatCurrency(forecast.global.forecast, forecast.global.currency)}
        </span>
        <span className="forecast-label">Forecast</span>
      </div>

      <div className="forecast-scenarios">
        <div className="scenario-item best">
          <span className="scenario-label">Best Case</span>
          <span className="scenario-value">
            {formatCurrency(forecast.global.bestCase, forecast.global.currency)}
          </span>
        </div>
        <div className="scenario-item worst">
          <span className="scenario-label">Worst Case</span>
          <span className="scenario-value">
            {formatCurrency(forecast.global.worstCase, forecast.global.currency)}
          </span>
        </div>
      </div>

      <div className="forecast-confidence">
        <div className="confidence-label">Confidence Level</div>
        <div className="confidence-bar">
          <div
            className="confidence-fill"
            style={{ width: `${forecast.global.confidence}%` }}
          />
        </div>
        <div className="confidence-value">{forecast.global.confidence}%</div>
      </div>

      <div className="forecast-variance">
        <span>Range: {variancePercent.toFixed(0)}%</span>
        <span>
          {formatCurrency(variance, forecast.global.currency)} variance
        </span>
      </div>
    </div>
  );
}
