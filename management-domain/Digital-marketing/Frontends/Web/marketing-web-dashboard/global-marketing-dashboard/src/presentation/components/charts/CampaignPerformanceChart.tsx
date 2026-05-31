// CampaignPerformanceChart Component
// Displays campaign performance metrics over time

import './CampaignPerformanceChart.css';

interface DataPoint {
  date: string;
  impressions: number;
  clicks: number;
  conversions: number;
  cost: number;
  revenue: number;
}

interface CampaignPerformanceChartProps {
  data: DataPoint[];
  metrics?: Array<'impressions' | 'clicks' | 'conversions' | 'cost' | 'revenue'>;
  height?: number;
  className?: string;
}

const metricColors = {
  impressions: '#6366f1',
  clicks: '#10b981',
  conversions: '#f59e0b',
  cost: '#ef4444',
  revenue: '#8b5cf6',
};

export function CampaignPerformanceChart({
  data,
  metrics = ['impressions', 'clicks', 'conversions'],
  height = 300,
  className = '',
}: CampaignPerformanceChartProps) {
  const maxValue = Math.max(
    ...data.flatMap((d) => metrics.map((m) => d[m] || 0))
  );

  const chartHeight = height - 40; // Space for labels
  const chartWidth = data.length * 60;
  const barWidth = 40;
  const groupWidth = chartWidth / data.length;

  return (
    <div className={`campaign-performance-chart ${className}`} style={{ height }}>
      <div className="chart-legend">
        {metrics.map((metric) => (
          <div key={metric} className="legend-item">
            <span
              className="legend-color"
              style={{ backgroundColor: metricColors[metric] }}
            />
            <span className="legend-label">{metric}</span>
          </div>
        ))}
      </div>
      <div className="chart-container" style={{ height: chartHeight }}>
        <svg width="100%" height="100%" viewBox={`0 0 ${chartWidth} ${chartHeight}`}>
          {/* Grid lines */}
          {[0, 0.25, 0.5, 0.75, 1].map((percent) => (
            <line
              key={percent}
              x1="0"
              y1={chartHeight * (1 - percent)}
              x2={chartWidth}
              y2={chartHeight * (1 - percent)}
              stroke="#e5e7eb"
              strokeDasharray="4"
            />
          ))}

          {/* Data bars */}
          {data.map((point, index) => {
            const x = index * groupWidth + (groupWidth - barWidth * metrics.length) / 2;

            return metrics.map((metric, metricIndex) => {
              const value = point[metric] || 0;
              const barHeight = maxValue > 0 ? (value / maxValue) * chartHeight * 0.9 : 0;
              const y = chartHeight - barHeight;
              const barX = x + metricIndex * barWidth;

              return (
                <g key={`${index}-${metric}`}>
                  <rect
                    x={barX}
                    y={y}
                    width={barWidth - 4}
                    height={barHeight}
                    fill={metricColors[metric]}
                    rx="2"
                    className="chart-bar"
                  />
                  <title>
                    {point.date}: {metric} - {value.toLocaleString()}
                  </title>
                </g>
              );
            });
          })}

          {/* X-axis labels */}
          {data
            .filter((_, index) => index % Math.ceil(data.length / 6) === 0)
            .map((point, index) => {
              const x = index * Math.ceil(data.length / 6) * groupWidth + groupWidth / 2;
              return (
                <text
                  key={index}
                  x={x}
                  y={chartHeight + 15}
                  textAnchor="middle"
                  fontSize="12"
                  fill="#6b7280"
                >
                  {new Date(point.date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}
                </text>
              );
            })}
        </svg>
      </div>
    </div>
  );
}
