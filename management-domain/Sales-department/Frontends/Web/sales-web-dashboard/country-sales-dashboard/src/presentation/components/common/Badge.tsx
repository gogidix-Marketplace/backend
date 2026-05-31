// Badge Component
// Small status/label badge component

import React from 'react';
import { getStatusColor } from '@shared';

export interface BadgeProps {
  children: React.ReactNode;
  variant?: 'default' | 'success' | 'warning' | 'error' | 'info';
  size?: 'sm' | 'md' | 'lg';
  status?: string;
  dot?: boolean;
  className?: string;
}

export const Badge: React.FC<BadgeProps> = ({
  children,
  variant = 'default',
  size = 'md',
  status,
  dot = false,
  className = '',
}) => {
  const customStyle = status ? { backgroundColor: getStatusColor(status) } : undefined;

  const classes = [
    'badge',
    `badge-${variant}`,
    `badge-${size}`,
    dot ? 'badge-dot' : '',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <span className={classes} style={customStyle}>
      {dot && <span className="badge-dot-indicator" />}
      {children}
    </span>
  );
};

export default Badge;
