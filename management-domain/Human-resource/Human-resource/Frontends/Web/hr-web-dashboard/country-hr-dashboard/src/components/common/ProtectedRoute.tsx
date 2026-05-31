'use client';

import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuth } from '../../hooks/useAuth';
import { LoadingScreen } from './LoadingScreen';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredPermission?: string;
  requiredRole?: string;
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  children,
  requiredPermission,
  requiredRole,
}) => {
  const { isAuthenticated, isLoading, user } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push('/login');
    }

    if (isAuthenticated && user) {
      // Check role-based access
      if (requiredRole && user.role !== requiredRole && !user.roles?.includes(requiredRole)) {
        router.push('/unauthorized');
        return;
      }

      // Check permission-based access
      if (requiredPermission && !user.permissions?.includes(requiredPermission)) {
        router.push('/unauthorized');
        return;
      }
    }
  }, [isAuthenticated, isLoading, user, requiredPermission, requiredRole, router]);

  if (isLoading) {
    return <LoadingScreen />;
  }

  if (!isAuthenticated) {
    return null;
  }

  if (requiredRole && user?.role !== requiredRole && !user?.roles?.includes(requiredRole)) {
    return null;
  }

  if (requiredPermission && !user?.permissions?.includes(requiredPermission)) {
    return null;
  }

  return <>{children}</>;
};

export default ProtectedRoute;
