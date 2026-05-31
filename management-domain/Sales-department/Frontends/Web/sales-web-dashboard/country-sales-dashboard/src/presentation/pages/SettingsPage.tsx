// SettingsPage Component
// Country-specific settings and configuration

import React, { useState } from 'react';
import { Card, Button, Tabs, StatusBadge } from '../components/common';
import { Modal } from '../components/common/Modal';
import { CURRENCY } from '@shared/constants';
import type { CountrySettings } from '@domain/types';

const mockSettings: CountrySettings = {
  country: {
    code: 'NG',
    name: 'Nigeria',
    flag: '\uD83C\uDDF3\uD83C\uDDEC',
    currency: 'NGN',
    region: 'West Africa',
    callingCode: '+234',
  },
  businessHours: {
    timezone: 'Africa/Lagos',
    workingDays: [1, 2, 3, 4, 5], // Mon-Fri
    workingHours: { start: '08:00', end: '17:00' },
  },
  quotas: {
    defaultIndividualQuota: 1000000,
    defaultTeamQuota: 5000000,
    quotaPeriod: 'monthly',
    autoAdjustQuota: false,
  },
  commission: {
    enabled: true,
    baseRate: 5,
    tierBonusEnabled: true,
    tiers: [
      { tier: 'BRONZE', rate: 3, minRevenue: 0 },
      { tier: 'SILVER', rate: 5, minRevenue: 100000 },
      { tier: 'GOLD', rate: 7, minRevenue: 500000 },
      { tier: 'PLATINUM', rate: 10, minRevenue: 1000000 },
    ],
    payoutFrequency: 'MONTHLY',
    payoutDayOfMonth: 25,
  },
  pipeline: {
    stages: ['PROSPECTING', 'QUALIFICATION', 'PROPOSAL', 'NEGOTIATION', 'CLOSING'],
    defaultStage: 'PROSPECTING',
    autoAdvanceDays: 30,
    probabilityPerStage: {
      PROSPECTING: 10,
      QUALIFICATION: 25,
      PROPOSAL: 50,
      NEGOTIATION: 75,
      CLOSING: 90,
    },
  },
  notifications: {
    email: true,
    push: true,
    sms: false,
    alerts: {
      dealStalled: true,
      dealWon: true,
      dealLost: true,
      quotaAtRisk: true,
      newLead: true,
      partnerApproval: true,
    },
  },
  integrations: [
    { type: 'email', name: 'Email Service', enabled: true, config: { provider: 'sendgrid' } },
    { type: 'sms', name: 'SMS Service', enabled: false, config: {} },
    { type: 'analytics', name: 'Analytics', enabled: true, config: { provider: 'google-analytics' } },
  ],
  updatedAt: new Date(),
  updatedBy: 'Chinedu Amadi',
};

const mockUsers = [
  { id: 'usr-001', name: 'Chinedu Amadi', email: 'chinedu.amadi@gogidix.com', role: 'COUNTRY_MANAGER', status: 'active' },
  { id: 'usr-002', name: 'Adebayo Okafor', email: 'adebayo.okafor@gogidix.com', role: 'REGIONAL_MANAGER', status: 'active' },
  { id: 'usr-003', name: 'Fatima Abdullahi', email: 'fatima.abdullahi@gogidix.com', role: 'REGIONAL_MANAGER', status: 'active' },
  { id: 'usr-004', name: 'Nkem Owusu', email: 'nkem.owusu@gogidix.com', role: 'TEAM_LEAD', status: 'active' },
  { id: 'usr-005', name: 'Emeka Nnamani', email: 'emeka.nnamani@gogidix.com', role: 'SALES_REPRESENTATIVE', status: 'active' },
];

