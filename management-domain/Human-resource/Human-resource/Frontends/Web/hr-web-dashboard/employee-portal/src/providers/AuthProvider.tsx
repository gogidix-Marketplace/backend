'use client';

import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { useRouter } from 'next/router';

interface Employee {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  employeeId: string;
  department: string;
  position: string;
  country: string;
}

interface AuthContextType {
  user: Employee | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
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
  const [user, setUser] = useState<Employee | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const storedToken = localStorage.getItem('employee_token');
    const storedUser = localStorage.getItem('employee_user');

    if (storedToken && storedUser) {
      try {
        setToken(storedToken);
        setUser(JSON.parse(storedUser));
      } catch (error) {
        console.error('Failed to parse stored user:', error);
        localStorage.removeItem('employee_token');
        localStorage.removeItem('employee_user');
      }
    }
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    // Mock login - replace with actual API call
    const mockUser: Employee = {
      id: '1',
      email,
      firstName: 'John',
      lastName: 'Doe',
      employeeId: 'EMP001',
      department: 'Engineering',
      position: 'Software Engineer',
      country: 'USA',
    };

    const mockToken = 'mock_jwt_token';

    setToken(mockToken);
    setUser(mockUser);

    localStorage.setItem('employee_token', mockToken);
    localStorage.setItem('employee_user', JSON.stringify(mockUser));

    router.push('/dashboard');
  };

  const logout = async () => {
    setToken(null);
    setUser(null);

    localStorage.removeItem('employee_token');
    localStorage.removeItem('employee_user');

    router.push('/login');
  };

  const value: AuthContextType = {
    user,
    token,
    isAuthenticated: !!token && !!user,
    isLoading,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
