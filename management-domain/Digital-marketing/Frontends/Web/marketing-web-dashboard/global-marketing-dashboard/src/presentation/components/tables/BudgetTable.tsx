// BudgetTable Component
// Displays budget allocations by country/category

import { useState } from 'react';
import './BudgetTable.css';

export interface BudgetItem {
  id: string;
  category: string;
  country: string;
  allocated: number;
  spent: number;
  remaining: number;
  utilization: number;
  status: 'on_track' | 'at_risk' | 'exceeded' | 'exhausted';
  fiscalYear: number;
  quarter?: number;
}

interface BudgetTableProps {
  budgets: BudgetItem[];
  onEdit?: (budget: BudgetItem) => void;
  onView?: (budget: BudgetItem) => void;
  className?: string;
}

type SortField = 'category' | 'country' | 'allocated' | 'spent' | 'remaining' | 'utilization';
type SortOrder = 'asc' | 'desc';

export function BudgetTable({
  budgets,
  onEdit,
  onView,
  className = '',
}: BudgetTableProps) {
  const [sortField, setSortField] = useState<SortField>('allocated');
  const [sortOrder, setSortOrder] = useState<SortOrder>('desc');

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortOrder('desc');
    }
  };

  const sortedBudgets = [...budgets].sort((a, b) => {
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

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'on_track': return '#10b981';
      case 'at_risk': return '#f59e0b';
      case 'exceeded': return '#ef4444';
      case 'exhausted': return '#6b7280';
      default: return '#9ca3af';
    }
  };

  const getUtilizationBarColor = (utilization: number) => {
    if (utilization >= 100) return '#ef4444';
    if (utilization >= 85) return '#f59e0b';
    if (utilization >= 50) return '#10b981';
    return '#6366f1';
  };

  const formatCurrency = (value: number) => {
    if (value >= 1000000) return `$${(value / 1000000).toFixed(1)}M`;
    if (value >= 1000) return `$${(value / 1000).toFixed(1)}K`;
    return `$${value.toFixed(0)}`;
  };

  return (
    <div className={`budget-table ${className}`}>
      <div className="table-header">
        <h3 className="table-title">Budget Allocation</h3>
        <div className="table-info">
          {budgets.length} budget items
        </div>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th onClick={() => handleSort('category')}>
                Category
                {sortField === 'category' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('country')}>
                Country
                {sortField === 'country' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('allocated')}>
                Allocated
                {sortField === 'allocated' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('spent')}>
                Spent
                {sortField === 'spent' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('remaining')}>
                Remaining
                {sortField === 'remaining' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th onClick={() => handleSort('utilization')}>
                Utilization
                {sortField === 'utilization' && <span className="sort-indicator">{sortOrder === 'asc' ? '\u2191' : '\u2193'}</span>}
              </th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sortedBudgets.map((budget) => (
              <tr key={budget.id}>
                <td className="category">{budget.category}</td>
                <td className="country">{budget.country}</td>
                <td className="amount allocated">{formatCurrency(budget.allocated)}</td>
                <td className="amount spent">{formatCurrency(budget.spent)}</td>
                <td className="amount remaining">{formatCurrency(budget.remaining)}</td>
                <td className="utilization">
                  <div className="utilization-container">
                    <div className="utilization-bar-wrapper">
                      <div
                        className="utilization-bar"
                        style={{
                          width: `${Math.min(budget.utilization, 100)}%`,
                          backgroundColor: getUtilizationBarColor(budget.utilization)
                        }}
                      />
                    </div>
                    <span className="utilization-value">{budget.utilization.toFixed(1)}%</span>
                  </div>
                </td>
                <td>
                  <div className="status-indicator">
                    <span
                      className="status-dot"
                      style={{ backgroundColor: getStatusColor(budget.status) }}
                    />
                    <span className="status-label">{budget.status.replace(/_/g, ' ')}</span>
                  </div>
                </td>
                <td className="actions-cell">
                  <button
                    className="action-btn"
                    onClick={() => onView?.(budget)}
                    title="View Details"
                  >
                    \u{1F50D}
                  </button>
                  <button
                    className="action-btn"
                    onClick={() => onEdit?.(budget)}
                    title="Edit"
                  >
                    \u270E
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="table-footer">
        <div className="footer-summary">
          <span className="summary-label">Total Allocated:</span>
          <span className="summary-value">{formatCurrency(budgets.reduce((sum, b) => sum + b.allocated, 0))}</span>
        </div>
        <div className="footer-summary">
          <span className="summary-label">Total Spent:</span>
          <span className="summary-value">{formatCurrency(budgets.reduce((sum, b) => sum + b.spent, 0))}</span>
        </div>
        <div className="footer-summary">
          <span className="summary-label">Total Remaining:</span>
          <span className="summary-value">{formatCurrency(budgets.reduce((sum, b) => sum + b.remaining, 0))}</span>
        </div>
      </div>
    </div>
  );
}
