// SocialPostsTable Component
// Table component for displaying social media posts

import React from 'react';
import { Table, Column } from './Table';
import { SocialPost, SocialPlatform, SocialPostStatus } from '../../domain/types';
import { StatusBadge } from '../common/StatusBadge';
import { formatCompactNumber } from '../../utils/formatters';

export interface SocialPostsTableProps {
  posts: SocialPost[];
  onPostClick?: (post: SocialPost) => void;
  className?: string;
  loading?: boolean;
}

const platformIcons: Record<SocialPlatform, string> = {
  facebook: '📘',
  instagram: '📷',
  twitter: '🐦',
  linkedin: '💼',
  tiktok: '🎵',
  youtube: '📺',
  pinterest: '📌',
};

const statusColors: Record<SocialPostStatus, string> = {
  draft: '#9CA3AF',
  scheduled: '#3B82F6',
  published: '#10B981',
  failed: '#EF4444',
};

export const SocialPostsTable: React.FC<SocialPostsTableProps> = ({
  posts,
  onPostClick,
  className = '',
  loading = false,
}) => {
  const columns: Column<SocialPost>[] = [
    {
      key: 'platform',
      title: 'Platform',
      render: (value: SocialPlatform, post) => (
        <div className="platform-cell">
          <span className="platform-icon">{platformIcons[value]}</span>
          <span className="platform-name">{value.charAt(0).toUpperCase() + value.slice(1)}</span>
        </div>
      ),
      sortable: true,
      width: '130px',
    },
    {
      key: 'content',
      title: 'Content',
      render: (value, post) => (
        <div className="content-cell">
          <span className="content-text">
            {post.content.length > 100
              ? post.content.substring(0, 100) + '...'
              : post.content}
          </span>
          {post.media.length > 0 && (
            <span className="media-indicator">
              {post.media.length} {post.media.length === 1 ? 'file' : 'files'}
            </span>
          )}
        </div>
      ),
    },
    {
      key: 'status',
      title: 'Status',
      render: (value: SocialPostStatus) => (
        <StatusBadge
          status={value}
          size="sm"
          customColor={statusColors[value]}
        />
      ),
      sortable: true,
      width: '120px',
    },
    {
      key: 'scheduledFor',
      title: 'Scheduled / Published',
      render: (value, post) => (
        <div className="schedule-cell">
          {post.status === 'scheduled' && post.scheduledFor && (
            <span className="scheduled-date">
              {new Date(post.scheduledFor).toLocaleString()}
            </span>
          )}
          {post.status === 'published' && post.publishedAt && (
            <span className="published-date">
              {new Date(post.publishedAt).toLocaleString()}
            </span>
          )}
          {post.status === 'draft' && (
            <span className="draft-label">Draft</span>
          )}
        </div>
      ),
      sortable: true,
      width: '160px',
    },
    {
      key: 'metrics',
      title: 'Metrics',
      render: (value, post) => (
        post.status === 'published' ? (
          <div className="metrics-cell">
            <div className="metric-item">
              <span className="metric-label">Impressions</span>
              <span className="metric-value">{formatCompactNumber(post.metrics.impressions)}</span>
            </div>
            <div className="metric-item">
              <span className="metric-label">Engagement</span>
              <span className="metric-value">{formatCompactNumber(post.metrics.engagement)}</span>
            </div>
            <div className="metric-item">
              <span className="metric-label">Clicks</span>
              <span className="metric-value">{formatCompactNumber(post.metrics.clicks)}</span>
            </div>
          </div>
        ) : (
          <span className="no-metrics">-</span>
        )
      ),
      width: '180px',
    },
    {
      key: 'country',
      title: 'Country',
      render: (value, post) => (
        <span className="country-code">{post.country}</span>
      ),
      sortable: true,
      width: '80px',
    },
    {
      key: 'campaignId',
      title: 'Campaign',
      render: (value, post) => (
        post.campaignId ? (
          <span className="campaign-link">Linked</span>
        ) : (
          <span className="no-campaign">-</span>
        )
      ),
      width: '100px',
    },
    {
      key: 'createdAt',
      title: 'Created',
      render: (value, post) => (
        <span className="date-cell">
          {new Date(post.createdAt).toLocaleDateString()}
        </span>
      ),
      sortable: true,
      width: '100px',
    },
  ];

  return (
    <Table
      columns={columns}
      data={posts}
      keyField="id"
      onRowClick={onPostClick}
      loading={loading}
      className={className}
    />
  );
};

export default SocialPostsTable;
