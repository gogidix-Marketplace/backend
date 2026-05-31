// PartnersPage Component
// Page showing all partner types in country

import React, { useState } from 'react';
import { usePartnersStore } from '@infrastructure/stores';
import { mockPartners, mockPartnerApplications, mockTerritories, PARTNER_TYPES } from '@shared/mock-data';
import { PartnersTable } from '../components/tables';
import { PartnerForm } from '../components/forms';
import { SearchBar, Button, Tabs } from '../components/common';
import { PartnerDistributionChart } from '../components/charts';
import type { CountryPartner, PartnerStatus, PartnerType } from '@domain/types';

export const PartnersPage: React.FC = () => {
  const { partners, isLoading } = usePartnersStore();
  const [searchQuery, setSearchQuery] = useState('');
  const [typeFilter, setTypeFilter] = useState<PartnerType | 'ALL'>('ALL');
  const [statusFilter, setStatusFilter] = useState<PartnerStatus | 'ALL'>('ALL');
  const [activeTab, setActiveTab] = useState('active');
  const [showForm, setShowForm] = useState(false);
  const [selectedPartner, setSelectedPartner] = useState<CountryPartner | undefined>();

  const displayPartners = mockPartners;
  const displayApplications = mockPartnerApplications;

  const activePartners = displayPartners.filter(p => p.status === 'ACTIVE');
  const pendingPartners = displayPartners.filter(p => p.status === 'PENDING');

  const handleAddPartner = () => {
    setSelectedPartner(undefined);
    setShowForm(true);
  };

  const handleEditPartner = (partner: CountryPartner) => {
    setSelectedPartner(partner);
    setShowForm(true);
  };

  const handleStatusChange = async (partnerId: string, status: PartnerStatus) => {
    // Status change logic
  };

  const handleSubmitPartner = async (partnerData: Partial<CountryPartner>) => {
    // Submit logic
    setShowForm(false);
  };

  const handleApproveApplication = async (applicationId: string) => {
    // Approval logic
  };

  const handleRejectApplication = async (applicationId: string) => {
    // Reject logic
  };

  const partnerDistributionData = Object.keys(PARTNER_TYPES).map(type => ({
    type: type as PartnerType,
    count: displayPartners.filter(p => p.partnerType === type).length,
  }));

  const tabs = [
    {
      id: 'active',
      label: 'Active Partners',
      badge: activePartners.length,
      content: (
        <PartnersTable
          partners={activePartners}
          isLoading={isLoading}
          onPartnerClick={(p) => {}}
          onEdit={handleEditPartner}
          onStatusChange={handleStatusChange}
        />
      ),
    },
    {
      id: 'pending',
      label: 'Pending Review',
      badge: pendingPartners.length + displayApplications.length,
      content: (
        <div className="pending-partners">
          {/* Pending partners table */}
          <h4>Pending Applications</h4>
          {displayApplications.map(app => (
            <div key={app.id} className="application-card">
              <div className="application-info">
                <span className="applicant-name">
                  {app.applicant.firstName} {app.applicant.lastName}
                </span>
                <span className="application-type">{PARTNER_TYPES[app.partnerType].label}</span>
                <span className="application-date">
                  Applied: {new Date(app.createdAt).toLocaleDateString()}
                </span>
              </div>
              <div className="application-actions">
                <button
                  className="btn btn-sm btn-secondary"
                  onClick={() => handleRejectApplication(app.id)}
                >
                  Reject
                </button>
                <button
                  className="btn btn-sm btn-primary"
                  onClick={() => handleApproveApplication(app.id)}
                >
                  Approve
                </button>
              </div>
            </div>
          ))}
        </div>
      ),
    },
    {
      id: 'analytics',
      label: 'Analytics',
      content: (
        <div className="partners-analytics">
          <div className="analytics-grid">
            <div className="analytics-card">
              <h4>Partner Distribution</h4>
              <PartnerDistributionChart data={partnerDistributionData} size="lg" />
            </div>
            <div className="analytics-card">
              <h4>Top Performers by Type</h4>
              {Object.entries(PARTNER_TYPES).map(([type, info]) => {
                const typePartners = displayPartners.filter(p => p.partnerType === type);
                const topPerformer = typePartners.sort((a, b) => b.revenueGenerated - a.revenueGenerated)[0];
                return (
                  topPerformer && (
                    <div key={type} className="top-perner-by-type">
                      <span className="type-badge">{info.icon} {info.label}</span>
                      <span className="performer-name">
                        {topPerformer.user.firstName} {topPerformer.user.lastName}
                      </span>
                      <span className="performer-revenue">
                        {topPerformer.revenueGenerated.toLocaleString()} USD
                      </span>
                    </div>
                  )
                );
              })}
            </div>
          </div>
        </div>
      ),
    },
  ];

  return (
    <div className="page partners-page">
      <div className="page-header">
        <div className="page-title">
          <h1>Partners</h1>
          <p className="page-subtitle">Manage partner relationships</p>
        </div>
        <div className="page-actions">
          <Button variant="secondary">Import</Button>
          <Button variant="primary" onClick={handleAddPartner}>Add Partner</Button>
        </div>
      </div>

      {/* Partner Type Summary */}
      <div className="partner-types-summary">
        {Object.entries(PARTNER_TYPES).map(([type, info]) => {
          const count = displayPartners.filter(p => p.partnerType === type).length;
          return (
            <div key={type} className="type-summary-card">
              <span className="type-icon">{info.icon}</span>
              <div className="type-info">
                <span className="type-name">{info.label}</span>
                <span className="type-count">{count} partners</span>
              </div>
            </div>
          );
        })}
      </div>

      {/* Filters */}
      <div className="page-filters">
        <SearchBar
          placeholder="Search partners..."
          value={searchQuery}
          onChange={setSearchQuery}
        />
        <select
          value={typeFilter}
          onChange={(e) => setTypeFilter(e.target.value as PartnerType | 'ALL')}
          className="filter-select"
        >
          <option value="ALL">All Types</option>
          {Object.entries(PARTNER_TYPES).map(([key, val]) => (
            <option key={key} value={key}>{val.label}</option>
          ))}
        </select>
      </div>

      {/* Tabs */}
      <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} />

      {/* Partner Form Modal */}
      <PartnerForm
        isOpen={showForm}
        onClose={() => setShowForm(false)}
        onSubmit={handleSubmitPartner}
        partner={selectedPartner}
        territories={mockTerritories}
      />
    </div>
  );
};

export default PartnersPage;
