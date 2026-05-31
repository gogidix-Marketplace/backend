// SettingsPage - Global Marketing Dashboard
// Global settings, user management, and integrations page

import React, { useState } from 'react';
import { Button } from '../../../shared/components/common/Button';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { Toggle } from '../../../shared/components/common/Toggle';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import './SettingsPage.css';

const SettingsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('general');
  const [notifications, setNotifications] = useState({
    email: true,
    push: true,
    campaignAlerts: true,
    budgetAlerts: true,
    leadAlerts: false,
    weeklyDigest: true,
  });

  const tabs = [
    { id: 'general', label: 'General' },
    { id: 'users', label: 'Users' },
    { id: 'integrations', label: 'Integrations' },
    { id: 'billing', label: 'Billing' },
  ];

  const integrations = [
    { name: 'Google Analytics', icon: '📊', status: 'connected', lastSync: '2 hours ago' },
    { name: 'Facebook Ads', icon: '📘', status: 'connected', lastSync: '1 day ago' },
    { name: 'Google Ads', icon: '🔍', status: 'connected', lastSync: '3 hours ago' },
    { name: 'LinkedIn', icon: '💼', status: 'connected', lastSync: '5 hours ago' },
    { name: 'Mailchimp', icon: '✉️', status: 'disconnected', lastSync: null },
    { name: 'HubSpot', icon: '🔶', status: 'disconnected', lastSync: null },
  ];

  const users = [
    { id: 'user-001', name: 'Sarah Mitchell', email: 'sarah@gogidix.com', role: 'VP Marketing', status: 'active' },
    { id: 'user-002', name: 'James Wilson', email: 'james@gogidix.com', role: 'Regional Director EMEA', status: 'active' },
    { id: 'user-003', name: 'Yuki Tanaka', email: 'yuki@gogidix.com', role: 'Regional Director APAC', status: 'active' },
    { id: 'user-004', name: 'Carlos Silva', email: 'carlos@gogidix.com', role: 'Regional Director LATAM', status: 'active' },
    { id: 'user-005', name: 'Emma Rodriguez', email: 'emma@gogidix.com', role: 'Campaign Manager', status: 'active' },
  ];

  return (
    <div className="settings-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Settings</h1>
              <p className="page__subtitle">Global settings, user management, and integrations</p>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} variant="underlined" />

        <TabPanel id="general" activeTab={activeTab}>
          <div className="settings-section">
            <h2 className="settings-section-title">Display Settings</h2>
            <div className="settings-grid">
              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Theme</span>
                  <span className="setting-description">Choose your preferred theme</span>
                </div>
                <div className="theme-options">
                  <button className="theme-option theme-option--light">Light</button>
                  <button className="theme-option theme-option--dark">Dark</button>
                  <button className="theme-option theme-option--system">System</button>
                </div>
              </div>

              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Timezone</span>
                  <span className="setting-description">Set your timezone</span>
                </div>
                <select className="settings-select">
                  <option>UTC</option>
                  <option>America/New_York</option>
                  <option>America/Los_Angeles</option>
                  <option>Europe/London</option>
                  <option>Asia/Tokyo</option>
                </select>
              </div>

              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Date Format</span>
                  <span className="setting-description">Choose your date format</span>
                </div>
                <select className="settings-select">
                  <option>MMM d, yyyy</option>
                  <option>dd/MM/yyyy</option>
                  <option>MM/dd/yyyy</option>
                  <option>yyyy-MM-dd</option>
                </select>
              </div>
            </div>
          </div>

          <div className="settings-section">
            <h2 className="settings-section-title">Notification Preferences</h2>
            <div className="settings-grid settings-grid--full">
              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Email Notifications</span>
                  <span className="setting-description">Receive notifications via email</span>
                </div>
                <Toggle enabled={notifications.email} onChange={() => setNotifications({ ...notifications, email: !notifications.email })} />
              </div>

              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Push Notifications</span>
                  <span className="setting-description">Receive push notifications</span>
                </div>
                <Toggle enabled={notifications.push} onChange={() => setNotifications({ ...notifications, push: !notifications.push })} />
              </div>

              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Campaign Alerts</span>
                  <span className="setting-description">Get notified about campaign milestones</span>
                </div>
                <Toggle enabled={notifications.campaignAlerts} onChange={() => setNotifications({ ...notifications, campaignAlerts: !notifications.campaignAlerts })} />
              </div>

              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Budget Alerts</span>
                  <span className="setting-description">Get notified when budgets are at risk</span>
                </div>
                <Toggle enabled={notifications.budgetAlerts} onChange={() => setNotifications({ ...notifications, budgetAlerts: !notifications.budgetAlerts })} />
              </div>

              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Lead Alerts</span>
                  <span className="setting-description">Get notified about important lead updates</span>
                </div>
                <Toggle enabled={notifications.leadAlerts} onChange={() => setNotifications({ ...notifications, leadAlerts: !notifications.leadAlerts })} />
              </div>

              <div className="setting-item">
                <div className="setting-info">
                  <span className="setting-label">Weekly Digest</span>
                  <span className="setting-description">Receive weekly summary emails</span>
                </div>
                <Toggle enabled={notifications.weeklyDigest} onChange={() => setNotifications({ ...notifications, weeklyDigest: !notifications.weeklyDigest })} />
              </div>
            </div>
          </div>
        </TabPanel>

        <TabPanel id="users" activeTab={activeTab}>
          <div className="users-header">
            <SearchBar placeholder="Search users..." />
            <Button variant="primary">+ Invite User</Button>
          </div>

          <div className="users-table">
            <table>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id}>
                    <td>{user.name}</td>
                    <td>{user.email}</td>
                    <td>{user.role}</td>
                    <td><span className="status-badge status--active">{user.status}</span></td>
                    <td>
                      <Button variant="secondary" size="sm">Edit</Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </TabPanel>

        <TabPanel id="integrations" activeTab={activeTab}>
          <div className="integrations-grid">
            {integrations.map((integration) => (
              <div key={integration.name} className="integration-card">
                <div className="integration-header">
                  <span className="integration-icon">{integration.icon}</span>
                  <div className="integration-info">
                    <h3 className="integration-name">{integration.name}</h3>
                    <span className={`integration-status integration-status--${integration.status}`}>
                      {integration.status}
                    </span>
                  </div>
                </div>
                {integration.status === 'connected' && (
                  <p className="integration-sync">Last sync: {integration.lastSync}</p>
                )}
                <div className="integration-actions">
                  {integration.status === 'connected' ? (
                    <>
                      <Button variant="secondary" size="sm">Configure</Button>
                      <Button variant="secondary" size="sm">Disconnect</Button>
                    </>
                  ) : (
                    <Button variant="primary" size="sm">Connect</Button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="billing" activeTab={activeTab}>
          <div className="billing-overview">
            <MetricCard label="Current Plan" value="Enterprise" />
            <MetricCard label="Monthly Spend" value="$12,450" />
            <MetricCard label="Remaining Credits" value="45,230" />
          </div>

          <div className="billing-section">
            <h2>Payment Methods</h2>
            <div className="payment-methods">
              <div className="payment-method">
                <span className="payment-icon">💳</span>
                <div className="payment-info">
                  <span>•••• •••• •••• 4242</span>
                  <span>Expires 12/2026</span>
                </div>
                <span className="payment-default">Default</span>
              </div>
            </div>
            <Button variant="secondary">+ Add Payment Method</Button>
          </div>

          <div className="billing-section">
            <h2>Billing History</h2>
            <div className="billing-history">
              {Array.from({ length: 6 }, (_, i) => (
                <div key={i} className="billing-item">
                  <span>Invoice #{2024006 - i}</span>
                  <span>{new Date(2026, i, 1).toLocaleDateString('MMMM yyyy')}</span>
                  <span>${Math.floor(Math.random() * 5000) + 1000}</span>
                  <span className="billing-status billing-status--paid">Paid</span>
                  <Button variant="secondary" size="sm">Download</Button>
                </div>
              ))}
            </div>
          </div>
        </TabPanel>
      </div>
    </div>
  );
};

// Simple Toggle Component
interface ToggleProps {
  enabled: boolean;
  onChange: () => void;
}

const Toggle: React.FC<ToggleProps> = ({ enabled, onChange }) => (
  <button
    type="button"
    className={`toggle ${enabled ? 'toggle--on' : ''}`}
    onClick={onChange}
    aria-pressed={enabled}
  >
    <span className="toggle-slider" />
  </button>
);

export default SettingsPage;
