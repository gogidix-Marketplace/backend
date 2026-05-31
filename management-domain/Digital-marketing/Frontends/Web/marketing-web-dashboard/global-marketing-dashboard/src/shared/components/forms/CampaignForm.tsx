// CampaignForm Component
// Form for creating and editing campaigns

import React, { useState, useEffect } from 'react';
import { Button } from '../common/Button';
import { SelectDropdown, SelectOption } from '../common/SelectDropdown';
import { Modal } from '../common/Modal';
import { Campaign, CampaignType, ChannelType, CampaignStatus, CountryTarget } from '../../domain/types';
import { formatCurrency } from '../../utils/formatters';
import './CampaignForm.css';

const campaignTypeOptions: SelectOption[] = [
  { value: 'awareness', label: 'Awareness' },
  { value: 'consideration', label: 'Consideration' },
  { value: 'conversion', label: 'Conversion' },
  { value: 'retention', label: 'Retention' },
];

const channelOptions: SelectOption[] = [
  { value: 'email', label: 'Email' },
  { value: 'social', label: 'Social Media' },
  { value: 'search', label: 'Search' },
  { value: 'display', label: 'Display' },
  { value: 'video', label: 'Video' },
  { value: 'direct_mail', label: 'Direct Mail' },
  { value: 'events', label: 'Events' },
  { value: 'webinars', label: 'Webinars' },
  { value: 'content', label: 'Content' },
];

const statusOptions: SelectOption[] = [
  { value: 'draft', label: 'Draft' },
  { value: 'scheduled', label: 'Scheduled' },
  { value: 'active', label: 'Active' },
  { value: 'paused', label: 'Paused' },
  { value: 'completed', label: 'Completed' },
  { value: 'cancelled', label: 'Cancelled' },
];

interface CountryAllocation {
  countryCode: string;
  countryName: string;
  budget: string;
  targetAudience: string;
}

interface KPI {
  name: string;
  target: string;
  unit: string;
}

export interface CampaignFormProps {
  campaign?: Campaign;
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: Partial<Campaign>) => void;
  mode?: 'create' | 'edit';
  countries?: SelectOption[];
}

