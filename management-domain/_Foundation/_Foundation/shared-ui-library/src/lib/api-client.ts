import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'

// API configuration
const API_CONFIG = {
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
}

// Create axios instance
const axiosInstance: AxiosInstance = axios.create(API_CONFIG)

// Request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    // Get auth token from localStorage (set by zustand persist)
    const authStorage = localStorage.getItem('gogidix-auth-storage')
    if (authStorage) {
      try {
        const authData = JSON.parse(authStorage)
        // Note: In a real app, you'd get a JWT token from the login response
        // For mock purposes, we're not using actual tokens
        if (authData.state?.isAuthenticated) {
          config.headers.Authorization = `Bearer mock-token-${authData.state?.user?.id}`
        }
      } catch (e) {
        // Ignore parse errors
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor
axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error) => {
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response

      switch (status) {
        case 401:
          // Unauthorized - redirect to login
          window.location.href = '/login'
          break
        case 403:
          // Forbidden
          console.error('Access forbidden:', data?.message || 'You do not have permission')
          break
        case 404:
          // Not found
          console.error('Resource not found:', data?.message || 'The requested resource was not found')
          break
        case 500:
          // Server error
          console.error('Server error:', data?.message || 'An unexpected error occurred')
          break
        default:
          console.error('API error:', data?.message || error.message)
      }

      return Promise.reject({
        message: data?.message || error.message,
        code: data?.code || 'API_ERROR',
        statusCode: status,
        details: data,
      })
    }

    if (error.request) {
      // Request was made but no response received
      console.error('Network error: No response received')
      return Promise.reject({
        message: 'Network error. Please check your connection.',
        code: 'NETWORK_ERROR',
        statusCode: 0,
      })
    }

    // Something else happened
    console.error('Error:', error.message)
    return Promise.reject({
      message: error.message || 'An unexpected error occurred',
      code: 'UNKNOWN_ERROR',
      statusCode: 0,
    })
  }
)

// API client methods
export const apiClient = {
  get: <T>(url: string, config?: AxiosRequestConfig): Promise<AxiosResponse<T>> =>
    axiosInstance.get<T>(url, config),

  post: <T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse<T>> => axiosInstance.post<T>(url, data, config),

  put: <T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse<T>> => axiosInstance.put<T>(url, data, config),

  patch: <T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<AxiosResponse<T>> => axiosInstance.patch<T>(url, data, config),

  delete: <T>(url: string, config?: AxiosRequestConfig): Promise<AxiosResponse<T>> =>
    axiosInstance.delete<T>(url, config),
}

export default axiosInstance

// Type definitions for API responses
export interface PaginatedResponse<T> {
  data: T[]
  pagination: {
    page: number
    limit: number
    total: number
    totalPages: number
    hasNext: boolean
    hasPrev: boolean
  }
}

export interface ApiError {
  message: string
  code: string
  statusCode: number
  details?: unknown
}
