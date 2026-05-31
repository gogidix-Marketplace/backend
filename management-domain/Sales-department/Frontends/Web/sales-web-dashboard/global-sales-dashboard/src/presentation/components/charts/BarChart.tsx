// BarChart Component
// Displays data as vertical bars using SVG

import type React from 'react';
import { formatCurrency } from '@shared/utils';
import './BarChart.css';

export interface BarDataPoint {
  label: string;
  value: number;
  color?: string;
  target?: number;
}

export interface BarChartProps {
  data: BarDataPoint[];
  height?: number;
  horizontal?: boolean;
  showGrid?: boolean;
  showValues?: boolean;
  showTarget?: boolean;
  barWidth?: number;
  barGap?: number;
  className?: string;
  title?: string;
  subtitle?: string;
  groupBy?: string;
}

export function BarChart({
  data,
  height = 200,
  horizontal = false,
  showGrid = true,
  showValues = true,
  showTarget = false,
  barWidth = 40,
  barGap = 20,
  className = '',
  title,
  subtitle,
  groupBy,
}: BarChartProps): React.ReactElement {
  if (data.length === 0) {
    return (
      <div className={`bar-chart bar-chart-empty ${className}`} style={{ height }}>
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
          <path d="M12 20V10" />
          <path d="M18 20V4" />
          <path d="M6 20v-4" />
        </svg>
        <p>No data available</p>
      </div>
    );
  }

  const values = data.map(d => d.value);
  const maxValue = Math.max(...values) * 1.1;
  const minValue = 0;

  const chartHeight = height;
  const chartWidth = horizontal ? 600 : data.length * (barWidth + barGap) + 80;
  const padding = { top: 20, right: 20, bottom: 60, left: 60 };
  const drawWidth = chartWidth - padding.left - padding.right;
  const drawHeight = chartHeight - padding.top - padding.bottom;

  // Color palette
  const colors = [
    '#2563EB', '#7C3AED', '#10B981', '#F59E0B', '#EF4444',
    '#06B6D4', '#8B5CF6', '#EC4899', '#F97316', '#14B8A6',
  ];

  const formatValue = (value: number): string => {
    return formatCurrency(value);
  };

  return (
    <div className={`bar-chart bar-chart-${horizontal ? 'horizontal' : 'vertical'} ${className}`}>
      {(title || subtitle) && (
        <div className="bar-chart-header">
          {title && <h4 className="bar-chart-title">{title}</h4>}
          {subtitle && <p className="bar-chart-subtitle">{subtitle}</p>}
        </div>
      )}

      <svg width="100%" height={height} viewBox={`0 0 ${chartWidth} ${chartHeight}`} className="bar-chart-svg">
        {/* Grid lines */}
        {showGrid && (
          <g className="bar-chart-grid">
            {Array.from({ length: 5 }, (_, i) => {
              const value = minValue + ((maxValue - minValue) / 5) * (4 - i);
              const y = padding.top + (i / 5) * drawHeight;
              return (
                <g key={i}>
                  <line
                    x1={padding.left}
                    y1={y}
                    x2={chartWidth - padding.right}
                    y2={y}
                    stroke="#E5E7EB"
                    strokeWidth="1"
                    strokeDasharray="4 4"
                  />
                  <text
                    x={padding.left - 10}
                    y={y}
                    textAnchor="end"
                    dominantBaseline="middle"
                    className="bar-chart-label"
                    fill="#6B7280"
                    fontSize="12"
                  >
                    {formatValue(Math.round(value))}
                  </text>
                </g>
              );
            })}
          </g>
        )}

        {/* Bars */}
        <g className="bar-chart-bars">
          {data.map((d, i) => {
            const barHeight = ((d.value - minValue) / (maxValue - minValue)) * drawHeight;
            const x = padding.left + i * (barWidth + barGap) + (drawWidth - data.length * (barWidth + barGap)) / 2;
            const y = padding.top + drawHeight - barHeight;
            const barColor = d.color || colors[i % colors.length];

            return (
              <g key={i} className="bar-chart-bar-group">
                {/* Target indicator */}
                {showTarget && d.target !== undefined && (
                  <line
                    x1={x}
                    x2={x + barWidth}
                    y1={padding.top + drawHeight - ((d.target - minValue) / (maxValue - minValue)) * drawHeight}
                    y2={padding.top + drawHeight - ((d.target - minValue) / (maxValue - minValue)) * drawHeight}
                    stroke="#9CA3AF"
                    strokeWidth="2"
                    strokeDasharray="4 2"
                  />
                )}

                {/* Bar */}
                <rect
                  x={x}
                  y={y}
                  width={barWidth}
                  height={barHeight}
                  fill={barColor}
                  rx="4"
                  className="bar-chart-bar"
                />

                {/* Value label */}
                {showValues && (
                  <text
                    x={x + barWidth / 2}
                    y={y - 8}
                    textAnchor="middle"
                    className="bar-chart-value"
                    fill="#374151"
                    fontSize="12"
                    fontWeight="600"
                  >
                    {formatValue(d.value)}
                  </text>
                )}

                {/* X-axis label */}
                <text
                  x={x + barWidth / 2}
                  y={chartHeight - padding.bottom + 20}
                  textAnchor="middle"
                  className="bar-chart-label"
                  fill="#6B7280"
                  fontSize="12"
                >
                  {d.label}
                </text>

                {/* Tooltip */}
                <title>{d.label}: {formatValue(d.value)}</title>
              </g>
            );
          })}
        </g>

        {/* Group label */}
        {groupBy && (
          <text
            x={chartWidth / 2}
            y={chartHeight - 10}
            textAnchor="middle"
            className="bar-chart-group-label"
            fill="#9CA3AF"
            fontSize="12"
          >
            {groupBy}
          </text>
        )}
      </svg>
    </div>
  );
}

