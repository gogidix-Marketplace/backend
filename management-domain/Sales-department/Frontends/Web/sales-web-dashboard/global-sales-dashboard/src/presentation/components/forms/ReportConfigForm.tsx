// Report Configuration Form Component
// Form for configuring and generating reports

import { useState } from 'react';
import type { Report, ReportConfig, ReportType, ReportCategory } from '@domain/types';
import { REPORT_TYPES } from '@shared';
import { Button } from '../common/Button';
import { Card, CardHeader, CardBody } from '../common/Card';
import './ReportConfigForm.css';

export interface ReportConfigFormProps {
  report?: Report;
  onSave: (config: ReportConfig) => Promise<void>;
  onCancel: () => void;
  isLoading?: boolean;
}

const METRIC_OPTIONS = [
  { value: 'revenue', label: 'Revenue' },
  { value: 'pipeline', label: 'Pipeline Value' },
  { value: 'deals', label: 'Deals Closed' },
  { value: 'winRate', label: 'Win Rate' },
  { value: 'forecast', label: 'Forecast' },
  { value: 'quota', label: 'Quota Attainment' },
] as const;

const VISUALIZATION_OPTIONS = [
  { value: 'number', label: 'Number Card' },
  { value: 'line', label: 'Line Chart' },
  { value: 'bar', label: 'Bar Chart' },
  { value: 'pie', label: 'Pie Chart' },
  { value: 'table', label: 'Table' },
] as const;

const EXPORT_FORMATS = [
  { value: 'PDF', label: 'PDF Document' },
  { value: 'EXCEL', label: 'Excel Spreadsheet' },
  { value: 'CSV', label: 'CSV Data' },
] as const;

