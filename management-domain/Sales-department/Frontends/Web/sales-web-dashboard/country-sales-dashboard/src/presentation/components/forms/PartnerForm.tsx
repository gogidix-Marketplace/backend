// PartnerForm Component
// Add/edit partner form

import React, { useState } from 'react';
import { Modal } from '../common';
import { PARTNER_TYPES } from '@shared';
import type { CountryPartner, PartnerType, PartnerStatus, Territory } from '@domain/types';

export interface PartnerFormProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (partner: Partial<CountryPartner>) => Promise<void>;
  partner?: CountryPartner;
  territories: Territory[];
  isLoading?: boolean;
}

export const PartnerForm: React.FC<PartnerFormProps> = ({
  isOpen,
  onClose,
  onSubmit,
  partner,
  territories,
  isLoading = false,
}) => {
  const isEditing = !!partner;
  const [formData, setFormData] = useState({
    firstName: partner?.user?.firstName || '',
    lastName: partner?.user?.lastName || '',
    email: partner?.user?.email || '',
    phone: partner?.user?.phone || '',
    partnerType: partner?.partnerType || 'COURIER' as PartnerType,
    territoryId: partner?.assignedTerritory?.id || '',
    status: partner?.status || 'ACTIVE' as PartnerStatus,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.firstName.trim()) newErrors.firstName = 'First name is required';
    if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required';
    if (!formData.email.trim()) newErrors.email = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Invalid email address';
    }
    if (!formData.phone.trim()) newErrors.phone = 'Phone number is required';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    const selectedTerritory = territories.find(t => t.id === formData.territoryId);

    await onSubmit({
      user: {
        firstName: formData.firstName.trim(),
        lastName: formData.lastName.trim(),
        email: formData.email.trim(),
        phone: formData.phone.trim(),
      },
      partnerType: formData.partnerType,
      assignedTerritory: selectedTerritory,
      status: formData.status,
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={isEditing ? 'Edit Partner' : 'Add New Partner'}
      size="md"
    >
      <form onSubmit={handleSubmit} className="partner-form">
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="firstName">First Name *</label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              className="form-input"
              placeholder="Enter first name"
              disabled={isLoading}
            />
            {errors.firstName && <span className="form-error">{errors.firstName}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="lastName">Last Name *</label>
            <input
              type="text"
              id="lastName"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              className="form-input"
              placeholder="Enter last name"
              disabled={isLoading}
            />
            {errors.lastName && <span className="form-error">{errors.lastName}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="email">Email Address *</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className="form-input"
              placeholder="partner@example.com"
              disabled={isLoading}
            />
            {errors.email && <span className="form-error">{errors.email}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="phone">Phone Number *</label>
            <input
              type="tel"
              id="phone"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              className="form-input"
              placeholder="+234 XXX XXX XXXX"
              disabled={isLoading}
            />
            {errors.phone && <span className="form-error">{errors.phone}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="partnerType">Partner Type *</label>
            <select
              id="partnerType"
              name="partnerType"
              value={formData.partnerType}
              onChange={handleChange}
              className="form-input"
              disabled={isLoading}
            >
              {Object.entries(PARTNER_TYPES).map(([key, val]) => (
                <option key={key} value={key}>
                  {val.icon} {val.label}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="territoryId">Assigned Territory</label>
            <select
              id="territoryId"
              name="territoryId"
              value={formData.territoryId}
              onChange={handleChange}
              className="form-input"
              disabled={isLoading}
            >
              <option value="">Select Territory</option>
              {territories.map(territory => (
                <option key={territory.id} value={territory.id}>{territory.name}</option>
              ))}
            </select>
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
              <option value="ACTIVE">Active</option>
              <option value="PENDING">Pending</option>
              <option value="SUSPENDED">Suspended</option>
              <option value="TERMINATED">Terminated</option>
            </select>
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
            {isLoading ? 'Saving...' : isEditing ? 'Update Partner' : 'Add Partner'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default PartnerForm;
