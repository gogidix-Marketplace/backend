// Partner Detail Modal Component
// Displays detailed information about a sales team partner

import { useState } from 'react';
import type { SalesTeamPartner, PartnerTier, PartnerStatus } from '@domain/types';
import { formatCurrency, formatDate } from '@shared';
import { PARTNER_TYPES, PARTNER_TIERS } from '@shared';
import { Button } from '../common/Button';
import { Card, CardHeader, CardBody } from '../common/Card';
import './PartnerDetailModal.css';

export interface PartnerDetailModalProps {
  partner: SalesTeamPartner;
  onClose: () => void;
  onStatusUpdate?: (partnerId: string, status: PartnerStatus) => void;
  onTierUpdate?: (partnerId: string, tier: PartnerTier) => void;
}

export function PartnerDetailModal({
  partner,
  onClose,
  onStatusUpdate,
  onTierUpdate,
}: PartnerDetailModalProps) {
  const [activeTab, setActiveTab] = useState<'overview' | 'performance' | 'commissions' | 'leads'>('overview');
  const [isEditingStatus, setIsEditingStatus] = useState(false);
  const [isEditingTier, setIsEditingTier] = useState(false);
  const [selectedStatus, setSelectedStatus] = useState<PartnerStatus>(partner.status);
  const [selectedTier, setSelectedTier] = useState<PartnerTier>(partner.tier);

  const tabs = [
    { id: 'overview', label: 'Overview' },
    { id: 'performance', label: 'Performance' },
    { id: 'commissions', label: 'Commissions' },
    { id: 'leads', label: 'Assigned Leads' },
  ] as const;

  const statusColors: Record<PartnerStatus, string> = {
    PENDING: '#F59E0B',
    ACTIVE: '#10B981',
    SUSPENDED: '#EF4444',
    TERMINATED: '#6B7280',
  };

  const tierColors: Record<PartnerTier, string> = {
    BRONZE: '#CD7F32',
    SILVER: '#C0C0C0',
    GOLD: '#FFD700',
    PLATINUM: '#E5E4E2',
  };

  const handleStatusSave = () => {
    if (onStatusUpdate && selectedStatus !== partner.status) {
      onStatusUpdate(partner.id, selectedStatus);
    }
    setIsEditingStatus(false);
  };

  const handleTierSave = () => {
    if (onTierUpdate && selectedTier !== partner.tier) {
      onTierUpdate(partner.id, selectedTier);
    }
    setIsEditingTier(false);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content modal-content-xl" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div className="partner-modal-header">
            {partner.user.avatar && (
              <img src={partner.user.avatar} alt={partner.user.firstName} className="partner-avatar-large" />
            )}
            <div className="partner-header-info">
              <h2>
                {partner.user.firstName} {partner.user.lastName}
              </h2>
              <span className="partner-number">{partner.partnerNumber}</span>
            </div>
          </div>
          <div className="modal-header-actions">
            <div className="partner-badges">
              <span
                className="partner-badge status-badge"
                style={{ backgroundColor: statusColors[partner.status] }}
              >
                {partner.status}
              </span>
              <span
                className="partner-badge tier-badge"
                style={{ backgroundColor: tierColors[partner.tier], color: partner.tier === 'GOLD' || partner.tier === 'BRONZE' ? '#000' : '#333' }}
              >
                {partner.tier}
              </span>
              <span className="partner-badge type-badge">
                {PARTNER_TYPES[partner.partnerType]?.icon} {PARTNER_TYPES[partner.partnerType]?.label}
              </span>
            </div>
            <button className="modal-close" onClick={onClose} aria-label="Close">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                <path d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" />
              </svg>
            </button>
          </div>
        </div>

        {/* Tabs */}
        <div className="modal-tabs">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              className={`modal-tab ${activeTab === tab.id ? 'modal-tab-active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
            >
              {tab.label}
            </button>
          ))}
        </div>

        <div className="modal-body">
          {activeTab === 'overview' && (
            <div className="partner-overview-tab">
              <div className="partner-overview-grid">
                {/* Contact Information */}
                <Card>
                  <CardHeader>
                    <h3>Contact Information</h3>
                  </CardHeader>
                  <CardBody>
                    <div className="detail-row">
                      <span className="detail-label">Email</span>
                      <a href={`mailto:${partner.user.email}`} className="detail-value detail-link">
                        {partner.user.email}
                      </a>
                    </div>
                    <div className="detail-row">
                      <span className="detail-label">Phone</span>
                      <a href={`tel:${partner.user.phone}`} className="detail-value detail-link">
                        {partner.user.phone}
                      </a>
                    </div>
                    <div className="detail-row">
                      <span className="detail-label">Partner Type</span>
                      <span className="detail-value">
                        {PARTNER_TYPES[partner.partnerType]?.label}
                      </span>
                    </div>
                    <div className="detail-row">
                      <span className="detail-label">Assigned Country</span>
                      <span className="detail-value">
                        {partner.assignedCountry.flag} {partner.assignedCountry.name}
                      </span>
                    </div>
                    {partner.territory && (
                      <div className="detail-row">
                        <span className="detail-label">Territory</span>
                        <span className="detail-value">{partner.territory.name}</span>
                      </div>
                    )}
                  </CardBody>
                </Card>

                {/* Status & Tier Management */}
                <Card>
                  <CardHeader>
                    <h3>Status Management</h3>
                  </CardHeader>
                  <CardBody>
                    <div className="status-management">
                      <div className="status-field">
                        <span className="detail-label">Status</span>
                        {isEditingStatus ? (
                          <div className="status-edit">
                            <select
                              value={selectedStatus}
                              onChange={(e) => setSelectedStatus(e.target.value as PartnerStatus)}
                              className="status-select"
                            >
                              <option value="PENDING">Pending</option>
                              <option value="ACTIVE">Active</option>
                              <option value="SUSPENDED">Suspended</option>
                              <option value="TERMINATED">Terminated</option>
                            </select>
                            <div className="status-edit-actions">
                              <Button variant="primary" size="sm" onClick={handleStatusSave}>
                                Save
                              </Button>
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => {
                                  setSelectedStatus(partner.status);
                                  setIsEditingStatus(false);
                                }}
                              >
                                Cancel
                              </Button>
                            </div>
                          </div>
                        ) : (
                          <div className="status-display">
                            <span
                              className="status-indicator"
                              style={{ backgroundColor: statusColors[partner.status] }}
                            >
                              {partner.status}
                            </span>
                            <button
                              className="edit-button"
                              onClick={() => setIsEditingStatus(true)}
                            >
                              Edit
                            </button>
                          </div>
                        )}
                      </div>

                      <div className="status-field">
                        <span className="detail-label">Tier</span>
                        {isEditingTier ? (
                          <div className="status-edit">
                            <select
                              value={selectedTier}
                              onChange={(e) => setSelectedTier(e.target.value as PartnerTier)}
                              className="status-select"
                            >
                              <option value="BRONZE">Bronze</option>
                              <option value="SILVER">Silver</option>
                              <option value="GOLD">Gold</option>
                              <option value="PLATINUM">Platinum</option>
                            </select>
                            <div className="status-edit-actions">
                              <Button variant="primary" size="sm" onClick={handleTierSave}>
                                Save
                              </Button>
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => {
                                  setSelectedTier(partner.tier);
                                  setIsEditingTier(false);
                                }}
                              >
                                Cancel
                              </Button>
                            </div>
                          </div>
                        ) : (
                          <div className="status-display">
                            <span
                              className="tier-indicator"
                              style={{ backgroundColor: tierColors[partner.tier] }}
                            >
                              {partner.tier}
                            </span>
                            <button className="edit-button" onClick={() => setIsEditingTier(true)}>
                              Edit
                            </button>
                          </div>
                        )}
                      </div>
                    </div>

                    <div className="detail-row">
                      <span className="detail-label">Joined</span>
                      <span className="detail-value">
                        {formatDate(partner.approvedAt || partner.createdAt, 'DISPLAY')}
                      </span>
                    </div>
                    <div className="detail-row">
                      <span className="detail-label">Last Activity</span>
                      <span className="detail-value">
                        {partner.lastActivityAt
                          ? formatDate(partner.lastActivityAt, 'DISPLAY')
                          : 'N/A'}
                      </span>
                    </div>
                  </CardBody>
                </Card>

                {/* Quick Stats */}
                <Card>
                  <CardHeader>
                    <h3>Quick Stats</h3>
                  </CardHeader>
                  <CardBody>
                    <div className="quick-stats">
                      <div className="quick-stat-item">
                        <span className="quick-stat-value">{partner.partnersRecruited}</span>
                        <span className="quick-stat-label">Partners Recruited</span>
                      </div>
                      <div className="quick-stat-item">
                        <span className="quick-stat-value">{partner.customersAcquired}</span>
                        <span className="quick-stat-label">Customers Acquired</span>
                      </div>
                      <div className="quick-stat-item">
                        <span className="quick-stat-value">{partner.activeDeals}</span>
                        <span className="quick-stat-label">Active Deals</span>
                      </div>
                      <div className="quick-stat-item">
                        <span className="quick-stat-value">
                          {formatCurrency(partner.pipelineValue)}
                        </span>
                        <span className="quick-stat-label">Pipeline Value</span>
                      </div>
                    </div>
                  </CardBody>
                </Card>
              </div>
            </div>
          )}

          {activeTab === 'performance' && (
            <div className="partner-performance-tab">
              <div className="performance-metrics">
                <Card className="performance-metric-card">
                  <CardBody>
                    <span className="metric-title">Total Revenue Generated</span>
                    <span className="metric-value">
                      {formatCurrency(partner.revenueGenerated)}
                    </span>
                    <span className="metric-subtitle">Lifetime earnings</span>
                  </CardBody>
                </Card>

                <Card className="performance-metric-card">
                  <CardBody>
                    <span className="metric-title">Lead Conversion Rate</span>
                    <span className="metric-value">{partner.leadConversionRate}%</span>
                    <span className="metric-subtitle">Conversion success</span>
                  </CardBody>
                </Card>

                <Card className="performance-metric-card">
                  <CardBody>
                    <span className="metric-title">Average Deal Size</span>
                    <span className="metric-value">
                      {formatCurrency(partner.avgDealSize)}
                    </span>
                    <span className="metric-subtitle">Per customer</span>
                  </CardBody>
                </Card>

                <Card className="performance-metric-card">
                  <CardBody>
                    <span className="metric-title">Tier Progress</span>
                    <span className="metric-value">
                      {formatCurrency(partner.tierProgression.currentTierRevenue)}
                    </span>
                    <span className="metric-subtitle">
                      {partner.tierProgression.nextTier
                        ? `${formatCurrency(partner.tierProgression.revenueToNextTier)} to ${partner.tierProgression.nextTier}`
                        : 'Highest tier achieved'}
                    </span>
                  </CardBody>
                </Card>
              </div>

              {/* Tier Progression Bar */}
              <Card>
                <CardHeader>
                  <h3>Tier Progression</h3>
                </CardHeader>
                <CardBody>
                  <div className="tier-progression">
                    {(['BRONZE', 'SILVER', 'GOLD', 'PLATINUM'] as PartnerTier[]).map((tier, index) => {
                      const threshold = PARTNER_TIERS[tier].threshold;
                      const current = partner.tierProgression.currentTierRevenue;
                      const isUnlocked = current >= threshold;
                      const isCurrent = tier === partner.tier;
                      const isNext = tier === partner.tierProgression.nextTier;

                      return (
                        <div key={tier} className="tier-progression-item">
                          <div
                            className={`tier-step ${isUnlocked ? 'unlocked' : ''} ${isCurrent ? 'current' : ''} ${isNext ? 'next' : ''}`}
                            style={{ backgroundColor: isUnlocked ? tierColors[tier] : '#E5E7EB' }}
                          >
                            <span className="tier-icon">
                              {isUnlocked ? '\u2713' : index + 1}
                            </span>
                          </div>
                          <span className="tier-label">{tier}</span>
                          {index < 3 && (
                            <div className={`tier-connector ${isUnlocked ? 'unlocked' : ''}`} />
                          )}
                        </div>
                      );
                    })}
                  </div>
                  {partner.tierProgression.nextTier && (
                    <div className="next-tier-info">
                      <span>
                        {formatCurrency(partner.tierProgression.revenueToNextTier)} more to reach{' '}
                        {partner.tierProgression.nextTier}
                      </span>
                      {partner.tierProgression.estimatedTimeToNextTier && (
                        <span>~{partner.tierProgression.estimatedTimeToNextTier} days estimated</span>
                      )}
                    </div>
                  )}
                </CardBody>
              </Card>
            </div>
          )}

          {activeTab === 'commissions' && (
            <div className="partner-commissions-tab">
              <div className="commissions-summary">
                <Card>
                  <CardHeader>
                    <h3>Commission Summary</h3>
                  </CardHeader>
                  <CardBody>
                    <div className="commission-breakdown">
                      <div className="commission-item">
                        <span className="commission-label">Sales Commission</span>
                        <span className="commission-value">
                          {formatCurrency(partner.commissionEarned)}
                        </span>
                      </div>
                      <div className="commission-item">
                        <span className="commission-label">Referral Bonus</span>
                        <span className="commission-value">
                          {formatCurrency(partner.referralBonusEarned)}
                        </span>
                      </div>
                      <div className="commission-item total">
                        <span className="commission-label">Total Earned</span>
                        <span className="commission-value">
                          {formatCurrency(partner.commissionEarned + partner.referralBonusEarned)}
                        </span>
                      </div>
                    </div>
                  </CardBody>
                </Card>
              </div>
            </div>
          )}

          {activeTab === 'leads' && (
            <div className="partner-leads-tab">
              <Card>
                <CardHeader>
                  <h3>Assigned Leads</h3>
                </CardHeader>
                <CardBody>
                  {partner.assignedLeads && partner.assignedLeads.length > 0 ? (
                    <div className="leads-list">
                      {partner.assignedLeads.map((lead) => (
                        <div key={lead.id} className="lead-item">
                          <div className="lead-info">
                            <span className="lead-company">{lead.lead.companyName}</span>
                            <span className="lead-contact">{lead.lead.contact}</span>
                          </div>
                          <div className="lead-details">
                            <span className="lead-value">
                              {formatCurrency(lead.lead.value)}
                            </span>
                            <span className="lead-territory">{lead.lead.territory}</span>
                          </div>
                          <div className={`lead-status lead-status-${lead.status.toLowerCase()}`}>
                            {lead.status}
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div className="empty-state">
                      <p>No leads assigned to this partner yet.</p>
                    </div>
                  )}
                </CardBody>
              </Card>
            </div>
          )}
        </div>

        <div className="modal-footer">
          <Button variant="secondary" onClick={onClose}>
            Close
          </Button>
          <Button variant="primary">
            View Full Profile
          </Button>
        </div>
      </div>
    </div>
  );
}
