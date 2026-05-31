import { apiClient } from './api';
import { User, AuthTokens, LoginCredentials } from '@/types';

interface LoginResponse {
  user: User;
  tokens: AuthTokens;
}

interface RefreshResponse {
  user: User;
  tokens: AuthTokens;
}

export const login = async (credentials: LoginCredentials): Promise<LoginResponse> => {
  return apiClient.post<LoginResponse>('/auth/login', credentials);
};

export const logout = async (): Promise<void> => {
  return apiClient.post<void>('/auth/logout');
};

export const refreshToken = async (refreshToken: string): Promise<RefreshResponse> => {
  return apiClient.post<RefreshResponse>('/auth/refresh', { refreshToken });
};

export const getCurrentUser = async (): Promise<{ user: User }> => {
  return apiClient.get<{ user: User }>('/auth/me');
};

export const updateProfile = async (data: Partial<User>): Promise<{ user: User }> => {
  return apiClient.put<{ user: User }>('/auth/profile', data);
};

export const changePassword = async (data: {
  currentPassword: string;
  newPassword: string;
}): Promise<void> => {
  return apiClient.post<void>('/auth/change-password', data);
};
