// ============================================
// DIGITAL MARKETING - DASHBOARD PAGE
// ============================================

import { useEffect } from 'react'
import { useMarketingStore } from '@shared/store'
import { formatNumber, formatCurrency, cn } from '@shared/utils'
import {
  TrendingUp,
  TrendingDown,
  Users,
  Eye,
  MousePointer,
  DollarSign,
  Mail,
  Share2,
  BarChart3,
  Calendar,
} from 'lucide-react'

export function MarketingDashboardPage() {
  const { campaigns, loadCampaigns } = useMarketingStore()

  useEffect(() => {
    loadCampaigns()
  }, [loadCampaigns])

  const totalBudget = campaigns.reduce((sum, c) => sum + c.budget, 0)
  const totalSpent = campaigns.reduce((sum, c) => sum + c.spent, 0)
  const totalClicks = campaigns.reduce((sum, c) => sum + c.clicks, 0)
  const totalImpressions = campaigns.reduce((sum, c) => sum + c.impressions, 0)
  const totalConversions = campaigns.reduce((sum, c) => sum + c.conversions, 0)
  const avgCTR = campaigns.length > 0
    ? campaigns.reduce((sum, c) => sum + c.ctr, 0) / campaigns.length
    : 0

  const kpis = [
    {
      title: 'Total Budget',
      value: formatCurrency(totalBudget),
      change: '+12%',
      trend: 'up',
      icon: DollarSign,
      color: 'emerald',
    },
    {
      title: 'Total Spent',
      value: formatCurrency(totalSpent),
      change: '+8%',
      trend: 'up',
      icon: TrendingUp,
      color: 'blue',
    },
    {
      title: 'Impressions',
      value: formatNumber(totalImpressions),
      change: '+23%',
      trend: 'up',
      icon: Eye,
      color: 'purple',
    },
    {
      title: 'Clicks',
      value: formatNumber(totalClicks),
      change: '+18%',
      trend: 'up',
      icon: MousePointer,
      color: 'amber',
    },
    {
      title: 'Conversions',
      value: formatNumber(totalConversions),
      change: '+15%',
      trend: 'up',
      icon: Users,
      color: 'rose',
    },
    {
      title: 'Avg CTR',
      value: avgCTR.toFixed(1) + '%',
      change: '+2.3%',
      trend: 'up',
      icon: BarChart3,
      color: 'cyan',
    },
  ]

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Marketing Dashboard</h1>
          <p className="text-slate-500">Campaign performance and analytics overview</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium">
          <Calendar size={20} />
          This Month
        </button>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
        {kpis.map((kpi, i) => {
          const Icon = kpi.icon
          return (
            <div
              key={i}
              className="bg-white rounded-xl border border-slate-200 p-4"
            >
              <div className="flex items-center justify-between mb-2">
                <div className={cn(
                  'p-2 rounded-lg',
                  kpi.color === 'emerald' && 'bg-emerald-100 text-emerald-600',
                  kpi.color === 'blue' && 'bg-blue-100 text-blue-600',
                  kpi.color === 'purple' && 'bg-purple-100 text-purple-600',
                  kpi.color === 'amber' && 'bg-amber-100 text-amber-600',
                  kpi.color === 'rose' && 'bg-rose-100 text-rose-600',
                  kpi.color === 'cyan' && 'bg-cyan-100 text-cyan-600',
                )}>
                  <Icon size={20} />
                </div>
                {kpi.trend === 'up' ? (
                  <TrendingUp size={16} className="text-emerald-600" />
                ) : (
                  <TrendingDown size={16} className="text-red-600" />
                )}
              </div>
              <p className="text-sm text-slate-500">{kpi.title}</p>
              <p className="text-2xl font-bold text-slate-900">{kpi.value}</p>
              <p className="text-xs text-slate-500 mt-1">{kpi.change} vs last month</p>
            </div>
          )
        })}
      </div>

      {/* Campaign Performance */}
      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div className="px-6 py-4 border-b border-slate-200">
          <h2 className="text-lg font-semibold text-slate-900">Active Campaigns</h2>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-slate-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Campaign</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Type</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Budget</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Spent</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">CTR</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Conversions</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {campaigns.map((campaign) => (
                <tr key={campaign.id} className="hover:bg-slate-50">
                  <td className="px-6 py-4 font-medium text-slate-900">{campaign.name}</td>
                  <td className="px-6 py-4">
                    <span className="px-2 py-1 bg-slate-100 text-slate-700 rounded text-xs capitalize">
                      {campaign.type}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-right text-slate-600">
                    {formatCurrency(campaign.budget)}
                  </td>
                  <td className="px-6 py-4 text-right text-slate-600">
                    {formatCurrency(campaign.spent)}
                  </td>
                  <td className="px-6 py-4 text-right font-medium text-slate-900">
                    {campaign.ctr}%
                  </td>
                  <td className="px-6 py-4 text-right text-slate-600">
                    {campaign.conversions}
                  </td>
                  <td className="px-6 py-4">
                    <span className={cn(
                      'px-2 py-1 rounded-full text-xs font-medium',
                      campaign.status === 'active' && 'bg-emerald-100 text-emerald-700',
                      campaign.status === 'paused' && 'bg-amber-100 text-amber-700',
                      campaign.status === 'completed' && 'bg-slate-100 text-slate-700',
                    )}>
                      {campaign.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <button className="bg-white rounded-xl border border-slate-200 p-6 text-left hover:border-blue-300 transition-colors">
          <div className="flex items-center gap-3 mb-3">
            <div className="p-2 bg-blue-100 rounded-lg">
              <Mail size={20} className="text-blue-600" />
            </div>
            <span className="font-medium text-slate-900">Create Email Campaign</span>
          </div>
          <p className="text-sm text-slate-500">Launch a new email marketing campaign</p>
        </button>

        <button className="bg-white rounded-xl border border-slate-200 p-6 text-left hover:border-blue-300 transition-colors">
          <div className="flex items-center gap-3 mb-3">
            <div className="p-2 bg-purple-100 rounded-lg">
              <Share2 size={20} className="text-purple-600" />
            </div>
            <span className="font-medium text-slate-900">Schedule Social Post</span>
          </div>
          <p className="text-sm text-slate-500">Create and schedule social media content</p>
        </button>

        <button className="bg-white rounded-xl border border-slate-200 p-6 text-left hover:border-blue-300 transition-colors">
          <div className="flex items-center gap-3 mb-3">
            <div className="p-2 bg-emerald-100 rounded-lg">
              <BarChart3 size={20} className="text-emerald-600" />
            </div>
            <span className="font-medium text-slate-900">View Analytics</span>
          </div>
          <p className="text-sm text-slate-500">Deep dive into campaign performance</p>
        </button>
      </div>
    </div>
  )
}
