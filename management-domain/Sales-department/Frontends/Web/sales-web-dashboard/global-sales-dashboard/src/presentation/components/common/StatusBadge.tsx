// StatusBadge Component
// Displays status indicators with appropriate colors and icons

import type React from 'react';

export interface StatusBadgeProps {
  status: string;
  size?: 'sm' | 'md' | 'lg';
  variant?: 'solid' | 'outline' | 'subtle';
  className?: string;
}

const statusConfig: Record<string, { color: string; bgColor: string; icon?: string }> = {
  // Status indicators
  on_track: { color: '#10B981', bgColor: '#D1FAE5' },
  at_risk: { color: '#F59E0B', bgColor: '#FEF3C7' },
  off_track: { color: '#EF4444', bgColor: '#FEE2E2' },
  active: { color: '#10B981', bgColor: '#D1FAE5' },
  inactive: { color: '#6B7280', bgColor: '#E5E7EB' },
  pending: { color: '#F59E0B', bgColor: '#FEF3C7' },
  approved: { color: '#10B981', bgColor: '#D1FAE5' },
  rejected: { color: '#EF4444', bgColor: '#FEE2E2' },
  under_review: { color: '#3B82F6', bgColor: '#DBEAFE' },

  // Pipeline stages
  new: { color: '#9CA3AF', bgColor: '#F3F4F6' },
  qualified: { color: '#3B82F6', bgColor: '#DBEAFE' },
  proposal: { color: '#8B5CF6', bgColor: '#EDE9FE' },
  negotiating: { color: '#F59E0B', bgColor: '#FEF3C7' },
  closing: { color: '#10B981', bgColor: '#D1FAE5' },
  won: { color: '#059669', bgColor: '#D1FAE5' },
  lost: { color: '#EF4444', bgColor: '#FEE2E2' },

  // Partner tiers
  bronze: { color: '#CD7F32', bgColor: '#FEF3C7' },
  silver: { color: '#6B7280', bgColor: '#E5E7EB' },
  gold: { color: '#D97706', bgColor: '#FEF3C7' },
  platinum: { color: '#7C3AED', bgColor: '#EDE9FE' },

  // Alert types
  info: { color: '#3B82F6', bgColor: '#DBEAFE' },
  warning: { color: '#F59E0B', bgColor: '#FEF3C7' },
  success: { color: '#10B981', bgColor: '#D1FAE5' },
  error: { color: '#EF4444', bgColor: '#FEE2E2' },
  critical: { color: '#DC2626', bgColor: '#FEE2E2' },

  // Risk levels
  low: { color: '#10B981', bgColor: '#D1FAE5' },
  medium: { color: '#F59E0B', bgColor: '#FEF3C7' },
  high: { color: '#EF4444', bgColor: '#FEE2E2' },
};

const sizeStyles = {
  sm: { padding: '2px 8px', fontSize: '0.75rem', borderRadius: '4px' },
  md: { padding: '4px 12px', fontSize: '0.875rem', borderRadius: '6px' },
  lg: { padding: '6px 16px', fontSize: '1rem', borderRadius: '8px' },
};

export function StatusBadge({
  status,
  size = 'md',
  variant = 'subtle',
  className = ''
}: StatusBadgeProps): React.ReactElement {
  const normalizedStatus = status.toLowerCase().replace(/[^a-z_]/g, '_');
  const config = statusConfig[normalizedStatus] || statusConfig.info;

  const baseStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: '4px',
    fontWeight: '500',
    whiteSpace: 'nowrap',
    ...sizeStyles[size],
  };

  let variantStyle: React.CSSProperties = {};

  switch (variant) {
    case 'solid':
      variantStyle = {
        backgroundColor: config.color,
        color: '#FFFFFF',
      };
      break;
    case 'outline':
      variantStyle = {
        backgroundColor: 'transparent',
        color: config.color,
        border: `1px solid ${config.color}`,
      };
      break;
    case 'subtle':
    default:
      variantStyle = {
        backgroundColor: config.bgColor,
        color: config.color,
      };
      break;
  }

  return (
    <span
      className={`status-badge status-badge-${normalizedStatus} status-badge-${size} status-badge-${variant} ${className}`}
      style={{ ...baseStyle, ...variantStyle }}
    >
      {config.icon && <span>{config.icon}</span>}
      <span className="status-badge-text">
        {status.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase())}
      </span>
    </span>
  );
}

export default StatusBadge;
