// Reports Page
// Global report generation and management

import { useState, useMemo } from 'react';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Table } from '../components/tables/Table';
import { REPORT_TYPES, EXPORT_FORMATS } from '@shared';
import { mockCountries, mockReports } from '@shared/mock-data';
import './ReportsPage.css';

interface ReportSchedule {
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly';
  recipients: string[];
  active: boolean;
}

export function ReportsPage() {
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [showNewReport, setShowNewReport] = useState(false);

  const reportStats = useMemo(() => {
    return {
      totalReports: mockReports.length,
      activeReports: mockReports.filter(r => r.schedule?.active).length,
      scheduledReports: mockReports.filter(r => r.schedule).length,
      thisMonthRuns: mockReports.filter(r => {
        if (!r.lastRunAt) return false;
        const now = new Date();
        const lastRun = new Date(r.lastRunAt);
        return lastRun.getMonth() === now.getMonth() && lastRun.getFullYear() === now.getFullYear();
      }).length,
    };
  }, []);

  const filteredReports = useMemo(() => {
    if (selectedCategory === 'all') return mockReports;
    return mockReports.filter(r => r.category === selectedCategory.toUpperCase());
  }, [selectedCategory]);

  const reportCategories = [
    { value: 'all', label: 'All Reports', icon: '\uD83D\uDCCB' },
    { value: 'executive', label: 'Executive', icon: '\uD83D\uDC68\u200D\uD83D\uDCBB' },
    { value: 'country', label: 'Country', icon: '\uD83C\uDDF0' },
    { value: 'team', label: 'Team', icon: '\uD83D\uDC65' },
    { value: 'forecast', label: 'Forecast', icon: '\uD83D\uDCC8' },
  ];

  return (
    <div className="reports-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Global Reports</h1>
          <p className="page-subtitle">Generate and manage sales reports across all countries</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary" size="sm">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
              <line x1="3" y1="9" x2="21" y2="9" />
              <line x1="9" y1="21" x2="9" y2="9" />
            </svg>
            Templates
          </Button>
          <Button variant="primary" size="sm" onClick={() => setShowNewReport(true)}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="12" y1="5" x2="12" y2="19" />
              <line x1="5" y1="12" x2="19" y2="12" />
            </svg>
            New Report
          </Button>
        </div>
      </div>

      {/* Report Stats */}
      <section className="stats-section">
        <div className="stats-grid">
          <MetricCard
            title="Total Reports"
            value={reportStats.totalReports}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                <polyline points="14 2 14 15 7 15 7 5" />
                <line x1="7" y1="15" x2="21" y2="15" />
                <line x1="11" y1="18" x2="15" y2="18" />
              </svg>
            }
          />
          <MetricCard
            title="Active Schedules"
            value={reportStats.activeReports}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <polyline points="20 6 9 17 4 12" />
              </svg>
            }
          />
          <MetricCard
            title="Runs This Month"
            value={reportStats.thisMonthRuns}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="12" cy="12" r="10" />
                <polyline points="12 6 12 12 16 14" />
              </svg>
            }
          />
          <MetricCard
            title="Report Categories"
            value="5"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <rect x="3" y="3" width="7" height="7" />
                <rect x="14" y="3" width="7" height="7" />
                <rect x="14" y="14" width="7" height="7" />
                <rect x="3" y="14" width="7" height="7" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Quick Actions */}
      <section className="quick-actions-section">
        <Card>
          <CardHeader>
            <h3>Quick Actions</h3>
          </CardHeader>
          <CardBody>
            <div className="quick-actions-grid">
              <button className="quick-action-btn">
                <span className="action-icon">\uD83D\uDCC8</span>
                <span className="action-label">Executive Dashboard</span>
              </button>
              <button className="quick-action-btn">
                <span className="action-icon">\uD83C\uDDF0</span>
                <span className="action-label">Country Comparison</span>
              </button>
              <button className="quick-action-btn">
                <span className="action-icon">\uD83D\uDC65</span>
                <span className="action-label">Team Performance</span>
              </button>
              <button className="quick-action-btn">
                <span className="action-icon">\uD83D\uDCCA</span>
                <span className="action-label">Pipeline Analysis</span>
              </button>
              <button className="quick-action-btn">
                <span className="action-icon">\uD83C\uDFAF</span>
                <span className="action-label">Forecast Report</span>
              </button>
              <button className="quick-action-btn">
                <span className="action-icon">\u270D\uFE0F</span>
                <span className="action-label">Custom Report</span>
              </button>
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Report Categories */}
      <section className="categories-section">
        <div className="category-tabs">
          {reportCategories.map(category => (
            <button
              key={category.value}
              className={`category-tab ${selectedCategory === category.value ? 'active' : ''}`}
              onClick={() => setSelectedCategory(category.value)}
            >
              <span className="category-icon">{category.icon}</span>
              <span className="category-label">{category.label}</span>
            </button>
          ))}
        </div>
      </section>

      {/* Reports List */}
      <section className="reports-section">
        <Card>
          <CardHeader>
            <h3>
              {selectedCategory === 'all' ? 'All Reports' : reportCategories.find(c => c.value === selectedCategory)?.label}
            </h3>
            <div className="reports-actions">
              <select className="sort-select">
                <option>Last Modified</option>
                <option>Name A-Z</option>
                <option>Name Z-A</option>
                <option>Recently Created</option>
              </select>
            </div>
          </CardHeader>
          <CardBody>
            <div className="reports-list">
              {filteredReports.map((report) => (
                <div key={report.id} className="report-item">
                  <div className="report-icon">
                    {REPORT_TYPES[report.type]?.icon || '\uD83D\uDCCB'}
                  </div>
                  <div className="report-info">
                    <h4 className="report-name">{report.name}</h4>
                    <p className="report-description">{report.description}</p>
                    <div className="report-meta">
                      <span className="report-type">{REPORT_TYPES[report.type]?.label}</span>
                      {report.schedule && (
                        <span className="report-schedule">
                          Scheduled: {report.schedule.frequency}
                        </span>
                      )}
                      {report.config?.exportFormat && (
                        <span className="report-format">{report.config.exportFormat}</span>
                      )}
                    </div>
                  </div>
                  <div className="report-status">
                    {report.schedule?.active ? (
                      <span className="status-badge active">Active Schedule</span>
                    ) : (
                      <span className="status-badge manual">Manual</span>
                    )}
                  </div>
                  <div className="report-actions">
                    <Button variant="link" size="sm">Run Now</Button>
                    <Button variant="link" size="sm">Edit</Button>
                    <Button variant="link" size="sm">\u22EE</Button>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Scheduled Reports */}
      <section className="scheduled-section">
        <Card>
          <CardHeader>
            <h3>Scheduled Reports</h3>
            <Button variant="link" size="sm">View Calendar</Button>
          </CardHeader>
          <CardBody>
            <div className="scheduled-grid">
              {mockReports.filter(r => r.schedule?.active).map((report) => (
                <div key={report.id} className="scheduled-card">
                  <div className="scheduled-header">
                    <span className="scheduled-icon">{REPORT_TYPES[report.type]?.icon}</span>
                    <div className="scheduled-info">
                      <h4 className="scheduled-name">{report.name}</h4>
                      <span className="scheduled-frequency">
                        Every {report.schedule?.frequency || 'month'}
                      </span>
                    </div>
                  </div>
                  <div className="scheduled-details">
                    <div className="scheduled-detail">
                      <span className="detail-label">Next Run</span>
                      <span className="detail-value">
                        {report.schedule?.nextRunAt ? new Date(report.schedule.nextRunAt).toLocaleDateString() : 'N/A'}
                      </span>
                    </div>
                    <div className="scheduled-detail">
                      <span className="detail-label">Recipients</span>
                      <span className="detail-value">
                        {report.schedule?.recipients.length || 0} people
                      </span>
                    </div>
                    <div className="scheduled-detail">
                      <span className="detail-label">Last Run</span>
                      <span className="detail-value">
                        {report.lastRunAt ? new Date(report.lastRunAt).toLocaleDateString() : 'Never'}
                      </span>
                    </div>
                  </div>
                  <div className="scheduled-actions">
                    <Button variant="ghost" size="sm">Pause</Button>
                    <Button variant="ghost" size="sm">Edit</Button>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* New Report Modal */}
      {showNewReport && (
        <div className="modal-overlay" onClick={() => setShowNewReport(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Create New Report</h2>
              <button className="modal-close" onClick={() => setShowNewReport(false)}>\u2715</button>
            </div>
            <div className="modal-body">
              <div className="form-group">
                <label>Report Name</label>
                <input type="text" placeholder="Enter report name" />
              </div>
              <div className="form-group">
                <label>Report Type</label>
                <select>
                  {Object.entries(REPORT_TYPES).map(([key, value]) => (
                    <option key={key} value={key}>{value.label}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Countries</label>
                <div className="countries-select">
                  {mockCountries.map(country => (
                    <label key={country.code} className="country-checkbox">
                      <input type="checkbox" />
                      <span>{country.flag} {country.name}</span>
                    </label>
                  ))}
                </div>
              </div>
              <div className="form-group">
                <label>Export Format</label>
                <select>
                  {EXPORT_FORMATS.map(format => (
                    <option key={format} value={format}>{format}</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Schedule (Optional)</label>
                <div className="schedule-options">
                  <label className="radio-option">
                    <input type="radio" name="schedule" value="none" defaultChecked />
                    <span>Run once</span>
                  </label>
                  <label className="radio-option">
                    <input type="radio" name="schedule" value="daily" />
                    <span>Daily</span>
                  </label>
                  <label className="radio-option">
                    <input type="radio" name="schedule" value="weekly" />
                    <span>Weekly</span>
                  </label>
                  <label className="radio-option">
                    <input type="radio" name="schedule" value="monthly" />
                    <span>Monthly</span>
                  </label>
                </div>
              </div>
            </div>
            <div className="modal-footer">
              <Button variant="ghost" onClick={() => setShowNewReport(false)}>Cancel</Button>
              <Button variant="primary">Create Report</Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
