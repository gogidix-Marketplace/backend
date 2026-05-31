// Teams Page
// Global sales team management across countries

import { useState, useMemo } from 'react';
import { NavLink, Outlet, useParams } from 'react-router-dom';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Table } from '../components/tables/Table';
import { TopPerformersList } from '../components/charts';
import { FilterForm, StatusFilter } from '../components/forms';
import { formatCurrency } from '@shared';
import { mockCountries, mockTopPerformers } from '@shared/mock-data';
import './TeamsPage.css';

// Mock team members data
const mockTeamMembers = [
  {
    id: 'usr-101',
    name: 'Sarah Williams',
    avatar: 'https://i.pravatar.cc/150?img=1',
    country: mockCountries[3],
    role: 'Country Director',
    department: 'Enterprise',
    revenue: 280000,
    quota: 200000,
    attainment: 140,
    dealsClosed: 35,
    teamSize: 12,
    status: 'active',
  },
  {
    id: 'usr-102',
    name: 'John Doe',
    avatar: 'https://i.pravatar.cc/150?img=2',
    country: mockCountries[0],
    role: 'Country Director',
    department: 'Enterprise',
    revenue: 250000,
    quota: 200000,
    attainment: 125,
    dealsClosed: 28,
    teamSize: 15,
    status: 'active',
  },
  {
    id: 'usr-103',
    name: 'Jane Smith',
    avatar: 'https://i.pravatar.cc/150?img=3',
    country: mockCountries[1],
    role: 'Regional Manager',
    department: 'SMB',
    revenue: 180000,
    quota: 150000,
    attainment: 120,
    dealsClosed: 42,
    teamSize: 8,
    status: 'active',
  },
  {
    id: 'usr-104',
    name: 'Bob Johnson',
    avatar: 'https://i.pravatar.cc/150?img=4',
    country: mockCountries[2],
    role: 'Sales Manager',
    department: 'Retail',
    revenue: 145000,
    quota: 150000,
    attainment: 97,
    dealsClosed: 38,
    teamSize: 6,
    status: 'active',
  },
  {
    id: 'usr-105',
    name: 'Michael Chen',
    avatar: 'https://i.pravatar.cc/150?img=5',
    country: mockCountries[4],
    role: 'Country Director',
    department: 'Enterprise',
    revenue: 135000,
    quota: 140000,
    attainment: 96,
    dealsClosed: 22,
    teamSize: 10,
    status: 'active',
  },
  {
    id: 'usr-106',
    name: 'Grace Amani',
    avatar: 'https://i.pravatar.cc/150?img=6',
    country: mockCountries[5],
    role: 'Regional Manager',
    department: 'SMB',
    revenue: 95000,
    quota: 100000,
    attainment: 95,
    dealsClosed: 18,
    teamSize: 5,
    status: 'active',
  },
];

const STATUS_OPTIONS = [
  { value: 'active', label: 'Active', color: '#10B981' },
  { value: 'inactive', label: 'Inactive', color: '#6B7280' },
  { value: 'on_leave', label: 'On Leave', color: '#F59E0B' },
];

export function TeamsPage() {
  return (
    <div className="teams-page">
      <Outlet />
    </div>
  );
}

