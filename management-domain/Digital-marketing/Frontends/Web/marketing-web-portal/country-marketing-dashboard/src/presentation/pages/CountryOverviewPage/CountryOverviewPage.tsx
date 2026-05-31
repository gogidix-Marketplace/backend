import { useEffect, useState } from 'react'
import { MetricCard } from '../../components/common/MetricCard'
import { Card, CardHeader, CardBody } from '../../components/common/Card'
import './CountryOverviewPage.css'

interface LocalMetrics {
  budget: { allocated: number; spent: number; remaining: number; utilization: number }
  campaigns: { active: number; total: number; thisMonth: number }
  leads: { total: number; qualified: number; conversionRate: number; thisMonth: number }
  revenue: { total: number; target: number; attainment: number; change: number }
  social: { followers: number; engagement: number; posts: number }
  email: { sent: number; openRate: number; clickRate: number }
}

export function CountryOverviewPage() {
  const [metrics, setMetrics] = useState<LocalMetrics | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Simulate data fetch
    setTimeout(() => {
      setMetrics({
        budget: {
          allocated: 150000,
          spent: 97500,
          remaining: 52500,
          utilization: 65
        },
        campaigns: {
          active: 8,
          total: 15,
          thisMonth: 3
        },
        leads: {
          total: 2450,
          qualified: 850,
          conversionRate: 18.5,
          thisMonth: 520
        },
        revenue: {
          total: 425000,
          target: 500000,
          attainment: 85,
          change: 12
        },
        social: {
          followers: 45200,
          engagement: 4.2,
          posts: 48
        },
        email: {
          sent: 125000,
          openRate: 24.5,
          clickRate: 3.8
        }
      })
      setIsLoading(false)
    }, 500)
  }, [])

  if (isLoading) {
    return (
      <div className="loading-state">
        <div className="loading-spinner"></div>
        <p>Loading dashboard...</p>
      </div>
    )
  }

  if (!metrics) return null

  return (
    <div className="country-overview-page">
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1>United States Dashboard</h1>
          <p className="page-subtitle">
            Marketing performance overview for February 2024
          </p>
        </div>
        <div className="page-actions">
          <button className="btn btn-secondary">
            \u{1F4CB} Report
          </button>
          <button className="btn btn-primary">
            + New Campaign
          </button>
        </div>
      </div>

      {/* Key Metrics */}
      <section className="metrics-section">
        <div className="metrics-grid">
          <MetricCard
            title="Budget Utilization"
            value={`${metrics.budget.utilization}%`}
            change={metrics.budget.utilization}
            changeType="increase"
            target="$150K"
            attainment={metrics.budget.utilization}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
          <MetricCard
            title="Active Campaigns"
            value={metrics.campaigns.active.toString()}
            change={12}
            changeType="increase"
            unit="active"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M12 2L2 7l10 5 10-5-10-5z" />
                <path d="M2 17l10 5 10-5" />
                <path d="M2 12l10 5 10-5" />
              </svg>
            }
          />
          <MetricCard
            title="Leads Generated"
            value={metrics.leads.total.toString()}
            change={18}
            changeType="increase"
            unit="total"
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                <circle cx="8.5" cy="7" r="4" />
                <path d="M20 8v6M23 11h-6" />
              </svg>
            }
          />
          <MetricCard
            title="Revenue"
            value="$425K"
            change={12}
            changeType="increase"
            target="$500K"
            attainment={85}
            icon={
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            }
          />
        </div>
      </section>

      {/* Charts Row */}
      <section className="charts-row">
        <Card className="chart-card flex-1">
          <CardHeader>
            <h3>Lead Generation Trend</h3>
          </CardHeader>
          <CardBody>
            <div className="chart-placeholder">
              <svg viewBox="0 0 400 200" className="chart-svg">
                <polyline
                  fill="none"
                  stroke="#6366f1"
                  strokeWidth="2"
                  points="0,150 50,130 100,140 150,100 200,80 250,90 300,60 350,40 400,50"
                />
                <polyline
                  fill="none"
                  stroke="#10b981"
                  strokeWidth="2"
                  strokeDasharray="5,5"
                  points="0,160 50,150 100,155 150,120 200,100 250,110 300,80 350,60 400,70"
                />
              </svg>
            </div>
            <div className="chart-legend">
              <span className="legend-item">
                <span className="legend-dot" style={{ backgroundColor: '#6366f1' }}></span>
                Leads
              </span>
              <span className="legend-item">
                <span className="legend-dot" style={{ backgroundColor: '#10b981' }}></span>
                Target
              </span>
            </div>
          </CardBody>
        </Card>

        <Card className="chart-card flex-1">
          <CardHeader>
            <h3>Budget by Channel</h3>
          </CardHeader>
          <CardBody>
            <div className="budget-breakdown">
              {[
                { channel: 'Social Media', amount: 42000, percentage: 43 },
                { channel: 'Email', amount: 28000, percentage: 29 },
                { channel: 'Search', amount: 18000, percentage: 18 },
                { channel: 'Display', amount: 9500, percentage: 10 },
              ].map((item, index) => (
                <div key={index} className="budget-item">
                  <div className="budget-item-header">
                    <span className="budget-channel">{item.channel}</span>
                    <span className="budget-amount">${item.amount.toLocaleString()}</span>
                  </div>
                  <div className="budget-bar">
                    <div
                      className="budget-fill"
                      style={{ width: `${item.percentage}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>

      {/* Campaign & Social */}
      <section className="bottom-row">
        <Card className="campaigns-card">
          <CardHeader>
            <h3>Active Campaigns</h3>
          </CardHeader>
          <CardBody>
            <div className="campaigns-list">
              {[
                { name: 'Q1 Brand Awareness', status: 'active', spent: 45000, budget: 50000, roi: 185 },
                { name: 'Product Launch Email', status: 'active', spent: 12000, budget: 15000, roi: 320 },
                { name: 'Retargeting Q1', status: 'active', spent: 28000, budget: 35000, roi: 210 },
              ].map((campaign, index) => (
                <div key={index} className="campaign-item">
                  <div className="campaign-info">
                    <h4 className="campaign-name">{campaign.name}</h4>
                    <div className="campaign-stats">
                      <span>${campaign.spent.toLocaleString()} / ${campaign.budget.toLocaleString()}</span>
                      <span className="campaign-roi">{campaign.roi}% ROI</span>
                    </div>
                  </div>
                  <div className="campaign-progress">
                    <div className="progress-bar">
                      <div
                        className="progress-fill"
                        style={{ width: `${(campaign.spent / campaign.budget) * 100}%` }}
                      />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>

        <Card className="social-card">
          <CardHeader>
            <h3>Social Media Summary</h3>
          </CardHeader>
          <CardBody>
            <div className="social-metrics">
              <div className="social-metric">
                <span className="metric-label">Followers</span>
                <span className="metric-value">{metrics.social.followers.toLocaleString()}</span>
              </div>
              <div className="social-metric">
                <span className="metric-label">Engagement Rate</span>
                <span className="metric-value">{metrics.social.engagement}%</span>
              </div>
              <div className="social-metric">
                <span className="metric-label">Posts This Month</span>
                <span className="metric-value">{metrics.social.posts}</span>
              </div>
            </div>
            <div className="platform-breakdown">
              {[
                { platform: 'LinkedIn', icon: '\u{1F4BC}', followers: '22.5K' },
                { platform: 'Twitter', icon: '\u{1F426}', followers: '12.8K' },
                { platform: 'Facebook', icon: '\u{1F3C6}', followers: '8.2K' },
                { platform: 'Instagram', icon: '\u{1F3CF}', followers: '1.7K' },
              ].map((item, index) => (
                <div key={index} className="platform-item">
                  <span className="platform-icon">{item.icon}</span>
                  <span className="platform-name">{item.platform}</span>
                  <span className="platform-followers">{item.followers}</span>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </section>
    </div>
  )
}