export const CampaignForm: React.FC<CampaignFormProps> = ({
  campaign,
  isOpen,
  onClose,
  onSubmit,
  mode = 'create',
  countries = [],
}) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    type: 'conversion' as CampaignType,
    status: 'draft' as CampaignStatus,
    channels: [] as ChannelType[],
    budget: '',
    currency: 'USD',
    startDate: '',
    endDate: '',
    brandId: '',
    objectives: [] as string[],
    tags: [] as string[],
  });

  const [countryAllocations, setCountryAllocations] = useState<CountryAllocation[]>([]);
  const [kpis, setKpis] = useState<KPI[]>([]);
  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    if (campaign && mode === 'edit') {
      setFormData({
        name: campaign.name,
        description: campaign.description,
        type: campaign.type,
        status: campaign.status,
        channels: campaign.channels,
        budget: campaign.budget.total.toString(),
        currency: campaign.budget.currency,
        startDate: new Date(campaign.dates.start).toISOString().split('T')[0],
        endDate: new Date(campaign.dates.end).toISOString().split('T')[0],
        brandId: campaign.brand.id,
        objectives: campaign.objectives,
        tags: campaign.tags,
      });

      setCountryAllocations(
        campaign.countries.map((c) => ({
          countryCode: c.countryCode,
          countryName: c.countryName,
          budget: c.budget.toString(),
          targetAudience: c.targetAudience,
        }))
      );

      setKpis(
        campaign.kpis.map((k) => ({
          name: k.name,
          target: k.target.toString(),
          unit: k.unit,
        }))
      );
    }
  }, [campaign, mode]);

  const handleInputChange = (field: string, value: string | string[]) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[field];
        return newErrors;
      });
    }
  };

  const handleChannelToggle = (channel: ChannelType) => {
    setFormData((prev) => ({
      ...prev,
      channels: prev.channels.includes(channel)
        ? prev.channels.filter((c) => c !== channel)
        : [...prev.channels, channel],
    }));
  };

  const handleAddCountry = () => {
    setCountryAllocations((prev) => [
      ...prev,
      { countryCode: '', countryName: '', budget: '', targetAudience: '' },
    ]);
  };

  const handleCountryChange = (index: number, field: keyof CountryAllocation, value: string) => {
    setCountryAllocations((prev) =>
      prev.map((item, i) =>
        i === index ? { ...item, [field]: value } : item
      )
    );
  };

  const handleRemoveCountry = (index: number) => {
    setCountryAllocations((prev) => prev.filter((_, i) => i !== index));
  };

  const handleAddKPI = () => {
    setKpis((prev) => [...prev, { name: '', target: '', unit: '' }]);
  };

  const handleKPIChange = (index: number, field: keyof KPI, value: string) => {
    setKpis((prev) =>
      prev.map((item, i) => (i === index ? { ...item, [field]: value } : item))
    );
  };

  const handleRemoveKPI = (index: number) => {
    setKpis((prev) => prev.filter((_, i) => i !== index));
  };

  const validate = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) {
      newErrors.name = 'Campaign name is required';
    }
    if (!formData.budget || parseFloat(formData.budget) <= 0) {
      newErrors.budget = 'Budget must be greater than 0';
    }
    if (!formData.startDate) {
      newErrors.startDate = 'Start date is required';
    }
    if (!formData.endDate) {
      newErrors.endDate = 'End date is required';
    }
    if (formData.startDate && formData.endDate && formData.startDate >= formData.endDate) {
      newErrors.endDate = 'End date must be after start date';
    }
    if (formData.channels.length === 0) {
      newErrors.channels = 'At least one channel must be selected';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    const totalBudget = parseFloat(formData.budget);
    const allocations = countryAllocations.map((c) => ({
      countryCode: c.countryCode,
      countryName: c.countryName,
      budget: parseFloat(c.budget) || 0,
      targetAudience: c.targetAudience,
    }));

    const data: Partial<Campaign> = {
      name: formData.name,
      description: formData.description,
      type: formData.type,
      status: formData.status,
      channels: formData.channels,
      countries: allocations,
      budget: {
        total: totalBudget,
        spent: campaign?.budget.spent || 0,
        remaining: totalBudget - (campaign?.budget.spent || 0),
        currency: formData.currency,
      },
      dates: {
        start: new Date(formData.startDate),
        end: new Date(formData.endDate),
        createdAt: campaign?.dates.createdAt || new Date(),
        updatedAt: new Date(),
      },
      metrics: campaign?.metrics || {
        impressions: 0,
        clicks: 0,
        conversions: 0,
        cost: 0,
        revenue: 0,
        leads: 0,
        ctr: 0,
        cpc: 0,
        cpa: 0,
        roas: 0,
      },
      owner: campaign?.owner || {
        id: 'current-user',
        name: 'Current User',
        email: 'user@gogidix.com',
      },
      brand: campaign?.brand || {
        id: formData.brandId || 'default',
        name: 'Gogidix Global',
      },
      tags: formData.tags,
      objectives: formData.objectives,
      kpis: kpis.map((k) => ({
        name: k.name,
        target: parseFloat(k.target) || 0,
        current: campaign?.kpis.find((ck) => ck.name === k.name)?.current || 0,
        unit: k.unit,
      })),
    };

    onSubmit(data);
  };

  const totalBudget = parseFloat(formData.budget) || 0;
  const allocatedBudget = countryAllocations.reduce((sum, c) => sum + (parseFloat(c.budget) || 0), 0);
  const remainingBudget = totalBudget - allocatedBudget;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={mode === 'create' ? 'Create New Campaign' : 'Edit Campaign'}
      size="lg"
    >
      <form onSubmit={handleSubmit} className="campaign-form">
        <div className="campaign-form__section">
          <h3 className="campaign-form__section-title">Basic Information</h3>

          <div className="form-group">
            <label htmlFor="campaign-name" className="form-label">
              Campaign Name <span className="required">*</span>
            </label>
            <input
              id="campaign-name"
              type="text"
              className="form-input"
              value={formData.name}
              onChange={(e) => handleInputChange('name', e.target.value)}
              placeholder="Enter campaign name"
            />
            {errors.name && <span className="form-error">{errors.name}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="campaign-description" className="form-label">
              Description
            </label>
            <textarea
              id="campaign-description"
              className="form-textarea"
              value={formData.description}
              onChange={(e) => handleInputChange('description', e.target.value)}
              placeholder="Describe your campaign objectives..."
              rows={3}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="campaign-type" className="form-label">
                Campaign Type <span className="required">*</span>
              </label>
              <SelectDropdown
                id="campaign-type"
                options={campaignTypeOptions}
                value={formData.type}
                onChange={(v) => handleInputChange('type', v)}
              />
            </div>

            <div className="form-group">
              <label htmlFor="campaign-status" className="form-label">
                Status
              </label>
              <SelectDropdown
                id="campaign-status"
                options={statusOptions}
                value={formData.status}
                onChange={(v) => handleInputChange('status', v as CampaignStatus)}
              />
            </div>
          </div>
        </div>

        <div className="campaign-form__section">
          <h3 className="campaign-form__section-title">Channels</h3>
          <div className="channel-selector">
            {channelOptions.map((channel) => (
              <label key={channel.value} className="channel-option">
                <input
                  type="checkbox"
                  checked={formData.channels.includes(channel.value as ChannelType)}
                  onChange={() => handleChannelToggle(channel.value as ChannelType)}
                />
                <span className="channel-option-label">{channel.label}</span>
              </label>
            ))}
          </div>
          {errors.channels && <span className="form-error">{errors.channels}</span>}
        </div>

        <div className="campaign-form__section">
          <h3 className="campaign-form__section-title">Budget & Dates</h3>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="campaign-budget" className="form-label">
                Total Budget ({formData.currency}) <span className="required">*</span>
              </label>
              <input
                id="campaign-budget"
                type="number"
                className="form-input"
                value={formData.budget}
                onChange={(e) => handleInputChange('budget', e.target.value)}
                placeholder="0.00"
                min="0"
                step="0.01"
              />
              {errors.budget && <span className="form-error">{errors.budget}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="campaign-currency" className="form-label">
                Currency
              </label>
              <SelectDropdown
                id="campaign-currency"
                options={[
                  { value: 'USD', label: 'USD' },
                  { value: 'EUR', label: 'EUR' },
                  { value: 'GBP', label: 'GBP' },
                  { value: 'JPY', label: 'JPY' },
                ]}
                value={formData.currency}
                onChange={(v) => handleInputChange('currency', v)}
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="campaign-start" className="form-label">
                Start Date <span className="required">*</span>
              </label>
              <input
                id="campaign-start"
                type="date"
                className="form-input"
                value={formData.startDate}
                onChange={(e) => handleInputChange('startDate', e.target.value)}
              />
              {errors.startDate && <span className="form-error">{errors.startDate}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="campaign-end" className="form-label">
                End Date <span className="required">*</span>
              </label>
              <input
                id="campaign-end"
                type="date"
                className="form-input"
                value={formData.endDate}
                onChange={(e) => handleInputChange('endDate', e.target.value)}
              />
              {errors.endDate && <span className="form-error">{errors.endDate}</span>}
            </div>
          </div>
        </div>

        <div className="campaign-form__section">
          <div className="section-header">
            <h3 className="campaign-form__section-title">Country Allocations</h3>
            <Button type="button" variant="secondary" size="sm" onClick={handleAddCountry}>
              + Add Country
            </Button>
          </div>

          {countryAllocations.length === 0 ? (
            <p className="empty-message">No countries added. Click "Add Country" to allocate budget.</p>
          ) : (
            <div className="country-allocations">
              {countryAllocations.map((allocation, index) => (
                <div key={index} className="country-allocation-row">
                  <div className="form-row">
                    <div className="form-group flex-1">
                      <label className="form-label">Country</label>
                      <SelectDropdown
                        options={countries}
                        value={allocation.countryCode}
                        onChange={(v) => {
                          const country = countries.find(c => c.value === v);
                          handleCountryChange(index, 'countryCode', v);
                          if (country) handleCountryChange(index, 'countryName', country.label);
                        }}
                      />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Budget</label>
                      <input
                        type="number"
                        className="form-input"
                        value={allocation.budget}
                        onChange={(e) => handleCountryChange(index, 'budget', e.target.value)}
                        placeholder="0.00"
                        min="0"
                        step="0.01"
                      />
                    </div>

                    <div className="form-group flex-1">
                      <label className="form-label">Target Audience</label>
                      <input
                        type="text"
                        className="form-input"
                        value={allocation.targetAudience}
                        onChange={(e) => handleCountryChange(index, 'targetAudience', e.target.value)}
                        placeholder="e.g., Professionals 25-45"
                      />
                    </div>

                    <div className="form-group">
                      <label className="form-label">&nbsp;</label>
                      <Button
                        type="button"
                        variant="danger"
                        size="sm"
                        onClick={() => handleRemoveCountry(index)}
                      >
                        Remove
                      </Button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {countryAllocations.length > 0 && (
            <div className="budget-summary">
              <div className="budget-summary-item">
                <span>Total Budget:</span>
                <span>{formatCurrency(totalBudget, formData.currency)}</span>
              </div>
              <div className="budget-summary-item">
                <span>Allocated:</span>
                <span>{formatCurrency(allocatedBudget, formData.currency)}</span>
              </div>
              <div className={`budget-summary-item ${remainingBudget < 0 ? 'over-budget' : ''}`}>
                <span>Remaining:</span>
                <span>{formatCurrency(remainingBudget, formData.currency)}</span>
              </div>
            </div>
          )}
        </div>

        <div className="campaign-form__section">
          <div className="section-header">
            <h3 className="campaign-form__section-title">Key Performance Indicators</h3>
            <Button type="button" variant="secondary" size="sm" onClick={handleAddKPI}>
              + Add KPI
            </Button>
          </div>

          {kpis.length === 0 ? (
            <p className="empty-message">No KPIs defined. Add KPIs to track campaign performance.</p>
          ) : (
            <div className="kpi-list">
              {kpis.map((kpi, index) => (
                <div key={index} className="kpi-row">
                  <input
                    type="text"
                    className="form-input"
                    placeholder="KPI Name (e.g., Impressions)"
                    value={kpi.name}
                    onChange={(e) => handleKPIChange(index, 'name', e.target.value)}
                  />
                  <input
                    type="number"
                    className="form-input"
                    placeholder="Target"
                    value={kpi.target}
                    onChange={(e) => handleKPIChange(index, 'target', e.target.value)}
                  />
                  <input
                    type="text"
                    className="form-input"
                    placeholder="Unit (e.g., %, $)"
                    value={kpi.unit}
                    onChange={(e) => handleKPIChange(index, 'unit', e.target.value)}
                  />
                  <Button
                    type="button"
                    variant="danger"
                    size="sm"
                    onClick={() => handleRemoveKPI(index)}
                  >
                    Remove
                  </Button>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="campaign-form__section">
          <h3 className="campaign-form__section-title">Tags</h3>
          <input
            type="text"
            className="form-input"
            value={formData.tags.join(', ')}
            onChange={(e) =>
              handleInputChange(
                'tags',
                e.target.value.split(',').map((t) => t.trim()).filter(Boolean)
              )
            }
            placeholder="Enter tags separated by commas"
          />
        </div>

        <div className="campaign-form__actions">
          <Button type="button" variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" variant="primary">
            {mode === 'create' ? 'Create Campaign' : 'Save Changes'}
          </Button>
        </div>
      </form>
    </Modal>
  );
};

export default CampaignForm;
