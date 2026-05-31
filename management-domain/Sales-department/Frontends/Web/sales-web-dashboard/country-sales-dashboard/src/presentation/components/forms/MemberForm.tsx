// MemberForm Component
// Add team member form

import React, { useState } from 'react';
import { Modal } from '../common';
import { SALES_ROLES } from '@shared';
import type { TeamMember, CountrySalesRole } from '@domain/types';

export interface MemberFormProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (member: Partial<TeamMember>) => Promise<void>;
  member?: TeamMember;
  availableUsers: Array<{ id: string; name: string; email: string; role: CountrySalesRole }>;
  isLoading?: boolean;
}

export const MemberForm: React.FC<MemberFormProps> = ({
  isOpen,
  onClose,
  onSubmit,
  member,
  availableUsers,
  isLoading = false,
}) => {
  const isEditing = !!member;
  const [selectedUserId, setSelectedUserId] = useState(member?.userId || '');
  const [formData, setFormData] = useState({
    role: member?.role || 'SALES_REPRESENTATIVE' as CountrySalesRole,
    individualQuota: member?.individualQuota || 0,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const selectedUser = availableUsers.find(u => u.id === selectedUserId);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleUserSelect = (userId: string) => {
    setSelectedUserId(userId);
    if (errors.userId) {
      setErrors(prev => ({ ...prev, userId: '' }));
    }
  };

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!selectedUserId) newErrors.userId = 'Please select a team member';
    if (formData.individualQuota <= 0) newErrors.individualQuota = 'Quota must be greater than 0';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    const user = availableUsers.find(u => u.id === selectedUserId);

    await onSubmit({
      userId: selectedUserId,
      name: user?.name || '',
      email: user?.email || '',
      role: formData.role,
      individualQuota: formData.individualQuota,
      revenueGenerated: member?.revenueGenerated || 0,
      attainment: member?.attainment || 0,
      joinedAt: member?.joinedAt || new Date(),
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={isEditing ? 'Edit Team Member' : 'Add Team Member'}
      size="md"
    >
      <form onSubmit={handleSubmit} className="member-form">
        <div className="form-grid">
          <div className="form-group full-width">
            <label>Select Team Member *</label>
            <div className="user-selection-list">
              {availableUsers.map(user => (
                <label
                  key={user.id}
                  className={`user-selection-item ${selectedUserId === user.id ? 'selected' : ''}`}
                >
                  <input
                    type="radio"
                    name="userId"
                    value={user.id}
                    checked={selectedUserId === user.id}
                    onChange={() => handleUserSelect(user.id)}
                    disabled={isLoading}
                  />
                  <div className="user-selection-info">
                    <div className="user-selection-name">{user.name}</div>
                    <div className="user-selection-email">{user.email}</div>
                    <div className="user-selection-role">{SALES_ROLES[user.role]?.label}</div>
                  </div>
                </label>
              ))}
              {availableUsers.length === 0 && (
                <div className="user-selection-empty">
                  No available users to add. Users must be assigned to a role first.
                </div>
              )}
            </div>
            {errors.userId && <span className="form-error">{errors.userId}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="role">Role in Team *</label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="form-input"
              disabled={isLoading}
            >
              {Object.entries(SALES_ROLES).map(([key, val]) => (
                <option key={key} value={key}>{val.label}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="individualQuota">Individual Quota (Monthly) *</label>
            <div className="input-with-prefix">
              <span className="input-prefix">\u20A6</span>
              <input
                type="number"
                id="individualQuota"
                name="individualQuota"
                value={formData.individualQuota}
                onChange={handleChange}
                className="form-input"
                placeholder="1000000"
                min="0"
                disabled={isLoading}
              />
            </div>
            {errors.individualQuota && <span className="form-error">{errors.individualQuota}</span>}
          </div>

          {selectedUser && (
            <div className="form-group full-width">
              <div className="member-preview">
                <h5>Member Summary</h5>
                <div className="preview-grid">
                  <div>
                    <span className="preview-label">Name:</span>
                    <span>{selectedUser.name}</span>
                  </div>
                  <div>
                    <span className="preview-label">Email:</span>
                    <span>{selectedUser.email}</span>
                  </div>
                  <div>
                    <span className="preview-label">Current Role:</span>
                    <span>{SALES_ROLES[selectedUser.role]?.label}</span>
                  </div>
                  <div>
                    <span className="preview-label">Quota:</span>
                    <span>\u20A6{formData.individualQuota.toLocaleString()}</span>
                  </div>
                </div>
              </div>
            </div>
          )}
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
            disabled={isLoading || !selectedUserId}
          >
            {isLoading ? 'Saving...' : isEditing ? 'Update Member' : 'Add Member'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default MemberForm;
