// BudgetChart Component
// Displays budget allocation and spending

import './BudgetChart.css';

interface BudgetData {
  category: string;
  allocated: number;
  spent: number;
  remaining: number;
  currency?: string;
}

interface BudgetChartProps {
  data: BudgetData[];
  type?: 'bar' | 'donut';
  height?: number;
  className?: string;
}

export function BudgetChart({
  data,
  type = 'bar',
  height = 300,
  className = '',
}: BudgetChartProps) {
  const totalAllocated = data.reduce((sum, d) => sum + d.allocated, 0);
  const totalSpent = data.reduce((sum, d) => sum + d.spent, 0);

  if (type === 'donut') {
    return renderDonutChart(data, totalAllocated, totalSpent, height, className);
  }

  return renderBarChart(data, totalAllocated, height, className);
}

function renderBarChart(
  data: BudgetData[],
  totalAllocated: number,
  height: number,
  className: string
) {
  const maxValue = Math.max(...data.map((d) => d.allocated));
  const chartHeight = height - 60;
  const barWidth = Math.min(60, (800 / data.length) * 0.7);

  return (
    <div className={`budget-chart budget-chart-bar ${className}`} style={{ height }}>
      <div className="budget-summary">
        <div className="summary-item">
          <span className="summary-label">Total Allocated</span>
          <span className="summary-value">{formatCurrency(totalAllocated)}</span>
        </div>
        <div className="summary-item">
          <span className="summary-label">Total Spent</span>
          <span className="summary-value">{formatCurrency(totalAllocated - data.reduce((sum, d) => sum + d.remaining, 0))}</span>
        </div>
        <div className="summary-item">
          <span className="summary-label">Utilization</span>
          <span className="summary-value">{((1 - data.reduce((sum, d) => sum + d.remaining, 0) / totalAllocated) * 100).toFixed(1)}%</span>
        </div>
      </div>
      <div className="chart-container" style={{ height: chartHeight }}>
        <svg width="100%" height="100%" preserveAspectRatio="xMidYMid meet">
          {data.map((item, index) => {
            const x = index * (100 / data.length);
            const allocatedHeight = (item.allocated / maxValue) * chartHeight * 0.9;
            const spentHeight = (item.spent / maxValue) * chartHeight * 0.9;
            const barWidth = 80 / data.length;

            return (
              <g key={index}>
                {/* Background bar (allocated) */}
                <rect
                  x={`${x + (100 / data.length - barWidth) / 2}%`}
                  y={chartHeight - allocatedHeight}
                  width={`${barWidth}%`}
                  height={allocatedHeight}
                  fill="#e5e7eb"
                  rx="2"
                />
                {/* Foreground bar (spent) */}
                <rect
                  x={`${x + (100 / data.length - barWidth) / 2}%`}
                  y={chartHeight - spentHeight}
                  width={`${barWidth}%`}
                  height={spentHeight}
                  fill="#6366f1"
                  rx="2"
                  className="budget-bar-spent"
                />
                {/* Label */}
                <text
                  x={`${x + 50 / data.length}%`}
                  y={chartHeight + 20}
                  textAnchor="middle"
                  fontSize="11"
                  fill="#374151"
                >
                  {item.category}
                </text>
                {/* Value */}
                <text
                  x={`${x + 50 / data.length}%`}
                  y={chartHeight - spentHeight - 5}
                  textAnchor="middle"
                  fontSize="10"
                  fill="#6b7280"
                >
                  {((item.spent / item.allocated) * 100).toFixed(0)}%
                </text>
              </g>
            );
          })}
        </svg>
      </div>
    </div>
  );
}

function renderDonutChart(
  data: BudgetData[],
  totalAllocated: number,
  totalSpent: number,
  height: number,
  className: string
) {
  const radius = Math.min(height, 400) / 2 - 20;
  const center = radius + 20;
  const circumference = 2 * Math.PI * radius;

  let currentOffset = 0;

  return (
    <div className={`budget-chart budget-chart-donut ${className}`} style={{ height }}>
      <div className="budget-summary">
        <div className="summary-item">
          <span className="summary-label">Total Budget</span>
          <span className="summary-value">{formatCurrency(totalAllocated)}</span>
        </div>
        <div className="summary-item">
          <span className="summary-label">Spent</span>
          <span className="summary-value summary-value-spent">{formatCurrency(totalSpent)}</span>
        </div>
        <div className="summary-item">
          <span className="summary-label">Remaining</span>
          <span className="summary-value summary-value-remaining">{formatCurrency(totalAllocated - totalSpent)}</span>
        </div>
      </div>
      <svg width="100%" height={height - 60} viewBox={`0 0 ${center * 2} ${center * 2}`}>
        {data.map((item, index) => {
          const percentage = item.allocated / totalAllocated;
          const strokeDasharray = `${percentage * circumference} ${circumference}`;
          const offset = -currentOffset;
          currentOffset += percentage * circumference;

          const colors = ['#6366f1', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'];

          return (
            <circle
              key={index}
              cx={center}
              cy={center}
              r={radius}
              fill="none"
              stroke={colors[index % colors.length]}
              strokeWidth="30"
              strokeDasharray={strokeDasharray}
              strokeDashoffset={offset}
              transform={`rotate(-90 ${center} ${center})`}
              className="budget-segment"
            >
              <title>{item.category}: {formatCurrency(item.allocated)}</title>
            </circle>
          );
        })}
        {/* Center text */}
        <text x={center} y={center - 5} textAnchor="middle" fontSize="24" fontWeight="bold" fill="#111827">
          {((totalSpent / totalAllocated) * 100).toFixed(0)}%
        </text>
        <text x={center} y={center + 15} textAnchor="middle" fontSize="12" fill="#6b7280">
          Utilized
        </text>
      </svg>
      <div className="budget-legend">
        {data.map((item, index) => (
          <div key={index} className="legend-item">
            <span
              className="legend-dot"
              style={{ backgroundColor: ['#6366f1', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'][index % 6] }}
            />
            <span className="legend-label">{item.category}</span>
            <span className="legend-value">{formatCurrency(item.allocated)}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

function formatCurrency(value: number): string {
  if (value >= 1000000) {
    return `$${(value / 1000000).toFixed(1)}M`;
  }
  if (value >= 1000) {
    return `$${(value / 1000).toFixed(1)}K`;
  }
  return `$${value.toFixed(0)}`;
}
