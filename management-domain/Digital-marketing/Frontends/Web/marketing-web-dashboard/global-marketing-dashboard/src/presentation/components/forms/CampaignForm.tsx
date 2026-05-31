// CampaignForm Component
// Form for creating and editing campaigns

import { useState } from 'react';
import { Button, Card, CardHeader, CardBody } from '../common';
import './CampaignForm.css';

export interface CampaignFormData {
  name: string;
  description: string;
  type: string;
  channels: string[];
  countries: Array<{ code: string; budget: number; targetAudience: string }>;
  budgetTotal: number;
  startDate: string;
  endDate: string;
  brandId: string;
  objectives: string[];
  kpis: Array<{ name: string; target: number; unit: string }>;
}

interface CampaignFormProps {
  initialData?: Partial<CampaignFormData>;
  onSubmit: (data: CampaignFormData) => void;
  onCancel: () => void;
  isLoading?: boolean;
  brands?: Array<{ id: string; name: string }>;
  countries?: Array<{ code: string; name: string; currency: string }>;
}

const campaignTypes = [
  { value: 'awareness', label: 'Brand Awareness' },
  { value: 'consideration', label: 'Consideration' },
  { value: 'conversion', label: 'Conversion' },
  { value: 'retention', label: 'Customer Retention' },
];

const channelOptions = [
  { value: 'email', label: 'Email' },
  { value: 'social', label: 'Social Media' },
  { value: 'search', label: 'Search Engine' },
  { value: 'display', label: 'Display Ads' },
  { value: 'video', label: 'Video' },
  { value: 'direct_mail', label: 'Direct Mail' },
  { value: 'events', label: 'Events' },
  { value: 'webinars', label: 'Webinars' },
];

const objectiveOptions = [
  'Increase brand awareness',
  'Generate leads',
  'Drive conversions',
  'Improve customer retention',
  'Launch new product',
  'Promote specific offer',
  'Build social media following',
  'Improve SEO rankings',
];