export const SettingsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('general');
  const [settings, setSettings] = useState<CountrySettings>(mockSettings);
  const [showUserModal, setShowUserModal] = useState(false);
  const [hasChanges, setHasChanges] = useState(false);

  const handleUpdateSettings = async () => {
    // Save settings logic
    setHasChanges(false);
  };

  const handleNotificationToggle = (alertType: string) => {
    setSettings({
      ...settings,
      notifications: {
        ...settings.notifications,
        alerts: {
          ...settings.notifications.alerts,
          [alertType]: !settings.notifications.alerts[alertType as keyof typeof settings.notifications.alerts],
        },
      },
    });
    setHasChanges(true);
  };

  const tabs = [
    {
      id: 'general',
      label: 'General',
      content: (
        <div className="settings-section">
          <Card>
            <h4>Country Information</h4>
            <div className="settings-grid">
              <div className="setting-item">
                <label>Country</label>
                <input
                  type="text"
                  value={settings.country.name}
                  disabled
                  className="form-input"
                />
              </div>
              <div className="setting-item">
                <label>Currency</label>
                <select className="form-input" value={settings.country.currency}>
                  {Object.entries(CURRENCY.SYMBOLS).map(([code, symbol]) => (
                    <option key={code} value={code}>{symbol} {code}</option>
                  ))}
                </select>
              </div>
              <div className="setting-item">
                <label>Timezone</label>
                <select className="form-input" value={settings.businessHours.timezone}>
                  <option value="Africa/Lagos">Africa/Lagos (GMT+1)</option>
                  <option value="Africa/Nairobi">Africa/Nairobi (GMT+3)</option>
                  <option value="Africa/Cairo">Africa/Cairo (GMT+2)</option>
                </select>
              </div>
            </div>
          </Card>

          <Card>
            <h4>Data Aggregation</h4>
            <p className="settings-description">
              Configure how data is aggregated for reporting to the Global Dashboard.
            </p>
            <div className="settings-list">
              <div className="setting-row">
                <div className="setting-info">
                  <span className="setting-title">Auto-sync to Global</span>
                  <span className="setting-desc">Automatically sync data to global dashboard</span>
                </div>
                <label className="toggle-switch">
                  <input type="checkbox" defaultChecked />
                  <span className="toggle-slider" />
                </label>
              </div>
              <div className="setting-row">
                <div className="setting-info">
                  <span className="setting-title">Sync Frequency</span>
                  <span className="setting-desc">How often to sync data</span>
                </div>
                <select className="form-input form-input-sm">
                  <option>Real-time</option>
                  <option>Hourly</option>
                  <option selected>Daily</option>
                  <option>Weekly</option>
                </select>
              </div>
              <div className="setting-row">
                <div className="setting-info">
                  <span className="setting-title">Include Sensitive Data</span>
                  <span className="setting-desc">Include customer/partersonal identifiable information</span>
                </div>
                <label className="toggle-switch">
                  <input type="checkbox" />
                  <span className="toggle-slider" />
                </label>
              </div>
            </div>
          </Card>
        </div>
      ),
    },
    {
      id: 'quotas',
      label: 'Quotas',
      content: (
        <div className="settings-section">
          <Card>
            <h4>Default Quota Settings</h4>
            <div className="settings-grid">
              <div className="setting-item">
                <label>Default Individual Quota (Monthly)</label>
                <div className="input-with-prefix">
                  <span className="input-prefix">{CURRENCY.SYMBOLS[settings.country.currency]}</span>
                  <input
                    type="number"
                    value={settings.quotas.defaultIndividualQuota}
                    onChange={(e) => {
                      setSettings({
                        ...settings,
                        quotas: { ...settings.quotas, defaultIndividualQuota: Number(e.target.value) }
                      });
                      setHasChanges(true);
                    }}
                    className="form-input"
                  />
                </div>
              </div>
              <div className="setting-item">
                <label>Default Team Quota (Monthly)</label>
                <div className="input-with-prefix">
                  <span className="input-prefix">{CURRENCY.SYMBOLS[settings.country.currency]}</span>
                  <input
                    type="number"
                    value={settings.quotas.defaultTeamQuota}
                    onChange={(e) => {
                      setSettings({
                        ...settings,
                        quotas: { ...settings.quotas, defaultTeamQuota: Number(e.target.value) }
                      });
                      setHasChanges(true);
                    }}
                    className="form-input"
                  />
                </div>
              </div>
              <div className="setting-item">
                <label>Quota Period</label>
                <select
                  value={settings.quotas.quotaPeriod}
                  onChange={(e) => {
                    setSettings({
                      ...settings,
                      quotas: { ...settings.quotas, quotaPeriod: e.target.value as 'monthly' | 'quarterly' }
                    });
                    setHasChanges(true);
                  }}
                  className="form-input"
                >
                  <option value="monthly">Monthly</option>
                  <option value="quarterly">Quarterly</option>
                </select>
              </div>
            </div>

            <div className="setting-row">
              <div className="setting-info">
                <span className="setting-title">Auto-adjust Quotas</span>
                <span className="setting-desc">Automatically adjust quotas based on historical performance</span>
              </div>
              <label className="toggle-switch">
                <input
                  type="checkbox"
                  checked={settings.quotas.autoAdjustQuota}
                  onChange={(e) => {
                    setSettings({
                      ...settings,
                      quotas: { ...settings.quotas, autoAdjustQuota: e.target.checked }
                    });
                    setHasChanges(true);
                  }}
                />
                <span className="toggle-slider" />
              </label>
            </div>
          </Card>
        </div>
      ),
    },
    {
      id: 'commissions',
      label: 'Commissions',
      content: (
        <div className="settings-section">
          <Card>
            <h4>Commission Settings</h4>

            <div className="setting-row">
              <div className="setting-info">
                <span className="setting-title">Enable Commissions</span>
                <span className="setting-desc">Enable commission calculations for sales</span>
              </div>
              <label className="toggle-switch">
                <input
                  type="checkbox"
                  checked={settings.commission.enabled}
                  onChange={(e) => {
                    setSettings({
                      ...settings,
                      commission: { ...settings.commission, enabled: e.target.checked }
                    });
                    setHasChanges(true);
                  }}
                />
                <span className="toggle-slider" />
              </label>
            </div>

            <div className="settings-grid">
              <div className="setting-item">
                <label>Base Rate (%)</label>
                <input
                  type="number"
                  value={settings.commission.baseRate}
                  onChange={(e) => {
                    setSettings({
                      ...settings,
                      commission: { ...settings.commission, baseRate: Number(e.target.value) }
                    });
                    setHasChanges(true);
                  }}
                  className="form-input"
                  min="0"
                  max="20"
                  step="0.5"
                />
              </div>
              <div className="setting-item">
                <label>Payout Frequency</label>
                <select
                  value={settings.commission.payoutFrequency}
                  onChange={(e) => {
                    setSettings({
                      ...settings,
                      commission: { ...settings.commission, payoutFrequency: e.target.value as 'MONTHLY' | 'QUARTERLY' }
                    });
                    setHasChanges(true);
                  }}
                  className="form-input"
                >
                  <option value="MONTHLY">Monthly</option>
                  <option value="QUARTERLY">Quarterly</option>
                </select>
              </div>
            </div>

            <h5>Commission Tiers</h5>
            <div className="tiers-list">
              {settings.commission.tiers.map((tier, index) => (
                <div key={tier.tier} className="tier-item">
                  <span className="tier-name">{tier.tier}</span>
                  <div className="tier-inputs">
                    <input
                      type="number"
                      value={tier.rate}
                      onChange={(e) => {
                        const newTiers = [...settings.commission.tiers];
                        newTiers[index].rate = Number(e.target.value);
                        setSettings({
                          ...settings,
                          commission: { ...settings.commission, tiers: newTiers }
                        });
                        setHasChanges(true);
                      }}
                      className="form-input form-input-sm"
                      min="0"
                      max="20"
                    />
                    <span>%</span>
                    <span>above</span>
                    <div className="input-with-prefix input-with-prefix-sm">
                      <span className="input-prefix">{CURRENCY.SYMBOLS[settings.country.currency]}</span>
                      <input
                        type="number"
                        value={tier.minRevenue}
                        onChange={(e) => {
                          const newTiers = [...settings.commission.tiers];
                          newTiers[index].minRevenue = Number(e.target.value);
                          setSettings({
                            ...settings,
                            commission: { ...settings.commission, tiers: newTiers }
                          });
                          setHasChanges(true);
                        }}
                        className="form-input form-input-sm"
                      />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>
      ),
    },
    {
      id: 'notifications',
      label: 'Notifications',
      content: (
        <div className="settings-section">
          <Card>
            <h4>Notification Preferences</h4>

            <div className="settings-list">
              <div className="setting-row">
                <div className="setting-info">
                  <span className="setting-title">Email Notifications</span>
                  <span className="setting-desc">Receive notifications via email</span>
                </div>
                <label className="toggle-switch">
                  <input
                    type="checkbox"
                    checked={settings.notifications.email}
                    onChange={(e) => {
                      setSettings({
                        ...settings,
                        notifications: { ...settings.notifications, email: e.target.checked }
                      });
                      setHasChanges(true);
                    }}
                  />
                  <span className="toggle-slider" />
                </label>
              </div>

              <div className="setting-row">
                <div className="setting-info">
                  <span className="setting-title">Push Notifications</span>
                  <span className="setting-desc">Receive push notifications in browser</span>
                </div>
                <label className="toggle-switch">
                  <input
                    type="checkbox"
                    checked={settings.notifications.push}
                    onChange={(e) => {
                      setSettings({
                        ...settings,
                        notifications: { ...settings.notifications, push: e.target.checked }
                      });
                      setHasChanges(true);
                    }}
                  />
                  <span className="toggle-slider" />
                </label>
              </div>

              <div className="setting-row">
                <div className="setting-info">
                  <span className="setting-title">SMS Notifications</span>
                  <span className="setting-desc">Receive notifications via SMS</span>
                </div>
                <label className="toggle-switch">
                  <input
                    type="checkbox"
                    checked={settings.notifications.sms}
                    onChange={(e) => {
                      setSettings({
                        ...settings,
                        notifications: { ...settings.notifications, sms: e.target.checked }
                      });
                      setHasChanges(true);
                    }}
                  />
                  <span className="toggle-slider" />
                </label>
              </div>
            </div>

            <h5>Alert Types</h5>
            <div className="settings-list">
              {Object.entries(settings.notifications.alerts).map(([key, value]) => (
                <div key={key} className="setting-row">
                  <div className="setting-info">
                    <span className="setting-title">{key.replace(/([A-Z])/g, ' $1').trim()}</span>
                    <span className="setting-desc">Get notified for {key.toLowerCase().replace(/_/g, ' ')} events</span>
                  </div>
                  <label className="toggle-switch">
                    <input
                      type="checkbox"
                      checked={value}
                      onChange={() => handleNotificationToggle(key)}
                    />
                    <span className="toggle-slider" />
                  </label>
                </div>
              ))}
            </div>
          </Card>
        </div>
      ),
    },
    {
      id: 'users',
      label: 'Users',
      content: (
        <div className="settings-section">
          <Card>
            <div className="card-header-with-action">
              <h4>Country Users</h4>
              <Button variant="primary" onClick={() => setShowUserModal(true)}>
                Add User
              </Button>
            </div>

            <table className="data-table">
              <thead>
                <tr>
                  <th>User</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {mockUsers.map(user => (
                  <tr key={user.id}>
                    <td>
                      <div className="user-cell">
                        <div className="user-avatar">
                          {user.name.split(' ').map(n => n[0]).join('')}
                        </div>
                        <span>{user.name}</span>
                      </div>
                    </td>
                    <td>{user.email}</td>
                    <td>
                      <span className="role-badge">{user.role.replace(/_/g, ' ')}</span>
                    </td>
                    <td>
                      <StatusBadge status={user.status} size="sm" />
                    </td>
                    <td>
                      <button className="btn-link">Edit</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </Card>
        </div>
      ),
    },
  ];

  return (
    <div className="page settings-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Settings</h1>
          <p className="page-subtitle">Configure country dashboard settings</p>
        </div>
        <div className="page-actions">
          {hasChanges && (
            <>
              <Button variant="secondary" onClick={() => {
                setSettings(mockSettings);
                setHasChanges(false);
              }}>
                Discard
              </Button>
              <Button variant="primary" onClick={handleUpdateSettings}>
                Save Changes
              </Button>
            </>
          )}
        </div>
      </div>

      {/* Tabs */}
      <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} />

      {/* Add User Modal */}
      <Modal isOpen={showUserModal} onClose={() => setShowUserModal(false)} title="Add New User" size="md">
        <form className="user-form">
          <div className="form-group">
            <label>First Name</label>
            <input type="text" className="form-input" placeholder="Enter first name" />
          </div>
          <div className="form-group">
            <label>Last Name</label>
            <input type="text" className="form-input" placeholder="Enter last name" />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input type="email" className="form-input" placeholder="user@example.com" />
          </div>
          <div className="form-group">
            <label>Role</label>
            <select className="form-input">
              <option value="COUNTRY_MANAGER">Country Manager</option>
              <option value="REGIONAL_MANAGER">Regional Manager</option>
              <option value="TEAM_LEAD">Team Lead</option>
              <option value="SALES_REPRESENTATIVE">Sales Representative</option>
              <option value="SALES_ANALYST">Sales Analyst</option>
              <option value="PARTNER_COORDINATOR">Partner Coordinator</option>
            </select>
          </div>
          <div className="form-actions">
            <Button variant="secondary" onClick={() => setShowUserModal(false)}>
              Cancel
            </Button>
            <Button variant="primary">Add User</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default SettingsPage;
