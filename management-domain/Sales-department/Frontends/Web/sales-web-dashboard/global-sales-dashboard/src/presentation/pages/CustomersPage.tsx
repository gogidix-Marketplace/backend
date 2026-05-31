// Customers Page
// Corporate customer management across all countries

import { useState, useMemo } from 'react';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Table } from '../components/tables/Table';
import { formatCurrency } from '@shared';
import { mockCountries } from '@shared/mock-data';
import './CustomersPage.css';

// Mock customer data
interface CorporateCustomer {
  id: string;
  name: string;
  logo?: string;
  industry: string;
  country: typeof mockCountries[0];
  segment: 'enterprise' | 'mid-market' | 'smb';
  status: 'active' | 'at-risk' | 'churned';
  totalRevenue: number;
  dealCount: number;
  avgDealSize: number;
  lastPurchaseDate: Date;
  nextRenewalDate: Date;
  accountOwner: {
    id: string;
    name: string;
  };
  satisfactionScore: number;
}

const mockCustomers: CorporateCustomer[] = [
  {
    id: 'cust-001',
    name: 'Global Corporation Nigeria',
    industry: 'Technology',
    country: mockCountries[0],
    segment: 'enterprise',
    status: 'active',
    totalRevenue: 1250000,
    dealCount: 18,
    avgDealSize: 69444,
    lastPurchaseDate: new Date('2025-01-15'),
    nextRenewalDate: new Date('2025-06-15'),
    accountOwner: { id: 'usr-001', name: 'John Doe' },
    satisfactionScore: 9.2,
  },
  {
    id: 'cust-002',
    name: 'TechPrime South Africa',
    industry: 'Financial Services',
    country: mockCountries[3],
    segment: 'enterprise',
    status: 'active',
    totalRevenue: 980000,
    dealCount: 12,
    avgDealSize: 81667,
    lastPurchaseDate: new Date('2025-02-01'),
    nextRenewalDate: new Date('2025-08-01'),
    accountOwner: { id: 'usr-004', name: 'Sarah Williams' },
    satisfactionScore: 8.8,
  },
  {
    id: 'cust-003',
    name: 'MegaTrade Kenya',
    industry: 'Logistics',
    country: mockCountries[1],
    segment: 'mid-market',
    status: 'active',
    totalRevenue: 650000,
    dealCount: 24,
    avgDealSize: 27083,
    lastPurchaseDate: new Date('2025-01-28'),
    nextRenewalDate: new Date('2025-07-28'),
    accountOwner: { id: 'usr-002', name: 'Jane Smith' },
    satisfactionScore: 7.5,
  },
  {
    id: 'cust-004',
    name: 'Prime Logistics Ghana',
    industry: 'Logistics',
    country: mockCountries[2],
    segment: 'mid-market',
    status: 'at-risk',
    totalRevenue: 420000,
    dealCount: 15,
    avgDealSize: 28000,
    lastPurchaseDate: new Date('2024-12-10'),
    nextRenewalDate: new Date('2025-06-10'),
    accountOwner: { id: 'usr-003', name: 'Bob Johnson' },
    satisfactionScore: 5.8,
  },
  {
    id: 'cust-005',
    name: 'Elite Corp Ethiopia',
    industry: 'Manufacturing',
    country: mockCountries[4],
    segment: 'enterprise',
    status: 'active',
    totalRevenue: 780000,
    dealCount: 9,
    avgDealSize: 86667,
    lastPurchaseDate: new Date('2025-02-05'),
    nextRenewalDate: new Date('2025-09-05'),
    accountOwner: { id: 'usr-005', name: 'Michael Chen' },
    satisfactionScore: 8.2,
  },
  {
    id: 'cust-006',
    name: 'FastTrack Uganda',
    industry: 'Retail',
    country: mockCountries[5],
    segment: 'smb',
    status: 'active',
    totalRevenue: 180000,
    dealCount: 32,
    avgDealSize: 5625,
    lastPurchaseDate: new Date('2025-01-20'),
    nextRenewalDate: new Date('2025-04-20'),
    accountOwner: { id: 'usr-006', name: 'Grace Amani' },
    satisfactionScore: 7.9,
  },
  {
    id: 'cust-007',
    name: 'Regional Bank Ghana',
    industry: 'Financial Services',
    country: mockCountries[2],
    segment: 'enterprise',
    status: 'churned',
    totalRevenue: 520000,
    dealCount: 8,
    avgDealSize: 65000,
    lastPurchaseDate: new Date('2024-08-15'),
    nextRenewalDate: new Date('2025-02-15'),
    accountOwner: { id: 'usr-003', name: 'Bob Johnson' },
    satisfactionScore: 4.2,
  },
  {
    id: 'cust-008',
    name: 'Digital Ventures Nigeria',
    industry: 'Technology',
    country: mockCountries[0],
    segment: 'mid-market',
    status: 'active',
    totalRevenue: 380000,
    dealCount: 14,
    avgDealSize: 27143,
    lastPurchaseDate: new Date('2025-02-10'),
    nextRenewalDate: new Date('2025-08-10'),
    accountOwner: { id: 'usr-001', name: 'John Doe' },
    satisfactionScore: 8.5,
  },
];

