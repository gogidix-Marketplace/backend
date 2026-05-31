// MetricCard Component
// Displays a single metric with trend indicator

import React from 'react';
import './MetricCard.css';

export interface MetricCardProps {
  label: string;
  value: string | number;
  change?: number;
  changeType?: 'positive' | 'negative' | 'neutral';
  icon?: React.ReactNode;
  unit?: string;
  prefix?: string;
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
  trend?: 'up' | 'down' | 'neutral';
  onClick?: () => void;
  className?: string;
}

export const MetricCard: React.FC<MetricCardProps> = ({
  label,
  value,
  change,
  changeType = 'neutral',
  icon,
  unit,
  prefix,
  size = 'md',
  loading = false,
  trend,
  onClick,
  className = '',
}) => {
  const calculatedTrend = trend || (change && change > 0 ? 'up' : change && change < 0 ? 'down' : 'neutral');
  const calculatedChangeType = changeType || (change && change > 0 ? 'positive' : change && change < 0 ? 'negative' : 'neutral');

  if (loading) {
    return (
      <div className={`metric-card metric-card--loading metric-card--${size} ${className}`}>
        <div className="metric-card__skeleton">
          <div className="skeleton-line skeleton-line--icon" />
          <div className="skeleton-line skeleton-line--value" />
          <div className="skeleton-line skeleton-line--label" />
        </div>
      </div>
    );
  }

  return (
    <div
      className={`metric-card metric-card--${size} ${onClick ? 'metric-card--clickable' : ''} ${className}`}
      onClick={onClick}
    >
      {icon && (
        <div className="metric-card__icon">
          {icon}
        </div>
      )}
      <div className="metric-card__content">
        <span className="metric-card__label">{label}</span>
        <div className="metric-card__value-row">
          <span className="metric-card__value">
            {prefix}{typeof value === 'number' ? value.toLocaleString() : value}{unit}
          </span>
          {change !== undefined && (
            <span className={`metric-card__change metric-card__change--${calculatedChangeType}`}>
              {calculatedTrend === 'up' && '↑'}
              {calculatedTrend === 'down' && '↓'}
              {Math.abs(change)}%
            </span>
          )}
        </div>
      </div>
    </div>
  );
};

export default MetricCard;
