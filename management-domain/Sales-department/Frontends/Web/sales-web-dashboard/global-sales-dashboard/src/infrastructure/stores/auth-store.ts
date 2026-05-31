// Auth Store - Zustand
// Manages authentication state and user session

import { create } from 'zustand';
import type { HQSalesUser, LoginRequest, RegionScope } from '@domain/types';
import { authRepository } from '../api';
import { STORAGE_KEYS, storage } from '@shared';

interface AuthState {
  // State
  user: HQSalesUser | null;
  accessToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  regionScope?: RegionScope;

  // Actions
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => Promise<void>;
  refreshToken: () => Promise<void>;
  loadUser: () => Promise<void>;
  clearError: () => void;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  // Initial state
  user: null,
  accessToken: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,
  regionScope: undefined,

  // Login action
  login: async (credentials: LoginRequest) => {
    set({ isLoading: true, error: null });

    try {
      const response = await authRepository.login(credentials);

      // Store tokens in localStorage for axios interceptor
      storage.set(STORAGE_KEYS.ACCESS_TOKEN, response.accessToken);
      storage.set(STORAGE_KEYS.REFRESH_TOKEN, response.refreshToken);
      storage.set(STORAGE_KEYS.USER_DATA, response.user);

      set({
        user: response.user,
        accessToken: response.accessToken,
        isAuthenticated: true,
        isLoading: false,
        regionScope: response.user.regionScope,
      });
    } catch (error: any) {
      set({
        error: error.response?.data?.message || 'Login failed. Please try again.',
        isLoading: false,
      });
      throw error;
    }
  },

  // Logout action
  logout: async () => {
    try {
      await authRepository.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // Clear all auth data
      storage.remove(STORAGE_KEYS.ACCESS_TOKEN);
      storage.remove(STORAGE_KEYS.REFRESH_TOKEN);
      storage.remove(STORAGE_KEYS.USER_DATA);

      set({
        user: null,
        accessToken: null,
        isAuthenticated: false,
        regionScope: undefined,
      });
    }
  },

  // Refresh token action
  refreshToken: async () => {
    const refreshToken = storage.get<string>(STORAGE_KEYS.REFRESH_TOKEN);

    if (!refreshToken) {
      throw new Error('No refresh token available');
    }

    try {
      const response = await authRepository.refreshToken(refreshToken);

      storage.set(STORAGE_KEYS.ACCESS_TOKEN, response.accessToken);

      set({
        accessToken: response.accessToken,
      });
    } catch (error) {
      // Refresh failed, logout user
      await get().logout();
      throw error;
    }
  },

  // Load user action
  loadUser: async () => {
    const token = storage.get<string>(STORAGE_KEYS.ACCESS_TOKEN);

    if (!token) {
      set({ isAuthenticated: false, user: null });
      return;
    }

    set({ isLoading: true });

    try {
      const user = await authRepository.getCurrentUser();

      storage.set(STORAGE_KEYS.USER_DATA, user);

      set({
        user,
        isAuthenticated: true,
        isLoading: false,
        regionScope: user.regionScope,
        accessToken: token,
      });
    } catch (error) {
      // Invalid token, clear auth data
      storage.remove(STORAGE_KEYS.ACCESS_TOKEN);
      storage.remove(STORAGE_KEYS.REFRESH_TOKEN);
      storage.remove(STORAGE_KEYS.USER_DATA);

      set({
        user: null,
        accessToken: null,
        isAuthenticated: false,
        isLoading: false,
      });
    }
  },

  // Clear error action
  clearError: () => set({ error: null }),
}));
