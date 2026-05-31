// Metric Card Component
// Displays a key metric with value, change indicator, and optional trend

import './MetricCard.css';

interface MetricCardProps {
  title: string;
  value: string | number;
  change?: number;
  changeType?: 'increase' | 'decrease' | 'neutral';
  target?: number;
  attainment?: number;
  unit?: string;
  icon?: React.ReactNode;
  trend?: React.ReactNode;
  loading?: boolean;
  className?: string;
  onClick?: () => void;
}

export function MetricCard({
  title,
  value,
  change,
  changeType = 'neutral',
  target,
  attainment,
  unit,
  icon,
  trend,
  loading = false,
  className = '',
  onClick,
}: MetricCardProps) {
  if (loading) {
    return (
      <div className={`metric-card metric-card-loading ${className}`}>
        <div className="metric-card-skeleton">
          <div className="skeleton-icon"></div>
          <div className="skeleton-content">
            <div className="skeleton-title"></div>
            <div className="skeleton-value"></div>
          </div>
        </div>
      </div>
    );
  }

  const changeIcon = {
    increase: (
      <svg className="metric-trend-icon metric-trend-up" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <polyline points="23 6 13.5 15.5 8.5 10.5 1 18" />
        <polyline points="17 6 23 6 23 12" />
      </svg>
    ),
    decrease: (
      <svg className="metric-trend-icon metric-trend-down" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <polyline points="23 18 13.5 8.5 8.5 13.5 1 6" />
        <polyline points="17 18 23 18 23 12" />
      </svg>
    ),
    neutral: null,
  }[changeType];

  return (
    <div className={`metric-card ${onClick ? 'metric-card-clickable' : ''} ${className}`} onClick={onClick}>
      <div className="metric-card-header">
        {icon && <div className="metric-card-icon">{icon}</div>}
        <h3 className="metric-card-title">{title}</h3>
      </div>

      <div className="metric-card-value-section">
        <div className="metric-card-value">
          {typeof value === 'number' ? value.toLocaleString() : value}
          {unit && <span className="metric-card-unit">{unit}</span>}
        </div>

        {change !== undefined && (
          <div className={`metric-card-change metric-card-change-${changeType}`}>
            {changeIcon}
            <span>{change > 0 ? '+' : ''}{change}%</span>
          </div>
        )}
      </div>

      {(target !== undefined || attainment !== undefined) && (
        <div className="metric-card-progress">
          {attainment !== undefined && (
            <div className="metric-card-attainment">
              <div className="metric-card-attainment-bar">
                <div
                  className="metric-card-attainment-fill"
                  style={{ width: `${Math.min(attainment, 100)}%` }}
                ></div>
              </div>
              <span className="metric-card-attainment-text">{attainment}%</span>
            </div>
          )}
          {target !== undefined && attainment === undefined && (
            <span className="metric-card-target">Target: {target.toLocaleString()}</span>
          )}
        </div>
      )}

      {trend && <div className="metric-card-trend">{trend}</div>}
    </div>
  );
}
