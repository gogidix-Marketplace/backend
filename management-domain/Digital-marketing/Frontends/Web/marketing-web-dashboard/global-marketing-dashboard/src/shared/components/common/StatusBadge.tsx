// StatusBadge Component
// Displays status with appropriate styling

import React from 'react';
import './StatusBadge.css';

export interface StatusBadgeProps {
  status: string;
  size?: 'sm' | 'md' | 'lg';
  showDot?: boolean;
  className?: string;
}

const statusConfig: Record<string, { label: string; color: string }> = {
  // Campaign statuses
  draft: { label: 'Draft', color: '#9CA3AF' },
  scheduled: { label: 'Scheduled', color: '#3B82F6' },
  active: { label: 'Active', color: '#10B981' },
  paused: { label: 'Paused', color: '#F59E0B' },
  completed: { label: 'Completed', color: '#6366F1' },
  cancelled: { label: 'Cancelled', color: '#EF4444' },

  // Budget statuses
  approved: { label: 'Approved', color: '#10B981' },
  exhausted: { label: 'Exhausted', color: '#EF4444' },

  // Lead statuses
  new: { label: 'New', color: '#3B82F6' },
  contacted: { label: 'Contacted', color: '#8B5CF6' },
  qualified: { label: 'Qualified', color: '#10B981' },
  proposal: { label: 'Proposal', color: '#06B6D4' },
  negotiation: { label: 'Negotiation', color: '#F59E0B' },
  won: { label: 'Won', color: '#10B981' },
  lost: { label: 'Lost', color: '#EF4444' },

  // Lead quality
  hot: { label: 'Hot', color: '#EF4444' },
  warm: { label: 'Warm', color: '#F59E0B' },
  cold: { label: 'Cold', color: '#3B82F6' },
  unqualified: { label: 'Unqualified', color: '#9CA3AF' },
};

export const StatusBadge: React.FC<StatusBadgeProps> = ({
  status,
  size = 'md',
  showDot = true,
  className = '',
}) => {
  const config = statusConfig[status.toLowerCase()] || {
    label: status,
    color: '#6B7280',
  };

  const classes = [
    'status-badge',
    `status-badge--${size}`,
    className,
  ]
    .filter(Boolean)
    .join(' ');

  const badgeStyle = {
    '--status-color': config.color,
  } as React.CSSProperties;

  return (
    <span className={classes} style={badgeStyle}>
      {showDot && <span className="status-badge__dot" />}
      <span className="status-badge__label">{config.label}</span>
    </span>
  );
};

export default StatusBadge;
