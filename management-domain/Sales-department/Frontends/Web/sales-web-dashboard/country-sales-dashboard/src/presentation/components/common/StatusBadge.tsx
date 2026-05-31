// StatusBadge Component
// Status indicators with colors for different statuses

import React from 'react';
import { getStatusColor } from '@shared/utils';

export type StatusType =
  | 'active' | 'inactive' | 'pending'
  | 'PROSPECTING' | 'QUALIFICATION' | 'PROPOSAL' | 'NEGOTIATION' | 'CLOSING' | 'WON' | 'LOST'
  | 'NEW' | 'CONTACTED' | 'QUALIFIED' | 'CONVERTED' | 'UNQUALIFIED'
  | 'PENDING' | 'ACTIVE' | 'SUSPENDED' | 'TERMINATED'
  | 'BRONZE' | 'SILVER' | 'GOLD' | 'PLATINUM'
  | 'hot' | 'warm' | 'cold'
  | 'high' | 'medium' | 'low' | 'urgent'
  | 'success' | 'warning' | 'error' | 'info';

export interface StatusBadgeProps {
  status: StatusType | string;
  label?: string;
  size?: 'sm' | 'md' | 'lg';
  variant?: 'solid' | 'outline' | 'subtle';
  showDot?: boolean;
  className?: string;
}

const STATUS_COLORS: Record<string, { bg: string; text: string; border: string }> = {
  // Team/Customer/Partner status
  active: { bg: '#D1FAE5', text: '#065F46', border: '#10B981' },
  inactive: { bg: '#F3F4F6', text: '#374151', border: '#9CA3AF' },
  pending: { bg: '#FEF3C7', text: '#92400E', border: '#F59E0B' },

  // Deal stages
  PROSPECTING: { bg: '#F3F4F6', text: '#374151', border: '#9CA3AF' },
  QUALIFICATION: { bg: '#DBEAFE', text: '#1E40AF', border: '#3B82F6' },
  PROPOSAL: { bg: '#EDE9FE', text: '#5B21B6', border: '#8B5CF6' },
  NEGOTIATION: { bg: '#FEF3C7', text: '#92400E', border: '#F59E0B' },
  CLOSING: { bg: '#D1FAE5', text: '#065F46', border: '#10B981' },
  WON: { bg: '#D1FAE5', text: '#065F46', border: '#059669' },
  LOST: { bg: '#FEE2E2', text: '#991B1B', border: '#EF4444' },

  // Lead status
  NEW: { bg: '#DBEAFE', text: '#1E40AF', border: '#3B82F6' },
  CONTACTED: { bg: '#EDE9FE', text: '#5B21B6', border: '#8B5CF6' },
  QUALIFIED: { bg: '#D1FAE5', text: '#065F46', border: '#10B981' },
  CONVERTED: { bg: '#D1FAE5', text: '#065F46', border: '#059669' },
  UNQUALIFIED: { bg: '#FEF3C7', text: '#92400E', border: '#F59E0B' },

  // Partner application status
  UNDER_REVIEW: { bg: '#E0E7FF', text: '#3730A3', border: '#6366F1' },
  APPROVED: { bg: '#D1FAE5', text: '#065F46', border: '#10B981' },
  REJECTED: { bg: '#FEE2E2', text: '#991B1B', border: '#EF4444' },
  ON_HOLD: { bg: '#F3F4F6', text: '#374151', border: '#6B7280' },
  SUSPENDED: { bg: '#FEE2E2', text: '#991B1B', border: '#EF4444' },
  TERMINATED: { bg: '#1F2937', text: '#F9FAFB', border: '#111827' },

  // Partner tiers
  BRONZE: { bg: '#FEF3C7', text: '#92400E', border: '#CD7F32' },
  SILVER: { bg: '#F3F4F6', text: '#374151', border: '#C0C0C0' },
  GOLD: { bg: '#FEF3C7', text: '#92400E', border: '#FFD700' },
  PLATINUM: { bg: '#E5E7EB', text: '#374151', border: '#E5E4E2' },

  // Lead rating
  hot: { bg: '#FEE2E2', text: '#991B1B', border: '#EF4444' },
  warm: { bg: '#FEF3C7', text: '#92400E', border: '#F59E0B' },
  cold: { bg: '#DBEAFE', text: '#1E40AF', border: '#3B82F6' },

  // Priority
  urgent: { bg: '#FEE2E2', text: '#991B1B', border: '#DC2626' },
  high: { bg: '#FED7D7', text: '#7F1D1D', border: '#EF4444' },
  medium: { bg: '#FEF3C7', text: '#92400E', border: '#F59E0B' },
  low: { bg: '#DBEAFE', text: '#1E40AF', border: '#3B82F6' },

  // Generic
  success: { bg: '#D1FAE5', text: '#065F46', border: '#10B981' },
  warning: { bg: '#FEF3C7', text: '#92400E', border: '#F59E0B' },
  error: { bg: '#FEE2E2', text: '#991B1B', border: '#EF4444' },
  info: { bg: '#DBEAFE', text: '#1E40AF', border: '#3B82F6' },

  // Customer status
  at_risk: { bg: '#FEF3C7', text: '#92400E', border: '#F59E0B' },
  churned: { bg: '#FEE2E2', text: '#991B1B', border: '#EF4444' },
  prospect: { bg: '#DBEAFE', text: '#1E40AF', border: '#3B82F6' },
};

export const StatusBadge: React.FC<StatusBadgeProps> = ({
  status,
  label,
  size = 'md',
  variant = 'solid',
  showDot = false,
  className = '',
}) => {
  const colors = STATUS_COLORS[status] || STATUS_COLORS.info;
  const displayLabel = label || status.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());

  const getVariantStyles = () => {
    switch (variant) {
      case 'outline':
        return {
          backgroundColor: 'transparent',
          color: colors.border,
          border: `1px solid ${colors.border}`,
        };
      case 'subtle':
        return {
          backgroundColor: colors.bg + '40',
          color: colors.text,
          border: 'none',
        };
      default: // solid
        return {
          backgroundColor: colors.bg,
          color: colors.text,
          border: `1px solid ${colors.border}`,
        };
    }
  };

  const styles = getVariantStyles();

  return (
    <span
      className={`status-badge status-badge-${size} status-badge-${variant} ${className}`}
      style={styles}
    >
      {showDot && (
        <span
          className="status-badge-dot"
          style={{ backgroundColor: colors.border }}
        />
      )}
      <span className="status-badge-label">{displayLabel}</span>
    </span>
  );
};

export default StatusBadge;
