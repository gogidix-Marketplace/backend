// RevenueChart Component
// Simple SVG-based revenue trend chart

import React from 'react';
import { formatCurrency, formatDate } from '@shared';

export interface RevenueChartProps {
  data: Array<{
    period: string;
    revenue: number;
    target?: number;
  }>;
  height?: number;
  showTarget?: boolean;
  currency?: string;
  className?: string;
}

export const RevenueChart: React.FC<RevenueChartProps> = ({
  data,
  height = 200,
  showTarget = true,
  currency = 'USD',
  className = '',
}) => {
  if (data.length === 0) {
    return (
      <div className={`revenue-chart revenue-chart-empty ${className}`} style={{ height }}>
        <p>No data available</p>
      </div>
    );
  }

  const padding = { top: 20, right: 20, bottom: 40, left: 60 };
  const chartWidth = 800;
  const chartHeight = height - padding.top - padding.bottom;

  const maxRevenue = Math.max(...data.map(d => Math.max(d.revenue, d.target || 0)));
  const minRevenue = 0;

  const xScale = (index: number) =>
    padding.left + (index / (data.length - 1)) * (chartWidth - padding.left - padding.right);

  const yScale = (value: number) =>
    chartHeight + padding.top - ((value - minRevenue) / (maxRevenue - minRevenue)) * chartHeight;

  // Generate path for revenue line
  const revenuePath = data
    .map((d, i) => {
      const x = xScale(i);
      const y = yScale(d.revenue);
      return `${i === 0 ? 'M' : 'L'} ${x} ${y}`;
    })
    .join(' ');

  // Generate area under revenue line
  const revenueArea = `${revenuePath} L ${xScale(data.length - 1)} ${chartHeight + padding.top} L ${padding.left} ${chartHeight + padding.top} Z`;

  // Generate path for target line
  const targetPath = data
    .map((d, i) => {
      if (d.target === undefined) return '';
      const x = xScale(i);
      const y = yScale(d.target);
      return `${i === 0 ? 'M' : 'L'} ${x} ${y}`;
    })
    .filter(Boolean)
    .join(' ');

  return (
    <div className={`revenue-chart ${className}`}>
      <svg width="100%" height={height} viewBox={`0 0 ${chartWidth} ${height}`}>
        {/* Grid lines */}
        {[0, 25, 50, 75, 100].map((percent) => {
          const y = yScale((maxRevenue * percent) / 100);
          return (
            <g key={percent}>
              <line
                x1={padding.left}
                y1={y}
                x2={chartWidth - padding.right}
                y2={y}
                className="chart-grid-line"
              />
              <text
                x={padding.left - 10}
                y={y + 4}
                className="chart-label"
                textAnchor="end"
                fontSize="12"
              >
                {formatCompactCurrency(maxRevenue * percent / 100)}
              </text>
            </g>
          );
        })}

        {/* Target area */}
        {showTarget && targetPath && (
          <path
            d={targetPath}
            fill="none"
            stroke="#10B981"
            strokeWidth={2}
            strokeDasharray="5,5"
            className="chart-target-line"
          />
        )}

        {/* Revenue area */}
        <path
          d={revenueArea}
          fill="rgba(37, 99, 235, 0.1)"
          className="chart-area"
        />

        {/* Revenue line */}
        <path
          d={revenuePath}
          fill="none"
          stroke="#2563EB"
          strokeWidth={3}
          strokeLinecap="round"
          strokeLinejoin="round"
          className="chart-line"
        />

        {/* Data points */}
        {data.map((d, i) => (
          <g key={i}>
            <circle
              cx={xScale(i)}
              cy={yScale(d.revenue)}
              r={5}
              fill="#2563EB"
              className="chart-point"
            />
            {d.target !== undefined && (
              <circle
                cx={xScale(i)}
                cy={yScale(d.target)}
                r={4}
                fill="#10B981"
                className="chart-target-point"
              />
            )}
          </g>
        ))}

        {/* X-axis labels */}
        {data.map((d, i) => (
          <text
            key={i}
            x={xScale(i)}
            y={height - padding.bottom + 20}
            className="chart-label"
            textAnchor="middle"
            fontSize="12"
          >
            {d.period}
          </text>
        ))}
      </svg>

      {/* Legend */}
      <div className="chart-legend">
        <div className="chart-legend-item">
          <div className="chart-legend-color" style={{ background: '#2563EB' }} />
          <span>Revenue</span>
        </div>
        {showTarget && (
          <div className="chart-legend-item">
            <div className="chart-legend-color chart-legend-dashed" style={{ background: '#10B981' }} />
            <span>Target</span>
          </div>
        )}
      </div>
    </div>
  );
};

// Helper function for compact currency
function formatCompactCurrency(value: number): string {
  if (value >= 1000000) return `$${(value / 1000000).toFixed(0)}M`;
  if (value >= 1000) return `$${(value / 1000).toFixed(0)}K`;
  return `$${value}`;
}

export default RevenueChart;
