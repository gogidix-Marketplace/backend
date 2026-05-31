'use client';

import React, { createContext, useContext, useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { hrApi, LoginRequest, LoginResponse, User } from '../api/hrApi';
import toast from 'react-hot-toast';

interface AuthContextType {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  accessibleCountries: string[];
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => Promise<void>;
  refreshToken: () => Promise<void>;
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
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [accessibleCountries, setAccessibleCountries] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    // Check for existing session on mount
    const storedToken = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');
    const storedCountries = localStorage.getItem('accessibleCountries');
    const tenantId = localStorage.getItem('tenantId');

    if (!tenantId) {
      localStorage.setItem('tenantId', 'default');
    }

    if (storedToken && storedUser) {
      try {
        setToken(storedToken);
        setUser(JSON.parse(storedUser));
        setAccessibleCountries(storedCountries ? JSON.parse(storedCountries) : []);
      } catch (error) {
        console.error('Failed to parse stored user:', error);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('accessibleCountries');
      }
    }
    setIsLoading(false);
  }, []);

  const login = async (credentials: LoginRequest) => {
    try {
      const response: LoginResponse = await hrApi.login(credentials);

      // Store in state
      setToken(response.token);
      setUser(response.user);
      setAccessibleCountries(response.accessibleCountries);

      // Store in localStorage
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.user));
      localStorage.setItem('accessibleCountries', JSON.stringify(response.accessibleCountries));

      if (credentials.rememberMe) {
        localStorage.setItem('rememberMe', 'true');
      }

      toast.success('Login successful!');

      // Redirect to dashboard
      router.push('/dashboard');
    } catch (error: any) {
      const message = error.response?.data?.message || 'Login failed. Please check your credentials.';
      toast.error(message);
      throw error;
    }
  };

  const logout = async () => {
    try {
      await hrApi.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // Clear state
      setToken(null);
      setUser(null);
      setAccessibleCountries([]);

      // Clear localStorage
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('accessibleCountries');

      toast.success('Logged out successfully');
      router.push('/login');
    }
  };

  const refreshToken = async () => {
    // Implementation for token refresh if needed
    // For now, just redirect to login
    await logout();
  };

  const value: AuthContextType = {
    user,
    token,
    isAuthenticated: !!token && !!user,
    isLoading,
    accessibleCountries,
    login,
    logout,
    refreshToken,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
