/**
 * Axios HTTP Client with Authentication and Interceptors
 *
 * Features:
 * - Automatic JWT token injection
 * - Token refresh handling
 * - Request/response logging
 * - Error handling
 * - Retry logic for failed requests
 * - Tenant context injection
 */

import axios, {
  AxiosInstance,
  AxiosError,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios'
import { useAuthStore } from '@shared/stores/authStore'
import { API_CONFIG } from './api-config'

/**
 * API Error types
 */
export class ApiError extends Error {
  constructor(
    public status: number,
    public code: string,
    message: string,
    public details?: any
  ) {
    super(message)
    this.name = 'ApiError'
  }
}

/**
 * Request queue for token refresh
 */
let isRefreshing = false
let failedQueue: Array<{
  resolve: (value?: any) => void
  reject: (reason?: any) => void
}> = []

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error)
    } else {
      prom.resolve(token)
    }
  })
  failedQueue = []
}

/**
 * Create base Axios instance
 */
function createAxiosInstance(baseURL: string, timeout = API_CONFIG.timeout.default): AxiosInstance {
  const instance = axios.create({
    baseURL,
    timeout,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
  })

  // Request interceptor
  instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      const authStore = useAuthStore()

      // Add authorization header if token exists
      if (authStore.user && authStore.isAuthenticated) {
        // In real implementation, get JWT token from authStore
        // For now, using a placeholder
        config.headers.Authorization = `Bearer ${getAuthToken()}`
      }

      // Add tenant context
      const tenantId = getTenantId()
      if (tenantId) {
        config.headers['X-Tenant-ID'] = tenantId
      }

      // Add correlation ID for tracing
      config.headers['X-Correlation-ID'] = generateCorrelationId()

      // Log request in development
      if (import.meta.env.DEV) {
        console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`, {
          params: config.params,
          data: config.data,
        })
      }

      return config
    },
    (error: AxiosError) => {
      return Promise.reject(error)
    }
  )

  // Response interceptor
  instance.interceptors.response.use(
    (response: AxiosResponse) => {
      // Log response in development
      if (import.meta.env.DEV) {
        console.log(`[API Response] ${response.status} ${response.config.url}`, response.data)
      }

      return response
    },
    async (error: AxiosError) => {
      const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

      // Log error in development
      if (import.meta.env.DEV) {
        console.error(`[API Error] ${error.response?.status || 'Network'} ${error.config?.url}`, error)
      }

      // Handle 401 Unauthorized - try to refresh token
      if (error.response?.status === 401 && !originalRequest._retry) {
        if (isRefreshing) {
          // If already refreshing, queue the request
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject })
          })
            .then((token) => {
              if (originalRequest.headers) {
                originalRequest.headers.Authorization = `Bearer ${token}`
              }
              return axios(originalRequest)
            })
            .catch((err) => {
              return Promise.reject(err)
            })
        }

        originalRequest._retry = true
        isRefreshing = true

        try {
          // Attempt to refresh token
          const newToken = await refreshToken()

          // Update auth store with new token
          const authStore = useAuthStore()
          // authStore.setToken(newToken) // To be implemented

          processQueue(null, newToken)

          if (originalRequest.headers) {
            originalRequest.headers.Authorization = `Bearer ${newToken}`
          }

          return axios(originalRequest)
        } catch (refreshError) {
          processQueue(refreshError, null)

          // Refresh failed - logout user
          const authStore = useAuthStore()
          authStore.logout()

          // Redirect to login
          window.location.href = '/login'

          return Promise.reject(refreshError)
        } finally {
          isRefreshing = false
        }
      }

      // Handle other errors
      const apiError = handleApiError(error)
      return Promise.reject(apiError)
    }
  )

  return instance
}

/**
 * Get auth token from storage
 */
function getAuthToken(): string {
  // In real implementation, get JWT from authStore or localStorage
  const authStore = useAuthStore()
  // For now, return a placeholder
  return localStorage.getItem('auth_token') || 'placeholder-token'
}

/**
 * Get tenant ID
 */
function getTenantId(): string | null {
  // Get tenant from auth store or context
  const authStore = useAuthStore()
  return (authStore.user as any)?.tenantId || localStorage.getItem('tenant_id') || null
}

/**
 * Generate correlation ID for request tracing
 */
function generateCorrelationId(): string {
  return `corr-${Date.now()}-${Math.random().toString(36).substring(2, 15)}`
}

/**
 * Refresh auth token
 */
async function refreshToken(): Promise<string> {
  try {
    // Call auth refresh endpoint
    const response = await axios.post(`${API_CONFIG.foundation.auth}/refresh`, {
      refreshToken: localStorage.getItem('refresh_token'),
    })

    const newToken = response.data.accessToken
    localStorage.setItem('auth_token', newToken)

    return newToken
  } catch (error) {
    throw new Error('Token refresh failed')
  }
}

/**
 * Handle API errors and convert to ApiError
 */
function handleApiError(error: AxiosError): ApiError {
  if (error.response) {
    const { status, data } = error.response

    // Extract error details from response
    const errorCode = (data as any)?.code || 'API_ERROR'
    const errorMessage = (data as any)?.message || data?.error || 'An unexpected error occurred'
    const details = (data as any)?.details || (data as any)?.validationErrors

    return new ApiError(status, errorCode, errorMessage, details)
  }

  if (error.request) {
    // Request made but no response
    return new ApiError(0, 'NETWORK_ERROR', 'Network error. Please check your connection.')
  }

  // Error setting up request
  return new ApiError(0, 'REQUEST_ERROR', error.message || 'Failed to make request')
}

/**
 * Pre-configured Axios instances for different services
 */
export const apiClient = createAxiosInstance(API_CONFIG.gatewayUrl)

export const executiveApiClient = createAxiosInstance(API_CONFIG.executive.analytics)

export const executiveApprovalApiClient = createAxiosInstance(API_CONFIG.executive.approval)

export const executiveReportApiClient = createAxiosInstance(API_CONFIG.executive.report)

export const aggregationApiClient = createAxiosInstance(API_CONFIG.aggregation.dataAggregation)

export const metricsAggregationApiClient = createAxiosInstance(API_CONFIG.aggregation.metricsAggregation)

export const centralizedDataAggregationApiClient = createAxiosInstance(API_CONFIG.aggregation.centralizedData)

export const aiApiClient = createAxiosInstance(API_CONFIG.ai.baseUrl)

export const businessApiClient = createAxiosInstance(API_CONFIG.business.ecommerce.baseUrl)

export const analyticsDashboardApiClient = createAxiosInstance(API_CONFIG.ai.dataAnalytics.analyticsDashboard)

/**
 * Helper function to make GET request
 */
export async function get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
  const response = await apiClient.get<T>(url, config)
  return response.data
}

/**
 * Helper function to make POST request
 */
export async function post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  const response = await apiClient.post<T>(url, data, config)
  return response.data
}

/**
 * Helper function to make PUT request
 */
export async function put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  const response = await apiClient.put<T>(url, data, config)
  return response.data
}

/**
 * Helper function to make PATCH request
 */
export async function patch<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  const response = await apiClient.patch<T>(url, data, config)
  return response.data
}

/**
 * Helper function to make DELETE request
 */
export async function del<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
  const response = await apiClient.delete<T>(url, config)
  return response.data
}

/**
 * Helper function to upload file
 */
export async function upload<T>(
  url: string,
  file: File,
  onProgress?: (progress: number) => void
): Promise<T> {
  const formData = new FormData()
  formData.append('file', file)

  const response = await apiClient.post<T>(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    timeout: API_CONFIG.timeout.upload,
    onUploadProgress: (progressEvent) => {
      if (onProgress && progressEvent.total) {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        onProgress(progress)
      }
    },
  })

  return response.data
}

/**
 * Helper function to download file
 */
export async function download(
  url: string,
  filename: string,
  config?: AxiosRequestConfig
): Promise<void> {
  const response = await apiClient.get(url, {
    ...config,
    responseType: 'blob',
    timeout: API_CONFIG.timeout.download,
  })

  // Create download link
  const blob = new Blob([response.data])
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = filename
  link.click()

  // Cleanup
  URL.revokeObjectURL(link.href)
}

export default apiClient