export function CampaignForm({
  initialData,
  onSubmit,
  onCancel,
  isLoading = false,
  brands = [],
  countries = [],
}: CampaignFormProps) {
  const [formData, setFormData] = useState<CampaignFormData>({
    name: initialData?.name || '',
    description: initialData?.description || '',
    type: initialData?.type || 'awareness',
    channels: initialData?.channels || [],
    countries: initialData?.countries || [],
    budgetTotal: initialData?.budgetTotal || 0,
    startDate: initialData?.startDate || '',
    endDate: initialData?.endDate || '',
    brandId: initialData?.brandId || '',
    objectives: initialData?.objectives || [],
    kpis: initialData?.kpis || [],
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) {
      newErrors.name = 'Campaign name is required';
    }
    if (!formData.description.trim()) {
      newErrors.description = 'Description is required';
    }
    if (formData.channels.length === 0) {
      newErrors.channels = 'At least one channel is required';
    }
    if (formData.countries.length === 0) {
      newErrors.countries = 'At least one country is required';
    }
    if (formData.budgetTotal <= 0) {
      newErrors.budgetTotal = 'Budget must be greater than 0';
    }
    if (!formData.startDate) {
      newErrors.startDate = 'Start date is required';
    }
    if (!formData.endDate) {
      newErrors.endDate = 'End date is required';
    }
    if (formData.startDate && formData.endDate && new Date(formData.endDate) <= new Date(formData.startDate)) {
      newErrors.endDate = 'End date must be after start date';
    }
    if (!formData.brandId) {
      newErrors.brandId = 'Brand selection is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateForm()) {
      onSubmit(formData);
    }
  };

  const handleChannelToggle = (channel: string) => {
    setFormData(prev => ({
      ...prev,
      channels: prev.channels.includes(channel)
        ? prev.channels.filter(c => c !== channel)
        : [...prev.channels, channel]
    }));
  };

  const handleObjectiveToggle = (objective: string) => {
    setFormData(prev => ({
      ...prev,
      objectives: prev.objectives.includes(objective)
        ? prev.objectives.filter(o => o !== objective)
        : [...prev.objectives, objective]
    }));
  };

  const addCountryTarget = () => {
    setFormData(prev => ({
      ...prev,
      countries: [...prev.countries, { code: '', budget: 0, targetAudience: '' }]
    }));
  };

  const updateCountryTarget = (index: number, field: string, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      countries: prev.countries.map((country, i) =>
        i === index ? { ...country, [field]: value } : country
      )
    }));
  };

  const removeCountryTarget = (index: number) => {
    setFormData(prev => ({
      ...prev,
      countries: prev.countries.filter((_, i) => i !== index)
    }));
  };

  const addKPI = () => {
    setFormData(prev => ({
      ...prev,
      kpis: [...prev.kpis, { name: '', target: 0, unit: '' }]
    }));
  };

  const updateKPI = (index: number, field: string, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      kpis: prev.kpis.map((kpi, i) =>
        i === index ? { ...kpi, [field]: value } : kpi
      )
    }));
  };

  const removeKPI = (index: number) => {
    setFormData(prev => ({
      ...prev,
      kpis: prev.kpis.filter((_, i) => i !== index)
    }));
  };

  return (
    <Card className="campaign-form">
      <CardHeader>
        <h3>{initialData?.name ? 'Edit Campaign' : 'Create New Campaign'}</h3>
      </CardHeader>
      <CardBody>
        <form onSubmit={handleSubmit}>
          {/* Basic Information */}
          <div className="form-section">
            <h4 className="form-section-title">Basic Information</h4>

            <div className="form-group">
              <label htmlFor="name">Campaign Name *</label>
              <input
                type="text"
                id="name"
                value={formData.name}
                onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                className={errors.name ? 'input-error' : ''}
                placeholder="Enter campaign name"
              />
              {errors.name && <span className="error-message">{errors.name}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="description">Description *</label>
              <textarea
                id="description"
                value={formData.description}
                onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
                className={errors.description ? 'input-error' : ''}
                placeholder="Describe the campaign goals and strategy"
                rows={3}
              />
              {errors.description && <span className="error-message">{errors.description}</span>}
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="type">Campaign Type *</label>
                <select
                  id="type"
                  value={formData.type}
                  onChange={(e) => setFormData(prev => ({ ...prev, type: e.target.value }))}
                >
                  {campaignTypes.map(type => (
                    <option key={type.value} value={type.value}>{type.label}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="brandId">Brand *</label>
                <select
                  id="brandId"
                  value={formData.brandId}
                  onChange={(e) => setFormData(prev => ({ ...prev, brandId: e.target.value }))}
                  className={errors.brandId ? 'input-error' : ''}
                >
                  <option value="">Select a brand</option>
                  {brands.map(brand => (
                    <option key={brand.id} value={brand.id}>{brand.name}</option>
                  ))}
                </select>
                {errors.brandId && <span className="error-message">{errors.brandId}</span>}
              </div>
            </div>
          </div>

          {/* Channels */}
          <div className="form-section">
            <h4 className="form-section-title">Channels *</h4>
            <div className="channel-grid">
              {channelOptions.map(channel => (
                <label key={channel.value} className="channel-option">
                  <input
                    type="checkbox"
                    checked={formData.channels.includes(channel.value)}
                    onChange={() => handleChannelToggle(channel.value)}
                  />
                  <span className="channel-label">{channel.label}</span>
                </label>
              ))}
            </div>
            {errors.channels && <span className="error-message">{errors.channels}</span>}
          </div>

          {/* Countries & Budget */}
          <div className="form-section">
            <h4 className="form-section-title">Countries & Budget Allocation *</h4>
            {formData.countries.map((country, index) => (
              <div key={index} className="country-target-row">
                <select
                  value={country.code}
                  onChange={(e) => updateCountryTarget(index, 'code', e.target.value)}
                  className="country-select"
                >
                  <option value="">Select country</option>
                  {countries.map(c => (
                    <option key={c.code} value={c.code}>{c.name}</option>
                  ))}
                </select>
                <input
                  type="number"
                  value={country.budget || ''}
                  onChange={(e) => updateCountryTarget(index, 'budget', parseFloat(e.target.value) || 0)}
                  placeholder="Budget"
                  className="budget-input"
                />
                <input
                  type="text"
                  value={country.targetAudience}
                  onChange={(e) => updateCountryTarget(index, 'targetAudience', e.target.value)}
                  placeholder="Target audience"
                  className="audience-input"
                />
                <button
                  type="button"
                  className="remove-btn"
                  onClick={() => removeCountryTarget(index)}
                >
                  \u{1F5D1}
                </button>
              </div>
            ))}
            <button type="button" className="add-btn" onClick={addCountryTarget}>
              + Add Country
            </button>
            {errors.countries && <span className="error-message">{errors.countries}</span>}
          </div>

          {/* Budget & Dates */}
          <div className="form-section">
            <h4 className="form-section-title">Budget & Schedule</h4>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="budgetTotal">Total Budget (USD) *</label>
                <input
                  type="number"
                  id="budgetTotal"
                  value={formData.budgetTotal || ''}
                  onChange={(e) => setFormData(prev => ({ ...prev, budgetTotal: parseFloat(e.target.value) || 0 }))}
                  className={errors.budgetTotal ? 'input-error' : ''}
                  placeholder="0.00"
                  min="0"
                  step="0.01"
                />
                {errors.budgetTotal && <span className="error-message">{errors.budgetTotal}</span>}
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="startDate">Start Date *</label>
                <input
                  type="date"
                  id="startDate"
                  value={formData.startDate}
                  onChange={(e) => setFormData(prev => ({ ...prev, startDate: e.target.value }))}
                  className={errors.startDate ? 'input-error' : ''}
                />
                {errors.startDate && <span className="error-message">{errors.startDate}</span>}
              </div>
              <div className="form-group">
                <label htmlFor="endDate">End Date *</label>
                <input
                  type="date"
                  id="endDate"
                  value={formData.endDate}
                  onChange={(e) => setFormData(prev => ({ ...prev, endDate: e.target.value }))}
                  className={errors.endDate ? 'input-error' : ''}
                />
                {errors.endDate && <span className="error-message">{errors.endDate}</span>}
              </div>
            </div>
          </div>

          {/* Objectives */}
          <div className="form-section">
            <h4 className="form-section-title">Campaign Objectives</h4>
            <div className="objectives-grid">
              {objectiveOptions.map(objective => (
                <label key={objective} className="objective-option">
                  <input
                    type="checkbox"
                    checked={formData.objectives.includes(objective)}
                    onChange={() => handleObjectiveToggle(objective)}
                  />
                  <span>{objective}</span>
                </label>
              ))}
            </div>
          </div>

          {/* KPIs */}
          <div className="form-section">
            <h4 className="form-section-title">Key Performance Indicators</h4>
            {formData.kpis.map((kpi, index) => (
              <div key={index} className="kpi-row">
                <input
                  type="text"
                  value={kpi.name}
                  onChange={(e) => updateKPI(index, 'name', e.target.value)}
                  placeholder="KPI name"
                  className="kpi-name"
                />
                <input
                  type="number"
                  value={kpi.target || ''}
                  onChange={(e) => updateKPI(index, 'target', parseFloat(e.target.value) || 0)}
                  placeholder="Target"
                  className="kpi-target"
                />
                <input
                  type="text"
                  value={kpi.unit}
                  onChange={(e) => updateKPI(index, 'unit', e.target.value)}
                  placeholder="Unit (e.g., %, $, leads)"
                  className="kpi-unit"
                />
                <button
                  type="button"
                  className="remove-btn"
                  onClick={() => removeKPI(index)}
                >
                  \u{1F5D1}
                </button>
              </div>
            ))}
            <button type="button" className="add-btn" onClick={addKPI}>
              + Add KPI
            </button>
          </div>

          {/* Actions */}
          <div className="form-actions">
            <Button type="button" variant="ghost" onClick={onCancel}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" loading={isLoading}>
              {initialData?.name ? 'Update Campaign' : 'Create Campaign'}
            </Button>
          </div>
        </form>
      </CardBody>
    </Card>
  );
}
