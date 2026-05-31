// LeadSourceChart Component
// Displays lead distribution by source

import './LeadSourceChart.css';

interface LeadSourceData {
  source: string;
  count: number;
  percentage: number;
  color?: string;
}

interface LeadSourceChartProps {
  data: LeadSourceData[];
  type?: 'pie' | 'bar' | 'horizontal';
  height?: number;
  className?: string;
}

const defaultColors = [
  '#6366f1', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6',
  '#ec4899', '#06b6d4', '#84cc16', '#f97316', '#6366f1'
];

export function LeadSourceChart({
  data,
  type = 'pie',
  height = 300,
  className = '',
}: LeadSourceChartProps) {
  const totalLeads = data.reduce((sum, d) => sum + d.count, 0);

  if (type === 'bar') {
    return renderVerticalBarChart(data, height, className);
  }

  if (type === 'horizontal') {
    return renderHorizontalBarChart(data, height, className);
  }

  return renderPieChart(data, totalLeads, height, className);
}

function renderPieChart(
  data: LeadSourceData[],
  totalLeads: number,
  height: number,
  className: string
) {
  const radius = Math.min(height, 300) / 2 - 10;
  const center = radius + 10;
  const circumference = 2 * Math.PI * radius;

  let currentOffset = 0;

  return (
    <div className={`lead-source-chart lead-source-pie ${className}`} style={{ height }}>
      <svg width="100%" height={height} viewBox={`0 0 ${center * 2} ${center * 2}`}>
        {data.map((item, index) => {
          const percentage = item.count / totalLeads;
          const strokeDasharray = `${percentage * circumference} ${circumference}`;
          const offset = -currentOffset;
          currentOffset += percentage * circumference;

          const color = item.color || defaultColors[index % defaultColors.length];

          return (
            <g key={index}>
              <circle
                cx={center}
                cy={center}
                r={radius}
                fill="none"
                stroke={color}
                strokeWidth="40"
                strokeDasharray={strokeDasharray}
                strokeDashoffset={offset}
                transform={`rotate(-90 ${center} ${center})`}
                className="chart-segment"
              />
              {/* Label for larger segments */}
              {percentage > 0.05 && (
                <text
                  x={center + (radius / 2) * Math.cos((currentOffset - percentage * Math.PI) * 2 - Math.PI / 2)}
                  y={center + (radius / 2) * Math.sin((currentOffset - percentage * Math.PI) * 2 - Math.PI / 2)}
                  textAnchor="middle"
                  dominantBaseline="middle"
                  fontSize="11"
                  fill="white"
                  fontWeight="600"
                >
                  {percentage > 0.08 ? `${(percentage * 100).toFixed(0)}%` : ''}
                </text>
              )}
            </g>
          );
        })}
      </svg>
      <div className="chart-legend">
        {data.map((item, index) => (
          <div key={index} className="legend-item">
            <span
              className="legend-dot"
              style={{ backgroundColor: item.color || defaultColors[index % defaultColors.length] }}
            />
            <span className="legend-label">{item.source}</span>
            <span className="legend-count">{item.count.toLocaleString()}</span>
            <span className="legend-percentage">{item.percentage.toFixed(1)}%</span>
          </div>
        ))}
      </div>
    </div>
  );
}

function renderVerticalBarChart(
  data: LeadSourceData[],
  height: number,
  className: string
) {
  const maxValue = Math.max(...data.map((d) => d.count));
  const chartHeight = height - 50;

  return (
    <div className={`lead-source-chart lead-source-bar ${className}`} style={{ height }}>
      <svg width="100%" height="100%" preserveAspectRatio="none">
        {data.map((item, index) => {
          const x = index * (100 / data.length);
          const barHeight = (item.count / maxValue) * chartHeight * 0.9;
          const y = chartHeight - barHeight;
          const barWidth = 70 / data.length;

          return (
            <g key={index}>
              <rect
                x={`${x + (100 / data.length - barWidth) / 2}%`}
                y={y}
                width={`${barWidth}%`}
                height={barHeight}
                fill={item.color || defaultColors[index % defaultColors.length]}
                rx="4"
                className="chart-bar"
              />
              <text
                x={`${x + 50 / data.length}%`}
                y={y - 5}
                textAnchor="middle"
                fontSize="12"
                fill="#374151"
                fontWeight="600"
              >
                {item.count.toLocaleString()}
              </text>
              <text
                x={`${x + 50 / data.length}%`}
                y={chartHeight + 15}
                textAnchor="middle"
                fontSize="11"
                fill="#6b7280"
              >
                {item.source.length > 10 ? item.source.substring(0, 8) + '...' : item.source}
              </text>
            </g>
          );
        })}
      </svg>
    </div>
  );
}

function renderHorizontalBarChart(
  data: LeadSourceData[],
  height: number,
  className: string
) {
  const maxValue = Math.max(...data.map((d) => d.count));
  const rowHeight = 40;
  const chartWidth = 100;

  return (
    <div className={`lead-source-chart lead-source-horizontal ${className}`} style={{ height }}>
      <div className="chart-rows">
        {data.map((item, index) => {
          const barWidth = (item.count / maxValue) * 70;

          return (
            <div key={index} className="chart-row">
              <div className="row-label">{item.source}</div>
              <div className="row-bar-container">
                <div
                  className="row-bar"
                  style={{
                    width: `${barWidth}%`,
                    backgroundColor: item.color || defaultColors[index % defaultColors.length]
                  }}
                />
              </div>
              <div className="row-value">{item.count.toLocaleString()}</div>
              <div className="row-percentage">{item.percentage.toFixed(1)}%</div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
