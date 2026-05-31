// MetricCard Component
// Displays a key metric with optional change indicator and target progress

import './MetricCard.css';

interface MetricCardProps {
  title: string;
  value: string;
  change?: number;
  changeType?: 'increase' | 'decrease' | 'neutral';
  target?: string;
  attainment?: number;
  unit?: string;
  icon?: React.ReactNode;
  trend?: 'up' | 'down' | 'neutral';
  className?: string;
}

export function MetricCard({
  title,
  value,
  change,
  changeType = 'neutral',
  target,
  attainment,
  unit = '',
  icon,
  trend,
  className = '',
}: MetricCardProps) {
  const getTrendIcon = () => {
    if (trend === 'up') return '\u2191';
    if (trend === 'down') return '\u2193';
    return '\u2192';
  };

  const getChangeColor = () => {
    if (changeType === 'increase') return 'text-green-600';
    if (changeType === 'decrease') return 'text-red-600';
    return 'text-gray-500';
  };

  return (
    <div className={`metric-card ${className}`}>
      <div className="metric-card-header">
        <span className="metric-card-title">{title}</span>
        {icon && <span className="metric-card-icon">{icon}</span>}
      </div>
      <div className="metric-card-value">
        {value}
        {unit && <span className="metric-card-unit">{unit}</span>}
      </div>
      {change !== undefined && (
        <div className={`metric-card-change ${getChangeColor()}`}>
          <span className="trend-icon">{getTrendIcon()}</span>
          <span className="change-value">{Math.abs(change)}%</span>
          <span className="change-label">vs last period</span>
        </div>
      )}
      {target && (
        <div className="metric-card-target">
          <span className="target-label">Target:</span>
          <span className="target-value">{target}</span>
        </div>
      )}
      {attainment !== undefined && (
        <div className="metric-card-progress">
          <div className="progress-bar">
            <div
              className={`progress-fill ${attainment >= 100 ? 'progress-success' : attainment >= 75 ? 'progress-warning' : 'progress-danger'}`}
              style={{ width: `${Math.min(attainment, 100)}%` }}
            />
          </div>
          <span className="progress-label">{attainment}%</span>
        </div>
      )}
    </div>
  );
}
