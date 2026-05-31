import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})

export interface DashboardStats {
  totalTransactions: number
  activeOnboardings: number
  openAlerts: number
  completedToday: number
}

export interface Alert {
  id: string
  transactionId?: string
  alertType: string
  severity: 'INFO' | 'WARNING' | 'ERROR' | 'CRITICAL'
  title: string
  description?: string
  status: 'OPEN' | 'ACKNOWLEDGED' | 'RESOLVED' | 'SUPPRESSED'
  createdAt: string
}

export interface Metric {
  id: string
  transactionId?: string
  metricType: string
  metricName: string
  metricValue: number
  metricUnit?: string
  severity: 'NORMAL' | 'WARNING' | 'CRITICAL'
  timestamp: string
}

export interface OnboardingTracker {
  id: string
  merchantId: string
  currentStage: string
  status: 'INITIATED' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED' | 'CANCELLED'
  startedAt: string
  completedAt?: string
}

export interface ProgressStep {
  id: string
  transactionId: string
  stepOrder: number
  stepName: string
  stepType: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED' | 'SKIPPED' | 'CANCELLED' | 'RETRYING'
  startedAt?: string
  completedAt?: string
  durationMilliseconds?: number
}

export interface AuditLog {
  id: string
  transactionId: string
  action: string
  actorType: string
  actorId: string
  severity: string
  status: string
  timestamp: string
}

