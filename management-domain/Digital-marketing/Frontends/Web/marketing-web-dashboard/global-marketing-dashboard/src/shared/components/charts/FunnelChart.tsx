// FunnelChart Component
// SVG-based funnel chart for conversion tracking

import React from 'react';
import { formatCompactNumber, formatPercentage } from '../../utils/formatters';

export interface FunnelDataPoint {
  stage: string;
  value: number;
  label?: string;
}

export interface FunnelChartProps {
  data: FunnelDataPoint[];
  height?: number;
  colors?: string[];
  showPercentages?: boolean;
  showValues?: boolean;
}

export const FunnelChart: React.FC<FunnelChartProps> = ({
  data,
  height = 350,
  colors = ['#3B82F6', '#6366F1', '#8B5CF6', '#A855F7', '#D946EF'],
  showPercentages = true,
  showValues = true,
}) => {
  const maxValue = data[0]?.value || 1;
  const chartColors = data.map((d, i) => colors[i % colors.length]);

  const calculateFunnelWidth = (value: number, index: number) => {
    const baseWidth = 100;
    const funnelWidth = (value / maxValue) * 80;
    const offset = (baseWidth - funnelWidth) / 2;
    return { baseWidth, funnelWidth, offset };
  };

  return (
    <div className="funnel-chart-container">
      <svg
        viewBox="0 0 400 400"
        className="funnel-chart"
        preserveAspectRatio="xMidYMid meet"
      >
        <defs>
          {data.map((_, i) => (
            <linearGradient key={i} id={`funnel-gradient-${i}`} x1="0%" y1="0%" x2="100%" y2="0%">
              <stop offset="0%" stopColor={chartColors[i]} stopOpacity="0.6" />
              <stop offset="50%" stopColor={chartColors[i]} stopOpacity="0.8" />
              <stop offset="100%" stopColor={chartColors[i]} stopOpacity="0.6" />
            </linearGradient>
          ))}
        </defs>

        {data.map((point, i) => {
          const { baseWidth, funnelWidth, offset } = calculateFunnelWidth(point.value, i);
          const y = i * 60 + 20;
          const stageHeight = 50;
          const dropOff = i > 0 ? ((data[i - 1].value - point.value) / data[i - 1].value) * 100 : 0;

          // Trapezoid path
          const nextWidth = i < data.length - 1
            ? calculateFunnelWidth(data[i + 1].value, i + 1).funnelWidth
            : funnelWidth * 0.6;
          const nextOffset = (baseWidth - nextWidth) / 2;

          const pathData = [
            `M ${offset} ${y}`,
            `L ${offset + funnelWidth} ${y}`,
            `L ${nextOffset + nextWidth} ${y + stageHeight}`,
            `L ${nextOffset} ${y + stageHeight}`,
            'Z',
          ].join(' ');

          return (
            <g key={i} className="funnel-stage">
              <path
                d={pathData}
                fill={`url(#funnel-gradient-${i})`}
                stroke={chartColors[i]}
                strokeWidth="1"
                className="funnel-stage-path"
              />

              {/* Stage label */}
              <text
                x="10"
                y={y + stageHeight / 2}
                dominantBaseline="middle"
                fill="#374151"
                fontSize="11"
                fontWeight="500"
              >
                {point.label || point.stage}
              </text>

              {/* Value */}
              {showValues && (
                <text
                  x="130"
                  y={y + stageHeight / 2}
                  dominantBaseline="middle"
                  fill="#374151"
                  fontSize="11"
                  fontWeight="600"
                >
                  {formatCompactNumber(point.value)}
                </text>
              )}

              {/* Percentage of original */}
              {showPercentages && (
                <text
                  x="180"
                  y={y + stageHeight / 2}
                  dominantBaseline="middle"
                  fill="#6B7280"
                  fontSize="11"
                >
                  {formatPercentage((point.value / maxValue) * 100)}
                </text>
              )}

              {/* Drop-off */}
              {i > 0 && dropOff > 0 && (
                <text
                  x="240"
                  y={y + stageHeight / 2}
                  dominantBaseline="middle"
                  fill="#EF4444"
                  fontSize="11"
                >
                  -{formatPercentage(dropOff)}
                </text>
              )}
            </g>
          );
        })}
      </svg>

      {/* Legend */}
      <div className="funnel-legend">
        <div className="funnel-legend-row">
          <span className="funnel-legend-label">Stage</span>
          <span className="funnel-legend-label">Value</span>
          <span className="funnel-legend-label">Conversion</span>
          {showPercentages && <span className="funnel-legend-label">Drop-off</span>}
        </div>
        {data.map((point, i) => {
          const dropOff = i > 0 ? ((data[i - 1].value - point.value) / data[i - 1].value) * 100 : 0;
          return (
            <div key={i} className="funnel-legend-row">
              <div className="funnel-legend-stage">
                <div
                  className="funnel-legend-color"
                  style={{ backgroundColor: chartColors[i] }}
                />
                <span>{point.label || point.stage}</span>
              </div>
              <span className="funnel-legend-value">{formatCompactNumber(point.value)}</span>
              <span className="funnel-legend-value">
                {formatPercentage((point.value / maxValue) * 100)}
              </span>
              {showPercentages && (
                <span className={`funnel-legend-dropoff ${dropOff > 10 ? 'dropoff-high' : ''}`}>
                  {i > 0 ? `-${formatPercentage(dropOff)}` : '—'}
                </span>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default FunnelChart;
