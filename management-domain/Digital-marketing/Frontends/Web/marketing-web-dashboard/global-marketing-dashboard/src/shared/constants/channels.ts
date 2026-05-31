// Marketing Channel Constants

export const MARKETING_CHANNELS = [
  { id: 'email', name: 'Email', icon: '✉️', color: '#6366f1' },
  { id: 'social', name: 'Social Media', icon: '📱', color: '#ec4899' },
  { id: 'search', name: 'Search/PPC', icon: '🔍', color: '#f59e0b' },
  { id: 'display', name: 'Display', icon: '🖼️', color: '#8b5cf6' },
  { id: 'video', name: 'Video', icon: '🎬', color: '#ef4444' },
  { id: 'direct_mail', name: 'Direct Mail', icon: '📬', color: '#10b981' },
  { id: 'events', name: 'Events', icon: '🎪', color: '#06b6d4' },
  { id: 'content', name: 'Content', icon: '📝', color: '#84cc16' },
  { id: 'other', name: 'Other', icon: '📦', color: '#6b7280' },
] as const;

export const SOCIAL_PLATFORMS = [
  { id: 'facebook', name: 'Facebook', icon: '📘', color: '#1877f2' },
  { id: 'instagram', name: 'Instagram', icon: '📷', color: '#e4405f' },
  { id: 'linkedin', name: 'LinkedIn', icon: '💼', color: '#0a66c2' },
  { id: 'twitter', name: 'Twitter/X', icon: '🐦', color: '#000000' },
  { id: 'youtube', name: 'YouTube', icon: '▶️', color: '#ff0000' },
  { id: 'tiktok', name: 'TikTok', icon: '🎵', color: '#000000' },
  { id: 'pinterest', name: 'Pinterest', icon: '📌', color: '#bd081c' },
] as const;

export const getCampaignTypeName = (type: string) => {
  const types: Record<string, string> = {
    awareness: 'Brand Awareness',
    consideration: 'Consideration',
    conversion: 'Conversion',
    retention: 'Retention',
  };
  return types[type] || type;
};

export const getCampaignStatusColor = (status: string) => {
  const colors: Record<string, string> = {
    draft: '#9ca3af',
    scheduled: '#3b82f6',
    active: '#10b981',
    paused: '#f59e0b',
    completed: '#6366f1',
    cancelled: '#ef4444',
  };
  return colors[status] || '#6b7280';
};
