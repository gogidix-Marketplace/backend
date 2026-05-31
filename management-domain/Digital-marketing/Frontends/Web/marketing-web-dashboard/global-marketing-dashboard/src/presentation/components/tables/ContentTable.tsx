// ContentTable Component
// Displays content pieces in a sortable table

import { useState } from 'react';
import { StatusBadge } from '../common';
import './ContentTable.css';

export interface Content {
  id: string;
  title: string;
  type: string;
  status: 'draft' | 'pending_approval' | 'approved' | 'scheduled' | 'published' | 'archived';
  author: string;
  category: string;
  tags: string[];
  views: number;
  clicks: number;
  shares: number;
  leads: number;
  engagementRate: number;
  publishedAt?: string;
  scheduledFor?: string;
  createdAt: string;
}

interface ContentTableProps {
  content: Content[];
  onView?: (item: Content) => void;
  onEdit?: (item: Content) => void;
  onDelete?: (contentId: string) => void;
  className?: string;
}

type SortField = 'title' | 'type' | 'status' | 'views' | 'clicks' | 'engagementRate' | 'createdAt';
type SortOrder = 'asc' | 'desc';

export function ContentTable({
  content,
  onView,
  onEdit,
  onDelete,
  className = '',
}: ContentTableProps) {
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

  const sortedContent = [...content].sort((a, b) => {
    const aVal = a[sortField];
    const bVal = b[sortField];

    if (typeof aVal === 'string' && typeof bVal === 'string') {
      return sortOrder === 'asc' ? aVal.localeCompare(bVal) : bVal.localeCompare(aVal);
    }

    if (typeof aVal === 'number' && typeof bVal === 'number') {
      return sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
    }

    return 0;
  });

  const getTypeIcon = (type: string) => {
    const icons = {
      blog_post: '\u{1F4D0}',
      social_post: '\u{1F4E2}',
      email: '\u2709',
      video: '\u{1F3B5}',
      infographic: '\u{1F4CA}',
      whitepaper: '\u{1F4C4}',
      case_study: '\u{1F4DC}',
      ebook: '\u{1F4DA}',
      webinar: '\u{1F3A5}',
      podcast: '\u{1F399}',
      press_release: '\u{1F4F0}',
      landing_page: '\u{1F3E0}',
    };
    return icons[type as keyof typeof icons] || '\u{1F4C4}';
  };

  const formatDate = (dateStr?: string) => {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    });
  };

  const formatNumber = (num: number) => {
    if (num >= 1000000) return `${(num / 1000000).toFixed(1)}M`;
    if (num >= 1000) return `${(num / 1000).toFixed(1)}K`;
    return num.toString();
  };

  return (
    <div className={`content-table ${className}`}>
      <div className="table-header">
        <h3 className="table-title">Content Library</h3>
        <div className="table-info">
          {content.length} items
        </div>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th onClick={() => handleSort('title')}>
                Content
                {sortField === 'title' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('type')}>
                Type
                {sortField === 'type' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('status')}>
                Status
                {sortField === 'status' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Author</th>
              <th>Category</th>
              <th onClick={() => handleSort('views')}>
                Views
                {sortField === 'views' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('clicks')}>
                Clicks
                {sortField === 'clicks' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('engagementRate')}>
                Engagement
                {sortField === 'engagementRate' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Published</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sortedContent.map((item) => (
              <tr key={item.id}>
                <td className="content-title">
                  <div>
                    <div className="title">{item.title}</div>
                    {item.tags.length > 0 && (
                      <div className="tags">
                        {item.tags.slice(0, 2).map((tag, i) => (
                          <span key={i} className="tag">{tag}</span>
                        ))}
                        {item.tags.length > 2 && (
                          <span className="tag-more">+{item.tags.length - 2}</span>
                        )}
                      </div>
                    )}
                  </div>
                </td>
                <td>
                  <div className="type-badge">
                    <span className="type-icon">{getTypeIcon(item.type)}</span>
                    <span className="type-name">{item.type.replace(/_/g, ' ')}</span>
                  </div>
                </td>
                <td>
                  <StatusBadge status={item.status} />
                </td>
                <td className="author">{item.author}</td>
                <td className="category">{item.category}</td>
                <td className="metric views">{formatNumber(item.views)}</td>
                <td className="metric clicks">{formatNumber(item.clicks)}</td>
                <td className="metric engagement">
                  <span className={`engagement-value ${item.engagementRate >= 3 ? 'high' : item.engagementRate >= 1 ? 'medium' : 'low'}`}>
                    {item.engagementRate.toFixed(2)}%
                  </span>
                </td>
                <td className="date">{formatDate(item.publishedAt || item.scheduledFor)}</td>
                <td className="actions-cell">
                  <button
                    className="action-btn"
                    onClick={() => onView?.(item)}
                    title="View"
                  >
                    \u{1F50D}
                  </button>
                  <button
                    className="action-btn"
                    onClick={() => onEdit?.(item)}
                    title="Edit"
                  >
                    \u270E
                  </button>
                  <button
                    className="action-btn action-btn-danger"
                    onClick={() => onDelete?.(item.id)}
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
