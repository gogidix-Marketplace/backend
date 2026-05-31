// MembersTable Component
// Team members listing

import React, { useState } from 'react';
import type { TeamMember, CountrySalesRole } from '@domain/types';
import { StatusBadge, ActionMenu } from '../common';
import { SALES_ROLES } from '@shared';
import { formatCurrency, formatPercentage } from '@shared';

export interface MembersTableProps {
  members: TeamMember[];
  teamName?: string;
  isLoading?: boolean;
  onMemberClick?: (member: TeamMember) => void;
  onEdit?: (member: TeamMember) => void;
  onRemove?: (memberId: string) => void;
  onStatusChange?: (memberId: string, status: 'active' | 'inactive' | 'on_leave') => void;
  className?: string;
}

export const MembersTable: React.FC<MembersTableProps> = ({
  members,
  teamName,
  isLoading = false,
  onMemberClick,
  onEdit,
  onRemove,
  onStatusChange,
  className = '',
}) => {
  const [roleFilter, setRoleFilter] = useState<CountrySalesRole | 'ALL'>('ALL');
  const [statusFilter, setStatusFilter] = useState<'active' | 'inactive' | 'on_leave' | 'ALL'>('ALL');

  const getActions = (member: TeamMember) => [
    { id: 'view', label: 'View Profile', icon: '\u{1F50D}', onClick: () => onMemberClick?.(member) },
    { id: 'edit', label: 'Edit Member', icon: '\u270E', onClick: () => onEdit?.(member) },
    { id: 'performance', label: 'View Performance', icon: '\uD83D\uDCCA', onClick: () => {} },
    { id: 'divider', label: '', divider: true } as any,
    { id: 'activate', label: 'Set Active', icon: '\u2705', onClick: () => onStatusChange?.(member.userId, 'active'), disabled: member.status === 'active' },
    { id: 'leave', label: 'Set On Leave', icon: '\uD83C\uDFD2', onClick: () => onStatusChange?.(member.userId, 'on_leave'), disabled: member.status === 'on_leave' },
    { id: 'remove', label: 'Remove from Team', icon: '\uD83D\uDDD1', destructive: true, onClick: () => onRemove?.(member.userId) },
  ];

  const filteredMembers = members.filter(m => {
    if (roleFilter !== 'ALL' && m.role !== roleFilter) return false;
    if (statusFilter !== 'ALL' && m.status !== statusFilter) return false;
    return true;
  });

  const getRoleLabel = (role: CountrySalesRole) => {
    return SALES_ROLES[role]?.label || role;
  };

  if (isLoading) {
    return (
      <div className={`members-table members-table-loading ${className}`}>
        <div className="table-skeleton">
          {[...Array(5)].map((_, i) => <div key={i} className="skeleton-row" />)}
        </div>
      </div>
    );
  }

  if (filteredMembers.length === 0) {
    return (
      <div className={`members-table members-table-empty ${className}`}>
        <div className="empty-state">
          <span className="empty-state-icon">\uD83D\uDC65</span>
          <h3>No Team Members</h3>
          <p>Add members to {teamName || 'this team'} to get started.</p>
        </div>
      </div>
    );
  }

  return (
    <div className={`members-table ${className}`}>
      <div className="table-filters">
        <select
          value={roleFilter}
          onChange={(e) => setRoleFilter(e.target.value as CountrySalesRole | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Roles</option>
          {Object.entries(SALES_ROLES).map(([key, val]) => (
            <option key={key} value={key}>{val.label}</option>
          ))}
        </select>
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as any)}
          className="filter-select"
        >
          <option value="ALL">All Statuses</option>
          <option value="active">Active</option>
          <option value="inactive">Inactive</option>
          <option value="on_leave">On Leave</option>
        </select>
      </div>

      <table className="data-table">
        <thead>
          <tr>
            <th>Member</th>
            <th>Role</th>
            <th>Individual Quota</th>
            <th>Revenue Generated</th>
            <th>Attainment</th>
            <th>Joined</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredMembers.map((member) => (
            <tr key={member.userId} className="data-row" onClick={() => onMemberClick?.(member)}>
              <td className="member-info-cell">
                <div className="member-avatar">
                  {member.avatar ? (
                    <img src={member.avatar} alt="" />
                  ) : (
                    <span>{member.name.split(' ').map(n => n[0]).join('')}</span>
                  )}
                </div>
                <div>
                  <div className="member-name">{member.name}</div>
                  <div className="member-email">{member.email}</div>
                </div>
              </td>
              <td>
                <span className="role-badge">{getRoleLabel(member.role)}</span>
              </td>
              <td>{formatCurrency(member.individualQuota, 'USD')}</td>
              <td>
                <div className="revenue-cell">
                  <span className="revenue-value">{formatCurrency(member.revenueGenerated, 'USD')}</span>
                </div>
              </td>
              <td>
                <div className="attainment-cell">
                  <div className="attainment-bar">
                    <div
                      className={`attainment-fill ${member.attainment >= 100 ? 'attainment-success' : member.attainment >= 80 ? 'attainment-warning' : 'attainment-danger'}`}
                      style={{ width: `${Math.min(member.attainment, 100)}%` }}
                    />
                  </div>
                  <span className="attainment-value">{formatPercentage(member.attainment)}</span>
                </div>
              </td>
              <td>
                <div className="joined-date">
                  {new Date(member.joinedAt).toLocaleDateString()}
                </div>
              </td>
              <td>
                <StatusBadge status={member.status} size="sm" />
              </td>
              <td onClick={(e) => e.stopPropagation()}>
                <ActionMenu actions={getActions(member)} />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MembersTable;
