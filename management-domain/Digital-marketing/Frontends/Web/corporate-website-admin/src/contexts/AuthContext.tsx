import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { User, AuthTokens, LoginCredentials } from '@/types';
import * as authApi from '@/services/authApi';
import { useAuthStore } from '@/stores/authStore';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => Promise<void>;
  refreshToken: () => Promise<void>;
  hasPermission: (permission: string) => boolean;
  hasRole: (role: string) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  const { user, tokens, setUser, setTokens, clearAuth } = useAuthStore();

  const isAuthenticated = !!user && !!tokens;

  const hasPermission = useCallback((permission: string): boolean => {
    return user?.permissions.includes(permission as any) || user?.role === 'admin';
  }, [user]);

  const hasRole = useCallback((role: string): boolean => {
    return user?.role === role;
  }, [user]);

  const login = useCallback(async (credentials: LoginCredentials) => {
    try {
      const response = await authApi.login(credentials);
      setUser(response.user);
      setTokens(response.tokens);
      navigate('/dashboard');
    } catch (error) {
      throw error;
    }
  }, [navigate, setUser, setTokens]);

  const logout = useCallback(async () => {
    try {
      await authApi.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      clearAuth();
      navigate('/login');
    }
  }, [navigate, clearAuth]);

  const refreshToken = useCallback(async () => {
    if (!tokens?.refreshToken) {
      throw new Error('No refresh token available');
    }

    try {
      const response = await authApi.refreshToken(tokens.refreshToken);
      setUser(response.user);
      setTokens(response.tokens);
    } catch (error) {
      clearAuth();
      navigate('/login');
      throw error;
    }
  }, [tokens, setUser, setTokens, clearAuth, navigate]);

  useEffect(() => {
    const initAuth = async () => {
      const storedTokens = useAuthStore.getState().tokens;
      if (storedTokens) {
        try {
          const response = await authApi.getCurrentUser();
          setUser(response.user);
        } catch (error) {
          console.error('Auth initialization error:', error);
          clearAuth();
        }
      }
      setIsLoading(false);
    };

    initAuth();
  }, [setUser, clearAuth]);

  useEffect(() => {
    if (!tokens) return;

    const tokenExpiryTime = tokens.expiresAt - Date.now();
    const refreshTime = Math.max(0, tokenExpiryTime - 5 * 60 * 1000); // Refresh 5 minutes before expiry

    let refreshTimer: NodeJS.Timeout;

    if (tokenExpiryTime <= 0) {
      refreshToken();
    } else {
      refreshTimer = setTimeout(() => {
        refreshToken();
      }, refreshTime);
    }

    return () => {
      if (refreshTimer) clearTimeout(refreshTimer);
    };
  }, [tokens, refreshToken]);

  const value: AuthContextType = {
    user,
    isAuthenticated,
    isLoading,
    login,
    logout,
    refreshToken,
    hasPermission,
    hasRole,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
