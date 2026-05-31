// SocialPostsTable Component
// Displays social media posts in a sortable table

import { useState } from 'react';
import { StatusBadge } from '../common';
import './SocialPostsTable.css';

export interface SocialPost {
  id: string;
  content: string;
  platform: 'facebook' | 'instagram' | 'twitter' | 'linkedin' | 'tiktok' | 'youtube';
  status: 'draft' | 'scheduled' | 'published' | 'failed';
  scheduledFor?: string;
  publishedAt?: string;
  metrics: {
    impressions: number;
    reach: number;
    engagement: number;
    likes: number;
    comments: number;
    shares: number;
    clicks: number;
  };
  country: string;
  campaign?: string;
  createdAt: string;
}

interface SocialPostsTableProps {
  posts: SocialPost[];
  onView?: (post: SocialPost) => void;
  onEdit?: (post: SocialPost) => void;
  onDelete?: (postId: string) => void;
  className?: string;
}

type SortField = 'content' | 'platform' | 'status' | 'scheduledFor' | 'engagement' | 'createdAt';
type SortOrder = 'asc' | 'desc';

export function SocialPostsTable({
  posts,
  onView,
  onEdit,
  onDelete,
  className = '',
}: SocialPostsTableProps) {
  const [sortField, setSortField] = useState<SortField>('createdAt');
  const [sortOrder, setSortOrder] = useState<SortOrder>('desc');

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortOrder('desc');
    }
  };

  const sortedPosts = [...posts].sort((a, b) => {
    if (sortField === 'engagement') {
      const aVal = a.metrics.engagement;
      const bVal = b.metrics.engagement;
      return sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
    }
    const aVal = a[sortField];
    const bVal = b[sortField];

    if (typeof aVal === 'string' && typeof bVal === 'string') {
      return sortOrder === 'asc' ? aVal.localeCompare(bVal) : bVal.localeCompare(aVal);
    }

    return 0;
  });

  const getPlatformIcon = (platform: string) => {
    const icons = {
      facebook: '\u{1F3C6}',
      instagram: '\u{1F3CF}',
      twitter: '\u{1F426}',
      linkedin: '\u{1F4BC}',
      tiktok: '\u{1F3B5}',
      youtube: '\u{1F3B5}',
    };
    return icons[platform as keyof typeof icons] || '\u{1F4E2}';
  };

  const getPlatformColor = (platform: string) => {
    const colors = {
      facebook: '#1877f2',
      instagram: '#e4405f',
      twitter: '#1da1f2',
      linkedin: '#0077b5',
      tiktok: '#000000',
      youtube: '#ff0000',
    };
    return colors[platform as keyof typeof colors] || '#6b7280';
  };

  const formatDate = (dateStr?: string) => {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatNumber = (num: number) => {
    if (num >= 1000000) return `${(num / 1000000).toFixed(1)}M`;
    if (num >= 1000) return `${(num / 1000).toFixed(1)}K`;
    return num.toString();
  };

  return (
    <div className={`social-posts-table ${className}`}>
      <div className="table-header">
        <h3 className="table-title">Social Media Posts</h3>
        <div className="table-info">
          {posts.length} posts
        </div>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th onClick={() => handleSort('content')}>
                Content
                {sortField === 'content' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('platform')}>
                Platform
                {sortField === 'platform' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('status')}>
                Status
                {sortField === 'status' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('scheduledFor')}>
                Scheduled/Published
                {sortField === 'scheduledFor' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('engagement')}>
                Engagement
                {sortField === 'engagement' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Metrics</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sortedPosts.map((post) => (
              <tr key={post.id}>
                <td className="post-content">
                  <div className="content-text">
                    {post.content.length > 60 ? post.content.substring(0, 60) + '...' : post.content}
                  </div>
                </td>
                <td>
                  <div
                    className="platform-badge"
                    style={{ backgroundColor: getPlatformColor(post.platform) }}
                  >
                    <span className="platform-icon">{getPlatformIcon(post.platform)}</span>
                    <span className="platform-name">{post.platform}</span>
                  </div>
                </td>
                <td>
                  <StatusBadge status={post.status} />
                </td>
                <td className="date">
                  {post.publishedAt ? formatDate(post.publishedAt) : formatDate(post.scheduledFor)}
                </td>
                <td className="engagement">
                  <span className="engagement-value">{formatNumber(post.metrics.engagement)}</span>
                  <span className="engagement-rate">
                    {post.metrics.impressions > 0
                      ? `${((post.metrics.engagement / post.metrics.impressions) * 100).toFixed(1)}%`
                      : '-'}
                  </span>
                </td>
                <td className="metrics">
                  <div className="metrics-list">
                    <span className="metric">\u{1F44D} {formatNumber(post.metrics.likes)}</span>
                    <span className="metric">\u{1F4AC} {formatNumber(post.metrics.comments)}</span>
                    <span className="metric">\u{1F4E4} {formatNumber(post.metrics.shares)}</span>
                    <span className="metric">\u{1F440} {formatNumber(post.metrics.impressions)}</span>
                  </div>
                </td>
                <td className="actions-cell">
                  <button
                    className="action-btn"
                    onClick={() => onView?.(post)}
                    title="View"
                  >
                    \u{1F50D}
                  </button>
                  <button
                    className="action-btn"
                    onClick={() => onEdit?.(post)}
                    title="Edit"
                  >
                    \u270E
                  </button>
                  <button
                    className="action-btn action-btn-danger"
                    onClick={() => onDelete?.(post.id)}
                    title="Delete"
                  >
                    \u{1F5D1}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
