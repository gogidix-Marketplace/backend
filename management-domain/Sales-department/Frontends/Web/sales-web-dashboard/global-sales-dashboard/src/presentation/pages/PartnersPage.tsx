// Sales Team Partners Page
// Main partners overview page with navigation to sub-pages

import { Outlet, Routes, Route, NavLink, useLocation } from 'react-router-dom';
import { usePartnersStore } from '@infrastructure';
import { MetricCard } from '../components/common/MetricCard';
import { Card, CardHeader, CardBody } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { PARTNER_SUB_ITEMS, PARTNER_TYPES, mockPartnersSummary } from '@shared';
import './PartnersPage.css';

export function PartnersPage() {
  const location = useLocation();
  const isOverview = location.pathname === '/partners' || location.pathname === '/partners/';
  const { summary, isLoading } = usePartnersStore();

  const mockData = mockPartnersSummary;

  return (
    <div className="partners-page">
      <Routes>
        <Route path="/" element={<PartnersOverview />} />
        <Route path="/applications/*" element={<PartnerApplications />} />
        <Route path="/performance/*" element={<PartnerPerformance />} />
        <Route path="/commission/*" element={<PartnerCommission />} />
        <Route path="/territory/*" element={<PartnerTerritory />} />
        <Route path="/analytics/*" element={<PartnerAnalytics />} />
      </Routes>
    </div>
  );
}

function PartnersOverview() {
  const summary = mockPartnersSummary;

  return (
    <div className="partners-overview">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>Sales Team Partners</h1>
          <p className="page-subtitle">Global partner management and performance overview</p>
        </div>
        <div className="page-actions">
          <Button variant="primary">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="12" y1="5" x2="12" y2="19" />
              <line x1="5" y1="12" x2="19" y2="12" />
            </svg>
            Add Partner
          </Button>
        </div>
      </div>

      {/* Sub-navigation */}
      <nav className="sub-nav">
        {PARTNER_SUB_ITEMS.map((item) => (
          <NavLink
            key={item.id}
            to={item.path}
            className={({ isActive }) => `sub-nav-link ${isActive ? 'sub-nav-link-active' : ''}`}
          >
            {item.label}
          </NavLink>
        ))}
      </nav>

      {/* Summary Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Total Active Partners"
            value={summary.totalPartners}
            change={12}
            changeType="increase"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                <circle cx="9" cy="7" r="4" />
                <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
                <path d="M16 3.13a4 4 0 0 1 0 7.75" />
              </svg>
            }
          />
          <MetricCard
            title="Pending Applications"
            value={summary.pendingApplications}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                <polyline points="14 2 14 15 7 15 7 5" />
                <line x1="7" y1="15" x2="21" y2="15" />
                <line x1="11" y1="18" x2="15" y2="18" />
              </svg>
            }
          />
          <MetricCard
            title="This Month Commissions"
            value={'₦' + (summary.commissionStats.totalPaid / 1000000).toFixed(1) + 'M'}
            change={15}
            changeType="increase"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Commission Paid Out"
            value={'₦' + (summary.commissionStats.totalPaid / 1000000).toFixed(1) + 'M'}
            change={8}
            changeType="increase"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <rect x="2" y="5" width="20" height="14" rx="2" />
                <line x1="2" y1="10" x2="22" y2="10" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Partners by Type */}
      <section className="partners-by-type">
        <Card>
          <CardHeader>
            <h3>Partners by Type</h3>
          </CardHeader>
          <CardBody>
            <div className="partner-types-grid">
              {summary.byPartnerType.map((type) => (
                <div key={type.partnerType} className="partner-type-card">
                  <div className="type-icon">{PARTNER_TYPES[type.partnerType]?.icon}</div>
                  <div className="type-info">
                    <h4 className="type-name">{PARTNER_TYPES[type.partnerType]?.label}</h4>
                    <div className="type-stats">
                      <span className="type-count">{type.count} partners</span>
                      <span className="type-revenue">{(type.revenueGenerated / 1000000).toFixed(1)}M revenue</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Partners by Country */}
      <section className="partners-by-country">
        <Card>
          <CardHeader>
            <h3>Partners by Country</h3>
          </CardHeader>
          <CardBody>
            <div className="countries-table">
              <table>
                <thead>
                  <tr>
                    <th>Country</th>
                    <th>Partners</th>
                    <th>Active</th>
                    <th>Pipeline</th>
                    <th>Commission</th>
                  </tr>
                </thead>
                <tbody>
                  {summary.byCountry.map((item) => (
                    <tr key={item.country.code}>
                      <td>
                        <span className="country-cell">
                          {item.country.flag} {item.country.name}
                        </span>
                      </td>
                      <td>{item.totalPartners}</td>
                      <td>{item.activePartners}</td>
                      <td>{(item.pipelineValue / 1000000).toFixed(1)}M</td>
                      <td>{(item.commissionPaid / 1000000).toFixed(1)}M</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Tier Distribution */}
      <section className="tier-distribution">
        <Card>
          <CardHeader>
            <h3>Tier Distribution</h3>
          </CardHeader>
          <CardBody>
            <div className="tier-stats">
              {summary.byTier.map((tier) => (
                <div key={tier.tier} className="tier-stat">
                  <div className="tier-info">
                    <h4 className="tier-name">{tier.tier}</h4>
                    <span className="tier-count">{tier.partnersAtTier} partners</span>
                  </div>
                  <div className="tier-bar">
                    <div
                      className={`tier-bar-fill tier-${tier.tier.toLowerCase()}`}
                      style={{ width: `${(tier.partnersAtTier / summary.totalPartners) * 100}%` }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>
    </div>
  );
}

function PartnerApplications() {
  return (
    <div className="partner-applications">
      <div className="page-header">
        <h1>Partner Applications</h1>
        <Button variant="primary">View Pending ({mockPartnersSummary.pendingApplications})</Button>
      </div>

      <Card>
        <CardHeader>
          <h3>Recent Applications</h3>
        </CardHeader>
        <CardBody>
          <p className="text-muted">Pending applications awaiting review...</p>
        </CardBody>
      </Card>
    </div>
  );
}

function PartnerPerformance() {
  return (
    <div className="partner-performance">
      <div className="page-header">
        <h1>Partner Performance</h1>
      </div>
      <Card>
        <CardBody>
          <p className="text-muted">Performance metrics and rankings...</p>
        </CardBody>
      </Card>
    </div>
  );
}

function PartnerCommission() {
  return (
    <div className="partner-commission">
      <div className="page-header">
        <h1>Commission Management</h1>
      </div>
      <Card>
        <CardBody>
          <p className="text-muted">Commission tracking and payout management...</p>
        </CardBody>
      </Card>
    </div>
  );
}

function PartnerTerritory() {
  return (
    <div className="partner-territory">
      <div className="page-header">
        <h1>Territory Allocation</h1>
      </div>
      <Card>
        <CardBody>
          <p className="text-muted">Territory assignment and lead allocation...</p>
        </CardBody>
      </Card>
    </div>
  );
}

function PartnerAnalytics() {
  return (
    <div className="partner-analytics">
      <div className="page-header">
        <h1>Partner Analytics</h1>
      </div>
      <Card>
        <CardBody>
          <p className="text-muted">Acquisition metrics and revenue contribution...</p>
        </CardBody>
      </Card>
    </div>
  );
}
