// DealsTable Component
// Deals in pipeline listing

import React, { useState } from 'react';
import type { Deal, DealStage, DealPriority } from '@domain/types';
import { StatusBadge, ActionMenu } from '../common';
import { DEAL_STAGES } from '@shared';
import { formatCurrency, formatDate, formatRelativeTime } from '@shared';

export interface DealsTableProps {
  deals: Deal[];
  isLoading?: boolean;
  onDealClick?: (deal: Deal) => void;
  onEdit?: (deal: Deal) => void;
  onDelete?: (dealId: string) => void;
  onStageChange?: (dealId: string, stage: DealStage) => void;
  className?: string;
}

export const DealsTable: React.FC<DealsTableProps> = ({
  deals,
  isLoading = false,
  onDealClick,
  onEdit,
  onDelete,
  onStageChange,
  className = '',
}) => {
  const [stageFilter, setStageFilter] = useState<DealStage | 'ALL'>('ALL');
  const [priorityFilter, setPriorityFilter] = useState<DealPriority | 'ALL'>('ALL');

  const getStageActions = (deal: Deal) => {
    const stages = Object.keys(DEAL_STAGES) as DealStage[];
    return stages
      .filter(s => s !== deal.stage && s !== 'WON' && s !== 'LOST')
      .map(stage => ({
        id: stage,
        label: DEAL_STAGES[stage].label,
        onClick: () => onStageChange?.(deal.id, stage),
      }));
  };

  const getActions = (deal: Deal) => [
    { id: 'view', label: 'View Details', icon: '\u{1F50D}', onClick: () => onDealClick?.(deal) },
    { id: 'edit', label: 'Edit Deal', icon: '\u270E', onClick: () => onEdit?.(deal) },
    { id: 'stage', label: 'Move Stage', icon: '\u2192', onClick: () => {} },
    { id: 'divider', label: '', divider: true } as any,
    { id: 'won', label: 'Mark as Won', icon: '\u2705', onClick: () => onStageChange?.(deal.id, 'WON'), disabled: deal.stage === 'WON' || deal.stage === 'LOST' },
    { id: 'lost', label: 'Mark as Lost', icon: '\u274C', destructive: true, onClick: () => onStageChange?.(deal.id, 'LOST'), disabled: deal.stage === 'WON' || deal.stage === 'LOST' },
    { id: 'delete', label: 'Delete', icon: '\uD83D\uDDD1', destructive: true, onClick: () => onDelete?.(deal.id) },
  ];

  const filteredDeals = deals.filter(d => {
    if (stageFilter !== 'ALL' && d.stage !== stageFilter) return false;
    if (priorityFilter !== 'ALL' && d.priority !== priorityFilter) return false;
    return true;
  });

  const getPriorityColor = (priority: DealPriority) => {
    const colors = {
      urgent: '#DC2626',
      high: '#EF4444',
      medium: '#F59E0B',
      low: '#3B82F6',
    };
    return colors[priority];
  };

  if (isLoading) {
    return (
      <div className={`deals-table deals-table-loading ${className}`}>
        <div className="table-skeleton">
          {[...Array(5)].map((_, i) => <div key={i} className="skeleton-row" />)}
        </div>
      </div>
    );
  }

  if (filteredDeals.length === 0) {
    return (
      <div className={`deals-table deals-table-empty ${className}`}>
        <div className="empty-state">
          <span className="empty-state-icon">\uD83D\uDCC8</span>
          <h3>No Deals Found</h3>
          <p>Try adjusting your filters.</p>
        </div>
      </div>
    );
  }

  return (
    <div className={`deals-table ${className}`}>
      <div className="table-filters">
        <select
          value={stageFilter}
          onChange={(e) => setStageFilter(e.target.value as DealStage | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Stages</option>
          {Object.entries(DEAL_STAGES).map(([key, val]) => (
            <option key={key} value={key}>{val.label}</option>
          ))}
        </select>
        <select
          value={priorityFilter}
          onChange={(e) => setPriorityFilter(e.target.value as DealPriority | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Priorities</option>
          <option value="urgent">Urgent</option>
          <option value="high">High</option>
          <option value="medium">Medium</option>
          <option value="low">Low</option>
        </select>
      </div>

      <table className="data-table">
        <thead>
          <tr>
            <th>Deal</th>
            <th>Account</th>
            <th>Owner</th>
            <th>Stage</th>
            <th>Value</th>
            <th>Probability</th>
            <th>Expected Close</th>
            <th>Priority</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredDeals.map((deal) => {
            const stageInfo = DEAL_STAGES[deal.stage];

            return (
              <tr key={deal.id} className="data-row" onClick={() => onDealClick?.(deal)}>
                <td className="deal-name-cell">
                  <div>
                    <div className="deal-name">{deal.name}</div>
                    <div className="deal-number">{deal.dealNumber}</div>
                  </div>
                </td>
                <td>
                  <div className="account-name">{deal.accountName}</div>
                  {deal.contactName && (
                    <div className="contact-name">{deal.contactName}</div>
                  )}
                </td>
                <td>
                  <div className="owner-info">
                    <div className="owner-name">{deal.owner.name}</div>
                    {deal.owner.team && (
                      <div className="owner-team">{deal.owner.team}</div>
                    )}
                  </div>
                </td>
                <td>
                  <StatusBadge status={deal.stage} size="sm" />
                </td>
                <td>
                  <div className="deal-value">{formatCurrency(deal.value, deal.currency)}</div>
                </td>
                <td>
                  <div className="probability-bar">
                    <div
                      className="probability-fill"
                      style={{ width: `${deal.probability}%`, backgroundColor: stageInfo.color }}
                    />
                  </div>
                  <span className="probability-value">{deal.probability}%</span>
                </td>
                <td>
                  <div className="expected-close">
                    <div>{formatDate(deal.expectedCloseDate, 'MMM d, yyyy')}</div>
                    <div className="sub-text">{formatRelativeTime(deal.expectedCloseDate)}</div>
                  </div>
                </td>
                <td>
                  <span
                    className="priority-dot"
                    style={{ backgroundColor: getPriorityColor(deal.priority) }}
                    title={deal.priority}
                  />
                </td>
                <td onClick={(e) => e.stopPropagation()}>
                  <ActionMenu actions={getActions(deal)} />
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default DealsTable;
