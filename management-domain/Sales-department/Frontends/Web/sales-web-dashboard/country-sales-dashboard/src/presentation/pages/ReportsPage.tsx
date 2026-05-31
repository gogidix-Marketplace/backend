// ReportsPage Component
// Country-specific reports generation

import React, { useState } from 'react';
import { Card, Button, Tabs, SearchBar, StatusBadge } from '../components/common';
import { REPORT_TYPES, EXPORT_FORMATS } from '@shared/constants';
import type { CountryReport, GeneratedReport } from '@domain/types';
import { formatDate } from '@shared';

const mockReports: CountryReport[] = [
  {
    id: 'rep-001',
    name: 'Monthly Sales Performance',
    description: 'Comprehensive monthly sales performance report',
    type: 'SALES_PERFORMANCE',
    category: 'SALES',
    createdBy: 'Chinedu Amadi',
    createdAt: new Date('2025-01-15'),
    lastRunAt: new Date('2025-02-01'),
    schedule: {
      frequency: 'monthly',
      dayOfMonth: 1,
      time: '09:00',
      recipients: ['manager@gogidix.com'],
      active: true,
      nextRunAt: new Date('2025-03-01'),
    },
    config: {
      metrics: ['revenue', 'quota', 'deals', 'winRate'],
      period: {
        start: new Date('2025-02-01'),
        end: new Date('2025-02-28'),
        type: 'monthly',
        label: 'February 2025',
      },
      exportFormat: 'PDF',
    },
  },
  {
    id: 'rep-002',
    name: 'Team Performance Analysis',
    description: 'Detailed team performance comparison',
    type: 'TEAM_PERFORMANCE',
    category: 'TEAM',
    createdBy: 'Adebayo Okafor',
    createdAt: new Date('2025-01-20'),
    lastRunAt: new Date('2025-02-15'),
    config: {
      metrics: ['revenue', 'quota', 'activities'],
      period: {
        start: new Date('2025-02-01'),
        end: new Date('2025-02-28'),
        type: 'monthly',
        label: 'February 2025',
      },
      exportFormat: 'EXCEL',
    },
  },
  {
    id: 'rep-003',
    name: 'Partner Commission Report',
    description: 'Monthly partner commission and payout report',
    type: 'PARTNER_PERFORMANCE',
    category: 'PARTNER',
    createdBy: 'Chinedu Amadi',
    createdAt: new Date('2025-01-25'),
    lastRunAt: new Date('2025-02-01'),
    schedule: {
      frequency: 'monthly',
      dayOfMonth: 5,
      time: '10:00',
      recipients: ['finance@gogidix.com'],
      active: true,
      nextRunAt: new Date('2025-03-05'),
    },
    config: {
      metrics: ['commission', 'referrals', 'sales'],
      period: {
        start: new Date('2025-02-01'),
        end: new Date('2025-02-28'),
        type: 'monthly',
        label: 'February 2025',
      },
      exportFormat: 'PDF',
    },
  },
];

const mockGeneratedReports: GeneratedReport[] = [
  {
    id: 'gen-001',
    reportId: 'rep-001',
    reportName: 'Monthly Sales Performance',
    generatedBy: 'Chinedu Amadi',
    generatedAt: new Date('2025-02-01T09:00:00'),
    period: {
      start: new Date('2025-01-01'),
      end: new Date('2025-01-31'),
      type: 'monthly',
      label: 'January 2025',
    },
    status: 'completed',
    fileUrl: '/reports/monthly-sales-jan-2025.pdf',
    expiresAt: new Date('2025-03-01'),
  },
  {
    id: 'gen-002',
    reportId: 'rep-001',
    reportName: 'Monthly Sales Performance',
    generatedBy: 'Chinedu Amadi',
    generatedAt: new Date('2025-02-01T09:30:00'),
    period: {
      start: new Date('2025-02-01'),
      end: new Date('2025-02-28'),
      type: 'monthly',
      label: 'February 2025',
    },
    status: 'completed',
    fileUrl: '/reports/monthly-sales-feb-2025.pdf',
    expiresAt: new Date('2025-04-01'),
  },
];

