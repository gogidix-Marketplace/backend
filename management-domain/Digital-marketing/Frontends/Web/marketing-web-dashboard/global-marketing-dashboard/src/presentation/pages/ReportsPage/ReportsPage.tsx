// ReportsPage - Global Marketing Dashboard
// Report generation and scheduling page

import React, { useState } from 'react';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { Modal } from '../../../shared/components/common/Modal';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { SelectDropdown } from '../../../shared/components/common/SelectDropdown';
import './ReportsPage.css';

const ReportsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('reports');
  const [searchQuery, setSearchQuery] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const mockReports = [
    { id: 'rpt-001', name: 'Monthly Performance Summary', type: 'EXECUTIVE_DASHBOARD', lastRun: '2026-02-18', schedule: 'monthly' },
    { id: 'rpt-002', name: 'Campaign ROI Analysis', type: 'ROI_ANALYSIS', lastRun: '2026-02-15', schedule: 'weekly' },
    { id: 'rpt-003', name: 'Country Comparison', type: 'COUNTRY_COMPARISON', lastRun: '2026-02-17', schedule: 'monthly' },
    { id: 'rpt-004', name: 'Channel Performance', type: 'CHANNEL_ANALYSIS', lastRun: '2026-02-16', schedule: 'weekly' },
    { id: 'rpt-005', name: 'Lead Generation Report', type: 'LEAD_GENERATION', lastRun: '2026-02-18', schedule: 'daily' },
    { id: 'rpt-006', name: 'Budget Utilization', type: 'BUDGET_UTILIZATION', lastRun: '2026-02-14', schedule: 'monthly' },
    { id: 'rpt-007', name: 'Social Media Summary', type: 'SOCIAL_MEDIA', lastRun: '2026-02-17', schedule: 'weekly' },
    { id: 'rpt-008', name: 'Email Marketing Report', type: 'EMAIL_MARKETING', lastRun: '2026-02-18', schedule: 'weekly' },
  ];

  const reportTypes = [
    { value: 'EXECUTIVE_DASHBOARD', label: 'Executive Dashboard' },
    { value: 'CAMPAIGN_PERFORMANCE', label: 'Campaign Performance' },
    { value: 'CHANNEL_ANALYSIS', label: 'Channel Analysis' },
    { value: 'LEAD_GENERATION', label: 'Lead Generation' },
    { value: 'ROI_ANALYSIS', label: 'ROI Analysis' },
    { value: 'BUDGET_UTILIZATION', label: 'Budget Utilization' },
    { value: 'SOCIAL_MEDIA', label: 'Social Media' },
    { value: 'EMAIL_MARKETING', label: 'Email Marketing' },
    { value: 'SEO_PERFORMANCE', label: 'SEO Performance' },
    { value: 'CONTENT_PERFORMANCE', label: 'Content Performance' },
  ];

  const exportFormats = [
    { value: 'PDF', label: 'PDF' },
    { value: 'EXCEL', label: 'Excel' },
    { value: 'CSV', label: 'CSV' },
  ];

  const tabs = [
    { id: 'reports', label: 'Reports' },
    { id: 'scheduled', label: 'Scheduled' },
    { id: 'history', label: 'History' },
  ];

  return (
    <div className="reports-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Reports</h1>
              <p className="page__subtitle">Generate, schedule, and export marketing reports</p>
            </div>
            <div className="page__actions">
              <Button variant="secondary">View History</Button>
              <Button variant="primary" onClick={() => setIsCreateModalOpen(true)}>+ Create Report</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        {/* Summary Metrics */}
        <div className="metrics-grid">
          <MetricCard label="Total Reports" value={mockReports.length} />
          <MetricCard label="Scheduled" value={6} />
          <MetricCard label="Generated This Month" value={124} />
          <MetricCard label="Active Schedules" value={8} />
        </div>

        {/* Tabs */}
        <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} variant="underlined" />

        <TabPanel id="reports" activeTab={activeTab}>
          <div className="reports-controls">
            <SearchBar value={searchQuery} onChange={setSearchQuery} placeholder="Search reports..." />
          </div>

          <div className="reports-grid">
            {mockReports.map((report) => (
              <div key={report.id} className="report-card">
                <div className="report-card__header">
                  <h3 className="report-name">{report.name}</h3>
                  <span className="report-type">{report.type.replace('_', ' ')}</span>
                </div>
                <div className="report-card__body">
                  <span className="report-schedule">
                    <span className="report-icon">📅</span>
                    {report.schedule}
                  </span>
                  <span className="report-last-run">
                    Last run: {new Date(report.lastRun).toLocaleDateString()}
                  </span>
                </div>
                <div className="report-card__actions">
                  <Button variant="secondary" size="sm">Run Now</Button>
                  <Button variant="secondary" size="sm">Export</Button>
                </div>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="scheduled" activeTab={activeTab}>
          <div className="scheduled-reports">
            {mockReports.slice(0, 4).map((report) => (
              <div key={report.id} className="scheduled-report-item">
                <div className="scheduled-info">
                  <h4>{report.name}</h4>
                  <span>Schedule: {report.schedule}</span>
                </div>
                <div className="scheduled-actions">
                  <Button variant="secondary" size="sm">Edit</Button>
                  <Button variant="secondary" size="sm">Pause</Button>
                </div>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="history" activeTab={activeTab}>
          <div className="report-history">
            {Array.from({ length: 15 }, (_, i) => (
              <div key={i} className="history-item">
                <span className="history-name">Monthly Performance Summary</span>
                <span className="history-date">{new Date(Date.now() - i * 86400000).toLocaleDateString()}</span>
                <span className="history-status">Completed</span>
                <Button variant="secondary" size="sm">Download</Button>
              </div>
            ))}
          </div>
        </TabPanel>
      </div>

      {/* Create Report Modal */}
      <Modal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        title="Create New Report"
        size="lg"
      >
        <form className="create-report-form" onSubmit={(e) => { e.preventDefault(); setIsCreateModalOpen(false); }}>
          <div className="form-group">
            <label className="form-label">Report Name</label>
            <input type="text" className="form-input" placeholder="Enter report name" />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Report Type</label>
              <SelectDropdown options={reportTypes} placeholder="Select type" />
            </div>
            <div className="form-group">
              <label className="form-label">Export Format</label>
              <SelectDropdown options={exportFormats} placeholder="Select format" />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Schedule (Optional)</label>
            <SelectDropdown options={[
              { value: 'none', label: 'No Schedule' },
              { value: 'daily', label: 'Daily' },
              { value: 'weekly', label: 'Weekly' },
              { value: 'monthly', label: 'Monthly' },
              { value: 'quarterly', label: 'Quarterly' },
            ]} placeholder="Select frequency" />
          </div>

          <div className="form-actions">
            <Button type="button" variant="secondary" onClick={() => setIsCreateModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Create Report
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default ReportsPage;
