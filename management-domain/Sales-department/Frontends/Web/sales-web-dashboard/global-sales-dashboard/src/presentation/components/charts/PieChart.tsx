// PieChart Component
// Displays data distribution as a pie chart using SVG

import type React from 'react';
import { formatCurrency } from '@shared/utils';
import './PieChart.css';

export interface PieDataPoint {
  label: string;
  value: number;
  color?: string;
}

export interface PieChartProps {
  data: PieDataPoint[];
  size?: number;
  innerRadius?: number;
  showLabels?: boolean;
  showLegend?: boolean;
  donut?: boolean;
  className?: string;
  title?: string;
}

export function PieChart({
  data,
  size = 200,
  innerRadius = 60,
  showLabels = true,
  showLegend = true,
  donut = false,
  className = '',
  title,
}: PieChartProps): React.ReactElement {
  if (data.length === 0) {
    return (
      <div className={`pie-chart pie-chart-empty ${className}`} style={{ width: size, height: size }}>
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
          <circle cx="12" cy="12" r="10" />
        </svg>
        <p>No data</p>
      </div>
    );
  }

  const total = data.reduce((sum, d) => sum + d.value, 0);
  const colors = [
    '#2563EB', '#7C3AED', '#10B981', '#F59E0B', '#EF4444',
    '#06B6D4', '#8B5CF6', '#EC4899', '#F97316', '#14B8A6',
  ];

  const radius = size / 2 - 10;
  const center = size / 2;
  const actualInnerRadius = donut ? innerRadius : 0;

  // Calculate slices
  let startAngle = -90;
  const slices = data.map((d, i) => {
    const angle = (d.value / total) * 360;
    const endAngle = startAngle + angle;
    const largeArcFlag = angle > 180 ? 1 : 0;

    // Convert to radians
    const startRad = (startAngle * Math.PI) / 180;
    const endRad = (endAngle * Math.PI) / 180;

    // Calculate coordinates
    const x1 = center + radius * Math.cos(startRad);
    const y1 = center + radius * Math.sin(startRad);
    const x2 = center + radius * Math.cos(endRad);
    const y2 = center + radius * Math.sin(endRad);

    // Inner circle coordinates for donut
    const x1Inner = center + actualInnerRadius * Math.cos(startRad);
    const y1Inner = center + actualInnerRadius * Math.sin(startRad);
    const x2Inner = center + actualInnerRadius * Math.cos(endRad);
    const y2Inner = center + actualInnerRadius * Math.sin(endRad);

    const sliceColor = d.color || colors[i % colors.length];

    const path = donut
      ? `M ${x1} ${y1} A ${radius} ${radius} 0 ${largeArcFlag} 1 ${x2} ${y2} L ${x2Inner} ${y2Inner} A ${actualInnerRadius} ${actualInnerRadius} 0 ${largeArcFlag} 0 ${x1Inner} ${y1Inner} Z`
      : `M ${center} ${center} L ${x1} ${y1} A ${radius} ${radius} 0 ${largeArcFlag} 1 ${x2} ${y2} Z`;

    // Label position
    const labelAngle = startAngle + angle / 2;
    const labelRad = ((labelAngle * Math.PI) / 180);
    const labelRadius = radius * 0.7;
    const labelX = center + labelRadius * Math.cos(labelRad);
    const labelY = center + labelRadius * Math.sin(labelRad);

    startAngle = endAngle;

    return {
      path,
      color: sliceColor,
      labelX,
      labelY,
      percentage: ((d.value / total) * 100).toFixed(1),
      value: d.value,
      name: d.label,
    };
  });

  return (
    <div className={`pie-chart ${donut ? 'pie-chart-donut' : ''} ${className}`}>
      {title && <h4 className="pie-chart-title">{title}</h4>}

      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} className="pie-chart-svg">
        {slices.map((slice, i) => (
          <g key={i} className="pie-chart-slice">
            <path
              d={slice.path}
              fill={slice.color}
              stroke="white"
              strokeWidth="2"
              className="pie-chart-slice-path"
            />
            {showLabels && parseFloat(slice.percentage) > 5 && (
              <text
                x={slice.labelX}
                y={slice.labelY}
                textAnchor="middle"
                dominantBaseline="middle"
                className="pie-chart-label"
                fill="white"
                fontSize="12"
                fontWeight="600"
              >
                {slice.percentage}%
              </text>
            )}
            <title>{slice.name}: {formatCurrency(slice.value)} ({slice.percentage}%)</title>
          </g>
        ))}

        {/* Center text for donut */}
        {donut && (
          <text
            x={center}
            y={center}
            textAnchor="middle"
            dominantBaseline="middle"
            className="pie-chart-center-label"
            fill="#374151"
            fontSize="14"
            fontWeight="600"
          >
            {formatCurrency(total)}
          </text>
        )}
      </svg>

      {/* Legend */}
      {showLegend && (
        <div className="pie-chart-legend">
          {data.map((d, i) => (
            <div key={i} className="legend-item">
              <span
                className="legend-color"
                style={{ backgroundColor: d.color || colors[i % colors.length] }}
              ></span>
              <span className="legend-label">{d.label}</span>
              <span className="legend-value">{formatCurrency(d.value)}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default PieChart;
