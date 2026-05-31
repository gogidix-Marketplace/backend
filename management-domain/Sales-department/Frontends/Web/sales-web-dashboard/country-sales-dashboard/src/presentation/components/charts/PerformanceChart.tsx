// PerformanceChart Component
// Team performance visualization using SVG

import React from 'react';

export interface PerformanceDataPoint {
  label: string;
  value: number;
  target?: number;
  color?: string;
}

export interface PerformanceChartProps {
  data: PerformanceDataPoint[];
  type?: 'bar' | 'line' | 'area';
  showTarget?: boolean;
  showLegend?: boolean;
  height?: number;
  className?: string;
  title?: string;
}

export const PerformanceChart: React.FC<PerformanceChartProps> = ({
  data,
  type = 'bar',
  showTarget = true,
  showLegend = true,
  height = 200,
  className = '',
  title,
}) => {
  const maxValue = Math.max(...data.map(d => Math.max(d.value, d.target || 0))) * 1.1;
  const chartWidth = 100;
  const chartHeight = 100;
  const barWidth = (chartWidth / data.length) * 0.6;
  const gap = (chartWidth / data.length) * 0.4;

  const getColor = (index: number) => {
    const colors = ['#2563EB', '#7C3AED', '#10B981', '#F59E0B', '#EF4444'];
    return data[index]?.color || colors[index % colors.length];
  };

  const getTargetY = (target: number) => chartHeight - (target / maxValue) * chartHeight;
  const getValueY = (value: number) => chartHeight - (value / maxValue) * chartHeight;

  return (
    <div className={`performance-chart performance-chart-${type} ${className}`}>
      {title && <h3 className="performance-chart-title">{title}</h3>}

      <svg
        viewBox={`0 0 ${chartWidth + 40} ${chartHeight + 40}`}
        className="performance-chart-svg"
        style={{ height: `${height}px` }}
      >
        {/* Y-axis grid lines */}
        {[0, 25, 50, 75, 100].map(percent => (
          <g key={percent}>
            <line
              x1={30}
              y1={(chartHeight * percent) / 100 + 20}
              x2={chartWidth + 35}
              y2={(chartHeight * percent) / 100 + 20}
              stroke="#E5E7EB"
              strokeWidth="0.5"
              strokeDasharray={percent === 0 ? '0' : '3,3'}
            />
            <text
              x={25}
              y={(chartHeight * percent) / 100 + 23}
              fontSize="8"
              fill="#6B7280"
              textAnchor="end"
            >
              {Math.round((maxValue * percent) / 100 / 1000)}K
            </text>
          </g>
        ))}

        {data.map((point, index) => {
          const x = 35 + index * (barWidth + gap) + gap / 2;
          const y = getValueY(point.value);
          const barHeight = chartHeight - y;
          const targetY = point.target ? getTargetY(point.target) : null;
          const color = getColor(index);
          const isBelowTarget = point.target && point.value < point.target;

          return (
            <g key={point.label}>
              {type === 'bar' && (
                <>
                  {/* Target line */}
                  {showTarget && targetY && (
                    <line
                      x1={x}
                      y1={targetY + 20}
                      x2={x + barWidth}
                      y2={targetY + 20}
                      stroke="#9CA3AF"
                      strokeWidth="1"
                      strokeDasharray="2,2"
                    />
                  )}

                  {/* Bar */}
                  <rect
                    x={x}
                    y={y + 20}
                    width={barWidth}
                    height={barHeight}
                    fill={color}
                    opacity={isBelowTarget ? '0.6' : '1'}
                    rx="2"
                  />
                </>
              )}

              {type === 'line' && (
                <>
                  {index > 0 && (
                    <line
                      x1={35 + (index - 1) * (barWidth + gap) + gap / 2 + barWidth / 2}
                      y1={getValueY(data[index - 1].value) + 20}
                      x2={x + barWidth / 2}
                      y2={y + 20}
                      stroke={color}
                      strokeWidth="2"
                    />
                  )}
                  <circle
                    cx={x + barWidth / 2}
                    cy={y + 20}
                    r="3"
                    fill={color}
                  />
                </>
              )}

              {type === 'area' && (
                <>
                  {index > 0 && (
                    <polygon
                      points={`
                        ${35 + (index - 1) * (barWidth + gap) + gap / 2 + barWidth / 2},
                        ${chartHeight + 20}
                        ${35 + (index - 1) * (barWidth + gap) + gap / 2 + barWidth / 2},
                        ${getValueY(data[index - 1].value) + 20}
                        ${x + barWidth / 2},
                        ${y + 20}
                        ${x + barWidth / 2},
                        ${chartHeight + 20}
                      `}
                      fill={color}
                      opacity="0.2"
                    />
                  )}
                  <circle
                    cx={x + barWidth / 2}
                    cy={y + 20}
                    r="3"
                    fill={color}
                  />
                </>
              )}

              {/* X-axis label */}
              <text
                x={x + barWidth / 2}
                y={chartHeight + 32}
                fontSize="7"
                fill="#6B7280"
                textAnchor="middle"
              >
                {point.label.slice(0, 8)}
              </text>

              {/* Value label */}
              <text
                x={x + barWidth / 2}
                y={y + 15}
                fontSize="7"
                fill={color}
                textAnchor="middle"
                fontWeight="bold"
              >
                {(point.value / 1000).toFixed(1)}K
              </text>
            </g>
          );
        })}
      </svg>

      {showLegend && (
        <div className="performance-chart-legend">
          {data.map((point, index) => (
            <div key={point.label} className="legend-item">
              <span
                className="legend-color"
                style={{ backgroundColor: getColor(index) }}
              />
              <span className="legend-label">{point.label}</span>
              {point.target && (
                <span className="legend-target">
                  Target: {(point.target / 1000).toFixed(0)}K
                </span>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PerformanceChart;
