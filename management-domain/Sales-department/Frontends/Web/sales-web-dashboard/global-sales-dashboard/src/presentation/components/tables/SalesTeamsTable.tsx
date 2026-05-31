// SalesTeamsTable Component
// Displays sales teams across countries with performance metrics

import { useState, useMemo, type React } from 'react';
import type { CountryInfo, GlobalTopPerformer } from '@domain/types';
import { formatCurrency, getInitials } from '@shared/utils';
import { StatusBadge } from '../common/StatusBadge';
import './SalesTeamsTable.css';

export interface SalesTeamMember {
  id: string;
  name: string;
  avatar?: string;
  country: CountryInfo;
  department: string;
  role: 'Director' | 'Manager' | 'Representative';
  revenue: number;
  quota: number;
  attainment: number;
  dealsClosed: number;
  pipelineValue: number;
  winRate: number;
  status: 'active' | 'inactive' | 'on_leave';
}

export interface SalesTeamsTableProps {
  teams: SalesTeamMember[];
  onMemberClick?: (memberId: string) => void;
  className?: string;
  showDepartmentFilter?: boolean;
  showCountryFilter?: boolean;
}

export function SalesTeamsTable({
  teams,
  onMemberClick,
  className = '',
  showDepartmentFilter = true,
  showCountryFilter = true,
}: SalesTeamsTableProps): React.ReactElement {
  const [sortColumn, setSortColumn] = useState<string>('revenue');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');
  const [filterDepartment, setFilterDepartment] = useState<string>('all');
  const [filterCountry, setFilterCountry] = useState<string>('all');

  // Get unique values for filters
  const departments = useMemo(() => {
    const deps = new Set(teams.map(t => t.department));
    return ['all', ...Array.from(deps)];
  }, [teams]);

  const countries = useMemo(() => {
    const ctrys = new Set(teams.map(t => t.country.code));
    return ['all', ...Array.from(ctrys)];
  }, [teams]);

  // Sort teams
  const sortedTeams = useMemo(() => {
    return [...teams].sort((a, b) => {
      const aValue = sortColumn === 'revenue' ? a.revenue
        : sortColumn === 'quota' ? a.quota
        : sortColumn === 'attainment' ? a.attainment
        : sortColumn === 'deals' ? a.dealsClosed
        : sortColumn === 'pipeline' ? a.pipelineValue
        : sortColumn === 'winRate' ? a.winRate
        : a.revenue;

      const bValue = sortColumn === 'revenue' ? b.revenue
        : sortColumn === 'quota' ? b.quota
        : sortColumn === 'attainment' ? b.attainment
        : sortColumn === 'deals' ? b.dealsClosed
        : sortColumn === 'pipeline' ? b.pipelineValue
        : sortColumn === 'winRate' ? b.winRate
        : b.revenue;

      if (sortDirection === 'asc') {
        return aValue - bValue;
      }
      return bValue - aValue;
    });
  }, [teams, sortColumn, sortDirection]);

  // Filter teams
  const filteredTeams = useMemo(() => {
    return sortedTeams.filter(team => {
      if (filterDepartment !== 'all' && team.department !== filterDepartment) return false;
      if (filterCountry !== 'all' && team.country.code !== filterCountry) return false;
      return true;
    });
  }, [sortedTeams, filterDepartment, filterCountry]);

  const handleSort = (column: string) => {
    if (sortColumn === column) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortColumn(column);
      setSortDirection('desc');
    }
  };

  const renderSortIcon = (column: string) => {
    if (sortColumn !== column) return null;
    return sortDirection === 'asc' ? '↑' : '↓';
  };

  const getRankIcon = (index: number) => {
    if (index === 0) return '🥇';
    if (index === 1) return '🥈';
    if (index === 2) return '🥉';
    return `${index + 1}`;
  };

  return (
    <div className={`sales-teams-table ${className}`}>
      {/* Filters */}
      <div className="sales-teams-filters">
        {showDepartmentFilter && (
          <select
            value={filterDepartment}
            onChange={(e) => setFilterDepartment(e.target.value)}
            className="team-filter"
          >
            <option value="all">All Departments</option>
            {departments.slice(1).map(dept => (
              <option key={dept} value={dept}>{dept}</option>
            ))}
          </select>
        )}

        {showCountryFilter && (
          <select
            value={filterCountry}
            onChange={(e) => setFilterCountry(e.target.value)}
            className="team-filter"
          >
            <option value="all">All Countries</option>
            {countries.slice(1).map(country => (
              <option key={country} value={country}>{country}</option>
            ))}
          </select>
        )}
      </div>

      {/* Table */}
      <div className="sales-teams-table-container">
        <table className="sales-teams-data-table">
          <thead>
            <tr>
              <th width="50"></th>
              <th>Team Member</th>
              <th className="sortable" onClick={() => handleSort('revenue')}>
                Revenue {renderSortIcon('revenue')}
              </th>
              <th className="sortable" onClick={() => handleSort('attainment')}>
                Attainment {renderSortIcon('attainment')}
              </th>
              <th className="sortable" onClick={() => handleSort('deals')}>
                Deals {renderSortIcon('deals')}
              </th>
              <th className="sortable" onClick={() => handleSort('pipeline')}>
                Pipeline {renderSortIcon('pipeline')}
              </th>
              <th className="sortable" onClick={() => handleSort('winRate')}>
                Win Rate {renderSortIcon('winRate')}
              </th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {filteredTeams.map((member, index) => (
              <tr
                key={member.id}
                className={onMemberClick ? 'clickable' : ''}
                onClick={() => onMemberClick?.(member.id)}
              >
                <td className="rank-cell">
                  <span className="rank-icon">{getRankIcon(index)}</span>
                </td>
                <td className="member-cell">
                  {member.avatar ? (
                    <img src={member.avatar} alt={member.name} className="member-avatar" />
                  ) : (
                    <div className="member-avatar-placeholder">
                      {getInitials(member.name.split(' ')[0] || '', member.name.split(' ')[1] || '')}
                    </div>
                  )}
                  <div className="member-info">
                    <span className="member-name">{member.name}</span>
                    <span className="member-meta">
                      {member.country.flag} {member.department}
                    </span>
                  </div>
                </td>
                <td className="revenue-cell">
                  <span className="revenue-value">{formatCurrency(member.revenue)}</span>
                </td>
                <td className="attainment-cell">
                  <div className="attainment-bar">
                    <div
                      className={`attainment-fill attainment-fill-${member.attainment >= 100 ? 'high' : member.attainment >= 80 ? 'medium' : 'low'}`}
                      style={{ width: `${Math.min(member.attainment, 100)}%` }}
                    ></div>
                  </div>
                  <span className="attainment-value">{member.attainment}%</span>
                </td>
                <td className="deals-cell">{member.dealsClosed}</td>
                <td className="pipeline-cell">{formatCurrency(member.pipelineValue)}</td>
                <td className="winrate-cell">{member.winRate}%</td>
                <td className="status-cell">
                  <StatusBadge status={member.status} size="sm" variant="subtle" />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Summary */}
      <div className="sales-teams-summary">
        <span>{filteredTeams.length} team members</span>
        <span>Total Revenue: {formatCurrency(filteredTeams.reduce((sum, m) => sum + m.revenue, 0))}</span>
        <span>Avg Attainment: {Math.round(filteredTeams.reduce((sum, m) => sum + m.attainment, 0) / filteredTeams.length || 0)}%</span>
      </div>
    </div>
  );
}

export default SalesTeamsTable;
