import { useQuery } from '@tanstack/react-query'
import { UserCheck, Clock, CheckCircle, TrendingUp } from 'lucide-react'
import { format } from 'date-fns'
import { apiClient, type OnboardingTracker, type ProgressStep } from '@/lib/api'
import { useState } from 'react'

export default function Onboarding() {
  const [selectedId, setSelectedId] = useState<string | null>(null)

  const { data: onboardings, isLoading: isLoadingOnboardings } = useQuery<OnboardingTracker[]>({
    queryKey: ['onboardings'],
    queryFn: () => apiClient.getOnboardingTrackers(),
    refetchInterval: 30000,
  })

  const { data: steps } = useQuery<ProgressStep[]>({
    queryKey: ['progress-steps', selectedId],
    queryFn: () => apiClient.getProgressSteps(selectedId || ''),
    enabled: !!selectedId,
  })

  const { data: summary } = useQuery({
    queryKey: ['execution-summary', selectedId],
    queryFn: () => apiClient.getExecutionSummary(selectedId || ''),
    enabled: !!selectedId,
  })

  const getStageProgress = (stage: string) => {
    if (!steps) return 0
    const stageSteps = steps.filter(s => s.stepType === stage)
    if (stageSteps.length === 0) return 0
    const completed = stageSteps.filter(s => s.status === 'COMPLETED').length
    return Math.round((completed / stageSteps.length) * 100)
  }

  const getStatusColor = (status: OnboardingTracker['status']) => {
    switch (status) {
      case 'COMPLETED':
        return 'bg-green-100 text-green-800'
      case 'FAILED':
        return 'bg-red-100 text-red-800'
      case 'CANCELLED':
        return 'bg-gray-100 text-gray-800'
      case 'IN_PROGRESS':
        return 'bg-blue-100 text-blue-800'
      case 'INITIATED':
      default:
        return 'bg-yellow-100 text-yellow-800'
    }
  }

  const getStepStatusColor = (status: ProgressStep['status']) => {
    switch (status) {
      case 'COMPLETED':
        return 'text-green-600 bg-green-50'
      case 'FAILED':
        return 'text-red-600 bg-red-50'
      case 'IN_PROGRESS':
        return 'text-blue-600 bg-blue-50'
      case 'RETRYING':
        return 'text-yellow-600 bg-yellow-50'
      case 'SKIPPED':
        return 'text-gray-600 bg-gray-50'
      case 'CANCELLED':
        return 'text-gray-500 bg-gray-100'
      case 'PENDING':
      default:
        return 'text-gray-400 bg-gray-50'
    }
  }

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-900">Onboarding Tracker</h1>
      <p className="mt-2 text-gray-600">Track merchant and user onboarding progress</p>

      <div className="grid grid-cols-1 gap-6 mt-8 lg:grid-cols-4">
        <div className="card">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-blue-500">
              <UserCheck className="w-6 h-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Total Onboardings</p>
              <p className="text-2xl font-bold text-gray-900">{onboardings?.length || 0}</p>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-yellow-500">
              <Clock className="w-6 h-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">In Progress</p>
              <p className="text-2xl font-bold text-gray-900">
                {onboardings?.filter(o => o.status === 'IN_PROGRESS').length || 0}
              </p>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-green-500">
              <CheckCircle className="w-6 h-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Completed</p>
              <p className="text-2xl font-bold text-gray-900">
                {onboardings?.filter(o => o.status === 'COMPLETED').length || 0}
              </p>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-purple-500">
              <TrendingUp className="w-6 h-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Success Rate</p>
              <p className="text-2xl font-bold text-gray-900">
                {onboardings && onboardings.length > 0
                  ? Math.round((onboardings.filter(o => o.status === 'COMPLETED').length / onboardings.length) * 100) + '%'
                  : '0%'}
              </p>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 mt-8 lg:grid-cols-3">
        {/* Onboarding List */}
        <div className="card lg:col-span-2">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold">Active Onboardings</h2>
          </div>
          <div className="space-y-3">
            {isLoadingOnboardings ? (
              <div className="flex items-center justify-center h-32">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
              </div>
            ) : onboardings && onboardings.length > 0 ? (
              onboardings
                .filter(o => o.status === 'IN_PROGRESS' || o.status === 'INITIATED')
                .slice(0, 10)
                .map((item) => (
                  <div
                    key={item.id}
                    className="p-4 border border-gray-200 rounded cursor-pointer hover:border-primary-300 transition-colors"
                    onClick={() => setSelectedId(selectedId === item.id ? null : item.id)}
                  >
                    <div className="flex items-center justify-between mb-2">
                      <div>
                        <p className="font-medium">{item.merchantId}</p>
                        <p className="text-sm text-gray-500">
                          Stage: {item.currentStage} • Started {format(new Date(item.startedAt), 'PPp')}
                        </p>
                      </div>
                      <div className="flex items-center gap-2">
                        <span className={`px-2 py-1 text-xs font-medium rounded ${getStatusColor(item.status)}`}>
                          {item.status}
                        </span>
                      </div>
                    </div>
                    {summary && summary.totalSteps > 0 && selectedId === item.id && (
                      <div className="mt-3">
                        <div className="flex items-center justify-between mb-1">
                          <span className="text-sm text-gray-600">Progress</span>
                          <span className="text-sm font-medium">{summary.progressPercentage}%</span>
                        </div>
                        <div className="w-full bg-gray-200 rounded-full h-2">
                          <div
                            className="bg-primary-600 h-2 rounded-full transition-all"
                            style={{ width: `${summary.progressPercentage}%` }}
                          />
                        </div>
                        <div className="grid grid-cols-4 gap-2 mt-3 text-xs text-gray-500">
                          <div>{summary.completedSteps} completed</div>
                          <div>{summary.inProgressSteps} in progress</div>
                          <div>{summary.pendingSteps} pending</div>
                          <div>{summary.failedSteps} failed</div>
                        </div>
                      </div>
                    )}
                  </div>
                ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <UserCheck className="w-12 h-12 mx-auto mb-2 text-gray-400" />
                <p>No active onboarding processes</p>
              </div>
            )}
          </div>
        </div>

        {/* Progress Steps */}
        <div className="card">
          <h2 className="text-lg font-semibold mb-4">
            {selectedId ? 'Progress Steps' : 'Select an Onboarding'}
          </h2>
          <div className="space-y-3 max-h-96 overflow-y-auto">
            {steps && steps.length > 0 ? (
              steps.map((step) => (
                <div key={step.id} className={`p-3 rounded border ${getStepStatusColor(step.status)}`}>
                  <div className="flex items-center justify-between mb-1">
                    <div>
                      <p className="text-sm font-medium">{step.stepName}</p>
                      <p className="text-xs text-gray-500">Order: {step.stepOrder} • {step.stepType}</p>
                    </div>
                    <span className="text-xs px-2 py-1 rounded bg-white bg-opacity-50">
                      {step.status}
                    </span>
                  </div>
                  {step.durationMilliseconds && (
                    <p className="text-xs text-gray-500">
                      Duration: {Math.round(step.durationMilliseconds / 1000)}s
                    </p>
                  )}
                  {step.errorMessage && (
                    <p className="text-xs text-red-600 mt-1">{step.errorMessage}</p>
                  )}
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <p className="text-sm">Select an onboarding to view progress steps</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
