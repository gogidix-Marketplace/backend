// Performance Chart Component
// Visualizes performance metrics, rankings, and trends

import { useMemo } from 'react';
import type { GlobalTopPerformer, PerformanceTrend, ProductPerformance } from '@domain/types';
import { formatCurrency, formatPercentage } from '@shared';
import './PerformanceChart.css';

export interface PerformanceBarChartProps {
  data: Array<{
    label: string;
    value: number;
    target?: number;
    previous?: number;
    color?: string;
  }>;
  height?: number;
  showTarget?: boolean;
  showTrend?: boolean;
  horizontal?: boolean;
}

export function PerformanceBarChart({
  data,
  height = 200,
  showTarget = false,
  showTrend = false,
  horizontal = false,
}: PerformanceBarChartProps) {
  const maxValue = useMemo(
    () => Math.max(...data.map(d => Math.max(d.value, d.target || 0))),
    [data]
  );

  if (horizontal) {
    return (
      <div className="performance-bar-chart horizontal" style={{ height: 'auto' }}>
        {data.map((item, index) => {
          const widthPercent = (item.value / maxValue) * 100;
          const previousPercent = item.previous !== undefined
            ? (item.previous / maxValue) * 100
            : null;
          const targetPercent = item.target !== undefined
            ? (item.target / maxValue) * 100
            : null;

          return (
            <div key={index} className="perf-bar-row">
              <div className="perf-bar-label">{item.label}</div>
              <div className="perf-bar-track">
                {targetPercent !== null && (
                  <div
                    className="perf-bar-target"
                    style={{ left: `${targetPercent}%` }}
                  />
                )}
                {previousPercent !== null && (
                  <div
                    className="perf-bar-previous"
                    style={{ width: `${previousPercent}%` }}
                  />
                )}
                <div
                  className="perf-bar-fill"
                  style={{
                    width: `${widthPercent}%`,
                    backgroundColor: item.color,
                  }}
                >
                  <span className="perf-bar-value">{item.value.toLocaleString()}</span>
                </div>
              </div>
              {showTrend && item.previous !== undefined && (
                <div className={`perf-trend ${item.value >= item.previous ? 'up' : 'down'}`}>
                  {item.value >= item.previous ? '\u2197' : '\u2198'}
                  {Math.abs(((item.value - item.previous) / item.previous) * 100).toFixed(0)}%
                </div>
              )}
            </div>
          );
        })}
      </div>
    );
  }

  return (
    <svg
      width="100%"
      height={height}
      viewBox={`0 0 100 ${height}`}
      preserveAspectRatio="none"
      className="performance-bar-chart vertical"
    >
      {data.map((item, i) => {
        const barWidth = 100 / data.length;
        const barHeight = (item.value / maxValue) * (height - 20);
        const x = i * barWidth;
        const y = height - 20 - barHeight;

        return (
          <g key={i}>
            <rect
              x={x + 1}
              y={y}
              width={barWidth - 2}
              height={barHeight}
              fill={item.color || '#2563EB'}
              opacity="0.8"
              rx="1"
            />
            <text
              x={x + barWidth / 2}
              y={y - 2}
              textAnchor="middle"
              fontSize="4"
              fill="#374151"
            >
              {item.value}
            </text>
            <text
              x={x + barWidth / 2}
              y={height - 5}
              textAnchor="middle"
              fontSize="3"
              fill="#6B7280"
            >
              {item.label.slice(0, 6)}
            </text>
            {showTarget && item.target && (
              <line
                x1={x}
                y1={height - 20 - (item.target / maxValue) * (height - 20)}
                x2={x + barWidth}
                y2={height - 20 - (item.target / maxValue) * (height - 20)}
                stroke="#10B981"
                strokeWidth="0.5"
                strokeDasharray="1,1"
              />
            )}
          </g>
        );
      })}
    </svg>
  );
}

export interface TopPerformersListProps {
  performers: GlobalTopPerformer[];
  limit?: number;
  showRank?: boolean;
  compact?: boolean;
}

