// TeamForm Component
// Add/edit sales team form

import React, { useState } from 'react';
import { Modal } from '../common';
import type { SalesTeam, TeamStatus, Region } from '@domain/types';

export interface TeamFormProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (team: Partial<SalesTeam>) => Promise<void>;
  team?: SalesTeam;
  regions: Region[];
  isLoading?: boolean;
}

export const TeamForm: React.FC<TeamFormProps> = ({
  isOpen,
  onClose,
  onSubmit,
  team,
  regions,
  isLoading = false,
}) => {
  const isEditing = !!team;
  const [formData, setFormData] = useState({
    name: team?.name || '',
    code: team?.code || '',
    description: team?.description || '',
    regionId: team?.region?.id || '',
    teamLeadId: team?.teamLeadId || '',
    status: team?.status || 'active' as TeamStatus,
    quotaMonthly: team?.quota.monthly || 0,
    quotaQuarterly: team?.quota.quarterly || 0,
    quotaAnnual: team?.quota.annual || 0,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) newErrors.name = 'Team name is required';
    if (!formData.code.trim()) newErrors.code = 'Team code is required';
    if (!formData.teamLeadId) newErrors.teamLeadId = 'Team lead is required';
    if (formData.quotaMonthly <= 0) newErrors.quotaMonthly = 'Monthly quota must be greater than 0';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    const selectedRegion = regions.find(r => r.id === formData.regionId);

    await onSubmit({
      name: formData.name.trim(),
      code: formData.code.trim().toUpperCase(),
      description: formData.description.trim(),
      region: selectedRegion,
      teamLeadId: formData.teamLeadId,
      status: formData.status,
      quota: {
        monthly: formData.quotaMonthly,
        quarterly: formData.quotaQuarterly,
        annual: formData.quotaAnnual,
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={isEditing ? 'Edit Sales Team' : 'Create Sales Team'}
      size="md"
    >
      <form onSubmit={handleSubmit} className="team-form">
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="name">Team Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="form-input"
              placeholder="e.g., Lagos Mainland Team"
              disabled={isLoading}
            />
            {errors.name && <span className="form-error">{errors.name}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="code">Team Code *</label>
            <input
              type="text"
              id="code"
              name="code"
              value={formData.code}
              onChange={handleChange}
              className="form-input"
              placeholder="e.g., LAG-LM"
              disabled={isLoading}
            />
            {errors.code && <span className="form-error">{errors.code}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="regionId">Region</label>
            <select
              id="regionId"
              name="regionId"
              value={formData.regionId}
              onChange={handleChange}
              className="form-input"
              disabled={isLoading}
            >
              <option value="">Select Region</option>
              {regions.map(region => (
                <option key={region.id} value={region.id}>{region.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="teamLeadId">Team Lead *</label>
            <select
              id="teamLeadId"
              name="teamLeadId"
              value={formData.teamLeadId}
              onChange={handleChange}
              className="form-input"
              disabled={isLoading}
              required
            >
              <option value="">Select Team Lead</option>
              {/* This would be populated with actual users */}
              <option value="usr-001">Chinedu Amadi</option>
              <option value="usr-002">Adebayo Okafor</option>
              <option value="usr-003">Fatima Abdullahi</option>
            </select>
            {errors.teamLeadId && <span className="form-error">{errors.teamLeadId}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="status">Status</label>
            <select
              id="status"
              name="status"
              value={formData.status}
              onChange={handleChange}
              className="form-input"
              disabled={isLoading}
            >
              <option value="active">Active</option>
              <option value="inactive">Inactive</option>
              <option value="pending">Pending</option>
            </select>
          </div>

          <div className="form-group full-width">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              className="form-textarea"
              placeholder="Brief description of the team's focus area..."
              rows={3}
              disabled={isLoading}
            />
          </div>

          <div className="form-section-title">
            <h4>Quota Settings</h4>
          </div>

          <div className="form-group">
            <label htmlFor="quotaMonthly">Monthly Quota *</label>
            <div className="input-with-prefix">
              <span className="input-prefix">\u20A6</span>
              <input
                type="number"
                id="quotaMonthly"
                name="quotaMonthly"
                value={formData.quotaMonthly}
                onChange={handleChange}
                className="form-input"
                placeholder="0"
                min="0"
                disabled={isLoading}
              />
            </div>
            {errors.quotaMonthly && <span className="form-error">{errors.quotaMonthly}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="quotaQuarterly">Quarterly Quota</label>
            <div className="input-with-prefix">
              <span className="input-prefix">\u20A6</span>
              <input
                type="number"
                id="quotaQuarterly"
                name="quotaQuarterly"
                value={formData.quotaQuarterly}
                onChange={handleChange}
                className="form-input"
                placeholder="0"
                min="0"
                disabled={isLoading}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="quotaAnnual">Annual Quota</label>
            <div className="input-with-prefix">
              <span className="input-prefix">\u20A6</span>
              <input
                type="number"
                id="quotaAnnual"
                name="quotaAnnual"
                value={formData.quotaAnnual}
                onChange={handleChange}
                className="form-input"
                placeholder="0"
                min="0"
                disabled={isLoading}
              />
            </div>
          </div>
        </div>

        <div className="form-actions">
          <button
            type="button"
            className="btn btn-secondary"
            onClick={onClose}
            disabled={isLoading}
          >
            Cancel
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={isLoading}
          >
            {isLoading ? 'Saving...' : isEditing ? 'Update Team' : 'Create Team'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default TeamForm;
