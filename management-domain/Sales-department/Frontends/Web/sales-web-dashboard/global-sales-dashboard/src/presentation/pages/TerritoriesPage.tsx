// Territories Page
// Territory management and assignment across all countries

import { useState, useMemo } from 'react';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Table } from '../components/tables/Table';
import { formatCurrency, formatPercentage } from '@shared';
import { mockCountries, mockTopPerformers } from '@shared/mock-data';
import './TerritoriesPage.css';

interface Territory {
  id: string;
  code: string;
  name: string;
  country: typeof mockCountries[0];
  region: string;
  type: 'urban' | 'suburban' | 'rural';
  status: 'active' | 'inactive' | 'pending';
  assignedTo?: {
    id: string;
    name: string;
    avatar?: string;
  };
  partnersAssigned: number;
  potentialRevenue: number;
  actualRevenue: number;
  attainment: number;
  leadsCount: number;
  conversionRate: number;
  lastActivity: Date;
}

const mockTerritories: Territory[] = [
  {
    id: 'ter-001',
    code: 'LM',
    name: 'Lagos Mainland',
    country: mockCountries[0],
    region: 'South West',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-101', name: 'John Doe', avatar: 'https://i.pravatar.cc/150?img=1' },
    partnersAssigned: 45,
    potentialRevenue: 2500000,
    actualRevenue: 1800000,
    attainment: 72,
    leadsCount: 234,
    conversionRate: 32,
    lastActivity: new Date('2025-02-17'),
  },
  {
    id: 'ter-002',
    code: 'LI',
    name: 'Lagos Island',
    country: mockCountries[0],
    region: 'South West',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-102', name: 'Jane Smith', avatar: 'https://i.pravatar.cc/150?img=2' },
    partnersAssigned: 38,
    potentialRevenue: 3200000,
    actualRevenue: 2100000,
    attainment: 66,
    leadsCount: 189,
    conversionRate: 28,
    lastActivity: new Date('2025-02-18'),
  },
  {
    id: 'ter-003',
    code: 'ABJ',
    name: 'Abuja Central',
    country: mockCountries[0],
    region: 'North Central',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-103', name: 'Bob Johnson', avatar: 'https://i.pravatar.cc/150?img=3' },
    partnersAssigned: 28,
    potentialRevenue: 1800000,
    actualRevenue: 1250000,
    attainment: 69,
    leadsCount: 145,
    conversionRate: 35,
    lastActivity: new Date('2025-02-16'),
  },
  {
    id: 'ter-004',
    code: 'NBI',
    name: 'Nairobi West',
    country: mockCountries[1],
    region: 'Nairobi',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-104', name: 'Sarah Williams', avatar: 'https://i.pravatar.cc/150?img=4' },
    partnersAssigned: 32,
    potentialRevenue: 1400000,
    actualRevenue: 980000,
    attainment: 70,
    leadsCount: 167,
    conversionRate: 30,
    lastActivity: new Date('2025-02-18'),
  },
  {
    id: 'ter-005',
    code: 'NBE',
    name: 'Nairobi East',
    country: mockCountries[1],
    region: 'Nairobi',
    type: 'suburban',
    status: 'active',
    assignedTo: { id: 'usr-105', name: 'Michael Chen', avatar: 'https://i.pravatar.cc/150?img=5' },
    partnersAssigned: 25,
    potentialRevenue: 950000,
    actualRevenue: 680000,
    attainment: 72,
    leadsCount: 134,
    conversionRate: 33,
    lastActivity: new Date('2025-02-15'),
  },
  {
    id: 'ter-006',
    code: 'ACC',
    name: 'Accra Central',
    country: mockCountries[2],
    region: 'Greater Accra',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-106', name: 'Grace Amani', avatar: 'https://i.pravatar.cc/150?img=6' },
    partnersAssigned: 22,
    potentialRevenue: 850000,
    actualRevenue: 520000,
    attainment: 61,
    leadsCount: 112,
    conversionRate: 26,
    lastActivity: new Date('2025-02-17'),
  },
  {
    id: 'ter-007',
    code: 'JHB',
    name: 'Johannesburg North',
    country: mockCountries[3],
    region: 'Gauteng',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-107', name: 'David Miller', avatar: 'https://i.pravatar.cc/150?img=7' },
    partnersAssigned: 52,
    potentialRevenue: 4200000,
    actualRevenue: 2850000,
    attainment: 68,
    leadsCount: 298,
    conversionRate: 31,
    lastActivity: new Date('2025-02-18'),
  },
  {
    id: 'ter-008',
    code: 'CAA',
    name: 'Cape Town Atlantic',
    country: mockCountries[3],
    region: 'Western Cape',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-108', name: 'Lisa Anderson', avatar: 'https://i.pravatar.cc/150?img=8' },
    partnersAssigned: 35,
    potentialRevenue: 2100000,
    actualRevenue: 1420000,
    attainment: 68,
    leadsCount: 178,
    conversionRate: 29,
    lastActivity: new Date('2025-02-14'),
  },
  {
    id: 'ter-009',
    code: 'ADD',
    name: 'Addis Ababa Central',
    country: mockCountries[4],
    region: 'Addis Ababa',
    type: 'urban',
    status: 'pending',
    partnersAssigned: 0,
    potentialRevenue: 650000,
    actualRevenue: 0,
    attainment: 0,
    leadsCount: 45,
    conversionRate: 0,
    lastActivity: new Date('2025-02-01'),
  },
  {
    id: 'ter-010',
    code: 'KLA',
    name: 'Kampala Central',
    country: mockCountries[5],
    region: 'Central',
    type: 'urban',
    status: 'active',
    assignedTo: { id: 'usr-109', name: 'James Okello', avatar: 'https://i.pravatar.cc/150?img=9' },
    partnersAssigned: 18,
    potentialRevenue: 520000,
    actualRevenue: 340000,
    attainment: 65,
    leadsCount: 89,
    conversionRate: 28,
    lastActivity: new Date('2025-02-16'),
  },
];