export function TopPerformersList({
  performers,
  limit = 10,
  showRank = true,
  compact = false,
}: TopPerformersListProps) {
  const displayPerformers = performers.slice(0, limit);

  return (
    <div className={`top-performers-list ${compact ? 'compact' : ''}`}>
      {displayPerformers.map((performer, index) => (
        <div key={performer.userId} className="performer-row">
          {showRank && (
            <div className="performer-rank">
              <span className={`rank-badge rank-${index + 1}`}>
                {index === 0 && '\u{1F947}'}
                {index === 1 && '\u{1F948}'}
                {index === 2 && '\u{1F949}'}
                {index > 2 && index + 1}
              </span>
            </div>
          )}
          {performer.avatar && (
            <img src={performer.avatar} alt={performer.name} className="performer-avatar" />
          )}
          <div className="performer-details">
            <div className="performer-name">{performer.name}</div>
            {!compact && (
              <div className="performer-meta">
                <span className="performer-country">{performer.country.flag}</span>
                <span className="performer-department">{performer.department}</span>
              </div>
            )}
          </div>
          <div className="performer-stats">
            <div className="performer-stat">
              <span className="stat-label">Revenue</span>
              <span className="stat-value">{formatCurrency(performer.revenue)}</span>
            </div>
            <div className="performer-stat">
              <span className="stat-label">Attainment</span>
              <span className={`stat-value ${performer.attainment >= 100 ? 'success' : ''}`}>
                {performer.attainment}%
              </span>
            </div>
            {!compact && (
              <div className="performer-stat">
                <span className="stat-label">Deals</span>
                <span className="stat-value">{performer.dealsClosed}</span>
              </div>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}

export interface PerformanceTrendChartProps {
  trends: PerformanceTrend[];
  height?: number;
  showRevenue?: boolean;
  showTarget?: boolean;
  showWinRate?: boolean;
}

export function PerformanceTrendChart({
  trends,
  height = 150,
  showRevenue = true,
  showTarget = false,
  showWinRate = false,
}: PerformanceTrendChartProps) {
  const maxValue = useMemo(
    () => Math.max(...trends.map(t => Math.max(t.revenue, t.target || 0))),
    [trends, showTarget]
  );

  return (
    <svg
      width="100%"
      height={height}
      viewBox={`0 0 100 ${height}`}
      preserveAspectRatio="none"
      className="performance-trend-chart"
    >
      {/* Grid */}
      {[0, 25, 50, 75, 100].map((p) => (
        <line
          key={p}
          x1="0"
          y1={(height - 10) * (1 - p / 100) + 5}
          x2="100"
          y2={(height - 10) * (1 - p / 100) + 5}
          stroke="#E5E7EB"
          strokeWidth="0.2"
        />
      ))}

      {/* Target line */}
      {showTarget && trends.some(t => t.target) && (
        <polyline
          points={trends.map((t, i) => {
            const x = (i / (trends.length - 1 || 1)) * 100;
            const y = (height - 10) * (1 - ((t.target || 0) / maxValue)) + 5;
            return `${x},${y}`;
          }).join(' ')}
          fill="none"
          stroke="#10B981"
          strokeWidth="1"
          strokeDasharray="2,2"
        />
      )}

      {/* Area fill */}
      <path
        d={`M ${trends.map((t, i) => {
          const x = (i / (trends.length - 1 || 1)) * 100;
          const y = (height - 10) * (1 - (t.revenue / maxValue)) + 5;
          return `${x},${y}`;
        }).join(' ')} L 100,${height - 5} L 0,${height - 5} Z`}
        fill="#2563EB"
        opacity="0.1"
      />

      {/* Revenue line */}
      <polyline
        points={trends.map((t, i) => {
          const x = (i / (trends.length - 1 || 1)) * 100;
          const y = (height - 10) * (1 - (t.revenue / maxValue)) + 5;
          return `${x},${y}`;
        }).join(' ')}
        fill="none"
        stroke="#2563EB"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />

      {/* Data points */}
      {trends.map((t, i) => {
        const x = (i / (trends.length - 1 || 1)) * 100;
        const y = (height - 10) * (1 - (t.revenue / maxValue)) + 5;
        return (
          <g key={i}>
            <circle cx={x} cy={y} r="1.5" fill="#2563EB" stroke="white" strokeWidth="0.5" />
            {trends.length <= 6 && (
              <text
                x={x}
                y={y - 3}
                textAnchor="middle"
                fontSize="3"
                fill="#374151"
              >
                {(t.revenue / 1000000).toFixed(1)}M
              </text>
            )}
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
            y={height - 1}
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

export interface ProductPerformanceChartProps {
  products: ProductPerformance[];
  height?: number;
}

export function ProductPerformanceChart({ products, height = 200 }: ProductPerformanceChartProps) {
  const maxValue = Math.max(...products.map(p => p.revenue));

  return (
    <div className="product-performance-chart">
      <div className="product-bars">
        {products
          .sort((a, b) => b.revenue - a.revenue)
          .map((product) => {
            const widthPercent = (product.revenue / maxValue) * 100;
            const attainmentColor = product.targetAttainment >= 100
              ? 'var(--color-success)'
              : product.targetAttainment >= 80
              ? 'var(--color-warning)'
              : 'var(--color-error)';

            return (
              <div key={product.productCode} className="product-bar-item">
                <div className="product-bar-header">
                  <div className="product-info">
                    <span className="product-name">{product.productName}</span>
                    <span className="product-category">{product.category}</span>
                  </div>
                  <div className="product-metrics">
                    <span className="product-revenue">{formatCurrency(product.revenue)}</span>
                    <span className="product-growth" style={{ color: attainmentColor }}>
                      {product.growth > 0 ? '+' : ''}{product.growth}%
                    </span>
                  </div>
                </div>
                <div className="product-bar-track">
                  <div
                    className="product-bar-fill"
                    style={{ width: `${widthPercent}%` }}
                  >
                    <span className="product-percentage">{product.percentage}%</span>
                  </div>
                </div>
                <div className="product-countries">
                  {Object.entries(product.byCountry).slice(0, 3).map(([code, metric]) => (
                    <span key={code} className="product-country-tag">
                      {code}: {formatCurrency((metric as any).revenue || 0)}
                    </span>
                  ))}
                </div>
              </div>
            );
          })}
      </div>
    </div>
  );
}

export interface AttainmentGaugeProps {
  value: number;
  target?: number;
  size?: number;
  showLabel?: boolean;
  color?: string;
}

export function AttainmentGauge({
  value,
  target = 100,
  size = 120,
  showLabel = true,
  color,
}: AttainmentGaugeProps) {
  const percentage = Math.min((value / target) * 100, 100);
  const circumference = 2 * Math.PI * (size / 2 - 10);
  const offset = circumference - (percentage / 100) * circumference;

  const getColor = () => {
    if (color) return color;
    if (percentage >= 100) return 'var(--color-success)';
    if (percentage >= 80) return 'var(--color-warning)';
    return 'var(--color-error)';
  };

  return (
    <div className="attainment-gauge" style={{ width: size, height: size }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        {/* Background circle */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={size / 2 - 10}
          fill="none"
          stroke="#E5E7EB"
          strokeWidth="12"
        />
        {/* Progress circle */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={size / 2 - 10}
          fill="none"
          stroke={getColor()}
          strokeWidth="12"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          strokeLinecap="round"
          transform={`rotate(-90 ${size / 2} ${size / 2})`}
          className="gauge-progress"
        />
        {/* Center text */}
        <text
          x={size / 2}
          y={size / 2 - 5}
          textAnchor="middle"
          fontSize={size / 5}
          fontWeight="bold"
          fill="#1F2937"
        >
          {percentage.toFixed(0)}%
        </text>
        {showLabel && (
          <text
            x={size / 2}
            y={size / 2 + size / 7}
            textAnchor="middle"
            fontSize={size / 10}
            fill="#6B7280"
          >
            Attainment
          </text>
        )}
      </svg>
    </div>
  );
}
