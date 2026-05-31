// LineChart Component
// SVG-based line chart for time series data

import React from 'react';
import { formatCompactNumber } from '../../utils/formatters';
import './ChartCard.css';

export interface DataPoint {
  label: string;
  value: number;
  date?: Date;
}

export interface LineChartProps {
  data: DataPoint[];
  height?: number;
  color?: string;
  showArea?: boolean;
  showDots?: boolean;
  showGrid?: boolean;
  showTooltip?: boolean;
  curve?: 'linear' | 'smooth';
  yAxisFormat?: 'number' | 'currency' | 'percentage' | 'compact';
}

export const LineChart: React.FC<LineChartProps> = ({
  data,
  height = 250,
  color = '#3B82F6',
  showArea = true,
  showDots = true,
  showGrid = true,
  curve = 'smooth',
  yAxisFormat = 'compact',
}) => {
  const padding = { top: 20, right: 20, bottom: 40, left: 60 };
  const chartWidth = 800;
  const chartHeight = height;

  const maxValue = Math.max(...data.map(d => d.value));
  const minValue = Math.min(...data.map(d => d.value));
  const range = maxValue - minValue || 1;

  const getX = (index: number) => {
    const availableWidth = chartWidth - padding.left - padding.right;
    return padding.left + (index / (data.length - 1)) * availableWidth;
  };

  const getY = (value: number) => {
    const availableHeight = chartHeight - padding.top - padding.bottom;
    const normalizedValue = (value - minValue) / range;
    return chartHeight - padding.bottom - normalizedValue * availableHeight;
  };

  const formatYValue = (value: number) => {
    switch (yAxisFormat) {
      case 'currency':
        return formatCompactNumber(value);
      case 'percentage':
        return `${value.toFixed(1)}%`;
      case 'number':
        return value.toLocaleString();
      default:
        return formatCompactNumber(value);
    }
  };

  // Generate grid lines
  const gridLines = showGrid ? [0, 0.25, 0.5, 0.75, 1].map(percent => {
    const value = minValue + range * percent;
    const y = getY(value);
    return { y, value, label: formatYValue(value) };
  }) : [];

  // Generate path data
  let pathD = '';
  let areaD = '';

  if (curve === 'smooth') {
    // Catmull-Rom spline for smooth curves
    for (let i = 0; i < data.length; i++) {
      const x = getX(i);
      const y = getY(data[i].value);

      if (i === 0) {
        pathD += `M ${x} ${y}`;
        areaD += `M ${x} ${chartHeight - padding.bottom} L ${x} ${y}`;
      } else {
        const prevX = getX(i - 1);
        const prevY = getY(data[i - 1].value);
        const cp1x = prevX + (x - prevX) * 0.5;
        const cp1y = prevY;
        const cp2x = prevX + (x - prevX) * 0.5;
        const cp2y = y;
        pathD += ` C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${x} ${y}`;
        areaD += ` C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${x} ${y}`;
      }

      if (i === data.length - 1) {
        areaD += ` L ${x} ${chartHeight - padding.bottom} Z`;
      }
    }
  } else {
    for (let i = 0; i < data.length; i++) {
      const x = getX(i);
      const y = getY(data[i].value);

      if (i === 0) {
        pathD += `M ${x} ${y}`;
        areaD += `M ${x} ${chartHeight - padding.bottom} L ${x} ${y}`;
      } else {
        pathD += ` L ${x} ${y}`;
        areaD += ` L ${x} ${y}`;
      }

      if (i === data.length - 1) {
        areaD += ` L ${x} ${chartHeight - padding.bottom} Z`;
      }
    }
  }

  return (
    <div className="line-chart-container">
      <svg
        viewBox={`0 0 ${chartWidth} ${chartHeight}`}
        className="line-chart"
        preserveAspectRatio="xMidYMid meet"
      >
        <defs>
          <linearGradient id={`area-gradient-${color.replace('#', '')}`} x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stopColor={color} stopOpacity="0.3" />
            <stop offset="100%" stopColor={color} stopOpacity="0" />
          </linearGradient>
        </defs>

        {/* Grid lines */}
        {gridLines.map((line, i) => (
          <g key={i}>
            <line
              x1={padding.left}
              y1={line.y}
              x2={chartWidth - padding.right}
              y2={line.y}
              stroke="#E5E7EB"
              strokeWidth="1"
              strokeDasharray="4 4"
            />
            <text
              x={padding.left - 10}
              y={line.y}
              textAnchor="end"
              dominantBaseline="middle"
              fill="#6B7280"
              fontSize="12"
            >
              {line.label}
            </text>
          </g>
        ))}

        {/* X-axis labels */}
        {data.map((point, i) => {
          const x = getX(i);
          const showLabel = data.length <= 12 || i % Math.ceil(data.length / 6) === 0;
          return (
            <text
              key={i}
              x={x}
              y={chartHeight - padding.bottom + 20}
              textAnchor="middle"
              fill="#6B7280"
              fontSize="12"
              style={{ display: showLabel ? 'block' : 'none' }}
            >
              {point.label}
            </text>
          );
        })}

        {/* Area fill */}
        {showArea && (
          <path
            d={areaD}
            fill={`url(#area-gradient-${color.replace('#', '')})`}
            stroke="none"
          />
        )}

        {/* Line */}
        <path
          d={pathD}
          fill="none"
          stroke={color}
          strokeWidth="3"
          strokeLinecap="round"
          strokeLinejoin="round"
        />

        {/* Dots */}
        {showDots && data.map((point, i) => {
          const x = getX(i);
          const y = getY(point.value);
          return (
            <circle
              key={i}
              cx={x}
              cy={y}
              r="4"
              fill={color}
              stroke="white"
              strokeWidth="2"
            />
          );
        })}
      </svg>
    </div>
  );
};

export default LineChart;
