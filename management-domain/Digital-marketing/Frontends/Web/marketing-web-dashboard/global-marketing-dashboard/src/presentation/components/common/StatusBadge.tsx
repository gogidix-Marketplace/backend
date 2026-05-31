// StatusBadge Component
// Displays status with color coding

import './StatusBadge.css';

type StatusType =
  | 'active' | 'draft' | 'paused' | 'completed' | 'cancelled'
  | 'success' | 'warning' | 'error' | 'info'
  | 'on_track' | 'at_risk' | 'off_track'
  | 'hot' | 'warm' | 'cold'
  | 'pending' | 'approved' | 'rejected';

interface StatusBadgeProps {
  status: StatusType | string;
  label?: string;
  size?: 'sm' | 'md' | 'lg';
  className?: string;
}

const statusConfig: Record<StatusType, { color: string; icon?: string }> = {
  active: { color: 'green', icon: '\u{25CF}' },
  draft: { color: 'gray', icon: '\u{25CF}' },
  paused: { color: 'yellow', icon: '\u23F8' },
  completed: { color: 'blue', icon: '\u{2705}' },
  cancelled: { color: 'red', icon: '\u{274C}' },
  success: { color: 'green', icon: '\u{2705}' },
  warning: { color: 'yellow', icon: '\u26A0' },
  error: { color: 'red', icon: '\u{274C}' },
  info: { color: 'blue', icon: '\u2139' },
  on_track: { color: 'green', icon: '\u{2705}' },
  at_risk: { color: 'yellow', icon: '\u26A0' },
  off_track: { color: 'red', icon: '\u{274C}' },
  hot: { color: 'red', icon: '\u{1F525}' },
  warm: { color: 'yellow', icon: '\u{1F312}' },
  cold: { color: 'blue', icon: '\u2744' },
  pending: { color: 'yellow', icon: '\u23F3' },
  approved: { color: 'green', icon: '\u{2705}' },
  rejected: { color: 'red', icon: '\u{274C}' },
};

export function StatusBadge({
  status,
  label,
  size = 'md',
  className = '',
}: StatusBadgeProps) {
  const config = statusConfig[status as StatusType] || { color: 'gray', icon: '\u{25CF}' };
  const displayLabel = label || status.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());

  return (
    <span className={`status-badge status-badge-${config.color} status-badge-${size} ${className}`}>
      <span className="status-badge-icon">{config.icon}</span>
      <span className="status-badge-label">{displayLabel}</span>
    </span>
  );
}
