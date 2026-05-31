// BarChart Component
// SVG-based bar chart for categorical data

import React from 'react';
import { formatCompactNumber } from '../../utils/formatters';

export interface BarDataPoint {
  label: string;
  value: number;
  color?: string;
}

export interface BarChartProps {
  data: BarDataPoint[];
  height?: number;
  horizontal?: boolean;
  showGrid?: boolean;
  showValues?: boolean;
  maxBarWidth?: number;
  colors?: string[];
}

export const BarChart: React.FC<BarChartProps> = ({
  data,
  height = 250,
  horizontal = false,
  showGrid = true,
  showValues = true,
  maxBarWidth = 60,
  colors = ['#3B82F6', '#8B5CF6', '#EC4899', '#10B981', '#F59E0B'],
}) => {
  const padding = { top: 20, right: 40, bottom: 60, left: horizontal ? 100 : 60 };
  const chartWidth = horizontal ? 600 : 800;
  const chartHeight = height;

  const maxValue = Math.max(...data.map(d => d.value));
  const chartColors = data.map((d, i) => d.color || colors[i % colors.length]);

  const formatValue = (value: number) => formatCompactNumber(value);

  if (horizontal) {
    const barHeight = Math.min(40, (chartHeight - padding.top - padding.bottom) / data.length - 10);
    const getY = (index: number) => padding.top + index * (barHeight + 10);

    return (
      <div className="bar-chart-container bar-chart--horizontal">
        <svg
          viewBox={`0 0 ${chartWidth} ${chartHeight}`}
          className="bar-chart"
          preserveAspectRatio="xMidYMid meet"
        >
          {showGrid && [0, 0.25, 0.5, 0.75, 1].map((percent, i) => {
            const x = padding.left + (chartWidth - padding.left - padding.right) * percent;
            const value = maxValue * percent;
            return (
              <g key={i}>
                <line
                  x1={x}
                  y1={padding.top}
                  x2={x}
                  y2={chartHeight - padding.bottom}
                  stroke="#E5E7EB"
                  strokeWidth="1"
                  strokeDasharray="4 4"
                />
                <text
                  x={x}
                  y={chartHeight - padding.bottom + 20}
                  textAnchor="middle"
                  fill="#6B7280"
                  fontSize="12"
                >
                  {formatValue(value)}
                </text>
              </g>
            );
          })}

          {data.map((point, i) => {
            const y = getY(i);
            const barWidth = ((point.value / maxValue) * (chartWidth - padding.left - padding.right));
            const x = padding.left;

            return (
              <g key={i} className="bar-group">
                <text
                  x={padding.left - 10}
                  y={y + barHeight / 2}
                  textAnchor="end"
                  dominantBaseline="middle"
                  fill="#374151"
                  fontSize="12"
                  fontWeight="500"
                >
                  {point.label}
                </text>
                <rect
                  x={x}
                  y={y}
                  width={barWidth}
                  height={barHeight}
                  fill={chartColors[i]}
                  rx="4"
                  className="bar-rect"
                >
                  <animate
                    attributeName="width"
                    from="0"
                    to={barWidth}
                    dur="0.5s"
                    fill="freeze"
                  />
                </rect>
                {showValues && point.value > 0 && (
                  <text
                    x={x + barWidth + 8}
                    y={y + barHeight / 2}
                    dominantBaseline="middle"
                    fill="#374151"
                    fontSize="12"
                    fontWeight="600"
                  >
                    {formatValue(point.value)}
                  </text>
                )}
              </g>
            );
          })}
        </svg>
      </div>
    );
  }

  // Vertical bars
  const barWidth = Math.min(
    maxBarWidth,
    (chartWidth - padding.left - padding.right) / data.length - 10
  );
  const getX = (index: number) => padding.left + index * (barWidth + 10) + barWidth / 2;

  return (
    <div className="bar-chart-container">
      <svg
        viewBox={`0 0 ${chartWidth} ${chartHeight}`}
        className="bar-chart"
        preserveAspectRatio="xMidYMid meet"
      >
        {showGrid && [0, 0.25, 0.5, 0.75, 1].map((percent, i) => {
          const y = chartHeight - padding.bottom - (chartHeight - padding.top - padding.bottom) * percent;
          const value = maxValue * percent;
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
                fill="#6B7280"
                fontSize="12"
              >
                {formatValue(value)}
              </text>
            </g>
          );
        })}

        {data.map((point, i) => {
          const x = getX(i) - barWidth / 2;
          const barHeight = ((point.value / maxValue) * (chartHeight - padding.top - padding.bottom));
          const y = chartHeight - padding.bottom - barHeight;

          return (
            <g key={i} className="bar-group">
              <rect
                x={x}
                y={y}
                width={barWidth}
                height={barHeight}
                fill={chartColors[i]}
                rx="4"
                className="bar-rect"
              >
                <animate
                  attributeName="height"
                  from="0"
                  to={barHeight}
                  dur="0.5s"
                  fill="freeze"
                />
                <animate
                  attributeName="y"
                  from={chartHeight - padding.bottom}
                  to={y}
                  dur="0.5s"
                  fill="freeze"
                />
              </rect>
              {showValues && point.value > 0 && (
                <text
                  x={x + barWidth / 2}
                  y={y - 8}
                  textAnchor="middle"
                  fill="#374151"
                  fontSize="12"
                  fontWeight="600"
                >
                  {formatValue(point.value)}
                </text>
              )}
              <text
                x={x + barWidth / 2}
                y={chartHeight - padding.bottom + 20}
                textAnchor="middle"
                fill="#374151"
                fontSize="12"
              >
                {point.label}
              </text>
            </g>
          );
        })}
      </svg>
    </div>
  );
};

export default BarChart;
