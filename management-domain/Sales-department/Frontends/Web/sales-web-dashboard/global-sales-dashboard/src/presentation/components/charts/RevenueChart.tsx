// Revenue Chart Component
// Simple SVG-based line/bar chart for revenue visualization

import { useMemo } from 'react';
import type { CountrySummary } from '@domain/types';
import { formatCurrency } from '@shared';
import './RevenueChart.css';

export interface RevenueChartProps {
  data: Array<{
    label: string;
    value: number;
    target?: number;
    previous?: number;
  }>;
  type?: 'line' | 'bar' | 'area';
  height?: number;
  showTarget?: boolean;
  showPrevious?: boolean;
  currency?: string;
  color?: string;
}

export function RevenueChart({
  data,
  type = 'bar',
  height = 200,
  showTarget = false,
  showPrevious = false,
  currency = 'USD',
  color = '#2563EB',
}: RevenueChartProps) {
  const chartData = useMemo(() => {
    const maxValue = Math.max(...data.map(d => Math.max(d.value, d.target || 0)));
    const padding = { top: 20, right: 20, bottom: 40, left: 60 };
    const chartWidth = 600 - padding.left - padding.right;
    const chartHeight = height - padding.top - padding.bottom;

    const points = data.map((d, i) => {
      const x = padding.left + (i * chartWidth) / (data.length - 1 || 1);
      const y = padding.top + chartHeight - (d.value / maxValue) * chartHeight;
      return { x, y, value: d.value, label: d.label };
    });

    const targetPoints = showTarget
      ? data.map((d, i) => {
          const x = padding.left + (i * chartWidth) / (data.length - 1 || 1);
          const y = padding.top + chartHeight - ((d.target || 0) / maxValue) * chartHeight;
          return { x, y };
        })
      : [];

    return { maxValue, padding, chartWidth, chartHeight, points, targetPoints };
  }, [data, height, showTarget]);

  const renderBars = () => (
    <g className="chart-bars">
      {chartData.points.map((point, i) => (
        <g key={i}>
          <rect
            x={point.x - (chartData.chartWidth / data.length) * 0.35}
            y={point.y}
            width={(chartData.chartWidth / data.length) * 0.7}
            height={chartData.chartHeight + chartData.padding.top - point.y}
            fill={color}
            opacity="0.8"
            rx="4"
          />
          <text
            x={point.x}
            y={point.y - 8}
            textAnchor="middle"
            className="chart-value"
            fontSize="12"
            fill="#374151"
          >
            {formatCurrency(point.value, currency)}
          </text>
          <text
            x={point.x}
            y={chartData.padding.top + chartData.chartHeight + 20}
            textAnchor="middle"
            className="chart-label"
            fontSize="11"
            fill="#6B7280"
          >
            {point.label}
          </text>
        </g>
      ))}
    </g>
  );

  const renderLine = () => (
    <g className="chart-lines">
      {/* Area fill */}
      <path
        d={`M ${chartData.points.map(p => `${p.x},${p.y}`).join(' L ')} L ${chartData.points[chartData.points.length - 1].x},${chartData.padding.top + chartData.chartHeight} L ${chartData.points[0].x},${chartData.padding.top + chartData.chartHeight} Z`}
        fill={color}
        opacity="0.1"
      />
      {/* Line */}
      <polyline
        points={chartData.points.map(p => `${p.x},${p.y}`).join(' ')}
        fill="none"
        stroke={color}
        strokeWidth="3"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
      {/* Data points */}
      {chartData.points.map((point, i) => (
        <g key={i}>
          <circle cx={point.x} cy={point.y} r="5" fill={color} stroke="white" strokeWidth="2" />
          <text
            x={point.x}
            y={point.y - 12}
            textAnchor="middle"
            className="chart-value"
            fontSize="12"
            fill="#374151"
            fontWeight="600"
          >
            {formatCurrency(point.value, currency)}
          </text>
          <text
            x={point.x}
            y={chartData.padding.top + chartData.chartHeight + 20}
            textAnchor="middle"
            className="chart-label"
            fontSize="11"
            fill="#6B7280"
          >
            {point.label}
          </text>
        </g>
      ))}
    </g>
  );

  const renderTargetLine = () => {
    if (!showTarget || chartData.targetPoints.length === 0) return null;
    return (
      <g className="chart-target-line">
        <polyline
          points={chartData.targetPoints.map(p => `${p.x},${p.y}`).join(' ')}
          fill="none"
          stroke="#10B981"
          strokeWidth="2"
          strokeDasharray="5,5"
        />
        <text x="10" y="15" fontSize="11" fill="#10B981" fontWeight="500">
          Target
        </text>
      </g>
    );
  };

  return (
    <div className="revenue-chart-container">
      <svg width="100%" height={height + 40} viewBox={`0 0 600 ${height + 40}`} className="revenue-chart">
        {/* Grid lines */}
        <g className="chart-grid">
          {[0, 25, 50, 75, 100].map((percent) => {
            const y = chartData.padding.top + chartData.chartHeight * (1 - percent / 100);
            return (
              <g key={percent}>
                <line
                  x1={chartData.padding.left}
                  y1={y}
                  x2={600 - chartData.padding.right}
                  y2={y}
                  stroke="#E5E7EB"
                  strokeDasharray="3,3"
                />
                <text
                  x={chartData.padding.left - 10}
                  y={y + 4}
                  textAnchor="end"
                  fontSize="10"
                  fill="#9CA3AF"
                >
                  {formatCurrency((chartData.maxValue * percent) / 100, currency)}
                </text>
              </g>
            );
          })}
        </g>

        {renderTargetLine()}
        {type === 'line' || type === 'area' ? renderLine() : renderBars()}
      </svg>
    </div>
  );
}

