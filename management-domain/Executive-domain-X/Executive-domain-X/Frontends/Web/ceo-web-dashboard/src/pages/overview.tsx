import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import {
  StrategicKPIOverview,
  KPITrendsChart,
  BusinessDomainPerformanceChart,
  RegionalRevenueChart,
} from '@features/ceo/components'

/**
 * CEO Overview Page
 * Using mock data by default to prevent loading/timeout issues
 */

export default function OverviewPage() {
  const navigate = useNavigate()
  const [dateRange, setDateRange] = useState<'today' | 'week' | 'month' | 'quarter'>('quarter')
  const [dismissedInsights, setDismissedInsights] = useState<Set<string>>(new Set())
  const [dismissedAlerts, setDismissedAlerts] = useState<Set<string>>(new Set())

  const handleKPIClick = (kpiId: string) => {
    navigate(`/analytics/${kpiId}`)
  }

  const handleDomainClick = (domain: string) => {
    navigate(`/domain/${domain.toLowerCase()}`)
  }

  const handleRegionClick = (regionId: string) => {
    navigate(`/regions/${regionId}`)
  }

  const handleApprove = (id: string) => {
    console.log('Approved:', id)
  }

  const handleReject = (id: string) => {
    console.log('Rejected:', id)
  }

  const handleReview = (id: string) => {
    navigate(`/approvals/${id}`)
  }

  const handleAskAI = () => {
    console.log('AI feature coming soon')
  }

  return (
    <div className="space-y-5">
        {/* Page Header */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Executive Overview</h1>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
              Strategic health and performance metrics across all domains
            </p>
          </div>

          {/* Dashboard Switcher */}
          <div className="flex items-center gap-2">
            <Button
              size="sm"
              variant="default"
              className="bg-[#0D47A1] hover:bg-[#0D3366]"
              onClick={() => navigate('/')}
            >
              CEO
            </Button>
            <Button
              size="sm"
              variant="outline"
              onClick={() => navigate('/cfo')}
            >
              CFO
            </Button>
            <Button
              size="sm"
              variant="outline"
              disabled
              className="opacity-50"
            >
              COO
            </Button>
            <Button
              size="sm"
              variant="outline"
              disabled
              className="opacity-50"
            >
              CTO
            </Button>
          </div>
        </div>

        {/* Strategic KPI Overview - Use mock data for fast loading */}
        <StrategicKPIOverview onKPIClick={handleKPIClick} useMockData={true} />

        {/* KPI Trends Chart - 6-month trend visualization */}
        <KPITrendsChart />

        {/* Two Column Layout - Business Domain Performance & Regional Revenue */}
        <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
          <BusinessDomainPerformanceChart />
          <RegionalRevenueChart />
        </div>

        {/* Two Column Layout - Simple content instead of complex charts */}
        <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
          {/* Pending Approvals - Simplified with inline data */}
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-lg font-bold">Pending Approvals</h2>
                  <p className="text-sm text-slate-500">Items requiring your attention</p>
                </div>
                <Badge variant="destructive">5</Badge>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {[
                  { type: 'Budget', title: 'Q3 Marketing Budget', dept: 'Digital Marketing', priority: 'high', amount: '$2.5M' },
                  { type: 'Hiring', title: 'Senior Engineers', dept: 'System Admin', priority: 'medium', amount: '-' },
                  { type: 'Initiative', title: 'AI Integration', dept: 'Executive', priority: 'urgent', amount: '$5M' },
                  { type: 'Compliance', title: 'Q2 Report Review', dept: 'Legal', priority: 'high', amount: '-' },
                ].map((item, i) => (
                  <div key={i} className="flex items-center justify-between p-3 border rounded-lg hover:bg-slate-50">
                    <div className="flex-1">
                      <Badge variant="outline" className="mb-1">{item.type}</Badge>
                      <p className="font-medium">{item.title}</p>
                      <p className="text-xs text-slate-500">{item.dept}</p>
                    </div>
                    <div className="text-right">
                      <p className="font-medium">{item.amount}</p>
                      <Badge variant={item.priority === 'urgent' ? 'destructive' : item.priority === 'high' ? 'warning' : 'secondary'}>
                        {item.priority}
                      </Badge>
                    </div>
                  </div>
                ))}
              </div>
              <Button variant="outline" className="mt-4 w-full" onClick={() => navigate('/approvals')}>
                View All Approvals
              </Button>
            </CardContent>
          </Card>

          {/* Recent Alerts - Simplified Crisis Center */}
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-lg font-bold">Recent Alerts</h2>
                  <p className="text-sm text-slate-500">System notifications</p>
                </div>
                <Badge variant="destructive">3</Badge>
              </div>
            </CardHeader>
            <CardContent className="space-y-3">
              {[
                { type: 'critical', title: 'European Revenue Decline', desc: '12% below Q2 projections', time: '2h ago' },
                { type: 'warning', title: 'API Latency High', desc: 'Response time increased by 200ms', time: '4h ago' },
                { type: 'info', title: 'Growth Milestone', desc: 'Market share increased to 15.2%', time: '6h ago' },
              ].map((item, i) => (
                <div key={i} className="flex gap-3 p-3 border rounded-lg">
                  <div className={`mt-1 h-2 w-2 rounded-full flex-shrink-0 ${
                    item.type === 'critical' ? 'bg-red-500' :
                    item.type === 'warning' ? 'bg-yellow-500' : 'bg-blue-500'
                  }`} />
                  <div className="flex-1 min-w-0">
                    <p className="font-medium text-sm truncate">{item.title}</p>
                    <p className="text-xs text-slate-500">{item.desc}</p>
                  </div>
                  <span className="text-xs text-slate-400 flex-shrink-0">{item.time}</span>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>

        {/* Strategic Health Score - Premium Card */}
        <Card className="overflow-hidden border-2 border-[#FFA000]/20 shadow-xl">
          <div className="bg-gradient-to-r from-[#0D47A1] via-[#1565C0] to-[#0A3D6E] p-4 sm:p-6">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
              <div className="flex-1">
                <div className="flex items-center gap-3 mb-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-white/20">
                    <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                  </div>
                  <div>
                    <p className="text-sm text-white/80 font-medium">Overall Strategic Health</p>
                    <div className="flex items-baseline gap-2 mt-1">
                      <span className="text-3xl sm:text-4xl font-bold text-white">87</span>
                      <span className="text-base sm:text-lg text-white/60">/100</span>
                      <span className="ml-2 inline-flex px-2 sm:px-3 py-1 rounded-full bg-emerald-500/20 border border-emerald-400/30 text-emerald-300 text-xs sm:text-sm font-medium">
                        On Track
                      </span>
                    </div>
                  </div>
                </div>
                <p className="text-xs sm:text-sm text-white/70 max-w-xl mt-2 sm:mt-3">
                  All domains performing within acceptable parameters. European region requires attention due to revenue decline.
                </p>
              </div>

              {/* Animated Donut Chart */}
              <div className="flex justify-center">
                <svg width="140" height="140" viewBox="0 0 160 160" className="transform -rotate-90">
                  <circle cx="80" cy="80" r="70" fill="none" stroke="rgba(255,255,255,0.15)" strokeWidth="14" />
                  <circle cx="80" cy="80" r="70" fill="none" stroke="url(#gradient)" strokeWidth="14" strokeDasharray={`${87 * 4.4} 440`} strokeLinecap="round" className="animate-[progress_2s_ease-out]" />
                  <text x="80" y="80" textAnchor="middle" dominantBaseline="middle" className="fill-white text-xl sm:text-2xl font-bold" style={{ transform: 'rotate(90deg)', transformOrigin: 'center' }}>
                    87%
                  </text>
                  <defs>
                    <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="0%">
                      <stop offset="0%" stopColor="#FFA000" />
                      <stop offset="100%" stopColor="#FFD700" />
                    </linearGradient>
                  </defs>
                </svg>
              </div>
            </div>
          </div>
        </Card>
      </div>
  )
}
