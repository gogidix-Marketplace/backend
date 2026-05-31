'use client'

import { useQuery } from '@tanstack/react-query'
import { dashboardApi, type DashboardOverview } from '@/lib/api'
import { Sidebar } from './Sidebar'
import { Header } from './Header'
import { StatCard } from './StatCard'
import { DepartmentChart } from './DepartmentChart'
import { AlertsPanel } from './AlertsPanel'
import { RecruitmentMetrics } from './RecruitmentMetrics'
import { LeaveSummary } from './LeaveSummary'
import { PayrollSummary } from './PayrollSummary'

export function DashboardPage() {
  const { data: dashboard, isLoading } = useQuery<DashboardOverview>({
    queryKey: ['dashboard-overview'],
    queryFn: async () => {
      const response = await dashboardApi.getOverview()
      return response.data
    },
  })

  if (isLoading) {
    return (
      <div className="flex h-screen items-center justify-center">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent" />
      </div>
    )
  }

  if (!dashboard) {
    return <div>Failed to load dashboard</div>
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />

      <div className="flex flex-1 flex-col overflow-hidden">
        <Header />

        <main className="flex-1 overflow-y-auto p-6">
          {/* Page Title */}
          <div className="mb-6">
            <h1 className="text-2xl font-bold text-gray-900">Dashboard Overview</h1>
            <p className="text-gray-600">
              Welcome back! Here is your HR summary for {dashboard.employeeStats.countryCode || 'NGA'}
            </p>
          </div>

          {/* Health Score */}
          <div className="mb-6 rounded-lg bg-gradient-to-r from-blue-500 to-blue-600 p-6 text-white">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-lg font-semibold">HR Health Score</h2>
                <p className="text-blue-100">
                  Overall HR operations health indicator
                </p>
              </div>
              <div className="text-right">
                <div className="text-4xl font-bold">{dashboard.healthScore.score}/100</div>
                <div className="text-sm text-blue-100">
                  {dashboard.healthScore.level} - Trend: {dashboard.healthScore.trend > 0 ? '+' : ''}
                  {dashboard.healthScore.trend}
                </div>
              </div>
            </div>
          </div>

          {/* Quick Stats */}
          <div className="mb-6 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
            <StatCard
              title="Total Employees"
              value={dashboard.employeeStats.totalEmployees}
              change={dashboard.employeeStats.newHiresThisMonth}
              changeLabel="new this month"
              icon="users"
              color="blue"
            />
            <StatCard
              title="On Leave"
              value={dashboard.employeeStats.onLeave}
              change={dashboard.leaveStats.pendingRequests}
              changeLabel="pending requests"
              icon="calendar"
              color="yellow"
            />
            <StatCard
              title="Open Positions"
              value={dashboard.recruitmentStats.activeJobs}
              change={dashboard.recruitmentStats.totalApplicants}
              changeLabel="total applicants"
              icon="briefcase"
              color="green"
            />
            <StatCard
              title="Training Programs"
              value={dashboard.trainingStats.activePrograms}
              change={dashboard.trainingStats.completionRate}
              changeLabel="% completion rate"
              icon="book-open"
              color="purple"
              suffix="%"
            />
          </div>

          {/* Main Content Grid */}
          <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
            {/* Department Breakdown */}
            <div className="lg:col-span-2">
              <DepartmentChart data={dashboard.departmentBreakdown} />
            </div>

            {/* Alerts */}
            <div>
              <AlertsPanel alerts={dashboard.alerts} />
            </div>

            {/* Recruitment Metrics */}
            <div className="lg:col-span-2">
              <RecruitmentMetrics stats={dashboard.recruitmentStats} />
            </div>

            {/* Leave Summary */}
            <div>
              <LeaveSummary stats={dashboard.leaveStats} />
            </div>

            {/* Payroll Summary */}
            <div className="lg:col-span-2">
              <PayrollSummary summary={dashboard.payrollSummary} />
            </div>

            {/* Compliance Score */}
            <div>
              <div className="rounded-lg border bg-white p-6 shadow-sm">
                <h3 className="mb-4 text-lg font-semibold">Compliance Score</h3>
                <div className="flex items-center justify-between">
                  <div className="text-3xl font-bold text-green-600">
                    {dashboard.complianceStats.score}%
                  </div>
                  <div className="text-right text-sm">
                    <div className="text-yellow-600">
                      {dashboard.complianceStats.warnings} Warnings
                    </div>
                    <div className="text-red-600">
                      {dashboard.complianceStats.criticalIssues} Critical
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}
