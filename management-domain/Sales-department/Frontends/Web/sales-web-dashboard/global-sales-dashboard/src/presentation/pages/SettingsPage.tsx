// Settings Page
// Global dashboard settings and configuration

import { useState } from 'react';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { useAuthStore, useUIStore } from '@infrastructure';
import './SettingsPage.css';

export function SettingsPage() {
  const { user, logout } = useAuthStore();
  const { theme, setTheme, sidebarOpen, toggleSidebar } = useUIStore();

  const [activeTab, setActiveTab] = useState('profile');
  const [profileForm, setProfileForm] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
  });

  const [notifications, setNotifications] = useState({
    emailAlerts: true,
    pushNotifications: false,
    weeklyDigest: true,
    majorDeals: true,
    forecastUpdates: false,
  });

  const [displaySettings, setDisplaySettings] = useState({
    theme: theme,
    compactMode: false,
    showAnimations: true,
    defaultPeriod: 'month',
  });

  const handleProfileSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle profile update
    console.log('Profile updated:', profileForm);
  };

  const handleLogout = async () => {
    await logout();
    window.location.href = '/login';
  };

  return (
    <div className="settings-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Settings</h1>
          <p className="page-subtitle">Manage your account and dashboard preferences</p>
        </div>
      </div>

      <div className="settings-layout">
        {/* Sidebar Navigation */}
        <aside className="settings-nav">
          <nav>
            <button
              className={`nav-item ${activeTab === 'profile' ? 'active' : ''}`}
              onClick={() => setActiveTab('profile')}
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
              Profile
            </button>
            <button
              className={`nav-item ${activeTab === 'security' ? 'active' : ''}`}
              onClick={() => setActiveTab('security')}
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                <path d="M7 11V7a5 5 0 0 1 10 0v4" />
              </svg>
              Security
            </button>
            <button
              className={`nav-item ${activeTab === 'notifications' ? 'active' : ''}`}
              onClick={() => setActiveTab('notifications')}
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
                <path d="M13.73 21a2 2 0 0 1-3.46 0" />
              </svg>
              Notifications
            </button>
            <button
              className={`nav-item ${activeTab === 'display' ? 'active' : ''}`}
              onClick={() => setActiveTab('display')}
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <rect x="2" y="3" width="20" height="14" rx="2" ry="2" />
                <line x1="8" y1="21" x2="16" y2="21" />
                <line x1="12" y1="17" x2="12" y2="21" />
              </svg>
              Display
            </button>
            <button
              className={`nav-item ${activeTab === 'data' ? 'active' : ''}`}
              onClick={() => setActiveTab('data')}
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <ellipse cx="12" cy="5" rx="9" ry="3" />
                <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3" />
                <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5" />
              </svg>
              Data & Privacy
            </button>
            <button
              className={`nav-item ${activeTab === 'integrations' ? 'active' : ''}`}
              onClick={() => setActiveTab('integrations')}
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M16 16v1a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2h2m5.66 0H14a2 2 0 0 1 2 2v3.34l1 1L23 7v10" />
                <line x1="1" y1="1" x2="23" y2="23" />
              </svg>
              Integrations
            </button>
          </nav>
        </aside>

        {/* Settings Content */}
        <main className="settings-content">
          {/* Profile Tab */}
          {activeTab === 'profile' && (
            <div className="tab-content">
              <Card>
                <CardHeader>
                  <h3>Profile Information</h3>
                  <p className="tab-description">Update your personal information</p>
                </CardHeader>
                <CardBody>
                  <form onSubmit={handleProfileSubmit} className="profile-form">
                    <div className="form-avatar">
                      <div className="avatar-preview">
                        {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                      </div>
                      <div className="avatar-actions">
                        <Button variant="secondary" size="sm">Change Photo</Button>
                        <Button variant="ghost" size="sm">Remove</Button>
                      </div>
                    </div>

                    <div className="form-row">
                      <div className="form-group">
                        <label>First Name</label>
                        <input
                          type="text"
                          value={profileForm.firstName}
                          onChange={(e) => setProfileForm({ ...profileForm, firstName: e.target.value })}
                        />
                      </div>
                      <div className="form-group">
                        <label>Last Name</label>
                        <input
                          type="text"
                          value={profileForm.lastName}
                          onChange={(e) => setProfileForm({ ...profileForm, lastName: e.target.value })}
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <label>Email Address</label>
                      <input
                        type="email"
                        value={profileForm.email}
                        onChange={(e) => setProfileForm({ ...profileForm, email: e.target.value })}
                      />
                    </div>

                    <div className="form-group">
                      <label>Role</label>
                      <input type="text" value={user?.role || ''} disabled />
                    </div>

                    <div className="form-actions">
                      <Button variant="primary">Save Changes</Button>
                      <Button variant="ghost" type="button">Cancel</Button>
                    </div>
                  </form>
                </CardBody>
              </Card>

              <Card>
                <CardHeader>
                  <h3>Danger Zone</h3>
                </CardHeader>
                <CardBody>
                  <div className="danger-zone">
                    <div className="danger-info">
                      <h4>Sign Out</h4>
                      <p>Sign out from your account on this device</p>
                    </div>
                    <Button variant="secondary" onClick={handleLogout}>Sign Out</Button>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}

          {/* Security Tab */}
          {activeTab === 'security' && (
            <div className="tab-content">
              <Card>
                <CardHeader>
                  <h3>Change Password</h3>
                  <p className="tab-description">Update your password to keep your account secure</p>
                </CardHeader>
                <CardBody>
                  <form className="security-form">
                    <div className="form-group">
                      <label>Current Password</label>
                      <input type="password" placeholder="Enter current password" />
                    </div>
                    <div className="form-group">
                      <label>New Password</label>
                      <input type="password" placeholder="Enter new password" />
                    </div>
                    <div className="form-group">
                      <label>Confirm New Password</label>
                      <input type="password" placeholder="Confirm new password" />
                    </div>
                    <div className="form-actions">
                      <Button variant="primary">Update Password</Button>
                    </div>
                  </form>
                </CardBody>
              </Card>

              <Card>
                <CardHeader>
                  <h3>Two-Factor Authentication</h3>
                  <p className="tab-description">Add an extra layer of security to your account</p>
                </CardHeader>
                <CardBody>
                  <div className="two-factor-status">
                    <div className="status-info">
                      <span className="status-icon disabled">\uD83D\uDD12</span>
                      <div>
                        <h4>2FA is not enabled</h4>
                        <p>Secure your account by enabling two-factor authentication</p>
                      </div>
                    </div>
                    <Button variant="primary">Enable 2FA</Button>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}

          {/* Notifications Tab */}
          {activeTab === 'notifications' && (
            <div className="tab-content">
              <Card>
                <CardHeader>
                  <h3>Email Notifications</h3>
                  <p className="tab-description">Choose which email notifications you receive</p>
                </CardHeader>
                <CardBody>
                  <div className="notification-settings">
                    <div className="notification-item">
                      <div className="notification-info">
                        <h4>Email Alerts</h4>
                        <p>Receive important alerts via email</p>
                      </div>
                      <label className="toggle">
                        <input
                          type="checkbox"
                          checked={notifications.emailAlerts}
                          onChange={(e) => setNotifications({ ...notifications, emailAlerts: e.target.checked })}
                        />
                        <span className="slider"></span>
                      </label>
                    </div>

                    <div className="notification-item">
                      <div className="notification-info">
                        <h4>Weekly Digest</h4>
                        <p>Get a weekly summary of your sales performance</p>
                      </div>
                      <label className="toggle">
                        <input
                          type="checkbox"
                          checked={notifications.weeklyDigest}
                          onChange={(e) => setNotifications({ ...notifications, weeklyDigest: e.target.checked })}
                        />
                        <span className="slider"></span>
                      </label>
                    </div>

                    <div className="notification-item">
                      <div className="notification-info">
                        <h4>Major Deal Alerts</h4>
                        <p>Get notified when major deals change stage</p>
                      </div>
                      <label className="toggle">
                        <input
                          type="checkbox"
                          checked={notifications.majorDeals}
                          onChange={(e) => setNotifications({ ...notifications, majorDeals: e.target.checked })}
                        />
                        <span className="slider"></span>
                      </label>
                    </div>

                    <div className="notification-item">
                      <div className="notification-info">
                        <h4>Forecast Updates</h4>
                        <p>Receive updates when forecasts are updated</p>
                      </div>
                      <label className="toggle">
                        <input
                          type="checkbox"
                          checked={notifications.forecastUpdates}
                          onChange={(e) => setNotifications({ ...notifications, forecastUpdates: e.target.checked })}
                        />
                        <span className="slider"></span>
                      </label>
                    </div>
                  </div>
                </CardBody>
              </Card>

              <Card>
                <CardHeader>
                  <h3>Push Notifications</h3>
                  <p className="tab-description">Manage browser push notifications</p>
                </CardHeader>
                <CardBody>
                  <div className="notification-item">
                    <div className="notification-info">
                      <h4>Enable Push Notifications</h4>
                      <p>Receive real-time notifications in your browser</p>
                    </div>
                    <label className="toggle">
                      <input
                        type="checkbox"
                        checked={notifications.pushNotifications}
                        onChange={(e) => setNotifications({ ...notifications, pushNotifications: e.target.checked })}
                      />
                      <span className="slider"></span>
                    </label>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}

          {/* Display Tab */}
          {activeTab === 'display' && (
            <div className="tab-content">
              <Card>
                <CardHeader>
                  <h3>Appearance</h3>
                  <p className="tab-description">Customize how the dashboard looks</p>
                </CardHeader>
                <CardBody>
                  <div className="display-settings">
                    <div className="setting-item">
                      <div className="setting-info">
                        <h4>Theme</h4>
                        <p>Choose your preferred color scheme</p>
                      </div>
                      <div className="theme-selector">
                        <button
                          className={`theme-btn ${theme === 'light' ? 'active' : ''}`}
                          onClick={() => { setTheme('light'); setDisplaySettings({ ...displaySettings, theme: 'light' }); }}
                        >
                          <span className="theme-icon">{'\u{1F324}'}</span>
                          <span>Light</span>
                        </button>
                        <button
                          className={`theme-btn ${theme === 'dark' ? 'active' : ''}`}
                          onClick={() => { setTheme('dark'); setDisplaySettings({ ...displaySettings, theme: 'dark' }); }}
                        >
                          <span className="theme-icon">{'\u{1F311}'}</span>
                          <span>Dark</span>
                        </button>
                      </div>
                    </div>

                    <div className="setting-item">
                      <div className="setting-info">
                        <h4>Compact Mode</h4>
                        <p>Use smaller spacing for more content</p>
                      </div>
                      <label className="toggle">
                        <input
                          type="checkbox"
                          checked={displaySettings.compactMode}
                          onChange={(e) => setDisplaySettings({ ...displaySettings, compactMode: e.target.checked })}
                        />
                        <span className="slider"></span>
                      </label>
                    </div>

                    <div className="setting-item">
                      <div className="setting-info">
                        <h4>Animations</h4>
                        <p>Enable smooth transitions and animations</p>
                      </div>
                      <label className="toggle">
                        <input
                          type="checkbox"
                          checked={displaySettings.showAnimations}
                          onChange={(e) => setDisplaySettings({ ...displaySettings, showAnimations: e.target.checked })}
                        />
                        <span className="slider"></span>
                      </label>
                    </div>

                    <div className="setting-item">
                      <div className="setting-info">
                        <h4>Default Period</h4>
                        <p>Default time period for dashboard data</p>
                      </div>
                      <select
                        value={displaySettings.defaultPeriod}
                        onChange={(e) => setDisplaySettings({ ...displaySettings, defaultPeriod: e.target.value })}
                        className="period-select"
                      >
                        <option value="today">Today</option>
                        <option value="week">This Week</option>
                        <option value="month">This Month</option>
                        <option value="quarter">This Quarter</option>
                        <option value="year">This Year</option>
                      </select>
                    </div>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}

          {/* Data Tab */}
          {activeTab === 'data' && (
            <div className="tab-content">
              <Card>
                <CardHeader>
                  <h3>Data Export</h3>
                  <p className="tab-description">Export your data from the dashboard</p>
                </CardHeader>
                <CardBody>
                  <div className="data-actions">
                    <div className="data-action-item">
                      <div className="action-icon">\uD83D\uDCC4</div>
                      <div className="action-info">
                        <h4>Export All Data</h4>
                        <p>Download all your dashboard data</p>
                      </div>
                      <Button variant="secondary">Export</Button>
                    </div>
                  </div>
                </CardBody>
              </Card>

              <Card>
                <CardHeader>
                  <h3>Privacy</h3>
                  <p className="tab-description">Manage your privacy settings</p>
                </CardHeader>
                <CardBody>
                  <div className="privacy-settings">
                    <div className="privacy-item">
                      <div className="privacy-info">
                        <h4>Analytics</h4>
                        <p>Help us improve by sharing anonymous usage data</p>
                      </div>
                      <label className="toggle">
                        <input type="checkbox" defaultChecked />
                        <span className="slider"></span>
                      </label>
                    </div>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}

          {/* Integrations Tab */}
          {activeTab === 'integrations' && (
            <div className="tab-content">
              <Card>
                <CardHeader>
                  <h3>Connected Services</h3>
                  <p className="tab-description">Manage third-party integrations</p>
                </CardHeader>
                <CardBody>
                  <div className="integrations-list">
                    <div className="integration-item">
                      <div className="integration-info">
                        <div className="integration-icon">\uD83D\uDECE</div>
                        <div>
                          <h4>Slack</h4>
                          <p className="integration-status">Not connected</p>
                        </div>
                      </div>
                      <Button variant="secondary">Connect</Button>
                    </div>

                    <div className="integration-item">
                      <div className="integration-info">
                        <div className="integration-icon">\uD83D\uDCE8</div>
                        <div>
                          <h4>Microsoft Teams</h4>
                          <p className="integration-status">Not connected</p>
                        </div>
                      </div>
                      <Button variant="secondary">Connect</Button>
                    </div>

                    <div className="integration-item">
                      <div className="integration-info">
                        <div className="integration-icon">\uD83D\uDCCA</div>
                        <div>
                          <h4>Salesforce</h4>
                          <p className="integration-status connected">Connected</p>
                        </div>
                      </div>
                      <Button variant="ghost">Disconnect</Button>
                    </div>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}
        </main>
      </div>
    </div>
  );
}
