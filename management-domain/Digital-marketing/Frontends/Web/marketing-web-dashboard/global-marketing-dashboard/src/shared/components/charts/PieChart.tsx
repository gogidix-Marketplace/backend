// PieChart Component
// SVG-based pie/donut chart

import React from 'react';
import { formatCompactNumber, formatPercentage } from '../../utils/formatters';

export interface PieDataPoint {
  label: string;
  value: number;
  color?: string;
}

export interface PieChartProps {
  data: PieDataPoint[];
  size?: number;
  donut?: boolean;
  innerRadius?: number;
  showLegend?: boolean;
  showLabels?: boolean;
  colors?: string[];
}

export const PieChart: React.FC<PieChartProps> = ({
  data,
  size = 300,
  donut = false,
  innerRadius = 0.5,
  showLegend = true,
  showLabels = true,
  colors = ['#3B82F6', '#8B5CF6', '#EC4899', '#10B981', '#F59E0B', '#EF4444', '#06B6D4', '#84CC16'],
}) => {
  const total = data.reduce((sum, d) => sum + d.value, 0);
  const radius = Math.min(size, 400) / 2;
  const innerR = donut ? radius * innerRadius : 0;
  const chartColors = data.map((d, i) => d.color || colors[i % colors.length]);

  const getCoordinates = (percent: number) => {
    const x = radius * Math.cos(2 * Math.PI * percent);
    const y = radius * Math.sin(2 * Math.PI * percent);
    return [x, y];
  };

  let currentPercent = 0;
  const slices = data.map((point, i) => {
    const percent = point.value / total;
    const startPercent = currentPercent;
    const endPercent = currentPercent + percent;
    currentPercent = endPercent;

    const [startX, startY] = getCoordinates(startPercent);
    const [endX, endY] = getCoordinates(endPercent);
    const largeArc = percent > 0.5 ? 1 : 0;

    let pathData: string;
    if (donut) {
      const [innerStartX, innerStartY] = [innerR * Math.cos(2 * Math.PI * startPercent), innerR * Math.sin(2 * Math.PI * startPercent)];
      const [innerEndX, innerEndY] = [innerR * Math.cos(2 * Math.PI * endPercent), innerR * Math.sin(2 * Math.PI * endPercent)];

      pathData = [
        `M ${startX} ${startY}`,
        `A ${radius} ${radius} 0 ${largeArc} 1 ${endX} ${endY}`,
        `L ${innerEndX} ${innerEndY}`,
        `A ${innerR} ${innerR} 0 ${largeArc} 0 ${innerStartX} ${innerStartY}`,
        'Z',
      ].join(' ');
    } else {
      pathData = [
        `M 0 0`,
        `L ${startX} ${startY}`,
        `A ${radius} ${radius} 0 ${largeArc} 1 ${endX} ${endY}`,
        'Z',
      ].join(' ');
    }

    return {
      ...point,
      color: chartColors[i],
      pathData,
      percent,
      midPercent: startPercent + percent / 2,
    };
  });

  const getLabelPosition = (percent: number, labelRadius: number) => {
    const x = labelRadius * Math.cos(2 * Math.PI * percent);
    const y = labelRadius * Math.sin(2 * Math.PI * percent);
    return [x, y];
  };

  return (
    <div className="pie-chart-container">
      <svg
        viewBox={`${-radius} ${-radius} ${size} ${size}`}
        className="pie-chart"
        style={{ maxWidth: size }}
      >
        <g transform="rotate(-90)">
          {slices.map((slice, i) => (
            <g key={i} className="pie-slice">
              <path
                d={slice.pathData}
                fill={slice.color}
                stroke="white"
                strokeWidth="2"
                className="pie-slice-path"
              >
                <animate
                  attributeName="opacity"
                  from="0"
                  to="1"
                  dur="0.3s"
                  fill="freeze"
                />
              </path>
              {showLabels && slice.percent > 0.05 && (
                <text
                  x={getLabelPosition(slice.midPercent, (radius + innerR) / 2)[0]}
                  y={getLabelPosition(slice.midPercent, (radius + innerR) / 2)[1]}
                  textAnchor="middle"
                  dominantBaseline="middle"
                  fill="white"
                  fontSize="12"
                  fontWeight="600"
                  transform={`rotate(90 ${getLabelPosition(slice.midPercent, (radius + innerR) / 2).join(' ')})`}
                >
                  {formatPercentage(slice.percent * 100)}
                </text>
              )}
            </g>
          ))}
        </g>

        {/* Center text for donut */}
        {donut && (
          <text
            x="0"
            y="0"
            textAnchor="middle"
            dominantBaseline="middle"
            className="pie-center-text"
          >
            <tspan x="0" dy="-0.5em" className="pie-center-value">
              {formatCompactNumber(total)}
            </tspan>
            <tspan x="0" dy="1.2em" className="pie-center-label">
              Total
            </tspan>
          </text>
        )}
      </svg>

      {showLegend && (
        <div className="pie-legend">
          {slices.map((slice, i) => (
            <div key={i} className="pie-legend-item">
              <div
                className="pie-legend-color"
                style={{ backgroundColor: slice.color }}
              />
              <span className="pie-legend-label">{slice.label}</span>
              <span className="pie-legend-value">
                {formatCompactNumber(slice.value)} ({formatPercentage(slice.percent * 100)})
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PieChart;