// Country Comparison Chart Component
export interface CountryComparisonChartProps {
  countries: CountrySummary[];
  metric: keyof CountrySummary['metrics'];
  height?: number;
}

export function CountryComparisonChart({
  countries,
  metric,
  height = 300,
}: CountryComparisonChartProps) {
  const maxValue = useMemo(
    () => Math.max(...countries.map(c => c.metrics[metric] as number)),
    [countries, metric]
  );

  const metricLabels: Record<string, string> = {
    revenue: 'Revenue',
    quota: 'Quota',
    attainment: 'Attainment',
    dealsClosed: 'Deals Closed',
    pipelineValue: 'Pipeline',
    winRate: 'Win Rate',
    growth: 'Growth',
  };

  return (
    <div className="country-comparison-chart">
      <h3 className="chart-title">Country Comparison - {metricLabels[metric]}</h3>
      <div className="country-bars">
        {countries
          .sort((a, b) => (b.metrics[metric] as number) - (a.metrics[metric] as number))
          .map((country, index) => {
            const value = country.metrics[metric] as number;
            const percentage = (value / maxValue) * 100;
            const barHeight = Math.max(percentage, 5);

            return (
              <div key={country.country.code} className="country-bar-item">
                <div className="country-bar-label">
                  <span className="country-flag">{country.country.flag}</span>
                  <span className="country-name">{country.country.name}</span>
                </div>
                <div className="country-bar-track">
                  <div
                    className={`country-bar-fill country-bar-fill-${country.status}`}
                    style={{ width: `${barHeight}%` }}
                  >
                    <span className="country-bar-value">
                      {metric === 'attainment' || metric === 'winRate' || metric === 'growth'
                        ? `${value}%`
                        : formatCurrency(value, country.country.currency)}
                    </span>
                  </div>
                </div>
                <div className={`country-status-indicator status-${country.trend}`}>
                  {country.trend === 'up' && '\u2197'}
                  {country.trend === 'down' && '\u2198'}
                  {country.trend === 'neutral' && '\u2192'}
                </div>
              </div>
            );
          })}
      </div>
    </div>
  );
}

// Monthly Trend Chart Component
export interface MonthlyTrendChartProps {
  data: Array<{
    month: string;
    revenue: number;
    target?: number;
    forecast?: number;
  }>;
  height?: number;
}

export function MonthlyTrendChart({ data, height = 180 }: MonthlyTrendChartProps) {
  const maxValue = useMemo(
    () => Math.max(...data.map(d => Math.max(d.revenue, d.target || 0, d.forecast || 0))),
    [data]
  );
  const hasForecast = data.some(d => d.forecast !== undefined);
  const hasTarget = data.some(d => d.target !== undefined);

  const width = 100;
  const padding = 5;
  const chartHeight = height - padding * 2;
  const step = (width - padding * 2) / (data.length - 1);

  const getPoints = (key: 'revenue' | 'target' | 'forecast') =>
    data.map((d, i) => {
      const x = padding + i * step;
      const y = padding + chartHeight - ((d[key] || 0) / maxValue) * chartHeight;
      return `${x},${y}`;
    }).join(' ');

  return (
    <svg
      width="100%"
      height={height}
      viewBox={`0 0 100 ${height}`}
      preserveAspectRatio="none"
      className="monthly-trend-chart"
    >
      {/* Grid */}
      {[0, 25, 50, 75, 100].map((p) => (
        <line
          key={p}
          x1={padding}
          y1={padding + chartHeight * (1 - p / 100)}
          x2={width - padding}
          y2={padding + chartHeight * (1 - p / 100)}
          stroke="#E5E7EB"
          strokeWidth="0.2"
        />
      ))}

      {/* Target line */}
      {hasTarget && (
        <polyline
          points={getPoints('target')}
          fill="none"
          stroke="#10B981"
          strokeWidth="1"
          strokeDasharray="2,2"
        />
      )}

      {/* Forecast line */}
      {hasForecast && (
        <polyline
          points={getPoints('forecast')}
          fill="none"
          stroke="#F59E0B"
          strokeWidth="1.5"
          strokeDasharray="3,1"
        />
      )}

      {/* Revenue area */}
      <path
        d={`M ${getPoints('revenue')} L ${padding + (data.length - 1) * step},${padding + chartHeight} L ${padding},${padding + chartHeight} Z`}
        fill="#2563EB"
        opacity="0.15"
      />

      {/* Revenue line */}
      <polyline
        points={getPoints('revenue')}
        fill="none"
        stroke="#2563EB"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* Data points */}
      {data.map((d, i) => {
        const x = padding + i * step;
        const y = padding + chartHeight - (d.revenue / maxValue) * chartHeight;
        return (
          <circle
            key={i}
            cx={x}
            cy={y}
            r="1.5"
            fill="#2563EB"
            stroke="white"
            strokeWidth="0.5"
          />
        );
      })}
    </svg>
  );
}
