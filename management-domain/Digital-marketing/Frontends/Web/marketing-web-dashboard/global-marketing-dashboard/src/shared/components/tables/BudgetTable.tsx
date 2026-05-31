// BudgetTable Component
// Table component for displaying budgets

import React from 'react';
import { Table, Column } from './Table';
import { Budget, BudgetStatus } from '../../domain/types';
import { StatusBadge } from '../common/StatusBadge';
import { formatCurrency, formatPercentage } from '../../utils/formatters';

export interface BudgetTableProps {
  budgets: Budget[];
  onBudgetClick?: (budget: Budget) => void;
  className?: string;
  loading?: boolean;
}

const statusColors: Record<BudgetStatus, string> = {
  draft: '#9CA3AF',
  approved: '#3B82F6',
  active: '#10B981',
  exceeded: '#F59E0B',
  exhausted: '#EF4444',
};

export const BudgetTable: React.FC<BudgetTableProps> = ({
  budgets,
  onBudgetClick,
  className = '',
  loading = false,
}) => {
  const columns: Column<Budget>[] = [
    {
      key: 'name',
      title: 'Budget',
      render: (value, budget) => (
        <div className="budget-name-cell">
          <span className="budget-name">{budget.name}</span>
          <span className="budget-period">
            {budget.fiscalYear}
            {budget.quarter ? ` - Q${budget.quarter}` : ''}
          </span>
        </div>
      ),
      sortable: true,
    },
    {
      key: 'status',
      title: 'Status',
      render: (value: BudgetStatus) => (
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
      key: 'totalAmount',
      title: 'Total Budget',
      render: (value, budget) => (
        <span className="budget-amount">
          {formatCurrency(budget.totalAmount, budget.currency)}
        </span>
      ),
      sortable: true,
      align: 'right',
    },
    {
      key: 'spentAmount',
      title: 'Spent',
      render: (value, budget) => (
        <span className="budget-spent">
          {formatCurrency(budget.spentAmount, budget.currency)}
        </span>
      ),
      sortable: true,
      align: 'right',
    },
    {
      key: 'remainingAmount',
      title: 'Remaining',
      render: (value, budget) => (
        <span className={`budget-remaining ${budget.remainingAmount < 0 ? 'negative' : ''}`}>
          {formatCurrency(budget.remainingAmount, budget.currency)}
        </span>
      ),
      sortable: true,
      align: 'right',
    },
    {
      key: 'utilization',
      title: 'Utilization',
      render: (value, budget) => {
        const utilization = (budget.spentAmount / budget.totalAmount) * 100;
        return (
          <div className="utilization-cell">
            <div className="utilization-bar">
              <div
                className="utilization-fill"
                style={{
                  width: `${Math.min(utilization, 100)}%`,
                  backgroundColor:
                    utilization > 100
                      ? '#EF4444'
                      : utilization > 90
                      ? '#F59E0B'
                      : '#10B981',
                }}
              />
            </div>
            <span className={`utilization-value ${utilization > 100 ? 'over' : utilization > 90 ? 'warning' : ''}`}>
              {formatPercentage(utilization)}
            </span>
          </div>
        );
      },
      sortable: true,
      width: '140px',
    },
    {
      key: 'allocations',
      title: 'Allocations',
      render: (value, budget) => (
        <span className="allocations-count">
          {budget.allocations.length} countries
        </span>
      ),
      width: '120px',
      align: 'center',
    },
    {
      key: 'categories',
      title: 'Categories',
      render: (value, budget) => (
        <div className="categories-cell">
          {budget.categories.slice(0, 2).map((cat, i) => (
            <span key={i} className="category-badge">
              {cat.channel}
            </span>
          ))}
          {budget.categories.length > 2 && (
            <span className="category-more">+{budget.categories.length - 2}</span>
          )}
        </div>
      ),
    },
    {
      key: 'approval',
      title: 'Approval',
      render: (value, budget) => (
        <div className="approval-cell">
          <span className={`approval-status approval-${budget.approval.status}`}>
            {budget.approval.status}
          </span>
          {budget.approval.approvedBy && (
            <span className="approved-by">by {budget.approval.approvedBy}</span>
          )}
        </div>
      ),
      width: '140px',
    },
    {
      key: 'createdAt',
      title: 'Created',
      render: (value, budget) => (
        <span className="date-cell">
          {new Date(budget.createdAt).toLocaleDateString()}
        </span>
      ),
      sortable: true,
      width: '100px',
    },
  ];

  return (
    <Table
      columns={columns}
      data={budgets}
      keyField="id"
      onRowClick={onBudgetClick}
      loading={loading}
      className={className}
    />
  );
};

export default BudgetTable;
