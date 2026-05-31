// CampaignsTable Component
// Table component for displaying campaigns

import React from 'react';
import { Table, Column } from './Table';
import { Campaign, CampaignStatus } from '../../domain/entities/Campaign.entity';
import { StatusBadge } from '../common/StatusBadge';
import { formatCurrency, formatPercentage, formatCompactNumber } from '../../utils/formatters';
import { getCampaignStatusColor } from '../../shared/constants/channels';

export interface CampaignsTableProps {
  campaigns: Campaign[];
  onCampaignClick?: (campaign: Campaign) => void;
  className?: string;
  loading?: boolean;
}

export const CampaignsTable: React.FC<CampaignsTableProps> = ({
  campaigns,
  onCampaignClick,
  className = '',
  loading = false,
}) => {
  const columns: Column<Campaign>[] = [
    {
      key: 'name',
      title: 'Campaign',
      render: (value, campaign) => (
        <div className="campaign-name-cell">
          <span className="campaign-name">{campaign.name}</span>
          {campaign.description && (
            <span className="campaign-description">{campaign.description}</span>
          )}
        </div>
      ),
      sortable: true,
    },
    {
      key: 'status',
      title: 'Status',
      render: (value: CampaignStatus) => (
        <StatusBadge status={value} size="sm" />
      ),
      sortable: true,
      width: '120px',
    },
    {
      key: 'type',
      title: 'Type',
      render: (value) => (
        <span className="campaign-type">
          {value.toString().charAt(0).toUpperCase() + value.toString().slice(1)}
        </span>
      ),
      sortable: true,
      width: '120px',
    },
    {
      key: 'channels',
      title: 'Channels',
      render: (value) => (
        <div className="channel-badges">
          {Array.isArray(value) && value.slice(0, 2).map((channel, i) => (
            <span key={i} className="channel-badge">
              {channel}
            </span>
          ))}
          {Array.isArray(value) && value.length > 2 && (
            <span className="channel-badge-more">+{value.length - 2}</span>
          )}
        </div>
      ),
    },
    {
      key: 'countries',
      title: 'Countries',
      render: (value) => (
        <span>{Array.isArray(value) ? value.length : 0}</span>
      ),
      width: '80px',
      align: 'center',
    },
    {
      key: 'budget',
      title: 'Budget',
      render: (value, campaign) => (
        <div className="budget-cell">
          <div className="budget-amount">
            {formatCurrency(campaign.budget.total, campaign.budget.currency)}
          </div>
          <div
            className="budget-bar"
            title={`${formatPercentage((campaign.budget.spent / campaign.budget.total) * 100)} spent`}
          >
            <div
              className="budget-fill"
              style={{
                width: `${(campaign.budget.spent / campaign.budget.total) * 100}%`,
                backgroundColor:
                  (campaign.budget.spent / campaign.budget.total) * 100 > 90
                    ? '#EF4444'
                    : (campaign.budget.spent / campaign.budget.total) * 100 > 75
                    ? '#F59E0B'
                    : '#10B981',
              }}
            />
          </div>
        </div>
      ),
      sortable: true,
      align: 'right',
    },
    {
      key: 'metrics',
      title: 'Performance',
      render: (value, campaign) => (
        <div className="metrics-cell">
          <div className="metric-item">
            <span className="metric-label">Impressions</span>
            <span className="metric-value">{formatCompactNumber(campaign.metrics.impressions)}</span>
          </div>
          <div className="metric-item">
            <span className="metric-label">Clicks</span>
            <span className="metric-value">{formatCompactNumber(campaign.metrics.clicks)}</span>
          </div>
          <div className="metric-item">
            <span className="metric-label">Conv.</span>
            <span className="metric-value">{formatCompactNumber(campaign.metrics.conversions)}</span>
          </div>
        </div>
      ),
    },
    {
      key: 'metrics.roas',
      title: 'ROAS',
      render: (value: number) => (
        <span className={`roas-value ${value >= 3 ? 'roas-good' : value >= 1.5 ? 'roas-ok' : 'roas-poor'}`}>
          {value.toFixed(2)}x
        </span>
      ),
      sortable: true,
      align: 'right',
      width: '80px',
    },
    {
      key: 'dates',
      title: 'Dates',
      render: (value, campaign) => (
        <div className="dates-cell">
          <span className="date-start">
            {new Date(campaign.dates.start).toLocaleDateString()}
          </span>
          <span className="date-sep">→</span>
          <span className="date-end">
            {new Date(campaign.dates.end).toLocaleDateString()}
          </span>
        </div>
      ),
      sortable: true,
    },
  ];

  return (
    <Table
      columns={columns}
      data={campaigns}
      keyField="id"
      onRowClick={onCampaignClick}
      loading={loading}
      className={className}
    />
  );
};

export default CampaignsTable;
