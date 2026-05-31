// SalesTeamsPage Component
// Page showing all sales teams in the country

import React, { useState, useEffect } from 'react';
import { useTeamsStore } from '@infrastructure/stores';
import { mockSalesTeams, mockRegions } from '@shared/mock-data';
import { TeamsTable } from '../components/tables';
import { TeamForm } from '../components/forms';
import { SearchBar, Button, StatusBadge } from '../components/common';
import { Tabs } from '../components/common';
import type { SalesTeam, TeamStatus } from '@domain/types';

export const SalesTeamsPage: React.FC = () => {
  const { teams, isLoading } = useTeamsStore();
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<TeamStatus | 'ALL'>('ALL');
  const [showForm, setShowForm] = useState(false);
  const [selectedTeam, setSelectedTeam] = useState<SalesTeam | undefined>();
  const [activeTab, setActiveTab] = useState('teams');

  // Use mock data for development
  const displayTeams = mockSalesTeams;

  const filteredTeams = displayTeams.filter(team => {
    const matchesSearch = team.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      team.code.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || team.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const handleAddTeam = () => {
    setSelectedTeam(undefined);
    setShowForm(true);
  };

  const handleEditTeam = (team: SalesTeam) => {
    setSelectedTeam(team);
    setShowForm(true);
  };

  const handleDeleteTeam = async (teamId: string) => {
    if (confirm('Are you sure you want to delete this team?')) {
      // Delete logic here
    }
  };

  const handleStatusChange = async (teamId: string, status: TeamStatus) => {
    // Status change logic here
  };

  const handleSubmitTeam = async (teamData: Partial<SalesTeam>) => {
    // Submit logic here
    setShowForm(false);
  };

  const teamStats = {
    total: displayTeams.length,
    active: displayTeams.filter(t => t.status === 'active').length,
    totalRevenue: displayTeams.reduce((sum, t) => sum + t.metrics.revenueThisMonth, 0),
    avgAttainment: Math.round(
      displayTeams.reduce((sum, t) => sum + t.metrics.quotaAttainment, 0) / displayTeams.length
    ),
  };

  const tabs = [
    {
      id: 'teams',
      label: 'Teams',
      content: (
        <TeamsTable
          teams={filteredTeams}
          isLoading={isLoading}
          onTeamClick={(team) => {/* Navigate to team details */}}
          onEdit={handleEditTeam}
          onDelete={handleDeleteTeam}
          onStatusChange={handleStatusChange}
        />
      ),
    },
    {
      id: 'members',
      label: 'All Members',
      content: (
        <div className="all-members-view">
          <p className="text-center">View all team members across all teams.</p>
        </div>
      ),
    },
  ];

  return (
    <div className="page sales-teams-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Sales Teams</h1>
          <p className="page-subtitle">Manage your country sales teams</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary">Import</Button>
          <Button variant="primary" onClick={handleAddTeam}>Add Team</Button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="stats-summary">
        <div className="stat-item">
          <span className="stat-label">Total Teams</span>
          <span className="stat-value">{teamStats.total}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Active Teams</span>
          <span className="stat-value">{teamStats.active}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Total Revenue</span>
          <span className="stat-value">{teamStats.totalRevenue.toLocaleString()} NGN</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Avg Attainment</span>
          <span className="stat-value">{teamStats.avgAttainment}%</span>
        </div>
      </div>

      {/* Filters */}
      <div className="page-filters">
        <SearchBar
          placeholder="Search teams..."
          value={searchQuery}
          onChange={setSearchQuery}
          className="filter-search"
        />
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as TeamStatus | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Statuses</option>
          <option value="active">Active</option>
          <option value="inactive">Inactive</option>
          <option value="pending">Pending</option>
        </select>
      </div>

      {/* Tabs */}
      <Tabs
        tabs={tabs}
        activeTab={activeTab}
        onChange={setActiveTab}
      />

      {/* Team Form Modal */}
      <TeamForm
        isOpen={showForm}
        onClose={() => setShowForm(false)}
        onSubmit={handleSubmitTeam}
        team={selectedTeam}
        regions={mockRegions}
      />
    </div>
  );
};

export default SalesTeamsPage;
