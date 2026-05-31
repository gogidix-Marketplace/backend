// LeadsTable Component
// Displays leads in a sortable, filterable table

import { useState } from 'react';
import { StatusBadge } from '../common';
import './LeadsTable.css';

export interface Lead {
  id: string;
  name: string;
  email: string;
  company?: string;
  status: 'new' | 'contacted' | 'qualified' | 'proposal' | 'negotiation' | 'won' | 'lost';
  quality: 'hot' | 'warm' | 'cold';
  source: string;
  campaign: string;
  value: number;
  score: number;
  createdAt: string;
  assignedTo?: string;
}

interface LeadsTableProps {
  leads: Lead[];
  onRowClick?: (lead: Lead) => void;
  onEdit?: (lead: Lead) => void;
  onDelete?: (leadId: string) => void;
  className?: string;
}

type SortField = 'name' | 'status' | 'quality' | 'source' | 'value' | 'score' | 'createdAt';
type SortOrder = 'asc' | 'desc';

export function LeadsTable({
  leads,
  onRowClick,
  onEdit,
  onDelete,
  className = '',
}: LeadsTableProps) {
  const [sortField, setSortField] = useState<SortField>('createdAt');
  const [sortOrder, setSortOrder] = useState<SortOrder>('desc');
  const [selectedLeads, setSelectedLeads] = useState<Set<string>>(new Set());

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortOrder('asc');
    }
  };

  const sortedLeads = [...leads].sort((a, b) => {
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
    if (selectedLeads.size === leads.length) {
      setSelectedLeads(new Set());
    } else {
      setSelectedLeads(new Set(leads.map(l => l.id)));
    }
  };

  const handleSelectLead = (id: string) => {
    const newSelected = new Set(selectedLeads);
    if (newSelected.has(id)) {
      newSelected.delete(id);
    } else {
      newSelected.add(id);
    }
    setSelectedLeads(newSelected);
  };

  const getScoreColor = (score: number) => {
    if (score >= 80) return 'score-hot';
    if (score >= 50) return 'score-warm';
    return 'score-cold';
  };

  const formatDate = (dateStr: string) => {
    return new Date(dateStr).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    });
  };

  return (
    <div className={`leads-table ${className}`}>
      <div className="table-header">
        <div className="table-actions">
          {selectedLeads.size > 0 && (
            <span className="selected-count">{selectedLeads.size} selected</span>
          )}
        </div>
        <div className="table-info">
          Showing {leads.length} leads
        </div>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th className="checkbox-cell">
                <input
                  type="checkbox"
                  checked={selectedLeads.size === leads.length && leads.length > 0}
                  onChange={handleSelectAll}
                />
              </th>
              <th onClick={() => handleSort('name')}>
                Lead
                {sortField === 'name' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('status')}>
                Status
                {sortField === 'status' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('quality')}>
                Quality
                {sortField === 'quality' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('source')}>
                Source
                {sortField === 'source' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('value')}>
                Value
                {sortField === 'value' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('score')}>
                Score
                {sortField === 'score' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Assigned To</th>
              <th onClick={() => handleSort('createdAt')}>
                Created
                {sortField === 'createdAt' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sortedLeads.map((lead) => (
              <tr
                key={lead.id}
                className={selectedLeads.has(lead.id) ? 'row-selected' : ''}
                onClick={() => onRowClick?.(lead)}
              >
                <td className="checkbox-cell" onClick={(e) => e.stopPropagation()}>
                  <input
                    type="checkbox"
                    checked={selectedLeads.has(lead.id)}
                    onChange={() => handleSelectLead(lead.id)}
                  />
                </td>
                <td className="lead-name">
                  <div>
                    <div className="name">{lead.name}</div>
                    <div className="email">{lead.email}</div>
                    {lead.company && <div className="company">{lead.company}</div>}
                  </div>
                </td>
                <td onClick={(e) => e.stopPropagation()}>
                  <StatusBadge status={lead.status} />
                </td>
                <td onClick={(e) => e.stopPropagation()}>
                  <StatusBadge status={lead.quality} />
                </td>
                <td>{lead.source}</td>
                <td className="value">${lead.value.toLocaleString()}</td>
                <td>
                  <span className={`score-badge ${getScoreColor(lead.score)}`}>
                    {lead.score}
                  </span>
                </td>
                <td>{lead.assignedTo || '-'}</td>
                <td className="date">{formatDate(lead.createdAt)}</td>
                <td className="actions-cell" onClick={(e) => e.stopPropagation()}>
                  <button
                    className="action-btn"
                    onClick={() => onEdit?.(lead)}
                    title="Edit"
                  >
                    \u270E
                  </button>
                  <button
                    className="action-btn action-btn-danger"
                    onClick={() => onDelete?.(lead.id)}
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