const SEGMENT_OPTIONS = [
  { value: 'all', label: 'All Segments' },
  { value: 'enterprise', label: 'Enterprise' },
  { value: 'mid-market', label: 'Mid-Market' },
  { value: 'smb', label: 'SMB' },
];

const STATUS_OPTIONS = [
  { value: 'all', label: 'All Status' },
  { value: 'active', label: 'Active' },
  { value: 'at-risk', label: 'At Risk' },
  { value: 'churned', label: 'Churned' },
];

const INDUSTRY_OPTIONS = [
  { value: 'all', label: 'All Industries' },
  { value: 'technology', label: 'Technology' },
  { value: 'financial', label: 'Financial Services' },
  { value: 'logistics', label: 'Logistics' },
  { value: 'manufacturing', label: 'Manufacturing' },
  { value: 'retail', label: 'Retail' },
];

export function CustomersPage() {
  const [selectedCountry, setSelectedCountry] = useState<string>('all');
  const [selectedSegment, setSelectedSegment] = useState<string>('all');
  const [selectedStatus, setSelectedStatus] = useState<string>('all');
  const [selectedIndustry, setSelectedIndustry] = useState<string>('all');
  const [searchQuery, setSearchQuery] = useState('');

  const filteredCustomers = useMemo(() => {
    return mockCustomers.filter((customer) => {
      if (selectedCountry !== 'all' && customer.country.code !== selectedCountry) {
        return false;
      }
      if (selectedSegment !== 'all' && customer.segment !== selectedSegment) {
        return false;
      }
      if (selectedStatus !== 'all' && customer.status !== selectedStatus) {
        return false;
      }
      if (selectedIndustry !== 'all' &&
          customer.industry.toLowerCase() !== selectedIndustry.toLowerCase()) {
        return false;
      }
      if (searchQuery && !customer.name.toLowerCase().includes(searchQuery.toLowerCase())) {
        return false;
      }
      return true;
    });
  }, [selectedCountry, selectedSegment, selectedStatus, selectedIndustry, searchQuery]);

  const globalMetrics = useMemo(() => {
    const totalRevenue = filteredCustomers.reduce((sum, c) => sum + c.totalRevenue, 0);
    const totalDeals = filteredCustomers.reduce((sum, c) => sum + c.dealCount, 0);
    const avgSatisfaction = filteredCustomers.length > 0
      ? filteredCustomers.reduce((sum, c) => sum + c.satisfactionScore, 0) / filteredCustomers.length
      : 0;
    const atRiskCount = filteredCustomers.filter(c => c.status === 'at-risk').length;
    const churnedCount = filteredCustomers.filter(c => c.status === 'churned').length;

    return {
      totalCustomers: filteredCustomers.length,
      totalRevenue,
      avgDealSize: totalDeals > 0 ? totalRevenue / totalDeals : 0,
      avgSatisfaction,
      atRiskCount,
      churnedCount,
    };
  }, [filteredCustomers]);

  const revenueBySegment = useMemo(() => {
    const segments = ['enterprise', 'mid-market', 'smb'] as const;
    return segments.map(segment => {
      const segmentCustomers = mockCustomers.filter(c => c.segment === segment);
      const revenue = segmentCustomers.reduce((sum, c) => sum + c.totalRevenue, 0);
      return {
        segment,
        label: segment.replace('-', ' ').replace(/^\w/, c => c.toUpperCase()),
        count: segmentCustomers.length,
        revenue,
      };
    });
  }, []);

  const topCustomers = useMemo(() => {
    return [...mockCustomers]
      .sort((a, b) => b.totalRevenue - a.totalRevenue)
      .slice(0, 10);
  }, []);

  return (
    <div className="customers-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Global Customers</h1>
          <p className="page-subtitle">Corporate customer management and account overview</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <polyline points="7 10 12 15 17 10" />
              <line x1="12" y1="15" x2="12" y2="3" />
            </svg>
            Export
          </Button>
          <Button variant="primary" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="12" y1="5" x2="12" y2="19" />
              <line x1="5" y1="12" x2="19" y2="12" />
            </svg>
            Add Customer
          </Button>
        </div>
      </div>

      {/* Filters */}
      <Card>
        <CardBody>
          <div className="customers-filters">
            <div className="filter-search">
              <input
                type="text"
                placeholder="Search customers..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="search-input"
              />
            </div>
            <select
              value={selectedCountry}
              onChange={(e) => setSelectedCountry(e.target.value)}
              className="filter-select"
            >
              <option value="all">All Countries</option>
              {mockCountries.map(country => (
                <option key={country.code} value={country.code}>
                  {country.flag} {country.name}
                </option>
              ))}
            </select>
            <select
              value={selectedSegment}
              onChange={(e) => setSelectedSegment(e.target.value)}
              className="filter-select"
            >
              {SEGMENT_OPTIONS.map(option => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))}
            </select>
            <select
              value={selectedIndustry}
              onChange={(e) => setSelectedIndustry(e.target.value)}
              className="filter-select"
            >
              {INDUSTRY_OPTIONS.map(option => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))}
            </select>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="filter-select"
            >
              {STATUS_OPTIONS.map(option => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))}
            </select>
          </div>
        </CardBody>
      </Card>

      {/* Key Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Total Customers"
            value={globalMetrics.totalCustomers}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                <circle cx="9" cy="7" r="4" />
                <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
                <path d="M16 3.13a4 4 0 0 1 0 7.75" />
              </svg>
            }
          />
          <MetricCard
            title="Total Revenue"
            value={formatCurrency(globalMetrics.totalRevenue)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Avg Satisfaction"
            value={globalMetrics.avgSatisfaction.toFixed(1) + '/10'}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
              </svg>
            }
          />
          <MetricCard
            title="At Risk Customers"
            value={globalMetrics.atRiskCount}
            change={globalMetrics.atRiskCount > 0 ? undefined : 0}
            changeType={globalMetrics.atRiskCount > 0 ? 'decrease' : 'increase'}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
                <line x1="12" y1="9" x2="12" y2="13" />
                <line x1="12" y1="17" x2="12.01" y2="17" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Revenue by Segment */}
      <section className="segment-section">
        <Card>
          <CardHeader>
            <h3>Revenue by Segment</h3>
          </CardHeader>
          <CardBody>
            <div className="segment-bars">
              {revenueBySegment.map((segment) => {
                const maxRevenue = Math.max(...revenueBySegment.map(s => s.revenue));
                const percentage = (segment.revenue / maxRevenue) * 100;
                return (
                  <div key={segment.segment} className="segment-bar-item">
                    <div className="segment-info">
                      <span className="segment-label">{segment.label}</span>
                      <span className="segment-stats">
                        {segment.count} customers · {formatCurrency(segment.revenue)}
                      </span>
                    </div>
                    <div className="segment-bar">
                      <div
                        className={`segment-bar-fill segment-${segment.segment}`}
                        style={{ width: `${percentage}%` }}
                      ></div>
                    </div>
                  </div>
                );
              })}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Customers Table */}
      <section className="table-section">
        <Card>
          <CardHeader>
            <h3>All Customers</h3>
            <span className="results-count">{filteredCustomers.length} results</span>
          </CardHeader>
          <CardBody>
            <Table
              columns={[
                {
                  key: 'customer',
                  label: 'Customer',
                  render: (customer: CorporateCustomer) => (
                    <div className="customer-cell">
                      {customer.logo && (
                        <img src={customer.logo} alt="" className="customer-logo" />
                      )}
                      <div className="customer-info">
                        <span className="customer-name">{customer.name}</span>
                        <span className="customer-industry">{customer.industry}</span>
                      </div>
                    </div>
                  ),
                },
                {
                  key: 'country',
                  label: 'Country',
                  render: (customer: CorporateCustomer) => (
                    <span>
                      {customer.country.flag} {customer.country.name}
                    </span>
                  ),
                },
                {
                  key: 'segment',
                  label: 'Segment',
                  render: (customer: CorporateCustomer) => (
                    <span className={`segment-badge segment-${customer.segment}`}>
                      {customer.segment.replace('-', ' ').replace(/^\w/, c => c.toUpperCase())}
                    </span>
                  ),
                },
                {
                  key: 'revenue',
                  label: 'Total Revenue',
                  render: (customer: CorporateCustomer) => formatCurrency(customer.totalRevenue),
                },
                {
                  key: 'deals',
                  label: 'Deals',
                  render: (customer: CorporateCustomer) => customer.dealCount,
                },
                {
                  key: 'avgDeal',
                  label: 'Avg Deal',
                  render: (customer: CorporateCustomer) => formatCurrency(customer.avgDealSize),
                },
                {
                  key: 'satisfaction',
                  label: 'CSAT',
                  render: (customer: CorporateCustomer) => (
                    <div className="satisfaction-cell">
                      <span className={`satisfaction-score score-${Math.floor(customer.satisfactionScore)}`}>
                        {customer.satisfactionScore.toFixed(1)}
                      </span>
                    </div>
                  ),
                },
                {
                  key: 'status',
                  label: 'Status',
                  render: (customer: CorporateCustomer) => (
                    <span className={`status-badge status-${customer.status}`}>
                      {customer.status === 'active' && 'Active'}
                      {customer.status === 'at-risk' && 'At Risk'}
                      {customer.status === 'churned' && 'Churned'}
                    </span>
                  ),
                },
                {
                  key: 'owner',
                  label: 'Account Owner',
                  render: (customer: CorporateCustomer) => customer.accountOwner.name,
                },
                {
                  key: 'actions',
                  label: 'Actions',
                  render: (customer: CorporateCustomer) => (
                    <Button variant="link" size="sm">View Details</Button>
                  ),
                },
              ]}
              data={filteredCustomers}
            />
          </CardBody>
        </Card>
      </section>

      {/* Top Customers */}
      <section className="top-customers-section">
        <Card>
          <CardHeader>
            <h3>Top Customers by Revenue</h3>
          </CardHeader>
          <CardBody>
            <div className="top-customers-list">
              {topCustomers.map((customer, index) => (
                <div key={customer.id} className="top-customer-item">
                  <div className="customer-rank">
                    <span className={`rank-badge rank-${index + 1}`}>
                      {index === 0 && '\u{1F947}'}
                      {index === 1 && '\u{1F948}'}
                      {index === 2 && '\u{1F949}'}
                      {index > 2 && index + 1}
                    </span>
                  </div>
                  <div className="customer-details">
                    <span className="customer-name">{customer.name}</span>
                    <span className="customer-meta">
                      {customer.country.flag} {customer.industry}
                    </span>
                  </div>
                  <div className="customer-metrics">
                    <div className="metric">
                      <span className="metric-label">Revenue</span>
                      <span className="metric-value">{formatCurrency(customer.totalRevenue)}</span>
                    </div>
                    <div className="metric">
                      <span className="metric-label">Deals</span>
                      <span className="metric-value">{customer.dealCount}</span>
                    </div>
                    <div className="metric">
                      <span className="metric-label">CSAT</span>
                      <span className="metric-value">{customer.satisfactionScore.toFixed(1)}</span>
                    </div>
                  </div>
                  <div className="customer-owner">
                    <span className="owner-name">{customer.accountOwner.name}</span>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>
    </div>
  );
}
