import axios, { AxiosInstance, AxiosRequestConfig, AxiosError } from 'axios';
import { useAuthStore } from '@/stores/authStore';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:4000';
const API_TIMEOUT = parseInt(import.meta.env.VITE_API_TIMEOUT || '30000');

class ApiClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: `${API_URL}/api`,
      timeout: API_TIMEOUT,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  private setupInterceptors() {
    // Request interceptor
    this.client.interceptors.request.use(
      (config) => {
        const tokens = useAuthStore.getState().tokens;
        if (tokens?.accessToken) {
          config.headers.Authorization = `Bearer ${tokens.accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Response interceptor
    this.client.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        const originalRequest = error.config as AxiosRequestConfig & { _retry?: boolean };

        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;

          try {
            const tokens = useAuthStore.getState().tokens;
            if (tokens?.refreshToken) {
              const response = await axios.post(`${API_URL}/api/auth/refresh`, {
                refreshToken: tokens.refreshToken,
              });

              const { tokens: newTokens, user } = response.data;
              useAuthStore.getState().setTokens(newTokens);
              useAuthStore.getState().setUser(user);

              if (originalRequest.headers) {
                originalRequest.headers.Authorization = `Bearer ${newTokens.accessToken}`;
              }

              return this.client(originalRequest);
            }
          } catch (refreshError) {
            useAuthStore.getState().clearAuth();
            window.location.href = '/login';
            return Promise.reject(refreshError);
          }
        }

        return Promise.reject(error);
      }
    );
  }

  public async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.get<T>(url, config);
    return response.data;
  }

  public async post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.post<T>(url, data, config);
    return response.data;
  }

  public async put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.put<T>(url, data, config);
    return response.data;
  }

  public async patch<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.patch<T>(url, data, config);
    return response.data;
  }

  public async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.delete<T>(url, config);
    return response.data;
  }

  public async upload<T>(url: string, file: File, onProgress?: (progress: number) => void): Promise<T> {
    const formData = new FormData();
    formData.append('file', file);

    const response = await this.client.post<T>(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress && progressEvent.total) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          onProgress(progress);
        }
      },
    });

    return response.data;
  }
}

export const apiClient = new ApiClient();

// API Error handler
export const handleApiError = (error: any): string => {
  if (error.response?.data?.message) {
    return error.response.data.message;
  }
  if (error.message) {
    return error.message;
  }
  return 'An unexpected error occurred';
};

// Query keys factory
export const queryKeys = {
  auth: ['auth'] as const,
  users: ['users'] as const,
  content: {
    all: ['content'] as const,
    pages: () => ['content', 'pages'] as const,
    page: (id: string) => ['content', 'pages', id] as const,
    blog: () => ['content', 'blog'] as const,
    blogPost: (id: string) => ['content', 'blog', id] as const,
    press: () => ['content', 'press'] as const,
    pressRelease: (id: string) => ['content', 'press', id] as const,
    resources: () => ['content', 'resources'] as const,
    resource: (id: string) => ['content', 'resources', id] as const,
  },
  products: {
    all: ['products'] as const,
    list: () => ['products', 'list'] as const,
    product: (id: string) => ['products', id] as const,
    categories: () => ['products', 'categories'] as const,
    features: () => ['products', 'features'] as const,
    pricing: () => ['products', 'pricing'] as const,
    integrations: () => ['products', 'integrations'] as const,
  },
  developer: {
    apiDocs: () => ['developer', 'api-docs'] as const,
    apiDoc: (id: string) => ['developer', 'api-docs', id] as const,
    sdks: () => ['developer', 'sdks'] as const,
    codeExamples: () => ['developer', 'examples'] as const,
  },
  careers: {
    jobs: () => ['careers', 'jobs'] as const,
    job: (id: string) => ['careers', 'jobs', id] as const,
    applications: () => ['careers', 'applications'] as const,
    application: (id: string) => ['careers', 'applications', id] as const,
  },
  partners: {
    programs: () => ['partners', 'programs'] as const,
    partners: () => ['partners'] as const,
    applications: () => ['partners', 'applications'] as const,
  },
  leads: {
    demoRequests: () => ['leads', 'demo-requests'] as const,
    salesInquiries: () => ['leads', 'sales-inquiries'] as const,
    supportTickets: () => ['leads', 'support-tickets'] as const,
  },
  analytics: {
    overview: (period: string) => ['analytics', 'overview', period] as const,
    behavior: (period: string) => ['analytics', 'behavior', period] as const,
    funnels: (period: string) => ['analytics', 'funnels', period] as const,
    seo: (period: string) => ['analytics', 'seo', period] as const,
  },
  settings: {
    general: () => ['settings', 'general'] as const,
    users: () => ['settings', 'users'] as const,
    workflows: () => ['settings', 'workflows'] as const,
    integrations: () => ['settings', 'integrations'] as const,
  },
  media: {
    all: () => ['media'] as const,
    folder: (folder: string) => ['media', folder] as const,
  },
};