// Grouped Bar Chart
export interface GroupedBarChartProps {
  groups: string[];
  series: {
    name: string;
    data: number[];
    color?: string;
  }[];
  height?: number;
  showLegend?: boolean;
  className?: string;
  title?: string;
}

export function GroupedBarChart({
  groups,
  series,
  height = 250,
  showLegend = true,
  className = '',
  title,
}: GroupedBarChartProps): React.ReactElement {
  const allValues = series.flatMap(s => s.data);
  const maxValue = Math.max(...allValues) * 1.1;
  const minValue = 0;

  const groupWidth = 100;
  const barWidth = (groupWidth - 20) / series.length;
  const chartWidth = groups.length * groupWidth + 100;
  const padding = { top: 20, right: 20, bottom: 60, left: 60 };
  const drawWidth = chartWidth - padding.left - padding.right;
  const drawHeight = height - padding.top - padding.bottom;

  const colors = [
    '#2563EB', '#7C3AED', '#10B981', '#F59E0B', '#EF4444',
  ];

  return (
    <div className={`bar-chart bar-chart-grouped ${className}`}>
      {title && <h4 className="bar-chart-title">{title}</h4>}

      <svg width="100%" height={height} viewBox={`0 0 ${chartWidth} ${height}`} className="bar-chart-svg">
        {/* Grid */}
        {Array.from({ length: 5 }, (_, i) => {
          const value = minValue + ((maxValue - minValue) / 5) * (4 - i);
          const y = padding.top + (i / 5) * drawHeight;
          return (
            <g key={i}>
              <line
                x1={padding.left}
                y1={y}
                x2={chartWidth - padding.right}
                y2={y}
                stroke="#E5E7EB"
                strokeWidth="1"
                strokeDasharray="4 4"
              />
              <text
                x={padding.left - 10}
                y={y}
                textAnchor="end"
                dominantBaseline="middle"
                className="bar-chart-label"
                fill="#6B7280"
                fontSize="12"
              >
                {formatCurrency(Math.round(value))}
              </text>
            </g>
          );
        })}

        {/* Bars */}
        {groups.map((group, gi) => (
          <g key={gi} transform={`translate(${padding.left + gi * groupWidth}, 0)`}>
            {series.map((s, si) => {
              const barHeight = (s.data[gi] / maxValue) * drawHeight;
              const x = si * barWidth + 10;
              const y = padding.top + drawHeight - barHeight;
              const color = s.color || colors[si % colors.length];

              return (
                <rect
                  key={si}
                  x={x}
                  y={y}
                  width={barWidth - 4}
                  height={barHeight}
                  fill={color}
                  rx="3"
                  className="bar-chart-bar"
                >
                  <title>{s.name}: {formatCurrency(s.data[gi])}</title>
                </rect>
              );
            })}

            {/* Group label */}
            <text
              x={groupWidth / 2}
              y={height - padding.bottom + 20}
              textAnchor="middle"
              className="bar-chart-label"
              fill="#6B7280"
              fontSize="12"
            >
              {group}
            </text>
          </g>
        ))}
      </svg>

      {/* Legend */}
      {showLegend && (
        <div className="bar-chart-legend">
          {series.map((s, i) => (
            <div key={i} className="legend-item">
              <span className="legend-color" style={{ backgroundColor: s.color || colors[i % colors.length] }}></span>
              <span className="legend-label">{s.name}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default BarChart;
