// ContentPage - Global Marketing Dashboard
// Global content calendar and management page

import React, { useState } from 'react';
import { Content, ContentStatus, ContentType } from '../../../domain/types';
import { useContentStore } from '../../../infrastructure/stores/contentStore';
import { ContentTable } from '../../../shared/components/tables/ContentTable';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { FilterPanel, FilterGroup } from '../../../shared/components/common/FilterPanel';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { formatCompactNumber } from '../../../shared/utils/formatters';
import './ContentPage.css';

const ContentPage: React.FC = () => {
  const content = useContentStore((state) => state.getFilteredContent());

  const [activeTab, setActiveTab] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  // Calculate summary metrics
  const summaryMetrics = {
    total: content.length,
    published: content.filter(c => c.status === 'published').length,
    pendingApproval: content.filter(c => c.status === 'pending_approval').length,
    scheduled: content.filter(c => c.status === 'scheduled').length,
    totalViews: content.reduce((sum, c) => sum + c.metrics.views, 0),
    totalLeads: content.reduce((sum, c) => sum + c.metrics.leads, 0),
  };

  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId);
  };

  const tabs = [
    { id: 'all', label: 'All Content' },
    { id: 'published', label: 'Published' },
    { id: 'pending_approval', label: 'Pending Approval' },
    { id: 'scheduled', label: 'Scheduled' },
    { id: 'draft', label: 'Draft' },
  ];

  const filterGroups: FilterGroup[] = [
    {
      id: 'type',
      label: 'Content Type',
      type: 'select',
      options: [
        { value: 'blog_post', label: 'Blog Post' },
        { value: 'social_post', label: 'Social Post' },
        { value: 'email', label: 'Email' },
        { value: 'video', label: 'Video' },
        { value: 'infographic', label: 'Infographic' },
        { value: 'whitepaper', label: 'Whitepaper' },
        { value: 'case_study', label: 'Case Study' },
        { value: 'webinar', label: 'Webinar' },
      ],
    },
    {
      id: 'author',
      label: 'Author',
      type: 'select',
      options: [
        { value: 'user-001', label: 'Sarah Mitchell' },
        { value: 'user-002', label: 'James Wilson' },
        { value: 'user-003', label: 'Emma Rodriguez' },
        { value: 'user-004', label: 'Michael Chen' },
      ],
    },
    {
      id: 'dateRange',
      label: 'Publish Date Range',
      type: 'daterange',
    },
  ];

  return (
    <div className="content-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Content Management</h1>
              <p className="page__subtitle">Global content calendar and approval workflows</p>
            </div>
            <div className="page__actions">
              <Button variant="secondary">Content Calendar</Button>
              <Button variant="primary">+ Create Content</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard label="Total Content" value={summaryMetrics.total} />
          <MetricCard
            label="Published"
            value={summaryMetrics.published}
            change={12}
            changeType="positive"
          />
          <MetricCard label="Pending Approval" value={summaryMetrics.pendingApproval} />
          <MetricCard label="Total Views" value={formatCompactNumber(summaryMetrics.totalViews)} />
          <MetricCard
            label="Content Leads"
            value={formatCompactNumber(summaryMetrics.totalLeads)}
            change={18}
            changeType="positive"
          />
        </div>

        {/* Tabs and Search */}
        <div className="content-controls">
          <Tabs tabs={tabs} activeTab={activeTab} onChange={handleTabChange} />
          <SearchBar value={searchQuery} onChange={setSearchQuery} placeholder="Search content..." />
        </div>

        {/* Filter Panel */}
        <FilterPanel
          filters={filterGroups}
          onFilterChange={(id, value) => console.log('Filter', id, value)}
          onClear={() => console.log('Clear filters')}
          defaultExpanded={false}
        />

        {/* Content Table */}
        <div className="content-table-container">
          <ContentTable content={content} />
        </div>
      </div>
    </div>
  );
};

export default ContentPage;
