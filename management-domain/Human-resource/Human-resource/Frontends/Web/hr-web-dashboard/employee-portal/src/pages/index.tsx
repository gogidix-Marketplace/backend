'use client';

import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { useRouter } from 'next/router';
import { employeeApi, EmployeeProfile, LeaveBalance } from '../api/employeeApi';
import { DashboardLayout } from '../components/layout/EmployeeDashboardLayout';

const EmployeeDashboard: React.FC = () => {
  const router = useRouter();

  const { data: profile } = useQuery({
    queryKey: ['employee-profile'],
    queryFn: () => employeeApi.getProfile(),
  });

  const { data: leaveBalance } = useQuery({
    queryKey: ['leave-balance'],
    queryFn: () => employeeApi.getLeaveBalance(),
  });

  const quickActions = [
    { title: 'Request Leave', href: '/leave', icon: '📅', color: 'bg-blue-500' },
    { title: 'View Payslips', href: '/payslips', icon: '💰', color: 'bg-green-500' },
    { title: 'Submit Timesheet', href: '/timesheet', icon: '⏱️', color: 'bg-purple-500' },
    { title: 'My Benefits', href: '/benefits', icon: '🏥', color: 'bg-orange-500' },
    { title: 'Training', href: '/training', icon: '📚', color: 'bg-pink-500' },
    { title: 'Documents', href: '/documents', icon: '📄', color: 'bg-gray-500' },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Welcome Header */}
        <div className="bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg p-6 text-white">
          <h1 className="text-2xl font-bold">
            Welcome back, {profile?.firstName}! 👋
          </h1>
          <p className="text-blue-100 mt-1">
            Here's what's happening with your HR information
          </p>
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Leave Balance</p>
            <p className="text-2xl font-bold text-gray-900">
              {leaveBalance?.remaining || 0} days
            </p>
            <p className="text-xs text-gray-500 mt-1">
              {leaveBalance?.used || 0} used this year
            </p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Next Payslip</p>
            <p className="text-2xl font-bold text-gray-900">
              {new Date().toLocaleString('default', { month: 'long' })}
            </p>
            <p className="text-xs text-gray-500 mt-1">
              Available on last day of month
            </p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Training Progress</p>
            <p className="text-2xl font-bold text-gray-900">3/5</p>
            <p className="text-xs text-gray-500 mt-1">courses completed</p>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
            {quickActions.map((action) => (
              <button
                key={action.href}
                onClick={() => router.push(action.href)}
                className="flex flex-col items-center p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition"
              >
                <span className="text-3xl mb-2">{action.icon}</span>
                <span className="text-sm font-medium text-gray-700">{action.title}</span>
              </button>
            ))}
          </div>
        </div>

        {/* Pending Items */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Pending Items</h2>
          <div className="space-y-3">
            <div className="flex items-center justify-between p-3 bg-yellow-50 rounded-lg">
              <div className="flex items-center">
                <span className="text-yellow-600 mr-3">⚠️</span>
                <div>
                  <p className="font-medium text-gray-900">Complete Timesheet</p>
                  <p className="text-sm text-gray-600">Week of March 11-15</p>
                </div>
              </div>
              <button className="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-lg text-sm font-medium">
                Complete
              </button>
            </div>
            <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
              <div className="flex items-center">
                <span className="text-blue-600 mr-3">📝</span>
                <div>
                  <p className="font-medium text-gray-900">Self-Assessment Due</p>
                  <p className="text-sm text-gray-600">Q1 2024 Performance Review</p>
                </div>
              </div>
              <button className="px-3 py-1 bg-blue-100 text-blue-800 rounded-lg text-sm font-medium">
                Start
              </button>
            </div>
          </div>
        </div>

        {/* Recent Activity */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Recent Activity</h2>
          <div className="space-y-3">
            <div className="flex items-center text-sm">
              <span className="text-green-600 mr-2">✓</span>
              <span className="text-gray-600">
                Leave request approved - <strong>March 25-26, 2024</strong>
              </span>
              <span className="ml-auto text-gray-400">2 days ago</span>
            </div>
            <div className="flex items-center text-sm">
              <span className="text-green-600 mr-2">✓</span>
              <span className="text-gray-600">
                Payslip available - <strong>February 2024</strong>
              </span>
              <span className="ml-auto text-gray-400">1 week ago</span>
            </div>
            <div className="flex items-center text-sm">
              <span className="text-green-600 mr-2">✓</span>
              <span className="text-gray-600">
                Training completed - <strong>Security Awareness 2024</strong>
              </span>
              <span className="ml-auto text-gray-400">2 weeks ago</span>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default EmployeeDashboard;
