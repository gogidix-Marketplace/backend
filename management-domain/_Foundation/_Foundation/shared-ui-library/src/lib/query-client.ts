import { QueryClient } from '@tanstack/react-query'

// Create React Query client with default options
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // Time in milliseconds that data remains fresh
      staleTime: 5 * 60 * 1000, // 5 minutes

      // Time in milliseconds that unused data is kept in cache
      gcTime: 10 * 60 * 1000, // 10 minutes (previously cacheTime)

      // Number of times to retry failed requests
      retry: (failureCount, error: any) => {
        // Don't retry on 4xx errors (except 408, 429)
        if (error?.statusCode >= 400 && error?.statusCode < 500) {
          if (error?.statusCode === 408 || error?.statusCode === 429) {
            return failureCount < 2
          }
          return false
        }
        // Retry 5xx errors up to 3 times
        return failureCount < 3
      },

      // Delay between retries (exponential backoff)
      retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 30000),

      // Refetch on window focus (can be disabled per query)
      refetchOnWindowFocus: true,

      // Refetch on reconnect
      refetchOnReconnect: true,

      // Refetch on mount
      refetchOnMount: true,
    },
    mutations: {
      // Retry mutations
      retry: (failureCount, error: any) => {
        // Don't retry on 4xx errors
        if (error?.statusCode >= 400 && error?.statusCode < 500) {
          return false
        }
        return failureCount < 2
      },

      // Delay between retries
      retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 30000),
    },
  },
})

// Default query keys (for cache management)
export const QUERY_KEYS = {
  // Auth
  auth: ['auth'] as const,
  user: () => [...QUERY_KEYS.auth, 'user'] as const,

  // Dashboard
  dashboard: ['dashboard'] as const,
  dashboardOverview: () => [...QUERY_KEYS.dashboard, 'overview'] as const,

  // Strategy
  strategy: ['strategy'] as const,
  strategyKPIs: () => [...QUERY_KEYS.strategy, 'kpis'] as const,
  strategyGoals: () => [...QUERY_KEYS.strategy, 'goals'] as const,
  strategyInitiatives: () => [...QUERY_KEYS.strategy, 'initiatives'] as const,

  // Analytics
  analytics: ['analytics'] as const,
  analyticsCrossDomain: () => [...QUERY_KEYS.analytics, 'cross-domain'] as const,
  analyticsRegional: () => [...QUERY_KEYS.analytics, 'regional'] as const,
  analyticsTrends: () => [...QUERY_KEYS.analytics, 'trends'] as const,

  // Approvals
  approvals: ['approvals'] as const,
  approvalsPending: () => [...QUERY_KEYS.approvals, 'pending'] as const,
  approvalsHistory: () => [...QUERY_KEYS.approvals, 'history'] as const,

  // Reports
  reports: ['reports'] as const,
  reportsExecutive: () => [...QUERY_KEYS.reports, 'executive'] as const,

  // Settings
  settings: ['settings'] as const,
  settingsPreferences: () => [...QUERY_KEYS.settings, 'preferences'] as const,

  // Generic list key factory
  list: (entity: string) => [entity, 'list'] as const,
  detail: (entity: string, id: string) => [entity, 'detail', id] as const,
} as const

export type QueryKeys = typeof QUERY_KEYS
