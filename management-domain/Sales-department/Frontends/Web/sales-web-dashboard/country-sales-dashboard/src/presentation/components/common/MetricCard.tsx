// MetricCard Component
// Display card for key metrics with trend indicators

import React from 'react';
import { formatCompactNumber, getTrendIcon, getTrendColor } from '@shared';

export interface MetricCardProps {
  title: string;
  value: number | string;
  previousValue?: number;
  change?: number;
  changeType?: 'increase' | 'decrease' | 'neutral';
  target?: number;
  currency?: string;
  prefix?: string;
  suffix?: string;
  isLoading?: boolean;
  icon?: React.ReactNode;
  trend?: 'up' | 'down' | 'neutral';
  size?: 'sm' | 'md' | 'lg';
  onClick?: () => void;
}

export const MetricCard: React.FC<MetricCardProps> = ({
  title,
  value,
  change,
  changeType,
  target,
  currency,
  prefix,
  suffix,
  isLoading = false,
  icon,
  trend,
  size = 'md',
  onClick,
}) => {
  if (isLoading) {
    return (
      <div className="metric-card metric-card-loading">
        <div className="metric-card-skeleton">
          <div className="skeleton-icon" />
          <div className="skeleton-content">
            <div className="skeleton-title" />
            <div className="skeleton-value" />
            <div className="skeleton-change" />
          </div>
        </div>
      </div>
    );
  }

  // Determine trend from changeType if not explicitly provided
  const displayTrend = trend || changeType;

  const displayValue = typeof value === 'number'
    ? formatCompactNumber(value)
    : value;

  const attainment = target && typeof value === 'number'
    ? Math.round((value / target) * 100)
    : null;

  return (
    <div className={`metric-card metric-card-${size} ${onClick ? 'metric-card-clickable' : ''}`} onClick={onClick}>
      <div className="metric-card-header">
        {icon && <div className="metric-card-icon">{icon}</div>}
        <div className="metric-card-content">
          <span className="metric-card-title">{title}</span>
          <div className="metric-card-value-row">
            {prefix && <span className="metric-card-prefix">{prefix}</span>}
            <span className="metric-card-value">{displayValue}</span>
            {suffix && <span className="metric-card-suffix">{suffix}</span>}
            {currency && <span className="metric-card-currency">{currency}</span>}
          </div>
          {(change !== undefined || displayTrend || attainment !== null) && (
            <div className="metric-card-footer">
              {change !== undefined && (
                <span
                  className={`metric-card-change metric-card-change-${changeType || 'neutral'}`}
                  style={{ color: getTrendColor(displayTrend || 'neutral') }}
                >
                  {displayTrend && <span className="trend-icon">{getTrendIcon(displayTrend)}</span>}
                  {change > 0 ? '+' : ''}{change}%
                </span>
              )}
              {attainment !== null && (
                <span className={`metric-card-attainment ${attainment >= 100 ? 'attainment-met' : attainment >= 80 ? 'attainment-close' : 'attainment-behind'}`}>
                  {attainment}% of target
                </span>
              )}
            </div>
          )}
        </div>
      </div>
      {target && typeof value === 'number' && (
        <div className="metric-card-progress">
          <div
            className="metric-card-progress-bar"
            style={{ width: `${Math.min(attainment || 0, 100)}%` }}
          />
        </div>
      )}
    </div>
  );
};

export default MetricCard;
