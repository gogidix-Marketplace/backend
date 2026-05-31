import { useState } from 'react'
import {
  CalendarClock,
  Plus,
  Play,
  Pause,
  Trash2,
  Eye,
  X,
  Mail,
  Clock,
  FileOutput,
} from 'lucide-react'
import { useReportsStore } from '@shared/store/reportsStore'
import { cn } from '@shared/utils/cn'

export default function ScheduledReportsPage() {
  const { scheduledReports, toggleScheduleStatus, deleteSchedule } = useReportsStore()
  const [selectedSchedule, setSelectedSchedule] = useState<string | null>(null)

  const selected = scheduledReports.find((s) => s.id === selectedSchedule)

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Scheduled Reports</h1>
          <p className="text-gray-500 mt-1">Manage automated report generation schedules</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2.5 bg-amber-600 hover:bg-amber-700 text-white rounded-lg font-medium transition-colors">
          <Plus size={18} />
          New Schedule
        </button>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="bg-gray-50 border-b border-gray-200">
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Report Name</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Schedule</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Next Run</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Recipients</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Status</th>
                <th className="text-right text-xs font-medium text-gray-500 uppercase px-5 py-3">Actions</th>
              </tr>
            </thead>
            <tbody>
              {scheduledReports.map((schedule) => (
                <tr key={schedule.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="px-5 py-4">
                    <div className="flex items-center gap-2">
                      <CalendarClock size={16} className="text-amber-500" />
                      <span className="text-sm font-medium text-gray-900">{schedule.reportName}</span>
                    </div>
                  </td>
                  <td className="px-5 py-4">
                    <span className="text-sm text-gray-600">{schedule.schedule}</span>
                  </td>
                  <td className="px-5 py-4 text-sm text-gray-500">
                    {new Date(schedule.nextRun).toLocaleDateString()}
                  </td>
                  <td className="px-5 py-4">
                    <div className="flex items-center gap-1">
                      <Mail size={14} className="text-gray-400" />
                      <span className="text-sm text-gray-600">{schedule.recipients.length} recipients</span>
                    </div>
                  </td>
                  <td className="px-5 py-4">
                    <span
                      className={cn(
                        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                        schedule.status === 'Active'
                          ? 'bg-green-50 text-green-700'
                          : schedule.status === 'Paused'
                          ? 'bg-yellow-50 text-yellow-700'
                          : 'bg-red-50 text-red-700'
                      )}
                    >
                      {schedule.status}
                    </span>
                  </td>
                  <td className="px-5 py-4">
                    <div className="flex items-center justify-end gap-1">
                      <button
                        onClick={() => setSelectedSchedule(schedule.id)}
                        className="p-1.5 text-gray-400 hover:text-amber-600 hover:bg-amber-50 rounded-lg transition-colors"
                        title="View details"
                      >
                        <Eye size={16} />
                      </button>
                      <button
                        onClick={() => toggleScheduleStatus(schedule.id)}
                        className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                        title={schedule.status === 'Active' ? 'Pause' : 'Resume'}
                      >
                        {schedule.status === 'Active' ? <Pause size={16} /> : <Play size={16} />}
                      </button>
                      <button
                        onClick={() => deleteSchedule(schedule.id)}
                        className="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                        title="Delete"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {selected && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl shadow-2xl w-full max-w-lg">
            <div className="flex items-center justify-between px-6 py-4 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-gray-900">Schedule Details</h3>
              <button
                onClick={() => setSelectedSchedule(null)}
                className="p-1 hover:bg-gray-100 rounded-lg"
              >
                <X size={20} className="text-gray-500" />
              </button>
            </div>
            <div className="px-6 py-5 space-y-4">
              <div>
                <p className="text-sm text-gray-500">Report Name</p>
                <p className="font-medium text-gray-900">{selected.reportName}</p>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-gray-500">Schedule</p>
                  <p className="font-medium text-gray-900">{selected.schedule}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500">Format</p>
                  <div className="flex items-center gap-1.5">
                    <FileOutput size={14} className="text-amber-500" />
                    <p className="font-medium text-gray-900">{selected.format}</p>
                  </div>
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-gray-500">Next Run</p>
                  <div className="flex items-center gap-1.5">
                    <Clock size={14} className="text-gray-400" />
                    <p className="font-medium text-gray-900">
                      {new Date(selected.nextRun).toLocaleString()}
                    </p>
                  </div>
                </div>
                <div>
                  <p className="text-sm text-gray-500">Last Run</p>
                  <div className="flex items-center gap-1.5">
                    <Clock size={14} className="text-gray-400" />
                    <p className="font-medium text-gray-900">
                      {new Date(selected.lastRun).toLocaleString()}
                    </p>
                  </div>
                </div>
              </div>
              <div>
                <p className="text-sm text-gray-500 mb-1">Recipients</p>
                <div className="space-y-1">
                  {selected.recipients.map((r) => (
                    <div key={r} className="flex items-center gap-2 text-sm text-gray-700">
                      <Mail size={14} className="text-gray-400" />
                      {r}
                    </div>
                  ))}
                </div>
              </div>
              <div>
                <p className="text-sm text-gray-500">Status</p>
                <span
                  className={cn(
                    'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                    selected.status === 'Active'
                      ? 'bg-green-50 text-green-700'
                      : selected.status === 'Paused'
                      ? 'bg-yellow-50 text-yellow-700'
                      : 'bg-red-50 text-red-700'
                  )}
                >
                  {selected.status}
                </span>
              </div>
            </div>
            <div className="px-6 py-4 border-t border-gray-100 flex justify-end gap-3">
              <button
                onClick={() => setSelectedSchedule(null)}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
              >
                Close
              </button>
              <button
                onClick={() => {
                  toggleScheduleStatus(selected.id)
                  setSelectedSchedule(null)
                }}
                className={cn(
                  'px-4 py-2 text-sm font-medium rounded-lg transition-colors',
                  selected.status === 'Active'
                    ? 'bg-yellow-100 text-yellow-700 hover:bg-yellow-200'
                    : 'bg-green-100 text-green-700 hover:bg-green-200'
                )}
              >
                {selected.status === 'Active' ? 'Pause Schedule' : 'Resume Schedule'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
