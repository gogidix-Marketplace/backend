'use client';

import { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { useRouter } from 'next/router';
import { employeeApi, EmployeeProfile } from '../api/employeeApi';
import { useToast } from 'react-hot-toast';

interface AuthContextType {
  user: EmployeeProfile | null;
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
  const [user, setUser] = useState<EmployeeProfile | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');
    const tenantId = localStorage.getItem('tenantId');

    if (!tenantId) {
      localStorage.setItem('tenantId', 'default');
    }

    if (storedToken && storedUser) {
      try {
        setToken(storedToken);
        setUser(JSON.parse(storedUser));
      } catch (error) {
        console.error('Failed to parse stored user:', error);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    }
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    try {
      // In real implementation, this would call the login API
      // For now, we'll simulate a successful login
      const mockUser: EmployeeProfile = {
        id: '1',
        employeeId: 'EMP001',
        firstName: 'John',
        lastName: 'Doe',
        email: email,
        phone: '+1234567890',
        department: 'Engineering',
        position: 'Software Engineer',
        manager: 'Jane Smith',
        location: 'United States',
        startDate: '2022-01-15',
        status: 'active',
      };

      const mockToken = 'mock-jwt-token';

      setToken(mockToken);
      setUser(mockUser);

      localStorage.setItem('token', mockToken);
      localStorage.setItem('user', JSON.stringify(mockUser));

      router.push('/employee');
    } catch (error: any) {
      const message = error.response?.data?.message || 'Login failed. Please check your credentials.';
      throw error;
    }
  };

  const logout = async () => {
    setToken(null);
    setUser(null);

    localStorage.removeItem('token');
    localStorage.removeItem('user');

    router.push('/employee/login');
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
