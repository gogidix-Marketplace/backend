// LeadsTable Component
// Table component for displaying leads

import React from 'react';
import { Table, Column } from './Table';
import { Lead, LeadStatus, LeadQuality, LeadSource } from '../../domain/types';
import { StatusBadge } from '../common/StatusBadge';
import { formatCurrency, formatCompactNumber } from '../../utils/formatters';

export interface LeadsTableProps {
  leads: Lead[];
  onLeadClick?: (lead: Lead) => void;
  className?: string;
  loading?: boolean;
}

const statusColors: Record<LeadStatus, string> = {
  new: '#3B82F6',
  contacted: '#F59E0B',
  qualified: '#10B981',
  proposal: '#8B5CF6',
  negotiation: '#EC4899',
  won: '#059669',
  lost: '#EF4444',
};

const qualityColors: Record<LeadQuality, string> = {
  hot: '#EF4444',
  warm: '#F59E0B',
  cold: '#3B82F6',
  unqualified: '#9CA3AF',
};

const qualityBadge = (quality: LeadQuality) => (
  <span
    className="quality-badge"
    style={{
      backgroundColor: qualityColors[quality] + '20',
      color: qualityColors[quality],
      padding: '0.125rem 0.5rem',
      borderRadius: '0.25rem',
      fontSize: '0.75rem',
      fontWeight: '500',
    }}
  >
    {quality.toUpperCase()}
  </span>
);

export const LeadsTable: React.FC<LeadsTableProps> = ({
  leads,
  onLeadClick,
  className = '',
  loading = false,
}) => {
  const columns: Column<Lead>[] = [
    {
      key: 'name',
      title: 'Lead',
      render: (value, lead) => (
        <div className="lead-name-cell">
          <span className="lead-name">{lead.firstName} {lead.lastName}</span>
          {lead.company && (
            <span className="lead-company">{lead.company}</span>
          )}
          {lead.jobTitle && (
            <span className="lead-title">{lead.jobTitle}</span>
          )}
        </div>
      ),
      sortable: true,
    },
    {
      key: 'email',
      title: 'Contact',
      render: (value, lead) => (
        <div className="contact-cell">
          <span className="contact-email">{lead.email}</span>
          {lead.phone && (
            <span className="contact-phone">{lead.phone}</span>
          )}
        </div>
      ),
    },
    {
      key: 'status',
      title: 'Status',
      render: (value: LeadStatus) => (
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
      key: 'quality',
      title: 'Quality',
      render: (value: LeadQuality) => qualityBadge(value),
      sortable: true,
      width: '100px',
    },
    {
      key: 'source',
      title: 'Source',
      render: (value: LeadSource, lead) => (
        <div className="source-cell">
          <span className="source-badge">{lead.campaign.name}</span>
          <span className="source-channel">{lead.campaign.channel}</span>
        </div>
      ),
      sortable: true,
    },
    {
      key: 'country',
      title: 'Country',
      render: (value, lead) => (
        <span>{lead.country.name}</span>
      ),
      sortable: true,
      width: '120px',
    },
    {
      key: 'score',
      title: 'Score',
      render: (value: number) => (
        <div className="score-cell">
          <div
            className="score-bar"
            title={`Lead score: ${value}`}
          >
            <div
              className="score-fill"
              style={{
                width: `${Math.min(value, 100)}%`,
                backgroundColor: value >= 80 ? '#10B981' : value >= 50 ? '#F59E0B' : '#EF4444',
              }}
            />
          </div>
          <span className="score-value">{value}</span>
        </div>
      ),
      sortable: true,
      width: '100px',
      align: 'center',
    },
    {
      key: 'value',
      title: 'Value',
      render: (value, lead) => (
        <div className="value-cell">
          <span className="value-amount">
            {formatCurrency(lead.value.estimated, lead.value.currency)}
          </span>
          {lead.value.actual && (
            <span className="value-actual">
              {formatCurrency(lead.value.actual, lead.value.currency)}
            </span>
          )}
        </div>
      ),
      sortable: true,
      align: 'right',
    },
    {
      key: 'assignedTo',
      title: 'Assigned To',
      render: (value, lead) => (
        lead.assignedTo ? (
          <div className="assigned-cell">
            <span className="assigned-name">{lead.assignedTo.name}</span>
          </div>
        ) : (
          <span className="unassigned">Unassigned</span>
        )
      ),
      width: '150px',
    },
    {
      key: 'createdAt',
      title: 'Created',
      render: (value, lead) => (
        <span className="date-cell">
          {new Date(lead.createdAt).toLocaleDateString()}
        </span>
      ),
      sortable: true,
      width: '100px',
    },
  ];

  return (
    <Table
      columns={columns}
      data={leads}
      keyField="id"
      onRowClick={onLeadClick}
      loading={loading}
      className={className}
    />
  );
};

export default LeadsTable;