export const ReportsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('saved');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedReport, setSelectedReport] = useState<CountryReport | null>(null);
  const [showGenerateModal, setShowGenerateModal] = useState(false);

  const filteredReports = mockReports.filter(report =>
    report.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    report.description.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleGenerateReport = async (report: CountryReport, format: string) => {
    // Generate report logic
  };

  const handleScheduleReport = (reportId: string) => {
    // Schedule report logic
  };

  const handleDeleteReport = (reportId: string) => {
    if (confirm('Are you sure you want to delete this report?')) {
      // Delete logic
    }
  };

  const reportTypeOptions = Object.entries(REPORT_TYPES).map(([key, val]) => ({
    value: key,
    label: `${val.icon} ${val.label}`,
  }));

  const exportFormatOptions = EXPORT_FORMATS.map(format => ({
    value: format,
    label: format,
  }));

  const tabs = [
    {
      id: 'saved',
      label: 'Saved Reports',
      badge: mockReports.length,
      content: (
        <div className="saved-reports">
          <div className="reports-list">
            {filteredReports.map(report => (
              <Card key={report.id} className="report-card">
                <div className="report-card-header">
                  <div className="report-icon">{REPORT_TYPES[report.type]?.icon}</div>
                  <div className="report-info">
                    <h4>{report.name}</h4>
                    <p className="report-description">{report.description}</p>
                    <div className="report-meta">
                      <span>Created by {report.createdBy}</span>
                      <span>Last run {formatDate(report.lastRunAt!, 'MMM d, yyyy')}</span>
                    </div>
                  </div>
                  <div className="report-schedule">
                    {report.schedule ? (
                      <span className="schedule-badge">
                        Scheduled: {report.schedule.frequency}
                      </span>
                    ) : (
                      <span className="schedule-badge schedule-none">Not scheduled</span>
                    )}
                  </div>
                </div>

                <div className="report-card-footer">
                  <div className="report-actions">
                    <button
                      className="btn btn-sm btn-primary"
                      onClick={() => handleGenerateReport(report, 'PDF')}
                    >
                      Run Now
                    </button>
                    <button
                      className="btn btn-sm btn-secondary"
                      onClick={() => {/* Edit report */}}
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-sm btn-secondary"
                      onClick={() => handleScheduleReport(report.id)}
                    >
                      {report.schedule?.active ? 'Edit Schedule' : 'Schedule'}
                    </button>
                    <button
                      className="btn btn-sm btn-danger"
                      onClick={() => handleDeleteReport(report.id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>
      ),
    },
    {
      id: 'generated',
      label: 'Generated Reports',
      badge: mockGeneratedReports.length,
      content: (
        <div className="generated-reports">
          <table className="data-table">
            <thead>
              <tr>
                <th>Report Name</th>
                <th>Period</th>
                <th>Generated</th>
                <th>Generated By</th>
                <th>Status</th>
                <th>Expires</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {mockGeneratedReports.map(gen => (
                <tr key={gen.id}>
                  <td>{gen.reportName}</td>
                  <td>{gen.period.label}</td>
                  <td>{formatDate(gen.generatedAt, 'MMM d, yyyy h:mm a')}</td>
                  <td>{gen.generatedBy}</td>
                  <td>
                    <StatusBadge
                      status={gen.status}
                      size="sm"
                    />
                  </td>
                  <td>{formatDate(gen.expiresAt, 'MMM d, yyyy')}</td>
                  <td>
                    <div className="table-actions">
                      {gen.fileUrl && (
                        <a href={gen.fileUrl} className="btn-link" download>
                          Download
                        </a>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ),
    },
    {
      id: 'create',
      label: 'Create Report',
      content: (
        <div className="create-report">
          <Card>
            <h4>Create Custom Report</h4>

            <form className="report-form">
              <div className="form-group">
                <label>Report Name</label>
                <input type="text" className="form-input" placeholder="Enter report name" />
              </div>

              <div className="form-group">
                <label>Description</label>
                <textarea className="form-textarea" placeholder="Describe the report" rows={3} />
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Report Type</label>
                  <select className="form-input">
                    {Object.entries(REPORT_TYPES).map(([key, val]) => (
                      <option key={key} value={key}>{val.label}</option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label>Export Format</label>
                  <select className="form-input">
                    {EXPORT_FORMATS.map(format => (
                      <option key={format} value={format}>{format}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label>Include Metrics</label>
                <div className="checkbox-group">
                  {['Revenue', 'Quota Attainment', 'Deals Closed', 'Win Rate', 'Pipeline Value', 'Team Performance'].map(metric => (
                    <label key={metric} className="checkbox-item">
                      <input type="checkbox" />
                      <span>{metric}</span>
                    </label>
                  ))}
                </div>
              </div>

              <div className="form-group">
                <label>Period</label>
                <select className="form-input">
                  <option>This Month</option>
                  <option>Last Month</option>
                  <option>This Quarter</option>
                  <option>Last Quarter</option>
                  <option>Custom Range</option>
                </select>
              </div>

              <div className="form-group">
                <label>Teams to Include</label>
                <div className="checkbox-group">
                  <label className="checkbox-item">
                    <input type="checkbox" defaultChecked />
                    <span>All Teams</span>
                  </label>
                </div>
              </div>

              <div className="form-group">
                <label>Schedule (Optional)</label>
                <div className="schedule-options">
                  <label className="radio-item">
                    <input type="radio" name="schedule" value="none" defaultChecked />
                    <span>Run once</span>
                  </label>
                  <label className="radio-item">
                    <input type="radio" name="schedule" value="daily" />
                    <span>Daily</span>
                  </label>
                  <label className="radio-item">
                    <input type="radio" name="schedule" value="weekly" />
                    <span>Weekly</span>
                  </label>
                  <label className="radio-item">
                    <input type="radio" name="schedule" value="monthly" />
                    <span>Monthly</span>
                  </label>
                </div>
              </div>

              <div className="form-actions">
                <Button variant="secondary" onClick={() => setActiveTab('saved')}>
                  Cancel
                </Button>
                <Button variant="primary">Create Report</Button>
              </div>
            </form>
          </Card>
        </div>
      ),
    },
  ];

  return (
    <div className="page reports-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Reports</h1>
          <p className="page-subtitle">Generate and manage country reports</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary">Report Templates</Button>
          <Button variant="primary" onClick={() => setActiveTab('create')}>
            Create Report
          </Button>
        </div>
      </div>

      {/* Search */}
      <div className="page-filters">
        <SearchBar
          placeholder="Search reports..."
          value={searchQuery}
          onChange={setSearchQuery}
        />
      </div>

      {/* Tabs */}
      <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} />
    </div>
  );
};

export default ReportsPage;
