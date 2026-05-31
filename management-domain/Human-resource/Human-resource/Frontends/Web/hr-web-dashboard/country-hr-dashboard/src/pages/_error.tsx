'use client';

import React from 'react';
import { useRouter } from 'next/router';

interface ErrorProps {
  statusCode?: number;
  title?: string;
}

export default function ErrorPage({ statusCode = 500, title }: ErrorProps) {
  const router = useRouter();

  const errorInfo: Record<number, { title: string; message: string }> = {
    404: {
      title: 'Page Not Found',
      message: 'The page you are looking for does not exist or has been moved.',
    },
    500: {
      title: 'Server Error',
      message: 'Something went wrong on our end. Please try again later.',
    },
    403: {
      title: 'Access Denied',
      message: 'You do not have permission to access this page.',
    },
  };

  const info = errorInfo[statusCode] || errorInfo[500];

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center px-4">
      <div className="max-w-md w-full text-center">
        <div className="mb-8">
          <div className="inline-flex items-center justify-center w-20 h-20 rounded-full bg-blue-100 mb-4">
            <svg className="w-10 h-10 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
          </div>
          <h1 className="text-6xl font-bold text-gray-900 mb-2">{statusCode}</h1>
          <h2 className="text-2xl font-semibold text-gray-800">{title || info.title}</h2>
        </div>

        <p className="text-gray-600 mb-8">{info.message}</p>

        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <button
            onClick={() => router.back()}
            className="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition"
          >
            Go Back
          </button>
          <button
            onClick={() => router.push('/dashboard')}
            className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
          >
            Go to Dashboard
          </button>
        </div>
      </div>
    </div>
  );
}

export function getInitialProps({ res, err }: any) {
  const statusCode = res ? res.statusCode : err ? err.statusCode : 404;
  return { statusCode };
}
