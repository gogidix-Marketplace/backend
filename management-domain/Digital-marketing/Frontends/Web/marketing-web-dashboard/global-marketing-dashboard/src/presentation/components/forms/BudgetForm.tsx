// BudgetForm Component
// Form for creating and editing budgets

import { useState } from 'react';
import { Button, Card, CardHeader, CardBody } from '../common';
import './BudgetForm.css';

export interface BudgetFormData {
  name: string;
  fiscalYear: number;
  quarter?: number;
  totalAmount: number;
  currency: string;
  allocations: Array<{ countryId: string; amount: number }>;
  categories: Array<{ channel: string; amount: number }>;
}

interface BudgetFormProps {
  initialData?: Partial<BudgetFormData>;
  onSubmit: (data: BudgetFormData) => void;
  onCancel: () => void;
  isLoading?: boolean;
  countries?: Array<{ id: string; name: string }>;
}

const currencyOptions = [
  { value: 'USD', label: 'USD - US Dollar' },
  { value: 'EUR', label: 'EUR - Euro' },
  { value: 'GBP', label: 'GBP - British Pound' },
  { value: 'JPY', label: 'JPY - Japanese Yen' },
  { value: 'CAD', label: 'CAD - Canadian Dollar' },
  { value: 'AUD', label: 'AUD - Australian Dollar' },
];

const categoryOptions = [
  { value: 'email', label: 'Email Marketing' },
  { value: 'social', label: 'Social Media' },
  { value: 'search', label: 'Search Engine Marketing' },
  { value: 'display', label: 'Display Advertising' },
  { value: 'video', label: 'Video Marketing' },
  { value: 'content', label: 'Content Marketing' },
  { value: 'events', label: 'Events & Experiences' },
  { value: 'webinars', label: 'Webinars' },
  { value: 'direct_mail', label: 'Direct Mail' },
  { value: 'other', label: 'Other' },
];

