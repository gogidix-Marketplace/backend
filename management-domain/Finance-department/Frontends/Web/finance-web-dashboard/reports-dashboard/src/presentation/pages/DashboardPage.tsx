import { useNavigate } from 'react-router-dom'
import {
  FileText,
  CalendarClock,
  LayoutTemplate,
  Download,
  TrendingUp,
  ArrowRight,
  Clock,
  Wand2,
} from 'lucide-react'
import { useReportsStore } from '@shared/store/reportsStore'
import { cn } from '@shared/utils/cn'

export default function DashboardPage() {
  const { reportsHistory, scheduledReports, templates } = useReportsStore()
  const navigate = useNavigate()

  const completedReports = reportsHistory.filter((r) => r.status === 'Completed')
  const activeSchedules = scheduledReports.filter((s) => s.status === 'Active')

  const kpis = [
    {
      label: 'Reports Generated',
      value: reportsHistory.length.toString(),
      change: '+3 this week',
      icon: FileText,
      color: 'bg-amber-50 text-amber-600',
    },
    {
      label: 'Scheduled Active',
      value: activeSchedules.length.toString(),
      change: `of ${scheduledReports.length} total`,
      icon: CalendarClock,
      color: 'bg-orange-50 text-orange-600',
    },
    {
      label: 'Templates Available',
      value: templates.length.toString(),
      change: '2 custom',
      icon: LayoutTemplate,
      color: 'bg-yellow-50 text-yellow-700',
    },
    {
      label: 'Downloads This Month',
      value: '24',
      change: '+8 from last month',
      icon: Download,
      color: 'bg-lime-50 text-lime-600',
    },
  ]

  const recentReports = reportsHistory.slice(0, 5)
  const upcomingSchedules = scheduledReports
    .filter((s) => s.status === 'Active')
    .slice(0, 4)

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Reports Dashboard</h1>
          <p className="text-gray-500 mt-1">Overview of report generation and management</p>
        </div>
        <div className="flex gap-3">
          <button
            onClick={() => navigate('/generate')}
            className="flex items-center gap-2 px-4 py-2.5 bg-amber-600 hover:bg-amber-700 text-white rounded-lg font-medium transition-colors"
          >
            <Wand2 size={18} />
            Generate Report
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {kpis.map((kpi) => (
          <div key={kpi.label} className="bg-white rounded-xl border border-gray-200 p-5">
            <div className="flex items-start justify-between">
              <div>
                <p className="text-sm text-gray-500">{kpi.label}</p>
                <p className="text-2xl font-bold text-gray-900 mt-1">{kpi.value}</p>
                <p className="text-xs text-gray-400 mt-1">{kpi.change}</p>
              </div>
              <div className={cn('p-2.5 rounded-lg', kpi.color)}>
                <kpi.icon size={20} />
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 bg-white rounded-xl border border-gray-200">
          <div className="flex items-center justify-between px-5 py-4 border-b border-gray-100">
            <h2 className="font-semibold text-gray-900">Recent Reports</h2>
            <button
              onClick={() => navigate('/history')}
              className="text-sm text-amber-600 hover:text-amber-700 font-medium flex items-center gap-1"
            >
              View All <ArrowRight size={14} />
            </button>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Name</th>
                  <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Type</th>
                  <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Date</th>
                  <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Status</th>
                </tr>
              </thead>
              <tbody>
                {recentReports.map((report) => (
                  <tr key={report.id} className="border-b border-gray-50 hover:bg-gray-50">
                    <td className="px-5 py-3">
                      <div className="flex items-center gap-2">
                        <FileText size={16} className="text-amber-500" />
                        <span className="text-sm font-medium text-gray-900">{report.name}</span>
                      </div>
                    </td>
                    <td className="px-5 py-3 text-sm text-gray-600">{report.type}</td>
                    <td className="px-5 py-3 text-sm text-gray-500">
                      {new Date(report.generatedDate).toLocaleDateString()}
                    </td>
                    <td className="px-5 py-3">
                      <span
                        className={cn(
                          'inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium',
                          report.status === 'Completed'
                            ? 'bg-green-50 text-green-700'
                            : report.status === 'Processing'
                            ? 'bg-amber-50 text-amber-700'
                            : 'bg-red-50 text-red-700'
                        )}
                      >
                        {report.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200">
          <div className="flex items-center justify-between px-5 py-4 border-b border-gray-100">
            <h2 className="font-semibold text-gray-900">Upcoming Scheduled</h2>
            <button
              onClick={() => navigate('/scheduled')}
              className="text-sm text-amber-600 hover:text-amber-700 font-medium"
            >
              View All
            </button>
          </div>
          <div className="p-4 space-y-3">
            {upcomingSchedules.map((schedule) => (
              <div key={schedule.id} className="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50">
                <div className="p-2 bg-amber-50 rounded-lg">
                  <CalendarClock size={16} className="text-amber-600" />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-gray-900 truncate">{schedule.reportName}</p>
                  <div className="flex items-center gap-2 mt-1">
                    <Clock size={12} className="text-gray-400" />
                    <span className="text-xs text-gray-500">
                      {new Date(schedule.nextRun).toLocaleDateString()}
                    </span>
                    <span className="text-xs text-gray-400">•</span>
                    <span className="text-xs text-gray-500">{schedule.format}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-5">
        <h2 className="font-semibold text-gray-900 mb-4">Quick Generate</h2>
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
          {templates.slice(0, 4).map((template) => (
            <button
              key={template.id}
              onClick={() => navigate('/generate')}
              className="flex items-center gap-2 p-3 rounded-lg border border-gray-200 hover:border-amber-400 hover:bg-amber-50 transition-colors text-left"
            >
              <TrendingUp size={16} className="text-amber-600" />
              <span className="text-sm font-medium text-gray-700">{template.name}</span>
            </button>
          ))}
        </div>
      </div>
    </div>
  )
}
