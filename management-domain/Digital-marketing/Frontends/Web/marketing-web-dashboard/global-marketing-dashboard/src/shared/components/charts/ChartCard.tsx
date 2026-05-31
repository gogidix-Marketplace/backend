// ChartCard Component
// Wrapper component for charts with title and actions

import React from 'react';
import './ChartCard.css';

export interface ChartCardProps {
  title: string;
  subtitle?: string;
  actions?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
  loading?: boolean;
  error?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl';
}

export const ChartCard: React.FC<ChartCardProps> = ({
  title,
  subtitle,
  actions,
  children,
  className = '',
  loading = false,
  error,
  size = 'md',
}) => {
  const classes = [
    'chart-card',
    `chart-card--${size}`,
    className,
  ].filter(Boolean).join(' ');

  return (
    <div className={classes}>
      <div className="chart-card__header">
        <div className="chart-card__title-section">
          <h3 className="chart-card__title">{title}</h3>
          {subtitle && <p className="chart-card__subtitle">{subtitle}</p>}
        </div>
        {actions && <div className="chart-card__actions">{actions}</div>}
      </div>

      <div className="chart-card__content">
        {loading ? (
          <div className="chart-card__loading">
            <div className="chart-card__spinner"></div>
            <p>Loading chart...</p>
          </div>
        ) : error ? (
          <div className="chart-card__error">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
            <p>{error}</p>
          </div>
        ) : (
          children
        )}
      </div>
    </div>
  );
};

export default ChartCard;
