'use client';

import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { useRouter } from 'next/router';
import { globalHrApi, LoginRequest, LoginResponse, GlobalUser } from '../api/globalHrApi';

interface AuthContextType {
  user: GlobalUser | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  accessibleRegions: string[];
  accessibleCountries: string[];
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => Promise<void>;
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
  const [user, setUser] = useState<GlobalUser | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [accessibleRegions, setAccessibleRegions] = useState<string[]>([]);
  const [accessibleCountries, setAccessibleCountries] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');
    const storedRegions = localStorage.getItem('accessibleRegions');
    const storedCountries = localStorage.getItem('accessibleCountries');

    if (storedToken && storedUser) {
      try {
        setToken(storedToken);
        setUser(JSON.parse(storedUser));
        setAccessibleRegions(storedRegions ? JSON.parse(storedRegions) : []);
        setAccessibleCountries(storedCountries ? JSON.parse(storedCountries) : []);
      } catch (error) {
        console.error('Failed to parse stored user:', error);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    }
    setIsLoading(false);
  }, []);

  const login = async (credentials: LoginRequest) => {
    const response: LoginResponse = await globalHrApi.login(credentials);

    setToken(response.token);
    setUser(response.user);
    setAccessibleRegions(response.user.accessibleRegions || []);
    setAccessibleCountries(response.user.accessibleCountries || []);

    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response.user));
    localStorage.setItem('accessibleRegions', JSON.stringify(response.user.accessibleRegions || []));
    localStorage.setItem('accessibleCountries', JSON.stringify(response.user.accessibleCountries || []));

    router.push('/dashboard');
  };

  const logout = async () => {
    try {
      await globalHrApi.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setToken(null);
      setUser(null);
      setAccessibleRegions([]);
      setAccessibleCountries([]);

      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('accessibleRegions');
      localStorage.removeItem('accessibleCountries');

      router.push('/login');
    }
  };

  const value: AuthContextType = {
    user,
    token,
    isAuthenticated: !!token && !!user,
    isLoading,
    accessibleRegions,
    accessibleCountries,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
