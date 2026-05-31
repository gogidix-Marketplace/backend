// ============================================
// FINANCE DEPARTMENT - API CLIENT
// ============================================

import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { useAuthStore } from '@store'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api/management/finance'
const API_TIMEOUT = 30000

// Request interceptor to add auth headers
const requestInterceptor = (config: InternalAxiosRequestConfig) => {
  const state = useAuthStore.getState()

  if (state.user) {
    config.headers = config.headers || {}
    // In production, add JWT token
    // config.headers.Authorization = `Bearer ${state.token}`
  }

  // Add tenant ID header
  // config.headers['X-Tenant-ID'] = state.tenantId

  return config
}

// Response interceptor for error handling
const responseInterceptor = (response: AxiosResponse) => {
  return response.data
}

const errorInterceptor = (error: AxiosError) => {
  if (error.response) {
    // Server responded with error status
    const status = error.response.status
    const data = error.response.data as { message?: string; error?: string }

    if (status === 401) {
      // Unauthorized - redirect to login
      useAuthStore.getState().logout()
      window.location.href = '/login'
    }

    return Promise.reject({
      status,
      message: data?.message || data?.error || 'An error occurred',
      code: data?.error || 'UNKNOWN_ERROR',
    })
  } else if (error.request) {
    // Request made but no response
    return Promise.reject({
      status: 0,
      message: 'Network error. Please check your connection.',
      code: 'NETWORK_ERROR',
    })
  } else {
    // Error setting up request
    return Promise.reject({
      status: -1,
      message: error.message || 'An unexpected error occurred',
      code: 'REQUEST_ERROR',
    })
  }
}

// Create axios instance
export const createApiClient = (baseURL: string = API_BASE_URL): AxiosInstance => {
  const client = axios.create({
    baseURL,
    timeout: API_TIMEOUT,
    headers: {
      'Content-Type': 'application/json',
    },
  })

  client.interceptors.request.use(requestInterceptor)
  client.interceptors.response.use(responseInterceptor, errorInterceptor)

  return client
}

// Default API client
export const apiClient = createApiClient()

// Mock adapter for development
export const mockAdapter = {
  async get<T>(url: string, data?: T): Promise<T> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    return data as T
  },

  async post<T>(url: string, data?: T): Promise<T> {
    await new Promise((resolve) => setTimeout(resolve, 500))
    return data as T
  },

  async put<T>(url: string, data?: T): Promise<T> {
    await new Promise((resolve) => setTimeout(resolve, 500))
    return data as T
  },

  async patch<T>(url: string, data?: T): Promise<T> {
    await new Promise((resolve) => setTimeout(resolve, 500))
    return data as T
  },

  async delete<T>(url: string): Promise<T> {
    await new Promise((resolve) => setTimeout(resolve, 500))
    return {} as T
  },
}
