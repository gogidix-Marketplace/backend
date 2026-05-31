// SocialPage - Global Marketing Dashboard
// Social media overview and management page

import React, { useState } from 'react';
import { SocialPost, SocialPlatform, SocialPostStatus } from '../../../domain/types';
import { useSocialStore } from '../../../infrastructure/stores/socialStore';
import { SocialPostsTable } from '../../../shared/components/tables/SocialPostsTable';
import { EngagementChart } from '../../../shared/components/charts/EngagementChart';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { formatCompactNumber, formatPercentage } from '../../../shared/utils/formatters';
import './SocialPage.css';

const SocialPage: React.FC = () => {
  const posts = useSocialStore((state) => state.getFilteredPosts());
  const metrics = useSocialStore((state) => state.getMetrics());

  const [activeTab, setActiveTab] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const engagementData = [
    {
      platform: 'facebook',
      followers: 150000,
      engagement: 45000,
      likes: 38000,
      comments: 4200,
      shares: 2800,
      clicks: 12500,
      impressions: 2500000,
      engagementRate: 1.8,
    },
    {
      platform: 'instagram',
      followers: 95000,
      engagement: 58000,
      likes: 48000,
      comments: 5600,
      shares: 4400,
      clicks: 18200,
      impressions: 3200000,
      engagementRate: 1.81,
    },
    {
      platform: 'linkedin',
      followers: 75000,
      engagement: 22000,
      likes: 14500,
      comments: 3800,
      shares: 3700,
      clicks: 28500,
      impressions: 850000,
      engagementRate: 2.59,
    },
    {
      platform: 'twitter',
      followers: 45000,
      engagement: 8500,
      likes: 6200,
      comments: 890,
      shares: 1410,
      clicks: 8900,
      impressions: 420000,
      engagementRate: 2.02,
    },
  ];

  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId);
  };

  const tabs = [
    { id: 'all', label: 'All Posts' },
    { id: 'published', label: 'Published' },
    { id: 'scheduled', label: 'Scheduled' },
    { id: 'draft', label: 'Draft' },
  ];

  const platformTabs = [
    { id: 'all', label: 'All Platforms' },
    { id: 'linkedin', label: 'LinkedIn' },
    { id: 'twitter', label: 'Twitter' },
    { id: 'facebook', label: 'Facebook' },
    { id: 'instagram', label: 'Instagram' },
  ];

  return (
    <div className="social-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Social Media</h1>
              <p className="page__subtitle">Social media overview across all platforms and countries</p>
            </div>
            <div className="page__actions">
              <Button variant="secondary">Schedule Bulk Post</Button>
              <Button variant="primary">+ Create Post</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard
            label="Total Followers"
            value={formatCompactNumber(365000)}
            change={8}
            changeType="positive"
          />
          <MetricCard
            label="Total Engagement"
            value={formatCompactNumber(metrics.totalEngagement)}
            change={15}
            changeType="positive"
          />
          <MetricCard
            label="Engagement Rate"
            value={formatPercentage(metrics.avgEngagementRate)}
            change={5}
            changeType="positive"
          />
          <MetricCard
            label="Published Posts"
            value={metrics.published}
            change={12}
            changeType="positive"
          />
          <MetricCard
            label="Scheduled"
            value={metrics.scheduled}
          />
        </div>

        {/* Engagement Chart */}
        <EngagementChart data={engagementData} />

        {/* Platform Tabs */}
        <div className="social-platform-tabs">
          <Tabs tabs={platformTabs} activeTab={activeTab} onChange={setActiveTab} variant="pills" />
          <SearchBar value={searchQuery} onChange={setSearchQuery} placeholder="Search posts..." />
        </div>

        {/* Posts Table */}
        <div className="social-posts-container">
          <SocialPostsTable posts={posts} />
        </div>
      </div>
    </div>
  );
};

export default SocialPage;
