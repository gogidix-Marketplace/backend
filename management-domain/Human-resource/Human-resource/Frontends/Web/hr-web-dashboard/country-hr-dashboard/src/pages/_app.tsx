'use client';

import React, { useState } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { FormProvider } from 'react-hook-form';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from '../providers/AuthProvider';
import { DashboardProvider } from '../providers/DashboardProvider';
import { ThemeProvider } from '../providers/ThemeProvider';
import { LoadingScreen } from '../components/common/LoadingScreen';

import '../styles/globals.css';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  const [queryClient] = useState(() => new QueryClient({
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false,
        retry: 1,
        staleTime: 5 * 60 * 1000, // 5 minutes
      },
    },
  }));

  return (
    <QueryClientProvider client={queryClient}>
      <FormProvider>
        <ThemeProvider>
          <AuthProvider>
            <DashboardProvider>
              <ReactQueryDevtools initialIsOpen={false} />
              {children}
              <Toaster
                position="top-right"
                toastOptions={{
                  duration: 5000,
                  style: { background: '#333', color: '#fff' }
                }}
              />
            </DashboardProvider>
          </AuthProvider>
        </ThemeProvider>
      </FormProvider>
    </QueryClientProvider>
  );
}
