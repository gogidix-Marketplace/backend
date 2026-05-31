// CustomersTable Component
// Corporate customers listing

import React, { useState } from 'react';
import type { Customer, CustomerStatus, CustomerTier } from '@domain/types';
import { StatusBadge, ActionMenu } from '../common';
import { CUSTOMER_STATUS, CUSTOMER_TIERS } from '@shared';
import { formatCurrency, formatDate, formatRelativeTime } from '@shared';

export interface CustomersTableProps {
  customers: Customer[];
  isLoading?: boolean;
  onCustomerClick?: (customer: Customer) => void;
  onEdit?: (customer: Customer) => void;
  onDelete?: (customerId: string) => void;
  onStatusChange?: (customerId: string, status: CustomerStatus) => void;
  className?: string;
}

export const CustomersTable: React.FC<CustomersTableProps> = ({
  customers,
  isLoading = false,
  onCustomerClick,
  onEdit,
  onDelete,
  onStatusChange,
  className = '',
}) => {
  const [statusFilter, setStatusFilter] = useState<CustomerStatus | 'ALL'>('ALL');
  const [tierFilter, setTierFilter] = useState<CustomerTier | 'ALL'>('ALL');

  const getActions = (customer: Customer) => [
    { id: 'view', label: 'View Details', icon: '\u{1F50D}', onClick: () => onCustomerClick?.(customer) },
    { id: 'edit', label: 'Edit Customer', icon: '\u270E', onClick: () => onEdit?.(customer) },
    { id: 'deals', label: 'View Deals', icon: '\uD83D\uDCC8', onClick: () => {} },
    { id: 'contacts', label: 'Manage Contacts', icon: '\uD83D\uDC65', onClick: () => {} },
    { id: 'divider', label: '', divider: true } as any,
    { id: 'active', label: 'Mark Active', icon: '\u2705', onClick: () => onStatusChange?.(customer.id, 'active'), disabled: customer.status === 'active' },
    { id: 'at-risk', label: 'Mark At Risk', icon: '\u26A0', onClick: () => onStatusChange?.(customer.id, 'at_risk'), disabled: customer.status === 'at_risk' },
    { id: 'delete', label: 'Delete', icon: '\uD83D\uDDD1', destructive: true, onClick: () => onDelete?.(customer.id) },
  ];

  const filteredCustomers = customers.filter(c => {
    if (statusFilter !== 'ALL' && c.status !== statusFilter) return false;
    if (tierFilter !== 'ALL' && c.tier !== tierFilter) return false;
    return true;
  });

  if (isLoading) {
    return (
      <div className={`customers-table customers-table-loading ${className}`}>
        <div className="table-skeleton">
          {[...Array(5)].map((_, i) => <div key={i} className="skeleton-row" />)}
        </div>
      </div>
    );
  }

  if (filteredCustomers.length === 0) {
    return (
      <div className={`customers-table customers-table-empty ${className}`}>
        <div className="empty-state">
          <span className="empty-state-icon">\uD83D\uDC65</span>
          <h3>No Customers Found</h3>
          <p>Try adjusting your filters.</p>
        </div>
      </div>
    );
  }

  return (
    <div className={`customers-table ${className}`}>
      <div className="table-filters">
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as CustomerStatus | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Statuses</option>
          <option value="active">Active</option>
          <option value="at_risk">At Risk</option>
          <option value="churned">Churned</option>
          <option value="prospect">Prospect</option>
        </select>
        <select
          value={tierFilter}
          onChange={(e) => setTierFilter(e.target.value as CustomerTier | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Tiers</option>
          <option value="enterprise">Enterprise</option>
          <option value="mid_market">Mid-Market</option>
          <option value="small_business">Small Business</option>
        </select>
      </div>

      <table className="data-table">
        <thead>
          <tr>
            <th>Customer</th>
            <th>Industry</th>
            <th>Tier</th>
            <th>Owner</th>
            <th>Total Revenue</th>
            <th>Deals</th>
            <th>Avg Deal Size</th>
            <th>Last Purchase</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredCustomers.map((customer) => {
            const tierInfo = CUSTOMER_TIERS[customer.tier];
            const statusInfo = CUSTOMER_STATUS[customer.status];

            return (
              <tr key={customer.id} className="data-row" onClick={() => onCustomerClick?.(customer)}>
                <td className="customer-name-cell">
                  <div className="customer-logo">
                    {customer.logo ? (
                      <img src={customer.logo} alt="" />
                    ) : (
                      <span>{customer.name.slice(0, 2).toUpperCase()}</span>
                    )}
                  </div>
                  <div>
                    <div className="customer-name">{customer.name}</div>
                    <div className="customer-location">{customer.address.city}</div>
                  </div>
                </td>
                <td>{customer.industry}</td>
                <td>
                  <span
                    className="tier-badge"
                    style={{ backgroundColor: tierInfo.color + '20', color: tierInfo.color }}
                  >
                    {tierInfo.label}
                  </span>
                </td>
                <td>
                  <div className="owner-info">
                    <div className="owner-name">{customer.accountOwner.name}</div>
                    <div className="owner-email">{customer.accountOwner.email}</div>
                  </div>
                </td>
                <td>{formatCurrency(customer.metrics.totalRevenue, 'USD')}</td>
                <td>{customer.metrics.dealsCount}</td>
                <td>{formatCurrency(customer.metrics.avgDealSize, 'USD')}</td>
                <td>
                  <div className="last-purchase">
                    <div>{formatDate(customer.metrics.lastPurchaseDate, 'MMM d')}</div>
                    <div className="sub-text">{formatRelativeTime(customer.metrics.lastPurchaseDate)}</div>
                  </div>
                </td>
                <td>
                  <StatusBadge status={customer.status} size="sm" />
                </td>
                <td onClick={(e) => e.stopPropagation()}>
                  <ActionMenu actions={getActions(customer)} />
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default CustomersTable;
