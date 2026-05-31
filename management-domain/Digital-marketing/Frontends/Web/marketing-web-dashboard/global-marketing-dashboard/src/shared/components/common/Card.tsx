// Card Component
// Container component for content grouping

import React from 'react';
import './Card.css';

export interface CardProps {
  children: React.ReactNode;
  title?: string;
  subtitle?: string;
  actions?: React.ReactNode;
  className?: string;
  padding?: 'none' | 'sm' | 'md' | 'lg';
  hoverable?: boolean;
  bordered?: boolean;
  onClick?: () => void;
}

export const Card: React.FC<CardProps> = ({
  children,
  title,
  subtitle,
  actions,
  className = '',
  padding = 'md',
  hoverable = false,
  bordered = true,
  onClick,
}) => {
  const classes = [
    'card',
    `card--padding-${padding}`,
    hoverable && 'card--hoverable',
    bordered && 'card--bordered',
    onClick && 'card--clickable',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className={classes} onClick={onClick}>
      {(title || subtitle || actions) && (
        <div className="card__header">
          <div className="card__header-content">
            {title && <h3 className="card__title">{title}</h3>}
            {subtitle && <p className="card__subtitle">{subtitle}</p>}
          </div>
          {actions && <div className="card__actions">{actions}</div>}
        </div>
      )}
      <div className="card__body">{children}</div>
    </div>
  );
};

export interface CardMetricProps {
  label: string;
  value: string | number;
  change?: number;
  changeType?: 'positive' | 'negative' | 'neutral';
  unit?: string;
  icon?: React.ReactNode;
  trend?: 'up' | 'down' | 'stable';
  loading?: boolean;
  size?: 'sm' | 'md' | 'lg';
}

export const CardMetric: React.FC<CardMetricProps> = ({
  label,
  value,
  change,
  changeType,
  unit,
  icon,
  trend,
  loading = false,
  size = 'md',
}) => {
  if (loading) {
    return (
      <div className={`card-metric card-metric--${size}`}>
        <div className="card-metric__skeleton">
          <div className="card-metric__skeleton-icon" />
          <div className="card-metric__skeleton-content">
            <div className="card-metric__skeleton-label" />
            <div className="card-metric__skeleton-value" />
          </div>
        </div>
      </div>
    );
  }

  const getTrendIcon = () => {
    switch (trend) {
      case 'up':
        return '↑';
      case 'down':
        return '↓';
      default:
        return '−';
    }
  };

  const getChangeColor = () => {
    if (changeType === 'positive') return 'var(--color-success)';
    if (changeType === 'negative') return 'var(--color-danger)';
    return 'var(--color-text-secondary)';
  };

  return (
    <div className={`card-metric card-metric--${size}`}>
      {icon && <div className="card-metric__icon">{icon}</div>}
      <div className="card-metric__content">
        <p className="card-metric__label">{label}</p>
        <div className="card-metric__value-row">
          <p className="card-metric__value">
            {value}
            {unit && <span className="card-metric__unit">{unit}</span>}
          </p>
          {change !== undefined && (
            <span
              className="card-metric__change"
              style={{ color: getChangeColor() }}
            >
              {getTrendIcon()} {Math.abs(change)}%
            </span>
          )}
        </div>
      </div>
    </div>
  );
};

export default Card;