export function BudgetForm({
  initialData,
  onSubmit,
  onCancel,
  isLoading = false,
  countries = [],
}: BudgetFormProps) {
  const [formData, setFormData] = useState<BudgetFormData>({
    name: initialData?.name || '',
    fiscalYear: initialData?.fiscalYear || new Date().getFullYear(),
    quarter: initialData?.quarter,
    totalAmount: initialData?.totalAmount || 0,
    currency: initialData?.currency || 'USD',
    allocations: initialData?.allocations || [{ countryId: '', amount: 0 }],
    categories: initialData?.categories || [{ channel: 'social', amount: 0 }],
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) {
      newErrors.name = 'Budget name is required';
    }
    if (formData.totalAmount <= 0) {
      newErrors.totalAmount = 'Total amount must be greater than 0';
    }
    const allocatedTotal = formData.allocations.reduce((sum, a) => sum + (a.amount || 0), 0);
    if (Math.abs(allocatedTotal - formData.totalAmount) > 0.01) {
      newErrors.allocations = `Country allocations (${allocatedTotal.toLocaleString()}) must equal total amount (${formData.totalAmount.toLocaleString()})`;
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

  const addCountryAllocation = () => {
    setFormData(prev => ({
      ...prev,
      allocations: [...prev.allocations, { countryId: '', amount: 0 }]
    }));
  };

  const updateCountryAllocation = (index: number, field: string, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      allocations: prev.allocations.map((alloc, i) =>
        i === index ? { ...alloc, [field]: value } : alloc
      )
    }));
  };

  const removeCountryAllocation = (index: number) => {
    setFormData(prev => ({
      ...prev,
      allocations: prev.allocations.filter((_, i) => i !== index)
    }));
  };

  const addCategory = () => {
    setFormData(prev => ({
      ...prev,
      categories: [...prev.categories, { channel: 'other', amount: 0 }]
    }));
  };

  const updateCategory = (index: number, field: string, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      categories: prev.categories.map((cat, i) =>
        i === index ? { ...cat, [field]: value } : cat
      )
    }));
  };

  const removeCategory = (index: number) => {
    setFormData(prev => ({
      ...prev,
      categories: prev.categories.filter((_, i) => i !== index)
    }));
  };

  const allocatedTotal = formData.allocations.reduce((sum, a) => sum + (a.amount || 0), 0);
  const remainingAmount = formData.totalAmount - allocatedTotal;

  return (
    <Card className="budget-form">
      <CardHeader>
        <h3>{initialData?.name ? 'Edit Budget' : 'Create New Budget'}</h3>
      </CardHeader>
      <CardBody>
        <form onSubmit={handleSubmit}>
          {/* Basic Information */}
          <div className="form-section">
            <h4 className="form-section-title">Basic Information</h4>

            <div className="form-group">
              <label htmlFor="name">Budget Name *</label>
              <input
                type="text"
                id="name"
                value={formData.name}
                onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                className={errors.name ? 'input-error' : ''}
                placeholder="e.g., 2024 Marketing Budget"
              />
              {errors.name && <span className="error-message">{errors.name}</span>}
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="fiscalYear">Fiscal Year *</label>
                <input
                  type="number"
                  id="fiscalYear"
                  value={formData.fiscalYear}
                  onChange={(e) => setFormData(prev => ({ ...prev, fiscalYear: parseInt(e.target.value) }))}
                  min="2020"
                  max="2030"
                />
              </div>

              <div className="form-group">
                <label htmlFor="quarter">Quarter (Optional)</label>
                <select
                  id="quarter"
                  value={formData.quarter || ''}
                  onChange={(e) => setFormData(prev => ({ ...prev, quarter: e.target.value ? parseInt(e.target.value) : undefined }))}
                >
                  <option value="">Full Year</option>
                  <option value="1">Q1</option>
                  <option value="2">Q2</option>
                  <option value="3">Q3</option>
                  <option value="4">Q4</option>
                </select>
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="totalAmount">Total Amount *</label>
                <input
                  type="number"
                  id="totalAmount"
                  value={formData.totalAmount || ''}
                  onChange={(e) => setFormData(prev => ({ ...prev, totalAmount: parseFloat(e.target.value) || 0 }))}
                  className={errors.totalAmount ? 'input-error' : ''}
                  placeholder="0.00"
                  min="0"
                  step="0.01"
                />
                {errors.totalAmount && <span className="error-message">{errors.totalAmount}</span>}
              </div>

              <div className="form-group">
                <label htmlFor="currency">Currency *</label>
                <select
                  id="currency"
                  value={formData.currency}
                  onChange={(e) => setFormData(prev => ({ ...prev, currency: e.target.value }))}
                >
                  {currencyOptions.map(currency => (
                    <option key={currency.value} value={currency.value}>{currency.label}</option>
                  ))}
                </select>
              </div>
            </div>

            {/* Budget Summary */}
            <div className="budget-summary">
              <div className="summary-item">
                <span className="summary-label">Total Budget</span>
                <span className="summary-value">{formData.currency} {formData.totalAmount.toLocaleString()}</span>
              </div>
              <div className="summary-item">
                <span className="summary-label">Allocated</span>
                <span className="summary-value">{formData.currency} {allocatedTotal.toLocaleString()}</span>
              </div>
              <div className="summary-item">
                <span className="summary-label">Remaining</span>
                <span className={`summary-value ${remainingAmount < 0 ? 'negative' : 'positive'}`}>
                  {formData.currency} {remainingAmount.toLocaleString()}
                </span>
              </div>
            </div>
          </div>

          {/* Country Allocations */}
          <div className="form-section">
            <h4 className="form-section-title">Country Allocations *</h4>
            {formData.allocations.map((allocation, index) => (
              <div key={index} className="allocation-row">
                <select
                  value={allocation.countryId}
                  onChange={(e) => updateCountryAllocation(index, 'countryId', e.target.value)}
                  className="country-select"
                >
                  <option value="">Select country</option>
                  {countries.map(c => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))}
                </select>
                <input
                  type="number"
                  value={allocation.amount || ''}
                  onChange={(e) => updateCountryAllocation(index, 'amount', parseFloat(e.target.value) || 0)}
                  placeholder="Amount"
                  className="amount-input"
                  min="0"
                  step="0.01"
                />
                <button
                  type="button"
                  className="remove-btn"
                  onClick={() => removeCountryAllocation(index)}
                  disabled={formData.allocations.length === 1}
                >
                  \u{1F5D1}
                </button>
              </div>
            ))}
            <button type="button" className="add-btn" onClick={addCountryAllocation}>
              + Add Country
            </button>
            {errors.allocations && <span className="error-message">{errors.allocations}</span>}
          </div>

          {/* Category Breakdown */}
          <div className="form-section">
            <h4 className="form-section-title">Category Breakdown</h4>
            {formData.categories.map((category, index) => (
              <div key={index} className="category-row">
                <select
                  value={category.channel}
                  onChange={(e) => updateCategory(index, 'channel', e.target.value)}
                  className="category-select"
                >
                  {categoryOptions.map(cat => (
                    <option key={cat.value} value={cat.value}>{cat.label}</option>
                  ))}
                </select>
                <input
                  type="number"
                  value={category.amount || ''}
                  onChange={(e) => updateCategory(index, 'amount', parseFloat(e.target.value) || 0)}
                  placeholder="Amount"
                  className="amount-input"
                  min="0"
                  step="0.01"
                />
                <button
                  type="button"
                  className="remove-btn"
                  onClick={() => removeCategory(index)}
                  disabled={formData.categories.length === 1}
                >
                  \u{1F5D1}
                </button>
              </div>
            ))}
            <button type="button" className="add-btn" onClick={addCategory}>
              + Add Category
            </button>

            {/* Category Chart */}
            <div className="category-chart">
              {formData.categories.map((cat, index) => {
                const percentage = formData.totalAmount > 0 ? (cat.amount / formData.totalAmount) * 100 : 0;
                const colors = ['#6366f1', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'];
                return (
                  <div key={index} className="category-bar-item">
                    <div className="category-bar-label">{categoryOptions.find(c => c.value === cat.channel)?.label}</div>
                    <div className="category-bar-wrapper">
                      <div
                        className="category-bar-fill"
                        style={{
                          width: `${percentage}%`,
                          backgroundColor: colors[index % colors.length]
                        }}
                      />
                      <span className="category-bar-value">{percentage.toFixed(1)}%</span>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Actions */}
          <div className="form-actions">
            <Button type="button" variant="ghost" onClick={onCancel}>
              Cancel
            </Button>
            <Button type="submit" variant="primary" loading={isLoading}>
              {initialData?.name ? 'Update Budget' : 'Create Budget'}
            </Button>
          </div>
        </form>
      </CardBody>
    </Card>
  );
}