export function ReportConfigForm({ report, onSave, onCancel, isLoading = false }: ReportConfigFormProps) {
  const [name, setName] = useState(report?.name || '');
  const [description, setDescription] = useState(report?.description || '');
  const [reportType, setReportType] = useState<ReportType>(report?.type || 'CUSTOM');
  const [selectedMetrics, setSelectedMetrics] = useState<string[]>(
    report?.config.metrics || ['revenue', 'pipeline']
  );
  const [selectedCountries, setSelectedCountries] = useState<string[]>(
    report?.config.countries || []
  );
  const [visualizations, setVisualizations] = useState(
    report?.config.visualizations || []
  );
  const [exportFormat, setExportFormat] = useState<'PDF' | 'EXCEL' | 'CSV'>(
    report?.config.exportFormat || 'PDF'
  );
  const [scheduleEnabled, setScheduleEnabled] = useState(!!report?.schedule?.active);
  const [scheduleFrequency, setScheduleFrequency] = useState(
    report?.schedule?.frequency || 'monthly'
  );
  const [recipients, setRecipients] = useState(report?.schedule?.recipients?.join(', ') || '');

  const availableCountries = ['NG', 'KE', 'GH', 'ZA', 'ET', 'UG'];

  const toggleMetric = (metric: string) => {
    setSelectedMetrics((prev) =>
      prev.includes(metric) ? prev.filter((m) => m !== metric) : [...prev, metric]
    );
  };

  const toggleCountry = (code: string) => {
    setSelectedCountries((prev) =>
      prev.includes(code) ? prev.filter((c) => c !== code) : [...prev, code]
    );
  };

  const addVisualization = () => {
    setVisualizations((prev) => [
      ...prev,
      { type: 'bar' as const, title: '', dataSource: 'revenue', config: {} },
    ]);
  };

  const updateVisualization = (index: number, field: string, value: any) => {
    setVisualizations((prev) =>
      prev.map((v, i) => (i === index ? { ...v, [field]: value } : v))
    );
  };

  const removeVisualization = (index: number) => {
    setVisualizations((prev) => prev.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const config: ReportConfig = {
      metrics: selectedMetrics,
      countries: selectedCountries,
      period: {
        start: new Date(),
        end: new Date(),
        type: 'monthly',
      },
      filters: {},
      visualizations,
      exportFormat,
    };

    await onSave(config);
  };

  const isValid = name.trim() !== '' && selectedMetrics.length > 0;

  return (
    <form onSubmit={handleSubmit} className="report-config-form">
      <div className="config-form-section">
        <h3>Report Details</h3>
        <Card>
          <CardBody>
            <div className="form-row">
              <label className="form-label">
                Report Name *
                <input
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Enter report name"
                  className="form-input"
                  required
                />
              </label>
            </div>

            <div className="form-row">
              <label className="form-label">
                Description
                <textarea
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="Describe this report..."
                  rows={3}
                  className="form-textarea"
                />
              </label>
            </div>

            <div className="form-row">
              <label className="form-label">
                Report Type
                <select
                  value={reportType}
                  onChange={(e) => setReportType(e.target.value as ReportType)}
                  className="form-select"
                >
                  {Object.entries(REPORT_TYPES).map(([key, { label }]) => (
                    <option key={key} value={key}>
                      {label}
                    </option>
                  ))}
                </select>
              </label>
            </div>
          </CardBody>
        </Card>
      </div>

      <div className="config-form-section">
        <h3>Data Configuration</h3>

        <Card>
          <CardHeader>
            <h4>Metrics</h4>
          </CardHeader>
          <CardBody>
            <div className="metrics-grid">
              {METRIC_OPTIONS.map((metric) => (
                <label
                  key={metric.value}
                  className={`metric-checkbox ${selectedMetrics.includes(metric.value) ? 'selected' : ''}`}
                >
                  <input
                    type="checkbox"
                    checked={selectedMetrics.includes(metric.value)}
                    onChange={() => toggleMetric(metric.value)}
                  />
                  <span>{metric.label}</span>
                </label>
              ))}
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <h4>Countries</h4>
            <Button variant="ghost" size="sm" onClick={() => setSelectedCountries([])}>
              Clear All
            </Button>
          </CardHeader>
          <CardBody>
            <div className="countries-grid">
              {availableCountries.map((code) => (
                <button
                  key={code}
                  type="button"
                  className={`country-checkbox ${selectedCountries.includes(code) ? 'selected' : ''}`}
                  onClick={() => toggleCountry(code)}
                >
                  {code}
                </button>
              ))}
            </div>
            {selectedCountries.length === 0 && (
              <p className="form-hint">All countries will be included if none selected.</p>
            )}
          </CardBody>
        </Card>
      </div>

      <div className="config-form-section">
        <h3>Visualizations</h3>
        <Card>
          <CardBody>
            {visualizations.length === 0 ? (
              <div className="empty-visualizations">
                <p>No visualizations added yet.</p>
                <Button variant="secondary" size="sm" onClick={addVisualization}>
                  Add Visualization
                </Button>
              </div>
            ) : (
              <div className="visualizations-list">
                {visualizations.map((viz, index) => (
                  <div key={index} className="visualization-item">
                    <div className="viz-config-row">
                      <select
                        value={viz.type}
                        onChange={(e) => updateVisualization(index, 'type', e.target.value)}
                        className="form-select form-select-sm"
                      >
                        {VISUALIZATION_OPTIONS.map((opt) => (
                          <option key={opt.value} value={opt.value}>
                            {opt.label}
                          </option>
                        ))}
                      </select>
                      <input
                        type="text"
                        value={viz.title}
                        onChange={(e) => updateVisualization(index, 'title', e.target.value)}
                        placeholder="Chart title"
                        className="form-input form-input-sm"
                      />
                      <select
                        value={viz.dataSource}
                        onChange={(e) => updateVisualization(index, 'dataSource', e.target.value)}
                        className="form-select form-select-sm"
                      >
                        {selectedMetrics.map((metric) => (
                          <option key={metric} value={metric}>
                            {METRIC_OPTIONS.find((m) => m.value === metric)?.label}
                          </option>
                        ))}
                      </select>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => removeVisualization(index)}
                      >
                        Remove
                      </Button>
                    </div>
                  </div>
                ))}
                <Button variant="secondary" size="sm" onClick={addVisualization}>
                  Add Another Visualization
                </Button>
              </div>
            )}
          </CardBody>
        </Card>
      </div>

      <div className="config-form-section">
        <h3>Export & Schedule</h3>
        <Card>
          <CardBody>
            <div className="form-row">
              <label className="form-label">
                Export Format
                <div className="radio-group">
                  {EXPORT_FORMATS.map((format) => (
                    <label key={format.value} className="radio-label">
                      <input
                        type="radio"
                        name="format"
                        value={format.value}
                        checked={exportFormat === format.value}
                        onChange={() => setExportFormat(format.value as any)}
                      />
                      <span>{format.label}</span>
                    </label>
                  ))}
                </div>
              </label>
            </div>

            <div className="form-row">
              <label className="form-checkbox-label">
                <input
                  type="checkbox"
                  checked={scheduleEnabled}
                  onChange={(e) => setScheduleEnabled(e.target.checked)}
                />
                <div>
                  <span>Enable Schedule</span>
                  <p className="form-hint">Automatically generate and email this report</p>
                </div>
              </label>
            </div>

            {scheduleEnabled && (
              <>
                <div className="form-row">
                  <label className="form-label">
                    Frequency
                    <select
                      value={scheduleFrequency}
                      onChange={(e) => setScheduleFrequency(e.target.value)}
                      className="form-select"
                    >
                      <option value="daily">Daily</option>
                      <option value="weekly">Weekly</option>
                      <option value="monthly">Monthly</option>
                      <option value="quarterly">Quarterly</option>
                    </select>
                  </label>
                </div>
                <div className="form-row">
                  <label className="form-label">
                    Email Recipients
                    <input
                      type="text"
                      value={recipients}
                      onChange={(e) => setRecipients(e.target.value)}
                      placeholder="user1@example.com, user2@example.com"
                      className="form-input"
                    />
                    <p className="form-hint">Comma-separated email addresses</p>
                  </label>
                </div>
              </>
            )}
          </CardBody>
        </Card>
      </div>

      <div className="config-form-actions">
        <Button variant="secondary" onClick={onCancel} disabled={isLoading}>
          Cancel
        </Button>
        <Button variant="primary" type="submit" disabled={!isValid || isLoading}>
          {isLoading ? 'Saving...' : report ? 'Update Report' : 'Create Report'}
        </Button>
      </div>
    </form>
  );
}
