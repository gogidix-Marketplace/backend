// TeamsTable Component
// Sales teams listing with actions

import React, { useState } from 'react';
import type { SalesTeam, TeamStatus } from '@domain/types';
import { StatusBadge, ActionMenu } from '../common';
import { formatCurrency, formatPercentage } from '@shared';

export interface TeamsTableProps {
  teams: SalesTeam[];
  isLoading?: boolean;
  onTeamClick?: (team: SalesTeam) => void;
  onEdit?: (team: SalesTeam) => void;
  onDelete?: (teamId: string) => void;
  onStatusChange?: (teamId: string, status: TeamStatus) => void;
  className?: string;
}

export const TeamsTable: React.FC<TeamsTableProps> = ({
  teams,
  isLoading = false,
  onTeamClick,
  onEdit,
  onDelete,
  onStatusChange,
  className = '',
}) => {
  const [sortField, setSortField] = useState<keyof SalesTeam>('name');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

  const handleSort = (field: keyof SalesTeam) => {
    if (sortField === field) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortOrder('asc');
    }
  };

  const getSortedTeams = () => {
    return [...teams].sort((a, b) => {
      let aVal: any, bVal: any;

      if (sortField === 'metrics') {
        aVal = a.metrics.quotaAttainment;
        bVal = b.metrics.quotaAttainment;
      } else {
        aVal = a[sortField];
        bVal = b[sortField];
      }

      if (aVal < bVal) return sortOrder === 'asc' ? -1 : 1;
      if (aVal > bVal) return sortOrder === 'asc' ? 1 : -1;
      return 0;
    });
  };

  const getActions = (team: SalesTeam) => [
    { id: 'view', label: 'View Details', icon: '\u{1F50D}', onClick: () => onTeamClick?.(team) },
    { id: 'edit', label: 'Edit Team', icon: '\u270E', onClick: () => onEdit?.(team) },
    { id: 'activate', label: 'Set Active', icon: '\u2705', onClick: () => onStatusChange?.(team.id, 'active'), disabled: team.status === 'active' },
    { id: 'deactivate', label: 'Set Inactive', icon: '\u274C', onClick: () => onStatusChange?.(team.id, 'inactive'), disabled: team.status === 'inactive' },
    { id: 'divider', label: '', divider: true } as any,
    { id: 'delete', label: 'Delete Team', icon: '\uD83D\uDDD1', destructive: true, onClick: () => onDelete?.(team.id) },
  ];

  if (isLoading) {
    return (
      <div className={`teams-table teams-table-loading ${className}`}>
        <div className="table-skeleton">
          <div className="skeleton-header" />
          {[...Array(5)].map((_, i) => (
            <div key={i} className="skeleton-row" />
          ))}
        </div>
      </div>
    );
  }

  if (teams.length === 0) {
    return (
      <div className={`teams-table teams-table-empty ${className}`}>
        <div className="empty-state">
          <span className="empty-state-icon">\uD83D\uDC65</span>
          <h3>No Sales Teams</h3>
          <p>Create your first sales team to get started.</p>
        </div>
      </div>
    );
  }

  const sortedTeams = getSortedTeams();

  return (
    <div className={`teams-table ${className}`}>
      <table className="data-table">
        <thead>
          <tr>
            <th onClick={() => handleSort('name')} className="sortable">
              Team Name
              {sortField === 'name' && <span className="sort-icon">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
            </th>
            <th>Team Lead</th>
            <th onClick={() => handleSort('members')} className="sortable">
              Members
              {sortField === 'members' && <span className="sort-icon">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
            </th>
            <th onClick={() => handleSort('metrics')} className="sortable">
              Quota Attainment
              {sortField === 'metrics' && <span className="sort-icon">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
            </th>
            <th>Revenue (MTD)</th>
            <th>Pipeline Value</th>
            <th>Win Rate</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {sortedTeams.map((team) => (
            <tr key={team.id} className="data-row" onClick={() => onTeamClick?.(team)}>
              <td className="team-name-cell">
                <div>
                  <div className="team-name">{team.name}</div>
                  <div className="team-code">{team.code}</div>
                </div>
              </td>
              <td>
                <div className="team-lead">
                  {team.teamLeadName}
                </div>
              </td>
              <td>
                <span className="member-count">{team.members.length} members</span>
              </td>
              <td>
                <div className="attainment-cell">
                  <div className="attainment-bar">
                    <div
                      className={`attainment-fill ${team.metrics.quotaAttainment >= 100 ? 'attainment-success' : team.metrics.quotaAttainment >= 80 ? 'attainment-warning' : 'attainment-danger'}`}
                      style={{ width: `${Math.min(team.metrics.quotaAttainment, 100)}%` }}
                    />
                  </div>
                  <span className="attainment-value">{team.metrics.quotaAttainment}%</span>
                </div>
              </td>
              <td>{formatCurrency(team.metrics.revenueThisMonth, team.country.currency)}</td>
              <td>{formatCurrency(team.metrics.pipelineValue, 'USD')}</td>
              <td>{formatPercentage(team.metrics.winRate)}</td>
              <td>
                <StatusBadge status={team.status} size="sm" />
              </td>
              <td onClick={(e) => e.stopPropagation()}>
                <ActionMenu actions={getActions(team)} />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TeamsTable;
