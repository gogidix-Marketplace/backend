// Color utility functions and constants

export const BRAND_COLORS = {
  primary: '#3B82F6',
  secondary: '#8B5CF6',
  success: '#10B981',
  warning: '#F59E0B',
  danger: '#EF4444',
  info: '#06B6D4',
  dark: '#1E293B',
  light: '#F1F5F9',
} as const;

export const STATUS_COLORS = {
  draft: '#9CA3AF',
  scheduled: '#3B82F6',
  active: '#10B981',
  paused: '#F59E0B',
  completed: '#6366F1',
  cancelled: '#EF4444',
} as const;

export const LEAD_QUALITY_COLORS = {
  hot: '#EF4444',
  warm: '#F59E0B',
  cold: '#3B82F6',
  unqualified: '#9CA3AF',
} as const;

export const LEAD_STATUS_COLORS = {
  new: '#3B82F6',
  contacted: '#8B5CF6',
  qualified: '#10B981',
  proposal: '#06B6D4',
  negotiation: '#F59E0B',
  won: '#10B981',
  lost: '#EF4444',
} as const;

export const CHANNEL_COLORS = {
  email: '#6366F1',
  social: '#EC4899',
  search: '#F59E0B',
  display: '#8B5CF6',
  video: '#EF4444',
  events: '#06B6D4',
  content: '#84CC16',
  other: '#6B7280',
} as const;

export const CHART_COLORS = [
  '#3B82F6',
  '#8B5CF6',
  '#EC4899',
  '#10B981',
  '#F59E0B',
  '#EF4444',
  '#06B6D4',
  '#84CC16',
  '#6366F1',
  '#14B8A6',
] as const;

export const GRADIENTS = {
  primary: 'linear-gradient(135deg, #3B82F6 0%, #2563EB 100%)',
  secondary: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)',
  success: 'linear-gradient(135deg, #10B981 0%, #059669 100%)',
  warning: 'linear-gradient(135deg, #F59E0B 0%, #D97706 100%)',
  danger: 'linear-gradient(135deg, #EF4444 0%, #DC2626 100%)',
  info: 'linear-gradient(135deg, #06B6D4 0%, #0891B2 100%)',
  dark: 'linear-gradient(135deg, #1E293B 0%, #0F172A 100%)',
} as const;

export const getChannelColor = (channel: string): string => {
  return CHANNEL_COLORS[channel as keyof typeof CHANNEL_COLORS] || CHANNEL_COLORS.other;
};

export const getStatusColor = (status: string): string => {
  return STATUS_COLORS[status as keyof typeof STATUS_COLORS] || '#9CA3AF';
};

export const getLeadQualityColor = (quality: string): string => {
  return LEAD_QUALITY_COLORS[quality as keyof typeof LEAD_QUALITY_COLORS] || '#9CA3AF';
};

export const getLeadStatusColor = (status: string): string => {
  return LEAD_STATUS_COLORS[status as keyof typeof LEAD_STATUS_COLORS] || '#9CA3AF';
};

export const getScoreColor = (score: number): string => {
  if (score >= 80) return LEAD_QUALITY_COLORS.hot;
  if (score >= 50) return LEAD_QUALITY_COLORS.warm;
  if (score >= 20) return LEAD_QUALITY_COLORS.cold;
  return LEAD_QUALITY_COLORS.unqualified;
};

export const getUtilizationColor = (percentage: number): string => {
  if (percentage >= 90) return '#EF4444'; // Over budget
  if (percentage >= 75) return '#F59E0B'; // Warning
  if (percentage >= 50) return '#10B981'; // On track
  return '#3B82F6'; // Under budget
};

export const getROIColor = (roi: number): string => {
  if (roi >= 300) return '#10B981';
  if (roi >= 200) return '#06B6D4';
  if (roi >= 100) return '#F59E0B';
  return '#EF4444';
};

export const getTrendColor = (trend: number, inverse: boolean = false): string => {
  if (Math.abs(trend) < 0.1) return '#9CA3AF';
  if (inverse) {
    return trend > 0 ? '#EF4444' : '#10B981';
  }
  return trend > 0 ? '#10B981' : '#EF4444';
};

export const getAlertColor = (type: string): string => {
  switch (type) {
    case 'error':
      return '#EF4444';
    case 'warning':
      return '#F59E0B';
    case 'success':
      return '#10B981';
    case 'info':
    default:
      return '#3B82F6';
  }
};

export const getAlertBgColor = (type: string): string => {
  switch (type) {
    case 'error':
      return 'rgba(239, 68, 68, 0.1)';
    case 'warning':
      return 'rgba(245, 158, 11, 0.1)';
    case 'success':
      return 'rgba(16, 185, 129, 0.1)';
    case 'info':
    default:
      return 'rgba(59, 130, 246, 0.1)';
  }
};

export const adjustColorOpacity = (hex: string, opacity: number): string => {
  const r = parseInt(hex.slice(1, 3), 16);
  const g = parseInt(hex.slice(3, 5), 16);
  const b = parseInt(hex.slice(5, 7), 16);
  return `rgba(${r}, ${g}, ${b}, ${opacity})`;
};

export const lightenColor = (hex: string, amount: number): string => {
  const r = Math.min(255, parseInt(hex.slice(1, 3), 16) + amount);
  const g = Math.min(255, parseInt(hex.slice(3, 5), 16) + amount);
  const b = Math.min(255, parseInt(hex.slice(5, 7), 16) + amount);
  return `#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}`;
};

export const darkenColor = (hex: string, amount: number): string => {
  const r = Math.max(0, parseInt(hex.slice(1, 3), 16) - amount);
  const g = Math.max(0, parseInt(hex.slice(3, 5), 16) - amount);
  const b = Math.max(0, parseInt(hex.slice(5, 7), 16) - amount);
  return `#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}`;
};

export const getHeatmapColor = (value: number, min: number, max: number): string => {
  const ratio = Math.max(0, Math.min(1, (value - min) / (max - min)));
  const r = Math.round(255 * ratio);
  const g = Math.round(255 * (1 - Math.abs(ratio - 0.5) * 2));
  const b = Math.round(255 * (1 - ratio));
  return `rgb(${r}, ${g}, ${b})`;
};

export const generatePalette = (baseColor: string, count: number): string[] => {
  const colors: string[] = [];
  for (let i = 0; i < count; i++) {
    const lightness = 100 - (i * 80) / count;
    colors.push(lightenColor(baseColor, Math.round(lightness * 2.55)));
  }
  return colors;
};
