// ContentTable Component
// Table component for displaying content items

import React from 'react';
import { Table, Column } from './Table';
import { Content, ContentStatus, ContentType } from '../../domain/types';
import { StatusBadge } from '../common/StatusBadge';
import { formatCompactNumber } from '../../utils/formatters';

export interface ContentTableProps {
  content: Content[];
  onContentClick?: (content: Content) => void;
  className?: string;
  loading?: boolean;
}

const statusColors: Record<ContentStatus, string> = {
  draft: '#9CA3AF',
  pending_approval: '#F59E0B',
  approved: '#3B82F6',
  scheduled: '#8B5CF6',
  published: '#10B981',
  archived: '#6B7280',
};

const contentTypeIcons: Record<ContentType, string> = {
  blog_post: '📝',
  social_post: '📱',
  email: '✉️',
  video: '🎬',
  infographic: '📊',
  whitepaper: '📄',
  case_study: '📋',
  ebook: '📚',
  webinar: '🎥',
  podcast: '🎙️',
  press_release: '📰',
  landing_page: '🌐',
  other: '📎',
};

export const ContentTable: React.FC<ContentTableProps> = ({
  content,
  onContentClick,
  className = '',
  loading = false,
}) => {
  const columns: Column<Content>[] = [
    {
      key: 'title',
      title: 'Content',
      render: (value, item) => (
        <div className="content-title-cell">
          <span className="content-type-icon">{contentTypeIcons[item.type]}</span>
          <div className="content-info">
            <span className="content-title">{item.title}</span>
            {item.excerpt && (
              <span className="content-excerpt">
                {item.excerpt.length > 60
                  ? item.excerpt.substring(0, 60) + '...'
                  : item.excerpt}
              </span>
            )}
          </div>
        </div>
      ),
      sortable: true,
    },
    {
      key: 'type',
      title: 'Type',
      render: (value: ContentType) => (
        <span className="content-type">
          {value.split('_').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')}
        </span>
      ),
      sortable: true,
      width: '140px',
    },
    {
      key: 'status',
      title: 'Status',
      render: (value: ContentStatus) => (
        <StatusBadge
          status={value}
          size="sm"
          customColor={statusColors[value]}
        />
      ),
      sortable: true,
      width: '140px',
    },
    {
      key: 'author',
      title: 'Author',
      render: (value, item) => (
        <div className="author-cell">
          <span className="author-name">{item.author.name}</span>
        </div>
      ),
      sortable: true,
      width: '140px',
    },
    {
      key: 'metrics',
      title: 'Performance',
      render: (value, item) => (
        item.status === 'published' ? (
          <div className="content-metrics-cell">
            <div className="metric-inline">
              <span className="metric-label">Views</span>
              <span className="metric-value">{formatCompactNumber(item.metrics.views)}</span>
            </div>
            <div className="metric-inline">
              <span className="metric-label">Leads</span>
              <span className="metric-value">{formatCompactNumber(item.metrics.leads)}</span>
            </div>
          </div>
        ) : (
          <span className="no-metrics">-</span>
        )
      ),
      width: '180px',
    },
    {
      key: 'publishing',
      title: 'Publishing',
      render: (value, item) => (
        <div className="publishing-cell">
          {item.status === 'published' && item.publishing.publishedAt && (
            <span className="published-date">
              {new Date(item.publishing.publishedAt).toLocaleDateString()}
            </span>
          )}
          {item.status === 'scheduled' && item.publishing.publishAt && (
            <span className="scheduled-date">
              {new Date(item.publishing.publishAt).toLocaleDateString()}
            </span>
          )}
          {item.status !== 'published' && item.status !== 'scheduled' && (
            <span className="not-published">-</span>
          )}
        </div>
      ),
      sortable: true,
      width: '120px',
    },
    {
      key: 'approval',
      title: 'Approval',
      render: (value, item) => (
        <div className="approval-cell">
          <span className={`approval-status approval-${item.approval.status}`}>
            {item.approval.status === 'none' ? 'N/A' : item.approval.status.replace('_', ' ')}
          </span>
        </div>
      ),
      width: '120px',
    },
    {
      key: 'tags',
      title: 'Tags',
      render: (value, item) => (
        <div className="tags-cell">
          {item.tags.slice(0, 3).map((tag, i) => (
            <span key={i} className="tag-badge">
              {tag}
            </span>
          ))}
          {item.tags.length > 3 && (
            <span className="tag-more">+{item.tags.length - 3}</span>
          )}
        </div>
      ),
      width: '160px',
    },
    {
      key: 'createdAt',
      title: 'Created',
      render: (value, item) => (
        <span className="date-cell">
          {new Date(item.createdAt).toLocaleDateString()}
        </span>
      ),
      sortable: true,
      width: '100px',
    },
  ];

  return (
    <Table
      columns={columns}
      data={content}
      keyField="id"
      onRowClick={onContentClick}
      loading={loading}
      className={className}
    />
  );
};

export default ContentTable;
