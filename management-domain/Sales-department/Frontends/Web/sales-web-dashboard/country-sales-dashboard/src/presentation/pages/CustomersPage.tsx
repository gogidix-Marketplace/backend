// CustomersPage Component
// Corporate customers in country

import React, { useState } from 'react';
import { useCustomersStore } from '@infrastructure/stores';
import { mockCustomers } from '@shared/mock-data';
import { CustomersTable } from '../components/tables';
import { SearchBar, Button, Tabs, Card } from '../components/common';
import { PartnerDistributionChart } from '../components/charts';
import type { Customer, CustomerStatus, CustomerTier } from '@domain/types';
import { formatCurrency, formatDate } from '@shared';

export const CustomersPage: React.FC = () => {
  const { customers, isLoading } = useCustomersStore();
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<CustomerStatus | 'ALL'>('ALL');
  const [tierFilter, setTierFilter] = useState<CustomerTier | 'ALL'>('ALL');
  const [activeTab, setActiveTab] = useState('all');

  const displayCustomers = mockCustomers;

  const customerStats = {
    total: displayCustomers.length,
    active: displayCustomers.filter(c => c.status === 'active').length,
    atRisk: displayCustomers.filter(c => c.status === 'at_risk').length,
    totalRevenue: displayCustomers.reduce((sum, c) => sum + c.metrics.totalRevenue, 0),
  };

  const byTier = {
    enterprise: displayCustomers.filter(c => c.tier === 'enterprise').length,
    midMarket: displayCustomers.filter(c => c.tier === 'mid_market').length,
    smallBusiness: displayCustomers.filter(c => c.tier === 'small_business').length,
  };

  const byIndustry = displayCustomers.reduce((acc, c) => {
    acc[c.industry] = (acc[c.industry] || 0) + 1;
    return acc;
  }, {} as Record<string, number>);

  const industryData = Object.entries(byIndustry).map(([industry, count], i) => ({
    type: industry as any,
    count,
  }));

  const handleStatusChange = async (customerId: string, status: CustomerStatus) => {
    // Status change logic
  };

  const tabs = [
    {
      id: 'all',
      label: 'All Customers',
      badge: displayCustomers.length,
      content: (
        <CustomersTable
          customers={displayCustomers}
          isLoading={isLoading}
          onCustomerClick={(c) => {}}
          onStatusChange={handleStatusChange}
        />
      ),
    },
    {
      id: 'enterprise',
      label: 'Enterprise',
      badge: byTier.enterprise,
      content: (
        <CustomersTable
          customers={displayCustomers.filter(c => c.tier === 'enterprise')}
          isLoading={isLoading}
          onCustomerClick={(c) => {}}
        />
      ),
    },
    {
      id: 'at-risk',
      label: 'At Risk',
      badge: customerStats.atRisk,
      content: (
        <div className="at-risk-customers">
          <Card>
            <h4>Customers At Risk</h4>
            {displayCustomers.filter(c => c.status === 'at_risk').map(customer => (
              <div key={customer.id} className="at-risk-item">
                <div className="customer-summary">
                  <span className="customer-name">{customer.name}</span>
                  <span className="customer-industry">{customer.industry}</span>
                </div>
                <div className="risk-metrics">
                  <span>Last purchase: {formatDate(customer.metrics.lastPurchaseDate, 'MMM d, yyyy')}</span>
                  <span>LTV: {formatCurrency(customer.metrics.ltv, 'USD')}</span>
                </div>
                <div className="risk-actions">
                  <button className="btn btn-sm btn-primary">Re-engage</button>
                </div>
              </div>
            ))}
          </Card>
        </div>
      ),
    },
    {
      id: 'analytics',
      label: 'Analytics',
      content: (
        <div className="customers-analytics">
          <div className="analytics-grid">
            <Card>
              <h4>By Tier</h4>
              <div className="tier-breakdown">
                <div className="tier-item">
                  <span className="tier-label">Enterprise</span>
                  <div className="tier-bar">
                    <div className="tier-fill" style={{ width: `${(byTier.enterprise / displayCustomers.length) * 100}%`, backgroundColor: '#7C3AED' }} />
                  </div>
                  <span className="tier-count">{byTier.enterprise}</span>
                </div>
                <div className="tier-item">
                  <span className="tier-label">Mid-Market</span>
                  <div className="tier-bar">
                    <div className="tier-fill" style={{ width: `${(byTier.midMarket / displayCustomers.length) * 100}%`, backgroundColor: '#3B82F6' }} />
                  </div>
                  <span className="tier-count">{byTier.midMarket}</span>
                </div>
                <div className="tier-item">
                  <span className="tier-label">Small Business</span>
                  <div className="tier-bar">
                    <div className="tier-fill" style={{ width: `${(byTier.smallBusiness / displayCustomers.length) * 100}%`, backgroundColor: '#10B981' }} />
                  </div>
                  <span className="tier-count">{byTier.smallBusiness}</span>
                </div>
              </div>
            </Card>

            <Card>
              <h4>By Industry</h4>
              <PartnerDistributionChart data={industryData} size="md" showLegend={true} />
            </Card>
          </div>

          <Card>
            <h4>Top Customers by Revenue</h4>
            <div className="top-customers">
              {[...displayCustomers].sort((a, b) => b.metrics.totalRevenue - a.metrics.totalRevenue)
                .slice(0, 10)
                .map((customer, index) => (
                  <div key={customer.id} className="top-customer-item">
                    <span className="customer-rank">#{index + 1}</span>
                    <span className="customer-name">{customer.name}</span>
                    <span className="customer-tier">{customer.tier.replace('_', '-')}</span>
                    <span className="customer-revenue">{formatCurrency(customer.metrics.totalRevenue, 'USD')}</span>
                    <span className="customer-deals">{customer.metrics.dealsCount} deals</span>
                  </div>
                ))}
            </div>
          </Card>
        </div>
      ),
    },
  ];

  return (
    <div className="page customers-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Customers</h1>
          <p className="page-subtitle">Manage corporate customer relationships</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary">Import</Button>
          <Button variant="primary">Add Customer</Button>
        </div>
      </div>

      {/* Stats Summary */}
      <div className="customers-summary">
        <div className="summary-card">
          <span className="summary-label">Total Customers</span>
          <span className="summary-value">{customerStats.total}</span>
        </div>
        <div className="summary-card">
          <span className="summary-label">Active</span>
          <span className="summary-value summary-success">{customerStats.active}</span>
        </div>
        <div className="summary-card">
          <span className="summary-label">At Risk</span>
          <span className="summary-value summary-warning">{customerStats.atRisk}</span>
        </div>
        <div className="summary-card">
          <span className="summary-label">Total Revenue</span>
          <span className="summary-value">{formatCurrency(customerStats.totalRevenue, 'USD')}</span>
        </div>
      </div>

      {/* Filters */}
      <div className="page-filters">
        <SearchBar
          placeholder="Search customers..."
          value={searchQuery}
          onChange={setSearchQuery}
        />
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as CustomerStatus | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Statuses</option>
          <option value="active">Active</option>
          <option value="at_risk">At Risk</option>
          <option value="churned">Churned</option>
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

      {/* Tabs */}
      <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} />
    </div>
  );
};

export default CustomersPage;