export function TeamsOverview() {
  const [selectedCountries, setSelectedCountries] = useState<string[]>([]);
  const [selectedStatuses, setSelectedStatuses] = useState<string[]>(['active']);
  const [searchQuery, setSearchQuery] = useState('');

  const filteredMembers = useMemo(() => {
    return mockTeamMembers.filter((member) => {
      if (selectedCountries.length > 0 && !selectedCountries.includes(member.country.code)) {
        return false;
      }
      if (selectedStatuses.length > 0 && !selectedStatuses.includes(member.status)) {
        return false;
      }
      if (searchQuery && !member.name.toLowerCase().includes(searchQuery.toLowerCase())) {
        return false;
      }
      return true;
    });
  }, [selectedCountries, selectedStatuses, searchQuery]);

  const globalMetrics = useMemo(() => {
    const totalRevenue = filteredMembers.reduce((sum, m) => sum + m.revenue, 0);
    const totalQuota = filteredMembers.reduce((sum, m) => sum + m.quota, 0);
    const totalDeals = filteredMembers.reduce((sum, m) => sum + m.dealsClosed, 0);
    const avgAttainment = filteredMembers.length > 0
      ? filteredMembers.reduce((sum, m) => sum + m.attainment, 0) / filteredMembers.length
      : 0;

    return {
      totalMembers: filteredMembers.length,
      totalRevenue,
      totalQuota,
      attainment: avgAttainment,
      totalDeals,
    };
  }, [filteredMembers]);

  return (
    <div className="teams-overview">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Global Sales Teams</h1>
          <p className="page-subtitle">Manage and track sales team performance across countries</p>
        </div>
        <div className="page-actions">
          <Button variant="primary">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
              <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z" />
            </svg>
            Add Team Member
          </Button>
        </div>
      </div>

      {/* Filters */}
      <Card>
        <CardBody>
          <div className="teams-filters">
            <div className="filter-search">
              <input
                type="text"
                placeholder="Search team members..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="search-input"
              />
            </div>
            <StatusFilter
              options={STATUS_OPTIONS}
              selected={selectedStatuses}
              onChange={setSelectedStatuses}
            />
          </div>
        </CardBody>
      </Card>

      {/* Key Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Total Team Members"
            value={globalMetrics.totalMembers}
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
            title="Team Revenue"
            value={formatCurrency(globalMetrics.totalRevenue)}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Avg Attainment"
            value={`${globalMetrics.attainment.toFixed(0)}%`}
            attainment={globalMetrics.attainment}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            }
          />
          <MetricCard
            title="Total Deals Closed"
            value={globalMetrics.totalDeals}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l3.76 3.76a1 1 0 0 0 1.4-0l1.6-1.6a1 1 0 0 0 0-1.4z" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Top Performers and Team List */}
      <section className="content-grid">
        {/* Top Performers */}
        <Card>
          <CardHeader>
            <h3>Top Performers</h3>
            <Button variant="link" size="sm">View All</Button>
          </CardHeader>
          <CardBody>
            <TopPerformersList performers={mockTopPerformers} limit={5} compact />
          </CardBody>
        </Card>

        {/* Team Members Table */}
        <Card>
          <CardHeader>
            <h3>Team Members</h3>
            <Button variant="link" size="sm">View Directory</Button>
          </CardHeader>
          <CardBody>
            <Table
              columns={[
                {
                  key: 'member',
                  label: 'Team Member',
                  render: (member: any) => (
                    <div className="member-cell">
                      {member.avatar && <img src={member.avatar} alt="" className="member-avatar" />}
                      <div className="member-info">
                        <span className="member-name">{member.name}</span>
                        <span className="member-role">{member.role}</span>
                      </div>
                    </div>
                  ),
                },
                {
                  key: 'country',
                  label: 'Country',
                  render: (member: any) => (
                    <span>
                      {member.country.flag} {member.country.name}
                    </span>
                  ),
                },
                {
                  key: 'department',
                  label: 'Department',
                  render: (member: any) => member.department,
                },
                {
                  key: 'revenue',
                  label: 'Revenue',
                  render: (member: any) => formatCurrency(member.revenue),
                },
                {
                  key: 'attainment',
                  label: 'Attainment',
                  render: (member: any) => `${member.attainment}%`,
                },
                {
                  key: 'deals',
                  label: 'Deals',
                  render: (member: any) => member.dealsClosed,
                },
                {
                  key: 'teamSize',
                  label: 'Team Size',
                  render: (member: any) => member.teamSize,
                },
                {
                  key: 'actions',
                  label: 'Actions',
                  render: (member: any) => (
                    <Button variant="link" size="sm">View Profile</Button>
                  ),
                },
              ]}
              data={filteredMembers}
            />
          </CardBody>
        </Card>
      </section>

      {/* Teams by Country */}
      <section className="by-country-section">
        <Card>
          <CardHeader>
            <h3>Teams by Country</h3>
          </CardHeader>
          <CardBody>
            <div className="countries-grid">
              {mockCountries.map((country) => {
                const countryMembers = mockTeamMembers.filter((m) => m.country.code === country.code);
                const countryRevenue = countryMembers.reduce((sum, m) => sum + m.revenue, 0);

                return (
                  <div key={country.code} className="country-team-card">
                    <div className="country-team-header">
                      <span className="country-flag">{country.flag}</span>
                      <span className="country-name">{country.name}</span>
                    </div>
                    <div className="country-team-stats">
                      <div className="stat">
                        <span className="stat-value">{countryMembers.length}</span>
                        <span className="stat-label">Members</span>
                      </div>
                      <div className="stat">
                        <span className="stat-value">{formatCurrency(countryRevenue)}</span>
                        <span className="stat-label">Revenue</span>
                      </div>
                    </div>
                    <Button variant="link" size="sm">View Team</Button>
                  </div>
                );
              })}
            </div>
          </CardBody>
        </Card>
      </section>
    </div>
  );
}

export function TeamMemberDetail() {
  const { memberId } = useParams<{ memberId: string }>();
  const member = mockTeamMembers.find((m) => m.id === memberId);

  if (!member) {
    return <div className="error-state">Team member not found</div>;
  }

  return (
    <div className="team-member-detail">
      <div className="member-header">
        {member.avatar && <img src={member.avatar} alt={member.name} className="member-avatar-large" />}
        <div className="member-header-info">
          <h1>{member.name}</h1>
          <p className="member-role">{member.role} - {member.department}</p>
          <p className="member-location">
            {member.country.flag} {member.country.name}
          </p>
        </div>
        <div className="member-header-actions">
          <Button variant="primary">Edit Profile</Button>
        </div>
      </div>

      <div className="member-metrics">
        <MetricCard title="Revenue" value={formatCurrency(member.revenue)} />
        <MetricCard title="Quota" value={formatCurrency(member.quota)} />
        <MetricCard title="Attainment" value={`${member.attainment}%`} attainment={member.attainment} />
        <MetricCard title="Deals Closed" value={member.dealsClosed} />
      </div>

      <Card>
        <CardHeader>
          <h3>Team Members ({member.teamSize})</h3>
        </CardHeader>
        <CardBody>
          <p className="text-muted">Team member listing...</p>
        </CardBody>
      </Card>
    </div>
  );
}
