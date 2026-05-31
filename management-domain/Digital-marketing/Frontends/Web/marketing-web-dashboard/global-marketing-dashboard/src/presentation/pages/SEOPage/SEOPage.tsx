// SEOPage - Global Marketing Dashboard
// Global SEO performance and keyword tracking page

import React, { useState } from 'react';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { BarChart, DataPoint } from '../../../shared/components/charts/BarChart';
import { LineChart } from '../../../shared/components/charts/LineChart';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { formatCompactNumber } from '../../../shared/utils/formatters';
import './SEOPage.css';

const SEOPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('overview');
  const [searchQuery, setSearchQuery] = useState('');

  const mockKeywords = [
    { keyword: 'marketing automation', volume: 49000, rank: 3, change: 2 },
    { keyword: 'digital marketing software', volume: 22200, rank: 5, change: -1 },
    { keyword: 'CRM marketing', volume: 18100, rank: 2, change: 0 },
    { keyword: 'email marketing tools', volume: 14500, rank: 7, change: 3 },
    { keyword: 'lead generation software', volume: 12100, rank: 4, change: 1 },
    { keyword: 'marketing analytics', volume: 9900, rank: 6, change: 4 },
    { keyword: 'B2B marketing platform', volume: 8100, rank: 1, change: 0 },
    { keyword: 'campaign management', volume: 6600, rank: 8, change: 2 },
    { keyword: 'social media management', volume: 5500, rank: 12, change: 5 },
    { keyword: 'marketing automation tools', volume: 4400, rank: 3, change: -2 },
  ];

  const rankingData: DataPoint[] = mockKeywords.map(k => ({
    label: k.keyword,
    value: k.volume,
  }));

  const trendData = [
    { label: 'Week 1', organicTraffic: 125000, keywords: 1250 },
    { label: 'Week 2', organicTraffic: 132000, keywords: 1280 },
    { label: 'Week 3', organicTraffic: 128000, keywords: 1265 },
    { label: 'Week 4', organicTraffic: 145000, keywords: 1320 },
  ];

  const tabs = [
    { id: 'overview', label: 'Overview' },
    { id: 'keywords', label: 'Keywords' },
    { id: 'backlinks', label: 'Backlinks' },
    { id: 'audit', label: 'Site Audit' },
  ];

  return (
    <div className="seo-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">SEO Performance</h1>
              <p className="page__subtitle">Global SEO performance, keyword rankings, and backlink monitoring</p>
            </div>
            <div className="page__actions">
              <Button variant="secondary">Run Audit</Button>
              <Button variant="primary">+ Track Keyword</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard label="Organic Traffic" value={formatCompactNumber(145000)} change={12} changeType="positive" />
          <MetricCard label="Organic Keywords" value={formatCompactNumber(1250)} change={8} changeType="positive" />
          <MetricCard label="Avg Position" value="8.5" change={-2} changeType="positive" />
          <MetricCard label="Backlinks" value={formatCompactNumber(4520)} change={15} changeType="positive" />
          <MetricCard label="Domain Authority" value="52" change={3} changeType="positive" />
          <MetricCard label="Page Authority" value="48" change={2} changeType="positive" />
        </div>

        {/* Tabs */}
        <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} variant="underlined" />

        <TabPanel id="overview" activeTab={activeTab}>
          <div className="grid grid--2">
            <div className="chart-card">
              <h3>Organic Traffic Trend</h3>
              <LineChart
                data={trendData.map(t => ({ label: t.label, value: t.organicTraffic }))}
                color="#10B981"
                height={200}
                showArea
              />
            </div>
            <div className="chart-card">
              <h3>Top Keywords by Volume</h3>
              <BarChart data={rankingData} horizontal height={200} showGrid />
            </div>
          </div>
        </TabPanel>

        <TabPanel id="keywords" activeTab={activeTab}>
          <SearchBar value={searchQuery} onChange={setSearchQuery} placeholder="Search keywords..." className="mb-md" />
          <div className="keywords-table">
            <table>
              <thead>
                <tr>
                  <th>Keyword</th>
                  <th>Volume</th>
                  <th>Rank</th>
                  <th>Change</th>
                  <th>CPC</th>
                  <th>Difficulty</th>
                </tr>
              </thead>
              <tbody>
                {mockKeywords.map((keyword, i) => (
                  <tr key={i}>
                    <td>{keyword.keyword}</td>
                    <td>{formatCompactNumber(keyword.volume)}</td>
                    <td className={`rank rank--${keyword.rank <= 3 ? 'high' : keyword.rank <= 7 ? 'medium' : 'low'}`}>
                      #{keyword.rank}
                    </td>
                    <td className={keyword.change >= 0 ? 'positive' : 'negative'}>
                      {keyword.change >= 0 ? '↑' : '↓'} {Math.abs(keyword.change)}
                    </td>
                    <td>${keyword.volume / 1000.toFixed(2)}</td>
                    <td>{keyword.volume > 10000 ? 'High' : 'Medium'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </TabPanel>

        <TabPanel id="backlinks" activeTab={activeTab}>
          <div className="backlinks-grid">
            {[
              { domain: 'example.com', authority: 75, links: 45, status: 'active' },
              { domain: 'marketing-blog.com', authority: 62, links: 28, status: 'active' },
              { domain: 'tech-news.io', authority: 68, links: 12, status: 'active' },
              { domain: 'industry-guide.net', authority: 45, links: 8, status: 'active' },
            ].map((backlink, i) => (
              <div key={i} className="backlink-card">
                <div className="backlink-header">
                  <span className="backlink-domain">{backlink.domain}</span>
                  <span className="backlink-authority">DA {backlink.authority}</span>
                </div>
                <span className="backlink-links">{backlink.links} links</span>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="audit" activeTab={activeTab}>
          <div className="audit-summary">
            <div className="audit-card audit--good">
              <h3>On-Page SEO</h3>
              <span className="audit-score">85%</span>
              <p>8 of 10 checks passed</p>
            </div>
            <div className="audit-card audit--warning">
              <h3>Technical SEO</h3>
              <span className="audit-score">72%</span>
              <p>6 of 10 checks passed</p>
            </div>
            <div className="audit-card audit--good">
              <h3>Content Quality</h3>
              <span className="audit-score">90%</span>
              <p>9 of 10 checks passed</p>
            </div>
            <div className="audit-card audit--warning">
              <h3>Mobile Optimization</h3>
              <span className="audit-score">78%</span>
              <p>7 of 10 checks passed</p>
            </div>
          </div>
        </TabPanel>
      </div>
    </div>
  );
};

export default SEOPage;