const TERRITORY_TYPES = [
  { value: 'all', label: 'All Types' },
  { value: 'urban', label: 'Urban' },
  { value: 'suburban', label: 'Suburban' },
  { value: 'rural', label: 'Rural' },
];

const STATUS_OPTIONS = [
  { value: 'all', label: 'All Status' },
  { value: 'active', label: 'Active' },
  { value: 'pending', label: 'Pending Assignment' },
  { value: 'inactive', label: 'Inactive' },
];

export function TerritoriesPage() {
  const [selectedCountry, setSelectedCountry] = useState<string>('all');
  const [selectedType, setSelectedType] = useState<string>('all');
  const [selectedStatus, setSelectedStatus] = useState<string>('all');
  const [searchQuery, setSearchQuery] = useState('');

  const filteredTerritories = useMemo(() => {
    return mockTerritories.filter((territory) => {
      if (selectedCountry !== 'all' && territory.country.code !== selectedCountry) {
        return false;
      }
      if (selectedType !== 'all' && territory.type !== selectedType) {
        return false;
      }
      if (selectedStatus !== 'all' && territory.status !== selectedStatus) {
        return false;
      }
      if (searchQuery && !territory.name.toLowerCase().includes(searchQuery.toLowerCase())) {
        return false;
      }
      return true;
    });
  }, [selectedCountry, selectedType, selectedStatus, searchQuery]);

  const globalMetrics = useMemo(() => {
    const totalTerritories = filteredTerritories.length;
    const activeTerritories = filteredTerritories.filter(t => t.status === 'active').length;
    const unassignedTerritories = filteredTerritories.filter(t => !t.assignedTo).length;
    const totalPotential = filteredTerritories.reduce((sum, t) => sum + t.potentialRevenue, 0);
    const totalActual = filteredTerritories.reduce((sum, t) => sum + t.actualRevenue, 0);
    const avgAttainment = totalPotential > 0 ? (totalActual / totalPotential) * 100 : 0;
    const totalPartners = filteredTerritories.reduce((sum, t) => sum + t.partnersAssigned, 0);
    const totalLeads = filteredTerritories.reduce((sum, t) => sum + t.leadsCount, 0);

    return {
      totalTerritories,
      activeTerritories,
      unassignedTerritories,
      totalPotential,
      totalActual,
      avgAttainment,
      totalPartners,
      totalLeads,
    };
  }, [filteredTerritories]);

  const territoriesByCountry = useMemo(() => {
    return mockCountries.map(country => {
      const countryTerritories = mockTerritories.filter(t => t.country.code === country.code);
      const potential = countryTerritories.reduce((sum, t) => sum + t.potentialRevenue, 0);
      const actual = countryTerritories.reduce((sum, t) => sum + t.actualRevenue, 0);
      return {
        country,
        count: countryTerritories.length,
        potential,
        actual,
        attainment: potential > 0 ? (actual / potential) * 100 : 0,
        territories: countryTerritories,
      };
    });
  }, []);

  const topPerformingTerritories = useMemo(() => {
    return [...mockTerritories]
      .filter(t => t.status === 'active')
      .sort((a, b) => b.attainment - a.attainment)
      .slice(0, 5);
  }, []);

  return (
    <div className="territories-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Territory Management</h1>
          <p className="page-subtitle">Manage territories and assignments across all countries</p>
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
            Add Territory
          </Button>
        </div>
      </div>

      {/* Filters */}
      <Card>
        <CardBody>
          <div className="territories-filters">
            <div className="filter-search">
              <input
                type="text"
                placeholder="Search territories..."
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
              value={selectedType}
              onChange={(e) => setSelectedType(e.target.value)}
              className="filter-select"
            >
              {TERRITORY_TYPES.map(option => (
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
            title="Total Territories"
            value={globalMetrics.totalTerritories}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
                <circle cx="12" cy="10" r="3" />
              </svg>
            }
          />
          <MetricCard
            title="Active Territories"
            value={globalMetrics.activeTerritories}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <polyline points="20 6 9 17 4 12" />
              </svg>
            }
          />
          <MetricCard
            title="Unassigned"
            value={globalMetrics.unassignedTerritories}
            change={globalMetrics.unassignedTerritories > 0 ? undefined : 0}
            changeType={globalMetrics.unassignedTerritories > 0 ? 'decrease' : 'increase'}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="12" cy="12" r="10" />
                <line x1="12" y1="8" x2="12" y2="12" />
                <line x1="12" y1="16" x2="12.01" y2="16" />
              </svg>
            }
          />
          <MetricCard
            title="Partners Assigned"
            value={globalMetrics.totalPartners}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                <circle cx="9" cy="7" r="4" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Revenue Metrics */}
      <section className="revenue-section">
        <div className="revenue-cards">
          <MetricCard
            title="Total Potential Revenue"
            value={formatCurrency(globalMetrics.totalPotential)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Actual Revenue"
            value={formatCurrency(globalMetrics.totalActual)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <polyline points="20 6 9 17 4 12" />
              </svg>
            }
          />
          <MetricCard
            title="Avg Attainment"
            value={formatPercentage(globalMetrics.avgAttainment)}
            attainment={globalMetrics.avgAttainment}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
          <MetricCard
            title="Total Leads"
            value={globalMetrics.totalLeads}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Territories by Country */}
      <section className="countries-section">
        <Card>
          <CardHeader>
            <h3>Territories by Country</h3>
          </CardHeader>
          <CardBody>
            <div className="countries-grid">
              {territoriesByCountry.map((item) => (
                <div key={item.country.code} className="country-territory-card">
                  <div className="country-header">
                    <span className="country-flag">{item.country.flag}</span>
                    <div className="country-info">
                      <h4 className="country-name">{item.country.name}</h4>
                      <span className="territory-count">{item.count} territories</span>
                    </div>
                  </div>
                  <div className="country-metrics">
                    <div className="metric">
                      <span className="metric-label">Potential</span>
                      <span className="metric-value">{formatCurrency(item.potential)}</span>
                    </div>
                    <div className="metric">
                      <span className="metric-label">Actual</span>
                      <span className="metric-value">{formatCurrency(item.actual)}</span>
                    </div>
                    <div className="metric">
                      <span className="metric-label">Attainment</span>
                      <span className={`metric-value ${item.attainment >= 70 ? 'success' : ''}`}>
                        {formatPercentage(item.attainment)}
                      </span>
                    </div>
                  </div>
                  <div className="territory-progress">
                    <div
                      className={`progress-fill ${item.attainment >= 70 ? 'success' : ''}`}
                      style={{ width: `${Math.min(item.attainment, 100)}%` }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Territories Table */}
      <section className="table-section">
        <Card>
          <CardHeader>
            <h3>All Territories</h3>
            <span className="results-count">{filteredTerritories.length} results</span>
          </CardHeader>
          <CardBody>
            <Table
              columns={[
                {
                  key: 'territory',
                  label: 'Territory',
                  render: (territory: Territory) => (
                    <div className="territory-cell">
                      <div className="territory-info">
                        <span className="territory-name">{territory.name}</span>
                        <span className="territory-code">{territory.code}</span>
                      </div>
                    </div>
                  ),
                },
                {
                  key: 'country',
                  label: 'Country',
                  render: (territory: Territory) => (
                    <span>
                      {territory.country.flag} {territory.country.name}
                    </span>
                  ),
                },
                {
                  key: 'type',
                  label: 'Type',
                  render: (territory: Territory) => (
                    <span className={`type-badge type-${territory.type}`}>
                      {territory.type}
                    </span>
                  ),
                },
                {
                  key: 'assignedTo',
                  label: 'Assigned To',
                  render: (territory: Territory) => (
                    territory.assignedTo ? (
                      <div className="assigned-cell">
                        {territory.assignedTo.avatar && (
                          <img src={territory.assignedTo.avatar} alt="" className="assigned-avatar" />
                        )}
                        <span className="assigned-name">{territory.assignedTo.name}</span>
                      </div>
                    ) : (
                      <span className="unassigned-badge">Unassigned</span>
                    )
                  ),
                },
                {
                  key: 'partners',
                  label: 'Partners',
                  render: (territory: Territory) => territory.partnersAssigned,
                },
                {
                  key: 'potential',
                  label: 'Potential',
                  render: (territory: Territory) => formatCurrency(territory.potentialRevenue),
                },
                {
                  key: 'actual',
                  label: 'Actual',
                  render: (territory: Territory) => formatCurrency(territory.actualRevenue),
                },
                {
                  key: 'attainment',
                  label: 'Attainment',
                  render: (territory: Territory) => (
                    <div className="attainment-cell">
                      <span className={`attainment-value ${territory.attainment >= 70 ? 'success' : ''}`}>
                        {formatPercentage(territory.attainment)}
                      </span>
                      <div className="attainment-bar">
                        <div
                          className={`attainment-fill ${territory.attainment >= 70 ? 'success' : ''}`}
                          style={{ width: `${Math.min(territory.attainment, 100)}%` }}
                        ></div>
                      </div>
                    </div>
                  ),
                },
                {
                  key: 'status',
                  label: 'Status',
                  render: (territory: Territory) => (
                    <span className={`status-badge status-${territory.status}`}>
                      {territory.status === 'active' && 'Active'}
                      {territory.status === 'pending' && 'Pending'}
                      {territory.status === 'inactive' && 'Inactive'}
                    </span>
                  ),
                },
                {
                  key: 'actions',
                  label: 'Actions',
                  render: (territory: Territory) => (
                    <div className="actions-cell">
                      <Button variant="link" size="sm">View</Button>
                      <Button variant="link" size="sm">Edit</Button>
                    </div>
                  ),
                },
              ]}
              data={filteredTerritories}
            />
          </CardBody>
        </Card>
      </section>

      {/* Top Performing Territories */}
      <section className="top-territories-section">
        <Card>
          <CardHeader>
            <h3>Top Performing Territories</h3>
          </CardHeader>
          <CardBody>
            <div className="top-territories-list">
              {topPerformingTerritories.map((territory, index) => (
                <div key={territory.id} className="top-territory-item">
                  <div className="territory-rank">
                    <span className={`rank-badge rank-${index + 1}`}>
                      {index + 1}
                    </span>
                  </div>
                  <div className="territory-details">
                    <span className="territory-name">{territory.name}</span>
                    <span className="territory-meta">
                      {territory.country.flag} {territory.assignedTo?.name || 'Unassigned'}
                    </span>
                  </div>
                  <div className="territory-metrics">
                    <div className="metric">
                      <span className="metric-label">Revenue</span>
                      <span className="metric-value">{formatCurrency(territory.actualRevenue)}</span>
                    </div>
                    <div className="metric">
                      <span className="metric-label">Attainment</span>
                      <span className="metric-value success">{formatPercentage(territory.attainment)}</span>
                    </div>
                    <div className="metric">
                      <span className="metric-label">Partners</span>
                      <span className="metric-value">{territory.partnersAssigned}</span>
                    </div>
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
