// CampaignsTable Component
// Displays campaigns in a sortable, filterable table

import { useState } from 'react';
import { StatusBadge } from '../common';
import './CampaignsTable.css';

export interface Campaign {
  id: string;
  name: string;
  status: 'draft' | 'scheduled' | 'active' | 'paused' | 'completed' | 'cancelled';
  type: string;
  channels: string[];
  budget: number;
  spent: number;
  revenue: number;
  roi: number;
  impressions: number;
  clicks: number;
  conversions: number;
  startDate: string;
  endDate: string;
  country: string;
}

interface CampaignsTableProps {
  campaigns: Campaign[];
  onRowClick?: (campaign: Campaign) => void;
  onEdit?: (campaign: Campaign) => void;
  onDelete?: (campaignId: string) => void;
  className?: string;
}

type SortField = 'name' | 'budget' | 'spent' | 'revenue' | 'roi' | 'impressions' | 'clicks' | 'conversions';
type SortOrder = 'asc' | 'desc';

export function CampaignsTable({
  campaigns,
  onRowClick,
  onEdit,
  onDelete,
  className = '',
}: CampaignsTableProps) {
  const [sortField, setSortField] = useState<SortField>('name');
  const [sortOrder, setSortOrder] = useState<SortOrder>('asc');
  const [selectedCampaigns, setSelectedCampaigns] = useState<Set<string>>(new Set());

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortOrder('asc');
    }
  };

  const sortedCampaigns = [...campaigns].sort((a, b) => {
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

  const handleSelectAll = () => {
    if (selectedCampaigns.size === campaigns.length) {
      setSelectedCampaigns(new Set());
    } else {
      setSelectedCampaigns(new Set(campaigns.map(c => c.id)));
    }
  };

  const handleSelectCampaign = (id: string) => {
    const newSelected = new Set(selectedCampaigns);
    if (newSelected.has(id)) {
      newSelected.delete(id);
    } else {
      newSelected.add(id);
    }
    setSelectedCampaigns(newSelected);
  };

  const formatCurrency = (value: number) => {
    if (value >= 1000000) return `$${(value / 1000000).toFixed(1)}M`;
    if (value >= 1000) return `$${(value / 1000).toFixed(1)}K`;
    return `$${value.toFixed(0)}`;
  };

  const formatNumber = (value: number) => {
    if (value >= 1000000) return `${(value / 1000000).toFixed(1)}M`;
    if (value >= 1000) return `${(value / 1000).toFixed(1)}K`;
    return value.toString();
  };

  return (
    <div className={`campaigns-table ${className}`}>
      <div className="table-header">
        <div className="table-actions">
          {selectedCampaigns.size > 0 && (
            <span className="selected-count">{selectedCampaigns.size} selected</span>
          )}
        </div>
        <div className="table-info">
          Showing {campaigns.length} campaigns
        </div>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th className="checkbox-cell">
                <input
                  type="checkbox"
                  checked={selectedCampaigns.size === campaigns.length && campaigns.length > 0}
                  onChange={handleSelectAll}
                />
              </th>
              <th onClick={() => handleSort('name')}>
                Campaign
                {sortField === 'name' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Status</th>
              <th>Channels</th>
              <th onClick={() => handleSort('budget')}>
                Budget
                {sortField === 'budget' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('spent')}>
                Spent
                {sortField === 'spent' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('revenue')}>
                Revenue
                {sortField === 'revenue' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('roi')}>
                ROI
                {sortField === 'roi' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('conversions')}>
                Conversions
                {sortField === 'conversions' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sortedCampaigns.map((campaign) => (
              <tr
                key={campaign.id}
                className={selectedCampaigns.has(campaign.id) ? 'row-selected' : ''}
                onClick={() => onRowClick?.(campaign)}
              >
                <td className="checkbox-cell" onClick={(e) => e.stopPropagation()}>
                  <input
                    type="checkbox"
                    checked={selectedCampaigns.has(campaign.id)}
                    onChange={() => handleSelectCampaign(campaign.id)}
                  />
                </td>
                <td className="campaign-name">
                  <div>
                    <div className="name">{campaign.name}</div>
                    <div className="type">{campaign.type}</div>
                  </div>
                </td>
                <td onClick={(e) => e.stopPropagation()}>
                  <StatusBadge status={campaign.status} />
                </td>
                <td>
                  <div className="channels">
                    {campaign.channels.slice(0, 2).map((ch, i) => (
                      <span key={i} className="channel-tag">{ch}</span>
                    ))}
                    {campaign.channels.length > 2 && (
                      <span className="channel-more">+{campaign.channels.length - 2}</span>
                    )}
                  </div>
                </td>
                <td>{formatCurrency(campaign.budget)}</td>
                <td>{formatCurrency(campaign.spent)}</td>
                <td>{formatCurrency(campaign.revenue)}</td>
                <td className={campaign.roi >= 0 ? 'positive' : 'negative'}>
                  {campaign.roi.toFixed(1)}%
                </td>
                <td>{formatNumber(campaign.conversions)}</td>
                <td className="actions-cell" onClick={(e) => e.stopPropagation()}>
                  <button
                    className="action-btn"
                    onClick={() => onEdit?.(campaign)}
                    title="Edit"
                  >
                    \u270E
                  </button>
                  <button
                    className="action-btn action-btn-danger"
                    onClick={() => onDelete?.(campaign.id)}
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