// API Functions - Using proxy for development
export const apiClient = {
  // Dashboard
  async getDashboardStats(): Promise<DashboardStats> {
    // Fallback to mock data if services are not available
    try {
      const [onboardingRes, alertsRes] = await Promise.allSettled([
        axios.get('/onboarding/stats'),
        axios.get('/monitoring/dashboard'),
      ])

      const totalOnboardings = onboardingRes.status === 'fulfilled' ? onboardingRes.value.data.totalOnboardings || 0 : 0
      const activeOnboardings = onboardingRes.status === 'fulfilled' ? onboardingRes.value.data.activeOnboardings || 0 : 0
      const completedToday = onboardingRes.status === 'fulfilled' ? onboardingRes.value.data.completedToday || 0 : 0
      const openAlerts = alertsRes.status === 'fulfilled' ? alertsRes.value.data.openAlerts || 0 : 0

      return {
        totalTransactions: totalOnboardings,
        activeOnboardings,
        openAlerts,
        completedToday,
      }
    } catch {
      // Return mock data for development
      return {
        totalTransactions: 12,
        activeOnboardings: 5,
        openAlerts: 2,
        completedToday: 7,
      }
    }
  },

  // Monitoring Service
  async getOpenAlerts(): Promise<Alert[]> {
    try {
      const res = await api.get('/monitoring/alerts/open')
      return res.data
    } catch {
      return []
    }
  },

  async getCriticalAlerts(): Promise<Alert[]> {
    try {
      const res = await api.get('/monitoring/alerts/critical')
      return res.data
    } catch {
      return []
    }
  },

  async getDashboardMetrics(): Promise<{ openAlerts: number; criticalAlerts: number; warningAlerts: number; recentAlerts: number }> {
    try {
      const res = await api.get('/monitoring/dashboard')
      return res.data
    } catch {
      return {
        openAlerts: 0,
        criticalAlerts: 0,
        warningAlerts: 0,
        recentAlerts: 0,
      }
    }
  },

  async acknowledgeAlert(alertId: string, acknowledgedBy: string): Promise<Alert> {
    const res = await api.put(`/monitoring/alerts/${alertId}/acknowledge`, null, {
      params: { acknowledgedBy }
    })
    return res.data
  },

  async resolveAlert(alertId: string, resolvedBy: string, resolutionNotes?: string): Promise<Alert> {
    const res = await api.put(`/monitoring/alerts/${alertId}/resolve`, null, {
      params: { resolvedBy, resolutionNotes }
    })
    return res.data
  },

  async createAlert(alert: Omit<Alert, 'id' | 'createdAt'>): Promise<Alert> {
    const res = await api.post('/monitoring/alerts', alert)
    return res.data
  },

  async recordMetric(metric: { transactionId: string; metricType: string; metricName: string; metricValue: number; metricUnit?: string }): Promise<Metric> {
    const res = await api.post('/monitoring/metrics', metric)
    return res.data
  },

  async getTransactionMetrics(transactionId: string): Promise<Metric[]> {
    try {
      const res = await api.get(`/monitoring/metrics/transaction/${transactionId}`)
      return res.data
    } catch {
      return []
    }
  },

  // Onboarding Service
  async getOnboardingTrackers(): Promise<OnboardingTracker[]> {
    try {
      const res = await api.get('/onboarding')
      return res.data
    } catch {
      return []
    }
  },

  async getOnboardingTracker(id: string): Promise<OnboardingTracker> {
    const res = await api.get(`/onboarding/${id}`)
    return res.data
  },

  async createOnboarding(onboarding: Omit<OnboardingTracker, 'id' | 'startedAt' | 'completedAt'>): Promise<OnboardingTracker> {
    const res = await api.post('/onboarding', onboarding)
    return res.data
  },

  async transitionStage(id: string, targetStage: string): Promise<OnboardingTracker> {
    const res = await api.post(`/onboarding/${id}/transition`, { targetStage })
    return res.data
  },

  // Progress Step Service
  async getProgressSteps(transactionId: string): Promise<ProgressStep[]> {
    try {
      const res = await api.get(`/progress-steps/transaction/${transactionId}`)
      return res.data
    } catch {
      return []
    }
  },

  async getExecutionSummary(transactionId: string): Promise<{
    totalSteps: number
    completedSteps: number
    failedSteps: number
    inProgressSteps: number
    pendingSteps: number
    skippedSteps: number
    progressPercentage: number
    isComplete: boolean
  }> {
    try {
      const res = await api.get(`/progress-steps/transaction/${transactionId}/summary`)
      return res.data
    } catch {
      return {
        totalSteps: 0,
        completedSteps: 0,
        failedSteps: 0,
        inProgressSteps: 0,
        pendingSteps: 0,
        skippedSteps: 0,
        progressPercentage: 0,
        isComplete: false,
      }
    }
  },

  async createProgressStep(step: Omit<ProgressStep, 'id' | 'startedAt' | 'completedAt' | 'durationMilliseconds'>): Promise<ProgressStep> {
    const res = await api.post('/progress-steps', step)
    return res.data
  },

  async startStep(stepId: string): Promise<ProgressStep> {
    const res = await api.post(`/progress-steps/${stepId}/start`)
    return res.data
  },

  async completeStep(stepId: string, outputData: Record<string, unknown>): Promise<ProgressStep> {
    const res = await api.post(`/progress-steps/${stepId}/complete`, outputData)
    return res.data
  },

  async failStep(stepId: string, errorMessage: string): Promise<ProgressStep> {
    const res = await api.post(`/progress-steps/${stepId}/fail`, null, {
      params: { errorMessage }
    })
    return res.data
  },

  // Audit Trail Service
  async getAuditLogs(transactionId?: string): Promise<AuditLog[]> {
    try {
      const url = transactionId ? `/audit/transaction/${transactionId}` : '/audit'
      const res = await api.get(url)
      return res.data
    } catch {
      return []
    }
  },

  // Status Broadcast Service
  async getBroadcastStats(): Promise<{ activeSubscribers: number; totalSubscribers: number }> {
    try {
      const res = await api.get('/status-broadcast/stats')
      return res.data
    } catch {
      return { activeSubscribers: 0, totalSubscribers: 0 }
    }
  },

  async broadcastStatus(transactionId: string, status: string, data: Record<string, unknown>): Promise<void> {
    await api.post('/status-broadcast/broadcast', {
      transactionId,
      status,
      data,
    })
  },
}

export default api
