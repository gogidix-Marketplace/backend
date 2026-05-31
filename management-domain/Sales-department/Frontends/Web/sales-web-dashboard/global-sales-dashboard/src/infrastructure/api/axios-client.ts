// Axios HTTP Client Configuration

import axios, { AxiosError, AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { API_CONFIG, STORAGE_KEYS, storage } from '@shared';

// Create axios instance
const axiosClient: AxiosInstance = axios.create({
  baseURL: API_CONFIG.baseURL,
  timeout: API_CONFIG.timeout,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
axiosClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = storage.get<string>(STORAGE_KEYS.ACCESS_TOKEN);
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // Add region scope header if available
    const userData = storage.get<any>(STORAGE_KEYS.USER_DATA);
    if (userData?.regionScope && config.headers) {
      config.headers['X-Region-Scope'] = userData.regionScope;
    }

    // Add request ID for tracking
    if (config.headers) {
      config.headers['X-Request-ID'] = `req-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling and token refresh
axiosClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

    // Handle token expiration
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = storage.get<string>(STORAGE_KEYS.REFRESH_TOKEN);
        if (refreshToken) {
          // Try to refresh token
          const { data } = await axios.post(`${API_CONFIG.baseURL}/auth/refresh`, {
            refreshToken,
          });

          const { accessToken, expiresIn } = data;
          storage.set(STORAGE_KEYS.ACCESS_TOKEN, accessToken);

          // Update the authorization header
          if (originalRequest.headers) {
            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
          }

          // Retry the original request
          return axiosClient(originalRequest);
        }
      } catch (refreshError) {
        // Refresh failed, clear tokens and redirect to login
        storage.remove(STORAGE_KEYS.ACCESS_TOKEN);
        storage.remove(STORAGE_KEYS.REFRESH_TOKEN);
        storage.remove(STORAGE_KEYS.USER_DATA);
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    // Handle other errors
    return Promise.reject(error);
  }
);

export default axiosClient;
