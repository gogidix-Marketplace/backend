// PartnersTable Component
// Partners listing with detailed info

import React, { useState } from 'react';
import type { CountryPartner, PartnerType, PartnerStatus, PartnerTier } from '@domain/types';
import { StatusBadge, ActionMenu } from '../common';
import { PARTNER_TYPES, PARTNER_TIERS } from '@shared';
import { formatCurrency, formatPercentage } from '@shared';

export interface PartnerTableProps {
  partners: CountryPartner[];
  isLoading?: boolean;
  onPartnerClick?: (partner: CountryPartner) => void;
  onEdit?: (partner: CountryPartner) => void;
  onDelete?: (partnerId: string) => void;
  onStatusChange?: (partnerId: string, status: PartnerStatus) => void;
  onTierChange?: (partnerId: string, tier: PartnerTier) => void;
  className?: string;
}

export const PartnersTable: React.FC<PartnerTableProps> = ({
  partners,
  isLoading = false,
  onPartnerClick,
  onEdit,
  onDelete,
  onStatusChange,
  onTierChange,
  className = '',
}) => {
  const [filterType, setFilterType] = useState<PartnerType | 'ALL'>('ALL');
  const [filterStatus, setFilterStatus] = useState<PartnerStatus | 'ALL'>('ALL');

  const getActions = (partner: CountryPartner) => [
    { id: 'view', label: 'View Details', icon: '\u{1F50D}', onClick: () => onPartnerClick?.(partner) },
    { id: 'edit', label: 'Edit Partner', icon: '\u270E', onClick: () => onEdit?.(partner) },
    { id: 'leads', label: 'View Leads', icon: '\u2708', onClick: () => {} },
    { id: 'divider', label: '', divider: true } as any,
    { id: 'activate', label: 'Activate', icon: '\u2705', onClick: () => onStatusChange?.(partner.id, 'ACTIVE'), disabled: partner.status === 'ACTIVE' },
    { id: 'suspend', label: 'Suspend', icon: '\u26A0', onClick: () => onStatusChange?.(partner.id, 'SUSPENDED'), disabled: partner.status === 'SUSPENDED' },
    { id: 'delete', label: 'Terminate', icon: '\uD83D\uDDD1', destructive: true, onClick: () => onDelete?.(partner.id) },
  ];

  const filteredPartners = partners.filter(p => {
    if (filterType !== 'ALL' && p.partnerType !== filterType) return false;
    if (filterStatus !== 'ALL' && p.status !== filterStatus) return false;
    return true;
  });

  if (isLoading) {
    return (
      <div className={`partners-table partners-table-loading ${className}`}>
        <div className="table-skeleton">
          {[...Array(5)].map((_, i) => <div key={i} className="skeleton-row" />)}
        </div>
      </div>
    );
  }

  if (filteredPartners.length === 0) {
    return (
      <div className={`partners-table partners-table-empty ${className}`}>
        <div className="empty-state">
          <span className="empty-state-icon">\uD83E\uDD1D</span>
          <h3>No Partners Found</h3>
          <p>Try adjusting your filters or add new partners.</p>
        </div>
      </div>
    );
  }

  return (
    <div className={`partners-table ${className}`}>
      <div className="table-filters">
        <select
          value={filterType}
          onChange={(e) => setFilterType(e.target.value as PartnerType | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Types</option>
          {Object.entries(PARTNER_TYPES).map(([key, val]) => (
            <option key={key} value={key}>{val.label}</option>
          ))}
        </select>
        <select
          value={filterStatus}
          onChange={(e) => setFilterStatus(e.target.value as PartnerStatus | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Statuses</option>
          <option value="ACTIVE">Active</option>
          <option value="PENDING">Pending</option>
          <option value="SUSPENDED">Suspended</option>
          <option value="TERMINATED">Terminated</option>
        </select>
      </div>

      <table className="data-table">
        <thead>
          <tr>
            <th>Partner</th>
            <th>Type</th>
            <th>Tier</th>
            <th>Customers</th>
            <th>Revenue</th>
            <th>Pipeline</th>
            <th>Conversion Rate</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredPartners.map((partner) => {
            const typeInfo = PARTNER_TYPES[partner.partnerType];
            const tierInfo = PARTNER_TIERS[partner.tier];

            return (
              <tr key={partner.id} className="data-row" onClick={() => onPartnerClick?.(partner)}>
                <td className="partner-info-cell">
                  <div className="partner-avatar">
                    {partner.user.avatar ? (
                      <img src={partner.user.avatar} alt="" />
                    ) : (
                      <span>{partner.user.firstName[0]}{partner.user.lastName[0]}</span>
                    )}
                  </div>
                  <div>
                    <div className="partner-name">
                      {partner.user.firstName} {partner.user.lastName}
                    </div>
                    <div className="partner-email">{partner.user.email}</div>
                  </div>
                </td>
                <td>
                  <span className="partner-type-badge" style={{ color: typeInfo.color }}>
                    {typeInfo.icon} {typeInfo.label}
                  </span>
                </td>
                <td>
                  <span
                    className="tier-badge"
                    style={{ backgroundColor: tierInfo.color + '20', color: tierInfo.color }}
                  >
                    {tierInfo.label}
                  </span>
                </td>
                <td>
                  <div>{partner.customersAcquired}</div>
                  <div className="sub-text">{partner.partnersRecruited} recruited</div>
                </td>
                <td>{formatCurrency(partner.revenueGenerated, partner.assignedCountry.currency)}</td>
                <td>{formatCurrency(partner.pipelineValue, 'USD')}</td>
                <td>{formatPercentage(partner.leadConversionRate)}</td>
                <td>
                  <StatusBadge status={partner.status} size="sm" />
                </td>
                <td onClick={(e) => e.stopPropagation()}>
                  <ActionMenu actions={getActions(partner)} />
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default PartnersTable;
