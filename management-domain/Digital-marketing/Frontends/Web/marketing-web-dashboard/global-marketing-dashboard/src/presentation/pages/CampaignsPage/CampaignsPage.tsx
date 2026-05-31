// CampaignsPage - Global Marketing Dashboard
// Global campaign management page

import React, { useState } from 'react';
import { Campaign, CampaignStatus, CampaignType } from '../../../domain/types';
import { useCampaignStore } from '../../../infrastructure/stores/campaignStore';
import { CampaignsTable } from '../../../shared/components/tables/CampaignsTable';
import { CampaignForm } from '../../../shared/components/forms/CampaignForm';
import { Button } from '../../../shared/components/common/Button';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { FilterPanel } from '../../../shared/components/common/FilterPanel';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { formatCurrency, formatCompactNumber } from '../../../shared/utils/formatters';
import './CampaignsPage.css';

const CampaignsPage: React.FC = () => {
  const campaigns = useCampaignStore((state) => state.getFilteredCampaigns());
  const { addCampaign, updateCampaign, setFilters } = useCampaignStore();

  const [selectedCampaign, setSelectedCampaign] = useState<Campaign | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [formMode, setFormMode] = useState<'create' | 'edit'>('create');
  const [activeTab, setActiveTab] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const handleCreateCampaign = () => {
    setSelectedCampaign(null);
    setFormMode('create');
    setIsFormOpen(true);
  };

  const handleEditCampaign = (campaign: Campaign) => {
    setSelectedCampaign(campaign);
    setFormMode('edit');
    setIsFormOpen(true);
  };

  const handleFormSubmit = (data: Partial<Campaign>) => {
    if (formMode === 'create') {
      const newCampaign: Campaign = {
        id: `camp-${Date.now()}`,
        ...data as Campaign,
      };
      addCampaign(newCampaign);
    } else if (selectedCampaign) {
      updateCampaign(selectedCampaign.id, data);
    }
    setIsFormOpen(false);
    setSelectedCampaign(null);
  };

  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId);
    if (tabId === 'all') {
      setFilters({ status: undefined });
    } else {
      setFilters({ status: tabId as CampaignStatus });
    }
  };

  // Calculate summary metrics
  const summaryMetrics = {
    total: campaigns.length,
    active: campaigns.filter(c => c.status === 'active').length,
    scheduled: campaigns.filter(c => c.status === 'scheduled').length,
    completed: campaigns.filter(c => c.status === 'completed').length,
    totalBudget: campaigns.reduce((sum, c) => sum + c.budget.total, 0),
    totalSpent: campaigns.reduce((sum, c) => sum + c.budget.spent, 0),
    totalRevenue: campaigns.reduce((sum, c) => sum + c.metrics.revenue, 0),
    avgROAS: campaigns.length > 0
      ? campaigns.reduce((sum, c) => sum + c.metrics.roas, 0) / campaigns.length
      : 0,
  };

  const tabs = [
    { id: 'all', label: 'All Campaigns' },
    { id: 'active', label: 'Active' },
    { id: 'scheduled', label: 'Scheduled' },
    { id: 'completed', label: 'Completed' },
    { id: 'draft', label: 'Draft' },
  ];

  return (
    <div className="campaigns-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Global Campaigns</h1>
              <p className="page__subtitle">Manage campaigns across all regions and channels</p>
            </div>
            <div className="page__actions">
              <Button variant="primary" onClick={handleCreateCampaign}>
                + Create Campaign
              </Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard
            label="Total Campaigns"
            value={summaryMetrics.total}
            size="md"
          />
          <MetricCard
            label="Active"
            value={summaryMetrics.active}
            change={12}
            changeType="positive"
            size="md"
          />
          <MetricCard
            label="Total Budget"
            value={formatCurrency(summaryMetrics.totalBudget)}
            size="md"
          />
          <MetricCard
            label="Total Spent"
            value={formatCurrency(summaryMetrics.totalSpent)}
            size="md"
          />
          <MetricCard
            label="Total Revenue"
            value={formatCurrency(summaryMetrics.totalRevenue)}
            change={22}
            changeType="positive"
            size="md"
          />
          <MetricCard
            label="Avg ROAS"
            value={`${summaryMetrics.avgROAS.toFixed(2)}x`}
            change={5}
            changeType="positive"
            size="md"
          />
        </div>

        {/* Tabs and Search */}
        <div className="campaigns-controls">
          <Tabs
            tabs={tabs}
            activeTab={activeTab}
            onChange={handleTabChange}
            variant="default"
          />
          <SearchBar
            value={searchQuery}
            onChange={setSearchQuery}
            placeholder="Search campaigns..."
            onSearch={(value) => setFilters({ search: value })}
          />
        </div>

        {/* Campaigns Table */}
        <div className="campaigns-table-container">
          <CampaignsTable
            campaigns={campaigns}
            onCampaignClick={handleEditCampaign}
          />
        </div>

        {/* Campaign Form Modal */}
        <CampaignForm
          isOpen={isFormOpen}
          onClose={() => {
            setIsFormOpen(false);
            setSelectedCampaign(null);
          }}
          onSubmit={handleFormSubmit}
          campaign={selectedCampaign || undefined}
          mode={formMode}
        />
      </div>
    </div>
  );
};

export default CampaignsPage;
