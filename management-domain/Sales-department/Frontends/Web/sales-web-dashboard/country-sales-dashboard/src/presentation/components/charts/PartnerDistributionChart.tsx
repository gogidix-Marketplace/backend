// PartnerDistributionChart Component
// Partner type distribution using donut chart

import React from 'react';
import { PARTNER_TYPES } from '@shared/constants';

export interface PartnerDistributionData {
  type: keyof typeof PARTNER_TYPES;
  count: number;
  color?: string;
}

export interface PartnerDistributionChartProps {
  data: PartnerDistributionData[];
  size?: 'sm' | 'md' | 'lg';
  showLegend?: boolean;
  showLabels?: boolean;
  className?: string;
  title?: string;
}

export const PartnerDistributionChart: React.FC<PartnerDistributionChartProps> = ({
  data,
  size = 'md',
  showLegend = true,
  showLabels = true,
  className = '',
  title,
}) => {
  const total = data.reduce((sum, item) => sum + item.count, 0);
  const sizes = { sm: 120, md: 180, lg: 240 };
  const chartSize = sizes[size];

  const slices = data.map((item, index) => {
    const percentage = (item.count / total) * 100;
    const startAngle = data.slice(0, index).reduce((sum, prev) => sum + (prev.count / total) * 360, 0);
    const endAngle = startAngle + (item.count / total) * 360;
    const color = item.color || PARTNER_TYPES[item.type]?.color || '#6B7280';

    return { ...item, percentage, startAngle, endAngle, color };
  });

  const getCoordinates = (angle: number, radius: number) => {
    const radian = (angle - 90) * (Math.PI / 180);
    return {
      x: 50 + radius * Math.cos(radian),
      y: 50 + radius * Math.sin(radian),
    };
  };

  const describeArc = (startAngle: number, endAngle: number, innerRadius: number, outerRadius: number) => {
    const start = getCoordinates(startAngle, outerRadius);
    const end = getCoordinates(endAngle, outerRadius);
    const startInner = getCoordinates(startAngle, innerRadius);
    const endInner = getCoordinates(endAngle, innerRadius);

    const largeArcFlag = endAngle - startAngle > 180 ? 1 : 0;

    return `
      M ${startInner.x} ${startInner.y}
      L ${start.x} ${start.y}
      A ${outerRadius} ${outerRadius} 0 ${largeArcFlag} 1 ${end.x} ${end.y}
      L ${endInner.x} ${endInner.y}
      A ${innerRadius} ${innerRadius} 0 ${largeArcFlag} 0 ${startInner.x} ${startInner.y}
      Z
    `;
  };

  return (
    <div className={`partner-distribution-chart partner-distribution-chart-${size} ${className}`}>
      {title && <h3 className="chart-title">{title}</h3>}

      <div className="chart-container">
        <svg
          viewBox="0 0 100 100"
          className="chart-svg"
          style={{ width: `${chartSize}px`, height: `${chartSize}px` }}
        >
          {slices.map((slice, index) => {
            const pathData = describeArc(slice.startAngle, slice.endAngle, 35, 48);

            return (
              <g key={slice.type}>
                <path
                  d={pathData}
                  fill={slice.color}
                  stroke="#fff"
                  strokeWidth="0.5"
                  className="chart-slice"
                />
                {showLabels && slice.percentage > 5 && (
                  <text
                    x={getCoordinates(
                      (slice.startAngle + slice.endAngle) / 2,
                      41.5
                    ).x}
                    y={getCoordinates(
                      (slice.startAngle + slice.endAngle) / 2,
                      41.5
                    ).y}
                    fontSize="4"
                    fill="#fff"
                    textAnchor="middle"
                    dominantBaseline="middle"
                  >
                    {`${Math.round(slice.percentage)}%`}
                  </text>
                )}
              </g>
            );
          })}

          {/* Center text */}
          <text x="50" y="47" fontSize="8" fill="#374151" textAnchor="middle" fontWeight="bold">
            {total}
          </text>
          <text x="50" y="54" fontSize="4" fill="#6B7280" textAnchor="middle">
            Partners
          </text>
        </svg>

        {showLegend && (
          <div className="chart-legend">
            {slices.map(slice => (
              <div key={slice.type} className="legend-item">
                <span
                  className="legend-dot"
                  style={{ backgroundColor: slice.color }}
                />
                <span className="legend-label">
                  {PARTNER_TYPES[slice.type]?.label || slice.type}
                </span>
                <span className="legend-count">{slice.count}</span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default PartnerDistributionChart;
