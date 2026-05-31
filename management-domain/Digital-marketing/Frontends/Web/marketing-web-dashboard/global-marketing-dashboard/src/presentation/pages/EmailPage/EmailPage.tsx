// EmailPage - Global Marketing Dashboard
// Email campaign performance and management page

import React, { useState } from 'react';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { BarChart, DataPoint } from '../../../shared/components/charts/BarChart';
import { PieChart } from '../../../shared/components/charts/PieChart';
import { formatCompactNumber, formatPercentage } from '../../../shared/utils/formatters';
import './EmailPage.css';

const EmailPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('campaigns');

  const mockEmailCampaigns = [
    { name: 'Q1 Newsletter', sent: 125000, delivered: 118750, opened: 35625, clicked: 7125, openRate: 30, clickRate: 20 },
    { name: 'Product Launch', sent: 85000, delivered: 80750, opened: 28262, clicked: 8478, openRate: 35, clickRate: 30 },
    { name: 'Webinar Invite', sent: 45000, delivered: 42750, opened: 17100, clicked: 5130, openRate: 40, clickRate: 30 },
    { name: 'Drip Campaign #1', sent: 250000, delivered: 237500, opened: 59375, clicked: 11875, openRate: 25, clickRate: 20 },
  ];

  const performanceData: DataPoint[] = mockEmailCampaigns.map(c => ({
    label: c.name,
    value: c.openRate,
  }));

  const deviceData = [
    { label: 'Desktop', value: 65, color: '#3B82F6' },
    { label: 'Mobile', value: 30, color: '#10B981' },
    { label: 'Tablet', value: 5, color: '#F59E0B' },
  ];

  const tabs = [
    { id: 'campaigns', label: 'Campaigns' },
    { id: 'automation', label: 'Automation' },
    { id: 'lists', label: 'Lists' },
    { id: 'templates', label: 'Templates' },
  ];

  return (
    <div className="email-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Email Marketing</h1>
              <p className="page__subtitle">Email campaign performance, list management, and automation</p>
            </div>
            <div className="page__actions">
              <Button variant="secondary">Create Automation</Button>
              <Button variant="primary">+ Create Campaign</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard label="Emails Sent" value={formatCompactNumber(505000)} icon="📧" />
          <MetricCard label="Delivered" value={formatCompactNumber(479750)} icon="✅" />
          <MetricCard label="Open Rate" value="30%" change={5} changeType="positive" icon="👁️" />
          <MetricCard label="Click Rate" value="6%" change={3} changeType="positive" icon="🖱️" />
          <MetricCard label="Unsubscribed" value="1.2%" change={-0.3} changeType="positive" icon="📉" />
          <MetricCard label="Bounce Rate" value="2.1%" change={-0.5} changeType="positive" icon="↩️" />
        </div>

        {/* Tabs */}
        <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} variant="underlined" />

        <TabPanel id="campaigns" activeTab={activeTab}>
          <div className="grid grid--2">
            <div className="chart-card">
              <h3>Campaign Performance</h3>
              <BarChart data={performanceData} height={200} showGrid showValues />
            </div>
            <div className="chart-card">
              <h3>Device Breakdown</h3>
              <PieChart data={deviceData} size="small" />
            </div>
          </div>

          <div className="email-campaigns-table">
            <table>
              <thead>
                <tr>
                  <th>Campaign</th>
                  <th>Sent</th>
                  <th>Delivered</th>
                  <th>Opened</th>
                  <th>Clicked</th>
                  <th>Open Rate</th>
                  <th>Click Rate</th>
                </tr>
              </thead>
              <tbody>
                {mockEmailCampaigns.map((campaign, i) => (
                  <tr key={i}>
                    <td>{campaign.name}</td>
                    <td>{formatCompactNumber(campaign.sent)}</td>
                    <td>{formatCompactNumber(campaign.delivered)}</td>
                    <td>{formatCompactNumber(campaign.opened)}</td>
                    <td>{formatCompactNumber(campaign.clicked)}</td>
                    <td>{formatPercentage(campaign.openRate)}</td>
                    <td>{formatPercentage(campaign.clickRate)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </TabPanel>

        <TabPanel id="automation" activeTab={activeTab}>
          <div className="automation-list">
            {[
              { name: 'Welcome Series', status: 'active', enrolled: 12500, completed: 8900, conversion: 15 },
              { name: 'Abandoned Cart', status: 'active', enrolled: 3200, completed: 1800, conversion: 8 },
              { name: 'Re-engagement', status: 'paused', enrolled: 8500, completed: 4200, conversion: 12 },
              { name: 'Post-Purchase', status: 'active', enrolled: 18500, completed: 15200, conversion: 22 },
            ].map((automation, i) => (
              <div key={i} className="automation-card">
                <div className="automation-header">
                  <h3>{automation.name}</h3>
                  <span className={`status-badge status--${automation.status}`}>{automation.status}</span>
                </div>
                <div className="automation-metrics">
                  <span>{formatCompactNumber(automation.enrolled)} Enrolled</span>
                  <span>{formatCompactNumber(automation.completed)} Completed</span>
                  <span>{automation.conversion}% Conversion</span>
                </div>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="lists" activeTab={activeTab}>
          <div className="email-lists">
            {[
              { name: 'Master List', subscribers: 150000, growth: 5.2 },
              { name: 'Customers', subscribers: 75000, growth: 3.1 },
              { name: 'Leads - Hot', subscribers: 12500, growth: 8.4 },
              { name: 'Newsletter Subscribers', subscribers: 45000, growth: 6.7 },
            ].map((list, i) => (
              <div key={i} className="list-card">
                <h4>{list.name}</h4>
                <span>{formatCompactNumber(list.subscribers)} subscribers</span>
                <span className="growth-positive">+{list.growth}% this month</span>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="templates" activeTab={activeTab}>
          <div className="templates-grid">
            {['Newsletter', 'Promotional', 'Welcome', 'Abandoned Cart', 'Product Launch', 'Event Invitation'].map((template) => (
              <div key={template} className="template-card-mini">
                <div className="template-preview">✉️</div>
                <span>{template}</span>
              </div>
            ))}
          </div>
        </TabPanel>
      </div>
    </div>
  );
};

export default EmailPage;
