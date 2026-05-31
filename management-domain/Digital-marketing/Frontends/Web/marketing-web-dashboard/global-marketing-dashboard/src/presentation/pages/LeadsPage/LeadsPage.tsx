// LeadsPage - Global Marketing Dashboard
// Global lead generation and management page

import React, { useState, useMemo } from 'react';
import { Lead, LeadStatus, LeadQuality } from '../../../domain/types';
import { useLeadStore } from '../../../infrastructure/stores/leadStore';
import { LeadsTable } from '../../../shared/components/tables/LeadsTable';
import { LeadSourceChart } from '../../../shared/components/charts/LeadSourceChart';
import { FunnelChart } from '../../../shared/components/charts/FunnelChart';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { FilterPanel, FilterGroup } from '../../../shared/components/common/FilterPanel';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { formatCurrency, formatCompactNumber, formatPercentage } from '../../../shared/utils/formatters';
import './LeadsPage.css';

const LeadsPage: React.FC = () => {
  const { metrics: leadMetrics, leads } = useLeadStore();

  const [activeTab, setActiveTab] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const leadSourceData = useMemo(() => {
    return Object.entries(leadMetrics.bySource).map(([source, count]) => ({
      source: source.charAt(0).toUpperCase() + source.slice(1).replace('_', ' '),
      leads: count,
      percentage: (count / leadMetrics.total) * 100,
      conversionRate: 2 + Math.random() * 6,
    }));
  }, [leadMetrics]);

  const funnelData = useMemo(() => {
    return [
      { name: 'Impressions', value: 15000000 },
      { name: 'Visitors', value: 2500000 },
      { name: 'Leads', value: leadMetrics.total },
      { name: 'Qualified', value: leadMetrics.qualified },
      { name: 'Customers', value: leadMetrics.converted },
    ];
  }, [leadMetrics]);

  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId);
  };

  const tabs = [
    { id: 'all', label: 'All Leads' },
    { id: 'new', label: 'New' },
    { id: 'contacted', label: 'Contacted' },
    { id: 'qualified', label: 'Qualified' },
    { id: 'converted', label: 'Converted' },
  ];

  const filterGroups: FilterGroup[] = [
    {
      id: 'quality',
      label: 'Lead Quality',
      type: 'select',
      options: [
        { value: 'hot', label: 'Hot' },
        { value: 'warm', label: 'Warm' },
        { value: 'cold', label: 'Cold' },
        { value: 'unqualified', label: 'Unqualified' },
      ],
    },
    {
      id: 'source',
      label: 'Lead Source',
      type: 'select',
      options: [
        { value: 'website', label: 'Website' },
        { value: 'email', label: 'Email' },
        { value: 'social_media', label: 'Social Media' },
        { value: 'search_engine', label: 'Search Engine' },
        { value: 'referral', label: 'Referral' },
        { value: 'event', label: 'Event' },
      ],
    },
    {
      id: 'country',
      label: 'Country',
      type: 'select',
      options: [
        { value: 'US', label: 'United States' },
        { value: 'GB', label: 'United Kingdom' },
        { value: 'DE', label: 'Germany' },
        { value: 'FR', label: 'France' },
        { value: 'CA', label: 'Canada' },
      ],
    },
    {
      id: 'dateRange',
      label: 'Date Range',
      type: 'daterange',
    },
  ];

  return (
    <div className="leads-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Global Leads</h1>
              <p className="page__subtitle">Manage and track lead generation across all channels</p>
            </div>
            <div className="page__actions">
              <Button variant="secondary">Export Leads</Button>
              <Button variant="primary">+ Add Lead</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard
            label="Total Leads"
            value={formatCompactNumber(leadMetrics.total)}
            change={18}
            changeType="positive"
            icon="👥"
          />
          <MetricCard
            label="New Leads"
            value={formatCompactNumber(leadMetrics.new)}
            change={22}
            changeType="positive"
            icon="✨"
          />
          <MetricCard
            label="Qualified"
            value={formatCompactNumber(leadMetrics.qualified)}
            change={15}
            changeType="positive"
            icon="⭐"
          />
          <MetricCard
            label="Converted"
            value={formatCompactNumber(leadMetrics.converted)}
            change={12}
            changeType="positive"
            icon="✅"
          />
          <MetricCard
            label="Conversion Rate"
            value={formatPercentage(leadMetrics.conversionRate)}
            change={8}
            changeType="positive"
            icon="📈"
          />
          <MetricCard
            label="Avg Value"
            value={formatCurrency(leadMetrics.averageValue)}
            change={5}
            changeType="positive"
            icon="💰"
          />
        </div>

        {/* Charts Row */}
        <div className="grid grid--2">
          <LeadSourceChart data={leadSourceData} view="horizontal" />
          <FunnelChart data={funnelData} height={250} />
        </div>

        {/* Tabs and Search */}
        <div className="leads-controls">
          <Tabs
            tabs={tabs}
            activeTab={activeTab}
            onChange={handleTabChange}
            variant="default"
          />
          <SearchBar
            value={searchQuery}
            onChange={setSearchQuery}
            placeholder="Search leads..."
          />
        </div>

        {/* Filter Panel */}
        <FilterPanel
          filters={filterGroups}
          onFilterChange={(id, value) => console.log('Filter', id, value)}
          onClear={() => console.log('Clear filters')}
          defaultExpanded={false}
        />

        {/* Leads Table */}
        <div className="leads-table-container">
          <LeadsTable leads={leads} />
        </div>
      </div>
    </div>
  );
};

export default LeadsPage;
